package com.wh.test.util;

/**
 * 2018/9/6
 */
public class AllocInStack {

  // -XX:+DoEscapeAnalysis     启用逃逸分析, 找出不出栈(没有逃逸)的object
  // -XX+EliminateAllocations  标量替换, 对于没有逃逸的object, 编译器把字段展开成局部栈变量
  public static void main(String[] args) {
    test();
    System.gc();
  }

  private static void test() {
    Test stackFoo = new Test("maybe stack foo");
    System.out.println(stackFoo);
  }

  private static final class Test {
    private final String v;

    private Test(String v) {
      this.v = v;
    }

    @Override
    public String toString() {
      return v;
    }

    @Override
    protected void finalize() {
      System.out.println("alloc in heap: " + v);
    }
  }
}
