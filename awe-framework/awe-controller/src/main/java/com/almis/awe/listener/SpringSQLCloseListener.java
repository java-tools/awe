package com.almis.awe.listener;

import com.almis.awe.model.util.data.StringUtil;
import com.querydsl.sql.SQLBaseListener;
import com.querydsl.sql.SQLListenerContext;
import lombok.extern.log4j.Log4j2;

/**
 * {@code SpringSQLCloseListener} closes the JDBC connection at the end of the query
 * or clause execution
 */
@Log4j2
public final class SpringSQLCloseListener extends SQLBaseListener {

  @Override
  public void end(SQLListenerContext context) {
    log.debug("Connection finished: {}", StringUtil.toUnilineText(context.getSQL()));
  }
}
