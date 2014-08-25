package feed.parser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.caudexorigo.time.ISO8601;

public class DateParser
{
	// Thu, 01 Jan 2004 19:48:21 GMT
	// Sun Jan 4 16:29:06 PST 2004
	// Mon, 26 January 2004 16:31:00 ET
	// Mon, 26 January 2004 16:31:00 AT
	// Mon, 26 January 2004 16:31:00 CT
	// Mon, 26 January 2004 16:31:00 MT
	// Mon, 26 January 2004 16:31:00 PT
	// -03-12
	// -0312
	// 03-12-31
	// 031231
	// 2003-335
	// 03335
	// Thu, 01 Jan 2004 19:48:21 GMT
	// Thu, 01 Jan 2004 19:48:21 GMT
	// Thu, 31 Jun 2004 19:48:21 GMT
	// Thu, 01 Jan 04 19:48:21 GMT
	// 2004-02-28T18:14:55-08:00
	// 2000-02-28T18:14:55-08:00
	// 2003-02-28T18:14:55-08:00
	// 2003-12-31T10:14:55-08:00
	// 2003-12-31T18:14:55+08:00
	// 2003-12-31T10:14:55Z
	// 2003
	// 2003-12
	// 2003-12-31
	// 20031231

	static String[] date_formats = { "EEE, d MMM yy kk:mm:ss z", // RFC822
			"EEE, d MMM yyyy kk:mm:ss z", // RFC2882
			"EEE, d MMM yyyy kk:mm z", // RFC2882 minus seconds
			"EEE MMM  d kk:mm:ss zzz yyyy", // ASC
			"EEE, dd MMMM yyyy kk:mm:ss", // Disney Mon, 26 January 2004
			"yyyy-MM-dd'T'kk:mm:ss'Z'", // ISO
			"yyyy-MM-dd'T'kk:mm:ssz", // ISO
			"yyyy-MM-dd'T'kk:mm:ss", // ISO
			"yyyy-MM-dd kk:mm:ss", // 2010-10-08 18:52:00
			"dd/MM/yyyy kk:mm", "dd-MM-yyyy kk:mm", // Sic 09-10-2010 00:36
			"dd MMM yy : kk:mm:ss", // RR 01 Sep 10 : 03:58:22
			"EEE, dd MMM yyyy", "dd-MM-yyyy", // 09-10-2010

			// 16:31:00 ET
			"yyyy-MM-dd kk:mm:ss.0", "-yy-MM", "-yyMM", "yy-MM-dd", "yyyy-MM-dd", "yyyy-MM", "yyyy-D", "-yyMM", "yyyyMMdd", "yyMMdd", "yyyy", "yyD"

	};

	// private static final Pattern p1 = Pattern.compile("([-+]\\d\\d:\\d\\d)");
	// private static final Pattern p2 = Pattern.compile(" ([ACEMP])T$");

	private static final ThreadLocal<SimpleDateFormat[]> sdf_local = new ThreadLocal<SimpleDateFormat[]>()
	{
		@Override
		protected SimpleDateFormat[] initialValue()
		{
			SimpleDateFormat[] date_formaters = new SimpleDateFormat[date_formats.length];

			for (int i = 0; i < date_formaters.length; i++)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(date_formats[i]);
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				date_formaters[i] = sdf;
			}

			return date_formaters;
		}
	};

	private static final ThreadLocal<SimpleDateFormat[]> sdf_pt_local = new ThreadLocal<SimpleDateFormat[]>()
	{
		private final Locale pt = Locale.forLanguageTag("pt-PT");

		@Override
		protected SimpleDateFormat[] initialValue()
		{
			SimpleDateFormat[] date_formaters = new SimpleDateFormat[date_formats.length];

			for (int i = 0; i < date_formaters.length; i++)
			{
				SimpleDateFormat sdf = new SimpleDateFormat(date_formats[i], pt);
				sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
				date_formaters[i] = sdf;
			}

			return date_formaters;
		}
	};

	public static Date parse(String d)
	{
		Date date = null;

		// d = d.replaceAll("([-+]\\d\\d:\\d\\d)", "GMT$1");
		// d = d.replaceAll(" ([ACEMP])T$", " $1ST"); // Correct Disney times
		// d = p1.matcher(d).replaceAll("GMT$1"); // Correct W3C times
		// d = p2.matcher(d).replaceAll(" $1ST"); // Correct W3C times

		SimpleDateFormat[] date_formaters = sdf_local.get();

		for (SimpleDateFormat formatter : date_formaters)
		{
			try
			{
				date = formatter.parse(d);
				break;
			}
			catch (Exception e)
			{
				// Oh well. We tried
			}
		}

		date_formaters = sdf_pt_local.get();

		for (SimpleDateFormat formatter : date_formaters)
		{
			try
			{
				date = formatter.parse(d);
				break;
			}
			catch (Exception e)
			{
				// Oh well. We tried
			}
		}

		return date;
	}

	public static void main(String[] args)
	{
		Date d = parse("Thu, 01 Jan 2004 19:48:21 GMT");

		if (d != null)
		{
			System.out.println(ISO8601.format(d));
		}
		else
		{
			System.out.println("Invalid date");
		}
	}
}