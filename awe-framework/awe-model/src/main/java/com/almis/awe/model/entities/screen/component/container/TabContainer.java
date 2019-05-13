package com.almis.awe.model.entities.screen.component.container;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * TabContainer Class
 *
 * Used to parse a tab with XStream
 *
 *
 * Generates an screen criteria
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("tabcontainer")
public class TabContainer extends Container {

  private static final long serialVersionUID = 485344960889412732L;

  @Override
  public TabContainer copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  @Override
  public String getComponentTag() {
    return "tabcontainer";
  }
}
