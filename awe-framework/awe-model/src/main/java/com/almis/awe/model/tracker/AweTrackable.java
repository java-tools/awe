package com.almis.awe.model.tracker;

public interface AweTrackable {

  /**
   * Tracker has an update
   * @param tracker Tracker
   * @param value Updated value
   */
  public void update(AweTracker tracker, Object value);
}
