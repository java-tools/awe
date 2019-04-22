package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Parameter Class
 * Used to parse the file Actions.xml with XStream
 * Client action parameter
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("parameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Parameter extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -2633008018819827229L;

  // Parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Parameter value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Parameter label
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Parameter variable
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable;

  // Parameter parameter
  @XStreamAlias("parameter")
  @XStreamAsAttribute
  private String requestParameter;

  // Parameter value
  @XStreamOmitField
  private CellData cellData;

  /**
   * Constructor
   *
   * @param name     parameter name
   * @param cellData parameter data
   */
  public Parameter(String name, CellData cellData) {
    this.name = name;
    this.cellData = cellData;
  }

  /**
   * Constructor by default
   */
  public Parameter() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Parameter(Parameter other) {
    super(other);
    this.name = other.name;
    this.value = other.value;
    this.label = other.label;
    this.variable = other.variable;
    this.requestParameter = other.requestParameter;
  }

  /**
   * Returns the parameter name
   *
   * @return Parameter name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Stores the parameter name
   *
   * @param name Parameter name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the parameter value. If label is defined, returns the label local. If variable is defined, returns the output defined variable.
   *
   * @return Parameter value
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Stores the parameter value
   *
   * @param value Parameter value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the parameter value as celldata
   *
   * @return Parameter objectvalue
   */
  public CellData getCellData() {
    return this.cellData;
  }

  /**
   * Stores the parameter value as celldata
   *
   * @param value Parameter value
   */
  public void setCellData(CellData value) {
    this.cellData = value;
  }

  /**
   * Returns the parameter label
   *
   * @return Parameter label
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Stores the parameter label
   *
   * @param label Parameter label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the parameter variable
   *
   * @return Parameter variable
   */
  public String getVariable() {
    return this.variable;
  }

  /**
   * Stores the parameter variable
   *
   * @param variable Parameter variable
   */
  public void setVariable(String variable) {
    this.variable = variable;
  }

  /**
   * Returns the parameter
   *
   * @return Parameter
   */
  public String getRequestParameter() {
    return this.requestParameter;
  }

  /**
   * Stores the parameter
   *
   * @param requestParameter Parameter
   */
  public void setRequestParameter(String requestParameter) {
    this.requestParameter = requestParameter;
  }

  @Override
  public Parameter copy() throws AWException {
    return new Parameter(this);
  }
}