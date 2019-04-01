package com.almis.awe.model.util.security;

import com.almis.awe.exception.AWException;
import com.almis.awe.model.constant.AweConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

/**
 * Cryptographic module Contains some basic encrypt utilities
 *
 * @author pgarcia
 */

public final class Crypto {

  /**
   * Private constructor to hide the implicit one
   */
  private Crypto() {}

  /**
   * Utils
   */
  public static class Utils {

    private static final Logger logger = LogManager.getLogger(Utils.class);
    private static Random random;

    static {
      try {
        random = SecureRandom.getInstance("SHA1PRNG");
      } catch (NoSuchAlgorithmException exc) {
        logger.error("Error initializing secure random", exc);
      }
    }

    /**
     * Private constructor of Utils class
     */
    private Utils() {}

    /**
     * Encode with pbkdf
     *
     * @param password
     * @param salt
     * @param iterationCount
     * @param dkLen
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     */
    public static String pbkdf2(String password, String salt, int iterationCount, int dkLen) throws InvalidKeyException, NoSuchAlgorithmException {

      int dkLenMod = dkLen;
      int numIter = iterationCount;

      if (dkLenMod != 16 && dkLenMod != 24 && dkLenMod != 32) {
        dkLenMod = 16;
      }
      if (numIter < 0) {
        numIter = 0;
      }

      byte[] arrPassword = password.getBytes();
      byte[] arrSalt = salt.getBytes();
      byte[] key = PBKDF2.deriveKey(arrPassword, arrSalt, numIter, dkLenMod);
      return new String(key, StandardCharsets.UTF_8);
    }

    /**
     * Retrieve random bytes
     *
     * @param len Length
     * @return random bytes
     */
    public static byte[] getRandomBytes(int len) {
      int lenMod = len;
      byte[] aesKey;

      if (lenMod < 0) {
        lenMod = 8;
      }

      aesKey = new byte[lenMod];
      random.nextBytes(aesKey);
      return aesKey;
    }

    /**
     * Get the recommended iteration number
     *
     * @return String
     */
    public static int getRecommendedIterationNumber() {

      // Set base values
      int baseIterations = 256000;
      final int baseDate = 2016;

      // Get the difference of years between 2016 and current year
      int diffYears = Calendar.getInstance().get(Calendar.YEAR) - baseDate;

      // Every 2 years from the base year (2016) the baseIterationNum has to
      // double its value
      int multiply = diffYears / 2;
      if (multiply >= 1) {
        baseIterations = baseIterations * multiply;
      }
      return baseIterations;
    }

    /**
     * Returns an hexadecimal string from a byte array
     *
     * @param hash Hash
     * @return String
     */
    public static String encodeHex(byte[] hash) {
      return String.format("%064x", new java.math.BigInteger(1, hash));
    }
  }

  /**
   * AES encryption utilities
   */
  public static class AES {

    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int AES_KEY_SIZE = 128; // in bits
    // symmetric algorithm for data encryption
    private static final String ALGORITHM = "AES";
    // Removed CBC
    private static final String MODE = "GCM";
    // Padding for symmetric algorithm - Removed PKCS5Padding
    private static final String PADDING = "NoPadding";
    private static final String CIPHER_TRANSFORMATION = ALGORITHM + "/" + MODE + "/" + PADDING;
    private static final byte[] initialVector = Utils.getRandomBytes(GCM_IV_LENGTH);

    private static final Logger logger = LogManager.getLogger(AES.class);

    /**
     * Constructor
     */
    private AES() {}

    /**
     * Generate IV
     * @return IV
     */
    private static Cipher generateCipher(byte[] iv, String passphrase, int mode) {
      try {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(mode, AES.getSecretKey(passphrase, iv), ivSpec);
        return cipher;
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException exc) {
        logger.error("Error initializing cipher: {} - {}", CIPHER_TRANSFORMATION, passphrase, exc);
      }
      return null;
    }

    /**
     * Get Secret key from key
     * @param key Key
     * @return Secret key spec
     */
    private static SecretKeySpec getSecretKey(String key, byte[] iv) {
      try {
        if (key != null) {
          SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
          SecretKey derivedKey = factory.generateSecret(new PBEKeySpec(key.toCharArray(), iv, Utils.getRecommendedIterationNumber(), AES_KEY_SIZE));
          return new SecretKeySpec(derivedKey.getEncoded(), ALGORITHM);
        }
      } catch (NoSuchAlgorithmException | InvalidKeySpecException exc) {
        logger.error("Error generating secret key spec", exc);
      }
      return null;
    }

