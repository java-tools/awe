package com.almis.awe.developer.service;

import com.almis.awe.builder.client.SelectActionBuilder;
import com.almis.awe.builder.client.grid.UpdateCellActionBuilder;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.developer.comparator.CompareLocal;
import com.almis.awe.developer.type.FormatType;
import com.almis.awe.developer.util.LocaleUtil;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.component.AweElements;
import com.almis.awe.model.component.XStreamSerializer;
import com.almis.awe.model.dto.CellData;
import com.almis.awe.model.dto.DataList;
import com.almis.awe.model.dto.ServiceData;
import com.almis.awe.model.entities.Global;
import com.almis.awe.model.entities.XMLFile;
import com.almis.awe.model.entities.actions.ComponentAddress;
import com.almis.awe.model.entities.locale.Locales;
import com.almis.awe.model.type.AnswerType;
import com.almis.awe.model.util.data.DataListUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.logging.log4j.Level;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author agomez
 */
public class LiteralsService extends ServiceConfig {

  private static final String FILE_DESCRIPTION = "Application Locales";

  @Value("${extensions.xml:.xml}")
  private String xmlExtension;
  // Locale file names without language code
  @Value("${application.files.locale:Locale-}")
  private String localeFile;

  @Value("${translation.api.key}")
  private String translationApiKey;

  @Value("${translation.api.url}")
  private String translationApiUrl;

  @Value("${translation.api.parameters.key}")
  private String keyParameter;

  @Value("${translation.api.parameters.language}")
  private String languageParameter;

  @Value("${translation.api.parameters.text}")
  private String textParameter;

  // Autowired services
  private final PathService pathService;
  private final XStreamSerializer serializer;

  /**
   * Autowired constructor
   *
   * @param pathService Path service
   * @param serializer  Serializer
   */
  public LiteralsService(PathService pathService, XStreamSerializer serializer) {
    this.pathService = pathService;
    this.serializer = serializer;
  }

  /**
   * Translate from one language to other
   *
   * @param text         Text to translate
   * @param fromLanguage Source language
   * @param toLanguage   Target language
   * @return Translation
   * @throws AWException Error translating text
   */
  public ServiceData translate(String text, String fromLanguage, String toLanguage) throws AWException {
    ServiceData serDat = new ServiceData();
    String result;

    if (Objects.equals(fromLanguage, toLanguage)) {
      // Skip translation
      result = text;
    } else {
      // Call translation API
      result = getTranslation(text, fromLanguage, toLanguage);
    }

    String[] arr = {result};
    serDat.setData(arr);

    return serDat;
  }

  /**
   * Returns the locale match list for a string given the language
   *
   * @param literal  Code
   * @param codeLang Language
   * @return Service
   * @throws AWException Error retrieving locale matches
   */
  public ServiceData getLocaleMatches(String literal, String codeLang) throws AWException {
    ServiceData serviceData = new ServiceData();
    DataList data = findStringInFile(codeLang.toUpperCase(), literal);
    serviceData.setDataList(data);
    return serviceData;
  }

  /**
   * Returns existing translations corresponding to a code
   *
   * @param code Code
   * @return Translation list
   * @throws AWException Error retrieving translation list
   */
  public ServiceData getTranslationList(String code) throws AWException {
    ServiceData serviceData = new ServiceData();

    // Get existing Locales
    Map<String, Map<String, String>> localeList = getElements().getLocales();

    // List of loaded languages
    DataList translations = new DataList();

    // Iterate by language
    for (String codeLang : localeList.keySet()) {
      // Get language
      DataList languageData = retrieveLocaleFromFile(code, codeLang.toUpperCase());
      translations.getRows().addAll(languageData.getRows());
    }

    serviceData.setDataList(translations);
    return serviceData;
  }

  /**
   * Save translation
   *
   * @param formatSelector Format selector
   * @param text           Text
   * @param markdown       Markdown
   * @param codeLang       Language
   * @param searchLang     Language (search)
   * @param code           Code
   * @return Translation stored
   * @throws AWException Error storing translation
   */
  public ServiceData saveTranslation(String formatSelector, String text, String markdown, String codeLang, String searchLang, String code) throws AWException {
    // Access to the memory
    ServiceData serviceData = new ServiceData();

    try {
      storeUpdatedLocale(codeLang.toUpperCase(), code, text, markdown, formatSelector);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_STORING_TRANSLATION"), getLocale("ERROR_MESSAGE_STORING_TRANSLATION", code, text), exc);
    }

    String value;
    if (FormatType.TEXT.toString().equalsIgnoreCase(formatSelector)) {
      value = text;
    } else {
      value = markdown;
    }

    serviceData.setTitle(getLocale("OK_TITLE_LOCAL_UPDATED"));
    serviceData.setMessage(getLocale("OK_MESSAGE_LOCAL_UPDATED", code));

    // Build address of cell
    ComponentAddress address = new ComponentAddress("report", "GrdTraLit", codeLang, "lite");

    // Add action to list
    serviceData.addClientAction(new UpdateCellActionBuilder(address, new CellData(value)).build());

    // If saved translation's language is the same as from searched, update locale list grid
    if (codeLang.equalsIgnoreCase(searchLang)) {
      // Build address of cell
      address = new ComponentAddress("report", "GrdStrLit", code, "lit");

      // Add action to list
      serviceData.addClientAction(new UpdateCellActionBuilder(address, new CellData(value)).build());
    }

    return serviceData;
  }

