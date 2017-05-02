package org.caudexorigo.time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class ISO8601
{
	private static final TimeZone timeZone = TimeZone.getTimeZone("UTC");
	private static final TimeZone localTimeZone = TimeZone.getDefault();

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
		calendar.setLenient(false);
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
	public static Date parse(String isodate) throws IllegalArgumentException
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

	public static String format(Date date)
	{
		return sdf.get().format(date);
	}

	public static String formatLocalTz(Date date)
	{
		return sdf_local.get().format(date);
	}

	public static void test(String isodate)
	{
		System.out.println("----------------------------------");
		try
		{
			Date date = parse(isodate);
			System.out.println(">> " + isodate);
			System.out.println(">> " + date.toString() + " [" + date.getTime() + "]");
			System.out.println(">> " + format(date));
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
			isodate = format(date);
			System.out.println(">> " + isodate);
			date = parse(isodate);
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
}