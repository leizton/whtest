package com.wh.test.util;

import com.google.common.base.Strings;

import java.util.TreeMap;

/**
 * 2018/5/21
 */
@SuppressWarnings("WeakerAccess")
public class TrieTree {

  private Node root = new Node('\0', 0, null);

  public Node strToNode(String s) {
    if (Strings.isNullOrEmpty(s)) {
      return root;
    }
    Node parent = root, child;
    for (int i = 0; i < s.length(); i++) {
      child = parent.findOrInsertChild(s.charAt(i));
      parent = child;
    }
    return parent;
  }

  public String nodeToStr(Node node) {
    if (node.level == 0) {
      return "";
    }
    char[] buf = new char[node.level];
    for (int i = node.level - 1; i >= 0; i--) {
      buf[i] = node.c;
      node = node.parent;
    }
    return new String(buf);
  }

  public static final class Node implements Comparable<Node> {
    public char c;
    private short level;
    private Node parent;
    private TreeMap<Character, Node> children = new TreeMap<>();

    private Node(char c, int level, Node parent) {
      this.c = c;
      this.level = (short) level;
      this.parent = parent;
    }

    public char getCh() {
      return c;
    }

    public short getLevel() {
      return level;
    }

    public Node getParent() {
      return parent;
    }

    private Node findOrInsertChild(char c) {
      return children.computeIfAbsent(c, k -> new Node(k, level + 1, this));
    }

    @Override
    public boolean equals(Object o) {
      return this == o;
    }

    @Override
    public int hashCode() {
      int h = 31 * parent.c + c;
      return 31 * h + level;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Node o) {
      if (this == o) {
        return 0;
      }
      if (level == 0) {
        return -1;
      }
      if (o.level == 0) {
        return 1;
      }

      int min = level <= o.level ? level : o.level;
      Node longer = min == level ? o : this;
      Node shorter = min == level ? this : o;
      final Node longer_ = longer;
      while (longer.level > min) {
        longer = longer.parent;
      }
      if (longer == shorter) {
        return longer_ == this ? 1 : -1;
      }
      while (longer.parent != shorter.parent) {
        longer = longer.parent;
        shorter = shorter.parent;
      }
      if (longer.c < shorter.c) {
        return longer_ == this ? -1 : 1;
      } else {
        return longer_ == this ? 1 : -1;
      }
    }
  }
}
