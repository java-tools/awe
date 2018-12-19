/*
 * Package definition
 */
package com.almis.awe.model.entities.email;

/*
 * File Imports
 */

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * EmailItem Class
 *
 * Used to parse the Email.xml file with XStream
 * This class is used to parse an email item (from, to, cc, cco, attachment)
 *
 * @author Pablo GARCIA - 28/JUN/2011
 */
public class EmailItem extends EmailMessage implements Copyable {

  private static final long serialVersionUID = 4004314012149656221L;

  // Is a list of data or a single data
  @XStreamAlias("list")
  @XStreamAsAttribute
  private String list;

  /**
   * Copy constructor
   *
   * @param other
   */
  public EmailItem(EmailItem other) throws AWException {
    super(other);
    this.list = other.list;
  }

  /**
   * Returns the item list
   *
   * @return Item list
   */
  public boolean isList() {
    return Boolean.parseBoolean(list);
  }

  /**
   * Stores the item list
   *
   * @param list Item list
   */
  public void setList(String list) {
    this.list = list;
  }

  @Override
  public EmailItem copy() throws AWException {
    return new EmailItem(this);
  }
}
