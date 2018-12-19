/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.data;

/*
 * File Imports
 */

import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * ScreenAction Class
 *
 * Used to parse screen actions with XStream
 *
 *
 * Parent class for ButtonAction, Menu and Option classes
 *
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScreenColumn extends ScreenComponent {

  @JsonUnwrapped
  private Component controller;
  private String gridId;

  @Override
  public Component getController() {
    return controller;
  }

  @Override
  public ScreenColumn setController(Component controller) {
    this.controller = controller;
    return this;
  }

  /**
   * Retrieve grid id
   *
   * @return Grid id
   */
  public String getGridId() {
    return gridId;
  }

  /**
   * Store grid id
   *
   * @param gridId Grid id
   * @return this
   */
  public ScreenColumn setGridId(String gridId) {
    this.gridId = gridId;
    return this;
  }
}
