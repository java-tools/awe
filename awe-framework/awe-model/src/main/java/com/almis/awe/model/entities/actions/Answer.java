package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * Answer Class
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse an answer
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
public class Answer extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -3977488792390703003L;

  // Client action list
  @XStreamImplicit(itemFieldName = "response")
  private List<ClientAction> responseList;

  // Answer target
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target;

  // Answer type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  /**
   * Default constructor
   */
  public Answer() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Answer(Answer other) throws AWException {
    super(other);
    this.target = other.target;
    this.type = other.type;
    this.responseList = ListUtil.copyList(other.responseList);
  }

  /**
   * Returns the response list of client actions
   *
   * @return Response client action list
   */
  public List<ClientAction> getResponseList() {
    return responseList;
  }

  /**
   * Stores the response list of client actions
   *
   * @param responseList Response client action list
   */
  public void setResponseList(List<ClientAction> responseList) {
    this.responseList = responseList;
  }

  /**
   * Returns the answer target
   *
   * @return Answer target
   */
  public String getTarget() {
    return target;
  }

  /**
   * Stores the answer target
   *
   * @param target Answer target
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Returns the answer type
   *
   * @return Answer type
   */
  public String getType() {
    return type;
  }

  /**
   * Stores the answer type
   *
   * @param type Answer type
   */
  public void setType(String type) {
    this.type = type;
  }

  @Override
  public Answer copy() throws AWException {
    return new Answer(this);
  }
}
