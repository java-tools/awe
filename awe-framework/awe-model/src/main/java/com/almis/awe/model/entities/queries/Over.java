package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Over Class
 *
 * Used to parse window functions
 * Generates a OVER condition in a query
 *
 * @author Pablo Garcia - 27/05/2019
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("over")
public class Over extends SqlField {

  // Query field list
  @XStreamImplicit
  private List<SqlField> fieldList;

  // Over partitionByList
  @XStreamImplicit
  private List<PartitionBy> partitionByList;

  // Over orderByList
  @XStreamImplicit
  private List<OrderBy> orderByList;

  @Override
  public Over copy() throws AWException {
    return this.toBuilder()
      .fieldList(ListUtil.copyList(getFieldList()))
      .partitionByList(ListUtil.copyList(getPartitionByList()))
      .orderByList(ListUtil.copyList(getOrderByList()))
      .build();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    // Add partition by list
    if (getFieldList() != null) {
      builder
        .append(StringUtils.join(getFieldList(), ", "));
    }

    builder.append(" OVER (");

    // Add partition by list
    if (getPartitionByList() != null) {
      builder
        .append("PARTITION BY ")
        .append(StringUtils.join(getPartitionByList(), ", "));
    }

    // Add add order by list
    if (getOrderByList() != null) {
      builder
        .append(getPartitionByList() != null ? " " : "")
        .append("ORDER BY ")
        .append(StringUtils.join(getOrderByList(), ", "));
    }

    return applyFunctionString(builder.append(")").toString()) + super.toString();
  }
}
