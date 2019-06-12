package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.Component;
import com.almis.awe.builder.enumerates.Printable;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AbstractCriteriaBuilder;
import com.almis.awe.model.entities.screen.component.criteria.AbstractCriteria;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CriteriaAttributes<B extends AbstractCriteriaBuilder> extends AbstractAttributes<B> {
  private Component component;
  private Printable printable;
  private String unit;
  private Integer leftLabel;
  private String message;
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
  private boolean readonly;

  public CriteriaAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractCriteria> E asCriterion(E element) {
    E criterion = (E) element
      .setUnit(getUnit())
      .setLeftLabel(getLeftLabel())
      .setMessage(getMessage())
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
      .setReadonly(isReadonly());

    if (getComponent() != null) {
      criterion.setComponentType(getComponent().toString());
    }

    if (getPrintable() != null) {
      criterion.setPrintable(getPrintable().toString());
    }

    return criterion;
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
