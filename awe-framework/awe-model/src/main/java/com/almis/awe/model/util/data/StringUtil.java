package com.almis.awe.model.util.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.commons.text.StringEscapeUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * File Imports
 */

/**
 * StringUtil Class
 *
 * String Utilities for AWE
 *
 * @author Pablo GARCIA - 20/JAN/2011
 */
public final class StringUtil {

  // EXPRESSIONS
  private static final String WILDCARD_START = "\\{\\{\\{";
  private static final String WILDCARD_END = "\\}\\}\\}";
  private static final String MULTI_COMMENT = "/\\*.*?\\*/";
  private static final String NEW_LINE = "/[\n\r\t]*/";
  private static final String BREAK_LINE_START = "<br>";
  private static final String BREAK_LINE = "<br/>";
  private static final String NEW_LINE_DASHED = "\\\\n";
  private static final String PATH_DELIMITER = "/";
  // PATTERNS
  private static final Pattern BREAK_LINE_PATTERN = Pattern.compile(NEW_LINE, Pattern.DOTALL | Pattern.MULTILINE);
  private static final Pattern MULTILINE_COMMENTS = Pattern.compile(MULTI_COMMENT, Pattern.DOTALL | Pattern.MULTILINE);
  private static final Pattern SINGLE_LINE_COMMENTS = Pattern.compile(MULTI_COMMENT, 0);
  private static final Pattern SPACES = Pattern.compile("\\s+", Pattern.DOTALL | Pattern.MULTILINE);

  // Markdown
  private static final MutableDataSet FORMAT_OPTIONS = new MutableDataSet();
  private static final Parser PARSER = Parser.builder(FORMAT_OPTIONS).build();
  private static final HtmlRenderer HTML_RENDERER = HtmlRenderer.builder(FORMAT_OPTIONS).build();

  /**
   * Private constructor of util class
   */
  private StringUtil() {
  }

  /**
   * Fixes an string value for a file name
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixFileName(String value) {

    String fixName = value;

    /* Remove wrong characters */
    fixName = fixName.replace("\\", "_");
    fixName = fixName.replace("/", "_");
    fixName = fixName.replace("'", "_");
    fixName = fixName.replace("\"", "_");
    fixName = fixName.replace(" ", "_");
    fixName = fixName.replace(",", "_");
    fixName = fixName.replace("*", "_");
    fixName = fixName.replace("?", "_");
    fixName = fixName.replace(":", "");
    fixName = fixName.replace(".", "");
    fixName = fixName.replace("-", "");
    fixName = fixName.replace("á", "a");
    fixName = fixName.replace("é", "e");
    fixName = fixName.replace("í", "i");
    fixName = fixName.replace("ó", "o");
    fixName = fixName.replace("ú", "u");
    fixName = fixName.replace("Á", "A");
    fixName = fixName.replace("É", "E");
    fixName = fixName.replace("Í", "I");
    fixName = fixName.replace("Ó", "O");
    fixName = fixName.replace("Ú", "U");
    fixName = fixName.replace("ñ", "n");
    fixName = fixName.replace("Ñ", "N");

