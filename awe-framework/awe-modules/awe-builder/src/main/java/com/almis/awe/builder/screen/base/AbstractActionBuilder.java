package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Action;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter
public abstract class AbstractActionBuilder<T, I extends AbstractAction> extends AweBuilder<T, I> {

  private Action type;
  private ServerAction serverAction;
  private boolean asynchronous = false;
  private boolean silent = false;
  private String targetAction;
  private String target;
  private String context;
  private String value;

  @Override
  public I build(I action) {
    super.build(action)
      .setAsync(isAsynchronous())
      .setSilent(isSilent())
      .setTargetAction(getTargetAction())
      .setTarget(getTarget())
      .setValue(getValue())
      .setScreenContext(getContext());

    if (getType() != null) {
      action.setType(getType().toString());
    }

    if (getServerAction() != null) {
      action.setServerAction(getServerAction().toString());
    }

    return action;
  }

  /**
   * Set the type
   * @param type Type
   * @return This
   */
  public T setType(Action type) {
    this.type = type;
    return (T) this;
  }

  /**
   * Set the server action
   * @param serverAction Server action
   * @return This
   */
  public T setServerAction(ServerAction serverAction) {
    this.serverAction = serverAction;
    return (T) this;
  }

  /**
   * Set the asynchronous flag
   * @param asynchronous Asynchronous flag
   * @return This
   */
  public T setAsynchronous(boolean asynchronous) {
    this.asynchronous = asynchronous;
    return (T) this;
  }

  /**
   * Set the silent flag
   * @param silent Silent flag
   * @return This
   */
  public T setSilent(boolean silent) {
    this.silent = silent;
    return (T) this;
  }

  /**
   * Set the target action
   * @param targetAction Target action
   * @return This
   */
  public T setTargetAction(String targetAction) {
    this.targetAction = targetAction;
    return (T) this;
  }

  /**
   * Set the target
   * @param target Target
   * @return This
   */
  public T setTarget(String target) {
    this.target = target;
    return (T) this;
  }

  /**
   * Set the context
   * @param context Context
   * @return This
   */
  public T setContext(String context) {
    this.context = context;
    return (T) this;
  }

  /**
   * Set the value
   * @param value Value
   * @return This
   */
  public T setValue(String value) {
    this.value = value;
    return (T) this;
  }
}
