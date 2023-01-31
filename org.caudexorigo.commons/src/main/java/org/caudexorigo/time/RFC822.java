package org.caudexorigo.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class RFC822 {
  private static final TimeZone gmt_timeZone = TimeZone.getTimeZone("GMT");

  private static final Pattern rfc_part_splitter = Pattern.compile(",|\\s");
  private static final Pattern rfc_hour_splitter = Pattern.compile(":");

  private static final ConcurrentMap<String, Integer> rfc_month;
  private static final ConcurrentMap<String, Integer> rfc_dow;

  static {
    rfc_month = new ConcurrentHashMap<String, Integer>();
    rfc_month.put("Jan", 0);
    rfc_month.put("Feb", 1);
    rfc_month.put("Mar", 2);
    rfc_month.put("Apr", 3);
    rfc_month.put("May", 4);
    rfc_month.put("Jun", 5);
    rfc_month.put("Jul", 6);
    rfc_month.put("Aug", 7);
    rfc_month.put("Sep", 8);
    rfc_month.put("Oct", 9);
    rfc_month.put("Nov", 10);
    rfc_month.put("Dec", 11);

    rfc_dow = new ConcurrentHashMap<String, Integer>();
    rfc_dow.put("Sun", 1);
    rfc_dow.put("Mon", 2);
    rfc_dow.put("Tue", 3);
    rfc_dow.put("Wed", 4);
    rfc_dow.put("Thu", 5);
    rfc_dow.put("Fri", 6);
    rfc_dow.put("Sat", 7);
  }

  private static final ThreadLocal<SimpleDateFormat> rfc_sdf = new ThreadLocal<SimpleDateFormat>() {
    @Override
    protected SimpleDateFormat initialValue() {
      final String _rfc_pattern = "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'zzz";
      SimpleDateFormat _rfc_format;
      _rfc_format = new SimpleDateFormat(_rfc_pattern, Locale.US);
      _rfc_format.setTimeZone(gmt_timeZone);
      return _rfc_format;
    }
  };

  public static String toISO(String rfc_date) throws ParseException {
    Date date = parse(rfc_date);
    return ISO8601.format(date);
  }

  public static Date parse(String rfc_date) {
    Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.US);
    calendar.clear();
    String[] parts = rfc_part_splitter.split(rfc_date);

    // System.out.println(Arrays.toString(parts));

    int month_pos = 0;
    boolean is_month_set = false;

    int dow_pos = 0;
    boolean is_dow_set = false;

    int hour_pos = 0;
    boolean is_hour_set = false;

    for (int i = 0; i < parts.length; i++) {
      String part = parts[i];

      if (!is_month_set) {
        Integer month_part = rfc_month.get(part);
        if (month_part != null) {
          calendar.set(Calendar.MONTH, month_part.intValue());
          month_pos = i;
          is_month_set = true;
        }
      }

      if (!is_dow_set) {
        Integer dow_part = rfc_dow.get(part);
        if (dow_part != null) {
          calendar.set(Calendar.DAY_OF_WEEK, dow_part.intValue());
          dow_pos = i;
          is_dow_set = true;
        }
      }

      if (!is_hour_set) {
        if (part.indexOf(":") > -1) {
          String[] hour_parts = rfc_hour_splitter.split(part);

          if (hour_parts.length >= 2 && hour_parts.length <= 3) {
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_parts[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(hour_parts[1]));

            if (hour_parts.length == 3) {
              calendar.set(Calendar.SECOND, Integer.parseInt(hour_parts[2]));
            }

          } else {
            throw new RuntimeException("Error parsing date value, illegal hour format: '" + part
                + "'");
          }
          is_hour_set = true;
          hour_pos = i;
        }
      }
    }

    int day_pos = month_pos - 1;
    int year_pos = month_pos + 1;

    String timeZone = parts[parts.length - 1];

    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[day_pos]));
    calendar.set(Calendar.YEAR, Integer.parseInt(parts[year_pos]));

    if (timeZone.startsWith("+")) {
      int offset = Integer.parseInt(timeZone.substring(1)) * 60000;

      // System.out.println("RfcTest.parseRfcDate.offset: " + offset);

      calendar.set(Calendar.ZONE_OFFSET, offset);
    } else if (timeZone.startsWith("-")) {
      int offset = -1 * Integer.parseInt(timeZone.substring(1)) * 60000;

      // System.out.println("RfcTest.parseRfcDate.offset: " + offset);

      calendar.set(Calendar.ZONE_OFFSET, offset);
    } else {
      // System.out.println("RfcTest.parseRfcDate.timezone: " + timeZone);
      calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

    return calendar.getTime();
  }

  public static String format(Date date) throws ParseException {
    return rfc_sdf.get().format(date);
  }
}
