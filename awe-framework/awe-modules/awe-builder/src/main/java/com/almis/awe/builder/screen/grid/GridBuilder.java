package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.IconLoading;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.model.entities.screen.component.grid.Grid;
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
public class GridBuilder extends AbstractCriteriaBuilder<GridBuilder, Grid> {

  private IconLoading iconLoading;
  private boolean checkMultiselect;
  private boolean editable;
  private boolean multiselect;
  private boolean paginationDisabled;
  private boolean rowNumbers;
  private boolean sendAll;
  private boolean sendOperations;
  private boolean showTotals;
  private boolean treeGrid;
  private boolean validateOnSave;
  private Integer initialLevel;
  private String expandColumn;
  private String iconCollapse;
  private String iconLeaf;
  private String pagerValues;
  private String treeId;
  private String treeLeaf;
  private String treeParent;

  @Override
  public Grid build() {
    return build(new Grid());
  }

  @Override
  public Grid build(Grid grid) {
    super.build(grid)
      .setCheckboxMultiselect(isCheckMultiselect())
      .setEditable(isEditable())
      .setMultiselect(isMultiselect())
      .setDisablePagination(isPaginationDisabled())
      .setRowNumbers(isRowNumbers())
      .setSendAll(isSendAll())
      .setMultioperation(isSendOperations())
      .setShowTotals(isShowTotals())
      .setTreegrid(isTreeGrid())
      .setValidateOnSave(isValidateOnSave())
      .setInitialLevel(getInitialLevel())
      .setExpandColumn(getExpandColumn())
      .setIconCollapse(getIconCollapse())
      .setIconLeaf(getIconLeaf())
      .setPagerValues(getPagerValues())
      .setTreeId(getTreeId())
      .setTreeLeaf(getTreeLeaf())
      .setTreeParent(getTreeParent());

    if (getIconLoading() != null) {
      grid.setIconLoading(getIconLoading().toString());
    }

    return grid;
  }

  /**
   * Add column
   *
   * @param column
   * @return
   */
  public GridBuilder addColumn(ColumnBuilder... column) {
    addAllElements(column);
    return this;
  }

  /**
   * Add button
   *
   * @param button
   * @return
   */
  public GridBuilder addButton(ButtonBuilder... button) {
    addAllElements(button);
    return this;
  }

  /**
   * Add group header
   *
   * @param groupHeader
   * @return
   */
  public GridBuilder addGroupHeader(GroupHeaderBuilder... groupHeader) {
    addAllElements(groupHeader);
    return this;
  }
}
