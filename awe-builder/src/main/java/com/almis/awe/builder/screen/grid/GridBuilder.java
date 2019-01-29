/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.IconLoading;
import com.almis.awe.builder.enumerates.InitialLoad;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.button.ButtonBuilder;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.grid.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class GridBuilder extends AweBuilder<GridBuilder> {

  private IconLoading iconLoading;
  private InitialLoad initialLoad;
  private ServerAction serverAction;
  private Boolean autoload;
  private Boolean autorefresh;
  private Boolean checkMultiselect;
  private Boolean editable;
  private Boolean loadAll;
  private Boolean multiselect;
  private Boolean paginationDisabled;
  private Boolean rowNumbers;
  private Boolean sendAll;
  private Boolean sendOperations;
  private Boolean showTotals;
  private Boolean treeGrid;
  private Boolean validateOnSave;
  private Boolean visible;
  private Integer initialLevel;
  private Integer max;
  private String help;
  private String helpImage;
  private String expandColumn;
  private String iconCollapse;
  private String iconLeaf;
  private String name;
  private String pagerValues;
  private String style;
  private String targetAction;
  private String treeId;
  private String treeLeaf;
  private String treeParent;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public GridBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();

  }

  @Override
  public GridBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Grid grid = new Grid();
    grid.setId(getId());
    grid.setAutoload(getValueAsString(isAutoload()));
    grid.setAutorefresh(getValueAsString(isAutorefresh()));
    grid.setCheckboxMultiselect(getValueAsString(isCheckMultiselect()));
    grid.setEditable(getValueAsString(isEditable()));
    grid.setLoadAll(getValueAsString(isLoadAll()));
    grid.setMultiselect(getValueAsString(isMultiselect()));
    grid.setDisablePagination(getValueAsString(isPaginationDisabled()));
    grid.setRowNumbers(getValueAsString(isRowNumbers()));
    grid.setSendAll(getValueAsString(isSendAll()));
    grid.setSendOperations(getValueAsString(isSendOperations()));
    grid.setShowTotals(getValueAsString(isShowTotals()));
    grid.setTreegrid(getValueAsString(isTreeGrid()));
    grid.setValidateOnSave(getValueAsString(isValidateOnSave()));
    grid.setVisible(getValueAsString(isVisible()));

    grid.setInitialLevel(getValueAsString(getInitialLevel()));
    grid.setMax(getValueAsString(getMax()));

    grid.setHelp(getHelp());
    grid.setHelpImage(getHelpImage());
    grid.setExpandColumn(getExpandColumn());
    grid.setIconCollapse(getIconCollapse());
    grid.setIconLeaf(getIconLeaf());
    grid.setName(getName());
    grid.setPagerValues(getPagerValues());
    grid.setStyle(getStyle());
    grid.setTargetAction(getTargetAction());
    grid.setTreeId(getTreeId());
    grid.setTreeLeaf(getTreeLeaf());
    grid.setTreeParent(getTreeParent());

    if (getIconLoading() != null) {
      grid.setIconLoading(getIconLoading().toString());
    }

    if (getInitialLoad() != null) {
      grid.setInitialLoad(getInitialLoad().toString());
    }

    if (getServerAction() != null) {
      grid.setServerAction(getServerAction().toString());
    }

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(grid, aweBuilder.build(grid));
    }

    return grid;
  }

  /**
   * Get icon loading
   *
   * @return
   */
  public IconLoading getIconLoading() {
    return iconLoading;
  }

  /**
   * Set icon loading
   *
   * @param iconLoading
   * @return
   */
  public GridBuilder setIconLoading(IconLoading iconLoading) {
    this.iconLoading = iconLoading;
    return this;
  }

  /**
   * Get inicital load
   *
   * @return
   */
  public InitialLoad getInitialLoad() {
    return initialLoad;
  }

  /**
   * Set initial load
   *
   * @param initialLoad
   * @return
   */
  public GridBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get server action
   *
   * @return
   */
  public ServerAction getServerAction() {
    return serverAction;
  }

  /**
   * Ser server action
   *
   * @param serverAction
   * @return
   */
  public GridBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Is autoload
   *
   * @return
   */
  public Boolean isAutoload() {
    return autoload;
  }

  /**
   * Set autoload
   *
   * @param autoload
   * @return
   */
  public GridBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * Is autorefresh
   *
   * @return
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * Set autorefresh
   *
   * @param autorefresh
   * @return
   */
  public GridBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Is check multiselect
   *
   * @return
   */
  public Boolean isCheckMultiselect() {
    return checkMultiselect;
  }

  /**
   * Set check multiselect
   *
   * @param checkMultiselect
   * @return
   */
  public GridBuilder setCheckMultiselect(Boolean checkMultiselect) {
    this.checkMultiselect = checkMultiselect;
    return this;
  }

  /**
   * Is editable
   *
   * @return
   */
  public Boolean isEditable() {
    return editable;
  }

  /**
   * Set editable
   *
   * @param editable
   * @return
   */
  public GridBuilder setEditable(Boolean editable) {
    this.editable = editable;
    return this;
  }

  /**
   * Is load all
   *
   * @return
   */
  public Boolean isLoadAll() {
    return loadAll;
  }

  /**
   * Set load all
   *
   * @param loadAll
   * @return
   */
  public GridBuilder setLoadAll(Boolean loadAll) {
    this.loadAll = loadAll;
    return this;
  }

  /**
   * Is multiselect
   *
   * @return
   */
  public Boolean isMultiselect() {
    return multiselect;
  }

  /**
   * Set multiselect
   *
   * @param multiselect
   * @return
   */
  public GridBuilder setMultiselect(Boolean multiselect) {
    this.multiselect = multiselect;
    return this;
  }

  /**
   * Is pagination disabled
   *
   * @return
   */
  public Boolean isPaginationDisabled() {
    return paginationDisabled;
  }

  /**
   * Set pagination disabled
   *
   * @param paginationDisabled
   * @return
   */
  public GridBuilder setPaginationDisabled(Boolean paginationDisabled) {
    this.paginationDisabled = paginationDisabled;
    return this;
  }

  /**
   * Is row numbers
   *
   * @return
   */
  public Boolean isRowNumbers() {
    return rowNumbers;
  }

  /**
   * Set row numbers
   *
   * @param rowNumbers
   * @return
   */
  public GridBuilder setRowNumbers(Boolean rowNumbers) {
    this.rowNumbers = rowNumbers;
    return this;
  }

  /**
   * Is send all
   *
   * @return
   */
  public Boolean isSendAll() {
    return sendAll;
  }

  /**
   * Set send all
   *
   * @param sendAll
   * @return
   */
  public GridBuilder setSendAll(Boolean sendAll) {
    this.sendAll = sendAll;
    return this;
  }

  /**
   * Is send operations
   *
   * @return
   */
  public Boolean isSendOperations() {
    return sendOperations;
  }

  /**
   * Set send operations
   *
   * @param sendOperations
   * @return
   */
  public GridBuilder setSendOperations(Boolean sendOperations) {
    this.sendOperations = sendOperations;
    return this;
  }

  /**
   * Is show totals
   *
   * @return
   */
  public Boolean isShowTotals() {
    return showTotals;
  }

  /**
   * Set show totals
   *
   * @param showTotals
   * @return
   */
  public GridBuilder setShowTotals(Boolean showTotals) {
    this.showTotals = showTotals;
    return this;
  }

  /**
   * Is tree grid
   *
   * @return
   */
  public Boolean isTreeGrid() {
    return treeGrid;
  }

  /**
   * Set tree grid
   *
   * @param treeGrid
   * @return
   */
  public GridBuilder setTreeGrid(Boolean treeGrid) {
    this.treeGrid = treeGrid;
    return this;
  }

  /**
   * Is validate on save
   *
   * @return
   */
  public Boolean isValidateOnSave() {
    return validateOnSave;
  }

  /**
   * Set validate on save
   *
   * @param validateOnSave
   * @return
   */
  public GridBuilder setValidateOnSave(Boolean validateOnSave) {
    this.validateOnSave = validateOnSave;
    return this;
  }

  /**
   * Is visible
   *
   * @return
   */
  public Boolean isVisible() {
    return visible;
  }

  /**
   * Set visible
   *
   * @param visible
   * @return
   */
  public GridBuilder setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  /**
   * Get initial level
   *
   * @return
   */
  public Integer getInitialLevel() {
    return initialLevel;
  }

  /**
   * Set initial level
   *
   * @param initialLevel
   * @return
   */
  public GridBuilder setInitialLevel(Integer initialLevel) {
    this.initialLevel = initialLevel;
    return this;
  }

  /**
   * Get max
   * @return
   */
  public Integer getMax() {
    return max;
  }

  /**
   * Set max
   *
   * @param max
   * @return
   */
  public GridBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get help
   *
   * @return
   */
  public String getHelp() {
    return help;
  }

  /**
   * Set help
   *
   * @param help
   * @return
   */
  public GridBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /**
   * Get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Set help image
   *
   * @param helpImage
   * @return
   */
  public GridBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Get expand column
   *
   * @return
   */
  public String getExpandColumn() {
    return expandColumn;
  }

  /**
   * Set expand column
   *
   * @param expandColumn
   * @return
   */
  public GridBuilder setExpandColumn(String expandColumn) {
    this.expandColumn = expandColumn;
    return this;
  }

  /**
   * Get icon collapse
   *
   * @return
   */
  public String getIconCollapse() {
    return iconCollapse;
  }

  /**
   * Set icon collapse
   *
   * @param iconCollapse
   * @return
   */
  public GridBuilder setIconCollapse(String iconCollapse) {
    this.iconCollapse = iconCollapse;
    return this;
  }

  /**
   * Get icon leaf
   *
   * @return
   */
  public String getIconLeaf() {
    return iconLeaf;
  }

  /**
   * Get icon leaf
   *
   * @param iconLeaf
   * @return
   */
  public GridBuilder setIconLeaf(String iconLeaf) {
    this.iconLeaf = iconLeaf;
    return this;
  }

  /**
   * Get name
   *
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   *
   * @param name
   * @return
   */
  public GridBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get pager values
   *
   * @return
   */
  public String getPagerValues() {
    return pagerValues;
  }

  /**
   * Set pager values
   *
   * @param pagerValues
   * @return
   */
  public GridBuilder setPagerValues(String pagerValues) {
    this.pagerValues = pagerValues;
    return this;
  }

  /**
   * Get Style
   *
   * @return
   */
  public String getStyle() {
    return style;
  }

  /**
   * Set style
   *
   * @param style
   * @return
   */
  public GridBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get target action
   *
   * @return
   */
  public String getTargetAction() {
    return targetAction;
  }

  /**
   * Set target action
   *
   * @param targetAction
   * @return
   */
  public GridBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get tree id
   *
   * @return
   */
  public String getTreeId() {
    return treeId;
  }

  /**
   * Set tree id
   *
   * @param treeId
   * @return
   */
  public GridBuilder setTreeId(String treeId) {
    this.treeId = treeId;
    return this;
  }

  /**
   * Get tree leaf
   *
   * @return
   */
  public String getTreeLeaf() {
    return treeLeaf;
  }

  /**
   * Set tree leaf
   *
   * @param treeLeaf
   * @return
   */
  public GridBuilder setTreeLeaf(String treeLeaf) {
    this.treeLeaf = treeLeaf;
    return this;
  }

  /**
   * Get tree parent
   *
   * @return
   */
  public String getTreeParent() {
    return treeParent;
  }

  /**
   * Set tree parent
   *
   * @param treeParent
   * @return
   */
  public GridBuilder setTreeParent(String treeParent) {
    this.treeParent = treeParent;
    return this;
  }

  /**
   * Add column
   *
   * @param column
   * @return
   */
  public GridBuilder addColumn(ColumnBuilder... column) {
    if(column != null) {
      this.elements.addAll(Arrays.asList(column));
    }
    return this;
  }

  /**
   * Add button
   *
   * @param button
   * @return
   */
  public GridBuilder addButton(ButtonBuilder... button) {
    if(button != null) {
      this.elements.addAll(Arrays.asList(button));
    }
    return this;
  }

  /**
   * add context button
   * @param contextButton
   * @return
   */
  public GridBuilder addContextButton(ContextButtonBuilder... contextButton) {
    if(contextButton != null) {
      this.elements.addAll(Arrays.asList(contextButton));
    }
    return this;
  }

  /**
   * Add context separator
   *
   * @param contextSeparator
   * @return
   */
  public GridBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparator) {
    if( contextSeparator != null) {
      this.elements.addAll(Arrays.asList(contextSeparator));
    }
    return this;
  }

  /**
   * Add group header
   *
   * @param groupHeader
   * @return
   */
  public GridBuilder addGroupHeader(GroupHeaderBuilder... groupHeader) {
    if(groupHeader!= null) {
      this.elements.addAll(Arrays.asList(groupHeader));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public GridBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      this.elements.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Get element list
   *
   * @return
   */
  public List<AweBuilder> getElementList() {
    return elements;
  }

}
