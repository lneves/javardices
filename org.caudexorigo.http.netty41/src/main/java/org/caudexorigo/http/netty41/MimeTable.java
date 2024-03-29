package org.caudexorigo.http.netty41;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.util.AsciiString;

public class MimeTable {
  private static final ConcurrentMap<String, CharSequence> TABLE =
      new ConcurrentHashMap<String, CharSequence>();

  static {
    TABLE.put("ai", new AsciiString("application/postscript"));
    TABLE.put("aif", new AsciiString("audio/x-aiff"));
    TABLE.put("aifc", new AsciiString("audio/x-aiff"));
    TABLE.put("aiff", new AsciiString("audio/x-aiff"));
    TABLE.put("asc", new AsciiString("text/plain"));
    TABLE.put("atom", new AsciiString("application/atom+xml"));
    TABLE.put("au", new AsciiString("audio/basic"));
    TABLE.put("avi", new AsciiString("video/x-msvideo"));
    TABLE.put("bcpio", new AsciiString("application/x-bcpio"));
    TABLE.put("bin", new AsciiString("application/octet-stream"));
    TABLE.put("bmp", new AsciiString("image/bmp"));
    TABLE.put("cdf", new AsciiString("application/x-netcdf"));
    TABLE.put("cgm", new AsciiString("image/cgm"));
    TABLE.put("class", new AsciiString("application/octet-stream"));
    TABLE.put("cpio", new AsciiString("application/x-cpio"));
    TABLE.put("cpt", new AsciiString("application/mac-compactpro"));
    TABLE.put("csh", new AsciiString("application/x-csh"));
    TABLE.put("css", new AsciiString("text/css"));
    TABLE.put("dcr", new AsciiString("application/x-director"));
    TABLE.put("dif", new AsciiString("video/x-dv"));
    TABLE.put("dir", new AsciiString("application/x-director"));
    TABLE.put("djv", new AsciiString("image/vnd.djvu"));
    TABLE.put("djvu", new AsciiString("image/vnd.djvu"));
    TABLE.put("dll", new AsciiString("application/octet-stream"));
    TABLE.put("dmg", new AsciiString("application/octet-stream"));
    TABLE.put("dms", new AsciiString("application/octet-stream"));
    TABLE.put("doc", new AsciiString("application/msword"));
    TABLE.put("dtd", new AsciiString("application/xml-dtd"));
    TABLE.put("dv", new AsciiString("video/x-dv"));
    TABLE.put("dvi", new AsciiString("application/x-dvi"));
    TABLE.put("dxr", new AsciiString("application/x-director"));
    TABLE.put("eps", new AsciiString("application/postscript"));
    TABLE.put("etx", new AsciiString("text/x-setext"));
    TABLE.put("exe", new AsciiString("application/octet-stream"));
    TABLE.put("ez", new AsciiString("application/andrew-inset"));
    TABLE.put("gif", new AsciiString("image/gif"));
    TABLE.put("gram", new AsciiString("application/srgs"));
    TABLE.put("grxml", new AsciiString("application/srgs+xml"));
    TABLE.put("gtar", new AsciiString("application/x-gtar"));
    TABLE.put("hdf", new AsciiString("application/x-hdf"));
    TABLE.put("hqx", new AsciiString("application/mac-binhex40"));
    TABLE.put("htm", new AsciiString("text/html; charset=UTF-8"));
    TABLE.put("html", new AsciiString("text/html; charset=UTF-8"));
    TABLE.put("ice", new AsciiString("x-conference/x-cooltalk"));
    TABLE.put("ico", new AsciiString("image/x-icon"));
    TABLE.put("ics", new AsciiString("text/calendar"));
    TABLE.put("ief", new AsciiString("image/ief"));
    TABLE.put("ifb", new AsciiString("text/calendar"));
    TABLE.put("iges", new AsciiString("model/iges"));
    TABLE.put("igs", new AsciiString("model/iges"));
    TABLE.put("jnlp", new AsciiString("application/x-java-jnlp-file"));
    TABLE.put("jp2", new AsciiString("image/jp2"));
    TABLE.put("jpe", new AsciiString("image/jpeg"));
    TABLE.put("jpeg", new AsciiString("image/jpeg"));
    TABLE.put("jpg", new AsciiString("image/jpeg"));
    TABLE.put("js", new AsciiString("application/x-javascript; charset=UTF-8"));
    TABLE.put("kar", new AsciiString("audio/midi"));
    TABLE.put("latex", new AsciiString("application/x-latex"));
    TABLE.put("lha", new AsciiString("application/octet-stream"));
    TABLE.put("lzh", new AsciiString("application/octet-stream"));
    TABLE.put("m3u", new AsciiString("audio/x-mpegurl"));
    TABLE.put("m4a", new AsciiString("audio/mp4a-latm"));
    TABLE.put("m4b", new AsciiString("audio/mp4a-latm"));
    TABLE.put("m4p", new AsciiString("audio/mp4a-latm"));
    TABLE.put("m4u", new AsciiString("video/vnd.mpegurl"));
    TABLE.put("m4v", new AsciiString("video/x-m4v"));
    TABLE.put("mac", new AsciiString("image/x-macpaint"));
    TABLE.put("man", new AsciiString("application/x-troff-man"));
    TABLE.put("mathml", new AsciiString("application/mathml+xml"));
    TABLE.put("me", new AsciiString("application/x-troff-me"));
    TABLE.put("mesh", new AsciiString("model/mesh"));
    TABLE.put("mid", new AsciiString("audio/midi"));
    TABLE.put("midi", new AsciiString("audio/midi"));
    TABLE.put("mif", new AsciiString("application/vnd.mif"));
    TABLE.put("mov", new AsciiString("video/quicktime"));
    TABLE.put("movie", new AsciiString("video/x-sgi-movie"));
    TABLE.put("mp2", new AsciiString("audio/mpeg"));
    TABLE.put("mp3", new AsciiString("audio/mpeg"));
    TABLE.put("mp4", new AsciiString("video/mp4"));
    TABLE.put("mpe", new AsciiString("video/mpeg"));
    TABLE.put("mpeg", new AsciiString("video/mpeg"));
    TABLE.put("mpg", new AsciiString("video/mpeg"));
    TABLE.put("mpga", new AsciiString("audio/mpeg"));
    TABLE.put("ms", new AsciiString("application/x-troff-ms"));
    TABLE.put("msh", new AsciiString("model/mesh"));
    TABLE.put("mxu", new AsciiString("video/vnd.mpegurl"));
    TABLE.put("nc", new AsciiString("application/x-netcdf"));
    TABLE.put("oda", new AsciiString("application/oda"));
    TABLE.put("ogg", new AsciiString("application/ogg"));
    TABLE.put("pbm", new AsciiString("image/x-portable-bitmap"));
    TABLE.put("pct", new AsciiString("image/pict"));
    TABLE.put("pdb", new AsciiString("chemical/x-pdb"));
    TABLE.put("pdf", new AsciiString("application/pdf"));
    TABLE.put("pgm", new AsciiString("image/x-portable-graymap"));
    TABLE.put("pgn", new AsciiString("application/x-chess-pgn"));
    TABLE.put("pic", new AsciiString("image/pict"));
    TABLE.put("pict", new AsciiString("image/pict"));
    TABLE.put("png", new AsciiString("image/png"));
    TABLE.put("pnm", new AsciiString("image/x-portable-anymap"));
    TABLE.put("pnt", new AsciiString("image/x-macpaint"));
    TABLE.put("pntg", new AsciiString("image/x-macpaint"));
    TABLE.put("ppm", new AsciiString("image/x-portable-pixmap"));
    TABLE.put("ppt", new AsciiString("application/vnd.ms-powerpoint"));
    TABLE.put("ps", new AsciiString("application/postscript"));
    TABLE.put("qt", new AsciiString("video/quicktime"));
    TABLE.put("qti", new AsciiString("image/x-quicktime"));
    TABLE.put("qtif", new AsciiString("image/x-quicktime"));
    TABLE.put("ra", new AsciiString("audio/x-pn-realaudio"));
    TABLE.put("ram", new AsciiString("audio/x-pn-realaudio"));
    TABLE.put("ras", new AsciiString("image/x-cmu-raster"));
    TABLE.put("rdf", new AsciiString("application/rdf+xml"));
    TABLE.put("rgb", new AsciiString("image/x-rgb"));
    TABLE.put("rm", new AsciiString("application/vnd.rn-realmedia"));
    TABLE.put("roff", new AsciiString("application/x-troff"));
    TABLE.put("rtf", new AsciiString("text/rtf"));
    TABLE.put("rtx", new AsciiString("text/richtext"));
    TABLE.put("sgm", new AsciiString("text/sgml"));
    TABLE.put("sgml", new AsciiString("text/sgml"));
    TABLE.put("sh", new AsciiString("application/x-sh"));
    TABLE.put("shar", new AsciiString("application/x-shar"));
    TABLE.put("silo", new AsciiString("model/mesh"));
    TABLE.put("sit", new AsciiString("application/x-stuffit"));
    TABLE.put("skd", new AsciiString("application/x-koan"));
    TABLE.put("skm", new AsciiString("application/x-koan"));
    TABLE.put("skp", new AsciiString("application/x-koan"));
    TABLE.put("skt", new AsciiString("application/x-koan"));
    TABLE.put("smi", new AsciiString("application/smil"));
    TABLE.put("smil", new AsciiString("application/smil"));
    TABLE.put("snd", new AsciiString("audio/basic"));
    TABLE.put("so", new AsciiString("application/octet-stream"));
    TABLE.put("spl", new AsciiString("application/x-futuresplash"));
    TABLE.put("src", new AsciiString("application/x-wais-source"));
    TABLE.put("sv4cpio", new AsciiString("application/x-sv4cpio"));
    TABLE.put("sv4crc", new AsciiString("application/x-sv4crc"));
    TABLE.put("svg", new AsciiString("image/svg+xml"));
    TABLE.put("swf", new AsciiString("application/x-shockwave-flash"));
    TABLE.put("t", new AsciiString("application/x-troff"));
    TABLE.put("tar", new AsciiString("application/x-tar"));
    TABLE.put("tcl", new AsciiString("application/x-tcl"));
    TABLE.put("tex", new AsciiString("application/x-tex"));
    TABLE.put("texi", new AsciiString("application/x-texinfo"));
    TABLE.put("texinfo", new AsciiString("application/x-texinfo"));
    TABLE.put("tif", new AsciiString("image/tiff"));
    TABLE.put("tiff", new AsciiString("image/tiff"));
    TABLE.put("tr", new AsciiString("application/x-troff"));
    TABLE.put("tsv", new AsciiString("text/tab-separated-values"));
    TABLE.put("txt", new AsciiString("text/plain"));
    TABLE.put("ustar", new AsciiString("application/x-ustar"));
    TABLE.put("vcd", new AsciiString("application/x-cdlink"));
    TABLE.put("vrml", new AsciiString("model/vrml"));
    TABLE.put("vxml", new AsciiString("application/voicexml+xml"));
    TABLE.put("wav", new AsciiString("audio/x-wav"));
    TABLE.put("wbmp", new AsciiString("image/vnd.wap.wbmp"));
    TABLE.put("wbmxl", new AsciiString("application/vnd.wap.wbxml"));
    TABLE.put("wml", new AsciiString("text/vnd.wap.wml"));
    TABLE.put("wmlc", new AsciiString("application/vnd.wap.wmlc"));
    TABLE.put("wmls", new AsciiString("text/vnd.wap.wmlscript"));
    TABLE.put("wmlsc", new AsciiString("application/vnd.wap.wmlscriptc"));
    TABLE.put("wrl", new AsciiString("model/vrml"));
    TABLE.put("wsdl", new AsciiString("application/xml"));
    TABLE.put("xbm", new AsciiString("image/x-xbitmap"));
    TABLE.put("xht", new AsciiString("application/xhtml+xml"));
    TABLE.put("xhtml", new AsciiString("application/xhtml+xml"));
    TABLE.put("xls", new AsciiString("application/vnd.ms-excel"));
    TABLE.put("xml", new AsciiString("application/xml"));
    TABLE.put("xpm", new AsciiString("image/x-xpixmap"));
    TABLE.put("xsl", new AsciiString("application/xml"));
    TABLE.put("xslt", new AsciiString("application/xslt+xml"));
    TABLE.put("xul", new AsciiString("application/vnd.mozilla.xul+xml"));
    TABLE.put("xwd", new AsciiString("image/x-xwindowdump"));
    TABLE.put("xyz", new AsciiString("chemical/x-xyz"));
    TABLE.put("otf", new AsciiString("application/vnd.ms-opentype"));
    TABLE.put("eot", new AsciiString("application/vnd.ms-fontobject"));
    TABLE.put("ttf", new AsciiString("application/x-font-ttf"));
    TABLE.put("woff", new AsciiString("application/octet-stream"));
    TABLE.put("woff2", new AsciiString("application/font-woff2"));

    TABLE.put("json", new AsciiString("application/json"));
  }

  public static final CharSequence getContentType(String file) {
    String extension = StringUtils.substringAfterLast(file, ".");
    return TABLE.get(extension);
  }
}
