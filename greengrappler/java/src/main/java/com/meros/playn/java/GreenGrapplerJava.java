package com.meros.playn.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

public class GreenGrapplerJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/meros/playn/resources");
    PlayN.run(new com.meros.playn.core.GreenGrappler(false));
  }
}
