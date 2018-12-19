package com.almis.awe.model.util.security;

import com.almis.awe.model.constant.AweConstants;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

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
    private SecureRandom random;
    private Base64Encoder base64Encoder;

    private static final Logger logger = LogManager.getLogger(AES.class);

    /**
     * Constructor
     */
    AES() {
      initializeAES();
    }

    /**
     * Initialize AES showing errors if failed
     */
    private void initializeAES() {
      try {
        base64Encoder = new Base64Encoder();
        random = SecureRandom.getInstanceStrong();
      } catch (NoSuchAlgorithmException exc) {
        logger.error("Error initializing secure random", exc);
      }
    }

    /**
     * Generate IV
     * @return IV
     */
    private byte[] generateIV() {
      byte[] ivGen = new byte[GCM_IV_LENGTH];
      random.nextBytes(ivGen);
      return ivGen;
    }

    /**
     * Generate IV
     * @return IV
     */
    private Cipher generateCipher(byte[] iv, String passphrase, int mode) {
      try {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        GCMParameterSpec ivSpec = new GCMParameterSpec(GCM_TAG_LENGTH * Byte.SIZE, iv);
        cipher.init(mode, getSecretKey(passphrase, iv), ivSpec);
        return cipher;
      } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | InvalidKeyException exc) {
        logger.error("Error initializing cipher: {0} - {1}", CIPHER_TRANSFORMATION, passphrase, exc);
      }
      return null;
    }

    /**
     * Get Secret key from key
     * @param key Key
     * @return Secret key spec
     */
    private SecretKeySpec getSecretKey(String key, byte[] iv) {
      try {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        SecretKey derivedKey = factory.generateSecret(new PBEKeySpec(key.toCharArray(), iv, 1000, AES_KEY_SIZE));
        return new SecretKeySpec(derivedKey.getEncoded(), ALGORITHM);
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
    public String encrypt(String plaintext, String passphrase, String encoding) {
      if (plaintext.length() == 0) {
        return null;
      }

      try {
        byte[] iv = generateIV();
        Cipher cipher = generateCipher(iv, passphrase, Cipher.ENCRYPT_MODE);

        if (cipher != null) {
          byte[] ciphertext = cipher.doFinal(plaintext.getBytes(encoding));
          byte[] encrypted = new byte[iv.length + ciphertext.length];
          System.arraycopy(iv, 0, encrypted, 0, iv.length);
          System.arraycopy(ciphertext, 0, encrypted, iv.length, ciphertext.length);
          return base64Encoder.encode(encrypted);
        }
      } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException exc) {
        logger.error("Error encoding string {0}", plaintext, exc);
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
    public String decrypt(String encrypted, String passphrase, String encoding) {
      if (encrypted.length() == 0) {
        return null;
      }

      try {
        byte[] decoded = base64Encoder.decode(encrypted);
        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
        Cipher cipher = generateCipher(iv, passphrase, Cipher.DECRYPT_MODE);
        if (cipher != null) {
          byte[] ciphertext = cipher.doFinal(decoded, GCM_IV_LENGTH, decoded.length - GCM_IV_LENGTH);
          return new String(ciphertext, encoding);
        }
      } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException exc) {
        logger.error("Error decoding string {0}", encrypted, exc);
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

    /**
     * Create a HASH with md2Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String md2Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest md2 = MessageDigest.getInstance("MD2");
      byte[] array = md2.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Create a HASH with md5Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String md5Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] array = md5.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Create a HASH with sha1Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String sha1Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
      byte[] array = sha1.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Create a HASH with sha256Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String sha256Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
      byte[] array = sha256.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Create a HASH with sha384Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String sha384Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest sha384 = MessageDigest.getInstance("SHA-384");
      byte[] array = sha384.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Create a HASH with sha512Hash
     *
     * @param message Message to hash
     * @return Hash
     * @throws NoSuchAlgorithmException No such algorithm
     */
    public String sha512Hash(String message) throws NoSuchAlgorithmException {
      MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
      byte[] array = sha512.digest(message.getBytes());
      return arrayToString(array);
    }

    /**
     * Copy byte array to a string
     *
     * @param array Array
     * @return Array converted
     */
    private String arrayToString(byte[] array) {
      StringBuilder sb = new StringBuilder();
      for (int i : array) {
        sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
      }
      return sb.toString();
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
      return Utils.byteArrayToBase64String(encrypted);
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
      byte[] dec = Utils.base64StringToByteArray(ciphertext);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      byte[] decrypted = cipher.doFinal(dec);
      Charset charset = Charset.forName(encoding);
      return new String(decrypted, charset);
    }
  }

  /**
   * Utils
   */
  public static class Utils {

    /**
     * Private constuctor of Utils class
     */
    private Utils() {
    }

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
      return new String(key);
    }

    /**
     * Retrieve random bytes
     *
     * @param len Length
     * @return random bytes
     */
    public static byte[] getRandomBytes(int len) throws NoSuchAlgorithmException {

      int lenMod = len;
      byte[] aesKey;

      if (lenMod < 0) {
        lenMod = 8;
      }
      Random random = SecureRandom.getInstance(AweConstants.RANDOM_ALGORITHM);

      aesKey = new byte[lenMod];
      random.nextBytes(aesKey);
      return aesKey;
    }

    /**
     * Copy byte array to hex string
     *
     * @param raw byte array
     * @return Hex string
     */
    public static String byteArrayToHexString(byte[] raw) {
      StringBuilder sb = new StringBuilder(2 + raw.length * 2);
      sb.append("0x");
      for (int i = 0; i < raw.length; i++) {
        sb.append(String.format("%02X", Integer.valueOf(raw[i] & 0xFF)));
      }
      return sb.toString();
    }

    /**
     * Copy hex string to byte array
     *
     * @param hex Hex string
     * @return Byte array
     */
    public static byte[] hexStringToByteArray(String hex) {
      Pattern replace = Pattern.compile("^0x");
      String s = replace.matcher(hex).replaceAll("");

      byte[] b = new byte[s.length() / 2];
      for (int i = 0; i < b.length; i++) {
        int index = i * 2;
        int v = Integer.parseInt(s.substring(index, index + 2), 16);
        b[i] = (byte) v;
      }
      return b;
    }

    /**
     * Copy byte array to base64 string
     *
     * @param raw Byte array
     * @return Base64 string
     */
    public static String byteArrayToBase64String(byte[] raw) {
      return new String(Base64Coder.encode(raw));
    }

    /**
     * Copy base64 string to byte array
     *
     * @param str Base64 string
     * @return Byte array
     */
    public static byte[] base64StringToByteArray(String str) {
      return Base64Coder.decode(str);
    }

    /**
     * Encode with base64
     *
     * @param str String
     * @return base64 string
     */
    public static String base64Encode(String str) {
      return Base64Coder.encodeString(str);
    }

    /**
     * Decode from base64
     *
     * @param str Base64 string
     * @return String decoded
     */
    public static String base64Decode(String str) {
      return Base64Coder.decodeString(str);
    }
  }

  /**
   * Base64Coder
   */
  private static final class Base64Coder {
    // The line separator string of the operating system.

    private static final String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");
    // Mapping table from 6-bit nibbles to Base64 characters.
    static final char[] MAP1 = new char[64];

    static {
      int i = 0;
      for (char c = 'A'; c <= 'Z'; c++) {
        MAP1[i++] = c;
      }
      for (char c = 'a'; c <= 'z'; c++) {
        MAP1[i++] = c;
      }
      for (char c = '0'; c <= '9'; c++) {
        MAP1[i++] = c;
      }
      MAP1[i++] = '+';
      MAP1[i++] = '/';
    }

    // Mapping table from Base64 characters to 6-bit nibbles.
    static final byte[] MAP2 = new byte[128];

    static {
      for (int i = 0; i < MAP2.length; i++) {
        MAP2[i] = -1;
      }
      for (int i = 0; i < 64; i++) {
        MAP2[MAP1[i]] = (byte) i;
      }
    }

    /**
     * Encodes a string into Base64 format. No blanks or line breaks are inserted.
     *
     * @param s A String to be encoded.
     * @return A String containing the Base64 encoded data.
     */
    private static String encodeString(String s) {
      return new String(encode(s.getBytes()));
    }

    /**
     * Encodes a byte array into Base 64 format and breaks the output into lines of 76 characters. This method is compatible with <code>sun.misc.BASE64Encoder.encodeBuffer(byte[])</code>.
     *
     * @param in An array containing the data bytes to be encoded.
     * @return A String containing the Base64 encoded data, broken into lines.
     */
    public static String encodeLines(byte[] in) {
      return encodeLines(in, 0, in.length, 76, SYSTEM_LINE_SEPARATOR);
    }

    /**
     * Encodes a byte array into Base 64 format and breaks the output into lines.
     *
     * @param in            An array containing the data bytes to be encoded.
     * @param iOff          Offset of the first byte in <code>in</code> to be processed.
     * @param iLen          Number of bytes to be processed in <code>in</code>, starting at <code>iOff</code>.
     * @param lineLen       Line length for the output data. Should be a multiple of 4.
     * @param lineSeparator The line separator to be used to separate the output lines.
     * @return A String containing the Base64 encoded data, broken into lines.
     */
    private static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
      int blockLen = (lineLen * 3) / 4;
      if (blockLen <= 0) {
        throw new IllegalArgumentException();
      }
      int lines = (iLen + blockLen - 1) / blockLen;
      int bufLen = ((iLen + 2) / 3) * 4 + lines * lineSeparator.length();
      StringBuilder buf = new StringBuilder(bufLen);
      int ip = 0;
      while (ip < iLen) {
        int l = Math.min(iLen - ip, blockLen);
        buf.append(encode(in, iOff + ip, l));
        buf.append(lineSeparator);
        ip += l;
      }
      return buf.toString();
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
     *
     * @param in An array containing the data bytes to be encoded.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in) {
      return encode(in, 0, in.length);
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
     *
     * @param in   An array containing the data bytes to be encoded.
     * @param iLen Number of bytes to process in <code>in</code>.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in, int iLen) {
      return encode(in, 0, iLen);
    }

    /**
     * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
     *
     * @param in   An array containing the data bytes to be encoded.
     * @param iOff Offset of the first byte in <code>in</code> to be processed.
     * @param iLen Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.
     * @return A character array containing the Base64 encoded data.
     */
    public static char[] encode(byte[] in, int iOff, int iLen) {
      // output length without padding
      int oDataLen = (iLen * 4 + 2) / 3;
      // output length including padding
      int oLen = ((iLen + 2) / 3) * 4;
      char[] out = new char[oLen];
      int ip = iOff;
      int iEnd = iOff + iLen;
      int op = 0;
      while (ip < iEnd) {
        int i0 = in[ip++] & 0xff;
        int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
        int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
        int o0 = i0 >>> 2;
        int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
        int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
        int o3 = i2 & 0x3F;
        out[op++] = MAP1[o0];
        out[op++] = MAP1[o1];
        out[op] = op < oDataLen ? MAP1[o2] : '=';
        op++;
        out[op] = op < oDataLen ? MAP1[o3] : '=';
        op++;
      }
      return out;
    }

    /**
     * Decodes a string from Base64 format. No blanks or line breaks are allowed within the Base64 encoded input data.
     *
     * @param s A Base64 String to be decoded.
     * @return A String containing the decoded data.
     * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
     */
    private static String decodeString(String s) {
      return new String(decode(s));
    }

    /**
     * Decodes a byte array from Base64 format and ignores line separators, tabs and blanks. CR, LF, Tab and Space characters are ignored in the input data. This method is compatible with
     * <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.
     *
     * @param s A Base64 String to be decoded.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
     */
    public static byte[] decodeLines(String s) {
      char[] buf = new char[s.length()];
      int p = 0;
      for (int ip = 0; ip < s.length(); ip++) {
        char c = s.charAt(ip);
        if (c != ' ' && c != '\r' && c != '\n' && c != '\t') {
          buf[p++] = c;
        }
      }
      return decode(buf, 0, p);
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64 encoded input data.
     *
     * @param s A Base64 String to be decoded.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(String s) {
      return decode(s.toCharArray());
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64 encoded input data.
     *
     * @param in A character array containing the Base64 encoded data.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(char[] in) {
      return decode(in, 0, in.length);
    }

    /**
     * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64 encoded input data.
     *
     * @param in   A character array containing the Base64 encoded data.
     * @param iOff Offset of the first character in <code>in</code> to be processed.
     * @param iLen Number of characters to process in <code>in</code>, starting at <code>iOff</code>.
     * @return An array containing the decoded data bytes.
     * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
     */
    public static byte[] decode(char[] in, int iOff, int iLen) {

      int numChar = iLen;
      if (numChar % 4 != 0) {
        throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
      }
      while (numChar > 0 && in[iOff + numChar - 1] == '=') {
        numChar--;
      }
      int oLen = (numChar * 3) / 4;
      byte[] out = new byte[oLen];
      int ip = iOff;
      int iEnd = iOff + numChar;
      int op = 0;
      List<Integer> output = new ArrayList<>();
      while (ip < iEnd) {
        ip = processInput(in, ip, iEnd, output);
        op = processOutput(out, op, oLen, output);
        output.clear();
      }
      return out;
    }

    /**
     * Process decode input
     * @param in Data
     * @param ip Index
     * @param iEnd Length
     * @param output Output variables
     * @return Input index
     */
    private static int processInput(char[] in, int ip, int iEnd, List<Integer> output) {
      String errorText = "Illegal character in Base64 encoded data.";
      int index = ip;
      int i0 = in[index++];
      int i1 = in[index++];
      int i2 = index < iEnd ? in[index++] : 'A';
      int i3 = index < iEnd ? in[index++] : 'A';
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127) {
        throw new IllegalArgumentException(errorText);
      }
      int b0 = MAP2[i0];
      int b1 = MAP2[i1];
      int b2 = MAP2[i2];
      int b3 = MAP2[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0) {
        throw new IllegalArgumentException(errorText);
      }
      output.add((b0 << 2) | (b1 >>> 4));
      output.add(((b1 & 0xf) << 4) | (b2 >>> 2));
      output.add(((b2 & 3) << 6) | b3);
      return index;
    }

    /**
     * Process decode output
     * @param out Data
     * @param op Index
     * @param oLen Length
     * @param output Output variables
     * @return Output index
     */
    private static int processOutput(byte[] out, int op, int oLen, List<Integer> output) {
      int index = op;
      int o0 = output.get(0);
      int o1 = output.get(1);
      int o2 = output.get(2);
      out[index++] = (byte) o0;
      if (index < oLen) {
        out[index++] = (byte) o1;
      }
      if (index < oLen) {
        out[index++] = (byte) o2;
      }
      return index;
    }

    // Dummy constructor.
    private Base64Coder() {
    }
  }
}