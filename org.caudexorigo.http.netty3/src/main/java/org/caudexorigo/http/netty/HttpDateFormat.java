package org.caudexorigo.http.netty;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HttpDateFormat
{

	/**
	 * By default, we update the format if it is more than a second old
	 */
	private static final int DEFAULT_GRANULARITY = 1000;

	private static int granularity = DEFAULT_GRANULARITY;

	private static final String DEFAULT_TIME_ZONE_NAME = "GMT";

	/**
	 * Thread local <code>HttpDateFormat</code>
	 */
	private static final ThreadLocal<HttpDateFormat> FORMAT_LOCAL = new ThreadLocal<HttpDateFormat>()
	{
		@Override
		protected HttpDateFormat initialValue()
		{
			return new HttpDateFormat();
		}
	};

	/**
	 * Format for HTTP dates
	 */
	private final SimpleDateFormat dateFormat;

	/**
	 * The time of the last format operation (0 if none have yet taken place)
	 */
	private long timeLastGenerated;

	/**
	 * The current formatted HTTP date
	 */
	private String currentHTTPDate;

	private HttpDateFormat()
	{
		// HTTP date format specifies GMT
		dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone(DEFAULT_TIME_ZONE_NAME));
	}

	/**
	 * Returns the current time formatted as specified in the HTTP 1.1
	 * specification.
	 * 
	 * @return The formatted date
	 */
	public static String getCurrentHttpDate()
	{
		return FORMAT_LOCAL.get().getCurrentDate();
	}

	public static String getHttpDate(Date d)
	{
		return FORMAT_LOCAL.get().getDate(d);
	}

	/**
	 * Provides the current formatted date to be employed. If we haven't updated
	 * our view of the time in the last 'granularity' ms, we format a fresh
	 * value.
	 * 
	 * @return The current http date
	 */
	private String getCurrentDate()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime - timeLastGenerated > granularity)
		{
			timeLastGenerated = currentTime;
			currentHTTPDate = dateFormat.format(new Date(currentTime));
		}
		return currentHTTPDate;
	}

	private String getDate(Date d)
	{
		return dateFormat.format(d);
	}

}