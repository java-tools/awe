package com.almis.awe.model.entities.access;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Profile Class
 *
 * Used to parse the files in profile folder with XStream
 * These files have the specific restrictions of a profile
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("profile")
public class Profile implements Copyable {

  private static final long serialVersionUID = -7990480714029113566L;

  // Restriction List
  @XStreamImplicit
  private List<Restriction> restrictions;

  @Override
  public Profile copy() throws AWException {
    return this.toBuilder()
      .restrictions(ListUtil.copyList(getRestrictions()))
      .build();
  }
}
