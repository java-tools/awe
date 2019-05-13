package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * CaseWhen Class
 *
 * Used to parse the file Queries.xml with XStream
 * Generates a CASE WHEN condition in a query
 *
 * @author Isaac Serna - 13/JUN/2018
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@XStreamAlias("case-when")
public class CaseWhen extends Filter implements CaseClause {

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
  public CaseWhen copy() throws AWException {
    return this.toBuilder().build();
  }
}
