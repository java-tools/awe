package com.almis.awe.test.unit.pojo;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.queries.*;
import com.almis.awe.test.unit.TestUtil;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * DataList, DataListUtil and DataListBuilder tests
 * @author pgarcia
 */
public class QueryTest extends TestUtil {

  String expectedQuery = "SQL QUERY:\n" +
    "SELECT variable(fieldVariable) as alias1, fieldTable.fieldId, CONCAT(\"tutu\", \"lala\", ADD(field, 1), \"lolo\") as alias3, " +
    "CASE WHEN (\"lala\" eq variable(lala)) THEN 1 WHEN (function(fieldTable.fieldId) eq \"lolo\") THEN 2 ELSE 0 as alias4 " +
    "LEFT JOIN tableSchema.tableId joinTable ON ((fieldTable.fieldId like variable(lala)) <- [ignorecase, trim]) UNION all unionQuery " +
    "WHERE ((CONCAT(\"tutu\", \"lala\", ADD(field, 1), \"lolo\") gt CASE WHEN (\"lala\" eq variable(lala)) THEN 1 " +
    "WHEN (function(fieldTable.fieldId) eq \"lolo\") THEN 2 ELSE 0) and (fieldTable.fieldId like variable(lala)) <- [ignorecase, trim] " +
    "and (min(leftTable.leftField) is not null ) <- [optional]) ORDER BY tableSort.fieldSort ASC GROUP BY tableGroup.fieldGroup";

  Operation concatOperation = new Operation()
    .setOperator("CONCAT")
    .setOperandList(Arrays.asList(
      new Constant().setValue("tutu").setType("STRING"),
      new Constant().setValue("lala"),
      new Operation().setOperator("ADD").setOperandList(Arrays.asList(
        new Field().setId("field"),
        new Constant().setType("INTEGER").setValue("1")
      )),
      new Constant().setValue("lolo").setType("STRING")
    ));

  Case caseExample = new Case().setCaseWhenList(Arrays.asList(
    (CaseWhen) new CaseWhen()
      .setThenOperand(new Constant().setValue("1").setType("INTEGER"))
      .setCondition("eq")
      .setLeftOperand(new Constant().setType("STRING").setValue("lala"))
      .setRightOperand(new Field().setVariable("lala")),
    (CaseWhen) new CaseWhen()
      .setThenOperand(new Constant().setValue("2").setType("INTEGER"))
      .setCondition("eq")
      .setLeftOperand(new Field().setId("fieldId").setTable("fieldTable").setFunction("function"))
      .setRightOperand(new Constant().setValue("lolo"))))
    .setCaseElse(new Constant().setType("INTEGER").setValue("0"));

  Table table = new Table()
    .setSchema("tableSchema")
    .setAlias("tableAlias")
    .setId("tableId");

  Filter filter = new Filter()
    .setCondition("like")
    .setIgnoreCase(true)
    .setTrim(true)
    .setLeftOperand(new Field().setTable("fieldTable").setId("fieldId"))
    .setRightOperand(new Field().setVariable("lala"));

  Query query;

  {
    try {
      query = new Query()
        .setId("query")
        .setTableList(Arrays.asList(table.copy()))
        .setSqlFieldList(Arrays.asList(
          (SqlField) new Field().setVariable("fieldVariable").setAlias("alias1"),
          new Field().setTable("fieldTable").setId("fieldId"),
          (SqlField) concatOperation.copy().setAlias("alias3"),
          (SqlField) caseExample.copy().setAlias("alias4")
        ))
        .setFilterGroup((FilterAnd) new FilterAnd()
          .setFilterList(Arrays.asList(
            new Filter()
              .setCondition("gt")
              .setLeftOperand(concatOperation.copy())
              .setRightOperand(caseExample.copy()),
            filter.copy(),
            new Filter()
              .setCondition("is not null")
              .setLeftField("leftField")
              .setLeftTable("leftTable")
              .setLeftFunction("MIN")
              .setOptional(true)
            )
          ))
        .setJoinList(Arrays.asList(new Join()
          .setType("left")
          .setTable(table.copy().setAlias("joinTable"))
          .setFilterGroupList(Arrays.asList(new FilterOr()
            .setFilterList(Arrays.asList(filter.copy())
            )))
        ))
        .setUnionList(Arrays.asList(new Union().setQuery("unionQuery").setType("all")))
        .setGroupByList(Arrays.asList(new GroupBy()
          .setField("fieldGroup")
          .setTable("tableGroup")))
        .setOrderByList(Arrays.asList(new OrderBy()
          .setField("fieldSort")
          .setTable("tableSort")
          .setType("ASC")));
    } catch (AWException exc) {}
  }

  /**
   * Query print toString
   * @throws Exception Test error
   */
  @Test
  public void testQuery() throws Exception {
    // Assert
    assertEquals(expectedQuery, query.toString());
  }

