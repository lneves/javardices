package feed.parser;

import java.net.URL;

import org.caudexorigo.Shutdown;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Test
{
	public static void main(String[] args)
	{
		try
		{
			// String rss = IOUtils.toString(new FileInputStream("./tests/maissemanario.xml"), Charset.forName("UTF-8"));

			URL oracle = new URL("http://sol.sapo.pt/feedsapo.xml");

			FeedParser parser = new FeedParser();
			FeedChannel feed = parser.parse(oracle.openStream(), true, true);
			// feed.addCategory("foo");
			// feed.addCategory("bar");

			// System.out.println(feed);

			final ObjectMapper mapper = new ObjectMapper();
			mapper.setSerializationInclusion(Include.NON_NULL);
			mapper.enable(SerializationFeature.INDENT_OUTPUT);

			String json_feed = mapper.writeValueAsString(feed);

			System.out.println(json_feed);

			FeedChannel feed_from_json = mapper.readValue(json_feed, FeedChannel.class);

			System.out.println(feed_from_json.toString());

			// JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
			// Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			//
			// // output pretty printed
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			//
			// jaxbMarshaller.marshal(new Rss(feed), System.out);

		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}
}