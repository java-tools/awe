package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
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
@XStreamAlias("when")
public class CaseWhen extends Filter {

  // Optional filter
  @XStreamAlias("then")
  @XStreamConverter(OperandConverter.class)
  private SqlField thenOperand;

  @Override
  public CaseWhen copy() throws AWException {
    return ((CaseWhen) super.copy())
      .setThenOperand(ListUtil.copyElement(thenOperand));
  }

  @Override
  public String toString() {
    String thenString = "";
    if (getThenOperand() != null) {
      thenString = getThenOperand().toString();
    }
    return "WHEN " + super.toString() + " THEN " + thenString;
  }
}
