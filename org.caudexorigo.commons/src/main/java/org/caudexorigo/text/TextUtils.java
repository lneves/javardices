package org.caudexorigo.text;

import org.apache.commons.lang3.StringUtils;

public class TextUtils {
  public static String coalesce(String... values) {
    for (String v : values) {
      if (StringUtils.isNotBlank(v)) {
        return v;
      }
    }
    return "";
  }

  public static String removeInvalidXmlChars(String in_string) {
    if (in_string == null)
      return null;

    StringBuilder sbOutput = new StringBuilder();
    char ch;

    for (int i = 0; i < in_string.length(); i++) {
      ch = in_string.charAt(i);
      if ((ch >= 0x0020 && ch <= 0xD7FF) ||
          (ch >= 0xE000 && ch <= 0xFFFD) ||
          ch == 0x0009 ||
          ch == 0x000A ||
          ch == 0x000D) {
        sbOutput.append(ch);
      }
    }
    return sbOutput.toString();
  }
}
