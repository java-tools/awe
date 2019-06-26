package com.almis.awe.model.component;

import java.io.Serializable;
import java.util.Observable;

/**
 * ClientTracker Class
 * Bean class with user navigation info
 *
 * @author Pablo GARCIA - 03/Aug/2017
 */
public class AweClientTracker extends Observable implements Serializable {

  private String lastScreen = "";

  /**
   * @return the lastFrameChanged
   */
  public String getLastScreenChanged() {
    return lastScreen;
  }

  /**
   * @param screen the last screen to set
   */
  public void setLastScreenChange(String screen) {
    this.lastScreen = screen == null ? "" : screen;
    this.setChanged();
    this.notifyObservers(lastScreen);
  }

  /**
   * Change screen for view
   *
   * @param screen New screen
   */
  public void navigateToScreen(String screen) {
    this.setLastScreenChange(screen);
  }

  /**
   * Notify observers on destroy
   */
  public void removeObservers() {
    setLastScreenChange("");
  }
}
