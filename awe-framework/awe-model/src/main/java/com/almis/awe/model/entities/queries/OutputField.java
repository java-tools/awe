package com.almis.awe.model.entities.queries;

import com.almis.awe.model.entities.Copyable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * OutputField Class
 *
 * Used to parse the files Queries.xml and Maintain.xml with XStream
 * Superclass of Field and Computed class
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamInclude({Field.class, Computed.class, Compound.class, Concat.class})
public abstract class OutputField implements Copyable {

  private static final long serialVersionUID = -5029397784962648558L;

  // Field alias
  @XStreamAlias("alias")
  @XStreamAsAttribute
  private String alias;

  // Field is not printable
  @XStreamAlias("noprint")
  @XStreamAsAttribute
  private Boolean noprint;

  // Output transformation (after formatting)
  @XStreamAlias("transform")
  @XStreamAsAttribute
  private String transform;

  // Pattern to apply to the field
  @XStreamAlias("pattern")
  @XStreamAsAttribute
  private String pattern;

  // Translate to apply to the field
  @XStreamAlias("translate")
  @XStreamAsAttribute
  private String translate;

  // Format for transform generic dates
  @XStreamAlias("format-from")
  @XStreamAsAttribute
  private String formatFrom;

  @XStreamAlias("format-to")
  @XStreamAsAttribute
  private String formatTo;

  /**
   * Returns if is noprint
   * @return Is noprint
   */
  public boolean isNoprint() {
    return noprint != null && noprint;
  }

  /**
   * Check if transform has been defined
   * @return Transform has been defined
   */
  public boolean isTransform() {
    return transform != null && !transform.isEmpty();
  }

  /**
   * Check if translate has been defined
   * @return Translate has been defined
   */
  public boolean isTranslate() {
    return translate != null && !translate.isEmpty();
  }
}
