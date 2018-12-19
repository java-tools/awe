package com.almis.awe.model.entities.access;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.type.RestrictionType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Restriction Class
 *
 * Used to parse the files in profile folder with XStream
 * This class is used to parse a restriction of an option
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@XStreamAlias("restriction")
public class Restriction extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 6237956002557950701L;
  // Option to restrict
  @XStreamAlias("option")
  @XStreamAsAttribute
  private String option = null;
  // Restriction type
  private RestrictionType restrictionType;

  /**
   * Default constructor
   */
  public Restriction() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Restriction(Restriction other) {
    this.option = other.option;
    this.restrictionType = other.restrictionType;
  }

  /**
   * Returns the restriction option
   *
   * @return Option restricted
   */
  public String getOption() {
    return option;
  }

  /**
   * Stores the restriction option
   *
   * @param option Option restricted
   */
  public void setOption(String option) {
    this.option = option;
  }

  /**
   * Returns the restriction type (R: Restricted or A: Allowed)
   *
   * @return RestrictionType
   */
  public RestrictionType getRestrictionType() {
    if (restrictionType == null) {
      return RestrictionType.R;
    } else {
      return this.restrictionType;
    }
  }

  /**
   * Store restriction type
   *
   * @param restrictionType Restriction type
   */
  public void setRestrictionType(RestrictionType restrictionType) {
    this.restrictionType = restrictionType;
  }

  @Override
  public Restriction copy() throws AWException {
    return new Restriction(this);
  }
}
