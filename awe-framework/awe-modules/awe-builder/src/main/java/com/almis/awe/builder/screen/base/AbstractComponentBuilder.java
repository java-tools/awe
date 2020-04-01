package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.enumerates.IconLoading;
import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.component.ComponentAttributes;
import com.almis.awe.builder.screen.component.ElementAttributes;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.entities.screen.component.Component;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
public abstract class AbstractComponentBuilder<T, I extends Component> extends AweBuilder<T, I> {

  private ElementAttributes elementAttributes;
  private ComponentAttributes componentAttributes;

  public AbstractComponentBuilder() {
    this.elementAttributes = new ElementAttributes(this);
    this.componentAttributes = new ComponentAttributes(this);
  }

  @Override
  public I build(I component) {
    component = super.build(component);
    getElementAttributes().asElement(component);
    getComponentAttributes().asComponent(component);
    return component;
  }

  /**
   * Set label
   *
   * @param label Label
   * @return Builder
   */
  public T setLabel(String label) {
    getElementAttributes().setLabel(label);
    return (T) this;
  }

  /**
   * Set title
   *
   * @param title Title
   * @return Builder
   */
  public T setTitle(String title) {
    getElementAttributes().setTitle(title);
    return (T) this;
  }

  /**
   * Set style (classes)
   *
   * @param style Style
   * @return Builder
   */
  public T setStyle(String style) {
    getElementAttributes().setStyle(style);
    return (T) this;
  }

  /**
   * Set type
   *
   * @param type Type
   * @return Builder
   */
  public T setType(String type) {
    getElementAttributes().setType(type);
    return (T) this;
  }

  /**
   * Set help label
   *
   * @param help Help label
   * @return Builder
   */
  public T setHelp(String help) {
    getElementAttributes().setHelp(help);
    return (T) this;
  }

  /**
   * Set help image
   *
   * @param helpImage Help image
   * @return Builder
   */
  public T setHelpImage(String helpImage) {
    getElementAttributes().setHelpImage(helpImage);
    return (T) this;
  }

  /**
   * Set expandible
   *
   * @param expandible Expandible
   * @return Builder
   */
  public T setExpandible(Expandible expandible) {
    getElementAttributes().setExpandible(expandible);
    return (T) this;
  }


  /**
   * Set autoload
   *
   * @param autoload Is autoload
   * @return Builder
   */
  public T setAutoload(boolean autoload) {
    getComponentAttributes().setAutoload(autoload);
    return (T) this;
  }

  /**
   * Set autorefresh time in seconds
   *
   * @param autorefresh Autorefresh time in seconds
   * @return Builder
   */
  public T setAutorefresh(Integer autorefresh) {
    getComponentAttributes().setAutorefresh(autorefresh);
    return (T) this;
  }

  /**
   * Set icon
   *
   * @param icon Icon
   * @return Builder
   */
  public T setIcon(String icon) {
    getComponentAttributes().setIcon(icon);
    return (T) this;
  }

  /**
   * Set initial load
   *
   * @param initialLoad Initial load
   * @return Builder
   */
  public T setInitialLoad(InitialLoad initialLoad) {
    getComponentAttributes().setInitialLoad(initialLoad);
    return (T) this;
  }

  /**
   * Set load all
   *
   * @param loadAll Load all
   * @return Builder
   */
  public T setLoadAll(boolean loadAll) {
    getComponentAttributes().setLoadAll(loadAll);
    return (T) this;
  }

  /**
   * Set max rows per page
   *
   * @param max max rows per page
   * @return Builder
   */
  public T setMax(Integer max) {
    getComponentAttributes().setMax(max);
    return (T) this;
  }

  /**
   * Set component name
   *
   * @param name component name
   * @return Builder
   */
  public T setName(String name) {
    getComponentAttributes().setName(name);
    return (T) this;
  }

  /**
   * Set component size
   *
   * @param size component size
   * @return Builder
   */
  public T setSize(String size) {
    getComponentAttributes().setSize(size);
    return (T) this;
  }

  /**
   * Set server action
   *
   * @param serverAction server action
   * @return Builder
   */
  public T setServerAction(ServerAction serverAction) {
    getComponentAttributes().setServerAction(serverAction);
    return (T) this;
  }

  /**
   * Set target action
   *
   * @param targetAction target action
   * @return Builder
   */
  public T setTargetAction(String targetAction) {
    getComponentAttributes().setTargetAction(targetAction);
    return (T) this;
  }

  /**
   * Set visible
   *
   * @param visible Visible
   * @return Builder
   */
  public T setVisible(boolean visible) {
    getComponentAttributes().setVisible(visible);
    return (T) this;
  }

  /**
   * Set loading icon
   *
   * @param icon
   * @return
   */
  public T setIconLoading(IconLoading icon) {
    getComponentAttributes().setIconLoading(icon);
    return (T) this;
  }

  /**
   * Set data
   *
   * @param data
   * @return
   */
  public T setData(DataList data) {
    getComponentAttributes().setData(data);
    return (T) this;
  }

  /**
   * Add context button
   *
   * @param contextButtons
   * @return
   */
  public T addContextButton(AbstractButtonBuilder... contextButtons) {
    addAllElements(contextButtons);
    return (T) this;
  }

  /**
   * Add context separator
   *
   * @param contextSeparators
   * @return
   */
  public T addContextSeparator(ContextSeparatorBuilder... contextSeparators) {
    addAllElements(contextSeparators);
    return (T) this;
  }

  /**
   * Add dependencies
   *
   * @param dependencyBuilder
   * @return
   */
  public T addDependency(DependencyBuilder... dependencyBuilder) {
    addAllElements(dependencyBuilder);
    return (T) this;
  }
}
