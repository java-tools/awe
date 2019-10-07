package com.almis.awe.model.entities.queries;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * SqlField Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Superclass of Field and Computed class
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Constant.class, Field.class, Case.class, Operation.class, Over.class})
public abstract class SqlField extends OutputField {

  // Field id (database id)
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Field table
  @XStreamAlias("table")
  @XStreamAsAttribute
  private String table;

  // Function to apply to the field
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function;

  // Cast to the field
  @XStreamAlias("cast")
  @XStreamAsAttribute
  private String cast;

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

  // Variable value to set into the id
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable;

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
    return audit == null || audit;
  }

  /**
   * Returns if is not audit
   * @return Is not audit
   */
  public boolean isNotAudit() {
    return audit == null || !audit;
  }

  @Override
  public String getIdentifier() {
    String identifier = super.getIdentifier();
    if (identifier == null && id != null) {
      identifier = id;
    }
    return identifier;
  }

  /**
   * Apply field modifiers to field string
   * @param field Field string
   * @return Field with function
   */
  public String applyFieldModifiers(String field) {
    String castField = getCast() != null ? "CAST (" + field + " AS " + getCast() + ")" : field;
    return getFunction() != null ? getFunction() + "(" + castField + ")" : castField;
  }
}
