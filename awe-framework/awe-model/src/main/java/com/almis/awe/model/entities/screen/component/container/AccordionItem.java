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
 * AccordionItem Class
 *
 * Used to parse an accordion item with XStream
 * Generates an accordion item
 *
 * @author Jorge BELLON - 16/02/2017
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("accordion-item")
public class AccordionItem extends Container {

  @Override
  public AccordionItem copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "accordion-item";
  }
}
