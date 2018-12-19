package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.type.JoinType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/*
 * File Imports
 */

/**
 * Join Class
 *
 * Used to parse the files Queries.xml with XStream
 *
 *
 * Generates a join with another table
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("join")
public class Join extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -2168129287969817829L;

  // Join type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  // Join table list
  private Table table;

  // Join filter group list
  @XStreamImplicit
  private List<FilterGroup> filterGroupList;

  /**
   * Default constructor
   */
  public Join() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Join(Join other) throws AWException {
    super(other);
    this.type = other.type;
    this.table = other.table == null ? null : new Table(other.table);
    this.filterGroupList = ListUtil.copyList(other.filterGroupList);
  }

  /**
   * Returns the join type
   *
   * @return Join type
   */
  public String getType() {
    return type;
  }

  /**
   * Retrieve join type as TYPE
   *
   * @return
   */
  public JoinType getJoinType() {
    return type == null ? JoinType.INNER : JoinType.valueOf(type);
  }

  /**
   * Stores the join type
   *
   * @param type Join type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the table list
   *
   * @return Table list
   */
  public Table getTable() {
    return table;
  }

  /**
   * Stores the table
   *
   * @param table Table
   */
  public void setTable(Table table) {
    this.table = table;
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
   * Returns the filter group
   *
   * @return Filter group
   */
  public FilterGroup getFilterGroup() {
    return filterGroupList == null || filterGroupList.isEmpty() ? null : filterGroupList.get(0);
  }

  /**
   * Stores the filter group
   *
   * @param filterGroupList Filter group
   */
  public void setFilterGroupList(List<FilterGroup> filterGroupList) {
    this.filterGroupList = filterGroupList;
  }

  @Override
  public String toString() {
    String joinString = "";

    // Add table on JOIN
    if (this.getTable() != null) {
      joinString += this.getType() != null ? "join " + this.getType().toLowerCase() : "join ";
      joinString += this.getTable().toString();
    }

    // Add on on join
    if (this.getFilterGroupList() != null) {
      joinString += " on " + StringUtils.join(getFilterGroupList(), " ");
    }

    return joinString;
  }

  @Override
  public Join copy() throws AWException {
    return new Join(this);
  }
}
