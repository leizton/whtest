package com.wh.test.util.java.detail;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * 2018/10/21
 */
public class UrlAndBase64EncodeTest {

  public static void main(String[] args) {
    String s = testEncode();
    testDecode(s);
  }

  private static String testEncode() {
    String s = "";
    s = base64Encode(s);
    System.out.println(s);
    s = urlEncode(s);
    System.out.println(s);
    return s;
  }

  private static void testDecode(String s) {
    s = urlDecode(s);
    System.out.println(s);
    s = base64Decode(s);
    System.out.println(s);
  }

  @SuppressWarnings("deprecation")
  private static String urlEncode(String s) {
    return URLEncoder.encode(s);
  }

  @SuppressWarnings("deprecation")
  private static String urlDecode(String s) {
    return URLDecoder.decode(s);
  }

  private static String base64Encode(String s) {
    return Base64.getEncoder().encodeToString(s.getBytes());
  }

  private static String base64Decode(String s) {
    return new String(Base64.getDecoder().decode(s), Charset.forName("utf-8"));
  }
}
