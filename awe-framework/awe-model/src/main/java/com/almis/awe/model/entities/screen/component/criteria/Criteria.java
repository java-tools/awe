package com.almis.awe.model.entities.screen.component.criteria;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/*
 * File Imports
 */

/**
 * Criteria Class
 *
 * Used to parse a criteria tag with XStream
 * Generates an screen criteria
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@XStreamAlias("criteria")
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Criteria extends AbstractCriteria {
  @Override
  public Criteria copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }
}
