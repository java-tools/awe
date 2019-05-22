package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Include Class
 *
 * Used to include another screen target with XStream
 *
 *
 * Loads another target from another screen
 *
 *
 * @author Pablo GARCIA - 01/NOV/2011
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("include")
public class Include extends Element {

  private static final long serialVersionUID = 9060558303231661651L;

  // Menu which belongs the screen
  @XStreamAlias("target-screen")
  @XStreamAsAttribute
  private String targetScreen;

  // Initial target action (to load data from a query)
  @XStreamAlias("target-source")
  @XStreamAsAttribute
  private String targetSource;

  @Override
  public Include copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_CHILDREN;
  }
}
