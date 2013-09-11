package jpt.test;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

import org.caudexorigo.Shutdown;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;

public class Tal05_1
{
	public static void main(String[] args)
	{
		try
		{
			String curDir = System.getProperty("user.dir");
			String templatePath = String.format("file://%s/templates/input/tal05.1.jpt", curDir);
			JptInstance jpt = JptInstanceBuilder.getJptInstance(URI.create(templatePath));
			Writer out = new OutputStreamWriter(System.out);
			jpt.render(out);
			out.flush();
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}