package com.almis.awe.model.entities.screen.component;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import com.almis.awe.model.entities.screen.component.chart.AbstractChart;
import com.almis.awe.model.entities.screen.component.container.Container;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.widget.AbstractWidget;
import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Component decorator
 * Used to parse a COMPONENT with XStream
 * Superclass of all component, like Window, Button, ButtonAction, Message, Column, Criteria, Grid, Tab
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Window.class, Resizable.class, AbstractCriteria.class, Dialog.class, Container.class, AbstractWidget.class,
  AbstractChart.class, Info.class, MenuContainer.class, Frame.class, TagList.class, Video.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public abstract class Component extends Element {

  private static final long serialVersionUID = 7408027548839969343L;

  // Component name (identifier)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  // Javascript Component
  @XStreamAlias("component")
  @XStreamAsAttribute
  @JsonProperty("component")
  private String componentType;

  // Initial Load Method
  @XStreamAlias("initial-load")
  @XStreamAsAttribute
  private String initialLoad;
  // Server Action
  @XStreamAlias("server-action")
  @XStreamAsAttribute
  private String serverAction;

  // Target Action
  @XStreamAlias("target-action")
  @XStreamAsAttribute
  private String targetAction;

  // Max number of elements
  @XStreamAlias("max")
  @XStreamAsAttribute
  private Integer max;

  // Set AUTOREFRESH TIMEOUT
  @XStreamAlias("autorefresh")
  @XStreamAsAttribute
  private Integer autorefresh;

  // Grid launches 'filter' action at screen startup
  @XStreamAlias("autoload")
  @XStreamAsAttribute
  private Boolean autoload;

  // Component icon class
  @XStreamAlias("icon")
  @XStreamAsAttribute
  private String icon;

  // Component size
  @XStreamAlias("size")
  @XStreamAsAttribute
  private String size;

  // Loading icon
  @XStreamAlias("icon-loading")
  @XStreamAsAttribute
  private String iconLoading;

  // Component visibility
  @XStreamAlias("visible")
  @XStreamAsAttribute
  private Boolean visible;

  // Load all the data initially or not
  @XStreamAlias("load-all")
  @XStreamAsAttribute
  private Boolean loadAll;

  /**
   * Returns if component is autoload
   * @return Component is autoload
   */
  @JsonGetter("autoload")
  public boolean isAutoload() {
    return getAutoload() != null && getAutoload();
  }

  /**
   * Returns if component is autoload
   * @return Component is autoload
   */
  @JsonGetter("loadAll")
  public boolean isLoadAll() {
    return getLoadAll() != null && getLoadAll();
  }

  /**
   * Returns if component is visible
   * @return Component is visible
   */
  @JsonGetter("visible")
  public boolean isVisible() {
    return getVisible() == null || getVisible();
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
    for (Element element : getElementList()) {
      // Generate the children
      children.add(element.generateTemplate(group));
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
}
