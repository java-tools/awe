package com.almis.awe.builder.screen.accordion;

import com.almis.awe.builder.screen.base.AbstractElementBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.model.entities.screen.component.panelable.Accordion;
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
public class AccordionBuilder extends AbstractElementBuilder<AccordionBuilder, Accordion> {

  private boolean autocollapse = true;
  private String selected;

  @Override
  public Accordion build() {
    return build(new Accordion());
  }

  @Override
  public Accordion build(Accordion accordion) {
    super.build(accordion)
      .setAutocollapse(isAutocollapse())
      .setSelected(getSelected());

    return accordion;
  }

  /**
   * Add accordion
   *
   * @param accordionItem
   * @return
   */
  public AccordionBuilder addAccordionItem(AccordionItemBuilder... accordionItem) {
    addAllElements(accordionItem);
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public AccordionBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    addAllElements(dependencyBuilder);
    return this;
  }
}
