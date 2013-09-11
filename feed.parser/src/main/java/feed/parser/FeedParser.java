package feed.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.caudexorigo.io.UnsynchronizedByteArrayInputStream;
import org.caudexorigo.io.UnsynchronizedByteArrayOutputStream;
import org.caudexorigo.text.DetectEncoding;

public class FeedParser
{
	private final StaxParser staxParser;
	private final DetectEncoding encDetector;

	public FeedParser()
	{
		super();
		staxParser = new StaxParser();
		encDetector = new DetectEncoding("UTF-8");
	}

	public FeedChannel parse(Reader reader)
	{
		return staxParser.parse(reader, true);
	}

	public FeedChannel parse(Reader reader, boolean failOnError)
	{
		return staxParser.parse(reader, failOnError);
	}

	public FeedChannel parse(InputStream stream)
	{
		return parse(stream, true);
	}

	public FeedChannel parse(InputStream stream, boolean failOnError)
	{
		try
		{
			Reader reader = buildReader(stream);
			return staxParser.parse(reader, failOnError);
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	private Reader buildReader(InputStream stream) throws IOException, UnsupportedEncodingException
	{
		if (stream.markSupported())
		{
			stream.mark(500);
			String encoding = encDetector.detect(stream);
			stream.reset();
			Reader reader = new InputStreamReader(stream, encoding);
			return reader;
		}
		else
		{
			UnsynchronizedByteArrayOutputStream bout = new UnsynchronizedByteArrayOutputStream();
			int b;

			while ((b = stream.read()) > -1)
			{
				bout.write(b);
			}

			return buildReader(new UnsynchronizedByteArrayInputStream(bout.toByteArray()));
		}
	}
}