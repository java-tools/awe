package com.almis.awe.annotation.processor.security;

import com.almis.awe.annotation.aspect.HashAnnotation;
import com.almis.awe.annotation.entities.security.Hash;
import com.almis.awe.exception.AWException;
import com.almis.awe.model.util.security.EncodeUtil;

/**
 * Processor for the Hash annotation
 *
 * @author dfuentes
 * Created by dfuentes on 18/04/2017.
 * @see HashAnnotation
 * @see Hash
 */
public class HashProcessor {

  /**
   * Process current annotation
   *
   * @param hash  Hash
   * @param value Value to process
   * @return Processed hash
   * @throws AWException Error processing hash
   */
  public static String processHashing(Hash hash, String value) throws AWException {
    return EncodeUtil.hash(hash.algorithm().getAlgorithm(), value, hash.salt());
  }
}
