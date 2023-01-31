package org.caudexorigo;

import org.caudexorigo.io.IOUtils;

import java.io.IOException;

public class Text {
  public static final String get(String path) {
    try {
      return IOUtils.toString(Text.class.getResourceAsStream(path));
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
}
