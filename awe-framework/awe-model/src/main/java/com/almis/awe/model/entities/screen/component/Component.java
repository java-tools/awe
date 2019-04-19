/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.Include;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.View;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.button.Button;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import com.almis.awe.model.entities.screen.component.button.InfoButton;
import com.almis.awe.model.entities.screen.component.chart.*;
import com.almis.awe.model.entities.screen.component.container.AccordionItem;
import com.almis.awe.model.entities.screen.component.container.TabContainer;
import com.almis.awe.model.entities.screen.component.container.WizardPanel;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.model.entities.screen.component.criteria.InfoCriteria;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;
import com.almis.awe.model.entities.screen.component.panelable.Accordion;
import com.almis.awe.model.entities.screen.component.panelable.Tab;
import com.almis.awe.model.entities.screen.component.panelable.Wizard;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;
import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import org.springframework.beans.factory.annotation.Value;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Component Class
 * Used to parse a COMPONENT with XStream
 * Superclass of all component, like Window, Button, ButtonAction, Message, Column, Criteria, Grid, Tab
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamInclude({Window.class, Resizable.class, Accordion.class, AccordionItem.class, Button.class, ContextButton.class, ContextSeparator.class, Message.class, Criteria.class, Grid.class,
        Include.class, Dialog.class, Tab.class, TabContainer.class, Wizard.class, WizardPanel.class, GroupHeader.class, Column.class, Widget.class, WidgetParameter.class, Chart.class, ChartLegend.class,
        ChartTooltip.class, ChartAxis.class, ChartSerie.class, ChartParameter.class, Info.class, InfoCriteria.class, InfoButton.class, MenuContainer.class, Frame.class, View.class, TagList.class,
        PivotTable.class, Video.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class Component extends Element {

  private static final long serialVersionUID = 7408027548839969343L;

  @Value("${application.data.rowsPerPage:30}")
  String rowsPerPage;

  // Component name (identifier)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name = null;

  // Javascript Component
  @XStreamAlias("component")
  @XStreamAsAttribute
  @JsonProperty("component")
  private String componentType = "other";

  // Initial Load Method
  @XStreamAlias("initial-load")
  @XStreamAsAttribute
  private String initialLoad = null;
  // Server Action
  @XStreamAlias("server-action")
  @XStreamAsAttribute
  private String serverAction = null;

  // Target Action
  @XStreamAlias("target-action")
  @XStreamAsAttribute
  private String targetAction = null;

  // Max number of elements
  @XStreamAlias("max")
  @XStreamAsAttribute
  private String max = null;

  // Set AUTOREFRESH TIMEOUT
  @XStreamAlias("autorefresh")
  @XStreamAsAttribute
  private String autorefresh = null;

  // Grid launches 'filter' action at screen startup
  @XStreamAlias("autoload")
  @XStreamAsAttribute
  private String autoload = null;

  // Component icon class
  @XStreamAlias("icon")
  @XStreamAsAttribute
  private String icon = null;

  // Component size
  @XStreamAlias("size")
  @XStreamAsAttribute
  private String size = null;

  // Loading icon
  @XStreamAlias("icon-loading")
  @XStreamAsAttribute
  private String iconLoading = null;

  // Component visibility
  @XStreamAlias("visible")
  @XStreamAsAttribute
  private String visible = null;

  // Load all the data initially or not
  @XStreamAlias("load-all")
  @XStreamAsAttribute
  private String loadAll = null;

  /**
   * Default constructor
   */
  public Component() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Component(Component other) throws AWException {
    super(other);
    this.rowsPerPage = other.rowsPerPage;
    this.name = other.name;
    this.componentType = other.componentType;
    this.initialLoad = other.initialLoad;
    this.serverAction = other.serverAction;
    this.targetAction = other.targetAction;
    this.max = other.max;
    this.autorefresh = other.autorefresh;
    this.autoload = other.autoload;
    this.icon = other.icon;
    this.size = other.size;
    this.iconLoading = other.iconLoading;
    this.visible = other.visible;
    this.loadAll = other.loadAll;
  }

  /**
   * Returns the criteria parameter name
   *
   * @return Parameter name
   */
  @JsonGetter("name")
  public String getName() {
    return name;
  }

  /**
   * Stores the criteria parameter name
   *
   * @param name Parameter name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the criteria javascript COMPONENT
   *
   * @return Javascript COMPONENT name
   */
  public String getComponentType() {
    return componentType;
  }

  /**
   * Stores the criteria javascript COMPONENT
   *
   * @param componentType Javascript COMPONENT
   */
  public void setComponentType(String componentType) {
    this.componentType = componentType;
  }

  /**
   * Returns the criteria initial load method
   *
   * @return Initial load method
   */
  @JsonIgnore
  public String getInitialLoad() {
    return initialLoad;
  }

  /**
   * Stores the criteria initial load method
   *
   * @param initialLoad Initial load method
   */
  public void setInitialLoad(String initialLoad) {
    this.initialLoad = initialLoad;
  }

  /**
   * Retrieve if is AUTOLOAD
   *
   * @return the AUTOLOAD
   */
  @JsonGetter("autoload")
  public boolean isAutoload() {
    return Boolean.parseBoolean(autoload);
  }

  /**
   * Retrieve if is AUTOLOAD
   *
   * @return the AUTOLOAD
   */
  public String getAutoload() {
    return autoload;
  }

  /**
   * Store if is AUTOLOAD
   *
   * @param autoload the AUTOLOAD to set
   */
  public void setAutoload(String autoload) {
    this.autoload = autoload;
  }

  /**
   * Returns the criteria server action
   *
   * @return Criteria server action
   */
  @JsonGetter("serverAction")
  public String getServerAction() {
    return serverAction;
  }

  /**
   * Stores the criteria server action
   *
   * @param serverAction Criteria server action
   */
  public void setServerAction(String serverAction) {
    this.serverAction = serverAction;
  }

  /**
   * Returns the criteria target action
   *
   * @return Criteria target action
   */
  @JsonGetter("targetAction")
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Stores the criteria target action
   *
   * @param targetAction Criteria target action
   */
  public void setTargetAction(String targetAction) {
    this.targetAction = targetAction;
  }

  /**
   * Returns the criteria MAX elements requested (0 for all)
   *
   * @return Max elements requested
   */
  public String getMax() {
    return max == null ? rowsPerPage : max;
  }

  /**
   * Return the criteria MAX elements requested (0 for all) as an INTEGER
   *
   * @return Max elements requested
   */
  @JsonGetter("max")
  public Integer getMaxConverter() {
    return max != null ? Integer.valueOf(max) : null;
  }

  /**
   * Stores the criteria MAX elements requested (0 for all)
   *
   * @param max Max elements requested
   */
  public void setMax(String max) {
    this.max = max;
  }

  /**
   * Returns the AUTOREFRESH TIMEOUT
   *
   * @return the AUTOREFRESH
   */
  @JsonGetter("autorefresh")
  public String getAutorefresh() {
    return autorefresh;
  }

  /**
   * Stores the AUTOREFRESH TIMEOUT
   *
   * @param autorefresh the AUTOREFRESH to set
   */
  public void setAutorefresh(String autorefresh) {
    this.autorefresh = autorefresh;
  }

  /**
   * Returns the icon
   *
   * @return the icon
   */
  @JsonGetter("icon")
  public String getIcon() {
    return icon;
  }

  /**
   * Stores the icon
   *
   * @param icon the icon to set
   */
  public void setIcon(String icon) {
    this.icon = icon;
  }

  /**
   * Retrieve the size
   *
   * @return the size
   */
  @JsonGetter("size")
  public String getSize() {
    return size;
  }

  /**
   * Store the size
   *
   * @param size the size
   */
  public void setSize(String size) {
    this.size = size;
  }

  /**
   * Returns if criterion is VISIBLE
   *
   * @return the restricted
   */
  @JsonGetter("visible")
  public boolean isVisible() {
    return Boolean.parseBoolean(this.getVisible());
  }

  /**
   * Retrieve if criterion is VISIBLE
   *
   * @return VISIBLE
   */
  public String getVisible() {
    return this.visible == null ? "true" : this.visible;
  }

  /**
   * Stores if criterion is VISIBLE
   *
   * @param visible the restricted to set
   */
  public void setVisible(String visible) {
    this.visible = visible;
  }

  /**
   * @return the iconLoading
   */
  @JsonGetter("iconLoading")
  public String getIconLoading() {
    return iconLoading;
  }

  /**
   * @param iconLoading the iconLoading to set
   */
  public void setIconLoading(String iconLoading) {
    this.iconLoading = iconLoading;
  }

  /**
   * Returns if all the data has to be loaded or not
   *
   * @return LOAD_ALL load all or not (string)
   */
  public String getLoadAll() {
    return loadAll;
  }

  /**
   * Stores if all data has to be loaded or not
   *
   * @param loadAll load all or not (string)
   */
  public void setLoadAll(String loadAll) {
    this.loadAll = loadAll;
  }

  /**
   * Returns if grid is LoadAll
   *
   * @return Grid is LoadAll
   */
  @JsonGetter("loadAll")
  public boolean isLoadAll() {
    return "true".equalsIgnoreCase(loadAll);
  }

  /**
   * Returns a serialized list with the context menu description
   *
   * @return Context Menu List
   */
  @JsonGetter("contextMenu")
  public List<Object> getContextMenuConverter() {
    Class[] classes = new Class[]{ContextButton.class, ContextSeparator.class};
    return getChildrenByType(classes);
  }

  /**
   * Returns the criteria dependency model for JSON serialization
   *
   * @return Dependency List
   */
  @JsonGetter("dependencies")
  public List<Dependency> getDependencyConverter() {
    return getChildrenByType(Dependency.class);
  }

  /**
   * Generates the output HTML of the element
   *
   * @param group String Template Group
   * @return Code
   */
  @Override
  public ST generateTemplate(STGroup group) {
    ST template = group.createStringTemplate(group.rawGetTemplate(getTemplate()));
    List<ST> children = new ArrayList<>();

    // Call generate method on all children
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        // Generate the children
        children.add(element.generateTemplate(group));
      }
    }

    // Generate template
    template.add("e", this).add("component", getComponentTag()).add("children", children);

    // Retrieve code
    return template;
  }

  /**
   * Retrieve element template
   *
   * @return Element template
   */
  @Override
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_COMPONENT;
  }

  /**
   * Retrieve component tag (to be overriden) tag
   *
   * @return Component tag
   */
  @JsonIgnore
  public String getComponentTag() {
    return getComponentType();
  }

  @Override
  public String getElementKey() {
    return this.getId() == null ? AweConstants.NO_KEY : this.getId();
  }
}
