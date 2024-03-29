package feed.parser;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.caudexorigo.Shutdown;

import java.net.URL;

public class Test {
  public static void main(String[] args) {
    try {
      // String rss = IOUtils.toString(new FileInputStream("./tests/maissemanario.xml"),
      // Charset.forName("UTF-8"));

      URL oracle = new URL("https://www.sabado.pt/rss");

      FeedParser parser = new FeedParser();
      FeedChannel feed = parser.parse(oracle.openStream(), true, true);
      // feed.addCategory("foo");
      // feed.addCategory("bar");

      // System.out.println(feed);

      final ObjectMapper mapper = new ObjectMapper();
      mapper.setSerializationInclusion(Include.NON_NULL);
      mapper.enable(SerializationFeature.INDENT_OUTPUT);

      // String json_feed = mapper.writeValueAsString(feed);

      // System.out.println(json_feed);

      // FeedChannel feed_from_json = mapper.readValue(json_feed, FeedChannel.class);

      feed.getEntries().forEach(e -> System.out.println(e.getLink()));

      // JAXBContext jaxbContext = JAXBContext.newInstance(Rss.class);
      // Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      //
      // // output pretty printed
      // jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      // jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      //
      // jaxbMarshaller.marshal(new Rss(feed), System.out);

    } catch (Throwable e) {
      Shutdown.now(e);
    }
  }
}
