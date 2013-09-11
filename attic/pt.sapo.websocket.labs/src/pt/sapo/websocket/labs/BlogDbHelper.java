package pt.sapo.websocket.labs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.caudexorigo.jdbc.Db;
import org.caudexorigo.jdbc.DbExecutor;
import org.caudexorigo.jdbc.DbPool;
import org.caudexorigo.jdbc.DbTemplate;
import org.caudexorigo.jdbc.ResultSetExtract;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogDbHelper
{
	private static final Logger log = LoggerFactory.getLogger(BlogDbHelper.class);

	private static final String ins_ev_sql = "INSERT INTO ev_raw(site_visit_id, new_site_visit, hostname, url, doc_title, is_direct, referer, keywords, municipality_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

	private static final String delete_stale_ev_sql = "DELETE FROM ev_raw WHERE ev_date < (current_timestamp - time '00:30:00');";
	private static final String delete_stale_vtrack_sql = "DELETE FROM visit_track WHERE ev_date < (current_timestamp - interval '7 day');";
	private static final String delete_stale_pvtrack_sql = "DELETE FROM pv_track WHERE ev_date < (current_timestamp - interval '1 day');";
	private static final String delete_stale_ptrack_sql = "DELETE FROM pv_track WHERE ev_date < (current_timestamp - interval '7 day');";

	private static final String track_visits_sql = "INSERT INTO visit_track(ev_date, visit_count) SELECT now(), COUNT(DISTINCT site_visit_id) AS visit_count FROM ev_raw WHERE ev_date > (current_timestamp - time '00:30:00');";
	
	private static final String track_pv_sql = "INSERT INTO pv_track(pv_rate) SELECT count(1)::real/ 300::real AS pv_sec FROM ev_raw WHERE ev_raw.ev_date > (now() - '00:05:00'::time without time zone::interval);";

	private static final String global_status_sql = "SELECT total_visits, new_visits, returning_visits, seven_day_min, int8larger(seven_day_max, total_visits) AS seven_day_max FROM v_global_status;";

	private static final String host_top_sql = "SELECT hostname, COUNT(DISTINCT site_visit_id) AS visit_count FROM ev_raw WHERE ev_date > (current_timestamp - time '00:30:00') GROUP BY hostname ORDER BY 2 DESC LIMIT 20;";

	private static final String page_top_sql = "SELECT url, doc_title, COUNT(DISTINCT site_visit_id) AS visit_count FROM ev_raw  WHERE (url ~ 'http://.*?/.+') AND (url !~ E'http://blogs\\.sapo\\.pt/.*') GROUP BY url, doc_title ORDER BY 3 DESC LIMIT 15;";
	//private static final String page_top_sql = "SELECT url, doc_title, COUNT(DISTINCT site_visit_id) AS visit_count FROM ev_raw  GROUP BY url, doc_title ORDER BY 3 DESC LIMIT 8;";

	private static final String box_plot_sql = "SELECT v FROM v_box_plot;";
	
	private static final String pv_rate_history_sql = "SELECT pv_rate AS v FROM (SELECT pv_rate, ev_date FROM pv_track ORDER BY ev_date DESC LIMIT 100) AS r ORDER BY ev_date ASC;";
	
	private static final String sources_sql = "SELECT vdirect, vsearch, vrefered FROM v_source;";

	public static final void insertPageView(long site_visit_id, boolean new_site_visit, String hostname, String url, String doc_title, boolean is_direct, String referer, String keywords, String municipality_id)
	{
		DbExecutor.runActionPreparedStatement(ins_ev_sql, site_visit_id, new_site_visit, hostname, url, doc_title, is_direct, referer, keywords, municipality_id);
	}

	public static final void deleteStale()
	{
		log.info("Deleting stale records");
		DbExecutor.runActionPreparedStatement(delete_stale_ev_sql);
		DbExecutor.runActionPreparedStatement(delete_stale_pvtrack_sql);
		DbExecutor.runActionPreparedStatement(delete_stale_vtrack_sql);
		DbExecutor.runActionPreparedStatement(delete_stale_ptrack_sql);
	}
	

	public static final void trackVisits()
	{
		DbExecutor.runActionPreparedStatement(track_visits_sql);
		DbExecutor.runActionPreparedStatement(track_pv_sql);
	}
	
	
	public static final String getSources()
	{
		Db dbexec = null;
		try
		{
			dbexec = DbPool.pick();
			final DbTemplate<String> jdbc = new DbTemplate<String>();
			String tstatus = jdbc.fetchObjectWithPreparedStatment(dbexec, sources_sql, sources_extractor);
			return tstatus;
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			DbPool.release(dbexec);
		}
	}

	public static final String getGlobalStatus()
	{
		Db dbexec = null;
		try
		{
			dbexec = DbPool.pick();
			final DbTemplate<String> jdbc = new DbTemplate<String>();
			String tstatus = jdbc.fetchObjectWithPreparedStatment(dbexec, global_status_sql, gs_extractor);
			return tstatus;
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			DbPool.release(dbexec);
		}
	}

	public static final String getTopHosts()
	{
		Db dbexec = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();

			((ObjectNode) rootNode).put("type", "top_hosts");

			dbexec = DbPool.pick();
			conn = dbexec.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(host_top_sql);

			ArrayNode nodes = ((ObjectNode) rootNode).putArray("records");

			while (rs.next())
			{
				JsonNode recordNode = mapper.createObjectNode();

				((ObjectNode) recordNode).put("hostname", rs.getString("hostname"));
				((ObjectNode) recordNode).put("visit_count", rs.getLong("visit_count"));

				nodes.add(recordNode);
			}

			return rootNode.toString();

		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			DbFinalizer.closeQuietly(stmt);
			DbFinalizer.closeQuietly(rs);
			DbPool.release(dbexec);
		}
	}

	public static final String getTopPages()
	{
		Db dbexec = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();

			((ObjectNode) rootNode).put("type", "top_pages");

			dbexec = DbPool.pick();
			conn = dbexec.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(page_top_sql);

			ArrayNode nodes = ((ObjectNode) rootNode).putArray("records");

			while (rs.next())
			{
				JsonNode recordNode = mapper.createObjectNode();

				((ObjectNode) recordNode).put("url", rs.getString("url"));
				((ObjectNode) recordNode).put("doc_title", rs.getString("doc_title"));
				((ObjectNode) recordNode).put("visit_count", rs.getLong("visit_count"));

				nodes.add(recordNode);
			}

			return rootNode.toString();

		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			DbFinalizer.closeQuietly(stmt);
			DbFinalizer.closeQuietly(rs);
			DbPool.release(dbexec);
		}
	}

	public static final JsonNode buildArrayNode(String sql)
	{
		Db dbexec = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			ArrayNode bp_node = mapper.createArrayNode();

			dbexec = DbPool.pick();
			conn = dbexec.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				JsonNode recordNode = mapper.createObjectNode();

				((ObjectNode) recordNode).put("v", rs.getDouble("v"));
				bp_node.add(recordNode);
			}

			return bp_node;

		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		finally
		{
			DbFinalizer.closeQuietly(stmt);
			DbFinalizer.closeQuietly(rs);
			DbPool.release(dbexec);
		}
	}
	
	private static final ResultSetExtract<String> sources_extractor = new ResultSetExtract<String>()
	{
		public String process(ResultSet rs)
		{
			try
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.createObjectNode();

				((ObjectNode) rootNode).put("type", "sources");
				

				((ObjectNode) rootNode).put("vdirect", rs.getLong("vdirect"));
				((ObjectNode) rootNode).put("vsearch", rs.getLong("vsearch"));
				((ObjectNode) rootNode).put("vrefered", rs.getLong("vrefered"));

				return rootNode.toString();
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	private static final ResultSetExtract<String> gs_extractor = new ResultSetExtract<String>()
	{
		public String process(ResultSet rs)
		{
			try
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.createObjectNode();

				((ObjectNode) rootNode).put("type", "global_status");

				((ObjectNode) rootNode).put("total_visits", rs.getLong("total_visits"));
				((ObjectNode) rootNode).put("new_visits", rs.getLong("new_visits"));
				((ObjectNode) rootNode).put("returning_visits", rs.getLong("returning_visits"));
				((ObjectNode) rootNode).put("seven_day_min", rs.getInt("seven_day_min"));
				((ObjectNode) rootNode).put("seven_day_max", rs.getInt("seven_day_max"));

				((ObjectNode) rootNode).put("box_plot_data", buildArrayNode(box_plot_sql));
				
				((ObjectNode) rootNode).put("pv_rate_data", buildArrayNode(pv_rate_history_sql));

				return rootNode.toString();
			}
			catch (Throwable e)
			{
				throw new RuntimeException(e);
			}
		}
	};
}
