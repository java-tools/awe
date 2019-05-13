package com.almis.awe.model.entities.screen.component.button;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Button Class
 *
 * Used to parse the Button Component in a screen with XStream
 *
 *
 * Launches contained Button Actions when it is clicked
 *
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("button")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Button extends AbstractButton {

  private static final long serialVersionUID = 4537762712903982049L;

  @Override
  public Button copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }
}
