package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * TotalizeBy Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Fields used to totalize
 *
 * @author Pablo GARCIA - 22/SEP/2011
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("totalize-by")
public class TotalizeBy implements Copyable {

  private static final long serialVersionUID = -5294879694115446878L;

  // Field to totalize
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  @Override
  public TotalizeBy copy() throws AWException {
    return this.toBuilder().build();
  }
}
