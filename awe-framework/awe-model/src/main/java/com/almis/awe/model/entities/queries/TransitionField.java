package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.entities.Copyable;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

/**
 * Transition field Class
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Table Fields from queries and maintain
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
public class TransitionField implements Copyable {

  // Query to be used as field
  @XStreamImplicit
  private List<SqlField> fields;

  @Override
  public TransitionField copy() throws AWException {
    return this.toBuilder()
      .fields(ListUtil.copyList(getFields()))
      .build();
  }

  /**
   * Retrieve field
   *
   * @return Field
   */
  public SqlField getField() {
    return fields == null ? null : fields.get(0);
  }

  /**
   * Set field
   *
   * @return this
   */
  public TransitionField setField(SqlField field) {
    fields = Collections.singletonList(field);
    return this;
  }

  @Override
  public String toString() {
    return getField().toString();
  }
}
