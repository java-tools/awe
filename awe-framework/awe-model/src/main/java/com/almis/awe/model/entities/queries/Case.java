package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Case Class
 *
 * Used to parse the file Queries.xml with XStream
 * Generates a CASE condition in a query
 *
 * @author Isaac Serna - 13/JUN/2018
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("case")
public class Case extends SqlField {

  // Field caseWhen list
  @XStreamImplicit
  private List<CaseWhen> caseWhenList;

  // Field caseElse
  @XStreamAlias("else")
  @XStreamConverter(OperandConverter.class)
  private SqlField caseElse;

  @Override
  public Case copy() throws AWException {
    return this.toBuilder()
      .caseWhenList(ListUtil.copyList(getCaseWhenList()))
      .caseElse(ListUtil.copyElement(getCaseElse()))
      .build();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder().append("CASE ");

    // Add case when list
    if (getCaseWhenList() != null) {
      builder.append(StringUtils.join(getCaseWhenList(), " "));
    }

    // Add case else
    if (getCaseElse() != null) {
      builder.append(" ELSE ").append(getCaseElse().toString());
    }

    return applyFieldModifiers(builder.toString()) + super.toString();
  }
}
