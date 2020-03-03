package com.almis.awe.model.entities.screen.component;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Info Class
 * <p>
 * Used to add an info element with XStream
 *
 * @author Pablo GARCIA - 04/JUN/2012
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("info")
public class Info extends AbstractButton {

  private static final long serialVersionUID = -2190505817555353007L;

  // Value from query
  @JsonProperty("text")
  @XStreamOmitField
  private String value;

  // Info unit
  @XStreamAlias("unit")
  @XStreamAsAttribute
  private String unit;

  // Info dropdown style
  @XStreamAlias("dropdown-style")
  @XStreamAsAttribute
  private String dropdownStyle;

  @Override
  public Info copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Get info dropdown children data
   *
   * @return Info dropdown children data
   */
  @JsonGetter("children")
  public Integer getChildren() {
    return getElementsByType(Component.class).size();
  }

  @Override
  @JsonIgnore
  public String getComponentTag() {
    return "info-dropdown";
  }
}
