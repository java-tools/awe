/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.container;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * AccordionItem Class
 *
 * Used to parse an accordion item with XStream
 *
 *
 * Generates an accordion item
 *
 *
 * @author Jorge BELLON - 16/02/2017
 */
@XStreamAlias("accordion-item")
public class AccordionItem extends Container {

  private static final long serialVersionUID = 8943795217481506399L;

  /**
   * Default constructor
   */
  public AccordionItem() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public AccordionItem(AccordionItem other) throws AWException {
    super(other);
  }

  @Override
  public AccordionItem copy() throws AWException {
    return new AccordionItem(this);
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
