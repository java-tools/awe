package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * Totalize Class
 * Used to parse the file Queries.xml with XStream
 * Totalize from queries. Generates new rows with totals and subtotals
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("totalize")
public class Totalize implements Copyable {

  private static final long serialVersionUID = -1703530186967387913L;

  // Totalize function (SUM, AVG, MAX, MIN)
  @XStreamAlias("function")
  @XStreamAsAttribute
  private String function;

  // Totalize label
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Totalize field (where to put the total label)
  @XStreamAlias("field")
  @XStreamAsAttribute
  private String field;

  // Style field (TOTAL/SUBTOTAL)
  @XStreamAlias("style")
  @XStreamAsAttribute
  private String style;

  // Query totalize by fields list
  @XStreamImplicit
  private List<TotalizeBy> totalizeByList;

  // Query totalize fields list
  @XStreamImplicit
  private List<TotalizeField> totalizeFieldList;

  // Query totalize value list
  @XStreamOmitField
  private Map<String, CellData> totalizeValueList;

  // Query totalize by value list
  @XStreamOmitField
  private Map<String, String> totalizeByValueList;

  @Override
  public Totalize copy() throws AWException {
    return this.toBuilder()
      .totalizeByList(ListUtil.copyList(getTotalizeByList()))
      .totalizeFieldList(ListUtil.copyList(getTotalizeFieldList()))
      .totalizeValueList(ListUtil.copyMap(getTotalizeValueList()))
      .totalizeByValueList(ListUtil.copyMap(getTotalizeByValueList(), String.class))
      .build();
  }
}
