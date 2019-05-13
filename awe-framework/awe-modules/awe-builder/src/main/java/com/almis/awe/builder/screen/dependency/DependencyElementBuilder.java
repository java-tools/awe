package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.enumerates.Attribute;
import com.almis.awe.builder.enumerates.Condition;
import com.almis.awe.builder.enumerates.Event;
import com.almis.awe.builder.enumerates.View;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author dfuentes
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class DependencyElementBuilder extends AweBuilder<DependencyElementBuilder, DependencyElement> {

  private Attribute attribute;
  private Attribute attribute2;
  private Condition condition;
  private Event event;
  private View view;
  private View view2;
  private String id2;
  private String alias;
  private String column;
  private String column2;
  private String name;
  private String row;
  private String value;
  private boolean cancel;
  private boolean checkChanges;
  private boolean optional;

  @Override
  public DependencyElement build() {
    return build(new DependencyElement());
  }

  @Override
  public DependencyElement build(DependencyElement dependencyElement) {
    super.build(dependencyElement)
      .setId2(getId2())
      .setAlias(getAlias())
      .setColumn(getColumn())
      .setColumn2(getColumn2())
      .setName(getName())
      .setRow(getRow())
      .setValue(getValue())
      .setCancel(isCancel())
      .setCheckChanges(isCheckChanges())
      .setOptional(isOptional());

    if (getAttribute() != null) {
      dependencyElement.setAttribute(getAttribute().toString());
    }

    if (getAttribute2() != null) {
      dependencyElement.setAttribute2(getAttribute2().toString());
    }

    if (getCondition() != null) {
      dependencyElement.setCondition(getCondition().toString());
    }

    if (getEvent() != null) {
      dependencyElement.setEvent(getEvent().toString());
    }

    if (getView() != null) {
      dependencyElement.setView(getView().toString());
    }

    if (getView2() != null) {
      dependencyElement.setView2(getView2().toString());
    }

    return dependencyElement;
  }
}
