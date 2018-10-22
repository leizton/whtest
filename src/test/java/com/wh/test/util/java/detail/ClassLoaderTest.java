package com.wh.test.util.java.detail;

/**
 * 2018/9/18
 */
public class ClassLoaderTest {

  public static void main(String[] args) throws Exception {
    ClassLoader appCL = ClassLoaderTest.class.getClassLoader();
    ClassLoader extCL = appCL.getParent();
    ClassLoader bootstrapCL = extCL.getParent();  // null
    System.out.println(appCL.getClass().getCanonicalName());
    System.out.println(extCL.getClass().getCanonicalName());
    System.out.println(bootstrapCL);

    Class clz = ClassLoader.getSystemClassLoader().loadClass("java.lang.String");
    System.out.println(clz.getClassLoader());  // null. String的实际loader是BootstrapClassLoader, 所以返回null
  }
}
