package org.caudexorigo.jpt;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Formatter;
import java.util.Locale;

public class JptFunctions {
  public JptFunctions() {}

  public static Number max(int a, int b) {
    return Integer.valueOf(Math.max(a, b));
  }

  public static Number min(int a, int b) {
    return Integer.valueOf(Math.min(a, b));
  }

  public static String now() {
    StringBuilder sb = new StringBuilder();
    Formatter formmater = new Formatter(sb);
    formmater.format("%tFT%<tT%<tz", System.currentTimeMillis());
    formmater.close();
    return sb.toString();
  }

  public static Object tif(boolean condition, Object thenResult, Object elseResult) {
    return condition ? thenResult : elseResult;
  }

  public static String currency(BigDecimal value, Locale locale) {
    NumberFormat money = NumberFormat.getCurrencyInstance(locale);
    return money.format(value);
  }

  public static String printf(String pattern, Object value) {
    StringBuilder sb = new StringBuilder();
    Formatter formmater = new Formatter(sb);
    formmater.format(pattern, value);
    formmater.close();
    return sb.toString();
  }

  public static String printf(String pattern, Object value, Locale locale) {
    StringBuilder sb = new StringBuilder();
    Formatter formmater = new Formatter(sb, locale);
    formmater.format(pattern, value);
    formmater.close();
    return sb.toString();
  }

  public static String cap_first(String s) {
    int i = 0;
    int ln = s.length();
    while (i < ln && Character.isWhitespace(s.charAt(i))) {
      i++;
    }
    if (i < ln) {
      StringBuilder b = new StringBuilder(s);
      b.setCharAt(i, Character.toUpperCase(s.charAt(i)));
      s = b.toString();
    }
    return s;
  }

  public static String uncap_first(String s) {
    int i = 0;
    int ln = s.length();
    while (i < ln && Character.isWhitespace(s.charAt(i))) {
      i++;
    }
    if (i < ln) {
      StringBuilder b = new StringBuilder(s);
      b.setCharAt(i, Character.toLowerCase(s.charAt(i)));
      s = b.toString();
    }
    return s;
  }

  public static String capitalize(String s) {
    return StringUtils.capitalize(s);
  }

  public static boolean ends_with(String s, String suf) {
    return s.endsWith(suf);
  }

  public static int index_of(String s, String str) {
    return s.indexOf(str);
  }

  public static int last_index_of(String s, String str) {
    return s.lastIndexOf(str);
  }

  public static int length(String s) {
    return s.length();
  }

  public static String lower_case(String s) {
    return s.toLowerCase();
  }

  public static boolean contains(String s, String str) {
    return s.contains(str);
  }

  public static boolean starts_with(String s, String suf) {
    return s.startsWith(suf);
  }
}
