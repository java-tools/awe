/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.almis.awe.service.data.processor;


import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;

import java.util.List;
import java.util.Map;

/**
 * Row list process interface
 */
public interface RowProcessor {
  /**
   * Process row list
   * @param rowList row list
   * @return row list processed
   * @throws AWException AWE exception
   */
  List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) throws AWException;
}
