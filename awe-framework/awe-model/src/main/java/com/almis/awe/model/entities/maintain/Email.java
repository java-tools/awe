package com.almis.awe.model.entities.maintain;

import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Email Class
 * <p>
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

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.EMAIL;
  }
}
