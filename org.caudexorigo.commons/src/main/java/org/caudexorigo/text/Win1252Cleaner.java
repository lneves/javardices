package org.caudexorigo.text;

import org.apache.commons.lang3.StringUtils;

/**
 * Replaces Windows-1252 'BOGUS characters' to a UTF-8 printable characters.
 */
public class Win1252Cleaner
{
	private static final String BOGUS_STRING;
	private static final int BOGUS_START = 128;
	private static final int BOGUS_END = 159;

	// Replacements suggested by http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WindowsBestFit/bestfit1252.txt except for codes 129, 141, 143, 144 and 157 ( Control Chars)
	// where the 'Replacement character' was used (0xfffd)
	private static char[] REPLACEMENT_CHARS = { 0x20ac, 0xfffd, 0x201a, 0x0192, 0x201e, 0x2026, 0x2020, 0x2021, 0x02c6, 0x2030, 0x0160, 0x2039, 0x0152, 0xfffd, 0x017d, 0xfffd, 0xfffd, 0x2018, 0x2019, 0x201c, 0x201d, 0x2022, 0x2013, 0x2014, 0x02dc, 0x2122, 0x0161, 0x203a, 0x0153, 0xfffd, 0x017e, 0x0178 };

	static
	{
		StringBuilder sb = new StringBuilder();
		for (int i = BOGUS_START; i <= BOGUS_END; ++i)
		{
			sb.append((char) i);
		}

		BOGUS_STRING = sb.toString();
	}

	private static String replaceBadchars(String text)
	{
		char[] chars = text.toCharArray();
		char[] out = new char[text.length()];

		for (int i = 0; i != chars.length; ++i)
		{
			char c = chars[i];

			if ((c >= BOGUS_START) && (c <= BOGUS_END))
			{
				int index = c - BOGUS_START;
				c = REPLACEMENT_CHARS[index];
			}

			out[i] = c;
		}

		return new String(out);
	}

	public static String clean(String text)
	{
		if (StringUtils.isBlank(text))
		{
			return text;
		}
		return replaceBadchars(text);
	}

	public static void main(String[] args)
	{
		testClean();
	}

	private static void testClean()
	{
		System.out.println("Bogus chars: " + BOGUS_STRING);
		System.out.println("Fixed chars: " + clean(BOGUS_STRING));

		String bogus_text = "Núcleo da Liga Portuguesa Contra o Cancro abre em Abrantes. OJL Redux toca hoje nOs Artistas";
		System.out.println("Bogus chars: " + bogus_text);
		System.out.println("Fixed chars: " + clean(bogus_text));
	}
}