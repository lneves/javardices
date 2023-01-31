package org.caudexorigo.http.netty4;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.netty.handler.codec.http.HttpHeaders;

public class MimeTable {
  private static final ConcurrentMap<String, CharSequence> TABLE =
      new ConcurrentHashMap<String, CharSequence>();

  static {
    TABLE.put("ai", HttpHeaders.newEntity("application/postscript"));
    TABLE.put("aif", HttpHeaders.newEntity("audio/x-aiff"));
    TABLE.put("aifc", HttpHeaders.newEntity("audio/x-aiff"));
    TABLE.put("aiff", HttpHeaders.newEntity("audio/x-aiff"));
    TABLE.put("asc", HttpHeaders.newEntity("text/plain"));
    TABLE.put("atom", HttpHeaders.newEntity("application/atom+xml"));
    TABLE.put("au", HttpHeaders.newEntity("audio/basic"));
    TABLE.put("avi", HttpHeaders.newEntity("video/x-msvideo"));
    TABLE.put("bcpio", HttpHeaders.newEntity("application/x-bcpio"));
    TABLE.put("bin", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("bmp", HttpHeaders.newEntity("image/bmp"));
    TABLE.put("cdf", HttpHeaders.newEntity("application/x-netcdf"));
    TABLE.put("cgm", HttpHeaders.newEntity("image/cgm"));
    TABLE.put("class", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("cpio", HttpHeaders.newEntity("application/x-cpio"));
    TABLE.put("cpt", HttpHeaders.newEntity("application/mac-compactpro"));
    TABLE.put("csh", HttpHeaders.newEntity("application/x-csh"));
    TABLE.put("css", HttpHeaders.newEntity("text/css"));
    TABLE.put("dcr", HttpHeaders.newEntity("application/x-director"));
    TABLE.put("dif", HttpHeaders.newEntity("video/x-dv"));
    TABLE.put("dir", HttpHeaders.newEntity("application/x-director"));
    TABLE.put("djv", HttpHeaders.newEntity("image/vnd.djvu"));
    TABLE.put("djvu", HttpHeaders.newEntity("image/vnd.djvu"));
    TABLE.put("dll", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("dmg", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("dms", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("doc", HttpHeaders.newEntity("application/msword"));
    TABLE.put("dtd", HttpHeaders.newEntity("application/xml-dtd"));
    TABLE.put("dv", HttpHeaders.newEntity("video/x-dv"));
    TABLE.put("dvi", HttpHeaders.newEntity("application/x-dvi"));
    TABLE.put("dxr", HttpHeaders.newEntity("application/x-director"));
    TABLE.put("eps", HttpHeaders.newEntity("application/postscript"));
    TABLE.put("etx", HttpHeaders.newEntity("text/x-setext"));
    TABLE.put("exe", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("ez", HttpHeaders.newEntity("application/andrew-inset"));
    TABLE.put("gif", HttpHeaders.newEntity("image/gif"));
    TABLE.put("gram", HttpHeaders.newEntity("application/srgs"));
    TABLE.put("grxml", HttpHeaders.newEntity("application/srgs+xml"));
    TABLE.put("gtar", HttpHeaders.newEntity("application/x-gtar"));
    TABLE.put("hdf", HttpHeaders.newEntity("application/x-hdf"));
    TABLE.put("hqx", HttpHeaders.newEntity("application/mac-binhex40"));
    TABLE.put("htm", HttpHeaders.newEntity("text/html; charset=UTF-8"));
    TABLE.put("html", HttpHeaders.newEntity("text/html; charset=UTF-8"));
    TABLE.put("ice", HttpHeaders.newEntity("x-conference/x-cooltalk"));
    TABLE.put("ico", HttpHeaders.newEntity("image/x-icon"));
    TABLE.put("ics", HttpHeaders.newEntity("text/calendar"));
    TABLE.put("ief", HttpHeaders.newEntity("image/ief"));
    TABLE.put("ifb", HttpHeaders.newEntity("text/calendar"));
    TABLE.put("iges", HttpHeaders.newEntity("model/iges"));
    TABLE.put("igs", HttpHeaders.newEntity("model/iges"));
    TABLE.put("jnlp", HttpHeaders.newEntity("application/x-java-jnlp-file"));
    TABLE.put("jp2", HttpHeaders.newEntity("image/jp2"));
    TABLE.put("jpe", HttpHeaders.newEntity("image/jpeg"));
    TABLE.put("jpeg", HttpHeaders.newEntity("image/jpeg"));
    TABLE.put("jpg", HttpHeaders.newEntity("image/jpeg"));
    TABLE.put("js", HttpHeaders.newEntity("application/x-javascript; charset=UTF-8"));
    TABLE.put("kar", HttpHeaders.newEntity("audio/midi"));
    TABLE.put("latex", HttpHeaders.newEntity("application/x-latex"));
    TABLE.put("lha", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("lzh", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("m3u", HttpHeaders.newEntity("audio/x-mpegurl"));
    TABLE.put("m4a", HttpHeaders.newEntity("audio/mp4a-latm"));
    TABLE.put("m4b", HttpHeaders.newEntity("audio/mp4a-latm"));
    TABLE.put("m4p", HttpHeaders.newEntity("audio/mp4a-latm"));
    TABLE.put("m4u", HttpHeaders.newEntity("video/vnd.mpegurl"));
    TABLE.put("m4v", HttpHeaders.newEntity("video/x-m4v"));
    TABLE.put("mac", HttpHeaders.newEntity("image/x-macpaint"));
    TABLE.put("man", HttpHeaders.newEntity("application/x-troff-man"));
    TABLE.put("mathml", HttpHeaders.newEntity("application/mathml+xml"));
    TABLE.put("me", HttpHeaders.newEntity("application/x-troff-me"));
    TABLE.put("mesh", HttpHeaders.newEntity("model/mesh"));
    TABLE.put("mid", HttpHeaders.newEntity("audio/midi"));
    TABLE.put("midi", HttpHeaders.newEntity("audio/midi"));
    TABLE.put("mif", HttpHeaders.newEntity("application/vnd.mif"));
    TABLE.put("mov", HttpHeaders.newEntity("video/quicktime"));
    TABLE.put("movie", HttpHeaders.newEntity("video/x-sgi-movie"));
    TABLE.put("mp2", HttpHeaders.newEntity("audio/mpeg"));
    TABLE.put("mp3", HttpHeaders.newEntity("audio/mpeg"));
    TABLE.put("mp4", HttpHeaders.newEntity("video/mp4"));
    TABLE.put("mpe", HttpHeaders.newEntity("video/mpeg"));
    TABLE.put("mpeg", HttpHeaders.newEntity("video/mpeg"));
    TABLE.put("mpg", HttpHeaders.newEntity("video/mpeg"));
    TABLE.put("mpga", HttpHeaders.newEntity("audio/mpeg"));
    TABLE.put("ms", HttpHeaders.newEntity("application/x-troff-ms"));
    TABLE.put("msh", HttpHeaders.newEntity("model/mesh"));
    TABLE.put("mxu", HttpHeaders.newEntity("video/vnd.mpegurl"));
    TABLE.put("nc", HttpHeaders.newEntity("application/x-netcdf"));
    TABLE.put("oda", HttpHeaders.newEntity("application/oda"));
    TABLE.put("ogg", HttpHeaders.newEntity("application/ogg"));
    TABLE.put("pbm", HttpHeaders.newEntity("image/x-portable-bitmap"));
    TABLE.put("pct", HttpHeaders.newEntity("image/pict"));
    TABLE.put("pdb", HttpHeaders.newEntity("chemical/x-pdb"));
    TABLE.put("pdf", HttpHeaders.newEntity("application/pdf"));
    TABLE.put("pgm", HttpHeaders.newEntity("image/x-portable-graymap"));
    TABLE.put("pgn", HttpHeaders.newEntity("application/x-chess-pgn"));
    TABLE.put("pic", HttpHeaders.newEntity("image/pict"));
    TABLE.put("pict", HttpHeaders.newEntity("image/pict"));
    TABLE.put("png", HttpHeaders.newEntity("image/png"));
    TABLE.put("pnm", HttpHeaders.newEntity("image/x-portable-anymap"));
    TABLE.put("pnt", HttpHeaders.newEntity("image/x-macpaint"));
    TABLE.put("pntg", HttpHeaders.newEntity("image/x-macpaint"));
    TABLE.put("ppm", HttpHeaders.newEntity("image/x-portable-pixmap"));
    TABLE.put("ppt", HttpHeaders.newEntity("application/vnd.ms-powerpoint"));
    TABLE.put("ps", HttpHeaders.newEntity("application/postscript"));
    TABLE.put("qt", HttpHeaders.newEntity("video/quicktime"));
    TABLE.put("qti", HttpHeaders.newEntity("image/x-quicktime"));
    TABLE.put("qtif", HttpHeaders.newEntity("image/x-quicktime"));
    TABLE.put("ra", HttpHeaders.newEntity("audio/x-pn-realaudio"));
    TABLE.put("ram", HttpHeaders.newEntity("audio/x-pn-realaudio"));
    TABLE.put("ras", HttpHeaders.newEntity("image/x-cmu-raster"));
    TABLE.put("rdf", HttpHeaders.newEntity("application/rdf+xml"));
    TABLE.put("rgb", HttpHeaders.newEntity("image/x-rgb"));
    TABLE.put("rm", HttpHeaders.newEntity("application/vnd.rn-realmedia"));
    TABLE.put("roff", HttpHeaders.newEntity("application/x-troff"));
    TABLE.put("rtf", HttpHeaders.newEntity("text/rtf"));
    TABLE.put("rtx", HttpHeaders.newEntity("text/richtext"));
    TABLE.put("sgm", HttpHeaders.newEntity("text/sgml"));
    TABLE.put("sgml", HttpHeaders.newEntity("text/sgml"));
    TABLE.put("sh", HttpHeaders.newEntity("application/x-sh"));
    TABLE.put("shar", HttpHeaders.newEntity("application/x-shar"));
    TABLE.put("silo", HttpHeaders.newEntity("model/mesh"));
    TABLE.put("sit", HttpHeaders.newEntity("application/x-stuffit"));
    TABLE.put("skd", HttpHeaders.newEntity("application/x-koan"));
    TABLE.put("skm", HttpHeaders.newEntity("application/x-koan"));
    TABLE.put("skp", HttpHeaders.newEntity("application/x-koan"));
    TABLE.put("skt", HttpHeaders.newEntity("application/x-koan"));
    TABLE.put("smi", HttpHeaders.newEntity("application/smil"));
    TABLE.put("smil", HttpHeaders.newEntity("application/smil"));
    TABLE.put("snd", HttpHeaders.newEntity("audio/basic"));
    TABLE.put("so", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("spl", HttpHeaders.newEntity("application/x-futuresplash"));
    TABLE.put("src", HttpHeaders.newEntity("application/x-wais-source"));
    TABLE.put("sv4cpio", HttpHeaders.newEntity("application/x-sv4cpio"));
    TABLE.put("sv4crc", HttpHeaders.newEntity("application/x-sv4crc"));
    TABLE.put("svg", HttpHeaders.newEntity("image/svg+xml"));
    TABLE.put("swf", HttpHeaders.newEntity("application/x-shockwave-flash"));
    TABLE.put("t", HttpHeaders.newEntity("application/x-troff"));
    TABLE.put("tar", HttpHeaders.newEntity("application/x-tar"));
    TABLE.put("tcl", HttpHeaders.newEntity("application/x-tcl"));
    TABLE.put("tex", HttpHeaders.newEntity("application/x-tex"));
    TABLE.put("texi", HttpHeaders.newEntity("application/x-texinfo"));
    TABLE.put("texinfo", HttpHeaders.newEntity("application/x-texinfo"));
    TABLE.put("tif", HttpHeaders.newEntity("image/tiff"));
    TABLE.put("tiff", HttpHeaders.newEntity("image/tiff"));
    TABLE.put("tr", HttpHeaders.newEntity("application/x-troff"));
    TABLE.put("tsv", HttpHeaders.newEntity("text/tab-separated-values"));
    TABLE.put("txt", HttpHeaders.newEntity("text/plain"));
    TABLE.put("ustar", HttpHeaders.newEntity("application/x-ustar"));
    TABLE.put("vcd", HttpHeaders.newEntity("application/x-cdlink"));
    TABLE.put("vrml", HttpHeaders.newEntity("model/vrml"));
    TABLE.put("vxml", HttpHeaders.newEntity("application/voicexml+xml"));
    TABLE.put("wav", HttpHeaders.newEntity("audio/x-wav"));
    TABLE.put("wbmp", HttpHeaders.newEntity("image/vnd.wap.wbmp"));
    TABLE.put("wbmxl", HttpHeaders.newEntity("application/vnd.wap.wbxml"));
    TABLE.put("wml", HttpHeaders.newEntity("text/vnd.wap.wml"));
    TABLE.put("wmlc", HttpHeaders.newEntity("application/vnd.wap.wmlc"));
    TABLE.put("wmls", HttpHeaders.newEntity("text/vnd.wap.wmlscript"));
    TABLE.put("wmlsc", HttpHeaders.newEntity("application/vnd.wap.wmlscriptc"));
    TABLE.put("wrl", HttpHeaders.newEntity("model/vrml"));
    TABLE.put("wsdl", HttpHeaders.newEntity("application/xml"));
    TABLE.put("xbm", HttpHeaders.newEntity("image/x-xbitmap"));
    TABLE.put("xht", HttpHeaders.newEntity("application/xhtml+xml"));
    TABLE.put("xhtml", HttpHeaders.newEntity("application/xhtml+xml"));
    TABLE.put("xls", HttpHeaders.newEntity("application/vnd.ms-excel"));
    TABLE.put("xml", HttpHeaders.newEntity("application/xml"));
    TABLE.put("xpm", HttpHeaders.newEntity("image/x-xpixmap"));
    TABLE.put("xsl", HttpHeaders.newEntity("application/xml"));
    TABLE.put("xslt", HttpHeaders.newEntity("application/xslt+xml"));
    TABLE.put("xul", HttpHeaders.newEntity("application/vnd.mozilla.xul+xml"));
    TABLE.put("xwd", HttpHeaders.newEntity("image/x-xwindowdump"));
    TABLE.put("xyz", HttpHeaders.newEntity("chemical/x-xyz"));
    TABLE.put("otf", HttpHeaders.newEntity("application/vnd.ms-opentype"));
    TABLE.put("eot", HttpHeaders.newEntity("application/vnd.ms-fontobject"));
    TABLE.put("ttf", HttpHeaders.newEntity("application/x-font-ttf"));
    TABLE.put("woff", HttpHeaders.newEntity("application/octet-stream"));
    TABLE.put("woff2", HttpHeaders.newEntity("application/font-woff2"));

    TABLE.put("json", HttpHeaders.newEntity("application/json"));
  }

  public static final CharSequence getContentType(String file) {
    String extension = StringUtils.substringAfterLast(file, ".");
    return TABLE.get(extension);
  }
}
