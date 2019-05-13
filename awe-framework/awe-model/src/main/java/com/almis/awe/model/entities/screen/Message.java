package com.almis.awe.model.entities.screen;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
 * @author Pablo GARCIA, Pablo VIDAL - 11/JUN/2014
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("message")
public class Message extends Element {

  private static final long serialVersionUID = 5151876057457019598L;

  // Message description
  @JsonProperty("message")
  @XStreamAlias("message")
  @XStreamAsAttribute
  private String text;

  @Override
  public Message copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }
}
