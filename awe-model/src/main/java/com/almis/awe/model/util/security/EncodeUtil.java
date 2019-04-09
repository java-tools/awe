package com.almis.awe.model.util.security;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/*
 * File Imports
 */

/**
 * EncodeUtil Class
 * Encode Utilities for awe
 *
 * @author Pablo GARCIA - 17/MAR/2011
 */
public final class EncodeUtil {

  private static final Logger logger = LogManager.getLogger(EncodeUtil.class);
  private static final String STRING_ENCODE_ERROR = "String encode error";

  /**
   * Static class to store hashing algorithms to make the call to these algorithms more understandable.
   */
  public static final class HashingAlgorithms {

    /**
     * Private constructor to enclose the default one
     */
    private HashingAlgorithms() {}

    // HASHING ALGORITHMS
    public static final String MD5 = "MD5";
    public static final String SHA_256 = "SHA-256";
    public static final String SHA_512 = "SHA-512";
  }

  // Static variables
  private static String encoding;
  private static String masterKey;
  private static StringKeyGenerator keyGenerator;

  /**
   * Initialize Utility class
   * @param springEnvironment Spring environment
   */
  public static void init(Environment springEnvironment)  {
    Environment environment = springEnvironment;
    encoding = environment.getProperty(AweConstants.PROPERTY_APPLICATION_ENCODING, AweConstants.APPLICATION_ENCODING);
    masterKey = environment.getProperty(AweConstants.PROPERTY_SECURITY_MASTER_KEY, "4W3M42T3RK3Y%$ED");
    keyGenerator = KeyGenerators.string();
  }

  /**
   * Hide the constructor
   */
  private EncodeUtil() {}

  /**
   * Encodes a text in RIPEMD-160
   *
   * @param text Text to encode
   * @return Text encoded
   */
  public static String encodeRipEmd160(String text) {

    /* Variable definition */
    RipEmd160 encoder = new RipEmd160();
    return encoder.encodeRipEmd160(text);
  }

  /**
   * Decrypts a string with RipEmd160
   *
   * @param text String to be decrypted
   * @return String decrypted
   * @throws AWException Error in decryption
   */
  public static String decryptRipEmd160(String text) throws AWException {
    return decryptRipEmd160WithPhraseKey(text, masterKey, encoding);
  }

  /**
   * Encrypts a string with RipEmd160
   *
   * @param text String to be encrypted
   * @return String encrypted
   * @throws AWException Error in encryption
   */
  public static String encryptRipEmd160(String text) throws AWException {
    return encryptRipEmd160WithPhraseKey(text, masterKey, encoding);
  }

  /**
   * Encrypts a string with RipEmd160 and phraseKey
   *
   * @param text      String to be encrypted
   * @param phraseKey Phrase key
   * @return String encrypted
   * @throws AWException Error in encryption
   */
  public static String encryptRipEmd160WithPhraseKey(String text, String phraseKey) throws AWException {
    return encryptRipEmd160WithPhraseKey(text, phraseKey, encoding);
  }


