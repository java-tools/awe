/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.panelable;

import com.almis.awe.exception.AWException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Accordion Class
 *
 * Used to parse an accordion with XStream
 *
 *
 * Generates an accordion with collapsable elements
 *
 *
 * @author Jorge BELLON - 16/02/2017
 */
@XStreamAlias("accordion")
public class Accordion extends Panelable {

  private static final long serialVersionUID = -6613949823525768993L;

  // Value attribute
  @XStreamAlias("selected")
  @XStreamAsAttribute
  private String selected = null;

  // Autocollapse attribute
  @XStreamAlias("autocollapse")
  @XStreamAsAttribute
  private String autocollapse = null;

  /**
   * Default constructor
   */
  public Accordion() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Accordion(Accordion other) throws AWException {
    super(other);
    this.selected = other.selected;
    this.autocollapse = other.autocollapse;
  }

  @Override
  public Accordion copy() throws AWException {
    return new Accordion(this);
  }

  /**
   * Retrieves the item's ID desired to be expanded at the beggining (if any)
   *
   * @return Item's ID
   */
  @JsonIgnore
  public String getSelected() {
    return selected;
  }

  /**
   * Set a new value for the item to be expanded initially
   *
   * @param selected
   */
  public void setSelected(String selected) {
    this.selected = selected;
  }

  /**
   * Retrieves the autocollapse value
   *
   * @return autocollapse
   */
  @JsonIgnore
  public String getAutocollapse() {
    return autocollapse;
  }

  /**
   * Retrieves the list of items selected for JSON serialization
   *
   * @return itemsSelected
   */
  @JsonGetter("itemsSelected")
  public List<String> getItemsSelectedConverter() {
    ArrayList<String> list = new ArrayList<>();
    if (this.getSelected() != null) {
      list.add(this.getSelected());
    }
    return list;
  }

  /**
   * Retrieves the autocollapse value as a boolean
   *
   * @return autocollapse
   */
  @JsonGetter("autocollapse")
  public boolean isAutocollapse() {
    if (autocollapse == null) {
      return true;
    }
    return "true".equals(autocollapse);
  }

  /**
   * Sets a new value for the autocollapse attribute
   *
   * @param autocollapse
   */
  public void setAutocollapse(String autocollapse) {
    this.autocollapse = autocollapse;
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return
   */
  @Override
  public String getComponentTag() {
    return "accordion";
  }
}
