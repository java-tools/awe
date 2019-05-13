package com.almis.awe.model.entities.queries;

import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Queries Class
 *
 * Used to parse the files Queries.xml with XStream
 * Stores the query list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("queries")
public class Queries implements XMLFile {

  private static final long serialVersionUID = -8280761936351955254L;
  // Query list
  @XStreamImplicit
  private List<Query> queryList;

  /**
   * Returns a query
   *
   * @param ide Query identifier
   * @return Selected query
   */
  public Query getQuery(String ide) {
    for (Query query: getBaseElementList()) {
      if (ide.equals(query.getId())) {
        return query;
      }
    }
    return null;
  }

  @Override
  public List<Query> getBaseElementList() {
    return queryList == null ? new ArrayList<>() : queryList;
  }
}
