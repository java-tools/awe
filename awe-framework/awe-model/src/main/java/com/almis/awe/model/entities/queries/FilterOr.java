package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.UnionType;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * FilterOr Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Filter group concatenated with 'OR'
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("or")
public class FilterOr extends FilterGroup {

  private static final long serialVersionUID = -4696818779067777019L;

  @Override
  public FilterOr copy() throws AWException {
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
    return UnionType.OR.toString();
  }
}
