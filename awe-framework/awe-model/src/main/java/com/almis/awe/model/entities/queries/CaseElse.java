package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * CaseElse Class
 *
 * Used to parse the file Queries.xml with XStream
 * Generates a CASE ELSE condition in a query
 *
 * @author Isaac Serna - 13/JUN/2018
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
@XStreamAlias("case-else")
public class CaseElse implements CaseClause {

  // Optional filter
  @XStreamAlias("then-field")
  @XStreamAsAttribute
  private String thenField;

  // Optional filter
  @XStreamAlias("then-table")
  @XStreamAsAttribute
  private String thenTable;

  // Optional filter
  @XStreamAlias("then-variable")
  @XStreamAsAttribute
  private String thenVariable;

  @Override
  public CaseElse copy() throws AWException {
    return this.toBuilder().build();
  }
}
