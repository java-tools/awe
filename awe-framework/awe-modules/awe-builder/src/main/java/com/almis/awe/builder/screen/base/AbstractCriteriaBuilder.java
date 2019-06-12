package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.Printable;
import com.almis.awe.builder.screen.component.CriteriaAttributes;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter(AccessLevel.PRIVATE)
public abstract class AbstractCriteriaBuilder<T, I extends AbstractCriteria> extends AbstractComponentBuilder<T, I> {

  private CriteriaAttributes criteriaAttributes;

  public AbstractCriteriaBuilder() {
    this.criteriaAttributes = new CriteriaAttributes(this);
  }

  @Override
  public I build(I criterion) {
    super.build(criterion);
    getCriteriaAttributes().asCriterion(criterion);
    return criterion;
  }

  /**
   * Set the component
   *
   * @param component component
   * @return This
   */
  public T setComponent(Component component) {
    getCriteriaAttributes().setComponent(component);
    return (T) this;
  }

  /**
   * Set the printable flag
   *
   * @param printable printable flag
   * @return This
   */
  public T setPrintable(Printable printable) {
    getCriteriaAttributes().setPrintable(printable);
    return (T) this;
  }

  /**
   * Set the unit label
   *
   * @param unit unit label
   * @return This
   */
  public T setUnit(String unit) {
    getCriteriaAttributes().setUnit(unit);
    return (T) this;
  }

  /**
   * Set the left label width in chars
   *
   * @param leftLabel left label width in chars
   * @return This
   */
  public T setLeftLabel(Integer leftLabel) {
    getCriteriaAttributes().setLeftLabel(leftLabel);
    return (T) this;
  }

  /**
   * Set the message
   *
   * @param message message
   * @return This
   */
  public T setMessage(String message) {
    getCriteriaAttributes().setMessage(message);
    return (T) this;
  }

  /**
   * Set the placeholder
   *
   * @param placeholder placeholder
   * @return This
   */
  public T setPlaceholder(String placeholder) {
    getCriteriaAttributes().setPlaceholder(placeholder);
    return (T) this;
  }

  /**
   * Set the property
   *
   * @param property property
   * @return This
   */
  public T setProperty(String property) {
    getCriteriaAttributes().setProperty(property);
    return (T) this;
  }

  /**
   * Set the session
   *
   * @param session session
   * @return This
   */
  public T setSession(String session) {
    getCriteriaAttributes().setSession(session);
    return (T) this;
  }

  /**
   * Set the specific
   *
   * @param specific specific field
   * @return This
   */
  public T setSpecific(String specific) {
    getCriteriaAttributes().setSpecific(specific);
    return (T) this;
  }

  /**
   * Set the validation
   *
   * @param validation validation
   * @return This
   */
  public T setValidation(String validation) {
    getCriteriaAttributes().setValidation(validation);
    return (T) this;
  }

  /**
   * Set the value
   *
   * @param value value
   * @return This
   */
  public T setValue(String value) {
    getCriteriaAttributes().setValue(value);
    return (T) this;
  }

  /**
   * Set the variable
   *
   * @param variable variable
   * @return This
   */
  public T setVariable(String variable) {
    getCriteriaAttributes().setVariable(variable);
    return (T) this;
  }

  /**
   * Set the capitalize flag
   *
   * @param capitalize capitalize flag
   * @return This
   */
  public T setCapitalize(boolean capitalize) {
    getCriteriaAttributes().setCapitalize(capitalize);
    return (T) this;
  }

  /**
   * Set the checkEmpty flag
   *
   * @param checkEmpty checkEmpty flag
   * @return This
   */
  public T setCheckEmpty(boolean checkEmpty) {
    getCriteriaAttributes().setCheckEmpty(checkEmpty);
    return (T) this;
  }

  /**
   * Set the checkInitial flag
   *
   * @param checkInitial checkInitial flag
   * @return This
   */
  public T setCheckInitial(boolean checkInitial) {
    getCriteriaAttributes().setCheckInitial(checkInitial);
    return (T) this;
  }

  /**
   * Set the readonly flag
   *
   * @param readonly readonly flag
   * @return This
   */
  public T setReadonly(boolean readonly) {
    getCriteriaAttributes().setReadonly(readonly);
    return (T) this;
  }
}
