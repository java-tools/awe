package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.entities.queries.Query;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * MaintainQuery Class
 * Used to parse the file Maintain.xml with XStream
 * Parent class for Insert, Update and Delete and Service operations. Contains default attributes and methods
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@XStreamInclude({Serve.class, Insert.class, Update.class, Delete.class, Multiple.class, Commit.class, Email.class, Queue.class, IncludeTarget.class})
public abstract class MaintainQuery extends Query implements Copyable {

  private static final long serialVersionUID = 418621393719461416L;

  // Audit table name
  @XStreamAlias("audit")
  @XStreamAsAttribute
  private String auditTable = null;

  // Launch as batch
  @XStreamAlias("batch")
  @XStreamAsAttribute
  private String batch = null;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.NONE;

  // Variable index
  @XStreamOmitField
  private Integer variableIndex = null;

  /**
   * Default constructor
   */
  public MaintainQuery() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public MaintainQuery(MaintainQuery other) throws AWException {
    super(other);
    this.auditTable = other.auditTable;
    this.batch = other.batch;
    this.variableIndex = other.variableIndex;
  }

  /**
   * Returns the audit table name
   *
   * @return Audit table name
   */
  public String getAuditTable() {
    return auditTable;
  }

  /**
   * Stores the audit table name
   *
   * @param auditTable the auditTable to set
   */
  public void setAuditTable(String auditTable) {
    this.auditTable = auditTable;
  }

  /**
   * @return the batch
   */
  public String getBatch() {
    return batch;
  }

  /**
   * @return the batch
   */
  public boolean isBatch() {
    return batch != null && Boolean.parseBoolean(batch);
  }

  /**
   * @param batch the batch to set
   */
  public void setBatch(String batch) {
    this.batch = batch;
  }

  /**
   * Returns the maintain type
   *
   * @return Maintain type
   */
  public MaintainType getMaintainType() {
    return maintainType;
  }

  /**
   * Retrieve variable index
   *
   * @return Variable index
   */
  public Integer getVariableIndex() {
    return variableIndex;
  }

  /**
   * Store variable index
   *
   * @param variableIndex Variable index
   * @return this
   */
  public MaintainQuery setVariableIndex(Integer variableIndex) {
    this.variableIndex = variableIndex;
    return this;
  }
}
