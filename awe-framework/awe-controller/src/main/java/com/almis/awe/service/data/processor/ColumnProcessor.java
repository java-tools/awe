package com.almis.awe.service.data.processor;


import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;

import java.util.Map;

/**
 * Row process interface
 */
public interface ColumnProcessor {
  /**
   * Process row to retrieve a CellData
   * @param row datalist row
   * @return processed CellData
   * @throws AWException AWE exception
   */
  CellData process(Map<String, CellData> row) throws AWException;

  /**
   * Get column identifier
   * @return column identifier
   */
  String getColumnIdentifier();
}
