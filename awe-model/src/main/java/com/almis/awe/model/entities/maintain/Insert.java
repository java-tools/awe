package com.almis.awe.model.entities.maintain;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.type.MaintainType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Insert Class
 * Used to parse the file Maintain.xml with XStream
 * Target for insert records. Generates a query which allows add new records into the table
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@XStreamAlias("insert")
public class Insert extends MaintainQuery {

  private static final long serialVersionUID = 3682159721536717236L;

  // Maintain type
  @XStreamOmitField
  private static final MaintainType maintainType = MaintainType.INSERT;

  // Insert from query
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query = null;

  /**
   * Default constructor
   */
  public Insert() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Insert(Insert other) throws AWException {
    super(other);
    this.query = other.query;
  }

  @Override
  public Insert copy() throws AWException {
    return new Insert(this);
  }

  /**
   * @return the query
   */
  public String getQuery() {
    return query;
  }

  /**
   * @param query the query to set
   */
  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Returns the audit table name (cancels audit in case of INSERT INTO SELECT statements)
   *
   * @return Audit table name
   */
  @Override
  public String getAuditTable() {
    String auditTable = null;
    if (this.getQuery() == null) {
      auditTable = super.getAuditTable();
    }
    return auditTable;
  }

  /**
   * Returns the maintain type
   *
   * @return Maintain type
   */
  @Override
  public MaintainType getMaintainType() {
    return maintainType;
  }
}
