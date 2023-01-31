package org.caudexorigo.jpt;

import org.caudexorigo.nu.xom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class SafeDocumentBuilder {
  private static Logger log = LoggerFactory.getLogger(SafeDocumentBuilder.class);

  public static Document getDocument(String path) {
    log.debug("document.path: '{}'", path);
    try {
      InputStream in = SafeDocumentBuilder.class.getResourceAsStream(path);
      return XomDocumentBuilder.getDocument(in);
    } catch (Throwable t) {
      log.error(t.getMessage());
      return null;
    }
  }
}
