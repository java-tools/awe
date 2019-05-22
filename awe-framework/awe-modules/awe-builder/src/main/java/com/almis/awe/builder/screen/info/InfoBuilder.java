package com.almis.awe.builder.screen.info;

import com.almis.awe.builder.screen.TagBuilder;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.Info;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class InfoBuilder extends AbstractComponentBuilder<InfoBuilder, Info> {

  private String dropdownStyle;
  private String property;
  private String session;
  private String title;
  private String unit;
  private String value;

  @Override
  public Info build() {
    return build(new Info());
  }

  @Override
  public Info build(Info info) {
    super.build(info)
      .setDropdownStyle(getDropdownStyle())
      .setUnit(getUnit())
      .setValue(getValue());

    return info;
  }

  /**
   * Add info
   *
   * @param info
   * @return
   */
  public InfoBuilder addInfo(InfoBuilder... info) {
    addAllElements(info);
    return this;
  }

  /**
   * Add info button
   *
   * @param infoButton
   * @return
   */
  public InfoBuilder addInfoButton(InfoButtonBuilder... infoButton) {
    addAllElements(infoButton);
    return this;
  }

  /**
   * Add info criteria
   *
   * @param infoCriteria
   * @return
   */
  public InfoBuilder addInfoCriteria(InfoCriteriaBuilder... infoCriteria) {
    addAllElements(infoCriteria);
    return this;
  }

  /**
   * Add tag
   *
   * @param tag
   * @return
   */
  public InfoBuilder addTag(TagBuilder... tag) {
    addAllElements(tag);
    return this;
  }
}
