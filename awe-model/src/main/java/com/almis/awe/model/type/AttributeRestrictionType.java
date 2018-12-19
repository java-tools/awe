/*
 * Package definition
 */
package com.almis.awe.model.type;

/**
 * AttributeRestrictionType Enumerated
 *
 * Attribute list of criteria
 *
 *
 * @author Pablo VIDAL - 29/MAY/2013
 */
public enum AttributeRestrictionType {

  // TAG ATTRIBUTES
  STYLE("style"),
  LABEL("label"),
  TYPE("type"),
  EXPAND("expand"),
  HELP("help"),
  HELP_IMAGE("helpImage"),

  // CRITERIA ATTRIBUTES
  REQUIRED("required"),
  VALUE("value"),
  SESSION("session"),
  VARIABLE("variable"),
  PROPERTY("property"),
  COMPONENT("component"),
  INITIAL_LOAD("initialLoad"),
  VALIDATION("validation"),
  GROUP("group"),
  NUMBER_FORMAT("numberFormat"),
  SHOW_SLIDER("showSlider"),
  DESTINATION("destination"),
  DEFAULT_VALUE("defaultValue"),
  CAPITALIZE("capitalize"),
  VISIBLE("visible"),
  READONLY("readonly"),
  CHECKED("checked"),
  STRICT("strict"),
  SERVER_ACTION("serverAction"),
  TARGET_ACTION("targetAction"),
  AUTOLOAD("autoload"),
  AUTOREFRESH("autorefresh"),
  MESSAGE("message"),
  FORMULE("formule"),
  PLACEHOLDER("placeholder"),
  AREA_ROWS("arearows"),
  OPTIONAL("optional"),
  MAX("max"),
  PRINTABLE("printable"),
  UNIT("unit"),
  CHECK_EMPTY("checkEmpty"),
  CHECK_INITIAL("checkInitial"),
  CHECK_TARGET("checkTarget"),
  SPECIFIC("specific"),
  TIMEOUT("timeout"),
  RESTRICTED_VALUE_LIST("restrictedValueList"),
  SHOW_WEEKENDS("showWeekends"),
  SHOW_FUTURE_DATES("showFutureDates"),
  DATE_FORMAT("dateFormat"),
  SHOW_TODAY_BUTTON("showTodayButton"),
  DATE_VIEW_MODE("dateViewMode"),

  // COLUMN ATTRIBUTES
  INDEX("index"),
  FIELD("field"),
  WIDTH("width"),
  CHAR_LENGTH("charLength"),
  ALIGN("align"),
  INPUT_TYPE("inputType"),
  SORTABLE("sortable"),
  MOVABLE("movable"),
  HIDDEN("hidden"),
  SENDABLE("sendable"),
  SUMMARY_TYPE("summaryType"),
  FORMATTER("formatter"),
  FORMAT_OPTIONS("formatOptions"),
  FROZEN("frozen"),
  POSITION("position"),

  // BUTTON ATTRIBUTES
  BROWSER_ACTION("browserAction"),
  BUTTON_TYPE("buttonType"),
  CANCEL("cancel"),

  // GRID ATTRIBUTES
  TOTALIZE("totalize"),
  SHOW_TOTALS("showTotals"),
  SEND_OPERATIONS("sendOperations"),
  TREEGRID("treegrid"),
  EDITABLE("editable"),
  MULTISELECT("multiselect"),
  EXPAND_COLUMN("expandColumn"),
  LOAD_ALL("loadAll"),
  SEND_ALL("sendAll"),
  TREE_ID("treeId"),
  TREE_PARENT("treeParent"),
  TREE_LEAF("treeLeaf"),
  PAGINATION_DISABLED("paginationDisabled"),
  PAGER_VALUES("pagerValues"),

  // PIVOT ATTRIBUTES
  TOTAL_COLUMN_PLACEMENT("totalColumnPlacement"),
  TOTAL_ROW_PLACEMENT("totalRowPlacement"),
  RENDERER("renderer"),
  AGGREGATOR("aggregator"),
  AGGREGATION_FIELD("aggregationField"),
  SORT_METHOD("sortMethod"),
  COLS("cols"),
  ROWS("rows"),
  DECIMAL_NUMBERS("decimalNumbers"),
  THOUSAND_SEPARATOR("thousandSeparator"),
  DECIMAL_SEPARATOR("decimalSeparator");

  // Parameter VALUE
  private String value;

  // Constructor
  private AttributeRestrictionType(String value) {
    this.value = value;
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
   * Override toString method to return PROPERTY VALUE
   *
   * @return Property VALUE (if defined)
   */
  @Override
  public String toString() {
    return value;
  }
}
