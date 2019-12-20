package com.almis.awe.model.settings;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
@Getter
@Setter
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "settings.pivot.options")
public class WebPivotOptions {
  private Integer numGroup;
}
