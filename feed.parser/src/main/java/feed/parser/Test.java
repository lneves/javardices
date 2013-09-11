package feed.parser;

import java.io.FileInputStream;
import java.nio.charset.Charset;

import org.caudexorigo.Shutdown;
import org.caudexorigo.io.IOUtils;
import org.caudexorigo.io.UnsynchronizedStringReader;

public class Test
{
	public static void main(String[] args)
	{
		try
		{
			String rss = IOUtils.toString(new FileInputStream("./tests/maissemanario.xml"), Charset.forName("UTF-8"));
			FeedParser parser = new FeedParser();
			FeedChannel feed = parser.parse(new UnsynchronizedStringReader(rss), true);

			System.out.println(feed);
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}
}