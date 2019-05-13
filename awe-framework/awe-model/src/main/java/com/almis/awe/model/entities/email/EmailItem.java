package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * EmailItem Class
 *
 * Used to parse the Email.xml file with XStream
 * This class is used to parse an email item (from, to, cc, cco, attachment)
 *
 * @author Pablo GARCIA - 28/JUN/2011
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class EmailItem extends EmailMessage implements Copyable {

  private static final long serialVersionUID = 4004314012149656221L;

  // Is a list of data or a single data
  @XStreamAlias("list")
  @XStreamAsAttribute
  private Boolean list;

  @Override
  public EmailItem copy() throws AWException {
    return this.toBuilder().build();
  }
}
