package jpt.test;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.caudexorigo.jpt.JptDocument;
import org.caudexorigo.jpt.JptInstanceBuilder;

public class StockPage
{
	public static void main(String[] args) throws IOException
	{
		String curDir = System.getProperty("user.dir");

		String templatePath = String.format("file://%s/templates/input/stocks.jpt.html", curDir);
		URI templateUri = URI.create(templatePath);
		System.out.println(templateUri);
		JptDocument jpt = JptInstanceBuilder.getJptInstance(templateUri).getJptDocument();

		Map<String, Object> ctx = new HashMap<>();

		List<StockView> viewItems = new ArrayList<>();
		final AtomicInteger ix = new AtomicInteger();
		Stock.dummyItems().forEach(s -> viewItems.add(new StockView(s, ix.incrementAndGet())));
		
		ctx.put("items", viewItems);

		Writer out = new OutputStreamWriter(System.out);

		jpt.render(ctx, out);
		
		out.flush();
	}
}
