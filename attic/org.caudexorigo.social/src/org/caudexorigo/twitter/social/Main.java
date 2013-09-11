package org.caudexorigo.twitter.social;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.caudexorigo.Shutdown;
import org.caudexorigo.cli.ArgumentValidationException;
import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.text.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.CliArgs;

import com.gist.twitter.FilterParameterFetcher;
import com.gist.twitter.TwitterClient;
import com.gist.twitter.TwitterStreamProcessor;

public class Main
{
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	private static final Pattern positive_locations = Pattern
			.compile(
					".*(abrantes|açores|águeda|alandroal|albergaria-a-velha|albufeira|alcanena|alcobaça|alcochete|alcoutim|alenquer|alentejo|algarve|alijó|aljezur|aljustrel|almada|almeida|almeirim|almodôvar|alpiarça|alvaiázere|alvito|amadora|amarante|amares|anadia|ansião|arganil|armamar|arouca|arraiolos|arronches|aveiro|avis|azambuja|baião|barcelos|barrancos|barreiro|batalha|beja|belmonte|benavente|bombarral|borba|boticas|braga|bragança|cadaval|calheta|caminha|cantanhede|cartaxo|cascais|chamusca|chaves|cinfães|coimbra|condeixa-a-nova|constancia|coruche|corvo|covilhã|crato|cuba|douro|elvas|entroncamento|espinho|esposende|estarreja|estremadura|estremoz|évora|fafe|faro|felgueiras|fronteira|funchal|fundão|gavião|góis|golegã|gondomar|gouveia|grandola|guarda|guimarães|horta|idanha-a-nova|ílhavo|lagoa|lagos|lamego|leiria|lisboa|lisbon|loulé|loures|lourinhã|lousã|lousada|mação|machico|madalena|madeira|mafra|maia|mangualde|manteigas|marvão|matosinhos|mealhada|meda|melgaço|mértola|minho|mira|mirandela|mogadouro|moita|monção|monchique|monforte|montalegre|montemor-o-novo|montemor-o-velho|montijo|mora|mortágua|moura|mourão|murça|murtosa|nazaré|nelas|nisa|nordeste|óbidos|odemira|odivelas|oeiras|oleiros|olivença|ourém|ourique|ovar|palmela|paredes|penacova|penafiel|penamacor|penedono|penela|peniche|pinhel|pombal|portalegre|portel|portimão|porto|portugal|povoação|proença-a-nova|redondo|resende|sabrosa|sabugal|santana|santarém|sardoal|sátão|seia|seixal|sernancelhe|serpa|sertã|sesimbra|setúbal|silves|sines|sintra|soure|sousel|tábua|tabuaço|tarouca|tavira|tomar|tondela|trancoso|trás-os-montes|trofa|vagos|valença|valongo|valpaços|velas|vidigueira|vimioso|vinhais|viseu|vizela|vouzela).*",
					Pattern.CASE_INSENSITIVE);

	private static final Pattern negative_locations = Pattern.compile(".*(brazil|brasil|brasiil|rio de janeiro|são paulo|sao paulo|bahia|bahiiia|porto alegre|pará).*", Pattern.CASE_INSENSITIVE);

	private static final Pattern splitter = Pattern.compile(";");

	public static void main(String[] args)
	{

		CliArgs cargs = null;
		try
		{
			cargs = CliFactory.parseArguments(CliArgs.class, args);
		}
		catch (ArgumentValidationException ae)
		{
			Shutdown.now(ae);
		}

		Collection<String> credentials = new ArrayList<String>();

		credentials.add("stream1test:teste123");
		credentials.add("stream2test:teste123");
		credentials.add("stream3test:teste123");
		credentials.add("stream4test:teste123");


		// Collection<String> followIds = null;
		Collection<String> followIds = new ArrayList<String>();

		loadFromFile(followIds, "users.txt", true);

		// Collection<String> trackKeywords = new ArrayList<String>();
		Collection<String> trackKeywords = null;

		// loadFromFile(trackKeywords, "track_words.txt", false);

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
			}
		};

		BrokerHandler bk_handler = new BrokerHandler(cargs.getHost(), cargs.getPort());

		bk_handler.ping();

		new TwitterClient(filterParameterFetcher, new SocialTwitterStreamProcessor(bk_handler, cargs.getDestination()), "http://stream.twitter.com/1/statuses/filter.json", 5000, 400, credentials, 60 * 1000L).execute();
	}

	private static void loadFromFile(Collection<String> collection, String fileName, boolean has_separator)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line = null;

			while ((line = br.readLine()) != null)
			{
				if (StringUtils.isNotBlank(line))
				{
					if (has_separator)
					{
						String[] parts = splitter.split(line);
						collection.add(parts[1].trim());
					}
					else
					{
						collection.add(line.trim());
					}

				}
			}
		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}

	private static class SocialTwitterStreamProcessor implements TwitterStreamProcessor
	{
		private final BrokerHandler bkHandler;
		private final String destination;

		public SocialTwitterStreamProcessor(BrokerHandler bkHandler, String destination)
		{
			this.bkHandler = bkHandler;
			this.destination = destination;
		}

		public void processTwitterStream(InputStream is, String credentials, HashSet<String> ids) throws InterruptedException, IOException
		{

			JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

			Reader reader = new InputStreamReader(is, "UTF-8");

			JsonParser jp = jsonFactory.createJsonParser(reader);

			while (true)
			{
				JsonNode results = jp.readValueAsTree();

				if (results != null)
				{
					String m = results.toString();

					if (log.isDebugEnabled())
					{
						log.debug("Got: {}", m);
					}

					JsonNode user_node = results.get("user");

					if (user_node == null)
					{
						continue;
					}

					String lang = getValue(user_node, "lang");
					String location = getValue(user_node, "location");
					String time_zone = getValue(user_node, "time_zone");


					if ("es".equals(lang) || negative_locations.matcher(location).matches() || negative_locations.matcher(time_zone).matches() || "Santiago".equals(time_zone))
					{
						continue;
					}
					else if (StringUtils.isBlank(location) || positive_locations.matcher(location).matches())
					{
						if (log.isDebugEnabled())
						{
							log.debug("Publish: {}", m);
						}
						bkHandler.publish(m, destination);
					}
				}

			}
		}

		private String getValue(JsonNode pnode, String fieldName)
		{
			try
			{
				JsonNode js = pnode.findValue(fieldName);

				if (js != null)
				{
					String l = js.getTextValue();
					return StringUtils.trimToEmpty(l);
				}
			}
			catch (Throwable t)
			{
				log.warn(t.getMessage());
			}

			return "";
		}
	}
}
