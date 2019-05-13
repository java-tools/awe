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
 * Message Class
 *
 * Used to parse a message tag with XStream
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
@Accessors(chain = true)
@XStreamAlias("frame")
public class Frame extends Component {

  private static final long serialVersionUID = 2809318507420231428L;
  // Frame screen
  @XStreamAlias("screen")
  @XStreamAsAttribute
  private String screen;
  // Frame screen variable
  @XStreamAlias("screen-variable")
  @XStreamAsAttribute
  private String screenVariable;

  // Frame scroll
  @XStreamAlias("scroll")
  @XStreamAsAttribute
  private Boolean scroll;

  /**
   * Returns if is scroll
   * @return Is scroll
   */
  public boolean isScroll() {
    return scroll != null && scroll;
  }

  @Override
  public Frame copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }
}
