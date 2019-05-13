package com.almis.awe.model.entities.screen.component.pivottable;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.screen.component.grid.AbstractGrid;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * PivotTable Class
 *
 * Used to parse a pivot table tag with XStream
 * Generates an screen pivot table
 *
 * @author Pablo Vidal - 12/JUN/2015
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("pivot-table")
public class PivotTable extends AbstractGrid {

  private static final long serialVersionUID = 8283520541161175828L;

  // Total column placement
  @XStreamAlias("total-column-placement")
  @XStreamAsAttribute
  private String totalColumnPlacement;

  // Total row placement
  @XStreamAlias("total-row-placement")
  @XStreamAsAttribute
  private String totalRowPlacement;

  // Renderer
  @XStreamAlias("renderer")
  @XStreamAsAttribute
  private String renderer;

  // Aggregator
  @XStreamAlias("aggregator")
  @XStreamAsAttribute
  private String aggregator;

  // Value
  @XStreamAlias("aggregation-field")
  @XStreamAsAttribute
  private String aggregationField;

  // Sort method
  @XStreamAlias("sort-method")
  @XStreamAsAttribute
  private String sortMethod;

  // Pivot numeric options: number of decimals
  @XStreamAlias("decimal-numbers")
  @XStreamAsAttribute
  private Integer decimalNumbers;

  // Pivot numeric options: separator of thousands
  @XStreamAlias("thousand-separator")
  @XStreamAsAttribute
  private String thousandSeparator;

  // Pivot numeric options: separator of decimal numbers
  @XStreamAlias("decimal-separator")
  @XStreamAsAttribute
  private String decimalSeparator;

  // Initially selected columns
  @XStreamAlias("cols")
  @XStreamAsAttribute
  private String cols;

  // Initially selected rows
  @XStreamAlias("rows")
  @XStreamAsAttribute
  private String rows;

  @Override
  public PivotTable copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  @Override
  public String getComponentTag() {
    return "pivot-table";
  }
}
