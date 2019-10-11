package com.almis.awe.model.util.data;

import com.almis.awe.exception.AWException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compression utilities
 */
@Log4j2
public final class CompressionUtil {

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
      log.debug("Compressing from {}  to {}", string.getBytes().length, os.toByteArray().length);
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
      log.debug("Uncompressing from {} to {}", compressed.length, bytes.length);
      return new String(bytes, StandardCharsets.UTF_8);
    } catch (IOException exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.getMessage(), exc);
    }
  }
}