  /**
   * Encrypts a string with RipEmd160 and phraseKey
   *
   * @param text      String to be encrypted
   * @param phraseKey Phrase key
   * @param encoding  Encoding
   * @return String encrypted
   * @throws AWException Error in encryption
   */
  public static String encryptRipEmd160WithPhraseKey(String text, String phraseKey, String encoding) throws AWException {
    try {
      String key = phraseKey == null || phraseKey.isEmpty() ? masterKey : phraseKey;
      RipEmd160 encoder = new RipEmd160(key);
      return encoder.encrypt(text, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Decrypts a string with RipEmd160 and phraseKey
   *
   * @param text      String to be decrypted
   * @param phraseKey Phrase key
   * @param encoding  Encoding
   * @return String decrypted
   * @throws AWException Error in decryption
   */
  public static String decryptRipEmd160WithPhraseKey(String text, String phraseKey, String encoding) throws AWException {
    try {
      String key = phraseKey == null || phraseKey.isEmpty() ? masterKey : phraseKey;
      RipEmd160 encoder = new RipEmd160(key);
      return encoder.decrypt(text, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Decrypts a string with Aes
   *
   * @param text String to be decrypted
   * @return String decrypted
   * @throws AWException Error in decryption
   */
  public static String decryptAes(String text) throws AWException {
    try {
      return Crypto.AES.decrypt(text, masterKey, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Decrypts a string with Aes
   *
   * @param text     String to be decrypted
   * @param password Password
   * @return String decrypted
   * @throws AWException Error in decryption
   */
  public static String decryptAes(String text, String password) throws AWException {
    try {
      return Crypto.AES.decrypt(text, password, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Encrypts a string with Aes
   *
   * @param text String to be encrypted
   * @return String encrypted
   * @throws AWException Error in encryption
   */
  public static String encryptAes(String text) throws AWException {
    try {
      return Crypto.AES.encrypt(text, masterKey, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Encrypts a string with Aes
   *
   * @param text     String to be encrypted
   * @param password Password
   * @return String encrypted
   * @throws AWException Error in encryption
   */
  public static String encryptAes(String text, String password) throws AWException {
    try {
      return Crypto.AES.encrypt(text, password, encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Encodes a text in DES (Symmetric)
   *
   * @param encodeValue Text to encode
   * @return Text encoded
   * @throws AWException Error in encoding
   */
  public static String encodeSymmetric(byte[] encodeValue) throws AWException {
    try {
      return base64Encode(encodeValue);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Encodes a text in DES (Symmetric)
   *
   * @param text Text to encode
   * @return Text encoded
   * @throws AWException Error in encoding
   */
  public static String encodeSymmetric(String text) throws AWException {
    return encodeSymmetric(text.getBytes());
  }

  /**
   * Decodes a text in DES (Symmetric)
   *
   * @param text Text to decode
   * @return Text decoded
   * @throws AWException Error in decoding
   */
  public static String decodeSymmetric(String text) throws AWException {
    try {
      return new String(decodeSymmetricAsByteArray(text), encoding);
    } catch (UnsupportedEncodingException exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Decodes a text in DES (Symmetric)
   *
   * @param text Text to decode
   * @return Text decoded
   * @throws AWException Error in decoding
   */
  public static byte[] decodeSymmetricAsByteArray(String text) throws AWException {
    try {
      if (text != null) {
        // Decode
        return base64DecodeAsByteArray(text);
      } else {
        return new byte[0];
      }
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }
  }

  /**
   * Encodes a text in Hex
   *
   * @param text Text to encode
   * @return Text encoded
   * @throws AWException Error encoding
   */
  public static String encodeHex(String text) throws AWException {

    /* Variable definition */
    String outStr = text;

    try {
      /* Encode */
      outStr = new String(new Hex().encode(text.getBytes()), encoding);
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }

    /* Transform encoded bytemap to string and return it */
    return outStr;
  }

  /**
   * Decodes a text in Hex
   *
   * @param text Text to decode
   * @return Text decoded
   * @throws AWException Error decoding
   */
  public static String decodeHex(String text) throws AWException {

    /* Variable definition */
    String outStr = null;

    try {
      if (text != null) {
        // Decode
        outStr = new String(new Hex().decode(text.getBytes()), encoding);
      }
    } catch (Exception exc) {
      throw new AWException(exc.getClass().getSimpleName(), exc.toString(), exc);
    }

    /* Transform encoded bytemap to string and return it */
    return outStr;
  }

  /**
   * Decodes a text in DES (Symmetric)
   *
   * @param text   Text to decode
   * @param active Encoding enabled
   * @return Text decoded
   * @throws AWException Error encoding
   */
  public static String encodeTransmission(String text, boolean active) throws AWException {

    String out = text;

    // Encode only if encoding is enabled
    if (active) {
      out = encodeSymmetric(text);
    }

    // Return encoded transmission
    return out;
  }

  /**
   * Decodes a text in DES (Symmetric)
   *
   * @param text   Text to decode
   * @param active Encoding enabled
   * @return Text decoded
   * @throws AWException Error decoding
   */
  public static String decodeTransmission(String text, boolean active) throws AWException {

    String out = text;

    // Decode only if encoding is enabled
    if (active) {
      out = decodeSymmetric(text);
    }

    // Return encoded transmission
    return out;
  }

  /**
   * Hashes an String with a SHA-X algorithm without any salt, where X is the algorithm given in the algorithm variable
   *
   * @param algorithm Algorithm
   * @param text      Text to hash
   * @return String Hexadecimal format of the output bytearray
   * @throws AWException Error hashing
   */
  public static String hash(String algorithm, String text) throws AWException {
    return hash(algorithm, text, null);
  }

  /* HASHING ALGORITHMS */

  /**
   * Hashes an String with a SHA-X algorithm with a salt, where X is the algorithm given in the algorithm variable
   *
   * @param algorithm Algorithm
   * @param text      Text to hash
   * @param salt      The salt to be applied to the algorithm
   * @return String Hexadecimal format of the output bytearray
   * @throws AWException Error hashing
   */
  public static String hash(String algorithm, String text, String salt) throws AWException {
    return Crypto.HASH.hash(text, algorithm, salt, Charset.forName(encoding));
  }

  /**
   * Hashes an String with a PBKDF2 algorithm based on SHA-1
   *
   * @param text Text to encode
   * @return String in Hexadecimal format
   * @throws AWException Error encoding
   */
  public static String encodePBKDF2WithHmacSHA1(String text) throws AWException {
    final int keyLength = 256;
    return encodePBKDF2WithHmacSHA1(text, masterKey, Crypto.Utils.getRecommendedIterationNumber(), keyLength);
  }

  /**
   * Hashes an String with a PBKDF2 algorithm based on SHA-1
   *
   * @param text Text to encode
   * @param salt should be of about 160 bit size or more to create a strong salt
   * @return String in Hexadecimal format
   * @throws AWException Error encoding
   */
  public static String encodePBKDF2WithHmacSHA1(String text, String salt) throws AWException {
    final int keyLength = 256;
    return encodePBKDF2WithHmacSHA1(text, salt, Crypto.Utils.getRecommendedIterationNumber(), keyLength);
  }

  /**
   * Hashes an String with a PBKDF2 algorithm based on SHA-1
   *
   * @param text       Text to encode
   * @param salt       should be of about 160 bit size or more to create a strong salt
   * @param iterations Number of iterations
   * @return String in Hexadecimal format
   * @throws AWException Error encoding
   */
  public static String encodePBKDF2WithHmacSHA1(String text, String salt, int iterations) throws AWException {
    final int keyLength = 256;
    return encodePBKDF2WithHmacSHA1(text, salt, iterations, keyLength);
  }

  /**
   * Hashes an String with a PBKDF2 algorithm based on SHA-1
   *
   * @param text       Text to encode
   * @param salt       should be of about 160 bit size or more to create a strong salt
   * @param iterations recommended value is 256000 in 2016, double this value every 2 years
   * @param keyLength  should not be bigger than the maximum output length of the SHA-1 algorithm wich is 160 bit (40 hex characters)
   * @return String in Hexadecimal format
   * @throws AWException Error encoding
   */

  public static String encodePBKDF2WithHmacSHA1(String text, String salt, int iterations, int keyLength) throws AWException {
    String hashVal = null;
    try {
      // Get instance of the hashing algorithm
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

      // Add parameters
      PBEKeySpec spec = new PBEKeySpec(text.toCharArray(), salt.getBytes(encoding), iterations, keyLength);

      // Generate a key for the hash
      SecretKey key = skf.generateSecret(spec);

      // Hash
      byte[] res = key.getEncoded();

      // Get the hashed value as hexadecimal string
      hashVal = Crypto.Utils.encodeHex(res);
    } catch (NoSuchAlgorithmException exc) {
      throw new AWException(STRING_ENCODE_ERROR, "The algorithm does not exist.", exc);
    } catch (InvalidKeySpecException exc) {
      throw new AWException(STRING_ENCODE_ERROR, "The specified key is not valid or is not long enough.", exc);
    } catch (UnsupportedEncodingException exc) {
      throw new AWException(STRING_ENCODE_ERROR, "The specified encoding is not valid.", exc);
    }
    return hashVal;
  }

  /*****************************************************/
  /************ Secure Random Methods ******************/
  /*****************************************************/
  /**
   * generate secure random object
   *
   * @return Secure random
   */
  public static SecureRandom getSecureRandom() {
    try {
      return SecureRandom.getInstance(AweConstants.RANDOM_ALGORITHM);
    } catch (NoSuchAlgorithmException exc) {
      logger.error("Selected algorithm does not exist: {0}", AweConstants.RANDOM_ALGORITHM, exc);
      return new SecureRandom();
    }
  }

  /**
   * Create a secure random string of the given length with the alphabet
   * @return Random string
   */
  public static String getSecureRandomString() {
    return keyGenerator.generateKey();
  }

  /**
   * Encode given text to the given encoding
   *
   * @param text Text to encode
   * @param encoding Encoding
   * @return Text encoded
   * @throws UnsupportedEncodingException Error encoding
   */
  public static String encodeLanguage(String text, String encoding) throws UnsupportedEncodingException {
    return new String(text.getBytes(), encoding);
  }

  /**
   * Symmetric encryption
   *
   * @param digest    Digest
   * @param valueEnc  Value to encode
   * @param secretKey Key
   * @return Value encoded
   */
  public static String encrypt(String digest, String valueEnc, final String secretKey) {

    String encryptedVal = null;

    try {
      SecretKey key = generateKeyFromString(digest, secretKey);
      final Cipher c = Cipher.getInstance(digest);

      byte[] ivBytes = new byte[16];
      getSecureRandom().nextBytes(ivBytes);
      IvParameterSpec iv = new IvParameterSpec(ivBytes);

      c.init(Cipher.ENCRYPT_MODE, key, iv);
      final byte[] encValue = c.doFinal(valueEnc.getBytes());
      return base64Encode(encValue);
    } catch (Exception ex) {
      logger.error(ex.getLocalizedMessage());
    }

    return encryptedVal;
  }

  /**
   * Symmetric decryption
   *
   * @param digest         Digest
   * @param encryptedValue Encrypted value
   * @param secretKey      Key
   * @return Decrypted value
   */
  public static String decrypt(String digest, String encryptedValue, final String secretKey) {

    String decryptedValue = null;

    try {
      SecretKey key = generateKeyFromString(digest, secretKey);
      final Cipher c = Cipher.getInstance(digest);

      byte[] ivBytes = new byte[16];
      getSecureRandom().nextBytes(ivBytes);
      IvParameterSpec iv = new IvParameterSpec(ivBytes);

      c.init(Cipher.DECRYPT_MODE, key, iv);
      final byte[] decorVal = base64Decode(encryptedValue).getBytes();
      final byte[] decValue = c.doFinal(decorVal);
      decryptedValue = new String(decValue, encoding);
    } catch (Exception ex) {
      logger.error(ex.getLocalizedMessage());
    }

    return decryptedValue;
  }

  /**
   * Generate valid key from user given string
   *
   * @param algorithm Algorithm
   * @param secKey    Secret key
   * @return Secret key
   * @throws NoSuchAlgorithmException Invalid algorithm
   * @throws InvalidKeySpecException Error retrieving key
   */
  private static SecretKey generateKeyFromString(String algorithm, final String secKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    KeySpec spec = new PBEKeySpec(secKey.toCharArray(), base64Encode(secKey).getBytes(), 65536, 256);
    SecretKey key = factory.generateSecret(spec);
    return new SecretKeySpec(key.getEncoded(), algorithm);
  }

  /**
   * Base64 encode string
   *
   * @param text Text to encode
   * @return Text encoded
   */
  public static String base64Encode(String text) {
    return Base64.encodeBase64String(text.getBytes());
  }

  /**
   * Base64 encode byte array
   *
   * @param text Text to encode
   * @return Text encoded
   */
  public static String base64Encode(byte[] text) {
    return Base64.encodeBase64URLSafeString(text);
  }

  /**
   * Base64 decode string
   *
   * @param text Text to decode
   * @return Text decoded
   * @throws java.io.UnsupportedEncodingException
   */
  public static String base64Decode(String text) throws UnsupportedEncodingException {
    return new String(Base64.decodeBase64(text.getBytes()), encoding);
  }

  /**
   * Base64 decode string
   *
   * @param text Text to decode
   * @return Text decoded
   */
  public static byte[] base64DecodeAsByteArray(String text) {
    return Base64.decodeBase64(text);
  }

  /**
   * Base64 decode byte array
   *
   * @param text Text to decode
   * @return Text decoded
   * @throws java.io.UnsupportedEncodingException
   */
  public static String base64Decode(byte[] text) throws UnsupportedEncodingException {
    return new String(Base64.decodeBase64(text), encoding);
  }
}
