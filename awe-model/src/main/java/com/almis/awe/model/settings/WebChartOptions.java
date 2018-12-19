/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.model.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
@ConfigurationProperties(prefix = "settings.chart.options")
public class WebChartOptions {

  private Integer limitPointsSerie;

  /**
   * @return the limitPointsSerie
   */
  public Integer getLimitPointsSerie() {
    return limitPointsSerie;
  }

  /**
   * @param limitPointsSerie the limitPointsSerie to set
   */
  public void setLimitPointsSerie(Integer limitPointsSerie) {
    this.limitPointsSerie = limitPointsSerie;
  }

}
