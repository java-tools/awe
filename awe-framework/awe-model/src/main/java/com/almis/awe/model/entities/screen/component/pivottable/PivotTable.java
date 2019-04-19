/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.component.pivottable;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.screen.component.grid.Grid;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * PivotTable Class
 *
 * Used to parse a pivot table tag with XStream
 *
 *
 * Generates an screen pivot table
 *
 *
 * @author Pablo Vidal - 12/JUN/2015
 */
@XStreamAlias("pivot-table")
public class PivotTable extends Grid {

  private static final long serialVersionUID = 8283520541161175828L;

  // Total column placement
  @XStreamAlias("total-column-placement")
  @XStreamAsAttribute
  private String totalColumnPlacement = null;

  // Total row placement
  @XStreamAlias("total-row-placement")
  @XStreamAsAttribute
  private String totalRowPlacement = null;

  // Renderer
  @XStreamAlias("renderer")
  @XStreamAsAttribute
  private String renderer = null;

  // Aggregator
  @XStreamAlias("aggregator")
  @XStreamAsAttribute
  private String aggregator = null;

  // Value
  @XStreamAlias("aggregation-field")
  @XStreamAsAttribute
  private String aggregationField = null;

  // Sort method
  @XStreamAlias("sort-method")
  @XStreamAsAttribute
  private String sortMethod = null;

  // Pivot numeric options: number of decimals
  @XStreamAlias("decimal-numbers")
  @XStreamAsAttribute
  private Integer decimalNumbers = null;

  // Pivot numeric options: separator of thousands
  @XStreamAlias("thousand-separator")
  @XStreamAsAttribute
  private String thousandSeparator = null;

  // Pivot numeric options: separator of decimal numbers
  @XStreamAlias("decimal-separator")
  @XStreamAsAttribute
  private String decimalSeparator = null;

  // Columns
  @XStreamAlias("cols")
  @XStreamAsAttribute
  private String cols = null;

  // Rows
  @XStreamAlias("rows")
  @XStreamAsAttribute
  private String rows = null;

  /**
   * Default constructor
   */
  public PivotTable() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public PivotTable(PivotTable other) throws AWException {
    super(other);
    this.totalColumnPlacement = other.totalColumnPlacement;
    this.totalRowPlacement = other.totalRowPlacement;
    this.renderer = other.renderer;
    this.aggregator = other.aggregator;
    this.aggregationField = other.aggregationField;
    this.sortMethod = other.sortMethod;
    this.decimalNumbers = other.decimalNumbers;
    this.thousandSeparator = other.thousandSeparator;
    this.decimalSeparator = other.decimalSeparator;
    this.cols = other.cols;
    this.rows = other.rows;
  }

  @Override
  public PivotTable copy() throws AWException {
    return new PivotTable(this);
  }

  /**
   * @return the totalColumnPlacement
   */
  public String getTotalColumnPlacement() {
    return totalColumnPlacement;
  }

  /**
   * @param totalColumnPlacement the totalColumnPlacement to set
   */
  public void setTotalColumnPlacement(String totalColumnPlacement) {
    this.totalColumnPlacement = totalColumnPlacement;
  }

  /**
   * @return the totalRowPlacement
   */
  public String getTotalRowPlacement() {
    return totalRowPlacement;
  }

  /**
   * @param totalRowPlacement the totalRowPlacement to set
   */
  public void setTotalRowPlacement(String totalRowPlacement) {
    this.totalRowPlacement = totalRowPlacement;
  }

  /**
   * @return the renderer
   */
  public String getRenderer() {
    return renderer;
  }

  /**
   * @param renderer the renderer to set
   */
  public void setRenderer(String renderer) {
    this.renderer = renderer;
  }

  /**
   * @return the aggregator
   */
  public String getAggregator() {
    return aggregator;
  }

  /**
   * @param aggregator the aggregator to set
   */
  public void setAggregator(String aggregator) {
    this.aggregator = aggregator;
  }

  /**
   * @return the aggregationField
   */
  public String getAggregationField() {
    return aggregationField;
  }

  /**
   * @param aggregationField the aggregationField to set
   */
  public void setAggregationField(String aggregationField) {
    this.aggregationField = aggregationField;
  }

  /**
   * @return the sortMethod
   */
  public String getSortMethod() {
    return sortMethod;
  }

  /**
   * @param sortMethod the sortMethod to set
   */
  public void setSortMethod(String sortMethod) {
    this.sortMethod = sortMethod;
  }

  /**
   * @return the decimalNumbers
   */
  public Integer getDecimalNumbers() {
    return decimalNumbers;
  }

  /**
   * @param decimalNumbers the decimalNumbers to set
   */
  public void setDecimalNumbers(Integer decimalNumbers) {
    this.decimalNumbers = decimalNumbers;
  }

  /**
   * @return the thousandSeparator
   */
  public String getThousandSeparator() {
    return thousandSeparator;
  }

  /**
   * @param thousandSeparator the thousandSeparator to set
   */
  public void setThousandSeparator(String thousandSeparator) {
    this.thousandSeparator = thousandSeparator;
  }

  /**
   * @return the decimalSeparator
   */
  public String getDecimalSeparator() {
    return decimalSeparator;
  }

  /**
   * @param decimalSeparator the decimalSeparator to set
   */
  public void setDecimalSeparator(String decimalSeparator) {
    this.decimalSeparator = decimalSeparator;
  }

  /**
   * @return the column group names
   */
  public String getCols() {
    return cols;
  }

  /**
   * @param cols set column names for init group
   */
  public void setCols(String cols) {
    this.cols = cols;
  }

  /**
   * @return the rows group names
   */
  public String getRows() {
    return rows;
  }

  /**
   * @param rows set rows names for init group
   */
  public void setRows(String rows) {
    this.rows = rows;
  }

  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public String getComponentTag() {
    return "pivot-table";
  }
}
