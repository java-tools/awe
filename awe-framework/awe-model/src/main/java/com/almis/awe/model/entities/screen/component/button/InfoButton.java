package com.almis.awe.model.entities.screen.component.button;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * InfoButton Class
 *
 * Used to add an info with info button element with XStream
 *
 *
 * @author Pablo Vidal - 04/JUN/2015
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("info-button")
public class InfoButton extends AbstractButton {

  private static final long serialVersionUID = -8521012517719249256L;
  // Info style
  @XStreamAlias("info-style")
  @XStreamAsAttribute
  private String infoStyle;

  @Override
  public InfoButton copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Retrieves value for JSON serialization
   *
   * @return value
   */
  @JsonGetter("text")
  public String getValueConverter() {
    return this.getValue();
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "info-button";
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_EMPTY;
  }
}
