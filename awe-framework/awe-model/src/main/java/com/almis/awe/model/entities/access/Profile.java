/*
 * Package definition
 */
package com.almis.awe.model.entities.access;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * Profile Class
 *
 * Used to parse the files in profile folder with XStream
 * These files have the specific restrictions of a profile
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("profile")
public class Profile extends XMLWrapper {

  private static final long serialVersionUID = -7990480714029113566L;

  // Restriction List
  @XStreamImplicit
  private List<Restriction> restrictionList;

  /**
   * Constructor
   */
  public Profile() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Profile(Profile other) throws AWException {
    super(other);
    this.restrictionList = ListUtil.copyList(other.restrictionList);
  }

  /**
   * Returns the restrictions list
   *
   * @return Restrictions list
   */
  public List<Restriction> getRestrictions() {
    return restrictionList;
  }

  /**
   * Stores the restrictions list
   *
   * @param restrictions Restrictions list
   */
  public void setRestrictions(List<Restriction> restrictions) {
    this.restrictionList = restrictions;
  }
}
