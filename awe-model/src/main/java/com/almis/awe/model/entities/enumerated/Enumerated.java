package com.almis.awe.model.entities.enumerated;

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
 * Enumerated Class
 *
 * Used to parse the file Enumerated.xml with XStream
 * Generates an enumerated group list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("enumerated")
public class Enumerated extends XMLWrapper {

  private static final long serialVersionUID = -5779427444052366131L;
  // Enumerated group list
  @XStreamImplicit
  private List<EnumeratedGroup> groupList;

  /**
   * Default constructor
   */
  public Enumerated() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Enumerated(Enumerated other) throws AWException {
    super(other);
    this.groupList = ListUtil.copyList(other.groupList);
  }

  /**
   * Returns the enumerated group list
   *
   * @return Enumerated group list
   */
  public List<EnumeratedGroup> getGroupList() {
    return groupList;
  }

  /**
   * Stores the enumerated group list
   *
   * @param groupList Enumerated group list
   */
  public void setGroupList(List<EnumeratedGroup> groupList) {
    this.groupList = groupList;
  }

  /**
   * Returns an enumerated group from identifier
   *
   * @param ide Enumerated group identifier
   * @return Selected enumerated group
   */
  public EnumeratedGroup getGroup(String ide) {
    // Check group existence
    for (EnumeratedGroup group: getBaseElementList()) {
      if (ide.equals(group.getId())) {
        return group;
      }
    }
    return null;
  }

  @Override
  public List<EnumeratedGroup> getBaseElementList() {
    return groupList == null ? Collections.emptyList() : groupList;
  }
}
