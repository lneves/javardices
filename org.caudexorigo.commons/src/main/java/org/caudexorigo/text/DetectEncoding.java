package org.caudexorigo.text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.caudexorigo.io.UnsynchronizedBufferedReader;
import org.caudexorigo.io.UnsynchronizedByteArrayInputStream;

/**
 * Class to detect the encoding of the input
 * 
 * @see http://diveintomark.org/archives/2004/02/13/xml-media-types
 * @author david
 * 
 */

public class DetectEncoding
{

	class Magic
	{
		byte[] magic;
		boolean hasBOM;
		String name;

		public Magic(String name, boolean hasBOM, byte a, byte b, byte c, byte d)
		{
			this.magic = new byte[4];
			this.magic[0] = a;
			this.magic[1] = b;
			this.magic[2] = c;
			this.magic[3] = d;
			this.hasBOM = hasBOM;
			this.name = name;
		}

		public Magic(String name, boolean hasBOM, byte a, byte b, byte c)
		{
			this.magic = new byte[3];
			this.magic[0] = a;
			this.magic[1] = b;
			this.magic[2] = c;
			this.hasBOM = hasBOM;
			this.name = name;
		}

		public Magic(String name, boolean hasBOM, byte a, byte b)
		{
			this.magic = new byte[2];
			this.magic[0] = a;
			this.magic[1] = b;
			this.hasBOM = hasBOM;
			this.name = name;
		}
	}