  /**
   * Query print toString with variables
   * @throws Exception Test error
   */
  @Test
  public void testQueryWithVariables() throws Exception {
    // Prepare
    String expected = expectedQuery + "\n" +
      "VARIABLES:\n" +
      "variable1: parameter(parameter) [STRING], variable2: property(property) [INTEGER], variable3: session(session) [BOOLEAN], " +
      "variable4: value [FLOAT], variable5: parameter(parameter) [DATE]";

    // Run
    Query queryCopy = query.copy().setVariableDefinitionList(Arrays.asList(
      new Variable().setId("variable1").setName("parameter").setType("STRING").setIgnoreCase(true),
      new Variable().setId("variable2").setProperty("property").setType("INTEGER"),
      new Variable().setId("variable3").setSession("session").setType("BOOLEAN"),
      new Variable().setId("variable4").setValue("value").setType("FLOAT"),
      new Variable().setId("variable5").setName("parameter").setType("DATE")
    ));


    // Assert
    assertEquals(expected, queryCopy.toString());
  }

  /**
   * Query print toString with variables
   * @throws Exception Test error
   */
  @Test
  public void testConvertQueryToXML() throws Exception {
    // Prepare
    String expected =
      "<query id=\"query\">\n" +
      "  <table id=\"tableId\" schema=\"tableSchema\" alias=\"tableAlias\"/>\n" +
      "  <field alias=\"alias1\" variable=\"fieldVariable\"/>\n" +
      "  <field id=\"fieldId\" table=\"fieldTable\"/>\n" +
      "  <operation alias=\"alias3\" operator=\"CONCAT\">\n" +
      "    <constant type=\"STRING\" value=\"tutu\"/>\n" +
      "    <constant value=\"lala\"/>\n" +
      "    <operation operator=\"ADD\">\n" +
      "      <field id=\"field\"/>\n" +
      "      <constant type=\"INTEGER\" value=\"1\"/>\n" +
      "    </operation>\n" +
      "    <constant type=\"STRING\" value=\"lolo\"/>\n" +
      "  </operation>\n" +
      "  <case alias=\"alias4\">\n" +
      "    <when condition=\"eq\">\n" +
      "      <left-operand value=\"lala\" type=\"STRING\"/>\n" +
      "      <right-operand variable=\"lala\"/>\n" +
      "      <then value=\"1\" type=\"INTEGER\"/>\n" +
      "    </when>\n" +
      "    <when condition=\"eq\">\n" +
      "      <left-operand id=\"fieldId\" table=\"fieldTable\" function=\"function\"/>\n" +
      "      <right-operand value=\"lolo\"/>\n" +
      "      <then value=\"2\" type=\"INTEGER\"/>\n" +
      "    </when>\n" +
      "    <else value=\"0\" type=\"INTEGER\"/>\n" +
      "  </case>\n" +
      "  <join type=\"left\">\n" +
      "    <table id=\"tableId\" schema=\"tableSchema\" alias=\"joinTable\"/>\n" +
      "    <or>\n" +
      "      <filter condition=\"like\" ignorecase=\"true\" trim=\"true\">\n" +
      "        <left-operand id=\"fieldId\" table=\"fieldTable\"/>\n" +
      "        <right-operand variable=\"lala\"/>\n" +
      "      </filter>\n" +
      "    </or>\n" +
      "  </join>\n" +
      "  <union query=\"unionQuery\" type=\"all\"/>\n" +
      "  <order-by field=\"fieldSort\" table=\"tableSort\" type=\"ASC\"/>\n" +
      "  <group-by field=\"fieldGroup\" table=\"tableGroup\"/>\n" +
      "  <where>\n" +
      "    <filter condition=\"gt\">\n" +
      "      <left-operand operator=\"CONCAT\">\n" +
      "        <constant type=\"STRING\" value=\"tutu\"/>\n" +
      "        <constant value=\"lala\"/>\n" +
      "        <operation operator=\"ADD\">\n" +
      "          <field id=\"field\"/>\n" +
      "          <constant type=\"INTEGER\" value=\"1\"/>\n" +
      "        </operation>\n" +
      "        <constant type=\"STRING\" value=\"lolo\"/>\n" +
      "      </left-operand>\n" +
      "      <right-operand>\n" +
      "        <when condition=\"eq\">\n" +
      "          <left-operand value=\"lala\" type=\"STRING\"/>\n" +
      "          <right-operand variable=\"lala\"/>\n" +
      "          <then value=\"1\" type=\"INTEGER\"/>\n" +
      "        </when>\n" +
      "        <when condition=\"eq\">\n" +
      "          <left-operand id=\"fieldId\" table=\"fieldTable\" function=\"function\"/>\n" +
      "          <right-operand value=\"lolo\"/>\n" +
      "          <then value=\"2\" type=\"INTEGER\"/>\n" +
      "        </when>\n" +
      "        <else type=\"INTEGER\" value=\"0\"/>\n" +
      "      </right-operand>\n" +
      "    </filter>\n" +
      "    <filter condition=\"like\" ignorecase=\"true\" trim=\"true\">\n" +
      "      <left-operand id=\"fieldId\" table=\"fieldTable\"/>\n" +
      "      <right-operand variable=\"lala\"/>\n" +
      "    </filter>\n" +
      "    <filter left-field=\"leftField\" left-table=\"leftTable\" left-function=\"MIN\" condition=\"is not null\" optional=\"true\"/>\n" +
      "  </where>\n" +
      "</query>";

    // Run
    XStream streamMarshaller = new XStream();
    streamMarshaller.autodetectAnnotations(true);
    streamMarshaller.processAnnotations(Query.class);
    streamMarshaller.aliasSystemAttribute(null, "class");

    String xmlOutput = streamMarshaller.toXML(query.copy());


    // Assert
    assertEquals(expected, xmlOutput);
  }
}