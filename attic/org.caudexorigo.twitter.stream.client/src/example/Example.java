package example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.caudexorigo.Shutdown;
import org.caudexorigo.text.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.gist.twitter.FilterParameterFetcher;
import com.gist.twitter.TwitterClient;
import com.gist.twitter.TwitterStreamProcessor;

class Example
{
	public static void main(String[] args)
	{
		Collection<String> credentials = new ArrayList<String>();
		credentials.add("pondtest:teste123");
		credentials.add("stream1test:teste123");
		credentials.add("stream2test:teste123");

		// Collection<String> followIds = null;
		Collection<String> followIds = new ArrayList<String>();

		try
		{
			BufferedReader br = new BufferedReader(new FileReader("users.txt"));
			String line = null;

			while ((line = br.readLine()) != null)
			{
				if (StringUtils.isNotBlank(line))
				{
					followIds.add(line.trim());
				}
			}
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
		
		System.out.println(followIds);

		// Collection<String> trackKeywords = new ArrayList<String>();
		Collection<String> trackKeywords = null;

		final Collection<String> finalFollowIds = followIds;
		final Collection<String> finalTrackKeywords = trackKeywords;

		FilterParameterFetcher filterParameterFetcher = new FilterParameterFetcher()
		{
			@Override
			public Collection<String> getFollowIds()
			{
				return finalFollowIds;
			}

			@Override
			public Collection<String> getTrackKeywords()
			{
				return finalTrackKeywords;
			}

			@Override
			public String getLocation()
			{
				return null;
				// return "37.74,-8.06,41.94,-6.61";
				// return "-122.75,36.8,-121.75,37.8,-74,40,-73,41";
			}
		};

		new TwitterClient(filterParameterFetcher, new ExampleTwitterStreamProcessor(), "http://stream.twitter.com/1/statuses/filter.json", 2000, 400, credentials, 60 * 1000L).execute();
	}

	/**
	 * Example TwitterStreamProcessor that uses org.json.* to process the stream and just prints out each tweet. This isn't an endorsement of any techniques, just an example. In real life the tweet would likely be put into some kind of queue system.
	 */
	private static class ExampleTwitterStreamProcessor implements TwitterStreamProcessor
	{
		public void processTwitterStream(InputStream is, String credentials, HashSet<String> ids) throws InterruptedException, IOException
		{
			System.out.println("Example.ExampleTwitterStreamProcessor.processTwitterStream()");

			JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

			JsonParser jp = jsonFactory.createJsonParser(new InputStreamReader(is, "UTF-8"));

			while (true)
			{
				JsonNode results = jp.readValueAsTree();

				System.out.println("results: " + results);
			}

		}
	}
}