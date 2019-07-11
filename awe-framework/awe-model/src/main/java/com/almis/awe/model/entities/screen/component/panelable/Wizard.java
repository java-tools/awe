package com.almis.awe.model.entities.screen.component.panelable;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Wizard Class
 *
 * Used to parse a wizard with XStream
 *
 *
 * Generates an screen wizard
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamAlias("wizard")
public class Wizard extends Panelable {

  private static final long serialVersionUID = -6523633454585300507L;

  @Override
  public Wizard copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve component tag
   *
   * @return <code>wizard</code> tag
   */
  @Override
  public String getComponentType() {
    return "wizard";
  }
}
