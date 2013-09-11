package org.caudexorigo.twitter.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterFetcher
{

	// =38.718765%%2C-9.183534%%2C

	private static final String url_format = "http://search.twitter.com/search.json?lang=pt&geocode=39.653798%%2C-11.346134%%2C520.0km&rpp=100&since_id=%s";

	private static final Logger log = LoggerFactory.getLogger(TwitterFetcher.class);

	private static final SearchResult EMPTY_RESULT;

	private static final UsersDB usersDB = new UsersDB();

	static
	{

		EMPTY_RESULT = new SearchResult();
		EMPTY_RESULT.max_id = 0;
		EMPTY_RESULT.lst_items = Collections.emptyList();
	}

	public static SearchResult fetch(long since_id)
	{
		String url = String.format(url_format, since_id);
		log.debug("Fetch url: '{}'", url);

		GetMethod get = new GetMethod(url);
		get.setFollowRedirects(true);

		try
		{
			HttpClient http_client = HttpHelper.getHttpClient();

			int status = http_client.executeMethod(get);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readValue(get.getResponseBodyAsStream(), JsonNode.class);

			if (status != 200)
			{
				throw new IllegalStateException(String.format("Received invalid status code: %s", status));
			}

			SearchResult sr = new SearchResult();
			sr.max_id = rootNode.get("max_id").getLongValue();

			JsonNode results = rootNode.get("results");
			List<String> lst = new ArrayList<String>();

			List<String> users = new ArrayList<String>();

			for (Iterator<JsonNode> node_lst = results.getElements(); node_lst.hasNext();)
			{
				JsonNode js = node_lst.next();

				JsonNode js_iso_lang_code = js.get("iso_language_code");

				if (js_iso_lang_code != null)
				{
					String iso_lang_code = js_iso_lang_code.getTextValue();

					// System.out.println("TwitterFetcher.fetch.iso_lang_code:"
					// + iso_lang_code);

					if ("pt".equals(iso_lang_code))
					{
						//System.out.println("TwitterFetcher.fetch.js:" + js);

						// long from_user_id =
						// js.get("from_user_id").getLongValue();
						String from_user = js.get("from_user").getTextValue();

						//System.out.println("TwitterFetcher.fetch.from_user:" + from_user);

						//System.out.println("*********************************************************************");

						users.add(from_user);
					}
				}

				String m = js.toString();
				lst.add(m);

			}

			if (users.size() > 0)
			{

				for (String u : users)
				{
					usersDB.addName(u);
				}
			}

			sr.lst_items = lst;
			return sr;

		}
		catch (Throwable t)
		{
			log.error(t.getMessage());
		}
		finally
		{
			get.releaseConnection();
		}

		return EMPTY_RESULT;
	}
}
