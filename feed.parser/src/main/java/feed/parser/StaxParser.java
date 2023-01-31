package feed.parser;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxParser {
  private static final Logger log = LoggerFactory.getLogger(StaxParser.class);

  private static final XMLInputFactory factory = XMLInputFactory.newInstance();

  private static Map<String, String> ns_aliases = new HashMap<String, String>();

  private static Map<String, FeedChannelProcessor> feed_channel_processors =
      new HashMap<String, FeedChannelProcessor>();

  private static Map<String, FeedEntryProcessor> feed_entry_processors =
      new HashMap<String, FeedEntryProcessor>();

  private boolean stripHtml;

  static {
    ns_aliases.put("http://a9.com/-/spec/opensearchrss/1.0/", "opensearch");
    ns_aliases.put("http://backend.userland.com/blogChannelModule", "blogChannel");
    ns_aliases.put("http://backend.userland.com/creativeCommonsRssModule", "creativeCommons");
    ns_aliases.put("http://example.com/dtds/podcast-1.0.dtd", "itunes");
    ns_aliases.put("http://freshmeat.net/rss/fm/", "fm");
    ns_aliases.put("http://hacks.benhammersley.com/rss/streaming/", "str");
    ns_aliases.put("http://madskills.com/public/xml/rss/module/pingback/", "pingback");
    ns_aliases.put("http://madskills.com/public/xml/rss/module/trackback/", "trackback");
    ns_aliases.put("http://media.tangent.org/rss/1.0/", "audio");
    ns_aliases.put("http://my.theinfo.org/changed/1.0/rss/", "cp");
    ns_aliases.put("http://postneo.com/icbm/", "icbm");
    ns_aliases.put("http://prismstandard.org/namespaces/1.2/basic/", "prism");
    ns_aliases.put("http://purl.org/dc/elements/1.1/", "dc");
    ns_aliases.put("http://purl.org/dc/terms/", "dcterms");
    ns_aliases.put("http://purl.org/rss/1.0/modules/aggregation/", "ag");
    ns_aliases.put("http://purl.org/rss/1.0/modules/annotate/", "annotate");
    ns_aliases.put("http://purl.org/rss/1.0/modules/company", "co");
    ns_aliases.put("http://purl.org/rss/1.0/modules/content/", "content");
    ns_aliases.put("http://purl.org/rss/1.0/modules/email/", "email");
    ns_aliases.put("http://purl.org/rss/1.0/modules/event/", "ev");
    ns_aliases.put("http://purl.org/rss/1.0/modules/image/", "image");
    ns_aliases.put("http://purl.org/rss/1.0/modules/link/", "l");
    ns_aliases.put("http://purl.org/rss/1.0/modules/reference/", "ref");
    ns_aliases.put("http://purl.org/rss/1.0/modules/richequiv/", "reqv");
    ns_aliases.put("http://purl.org/rss/1.0/modules/search/", "search");
    ns_aliases.put("http://purl.org/rss/1.0/modules/servicestatus/", "ss");
    ns_aliases.put("http://purl.org/rss/1.0/modules/slash/", "slash");
    ns_aliases.put("http://purl.org/rss/1.0/modules/subscription/", "sub");
    ns_aliases.put("http://purl.org/rss/1.0/modules/syndication/", "sy");
    ns_aliases.put("http://purl.org/rss/1.0/modules/taxonomy/", "taxo");
    ns_aliases.put("http://purl.org/rss/1.0/modules/textinput/", "ti");
    ns_aliases.put("http://purl.org/rss/1.0/modules/threading/", "thr");
    ns_aliases.put("http://purl.org/rss/1.0/modules/wiki/", "wiki");
    ns_aliases.put("http://purl.org/rss/1.0/", "rdf");
    ns_aliases.put("http://rssnamespace.org/feedburner/ext/1.0", "feedburner");

    ns_aliases.put("http://schemas.xmlsoap.org/soap/envelope/", "soap");
    ns_aliases.put("http://webns.net/mvcb/", "admin");
    ns_aliases.put("http://web.resource.org/cc/", "cc");
    ns_aliases.put("http://wellformedweb.org/commentapi/", "wfw");
    ns_aliases.put("http://www.georss.org/georss", "georss");
    ns_aliases.put("http://www.itunes.com/dtds/podcast-1.0.dtd", "itunes");
    ns_aliases.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf");
    ns_aliases.put("http://www.w3.org/1999/xhtml", "xhtml");
    ns_aliases.put("http://www.w3.org/2000/01/rdf-schema#", "rdfs");
    ns_aliases.put("http://www.w3.org/2003/01/geo/wgs84_pos#", "wgs84");
    ns_aliases.put("http://www.w3.org/2005/atom", "atom");
    ns_aliases.put("http://www.w3.org/2005/Atom", "Atom");
    ns_aliases.put("http://www.w3.org/XML/1998/namespace", "xml");
    ns_aliases.put("http://xmlns.com/foaf/0.1/", "foaf");
    ns_aliases.put("http://search.yahoo.com/mrss/", "media");
    ns_aliases.put("", "rss");

    feed_channel_processors.put("/rss:rss/rss:channel/rss:title", new SimpleFeedChannelProcessor(
        "title"));
    feed_channel_processors.put("/rss:rss/rss:channel/rss:link", new SimpleFeedChannelProcessor(
        "link"));
    feed_channel_processors.put("/rss:rss/rss:channel/rss:description",
        new SimpleFeedChannelProcessor("description"));
    feed_channel_processors.put("/rss:rss/rss:channel/rss:language", new SimpleFeedChannelProcessor(
        "language"));
    feed_channel_processors.put("/rss:rss/rss:channel/rss:item", new AddFeedEntryProcessor());

    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:title",
        new SimpleFeedEntryProcessor("title"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:author",
        new SimpleFeedEntryProcessor("author"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:guid", new GuidProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:pubdate",
        new SimpleFeedEntryProcessor("pubdate"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:description",
        new SimpleFeedEntryProcessor("body"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/atom:summary",
        new SimpleFeedEntryProcessor("body"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:link",
        new SimpleFeedEntryProcessor("link"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/atom:link",
        new AtomFeedChannelLinkProcessor("service.feed", "application/x.atom+xml"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/Atom:link",
        new RssFeedAtomLinkProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/feedburner:origlink",
        new SimpleFeedEntryProcessor("origlink"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:category",
        new CategoryProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:tag", new CategoryProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:subject", new CategoryProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:subtitle",
        new CategoryProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/dc:subject", new CategoryProcessor());
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/dc:date", new SimpleFeedEntryProcessor(
        "pubdate"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/dc:creator",
        new SimpleFeedEntryProcessor("author"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/content:encoded",
        new SimpleFeedEntryProcessor("body"));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/rss:enclosure", new EnclosureProcessor(
        Enclosure.Type.RSS));
    feed_entry_processors.put("/rss:rss/rss:channel/rss:item/media:content", new EnclosureProcessor(
        Enclosure.Type.YAHOO_MEDIA));

    feed_channel_processors.put("/rdf:rdf/rdf:channel/rdf:title", new SimpleFeedChannelProcessor(
        "title"));
    feed_channel_processors.put("/rdf:rdf/rdf:channel/rdf:link", new SimpleFeedChannelProcessor(
        "link"));
    feed_channel_processors.put("/rdf:rdf/rdf:channel/rdf:description",
        new SimpleFeedChannelProcessor("description"));
    feed_channel_processors.put("/rdf:rdf/rdf:item", new AddFeedEntryProcessor());
    feed_entry_processors.put("/rdf:rdf/rdf:item/rdf:title", new SimpleFeedEntryProcessor("title"));
    feed_entry_processors.put("/rdf:rdf/rdf:item/dc:subject", new CategoryProcessor());
    feed_entry_processors.put("/rdf:rdf/rdf:item/rdf:link", new SimpleFeedEntryProcessor("link"));
    feed_entry_processors.put("/rdf:rdf/rdf:item/feedburner:origlink", new SimpleFeedEntryProcessor(
        "origlink"));
    feed_entry_processors.put("/rdf:rdf/rdf:item/dc:creator", new SimpleFeedEntryProcessor(
        "author"));
    feed_entry_processors.put("/rdf:rdf/rdf:item/dc:date", new SimpleFeedEntryProcessor("pubdate"));
    feed_entry_processors.put("/rdf:rdf/rdf:item/rdf:description", new SimpleFeedEntryProcessor(
        "body"));

    feed_channel_processors.put("/atom:feed/atom:title", new SimpleFeedChannelProcessor("title"));
    feed_channel_processors.put("/atom:feed/atom:subtitle", new SimpleFeedChannelProcessor(
        "description"));
    feed_channel_processors.put("/atom:feed/atom:link", new AtomFeedChannelLinkProcessor(
        "service.feed", "application/x.atom+xml"));
    feed_channel_processors.put("/atom:feed/atom:entry", new AddFeedEntryProcessor());
    feed_entry_processors.put("/atom:feed/atom:entry/atom:title", new SimpleFeedEntryProcessor(
        "title"));
    feed_entry_processors.put("/atom:feed/atom:entry/feedburner:origlink",
        new SimpleFeedEntryProcessor("origlink"));
    feed_entry_processors.put("/atom:feed/atom:entry/atom:id", new GuidProcessor());
    feed_entry_processors.put("/atom:feed/atom:entry/atom:link", new AtomFeedEntryLinkProcessor(
        "alternate", "text/html"));
    feed_entry_processors.put("/atom:feed/atom:entry/atom:author/atom:name",
        new SimpleFeedEntryProcessor("author"));
    feed_entry_processors.put("/atom:feed/atom:entry/atom:published", new SimpleFeedEntryProcessor(
        "pubdate"));
    feed_entry_processors.put("/atom:feed/atom:entry/atom:category", new CategoryProcessor());
    feed_entry_processors.put("/atom:feed/atom:entry/atom:content", new AtomContentProcessor());

  }

  public StaxParser() {
    super();
  }

  // public FeedChannel parse(InputStream stream)
  // {
  // return parse(stream, true);
  // }
  //
  // public FeedChannel parse(InputStream stream, boolean failOnError)
  // {
  // try
  // {
  // XMLStreamReader staxXmlReader = factory.createXMLStreamReader(stream,
  // "UTF-8");
  // return _parse(staxXmlReader, failOnError);
  // }
  // catch (Throwable t)
  // {
  // if (failOnError)
  // {
  // throw new RuntimeException(t);
  // }
  // else
  // {
  // Throwable r = ErrorAnalyser.findRootCause(t);
  // log.error(r.getMessage(), r);
  // return new FeedChannel();
  // }
  // }
  // }

  public FeedChannel parse(Reader reader) {
    return parse(reader, true, false);
  }

  public FeedChannel parse(Reader reader, boolean failOnError, boolean stripHtml) {
    this.stripHtml = stripHtml;
    try {
      XMLStreamReader staxXmlReader = factory.createXMLStreamReader(reader);
      return _parse(staxXmlReader, failOnError);
    } catch (Throwable t) {
      if (failOnError) {
        throw new RuntimeException(t);
      } else {
        Throwable r = ErrorAnalyser.findRootCause(t);
        log.error(r.getMessage(), r);
        return new FeedChannel(stripHtml);
      }
    }
  }

  private FeedChannel _parse(XMLStreamReader staxXmlReader, boolean failOnError) throws Throwable {
    int eventType = staxXmlReader.getEventType();

    Stack<FeedXmlElement> stack = new Stack<FeedXmlElement>();
    FeedChannel feed_channel = new FeedChannel(stripHtml);

    do {
      try {
        if (eventType == XMLStreamConstants.START_ELEMENT) {
          String ns = StringUtils.isBlank(staxXmlReader.getNamespaceURI()) ? ""
              : staxXmlReader.getNamespaceURI();
          String lname = staxXmlReader.getLocalName().toLowerCase();
          String alias_prefix = ns_aliases.get(ns);

          FeedXmlElement fxe = new FeedXmlElement(alias_prefix, lname);
          stack.push(fxe);

          String path = buildPath(stack);

          if (log.isDebugEnabled()) {
            log.debug("Xml element path: {}", path);
          }

          FeedChannelProcessor fc_proc = feed_channel_processors.get(path);

          if (fc_proc != null) {
            fc_proc.process(feed_channel, staxXmlReader);
          }

          FeedEntryProcessor fe_proc = feed_entry_processors.get(path);

          if (fe_proc != null) {
            try {
              fe_proc.process(feed_channel, staxXmlReader);
            } catch (Throwable t) {
              Throwable r = ErrorAnalyser.findRootCause(t);

              if (r instanceof XMLStreamException) {
                log.error("Invalid feed element", r);
                continue;
              } else {
                throw new RuntimeException(r);
              }
            }
          }

          eventType = staxXmlReader.getEventType();
        }

        if (eventType == XMLStreamConstants.END_ELEMENT) {
          stack.pop();
        }
      } catch (Throwable t) {
        if (failOnError) {
          throw new RuntimeException(t);
        } else {
          Throwable r = ErrorAnalyser.findRootCause(t);
          log.error(r.getMessage(), r);
        }
      }

      eventType = staxXmlReader.next();
    } while (eventType != XMLStreamConstants.END_DOCUMENT);

    return feed_channel;
  }

  private String buildPath(Stack<FeedXmlElement> stack) {
    StringBuilder sb = new StringBuilder();
    sb.append("/");
    int s = stack.size();
    for (FeedXmlElement fxe : stack) {
      sb.append(fxe.toString());
      if (--s > 0) {
        sb.append("/");
      }
    }
    return sb.toString();
  }
}
