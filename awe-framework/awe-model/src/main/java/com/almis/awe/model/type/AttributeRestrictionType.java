package com.almis.awe.model.type;

import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.button.AbstractButton;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import com.almis.awe.model.entities.screen.component.grid.AbstractGrid;
import com.almis.awe.model.entities.screen.component.grid.Column;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;

/**
 * AttributeRestrictionType Enumerated
 *
 * Attribute list of components
 *
 *
 * @author Pablo VIDAL - 29/MAY/2013
 */
public enum AttributeRestrictionType {

  // ELEMENT ATTRIBUTES
  STYLE("style", Element.class),
  LABEL("label", Element.class),
  TITLE("title", Element.class),
  TYPE("type", Element.class),
  EXPAND("expand", Element.class),
  HELP("help", Element.class),
  HELP_IMAGE("helpImage", Element.class),

  // COMPONENT ATTRIBUTES
  COMPONENT("componentType", Component.class),
  INITIAL_LOAD("initialLoad", Component.class),
  SERVER_ACTION("serverAction", Component.class),
  TARGET_ACTION("targetAction", Component.class),
  MAX("max", Component.class),
  AUTOLOAD("autoload", Component.class),
  AUTOREFRESH("autorefresh", Component.class),

  // CRITERIA ATTRIBUTES
  REQUIRED("required", AbstractCriteria.class),
  VALUE("value", AbstractCriteria.class),
  SESSION("session", AbstractCriteria.class),
  VARIABLE("variable", AbstractCriteria.class),
  PROPERTY("property", AbstractCriteria.class),
  VALIDATION("validation", AbstractCriteria.class),
  GROUP("group", AbstractCriteria.class),
  NUMBER_FORMAT("numberFormat", AbstractCriteria.class),
  SHOW_SLIDER("showSlider", AbstractCriteria.class),
  DESTINATION("destination", AbstractCriteria.class),
  DEFAULT_VALUE("defaultValue", AbstractCriteria.class),
  CAPITALIZE("capitalize", AbstractCriteria.class),
  VISIBLE("visible", AbstractCriteria.class),
  READONLY("readonly", AbstractCriteria.class),
  CHECKED("checked", AbstractCriteria.class),
  STRICT("strict", AbstractCriteria.class),
  MESSAGE("message", AbstractCriteria.class),
  FORMULE("formule", AbstractCriteria.class),
  PLACEHOLDER("placeholder", AbstractCriteria.class),
  AREA_ROWS("arearows", AbstractCriteria.class),
  OPTIONAL("optional", AbstractCriteria.class),
  PRINTABLE("printable", AbstractCriteria.class),
  UNIT("unit", AbstractCriteria.class),
  CHECK_EMPTY("checkEmpty", AbstractCriteria.class),
  CHECK_INITIAL("checkInitial", AbstractCriteria.class),
  CHECK_TARGET("checkTarget", AbstractCriteria.class),
  SPECIFIC("specific", AbstractCriteria.class),
  TIMEOUT("timeout", AbstractCriteria.class),
  RESTRICTED_VALUE_LIST("restrictedValueList", AbstractCriteria.class),
  SHOW_WEEKENDS("showWeekends", AbstractCriteria.class),
  SHOW_FUTURE_DATES("showFutureDates", AbstractCriteria.class),
  DATE_FORMAT("dateFormat", AbstractCriteria.class),
  SHOW_TODAY_BUTTON("showTodayButton", AbstractCriteria.class),
  DATE_VIEW_MODE("dateViewMode", AbstractCriteria.class),

  // COLUMN ATTRIBUTES
  INDEX("index", Column.class),
  FIELD("field", Column.class),
  WIDTH("width", Column.class),
  CHAR_LENGTH("charLength", Column.class),
  ALIGN("align", Column.class),
  INPUT_TYPE("inputType", Column.class),
  SORTABLE("sortable", Column.class),
  MOVABLE("movable", Column.class),
  HIDDEN("hidden", Column.class),
  SENDABLE("sendable", Column.class),
  SUMMARY_TYPE("summaryType", Column.class),
  FORMATTER("formatter", Column.class),
  FORMAT_OPTIONS("formatOptions", Column.class),
  FROZEN("frozen", Column.class),
  POSITION("position", Column.class),

  // BUTTON ATTRIBUTES
  BROWSER_ACTION("browserAction", AbstractButton.class),
  BUTTON_TYPE("buttonType", AbstractButton.class),
  CANCEL("cancel", AbstractButton.class),

  // GRID ATTRIBUTES
  TOTALIZE("totalize", AbstractGrid.class),
  SHOW_TOTALS("showTotals", AbstractGrid.class),
  SEND_OPERATIONS("sendOperations", AbstractGrid.class),
  TREEGRID("treegrid", AbstractGrid.class),
  EDITABLE("editable", AbstractGrid.class),
  MULTISELECT("multiselect", AbstractGrid.class),
  EXPAND_COLUMN("expandColumn", AbstractGrid.class),
  LOAD_ALL("loadAll", AbstractGrid.class),
  SEND_ALL("sendAll", AbstractGrid.class),
  TREE_ID("treeId", AbstractGrid.class),
  TREE_PARENT("treeParent", AbstractGrid.class),
  TREE_LEAF("treeLeaf", AbstractGrid.class),
  PAGINATION_DISABLED("paginationDisabled", AbstractGrid.class),
  PAGER_VALUES("pagerValues", AbstractGrid.class),

  // PIVOT ATTRIBUTES
  TOTAL_COLUMN_PLACEMENT("totalColumnPlacement", PivotTable.class),
  TOTAL_ROW_PLACEMENT("totalRowPlacement", PivotTable.class),
  RENDERER("renderer", PivotTable.class),
  AGGREGATOR("aggregator", PivotTable.class),
  AGGREGATION_FIELD("aggregationField", PivotTable.class),
  SORT_METHOD("sortMethod", PivotTable.class),
  COLS("cols", PivotTable.class),
  ROWS("rows", PivotTable.class),
  DECIMAL_NUMBERS("decimalNumbers", PivotTable.class),
  THOUSAND_SEPARATOR("thousandSeparator", PivotTable.class),
  DECIMAL_SEPARATOR("decimalSeparator", PivotTable.class);

  // Parameter VALUE
  private String value;
  private Class<?> componentClass;

  // Constructor
  private AttributeRestrictionType(String value, Class<?> componentClass) {
    this.value = value;
    this.componentClass = componentClass;
  }

  /**
   * Retrieve enumerated value
   *
   * @param value Enumerated value
   * @return Enumerated
   */
  public static AttributeRestrictionType getEnum(String value) {
    for (AttributeRestrictionType enumerated : values()) {
      if (enumerated.value.equalsIgnoreCase(value)) {
        return enumerated;
      }
    }
    throw new IllegalArgumentException();
  }

  /**
   * Retrieve enumerated component class
   * @return Component class
   */
  public Class<?> getComponentClass() {
    return componentClass;
  }

  /**
   * Retrieve enumerated value
   * @return Value
   */
  public String getValue() {
    return value;
  }

  /**
   * Override toString method to return PROPERTY VALUE
   *
   * @return Property VALUE (if defined)
   */
  @Override
  public String toString() {
    return getValue();
  }
}
