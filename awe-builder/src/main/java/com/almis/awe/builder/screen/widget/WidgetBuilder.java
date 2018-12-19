/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.widget;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.enumerates.WidgetComponent;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Widget;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class WidgetBuilder extends AweBuilder<WidgetBuilder> {

  private WidgetComponent component;
  private InitialLoad initialLoad;
  private ServerAction serverAction;
  private Boolean autoload, autorefresh, visible;
  private Integer max;
  private String help, helpImage;
  private String style, targetAction;
  private List<AweBuilder> elements;
  private List<WidgetParameterBuilder> widgetParameterList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public WidgetBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
    widgetParameterList = new ArrayList<>();
  }

  @Override
  public WidgetBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Widget widget = new Widget();

    widget.setId(getId());

    if (getInitialLoad() != null) {
      widget.setInitialLoad(getInitialLoad().toString());
    }

    if (getComponent() != null) {
      widget.setComponentType(getComponent().toString());
    }

    if (getServerAction() != null) {
      widget.setServerAction(getServerAction().toString());
    }

    if (isAutoload() != null) {
      widget.setAutoload(String.valueOf(isAutoload()));
    }

    if (isAutorefresh() != null) {
      widget.setAutorefresh(String.valueOf(isAutorefresh()));
    }

    if (isVisible() != null) {
      widget.setVisible(String.valueOf(isVisible()));
    }

    if (getMax() != null) {
      widget.setMax(String.valueOf(getMax()));
    }

    if (getHelpImage() != null) {
      widget.setHelpImage(getHelpImage());
    }

    if (getStyle() != null) {
      widget.setStyle(getStyle());
    }

    if (getTargetAction() != null) {
      widget.setTargetAction(getTargetAction());
    }

    for (AweBuilder aweBuilder : elements) {
      addElement(widget, aweBuilder.build(widget));
    }

    for (WidgetParameterBuilder widgetParameterBuilder : widgetParameterList) {
      addElement(widget, widgetParameterBuilder.build(widget));
    }

    return widget;
  }

  /**
   * Get component
   *
   * @return
   */
  public WidgetComponent getComponent() {
    return component;
  }

  /**
   * Set component
   *
   * @param component
   * @return
   */
  public WidgetBuilder setComponent(WidgetComponent component) {
    this.component = component;
    return this;
  }

  /**
   * Get initial load
   *
   * @return
   */
  public InitialLoad getInitialLoad() {
    return initialLoad;
  }

  /**
   * Set initial load
   *
   * @param initialLoad
   * @return
   */
  public WidgetBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get server action
   *
   * @return
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Set server action
   *
   * @param serverAction
   * @return
   */
  public WidgetBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Is autoload
   *
   * @return
   */
  public Boolean isAutoload() {
    return autoload;
  }

  /**
   * Set autoload
   *
   * @param autoload
   * @return
   */
  public WidgetBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * Is autorefresh
   *
   * @return
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * Set autorefresh
   *
   * @param autorefresh
   * @return
   */
  public WidgetBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Is visible
   *
   * @return
   */
  public Boolean isVisible() {
    return visible;
  }

  /**
   * Set visible
   *
   * @param visible
   * @return
   */
  public WidgetBuilder setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  /**
   * Get max
   *
   * @return
   */
  public Integer getMax() {
    return max;
  }

  /**
   * Set max
   *
   * @param max
   * @return
   */
  public WidgetBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  public WidgetBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  public WidgetBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Get style
   *
   * @return
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  public WidgetBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get target action
   *
   * @return
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction
   * @return
   */
  public WidgetBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Add context button
   *
   * @param contextButtonBuilder
   * @return
   */
  public WidgetBuilder addContextButton(ContextButtonBuilder... contextButtonBuilder) {
    if (contextButtonBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(contextButtonBuilder));
    }
    return this;
  }

  /**
   * Add context separator
   *
   * @param contextSeparatorBuilder
   * @return
   */
  public WidgetBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparatorBuilder) {
    if (contextSeparatorBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(contextSeparatorBuilder));
    }
    return this;
  }

  /**
   * Add widget parameter
   *
   * @param widgetParameterBuilder
   * @return
   */
  public WidgetBuilder addWidgetParameter(WidgetParameterBuilder... widgetParameterBuilder) {
    if (widgetParameterBuilder != null) {
      if (this.widgetParameterList == null) {
        this.widgetParameterList = new ArrayList<>();
      }
      this.widgetParameterList.addAll(Arrays.asList(widgetParameterBuilder));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public WidgetBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return this.elements;
  }
}