    /**
     * Encrypt with AES algorithm
     *
     * @param plaintext  Text to encrypt
     * @param passphrase Password for encryption
     * @param encoding Encoding
     * @return Encrypted text
     */
    public static String encrypt(@NotNull String plaintext, String passphrase, String encoding) {
      if (plaintext.length() == 0) {
        return null;
      }

      try {
        Cipher cipher = generateCipher(initialVector, passphrase, Cipher.ENCRYPT_MODE);

        if (cipher != null) {
          byte[] ciphertext = cipher.doFinal(plaintext.getBytes(encoding));
          byte[] encrypted = new byte[initialVector.length + ciphertext.length];
          System.arraycopy(initialVector, 0, encrypted, 0, initialVector.length);
          System.arraycopy(ciphertext, 0, encrypted, initialVector.length, ciphertext.length);
          return Base64.getEncoder().encodeToString(encrypted);
        }
      } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException exc) {
        logger.error("Error encoding string {}", plaintext, exc);
      }
      return null;
    }

    /**
     * Decrypt with AES algorithm
     *
     * @param encrypted Ciphered text
     * @param passphrase Password for decryption
     * @param encoding Encoding
     * @return Decrypted text
     */
    public static String decrypt(@NotNull String encrypted, String passphrase, String encoding) {
      if (encrypted.length() == 0) {
        return null;
      }

      try {
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
        Cipher cipher = generateCipher(iv, passphrase, Cipher.DECRYPT_MODE);
        if (cipher != null) {
          byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
          return new String(ciphertext, encoding);
        }
      } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException exc) {
        logger.error("Error decoding string {}", encrypted, exc);
      }
      return null;
    }
  }

  /**
   * PBKDF2: deriveKey
   */
  private static final class PBKDF2 {

    private static byte[] deriveKey(byte[] password, byte[] salt, int iterationCount, int dkLen) throws NoSuchAlgorithmException, InvalidKeyException {

      final String hmacSHA256 = "HmacSHA256";
      SecretKeySpec keyspec = new SecretKeySpec(password, hmacSHA256);
      Mac prf = Mac.getInstance(hmacSHA256);
      prf.init(keyspec);

      // Note: hLen, dkLen, l, r, T, f, etc. are horrible names for
      // variables and functions in this day and age, but they
      // reflect the terse symbols used in RFC 2898 to describe
      // the PBKDF2 algorithm, which improves validation of the
      // code vs. the RFC.
      //
      // dklen is expressed in bytes. (16 for a 128-bit key)

      // 20 for sha1Hash<
      int hLen = prf.getMacLength();
      // 1 for 128bit (16-byte) keys
      int l = Math.max(dkLen, hLen);
      // 16 for 128bit (16-byte) keys
      int r = dkLen - (l - 1) * hLen;
      byte[] arrByt = new byte[l * hLen];
      int tiOffset = 0;
      for (int i = 1; i <= l; i++) {
        f(arrByt, tiOffset, prf, salt, iterationCount, i);
        tiOffset += hLen;
      }

      if (r < hLen) {
        // Incomplete last block
        byte[] arrDk = new byte[dkLen];
        System.arraycopy(arrByt, 0, arrDk, 0, dkLen);
        return arrDk;
      }
      return arrByt;
    }

    private static void f(byte[] dest, int offset, Mac prf, byte[] arrS, int c, int blockIndex) {
      final int hLen = prf.getMacLength();
      byte[] arrU = new byte[hLen];
      byte[] arrI = new byte[arrS.length + 4];
      System.arraycopy(arrS, 0, arrI, 0, arrS.length);
      intFunction(arrI, arrS.length, blockIndex);
      for (int i = 0; i < c; i++) {
        arrI = prf.doFinal(arrI);
        xor(arrU, arrI);
      }

      System.arraycopy(arrU, 0, dest, offset, hLen);
    }

    private static void xor(byte[] dest, byte[] src) {
      for (int i = 0; i < dest.length; i++) {
        dest[i] ^= src[i];
      }
    }

    private static void intFunction(byte[] dest, int offset, int i) {
      dest[offset] = (byte) (i / (256 * 256 * 256));
      dest[offset + 1] = (byte) (i / (256 * 256));
      dest[offset + 2] = (byte) (i / (256));
      dest[offset + 3] = (byte) (i);
    }

    // Costructor
    private PBKDF2() {
    }
  }

  /**
   * HASH
   */
  public static class HASH {

    private HASH(){}

    /**
     * Create a HASH with algorithm
     *
     * @param message Message to hash
     * @param algorithm algorithm to use
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String hash(String message, String algorithm, String salt, String encoding) throws AWException {
      try {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

        // Add salt if defined
        if (salt != null) {
          messageDigest.digest(salt.getBytes(encoding));
        }

        // Put the text to hash
        messageDigest.update(message.getBytes(encoding));

        // Encode to hex
        return Utils.encodeHex(messageDigest.digest());

      } catch (NoSuchAlgorithmException exc) {
        throw new AWException("Hash generation error", "The algorithm does not exist", exc);
      } catch (UnsupportedEncodingException exc) {
        throw new AWException("Hash generation error", "The encoding is not supported: " + encoding, exc);
      }
    }

    /**
     * Create a HASH with md2Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String md2Hash(String message) throws AWException {
      return hash(message, "MD2", null, AweConstants.APPLICATION_ENCODING);
    }

    /**
     * Create a HASH with md5Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String md5Hash(String message) throws AWException {
      return hash(message, "MD5", null, AweConstants.APPLICATION_ENCODING);
    }

    /**
     * Create a HASH with sha1Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String sha1Hash(String message) throws AWException {
      return hash(message, "SHA-1", null, AweConstants.APPLICATION_ENCODING);
    }

    /**
     * Create a HASH with sha256Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String sha256Hash(String message) throws AWException {
      return hash(message, "SHA-256", null, AweConstants.APPLICATION_ENCODING);
    }

    /**
     * Create a HASH with sha384Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String sha384Hash(String message) throws AWException {
      return hash(message, "SHA-348", null, AweConstants.APPLICATION_ENCODING);
    }

    /**
     * Create a HASH with sha512Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws AWException No such algorithm
     */
    public static String sha512Hash(String message) throws AWException {
      return hash(message, "SHA-512", null, AweConstants.APPLICATION_ENCODING);
    }
  }

  /**
   * RSA
   */
  public static class RSA {

    private static final int KEY_SIZE_BITS = 1024;
    private Key publicKey;
    private Key privateKey;
    private BigInteger modulus;
    private BigInteger exponent;
    Cipher cipher;
    KeyFactory fact;

    /**
     * Constructor
     *
     * @throws NoSuchAlgorithmException No such algorithm
     * @throws InvalidKeySpecException Invalid key
     * @throws NoSuchPaddingException No such padding
     */
    public RSA() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException {
      cipher = Cipher.getInstance("RSA");
      fact = KeyFactory.getInstance("RSA");
      setNewKey(KEY_SIZE_BITS);
    }

    /**
     * Store new key
     *
     * @param keySize Key size
     * @return Key is stored
     * @throws NoSuchAlgorithmException No such algorithm
     * @throws InvalidKeySpecException Invalid key
     */
    private boolean setNewKey(int keySize) throws NoSuchAlgorithmException, InvalidKeySpecException {
      if (keySize <= 0) {
        return false;
      }
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      kpg.initialize(keySize);
      KeyPair kp = kpg.genKeyPair();
      publicKey = kp.getPublic();
      privateKey = kp.getPrivate();

      RSAPublicKeySpec pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
      modulus = pub.getModulus();
      exponent = pub.getPublicExponent();

      return true;
    }

    /**
     * Get modulus
     *
     * @return Modulus
     */
    public BigInteger getModulus() {
      return modulus;
    }

    /**
     * Get exponent
     *
     * @return Exponent
     */
    public BigInteger getExponent() {
      return exponent;
    }

    /**
     * Get public key
     *
     * @return Public key
     */
    public Key getPublicKey() {
      return publicKey;
    }

    /**
     * Get private key
     *
     * @return Private key
     */
    public Key getPrivateKey() {
      return privateKey;
    }

    /**
     * Encrypt text
     *
     * @param plaintext Text to encrypt
     * @return encrypted text
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public String encrypt(String plaintext) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
      if (plaintext.length() == 0) {
        return null;
      }
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      byte[] encrypted = cipher.doFinal(plaintext.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypt text
     *
     * @param ciphertext Encrypted text
     * @param encoding
     * @return Decrypted text
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws InvalidKeyException
     */
    public String decrypt(String ciphertext, String encoding) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
      if (ciphertext.length() == 0) {
        return null;
      }
      byte[] dec = Base64.getDecoder().decode(ciphertext);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] decrypted = cipher.doFinal(dec);
      Charset charset = Charset.forName(encoding);
      return new String(decrypted, charset);
    }
  }
}