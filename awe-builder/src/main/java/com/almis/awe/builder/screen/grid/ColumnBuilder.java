/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen.grid;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.AweBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.grid.Column;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class ColumnBuilder extends AweBuilder<ColumnBuilder> {

  private Align align;
  private Component component;
  private DateViewMode dateViewMode;
  private InitialLoad initialLoad;
  private Printable printable;
  private ServerAction serverAction;
  private DataType dataType;
  private String help;
  private String helpImage;
  private String name;
  private String dateFormat;
  private String dialog;
  private String formatOptions;
  private String formatter;
  private String icon;
  private String label;
  private String numberFormat;
  private String sortField;
  private String style;
  private String summaryType;
  private String targetAction;
  private String unit;
  private String validation;
  private String value;
  private Boolean autorefresh;
  private Boolean checkInitial;
  private Boolean checked;
  private Boolean dateShowTodayButton;
  private Boolean frozen;
  private Boolean hidden;
  private Boolean optional;
  private Boolean pageBreak;
  private Boolean readonly;
  private Boolean sendable;
  private Boolean showFutureDates;
  private Boolean showWeekends;
  private Boolean sortable;
  private Boolean strict;
  private Boolean visible;
  private Integer charlength;
  private Integer max;
  private Integer width;

  private List<DependencyBuilder> dependencyList;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public ColumnBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    dependencyList = new ArrayList<>();
  }

  @Override
  public ColumnBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    Column column = new Column();
    column.setId(getId());
    column.setHelp(getHelp());
    column.setHelpImage(getHelpImage());
    column.setName(getName());
    column.setDateFormat(getDateFormat());
    column.setDialog(getDialog());
    column.setFormatOptions(getFormatOptions());
    column.setFormatter(getFormatter());
    column.setIcon(getIcon());
    column.setLabel(getLabel());
    column.setNumberFormat(getNumberFormat());
    column.setField(getSortField());
    column.setStyle(getStyle());
    column.setSummaryType(getSummaryType());
    column.setTargetAction(getTargetAction());
    column.setUnit(getUnit());
    column.setValidation(getValidation());
    column.setValue(getValue());

    column.setAutorefresh(getValueAsString(isAutorefresh()));
    column.setCheckInitial(getValueAsString(isCheckInitial()));
    column.setChecked(getValueAsString(isChecked()));
    column.setShowTodayButton(getValueAsString(isDateShowTodayButton()));
    column.setFrozen(getValueAsString(isFrozen()));
    column.setHidden(getValueAsString(isHidden()));
    column.setOptional(getValueAsString(isOptional()));
    column.setPagebreak(getValueAsString(isPageBreak()));
    column.setReadonly(getValueAsString(isReadonly()));
    column.setSendable(getValueAsString(isSendable()));
    column.setShowFutureDates(getValueAsString(isShowFutureDates()));
    column.setShowWeekends(getValueAsString(isShowWeekends()));
    column.setSortable(getValueAsString(isSortable()));
    column.setStrict(getValueAsString(isStrict()));
    column.setVisible(getValueAsString(isVisible()));

    column.setCharLength(getValueAsString(getCharlength()));
    column.setMax(getValueAsString(getMax()));
    column.setWidth(getValueAsString(getWidth()));

    if (getAlign() != null) {
      column.setAlign(getAlign().toString());
    }

    if (getComponent() != null) {
      column.setComponentType(getComponent().toString());
    }

    if (getDateViewMode() != null) {
      column.setDateViewMode(getDateViewMode().toString());
    }

    if (getInitialLoad() != null) {
      column.setInitialLoad(getInitialLoad().toString());
    }

    if (getPrintable() != null) {
      column.setPrintable(getPrintable().toString());
    }

    if (getServerAction() != null) {
      column.setServerAction(getServerAction().toString());
    }

    if (getDataType() != null) {
      column.setType(getDataType().toString());
    }

    for (DependencyBuilder dependencyBuilder : getDependencyList()) {
      addElement(column, dependencyBuilder.build(column));
    }

    return column;
  }

  /**
   * Get align
   *
   * @return
   */
  public Align getAlign() {
    return align;
  }

  /**
   * Set align
   *
   * @param align
   * @return
   */
  public ColumnBuilder setAlign(Align align) {
    this.align = align;
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
  public ColumnBuilder setComponent(Component component) {
    this.component = component;
    return this;
  }

  /**
   * Get date view
   *
   * @return
   */
  public DateViewMode getDateViewMode() {
    return dateViewMode;
  }

  /**
   * Set date view
   *
   * @param dateViewMode
   * @return
   */
  public ColumnBuilder setDateViewMode(DateViewMode dateViewMode) {
    this.dateViewMode = dateViewMode;
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
  public ColumnBuilder setInitialLoad(InitialLoad initialLoad) {
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
  public ColumnBuilder setPrintable(Printable printable) {
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
  public ColumnBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get data type
   *
   * @return
   */
  public DataType getDataType() {
    return dataType;
  }

  /**
   * Set data type
   *
   * @param dataType
   * @return
   */
  public ColumnBuilder setDataType(DataType dataType) {
    this.dataType = dataType;
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
  public ColumnBuilder setHelp(String help) {
    this.help = help;
    return this;
  }

  /***
   * get help image
   *
   * @return
   */
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * set help image
   *
   * @param helpImage
   * @return
   */
  public ColumnBuilder setHelpImage(String helpImage) {
    this.helpImage = helpImage;
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
   * @return
   */
  public ColumnBuilder setName(String name) {
    this.name = name;
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
  public ColumnBuilder setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return this;
  }

  /**
   * Get dialog
   *
   * @return
   */
  public String getDialog() {
    return dialog;
  }

  /**
   * Set dialog
   *
   * @param dialog
   * @return
   */
  public ColumnBuilder setDialog(String dialog) {
    this.dialog = dialog;
    return this;
  }

  /**
   * Get format options
   *
   * @return
   */
  public String getFormatOptions() {
    return formatOptions;
  }

  /**
   * Set format options
   *
   * @param formatOptions
   * @return
   */
  public ColumnBuilder setFormatOptions(String formatOptions) {
    this.formatOptions = formatOptions;
    return this;
  }

  /**
   * Get formatter
   *
   * @return
   */
  public String getFormatter() {
    return formatter;
  }

  /**
   * Set formatter
   *
   * @param formatter
   * @return
   */
  public ColumnBuilder setFormatter(String formatter) {
    this.formatter = formatter;
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
  public ColumnBuilder setIcon(String icon) {
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
  public ColumnBuilder setLabel(String label) {
    this.label = label;
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
  public ColumnBuilder setNumberFormat(String numberFormat) {
    this.numberFormat = numberFormat;
    return this;
  }

  /**
   * Get sort field
   *
   * @return
   */
  public String getSortField() {
    return sortField;
  }

  /**
   * Set sort field
   *
   * @param sortField
   * @return
   */
  public ColumnBuilder setSortField(String sortField) {
    this.sortField = sortField;
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
  public ColumnBuilder setStyle(String style) {
    this.style = style;
    return this;
  }

  /**
   * Get summary type
   *
   * @return
   */
  public String getSummaryType() {
    return summaryType;
  }

  /**
   * Set summary type
   *
   * @param summaryType
   * @return
   */
  public ColumnBuilder setSummaryType(String summaryType) {
    this.summaryType = summaryType;
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
   * set target action
   *
   * @param targetAction
   * @return
   */
  public ColumnBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * get unit
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
  public ColumnBuilder setUnit(String unit) {
    this.unit = unit;
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
  public ColumnBuilder setValidation(String validation) {
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
   * set value
   *
   * @param value
   * @return
   */
  public ColumnBuilder setValue(String value) {
    this.value = value;
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
  public ColumnBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
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
  public ColumnBuilder setCheckInitial(Boolean checkInitial) {
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
  public ColumnBuilder setChecked(Boolean checked) {
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
  public ColumnBuilder setDateShowTodayButton(Boolean dateShowTodayButton) {
    this.dateShowTodayButton = dateShowTodayButton;
    return this;
  }

  /**
   * Is frozen
   *
   * @return
   */
  public Boolean isFrozen() {
    return frozen;
  }

  /**
   * set frozen
   *
   * @param frozen
   * @return
   */
  public ColumnBuilder setFrozen(Boolean frozen) {
    this.frozen = frozen;
    return this;
  }

  /**
   * Is hidden
   *
   * @return
   */
  public Boolean isHidden() {
    return hidden;
  }

  /**
   * Set hidden
   *
   * @param hidden
   * @return
   */
  public ColumnBuilder setHidden(Boolean hidden) {
    this.hidden = hidden;
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
  public ColumnBuilder setOptional(Boolean optional) {
    this.optional = optional;
    return this;
  }

  /**
   * Is page break
   *
   * @return
   */
  public Boolean isPageBreak() {
    return pageBreak;
  }

  /**
   * Set page break
   *
   * @param pageBreak
   * @return
   */
  public ColumnBuilder setPageBreak(Boolean pageBreak) {
    this.pageBreak = pageBreak;
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
  public ColumnBuilder setReadonly(Boolean readonly) {
    this.readonly = readonly;
    return this;
  }

  /**
   * Is sendable
   *
   * @return
   */
  public Boolean isSendable() {
    return sendable;
  }

  /**
   * set sendable
   *
   * @param sendable
   * @return
   */
  public ColumnBuilder setSendable(Boolean sendable) {
    this.sendable = sendable;
    return this;
  }

  /**
   * Checks if future dates need to be shown
   *
   * @return
   */
  public Boolean isShowFutureDates() {
    return showFutureDates;
  }

  /**
   * Sets if future dates need to be shown
   *
   * @param showFutureDates
   * @return
   */
  public ColumnBuilder setShowFutureDates(Boolean showFutureDates) {
    this.showFutureDates = showFutureDates;
    return this;
  }

  /**
   * Checks if weekends need to be shown
   *
   * @return
   */
  public Boolean isShowWeekends() {
    return showWeekends;
  }

  /**
   * Sets if weekend need to be shown
   *
   * @param showWeekends
   * @return
   */
  public ColumnBuilder setShowWeekends(Boolean showWeekends) {
    this.showWeekends = showWeekends;
    return this;
  }

  /**
   * Is sortable
   *
   * @return
   */
  public Boolean isSortable() {
    return sortable;
  }

  /**
   * Set sortable
   *
   * @param sortable
   * @return
   */
  public ColumnBuilder setSortable(Boolean sortable) {
    this.sortable = sortable;
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
   * set strict
   *
   * @param strict
   * @return
   */
  public ColumnBuilder setStrict(Boolean strict) {
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
  public ColumnBuilder setVisible(Boolean visible) {
    this.visible = visible;
    return this;
  }

  /**
   * Get char lenth
   *
   * @return
   */
  public Integer getCharlength() {
    return charlength;
  }

  /**
   * Set char length
   *
   * @param charlength
   * @return
   */
  public ColumnBuilder setCharlength(Integer charlength) {
    this.charlength = charlength;
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
  public ColumnBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get width
   *
   * @return
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * Set width
   *
   * @param width
   * @return
   */
  public ColumnBuilder setWidth(Integer width) {
    this.width = width;
    return this;
  }

  /**
   * Adds a dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public ColumnBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
      if (this.dependencyList == null) {
        this.dependencyList = new ArrayList<>();
      }
      this.dependencyList.addAll(Arrays.asList(dependencyBuilder));
    }
    return this;
  }

  /**
   * Get dependency list
   *
   * @return
   */
  public List<DependencyBuilder> getDependencyList() {
    return dependencyList;
  }

}
