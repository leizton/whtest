package com.wh.test.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * 2018/5/6
 */
public class FileUtil {
  private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

  public static void processEachLine(String filename, BiFunction<Integer, String, Boolean> proc) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      int lineNum = 0;
      for (String line; (line = reader.readLine()) != null; lineNum++) {
        if (!proc.apply(lineNum, line)) {
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
