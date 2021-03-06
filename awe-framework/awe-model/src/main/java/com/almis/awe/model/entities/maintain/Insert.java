package com.almis.awe.model.entities.maintain;

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
 * Insert Class
 * Used to parse the file Maintain.xml with XStream
 * Target for insert records. Generates a query which allows add new records into the table
 *
 * @author Ismael SERRANO - 28/JUN/2010
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("insert")
public class Insert extends MaintainQuery {

  // Insert from query
  @XStreamAlias("query")
  @XStreamAsAttribute
  private String query;

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

  @Override
  public MaintainType getMaintainType() {
    return MaintainType.INSERT;
  }
}
