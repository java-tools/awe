package com.almis.awe.model.entities.screen.data;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Screen;
import com.almis.awe.model.entities.screen.component.Component;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Screen data Class
 * <p>
 * Used to retrieve screen data in JSON format
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScreenData {

  // Screen components
  private final List<ScreenComponent> components = new ArrayList<>();

  // Screen data messages
  private final Map<String, Message> messages = new HashMap<>();

  // Screen properties
  private Map<String, String> screenProperties;

  // Screen structure
  private Screen structure;

  // Screen actions
  private final List<ClientAction> actions = new ArrayList<>();

  /**
   * Add a component
   *
   * @param component Component
   * @return this
   */
  public ScreenData addComponent(ScreenComponent component) {
    components.add(component);
    return this;
  }

  /**
   * Add a component
   *
   * @param key        Component key
   * @param controller Component controller
   * @param model      Component model
   * @return this
   */
  public ScreenData addComponent(String key, Component controller, ComponentModel model) {
    // Add element
    return addComponent(new ScreenComponent()
      .setId(key)
      .setController(controller)
      .setModel(model));
  }

  /**
   * Add a message
   *
   * @param key     Message key
   * @param message Message text
   * @return this
   */
  public ScreenData addMessage(String key, Message message) {
    // Add element
    messages.put(key, message);
    return this;
  }

  /**
   * Getter for screen properties (to retrieve it empty if not defined)
   *
   * @return Screen properties
   */
  @JsonGetter("screen")
  public Map<String, String> getScreen() {
    if (screenProperties == null) {
      screenProperties = new HashMap<>();
    }
    return screenProperties;
  }

  /**
   * Add a screen property
   *
   * @param key      Property key
   * @param property Property text
   * @return this
   */
  public ScreenData addScreenProperty(String key, String property) {
    if (screenProperties == null) {
      screenProperties = new HashMap<>();
    }
    // Add element
    screenProperties.put(key, property);
    return this;
  }

  /**
   * Add a error
   *
   * @param error Error to add as message
   * @return this
   */
  public ScreenData addError(AWException error) {
    // Add element
    return addAction(new ClientAction("message")
      .addParameter(AweConstants.ACTION_MESSAGE_TYPE_ATTRIBUTE, error.getType().toString())
      .addParameter(AweConstants.ACTION_MESSAGE_TITLE_ATTRIBUTE, error.getTitle())
      .addParameter(AweConstants.ACTION_MESSAGE_DESCRIPTION_ATTRIBUTE, error.getMessage())
      .setAsync(true)
      .setSilent(true));
  }

  /**
   * Add an action
   *
   * @param action Action to add
   * @return this
   */
  public ScreenData addAction(ClientAction action) {
    // Add element
    actions.add(action);
    return this;
  }
}
