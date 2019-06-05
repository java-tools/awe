package com.almis.awe.model.entities.screen.data;

import com.almis.awe.model.dto.CellData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pgarcia
 */
@Data
@Accessors(chain = true)
public class ComponentModel {

  // Selected values
  private List<CellData> selected = new ArrayList<>();

  // Default values
  private List<CellData> defaultValues = new ArrayList<>();

  // Selection values
  private List<Map<String, CellData>> values = new ArrayList<>();

  // Page
  private Long page = 1L;

  // Total pages
  private Long total = 0L;

  // Records
  private Long records = 0L;
}
