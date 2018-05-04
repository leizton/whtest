package com.wh.test.util;

import java.util.Map;
import java.util.Properties;

/**
 * 2018/5/4
 */
public class PropertiesUtil {

  public static Properties fromMap(Map<String, String> map) {
    Properties prop = new Properties();
    prop.putAll(map);
    return prop;
  }
}
