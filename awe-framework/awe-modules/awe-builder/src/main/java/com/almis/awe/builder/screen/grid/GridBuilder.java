package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.screen.base.AbstractColumnBuilder;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.component.GridAttributes;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
public class GridBuilder extends AbstractComponentBuilder<GridBuilder, Grid> {

  private GridAttributes gridAttributes;

  public GridBuilder() {
    super();
    this.gridAttributes = new GridAttributes(this);
  }

  @Override
  public Grid build() {
    return build(new Grid());
  }

  @Override
  public Grid build(Grid grid) {
    getGridAttributes().asGrid(grid);
    return super.build(grid);
  }

  /**
   * Set check multiselect
   *
   * @param checkMultiselect
   * @return
   */
  public GridBuilder setCheckMultiselect(boolean checkMultiselect) {
    getGridAttributes().setCheckMultiselect(checkMultiselect);
    return this;
  }

  /**
   * Set editable
   *
   * @param editable
   * @return
   */
  public GridBuilder setEditable(boolean editable) {
    getGridAttributes().setEditable(editable);
    return this;
  }

  /**
   * Set multiselect
   *
   * @param multiselect
   * @return
   */
  public GridBuilder setMultiselect(boolean multiselect) {
    getGridAttributes().setMultiselect(multiselect);
    return this;
  }

  /**
   * Set pagination disabled
   *
   * @param paginationDisabled
   * @return
   */
  public GridBuilder setPaginationDisabled(boolean paginationDisabled) {
    getGridAttributes().setPaginationDisabled(paginationDisabled);
    return this;
  }

  /**
   * Set row numbers
   *
   * @param rowNumbers
   * @return
   */
  public GridBuilder setRowNumbers(boolean rowNumbers) {
    getGridAttributes().setRowNumbers(rowNumbers);
    return this;
  }

  /**
   * Set send all
   *
   * @param sendAll
   * @return
   */
  public GridBuilder setSendAll(boolean sendAll) {
    getGridAttributes().setSendAll(sendAll);
    return this;
  }

  /**
   * Set send operations
   *
   * @param sendOperations
   * @return
   */
  public GridBuilder setSendOperations(boolean sendOperations) {
    getGridAttributes().setSendOperations(sendOperations);
    return this;
  }

  /**
   * Set show totals
   *
   * @param showTotals
   * @return
   */
  public GridBuilder setShowTotals(boolean showTotals) {
    getGridAttributes().setShowTotals(showTotals);
    return this;
  }

  /**
   * Set tree grid
   *
   * @param treeGrid
   * @return
   */
  public GridBuilder setTreeGrid(boolean treeGrid) {
    getGridAttributes().setTreeGrid(treeGrid);
    return this;
  }

  /**
   * Set validate on save
   *
   * @param validateOnSave
   * @return
   */
  public GridBuilder setValidateOnSave(boolean validateOnSave) {
    getGridAttributes().setValidateOnSave(validateOnSave);
    return this;
  }

  /**
   * Set initial level
   *
   * @param level
   * @return
   */
  public GridBuilder setInitialLevel(Integer level) {
    getGridAttributes().setInitialLevel(level);
    return this;
  }

  /**
   * Set expand column
   *
   * @param column
   * @return
   */
  public GridBuilder setExpandColumn(String column) {
    getGridAttributes().setExpandColumn(column);
    return this;
  }

  /**
   * Set collapse icon
   *
   * @param icon
   * @return
   */
  public GridBuilder setIconCollapse(String icon) {
    getGridAttributes().setIconCollapse(icon);
    return this;
  }

  /**
   * Set leaf icon
   *
   * @param icon
   * @return
   */
  public GridBuilder setIconLeaf(String icon) {
    getGridAttributes().setIconLeaf(icon);
    return this;
  }

  /**
   * Set pager values
   *
   * @param pagerValues
   * @return
   */
  public GridBuilder setPagerValues(String pagerValues) {
    getGridAttributes().setPagerValues(pagerValues);
    return this;
  }

  /**
   * Set tree id
   *
   * @param id
   * @return
   */
  public GridBuilder setTreeId(String id) {
    getGridAttributes().setTreeId(id);
    return this;
  }

  /**
   * Set tree leaf
   *
   * @param treeLeaf
   * @return
   */
  public GridBuilder setTreeLeaf(String treeLeaf) {
    getGridAttributes().setTreeLeaf(treeLeaf);
    return this;
  }

  /**
   * Set tree parent
   *
   * @param parent
   * @return
   */
  public GridBuilder setTreeParent(String parent) {
    getGridAttributes().setTreeParent(parent);
    return this;
  }

  /**
   * Add column
   *
   * @param columnBuilders
   * @return
   */
  public GridBuilder addColumn(AbstractColumnBuilder... columnBuilders) {
    addAllElements(columnBuilders);
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
