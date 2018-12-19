/*
 * Package definition
 */
package com.almis.awe.model.entities.queues;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/*
 * File Imports
 */

/**
 * MessageWrapper Class
 *
 * Used to parse the tag 'message-wrapper' in file Queues.xml with XStream
 *
 *
 * This class is used to instantiate message parameters for a queue
 *
 *
 * @author Pablo GARCIA - 31/OCT/2013
 */
@XStreamAlias("message-wrapper")
public class MessageWrapper extends XMLWrapper {

  private static final long serialVersionUID = -5436753008930958151L;
  // Wrapper parameter name
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;
  // Wrapper type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;
  // Wrapper java classname
  @XStreamAlias("classname")
  @XStreamAsAttribute
  private String className;

  /**
   * Default constructor
   */
  public MessageWrapper() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public MessageWrapper(MessageWrapper other) throws AWException {
    super(other);
    this.name = other.name;
    this.type = other.type;
    this.className = other.className;
  }

  /**
   * Returns the classname
   *
   * @return Classname
   */
  public String getClassName() {
    return className;
  }

  /**
   * Stores the classname
   *
   * @param classname Classname
   */
  public void setClassName(String classname) {
    this.className = classname;
  }

  /**
   * Returns the parameter name
   *
   * @return Parameter name
   */
  public String getName() {
    return name;
  }

  /**
   * Store parameter object name
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Retrieve parameter object type
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Store parameter object type
   *
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }
}
