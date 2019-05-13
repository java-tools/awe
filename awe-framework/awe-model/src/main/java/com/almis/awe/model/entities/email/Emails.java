package com.almis.awe.model.entities.email;

import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Emails Class
 *
 * Used to parse the files Email.xml with XStream
 * Stores the email list
 *
 * @author Pablo GARCIA - 28/JUL/2011
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("emails")
public class Emails implements XMLFile {

  private static final long serialVersionUID = 7127095378963356899L;
  // Query list
  @XStreamImplicit
  private List<Email> emailList;

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

  @Override
  public List<Email> getBaseElementList() {
    return emailList == null ? new ArrayList<>() : emailList;
  }
}
