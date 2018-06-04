package com.wh.test.util;

import com.google.common.base.Preconditions;

import java.util.Random;

/**
 * 2018/6/4
 */
@SuppressWarnings("WeakerAccess")
public class ShuffleUtil {

  public static void shuffle(int[] cards) {
    Preconditions.checkNotNull(cards);

    Random rand = new Random();
    rand.setSeed(System.currentTimeMillis());

    for (int i = 1; i < cards.length; i++) {
      int j = rand.nextInt(i + 1);
      if (j != i) {
        int tmp = cards[i];
        cards[i] = cards[j];
        cards[j] = tmp;
      }
    }
  }
}
