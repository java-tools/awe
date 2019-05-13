package com.almis.awe.model.entities.queries;

import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * FilterGroup Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group. Generates a list of filters
 * Can contain filter lists or filters
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@Accessors(chain = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@XStreamInclude({FilterAnd.class, FilterOr.class})
public abstract class FilterGroup implements Copyable {

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
