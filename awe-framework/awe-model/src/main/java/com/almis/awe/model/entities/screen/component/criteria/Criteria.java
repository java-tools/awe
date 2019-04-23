/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.criteria;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;

/*
 * File Imports
 */

/**
 * Criteria Class
 *
 * Used to parse a criteria tag with XStream
 * Generates an screen criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("criteria")
@JsonInclude(Include.NON_NULL)
public class Criteria extends Component {

  private static final long serialVersionUID = -8937301673779113798L;

  // Static VALUE
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value = null;

  // Session VARIABLE
  @XStreamAlias("session")
  @XStreamAsAttribute
  private String session = null;

  // Variable
  @XStreamAlias("variable")
  @XStreamAsAttribute
  private String variable = null;

  // Property
  @XStreamAlias("property")
  @XStreamAsAttribute
  private String property = null;

  // Validation Format
  @XStreamAlias("validation")
  @XStreamAsAttribute
  private String validation = null;

  // Number Format
  @XStreamAlias("number-format")
  @XStreamAsAttribute
  private String numberFormat = null;

  // Show slider flag for numeric criteria
  @XStreamAlias("show-slider")
  @XStreamAsAttribute
  private String showSlider = null;

  // Capitalize data in criteria
  @XStreamAlias("capitalize")
  @XStreamAsAttribute
  private String capitalize = null;

  // Criteria is read-only
  @XStreamAlias("readonly")
  @XStreamAsAttribute
  private String readonly = null;
  // Criteria is CHECKED
  @XStreamAlias("checked")
  @XStreamAsAttribute
  private String checked = null;

  // Criteria is an STRICT VALUE
  @XStreamAlias("strict")
  @XStreamAsAttribute
  private String strict = null;

  // Validation MESSAGE
  @XStreamAlias("message")
  @XStreamAsAttribute
  private String message = null;

  // Criteria Formule
  @XStreamAlias("formule")
  @XStreamAsAttribute
  private String formule = null;

  // Criteria is OPTIONAL
  @XStreamAlias("optional")
  @XStreamAsAttribute
  private String optional = null;

  // Criteria is PRINTABLE
  @XStreamAlias("printable")
  @XStreamAsAttribute
  private String printable = null;

  // Criteria UNIT LABEL
  @XStreamAlias("unit")
  @XStreamAsAttribute
  private String unit = null;

  // Criteria will be CHECKED for empty values
  @XStreamAlias("check-empty")
  @XStreamAsAttribute
  private String checkEmpty = null;

  // Suggest TYPE criteria initial query check
  @XStreamAlias("check-initial")
  @XStreamAsAttribute
  private String checkInitial = null;

  // Suggest TYPE criteria initial query target
  @XStreamAlias("check-target")
  @XStreamAsAttribute
  private String checkTarget = null;

  // Criteria will be CHECKED for empty values
  @XStreamAlias("specific")
  @XStreamAsAttribute
  private String specific = null;

  // Criteria search TIMEOUT
  @XStreamAlias("timeout")
  @XStreamAsAttribute
  private String timeout = null;

  // Group name
  @XStreamAlias("group")
  @XStreamAsAttribute
  private String group = null;

  // Input PLACEHOLDER
  @XStreamAlias("placeholder")
  @XStreamAsAttribute
  private String placeholder = null;

  // Text area rows
  @XStreamAlias("area-rows")
  @XStreamAsAttribute
  private String areaRows = null;

  // Destination VALUE
  @XStreamAlias("destination")
  @XStreamAsAttribute
  private String destination = null;

  // Show weekends in Date criteria
  @XStreamAlias("show-weekends")
  @XStreamAsAttribute
  private String showWeekends = null;

  // Show future dates in Date criteria
  @XStreamAlias("show-future-dates")
  @XStreamAsAttribute
  private String showFutureDates = null;

  // Format of a date
  @XStreamAlias("date-format")
  @XStreamAsAttribute
  private String dateFormat = null;

  // Show today button in date criteria
  @XStreamAlias("date-show-today-button")
  @XStreamAsAttribute
  private String showTodayButton = null;

  // Show days, months or years in date criteria
  @XStreamAlias("date-view-mode")
  @XStreamAsAttribute
  private String dateViewMode = null;

  // Put label at left and give it a size
  @XStreamAlias("left-label")
  @XStreamAsAttribute
  private String leftLabel = null;

  // Parameter VALUE
  @XStreamOmitField
  private String parameterValue = null;

  // Parameter VALUE
  @XStreamOmitField
  private transient List<String> parameterValueList = null;

  // Default VALUE
  @XStreamOmitField
  private String defaultValue = null;

  /**
   * Default constructor
   */
  public Criteria() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Criteria(Criteria other) throws AWException {
    super(other);
    this.value = other.value;
    this.session = other.session;
    this.variable = other.variable;
    this.property = other.property;
    this.validation = other.validation;
    this.numberFormat = other.numberFormat;
    this.showSlider = other.showSlider;
    this.capitalize = other.capitalize;
    this.readonly = other.readonly;
    this.checked = other.checked;
    this.strict = other.strict;
    this.message = other.message;
    this.formule = other.formule;
    this.optional = other.optional;
    this.printable = other.printable;
    this.unit = other.unit;
    this.checkEmpty = other.checkEmpty;
    this.checkInitial = other.checkInitial;
    this.checkTarget = other.checkTarget;
    this.specific = other.specific;
    this.timeout = other.timeout;
    this.group = other.group;
    this.placeholder = other.placeholder;
    this.areaRows = other.areaRows;
    this.destination = other.destination;
    this.showWeekends = other.showWeekends;
    this.showFutureDates = other.showFutureDates;
    this.dateFormat = other.dateFormat;
    this.showTodayButton = other.showTodayButton;
    this.dateViewMode = other.dateViewMode;
    this.leftLabel = other.leftLabel;
  }

  @Override
  public Criteria copy() throws AWException {
    return new Criteria(this);
  }

  /**
   * Retrieve component tag
   *
   * @return Component tag
   */
  @Override
  public String getComponentTag() {
    return "input-" + this.getComponentType();
  }

  /**
   * Returns if the criteria VALUE list has to be capitalized
   *
   * @return Value list has to be capitalized
   */
  @JsonGetter("capitalize")
  public String getCapitalize() {
    return capitalize;
  }

  /**
   * Stores if the criteria VALUE list has to be capitalized
   *
   * @param capitalize Value list has to be capitalized
   */
  public void setCapitalize(String capitalize) {
    this.capitalize = capitalize;
  }

  /**
   * Returns the criteria VALIDATION FORMAT
   *
   * @return Validation FORMAT
   */
  @JsonGetter("validation")
  public String getValidation() {
    return validation;
  }

  /**
   * If validation is set to required, returns true
   *
   * @return required required value
   */
  @JsonGetter("required")
  public boolean isRequiredConverter() {
    return this.getValidation() != null && this.getValidation().contains("required");
  }

  /**
   * Stores the criteria VALIDATION FORMAT
   *
   * @param validation Validation FORMAT
   */
  public void setValidation(String validation) {
    this.validation = validation;
  }

  /**
   * Returns if criteria is READONLY is READONLY (string)
   *
   * @return readonly
   */
  public String getReadonly() {
    return readonly;
  }

  /**
   * Returns if criteria is READONLY is READONLY (string)
   *
   * @return readonly
   */
  @JsonGetter("readonly")
  public boolean getReadonlyConverter() {
    return Boolean.parseBoolean(readonly);
  }

  /**
   * Stores if criteria is READONLY
   *
   * @param readonly Criteria is READONLY
   */
  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  /**
   * Returns if criteria is CHECKED by default is CHECKED (string)
   *
   * @return checked
   */
  public String getChecked() {
    return checked;
  }

  /**
   * Stores if criteria is CHECKED by default
   *
   * @param checked Criteria is CHECKED by default
   */
  public void setChecked(String checked) {
    this.checked = checked;
  }

  /**
   * Return true if criteria has set attribute CHECKED as true else return false
   *
   * @return boolean
   */
  @JsonGetter("checked")
  public boolean isChecked() {
    return Boolean.parseBoolean(this.checked);
  }

  /**
   * Returns the criteria FORMULE definition FORMULE definition
   *
   * @return formule
   */
  @JsonGetter("formule")
  public String getFormule() {
    return formule;
  }

  /**
   * Stores the criteria FORMULE definition
   *
   * @param formule Criteria FORMULE definition
   */
  public void setFormule(String formule) {
    this.formule = formule;
  }

  /**
   * Returns the criteria SESSION VARIABLE
   *
   * @return Session variable
   */
  @JsonIgnore
  public String getSession() {
    return session;
  }

  /**
   * Stores the criteria SESSION VARIABLE identifier
   *
   * @param session Criteria SESSION VARIABLE identifier
   */
  public void setSession(String session) {
    this.session = session;
  }

  /**
   * Returns the criteria PROPERTY VARIABLE
   *
   * @return Property VARIABLE
   */
  @JsonIgnore
  public String getProperty() {
    return property;
  }

  /**
   * Stores the PROPERTY VARIABLE identifier
   *
   * @param property Criteria PROPERTY VARIABLE identifier
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * Returns the criteria VARIABLE identifier
   *
   * @return VARIABLE identifier
   */
  @JsonIgnore
  public String getVariable() {
    return variable;
  }

  /**
   * Stores the criteria VARIABLE identifier
   *
   * @param variable Criteria VARIABLE identifier
   */
  public void setVariable(String variable) {
    this.variable = variable;
  }

  /**
   * Returns the VALIDATION MESSAGE
   *
   * @return Validation MESSAGE
   */
  @JsonGetter("message")
  public String getMessage() {
    return message;
  }

  /**
   * Stores the VALIDATION MESSAGE
   *
   * @param message Validation MESSAGE
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Returns the criteria parameter VALUE
   *
   * @return Parameter VALUE
   */
  @JsonIgnore
  public String getParameterValue() {
    return parameterValue;
  }

  /**
   * Stores the criteria parameter VALUE
   *
   * @param parameterValue Parameter VALUE
   */
  public void setParameterValue(String parameterValue) {
    this.parameterValue = parameterValue;
  }

  /**
   * Returns the criteria parameter VALUE
   *
   * @return Parameter VALUE list
   */
  @JsonIgnore
  public List<String> getParameterValueList() {
    return parameterValueList;
  }

  /**
   * Stores the criteria parameter VALUE
   *
   * @param parameterValueList Parameter VALUE
   */
  public void setParameterValueList(List<String> parameterValueList) {
    this.parameterValueList = parameterValueList;
  }

  /**
   * Returns the criteria default VALUE
   *
   * @return Default VALUE
   */
  @JsonIgnore
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Stores the criteria default VALUE
   *
   * @param defaultValue Default VALUE
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * Returns if the criteria is OPTIONAL
   *
   * @return is OPTIONAL (string)
   */
  public String getOptional() {
    return optional;
  }

  /**
   * Stores if the criteria is OPTIONAL
   *
   * @param optional Criteria is OPTIONAL (string)
   */
  public void setOptional(String optional) {
    this.optional = optional;
  }

  /**
   * Check if Criteria has OPTIONAL VALUE
   *
   * @return OPTIONAL
   */
  @JsonGetter("optional")
  public boolean isOptional() {
    return Boolean.parseBoolean(this.optional);
  }

  /**
   * Returns the numeric FORMAT (for autonumeric criteria)
   *
   * @return the numeric FORMAT
   */
  @JsonGetter("numberFormat")
  public String getNumberFormat() {
    return numberFormat;
  }

  /**
   * Stores the numeric FORMAT (for autonumeric criteria)
   *
   * @param numberFormat numeric FORMAT
   */
  public void setNumberFormat(String numberFormat) {
    this.numberFormat = numberFormat;
  }

  /**
   * Returns the flag show-slider (for autonumeric criteria)
   *
   * @return flag show-slider
   */
  public String getShowSlider() {
    return showSlider;
  }

  /**
   * Stores the flag to show slider (for autonumeric criteria)
   *
   * @param showSlider Show numeric slider
   */
  public void setShowSlider(String showSlider) {
    this.showSlider = showSlider;
  }

  /**
   * Check if Criteria has slider (for autonumeric criteria)
   *
   * @return SHOW_SLIDER
   */
  @JsonGetter("showSlider")
  public boolean isShowSlider() {
    return Boolean.parseBoolean(this.showSlider);
  }

  /**
   * Returns if the criteria needs to have the STRICT VALUE (not free VALUE)
   *
   * @return True if criteria has a STRICT VALUE
   */
  public String getStrict() {
    return strict;
  }

  /**
   * Returns if criteria follows strict behavior
   *
   * @return true if criteria follows strict behavior or no value. In other case, false
   */
  @JsonGetter("strict")
  public boolean isStrict() {
    return getStrict() == null || Boolean.parseBoolean(getStrict());
  }

  /**
   * Stores if the criteria needs to have the STRICT VALUE (not free VALUE)
   *
   * @param strict True if criteria has a STRICT VALUE
   */
  public void setStrict(String strict) {
    this.strict = strict;
  }

  /**
   * Returns if the criteria is PRINTABLE
   *
   * @return PRINTABLE
   */
  public String getPrintable() {
    return printable;
  }

  /**
   * Returns if the criteria is PRINTABLE
   *
   * @return PRINTABLE
   */
  @JsonGetter("printable")
  public boolean isPrintable() {
    return printable == null || Boolean.parseBoolean(printable);
  }

  /**
   * Stores if criteria is PRINTABLE
   *
   * @param printable Set criteria printable
   */
  public void setPrintable(String printable) {
    this.printable = printable;
  }

  /**
   * Returns the UNIT LABEL
   *
   * @return the UNIT LABEL
   */
  @JsonGetter("unit")
  public String getUnit() {
    return unit;
  }

  /**
   * Stores the UNIT LABEL
   *
   * @param unit the UNIT LABEL to set
   */
  public void setUnit(String unit) {
    this.unit = unit;
  }

  /**
   * Returns the check empty flag
   *
   * @return check empty flag
   */
  public String getCheckEmpty() {
    return checkEmpty;
  }

  /**
   * Returns the check empty flag as a BOOLEAN
   *
   * @return check empty flag
   */
  @JsonGetter("checkEmpty")
  public boolean getCheckEmptyConverter() {
    return Boolean.parseBoolean(this.getCheckEmpty());
  }

  /**
   * Stores the check empty flag
   *
   * @param checkEmpty the check empty flag to set
   */
  public void setCheckEmpty(String checkEmpty) {
    this.checkEmpty = checkEmpty;
  }

  /**
   * Returns the check initial flag
   *
   * @return check initial flag
   */
  public String getCheckInitial() {
    return checkInitial;
  }

  /**
   * Stores the check initial flag
   *
   * @param checkInitial the check initial flag to set
   */
  public void setCheckInitial(String checkInitial) {
    this.checkInitial = checkInitial;
  }

  /**
   * Returns if the criteria is checkInitial
   *
   * @return checkInitial
   */
  @JsonGetter("checkInitial")
  public boolean isCheckInitial() {
    return checkInitial == null || Boolean.parseBoolean(checkInitial);
  }

  /**
   * Returns the check target
   *
   * @return check target
   */
  public String getCheckTarget() {
    return checkTarget;
  }

  /**
   * Returns the check target flag as a BOOLEAN
   *
   * @return check target flag
   */
  @JsonGetter("checkTarget")
  public Object getCheckTargetConverter() {
    return getCheckTarget() != null ? checkTarget : false;
  }

  /**
   * Stores the check target
   *
   * @param checkTarget the check target to set
   */
  public void setCheckTarget(String checkTarget) {
    this.checkTarget = checkTarget;
  }

  /**
   * Returns the SPECIFIC attributes
   *
   * @return the SPECIFIC attributes
   */
  @JsonGetter("specific")
  public String getSpecific() {
    return specific;
  }

  /**
   * Stores the SPECIFIC attributes
   *
   * @param specific the SPECIFIC attributes to set
   */
  public void setSpecific(String specific) {
    this.specific = specific;
  }

  /**
   * Retrieve the search TIMEOUT
   *
   * @return the TIMEOUT
   */
  @JsonGetter("timeout")
  public String getTimeout() {
    return timeout;
  }

  /**
   * Stores the search TIMEOUT
   *
   * @param timeout the TIMEOUT to set
   */
  public void setTimeout(String timeout) {
    this.timeout = timeout;
  }

  /**
   * Retrieve the GROUP name
   *
   * @return the GROUP name
   */
  @JsonGetter("group")
  public String getGroup() {
    return group;
  }

  /**
   * Store the GROUP name
   *
   * @param group the GROUP name
   */
  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Retrieve the PLACEHOLDER
   *
   * @return the PLACEHOLDER
   */
  @JsonGetter("placeholder")
  public String getPlaceholder() {
    return placeholder;
  }

  /**
   * Store the PLACEHOLDER
   *
   * @param placeholder the PLACEHOLDER
   */
  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  /**
   * Retrieve rows of text area
   *
   * @return rows
   */
  @JsonGetter("areaRows")
  public String getAreaRows() {
    return areaRows;
  }

  /**
   * Store rows of text area
   *
   * @param areaRows Area rows
   */
  public void setAreaRows(String areaRows) {
    this.areaRows = areaRows;
  }

  /**
   * Returns the criteria static VALUE
   *
   * @return static VALUE
   */
  @JsonGetter("value")
  public String getValue() {
    return value;
  }

  /**
   * Stores the criteria static VALUE
   *
   * @param value Criteria static VALUE
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Returns the element's TYPE
   *
   * @return Element TYPE
   */
  @JsonGetter("inputType")
  @Override
  public String getType() {
    return super.getType();
  }

  /**
   * @return the DESTINATION
   */
  @JsonGetter("destination")
  public String getDestination() {
    return destination;
  }

  /**
   * @return the SHOW_WEEKENDS
   */
  public String getShowWeekends() {
    return showWeekends;
  }

  /**
   * @param showWeekends the SHOW_WEEKENDS to set
   */
  public void setShowWeekends(String showWeekends) {
    this.showWeekends = showWeekends;
  }

  /**
   * Check if Date has weekends enabled
   *
   * @return SHOW_WEEKENDS
   */
  @JsonGetter("showWeekends")
  public boolean isShowWeekends() {
    return this.showWeekends == null || Boolean.parseBoolean(this.getShowWeekends());
  }

  /**
   * @return the SHOW_FUTURE_DATES
   */
  public String getShowFutureDates() {
    return showFutureDates;
  }

  /**
   * @param showFutureDates the SHOW_FUTURE_DATES to set
   */
  public void setShowFutureDates(String showFutureDates) {
    this.showFutureDates = showFutureDates;
  }

  /**
   * Check if Date has future dates enabled
   *
   * @return SHOW_FUTURE_DATES
   */
  @JsonGetter("showFutureDates")
  public boolean isShowFutureDates() {
    return this.showFutureDates == null || Boolean.parseBoolean(this.getShowFutureDates());
  }

  /**
   * @param destination the DESTINATION to set
   */
  public void setDestination(String destination) {
    this.destination = destination;
  }

  /**
   * Get the date format
   *
   * @return the date format
   */
  @JsonGetter("dateFormat")
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Stores the date format
   *
   * @param dateFormat Date format
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * Get VALUE of show today button
   *
   * @return Show Today button
   */
  public String getShowTodayButton() {
    return showTodayButton;
  }

  /**
   * Get if today button is shown in date criteria
   *
   * @return if today button is shown in date criteria
   */
  @JsonGetter("showTodayButton")
  public boolean isShowTodayButton() {
    return this.showTodayButton == null || Boolean.parseBoolean(this.getShowTodayButton());
  }

  /**
   * Stores if today button is enabled or not in date criteria
   *
   * @param showTodayButton Show today button
   */
  public void setShowTodayButton(String showTodayButton) {
    this.showTodayButton = showTodayButton;
  }

  /**
   * Get the minimum view mode of date criteria
   *
   * @return the minimum view mode of date criteria
   */
  @JsonGetter("dateViewMode")
  public String getDateViewMode() {
    return dateViewMode;
  }

  /**
   * Stores the minimum view mode of date criteria
   *
   * @param dateViewMode Date view mode
   */
  public void setDateViewMode(String dateViewMode) {
    this.dateViewMode = dateViewMode;
  }

  /**
   * @return the leftLabel
   */
  @JsonGetter("leftLabel")
  public String getLeftLabel() {
    return leftLabel;
  }

  /**
   * @param leftLabel the leftLabel to set
   */
  public void setLeftLabel(String leftLabel) {
    this.leftLabel = leftLabel;
  }

  /**
   * Get elementGroup
   *
   * @return key
   */
  @JsonIgnore
  public String getElementGroup() {
    return this.getGroup();
  }

  @JsonIgnore
  @Override
  public String getHelpTemplate() {
    // Retrieve code
    if (AweConstants.HIDDEN_COMPONENT.equalsIgnoreCase(getComponentType())) {
      return AweConstants.TEMPLATE_HELP_EMPTY;
    } else {
      return AweConstants.TEMPLATE_HELP_CRITERION;
    }
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
    if (getComponentType() != null && isPrintable() && !"hidden".equalsIgnoreCase(getComponentType())) {
      printElementList.add(this);
    }
    return printElementList;
  }
}
