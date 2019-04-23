package com.almis.awe.developer.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities related to locale files
 */
public final class LocaleUtil {

  private static final Logger logger = LogManager.getLogger(LocaleUtil.class);

  // Environment
  private static Environment environment;

  // Static variables
  private static String extensionXML;
  private static String localeFileName;

  /**
   * Hide the constructor
   */
  private LocaleUtil() {}

  /**
   * Initialize Utility class
   * @param springEnvironment Spring environment
   */
  public static void init(Environment springEnvironment)  {
    environment = springEnvironment;
    extensionXML = environment.getProperty("extensions.xml", ".xml");
    localeFileName = environment.getProperty("application.files.locale", "Locale-");
  }

  /**
   * Given a path, obtains the list of languages whose locale file is defined
   * 
   * @param path Path
   * @return Language list
   */
  public static List<String> getLanguageList(String path) {
    ArrayList<String> languages = new ArrayList<>();

    try {
      File folder = new File(path);
      if (folder.exists() && folder.isDirectory()) {

        String patternString = localeFileName + "([a-zA-Z]+)" + extensionXML;
        final Pattern pattern = Pattern.compile(patternString);
        String[] files = folder.list((File dir, String name) -> pattern.matcher(name).matches());
        for (String file : files) {
          Matcher matcher = pattern.matcher(file);
          if (matcher.find()) {
            languages.add(matcher.group(1));
          }
        }
      }

    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }

    return languages;
  }

  /**
   * Print a XML header into a fileoutputstream
   *
   * @param out Output Stream
   * @param doc Document name
   * @param usr User name
   * @param des Document description
   * @param upd
   * @param AddHdg
   * @throws Exception
   */
  public static void printHeader(Writer out, String doc, String usr, String des, Boolean upd, Boolean AddHdg) throws Exception {
    String strOut = "";
    strOut += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
    if (AddHdg) {
      strOut += "<!--\n";
      strOut += "  Document   : " + doc + "\n";
      strOut += "  Description: " + des + "\n";
      strOut += "-->\n\n";
    }
    // Add SVN id property to be expanded when comitting a file
    strOut += "<!--" + (char) 36 + "Id" + (char) 36 + "-->\n\n";

    out.write(strOut);
  }
}
