package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Resizable Class
 *
 * Used to parse a resizable tag with XStream
 *
 *
 * Generates a resizable structure
 *
 *
 * @author Pablo GARCIA - 05/MAY/2015
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("resizable")
public class Resizable extends Component {

  private static final long serialVersionUID = -185783597311876025L;
  // Resizable directions
  @XStreamAlias("directions")
  @XStreamAsAttribute
  private String directions;

  @Override
  public Resizable copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve component tag (to be overriden)
   *
   * @return get <code>resizable</code> tag
   */
  @Override
  public String getComponentTag() {
    return "resizable";
  }
}