  /**
   * Save new literal
   *
   * @param codeLang Language
   * @param code     Code
   * @param literal  Text
   * @return New literal created
   * @throws AWException Error creating new locale
   */
  public ServiceData newLiteral(String codeLang, String code, String literal) throws AWException {
    ServiceData serviceData = new ServiceData();

    List<String> languages = LocaleUtil.getLanguageList(pathService.getPath());

    // Iterate by language
    for (String actualLangCode : languages) {
      // Get locals of one language
      Locales localesFromFile = readLocalesFromFile(actualLangCode);

      // Check if local already exists
      if (localesFromFile != null) {
        if (localesFromFile.getLocale(code) == null) {
          String newLiteral = getTranslation(literal, codeLang.toUpperCase(), actualLangCode);

          // Create new local in XML file
          storeNewLocale(actualLangCode, code, newLiteral);

          // Set service data
          serviceData.setTitle(getLocale("OK_TITLE_NEW_LOCAL"));
          serviceData.setMessage(getLocale("OK_MESSAGE_NEW_LOCAL", code));
        } else {
          // Send warning
          serviceData.setTitle(getLocale("WARNING_TITLE_NEW_LOCAL"));
          serviceData.setMessage(getLocale("WARNING_MESSAGE_LOCAL_ALREADY_EXISTS", code));
          serviceData.setType(AnswerType.WARNING);
        }
      }
    }

    return serviceData;

  }

  /**
   * Delete literal
   *
   * @param code Code
   * @return Deletion message
   * @throws AWException Error deleting locale
   */
  public ServiceData deleteLiteral(String code) throws AWException {
    ServiceData serviceData = new ServiceData();

    // Get existing locals
    Map<String, Map<String, String>> localeList = getElements().getLocales();

    // * Iterate by language
    for (String codeLang : localeList.keySet()) {
      // Get language
      storeDeletedLocale(codeLang, code);
    }

    serviceData.setTitle(getLocale("OK_TITLE_REMOVED_LOCAL"));
    serviceData.setMessage(getLocale("OK_MESSAGE_REMOVED_LOCAL", code));
    return serviceData;

  }

  /**
   * Returns using language
   *
   * @return Used language
   */
  public ServiceData getUsingLanguage() {
    getLogger().log(LiteralsService.class, Level.INFO, "getUsingLanguage");
    ServiceData serviceData = new ServiceData();

    String codeLang = getElements().getProperty("var.glb.lan");
    String[] labval = {"ENUM_LAN_" + codeLang, codeLang.toLowerCase()};
    serviceData.setData(labval);

    return serviceData;
  }

  /**
   * Get markdown text of literal from file
   *
   * @param codeLang Language
   * @param code     code
   * @return markdown text
   * @throws AWException Error retrieving markdown
   */
  public ServiceData getSelectedLocale(String codeLang, String code) throws AWException {
    List<String> values = new ArrayList<>();
    List<String> types = new ArrayList<>();
    FormatType format = FormatType.TEXT;
    ServiceData serviceData = new ServiceData();

    // Read Locale File List for a LANGUAGE
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();
      for (Global global : globals) {
        if (global.getName().equalsIgnoreCase(code)) {
          if (global.getMarkdown() != null && !"".equals(global.getMarkdown())) {
            values.add(global.getMarkdown());
            format = FormatType.MARKDOWN;
          } else {
            values.add(global.getValue());
            format = FormatType.TEXT;
          }
        }
      }
      // Store format
      types.add(format.toString());

      // Add actions to list
      serviceData.addClientAction(new SelectActionBuilder("litTxt", values).setAsync(true).build());
      serviceData.addClientAction(new SelectActionBuilder("litMrk", values).setAsync(true).build());
      serviceData.addClientAction(new SelectActionBuilder("FormatSelector", types).setAsync(true).build());
      serviceData.addClientAction(new SelectActionBuilder("FlgStoLit", types).setAsync(true).build());
    }

