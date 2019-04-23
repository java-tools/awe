package com.almis.awe.listener;

import com.almis.awe.model.util.log.LogUtil;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

public class TemplateErrorListener implements STErrorListener {

  // Autowired services
  LogUtil logger;

  /**
   * Autowired constructor
   * @param logger Logger
   */
  @Autowired
  public TemplateErrorListener(LogUtil logger) {
    this.logger = logger;
  }

  @Override
  public void compileTimeError(STMessage stMessage) {
    logger.log(TemplateErrorListener.class, Level.ERROR, "Compile time error: " + stMessage.toString(), stMessage.cause);
  }

  @Override
  public void runTimeError(STMessage stMessage) {
    logger.log(TemplateErrorListener.class, Level.ERROR, "Run time error: " + stMessage.toString(), stMessage.cause);
  }

  @Override
  public void IOError(STMessage stMessage) {
    logger.log(TemplateErrorListener.class, Level.ERROR, "I/O error: " + stMessage.toString(), stMessage.cause);
  }

  @Override
  public void internalError(STMessage stMessage) {
    logger.log(TemplateErrorListener.class, Level.ERROR, "Internal error: " +stMessage.toString(), stMessage.cause);
  }
}
