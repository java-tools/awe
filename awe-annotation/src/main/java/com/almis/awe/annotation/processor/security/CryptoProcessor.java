package com.almis.awe.annotation.processor.security;

import com.almis.awe.annotation.aspect.CryptoAnnotation;
import com.almis.awe.annotation.classload.SecurityAnnotationProcessor;
import com.almis.awe.annotation.entities.security.Crypto;
import com.almis.awe.config.ServiceConfig;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.security.EncodeUtil;
import org.apache.logging.log4j.Level;

/**
 * Processor for the Crypto annotation
 *
 * @see SecurityAnnotationProcessor
 * @see CryptoAnnotation
 * @see Crypto
 * @author dfuentes
 * Created by dfuentes on 18/04/2017.
 */
public class CryptoProcessor extends ServiceConfig {

  /**
   * Process current annotation
   *
   * @param crypto Crypto text
   * @param text Value
   * @return
   */
  public String processCrypto(Crypto crypto, String text) {
    String processedValue = null;
    switch(crypto.action()){
      case ENCRYPT:
        try {
          processedValue = EncodeUtil.encryptAes(text, crypto.password());
        } catch (AWException e) {
          getLogger().log(SecurityAnnotationProcessor.class, Level.ERROR,e.getMessage(),e);
        }
        break;
      case DECRYPT:
        try {
          processedValue = EncodeUtil.decryptAes(text, crypto.password());
        } catch (AWException e) {
          getLogger().log(SecurityAnnotationProcessor.class, Level.ERROR,e.getMessage(),e);
        }
        break;
      default:
        processedValue = text;
        break;
    }
    return processedValue;
  }
}
