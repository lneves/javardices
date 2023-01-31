package org.caudexorigo.text;

import org.caudexorigo.io.IOUtils;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlStripper {
  private static final Pattern breaker = Pattern.compile(
      "(<blockquote|<center|<div|<p|<br|<h\\d|<ul|<dl|<ol|<hr|<table)", Pattern.CASE_INSENSITIVE);

  private static final Pattern markup_cleaner = Pattern.compile(
      "<xml.*?xml>|<style.*?style>|<script.*?script>|<.*?>", Pattern.CASE_INSENSITIVE
          | Pattern.DOTALL);

  private static final Pattern space_cleaner = Pattern.compile(
      "[\\u2002\\u2003\\u2004\\u2005\\u2006\\u2007\\u2008\\u2009\\u200a\\u00a0\\u1680\\u000b\\u0020\\u00a0\\u1680\\u202f\\u205f\\u3000\\u0009]");

  private static final Pattern nl_cleaner = Pattern.compile(
      "[\r\\u000a\\u000c\\u000d\\u0085\\u2028\\u2029]");

  private static final Pattern trim_space = Pattern.compile("^[ \t]+|[ \t]+$", Pattern.MULTILINE);

  private static final Pattern multi_space = Pattern.compile("[ \t]{2,}");

  private static final Pattern multi_ln = Pattern.compile("[\\x0B\n]{2,}");

  public static String strip(String html) {
    if (StringUtils.isBlank(html)) {
      return "";
    }

    Matcher m = breaker.matcher(html);

    String step0;

    if (m.find()) {
      step0 = m.replaceAll("\n" + m.group(1));
    } else {
      step0 = html;
    }

    String step1 = markup_cleaner.matcher(step0).replaceAll(" ");
    String step2 = StringEscapeUtils.unescapeHtml(step1);
    String step3 = space_cleaner.matcher(step2).replaceAll(" ");
    String step4 = nl_cleaner.matcher(step3).replaceAll("\n");
    String step5 = trim_space.matcher(step4).replaceAll("");
    String step6 = multi_ln.matcher(step5).replaceAll("\n");
    String step7 = multi_space.matcher(step6).replaceAll(" ").trim();

    return step7;
  }

  public static void main(String[] args) throws Throwable {
    String link = "http://technotes.blogs.sapo.pt/";
    URL url = new URL(link);

    String html = IOUtils.toString(url.openStream());

    System.out.println(strip(html));
  }

}