	final Magic[] magics = {
			// UCS-4, big-endian machine (1234 order)
			new Magic("utf-32be", true, (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF),

			// UCS-4, little-endian machine (4321 order)
			new Magic("utf-32le", true, (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00),

			// UCS-4, unusual octet order (2143)
			new Magic("USC-4odd", true, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE),

			// UCS-4, unusual octet order (3412)
			new Magic("USC-4odder", true, (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00),

			// UTF-16, big-endian
			new Magic("utf-16be", true, (byte) 0xFE, (byte) 0xFF),

			// UTF-16, little-endian
			new Magic("utf-16le", true, (byte) 0xFF, (byte) 0xFE),

			// UTF-8
			new Magic("utf-8", true, (byte) 0xEF, (byte) 0xBB, (byte) 0xBF),

			// UCS-4 or other encoding with a 32-bit code unit and ASCII
			// characters encoded as ASCII values, in respectively big-endian
			// (1234), little-endian (4321) and two unusual byte orders (2143
			// and 3412). The encoding declaration must be read to determine
			// which of UCS-4 or other supported 32-bit encodings applies.
			new Magic("utf-32be", false, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x3C), new Magic("utf-32le", false, (byte) 0x3C, (byte) 0x00, (byte) 0x00, (byte) 0x00), new Magic("USC-4 ASCII 2143", false, (byte) 0x00, (byte) 0x00, (byte) 0x3C, (byte) 0x00), new Magic("USC-4 ASCII 3412", false, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x00),

			// UTF-16BE or big-endian ISO-10646-UCS-2 or other encoding with a
			// 16-bit code unit in big-endian order and ASCII characters encoded
			// as ASCII values (the encoding declaration must be read to
			// determine which)
			new Magic("utf-16be", false, (byte) 0x00, (byte) 0x3C, (byte) 0x00, (byte) 0x3F),

			// UTF-16LE or little-endian ISO-10646-UCS-2 or other
			// encoding with a 16-bit code unit in little-endian order
			// and ASCII characters encoded as ASCII values (the
			// encoding declaration must be read to determine which)
			new Magic("utf-16le", false, (byte) 0x3C, (byte) 0x00, (byte) 0x3F, (byte) 0x00),

			// UTF-8, ISO 646, ASCII, some part of ISO 8859, Shift-JIS,
			// EUC, or any other 7-bit, 8-bit, or mixed-width encoding
			// which ensures that the characters of ASCII have their
			// normal positions, width, and values; the actual encoding
			// declaration must be read to detect which of these
			// applies, but since all of these encodings use the same
			// bit patterns for the relevant ASCII characters, the
			// encoding declaration itself may be read reliably
			new Magic("UTF-8", false, (byte) 0x3C, (byte) 0x3F, (byte) 0x78, (byte) 0x6D),

			// EBCDIC (in some flavor; the full encoding declaration
			// must be read to tell which code page is in use)
			new Magic("EBCDIC", false, (byte) 0x4C, (byte) 0x6F, (byte) 0xA7, (byte) 0x94),

	// Other UTF-8 without an encoding declaration, or else the
	// data stream is mislabeled (lacking a required encoding
	// declaration), corrupt, fragmentary, or enclosed in a
	// wrapper of some kind
	};

	static Map<String, String> encoding_map = getEncodingMap();

	String defaultEncoding;
	private boolean error = false;

	private int bomLength = 0;

	public DetectEncoding(String defaultEncoding)
	{
		this.defaultEncoding = defaultEncoding;
	}

	public String detect(String filename) throws FileNotFoundException
	{
		return detect(new FileInputStream(filename));
	}

	private static Map<String, String> getEncodingMap()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("windows_1250", "windows-1250");
		map.put("windows_1251", "windows-1251");
		map.put("windows_1252", "windows-1252");
		map.put("windows_1253", "windows-1253");
		map.put("windows_1254", "windows-1254");
		map.put("windows_1255", "windows-1255");
		map.put("windows_1256", "windows-1256");
		map.put("windows_1257", "windows-1257");
		map.put("windows_1258", "windows-1258");
		map.put("ms-ee", "windows-1250");
		map.put("ms-cyrl", "windows-1251");
		map.put("ms-ansi", "windows-1252");
		map.put("ms-greek", "windows-1253");
		map.put("ms-turk", "windows-1254");
		map.put("ms-hebr", "windows-1255");
		map.put("ms-arab", "windows-1256");
		map.put("winbaltrim", "windows-1257");
		map.put("maclatin2", "x-MacRoman");
		map.put("macintosh", "x-MacRoman");
		map.put("csmacintosh", "x-MacRoman");
		map.put("mac", "x-MacRoman");
		map.put("mac-cyrillic", "x-MacCyrillic");
		map.put("us-ascii", "US-ASCII");

		map.put("cspc775baltic", "IBM775");
		map.put("iso-10646-ucs-2", "utf-16be");

		map.put("ebcdic_cp_be", "CP500");
		map.put("ebcdic_cp_us", "IBM037");
		map.put("ebcdic_cp_ca", "IBM037");
		map.put("ebcdic_cp_nl", "IBM037");
		map.put("ebcdic_cp_wt", "IBM037");
		map.put("ebcdic_cp_dk", "CP277");
		map.put("ebcdic_cp_no", "CP277");
		map.put("ebcdic_cp_fi", "CP278");
		map.put("ebcdic_cp_se", "CP278");
		map.put("ebcdic_cp_it", "CP280");
		map.put("ebcdic_cp_es", "CP284");
		map.put("ebcdic_cp_gb", "CP285");
		map.put("ebcdic_cp_fr", "CP297");
		map.put("ebcdic_cp_ch", "CP500");
		map.put("ebcdic_cp_be", "CP500");

		map.put("ebcdic-cp-be", "CP500");
		map.put("ebcdic-cp-us", "IBM037");
		map.put("ebcdic-cp-ca", "IBM037");
		map.put("ebcdic-cp-nl", "IBM037");
		map.put("ebcdic-cp-wt", "IBM037");
		map.put("ebcdic-cp-dk", "CP277");
		map.put("ebcdic-cp-no", "CP277");
		map.put("ebcdic-cp-fi", "CP278");
		map.put("ebcdic-cp-se", "CP278");
		map.put("ebcdic-cp-it", "CP280");
		map.put("ebcdic-cp-es", "CP284");
		map.put("ebcdic-cp-gb", "CP285");
		map.put("ebcdic-cp-fr", "CP297");
		map.put("ebcdic-cp-ch", "CP500");
		map.put("ebcdic-cp-be", "CP500");
		map.put("ibm039", "IBM037");
		map.put("ibm1140", "IBM037");
		map.put("dbcs", "CP1252");

		// This isn't really true, but it's as close as we're going to get
		// http://www.haible.de/bruno/charsets/conversion-tables/CP1125.html
		map.put("cp1125", "CP866");

		map.put("cp_is", "cp861");
		map.put("IBM277", "CP277");
		map.put("CP277", "CP277");
		map.put("CSIBM277", "CP277");

		map.put("IBM278", "CP278");
		map.put("CP278", "CP278");
		map.put("CSIBM278", "CP278");

		map.put("IBM280", "CP280");
		map.put("CP280", "CP280");
		map.put("CSIBM280", "CP280");

		map.put("IBM284", "CP284");
		map.put("CP284", "CP284");
		map.put("CSIBM284", "CP284");

		map.put("IBM285", "CP285");
		map.put("CP285", "CP285");
		map.put("CSIBM285", "CP285");

		map.put("IBM297", "CP297");
		map.put("CP297", "CP297");
		map.put("CSIBM297", "CP297");

		map.put("IBM420", "CP420");
		map.put("CP420", "CP420");
		map.put("CSIBM420", "CP420");
		map.put("ebcdic-cp-ar1", "CP420");
		map.put("ebcdic-cp-he", "CP424");

		map.put("IBM424", "CP424");
		map.put("CP424", "CP424");
		map.put("CSIBM424", "CP424");

		map.put("IBM500", "CP500");
		map.put("CP500", "CP500");
		map.put("CSIBM500", "CP500");

		map.put("csibm855", "CP855");

		map.put("IBM868", "CP868");
		map.put("CP868", "CP868");
		map.put("CSIBM868", "CP868");
		map.put("CP-AR", "CP868");
		map.put("IBM869", "CP869");
		map.put("CP869", "CP869");
		map.put("CSIBM869", "CP869");
		map.put("CP-GR", "CP869");
		map.put("IBM870", "CP870");
		map.put("CP870", "CP870");
		map.put("CSIBM870", "CP870");

		map.put("IBM871", "CP871");
		map.put("CP871", "CP871");
		map.put("CSIBM871", "CP871");
		map.put("ebcdic-cp-is", "CP871");
		map.put("IBM918", "CP918");
		map.put("CP918", "CP918");
		map.put("CSIBM918", "CP918");
		map.put("ebcdic-cp-ar2", "CP918");
		map.put("EUC-JP", "EUCJIS");
		map.put("CSEUCPkdFmtJapanese", "EUCJIS");
		map.put("EUC-KR", "KSC5601");
		map.put("GB2312", "GB2312");
		map.put("CSGB2312", "GB2312");
		map.put("ISO-2022-JP", "JIS");
		map.put("CSISO2022JP", "JIS");
		map.put("ISO-2022-KR", "ISO2022KR");
		map.put("CSISO2022KR", "ISO2022KR");
		map.put("ISO-2022-CN", "ISO2022CN");
		return map;
	}

	public String detect(InputStream in)
	{
		byte[] data = new byte[100];
		try
		{

			in.read(data, 0, 100);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return detect(data);
	}

	public String detect(byte[] data)
	{
		String encoding = null;

		// check for a <? as the first two byte
		if (data[0] == 0x3c && data[1] == 0x3f)
		{
			encoding = detectUsingXML(data);
		}
		else
		{
			encoding = detectUsingMagic(data);
			if ("EBCDIC".equals(encoding))
			{
				encoding = detectEBCDIC(data);
			}
		}
		encoding = alias(encoding);
		if (encoding == null)
		{
			encoding = defaultEncoding;
		}
		try
		{
			Charset charset = Charset.forName(encoding);
		}
		catch (UnsupportedCharsetException ex)
		{
			encoding = defaultEncoding;
			error = true;
		}

		return encoding;
	}

	private String detectEBCDIC(byte[] data)
	{
		data = asciiToEbcdic(data);
		return detectUsingXML(data);
	}

	public String alias(final String encoding)
	{
		String new_encoding = encoding;
		if (encoding_map.containsKey(encoding))
		{
			new_encoding = encoding_map.get(encoding);
		}
		return new_encoding;
	}

	private String detectUsingXML(byte[] data)
	{
		String encoding = null;
		try
		{

			UnsynchronizedBufferedReader reader = new UnsynchronizedBufferedReader(new InputStreamReader(new UnsynchronizedByteArrayInputStream(data)));

			String str = reader.readLine();

			Pattern pattern = Pattern.compile(".*encoding=\"([^\"]*)\".*");
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches())
			{
				encoding = matcher.group(1);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return encoding;
	}

	private String detectUsingMagic(byte[] data)
	{
		String encoding = null;
		int index = 0;
		for (Magic magic : magics)
		{
			if (checkArray(data, magic.magic))
			{
				encoding = magic.name;
				if (magic.hasBOM)
				{
					bomLength = magic.magic.length;
				}
				break;
			}
			index++;
		}
		return encoding;
	}

	private boolean checkArray(byte[] a, byte[] b)
	{
		boolean equals = true;
		for (int i = 0; i < b.length; ++i)
		{
			if (a[i] != b[i])
			{
				equals = false;
				break;
			}
		}
		return equals;
	}

	private byte[] asciiToEbcdic(byte[] data)
	{
		int[] map = { 0, 1, 2, 3, 156, 9, 134, 127, 151, 141, 142, 11, 12, 13, 14, 15, 16, 17, 18, 19, 157, 133, 8, 135, 24, 25, 146, 143, 28, 29, 30, 31, 128, 129, 130, 131, 132, 10, 23, 27, 136, 137, 138, 139, 140, 5, 6, 7, 144, 145, 22, 147, 148, 149, 150, 4, 152, 153, 154, 155, 20, 21, 158, 26, 32, 160, 161, 162, 163, 164, 165, 166, 167, 168, 91, 46, 60, 40, 43, 33, 38, 169, 170, 171, 172, 173, 174, 175, 176, 177, 93, 36, 42, 41, 59, 94, 45, 47, 178, 179, 180, 181, 182, 183, 184, 185, 124, 44, 37, 95, 62, 63, 186, 187, 188, 189, 190, 191, 192, 193, 194, 96, 58, 35, 64, 39, 61, 34, 195, 97, 98, 99, 100, 101, 102, 103, 104, 105, 196, 197, 198, 199, 200, 201, 202, 106, 107, 108, 109, 110, 111, 112, 113, 114, 203, 204, 205, 206, 207, 208, 209, 126, 115, 116, 117, 118, 119, 120, 121, 122,
				210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 123, 65, 66, 67, 68, 69, 70, 71, 72, 73, 232, 233, 234, 235, 236, 237, 125, 74, 75, 76, 77, 78, 79, 80, 81, 82, 238, 239, 240, 241, 242, 243, 92, 159, 83, 84, 85, 86, 87, 88, 89, 90, 244, 245, 246, 247, 248, 249, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 250, 251, 252, 253, 254, 255 };

		for (int i = 0; i < data.length; i++)
		{

			data[i] = (byte) map[data[i] > 0 ? data[i] : 256 + data[i]];
		}
		return data;
	}

	public void stripBOM(InputStream is) throws IOException
	{
		for (int i = 0; i < bomLength; i++)
		{
			is.read();
		}
	}

	public boolean isError()
	{
		return error;
	}

}
