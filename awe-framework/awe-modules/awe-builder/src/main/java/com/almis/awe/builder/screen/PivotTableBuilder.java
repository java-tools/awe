package com.almis.awe.builder.screen;

import com.almis.awe.builder.enumerates.*;
import com.almis.awe.builder.screen.base.AbstractComponentBuilder;
import com.almis.awe.model.entities.screen.component.pivottable.PivotTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PivotTableBuilder extends AbstractComponentBuilder<PivotTableBuilder, PivotTable> {

  private Aggregator aggregator;
  private Renderer renderer;
  private SortMethod sortMethod;
  private TotalColumnPlacement totalColumnPlacement;
  private TotalRowPlacement totalRowPlacement;
  private String aggregationField;
  private String decimalSeparator;
  private String thousandSeparator;
  private Integer decimalNumbers;
  private String cols;
  private String rows;

  @Override
  public PivotTable build() {
    return build(new PivotTable());
  }

  @Override
  public PivotTable build(PivotTable pivotTable) {
    super.build(pivotTable)
      .setAggregationField(getAggregationField())
      .setDecimalNumbers(getDecimalNumbers())
      .setDecimalSeparator(getDecimalSeparator())
      .setThousandSeparator(getThousandSeparator())
      .setCols(getCols())
      .setRows(getRows());

    if (getAggregator() != null) {
      pivotTable.setAggregator(getAggregator().toString());
    }

    if (getRenderer() != null) {
      pivotTable.setRenderer(getRenderer().toString());
    }

    if (getSortMethod() != null) {
      pivotTable.setSortMethod(getSortMethod().toString());
    }

    if (getTotalColumnPlacement() != null) {
      pivotTable.setTotalColumnPlacement(getTotalColumnPlacement().toString());
    }

    if (getTotalRowPlacement() != null) {
      pivotTable.setTotalRowPlacement(getTotalRowPlacement().toString());
    }

    return pivotTable;
  }
}
