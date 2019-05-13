/*
 * Package definition
 */
package com.almis.awe.model.entities.screen.data;

/*
 * File Imports
 */

import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * AbstractAction Class
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
public class ScreenComponent {

  // Component id
  private String id;

  // Component controller
  private Component controller;

  // Component model
  private ComponentModel model;

  /**
   * Retrieve component id
   *
   * @return Component id
   */
  public String getId() {
    return id;
  }

  /**
   * Set component id
   *
   * @param id Component id
   * @return Screen component
   */
  public ScreenComponent setId(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get controller
   *
   * @return Component controller
   */
  public Component getController() {
    return controller;
  }

  /**
   * Set controller
   *
   * @param controller Component controller
   * @return Screen component
   */
  public ScreenComponent setController(Component controller) {
    this.controller = controller;
    return this;
  }

  /**
   * Get model
   *
   * @return Component model
   */
  public ComponentModel getModel() {
    return model;
  }

  /**
   * Set model
   *
   * @param model Component model
   * @return Screen component
   */
  public ScreenComponent setModel(ComponentModel model) {
    this.model = model;
    return this;
  }
}
