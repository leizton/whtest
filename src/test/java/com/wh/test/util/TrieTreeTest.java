package com.wh.test.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * 2018/5/21
 */
public class TrieTreeTest {

  @Test
  public void test() {
    TrieTree tree = new TrieTree();

    TrieTree.Node n1 = tree.strToNode("ab1");
    Assert.assertEquals(n1.getCh(), '1');
    Assert.assertEquals(n1.getLevel(), 3);

    TrieTree.Node n2 = tree.strToNode("ab1");
    Assert.assertSame(n1, n2);
    Assert.assertEquals(0, n1.compareTo(n2));

    TrieTree.Node n3 = tree.strToNode("ab2");
    Assert.assertNotSame(n1, n3);
    Assert.assertSame(n1.getParent(), n3.getParent());
    Assert.assertTrue(n1.compareTo(n3) < 0);

    TrieTree.Node n4 = tree.strToNode("ab2");
    Assert.assertSame(n3, n4);
    Assert.assertEquals(0, n4.compareTo(n3));
    Assert.assertTrue(n4.compareTo(n1) > 0);

    TrieTree.Node n5 = tree.strToNode("ac1");
    Assert.assertTrue(n5.compareTo(n1) > 0);
    Assert.assertTrue(n5.compareTo(n3) > 0);
    Assert.assertTrue(n1.compareTo(n5) < 0);
    Assert.assertTrue(n3.compareTo(n5) < 0);
  }
}
