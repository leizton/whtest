package com.wh.test.util.java;

import com.wh.test.util.BinFileUtil;

import java.util.Arrays;

/**
 * 2020/7/16
 */
public class BinFileTest {
  public static void main(String[] args) throws Exception {
    String dir = System.getProperty("user.home") + "/Downloads/curread/tmp/";
    var diff = BinFileUtil.diff(dir + "1", dir + "2", 1024*4);
    System.out.println(diff.length - 3);
    System.out.println(Arrays.toString(diff));
  }
}
