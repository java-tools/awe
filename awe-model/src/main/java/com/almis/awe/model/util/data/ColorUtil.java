/*
 * Package definition
 */
package com.almis.awe.model.util.data;

/*
 * File Imports
 */

import java.awt.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ColorUtil Class
 * Utils to do conversions of color codes.
 * 
 * @author Aitor UGARTE - 13/AUG/2013
 */
public final class ColorUtil {

  // Class name
  private static final String UTILITY_NAME = "COLOR UTILITY";
  private static final Logger logger = LogManager.getLogger(ColorUtil.class);

  /**
   * Private constructor to enclose the default one
   */
  private ColorUtil() {}

  /**
   * Hexadecimal to RGB
   *
   * @param colorStr Hex color
   * @return RGB Color
   */
  public static String hex2RgbStr(String colorStr) {
    String output = "0-0-0";
    try {
      output = Integer.valueOf(colorStr.substring(1, 3), 16) + "-" + Integer.valueOf(colorStr.substring(3, 5), 16) + "-" + Integer.valueOf(colorStr.substring(5, 7), 16);
    } catch (Exception exc) {
      logger.error("[{0}] Error in color conversion from hexadecimal ({1}) to rgb string", new Object[] { UTILITY_NAME, colorStr }, exc);
    }

    return output;
  }

  /**
   * Hexadecimal to Color
   *
   * @param colorStr Hexadecimal
   * @return Color
   */
  public static Color hex2Rgb(String colorStr) {
    Color output = Color.WHITE;
    try {
      String rgb = hex2RgbStr(colorStr);
      output = getRgbCol(rgb);
    } catch (Exception exc) {
      logger.error("[{0}] Error in color conversion from hexadecimal ({1}) to rgb color", new Object[] { UTILITY_NAME, colorStr }, exc);
    }
    return output;
  }

  /**
   * Retrieve rgb color
   *
   * @param rgb String rgb color
   * @return Color
   */
  public static Color getRgbCol(String rgb) {
    Color output = Color.WHITE;
    try {
      if (rgb != null && !rgb.trim().isEmpty()) {
        String[] color = rgb.split("-");
        output = new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
      }
    } catch (Exception exc) {
      logger.error("[{0}] Error in color conversion from rgb string ({1}) to rgb color", new Object[] { UTILITY_NAME, rgb }, exc);
    }
    return output;
  }
}