    return fixName.trim();
  }

  /**
   * Fixes an string value for a criteria
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixHTMLValue(String value) {
    return StringEscapeUtils.escapeXml10(value).trim();
  }

  /**
   * Fixes an string value for a criteria
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixCriteriaValue(String value) {
    return StringEscapeUtils.escapeEcmaScript(value).trim();
  }

  /**
   * Fixes an string value for a criteria
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixQueryValue(String value) {
    return value.replace("'", "''");
  }

  /**
   * Fixes an string value for a JSON File
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixJSonValue(String value) {
    return StringEscapeUtils.escapeEcmaScript(value).trim();
  }

  /**
   * Fixes an string value for a JSON File
   *
   * @param value String value
   * @return Value fixed
   */
  public static String fixFormatValue(String value) {
    String fixed = value;
    fixed = fixed.replace("\\'", "\"");
    fixed = fixed.replace("\'", "\"");
    return StringUtil.fixJSonValue(fixed);
  }

  /**
   * Removes HTML characters from text
   *
   * @param value String value
   * @return Value fixed
   */
  public static String removeHTMLFromText(String value) {
    // Unescape HTML code
    String removeHtml = StringEscapeUtils.unescapeHtml4(value);

    // Change <br/> to new lines
    removeHtml = removeHtml.replace(BREAK_LINE, "\n");
    removeHtml = removeHtml.replace(BREAK_LINE_START, "\n");

    // Return value
    return removeHtml;
  }

  /**
   * Generates an HTML Format
   *
   * @param value String value
   * @return Value fixed
   */
  public static String toHTMLText(String value) {
    // Escape value
    String strHtml = StringEscapeUtils.escapeHtml4(value);

    // Change \\n to new lines
    strHtml = strHtml.replace(NEW_LINE_DASHED, BREAK_LINE);
    strHtml = strHtml.replace("\\n", BREAK_LINE);

    // Return value
    return strHtml;
  }

  /**
   * Generates a Plain Text
   *
   * @param value String value
   * @return Value fixed
   */
  public static String toPlainText(String value) {

    String strText = value;

    // Change \\n to new lines
    strText = strText.replace(NEW_LINE_DASHED, "\n");
    strText = strText.replace("\\n", "\n");
    strText = strText.replace("\\\\t", "\t");
    strText = strText.replace("\\t", "\t");

    /* Return value */
    return strText;
  }

  /**
   * Generates an Uniline Text
   *
   * @param value String value
   * @return Value fixed
   */
  public static String toUnilineText(String value) {

    String strUniline = value;

    // Change \\n to SPACES
    strUniline = removeHTMLFromText(strUniline);
    strUniline = strUniline.replace(NEW_LINE_DASHED, " ");
    strUniline = strUniline.replace("\\n", " ");
    strUniline = strUniline.replace("\n", " ");

    return strUniline;
  }

  /**
   * Remove new lines
   *
   * @param value String value
   * @return Value fixed
   */
  public static String removeNewLine(String value) {
    Matcher m = BREAK_LINE_PATTERN.matcher(value);
    return m.replaceAll("");
  }

  /**
   * Compress Json
   *
   * @param value String value
   * @return Value fixed
   */
  public static String compressJson(String value) {
    // Remove '{' and '}'
    String compressed = value.substring(1, value.length() - 1);

    // Remove value separators
    compressed = compressed.replace("\":\"", "=");

    // Remove field separators
    return compressed.replace("\",\"", "|");
  }

  /**
   * Decompress Json
   *
   * @param compressed String value
   * @return Value fixed
   */
  public static String decompressJson(String compressed) {
    // Add value separators
    String value = compressed.replace("=", "\":\"");

    // Add field separators and '{' '}'
    return "{" + value.replace("|", "\",\"") + "}";
  }

  /**
   * Removes comments in js and css
   *
   * @param value       String value
   * @param removeLines Remove new lines
   * @return Value fixed
   */
  public static String removeComments(String value, boolean removeLines) {
    // Remove group comments
    Pattern p = removeLines ? MULTILINE_COMMENTS : SINGLE_LINE_COMMENTS;
    Matcher m = p.matcher(value);
    String textWithoutComments = m.replaceAll("");

    // Remove multiple lines
    if (removeLines) {
      m = SPACES.matcher(textWithoutComments);
      textWithoutComments = m.replaceAll(" ");
    }

    // Return trimmed value
    return textWithoutComments.trim();
  }

  /**
   * Fixes an string value for a Path
   *
   * @param path String path
   * @return Path fixed
   */
  public static String fixPath(String path) {

    String fixPath = path;

    if (fixPath == null) {
      return null;
    }
    fixPath = fixPath.replace("\\", PATH_DELIMITER);
    return fixPath.trim();
  }

  /**
   * Retrieves the absolute path of an unknown type path
   *
   * @param path String path
   * @param base Base path
   * @return Absolute path
   */
  public static String getAbsolutePath(String path, String base) {

    String absPath = path;
    absPath = absPath.indexOf('@') > -1 ? base + PATH_DELIMITER + absPath.substring(path.lastIndexOf('@') + 1) : absPath;
    return absPath.trim();
  }

  /**
   * Fixes an string value for an URI
   *
   * @param uri String path
   * @return URI fixed
   */
  public static String fixURI(String uri) {
    String fixUri = uri;
    fixUri = fixUri.replace("\\\\", "\\");
    return fixUri.trim();
  }

  /**
   * Get the last word of a path
   *
   * @param path Complete path
   * @return Last word of a path
   */
  public static String getContextPath(String path) {

    // Check if path is empty or null
    if (path == null || path.isEmpty()) {
      return "";
    }

    // Return path name
    return new java.io.File(path).getName();
  }

  /**
   * Replaces a wildcard inside a script or css file
   *
   * @param target string to replace
   * @param key    wildcard key
   * @param val    wildcard value
   * @return file with the wildcard replaced
   */
  public static String replaceWildcard(String target, String key, String val) {
    Pattern p = Pattern.compile("(" + WILDCARD_START + key + WILDCARD_END + ")", Pattern.DOTALL);
    Matcher m = p.matcher(target);
    return m.replaceAll(val);
  }

  /**
   * Replaces a wildcard
   *
   * @param target string to replace
   * @param key    wildcard key
   * @param val    wildcard value
   * @param start  Pattern header
   * @param end    Pattern footer
   * @return Value fixed
   */
  public static String replaceWildcard(String target, String key, String val, String start, String end) {
    String fixedValue = val;

    // Change \' to '
    fixedValue = fixedValue.replace("\\'", "'");
    fixedValue = fixedValue.replace("\'", "'");

    // Fix $ issue
    fixedValue = Matcher.quoteReplacement(fixedValue);

    // Replace wildcard
    Pattern p = Pattern.compile("(" + start + key + end + ")", Pattern.DOTALL);
    Matcher m = p.matcher(target);
    return m.replaceAll(fixedValue);
  }

  /**
   * Removes first and last characters and changes them
   *
   * @param list  List as string value
   * @param start New start character
   * @param end   New end character
   * @return List value fixed
   */
  public static String fixListString(String list, String start, String end) {
    String listString = list;
    Integer lisLng = listString.length();
    listString = start + (listString.substring(1, lisLng - 1)) + end;
    return listString;
  }

  /**
   * Evaluates an expression as a javascript engine
   *
   * @param expression       Expression to evaluate
   * @param javascriptEngine Javascript engine
   * @return Evaluated expression
   * @throws javax.script.ScriptException Script exception
   */
  public static Object eval(String expression, ScriptEngine javascriptEngine) throws ScriptException {
    return javascriptEngine.eval(expression);
  }

  /**
   * Evaluates an expression as a javascript engine
   *
   * @param expression Expression to evaluate
   * @return Evaluated expression
   */
  public static String evalMarkdown(String expression) {
    Node document = PARSER.parse(expression);
    String html = HTML_RENDERER.render(document);

    // There is only one paragraph, remove it
    if (html.indexOf("") == html.lastIndexOf("")) {
      html = html.replace("", "").replace("", "");
    }
    return html;
  }

  /**
   * Generates an ArrayNode
   *
   * @param pattern Pattern
   * @param value   String value
   * @return Value as ArrayNode
   */
  public static ArrayNode toArrayNode(String pattern, String value) {
    // Mapper Object
    ObjectMapper mapper = new ObjectMapper();

    // Split value by pattern
    String[] splittedVal = value.split(pattern);

    // Adds splitted Array to ArrayNode
    return mapper.valueToTree(splittedVal);
  }

  /**
   * Generates a string list
   *
   * @param pattern Pattern
   * @param value   String value
   * @return Value as ArrayNode
   */
  public static List<String> toStringList(String pattern, String value) {
    return value.trim().isEmpty() ? Collections.emptyList() : Arrays.asList(value.split(pattern));
  }

  /**
   * Compare two values ignoring case
   *
   * @param base      Base string
   * @param compareTo compare to element
   * @return Base contains compareTo
   */
  public static boolean containsIgnoreCase(String base, String compareTo) {
    if (base == null && compareTo == null) {
      return true;
    } else if (base == null || compareTo == null) {
      return false;
    } else {
      return base.toLowerCase().contains(compareTo.toLowerCase());
    }
  }

  /**
   * Get parameter as string list
   *
   * @param parameter parameter
   * @return parameter list
   */
  public static List<String> asList(JsonNode parameter) {
    List<String> parameterAsList = new ArrayList<>();
    if (parameter != null) {
      if (parameter.isTextual()) {
        parameterAsList.add(parameter.asText());
      } else if (parameter.isArray()) {
        ArrayNode parameterList = (ArrayNode) parameter;
        for (JsonNode parameterValue : parameterList) {
          parameterAsList.add(parameterValue.asText());
        }
      }
    }
    return parameterAsList;
  }

  /**
   * Shorten a text if longer than size
   * @param text Text to shorten
   * @param size Max text size
   * @param replacement Text to replace the extra characters
   * @return Shortened string
   */
  public static String shortenText(String text, Integer size, String replacement) {
    return size > 0 && text.length() > size ? text.substring(0, size - replacement.length()) + replacement : text;
  }
}
