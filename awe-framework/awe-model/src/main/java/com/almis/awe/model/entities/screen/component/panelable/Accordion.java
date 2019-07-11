package com.almis.awe.model.entities.screen.component.panelable;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Accordion Class
 *
 * Used to parse an accordion with XStream
 * Generates an accordion with collapsible elements
 *
 * @author Jorge BELLON - 16/02/2017
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@XStreamAlias("accordion")
public class Accordion extends Panelable {

  private static final long serialVersionUID = -6613949823525768993L;

  // Value attribute
  @XStreamAlias("selected")
  @XStreamAsAttribute
  private String selected;

  // Autocollapse attribute
  @XStreamAlias("autocollapse")
  @XStreamAsAttribute
  private Boolean autocollapse;

  @Override
  public Accordion copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns if is autocollapse
   * @return Is autocollapse
   */
  public boolean isAutocollapse() {
    return getAutocollapse() != null && getAutocollapse();
  }

  /**
   * Retrieves the list of items selected for JSON serialization
   *
   * @return itemsSelected
   */
  @JsonGetter("itemsSelected")
  public List<String> getItemsSelectedConverter() {
    ArrayList<String> list = new ArrayList<>();
    if (getSelected() != null) {
      list.add(getSelected());
    }
    return list;
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return <code>accordion</code> tag
   */
  @Override
  public String getComponentTag() {
    return "accordion";
  }
}
