package com.almis.awe.builder.screen.accordion;

import com.almis.awe.builder.screen.base.AbstractTagBuilder;
import com.almis.awe.model.entities.screen.component.container.AccordionItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AccordionItemBuilder extends AbstractTagBuilder<AccordionItemBuilder, AccordionItem> {

  @Override
  public AccordionItem build() {
    return build(new AccordionItem());
  }
}
