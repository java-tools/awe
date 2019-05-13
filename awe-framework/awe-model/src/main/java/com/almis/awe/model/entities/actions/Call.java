package com.almis.awe.model.entities.actions;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Call Class
 * Used to parse the Actions.xml file with XStream
 * This class is used to parse a call
 *
 * @author Pablo GARCIA - 25/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class Call implements Copyable {
  private static final long serialVersionUID = -3441589614416031154L;
  // Service called
  @XStreamAlias("service")
  @XStreamAsAttribute
  private String service;

  @Override
  public Call copy() throws AWException {
    return this.toBuilder().build();
  }
}
