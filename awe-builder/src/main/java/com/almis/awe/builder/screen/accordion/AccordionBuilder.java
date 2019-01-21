/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.accordion;

import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.panelable.Accordion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class AccordionBuilder extends AweBuilder<AccordionBuilder> {

  private Boolean autocollapse;
  private String selected, style;
  private List<AweBuilder> elements;

  /**
   * Cosntructor
   *
   * @throws AWException
   */
  public AccordionBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    setAutocollapse(true);
    elements = new ArrayList<>();
  }

  @Override
  public AccordionBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Accordion accordion = new Accordion();

    accordion.setId(getId());

    if (isAutocollapse() != null) {
      accordion.setAutocollapse(String.valueOf(isAutocollapse()));
    }

    if (getSelected() != null) {
      accordion.setSelected(getSelected());
    }

    if (getStyle() != null) {
      accordion.setStyle(getStyle());
    }

    for (AweBuilder aweBuilder : elements) {
      addElement(accordion, aweBuilder.build(accordion));
    }

    return accordion;
  }

  /**
   * Is autocollapse
   *
   * @return
   */
  public Boolean isAutocollapse() {
    return autocollapse;
  }

  /**
   * Set autocollapse
   *
   * @param autocollapse
   * @return
   */
  public AccordionBuilder setAutocollapse(Boolean autocollapse) {
    this.autocollapse = autocollapse;
    return this;
  }

  /**
   * Get selected
   *
   * @return
   */
  public String getSelected() {
    return selected;
  }

  /**
   * Set selected
   *
   * @param selected
   * @return
   */
  public AccordionBuilder setSelected(String selected) {
    this.selected = selected;
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
  public AccordionBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Add accordion
   *
   * @param accordionItem
   * @return
   */
  public AccordionBuilder addAccordionItem(AccordionItemBuilder... accordionItem) {
    if (accordionItem != null) {
      this.elements.addAll(Arrays.asList(accordionItem));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public AccordionBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
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
