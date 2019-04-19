/*
 * Package definition
 */
package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Queries Class
 *
 * Used to parse the files Queries.xml with XStream
 * Stores the query list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("queries")
public class Queries extends XMLWrapper {

  private static final long serialVersionUID = -8280761936351955254L;
  // Query list
  @XStreamImplicit
  private List<Query> queryList;

  /**
   * Default constructor
   */
  public Queries() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Queries(Queries other) throws AWException {
    super(other);
    this.queryList = ListUtil.copyList(other.queryList);
  }

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

  /**
   * Returns the query list
   *
   * @return Query list
   */
  public List<Query> getQueryList() {
    return queryList;
  }

  /**
   * Stores the query list
   *
   * @param queryList Query list
   */
  public void setQueryList(List<Query> queryList) {
    this.queryList = queryList;
  }

  @Override
  public List<Query> getBaseElementList() {
    return queryList == null ? Collections.emptyList() : queryList;
  }
}
