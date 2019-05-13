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
 * Commit Class
 * Used to parse the file Maintain.xml with XStream
 * Target for launching a commit
 *
 * @author Pablo GARCIA - 09/NOV/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("commit")
public class Commit extends MaintainQuery {

  private static final long serialVersionUID = 1396295624485675536L;
  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.COMMIT;

  @Override
  public Commit copy() throws AWException {
    return this.toBuilder().build();
  }

  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
