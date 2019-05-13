package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Concat Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 *
 *
 * Wrapper around elements that should be concatenated from queries and maintain
 *
 *
 * @author Jorge BELLON - 07/SEP/2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("concat")
public class Concat extends Field {

  private static final long serialVersionUID = -3675559309732593756L;

  @Override
  public Concat copy() throws AWException {
    return this.toBuilder().build();
  }
}
