package com.almis.awe.model.entities.screen.component.container;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

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
@XStreamAlias("tabcontainer")
public class TabContainer extends Container {

  private static final long serialVersionUID = 485344960889412732L;

  /**
   * Default constructor
   */
  public TabContainer() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public TabContainer(TabContainer other) throws AWException {
    super(other);
  }

  @Override
  public TabContainer copy() throws AWException {
    return new TabContainer(this);
  }

  @Override
  public String getComponentTag() {
    return "tabcontainer";
  }
}
