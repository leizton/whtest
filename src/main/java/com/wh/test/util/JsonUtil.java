package com.wh.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 2018/4/25
 */
public class JsonUtil {
  private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

  private final static ObjectMapper MAPPER = new ObjectMapper();

  public static <T> Optional<T> parse(String json, Class<T> clz) {
    try {
      return Optional.of(MAPPER.readValue(json, clz));
    } catch (Exception e) {
      LOG.error("parse {} exception: {}", clz.getCanonicalName(), json, e);
      return Optional.empty();
    }
  }

  public static <T> Optional<T> parse(byte[] json, Class<T> clz) {
    try {
      return Optional.of(MAPPER.readValue(json, clz));
    } catch (Exception e) {
      LOG.error("parse {} exception: {}", clz.getCanonicalName(), new String(json), e);
      return Optional.empty();
    }
  }
}
