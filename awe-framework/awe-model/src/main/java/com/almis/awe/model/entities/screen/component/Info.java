/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Info Class
 *
 * Used to add an info element with XStream
 *
 *
 * @author Pablo GARCIA - 04/JUN/2012
 */
@XStreamAlias("info")
public class Info extends Component {

  private static final long serialVersionUID = -2190505817555353007L;

  // Value from query
  @XStreamOmitField
  private String value = null;

  // Info unit
  @XStreamAlias("unit")
  @XStreamAsAttribute
  private String unit = null;

  // Info dropdown style
  @XStreamAlias("dropdown-style")
  @XStreamAsAttribute
  private String dropdownStyle = null;

  /**
   * Default constructor
   */
  public Info() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Info(Info other) throws AWException {
    super(other);
    this.value = other.value;
    this.unit = other.unit;
    this.dropdownStyle = other.dropdownStyle;
  }

  @Override
  public Info copy() throws AWException {
    return new Info(this);
  }

  /**
   * Returns the static value
   *
   * @return Static value
   */
  @JsonGetter("text")
  public String getValue() {
    return value;
  }

  /**
   * Stores the static value
   *
   * @param value Static value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the unit info
   *
   * @return unit info
   */
  @JsonGetter("unit")
  public String getUnit() {
    return unit;
  }

  /**
   * Stores the info unit
   *
   * @param unit info unit
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * @return the dropdownStyle
   */
  @JsonGetter("dropdownStyle")
  public String getDropdownStyle() {
    return dropdownStyle;
  }

  /**
   * @param dropdownStyle the dropdownStyle to set
   */
  public void setDropdownStyle(String dropdownStyle) {
    this.dropdownStyle = dropdownStyle;
  }

  /**
   * Get info dropdown children data
   *
   * @return Info dropdown children data
   */
  @JsonGetter("children")
  public Integer getChildren() {
    return getElementsByType(Component.class).size();
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "info-dropdown";
  }
}
