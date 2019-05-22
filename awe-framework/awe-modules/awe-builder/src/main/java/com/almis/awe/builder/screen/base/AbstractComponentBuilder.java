package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.model.entities.screen.component.Component;
import lombok.Getter;

/**
 *
 * @author dfuentes
 */
@Getter
public abstract class AbstractComponentBuilder<T, I extends Component> extends AbstractElementBuilder<T, I> {

  private InitialLoad initialLoad;
  private boolean loadAll;
  private ServerAction serverAction;
  private Integer max;
  private String icon;
  private String size;
  private String targetAction;
  private boolean autoload;
  private Integer autorefresh;
  private boolean visible;
  private String name;

  @Override
  public I build(I component) {
    super.build(component)
      .setName(getName())
      .setMax(getMax())
      .setIcon(getIcon())
      .setSize(getSize())
      .setTargetAction(getTargetAction())
      .setAutoload(isAutoload())
      .setAutorefresh(getAutorefresh())
      .setLoadAll(isLoadAll())
      .setVisible(isVisible());

    if (getInitialLoad() != null) {
      component.setInitialLoad(getInitialLoad().toString());
    }

    if (getServerAction() != null) {
      component.setServerAction(getServerAction().toString());
    }

    return component;
  }

  /**
   * Set the initialLoad flag
   * @param initialLoad initialLoad flag
   * @return This
   */
  public T setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return (T) this;
  }

  /**
   * Set the loadAll flag
   * @param loadAll loadAll flag
   * @return This
   */
  public T setLoadAll(boolean loadAll) {
    this.loadAll = loadAll;
    return (T) this;
  }

  /**
   * Set the server action
   * @param serverAction server action
   * @return This
   */
  public T setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return (T) this;
  }

  /**
   * Set the max
   * @param max max
   * @return This
   */
  public T setMax(Integer max) {
    this.max = max;
    return (T) this;
  }

  /**
   * Set the icon
   * @param icon icon
   * @return This
   */
  public T setIcon(String icon) {
    this.icon = icon;
    return (T) this;
  }

  /**
   * Set the size
   * @param size size
   * @return This
   */
  public T setSize(String size) {
    this.size = size;
    return (T) this;
  }

  /**
   * Set the target action
   * @param targetAction target action
   * @return This
   */
  public T setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return (T) this;
  }

  /**
   * Set the autoload flag
   * @param autoload autoload flag
   * @return This
   */
  public T setAutoload(boolean autoload) {
    this.autoload = autoload;
    return (T) this;
  }

  /**
   * Set the autorefresh time
   * @param autorefresh autorefresh time in seconds
   * @return This
   */
  public T setAutorefresh(Integer autorefresh) {
    this.autorefresh = autorefresh;
    return (T) this;
  }

  /**
   * Set the visible flag
   * @param visible visible flag
   * @return This
   */
  public T setVisible(boolean visible) {
    this.visible = visible;
    return (T) this;
  }

  /**
   * Set the name
   * @param name name
   * @return This
   */
  public T setName(String name) {
    this.name = name;
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
