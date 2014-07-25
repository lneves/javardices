package org.caudexorigo.http.netty;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

public class MimeTable
{
	private static final ConcurrentMap<String, String> TABLE = new ConcurrentHashMap<String, String>();

	static
	{
		TABLE.put("ai", "application/postscript");
		TABLE.put("aif", "audio/x-aiff");
		TABLE.put("aifc", "audio/x-aiff");
		TABLE.put("aiff", "audio/x-aiff");
		TABLE.put("asc", "text/plain");
		TABLE.put("atom", "application/atom+xml");
		TABLE.put("au", "audio/basic");
		TABLE.put("avi", "video/x-msvideo");
		TABLE.put("bcpio", "application/x-bcpio");
		TABLE.put("bin", "application/octet-stream");
		TABLE.put("bmp", "image/bmp");
		TABLE.put("cdf", "application/x-netcdf");
		TABLE.put("cgm", "image/cgm");
		TABLE.put("class", "application/octet-stream");
		TABLE.put("cpio", "application/x-cpio");
		TABLE.put("cpt", "application/mac-compactpro");
		TABLE.put("csh", "application/x-csh");
		TABLE.put("css", "text/css");
		TABLE.put("dcr", "application/x-director");
		TABLE.put("dif", "video/x-dv");
		TABLE.put("dir", "application/x-director");
		TABLE.put("djv", "image/vnd.djvu");
		TABLE.put("djvu", "image/vnd.djvu");
		TABLE.put("dll", "application/octet-stream");
		TABLE.put("dmg", "application/octet-stream");
		TABLE.put("dms", "application/octet-stream");
		TABLE.put("doc", "application/msword");
		TABLE.put("dtd", "application/xml-dtd");
		TABLE.put("dv", "video/x-dv");
		TABLE.put("dvi", "application/x-dvi");
		TABLE.put("dxr", "application/x-director");
		TABLE.put("eps", "application/postscript");
		TABLE.put("etx", "text/x-setext");
		TABLE.put("exe", "application/octet-stream");
		TABLE.put("ez", "application/andrew-inset");
		TABLE.put("gif", "image/gif");
		TABLE.put("gram", "application/srgs");
		TABLE.put("grxml", "application/srgs+xml");
		TABLE.put("gtar", "application/x-gtar");
		TABLE.put("hdf", "application/x-hdf");
		TABLE.put("hqx", "application/mac-binhex40");
		TABLE.put("htm", "text/html; charset=UTF-8");
		TABLE.put("html", "text/html; charset=UTF-8");
		TABLE.put("ice", "x-conference/x-cooltalk");
		TABLE.put("ico", "image/x-icon");
		TABLE.put("ics", "text/calendar");
		TABLE.put("ief", "image/ief");
		TABLE.put("ifb", "text/calendar");
		TABLE.put("iges", "model/iges");
		TABLE.put("igs", "model/iges");
		TABLE.put("jnlp", "application/x-java-jnlp-file");
		TABLE.put("jp2", "image/jp2");
		TABLE.put("jpe", "image/jpeg");
		TABLE.put("jpeg", "image/jpeg");
		TABLE.put("jpg", "image/jpeg");
		TABLE.put("js", "application/x-javascript");
		TABLE.put("kar", "audio/midi");
		TABLE.put("latex", "application/x-latex");
		TABLE.put("lha", "application/octet-stream");
		TABLE.put("lzh", "application/octet-stream");
		TABLE.put("m3u", "audio/x-mpegurl");
		TABLE.put("m4a", "audio/mp4a-latm");
		TABLE.put("m4b", "audio/mp4a-latm");
		TABLE.put("m4p", "audio/mp4a-latm");
		TABLE.put("m4u", "video/vnd.mpegurl");
		TABLE.put("m4v", "video/x-m4v");
		TABLE.put("mac", "image/x-macpaint");
		TABLE.put("man", "application/x-troff-man");
		TABLE.put("mathml", "application/mathml+xml");
		TABLE.put("me", "application/x-troff-me");
		TABLE.put("mesh", "model/mesh");
		TABLE.put("mid", "audio/midi");
		TABLE.put("midi", "audio/midi");
		TABLE.put("mif", "application/vnd.mif");
		TABLE.put("mov", "video/quicktime");
		TABLE.put("movie", "video/x-sgi-movie");
		TABLE.put("mp2", "audio/mpeg");
		TABLE.put("mp3", "audio/mpeg");
		TABLE.put("mp4", "video/mp4");
		TABLE.put("mpe", "video/mpeg");
		TABLE.put("mpeg", "video/mpeg");
		TABLE.put("mpg", "video/mpeg");
		TABLE.put("mpga", "audio/mpeg");
		TABLE.put("ms", "application/x-troff-ms");
		TABLE.put("msh", "model/mesh");
		TABLE.put("mxu", "video/vnd.mpegurl");
		TABLE.put("nc", "application/x-netcdf");
		TABLE.put("oda", "application/oda");
		TABLE.put("ogg", "application/ogg");
		TABLE.put("pbm", "image/x-portable-bitmap");
		TABLE.put("pct", "image/pict");
		TABLE.put("pdb", "chemical/x-pdb");
		TABLE.put("pdf", "application/pdf");
		TABLE.put("pgm", "image/x-portable-graymap");
		TABLE.put("pgn", "application/x-chess-pgn");
		TABLE.put("pic", "image/pict");
		TABLE.put("pict", "image/pict");
		TABLE.put("png", "image/png");
		TABLE.put("pnm", "image/x-portable-anymap");
		TABLE.put("pnt", "image/x-macpaint");
		TABLE.put("pntg", "image/x-macpaint");
		TABLE.put("ppm", "image/x-portable-pixmap");
		TABLE.put("ppt", "application/vnd.ms-powerpoint");
		TABLE.put("ps", "application/postscript");
		TABLE.put("qt", "video/quicktime");
		TABLE.put("qti", "image/x-quicktime");
		TABLE.put("qtif", "image/x-quicktime");
		TABLE.put("ra", "audio/x-pn-realaudio");
		TABLE.put("ram", "audio/x-pn-realaudio");
		TABLE.put("ras", "image/x-cmu-raster");
		TABLE.put("rdf", "application/rdf+xml");
		TABLE.put("rgb", "image/x-rgb");
		TABLE.put("rm", "application/vnd.rn-realmedia");
		TABLE.put("roff", "application/x-troff");
		TABLE.put("rtf", "text/rtf");
		TABLE.put("rtx", "text/richtext");
		TABLE.put("sgm", "text/sgml");
		TABLE.put("sgml", "text/sgml");
		TABLE.put("sh", "application/x-sh");
		TABLE.put("shar", "application/x-shar");
		TABLE.put("silo", "model/mesh");
		TABLE.put("sit", "application/x-stuffit");
		TABLE.put("skd", "application/x-koan");
		TABLE.put("skm", "application/x-koan");
		TABLE.put("skp", "application/x-koan");
		TABLE.put("skt", "application/x-koan");
		TABLE.put("smi", "application/smil");
		TABLE.put("smil", "application/smil");
		TABLE.put("snd", "audio/basic");
		TABLE.put("so", "application/octet-stream");
		TABLE.put("spl", "application/x-futuresplash");
		TABLE.put("src", "application/x-wais-source");
		TABLE.put("sv4cpio", "application/x-sv4cpio");
		TABLE.put("sv4crc", "application/x-sv4crc");
		TABLE.put("svg", "image/svg+xml");
		TABLE.put("swf", "application/x-shockwave-flash");
		TABLE.put("t", "application/x-troff");
		TABLE.put("tar", "application/x-tar");
		TABLE.put("tcl", "application/x-tcl");
		TABLE.put("tex", "application/x-tex");
		TABLE.put("texi", "application/x-texinfo");
		TABLE.put("texinfo", "application/x-texinfo");
		TABLE.put("tif", "image/tiff");
		TABLE.put("tiff", "image/tiff");
		TABLE.put("tr", "application/x-troff");
		TABLE.put("tsv", "text/tab-separated-values");
		TABLE.put("txt", "text/plain");
		TABLE.put("ustar", "application/x-ustar");
		TABLE.put("vcd", "application/x-cdlink");
		TABLE.put("vrml", "model/vrml");
		TABLE.put("vxml", "application/voicexml+xml");
		TABLE.put("wav", "audio/x-wav");
		TABLE.put("wbmp", "image/vnd.wap.wbmp");
		TABLE.put("wbmxl", "application/vnd.wap.wbxml");
		TABLE.put("wml", "text/vnd.wap.wml");
		TABLE.put("wmlc", "application/vnd.wap.wmlc");
		TABLE.put("wmls", "text/vnd.wap.wmlscript");
		TABLE.put("wmlsc", "application/vnd.wap.wmlscriptc");
		TABLE.put("wrl", "model/vrml");
		TABLE.put("wsdl", "application/xml");
		TABLE.put("xbm", "image/x-xbitmap");
		TABLE.put("xht", "application/xhtml+xml");
		TABLE.put("xhtml", "application/xhtml+xml; charset=UTF-8");
		TABLE.put("xls", "application/vnd.ms-excel");
		TABLE.put("xml", "application/xml");
		TABLE.put("xpm", "image/x-xpixmap");
		TABLE.put("xsl", "application/xml");
		TABLE.put("xslt", "application/xslt+xml");
		TABLE.put("xul", "application/vnd.mozilla.xul+xml");
		TABLE.put("xwd", "image/x-xwindowdump");
		TABLE.put("xyz", "chemical/x-xyz");
		TABLE.put("zip", "application/zip");
		TABLE.put("otf", "application/vnd.ms-opentype");
		TABLE.put("eot", "application/vnd.ms-fontobject");
		TABLE.put("ttf", "application/x-font-ttf");
		TABLE.put("woff", "application/octet-stream");
	}

	public static final String getContentType(String file)
	{
		String extension = StringUtils.substringAfterLast(file, ".");
		return TABLE.get(extension);
	}

}
