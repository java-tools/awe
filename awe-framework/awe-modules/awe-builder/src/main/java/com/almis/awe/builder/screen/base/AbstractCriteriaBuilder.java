package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.DateViewMode;
import com.almis.awe.builder.enumerates.Printable;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter
public abstract class AbstractCriteriaBuilder<T, I extends AbstractCriteria> extends AbstractComponentBuilder<T, I> {

  private DateViewMode dateViewMode;
  private Component component;
  private Printable printable;
  private Integer areaRows;
  private Integer timeout;
  private String unit;
  private String checkTarget;
  private String dateFormat;
  private String destination;
  private String group;
  private Integer leftLabel;
  private String message;
  private String numberFormat;
  private String placeholder;
  private String property;
  private String session;
  private String specific;
  private String validation;
  private String value;
  private String variable;
  private boolean capitalize;
  private boolean checkEmpty;
  private boolean checkInitial;
  private boolean checked;
  private boolean dateShowTodayButton;
  private boolean optional;
  private boolean readonly;
  private boolean showFutureDates;
  private boolean showSlider;
  private boolean showWeekends;
  private boolean strict;

  @Override
  public I build(I criterion) {
    super.build(criterion)
      .setAreaRows(getAreaRows())
      .setTimeout(getTimeout())
      .setUnit(getUnit())
      .setCheckTarget(getCheckTarget())
      .setDateFormat(getDateFormat())
      .setDestination(getDestination())
      .setGroup(getGroup())
      .setLeftLabel(getLeftLabel())
      .setMessage(getMessage())
      .setNumberFormat(getNumberFormat())
      .setPlaceholder(getPlaceholder())
      .setProperty(getProperty())
      .setSession(getSession())
      .setSpecific(getSpecific())
      .setValidation(getValidation())
      .setValue(getValue())
      .setVariable(getVariable())
      .setCapitalize(isCapitalize())
      .setCheckEmpty(isCheckEmpty())
      .setCheckInitial(isCheckInitial())
      .setChecked(isChecked())
      .setShowTodayButton(isDateShowTodayButton())
      .setOptional(isOptional())
      .setReadonly(isReadonly())
      .setShowFutureDates(isShowFutureDates())
      .setShowSlider(isShowSlider())
      .setShowWeekends(isShowWeekends())
      .setStrict(isStrict());

    if (getDateViewMode() != null) {
      criterion.setDateViewMode(getDateViewMode().toString());
    }
    if (getComponent() != null) {
      criterion.setComponentType(getComponent().toString());
    }
    if (getPrintable() != null) {
      criterion.setPrintable(getPrintable().toString());
    }

    return criterion;
  }

  /**
   * Set the dateViewMode
   *
   * @param dateViewMode dateViewMode
   * @return This
   */
  public T setDateViewMode(DateViewMode dateViewMode) {
    this.dateViewMode = dateViewMode;
    return (T) this;
  }

  /**
   * Set the component
   *
   * @param component component
   * @return This
   */
  public T setComponent(Component component) {
    this.component = component;
    return (T) this;
  }

  /**
   * Set the printable flag
   *
   * @param printable printable flag
   * @return This
   */
  public T setPrintable(Printable printable) {
    this.printable = printable;
    return (T) this;
  }

  /**
   * Set the areaRows
   *
   * @param areaRows areaRows
   * @return This
   */
  public T setAreaRows(Integer areaRows) {
    this.areaRows = areaRows;
    return (T) this;
  }

  /**
   * Set the timeout
   *
   * @param timeout timeout
   * @return This
   */
  public T setTimeout(Integer timeout) {
    this.timeout = timeout;
    return (T) this;
  }

  /**
   * Set the unit label
   *
   * @param unit unit label
   * @return This
   */
  public T setUnit(String unit) {
    this.unit = unit;
    return (T) this;
  }

  /**
   * Set the checkTarget
   *
   * @param checkTarget checkTarget
   * @return This
   */
  public T setCheckTarget(String checkTarget) {
    this.checkTarget = checkTarget;
    return (T) this;
  }

