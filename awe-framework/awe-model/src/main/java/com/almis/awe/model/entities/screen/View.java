package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
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
@XStreamAlias("view")
public class View extends Element {

  private static final long serialVersionUID = 5250926094818702640L;
  // View name (identifier)
  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  @Override
  public View copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieve element template
   *
   * @return Element template
   */
  @Override
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_VIEW;
  }
}
