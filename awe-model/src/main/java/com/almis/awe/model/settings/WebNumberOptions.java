package com.almis.awe.model.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
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

  /**
   * @return the aSep
   */
  public String getaSep() {
    return aSep;
  }

  /**
   * @param aSep the aSep to set
   */
  public void setaSep(String aSep) {
    this.aSep = aSep;
  }

  /**
   * @return the dGroup
   */
  public Integer getdGroup() {
    return dGroup;
  }

  /**
   * @param dGroup the dGroup to set
   */
  public void setdGroup(Integer dGroup) {
    this.dGroup = dGroup;
  }

  /**
   * @return the aDec
   */
  public String getaDec() {
    return aDec;
  }

  /**
   * @param aDec the aDec to set
   */
  public void setaDec(String aDec) {
    this.aDec = aDec;
  }

  /**
   * @return the aSign
   */
  public String getaSign() {
    return aSign;
  }

  /**
   * @param aSign the aSign to set
   */
  public void setaSign(String aSign) {
    this.aSign = aSign;
  }

  /**
   * @return the pSign
   */
  public String getpSign() {
    return pSign;
  }

  /**
   * @param pSign the pSign to set
   */
  public void setpSign(String pSign) {
    this.pSign = pSign;
  }

  /**
   * @return the vMin
   */
  public Float getvMin() {
    return vMin;
  }

  /**
   * @param vMin the vMin to set
   */
  public void setvMin(Float vMin) {
    this.vMin = vMin;
  }

  /**
   * @return the vMax
   */
  public Float getvMax() {
    return vMax;
  }

  /**
   * @param vMax the vMax to set
   */
  public void setvMax(Float vMax) {
    this.vMax = vMax;
  }

  /**
   * @return the mDec
   */
  public Integer getmDec() {
    return mDec;
  }

  /**
   * @param mDec the mDec to set
   */
  public void setmDec(Integer mDec) {
    this.mDec = mDec;
  }

  /**
   * @return the mRound
   */
  public String getmRound() {
    return mRound;
  }

  /**
   * @param mRound the mRound to set
   */
  public void setmRound(String mRound) {
    this.mRound = mRound;
  }

  /**
   * @return the aPad
   */
  public Boolean getaPad() {
    return aPad;
  }

  /**
   * @param aPad the aPad to set
   */
  public void setaPad(Boolean aPad) {
    this.aPad = aPad;
  }

  /**
   * @return the wEmpty
   */
  public String getwEmpty() {
    return wEmpty;
  }

  /**
   * @param wEmpty the wEmpty to set
   */
  public void setwEmpty(String wEmpty) {
    this.wEmpty = wEmpty;
  }

}
