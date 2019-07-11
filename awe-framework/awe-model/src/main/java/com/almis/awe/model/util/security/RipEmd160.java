package com.almis.awe.model.util.security;

import com.almis.awe.exception.AWException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Encoder in RipEmd160
 *
 * @author pgarcia
 */
public final class RipEmd160 {

  private static final int[][] ARG_ARRAY = {
    {11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8, 7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12, 11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5, 11, 12, 14, 15, 14, 15,
      9, 8, 9, 14, 5, 6, 8, 6, 5, 12, 9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6},
    {8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6, 9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11, 9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5, 15, 5, 8, 11, 14, 14,
      6, 14, 6, 9, 12, 9, 12, 5, 15, 8, 8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11}};
  private static final int[][] INDEX_ARRAY = {
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8, 3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12, 1, 9, 11, 10, 0, 8, 12, 4,
      13, 3, 7, 15, 14, 5, 6, 2, 4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13},
    {5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12, 6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2, 15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13, 8, 6, 4, 1, 3, 11, 15, 0, 5,
      12, 2, 13, 9, 7, 10, 14, 12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11}};
  public static final String PBE_WITH_MD5_AND_DESCBCPKCS5_PADDING = "PBEWithMD5AndDES/CBC/PKCS5Padding";
  private int[] mdBuf;
  private Cipher encrypter;
  private Cipher decrypter;
  private static byte[] salt = {(byte) 0x13, (byte) 0x20, (byte) 0x12, (byte) 0x08, (byte) 0x11, (byte) 0x67, (byte) 0x66, (byte) 0x34};
  private int[] working;
  private int workingPtr;
  private int msglen;

  private static final int C2 = 2;
  private static final int C3 = 3;
  private static final int C4 = 4;
  private static final int C10 = 10;
  private static final int C20 = 20;
  private static final int C22 = 22;
  private static final int C16 = 16;
  private static final int C32 = 32;
  private static final int C48 = 48;
  private static final int C64 = 64;

  /**
   * Constructs a EncodeUtil object for Password Hash
   */
  public RipEmd160() {
    reset();
  }

  /**
   * Constructs a Encode util object for encryption
   *
   * @param password Master password
   * @throws AWException AWE exception
   */
  public RipEmd160(String password) throws AWException {

    try {
      PBEParameterSpec ps = new PBEParameterSpec(salt, C20);
      SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(password.toCharArray()));
      encrypter = Cipher.getInstance(PBE_WITH_MD5_AND_DESCBCPKCS5_PADDING);
      decrypter = Cipher.getInstance(PBE_WITH_MD5_AND_DESCBCPKCS5_PADDING);
      encrypter.init(Cipher.ENCRYPT_MODE, k, ps);
      decrypter.init(Cipher.DECRYPT_MODE, k, ps);
    } catch (Exception exc) {
      throw new AWException("RipEmd160 initialization error", "There was an error trying to initialize RipEmd160", exc);
    }
  }

  /**
   * Reset variables
   */
  private void reset() {
    mdBuf = new int[]{0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0};
    working = new int[C16];
    workingPtr = 0;
    msglen = 0;
  }

  /**
   * Internal util for compress
   *
   * @param x int array
   */
  private void compress(int[] x) {
    int index = 0;

    int a;
    int b;
    int c;
    int d;
    int e;
    int aBis;
    int bBis;
    int cBis;
    int dBis;
    int eBis;
    int temp;
    int s;

    aBis = a = mdBuf[0];
    bBis = b = mdBuf[1];
    cBis = c = mdBuf[C2];
    dBis = d = mdBuf[C3];
    eBis = e = mdBuf[C4];

    for (; index < C16; index++) {
      // The 16 FF functions - round 1 */
      temp = a + (b ^ c ^ d) + x[INDEX_ARRAY[0][index]];
      a = e;
      e = d;
      d = (c << C10) | (c >>> C22);
      c = b;
      s = ARG_ARRAY[0][index];
      b = ((temp << s) | (temp >>> (C32 - s))) + a;

      // The 16 JJJ functions - parallel round 1 */
      temp = aBis + (bBis ^ (cBis | ~dBis)) + x[INDEX_ARRAY[1][index]] + 0x50a28be6;
      aBis = eBis;
      eBis = dBis;
      dBis = (cBis << C10) | (cBis >>> C22);
      cBis = bBis;
      s = ARG_ARRAY[1][index];
      bBis = ((temp << s) | (temp >>> (C32 - s))) + aBis;
    }

    for (; index < C32; index++) {
      // The 16 GG functions - round 2 */
      temp = a + ((b & c) | (~b & d)) + x[INDEX_ARRAY[0][index]] + 0x5a827999;
      a = e;
      e = d;
      d = (c << C10) | (c >>> C22);
      c = b;
      s = ARG_ARRAY[0][index];
      b = ((temp << s) | (temp >>> (C32 - s))) + a;

      // The 16 III functions - parallel round 2 */
      temp = aBis + ((bBis & dBis) | (cBis & ~dBis)) + x[INDEX_ARRAY[1][index]] + 0x5c4dd124;
      aBis = eBis;
      eBis = dBis;
      dBis = (cBis << C10) | (cBis >>> C22);
      cBis = bBis;
      s = ARG_ARRAY[1][index];
      bBis = ((temp << s) | (temp >>> (C32 - s))) + aBis;
    }

    for (; index < C48; index++) {
      // The 16 HH functions - round 3 */
      temp = a + ((b | ~c) ^ d) + x[INDEX_ARRAY[0][index]] + 0x6ed9eba1;
      a = e;
      e = d;
      d = (c << C10) | (c >>> C22);
      c = b;
      s = ARG_ARRAY[0][index];
      b = ((temp << s) | (temp >>> (C32 - s))) + a;

      // The 16 HHH functions - parallel round 3 */
      temp = aBis + ((bBis | ~cBis) ^ dBis) + x[INDEX_ARRAY[1][index]] + 0x6d703ef3;
      aBis = eBis;
      eBis = dBis;
      dBis = (cBis << C10) | (cBis >>> C22);
      cBis = bBis;
      s = ARG_ARRAY[1][index];
      bBis = ((temp << s) | (temp >>> (C32 - s))) + aBis;
    }

    for (; index < C64; index++) {
      // The 16 II functions - round 4 */
      temp = a + ((b & d) | (c & ~d)) + x[INDEX_ARRAY[0][index]] + 0x8f1bbcdc;
      a = e;
      e = d;
      d = (c << C10) | (c >>> C22);
      c = b;
      s = ARG_ARRAY[0][index];
      b = ((temp << s) | (temp >>> (C32 - s))) + a;

      // The 16 GGG functions - parallel round 4 */
      temp = aBis + ((bBis & cBis) | (~bBis & dBis)) + x[INDEX_ARRAY[1][index]] + 0x7a6d76e9;
      aBis = eBis;
      eBis = dBis;
      dBis = (cBis << C10) | (cBis >>> C22);
      cBis = bBis;
      s = ARG_ARRAY[1][index];
      bBis = ((temp << s) | (temp >>> (C32 - s))) + aBis;
    }

    for (; index < 80; index++) {
      // The 16 JJ functions - round 5 */
      temp = a + (b ^ (c | ~d)) + x[INDEX_ARRAY[0][index]] + 0xa953fd4e;
      a = e;
      e = d;
      d = (c << C10) | (c >>> C22);
      c = b;
      s = ARG_ARRAY[0][index];
      b = ((temp << s) | (temp >>> (C32 - s))) + a;

      // The 16 FFF functions - parallel round 5 */
      temp = aBis + (bBis ^ cBis ^ dBis) + x[INDEX_ARRAY[1][index]];
      aBis = eBis;
      eBis = dBis;
      dBis = (cBis << C10) | (cBis >>> C22);
      cBis = bBis;
      s = ARG_ARRAY[1][index];
      bBis = ((temp << s) | (temp >>> (C32 - s))) + aBis;
    }

    /* combine results */
    /* final result for MDbuf[0] */
    dBis += c + mdBuf[1];
    mdBuf[1] = mdBuf[C2] + d + eBis;
    mdBuf[C2] = mdBuf[C3] + e + aBis;
    mdBuf[C3] = mdBuf[C4] + a + bBis;
    mdBuf[C4] = mdBuf[0] + b + cBis;
    mdBuf[0] = dBis;
  }

  /**
   * MD finish
   *
   * @param array
   * @param lswlen
   * @param mswlen
   */
  private void mdFinish(int[] array, int lswlen, int mswlen) {
    /* message words */
    int[] arrX = array;

    /* append the bit m_n == 1 */
    arrX[(lswlen >> C2) & 15] ^= 1 << (((lswlen & C3) << C3) + 7);

    if ((lswlen & 63) > 55) {
      /* length goes to next block */
      compress(arrX);
      for (int i = 0; i < 14; i++) {
        arrX[i] = 0;
      }
    }

    /* append length in bits */
    arrX[14] = lswlen << C3;
    arrX[15] = (lswlen >> 29) | (mswlen << C3);
    compress(arrX);
  }

  /**
   * Internal util for update input
   *
   * @param input
   */
  private void update(byte[] input) {
    for (int i = 0; i < input.length; i++) {
      working[workingPtr >> C2] ^= ((int) input[i]) << ((workingPtr & C3) << C3);
      workingPtr++;
      if (workingPtr == C64) {
        compress(working);
        for (int j = 0; j < C16; j++) {
          working[j] = 0;
        }
        workingPtr = 0;
      }
    }
    msglen += input.length;
  }

  /**
   * Internal util for update string
   *
   * @param s
   */
  private void update(String s) {
    byte[] bytearray = new byte[s.length()];
    for (int i = 0; i < bytearray.length; i++) {
      bytearray[i] = (byte) s.charAt(i);
    }
    update(bytearray);
  }

  /**
   * Create digest
   *
   * @return digest
   */
  private byte[] digest() {
    mdFinish(working, msglen, 0);
    byte[] res = new byte[C20];
    for (int i = 0; i < C20; i++) {
      res[i] = (byte) ((mdBuf[i >> C2] >>> ((i & C3) << C3)) & 0x000000FF);
    }
    return res;
  }

  /**
   * Decrypts a string
   *
   * @param text     String to be decrypted
   * @param encoding Text encoding
   * @return String decrypted
   * @throws AWException AWE exception
   */
  public String decrypt(String text, String encoding) throws AWException {

    byte[] out;
    byte[] dec;
    String strDecrypt;
    try {
      dec = new Base64().decode(text.getBytes());
      out = decrypter.doFinal(dec);
      strDecrypt = new String(out, encoding);

    } catch (Exception exc) {
      throw new AWException("String decrypt error", "There was an error trying to decrypt the string: " + text, exc);
    }
    return strDecrypt;
  }

  /**
   * Encrypts a string
   *
   * @param text     String to be decrypted
   * @param encoding Text encoding
   * @return String encrypted
   * @throws AWException AWE exception
   */
  public String encrypt(String text, String encoding) throws AWException {

    byte[] ini;
    byte[] enc;

    try {
      ini = text.getBytes(encoding);
      enc = encrypter.doFinal(ini);
    } catch (Exception exc) {
      throw new AWException("String encrypt error", "There was an error trying to encrypt the string: " + text, exc);
    }
    return new String(new Base64().encode(enc));
  }

  /**
   * Encodes a text in RIPEMD-160
   *
   * @param text Text to encode
   * @return Text encoded
   */
  public String encodeRipEmd160(String text) {

    /* Variable definition */
    byte[] outByt;
    int outItr;
    StringBuilder stringBuilder = new StringBuilder();

    /* Insert text to encode */
    this.update(text);

    /* Get encoded output */
    outByt = this.digest();
    for (outItr = 0; outItr < outByt.length; outItr++) {
      String outChr = Integer.toHexString(outByt[outItr]);
      outChr = outChr.length() < C2 ? "0" + outChr : outChr;
      outChr = outChr.length() > C2 ? outChr.substring(outChr.length() - C2) : outChr;
      outChr = "00".equals(outChr) ? "1j" : outChr;
      stringBuilder.append(outChr);
    }

    /* Transform encoded bytemap to string and return it */
    return stringBuilder.toString();
  }
}
