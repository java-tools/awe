package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Operation Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Wrapper around elements that should be operated by an operator
 *
 * @author Jorge BELLON - 07/SEP/2017
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("operation")
public class Operation extends SqlField {

  // Field id (database id)
  @XStreamAlias("operator")
  @XStreamAsAttribute
  private String operator;

  // Field concat list
  @XStreamImplicit
  private List<SqlField> operandList;

  @Override
  public Operation copy() throws AWException {
    return this.toBuilder()
      .operandList(ListUtil.copyList(getOperandList()))
      .build();
  }

  @Override
  public String toString() {
    StringBuilder operationBuilder = new StringBuilder().append(operator).append("(");
    int i = 0;
    for (OutputField operand : getOperandList()) {
      operationBuilder.append(operand.toString());
      i++;
      if (i < getOperandList().size()) {
        operationBuilder.append(", ");
      }
    }

    // Add function if defined
    String field = operationBuilder.append(")").toString();
    field = getFunction() != null ? getFunction() + "(" + field + ")" : field;

    return field + super.toString();
  }
}
