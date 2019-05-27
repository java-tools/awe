package com.almis.awe.model.entities.queries;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.util.ArrayList;
import java.util.List;

public class OperandConverter implements Converter {

  private static final String OPERATOR = "operator";
  private static final String VALUE = "value";
  private static final String TYPE = "type";
  private static final String VARIABLE = "variable";
  private static final String FUNCTION = "function";
  private static final String QUERY = "query";
  private static final String ID = "id";
  private static final String TABLE = "table";

  @Override
  public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
    SqlField sqlField = (SqlField) source;
    if (source instanceof Operation) {
      Operation field = (Operation) source;
      writer.addAttribute(OPERATOR, field.getOperator());
      writeOperation(field, writer, context);
    } else if (source instanceof Static) {
      Static field = (Static) source;
      writer.addAttribute(VALUE, field.getValue());
      if (field.getType() != null) writer.addAttribute(TYPE, field.getType());
    } else if (source instanceof Field) {
      Field field = (Field) source;
      if (field.getVariable() != null) writer.addAttribute(VARIABLE, field.getVariable());
      if (field.getQuery() != null) writer.addAttribute(QUERY, field.getQuery());
    } else if (source instanceof Case) {
      writeCase((Case) source, writer, context);
    }

    // Common attributes
    if (sqlField.getId() != null) writer.addAttribute(ID, sqlField.getId());
    if (sqlField.getTable() != null) writer.addAttribute(TABLE, sqlField.getTable());
    if (sqlField.getFunction() != null) writer.addAttribute(FUNCTION, sqlField.getFunction());
  }

  /**
   * Marshal operation
   * @param field
   * @param context
   */
  private void writeOperation(Operation field, HierarchicalStreamWriter writer, MarshallingContext context) {
    if (field.getOperandList() != null) {
      for (SqlField operand : field.getOperandList()) {
        writer.startNode(operand.getClass().getSimpleName().toLowerCase());
        context.convertAnother(operand);
        writer.endNode();
      }
    }
  }

  /**
   * Marshal case
   * @param field
   * @param context
   */
  private void writeCase(Case field, HierarchicalStreamWriter writer, MarshallingContext context) {
    if (field.getCaseWhenList() != null) {
      for (CaseWhen caseWhen : field.getCaseWhenList()) {
        writer.startNode("when");
        context.convertAnother(caseWhen);
        writer.endNode();
      }
    }
    writer.startNode("else");
    context.convertAnother(field.getCaseElse());
    writer.endNode();
  }

  @Override
  public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
    SqlField sqlField = null;
    if (reader.getAttribute(OPERATOR) != null) {
      sqlField = new Operation()
        .setOperator(reader.getAttribute(OPERATOR))
        .setOperandList(getOperands(reader, context, new ArrayList<>()));
    } else if (reader.getAttribute(VALUE) != null) {
      // Convert to static
      sqlField = new Static()
        .setValue(reader.getAttribute(VALUE))
        .setType(reader.getAttribute(TYPE));
    } else if (reader.hasMoreChildren()) {
      // Convert to case
      sqlField = new Case();
      getCaseList((Case) sqlField, reader, context, new ArrayList<>());
    } else {
      // Convert to field
      sqlField = new Field()
        .setVariable(reader.getAttribute(VARIABLE))
        .setQuery(reader.getAttribute(QUERY));
    }

    // Add sqlfield attributes
    sqlField
      .setFunction(reader.getAttribute(FUNCTION))
      .setTable(reader.getAttribute(TABLE))
      .setId(reader.getAttribute(ID));

    return sqlField;
  }

  private List<SqlField> getOperands(HierarchicalStreamReader reader, UnmarshallingContext context, List<SqlField> operands){

    // Exit condition: No more children
    if (!reader.hasMoreChildren()){
      return operands;
    }

    reader.moveDown();
    operands.add((SqlField) unmarshal(reader, context));
    reader.moveUp();
    getOperands(reader, context, operands);
    return operands;
  }

  private void getCaseList(Case caseClause, HierarchicalStreamReader reader, UnmarshallingContext context, List<CaseWhen> caseWhenList){
    if (!reader.hasMoreChildren()){
      return;
    }
    reader.moveDown();
    if (reader.getNodeName().equals("when")){
      caseWhenList.add((CaseWhen) context.convertAnother(caseClause, CaseWhen.class));
      caseClause.setCaseWhenList(caseWhenList);
    } else if (reader.getNodeName().equals("else")) {
      caseClause.setCaseElse((SqlField) unmarshal(reader, context));
    }
    reader.moveUp();
    getCaseList(caseClause, reader, context, caseWhenList);
  }

  public boolean canConvert(Class type) {
    return SqlField.class.isAssignableFrom(type);
  }
}
