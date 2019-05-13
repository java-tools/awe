package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("menu-container")
public class MenuContainer extends Component {

  private static final long serialVersionUID = 8200951388289707350L;

  @JsonIgnore
  @XStreamOmitField
  private Menu menu;

  @Override
  public MenuContainer copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
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
}
