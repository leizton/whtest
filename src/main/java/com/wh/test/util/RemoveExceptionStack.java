package com.wh.test.util;

import com.google.common.collect.Lists;
import com.wh.test.util.SystemEnv;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 2018/4/28
 */
public class RemoveExceptionStack {

  private static final Pattern matchAt = Pattern.compile("^\\s+at .*");
  private static final Pattern matchCauseby = Pattern.compile("^\\s*Caused by: .*");
  private static final Pattern matchSuppressed = Pattern.compile("^\\s*Suppressed: .*");
  private static final Pattern matchExStackCompress = Pattern.compile("^\\s*\\.\\.\\. \\d+ more");
  private static final List<Pattern> exStackPatterns = Lists.newArrayList(matchAt, matchCauseby, matchSuppressed, matchExStackCompress);

  private static final boolean isSaveExMessage = true;
  private static final String exceptionStart = "java.io.IOException: ";

  public static void main(String[] args) {
    run(SystemEnv.getUserHomeDir() + "/tmp/py/data/log6", SystemEnv.getUserHomeDir() + "/tmp/py/data/log7");
  }

  private static void run(String inPath, String outPath) {
    try (
        BufferedReader reader = new BufferedReader(new FileReader(new File(inPath)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outPath)))
    ) {
      boolean exStarted = false;
      String line;
      while ((line = reader.readLine()) != null) {
        if (match(line, exStarted)) {
          if (!exStarted && isSaveExMessage) {
            writer.write(line);
            writer.newLine();
          }
          exStarted = true;
        } else {
          exStarted = false;
          writer.write(line);
          writer.newLine();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static boolean match(String line, boolean exStarted) {
    if (exStarted) {
      return exStackPatterns.stream().anyMatch(pattern -> pattern.matcher(line).matches());
    } else {
      return line.startsWith(exceptionStart);
    }
  }
}
