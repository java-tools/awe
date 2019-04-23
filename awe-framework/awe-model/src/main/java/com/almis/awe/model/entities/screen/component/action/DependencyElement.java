/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.action;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * DependencyElement Class
 *
 * Used to parse a criteria dependency element with XStream
 *
 *
 * @author Pablo GARCIA - 05/AGO/2010
 */
@XStreamAlias("dependency-element")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "dependencies", "contextMenu", "iconLoading"})
public class DependencyElement extends Element {

  private static final long serialVersionUID = 8299981147831992476L;

  // Dependency condition
  @XStreamAlias("condition")
  @XStreamAsAttribute
  private String condition = null;

  // Dependency query
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Optional element
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private String optional = null;

  // Dependency element alias
  @XStreamAlias("alias")
  @XStreamAsAttribute
  private String alias = null;

  // Dependency comparator (new)
  @XStreamAlias("id2")
  @XStreamAsAttribute
  private String id2 = null;

  // Dependency element attribute
  @XStreamAlias("attribute")
  @XStreamAsAttribute
  private String attribute = null;

  // Dependency comparator attribute
  @XStreamAlias("attribute2")
  @XStreamAsAttribute
  private String attribute2 = null;

  // Dependency element column
  @XStreamAlias("column")
  @XStreamAsAttribute
  private String column = null;

  // Dependency element row
  @XStreamAlias("row")
  @XStreamAsAttribute
  private String row = null;

  // Dependency comparator column
  @XStreamAlias("column2")
  @XStreamAsAttribute
  private String column2 = null;

  // Dependency query
  @XStreamAlias("cancel")
  @XStreamAsAttribute
  private String cancel = null;

  // Dependency event
  @XStreamAlias("event")
  @XStreamAsAttribute
  private String event = null;

  // Dependency element attribute
  @XStreamAlias("view")
  @XStreamAsAttribute
  private String view = null;

  // Dependency comparator attribute
  @XStreamAlias("view2")
  @XStreamAsAttribute
  private String view2 = null;

  // Check dependency element
  @XStreamAlias("check-changes")
  @XStreamAsAttribute
  private String checkChanges = null;

  // Name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  /**
   * Default constructor
   */
  public DependencyElement() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public DependencyElement(DependencyElement other) throws AWException {
    super(other);
    this.condition = other.condition;
    this.value = other.value;
    this.optional = other.optional;
    this.alias = other.alias;
    this.id2 = other.id2;
    this.attribute = other.attribute;
    this.attribute2 = other.attribute2;
    this.column = other.column;
    this.row = other.row;
    this.column2 = other.column2;
    this.cancel = other.cancel;
    this.event = other.event;
    this.view = other.view;
    this.view2 = other.view2;
    this.checkChanges = other.checkChanges;
    this.name = other.name;
  }

