package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Field Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Table Fields from queries and maintain
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("field")
public class Field extends OutputField {

  private static final long serialVersionUID = 7587109759292448862L;

  // Field id (database id)
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Field table
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table;

  // Variable value to set into the id
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable;

  // Function to apply to the field
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function;

  // Value to set into the id (for system variables like SYSDATE)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Query to be used as field
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Defined if field is for audit only
  @XStreamAlias("audit")
  @XStreamAsAttribute
  private Boolean audit;

  // Defined if field is a key field
  @XStreamAlias("key")
  @XStreamAsAttribute
  private Boolean key;

  // Sequence
  @XStreamAlias("sequence")
  @XStreamAsAttribute
  private String sequence;

  // Field concat list
  @XStreamImplicit
  private List<Concat> concatList;

  // Field caseWhen list
  @XStreamImplicit
  private List<CaseWhen> caseWhenList;

  // Field caseElse
  @XStreamAlias("case-else")
  private CaseElse caseElse;

  /**
   * Returns if is key
   * @return Is key
   */
  public boolean isKey() {
    return key != null && key;
  }

  /**
   * Returns if is audit
   * @return Is audit
   */
  public boolean isAudit() {
    return audit != null && audit;
  }

  @Override
  public Field copy() throws AWException {
    return this.toBuilder()
      .concatList(ListUtil.copyList(getConcatList()))
      .caseWhenList(ListUtil.copyList(getCaseWhenList()))
      .caseElse(ListUtil.copyElement(getCaseElse()))
      .build();
  }

  @Override
  public String toString() {

    String fieldDefinition;

    // Field as Subquery
    if (this.getQuery() != null) {
      fieldDefinition = "query(" + this.getQuery() + ")";
    } else if (this.getVariable() != null) {
      // Field as variable
      fieldDefinition = "variable(" + this.getVariable() + ")";
    } else if (this.getValue() != null) {
      // Field as value
      fieldDefinition = this.getValue();
    } else {
      // Standard field
      fieldDefinition = this.getTable() != null ? this.getTable() + "." + this.getId() : this.getId();
    }

    // Generate string representation
    String fieldFunction = this.getFunction() != null ? this.getFunction() + "(" + fieldDefinition + ")" : fieldDefinition;
    String alias = this.getAlias() != null ? " as " + this.getAlias() : "";
    return fieldFunction + alias;
  }
}
