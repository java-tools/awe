package com.almis.awe.model.entities.screen.component.action;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActionParameters {
  private String target;
  private String value;
  private String label;
  private String serverAction;
  private String targetAction;
}
