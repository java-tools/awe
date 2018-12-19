/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.almis.awe.service.data.processor;


import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;

/**
 * Row process interface
 */
public interface CellProcessor {
  /**
   * Process cell to retrieve a CellData
   * @param cell
   * @return
   * @throws AWException
   */
  CellData process(CellData cell) throws AWException;

  /**
   * Get column identifier
   * @return
   */
  String getColumnIdentifier();
}
