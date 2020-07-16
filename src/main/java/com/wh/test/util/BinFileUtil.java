package com.wh.test.util;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;

import java.io.IOException;

/**
 * 2020/7/16
 */
public class BinFileUtil {

  public static int[] diff(final String xFile, final String yFile, final int blockSize) throws IOException {
    if (blockSize <= 0) {
      throw new IllegalArgumentException("blockSize <= 0");
    }
    var xBytes = FileUtil.readFile(xFile);
    var yBytes = FileUtil.readFile(yFile);
    final int xBlockNum = (int) Math.ceil(xBytes.length / (double) blockSize);
    final int yBlockNum = (int) Math.ceil(yBytes.length / (double) blockSize);
    final int commonNum = Math.min(xBytes.length, yBytes.length) / blockSize;
    var ret = Lists.newArrayList(xBlockNum, yBlockNum, commonNum);
    int off = 0;
    for (int i = 0; i < commonNum; i++) {
      for (int j = off + blockSize - 1; j >= off; j--) {
        if (xBytes[j] != yBytes[j]) {
          ret.add(i);
          break;
        }
      }
      off += blockSize;
    }
    return Ints.toArray(ret);
  }
}
