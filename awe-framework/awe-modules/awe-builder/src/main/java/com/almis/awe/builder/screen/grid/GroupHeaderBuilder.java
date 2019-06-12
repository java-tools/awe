package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;
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
public class GroupHeaderBuilder extends AweBuilder<GroupHeaderBuilder, GroupHeader> {

  private String label;

  @Override
  public GroupHeader build() {
    return build(new GroupHeader());
  }

  @Override
  public GroupHeader build(GroupHeader groupHeader) {
    return (GroupHeader) super.build(groupHeader)
      .setLabel(getLabel());
  }

  /**
   * Add column list
   *
   * @param columnBuilders
   * @return
   */
  public GroupHeaderBuilder addColumnList(AbstractColumnBuilder... columnBuilders) {
    addAllElements(columnBuilders);
    return this;
  }
}
