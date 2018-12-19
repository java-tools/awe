/*
 * Package definition
 */
package com.almis.awe.model.entities.maintain;

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
 * Maintain Class
 *
 * Used to parse the file Maintain.xml with XStream
 * Contains a list of maintain targets
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@XStreamAlias("maintain")
public class Maintain extends XMLWrapper {

  private static final long serialVersionUID = -1962157356340282704L;
  // Maintain Target List
  @XStreamImplicit
  private List<Target> maintainList;

  /**
   * Default constructor
   */
  public Maintain() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Maintain(Maintain other) throws AWException {
    super(other);
    this.maintainList = ListUtil.copyList(other.maintainList);
  }

  /**
   * Returns a a target from its identifier
   *
   * @param ide Target identifier
   * @return Selected Target
   */
  public Target getTarget(String ide) {
    for (Target target: getBaseElementList()) {
      if (ide.equals(target.getName())) {
        return target;
      }
    }
    return null;
  }

  /**
   * Returns the Target List
   *
   * @return Target List
   */
  public List<Target> getTargetList() {
    return maintainList;
  }

  /**
   * Stores the Target List
   *
   * @param List Target List
   */
  public void setTargetList(List<Target> List) {
    this.maintainList = List;
  }

  @Override
  public List<Target> getBaseElementList() {
    return maintainList == null ? Collections.emptyList() : maintainList;
  }
}
