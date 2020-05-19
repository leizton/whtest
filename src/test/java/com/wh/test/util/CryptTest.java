package com.wh.test.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 2020/5/19
 */
public class CryptTest {

  @Test
  public void test() throws Exception {
    byte[] text = "abcd1234".getBytes();
    byte[] key = "crypt_data".getBytes();

    byte[] crypted = CryptUtil.encryptAES(key, text);
    int diff = 0;
    for (int i = Math.min(text.length, crypted.length) - 1; i >= 0; i--) {
      if (text[i] != crypted[i]) {
        diff++;
      }
    }
    System.out.println("diff: " + diff);

    byte[] decrypted = CryptUtil.decryptAES(key, crypted);
    Assert.assertEquals(text.length, decrypted.length);
    for (int i = 0; i < text.length; i++) {
      Assert.assertEquals(text[i], decrypted[i]);
    }
  }
}
