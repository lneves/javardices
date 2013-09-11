package example;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

public class LanguageParser
{
	public static void main(String[] args) throws JsonParseException, FileNotFoundException, IOException
	{
		JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

		JsonParser jp = jsonFactory.createJsonParser(new FileReader("languages.json"));

		JsonNode results = jp.readValueAsTree();
		
		System.out.println("Size: " + results.size());

		JsonNode pt = results.get("pt");

		System.out.println("Size: " + pt.size());
		
		
		System.out.println("isContainerNode: " + pt.isContainerNode());
		System.out.println("isObject: " + pt.isObject());
		
		Iterator<String> words = pt.getFieldNames();
		
		while (words.hasNext())
		{
		//	System.out.println(words.next());
			
			String word = words.next();
			JsonNode js_val = pt.get(word);
			
			String sql = String.format("INSERT INTO word(w, df, tf) VALUES ('%s', %s, %s);", word, js_val.get(0).getValueAsDouble(), js_val.get(1).getValueAsDouble());
			
			System.out.println(sql);
			
		}
		


		for (JsonNode jsonNode : pt)
		{

			jsonNode.toString();

		}
	}
}
