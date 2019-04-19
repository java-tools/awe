package com.almis.awe.model.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
@ConfigurationProperties(prefix = "settings.pivot.options")
public class WebPivotOptions {

  private Integer numGroup;

  /**
   * @return the numGroup
   */
  public Integer getNumGroup() {
    return numGroup;
  }

  /**
   * @param numGroup the numGroup to set
   */
  public void setNumGroup(Integer numGroup) {
    this.numGroup = numGroup;
  }

}