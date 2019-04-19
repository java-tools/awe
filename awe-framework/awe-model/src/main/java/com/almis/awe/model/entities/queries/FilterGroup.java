/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * File Imports
 */

/**
 * FilterGroup Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group. Generates a list of filters
 * Can contain filter lists or filters
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamInclude({FilterAnd.class, FilterOr.class})
public abstract class FilterGroup extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 2852710192340831798L;

  /* Filter group */
  @XStreamImplicit
  private List<FilterGroup> filterGroupList;

  /* Filter list */
  @XStreamImplicit
  private List<Filter> filterList;

  /* Union type */
  @XStreamOmitField
  protected String union;

  /**
   * Default constructor
   */
  public FilterGroup() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public FilterGroup(FilterGroup other) throws AWException {
    super(other);
    this.filterGroupList = ListUtil.copyList(other.filterGroupList);
    this.filterList = ListUtil.copyList(other.filterList);
  }

  /**
   * Returns the filter group list
   *
   * @return Filter group list
   */
  public List<FilterGroup> getFilterGroupList() {
    return filterGroupList;
  }

  /**
   * Stores the filter group list
   *
   * @param filterGroupList Filter group list
   */
  public void setFilterGroupList(List<FilterGroup> filterGroupList) {
    this.filterGroupList = filterGroupList;
  }

  /**
   * Returns the filter list
   *
   * @return Filter list
   */
  public List<Filter> getFilterList() {
    return filterList;
  }

  /**
   * Stores the filter list
   *
   * @param filterList Filter list
   */
  public void setFilterList(List<Filter> filterList) {
    this.filterList = filterList;
  }

  /**
   * Returns the union type
   *
   * @return Union type
   */
  public String getUnion() {
    return union;
  }

  /**
   * Stores the union type
   *
   * @param union Union type
   */
  public void setUnion(String union) {
    this.union = union;
  }

  @Override
  public String toString() {
    // Generate full filter group
    List<Object> filterGroupAll = new ArrayList<>();
    if (getFilterList() != null) {
      filterGroupAll.addAll(getFilterList());
    }
    if (getFilterGroupList() != null) {
      filterGroupAll.addAll(getFilterGroupList());
    }
    return StringUtils.join(filterGroupAll, " " + getUnion().toLowerCase() + " ");
  }
}
