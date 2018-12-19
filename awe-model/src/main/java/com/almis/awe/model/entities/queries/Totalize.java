
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.util.List;
import java.util.Map;

/*
 * File Imports
 */

/**
 * Totalize Class
 * Used to parse the file Queries.xml with XStream
 * Totalize from queries. Generates new rows with totals and subtotals
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("totalize")
public class Totalize extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -1703530186967387913L;

  // Totalize function (SUM, AVG, MAX, MIN)
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function = null;

  // Totalize label
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label = null;

  // Totalize field (where to put the total label)
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field = null;

  // Style field (TOTAL/SUBTOTAL)
  @XStreamAlias("style")
  @XStreamAsAttribute
  private String style = null;

  // Query totalize by fields list
  @XStreamImplicit
  private List<TotalizeBy> totalizeByList;

  // Query totalize fields list
  @XStreamImplicit
  private List<TotalizeField> totalizeFieldList;

  // Query totalize value list
  @XStreamOmitField
  private Map<String, CellData> totalizeValueList;

  // Query totalize by value list
  @XStreamOmitField
  private Map<String, String> totalizeByValueList;

  /**
   * Default constructor
   */
  public Totalize() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Totalize(Totalize other) throws AWException {
    super(other);
    this.function = other.function;
    this.label = other.label;
    this.field = other.field;
    this.style = other.style;
    this.totalizeByList = ListUtil.copyList(other.totalizeByList);
    this.totalizeFieldList = ListUtil.copyList(other.totalizeFieldList);
  }

  /**
   * Returns the totalize function
   *
   * @return Totalize function (SUM, AVG, MAX, MIN, CNT)
   */
  public String getFunction() {
    return function;
  }

  /**
   * Stores the totalize function
   *
   * @param function Totalize function (SUM, AVG, MAX, MIN)
   */
  public void setFunction(String function) {
    this.function = function;
  }

  /**
   * Returns the totalize label
   *
   * @return Totalize label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Stores the totalize label
   *
   * @param label Totalize label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the totalize field (where to put the total label)
   *
   * @return Totalize field
   */
  public String getField() {
    return field;
  }

  /**
   * Stores the totalize field (where to put the total label)
   *
   * @param field Totalize field
   */
  public void setField(String field) {
    this.field = field;
  }

  /**
   * Returns the list of the fields used to totalize
   *
   * @return List of totalize by fields
   */
  public List<TotalizeBy> getTotalizeByList() {
    return totalizeByList;
  }

  /**
   * Stores the list of the fields used to totalize
   *
   * @param totalizeByList List of totalize by fields
   */
  public void setTotalizeByList(List<TotalizeBy> totalizeByList) {
    this.totalizeByList = totalizeByList;
  }

  /**
   * Returns the list of the fields which are going to be totalized
   *
   * @return Fields to be totalized
   */
  public List<TotalizeField> getTotalizeFieldList() {
    return totalizeFieldList;
  }

  /**
   * Stores the list of the fields which are going to be totalized
   *
   * @param totalizeFieldList Fields to be totalized
   */
  public void setTotalizeFieldList(List<TotalizeField> totalizeFieldList) {
    this.totalizeFieldList = totalizeFieldList;
  }

  /**
   * Returns the temporary value list
   *
   * @return Temporary value list
   */
  public Map<String, CellData> getTotalizeValueList() {
    return totalizeValueList;
  }

  /**
   * Stores the temporary value list
   *
   * @param totalizeValueList Temporary value list
   */
  public void setTotalizeValueList(Map<String, CellData> totalizeValueList) {
    this.totalizeValueList = totalizeValueList;
  }

  /**
   * Returns the temporary by list
   *
   * @return Temporary by list
   */
  public Map<String, String> getTotalizeByValueList() {
    return totalizeByValueList;
  }

  /**
   * Stores the temporary by list
   *
   * @param totalizeByValueList Temporary by list
   */
  public void setTotalizeByValueList(Map<String, String> totalizeByValueList) {
    this.totalizeByValueList = totalizeByValueList;
  }

  /**
   * Returns the totalizer style
   *
   * @return Totalizer Style
   */
  public String getStyle() {
    return style;
  }

  /**
   * Stores the totalizer style
   *
   * @param style Totalizer style
   */
  public void setStyle(String style) {
    this.style = style;
  }

  @Override
  public Totalize copy() throws AWException {
    return new Totalize(this);
  }
}
