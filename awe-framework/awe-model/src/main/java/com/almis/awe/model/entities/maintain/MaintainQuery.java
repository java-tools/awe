package com.almis.awe.model.entities.maintain;

import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * MaintainQuery Class
 * Used to parse the file Maintain.xml with XStream
 * Parent class for Insert, Update and Delete and Service operations. Contains default attributes and methods
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Serve.class, Insert.class, Update.class, Delete.class, RetrieveData.class, Multiple.class, Commit.class, Email.class, Queue.class, IncludeTarget.class})
public abstract class MaintainQuery extends Query {

  private static final long serialVersionUID = 418621393719461416L;

  // Audit table name
  @XStreamAlias("audit")
  @XStreamAsAttribute
  private String auditTable;

  // Launch as batch
  @XStreamAlias("batch")
  @XStreamAsAttribute
  private Boolean batch;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.NONE;

  // Variable index
  @XStreamOmitField
  private Integer variableIndex;

  /**
   * Returns if is batch
   * @return Is batch
   */
  public boolean isBatch() {
    return batch != null && batch;
  }

  /**
   * Returns the maintain type
   *
   * @return Maintain type
   */
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
