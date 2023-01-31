package org.caudexorigo.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PropertyReader {
  public PropertyReader() {
    super();
  }

  public void read(File f) throws IOException {
    read(new PropertyResourceBundle(new FileInputStream(f)));
  }

  public void read(ResourceBundle bundle) {
    Properties p = System.getProperties();
    Enumeration<String> keys = bundle.getKeys();

    while (keys.hasMoreElements()) {
      String key = keys.nextElement();

      if (!p.containsKey(key)) {
        p.put(key, bundle.getString(key));
      }
    }
    System.setProperties(p);
  }

  public void read(String res) throws MissingResourceException {
    read(ResourceBundle.getBundle(res));
  }
}
