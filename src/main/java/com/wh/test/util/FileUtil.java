package com.wh.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 2018/5/6
 */
public class FileUtil {
  private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

  public static void processEachLine(String filename, Function<String, Boolean> proc) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if (!proc.apply(line)) {
          break;
        }
      }
    }
  }

  public static BiConsumer<String, Boolean> createWriter(String filename) throws IOException {
    BufferedWriter rawWriter = new BufferedWriter(new FileWriter(filename));
    return (line, writeCompleted) -> {
      try {
        rawWriter.write(line);
        rawWriter.newLine();
        if (writeCompleted) {
          rawWriter.close();
        }
      } catch (IOException e) {
        LOG.error("write exception. file={}", filename, e);
      }
    };
  }
}
