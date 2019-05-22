package com.almis.awe.model.entities.locale;

import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLFile;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Locals class
 *
 * Used to parse the files in 'local' folder with XStream
 * Generates a local list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Accessors(chain = true)
@XStreamAlias("locales")
public class Locales implements XMLFile {

  private static final long serialVersionUID = 2560444152446535714L;
  /* Local list */
  @XStreamImplicit(itemFieldName = "locale")
  private List<Global> locales;

  @XStreamAlias("xmlns:xsi")
  @XStreamAsAttribute
  protected String xmlns;

  @XStreamAlias("xsi:noNamespaceSchemaLocation")
  @XStreamAsAttribute
  private String xsd;

  /**
   * Returns a translated locale
   *
   * @param localeIdentifier Locale identifier
   * @return Selected locale translated
   */
  public String getLocale(String localeIdentifier) {
    for (Global locale : this.getBaseElementList()) {
      if (localeIdentifier.equals(locale.getName())) {
        return locale.getValue();
      }
    }
    return null;
  }

  @Override
  public List<Global> getBaseElementList() {
    return locales == null ? new ArrayList<>() : locales;
  }
}
