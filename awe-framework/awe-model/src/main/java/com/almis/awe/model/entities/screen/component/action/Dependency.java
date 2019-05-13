package com.almis.awe.model.entities.screen.component.action;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Element;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Dependency Class
 *
 * Used to parse a criteria dependency with XStream
 *
 * @author Pablo GARCIA - 05/AGO/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("dependency")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"visible", "iconLoading"})
public class Dependency extends Element {

  private static final long serialVersionUID = -6213314601320091023L;

  // Dependency action
  @XStreamAlias("action")
  @XStreamAsAttribute
  private String action;

  // Dependency initial attribute
  @XStreamAlias("initial")
  @XStreamAsAttribute
  private Boolean initial;

  // Invert dependency conditions
  @XStreamAlias("invert")
  @XStreamAsAttribute
  private Boolean invert;

  // Dependency value
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Dependency formule
  @XStreamAlias("formule")
  @XStreamAsAttribute
  private String formule;

  // Dependency source
  @JsonProperty("source")
  @XStreamAlias("source-type")
  @XStreamAsAttribute
  private String sourceType;

  // Dependency target
  @JsonProperty("target")
  @XStreamAlias("target-type")
  @XStreamAsAttribute
  private String targetType;

  // Silent action
  @XStreamAlias("silent")
  @XStreamAsAttribute
  private Boolean silent;

  // Async action
  @XStreamAlias("async")
  @XStreamAsAttribute
  private Boolean async;

  // Server Action
  @XStreamAlias("server-action")
  @XStreamAsAttribute
  private String serverAction;

  // Target Action
  @XStreamAlias("target-action")
  @XStreamAsAttribute
  private String targetAction;

  @Override
  public Dependency copy() throws AWException {
    return this.toBuilder()
      .elementList(ListUtil.copyList(getElementList()))
      .build();
  }

  /**
   * Returns if is initial
   * @return Is initial
   */
  @JsonGetter("initial")
  public boolean isInitial() {
    return initial != null && initial;
  }

  /**
   * Returns if is invert
   * @return Is invert
   */
  @JsonGetter("invert")
  public boolean isInvert() {
    return invert != null && invert;
  }

  /**
   * Returns if is async
   * @return Is async
   */
  @JsonGetter("async")
  public boolean isAsync() {
    return async != null && async;
  }

  /**
   * Returns if is silent
   * @return Is silent
   */
  @JsonGetter("silent")
  public boolean isSilent() {
    return silent != null && silent;
  }

  /**
   * Returns the dependency target action
   *
   * @return Dependency target action
   */
  @JsonGetter("query")
  public String getTargetActionAsQuery() {
    return getTargetAction();
  }

  @Override
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_EMPTY;
  }

  /**
   * Return type for JSON serialization
   *
   * @return type
   */
  @JsonGetter("type")
  public String getTypeConverter() {
    if (getType() != null) {
      return getType();
    }
    return "and";
  }

  /**
   * Returns the dependency element list for JSON serialization
   *
   * @return Dependency element list
   */
  @JsonGetter("elements")
  public List<DependencyElement> getDependencyElementConverter() {
    // Return string parameter list
    return getElementsByType(DependencyElement.class);
  }

  /**
   * Returns the dependency action list for JSON serialization
   *
   * @return Dependency action list
   */
  @JsonGetter("actions")
  public List<DependencyAction> getDependencyActionConverter() {
    return getElementsByType(DependencyAction.class);
  }
}
