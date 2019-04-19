package com.almis.awe.model.entities;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

/**
 * Global Class
 *
 * Used to parse an element with name, value and label inside an XML with XStream
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@XStreamAlias("global")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"markdown"})
public class Global extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 5073568362212574171L;

  // Name of the element
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Value of the element
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Label of the element
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Global content as markdown
  private String markdown;

  /**
   * Default constructor
   */
  public Global() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Global(Global other) {
    super(other);
    this.name = other.name;
    this.value = other.value;
    this.label = other.label;
    this.markdown = other.markdown;
  }

  /**
   * @return the elementKey
   */
  @JsonIgnore
  @Override
  public String getElementKey() {
    return getName();
  }

  /**
   * Returns the name of the element
   *
   * @return Element name
   */
  public String getName() {
    return name;
  }

  /**
   * Stores the name of the element
   *
   * @param name Element name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the value of the element
   *
   * @return Element value
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the value of the element
   *
   * @param value Element value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the label of the element (Should be translated with getContext().getLocal())
   *
   * @return Element label
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Stores the label of the element
   *
   * @param label Element label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Retrieve the markdown label of the element
   *
   * @return the markdown
   */
  public String getMarkdown() {
    return markdown;
  }

  /**
   * Store the markdown label of the element
   *
   * @param markdown the markdown to set
   */
  public void setMarkdown(String markdown) {
    this.markdown = markdown;
  }

  @Override
  public Global copy() throws AWException {
    return new Global(this);
  }
}
