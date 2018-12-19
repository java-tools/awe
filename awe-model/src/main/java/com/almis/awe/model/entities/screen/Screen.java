/*
 * Package definition
 */
package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.XMLWrapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/**
 * Screen Class
 *
 * Used to parse the files in the screen folder with XStream
 * Base tag for each screen
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("screen")
public class Screen extends Element {

  private static final long serialVersionUID = -7723166098377367572L;

  // Template of the screen
  @XStreamAlias("template")
  @XStreamAsAttribute
  private String template = null;

  // Menu which belongs the screen
  @XStreamAlias("menu")
  @XStreamAsAttribute
  private String mnu = null;

  // Initial target action (to load data from a query)
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target = null;

  // Initial Maintain Target action (to launch a maintain on screen load)
  @XStreamAlias("onload")
  @XStreamAsAttribute
  private String onLoad = null;

  // Final Maintain Target action (to launch a maintain on screen unload)
  @XStreamAlias("onunload")
  @XStreamAsAttribute
  private String onUnload = null;

  // Indicates if screen must keep the criteria or not
  @XStreamAlias("keep-criteria")
  @XStreamAsAttribute
  private String keepCriteria = null;

  @XStreamAlias("xsi:noNamespaceSchemaLocation")
  @XStreamAsAttribute
  private String noNamespaceSchemaLocation = "../../../tools/awe/sch/screen.xsd";

  @XStreamOmitField
  private boolean initialized = false;

  /**
   * Default constructor
   */
  public Screen() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Screen(Screen other) throws AWException {
    super(other);
    this.template = other.template;
    this.mnu = other.mnu;
    this.target = other.target;
    this.onLoad = other.onLoad;
    this.onUnload = other.onUnload;
    this.keepCriteria = other.keepCriteria;
    this.noNamespaceSchemaLocation = other.noNamespaceSchemaLocation;
    this.initialized = other.initialized;
  }

  @Override
  public Screen copy() throws AWException {
    return new Screen(this);
  }

  /**
   * Returns the screen template
   *
   * @return Screen template
   */
  @Override
  public String getTemplate() {
    return template;
  }

  /**
   * Stores the screen template
   *
   * @param template Screen Template
   */
  public void setTemplate(String template) {
    this.template = template;
  }

  /**
   * Retrieve On Load maintain
   *
   * @return the onLoad
   */
  public String getOnLoad() {
    return onLoad;
  }

  /**
   * Set On Load maintain
   *
   * @param onLoad the onLoad to set
   */
  public void setOnLoad(String onLoad) {
    this.onLoad = onLoad;
  }

  /**
   * Retrieve On Unload maintain
   *
   * @return the onUnload
   */
  public String getOnUnload() {
    return onUnload;
  }

  /**
   * Set On Unload maintain
   *
   * @param onUnload the onUnload to set
   */
  public void setOnUnload(String onUnload) {
    this.onUnload = onUnload;
  }

  /**
   * Retrieve noNamespaceSchemaLocation
   *
   * @return the noNamespaceSchemaLocation
   */
  public String getNoNamespaceSchemaLocation() {
    return noNamespaceSchemaLocation;
  }

  /**
   * Set noNamespaceSchemaLocation
   *
   * @param noNamespaceSchemaLocation the noNamespaceSchemaLocation to set
   */
  public void setNoNamespaceSchemaLocation(String noNamespaceSchemaLocation) {
    this.noNamespaceSchemaLocation = noNamespaceSchemaLocation;
  }

  /**
   * Returns the screen menu identifier
   *
   * @return Screen menu identifier
   */
  public String getMenu() {
    return mnu;
  }

  /**
   * Stores the screen menu identifier
   *
   * @param menu Screen menu identifier
   */
  public void setMenu(String menu) {
    this.mnu = menu;
  }

  /**
   * Returns the target action identifier
   *
   * @return Target action identifier
   */
  public String getTarget() {
    return target;
  }

  /**
   * Stores the target action identifier
   *
   * @param target Target action identifier
   */
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Returns if the screen must keep the criteria
   *
   * @return Keep the screen criteria or not
   */
  public String getKeepCriteria() {
    return keepCriteria;
  }

  /**
   * Returns if the screen must keep the criteria
   *
   * @return Keep the screen criteria or not
   */
  public boolean keepCriteria() {
    return "true".equalsIgnoreCase(keepCriteria);
  }

  /**
   * Stores if the screen must keep the criteria or not
   *
   * @param keep Keep the screen criteria or not
   */
  public void setKeepCriteria(String keep) {
    this.keepCriteria = keep;
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
    printElementList.add(this);
    return super.getReportStructure(printElementList, null, parameters, dataSuffix);
  }

  /**
   * Screen has been initialized
   *
   * @return initialized
   */
  @JsonIgnore
  public boolean isInitialized() {
    return initialized;
  }

  /**
   * Set screen as initialized
   *
   * @param initialized Initialized
   * @return this
   */
  public Screen setInitialized(boolean initialized) {
    this.initialized = initialized;
    return this;
  }
}
