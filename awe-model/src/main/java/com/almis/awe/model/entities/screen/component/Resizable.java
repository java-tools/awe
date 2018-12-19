/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Resizable Class
 *
 * Used to parse a resizable tag with XStream
 *
 *
 * Generates a resizable structure
 *
 *
 * @author Pablo GARCIA - 05/MAY/2015
 */
@XStreamAlias("resizable")
public class Resizable extends Component {

  private static final long serialVersionUID = -185783597311876025L;
  // Resizable directions
  @XStreamAlias("directions")
  @XStreamAsAttribute
  private String directions = null;

  /**
   * Default constructor
   */
  public Resizable() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Resizable(Resizable other) throws AWException {
    super(other);
    this.directions = other.directions;
  }

  @Override
  public Resizable copy() throws AWException {
    return new Resizable(this);
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "resizable";
  }

  /**
   * @return the directions
   */
  @JsonGetter("directions")
  public String getDirections() {
    return directions;
  }

  /**
   * @param directions the directions to set
   */
  public void setDirection(String directions) {
    this.directions = directions;
  }
}
