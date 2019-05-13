package com.almis.awe.model.entities.queries;

import com.almis.awe.exception.AWException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * Computed Class
 *
 * Used to parse the file Queries.xml with XStream
 * Computed field from queries. Generates a new field using other fields
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("computed")
public class Computed extends OutputField {

  private static final long serialVersionUID = 8404251573226537433L;

  // Field format (p.e. '[alias1] - [alias2]')
  @XStreamAlias("format")
  @XStreamAsAttribute
  private String format;

  // Field format label (p.e. 'FORMAT_LABEL_1 ->[alias1] - [alias2]')
  @XStreamAlias("label")
  @XStreamAsAttribute
  private String label;

  // Field evaluation (Default is false)
  @XStreamAlias("eval")
  @XStreamAsAttribute
  private Boolean eval;

  // Null value treating (Default is blank)
  @XStreamAlias("nullValue")
  @XStreamAsAttribute
  private String nullValue;

  /**
   * Returns if is eval
   * @return Is eval
   */
  public boolean isEval() {
    return eval != null && eval;
  }

  @Override
  public Computed copy() throws AWException {
    return this.toBuilder().build();
  }
}
