package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * DependencyElement Class
 *
 * Used to parse a criteria dependency element with XStream
 *
 *
 * @author Pablo GARCIA - 05/AGO/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("dependency-element")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "dependencies", "contextMenu", "iconLoading"})
public class DependencyElement extends Element {

  private static final long serialVersionUID = 8299981147831992476L;

  // Dependency condition
  @XStreamAlias("condition")
  @XStreamAsAttribute
  private String condition;

  // Dependency query
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Optional element
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private Boolean optional;

  // Dependency element alias
  @XStreamAlias("alias")
  @XStreamAsAttribute
  private String alias;

  // Dependency comparator (new)
  @JsonIgnore
  @XStreamAlias("id2")
  @XStreamAsAttribute
  private String id2;

  // Dependency element attribute
  @JsonProperty("attribute1")
  @XStreamAlias("attribute")
  @XStreamAsAttribute
  private String attribute;

  // Dependency comparator attribute
  @XStreamAlias("attribute2")
  @XStreamAsAttribute
  private String attribute2;

  // Dependency element column
  @JsonProperty("column1")
  @XStreamAlias("column")
  @XStreamAsAttribute
  private String column;

  // Dependency element row
  @JsonProperty("row1")
  @XStreamAlias("row")
  @XStreamAsAttribute
  private String row;

  // Dependency comparator column
  @XStreamAlias("column2")
  @XStreamAsAttribute
  private String column2;

  // Dependency query
  @XStreamAlias("cancel")
  @XStreamAsAttribute
  private Boolean cancel;

  // Dependency event
  @XStreamAlias("event")
  @XStreamAsAttribute
  private String event;

  // Dependency element attribute
  @JsonProperty("view1")
  @XStreamAlias("view")
  @XStreamAsAttribute
  private String view;

  // Dependency comparator attribute
  @XStreamAlias("view2")
  @XStreamAsAttribute
  private String view2;

  // Check dependency element
  @XStreamAlias("check-changes")
  @XStreamAsAttribute
  private Boolean checkChanges;

  // Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  @Override
  public DependencyElement copy() throws AWException {
    return this.toBuilder().build();
  }

  /**
   * Retrieve element to compare for JSON serialization
   *
   * @return the id2
   */
  @JsonGetter("id2")
  public String getId2Converter() {
    return getName() != null ? getName() : getId2();
  }

  /**
   * Returns if is optional
   * @return Is optional
   */
  public boolean isOptional() {
    return getOptional() != null && getOptional();
  }

  /**
   * Returns if is cancel
   * @return Is cancel
   */
  public boolean isCancel() {
    return getCancel() != null && getCancel();
  }

  /**
   * Returns if check changes
   * @return Is check changes
   */
  @JsonGetter("checkChanges")
  public boolean isCheckChanges() {
    return getCheckChanges() == null || getCheckChanges();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return AweConstants.NO_KEY;
  }
}
