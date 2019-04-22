/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Variable Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Parses a variable in the query
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("variable")
public class Variable extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 7027384293892109171L;

  // Variable identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id = null;

  // Variable type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  // Variable value (static)
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Session variable identifier
  @XStreamAlias("session")
  @XStreamAsAttribute
  private String session = null;

  // Property identifier
  @XStreamAlias("property")
  @XStreamAsAttribute
  private String property = null;

  // Variable name (from request)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  // Optional variable
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private String optional = null;

  // Variable is from an audit field
  @XStreamOmitField
  private String audit = null;

  // Variable ignores case
  @XStreamOmitField
  private String ignoreCase = null;

  // Variable is trimmed
  @XStreamOmitField
  private String trim = null;

  // Variable is defined by sequence
  @XStreamOmitField
  private String sequence = null;

  /**
   * Default constructor
   */
  public Variable() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Variable(Variable other) {
    super(other);
    this.id = other.id;
    this.type = other.type;
    this.value = other.value;
    this.session = other.session;
    this.property = other.property;
    this.name = other.name;
    this.optional = other.optional;
    this.audit = other.audit;
    this.ignoreCase = other.ignoreCase;
    this.trim = other.trim;
    this.sequence = other.sequence;
  }

  /**
   * Returns the variable identifier
   *
   * @return Variable identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the variable identifier
   *
   * @param id Variable identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the variable type (ParameterType)
   *
   * @return Variable type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the variable type (ParameterType)
   *
   * @param type Variable type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the variable static value
   *
   * @return Variable static value
   */
  public String getValue() {
    return value;
  }

  /**
   * Stores the variable static value
   *
   * @param value Variable static value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the name of the request variable
   *
   * @return Name of the request variable
   */
  public String getName() {
    return name;
  }

  /**
   * Stores the name of the request variable
   *
   * @param name Name of the request variable
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the name of the session variable
   *
   * @return Name of the session variable
   */
  public String getSession() {
    return session;
  }

  /**
   * Stores the name of the session variable
   *
   * @param session Name of the session variable
   */
  public void setSession(String session) {
    this.session = session;
  }

  /**
   * Returns the name of the property
   *
   * @return Name of the property
   */
  public String getProperty() {
    return property;
  }

  /**
   * Stores the name of the property
   *
   * @param property Name of the property
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Returns if variable is from an audit field
   *
   * @return Variable is from an audit field
   */
  public boolean isAudit() {
    return Boolean.parseBoolean(audit);
  }

  /**
   * Stores if variable is from an audit field
   *
   * @param audit Variable is from an audit field
   */
  public void setAudit(String audit) {
    this.audit = audit;
  }

  /**
   * Returns if variable is optional
   *
   * @return Variable is optional
   */
  public Boolean isOptional() {
    return Boolean.parseBoolean(optional);
  }

  /**
   * Stores if variable is optional
   *
   * @param optional Variable is optional
   */
  public void setOptional(String optional) {
    this.optional = optional;
  }

  /**
   * Returns if variable ignores case
   *
   * @return the ignoreCase
   */
  public Boolean isIgnoreCase() {
    return Boolean.parseBoolean(ignoreCase);
  }

  /**
   * Sets if variable ignores case
   *
   * @param ignoreCase the ignoreCase to set
   */
  public void setIgnoreCase(String ignoreCase) {
    this.ignoreCase = ignoreCase;
  }

  /**
   * Returns if variable is trimmed
   *
   * @return Variable is trimmed
   */
  public Boolean isTrim() {
    return Boolean.parseBoolean(trim);
  }

  /**
   * Sets if variable is trimmed
   *
   * @param trim Variable is trimmed
   */
  public void setTrim(String trim) {
    this.trim = trim;
  }

  /**
   * @return the sequence
   */
  public String getSequence() {
    return sequence;
  }

  /**
   * @param sequence the sequence to set
   */
  public void setSequence(String sequence) {
    this.sequence = sequence;
  }

  @Override
  public Variable copy() throws AWException {
    return new Variable(this);
  }
}
