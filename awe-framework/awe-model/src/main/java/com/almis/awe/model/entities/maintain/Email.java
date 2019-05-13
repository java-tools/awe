package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Email Class
 *
 * Used to parse the file Maintain.xml with XStream
 * Target for launching an email send
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("send-email")
public class Email extends MaintainQuery {

  private static final long serialVersionUID = 2606138177846420718L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.EMAIL;

  @Override
  public Email copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
