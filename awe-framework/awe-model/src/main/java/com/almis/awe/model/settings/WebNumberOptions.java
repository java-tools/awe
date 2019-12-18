package com.almis.awe.model.settings;

import com.fasterxml.jackson.annotation.JsonGetter;
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
@Setter
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "settings.numbers.options")
public class WebNumberOptions {
  @Getter(onMethod = @__(@JsonGetter("aSep")))
  private String aSep;
  @Getter(onMethod = @__(@JsonGetter("dGroup")))
  private Integer dGroup;
  @Getter(onMethod = @__(@JsonGetter("aDec")))
  private String aDec;
  @Getter(onMethod = @__(@JsonGetter("aSign")))
  private String aSign;
  @Getter(onMethod = @__(@JsonGetter("pSign")))
  private String pSign;
  @Getter(onMethod = @__(@JsonGetter("vMin")))
  private Float vMin;
  @Getter(onMethod = @__(@JsonGetter("vMax")))
  private Float vMax;
  @Getter(onMethod = @__(@JsonGetter("mDec")))
  private Integer mDec;
  @Getter(onMethod = @__(@JsonGetter("mRound")))
  private String mRound;
  @Getter(onMethod = @__(@JsonGetter("aPad")))
  private Boolean aPad;
  @Getter(onMethod = @__(@JsonGetter("wEmpty")))
  private String wEmpty;
}