    return serviceData;
  }

  /**
   * Switch the languages
   *
   * @param fromLanguage Source language
   * @param toLanguage   Target language
   * @param fromTarget   Source target
   * @param toTarget     Target target
   * @return Languages changed
   */
  public ServiceData switchLanguages(String fromLanguage, String toLanguage, String fromTarget, String toTarget) {
    return new ServiceData()
      .addClientAction(new SelectActionBuilder(fromTarget, Collections.singletonList(toLanguage)).build())
      .addClientAction(new SelectActionBuilder(toTarget, Collections.singletonList(fromLanguage)).build());
  }

  /**
   * Extract translation from API result
   *
   * @param literal  Locale
   * @param fromLang Source language
   * @param toLang   Target language
   * @return Locale translated
   * @throws AWException Error translating locale
   */
  private String getTranslation(String literal, String fromLang, String toLang) throws AWException {

    String translation;

    try {

      // Get translation from cloud
      String result = getUrlString(literal, fromLang, toLang);

      // Extract translation of the result
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonResponse = mapper.readTree(result);
      translation = jsonResponse.path("responseData").path("translatedText").asText();

      if ("".equalsIgnoreCase(translation)) {
        throw new AWException(getLocale("ERROR_TITLE_RETRIEVING_TRANSLATION"),
          getLocale("ERROR_MESSAGE_RETRIEVING_TRANSLATION", toLang, literal));
      }
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_RETRIEVING_TRANSLATION"),
        getLocale("ERROR_MESSAGE_RETRIEVING_TRANSLATION", toLang, literal), exc);
    }

    return translation;

  }

  /**
   * Search literal in file
   *
   * @param codeLang Language
   * @param search   Search string
   * @return matches
   * @throws AWException Error finding string in file
   */
  private DataList findStringInFile(String codeLang, String search) throws AWException {
    DataList dataList = new DataList();

    // Add row to dataList
    List<String> keys = new ArrayList<>();
    List<String> values = new ArrayList<>();

    // Read Locale File List for a LANGUAGE
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();
      for (Global locale : globals) {
        String key = locale.getName();
        String text = locale.getValue() != null ? locale.getValue() : locale.getMarkdown();

        if (matchesSearch(search, key, text)) {
          // Get value
          keys.add(key);
          values.add(text);
        }
      }

      DataListUtil.addColumn(dataList, "key", keys);
      DataListUtil.addColumn(dataList, "value", values);
      dataList.setRecords(dataList.getRows().size());
    }

    return dataList;
  }

  /**
   * Check if search string matches values
   *
   * @param search Search string
   * @param values Values
   * @return Search string matches values
   */
  private boolean matchesSearch(String search, String... values) {
    if (search == null) {
      return true;
    } else {
      for (String value : values) {
        if (value.toLowerCase().contains(search.toLowerCase())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns locale by the locale key and language code
   *
   * @param code     Code
   * @param codeLang Language
   * @return result Array of translations
   * @throws AWException Error retrieving locale
   */
  private DataList retrieveLocaleFromFile(String code, String codeLang) throws AWException {

    DataList dataList = new DataList();
    List<String> values = new ArrayList<>();
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();
      for (Global locale : globals) {
        String actualKey = locale.getName();
        if (code.equalsIgnoreCase(actualKey)) {
          // Get matches literals
          values.add(getElements().parseLocale(locale));
        }
      }
      DataListUtil.addColumn(dataList, "value", values);
      DataListUtil.addColumn(dataList, "key", codeLang);
      DataListUtil.addColumn(dataList, "code", code);
      dataList.setRecords(dataList.getRows().size());
    }

    return dataList;
  }

  /**
   * Save modifications in file
   *
   * @param codeLang       Language
   * @param code           Code
   * @param text           Text
   * @param markdown       Markdown
   * @param formatSelector Format
   * @throws AWException Error stroring locale
   */
  private void storeUpdatedLocale(String codeLang, String code, String text, String markdown, String formatSelector) throws AWException {

    // Read locale File List for a LANGUAGE
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();
      for (Global global : globals) {
        if (code.equalsIgnoreCase(global.getName())) {
          if (FormatType.TEXT.toString().equalsIgnoreCase(formatSelector)) {
            global.setValue(text);
            global.setMarkdown(null);
          } else {
            global.setMarkdown(markdown);
            global.setValue(null);
          }
        }
      }

      // Fix markdown attribute
      fixMarkdown(globals);
    }

    // Store updated locale file
    storeLocaleListFile(codeLang, localesFromFile);
  }

  /**
   * Save new local in XML file
   *
   * @param codeLang Language
   * @param code     Code
   * @param text     Text
   * @throws AWException Error storing locale
   */
  private void storeNewLocale(String codeLang, String code, String text) throws AWException {

    Global locale = new Global();
    locale.setName(code);
    locale.setValue(text);
    locale.setMarkdown(null);

    // Read Local File List for a LANGUAGE
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();

      // If no locals add the global list
      if (globals == null) {
        globals = new ArrayList<>();
      }

      // Add new global
      globals.add(locale);

      // Fix markdown attribute
      fixMarkdown(globals);

      globals.sort(new CompareLocal());
      localesFromFile.setLocales(globals);
    }

    // Store updated local file
    storeLocaleListFile(codeLang, localesFromFile);
  }

  /**
   * Delete literal from file
   *
   * @param codeLang Language
   * @param code     Code
   * @throws AWException Error deleting locale
   */
  private void storeDeletedLocale(String codeLang, String code) throws AWException {

    // Read locale File List for a LANGUAGE
    Locales localesFromFile = readLocalesFromFile(codeLang);
    if (localesFromFile != null) {
      List<Global> globals = localesFromFile.getLocales();

      for (Global global : globals) {
        if (global.getName().equalsIgnoreCase(code)) {
          globals.remove(global);
          break;
        }
      }

      // Fix markdown attribute
      fixMarkdown(globals);
    }

    // Store updated local file
    storeLocaleListFile(codeLang, localesFromFile);
  }

  /**
   * Fix markdown attribute
   *
   * @param globals Globals
   */
  private void fixMarkdown(List<Global> globals) {
    // Fix markdown attribute
    for (Global global : globals) {
      if (global.getMarkdown() != null && global.getMarkdown().isEmpty()) {
        // Remove </local> if not needed
        global.setMarkdown(null);
      }
    }
  }

  /**
   * Read local list from file
   *
   * @param codeLang Language code (ES, EN, FR...)
   * @return List of locales loaded
   */
  private Locales readLocalesFromFile(String codeLang) throws AWException {

    String fileName = localeFile + codeLang;
    String path = pathService.getPath() + fileName + xmlExtension;

    return (Locales) readXmlFile(path);
  }

  /**
   * Read all XML files and return them
   *
   * @param path File path
   * @return Xml file object
   */
  private XMLFile readXmlFile(String path) {
    XMLFile xml = null;
    try {
      // Unmarshall XML
      File file = new File(path);
      if (file.exists()) {
        InputStream resourceInputStream = new FileInputStream(file);
        xml = serializer.getObjectFromXml((Class<? extends XMLFile>) Locales.class, resourceInputStream);
        getLogger().log(AweElements.class, Level.DEBUG, "Reading ''{0}'' - OK", path);
      } else {
        getLogger().log(AweElements.class, Level.DEBUG, "Reading ''{0}'' - NOT FOUND", path);
      }
    } catch (IOException exc) {
      getLogger().log(AweElements.class, Level.ERROR, "Error parsing XML - ''{0}''", path, exc);
    }
    return xml;
  }

  /**
   * Store local file
   *
   * @param codeLang Language
   */
  private void storeLocaleListFile(String codeLang, Locales locales) throws AWException {

    String fileName = localeFile + codeLang;
    XStream xstream;
    // Define XML path
    String xmlPth = pathService.getPath() + fileName + xmlExtension;

    try (FileOutputStream fileOutputStream = new FileOutputStream(xmlPth)) {
      // Retrieve xstream serializer
      xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
          return new PrettyPrintWriter(out) {
            @Override
            protected void writeText(QuickWriter writer, String text) {
              if (!text.trim().isEmpty()) {
                writer.write("<![CDATA[");
                writer.write(text);
                writer.write("]]>");
              }
            }
          };
        }
      });

      // Process locales annotations
      xstream.processAnnotations(Locales.class);

      // Generate xml file
      BufferedWriter xmlOut = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
      LocaleUtil.printHeader(xmlOut, fileName, FILE_DESCRIPTION, true);
      xstream.toXML(locales, xmlOut);
    } catch (Exception exc) {
      throw new AWException(getLocale("ERROR_TITLE_STORE_FILE"),
        getLocale("ERROR_MESSAGE_STORE_FILE", fileName), exc);
    }
  }

  /**
   * Returns the URL to make a call for translation to the API
   *
   * @param literal  Locale
   * @param fromLang Source language
   * @param toLang   Target language
   * @return Url string
   * @throws IOException Error calling api
   */
  private String getUrlString(String literal, String fromLang, String toLang) throws IOException {

    /* GLOSBE */
    String encodedText = URLEncoder.encode(literal, StandardCharsets.UTF_8.toString());
    String url = translationApiUrl +
      "?" + keyParameter + "=" + translationApiKey +
      "&" + languageParameter + "=" + fromLang.toLowerCase() + "|" + toLang.toLowerCase() +
      "&" + textParameter + "=" + encodedText;

    URLConnection connection = new URL(url).openConnection();

    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
    StringBuilder stringBuilder = new StringBuilder();
    String current;
    while ((current = in.readLine()) != null) {
      stringBuilder.append(current);
    }

    return stringBuilder.toString();
  }
}
