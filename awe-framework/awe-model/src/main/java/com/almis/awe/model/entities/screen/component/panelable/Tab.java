package com.almis.awe.model.entities.screen.component.panelable;

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
 * Tab Class
 * Used to parse a tab with XStream
 * Generates an screen criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("tab")
public class Tab extends Panelable {

  private static final long serialVersionUID = 6498566666548618060L;
  // Tab can be maximized or not
  @XStreamAlias("maximize")
  @XStreamAsAttribute
  private Boolean maximize;

  @Override
  public Tab copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Check if tab is maximizable
   * @return Tab is maximizable
   */
  public boolean isMaximize() {
    return maximize != null && maximize;
  }

  /**
   * Retrieve component tag
   *
   * @return <code>tab</code> tag
   */
  @Override
  public String getComponentType() {
    return "tab";
  }
}
