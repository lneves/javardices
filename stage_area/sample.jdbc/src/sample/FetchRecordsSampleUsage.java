package sample;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.DbRunner;
import org.caudexorigo.jdbc.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FetchRecordsSampleUsage
{
	private static final Logger log = LoggerFactory.getLogger(FetchRecordsSampleUsage.class);

	static final ResultSetHandler<User> extractor = new ResultSetHandler<User>()
	{
		public User process(ResultSet rs)
		{
			try
			{
				String user_first_name = rs.getString("user_first_name");
				String user_last_name = rs.getString("user_last_name");

				User obj = new User(user_first_name, user_last_name);
				return obj;
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static void main(String[] args)
	{
		try
		{
			DbFetcher<User> fetcher = new DbFetcher<User>();

			// sem parâmetros
			List<User> lst_users = fetcher.fetchList("SELECT user_first_name, user_last_name FROM user", extractor);

			// com parâmetros
			boolean is_active = true;
			List<User> lst_filtered_users_0 = fetcher.fetchList("SELECT user_first_name, user_last_name FROM user WHERE is_active=? ", extractor, is_active);

			// mais do que um parâmetro
			Date d = new Date();
			List<User> lst_filtered_users_1 = fetcher.fetchList("SELECT user_first_name, user_last_name FROM user WHERE is_active=? AND last_login>?", extractor, is_active, d);

			DbRunner runner = new DbRunner();

			String user_fname = "joe";
			String user_lname = "shmoe";

			runner.executePreparedStatement("INSERT INTO user (user_first_name, user_last_name) VALUES(?, ?)", user_fname, user_lname);

		}
		catch (Throwable ex)
		{
			Throwable rc = ErrorAnalyser.findRootCause(ex);
			log.error(rc.getMessage(), rc);
		}
	}
}