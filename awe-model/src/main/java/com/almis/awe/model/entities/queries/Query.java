/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * Query Class
 *
 * Used to parse the files Queries.xml with XStream
 * Generates and launches a query statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("query")
public class Query extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 4116800522035824625L;

  // Query identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id = null;

  // Service identifier (if query is resolved with a service)
  @XStreamAlias("service")
  @XStreamAsAttribute
  private String service = null;

  // Enumerated identifier (if query is resolved with an enumerated)
  @XStreamAlias("enumerated")
  @XStreamAsAttribute
  private String enumerated = null;

  // Queue identifier (if query is resolved with a queue)
  @XStreamAlias("queue")
  @XStreamAsAttribute
  private String queue = null;

  // Query is a select distinct
  @XStreamAlias("distinct")
  @XStreamAsAttribute
  private String distinct = null;

  // Launch multiple queries
  @XStreamAlias("multiple")
  @XStreamAsAttribute
  private String multiple = null;

  // Query Label (message description)
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label = null;

  // Query can be launched out of session
  @XStreamAlias("public")
  @XStreamAsAttribute
  private String isPublic = null;

  // Query table list
  @XStreamImplicit
  private List<Table> tableList;

  // Query field list
  @XStreamImplicit
  private List<Field> fieldList;

  // Query join list
  @XStreamImplicit
  private List<Join> joinList;

  // Query union list
  @XStreamImplicit
  private List<Union> unionList;

  // Query computed fields list
  @XStreamImplicit
  private List<Computed> computedList;

  // Query compound fields list
  @XStreamImplicit
  private List<Compound> compoundList;

  // Query order by list
  @XStreamImplicit
  private List<OrderBy> orderByList;

  // Query group by list
  @XStreamImplicit
  private List<GroupBy> groupByList;

  // Query totalize list
  @XStreamImplicit
  private List<Totalize> totalizeList;

  // Query is cacheable
  @XStreamAlias("cacheable")
  @XStreamAsAttribute
  private String cacheable;

  // Query is pagination
  @XStreamAlias("managed-pagination")
  @XStreamAsAttribute
  private String managedPagination;

  // Query filter group list
  @XStreamAlias("where")
  private FilterAnd filterGroup;

  // Query filter group list
  @XStreamAlias("having")
  private FilterAnd havingGroup;

  // Query variable definition list
  @XStreamImplicit
  private List<Variable> variableDefinitionList;

  /**
   * Default constructor
   */
  public Query() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Query(Query other) throws AWException {
    super(other);
    this.id = other.id;
    this.service = other.service;
    this.enumerated = other.enumerated;
    this.queue = other.queue;
    this.distinct = other.distinct;
    this.multiple = other.multiple;
    this.label = other.label;
    this.isPublic = other.isPublic;
    this.cacheable = other.cacheable;
    this.managedPagination = other.managedPagination;
    this.filterGroup = other.filterGroup == null ? null : other.filterGroup.copy();
    this.havingGroup = other.havingGroup == null ? null : other.havingGroup.copy();
    this.tableList = ListUtil.copyList(other.tableList);
    this.fieldList = ListUtil.copyList(other.fieldList);
    this.joinList = ListUtil.copyList(other.joinList);
    this.unionList = ListUtil.copyList(other.unionList);
    this.computedList = ListUtil.copyList(other.computedList);
    this.compoundList = ListUtil.copyList(other.compoundList);
    this.orderByList = ListUtil.copyList(other.orderByList);
    this.groupByList = ListUtil.copyList(other.groupByList);
    this.totalizeList = ListUtil.copyList(other.totalizeList);
    this.variableDefinitionList = ListUtil.copyList(other.variableDefinitionList);
  }

  /**
   * Returns the query identifier
   *
   * @return Query identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the query identifier
   *
   * @param id Query identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns if the query has more than one iterations
   *
   * @return The query has more than one iterations
   */
  public String getMultiple() {
    return multiple;
  }

  /**
   * Stores if the query has more than one iterations
   *
   * @param multiple The query has more than one iterations
   */
  public void setMultiple(String multiple) {
    this.multiple = multiple;
  }

  /**
   * Returns the query label (message description)
   *
   * @return The query label (message description)
   */
  public String getLabel() {
    return label;
  }

  /**
   * Stores the query label (message description)
   *
   * @param label The query label (message description)
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns Check if query can be launched off session
   *
   * @return If query can be launched off session
   */
  public String getPublic() {
    return isPublic;
  }

  /**
   * Returns Check if query can be launched off session
   *
   * @return If query can be launched off session
   */
  public boolean isPublic() {
    return "true".equalsIgnoreCase(isPublic);
  }

  /**
   * Stores if query can be launched off session
   *
   * @param isPublic query can be launched off session
   */
  public void setPublic(String isPublic) {
    this.isPublic = isPublic;
  }

  /**
   * Returns the query table list
   *
   * @return Table list
   */
  public List<Table> getTableList() {
    return tableList;
  }

  /**
   * Stores the query table list
   *
   * @param tableList Table list
   */
  public void setTableList(List<Table> tableList) {
    this.tableList = tableList;
  }

  /**
   * Returns the query field list
   *
   * @return Field list
   */
  public List<Field> getFieldList() {
    return fieldList;
  }

  /**
   * Stores the query field list
   *
   * @param fieldList Field list
   */
  public void setFieldList(List<Field> fieldList) {
    this.fieldList = fieldList;
  }

  /**
   * Returns the query order by list
   *
   * @return Order by list
   */
  public List<OrderBy> getOrderByList() {
    return orderByList;
  }

  /**
   * Stores the query order by list
   *
   * @param orderByList Order by list
   */
  public void setOrderByList(List<OrderBy> orderByList) {
    this.orderByList = orderByList;
  }

  /**
   * Returns the query group by list
   *
   * @return Group by list
   */
  public List<GroupBy> getGroupList() {
    return groupByList;
  }

  /**
   * Stores the query group by list
   *
   * @param groupByList Group by list
   */
  public void setGroupList(List<GroupBy> groupByList) {
    this.groupByList = groupByList;
  }

  /**
   * Returns the query totalize list
   *
   * @return Totalize list
   */
  public List<Totalize> getTotalizeList() {
    return totalizeList;
  }

  /**
   * Stores the query totalize list
   *
   * @param totalizeList Totalize list
   */
  public void setTotalizeList(List<Totalize> totalizeList) {
    this.totalizeList = totalizeList;
  }

  /**
   * Returns the query filter group list
   *
   * @return Filter group list
   */
  public FilterGroup getFilterGroup() {
    return filterGroup;
  }

  /**
   * Returns the query filter group list
   *
   * @return Filter group list
   */
  public FilterGroup getHavingGroup() {
    return havingGroup;
  }

  /**
   * Stores the filter group list
   *
   * @param filterGroup Filter group list
   */
  public void setFilterGroup(FilterAnd filterGroup) {
    this.filterGroup = filterGroup;
  }

  /**
   * Stores the filter group list
   *
   * @param havingGroup Filter group list
   */
  public void setHavingGroup(FilterAnd havingGroup) {
    this.havingGroup = havingGroup;
  }

  /**
   * Returns the service identifier
   *
   * @return Service identifier
   */
  public String getService() {
    return service;
  }

  /**
   * Stores the service identifier
   *
   * @param service Service identifier
   */
  public void setService(String service) {
    this.service = service;
  }

  /**
   * Returns the variable definition list
   *
   * @return Variable definition list
   */
  public List<Variable> getVariableDefinitionList() {
    return variableDefinitionList;
  }

  /**
   * Stores the variable definition list
   *
   * @param variableDefinitionList Variable definition list
   */
  public void setVariableDefinitionList(List<Variable> variableDefinitionList) {
    this.variableDefinitionList = variableDefinitionList;
  }

  /**
   * Returns a variable definition
   *
   * @param variableId Variable identifier
   * @return Selected definition
   */
  public Variable getVariableDefinition(String variableId) {
    if (variableId != null && this.getVariableDefinitionList() != null) {
      for (Variable variable : this.getVariableDefinitionList()) {
        if (variableId.equals(variable.getId())) {
          return variable;
        }
      }
    }
    return null;
  }

  /**
   * Returns the computed field list
   *
   * @return Computed field list
   */
  public List<Computed> getComputedList() {
    return computedList;
  }

  /**
   * Stores the computed field list
   *
   * @param computedList Computed field list
   */
  public void setComputedList(List<Computed> computedList) {
    this.computedList = computedList;
  }

  /**
   * Returns the compound field list
   *
   * @return Compound field list
   */
  public List<Compound> getCompoundList() {
    return compoundList;
  }

  /**
   * Stores the compound field list
   *
   * @param compoundList Compound field list
   */
  public void setCompoundList(List<Compound> compoundList) {
    this.compoundList = compoundList;
  }

  /**
   * Returns if the query is a select distinct (!= null)
   *
   * @return Query is a select distinct
   */
  public String getDistinct() {
    return distinct;
  }

  /**
   * Stores if the query is a select distinct (!= null)
   *
   * @param distinct Query is a select distinct
   */
  public void setDistinct(String distinct) {
    this.distinct = distinct;
  }

  /**
   * Returns if the query is a select distinct (!= null)
   *
   * @return Query is a select distinct
   */
  public boolean isDistinct() {
    return "true".equalsIgnoreCase(distinct);
  }

  /**
   * Returns the query join list
   *
   * @return Query join list
   */
  public List<Join> getJoinList() {
    return joinList;
  }

  /**
   * Stores the query join list
   *
   * @param joinList Query join list
   */
  public void setJoinList(List<Join> joinList) {
    this.joinList = joinList;
  }

  /**
   * Returns the query union list
   *
   * @return Query union list
   */
  public List<Union> getUnionList() {
    return unionList;
  }

  /**
   * Stores the query union list
   *
   * @param unionList Query union list
   */
  public void setUnionList(List<Union> unionList) {
    this.unionList = unionList;
  }

  /**
   * Returns the query enumerated identifier
   *
   * @return Query enumerated identifier
   */
  public String getEnumerated() {
    return enumerated;
  }

  /**
   * Stores the query enumerated identifier
   *
   * @param enumerated Query enumerated identifier
   */
  public void setEnumerated(String enumerated) {
    this.enumerated = enumerated;
  }

  /**
   * Returns the query queue identifier
   *
   * @return Query queue identifier
   */
  public String getQueue() {
    return queue;
  }

  /**
   * Stores the query queue identifier
   *
   * @param queue Query queue identifier
   */
  public void setQueue(String queue) {
    this.queue = queue;
  }

  /**
   * Returns if query is cacheable
   *
   * @return query is cacheable
   */
  public boolean isCacheable() {
    return "true".equalsIgnoreCase(getCacheable());
  }

  /**
   * Returns if query is cacheable
   *
   * @return Query is cacheable (string)
   */
  public String getCacheable() {
    return cacheable;
  }

  /**
   * Stores if service is cacheable
   *
   * @param cacheable Service is cacheable (string)
   */
  public void setCacheable(String cacheable) {
    this.cacheable = cacheable;
  }

  /**
   * Returns if pagination is managed by the engine
   *
   * @return pagination is managed
   */
  public boolean isPaginationManaged() {
    return "true".equalsIgnoreCase(getManagedPagination());
  }

  /**
   * Returns if query is pagination
   *
   * @return Query is pagination (string)
   */
  public String getManagedPagination() {
    return managedPagination;
  }

  /**
   * Stores if query's pagination is managed by the engine
   *
   * @param managedPagination
   */
  public void setManagedPagination(String managedPagination) {
    this.managedPagination = managedPagination;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide Element identifier
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getId().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public Query copy() throws AWException {
    return new Query(this);
  }

  @Override
  public String toString() {
    return "Query{" +
            "id='" + id + '\'' +
            ", service='" + service + '\'' +
            ", enumerated='" + enumerated + '\'' +
            ", queue='" + queue + '\'' +
            ", distinct='" + distinct + '\'' +
            ", multiple='" + multiple + '\'' +
            ", label='" + label + '\'' +
            ", isPublic='" + isPublic + '\'' +
            ", tableList=" + tableList +
            ", fieldList=" + fieldList +
            ", joinList=" + joinList +
            ", unionList=" + unionList +
            ", computedList=" + computedList +
            ", compoundList=" + compoundList +
            ", orderByList=" + orderByList +
            ", groupByList=" + groupByList +
            ", totalizeList=" + totalizeList +
            ", cacheable='" + cacheable + '\'' +
            ", managedPagination='" + managedPagination + '\'' +
            ", filterGroup=" + filterGroup +
            ", havingGroup=" + havingGroup +
            ", variableDefinitionList=" + variableDefinitionList +
            '}';
  }
}
