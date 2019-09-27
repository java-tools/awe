package com.almis.awe.model.util.data;

import com.almis.awe.model.constant.AweConstants;
import com.almis.awe.model.type.NumericType;
import com.almis.awe.model.type.RoundingType;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.env.Environment;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * NumericUtil Class
 * Numeric Utilities for AWE
 *
 * @author Pablo GARCIA - 16/APR/2012
 */
@Log4j2
public final class NumericUtil {

  // Static variables
  private static final NumberFormat AMERICAN_NUMBER_FORMAT = NumberFormat.getInstance(Locale.US);
  private static final NumberFormat EUROPEAN_NUMBER_FORMAT = NumberFormat.getInstance(Locale.GERMANY);
  private static String formattedPattern;
  private static String unformattedPattern;
  private static String numericFormat;
  private static RoundingType roundingType;
  private static NumberFormat americanFormat;

  /**
   * Hide the constructor
   */
  private NumericUtil() {}

  /**
   * Initialize Utility class
   * @param springEnvironment Spring environment
   */
  public static void init(Environment springEnvironment) {
    Environment environment = springEnvironment;
    formattedPattern = environment.getProperty(AweConstants.PROPERTY_NUMERIC_PATTERN_FORMATTED, "###,###.####");
    unformattedPattern = environment.getProperty(AweConstants.PROPERTY_NUMERIC_PATTERN_UNFORMATTED, "######.####");
    numericFormat = environment.getProperty(AweConstants.PROPERTY_NUMERIC_FORMAT, "eur");
    roundingType = RoundingType.fromCode(environment.getProperty(AweConstants.PROPERTY_NUMERIC_ROUND_TYPE, "S"));
    americanFormat = AMERICAN_NUMBER_FORMAT;
    americanFormat.setRoundingMode(roundingType.getRoundingMode());
  }

  /**
   * Fixes an string value for a criteria
   *
   * @param pattern pattern to apply
   * @param value   string number value
   * @return number formatted
   */
  public static String applyPattern(String pattern, Double value) {
    // Get pattern
    String patternToSet = pattern == null ? formattedPattern : pattern;
    return applyPatternWithLocale(patternToSet, value, getNumberFormat(numericFormat));
  }

  /**
   * Fixes an string value for a criteria in a raw pattern Use Locale US
   *
   * @param pattern Number pattern
   * @param value   String value
   * @return Value fixed
   */
  public static String applyRawPattern(String pattern, Double value) {
    String patternToSet = pattern == null ? unformattedPattern : pattern;
    return applyPatternWithLocale(patternToSet, value, americanFormat);
  }

  /**
   * Fixes an string value for a criteria in a raw pattern with locale
   *
   * @param pattern patter to apply
   * @param value   number value
   * @param format  LOCALE to apply
   * @return String with pattern applied
   */
  public static String applyPatternWithLocale(String pattern, Double value, String format) {
    return applyPatternWithLocale(pattern, value, getNumberFormat(format));
  }

  /**
   * Fixes an string value for a criteria in a raw pattern with locale
   *
   * @param pattern      patter to apply
   * @param value        number value
   * @param numberFormat LOCALE to apply
   * @return String with pattern applied
   */
  public static String applyPatternWithLocale(String pattern, Double value, NumberFormat numberFormat) {
    ((DecimalFormat) numberFormat).applyPattern(pattern);
    return formatNumber(numberFormat, value);
  }

  /**
   * Fixes an string value for a criteria in a raw pattern with locale
   *
   * @param format Number format
   * @param value  number value
   * @return Number formatted
   */
  public static String formatNumber(NumberFormat format, Double value) {
    try {
      return format.format(value);
    } catch (Exception exc) {
      log.error("Error formatting number {} with formatter {}", value, format, exc);
    }
    return String.valueOf(value);
  }

  /**
   * Parses a string value to a number. It reads the numeric type from
   * numeric.properties
   *
   * @param val String value with format as defined in numeric.properties
   * @return parsedValue parsed number
   * @throws ParseException Error parsing number
   */
  public static Number parseNumericString(String val) throws ParseException {
    return getNumberFormat(numericFormat).parse(val);
  }

  /**
   * Parses a string value to a number. It parses a string that represents a raw formatted numeric
   *
   * @param val String value with format as defined in numeric.properties
   * @return parsedValue parsed number
   * @throws ParseException Error parsing number
   */
  public static Number parseRawNumericString(String val) throws ParseException {
    return americanFormat.parse(val);
  }

  /**
   * Returns decimals number a string that represents a formatted numeric
   *
   * @param val String value with format as defined in numeric.properties
   * @return decimals number
   * @throws ParseException Error parsing number
   */
  public static int getDecimalsNumberInNumericString(String val) throws ParseException {
    NumberFormat numberFormat = getNumberFormat(numericFormat);
    numberFormat.parse(val);

    int separatorIndex = val.indexOf(((DecimalFormat) numberFormat).getDecimalFormatSymbols().getDecimalSeparator());
    return (separatorIndex > -1) ? val.length() - separatorIndex - 1 : 0;
  }

  /**
   * Returns decimals number a string that represents a raw formatted numeric
   *
   * @param val String value with format as defined in numeric.properties
   * @return decimals number
   * @throws ParseException Error parsing number
   */
  public static int getDecimalsNumberInRawNumericString(String val) throws ParseException {
    americanFormat.parse(val);
    String decimalToSplit = String.valueOf(((DecimalFormat) americanFormat).getDecimalFormatSymbols().getDecimalSeparator());
    String[] splitted = val.split(Pattern.quote(decimalToSplit));
    return (splitted.length > 1) ? splitted[1].length() : 0;
  }

  /**
   * Gets the numeric format defined in numeric.properties
   *
   * @return numeric format
   */
  private static NumberFormat getNumberFormat(String type) {
    NumberFormat numberFormat;

    // Apply locale based on numeric.properties
    switch (NumericType.getEnum(type)) {
      case AME:
      case AME_NO:
        // American formatting
        numberFormat = AMERICAN_NUMBER_FORMAT;
        break;
      default:
        // European formatting
        numberFormat = EUROPEAN_NUMBER_FORMAT;
        break;
    }

    // Apply rounding based on numeric.properties
    numberFormat.setRoundingMode(roundingType.getRoundingMode());

    return numberFormat;
  }
}
