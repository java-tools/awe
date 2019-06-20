package com.almis.awe.model.settings;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
@Getter
@Setter
@Accessors(chain = true)
@ConfigurationProperties(prefix = "settings.chart.options")
public class WebChartOptions {
  private Integer limitPointsSerie;
}
