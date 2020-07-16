package com.wh.test.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 2020/5/19
 */
public class CryptUtil {

  private static SecretKey buildSecretKey(byte[] keyText) throws NoSuchAlgorithmException {
    SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
    rand.setSeed(keyText);
    KeyGenerator keygen = KeyGenerator.getInstance("AES");
    keygen.init(128, rand);
    SecretKey key = keygen.generateKey();
    byte[] rawKey = key.getEncoded();
    return new SecretKeySpec(rawKey, "AES");
  }

  public static byte[] encryptAES(byte[] key, byte[] text) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, buildSecretKey(key));
    return cipher.doFinal(text);
  }

  public static byte[] decryptAES(byte[] key, byte[] crypted) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, buildSecretKey(key));
    return cipher.doFinal(crypted);
  }
}
