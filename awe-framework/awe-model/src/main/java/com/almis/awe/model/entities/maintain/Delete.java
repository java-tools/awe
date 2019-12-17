package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Delete Class
 * <p>
 * Used to parse the file Maintain.xml with XStream
 * <p>
 * <p>
 * Target for delete records. Generates a query which allows to filter a group of records and delete them
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

  @Override
  public Delete copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.DELETE;
  }
}
