package com.almis.awe.model.entities.maintain;

import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Update Class
 * <p>
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

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.UPDATE;
  }
}
