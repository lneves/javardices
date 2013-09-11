package org.caudexorigo.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db
{
	private static Logger log = LoggerFactory.getLogger(Db.class);
	private Map<String, CallableStatementEntry> call_statement_cache;
	private Connection connection;
	private final DbInfo dbinfo;
	private boolean isActive;
	private boolean isOracle = false;
	private Map<String, PreparedStatementEntry> prep_statement_cache;
	private Statement statement;

	public Db(DbInfo dbinfo)
	{
		super();
		this.dbinfo = dbinfo;
		init();
	}

	public void beginTransaction()
	{
		try
		{
			validateConnection();
			connection.setAutoCommit(false);
		}
		catch (SQLException e)
		{
			isActive = false;
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void bindParameter(PreparedStatement prepStatement, Object param, int index, int step) throws SQLException
	{
		// Have to handle null values seperately
		if (param != null)
		{
			if (log.isDebugEnabled())
			{
				log.debug("Setting the '" + param + "' value in the statement at position " + (index + step));
			}

			// If the object is our representation of a null value, then
			// handle it seperately
			if (param instanceof NullSQLType)
			{
				prepStatement.setNull(index + step, ((NullSQLType) param).getFieldType());
			}
			else if (param instanceof String)
			{
				String s = (String) param;
				prepStatement.setString(index + step, s);
			}
			else if (param instanceof Boolean)
			{
				boolean b = ((Boolean) param).booleanValue();
				prepStatement.setBoolean(index + 1, b);
			}
			else if (param instanceof Short)
			{
				short sh = ((Short) param).shortValue();
				prepStatement.setShort(index + step, sh);
			}
			else if (param instanceof Integer)
			{
				int value = ((Integer) param).intValue();
				prepStatement.setInt(index + 1, value);
			}
			else if (param instanceof Long)
			{
				long l = ((Long) param).longValue();
				prepStatement.setLong(index + step, l);
			}
			else if (param instanceof Float)
			{
				float f = ((Float) param).floatValue();
				prepStatement.setFloat(index + step, f);
			}
			else if (param instanceof Double)
			{
				double d = ((Double) param).doubleValue();
				prepStatement.setDouble(index + step, d);
			}
			else if (param instanceof java.util.Date)
			{
				java.util.Date pdate = (java.util.Date) param;
				prepStatement.setTimestamp(index + step, new java.sql.Timestamp(pdate.getTime()));
			}
			else if (param instanceof java.sql.Timestamp)
			{
				prepStatement.setTimestamp(index + step, (java.sql.Timestamp) param);
			}
			else if (param instanceof java.sql.Date)
			{
				prepStatement.setDate(index + step, (java.sql.Date) param);
			}
			else if (param instanceof java.sql.Time)
			{
				prepStatement.setTime(index + step, (java.sql.Time) param);
			}
			else if (param instanceof org.caudexorigo.jdbc.SqlArray)
			{
				org.caudexorigo.jdbc.SqlArray arr_sql = (org.caudexorigo.jdbc.SqlArray) param;
				java.sql.Array arr_param = connection.createArrayOf(arr_sql.typeName, arr_sql.elements);
				prepStatement.setArray(index + step, arr_param);
			}
			else if (param instanceof byte[])
			{
				prepStatement.setBytes(index + step, (byte[]) param);
			}
			else
			{
				prepStatement.setObject(index + step, param);
			}
		}
		else
		{
			// Can't use a null value in a prepared statement. If you want
			// to persist a null value, use the "NullSQLType" object to
			// represent this in the prepared statement.;
			// Try to use the generic NULL placeholder
			prepStatement.setNull(index + step, java.sql.Types.NULL);
		}
	}

	private CallableStatement buildCallableStatement(String spName, int param_lenght)
	{
		StringBuilder sql;
		CallableStatement cs;
		if (isOracle)
		{
			sql = new StringBuilder("{ call ? := " + spName + "(");
		}
		else
		{
			sql = new StringBuilder("{call " + spName + "(");
		}

		for (int i = 0; i < param_lenght; i++)
		{
			sql.append("?");
			if (i < (param_lenght - 1))
				sql.append(",");
		}
		sql.append(")}");

		try
		{
			cs = connection.prepareCall(sql.toString());

		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
		return cs;
	}

	private PreparedStatement buildPreparedStatment(String sql)
	{
		PreparedStatement prepStatement;

		try
		{
			prepStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}

		return prepStatement;
	}

	private Statement buildStatement()
	{
		try
		{
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			return statement;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String ClobToString(Clob cl)
	{
		if (cl == null)
			return "";
		StringBuilder strOut = new StringBuilder();
		String aux;
		// We access to stream, as this way we don't have to use the
		// CLOB.length() which is slower...
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(cl.getCharacterStream());
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
		try
		{
			while ((aux = br.readLine()) != null)
				strOut.append(aux);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return strOut.toString();
	}

	private void __closeQuietly(Connection conn)
	{
		closeQuietly(conn);

		isActive = false;
	}

	public static void closeQuietly(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			// quiet
		}
	}

	public static void closeQuietly(ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		}
		catch (SQLException e)
		{
			// quiet
		}
	}

	public static void closeQuietly(Statement stmt)
	{
		try
		{
			if (stmt != null)
			{
				stmt.close();
			}
		}
		catch (SQLException e)
		{
			// quiet
		}
	}

	public void commitTransaction()
	{
		try
		{
			validateConnection();
			connection.commit();
			connection.setAutoCommit(true);
		}
		catch (SQLException e)
		{
			isActive = false;
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected void destroy()
	{
		closeQuietly(statement);

		Set<Map.Entry<String, PreparedStatementEntry>> pres_lst = prep_statement_cache.entrySet();
		for (Entry<String, PreparedStatementEntry> entry : pres_lst)
		{
			closeQuietly(entry.getValue().get());
		}

		Set<Map.Entry<String, CallableStatementEntry>> cs_lst = call_statement_cache.entrySet();
		for (Entry<String, CallableStatementEntry> entry : cs_lst)
		{
			closeQuietly(entry.getValue().get());
		}

		__closeQuietly(connection);
	}

	private CallableStatement getCallableStatement(String spName, int param_lenght)
	{
		String cacheKey = spName + param_lenght;

		CallableStatementEntry cse = call_statement_cache.get(cacheKey);

		if ((cse == null) || cse.isStale())
		{
			CallableStatement cs = buildCallableStatement(spName, param_lenght);
			cse = new CallableStatementEntry(dbinfo.getTtl(), cs);
			call_statement_cache.put(cacheKey, cse);
		}

		return cse.get();
	}

	public Connection getConnection()
	{
		return connection;
	}

	public DbInfo getDbInfo()
	{
		return dbinfo;
	}

	private PreparedStatement getPreparedStatment(String sql)
	{
		PreparedStatementEntry pse = prep_statement_cache.get(sql);

		if ((pse == null) || pse.isStale())
		{
			PreparedStatement cs = buildPreparedStatment(sql);
			pse = new PreparedStatementEntry(dbinfo.getTtl(), cs);
			prep_statement_cache.put(sql, pse);
		}

		return pse.get();
	}

	private void init()
	{
		try
		{
			this.connection = DriverManager.getConnection(dbinfo.getDriverUrl(), dbinfo.getUsername(), dbinfo.getPassword());
			this.statement = buildStatement();
			isActive = true;
		}
		catch (Throwable error)
		{
			log.error(error.getMessage());
			isActive = false;
		}

		this.prep_statement_cache = new HashMap<String, PreparedStatementEntry>();
		this.call_statement_cache = new HashMap<String, CallableStatementEntry>();

		if (dbinfo.getDriverClass().equals("oracle.jdbc.OracleDriver"))
		{
			isOracle = true;
		}
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void rollbackTransaction()
	{
		try
		{
			// validateConnection();
			connection.rollback();
			connection.setAutoCommit(true);
		}
		catch (SQLException e)
		{
			isActive = false;
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public int runActionPreparedStatement(String sql, Object... params)
	{
		validateConnection();

		PreparedStatement prepStatement = getPreparedStatment(sql);
		try
		{

			setPreparedStatementParameters(prepStatement, params);
			return prepStatement.executeUpdate();
		}
		catch (Throwable t)
		{
			isActive = false;
			prep_statement_cache.remove(sql);
			throw new RuntimeException(t);
		}

	}

	public int runActionStatement(String sql)
	{
		validateConnection();
		int numRecordsUpdated = 0;
		try
		{
			numRecordsUpdated = statement.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			isActive = false;
			throw new RuntimeException(e);
		}
		return numRecordsUpdated;
	}

	public int runActionStoredProc(String spName, Object... params)
	{
		validateConnection();

		int retVal = 0;

		CallableStatement cs = buildCallableStatement(spName, params.length);

		setCallableStatementParameters(cs, params);

		try
		{
			retVal = cs.executeUpdate(); // run the stored procedure
		}
		catch (Throwable t)
		{
			isActive = false;
			String cacheKey = spName + params.length;
			call_statement_cache.remove(cacheKey);
			throw new RuntimeException(t);
		}
		finally
		{
			closeQuietly(cs);
		}

		return (retVal);
	}

	public ResultSet runRetrievalPreparedStatement(String sql, Object... params)
	{
		validateConnection();
		PreparedStatement prepStatement = getPreparedStatment(sql);
		ResultSet rs = null;
		try
		{
			setPreparedStatementParameters(prepStatement, params);
			rs = prepStatement.executeQuery();
		}
		catch (SQLException e)
		{
			isActive = false;
			prep_statement_cache.remove(sql);
			throw new RuntimeException(e);
		}
		return (rs);
	}

	public ResultSet runRetrievalStatement(String sql)
	{
		validateConnection();
		ResultSet rs = null;
		try
		{
			rs = statement.executeQuery(sql);
		}
		catch (SQLException e)
		{
			isActive = false;
			throw new RuntimeException(e);
		}
		return (rs);
	}

	public ResultSet runRetrievalStoredProc(String spName, Object... params)
	{
		validateConnection();
		ResultSet retVal = null;

		try
		{
			CallableStatement cs = getCallableStatement(spName, params.length);
			if (isOracle)
			{
				// oracle.jdbc.driver.OracleTypes.CURSOR
				// public static final int CURSOR = -10;
				final int ORACLE_CURSOR = -10;
				cs.registerOutParameter(1, ORACLE_CURSOR);
				setCallableStatementParameters(cs, params);
				cs.execute();
				retVal = (ResultSet) cs.getObject(1);
			}
			else
			{
				setCallableStatementParameters(cs, params);
				retVal = cs.executeQuery(); // run the stored procedure
			}
		}
		catch (Throwable t)
		{
			isActive = false;
			String cacheKey = spName + params.length;
			call_statement_cache.remove(cacheKey);
			throw new RuntimeException(t);
		}
		return (retVal);
	}

	private void setCallableStatementParameters(CallableStatement cs, Object... params)
	{
		try
		{
			validateStatement(cs, params);

			int step = 1;
			if (isOracle)
			{
				step = 2;
			}

			for (int i = 0; i < params.length; i++)
			{
				Object param = params[i];

				bindParameter(cs, param, i, step);
			}
		}
		catch (SQLException e)
		{
			throw new RuntimeException("Store proc call failed: " + e.getMessage());
		}
	}

	private void setPreparedStatementParameters(PreparedStatement prepStatement, Object[] params) throws SQLException
	{
		validateStatement(prepStatement, params);

		// Loop through each value, determine it's corresponding SQL type,
		// and stuff that value into the prepared statement.

		for (int i = 0; i < params.length; i++)
		{
			Object param = params[i];

			bindParameter(prepStatement, param, i, 1);
		}
	}

	private void validateConnection()
	{
		try
		{
			if ((connection != null) && !connection.getAutoCommit())
			{
				return;
			}
		}
		catch (SQLException e)
		{
			log.warn("Database Connection (in auto commit mode == false) is broken. Will try to create a new connection");
			__closeQuietly(connection);
			init();
			throw new RuntimeException(e);
		}

		boolean isClosed = false;

		try
		{
			isClosed = connection.isClosed();
		}
		catch (Throwable e)
		{
			isClosed = true;
		}

		if ((connection == null) || !isActive() || isClosed)
		{
			log.warn("Database Connection to the configured database is broken. Will try to create a new connection");

			__closeQuietly(connection);
			init();
		}
	}

	private void validateStatement(PreparedStatement prepStatement, Object[] params) throws SQLException
	{
		// If we have a null value here, then bail.
		if ((params == null) || (prepStatement == null))
		{
			String message = "Cannot prepare statement with null arguments.";
			log.error(message);
			throw new SQLException(message);
		}
	}
}
