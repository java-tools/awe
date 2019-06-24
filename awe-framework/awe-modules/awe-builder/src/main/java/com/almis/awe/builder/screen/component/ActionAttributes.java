package com.almis.awe.builder.screen.component;

import com.almis.awe.builder.enumerates.Action;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.screen.base.AbstractAttributes;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ActionAttributes<B extends AweBuilder> extends AbstractAttributes<B> {
  private Action type;
  private ServerAction serverAction;
  private boolean asynchronous = false;
  private boolean silent = false;
  private String targetAction;
  private String target;
  private String context;
  private String value;
  private String label;

  public ActionAttributes(B builder) {
    super(builder);
  }

  /**
   * Build attributes in criterion
   *
   * @param element Criterion
   * @param <E>
   * @return Element with attributes
   */
  public <E extends AbstractAction> E asAction(E element) {
    E action = (E) element
      .setAsync(isAsynchronous())
      .setSilent(isSilent())
      .setTargetAction(getTargetAction())
      .setTarget(getTarget())
      .setValue(getValue())
      .setScreenContext(getContext())
      .setLabel(getLabel());

    if (getType() != null) {
      action.setType(getType().toString());
    }

    if (getServerAction() != null) {
      action.setServerAction(getServerAction().toString());
    }

    return action;
  }
}
