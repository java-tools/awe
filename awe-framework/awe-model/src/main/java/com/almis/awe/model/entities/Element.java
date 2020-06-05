package com.almis.awe.model.entities;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.screen.*;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.action.AbstractAction;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.model.entities.screen.component.grid.GroupHeader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Element decorator
 * Used to parse all tags from screen XML files with XStream
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@Accessors(chain = true)
@NoArgsConstructor
@XStreamInclude({Screen.class, Tag.class, Component.class, Menu.class, Message.class, Dependency.class, DependencyElement.class,
  AbstractAction.class, GroupHeader.class, Include.class, View.class, Message.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Element implements XMLNode, Copyable {

  private static final long serialVersionUID = -5600527339665790940L;

  // Element identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Tag TYPE (DIV, P, INPUT, etc)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  // Element CSS classes
  @XStreamAlias("style")
  @XStreamAsAttribute
  private String style;

  // Contained text
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Title
  @XStreamAlias("title")
  @XStreamAsAttribute
  private String title;

  // Expandible
  @XStreamAlias("expandible")
  @XStreamAsAttribute
  private String expand;

  // Help attribute (contains literal indicating HELP text VALUE)
  @XStreamAlias("help")
  @XStreamAsAttribute
  private String help;

  // HelpImage attribute that indicates image location for HELP
  @XStreamAlias("help-image")
  @XStreamAsAttribute
  private String helpImage;

  // Children List
  @XStreamImplicit
  private List<Element> elementList;

  /**
   * Returns the children element list
   *
   * @param <T> element list
   * @return Children List
   */
  @JsonIgnore
  public <T extends Element> List<T> getElementList() {
    return elementList == null ? Collections.emptyList() : (List<T>) elementList;
  }

  /**
   * Add an element to the list
   *
   * @param element element to add
   * @param <T> element object type
   * @param <G> return element type
   * @return element
   */
  public <T extends Element, G extends Element> G addElement(T element) {
    if (elementList == null) {
      elementList = new ArrayList<>();
    }
    elementList.add(element);
    return (G) this;
  }

  /**
   * Generates the output HTML of the element
   *
   * @param group String Template Group
   * @return Code
   */
  public ST generateTemplate(STGroup group) {
    ST template = group.createStringTemplate(group.rawGetTemplate(getTemplate()));
    List<ST> children = new ArrayList<>();

    // Call generate method on all children
    for (Element element : getElementList()) {
      // Generate the children
      children.add(element.generateTemplate(group));
    }

    // Generate template
    template.add("e", this).add("children", children);

    // Retrieve code
    return template;
  }

  /**
   * Generates the output HTML of the element
   *
   * @param group      String Template Group
   * @param label      Parent label
   * @param developers Help for developers
   * @return Code
   */
  public ST generateHelpTemplate(STGroup group, String label, boolean developers) {
    return generateHelpTemplate(group, label, getHelpTemplate(), developers);
  }

  /**
   * Generates the help template of the element
   *
   * @param group        String Template Group
   * @param label        Parent label
   * @param templateName String Template name
   * @param developers   Help for developers
   * @return Code
   */
  public ST generateHelpTemplate(STGroup group, String label, String templateName, boolean developers) {
    ST template = group.createStringTemplate(group.rawGetTemplate(templateName));
    List<ST> children = new ArrayList<>();
    String currentLabel = getLabel() == null ? label : getLabel();

    // Call generate method on all children
    for (Element element : getElementList()) {
      // Generate the children
      children.add(element.generateHelpTemplate(group, currentLabel, developers));
    }

    // Generate template
    template.add("e", this)
      .add("label", currentLabel)
      .add("content", children)
      .add("developers", developers);

    // Retrieve code
    return template;
  }

  /**
   * Retrieve element template (To be overwritten)
   *
   * @return Element template
   */
  @JsonIgnore
  public String getTemplate() {
    // Retrieve code
    return "";
  }

  /**
   * Retrieve help template
   *
   * @return Help template
   */
  @JsonIgnore
  public String getHelpTemplate() {
    // Retrieve code
    return AweConstants.TEMPLATE_HELP_ELEMENT;
  }

  /**
   * Returns the children element list of a desired TYPE
   *
   * @param <T>              element class type
   * @param elementClassList Element class
   * @return Children List
   */
  @SafeVarargs
  public final <T> List<T> getElementsByType(Class<T>... elementClassList) {
    return getElementsByType(true, elementClassList);
  }

  /**
   * Returns the children element list of a desired TYPE
   *
   * @param <T> element class type
   * @param elementClassList element class
   * @param processDialog    flag to check dialog elements
   * @return Children List
   */
  public <T> List<T> getElementsByType(boolean processDialog, Class<T>... elementClassList) {

    // Variable definition
    List<T> outLst = new ArrayList<>();

    // Call generate method on all children
    if (this.elementList != null) {
      for (Element element : this.elementList) {
        // If the element is of the desired TYPE, add it
        for (Class<T> elementClass : elementClassList) {
          if (elementClass.isInstance(element)) {
            outLst.add((T) element);
          }
        }

        // Find the elements TYPE in children
        outLst.addAll(element.getElementsByType(processDialog, elementClassList));
      }
    }

    // Return the array
    return outLst;
  }

  /**
   * Returns the children element list of a desired TYPE
   *
   * @param <T>              element class type
   * @param elementClassList element class
   * @return Children List
   */
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public final <T> List<T> getChildrenByType(Class<T>... elementClassList) {

    // Variable definition
    List<T> outLst = new ArrayList<>();

    // Call generate method on all children
    if (this.elementList != null) {
      for (Element element : this.elementList) {
        // If the element is of the desired TYPE, add it
        for (Class<T> elementClass : elementClassList) {
          if (elementClass.isInstance(element)) {
            outLst.add((T) element);
          }
        }
      }
    }

    // Return the array
    return outLst;
  }

  /**
   * Returns the children element list of a desired id
   *
   * @param identifier Element identifier
   * @return Children List
   */
  public List<Element> getElementsById(String identifier) {

    // Variable definition
    List<Element> outLst = new ArrayList<>();

    // Call generate method on all children
    if (this.elementList != null) {
      for (Element cnt : this.elementList) {
        // If the element has the right id, add it
        if (cnt.getElementKey() != null && identifier.equalsIgnoreCase(cnt.getElementKey())) {
          outLst.add(cnt);
        }

        // Find the elements TYPE in children
        outLst.addAll(cnt.getElementsById(identifier));
      }
    }

    // Return the array
    return outLst;
  }

  /**
   * Get print element list (to be overwritten)
   *
   * @param printElementList Print element list
   * @param label            Previous label
   * @param parameters       Parameters
   * @param dataSuffix       data suffix
   * @return Print bean
   */
  @JsonIgnore
  public List<Element> getReportStructure(List<Element> printElementList, String label, ObjectNode parameters, String dataSuffix) {
    // Call generate method on all children
    for (Element element : getElementList()) {
      element.getReportStructure(printElementList, label, parameters, dataSuffix);
    }
    return printElementList;
  }

  @JsonIgnore
  @Override
  public String getElementKey() {
    return getId() == null ? AweConstants.NO_KEY : getId();
  }
}
