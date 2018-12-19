/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.almis.awe.service.data.processor;


import java.util.List;
import java.util.Map;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.dto.CellData;

/**
 * Row list process interface
 */
public interface RowProcessor {
  /**
   * Process row list
   * @param rowList
   * @return
   * @throws AWException
   */
  List<Map<String, CellData>> process(List<Map<String, CellData>> rowList) throws AWException;
}
