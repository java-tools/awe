package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("compound")
public class Compound extends OutputField {

  private static final long serialVersionUID = -2436308077990244530L;

  // Computed list
  @XStreamImplicit
  private List<Computed> computedList;

  @Override
  public Compound copy() throws AWException {
    return this.toBuilder()
      .computedList(ListUtil.copyList(getComputedList()))
      .build();
  }
}
