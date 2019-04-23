package com.almis.awe.model.entities;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.menu.Menu;
import com.almis.awe.model.entities.menu.Option;
import com.almis.awe.model.entities.screen.Message;
import com.almis.awe.model.entities.screen.Tag;
import com.almis.awe.model.entities.screen.component.Component;
import com.almis.awe.model.entities.screen.component.action.Dependency;
import com.almis.awe.model.entities.screen.component.action.DependencyElement;
import com.almis.awe.model.entities.screen.component.action.ScreenAction;
import com.almis.awe.model.util.data.ListUtil;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Element Class
 * Used to parse all tags from screen XML files with XStream
 *
 * @author Pablo GARCIA - 24/JUN/2010
 */
@XStreamInclude({Tag.class, Component.class, Option.class, Menu.class, Message.class, Dependency.class, DependencyElement.class, ScreenAction.class})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Element extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = -5600527339665790940L;

  // Element identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id = null;

  // Source where generated HTML code is going to be stored
  @XStreamAlias("source")
  @XStreamAsAttribute
  private String source = null;

  // Tag TYPE (DIV, P, INPUT, etc)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type = null;

  // Element CSS classes
  @XStreamAlias("style")
  @XStreamAsAttribute
  private String style = null;

  // Contained text
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label = null;

  // Title
  @XStreamAlias("title")
  @XStreamAsAttribute
  private String title = null;

  // Expandible
  @XStreamAlias("expandible")
  @XStreamAsAttribute
  private String expand = null;

  // Help atribute (contains literal indicating HELP text VALUE)
  @XStreamAlias("help")
  @XStreamAsAttribute
  private String help = null;

  // HelpImage attribute that indicates image location for HELP
  @XStreamAlias("help-image")
  @XStreamAsAttribute
  private String helpImage = null;

  // Children List
  @XStreamImplicit
  private List<Element> elementList;

  /**
   * Default constructor
   */
  public Element() {
  }

  /**
   * Copy constructor
   *
   * @param other Element to copy
   */
  public Element(Element other) throws AWException {
    super(other);
    this.id = other.id;
    this.source = other.source;
    this.type = other.type;
    this.style = other.style;
    this.label = other.label;
    this.title = other.title;
    this.expand = other.expand;
    this.help = other.help;
    this.helpImage = other.helpImage;
    this.elementList = ListUtil.copyList(other.elementList);
  }

  /**
   * Returns the children element list
   *
   * @return Children List
   */
  @JsonIgnore
  public <T extends Element> List<T> getElementList() {
    return elementList == null ? Collections.emptyList() : (List<T>) elementList;
  }

  /**
   * Stores the children element list
   *
   * @param elementList Children List
   */
  public <T extends Element> void setElementList(List<T> elementList) {
    this.elementList = (List<Element>) elementList;
  }

  /**
   * Add an element to the list
   *
   * @param <T> Element type
   * @param element element to add
   */
  public <T extends Element, G extends Element> G addElement(T element) {
    if (elementList == null) {
      elementList = new ArrayList<>();
    }
    elementList.add(element);
    return (G) this;
  }

  /**
   * Returns element id
   *
   * @return Element id
   */
  @JsonGetter("id")
  public String getId() {
    return id;
  }

  /**
   * Stores element id
   *
   * @param id Element id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the element's TYPE
   *
   * @return Element TYPE
   */
  @JsonGetter("type")
  public String getType() {
    return type;
  }

  /**
   * Stores the element's TYPE
   *
   * @param type Element type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Return the element's CSS Class List
   *
   * @return CSS Class List
   */
  @JsonGetter("style")
  public String getStyle() {
    return style;
  }

  /**
   * Stores the CSS Class List
   *
   * @param style element style
   */
  public void setStyle(String style) {
    this.style = style;
  }

  /**
   * Returns the element's LABEL. (Text inside element) Should be translated with Context.getLocal
   *
   * @return Element Label
   */
  @JsonGetter("label")
  public String getLabel() {
    return label;
  }

  /**
   * Stores the element's LABEL.
   *
   * @param label Element Label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Returns the element's TITLE. (element description) Should be translated with Context.getLocal
   *
   * @return Element Title
   */
  @JsonGetter("title")
  public String getTitle() {
    return title;
  }

  /**
   * Stores the element's TITLE.
   *
   * @param title Element title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the element's source name (where generated HTML code is going to be stored)
   *
   * @return Source name
   */
  public String getSource() {
    return source;
  }

  /**
   * Stores the element's source name (where generated HTML code is going to be stored)
   *
   * @param source Source name
   */
  public void setSource(String source) {
    this.source = source;
  }

  /**
   * Returns the element's EXPAND operation (if element has to EXPAND children)
   *
   * @return Expand TYPE
   */
  @JsonGetter("expand")
  public String getExpand() {
    return expand;
  }

  /**
   * Stores the element's EXPAND operation (if element has to EXPAND children).
   *
   * @param expand Expand TYPE
   */
  public void setExpand(String expand) {
    this.expand = expand;
  }

  /**
   * Returns HELP text literal VALUE
   *
   * @return HELP literal
   */
  @JsonGetter("help")
  public String getHelp() {
    return help;
  }

  /**
   * Stores Help literal
   *
   * @param help HELP literal to set
   */
  public void setHelp(String help) {
    this.help = help;
  }

  /**
   * Returns HELP image literal
   *
   * @return returns HELP image literal
   */
  @JsonGetter("helpImage")
  public String getHelpImage() {
    return helpImage;
  }

  /**
   * Stores HELP_IMAGE literal
   *
   * @param helpImage Set HELP_IMAGE attribute
   */
  public void setHelpImage(String helpImage) {
    this.helpImage = helpImage;
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
    if (this.getElementList() != null) {
      for (Element element : (List<Element>) this.getElementList()) {
        // Generate the children
        children.add(element.generateTemplate(group));
      }
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
    if (this.getElementList() != null) {
      for (Element element : this.getElementList()) {
        // Generate the children
        children.add(element.generateHelpTemplate(group, currentLabel, developers));
      }
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
   * @param <T>
   * @param elementClassList Element class
   * @return Children List
   */
  public <T> List<T> getElementsByType(Class<T>... elementClassList) {
    return getElementsByType(true, elementClassList);
  }

  /**
   * Returns the children element list of a desired TYPE
   *
   * @param <T>
   * @param elementClassList element class
   * @param processDialog    flag to check dialog elements
   * @return Children List
   */
  public <T> List<T> getElementsByType(Boolean processDialog, Class<T>... elementClassList) {

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
   * @param <T>
   * @param elementClassList element class
   * @return Children List
   */
  public <T> List<T> getChildrenByType(Class<T>... elementClassList) {

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
    if (getElementList() != null) {
      for (Element element : getElementList()) {
        element.getReportStructure(printElementList, label, parameters, dataSuffix);
      }
    }
    return printElementList;
  }

  /**
   * Retrieve logger
   * @return Logger
   */
  protected Logger getLogger() {
    return LogManager.getLogger(this.getClass());
  }
}
