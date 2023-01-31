package org.caudexorigo.http.netty.reporting;

import org.caudexorigo.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorTemplates {
  private static Logger log = LoggerFactory.getLogger(ErrorTemplates.class);
  static {
    try {
      DEFAULT_TEMPLATE = IOUtils.toString(ErrorTemplates.class.getResourceAsStream("std.html"));
    } catch (IOException e) {
      log.error("Failed to load error template", e);
      DEFAULT_TEMPLATE = "";
    } ;
  }

  private static Map<Integer, String> templates = new HashMap<Integer, String>();

  private static String DEFAULT_TEMPLATE;


  public static String getTemplate(int code) {
    String template = templates.get(code);
    return (template != null) ? template : DEFAULT_TEMPLATE;
  }

  public static void setTemplate(int code, String template) {
    templates.put(code, template);
  }

  public static void setTemplateFromFile(int code, String fileLocation) {
    try {
      String template = IOUtils.toString(new FileInputStream(new File(fileLocation)));
      setTemplate(code, template);
    } catch (Exception e) {
      log.error(String.format("Error trying to read file '%s' for code %s.", fileLocation, code));
    }
  }
}
