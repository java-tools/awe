package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.IconLoading;
import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.Component;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter(AccessLevel.PRIVATE)
@Setter
@Accessors(chain = true)
public class ComponentAttributes<B extends AbstractComponentBuilder> extends AbstractAttributes<B> {
  private InitialLoad initialLoad;
  private IconLoading iconLoading;
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

  public ComponentAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends Component> E asComponent(E element) {
    E component = (E) element
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

    if (getIconLoading() != null) {
      component.setIconLoading(getIconLoading().toString());
    }

    if (getServerAction() != null) {
      component.setServerAction(getServerAction().toString());
    }
    return component;
  }

  /**
   * Retrieve builder
   *
   * @return Builder
   */
  @Override
  public B builder() {
    return parent;
  }
}
