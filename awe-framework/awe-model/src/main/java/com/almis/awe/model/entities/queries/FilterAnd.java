package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.UnionType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * FilterAnd Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group concatenated with 'AND'
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("and")
public class FilterAnd extends FilterGroup {

  @Override
  public FilterAnd copy() throws AWException {
    return this.toBuilder()
      .filterGroupList(ListUtil.copyList(getFilterGroupList()))
      .filterList(ListUtil.copyList(getFilterList()))
      .build();
  }

  /**
   * Returns the filter group union type
   *
   * @return Filter group union type
   */
  @Override
  public String getUnion() {
    return UnionType.AND.toString();
  }
}
