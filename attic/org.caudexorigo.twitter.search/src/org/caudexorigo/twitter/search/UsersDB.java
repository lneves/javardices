package org.caudexorigo.twitter.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.caudexorigo.Shutdown;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsersDB
{
	private static final Logger log = LoggerFactory.getLogger(UsersDB.class);
	private final Set<String> names = new HashSet<String>();
	private static final String users_filename = "users.txt";
	private static final Pattern id_match = Pattern.compile("http://twitter.com/statuses/user_timeline/(.*?).rss");
	private static final Pattern splitter = Pattern.compile(";");

	public UsersDB()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(users_filename));
			String line = null;

			while ((line = br.readLine()) != null)
			{
				if (StringUtils.isNotBlank(line))
				{
					String[] parts = splitter.split(line);
					names.add(parts[0].trim());
				}
			}
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}

	public void addName(String name)
	{
		synchronized (names)
		{
			if (!names.contains(name))
			{
				log.info("Found new user: {}", name);

				String user_id = lookupUserId(name);
				log.info("Got user_id: {}", user_id);

				if (StringUtils.isNotBlank(user_id))
				{
					String line = String.format("%s;%s%n", name, user_id);
					appendToFile(users_filename, line);
					names.add(name);
				}
			}
		}
	}

	private String lookupUserId(String name)
	{
		String url = "http://twitter.com/" + name;

		HttpClient http_client = HttpHelper.getHttpClient();

		GetMethod get = new GetMethod(url);
		get.setFollowRedirects(true);

		String id = "";

		try
		{
			int status = http_client.executeMethod(get);

			if (status != 200)
			{
				throw new IllegalStateException(String.format("Received invalid status code: %s", status));
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(get.getResponseBodyAsStream()));

			String line = null;

			while ((line = br.readLine()) != null)
			{
				if (StringUtils.isNotBlank(line))
				{
					Matcher matcher = id_match.matcher(line);
					boolean matchFound = matcher.find();

					if (matchFound)
					{
						id = matcher.group(1);

						break;
					}

				}
			}
		}
		catch (Throwable t)
		{
			log.error(t.getMessage());
		}
		finally
		{
			get.releaseConnection();
		}

		return id;
	}

	private void appendToFile(String file_name, String name)
	{
		try
		{

			if (StringUtils.isNotBlank(name))
			{
				FileWriter fstream = new FileWriter(file_name, true);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(name);
				out.flush();
				out.close();
				fstream.close();
			}
			else
			{
				log.warn("Could not append empty value to file '{}'", file_name);
			}

		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}

}
