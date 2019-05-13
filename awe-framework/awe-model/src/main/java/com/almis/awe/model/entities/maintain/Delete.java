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
 * Delete Class
 *
 * Used to parse the file Maintain.xml with XStream
 *
 *
 * Target for delete records. Generates a query which allows to filter a group of records and delete them
 *
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("delete")
public class Delete extends MaintainQuery {

  private static final long serialVersionUID = 3169621156759677359L;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.DELETE;

  @Override
  public Delete copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
