/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.panelable;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tab Class
 *
 * Panelable class
 *
 *
 * Generates an screen criteria
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamInclude({Accordion.class, Tab.class, Wizard.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class Panelable extends Criteria {

  private static final long serialVersionUID = 4769623059339446522L;

  @XStreamOmitField
  private
  Map<String, String> tabValues;

  /**
   * Default constructor
   */
  public Panelable() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Panelable(Panelable other) throws AWException {
    super(other);
    this.tabValues = ListUtil.copyMap(other.tabValues, String.class);
  }

  @JsonIgnore
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    // Check if print all tabs or only selected tab
    boolean printAllTabs = "0".equalsIgnoreCase(parameters.get(AweConstants.PRINT_TABS).asText());
    String selectedTab = parameters.get(this.getId()).asText();
    ObjectNode dataNode = (ObjectNode) parameters.get(this.getId() + dataSuffix);
    ArrayNode panelableValues = (ArrayNode) dataNode.get(AweConstants.JSON_ALL);

    // Call generate method on all children
    if (getElementList() != null) {
      for (Element element : getElementList()) {
        if (printAllTabs || element.getId().equalsIgnoreCase(selectedTab)) {
          element.getReportStructure(printElementList, getTabLabel(element.getId(), panelableValues), parameters, dataSuffix);
        }
      }
    }
    return printElementList;
  }

  /**
   * Retrieve tab label
   *
   * @param tabId     Tab identifier
   * @param tabValues Tab values
   * @return Tab label
   */
  private String getTabLabel(String tabId, ArrayNode tabValues) {
    for (JsonNode tabValue : tabValues) {
      if (tabId.equalsIgnoreCase(tabValue.get(AweConstants.JSON_VALUE_PARAMETER).asText())) {
        return tabValue.get(AweConstants.JSON_LABEL_PARAMETER).asText();
      }
    }
    return null;
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_EMPTY;
  }

  /**
   * Generates the help template of the element
   *
   * @param group        String Template Group
   * @param label        Parent label
   * @param templateName String Template name
   * @param developers   Help for developers
   * @return Code
   */
  @Override
  public ST generateHelpTemplate(STGroup group, String label, String templateName, boolean developers) {
    ST template = group.createStringTemplate(group.rawGetTemplate(templateName));
    List<ST> children = new ArrayList<>();
    Map<String, String> panelableValues = getTabValues();
    String currentLabel = getLabel() == null ? label : getLabel();

    // Call generate method on all children
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        // Generate the children
        String tabContainerLabel = element.getLabel();
        if (tabContainerLabel == null) {
          if (panelableValues != null && panelableValues.containsKey(element.getId())) {
            tabContainerLabel = panelableValues.get(element.getId());
          } else {
            tabContainerLabel = currentLabel;
          }
        }
        children.add(element.generateHelpTemplate(group, tabContainerLabel, developers));
      }
    }

    // Generate template
    template.add("e", this)
            .add("label", currentLabel)
            .add("content", children)
            .add("developers", developers);

    // Retrieve code
    return template;
  }

  /**
   * Set tab values
   *
   * @param tabValues Tab values
   * @return this
   */
  public Panelable setTabValues(Map<String, String> tabValues) {
    this.tabValues = tabValues;
    return this;
  }

  /**
   * Get tab values
   *
   * @return Tab values
   */
  public Map<String, String> getTabValues() {
    return tabValues;
  }
}
