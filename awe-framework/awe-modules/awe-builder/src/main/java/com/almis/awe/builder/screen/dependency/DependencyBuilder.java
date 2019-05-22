package com.almis.awe.builder.screen.dependency;

import com.almis.awe.builder.enumerates.DependencyType;
import com.almis.awe.builder.enumerates.ServerAction;
import com.almis.awe.builder.enumerates.SourceType;
import com.almis.awe.builder.enumerates.TargetType;
import com.almis.awe.builder.screen.base.AweBuilder;
import com.almis.awe.model.entities.screen.component.action.Dependency;
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
public class DependencyBuilder extends AweBuilder<DependencyBuilder, Dependency> {

  private ServerAction serverAction;
  private SourceType sourceType;
  private TargetType targetType;
  private DependencyType type;
  private boolean invert;
  private boolean initial;
  private String formule;
  private String label;
  private String targetAction;
  private String value;

  @Override
  public Dependency build() {
    return build(new Dependency());
  }

  @Override
  public Dependency build(Dependency dependency) {
    super.build(dependency)
      .setInvert(isInvert())
      .setInitial(isInitial())
      .setFormule(getFormule())
      .setTargetAction(getTargetAction())
      .setValue(getValue())
      .setLabel(getLabel());

    if (getServerAction() != null) {
      dependency.setServerAction(getServerAction().toString());
    }

    if (getSourceType() != null) {
      dependency.setSourceType(getSourceType().toString());
    }

    if (getTargetType() != null) {
      dependency.setTargetType(getTargetType().toString());
    }

    if (getType() != null) {
      dependency.setType(getType().toString());
    }

    return dependency;
  }

  /**
   * Add dependency action
   *
   * @param dependencyAction
   * @return
   */
  public DependencyBuilder addDependencyAction(DependencyActionBuilder... dependencyAction) {
    addAllElements(dependencyAction);
    return this;
  }

  /**
   * Add dependency element
   *
   * @param dependencyElement
   * @return
   */
  public DependencyBuilder addDependencyElement(DependencyElementBuilder... dependencyElement) {
    addAllElements(dependencyElement);
    return this;
  }
}
