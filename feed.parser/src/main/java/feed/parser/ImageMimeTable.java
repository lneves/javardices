package feed.parser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;

public class ImageMimeTable
{
	private static final ConcurrentMap<String, String> TABLE = new ConcurrentHashMap<String, String>();

	static
	{
		TABLE.put("bmp", "image/bmp");
		TABLE.put("cgm", "image/cgm");
		TABLE.put("djv", "image/vnd.djvu");
		TABLE.put("djvu", "image/vnd.djvu");
		TABLE.put("gif", "image/gif");
		TABLE.put("ice", "x-conference/x-cooltalk");
		TABLE.put("ico", "image/x-icon");
		TABLE.put("ief", "image/ief");
		TABLE.put("iges", "model/iges");
		TABLE.put("igs", "model/iges");
		TABLE.put("jp2", "image/jp2");
		TABLE.put("jpe", "image/jpeg");
		TABLE.put("jpeg", "image/jpeg");
		TABLE.put("jpg", "image/jpeg");
		TABLE.put("mac", "image/x-macpaint");
		TABLE.put("mesh", "model/mesh");
		TABLE.put("pbm", "image/x-portable-bitmap");
		TABLE.put("pct", "image/pict");

		TABLE.put("pgm", "image/x-portable-graymap");
		TABLE.put("pic", "image/pict");
		TABLE.put("pict", "image/pict");
		TABLE.put("png", "image/png");
		TABLE.put("pnm", "image/x-portable-anymap");
		TABLE.put("pnt", "image/x-macpaint");
		TABLE.put("pntg", "image/x-macpaint");
		TABLE.put("ppm", "image/x-portable-pixmap");
		TABLE.put("qti", "image/x-quicktime");
		TABLE.put("qtif", "image/x-quicktime");
		TABLE.put("ras", "image/x-cmu-raster");
		TABLE.put("rgb", "image/x-rgb");
		TABLE.put("silo", "model/mesh");
		TABLE.put("svg", "image/svg+xml");
		TABLE.put("tif", "image/tiff");
		TABLE.put("tiff", "image/tiff");
		TABLE.put("vrml", "model/vrml");
		TABLE.put("wbmp", "image/vnd.wap.wbmp");
		TABLE.put("xbm", "image/x-xbitmap");
		TABLE.put("xpm", "image/x-xpixmap");
		TABLE.put("xwd", "image/x-xwindowdump");
	}

	public static final String getContentType(String file)
	{
		String extension = StringUtils.substringAfterLast(file, ".");
		return TABLE.get(extension);
	}
}
