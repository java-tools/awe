package com.almis.awe.model.entities.enumerated;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/*
 * File Imports
 */

/**
 * EnumeratedGroup Class
 *
 * Used to parse the file Enumerated.xml with XStream
 * Generates an enumerated group
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("group")
public class EnumeratedGroup extends XMLWrapper implements Copyable {

  private static final long serialVersionUID = 405249052409598721L;

  // Group identifier
  @XStreamAlias("id")
  @XStreamAsAttribute
  private String id;

  // Group option list
  @XStreamImplicit(itemFieldName = "option")
  private List<Global> optionList;

  /**
   * Default constructor
   */
  public EnumeratedGroup() {
    super();
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public EnumeratedGroup(EnumeratedGroup other) throws AWException {
    super(other);
    this.id = other.id;
    this.optionList = ListUtil.copyList(other.optionList);
  }

  /**
   * Returns the group identifier
   *
   * @return Group identifier
   */
  public String getId() {
    return id;
  }

  /**
   * Stores the group identifier
   *
   * @param id Group identifier
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the group option list
   *
   * @return Group option list
   */
  public List<Global> getOptionList() {
    return optionList;
  }

  /**
   * Stores the group option list
   *
   * @param optionList Group option list
   */
  public void setOptionList(List<Global> optionList) {
    this.optionList = optionList;
  }

  /**
   * Returns the label of the selected value (for translate purpose in queries)
   *
   * @param value Value to find
   * @return Label of the value
   */
  public String findLabel(String value) {
    for (Global option : this.getOptionList()) {
      if (option.getValue().equalsIgnoreCase(value)) {
        return option.getLabel();
      }
    }

    return value;
  }

  /**
   * Returns if identifier belongs to the element
   *
   * @param ide
   * @return true if the identifier belongs to the element
   */
  @Override
  public boolean isElement(String ide) {
    return this.getId().equals(ide);
  }

  /**
   * Return the XML Element Key
   *
   * @return the elementKey
   */
  @Override
  public String getElementKey() {
    return this.getId();
  }

  @Override
  public EnumeratedGroup copy() throws AWException {
    return new EnumeratedGroup(this);
  }
}
