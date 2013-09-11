package org.caudexorigo.twitter.search;

import java.io.File;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonParseTest
{

	public static void main(String[] args) throws Throwable
	{
		ObjectMapper mapper = new ObjectMapper();

		JsonNode rootNode = mapper.readValue(new File("search.json"), JsonNode.class);

		long max_id = rootNode.get("max_id").getLongValue();
		System.out.println(max_id);

		JsonNode results = rootNode.get("results");

		System.out.println(results.size());

		for (Iterator<JsonNode> node_lst = results.getElements(); node_lst.hasNext();)
		{

			JsonNode js = node_lst.next();
			js.toString();

			System.out.println(js.toString());

		}

	}

}
