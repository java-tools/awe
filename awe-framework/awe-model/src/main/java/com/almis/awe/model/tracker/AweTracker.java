package com.almis.awe.model.tracker;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public abstract class AweTracker {
  private Set<AweTrackable> trackables = new ConcurrentSkipListSet<>();

  /**
   * Notify an update to trackables
   * @param value Value updated
   */
  public void notify(Object value) {
    for (AweTrackable trackable : trackables) {
      trackable.update(this, value);
    }
  }

  public void track(AweTrackable trackable) {
    trackables.add(trackable);
  }

  public void untrack(AweTrackable trackable) {
    trackables.remove(trackable);
  }

  public void untrackAll() {
    trackables.clear();
  }
}
