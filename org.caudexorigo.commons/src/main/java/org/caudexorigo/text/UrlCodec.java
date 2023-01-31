package org.caudexorigo.text;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.io.UnsynchronizedCharArrayWriter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.BitSet;

public class UrlCodec {
  private static BitSet dontNeedEncoding;
  private static final int caseDiff = ('a' - 'A');

  private static final String[] searchList = {"\\+", "\\%21", "\\%27", "\\%28", "\\%29", "\\%7E",
      "%0A"};
  private static final String[] replacementList = {"%20", "!", "'", "(", ")", "~", "\n"};

  static {
    dontNeedEncoding = new BitSet(256);
    int i;
    for (i = 'a'; i <= 'z'; i++) {
      dontNeedEncoding.set(i);
    }
    for (i = 'A'; i <= 'Z'; i++) {
      dontNeedEncoding.set(i);
    }
    for (i = '0'; i <= '9'; i++) {
      dontNeedEncoding.set(i);
    }
    dontNeedEncoding.set(' ');
    dontNeedEncoding.set('-');
    dontNeedEncoding.set('_');
    dontNeedEncoding.set('.');
    dontNeedEncoding.set('*');
  }

  public static String decode(String s, String enc) {
    try {
      boolean needToChange = false;
      int numChars = s.length();
      StringBuilder sb = new StringBuilder(numChars > 500 ? numChars / 2 : numChars);
      int i = 0;

      if (enc.length() == 0) {
        throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
      }

      char c;
      byte[] bytes = null;
      while (i < numChars) {
        c = s.charAt(i);
        switch (c) {
          case '+':
            sb.append(' ');
            i++;
            needToChange = true;
            break;
          case '%':
            /*
             * Starting with this instance of %, process all consecutive substrings of the
             * form %xy. Each substring %xy will yield a byte. Convert all consecutive
             * bytes obtained this way to whatever character(s) they represent in the
             * provided encoding.
             */

            try {

              // (numChars-i)/3 is an upper bound for the number
              // of remaining bytes
              if (bytes == null)
                bytes = new byte[(numChars - i) / 3];
              int pos = 0;

              while (((i + 2) < numChars) && (c == '%')) {
                bytes[pos++] = (byte) Integer.parseInt(s.substring(i + 1, i + 3), 16);
                i += 3;
                if (i < numChars)
                  c = s.charAt(i);
              }

              // A trailing, incomplete byte encoding such as
              // "%x" will cause an exception to be thrown

              if ((i < numChars) && (c == '%'))
                throw new IllegalArgumentException(
                    "URLDecoder: Incomplete trailing escape (%) pattern");

              sb.append(new String(bytes, 0, pos, enc));
            } catch (NumberFormatException e) {
              throw new IllegalArgumentException(
                  "URLDecoder: Illegal hex characters in escape (%) pattern - " + e.getMessage());
            }
            needToChange = true;
            break;
          default:
            sb.append(c);
            i++;
            break;
        }
      }

      return (needToChange ? sb.toString() : s);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  /**
   * Translates a string into <code>application/x-www-form-urlencoded</code> format using
   * a specific encoding scheme. This method uses the supplied encoding scheme to obtain
   * the bytes for unsafe characters.
   * <p>
   * <em><strong>Note:</strong> The
   * <a href= "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars"> World
   * Wide Web Consortium Recommendation</a> states that UTF-8 should be used. Not doing so
   * may introduce incompatibilites.</em>
   * 
   * @param s <code>String</code> to be translated.
   * @param enc The name of a supported
   *        <a href="../lang/package-summary.html#charenc">character encoding</a>.
   * @return the translated <code>String</code>.
   * @exception UnsupportedEncodingException If the named encoding is not supported
   * @see URLDecoder#decode(java.lang.String, java.lang.String)
   * @since 1.4
   */
  public static String encode(String s, String enc) {
    try (UnsynchronizedCharArrayWriter charArrayWriter = new UnsynchronizedCharArrayWriter();) {
      boolean needToChange = false;
      StringBuilder out = new StringBuilder(s.length());

      if (enc == null)
        throw new NullPointerException("charsetName");

      Charset charset = Charset.forName(enc);

      for (int i = 0; i < s.length();) {
        int c = (int) s.charAt(i);
        // System.out.println("Examining character: " + c);
        if (dontNeedEncoding.get(c)) {
          if (c == ' ') {
            c = '+';
            needToChange = true;
          }
          // System.out.println("Storing: " + c);
          out.append((char) c);
          i++;
        } else {
          // convert to external encoding before hex conversion
          do {
            charArrayWriter.write(c);
            /*
             * If this character represents the start of a Unicode surrogate pair, then
             * pass in two characters. It's not clear what should be done if a bytes
             * reserved in the surrogate pairs range occurs outside of a legal surrogate
             * pair. For now, just treat it as if it were any other character.
             */
            if (c >= 0xD800 && c <= 0xDBFF) {
              /*
               * System.out.println(Integer.toHexString(c) + " is high surrogate");
               */
              if ((i + 1) < s.length()) {
                int d = (int) s.charAt(i + 1);
                /*
                 * System.out.println("\tExamining " + Integer.toHexString(d));
                 */
                if (d >= 0xDC00 && d <= 0xDFFF) {
                  /*
                   * System.out.println("\t" + Integer.toHexString(d) +
                   * " is low surrogate");
                   */
                  charArrayWriter.write(d);
                  i++;
                }
              }
            }
            i++;
          } while (i < s.length() && !dontNeedEncoding.get((c = (int) s.charAt(i))));

          charArrayWriter.flush();
          String str = new String(charArrayWriter.toCharArray());
          byte[] ba = str.getBytes(charset);
          for (int j = 0; j < ba.length; j++) {
            out.append('%');
            char ch = Character.forDigit((ba[j] >> 4) & 0xF, 16);
            // converting to use uppercase letter as part of
            // the hex value if ch is a letter.
            if (Character.isLetter(ch)) {
              ch -= caseDiff;
            }
            out.append(ch);
            ch = Character.forDigit(ba[j] & 0xF, 16);
            if (Character.isLetter(ch)) {
              ch -= caseDiff;
            }
            out.append(ch);
          }
          charArrayWriter.reset();
          needToChange = true;
        }
      }

      charArrayWriter.close();

      return (needToChange ? out.toString() : s);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }

  public static String encodeUriComponent(String link) {
    String stmp = encode(link, "utf-8");
    return StringUtils.replaceEach(stmp, searchList, replacementList);
  }
}
