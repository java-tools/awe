package com.almis.awe.model.util.data;

import com.almis.awe.exception.AWException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compression utilities
 */
public final class CompressionUtil {

  private static final Logger logger = LogManager.getLogger(CompressionUtil.class);

  /**
   * Private constructor to enclose the default one
   */
  private CompressionUtil() {}

  /**
   * Compress a string
   * @param string String to compress
   * @return String compressed
   * @throws AWException AWE exception
   */
  public static byte[] compress(String string) throws AWException {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream(string.length());
         GZIPOutputStream gos = new GZIPOutputStream(os)) {
      gos.write(string.getBytes());
      gos.finish();
      logger.debug("Compressing from " + string.getBytes().length + " to " + os.toByteArray().length);
      return os.toByteArray();
    } catch (IOException exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.getMessage(), exc);
    }
  }

  /**
   * Decompress a string
   * @param compressed string compressed
   * @return String decompress
   * @throws AWException AWE exception
   */
  public static String decompress(byte[] compressed) throws AWException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
         GZIPInputStream gis = new GZIPInputStream(bis)) {
      byte[] bytes = IOUtils.toByteArray(gis);
      logger.debug("Uncompressing from " + compressed.length + " to " + bytes.length);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (IOException exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.getMessage(), exc);
    }
  }
}
