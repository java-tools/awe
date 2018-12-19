package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compound Class
 *
 * Used to parse the files Queries.xml with XStream
 *
 *
 * Compund fields
 *
 *
 * @author Pablo VIDAL - 14/Oct/2014
 */
@XStreamAlias("compound")
public class Compound extends OutputField {

  private static final long serialVersionUID = -2436308077990244530L;

  // Computed list
  @XStreamImplicit
  private List<Computed> computedList;

  /**
   * Default constructor
   */
  public Compound() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Compound(Compound other) throws AWException {
    super(other);
    this.computedList = ListUtil.copyList(other.computedList);
  }

  @Override
  public Compound copy() throws AWException {
    return new Compound(this);
  }

  /**
   * Get computed list
   *
   * @return computed list
   */
  public List<Computed> getComputedList() {
    return computedList;
  }

  /**
   * Set computed list
   *
   * @param computedList list with computed elements
   */
  public void setComputedList(List<Computed> computedList) {
    this.computedList = computedList;
  }

  /**
   * Get computed list as Map (Key map is the alias of computed element)
   *
   * @return Map with computed elements
   */
  public Map<String, Computed> getComputedListAsMap() {
    Map<String, Computed> computedListMap = null;
    if (this.computedList != null && !this.computedList.isEmpty()) {
      computedListMap = new HashMap<>();
      for (Computed computed : computedList) {
        computedListMap.put(computed.getAlias(), computed);
      }
    }
    return computedListMap;
  }

}
