package com.almis.awe.listener;

import com.almis.awe.model.util.data.StringUtil;
import com.almis.awe.model.util.log.LogUtil;
import com.querydsl.sql.SQLBaseListener;
import com.querydsl.sql.SQLListenerContext;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@code SpringSQLCloseListener} closes the JDBC connection at the end of the query
 * or clause execution
 */
public final class SpringSQLCloseListener extends SQLBaseListener {

  // Logger
  private LogUtil logger;

  /**
   * Autowired constructor
   * @param logger Log util service
   */
  @Autowired
  public SpringSQLCloseListener(LogUtil logger) {
    this.logger = logger;
  }

  @Override
  public void end(SQLListenerContext context) {
    logger.log(SpringSQLCloseListener.class, Level.DEBUG, "Connection finished: {0}", StringUtil.toUnilineText(context.getSQL()));
  }
}
