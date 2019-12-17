package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Serve Class
 * <p>
 * Used to parse the file Maintain.xml with XStream
 * <p>
 * <p>
 * Target for calling services
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("include-target")
public class IncludeTarget extends MaintainQuery {

  @XStreamAlias("name")
  @XStreamAsAttribute
  private String name;

  @Override
  public IncludeTarget copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.INCLUDE;
  }
}
