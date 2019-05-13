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
 * Update Class
 *
 * Used to parse the file Maintain.xml with XStream
 * Target for update records. Generates a query which allows to update records from the table
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("update")
public class Update extends MaintainQuery {

  private static final long serialVersionUID = -6440038028978617354L;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.UPDATE;

  @Override
  public Update copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
