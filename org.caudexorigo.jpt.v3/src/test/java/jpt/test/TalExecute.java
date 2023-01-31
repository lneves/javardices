package jpt.test;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

public class TalExecute {
  public static void main(String[] args) {
    try {
      if ((args.length < 1) || StringUtils.isBlank(args[0])) {
        throw new IllegalArgumentException("missing template file");
      }

      String curDir = System.getProperty("user.dir");
      String template = args[0];
      String templatePath = String.format("file://%s/templates/input/%s", curDir, template);
      JptInstance jpt = JptInstanceBuilder.getJptInstance(URI.create(templatePath));
      Writer out = new OutputStreamWriter(System.out);
      jpt.render(out);
      out.flush();
    } catch (Throwable t) {
      findRootCause(t).printStackTrace();
    }
  }

  private static Throwable findRootCause(Throwable ex) {
    Throwable error_ex = new Exception(ex);
    while (error_ex.getCause() != null) {
      error_ex = error_ex.getCause();
    }
    return error_ex;
  }
}
