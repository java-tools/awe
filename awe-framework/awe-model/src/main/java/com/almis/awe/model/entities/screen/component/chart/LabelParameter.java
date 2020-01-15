package com.almis.awe.model.entities.screen.component.chart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Text parameter
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LabelParameter {
  private String format;
  private String formatter;
  private Float rotation;
}
