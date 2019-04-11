package com.almis.awe.annotation.processor.security;

import com.almis.awe.annotation.aspect.CryptoAnnotation;
import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.security.EncodeUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Processor for the Crypto annotation
 *
 * @see CryptoAnnotation
 * @see Crypto
 * @author dfuentes
 * Created by dfuentes on 18/04/2017.
 */
public class CryptoProcessor {

  // Logger
  private static Logger logger = LogManager.getLogger(CryptoProcessor.class);

  // Private constructor
  private CryptoProcessor() {}

  /**
   * Process current annotation
   *
   * @param crypto Crypto text
   * @param text Value
   * @return Processed value
   */
  public static String processCrypto(Crypto crypto, String text) {
    String processedValue = null;
    try {
      switch (crypto.action()) {
        case ENCRYPT:
          processedValue = EncodeUtil.encryptAes(text, crypto.password());
          break;
        case DECRYPT:
          processedValue = EncodeUtil.decryptAes(text, crypto.password());
          break;
        default:
          processedValue = text;
      }
    } catch (AWException exc) {
      logger.error(exc.getMessage(), exc);
    }
    return processedValue;
  }
}
