package feed.parser;

import java.net.URL;
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
			// String rss = IOUtils.toString(new FileInputStream("./tests/maissemanario.xml"), Charset.forName("UTF-8"));

			URL oracle = new URL("http://www.jornaldenegocios.pt/funcionalidades/Rss.aspx");
			String rss = IOUtils.toString(oracle.openStream(), Charset.forName("latin1"));

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

// http://visao.sapo.pt/static/rss/opiniao_23424.xml