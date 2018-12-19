package com.almis.awe.builder.screen;


import com.almis.awe.builder.enumerates.Expandible;
import com.almis.awe.builder.interfaces.IBuilderInitializer;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author dfuentes
 */
public abstract class AweBuilder<T> implements IBuilderInitializer<T> {

  private T parent;
  private String id;
  private Expandible expandible;

  /**
   * Constructor
   *
   * @throws AWException
   */
  public AweBuilder() throws AWException {
    initializeElements();
    this.parent = setParent();
  }

  /**
   * Get id
   *
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * Set id
   *
   * @param id Identifier
   * @return parent
   * @throws AWException
   */
  public T setId(String id) throws AWException {
    if (checkValidId(id)) {
      this.id = id;
    } else {
      throw new AWException("The id '" + id + "' is not valid.");
    }
    return parent;
  }

  /**
   * Check if id is valid
   *
   * @param id Identifier
   * @return Id is valid
   */
  private boolean checkValidId(String id) {
    String patternString = "^[a-zA-Z0-9\\-_]*$";
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(id);
    return matcher.matches();
  }

  /**
   * Build element
   *
   * @param element Element
   * @return Element built
   */
  public abstract Element build(Element element);

  /**
   * Get element list from the given element
   *
   * @param element Element
   * @return Element list
   */
  protected List<Element> getElementList(Element element) {
    return element.getElementList();
  }

  /**
   * Add an element to the list
   *
   * @param parent Parent element
   * @param child Element to add
   */
  protected void addElement(Element parent, Element child) {
    parent.addElement(child);
  }

  /**
   * Retrieve boolean as string
   * @param value Boolean value
   * @return Boolean as string or null
   */
  protected String getValueAsString(Integer value) {
    return value == null ? null : String.valueOf(value);
  }

  /**
   * Retrieve as string
   * @param value Value
   * @return Value as string or null
   */
  protected String getValueAsString(Boolean value) {
    return value == null ? null : String.valueOf(value);
  }

  /**
   * Get expandible
   *
   * @return
   */
  public Expandible getExpandible() {
    return expandible;
  }

  /**
   * Set expandible
   *
   * @param expandible
   * @return
   */
  public T setExpandible(Expandible expandible) {
    this.expandible = expandible;
    return (T) this;
  }
}