  /**
   * Set the dateFormat
   *
   * @param dateFormat dateFormat
   * @return This
   */
  public T setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return (T) this;
  }

  /**
   * Set the destination
   *
   * @param destination destination
   * @return This
   */
  public T setDestination(String destination) {
    this.destination = destination;
    return (T) this;
  }

  /**
   * Set the group
   *
   * @param group group
   * @return This
   */
  public T setGroup(String group) {
    this.group = group;
    return (T) this;
  }

  /**
   * Set the left label width in chars
   *
   * @param leftLabel left label width in chars
   * @return This
   */
  public T setLeftLabel(Integer leftLabel) {
    this.leftLabel = leftLabel;
    return (T) this;
  }

  /**
   * Set the message
   *
   * @param message message
   * @return This
   */
  public T setMessage(String message) {
    this.message = message;
    return (T) this;
  }

  /**
   * Set the numberFormat
   *
   * @param numberFormat numberFormat
   * @return This
   */
  public T setNumberFormat(String numberFormat) {
    this.numberFormat = numberFormat;
    return (T) this;
  }

  /**
   * Set the placeholder
   *
   * @param placeholder placeholder
   * @return This
   */
  public T setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
    return (T) this;
  }

  /**
   * Set the property
   *
   * @param property property
   * @return This
   */
  public T setProperty(String property) {
    this.property = property;
    return (T) this;
  }

  /**
   * Set the session
   *
   * @param session session
   * @return This
   */
  public T setSession(String session) {
    this.session = session;
    return (T) this;
  }

  /**
   * Set the specific
   *
   * @param specific specific field
   * @return This
   */
  public T setSpecific(String specific) {
    this.specific = specific;
    return (T) this;
  }

  /**
   * Set the validation
   *
   * @param validation validation
   * @return This
   */
  public T setValidation(String validation) {
    this.validation = validation;
    return (T) this;
  }

  /**
   * Set the value
   *
   * @param value value
   * @return This
   */
  public T setValue(String value) {
    this.value = value;
    return (T) this;
  }

  /**
   * Set the variable
   *
   * @param variable variable
   * @return This
   */
  public T setVariable(String variable) {
    this.variable = variable;
    return (T) this;
  }

  /**
   * Set the capitalize flag
   *
   * @param capitalize capitalize flag
   * @return This
   */
  public T setCapitalize(boolean capitalize) {
    this.capitalize = capitalize;
    return (T) this;
  }

  /**
   * Set the checkEmpty flag
   *
   * @param checkEmpty checkEmpty flag
   * @return This
   */
  public T setCheckEmpty(boolean checkEmpty) {
    this.checkEmpty = checkEmpty;
    return (T) this;
  }

  /**
   * Set the checkInitial flag
   *
   * @param checkInitial checkInitial flag
   * @return This
   */
  public T setCheckInitial(boolean checkInitial) {
    this.checkInitial = checkInitial;
    return (T) this;
  }

  /**
   * Set the checked flag
   *
   * @param checked checked flag
   * @return This
   */
  public T setChecked(boolean checked) {
    this.checked = checked;
    return (T) this;
  }

  /**
   * Set the dateShowTodayButton flag
   *
   * @param dateShowTodayButton dateShowTodayButton flag
   * @return This
   */
  public T setDateShowTodayButton(boolean dateShowTodayButton) {
    this.dateShowTodayButton = dateShowTodayButton;
    return (T) this;
  }

  /**
   * Set the optional flag
   *
   * @param optional optional flag
   * @return This
   */
  public T setOptional(boolean optional) {
    this.optional = optional;
    return (T) this;
  }

  /**
   * Set the readonly flag
   *
   * @param readonly readonly flag
   * @return This
   */
  public T setReadonly(boolean readonly) {
    this.readonly = readonly;
    return (T) this;
  }

  /**
   * Set the showFutureDates flag
   *
   * @param showFutureDates showFutureDates flag
   * @return This
   */
  public T setShowFutureDates(boolean showFutureDates) {
    this.showFutureDates = showFutureDates;
    return (T) this;
  }

  /**
   * Set the showSlider flag
   *
   * @param showSlider showSlider flag
   * @return This
   */
  public T setShowSlider(boolean showSlider) {
    this.showSlider = showSlider;
    return (T) this;
  }

  /**
   * Set the showWeekends flag
   *
   * @param showWeekends showWeekends flag
   * @return This
   */
  public T setShowWeekends(boolean showWeekends) {
    this.showWeekends = showWeekends;
    return (T) this;
  }

  /**
   * Set the strict flag
   *
   * @param strict strict flag
   * @return This
   */
  public T setStrict(boolean strict) {
    this.strict = strict;
    return (T) this;
  }
}
