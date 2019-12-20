package com.almis.awe.model.entities.email;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * EmailItem Class
 * <p>
 * Used to parse the Email.xml file with XStream
 * This class is used to parse an email item (from, to, cc, cco, attachment)
 *
 * @author Pablo GARCIA - 28/JUN/2011
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
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
