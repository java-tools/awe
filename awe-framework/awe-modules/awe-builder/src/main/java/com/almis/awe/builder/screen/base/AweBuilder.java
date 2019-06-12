package com.almis.awe.builder.screen.base;

import com.almis.awe.builder.interfaces.IBuilderInitializer;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Element;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dfuentes
 */
@Getter
@Setter
public abstract class AweBuilder<T, I extends Element> implements IBuilderInitializer<T, I> {

  private T parent;
  private String id;
  private List<AweBuilder> elementList = new ArrayList<>();

  /**
   * Constructor
   */
  public AweBuilder() {
    this.parent = setParent();
  }

  @Override
  public T setParent() {
    return (T) this;
  }

  @Override
  public I build(I element) {
    element
      .setId(getId());

    for (AweBuilder aweBuilder : getElementList()) {
      addElement(element, aweBuilder.build());
    }

    return element;
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
   * Add an element to the list
   *
   * @param parent Parent element
   * @param child  Element to add
   */
  protected void addElement(Element parent, Element child) {
    parent.addElement(child);
  }

  /**
   * Add a list of elements
   *
   * @param elementList Element list
   */
  protected void addAllElements(AweBuilder... elementList) {
    setElementList(addAllElements(getElementList(), elementList));
  }

  /**
   * Add a list of elements
   *
   * @param elementList Element list
   */
  protected List addAllElements(List list, AweBuilder... elementList) {
    if (list == null) {
      list = new ArrayList();
    }

    if (elementList != null) {
      list.addAll(Arrays.asList(elementList));
    }
    return list;
  }
}
