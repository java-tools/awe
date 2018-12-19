/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.enumerates.Attribute;
import com.almis.awe.builder.enumerates.Condition;
import com.almis.awe.builder.enumerates.Event;
import com.almis.awe.builder.enumerates.View;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;

/**
 *
 * @author dfuentes
 */
public class DependencyElementBuilder extends AweBuilder<DependencyElementBuilder> {

  private Attribute attribute;
  private Attribute attribute2;
  private Condition condition;
  private Event event;
  private View view;
  private View view2;
  private String id2;
  private String alias;
  private String column;
  private String column2;
  private String name;
  private String row;
  private String value;
  private Boolean cancel;
  private Boolean checkChanges;
  private Boolean optional;

  /**
   * Cosntructor
   *
   * @throws AWException
   */
  public DependencyElementBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    //
  }

  @Override
  public DependencyElementBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    DependencyElement dependencyElement = new DependencyElement();
    dependencyElement.setId(getId());
    dependencyElement.setId2(getId2());
    dependencyElement.setAlias(getAlias());
    dependencyElement.setColumn(getColumn());
    dependencyElement.setColumn2(getColumn2());
    dependencyElement.setName(getName());
    dependencyElement.setRow(getRow());
    dependencyElement.setValue(getValue());

    dependencyElement.setCancel(getValueAsString(isCancel()));
    dependencyElement.setCheckChanges(getValueAsString(isCheckChanges()));
    dependencyElement.setOptional(getValueAsString(isOptional()));

    if (getAttribute() != null) {
      dependencyElement.setAttribute(getAttribute().toString());
    }

    if (getAttribute2() != null) {
      dependencyElement.setAttribute2(getAttribute2().toString());
    }

    if (getCondition() != null) {
      dependencyElement.setCondition(getCondition().toString());
    }

    if (getEvent() != null) {
      dependencyElement.setEvent(getEvent().toString());
    }

    if (getView() != null) {
      dependencyElement.setView(getView().toString());
    }

    if (getView2() != null) {
      dependencyElement.setView2(getView2().toString());
    }

    return dependencyElement;
  }

  /**
   * Get attribute
   *
   * @return
   */
  public Attribute getAttribute() {
    return attribute;
  }

  /**
   * Set attribute
   *
   * @param attribute
   */
  public DependencyElementBuilder setAttribute(Attribute attribute) {
    this.attribute = attribute;
    return this;
  }

  /**
   * Get second attribute
   *
   * @return
   */
  public Attribute getAttribute2() {
    return attribute2;
  }

  /**
   * Set second attribute
   *
   * @param attribute2
   */
  public DependencyElementBuilder setAttribute2(Attribute attribute2) {
    this.attribute2 = attribute2;
    return this;
  }

  /**
   * Get condition
   *
   * @return
   */
  public Condition getCondition() {
    return condition;
  }

  /**
   * Set condition
   *
   * @param condition
   */
  public DependencyElementBuilder setCondition(Condition condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Set event
   *
   * @return
   */
  public Event getEvent() {
    return event;
  }

  /**
   * Get event
   *
   * @param event
   */
  public DependencyElementBuilder setEvent(Event event) {
    this.event = event;
    return this;
  }

  /**
   * Get view
   *
   * @return
   */
  public View getView() {
    return view;
  }

  /**
   * Set view
   *
   * @param view
   */
  public DependencyElementBuilder setView(View view) {
    this.view = view;
    return this;
  }

  /**
   * Get second view
   *
   * @return
   */
  public View getView2() {
    return view2;
  }

  /**
   * Set second view
   *
   * @param view2
   */
  public DependencyElementBuilder setView2(View view2) {
    this.view2 = view2;
    return this;
  }

  /**
   * Get id 2
   *
   * @return
   */
  public String getId2() {
    return id2;
  }

  /**
   * Set id 2
   *
   * @param id2
   */
  public DependencyElementBuilder setId2(String id2) {
    this.id2 = id2;
    return this;
  }

  /**
   * Get alias
   *
   * @return
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Set alias
   *
   * @param alias
   */
  public DependencyElementBuilder setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  /**
   * Get column
   *
   * @return
   */
  public String getColumn() {
    return column;
  }

  /**
   * Set column
   *
   * @param column
   */
  public DependencyElementBuilder setColumn(String column) {
    this.column = column;
    return this;
  }

  /**
   * Get column 2
   *
   * @return
   */
  public String getColumn2() {
    return column2;
  }

  /**
   * Set column 2
   *
   * @param column2
   */
  public DependencyElementBuilder setColumn2(String column2) {
    this.column2 = column2;
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
   */
  public DependencyElementBuilder setName(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get row
   *
   * @return
   */
  public String getRow() {
    return row;
  }

  /**
   * Set row
   *
   * @param row
   */
  public DependencyElementBuilder setRow(String row) {
    this.row = row;
    return this;
  }

  /**
   * Get value
   *
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * Set value
   *
   * @param value
   */
  public DependencyElementBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Is cancel
   *
   * @return
   */
  public Boolean isCancel() {
    return cancel;
  }

  /**
   * Set cancel
   *
   * @param cancel
   */
  public DependencyElementBuilder setCancel(Boolean cancel) {
    this.cancel = cancel;
    return this;
  }

  /**
   * Is check changes
   *
   * @return
   */
  public Boolean isCheckChanges() {
    return checkChanges;
  }

  /**
   * Set check changes
   *
   * @param checkChanges
   */
  public DependencyElementBuilder setCheckChanges(Boolean checkChanges) {
    this.checkChanges = checkChanges;
    return this;
  }

  /**
   * Is optional
   *
   * @return
   */
  public Boolean isOptional() {
    return optional;
  }

  /**
   * Set optional
   *
   * @param optional
   */
  public DependencyElementBuilder setOptional(Boolean optional) {
    this.optional = optional;
    return this;
  }
}
