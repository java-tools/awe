/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Menu;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/*
 * File Imports
 */

/**
 * MenuContainer Class
 *
 * Used to parse a menu tag with XStream
 *
 *
 * Generates a piece of code with literals that can be used to retrieve confirmation title and descriptions
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("menu-container")
public class MenuContainer extends Component {

  private static final long serialVersionUID = 8200951388289707350L;
  @XStreamOmitField
  private Menu menu;

  /**
   * Default constructor
   */
  public MenuContainer() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public MenuContainer(MenuContainer other) throws AWException {
    super(other);
  }

  @Override
  public MenuContainer copy() throws AWException {
    return new MenuContainer(this);
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "menu";
  }

  /**
   * Retrieve Option list
   *
   * @return Option list
   */
  @JsonGetter("options")
  public List<Element> getOptions() {
    return menu.getElementList();
  }

  /**
   * Get menu
   *
   * @return Menu
   */
  @JsonIgnore
  public Menu getMenu() {
    return menu;
  }

  /**
   * Set menu
   *
   * @param menu Menu
   * @return this
   */
  public MenuContainer setMenu(Menu menu) {
    this.menu = menu;
    return this;
  }
}
