package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.enumerates.Action;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.component.ActionAttributes;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import lombok.Getter;

/**
 * @author dfuentes
 */
@Getter
public abstract class AbstractActionBuilder<T extends AbstractActionBuilder, I extends AbstractAction> extends AweBuilder<T, I> {

  private ActionAttributes actionAttributes;

  public AbstractActionBuilder() {
    super();
    this.actionAttributes = new ActionAttributes(this);
  }

  @Override
  public I build(I action) {
    action = super.build(action);
    getActionAttributes().asAction(action);
    return action;
  }

  /**
   * Set asynchronous
   *
   * @param asynchronous
   * @return Builder
   */
  public T setAsynchronous(boolean asynchronous) {
    getActionAttributes().setAsynchronous(asynchronous);
    return (T) this;
  }

  /**
   * Set silent
   *
   * @param silent
   * @return Builder
   */
  public T setSilent(boolean silent) {
    getActionAttributes().setSilent(silent);
    return (T) this;
  }

  /**
   * Set value
   *
   * @param value value
   * @return Builder
   */
  public T setValue(String value) {
    getActionAttributes().setValue(value);
    return (T) this;
  }

  /**
   * Set context
   *
   * @param context context
   * @return Builder
   */
  public T setContext(String context) {
    getActionAttributes().setContext(context);
    return (T) this;
  }

  /**
   * Set server action
   *
   * @param serverAction server action
   * @return Builder
   */
  public T setServerAction(ServerAction serverAction) {
    getActionAttributes().setServerAction(serverAction);
    return (T) this;
  }

  /**
   * Set target
   *
   * @param target target
   * @return Builder
   */
  public T setTarget(String target) {
    getActionAttributes().setTarget(target);
    return (T) this;
  }


  /**
   * Set target action
   *
   * @param target target action
   * @return Builder
   */
  public T setTargetAction(String target) {
    getActionAttributes().setTargetAction(target);
    return (T) this;
  }

  /**
   * Set type
   *
   * @param type action type
   * @return Builder
   */
  public T setType(Action type) {
    getActionAttributes().setType(type);
    return (T) this;
  }
}
