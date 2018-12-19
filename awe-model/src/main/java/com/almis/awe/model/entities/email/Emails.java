package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Emails Class
 *
 * Used to parse the files Email.xml with XStream
 * Stores the email list
 *
 * @author Pablo GARCIA - 28/JUL/2011
 */
@XStreamAlias("emails")
public class Emails extends XMLWrapper {

  private static final long serialVersionUID = 7127095378963356899L;
  // Query list
  @XStreamImplicit
  private List<Email> emailList;

  /**
   * Default constructor
   */
  public Emails() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Emails(Emails other) throws AWException {
    super(other);
    this.emailList = ListUtil.copyList(other.emailList);
  }

  /**
   * Returns an email
   *
   * @param ide Email identifier
   * @return Selected email
   */
  public Email getEmail(String ide) {
    for (Email email: getBaseElementList()) {
      if (ide.equals(email.getId())) {
        return email;
      }
    }
    return null;
  }

  /**
   * Returns the email list
   *
   * @return Email list
   */
  public List<Email> getEmailList() {
    return emailList;
  }

  /**
   * Stores the email list
   *
   * @param emailList Email list
   */
  public void setEmailList(List<Email> emailList) {
    this.emailList = emailList;
  }

  @Override
  public List<Email> getBaseElementList() {
    return emailList == null ? Collections.emptyList() : emailList;
  }
}
