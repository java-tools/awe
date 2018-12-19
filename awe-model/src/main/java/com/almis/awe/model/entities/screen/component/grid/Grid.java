package com.almis.awe.model.entities.screen.component.grid;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.dto.SortColumn;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.button.Button;
import com.almis.awe.model.entities.screen.component.button.ContextButton;
import com.almis.awe.model.entities.screen.component.button.ContextSeparator;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;
import com.almis.awe.model.entities.screen.data.ScreenColumn;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.List;

import static com.almis.awe.model.constant.AweConstants.NO_KEY;

/**
 * Grid Class
 *
 * Used to parse a grid tag with XStream
 *
 *
 * Generates an screen data grid
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("grid")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Grid extends Criteria {

  private static final long serialVersionUID = 3775377158422655923L;

  // Send all data of the grid or only selected rows
  @XStreamAlias("send-all")
  @XStreamAsAttribute
  private String sendAll = null;

  // Column to EXPAND the TREEGRID
  @XStreamAlias("expand-column")
  @XStreamAsAttribute
  private String expandColumn = null;

  // Grid is TREEGRID
  @XStreamAlias("treegrid")
  @XStreamAsAttribute
  private String treegrid = null;

  // Column name for TREEGRID Id
  @XStreamAlias("tree-id")
  @XStreamAsAttribute
  private String treeId = null;

  // Column name for TREEGRID parent
  @XStreamAlias("tree-parent")
  @XStreamAsAttribute
  private String treeParent = null;

  // Column name for TREEGRID leaf
  @XStreamAlias("tree-leaf")
  @XStreamAsAttribute
  private String treeLeaf = null;

  // Grid is EDITABLE
  @XStreamAlias("editable")
  @XStreamAsAttribute
  private String editable = null;

  // User can select more than one record
  @XStreamAlias("multiselect")
  @XStreamAsAttribute
  private String multiselect = null;

  // User can select more than one record
  @XStreamAlias("checkbox-multiselect")
  @XStreamAsAttribute
  private String checkboxMultiselect = null;

  // Grid sends on submit row operation TYPE (insert, update, delete or none)
  @XStreamAlias("send-operations")
  @XStreamAsAttribute
  private String sendOperations = null;

  // Style class for total rows
  @XStreamAlias("totalize")
  @XStreamAsAttribute
  private String totalize = null;

  // Show row numbers or not
  @XStreamAlias("row-numbers")
  @XStreamAsAttribute
  private String rowNumbers = null;

  // Show calculated totals
  @XStreamAlias("show-totals")
  @XStreamAsAttribute
  private String showTotals = null;

  // Show calculated totals
  @XStreamAlias("enable-filters")
  @XStreamAsAttribute
  private String enableFilters = null;

  // Validate on save
  @XStreamAlias("validate-on-save")
  @XStreamAsAttribute
  private String validateOnSave = null;

  // Column list of grid
  @XStreamOmitField
  private transient List<ScreenColumn> columnList;

  // List of buttons
  @XStreamOmitField
  private transient List<SortColumn> sortList = null;

  // Expand icon
  @XStreamAlias("icon-expand")
  @XStreamAsAttribute
  private String iconExpand = null;

  // Collapse icon
  @XStreamAlias("icon-collapse")
  @XStreamAsAttribute
  private String iconCollapse = null;

  // Leaf icon
  @XStreamAlias("icon-leaf")
  @XStreamAsAttribute
  private String iconLeaf = null;

  // Initial expansion level
  @XStreamAlias("initial-level")
  @XStreamAsAttribute
  private String initialLevel = null;

  // Disable pagination
  @XStreamAlias("pagination-disabled")
  @XStreamAsAttribute
  private String disablePagination = null;

  // Grid pager values
  @XStreamAlias("pager-values")
  @XStreamAsAttribute
  private String pagerValues = null;

  // Grid row height
  @XStreamAlias("row-height")
  @XStreamAsAttribute
  private String rowHeight = null;

  /**
   * Default constructor
   */
  public Grid() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Grid(Grid other) throws AWException {
    super(other);
    this.sendAll = other.sendAll;
    this.expandColumn = other.expandColumn;
    this.treegrid = other.treegrid;
    this.treeId = other.treeId;
    this.treeParent = other.treeParent;
    this.treeLeaf = other.treeLeaf;
    this.editable = other.editable;
    this.multiselect = other.multiselect;
    this.checkboxMultiselect = other.checkboxMultiselect;
    this.sendOperations = other.sendOperations;
    this.totalize = other.totalize;
    this.rowNumbers = other.rowNumbers;
    this.showTotals = other.showTotals;
    this.enableFilters = other.enableFilters;
    this.validateOnSave = other.validateOnSave;
    this.iconExpand = other.iconExpand;
    this.iconCollapse = other.iconCollapse;
    this.iconLeaf = other.iconLeaf;
    this.initialLevel = other.initialLevel;
    this.disablePagination = other.disablePagination;
    this.pagerValues = other.pagerValues;
    this.rowHeight = other.rowHeight;
  }

  @Override
  public Grid copy() throws AWException {
    return new Grid(this);
  }

  /**
   * Retrieve component tag
   *
   * @return
   */
  @Override
  public String getComponentType() {
    return "grid";
  }

  /**
   * Returns if data has to be loaded at init for JSON serialization
   *
   * @return INITIAL_LOAD load at init or not (boolean)
   */
  @JsonGetter("initialLoad")
  public boolean getInitialLoadConverter() {
    return this.isAutoload();
  }

  /**
   * Returns if all the data has to be sent or not
   *
   * @return SEND_ALL load all or not (string)
   */
  public String getSendAll() {
    return sendAll;
  }

  /**
   * Stores if all data has to be sent or not
   *
   * @param sendAll send all or not (string)
   */
  public void setSendAll(String sendAll) {
    this.sendAll = sendAll;
  }

  /**
   * Returns if grid is sendAll
   *
   * @return Grid is sendAll
   */
  @JsonGetter("sendAll")
  public boolean isSendAll() {
    return "true".equalsIgnoreCase(sendAll);
  }

  /**
   * Returns the column name to EXPAND the TREEGRID
   *
   * @return Column to EXPAND (string)
   */
  @JsonGetter("expandColumn")
  public String getExpandColumn() {
    return expandColumn;
  }

  /**
   * Store the column name to EXPAND the TREEGRID
   *
   * @param expandColumn Column name to EXPAND TREEGRID
   */
  public void setExpandColumn(String expandColumn) {
    this.expandColumn = expandColumn;
  }

  /**
   * Returns if grid is TREEGRID
   *
   * @return Grid is TREEGRID (string)
   */
  public String getTreegrid() {
    return treegrid;
  }

  /**
   * Stores if grid is TREEGRID
   *
   * @param treegrid Grid is TREEGRID (string)
   */
  public void setTreegrid(String treegrid) {
    this.treegrid = treegrid;
  }

  /**
   * Returns if grid is TREEGRID
   *
   * @return Grid is TREEGRID
   */
  @JsonGetter("treegrid")
  public boolean isTreegrid() {
    return "true".equalsIgnoreCase(treegrid);
  }

  /**
   * Get the column name of grid as TreeId
   *
   * @return the TREE_ID
   */
  public String getTreeId() {
    return treeId;
  }

  /**
   * Get the column name of grid as TreeId for JSON serialization
   *
   * @return the TREE_ID
   */
  @JsonGetter("treeId")
  public String getTreeIdConverter() {
    if (isTreegrid()) {
      return this.getTreeId();
    }
    return null;
  }

  /**
   * Store TREE_ID
   *
   * @param treeId the TREE_ID to set
   */
  public void setTreeId(String treeId) {
    this.treeId = treeId;
  }

  /**
   * Get the column name of grid as TreeParent
   *
   * @return the TREE_PARENT
   */
  public String getTreeParent() {
    return treeParent;
  }

  /**
   * Get the column name of grid as TreeParent for JSON serialization
   *
   * @return the TREE_PARENT
   */
  @JsonGetter("treeParent")
  public String getTreeParentConverter() {
    if (isTreegrid()) {
      return this.getTreeParent();
    }
    return null;
  }

  /**
   * Store tree parent
   *
   * @param treeParent the TREE_PARENT to set
   */
  public void setTreeParent(String treeParent) {
    this.treeParent = treeParent;
  }

  /**
   * Returns if grid is EDITABLE
   *
   * @return Grid is EDITABLE (string)
   */
  public String getEditable() {
    return editable;
  }

  /**
   * Stores if grid is EDITABLE
   *
   * @param editable Grid is EDITABLE (string)
   */
  public void setEditable(String editable) {
    this.editable = editable;
  }

  /**
   * Returns if grid is EDITABLE
   *
   * @return Grid is EDITABLE
   */
  @JsonGetter("editable")
  public boolean isEditable() {
    return "true".equalsIgnoreCase(editable);
  }

  /**
   * Returns if user can select more than one line
   *
   * @return Grid is MULTISELECT (string)
   */
  public String getMultiselect() {
    return multiselect;
  }

  /**
   * Stores if user can select more than one line
   *
   * @param multiselect Grid is MULTISELECT (string)
   */
  public void setMultiselect(String multiselect) {
    this.multiselect = multiselect;
  }

  /**
   * Returns if grid is MULTISELECT
   *
   * @return Grid is MULTISELECT
   */
  @JsonGetter("multiselect")
  public boolean isMultiselect() {
    return "true".equalsIgnoreCase(multiselect) || isCheckboxMultiselect();
  }

  /**
   * @return the checkboxMultiselect
   */
  public String getCheckboxMultiselect() {
    return checkboxMultiselect;
  }

  /**
   * @param checkboxMultiselect the checkboxMultiselect to set
   */
  public void setCheckboxMultiselect(String checkboxMultiselect) {
    this.checkboxMultiselect = checkboxMultiselect;
  }

  /**
   * Returns if grid is checkbox Multiselect
   *
   * @return Grid is checkbox Multiselect
   */
  @JsonGetter("checkboxMultiselect")
  public boolean isCheckboxMultiselect() {
    return "true".equalsIgnoreCase(checkboxMultiselect);
  }

  /**
   * Returns the send operations flag
   *
   * @return Send Operations flag
   */
  @JsonIgnore
  public String getSendOperations() {
    return sendOperations;
  }

  /**
   * Stores the send operations flag
   *
   * @param sendOperations Send Operations flag
   */
  public void setSendOperations(String sendOperations) {
    this.sendOperations = sendOperations;
  }

  /**
   * Returns if grid is multioperation
   *
   * @return Grid is multioperation
   */
  @JsonGetter("multioperation")
  public boolean isMultioperation() {
    return "true".equalsIgnoreCase(sendOperations);
  }

  /**
   * @return the rowNumbers
   */
  public String getRowNumbers() {
    return rowNumbers;
  }

  /**
   * @param rowNumbers the rowNumbers to set
   */
  public void setRowNumbers(String rowNumbers) {
    this.rowNumbers = rowNumbers;
  }

  /**
   * Retrieve if show row numbers or not
   *
   * @return Show row numbers or not
   */
  @JsonGetter("rownumbers")
  public boolean isRowNumbers() {
    return rowNumbers == null || "true".equalsIgnoreCase(rowNumbers);
  }

  /**
   * Returns the STYLE class for total rows
   *
   * @return Style class for total rows
   */
  @JsonGetter("totalize")
  public String getTotalize() {
    return totalize;
  }

  /**
   * Stores the STYLE class for total rows
   *
   * @param totalize Style class for total rows
   */
  public void setTotalize(String totalize) {
    this.totalize = totalize;
  }

  /**
   * Returns if grid has to show calculated totals
   *
   * @return Show calculated totals
   */
  public String getShowTotals() {
    return showTotals;
  }

  /**
   * Returns if grid has to show calculated totals
   *
   * @return Show calculated totals
   */
  @JsonGetter("showTotals")
  public boolean isShowTotals() {
    return showTotals != null && "true".equalsIgnoreCase(showTotals);
  }

  /**
   * Stores if grid has to show calculated totals
   *
   * @param showTotals Show calculated totals
   */
  public void setShowTotals(String showTotals) {
    this.showTotals = showTotals;
  }

  /**
   * Returns if grid has to validate on save
   *
   * @return Validate on save
   */
  @JsonGetter("validateOnSave")
  public boolean isValidateOnSave() {
    return validateOnSave == null || "true".equalsIgnoreCase(validateOnSave);
  }

  /**
   * Stores validate on save
   *
   * @param validateOnSave Validate on save
   */
  public void setValidateOnSave(String validateOnSave) {
    this.validateOnSave = validateOnSave;
  }

  /**
   * Store column list
   *
   * @param columnList Column list
   */
  public void setColumnList(List<ScreenColumn> columnList) {
    this.columnList = columnList;
  }

  /**
   * @return the iconExpand
   */
  @JsonGetter("iconExpand")
  public String getIconExpand() {
    return iconExpand;
  }

  /**
   * @param iconExpand the iconExpand to set
   */
  public void setIconExpand(String iconExpand) {
    this.iconExpand = iconExpand;
  }

  /**
   * @return the iconCollapse
   */
  @JsonGetter("iconCollapse")
  public String getIconCollapse() {
    return iconCollapse;
  }

  /**
   * @param iconCollapse the iconCollapse to set
   */
  public void setIconCollapse(String iconCollapse) {
    this.iconCollapse = iconCollapse;
  }

  /**
   * @return the iconLeaf
   */
  @JsonGetter("iconLeaf")
  public String getIconLeaf() {
    return iconLeaf;
  }

  /**
   * @param iconLeaf the iconLeaf to set
   */
  public void setIconLeaf(String iconLeaf) {
    this.iconLeaf = iconLeaf;
  }

  /**
   * @return the initialLevel
   */
  @JsonGetter("initialLevel")
  public String getInitialLevel() {
    return initialLevel;
  }

  /**
   * @param initialLevel the initialLevel to set
   */
  public void setInitialLevel(String initialLevel) {
    this.initialLevel = initialLevel;
  }

  /**
   * @return the TREE_LEAF for JSON
   */
  @JsonGetter("treeLeaf")
  public String getTreeLeafConverter() {
    return getTreeLeaf();
  }

  /**
   * @return the TREE_LEAF
   */
  public String getTreeLeaf() {
    return treeLeaf;
  }

  /**
   * @param treeLeaf the TREE_LEAF to set
   */
  public void setTreeLeaf(String treeLeaf) {
    this.treeLeaf = treeLeaf;
  }

  /**
   * @return the disablePagination
   */
  @JsonIgnore
  public String getDisablePagination() {
    return disablePagination;
  }

  /**
   * @param disablePagination the disablePagination to set
   */
  public void setDisablePagination(String disablePagination) {
    this.disablePagination = disablePagination;
  }

  /**
   * Returns if grid has disabled pagination
   *
   * @return Disabled pagination
   */
  @JsonGetter("disablePagination")
  public boolean isDisabledPagination() {
    return Boolean.parseBoolean(disablePagination);
  }

  /**
   * @return the pagerValues
   */
  public String getPagerValues() {
    return pagerValues;
  }

  /**
   * @return the enableFilters
   */
  public String getEnableFilters() {
    return enableFilters;
  }

  /**
   * @param enableFilters the enableFilters to set
   */
  public void setEnableFilters(String enableFilters) {
    this.enableFilters = enableFilters;
  }

  /**
   * Returns if grid has enabled filters
   *
   * @return Enabled filters
   */
  @JsonGetter("enableFilters")
  public boolean isEnabledFilters() {
    return "true".equalsIgnoreCase(enableFilters);
  }

  /**
   * @param pagerValues the values for fill the pager of grid
   */
  public void setPagerValues(String pagerValues) {
    this.pagerValues = pagerValues;
  }

  /**
   * @return the row height
   */
  public String getRowHeight() {
    return rowHeight;
  }

  /**
   * @param rowHeight the value for the height of every row
   */
  public void setRowHeight(String rowHeight) {
    this.rowHeight = rowHeight;
  }

  @JsonIgnore
  @Override
  public String getComponentTag() {
    return isTreegrid() ? "tree-grid" : "grid";
  }

  /**
   * Returns a list with the grid pager values for JSON serialization
   *
   * @return Column names list
   */
  @JsonGetter("pagerValues")
  public List<Integer> getGridPagerValuesConverter() {
    // Variable definition
    List<Integer> pagerValueList = new ArrayList<>();

    if (this.getPagerValues() != null) {
      String[] strPagerValues = this.getPagerValues().replaceAll("\\s+", "").split(",");
      for (String value : strPagerValues) {
        pagerValueList.add(Integer.valueOf(value));
      }
    }

    // Return array
    return pagerValueList;
  }

  /**
   * Returns a list with the column list description for JSON serialization
   *
   * @return Column Model List
   */
  @JsonGetter("columnModel")
  public List<ScreenColumn> getColumnList() {
    return columnList;
  }

  /**
   * Returns the column list description for JSON serialization
   *
   * @return Column Model List
   */
  @JsonGetter("headerModel")
  public List<GroupHeader> getGroupHeaderModel() {

    // Variable definition
    List<GroupHeader> groupHeaderModel = new ArrayList<>();

    // Get elements (columns)
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (element instanceof GroupHeader) {
          // Get GROUP header
          GroupHeader groupHeader = (GroupHeader) element;
          // Retrieve column model
          groupHeaderModel.add(groupHeader);
        }
      }
    }

    // Return string parameter list
    return groupHeaderModel;
  }

  /**
   * Returns a the button list description for JSON serialization
   *
   * @return Button Model List
   */
  @JsonGetter("buttonModel")
  public List<Button> getButtonModelConverter() {

    List<Button> buttonModel = new ArrayList<>();

    // Get elements (buttons)
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        if (isButtonValid(element)) {
          // Get button
          Button button = (Button) element;
          // Retrieve button menu model
          buttonModel.add(button);
        }
      }
    }
    // Return string parameter list
    return buttonModel;
  }

  /**
   * Checks if an element is a valid button
   *
   * @param element Element
   * @return Is valid or not
   */
  private boolean isButtonValid(Element element) {

    String elementKey = element.getElementKey();
    return elementKey != null && !NO_KEY.equalsIgnoreCase(elementKey) && element instanceof Button && !(element instanceof ContextButton) && !(element instanceof ContextSeparator);
  }

  /**
   * Retrieve column by column id
   *
   * @param name column id
   * @return Column found
   */
  public ScreenColumn getColumnById(String name) {
    ScreenColumn columnFound = null;
    // Get grid columns
    List<ScreenColumn> columns = getColumnList();
    for (ScreenColumn columnComponent : columns) {
      // Generate the children
      if (name.equalsIgnoreCase(columnComponent.getId())) {
        columnFound = columnComponent;
      }
    }
    return columnFound;
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_GRID;
  }

  @Override
  public ST generateHelpTemplate(STGroup group, String label, boolean developers) {
    ST template = group.createStringTemplate(group.rawGetTemplate(getHelpTemplate()));
    List<ST> columns = new ArrayList<>();
    List<ST> buttons = new ArrayList<>();
    String currentLabel = getLabel() == null ? label : getLabel();

    // Get grid columns
    List<Column> gridColumns = getElementsByType(Column.class);
    for (Column column : gridColumns) {
      // Generate the children
      if (!column.isHidden() && column.getLabel() != null) {
        columns.add(column.generateHelpTemplate(group, null, developers));
      }
    }

    // Get grid buttons
    List<Button> buttonList = getElementsByType(Button.class);
    for (Button button : buttonList) {
      // Generate the children
      buttons.add(button.generateHelpTemplate(group, null, AweConstants.TEMPLATE_HELP_BUTTON_GRID, developers));
    }

    // Generate template
    template.add("e", this).add("label", currentLabel).add("developers", developers).add("columns", columns).add("buttons", buttons);

    // Retrieve code
    return template;
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Last label
   * @param parameters       Parameters
   * @param dataSuffix       Data suffix
   * @return Print bean
   */
  @JsonIgnore
  @Override
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    if (getLabel() == null) {
      setLabel(label);
    }
    printElementList.add(this);
    return printElementList;
  }
}
