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
@ConfigurationProperties(prefix = "settings.tooltip.timeout")
public class WebTooltip {
  private Integer info;
  private Integer error;
  private Integer validate;
  private Integer help;
  private Integer warning;
  private Integer ok;
  private Integer wrong;
  private Integer chat;
}
