/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.almis.awe.model.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WebTooltip options
 * 
 * @author pgarcia
 */
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

  /**
   * @return the info
   */
  public Integer getInfo() {
    return info;
  }

  /**
   * @param info the info to set
   */
  public void setInfo(Integer info) {
    this.info = info;
  }

  /**
   * @return the error
   */
  public Integer getError() {
    return error;
  }

  /**
   * @param error the error to set
   */
  public void setError(Integer error) {
    this.error = error;
  }

  /**
   * @return the validate
   */
  public Integer getValidate() {
    return validate;
  }

  /**
   * @param validate the validate to set
   */
  public void setValidate(Integer validate) {
    this.validate = validate;
  }

  /**
   * @return the help
   */
  public Integer getHelp() {
    return help;
  }

  /**
   * @param help the help to set
   */
  public void setHelp(Integer help) {
    this.help = help;
  }

  /**
   * @return the warning
   */
  public Integer getWarning() {
    return warning;
  }

  /**
   * @param warning the warning to set
   */
  public void setWarning(Integer warning) {
    this.warning = warning;
  }

  /**
   * @return the ok
   */
  public Integer getOk() {
    return ok;
  }

  /**
   * @param ok the ok to set
   */
  public void setOk(Integer ok) {
    this.ok = ok;
  }

  /**
   * @return the wrong
   */
  public Integer getWrong() {
    return wrong;
  }

  /**
   * @param wrong the wrong to set
   */
  public void setWrong(Integer wrong) {
    this.wrong = wrong;
  }

  /**
   * @return the chat
   */
  public Integer getChat() {
    return chat;
  }

  /**
   * @param chat the chat to set
   */
  public void setChat(Integer chat) {
    this.chat = chat;
  }
}
