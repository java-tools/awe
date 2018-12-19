/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.criteria;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.criteria.Criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class CriteriaBuilder extends AweBuilder<CriteriaBuilder> {

  //TODO create a builder for each component, with specific properties for each one.
  private DateViewMode dateViewMode;
  private Component component;
  private InitialLoad initialLoad;
  private Printable printable;
  private ServerAction serverAction;
  private Integer areaRows;
  private Integer max;
  private Integer timeout;
  private String unit;
  private String help;
  private String helpImage;
  private String checkTarget;
  private String dateFormat;
  private String destination;
  private String group;
  private String icon;
  private String label;
  private String leftLabel;
  private String message;
  private String numberFormat;
  private String placeholder;
  private String property;
  private String session;
  private String size;
  private String specific;
  private String style;
  private String targetAction;
  private String validation;
  private String value;
  private String variable;
  private Boolean autoload;
  private Boolean autorefresh;
  private Boolean capitalize;
  private Boolean checkEmpty;
  private Boolean checkInitial;
  private Boolean checked;
  private Boolean dateShowTodayButton;
  private Boolean optional;
  private Boolean readonly;
  private Boolean showFutureDates;
  private Boolean showSlider;
  private Boolean showWeekends;
  private Boolean strict;
  private Boolean visible;

  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public CriteriaBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public CriteriaBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Criteria criteria = new Criteria();
    criteria.setId(getId());
    if (getDateViewMode() != null) {
      criteria.setDateViewMode(getDateViewMode().toString());
    }
    if (getComponent() != null) {
      criteria.setComponentType(getComponent().toString());
    }
    if (getInitialLoad() != null) {
      criteria.setInitialLoad(getInitialLoad().toString());
    }
    if (getPrintable() != null) {
      criteria.setPrintable(getPrintable().toString());
    }
    if (getServerAction() != null) {
      criteria.setServerAction(getServerAction().toString());
    }

    criteria.setAreaRows(getValueAsString(getAreaRows()));
    criteria.setMax(getValueAsString(getMax()));
    criteria.setTimeout(getValueAsString(getTimeout()));

    criteria.setUnit(getUnit());
    criteria.setHelp(getHelp());
    criteria.setHelpImage(getHelpImage());
    criteria.setCheckTarget(getCheckTarget());
    criteria.setDateFormat(getDateFormat());
    criteria.setDestination(getDestination());
    criteria.setGroup(getGroup());
    criteria.setIcon(getIcon());
    criteria.setLabel(getLabel());
    criteria.setLeftLabel(getLeftLabel());
    criteria.setMessage(getMessage());
    criteria.setNumberFormat(getNumberFormat());
    criteria.setPlaceholder(getPlaceholder());
    criteria.setProperty(getProperty());
    criteria.setSession(getSession());
    criteria.setSize(getSize());
    criteria.setSpecific(getSpecific());
    criteria.setStyle(getStyle());
    criteria.setTargetAction(getTargetAction());
    criteria.setValidation(getValidation());
    criteria.setValue(getValue());
    criteria.setVariable(getVariable());

    criteria.setAutoload(getValueAsString(isAutoload()));
    criteria.setAutorefresh(getValueAsString(isAutorefresh()));
    criteria.setCapitalize(getValueAsString(isCapitalize()));
    criteria.setCheckEmpty(getValueAsString(isCheckEmpty()));
    criteria.setCheckInitial(getValueAsString(isCheckInitial()));
    criteria.setChecked(getValueAsString(isChecked()));
    criteria.setShowTodayButton(getValueAsString(isDateShowTodayButton()));
    criteria.setOptional(getValueAsString(isOptional()));
    criteria.setReadonly(getValueAsString(isReadonly()));
    criteria.setShowFutureDates(getValueAsString(isShowFutureDates()));
    criteria.setShowSlider(getValueAsString(isShowSlider()));
    criteria.setShowWeekends(getValueAsString(isShowWeekends()));
    criteria.setStrict(getValueAsString(isStrict()));
    criteria.setVisible(getValueAsString(isVisible()));

    for (AweBuilder aweBuilder : elements) {
      addElement(criteria, aweBuilder.build(criteria));
    }

    return criteria;
  }

  /**
   * Get date view mode
   *
   * @return
   */
  public DateViewMode getDateViewMode() {
    return dateViewMode;
  }

  /**
   * Set date view mode
   *
   * @param dateViewMode
   * @return
   */
  public CriteriaBuilder setDateViewMode(DateViewMode dateViewMode) {
    this.dateViewMode = dateViewMode;
    return this;
  }

  /**
   * Get component
   *
   * @return
   */
  public Component getComponent() {
    return component;
  }

  /**
   * Set component
   *
   * @param component
   * @return
   */
  public CriteriaBuilder setComponent(Component component) {
    this.component = component;
    return this;
  }

  /**
   * Get initial load
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
  public CriteriaBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
    return this;
  }

  /**
   * Get printable
   *
   * @return
   */
  public Printable getPrintable() {
    return printable;
  }

  /**
   * Set printable
   *
   * @param printable
   * @return
   */
  public CriteriaBuilder setPrintable(Printable printable) {
    this.printable = printable;
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
   * Set server action
   *
   * @param serverAction
   * @return
   */
  public CriteriaBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get area rows
   *
   * @return
   */
  public Integer getAreaRows() {
    return areaRows;
  }

  /**
   * Set area rows
   *
   * @param areaRows
   * @return
   */
  public CriteriaBuilder setAreaRows(Integer areaRows) {
    this.areaRows = areaRows;
    return this;
  }

  /**
   * Get max
   *
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
  public CriteriaBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get timeout
   *
   * @return
   */
  public Integer getTimeout() {
    return timeout;
  }

  /**
   * Set timeout
   *
   * @param timeout
   * @return
   */
  public CriteriaBuilder setTimeout(Integer timeout) {
    this.timeout = timeout;
    return this;
  }

  /**
   * Get unit
   *
   * @return
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Set unit
   *
   * @param unit
   * @return
   */
  public CriteriaBuilder setUnit(String unit) {
    this.unit = unit;
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
  public CriteriaBuilder setHelp(String help) {
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
  public CriteriaBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
    return this;
  }

  /**
   * Get check target
   *
   * @return
   */
  public String getCheckTarget() {
    return checkTarget;
  }

  /**
   * Set check target
   *
   * @param checkTarget
   * @return
   */
  public CriteriaBuilder setCheckTarget(String checkTarget) {
    this.checkTarget = checkTarget;
    return this;
  }

  /**
   * Get date format
   *
   * @return
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Set date format
   *
   * @param dateFormat
   * @return
   */
  public CriteriaBuilder setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return this;
  }

  /**
   * Get destination
   *
   * @return
   */
  public String getDestination() {
    return destination;
  }

  /**
   * Set Destination
   *
   * @param destination
   * @return
   */
  public CriteriaBuilder setDestination(String destination) {
    this.destination = destination;
    return this;
  }

  /**
   * Get group
   *
   * @return
   */
  public String getGroup() {
    return group;
  }

  /**
   * Set group
   *
   * @param group
   * @return
   */
  public CriteriaBuilder setGroup(String group) {
    this.group = group;
    return this;
  }

  /**
   * Get icon
   *
   * @return
   */
  public String getIcon() {
    return icon;
  }

  /**
   * Set icon
   *
   * @param icon
   * @return
   */
  public CriteriaBuilder setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get label
   *
   * @return
   */
  public String getLabel() {
    return label;
  }

  /**
   * Set label
   *
   * @param label
   * @return
   */
  public CriteriaBuilder setLabel(String label) {
    this.label = label;
    return this;
  }

  /**
   * Get left label
   *
   * @return
   */
  public String getLeftLabel() {
    return leftLabel;
  }

  /**
   * Set left label
   *
   * @param leftLabel
   * @return
   */
  public CriteriaBuilder setLeftLabel(String leftLabel) {
    this.leftLabel = leftLabel;
    return this;
  }

  /**
   * Get message
   *
   * @return
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set message
   *
   * @param message
   * @return
   */
  public CriteriaBuilder setMessage(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get number format
   *
   * @return
   */
  public String getNumberFormat() {
    return numberFormat;
  }

  /**
   * Set number format
   *
   * @param numberFormat
   * @return
   */
  public CriteriaBuilder setNumberFormat(String numberFormat) {
    this.numberFormat = numberFormat;
    return this;
  }

  /**
   * Get placeholder
   *
   * @return
   */
  public String getPlaceholder() {
    return placeholder;
  }

  /**
   * Set placeholder
   *
   * @param placeholder
   * @return
   */
  public CriteriaBuilder setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
    return this;
  }

  /**
   * Get property
   *
   * @return
   */
  public String getProperty() {
    return property;
  }

  /**
   * Set property
   *
   * @param property
   * @return
   */
  public CriteriaBuilder setProperty(String property) {
    this.property = property;
    return this;
  }

  /**
   * Get session
   *
   * @return
   */
  public String getSession() {
    return session;
  }

  /**
   * Set session
   *
   * @param session
   * @return
   */
  public CriteriaBuilder setSession(String session) {
    this.session = session;
    return this;
  }

  /**
   * Get size
   *
   * @return
   */
  public String getSize() {
    return size;
  }

  /**
   * Set size
   *
   * @param size
   * @return
   */
  public CriteriaBuilder setSize(String size) {
    this.size = size;
    return this;
  }

  /**
   * Get specific
   *
   * @return
   */
  public String getSpecific() {
    return specific;
  }

  /**
   * Set specific
   *
   * @param specific
   * @return
   */
  public CriteriaBuilder setSpecific(String specific) {
    this.specific = specific;
    return this;
  }

  /**
   * Get style
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
  public CriteriaBuilder setStyle(String style) {
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
  public CriteriaBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get validation
   *
   * @return
   */
  public String getValidation() {
    return validation;
  }

  /**
   * Set validation
   *
   * @param validation
   * @return
   */
  public CriteriaBuilder setValidation(String validation) {
    this.validation = validation;
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
   * @return
   */
  public CriteriaBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  /**
   * Get variable
   *
   * @return
   */
  public String getVariable() {
    return variable;
  }

  /**
   * Set variable
   *
   * @param variable
   * @return
   */
  public CriteriaBuilder setVariable(String variable) {
    this.variable = variable;
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
  public CriteriaBuilder setAutoload(Boolean autoload) {
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
  public CriteriaBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Is capitalize
   *
   * @return
   */
  public Boolean isCapitalize() {
    return capitalize;
  }

  /**
   * Set capitalize
   *
   * @param capitalize
   * @return
   */
  public CriteriaBuilder setCapitalize(Boolean capitalize) {
    this.capitalize = capitalize;
    return this;
  }

  /**
   * Is check empty
   *
   * @return
   */
  public Boolean isCheckEmpty() {
    return checkEmpty;
  }

  /**
   * Set check empty
   *
   * @param checkEmpty
   * @return
   */
  public CriteriaBuilder setCheckEmpty(Boolean checkEmpty) {
    this.checkEmpty = checkEmpty;
    return this;
  }

  /**
   * Is check initial
   *
   * @return
   */
  public Boolean isCheckInitial() {
    return checkInitial;
  }

  /**
   * Set check initial
   *
   * @param checkInitial
   * @return
   */
  public CriteriaBuilder setCheckInitial(Boolean checkInitial) {
    this.checkInitial = checkInitial;
    return this;
  }

  /**
   * Is checked
   *
   * @return
   */
  public Boolean isChecked() {
    return checked;
  }

  /**
   * Set checked
   *
   * @param checked
   * @return
   */
  public CriteriaBuilder setChecked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  /**
   * Checks if today button needs to be shown
   *
   * @return
   */
  public Boolean isDateShowTodayButton() {
    return dateShowTodayButton;
  }

  /**
   * Sets if today button needs to be shown
   *
   * @param dateShowTodayButton
   * @return
   */
  public CriteriaBuilder setDateShowTodayButton(Boolean dateShowTodayButton) {
    this.dateShowTodayButton = dateShowTodayButton;
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
   * @return
   */
  public CriteriaBuilder setOptional(Boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * Is readonly
   *
   * @return
   */
  public Boolean isReadonly() {
    return readonly;
  }

  /**
   * Set readonly
   *
   * @param readonly
   * @return
   */
  public CriteriaBuilder setReadonly(Boolean readonly) {
    this.readonly = readonly;
    return this;
  }

  /**
   * Check if future dates need to be shown
   *
   * @return
   */
  public Boolean isShowFutureDates() {
    return showFutureDates;
  }

  /**
   * Set if future dates need to be shown
   *
   * @param showFutureDates
   * @return
   */
  public CriteriaBuilder setShowFutureDates(Boolean showFutureDates) {
    this.showFutureDates = showFutureDates;
    return this;
  }

  /**
   * Is show slider
   *
   * @return
   */
  public Boolean isShowSlider() {
    return showSlider;
  }

  /**
   * Set show slider
   *
   * @param showSlider
   * @return
   */
  public CriteriaBuilder setShowSlider(Boolean showSlider) {
    this.showSlider = showSlider;
    return this;
  }

  /**
   * Checks if weekends needs to be shown
   *
   * @return
   */
  public Boolean isShowWeekends() {
    return showWeekends;
  }

  /**
   * Set if weekends needs to be shown
   *
   * @param showWeekends
   * @return
   */
  public CriteriaBuilder setShowWeekends(Boolean showWeekends) {
    this.showWeekends = showWeekends;
    return this;
  }

  /**
   * Is strict
   *
   * @return
   */
  public Boolean isStrict() {
    return strict;
  }

  /**
   * Set strict
   *
   * @param strict
   * @return
   */
  public CriteriaBuilder setStrict(Boolean strict) {
    this.strict = strict;
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
  public CriteriaBuilder setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  /**
   * Add context button
   *
   * @param contextButton
   * @return
   */
  public CriteriaBuilder addContextButton(ContextButtonBuilder... contextButton) {
    if (contextButton != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
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
  public CriteriaBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparator) {
    if (contextSeparator != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
      this.elements.addAll(Arrays.asList(contextSeparator));
    }
    return this;
  }

  /**
   * Add dependencies
   *
   * @param dependencyBuilder
   * @return
   */
  public CriteriaBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      if (this.elements == null) {
        this.elements = new ArrayList<>();
      }
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
    return this.elements;
  }
}
