/*
 * Package definition
 */
package com.almis.awe.model.entities.locale;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLWrapper;
import com.almis.awe.model.util.data.ListUtil;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Collections;
import java.util.List;

/*
 * File Imports
 */

/**
 * Locals class
 *
 * Used to parse the files in 'local' folder with XStream
 * Generates a local list
 *
 * @author Pablo GARCIA - 28/JUN/2010
 */
@XStreamAlias("locales")
public class Locales extends XMLWrapper {

  private static final long serialVersionUID = 2560444152446535714L;
  /* Local list */
  @XStreamImplicit(itemFieldName = "locale")
  private List<Global> locales;

  @XStreamAlias("xmlns:xsi")
  @XStreamAsAttribute
  protected String xmlns = AweConstants.XMLNS;

  @XStreamAlias("xsi:noNamespaceSchemaLocation")
  @XStreamAsAttribute
  private String xsd = AweConstants.XSD_LOCALES;

  /**
   * Default constructor
   */
  public Locales() {
  }

  /**
   * Copy constructor
   *
   * @param other
   */
  public Locales(Locales other) throws AWException {
    super(other);
    this.locales = ListUtil.copyList(other.locales);
    this.xmlns = other.xmlns;
    this.xsd = other.xsd;
  }

  /**
   * Returns the locale list
   *
   * @return Locale list
   */
  public List<Global> getLocales() {
    return locales;
  }

  /**
   * Stores the locale list
   *
   * @param locales Locale list
   */
  public void setLocales(List<Global> locales) {
    this.locales = locales;
  }

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
    return locales == null ? Collections.emptyList() : locales;
  }
}
