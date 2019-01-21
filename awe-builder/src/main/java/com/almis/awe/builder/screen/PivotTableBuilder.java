/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.context.ContextButtonBuilder;
import com.almis.awe.builder.screen.context.ContextSeparatorBuilder;
import com.almis.awe.builder.screen.dependency.DependencyBuilder;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author dfuentes
 */
public class PivotTableBuilder extends AweBuilder<PivotTableBuilder> {

  private InitialLoad initialLoad;
  private ServerAction serverAction;
  private Aggregator aggregator;
  private Renderer renderer;
  private SortMethod sortMethod;
  private TotalColumnPlacement totalColumnPlacement;
  private TotalRowPlacement totalRowPlacement;
  private String aggregationField;
  private String decimalSeparator;
  private String style;
  private String targetAction;
  private String thousandSeparator;
  private Integer cols;
  private Integer max;
  private Integer decimalNumbers;
  private Integer rows;
  private Boolean autoload;
  private Boolean autorefresh;
  private List<AweBuilder> elements;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public PivotTableBuilder() throws AWException {
    super();
  }

  @Override
  public void initializeElements() {
    elements = new ArrayList<>();
  }

  @Override
  public PivotTableBuilder setParent() {
    return this;
  }

  @Override
  public Element build(Element element) {
    PivotTable pivotTable = new PivotTable();
    pivotTable.setId(getId());
    pivotTable.setAggregationField(getAggregationField());
    pivotTable.setDecimalSeparator(getDecimalSeparator());
    pivotTable.setStyle(getStyle());
    pivotTable.setTargetAction(getTargetAction());
    pivotTable.setThousandSeparator(getThousandSeparator());
    pivotTable.setDecimalNumbers(getDecimalNumbers());

    if (getInitialLoad() != null) {
      pivotTable.setInitialLoad(getInitialLoad().toString());
    }

    if (getServerAction() != null) {
      pivotTable.setServerAction(getServerAction().toString());
    }

    if (getAggregator() != null) {
      pivotTable.setAggregator(getAggregator().toString());
    }

    if (getRenderer() != null) {
      pivotTable.setRenderer(getRenderer().toString());
    }

    if (getSortMethod() != null) {
      pivotTable.setSortMethod(getSortMethod().toString());
    }

    if (getTotalColumnPlacement() != null) {
      pivotTable.setTotalColumnPlacement(getTotalColumnPlacement().toString());
    }

    if (getTotalRowPlacement() != null) {
      pivotTable.setTotalRowPlacement(getTotalRowPlacement().toString());
    }

    if (getCols() != null) {
      pivotTable.setCols(String.valueOf(getCols()));
    }

    if (getMax() != null) {
      pivotTable.setMax(String.valueOf(getMax()));
    }

    if (getRows() != null) {
      pivotTable.setRows(String.valueOf(getRows()));
    }

    if (isAutoload() != null) {
      pivotTable.setAutoload(String.valueOf(isAutoload()));
    }

    if (isAutorefresh() != null) {
      pivotTable.setAutorefresh(String.valueOf(isAutorefresh()));
    }

    for (AweBuilder aweBuilder : elements) {
      addElement(pivotTable, aweBuilder.build(pivotTable));
    }

    return pivotTable;
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
  public PivotTableBuilder setInitialLoad(InitialLoad initialLoad) {
    this.initialLoad = initialLoad;
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
  public PivotTableBuilder setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return this;
  }

  /**
   * Get aggregator
   *
   * @return
   */
  public Aggregator getAggregator() {
    return aggregator;
  }

  /**
   * Set aggregator
   *
   * @param aggregator
   * @return
   */
  public PivotTableBuilder setAggregator(Aggregator aggregator) {
    this.aggregator = aggregator;
    return this;
  }

  /**
   * Get renderer
   *
   * @return
   */
  public Renderer getRenderer() {
    return renderer;
  }

  /**
   * Set renderer
   *
   * @param renderer
   * @return
   */
  public PivotTableBuilder setRenderer(Renderer renderer) {
    this.renderer = renderer;
    return this;
  }

  /**
   * Get sort method
   *
   * @return
   */
  public SortMethod getSortMethod() {
    return sortMethod;
  }

  /**
   * Set sort method
   *
   * @param sortMethod
   * @return
   */
  public PivotTableBuilder setSortMethod(SortMethod sortMethod) {
    this.sortMethod = sortMethod;
    return this;
  }

  /**
   * Get total column placement
   *
   * @return
   */
  public TotalColumnPlacement getTotalColumnPlacement() {
    return totalColumnPlacement;
  }

  /**
   * Set total column placement
   *
   * @param totalColumnPlacement
   * @return
   */
  public PivotTableBuilder setTotalColumnPlacement(TotalColumnPlacement totalColumnPlacement) {
    this.totalColumnPlacement = totalColumnPlacement;
    return this;
  }

  /**
   * Get total row placement
   *
   * @return
   */
  public TotalRowPlacement getTotalRowPlacement() {
    return totalRowPlacement;
  }

  /**
   * Set total row placement
   *
   * @param totalRowPlacement
   * @return
   */
  public PivotTableBuilder setTotalRowPlacement(TotalRowPlacement totalRowPlacement) {
    this.totalRowPlacement = totalRowPlacement;
    return this;
  }

  /**
   * Get aggregation field
   *
   * @return
   */
  public String getAggregationField() {
    return aggregationField;
  }

  /**
   * Set aggregation field
   *
   * @param aggregationField
   * @return
   */
  public PivotTableBuilder setAggregationField(String aggregationField) {
    this.aggregationField = aggregationField;
    return this;
  }

  /**
   * Get decimal separator
   *
   * @return
   */
  public String getDecimalSeparator() {
    return decimalSeparator;
  }

  /**
   * Set decimal separator
   *
   * @param decimalSeparator
   * @return
   */
  public PivotTableBuilder setDecimalSeparator(String decimalSeparator) {
    this.decimalSeparator = decimalSeparator;
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
  public PivotTableBuilder setStyle(String style) {
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
  public PivotTableBuilder setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return this;
  }

  /**
   * Get thousand separator
   *
   * @return
   */
  public String getThousandSeparator() {
    return thousandSeparator;
  }

  /**
   * Set thousand separator
   *
   * @param thousandSeparator
   * @return
   */
  public PivotTableBuilder setThousandSeparator(String thousandSeparator) {
    this.thousandSeparator = thousandSeparator;
    return this;
  }

  /**
   * Get columns
   *
   * @return
   */
  public Integer getCols() {
    return cols;
  }

  /**
   * Set columns
   *
   * @param cols
   * @return
   */
  public PivotTableBuilder setCols(Integer cols) {
    this.cols = cols;
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
  public PivotTableBuilder setMax(Integer max) {
    this.max = max;
    return this;
  }

  /**
   * Get decimal numbers
   *
   * @return
   */
  public Integer getDecimalNumbers() {
    return decimalNumbers;
  }

  /**
   * Set decimal numbers
   *
   * @param decimalNumbers
   * @return
   */
  public PivotTableBuilder setDecimalNumbers(Integer decimalNumbers) {
    this.decimalNumbers = decimalNumbers;
    return this;
  }

  /**
   * Get rows
   *
   * @return
   */
  public Integer getRows() {
    return rows;
  }

  /**
   * Set rows
   *
   * @param rows
   * @return
   */
  public PivotTableBuilder setRows(Integer rows) {
    this.rows = rows;
    return this;
  }

  /**
   * is autoload
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
  public PivotTableBuilder setAutoload(Boolean autoload) {
    this.autoload = autoload;
    return this;
  }

  /**
   * is autorefresh
   *
   * @return
   */
  public Boolean isAutorefresh() {
    return autorefresh;
  }

  /**
   * set autorefresh
   *
   * @param autorefresh
   * @return
   */
  public PivotTableBuilder setAutorefresh(Boolean autorefresh) {
    this.autorefresh = autorefresh;
    return this;
  }

  /**
   * Add context button
   *
   * @param contextButtonBuilders
   * @return
   */
  public PivotTableBuilder addContextButton(ContextButtonBuilder... contextButtonBuilders) {
    if (contextButtonBuilders != null) {
      this.elements.addAll(Arrays.asList(contextButtonBuilders));
    }
    return this;
  }

  /**
   * Add context separator
   *
   * @param contextSeparatorBuilders
   * @return
   */
  public PivotTableBuilder addContextSeparator(ContextSeparatorBuilder... contextSeparatorBuilders) {
    if (contextSeparatorBuilders != null) {
      this.elements.addAll(Arrays.asList(contextSeparatorBuilders));
    }
    return this;
  }

  /**
   * Add dependency
   *
   * @param dependencyBuilder
   * @return
   */
  public PivotTableBuilder addDependency(DependencyBuilder... dependencyBuilder) {
    if (dependencyBuilder != null) {
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
    return elements;
  }

}
