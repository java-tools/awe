package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * EmailMessage Class
 * <p>
 * Used to parse the Email.xml file with XStream
 * This class is used to parse an email message (subject, body)
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class EmailMessage implements Copyable {

  private static final long serialVersionUID = 4316195653459469148L;

  // Query called
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

  // Local with the text
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Variable value with the text
  @XStreamAlias("value")
  @XStreamAsAttribute
  private String value;

  // Type of message (html/text)
  @XStreamAlias("type")
  @XStreamAsAttribute
  private String type;

  @Override
  public EmailMessage copy() throws AWException {
    return this.toBuilder().build();
  }
}
