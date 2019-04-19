/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;

/**
 * Window Class
 *
 * Used to parse a window tag with XStream
 *
 *
 * Generates a window structure with header center and footing
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("window")
@JsonIgnoreProperties({"id"})
public class Window extends Component {

  private static final long serialVersionUID = 5159433149044786985L;
  // Window can be maximized or not
  @XStreamAlias("maximize")
  @XStreamAsAttribute
  private String maximize = null;

  /**
   * Default constructor
   */
  public Window() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Window(Window other) throws AWException {
    super(other);
    this.maximize = other.maximize;
  }

  @Override
  public Window copy() throws AWException {
    return new Window(this);
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return component tag
   */
  @Override
  public String getComponentTag() {
    return "window";
  }

  /**
   * Returns if window allows to maximize/restore itself
   *
   * @return Window allows to maximize/restore itself
   */
  public boolean allowMaximize() {
    if ("true".equalsIgnoreCase(this.getMaximize())) {
      return this.getLabel() != null;
    } else {
      return false;
    }
  }

  /**
   * Returns if window allows to maximize/restore itself for JSON serialization
   *
   * @return Window allows to maximize/restore itself
   */
  @JsonGetter("maximize")
  public boolean getMaximizeConverter() {
    if ("true".equalsIgnoreCase(this.getMaximize())) {
      return this.getLabel() != null;
    } else {
      return false;
    }
  }

  /**
   * Returns if window maximizes or not
   *
   * @return Window has the maximize button or not
   */
  public String getMaximize() {
    return maximize;
  }


  /**
   * Sets if window maximizes or not
   *
   * @param maximize Maximize window
   */
  public void setMaximize(String maximize) {
    this.maximize = maximize;
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Previous label
   * @param parameters       Parameters
   * @param dataSuffix       Data suffix
   * @return Print bean
   */
  @JsonIgnore
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    return super.getReportStructure(printElementList, getLabel() == null ? label : getLabel(), parameters, dataSuffix);
  }
}
