package org.caudexorigo.text;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class DateUtil
{

	private static final TimeZone timeZone = TimeZone.getTimeZone("UTC");
	private static final TimeZone gmt_timeZone = TimeZone.getTimeZone("GMT");
	private static final TimeZone localTimeZone = TimeZone.getDefault();

	private static final Pattern rfc_part_splitter = Pattern.compile(",|\\s");
	private static final Pattern rfc_hour_splitter = Pattern.compile(":");

	private static final ConcurrentMap<String, Integer> rfc_month;

	private static final ConcurrentMap<String, Integer> rfc_dow;

	static
	{
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

	private static final ThreadLocal<SimpleDateFormat> sdf_local = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			SimpleDateFormat _format;
			_format = new SimpleDateFormat(pattern);
			_format.setTimeZone(localTimeZone);
			return _format;

		}
	};

	private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			final String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
			SimpleDateFormat _format;
			_format = new SimpleDateFormat(pattern);
			_format.setTimeZone(timeZone);
			return _format;

		}
	};

	private static final ThreadLocal<SimpleDateFormat> rfc_sdf = new ThreadLocal<SimpleDateFormat>()
	{
		@Override
		protected SimpleDateFormat initialValue()
		{
			final String _rfc_pattern = "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'zzz";
			SimpleDateFormat _rfc_format;
			_rfc_format = new SimpleDateFormat(_rfc_pattern, Locale.US);
			_rfc_format.setTimeZone(gmt_timeZone);
			return _rfc_format;

		}
	};

	public static String rfc822ToISO(String rfc_date) throws ParseException
	{
		Date date = parseRfc822(rfc_date);
		return formatISODate(date);
	}

	public static Date parseRfc822(String rfc_date)
	{
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

		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];

			if (!is_month_set)
			{
				Integer month_part = rfc_month.get(part);
				if (month_part != null)
				{
					calendar.set(Calendar.MONTH, month_part.intValue());
					month_pos = i;
					is_month_set = true;
				}
			}

			if (!is_dow_set)
			{
				Integer dow_part = rfc_dow.get(part);
				if (dow_part != null)
				{
					calendar.set(Calendar.DAY_OF_WEEK, dow_part.intValue());
					dow_pos = i;
					is_dow_set = true;
				}
			}

			if (!is_hour_set)
			{
				if (part.indexOf(":") > -1)
				{
					String[] hour_parts = rfc_hour_splitter.split(part);

					if (hour_parts.length >= 2 && hour_parts.length <= 3)
					{
						calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_parts[0]));
						calendar.set(Calendar.MINUTE, Integer.parseInt(hour_parts[1]));

						if (hour_parts.length == 3)
						{
							calendar.set(Calendar.SECOND, Integer.parseInt(hour_parts[2]));
						}

					}
					else
					{
						throw new RuntimeException("Error parsing date value, illegal hour format: '" + part + "'");
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

		if (timeZone.startsWith("+"))
		{
			int offset = Integer.parseInt(timeZone.substring(1)) * 60000;

			// System.out.println("RfcTest.parseRfcDate.offset: " + offset);

			calendar.set(Calendar.ZONE_OFFSET, offset);
		}
		else if (timeZone.startsWith("-"))
		{
			int offset = -1 * Integer.parseInt(timeZone.substring(1)) * 60000;

			// System.out.println("RfcTest.parseRfcDate.offset: " + offset);

			calendar.set(Calendar.ZONE_OFFSET, offset);
		}
		else
		{
			// System.out.println("RfcTest.parseRfcDate.timezone: " + timeZone);
			calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
		}

		return calendar.getTime();

	}

	public static String formatRfc822(Date date) throws ParseException
	{
		return rfc_sdf.get().format(date);
	}

	private static boolean check(StringTokenizer st, String token) throws IllegalArgumentException
	{
		try
		{
			if (st.nextToken().equals(token))
			{
				return true;
			}
			else
			{
				throw new IllegalArgumentException("Missing [" + token + "]");
			}
		}
		catch (NoSuchElementException ex)
		{
			return false;
		}
	}

	private static Calendar getCalendar(String isodate) throws IllegalArgumentException
	{
		// YYYY-MM-DDThh:mm:ss.sTZD
		StringTokenizer st = new StringTokenizer(isodate, "-T:.+Z", true);

		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		try
		{
			// Year
			if (st.hasMoreTokens())
			{
				int year = Integer.parseInt(st.nextToken());
				calendar.set(Calendar.YEAR, year);
			}
			else
			{
				return calendar;
			}
			// Month
			if (check(st, "-") && (st.hasMoreTokens()))
			{
				int month = Integer.parseInt(st.nextToken()) - 1;
				calendar.set(Calendar.MONTH, month);
			}
			else
			{
				return calendar;
			}
			// Day
			if (check(st, "-") && (st.hasMoreTokens()))
			{
				int day = Integer.parseInt(st.nextToken());
				calendar.set(Calendar.DAY_OF_MONTH, day);
			}
			else
			{
				return calendar;
			}
			// Hour
			if (check(st, "T") && (st.hasMoreTokens()))
			{
				int hour = Integer.parseInt(st.nextToken());
				calendar.set(Calendar.HOUR_OF_DAY, hour);
			}
			else
			{
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar;
			}
			// Minutes
			if (check(st, ":") && (st.hasMoreTokens()))
			{
				int minutes = Integer.parseInt(st.nextToken());
				calendar.set(Calendar.MINUTE, minutes);
			}
			else
			{
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar;
			}

			//
			// Not mandatory now
			//

			// Secondes
			if (!st.hasMoreTokens())
			{
				return calendar;
			}
			String tok = st.nextToken();
			if (tok.equals(":"))
			{ // secondes
				if (st.hasMoreTokens())
				{
					int secondes = Integer.parseInt(st.nextToken());
					calendar.set(Calendar.SECOND, secondes);
					if (!st.hasMoreTokens())
					{
						return calendar;
					}
					// frac sec
					tok = st.nextToken();
					if (tok.equals("."))
					{
						// bug fixed, thx to Martin Bottcher
						String nt = st.nextToken();
						while (nt.length() < 3)
						{
							nt += "0";
						}
						nt = nt.substring(0, 3); // Cut trailing chars..
						int millisec = Integer.parseInt(nt);
						// int millisec = Integer.parseInt(st.nextToken()) * 10;
						calendar.set(Calendar.MILLISECOND, millisec);
						if (!st.hasMoreTokens())
						{
							return calendar;
						}
						tok = st.nextToken();
					}
					else
					{
						calendar.set(Calendar.MILLISECOND, 0);
					}
				}
				else
				{
					throw new IllegalArgumentException("No secondes specified");
				}
			}
			else
			{
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
			}
			// Timezone
			if (!tok.equals("Z"))
			{ // UTC
				if (!(tok.equals("+") || tok.equals("-")))
				{
					throw new IllegalArgumentException("only Z, + or - allowed");
				}
				boolean plus = tok.equals("+");
				if (!st.hasMoreTokens())
				{
					throw new IllegalArgumentException("Missing hour field");
				}
				int tzhour = Integer.parseInt(st.nextToken());
				int tzmin = 0;
				if (check(st, ":") && (st.hasMoreTokens()))
				{
					tzmin = Integer.parseInt(st.nextToken());
				}
				else
				{
					throw new IllegalArgumentException("Missing minute field");
				}

				if (plus)
				{
					calendar.add(Calendar.HOUR, -tzhour);
					calendar.add(Calendar.MINUTE, -tzmin);
				}
				else
				{
					calendar.add(Calendar.HOUR, tzhour);
					calendar.add(Calendar.MINUTE, tzmin);
				}
			}
		}
		catch (NumberFormatException ex)
		{
			throw new IllegalArgumentException("[" + ex.getMessage() + "] is not an integer");
		}
		return calendar;
	}

	/**
	 * Parse the given string in ISO 8601 format and build a Date object.
	 * 
	 * @param isodate
	 *            the date in ISO 8601 format
	 * @return a Date instance
	 * @exception IllegalArgumentException
	 *                if the date is not valid
	 */
	public static Date parseISODate(String isodate) throws IllegalArgumentException
	{
		Calendar calendar = getCalendar(isodate);
		return calendar.getTime();
	}

	/**
	 * Generate a date string in ISO 8601 format
	 * 
	 * @param date
	 *            a Date instance
	 * @return a string representing the date in the ISO 8601 format
	 */

	public static String formatISODate(Date date)
	{
		return sdf.get().format(date);
	}

	public static String formatISODateLocalTz(Date date)
	{
		return sdf_local.get().format(date);
	}

	public static void test(String isodate)
	{
		System.out.println("----------------------------------");
		try
		{
			Date date = parseISODate(isodate);
			System.out.println(">> " + isodate);
			System.out.println(">> " + date.toString() + " [" + date.getTime() + "]");
			System.out.println(">> " + formatISODate(date));
		}
		catch (IllegalArgumentException ex)
		{
			System.err.println(isodate + " is invalid");
			System.err.println(ex.getMessage());
		}
		System.out.println("----------------------------------");
	}

	public static void test(Date date)
	{
		String isodate = null;
		System.out.println("----------------------------------");
		try
		{
			System.out.println(">> " + date.toString() + " [" + date.getTime() + "]");
			isodate = formatISODate(date);
			System.out.println(">> " + isodate);
			date = parseISODate(isodate);
			System.out.println(">> " + date.toString() + " [" + date.getTime() + "]");
		}
		catch (IllegalArgumentException ex)
		{
			System.err.println(isodate + " is invalid");
			System.err.println(ex.getMessage());
		}
		System.out.println("----------------------------------");
	}

	public static void main(String args[])
	{
		test("2007-12-14T21:47:35.538680Z");
		test("2007-12-14T21:47:35.538Z");
		test("2007-12-14T21:47:35Z");
		test("1997-07-16T19:20:30.45+01:00");
		test("1997-07-16T19:20:30.45123+01:00");

		test("1997-07-16T19:20:30.45-02:00");
		test("1997-07-16T19:20:30+01:00");
		test("1997-07-16T19:20:30+01:00");
		test("1997-07-16T19:20");
		test("1997-07-16");
		test("1997-07");
		test("1997");
		test(new Date());
	}

	/**
	 * The UTC time zone (often referred to as GMT).
	 */
	public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("GMT");
	/**
	 * Number of milliseconds in a standard second.
	 * 
	 * @since 2.1
	 */
	public static final long MILLIS_PER_SECOND = 1000;
	/**
	 * Number of milliseconds in a standard minute.
	 * 
	 * @since 2.1
	 */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	/**
	 * Number of milliseconds in a standard hour.
	 * 
	 * @since 2.1
	 */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	/**
	 * Number of milliseconds in a standard day.
	 * 
	 * @since 2.1
	 */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	/**
	 * This is half a month, so this represents whether a date is in the top or bottom half of the month.
	 */
	public final static int SEMI_MONTH = 1001;

	private static final int[][] fields = { { Calendar.MILLISECOND }, { Calendar.SECOND }, { Calendar.MINUTE }, { Calendar.HOUR_OF_DAY, Calendar.HOUR }, { Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM
	/* Calendar.DAY_OF_YEAR, Calendar.DAY_OF_WEEK, Calendar.DAY_OF_WEEK_IN_MONTH */
	}, { Calendar.MONTH, SEMI_MONTH }, { Calendar.YEAR }, { Calendar.ERA } };

	/**
	 * A week range, starting on Sunday.
	 */
	public final static int RANGE_WEEK_SUNDAY = 1;

	/**
	 * A week range, starting on Monday.
	 */
	public final static int RANGE_WEEK_MONDAY = 2;

	/**
	 * A week range, starting on the day focused.
	 */
	public final static int RANGE_WEEK_RELATIVE = 3;

	/**
	 * A week range, centered around the day focused.
	 */
	public final static int RANGE_WEEK_CENTER = 4;

	/**
	 * A month range, the week starting on Sunday.
	 */
	public final static int RANGE_MONTH_SUNDAY = 5;

	/**
	 * A month range, the week starting on Monday.
	 */
	public final static int RANGE_MONTH_MONDAY = 6;

	/**
	 * <p>
	 * <code>DateUtils</code> instances should NOT be constructed in standard programming. Instead, the class should be used as <code>DateUtils.parse(str);</code>.
	 * </p>
	 * 
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean instance to operate.
	 * </p>
	 */
	private DateUtil()
	{
		super();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if two date objects are on the same day ignoring time.
	 * </p>
	 * 
	 * <p>
	 * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
	 * </p>
	 * 
	 * @param date1
	 *            the first date, not altered, not null
	 * @param date2
	 *            the second date, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameDay(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	/**
	 * <p>
	 * Checks if two calendar objects are on the same day ignoring time.
	 * </p>
	 * 
	 * <p>
	 * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same day
	 * @throws IllegalArgumentException
	 *             if either calendar is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameDay(Calendar cal1, Calendar cal2)
	{
		if (cal1 == null || cal2 == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if two date objects represent the same instant in time.
	 * </p>
	 * 
	 * <p>
	 * This method compares the long millisecond time of the two objects.
	 * </p>
	 * 
	 * @param date1
	 *            the first date, not altered, not null
	 * @param date2
	 *            the second date, not altered, not null
	 * @return true if they represent the same millisecond instant
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameInstant(Date date1, Date date2)
	{
		if (date1 == null || date2 == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		return date1.getTime() == date2.getTime();
	}

	/**
	 * <p>
	 * Checks if two calendar objects represent the same instant in time.
	 * </p>
	 * 
	 * <p>
	 * This method compares the long millisecond time of the two objects.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same millisecond instant
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameInstant(Calendar cal1, Calendar cal2)
	{
		if (cal1 == null || cal2 == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		return cal1.getTime().getTime() == cal2.getTime().getTime();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Checks if two calendar objects represent the same local time.
	 * </p>
	 * 
	 * <p>
	 * This method compares the values of the fields of the two objects. In addition, both calendars must be the same of the same type.
	 * </p>
	 * 
	 * @param cal1
	 *            the first calendar, not altered, not null
	 * @param cal2
	 *            the second calendar, not altered, not null
	 * @return true if they represent the same millisecond instant
	 * @throws IllegalArgumentException
	 *             if either date is <code>null</code>
	 * @since 2.1
	 */
	public static boolean isSameLocalTime(Calendar cal1, Calendar cal2)
	{
		if (cal1 == null || cal2 == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		return (cal1.get(Calendar.MILLISECOND) == cal2.get(Calendar.MILLISECOND) && cal1.get(Calendar.SECOND) == cal2.get(Calendar.SECOND) && cal1.get(Calendar.MINUTE) == cal2.get(Calendar.MINUTE) && cal1.get(Calendar.HOUR) == cal2.get(Calendar.HOUR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) && cal1.getClass() == cal2.getClass());
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Parses a string representing a date by trying a variety of different parsers.
	 * </p>
	 * 
	 * <p>
	 * The parse will try each parse pattern in turn. A parse is only deemed sucessful if it parses the whole of the input string. If no parse patterns match, a ParseException is thrown.
	 * </p>
	 * 
	 * @param str
	 *            the date to parse, not null
	 * @param parsePatterns
	 *            the date format patterns to use, see SimpleDateFormat, not null
	 * @return the parsed date
	 * @throws IllegalArgumentException
	 *             if the date string or pattern array is null
	 * @throws ParseException
	 *             if none of the date patterns were suitable
	 */
	public static Date parseDate(String str, String[] parsePatterns) throws ParseException
	{
		if (str == null || parsePatterns == null)
		{
			throw new IllegalArgumentException("Date and Patterns must not be null");
		}

		SimpleDateFormat parser = null;
		ParsePosition pos = new ParsePosition(0);
		for (int i = 0; i < parsePatterns.length; i++)
		{
			if (i == 0)
			{
				parser = new SimpleDateFormat(parsePatterns[0]);
			}
			else
			{
				parser.applyPattern(parsePatterns[i]);
			}
			pos.setIndex(0);
			Date date = parser.parse(str, pos);
			if (date != null && pos.getIndex() == str.length())
			{
				return date;
			}
		}
		throw new ParseException("Unable to parse the date: " + str, -1);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of years to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addYears(Date date, int amount)
	{
		return add(date, Calendar.YEAR, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of months to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMonths(Date date, int amount)
	{
		return add(date, Calendar.MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of weeks to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addWeeks(Date date, int amount)
	{
		return add(date, Calendar.WEEK_OF_YEAR, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of days to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addDays(Date date, int amount)
	{
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of hours to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addHours(Date date, int amount)
	{
		return add(date, Calendar.HOUR_OF_DAY, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of minutes to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMinutes(Date date, int amount)
	{
		return add(date, Calendar.MINUTE, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of seconds to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addSeconds(Date date, int amount)
	{
		return add(date, Calendar.SECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds a number of milliseconds to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addMilliseconds(Date date, int amount)
	{
		return add(date, Calendar.MILLISECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to add to
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @deprecated Will become privately scoped in 3.0
	 */
	public static Date add(Date date, int calendarField, int amount)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the years field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setYears(Date date, int amount)
	{
		return set(date, Calendar.YEAR, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the months field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMonths(Date date, int amount)
	{
		return set(date, Calendar.MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the day of month field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setDays(Date date, int amount)
	{
		return set(date, Calendar.DAY_OF_MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the hours field to a date returning a new object. Hours range from 0-23. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setHours(Date date, int amount)
	{
		return set(date, Calendar.HOUR_OF_DAY, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the minute field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMinutes(Date date, int amount)
	{
		return set(date, Calendar.MINUTE, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the seconds field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setSeconds(Date date, int amount)
	{
		return set(date, Calendar.SECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the miliseconds field to a date returning a new object. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMilliseconds(Date date, int amount)
	{
		return set(date, Calendar.MILLISECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the specified field to a date returning a new object. This does not use a lenient calendar. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to set the amount to
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	private static Date set(Date date, int calendarField, int amount)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		// getInstance() returns a new object, so this method is thread safe.
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.set(calendarField, amount);
		return c.getTime();
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Round this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it would return 1 April 2002 0:00:00.000.
	 * </p>
	 * 
	 * <p>
	 * For a date in a timezone that handles the change to daylight saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight saving time begins at 02:00 on March 30. Rounding a date that crosses this time would produce the following values:
	 * <ul>
	 * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
	 * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Date round(Date date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar gval = Calendar.getInstance();
		gval.setTime(date);
		modify(gval, field, true);
		return gval.getTime();
	}

	/**
	 * <p>
	 * Round this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it would return 1 April 2002 0:00:00.000.
	 * </p>
	 * 
	 * <p>
	 * For a date in a timezone that handles the change to daylight saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight saving time begins at 02:00 on March 30. Rounding a date that crosses this time would produce the following values:
	 * <ul>
	 * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
	 * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date (a different object)
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Calendar round(Calendar date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar rounded = (Calendar) date.clone();
		modify(rounded, field, true);
		return rounded;
	}

	/**
	 * <p>
	 * Round this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was passed with MONTH, it would return 1 April 2002 0:00:00.000.
	 * </p>
	 * 
	 * <p>
	 * For a date in a timezone that handles the change to daylight saving time, rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight saving time begins at 02:00 on March 30. Rounding a date that crosses this time would produce the following values:
	 * <ul>
	 * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
	 * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
	 * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, either Date or Calendar
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ClassCastException
	 *             if the object type is not a <code>Date</code> or <code>Calendar</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Date round(Object date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		if (date instanceof Date)
		{
			return round((Date) date, field);
		}
		else if (date instanceof Calendar)
		{
			return round((Calendar) date, field).getTime();
		}
		else
		{
			throw new ClassCastException("Could not round " + date);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Truncate this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
	 * </p>
	 * 
	 * @param date
	 *            the date to work with
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Date truncate(Date date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar gval = Calendar.getInstance();
		gval.setTime(date);
		modify(gval, field, false);
		return gval.getTime();
	}

	/**
	 * <p>
	 * Truncate this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
	 * </p>
	 * 
	 * @param date
	 *            the date to work with
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date (a different object)
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Calendar truncate(Calendar date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar truncated = (Calendar) date.clone();
		modify(truncated, field, false);
		return truncated;
	}

	/**
	 * <p>
	 * Truncate this date, leaving the field specified as the most significant field.
	 * </p>
	 * 
	 * <p>
	 * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, either <code>Date</code> or <code>Calendar</code>
	 * @param field
	 *            the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
	 * @return the rounded date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ClassCastException
	 *             if the object type is not a <code>Date</code> or <code>Calendar</code>
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	public static Date truncate(Object date, int field)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		if (date instanceof Date)
		{
			return truncate((Date) date, field);
		}
		else if (date instanceof Calendar)
		{
			return truncate((Calendar) date, field).getTime();
		}
		else
		{
			throw new ClassCastException("Could not truncate " + date);
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * Internal calculation method.
	 * </p>
	 * 
	 * @param val
	 *            the calendar
	 * @param field
	 *            the field constant
	 * @param round
	 *            true to round, false to truncate
	 * @throws ArithmeticException
	 *             if the year is over 280 million
	 */
	private static void modify(Calendar val, int field, boolean round)
	{
		if (val.get(Calendar.YEAR) > 280000000)
		{
			throw new ArithmeticException("Calendar value too large for accurate calculations");
		}

		if (field == Calendar.MILLISECOND)
		{
			return;
		}

		// ----------------- Fix for LANG-59 ---------------------- START ---------------
		// see http://issues.apache.org/jira/browse/LANG-59
		//
		// Manually truncate milliseconds, seconds and minutes, rather than using
		// Calendar methods.

		Date date = val.getTime();
		long time = date.getTime();
		boolean done = false;

		// truncate milliseconds
		int millisecs = val.get(Calendar.MILLISECOND);
		if (!round || millisecs < 500)
		{
			time = time - millisecs;
		}
		if (field == Calendar.SECOND)
		{
			done = true;
		}

		// truncate seconds
		int seconds = val.get(Calendar.SECOND);
		if (!done && (!round || seconds < 30))
		{
			time = time - (seconds * 1000L);
		}
		if (field == Calendar.MINUTE)
		{
			done = true;
		}

		// truncate minutes
		int minutes = val.get(Calendar.MINUTE);
		if (!done && (!round || minutes < 30))
		{
			time = time - (minutes * 60000L);
		}

		// reset time
		if (date.getTime() != time)
		{
			date.setTime(time);
			val.setTime(date);
		}
		// ----------------- Fix for LANG-59 ----------------------- END ----------------

		boolean roundUp = false;
		for (int i = 0; i < fields.length; i++)
		{
			for (int j = 0; j < fields[i].length; j++)
			{
				if (fields[i][j] == field)
				{
					// This is our field... we stop looping
					if (round && roundUp)
					{
						if (field == SEMI_MONTH)
						{
							// This is a special case that's hard to generalize
							// If the date is 1, we round up to 16, otherwise
							// we subtract 15 days and add 1 month
							if (val.get(Calendar.DATE) == 1)
							{
								val.add(Calendar.DATE, 15);
							}
							else
							{
								val.add(Calendar.DATE, -15);
								val.add(Calendar.MONTH, 1);
							}
						}
						else
						{
							// We need at add one to this field since the
							// last number causes us to round up
							val.add(fields[i][0], 1);
						}
					}
					return;
				}
			}
			// We have various fields that are not easy roundings
			int offset = 0;
			boolean offsetSet = false;
			// These are special types of fields that require different rounding rules
			switch (field)
			{
			case SEMI_MONTH:
				if (fields[i][0] == Calendar.DATE)
				{
					// If we're going to drop the DATE field's value,
					// we want to do this our own way.
					// We need to subtrace 1 since the date has a minimum of 1
					offset = val.get(Calendar.DATE) - 1;
					// If we're above 15 days adjustment, that means we're in the
					// bottom half of the month and should stay accordingly.
					if (offset >= 15)
					{
						offset -= 15;
					}
					// Record whether we're in the top or bottom half of that range
					roundUp = offset > 7;
					offsetSet = true;
				}
				break;
			case Calendar.AM_PM:
				if (fields[i][0] == Calendar.HOUR_OF_DAY)
				{
					// If we're going to drop the HOUR field's value,
					// we want to do this our own way.
					offset = val.get(Calendar.HOUR_OF_DAY);
					if (offset >= 12)
					{
						offset -= 12;
					}
					roundUp = offset > 6;
					offsetSet = true;
				}
				break;
			}
			if (!offsetSet)
			{
				int min = val.getActualMinimum(fields[i][0]);
				int max = val.getActualMaximum(fields[i][0]);
				// Calculate the offset from the minimum allowed value
				offset = val.get(fields[i][0]) - min;
				// Set roundUp if this is more than half way between the minimum and maximum
				roundUp = offset > ((max - min) / 2);
			}
			// We need to remove this field
			if (offset != 0)
			{
				val.set(fields[i][0], val.get(fields[i][0]) - offset);
			}
		}
		throw new IllegalArgumentException("The field " + field + " is not supported");

	}

	// -----------------------------------------------------------------------
	/**
	 * <p>
	 * This constructs an <code>Iterator</code> over each day in a date range defined by a focus date and range style.
	 * </p>
	 * 
	 * <p>
	 * For instance, passing Thursday, July 4, 2002 and a <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002, returning a Calendar instance for each intermediate day.
	 * </p>
	 * 
	 * <p>
	 * This method provides an iterator that returns Calendar objects. The days are progressed using {@link Calendar#add(int, int)}.
	 * </p>
	 * 
	 * @param focus
	 *            the date to work with, not null
	 * @param rangeStyle
	 *            the style constant to use. Must be one of {@link DateUtils#RANGE_MONTH_SUNDAY}, {@link DateUtils#RANGE_MONTH_MONDAY}, {@link DateUtils#RANGE_WEEK_SUNDAY}, {@link DateUtils#RANGE_WEEK_MONDAY}, {@link DateUtils#RANGE_WEEK_RELATIVE}, {@link DateUtils#RANGE_WEEK_CENTER}
	 * @return the date iterator, which always returns Calendar instances
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the rangeStyle is invalid
	 */
	public static Iterator iterator(Date focus, int rangeStyle)
	{
		if (focus == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar gval = Calendar.getInstance();
		gval.setTime(focus);
		return iterator(gval, rangeStyle);
	}

	/**
	 * <p>
	 * This constructs an <code>Iterator</code> over each day in a date range defined by a focus date and range style.
	 * </p>
	 * 
	 * <p>
	 * For instance, passing Thursday, July 4, 2002 and a <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002, returning a Calendar instance for each intermediate day.
	 * </p>
	 * 
	 * <p>
	 * This method provides an iterator that returns Calendar objects. The days are progressed using {@link Calendar#add(int, int)}.
	 * </p>
	 * 
	 * @param focus
	 *            the date to work with
	 * @param rangeStyle
	 *            the style constant to use. Must be one of {@link DateUtils#RANGE_MONTH_SUNDAY}, {@link DateUtils#RANGE_MONTH_MONDAY}, {@link DateUtils#RANGE_WEEK_SUNDAY}, {@link DateUtils#RANGE_WEEK_MONDAY}, {@link DateUtils#RANGE_WEEK_RELATIVE}, {@link DateUtils#RANGE_WEEK_CENTER}
	 * @return the date iterator
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the rangeStyle is invalid
	 */
	public static Iterator iterator(Calendar focus, int rangeStyle)
	{
		if (focus == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar start = null;
		Calendar end = null;
		int startCutoff = Calendar.SUNDAY;
		int endCutoff = Calendar.SATURDAY;
		switch (rangeStyle)
		{
		case RANGE_MONTH_SUNDAY:
		case RANGE_MONTH_MONDAY:
			// Set start to the first of the month
			start = truncate(focus, Calendar.MONTH);
			// Set end to the last of the month
			end = (Calendar) start.clone();
			end.add(Calendar.MONTH, 1);
			end.add(Calendar.DATE, -1);
			// Loop start back to the previous sunday or monday
			if (rangeStyle == RANGE_MONTH_MONDAY)
			{
				startCutoff = Calendar.MONDAY;
				endCutoff = Calendar.SUNDAY;
			}
			break;
		case RANGE_WEEK_SUNDAY:
		case RANGE_WEEK_MONDAY:
		case RANGE_WEEK_RELATIVE:
		case RANGE_WEEK_CENTER:
			// Set start and end to the current date
			start = truncate(focus, Calendar.DATE);
			end = truncate(focus, Calendar.DATE);
			switch (rangeStyle)
			{
			case RANGE_WEEK_SUNDAY:
				// already set by default
				break;
			case RANGE_WEEK_MONDAY:
				startCutoff = Calendar.MONDAY;
				endCutoff = Calendar.SUNDAY;
				break;
			case RANGE_WEEK_RELATIVE:
				startCutoff = focus.get(Calendar.DAY_OF_WEEK);
				endCutoff = startCutoff - 1;
				break;
			case RANGE_WEEK_CENTER:
				startCutoff = focus.get(Calendar.DAY_OF_WEEK) - 3;
				endCutoff = focus.get(Calendar.DAY_OF_WEEK) + 3;
				break;
			}
			break;
		default:
			throw new IllegalArgumentException("The range style " + rangeStyle + " is not valid.");
		}
		if (startCutoff < Calendar.SUNDAY)
		{
			startCutoff += 7;
		}
		if (startCutoff > Calendar.SATURDAY)
		{
			startCutoff -= 7;
		}
		if (endCutoff < Calendar.SUNDAY)
		{
			endCutoff += 7;
		}
		if (endCutoff > Calendar.SATURDAY)
		{
			endCutoff -= 7;
		}
		while (start.get(Calendar.DAY_OF_WEEK) != startCutoff)
		{
			start.add(Calendar.DATE, -1);
		}
		while (end.get(Calendar.DAY_OF_WEEK) != endCutoff)
		{
			end.add(Calendar.DATE, 1);
		}
		return new DateIterator(start, end);
	}

	/**
	 * <p>
	 * This constructs an <code>Iterator</code> over each day in a date range defined by a focus date and range style.
	 * </p>
	 * 
	 * <p>
	 * For instance, passing Thursday, July 4, 2002 and a <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002, returning a Calendar instance for each intermediate day.
	 * </p>
	 * 
	 * @param focus
	 *            the date to work with, either <code>Date</code> or <code>Calendar</code>
	 * @param rangeStyle
	 *            the style constant to use. Must be one of the range styles listed for the {@link #iterator(Calendar, int)} method.
	 * @return the date iterator
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code>
	 * @throws ClassCastException
	 *             if the object type is not a <code>Date</code> or <code>Calendar</code>
	 */
	public static Iterator iterator(Object focus, int rangeStyle)
	{
		if (focus == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		if (focus instanceof Date)
		{
			return iterator((Date) focus, rangeStyle);
		}
		else if (focus instanceof Calendar)
		{
			return iterator((Calendar) focus, rangeStyle);
		}
		else
		{
			throw new ClassCastException("Could not iterate based on " + focus);
		}
	}

	/**
	 * <p>
	 * Returns the number of milliseconds within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the milliseconds of any date will only return the number of milliseconds of the current second (resulting in a number between 0 and 999). This method will retrieve the number of milliseconds for any fragment. For example, if you want to calculate the number of milliseconds past today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result will be all milliseconds of the past hour(s), minutes(s) and second(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a SECOND field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10538 (10*1000 + 538)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in milliseconds)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @return number of milliseconds within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInMilliseconds(Date date, int fragment)
	{
		return getFragment(date, fragment, Calendar.MILLISECOND);
	}

	/**
	 * <p>
	 * Returns the number of seconds within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the seconds of any date will only return the number of seconds of the current minute (resulting in a number between 0 and 59). This method will retrieve the number of seconds for any fragment. For example, if you want to calculate the number of seconds past today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result will be all seconds of the past hour(s) and minutes(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a SECOND field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent to deprecated date.getSeconds())</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent to deprecated date.getSeconds())</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 26110 (7*3600 + 15*60 + 10)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in seconds)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @return number of seconds within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInSeconds(Date date, int fragment)
	{
		return getFragment(date, fragment, Calendar.SECOND);
	}

	/**
	 * <p>
	 * Returns the number of minutes within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the minutes of any date will only return the number of minutes of the current hour (resulting in a number between 0 and 59). This method will retrieve the number of minutes for any fragment. For example, if you want to calculate the number of minutes past this month, your fragment is Calendar.MONTH. The result will be all minutes of the past day(s) and hour(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a MINUTE field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15 (equivalent to deprecated date.getMinutes())</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15 (equivalent to deprecated date.getMinutes())</li>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 15</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 435 (7*60 + 15)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in minutes)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @return number of minutes within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInMinutes(Date date, int fragment)
	{
		return getFragment(date, fragment, Calendar.MINUTE);
	}

	/**
	 * <p>
	 * Returns the number of hours within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the hours of any date will only return the number of hours of the current day (resulting in a number between 0 and 23). This method will retrieve the number of hours for any fragment. For example, if you want to calculate the number of hours past this month, your fragment is Calendar.MONTH. The result will be all hours of the past day(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a HOUR field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7 (equivalent to deprecated date.getHours())</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7 (equivalent to deprecated date.getHours())</li>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 7</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 127 (5*24 + 7)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in hours)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @return number of hours within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInHours(Date date, int fragment)
	{
		return getFragment(date, fragment, Calendar.HOUR_OF_DAY);
	}

	/**
	 * <p>
	 * Returns the number of days within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the days of any date will only return the number of days of the current month (resulting in a number between 1 and 31). This method will retrieve the number of days for any fragment. For example, if you want to calculate the number of days past this year, your fragment is Calendar.YEAR. The result will be all days of the past month(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a DAY field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to deprecated date.getDay())</li>
	 * <li>February 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to deprecated date.getDay())</li>
	 * <li>January 28, 2008 with Calendar.YEAR as fragment will return 28</li>
	 * <li>February 28, 2008 with Calendar.YEAR as fragment will return 59</li>
	 * <li>January 28, 2008 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in days)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @return number of days within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInDays(Date date, int fragment)
	{
		return getFragment(date, fragment, Calendar.DAY_OF_YEAR);
	}

	/**
	 * <p>
	 * Returns the number of milliseconds within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the milliseconds of any date will only return the number of milliseconds of the current second (resulting in a number between 0 and 999). This method will retrieve the number of milliseconds for any fragment. For example, if you want to calculate the number of seconds past today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result will be all seconds of the past hour(s), minutes(s) and second(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a MILLISECOND field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538 (equivalent to calendar.get(Calendar.MILLISECOND))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538 (equivalent to calendar.get(Calendar.MILLISECOND))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10538 (10*1000 + 538)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in milliseconds)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @return number of milliseconds within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInMilliseconds(Calendar calendar, int fragment)
	{
		return getFragment(calendar, fragment, Calendar.MILLISECOND);
	}

	/**
	 * <p>
	 * Returns the number of seconds within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the seconds of any date will only return the number of seconds of the current minute (resulting in a number between 0 and 59). This method will retrieve the number of seconds for any fragment. For example, if you want to calculate the number of seconds past today, your fragment is Calendar.DATE or Calendar.DAY_OF_YEAR. The result will be all seconds of the past hour(s) and minutes(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a SECOND field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent to calendar.get(Calendar.SECOND))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10 (equivalent to calendar.get(Calendar.SECOND))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 26110 (7*3600 + 15*60 + 10)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in seconds)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @return number of seconds within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInSeconds(Calendar calendar, int fragment)
	{
		return getFragment(calendar, fragment, Calendar.SECOND);
	}

	/**
	 * <p>
	 * Returns the number of minutes within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the minutes of any date will only return the number of minutes of the current hour (resulting in a number between 0 and 59). This method will retrieve the number of minutes for any fragment. For example, if you want to calculate the number of minutes past this month, your fragment is Calendar.MONTH. The result will be all minutes of the past day(s) and hour(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a MINUTE field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15 (equivalent to calendar.get(Calendar.MINUTES))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15 (equivalent to calendar.get(Calendar.MINUTES))</li>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 15</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 435 (7*60 + 15)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in minutes)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @return number of minutes within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInMinutes(Calendar calendar, int fragment)
	{
		return getFragment(calendar, fragment, Calendar.MINUTE);
	}

	/**
	 * <p>
	 * Returns the number of hours within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the hours of any date will only return the number of hours of the current day (resulting in a number between 0 and 23). This method will retrieve the number of hours for any fragment. For example, if you want to calculate the number of hours past this month, your fragment is Calendar.MONTH. The result will be all hours of the past day(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a HOUR field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7 (equivalent to calendar.get(Calendar.HOUR_OF_DAY))</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7 (equivalent to calendar.get(Calendar.HOUR_OF_DAY))</li>
	 * <li>January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 7</li>
	 * <li>January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 127 (5*24 + 7)</li>
	 * <li>January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in hours)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @return number of hours within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInHours(Calendar calendar, int fragment)
	{
		return getFragment(calendar, fragment, Calendar.HOUR_OF_DAY);
	}

	/**
	 * <p>
	 * Returns the number of days within the fragment. All datefields greater than the fragment will be ignored.
	 * </p>
	 * 
	 * <p>
	 * Asking the days of any date will only return the number of days of the current month (resulting in a number between 1 and 31). This method will retrieve the number of days for any fragment. For example, if you want to calculate the number of days past this year, your fragment is Calendar.YEAR. The result will be all days of the past month(s).
	 * </p>
	 * 
	 * <p>
	 * Valid fragments are: Calendar.YEAR, Calendar.MONTH, both Calendar.DAY_OF_YEAR and Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND and Calendar.MILLISECOND A fragment less than or equal to a DAY field will return 0.
	 * </p>
	 * 
	 * <p>
	 * <ul>
	 * <li>January 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to calendar.get(Calendar.DAY_OF_MONTH))</li>
	 * <li>February 28, 2008 with Calendar.MONTH as fragment will return 28 (equivalent to calendar.get(Calendar.DAY_OF_MONTH))</li>
	 * <li>January 28, 2008 with Calendar.YEAR as fragment will return 28 (equivalent to calendar.get(Calendar.DAY_OF_YEAR))</li>
	 * <li>February 28, 2008 with Calendar.YEAR as fragment will return 59 (equivalent to calendar.get(Calendar.DAY_OF_YEAR))</li>
	 * <li>January 28, 2008 with Calendar.MILLISECOND as fragment will return 0 (a millisecond cannot be split in days)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @return number of days within the fragment of date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	public static long getFragmentInDays(Calendar calendar, int fragment)
	{
		return getFragment(calendar, fragment, Calendar.DAY_OF_YEAR);
	}

	/**
	 * Date-version for fragment-calculation in any unit
	 * 
	 * @param date
	 *            the date to work with, not null
	 * @param fragment
	 *            the Calendar field part of date to calculate
	 * @param unit
	 *            Calendar field defining the unit
	 * @return number of units within the fragment of the date
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	private static long getFragment(Date date, int fragment, int unit)
	{
		if (date == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return getFragment(calendar, fragment, unit);
	}

	/**
	 * Calendar-version for fragment-calculation in any unit
	 * 
	 * @param calendar
	 *            the calendar to work with, not null
	 * @param fragment
	 *            the Calendar field part of calendar to calculate
	 * @param unit
	 *            Calendar field defining the unit
	 * @return number of units within the fragment of the calendar
	 * @throws IllegalArgumentException
	 *             if the date is <code>null</code> or fragment is not supported
	 * @since 2.4
	 */
	private static long getFragment(Calendar calendar, int fragment, int unit)
	{
		if (calendar == null)
		{
			throw new IllegalArgumentException("The date must not be null");
		}
		long millisPerUnit = getMillisPerUnit(unit);
		long result = 0;

		// Fragments bigger than a day require a breakdown to days
		switch (fragment)
		{
		case Calendar.YEAR:
			result += (calendar.get(Calendar.DAY_OF_YEAR) * MILLIS_PER_DAY) / millisPerUnit;
			break;
		case Calendar.MONTH:
			result += (calendar.get(Calendar.DAY_OF_MONTH) * MILLIS_PER_DAY) / millisPerUnit;
			break;
		}

		switch (fragment)
		{
		// Number of days already calculated for these cases
		case Calendar.YEAR:
		case Calendar.MONTH:

			// The rest of the valid cases
		case Calendar.DAY_OF_YEAR:
		case Calendar.DATE:
			result += (calendar.get(Calendar.HOUR_OF_DAY) * MILLIS_PER_HOUR) / millisPerUnit;
		case Calendar.HOUR_OF_DAY:
			result += (calendar.get(Calendar.MINUTE) * MILLIS_PER_MINUTE) / millisPerUnit;
		case Calendar.MINUTE:
			result += (calendar.get(Calendar.SECOND) * MILLIS_PER_SECOND) / millisPerUnit;
		case Calendar.SECOND:
			result += (calendar.get(Calendar.MILLISECOND) * 1) / millisPerUnit;
			break;
		case Calendar.MILLISECOND:
			break;// never useful
		default:
			throw new IllegalArgumentException("The fragment " + fragment + " is not supported");
		}
		return result;
	}

	/**
	 * Returns the number of millis of a datefield, if this is a constant value
	 * 
	 * @param unit
	 *            A Calendar field which is a valid unit for a fragment
	 * @return number of millis
	 * @throws IllegalArgumentException
	 *             if date can't be represented in millisenconds
	 * @since 2.4
	 */
	private static long getMillisPerUnit(int unit)
	{
		long result = Long.MAX_VALUE;
		switch (unit)
		{
		case Calendar.DAY_OF_YEAR:
		case Calendar.DATE:
			result = MILLIS_PER_DAY;
			break;
		case Calendar.HOUR_OF_DAY:
			result = MILLIS_PER_HOUR;
			break;
		case Calendar.MINUTE:
			result = MILLIS_PER_MINUTE;
			break;
		case Calendar.SECOND:
			result = MILLIS_PER_SECOND;
			break;
		case Calendar.MILLISECOND:
			result = 1;
			break;
		default:
			throw new IllegalArgumentException("The unit " + unit + " cannot be represented is milleseconds");
		}
		return result;
	}

	/**
	 * <p>
	 * Date iterator.
	 * </p>
	 */
	static class DateIterator implements Iterator
	{
		private final Calendar endFinal;
		private final Calendar spot;

		/**
		 * Constructs a DateIterator that ranges from one date to another.
		 * 
		 * @param startFinal
		 *            start date (inclusive)
		 * @param endFinal
		 *            end date (not inclusive)
		 */
		DateIterator(Calendar startFinal, Calendar endFinal)
		{
			super();
			this.endFinal = endFinal;
			spot = startFinal;
			spot.add(Calendar.DATE, -1);
		}

		/**
		 * Has the iterator not reached the end date yet?
		 * 
		 * @return <code>true</code> if the iterator has yet to reach the end date
		 */
		public boolean hasNext()
		{
			return spot.before(endFinal);
		}

		/**
		 * Return the next calendar in the iteration
		 * 
		 * @return Object calendar for the next date
		 */
		public Object next()
		{
			if (spot.equals(endFinal))
			{
				throw new NoSuchElementException();
			}
			spot.add(Calendar.DATE, 1);
			return spot.clone();
		}

		/**
		 * Always throws UnsupportedOperationException.
		 * 
		 * @throws UnsupportedOperationException
		 * @see java.util.Iterator#remove()
		 */
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

}
