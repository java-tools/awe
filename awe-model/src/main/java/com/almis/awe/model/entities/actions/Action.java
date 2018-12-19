/*
 * Package definition
 */
package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * Action Class
 *
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse a single action inside the file
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
public class Action extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 8303124070775783625L;

  // Action id
  @XStreamAsAttribute
  private String id;

  // Action format
  @XStreamAsAttribute
  private String format;

  // Action call list
  @XStreamAlias("call")
  private Call call = null;

  // Action answer if output is OK
  @XStreamImplicit(itemFieldName = "answer")
  private List<Answer> answers;

  /**
   * Default constructor
   */
  public Action() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Action(Action other) throws AWException {
    super(other);
    this.id = other.id;
    this.format = other.format;
    this.call = other.call == null ? null : new Call(other.call);
    this.answers = ListUtil.copyList(other.answers);
  }

  /**
   * Returns the action id
   *
   * @return Action id
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the action id
   *
   * @param id Action id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the action call list
   *
   * @return Action call list
   */
  public Call getCall() {
    return call;
  }

  /**
   * Stores the action call
   *
   * @param calls Action call
   */
  public void setCall(Call calls) {
    this.call = calls;
  }

  /**
   * Returns the action format
   *
   * @return Action format
   */
  public String getFormat() {
    return format;
  }

  /**
   * Stores the action format
   *
   * @param format Action format
   */
  public void setFormat(String format) {
    this.format = format;
  }

  /**
   * Returns the list of answers for the action
   *
   * @return List of action answers
   */
  public List<Answer> getAnswers() {
    return answers;
  }

  /**
   * Stores the list of answers for the action
   *
   * @param answers List of action answers
   */
  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  /**
   * Returns an answer given its type
   *
   * @param type
   * @return List of action answers
   */
  public Answer getAnswer(AnswerType type) throws AWException {
    return getAnswer(type.toString());
  }

  /**
   * Returns an answer given its type
   *
   * @param type
   * @return List of action answers
   */
  public Answer getAnswer(String type) throws AWException {

    // Variable definition
    Answer out = null;

    // Check if list is not empty
    if (this.getAnswers() != null) {
      for (Answer answer : this.getAnswers()) {
        if (answer.getType().equals(type)) {
          out = new Answer(answer);
        }
      }
    }

    return out;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getId().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public Action copy() throws AWException {
    return new Action(this);
  }
}
