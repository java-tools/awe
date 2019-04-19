/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Message Class
 *
 * Used to parse a message tag with XStream
 *
 *
 * Generates a piece of code with literals that can be used to retrieve confirmation title and descriptions
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("frame")
public class Frame extends Component {

  private static final long serialVersionUID = 2809318507420231428L;
  // Frame screen
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen = null;
  // Frame screen variable
  @XStreamAlias("screen-variable")
  @XStreamAsAttribute
  private String screenVariable = null;

  // Frame scroll
  @XStreamAlias("scroll")
  @XStreamAsAttribute
  private String scroll = null;

  /**
   * Default constructor
   */
  public Frame() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Frame(Frame other) throws AWException {
    super(other);
    this.screen = other.screen;
    this.screenVariable = other.screenVariable;
    this.scroll = other.scroll;
  }

  @Override
  public Frame copy() throws AWException {
    return new Frame(this);
  }

  /**
   * Returns the frame initial screen
   *
   * @return Frame initial screen
   */
  public String getScreen() {
    return screen;
  }

  /**
   * Stores the initial screen
   *
   * @param screen Initial screen
   */
  public void setScreen(String screen) {
    this.screen = screen;
  }

  /**
   * Returns the frame scroll
   *
   * @return Frame scroll
   */
  public String getScroll() {
    return scroll;
  }

  /**
   * Stores the frame scroll
   *
   * @param scroll Frame scroll
   */
  public void setScroll(String scroll) {
    this.scroll = scroll;
  }

  /**
   * @return the screenVariable
   */
  public String getScreenVariable() {
    return screenVariable;
  }

  /**
   * @param screenVariable the screenVariable to set
   */
  public void setScreenVariable(String screenVariable) {
    this.screenVariable = screenVariable;
  }
}
