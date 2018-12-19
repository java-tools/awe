/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.button;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * InfoButton Class
 *
 * Used to add an info with info button element with XStream
 *
 *
 * @author Pablo Vidal - 04/JUN/2015
 */
@XStreamAlias("info-button")
public class InfoButton extends Button {

  private static final long serialVersionUID = -8521012517719249256L;
  // Info style
  @XStreamAlias("info-style")
  @XStreamAsAttribute
  private String infoStyle = null;

  /**
   * Default constructor
   */
  public InfoButton() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public InfoButton(InfoButton other) throws AWException {
    super(other);
    this.infoStyle = other.infoStyle;
  }

  @Override
  public InfoButton copy() throws AWException {
    return new InfoButton(this);
  }

  /**
   * @return the infoStyle
   */
  @JsonGetter("infoStyle")
  public String getInfoStyle() {
    return infoStyle;
  }

  /**
   * @param infoStyle the infoStyle to set
   */
  public void setInfoStyle(String infoStyle) {
    this.infoStyle = infoStyle;
  }

  /**
   * Retrieves value for JSON serialization
   *
   * @return value
   */
  @JsonGetter("text")
  public String getValueConverter() {
    return this.getValue();
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "info-button";
  }
}
