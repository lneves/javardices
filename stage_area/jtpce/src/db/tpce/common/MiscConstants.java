package db.tpce.common;

public class MiscConstants
{
	public static final long iTIdentShift = 4300000000L; // 4.3 billion

	public static final char[] UpperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	public static final char[] LowerCaseLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	public static final char[] Numerals = "0123456789".toCharArray();
	
	public static final int SLOW_QUERY_THRESHOLD = 250;
}
