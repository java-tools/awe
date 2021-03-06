package com.almis.awe.builder.enumerates;

/**
 *
 * @author dfuentes
 */
public enum Component {
  BUTTON_CHECKBOX("button-checkbox"),
  BUTTON_RADIO("button-radio"),
  CHECKBOX("checkbox"),
  COLOR("color"),
  DATE("date"),
  FILTERED_CALENDAR("filtered-calendar"),
  HIDDEN("hidden"),
  MARKDOWN_EDITOR("markdown-editor"),
  NUMERIC("numeric"),
  PASSWORD("password"),
  PROGRESS("progress"),
  ICON("icon"),
  IMAGE("image"),
  RADIO("radio"),
  SELECT("select"),
  SELECT_MULTIPLE("select-multiple"),
  SUGGEST("suggest"),
  SUGGEST_MULTIPLE("suggest-multiple"),
  TEXT("text"),
  TEXT_VIEW("text-view"),
  TEXTAREA("textarea"),
  TIME("time"),
  UPLOADER("uploader");
  
  private final String value;

  private Component(String value) {
    this.value = value;
  }

  /**
   * Equals method
   *
   * @param value
   * @return
   */
  public boolean equalsStr(String value) {
    return this.value.equals(value);
  }

  @Override
  public String toString() {
    return this.value;
  }
  
}
