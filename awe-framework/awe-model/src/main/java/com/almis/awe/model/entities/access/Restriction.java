package com.almis.awe.model.entities.access;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.type.RestrictionType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Restriction Class
 *
 * Used to parse the files in profile folder with XStream
 * This class is used to parse a restriction of an option
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@XStreamAlias("restriction")
public class Restriction implements Copyable {

  private static final long serialVersionUID = 6237956002557950701L;

  // Option to restrict
  @XStreamAlias("option")
  @XStreamAsAttribute
  private String option;

  // Restriction type
  private RestrictionType restrictionType;

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
    return this.toBuilder().build();
  }
}
