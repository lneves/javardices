package db.bench;

import java.io.InputStream;

import org.caudexorigo.Shutdown;
import org.caudexorigo.io.IOUtils;

public class Dml
{

	public static String load(String sql_file)
	{
		try
		{
			InputStream in = Dml.class.getResourceAsStream("/trade/api/dml/" + sql_file);

			if (in == null)
			{
				throw new RuntimeException("File not found or is invalid");
			}
			else
			{
				return IOUtils.toString(in);
			}
		}
		catch (Throwable t)
		{
			String message = String.format("Could not load file '%s'. Reason: %s", sql_file, t.getMessage());
			Shutdown.now(new RuntimeException(message));
			return null;
		}
	}
}