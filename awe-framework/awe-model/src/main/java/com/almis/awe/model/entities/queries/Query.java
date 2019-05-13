package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLNode;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Query Class
 *
 * Used to parse the files Queries.xml with XStream
 * Generates and launches a query statement
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@ToString
@XStreamAlias("query")
public class Query implements XMLNode, Copyable {

  private static final long serialVersionUID = 4116800522035824625L;

  // Query identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Service identifier (if query is resolved with a service)
  @XStreamAlias("service")
  @XStreamAsAttribute
  private String service;

  // Enumerated identifier (if query is resolved with an enumerated)
  @XStreamAlias("enumerated")
  @XStreamAsAttribute
  private String enumerated;

  // Queue identifier (if query is resolved with a queue)
  @XStreamAlias("queue")
  @XStreamAsAttribute
  private String queue;

  // Query is a select distinct
  @XStreamAlias("distinct")
  @XStreamAsAttribute
  private Boolean distinct;

  // Launch multiple queries
  @XStreamAlias("multiple")
  @XStreamAsAttribute
  private String multiple;

  // Query Label (message description)
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Query can be launched out of session
  @XStreamAlias("public")
  @XStreamAsAttribute
  private Boolean isPublic;

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
  private Boolean cacheable;

  // Query is pagination
  @XStreamAlias("managed-pagination")
  @XStreamAsAttribute
  private Boolean paginationManaged;

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
   * Returns if is distinct
   * @return Is distinct
   */
  public boolean isCacheable() {
    return cacheable != null && cacheable;
  }

  /**
   * Returns if is distinct
   * @return Is distinct
   */
  public boolean isDistinct() {
    return distinct != null && distinct;
  }

  /**
   * Returns if is paginationManaged
   * @return Is paginationManaged
   */
  public boolean isPaginationManaged() {
    return paginationManaged != null && paginationManaged;
  }

  /**
   * Returns if is list
   * @return Is list
   */
  public boolean isPublic() {
    return isPublic != null && isPublic;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getId();
  }

  @Override
  public Query copy() throws AWException {
    return this.toBuilder()
      .tableList(ListUtil.copyList(getTableList()))
      .fieldList(ListUtil.copyList(getFieldList()))
      .joinList(ListUtil.copyList(getJoinList()))
      .unionList(ListUtil.copyList(getUnionList()))
      .computedList(ListUtil.copyList(getComputedList()))
      .compoundList(ListUtil.copyList(getCompoundList()))
      .orderByList(ListUtil.copyList(getOrderByList()))
      .groupByList(ListUtil.copyList(getGroupByList()))
      .totalizeList(ListUtil.copyList(getTotalizeList()))
      .filterGroup(ListUtil.copyElement(getFilterGroup()))
      .havingGroup(ListUtil.copyElement(getHavingGroup()))
      .variableDefinitionList(ListUtil.copyList(getVariableDefinitionList()))
      .build();
  }
}
