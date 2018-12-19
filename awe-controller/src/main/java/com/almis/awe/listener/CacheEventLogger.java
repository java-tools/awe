package com.almis.awe.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import java.io.Serializable;

public class CacheEventLogger implements CacheEventListener<Serializable, Object> {

  Logger logger = LogManager.getLogger(CacheEventLogger.class);

  @Override
  public void onEvent(CacheEvent<? extends Serializable, ? extends Object> cacheEvent) {
    logger.debug("Event: " + cacheEvent.getType() + " Key: " + cacheEvent.getKey() + " old value: " + cacheEvent.getOldValue() + " new value: " + cacheEvent.getNewValue());
  }
}