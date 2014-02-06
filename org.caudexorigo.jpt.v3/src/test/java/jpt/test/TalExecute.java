package jpt.test;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

import org.caudexorigo.Shutdown;
import org.caudexorigo.jpt.JptInstance;
import org.caudexorigo.jpt.JptInstanceBuilder;
import org.caudexorigo.text.StringUtils;

public class TalExecute
{
	public static void main(String[] args)
	{
		try
		{
			if ((args.length < 1) || StringUtils.isBlank(args[0]))
			{
				throw new IllegalArgumentException("missing template file");
			}

			String curDir = System.getProperty("user.dir");
			String template = args[0];
			String templatePath = String.format("file://%s/templates/input/%s", curDir, template);
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