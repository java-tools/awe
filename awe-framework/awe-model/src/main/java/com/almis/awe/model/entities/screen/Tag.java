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
 * Tag Class
 * Used to parse screen actions with XStream
 * Default HTML tag
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("tag")
public class Tag extends Element {

  private static final long serialVersionUID = 9180792194108625980L;

  // Tag text
  @XStreamAlias("text")
  private String value;

  // Source where generated HTML code is going to be stored
  @XStreamAlias("source")
  @XStreamAsAttribute
  private String source;

  @Override
  public Tag copy() throws AWException {
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
    return AweConstants.TEMPLATE_TAG;
  }
}
