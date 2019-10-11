package com.almis.awe.listener;

import lombok.extern.log4j.Log4j2;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import java.io.Serializable;

@Log4j2
public class CacheEventLogger implements CacheEventListener<Serializable, Object> {

  @Override
  public void onEvent(CacheEvent<? extends Serializable, ? extends Object> cacheEvent) {
    log.debug("Event: {} - Key: {} - old value: {} - new value: {}", cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
  }
}