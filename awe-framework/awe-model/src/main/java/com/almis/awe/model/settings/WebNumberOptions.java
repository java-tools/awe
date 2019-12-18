package com.almis.awe.model.settings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
@ConfigurationProperties(prefix = "settings.numbers.options")
public class WebNumberOptions {
  private String aSep;
  private Integer dGroup;
  private String aDec;
  private String aSign;
  private String pSign;
  private Float vMin;
  private Float vMax;
  private Integer mDec;
  private String mRound;
  private Boolean aPad;
  private String wEmpty;
}
