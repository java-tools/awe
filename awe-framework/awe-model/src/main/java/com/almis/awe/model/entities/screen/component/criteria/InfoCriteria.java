/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.criteria;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

/**
 * InfoCriteria Class
 *
 * Used to add an info with criteria element with XStream
 *
 *
 * @author Pablo GARCIA - 04/JUN/2012
 */
@XStreamAlias("info-criteria")
public class InfoCriteria extends Criteria {

  private static final long serialVersionUID = 6550382841315255907L;
  // Button Type (button, submit, reset)
  @XStreamAlias("info-style")
  @XStreamAsAttribute
  private String infoStyle = null;

  /**
   * Default constructor
   */
  public InfoCriteria() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public InfoCriteria(InfoCriteria other) throws AWException {
    super(other);
    this.infoStyle = other.infoStyle;
  }

  @Override
  public InfoCriteria copy() throws AWException {
    return new InfoCriteria(this);
  }

  /**
   * @return the infoStyle
   */
  @JsonIgnore
  public String getInfoStyle() {
    return infoStyle;
  }

  /**
   * @param infoStyle the infoStyle to set
   */
  public void setInfoStyle(String infoStyle) {
    this.infoStyle = infoStyle;
  }

  @Override
  @JsonIgnore
  public ST generateTemplate(STGroup group) {
    ST template = group.createStringTemplate(group.rawGetTemplate(AweConstants.TEMPLATE_INFO));
    ST children = super.generateTemplate(group);

    // Generate template
    template.add("e", this).add("children", children);

    // Retrieve code
    return template;
  }
}
