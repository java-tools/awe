package com.almis.awe.model.entities.maintain;

import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Multiple Class
 * Used to parse the file Maintain.xml with XStream
 * Target for multiple update records. Generates a query which allows add new records into the table
 *
 * @author Pablo GARCIA - 15/MAR/2012
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("retrieve-data")
public class RetrieveData extends MaintainQuery {

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.RETRIEVE_DATA;
  }
}
