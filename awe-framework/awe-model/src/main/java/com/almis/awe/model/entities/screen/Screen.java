package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Screen Class
 *
 * Used to parse the files in the screen folder with XStream
 * Base tag for each screen
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@XStreamAlias("screen")
@NoArgsConstructor
public class Screen extends Element {

  private static final long serialVersionUID = -7723166098377367572L;

  // Template of the screen
  @XStreamAlias("template")
  @XStreamAsAttribute
  private String template;

  // Menu which belongs the screen
  @XStreamAlias("menu")
  @XStreamAsAttribute
  private String menu;

  // Initial target action (to load data from a query)
  @XStreamAlias("target")
  @XStreamAsAttribute
  private String target;

  // Initial Maintain Target action (to launch a maintain on screen load)
  @XStreamAlias("onload")
  @XStreamAsAttribute
  private String onLoad;

  // Final Maintain Target action (to launch a maintain on screen unload)
  @XStreamAlias("onunload")
  @XStreamAsAttribute
  private String onUnload;

  // Indicates if screen must keep the criteria or not
  @XStreamAlias("keep-criteria")
  @XStreamAsAttribute
  private Boolean keepCriteria;

  @XStreamAlias("xsi:noNamespaceSchemaLocation")
  @XStreamAsAttribute
  private String noNamespaceSchemaLocation;

  @JsonIgnore
  @XStreamOmitField
  private boolean initialized;

  @Override
  public Screen copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Avoid to retrieve screen template
   *
   * @return Screen template
   */
  public String getScreenTemplate() {
    return this.template;
  }

  /**
   * Returns if is keep criteria
   *
   * @return Is keep criteria
   */
  public boolean isKeepCriteria() {
    return keepCriteria != null && keepCriteria;
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
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    printElementList.add(this);
    return super.getReportStructure(printElementList, null, parameters, dataSuffix);
  }
}
