package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.type.JoinType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Join Class
 *
 * Used to parse the files Queries.xml with XStream
 * Generates a join with another table
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("join")
public class Join implements Copyable {

  private static final long serialVersionUID = -2168129287969817829L;

  // Join type
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Join table list
  private Table table;

  // Join filter group list
  @XStreamImplicit
  private List<FilterGroup> filterGroupList;

  /**
   * Retrieve join type as TYPE
   *
   * @return
   */
  public JoinType getJoinType() {
    return type == null ? JoinType.INNER : JoinType.valueOf(type);
  }

  /**
   * Returns the filter group
   *
   * @return Filter group
   */
  public FilterGroup getFilterGroup() {
    return getFilterGroupList() == null || getFilterGroupList().isEmpty() ? null : getFilterGroupList().get(0);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    // Add table on JOIN
    if (getTable() != null) {
      builder
        .append(getType() != null ? " " + getType().toUpperCase() : "")
        .append(" JOIN ")
        .append(getTable().toString());
    }

    // Add on on join
    if (getFilterGroupList() != null) {
      builder
        .append(" ON ")
        .append(StringUtils.join(getFilterGroupList(), " "));
    }

    return builder.toString();
  }

  @Override
  public Join copy() throws AWException {
    return this.toBuilder()
      .filterGroupList(ListUtil.copyList(getFilterGroupList()))
      .build();
  }
}
