package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.grid.GridBuilder;
import com.almis.awe.model.entities.screen.component.grid.AbstractGrid;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class GridAttributes<B extends GridBuilder> extends AbstractAttributes<B> {
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

  public GridAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractGrid> E asGrid(E element) {
    return (E) element
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
  }

  /**
   * Retrieve builder
   *
   * @return Builder
   */
  @Override
  public B builder() {
    return parent;
  }
}
