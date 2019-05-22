package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Serve Class
 * Used to parse the file Maintain.xml with XStream
 * Target for calling service
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("queue")
public class Queue extends MaintainQuery {

  private static final long serialVersionUID = -8215821861086129388L;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.QUEUE;

  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  @Override
  public Queue copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
