package com.almis.awe.builder.client;

import com.almis.awe.model.entities.actions.ClientAction;
import com.almis.awe.model.entities.actions.ComponentAddress;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * Client action builder
 * @author pgarcia
 */
@Getter
@Setter
@Accessors(chain = true)
public class ClientActionBuilder<T> {

  private String type;
  private String target;
  private String context;
  private ComponentAddress address;
  private Boolean async;
  private Boolean silent;
  private Map<String, Object> parameters;

  /**
   * Build Client action
   * @return Client action built
   */
  public ClientAction build() {
    return ClientAction.builder()
      .type(getType())
      .target(getTarget())
      .context(getContext())
      .address(getAddress())
      .async(getAsync())
      .silent(getSilent())
      .parameterMap(getParameters())
      .build();
  }

  /**
   * Add a parameter to client action
   * @param name
   * @param value
   * @return
   */
  public T addParameter(String name, Object value) {
    if (parameters == null) {
      parameters = new HashMap<>();
    }
    parameters.put(name, value);
    return (T) this;
  }

  /**
   * Set async
   * @param async Async
   * @return this
   */
  public T setAsync(boolean async) {
    this.async = async;
    return (T) this;
  }

  /**
   * Set silent
   * @param silent Silent
   * @return this
   */
  public T setSilent(boolean silent) {
    this.silent = silent;
    return (T) this;
  }

  /**
   * Set target
   * @param target Target
   * @return this
   */
  public T setTarget(String target) {
    this.target = target;
    return (T) this;
  }

  /**
   * Set address
   * @param address Component address
   * @return this
   */
  public T setAddress(ComponentAddress address) {
    this.address = address;
    return (T) this;
  }

}