  @Override
  public DependencyElement copy() throws AWException {
    return new DependencyElement(this);
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Returns the element condition operator
   *
   * @return Element condition operator
   */
  @JsonGetter("condition")
  public String getCondition() {
    return condition;
  }

  /**
   * Stores the element condition
   *
   * @param condition Element condition operator
   */
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * Returns the elements value to compare
   *
   * @return Value to compare
   */
  @JsonGetter("value")
  public String getValue() {
    return value;
  }

  /**
   * Stores elements value to compare
   *
   * @param value Value to compare
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns if element is optional
   *
   * @return Element is optional
   */
  @JsonGetter("optional")
  public String getOptional() {
    return optional;
  }

  /**
   * Stores if element is optional
   *
   * @param optional Element is optional
   */
  public void setOptional(String optional) {
    this.optional = optional;
  }

  /**
   * Returns if element is in the same row
   *
   * @return Element is in the same row
   */
  @JsonGetter("row")
  public String getRow() {
    return row;
  }

  /**
   * Stores if element is in the same row
   *
   * @param row Element is in the same row
   */
  public void setRow(String row) {
    this.row = row;
  }

  /**
   * Stores if element is canceller
   *
   * @return Element is canceller
   */
  @JsonGetter("cancel")
  public String getCancel() {
    return cancel;
  }

  /**
   * Stores if element is canceller
   *
   * @param cancel Element is canceller
   */
  public void setCancel(String cancel) {
    this.cancel = cancel;
  }

  /**
   * Returns the element caller event
   *
   * @return Element caller event
   */
  @JsonGetter("event")
  public String getEvent() {
    return event;
  }

  /**
   * Stores the element caller event
   *
   * @param event Element caller event
   */
  public void setEvent(String event) {
    this.event = event;
  }

  /**
   * Retrieve element to compare
   *
   * @return the id2
   */
  public String getId2() {
    return id2;
  }

  /**
   * Retrieve element to compare for JSON serialization
   *
   * @return the id2
   */
  @JsonGetter("id2")
  public String getId2Converter() {
    String id2 = null;
    if (this.getName() != null) {
      id2 = this.getName();
    }

    if (this.getId2() != null) {
      id2 = this.getId2();
    }

    return id2;
  }

  /**
   * Store element to compare
   *
   * @param id2 the id2 to set
   */
  public void setId2(String id2) {
    this.id2 = id2;
  }

  /**
   * Retrieve element attribute
   *
   * @return the attribute
   */
  @JsonGetter("attribute1")
  public String getAttribute() {
    return attribute;
  }

  /**
   * Store element attribute
   *
   * @param attribute the attribute to set
   */
  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }

  /**
   * Retrieve element attribute to compare
   *
   * @return the attribute2
   */
  @JsonGetter("attribute2")
  public String getAttribute2() {
    return attribute2;
  }

  /**
   * Store element attribute to compare
   *
   * @param attribute2 the attribute2 to set
   */
  public void setAttribute2(String attribute2) {
    this.attribute2 = attribute2;
  }

  /**
   * Retrieve element view
   *
   * @return the view
   */
  @JsonGetter("view1")
  public String getView() {
    return view;
  }

  /**
   * Store element view
   *
   * @param view the view to set
   */
  public void setView(String view) {
    this.view = view;
  }

  /**
   * Retrieve element view to compare
   *
   * @return the view2
   */
  @JsonGetter("view2")
  public String getView2() {
    return view2;
  }

  /**
   * Store element view to compare
   *
   * @param view the view to set
   */
  public void setView2(String view) {
    this.view2 = view;
  }

  /**
   * Retrieve element column
   *
   * @return the column
   */
  @JsonGetter("column1")
  public String getColumn() {
    return column;
  }

  /**
   * Store element column
   *
   * @param column the column to set
   */
  public void setColumn(String column) {
    this.column = column;
  }

  /**
   * Retrieve element column to compare
   *
   * @return the column2
   */
  @JsonGetter("column2")
  public String getColumn2() {
    return column2;
  }

  /**
   * Store element column to compare
   *
   * @param column2 the column2 to set
   */
  public void setColumn2(String column2) {
    this.column2 = column2;
  }

  /**
   * Retrieve element alias
   *
   * @return the alias
   */
  @JsonGetter("alias")
  public String getAlias() {
    return alias;
  }

  /**
   * Store element alias
   *
   * @param alias the alias to set
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * @return the checkChanges
   */
  public String getCheckChanges() {
    return checkChanges;
  }

  /**
   * @return the checkChanges
   */
  @JsonGetter("checkChanges")
  public boolean isCheckChanges() {
    return checkChanges == null || checkChanges.equalsIgnoreCase("true");
  }

  /**
   * @param checkChanges the checkChanges to set
   */
  public void setCheckChanges(String checkChanges) {
    this.checkChanges = checkChanges;
  }

  /**
   * Get action name
   *
   * @return Action name
   */
  public String getName() {
    return name;
  }

  /**
   * Set action name
   *
   * @param name Action name
   * @return Action
   */
  public DependencyElement setName(String name) {
    this.name = name;
    return this;
  }
}
