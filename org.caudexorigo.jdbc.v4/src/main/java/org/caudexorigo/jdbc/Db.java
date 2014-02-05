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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.caudexorigo.ErrorAnalyser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db
{
	private static Logger log = LoggerFactory.getLogger(Db.class);
	private static final int MAX_TRIES = 0;

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

	private Map<String, CallableStatementEntry> call_statement_cache;
	private Connection connection;
	private final DbInfo dbinfo;
	private boolean isActive;

	private boolean isOracle = false;

	private Map<String, PreparedStatementEntry> prep_statement_cache;

	private Statement statement;

	private Object mutex = new Object();

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

	public Connection getConnection()
	{
		validateConnection();
		return connection;
	}

	public DbInfo getDbInfo()
	{
		return dbinfo;
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

	private void __closeQuietly(Connection conn)
	{
		closeQuietly(conn);
		isActive = false;
		conn = null;
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
			CallableStatement cs = connection.prepareCall(sql.toString());

			try
			{
				cs.setQueryTimeout(dbinfo.getQueryTimeout());
			}
			catch (Throwable t)
			{
				log.warn("'setQueryTimeout(int)' is not yet implemented");
			}

			return cs;
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	private PreparedStatement buildPreparedStatment(String sql)
	{
		synchronized (mutex)
		{
			try
			{
				PreparedStatement prepStatement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

				try
				{
					prepStatement.setQueryTimeout(dbinfo.getQueryTimeout());
				}
				catch (Throwable t)
				{
					log.warn("'setQueryTimeout(int)' is not yet implemented");
				}

				return prepStatement;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private Statement buildStatement()
	{
		try
		{
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			try
			{
				statement.setQueryTimeout(dbinfo.getQueryTimeout());
			}
			catch (Throwable t)
			{
				log.warn("'setQueryTimeout(int)' is not yet implemented");
			}

			return statement;
		}
		catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private String clobToString(Clob cl)
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

	private int executeCallableStatement(int retryCount, String spName, Object... params)
	{
		if (params == null)
		{
			params = new Object[0];
		}

		validateConnection();

		CallableStatement cs = getCallableStatement(spName, params.length);

		setCallableStatementParameters(cs, params);

		try
		{
			return cs.executeUpdate(); // run the stored procedure
		}
		catch (Throwable t)
		{
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return executeCallableStatement(++retryCount, spName, params);
			}
		}
		finally
		{
			if (!dbinfo.getUseCache())
			{
				closeQuietly(cs);
			}
		}
	}

	private int executePreparedStatement(int retryCount, String sql, Object... params)
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
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return executePreparedStatement(++retryCount, sql, params);
			}
		}
		finally
		{
			if (!dbinfo.getUseCache())
			{
				closeQuietly(prepStatement);
			}
		}
	}

	private int executeStatement(int retryCount, String sql)
	{
		validateConnection();
		try
		{
			return statement.executeUpdate(sql);
		}
		catch (Throwable t)
		{
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return executeStatement(++retryCount, sql);
			}
		}
	}

	private ResultSet fetchResultSetWithCallableStatement(int retryCount, String spName, Object... params)
	{
		if (params == null)
		{
			params = new Object[0];
		}

		validateConnection();

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
				return (ResultSet) cs.getObject(1);
			}
			else
			{
				setCallableStatementParameters(cs, params);
				return cs.executeQuery(); // run the stored procedure
			}
		}
		catch (Throwable t)
		{
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return fetchResultSetWithPreparedStatment(++retryCount, spName, params);
			}
		}
	}

	private ResultSet fetchResultSetWithPreparedStatment(int retryCount, String sql, Object... params)
	{
		validateConnection();

		PreparedStatement prepStatement = getPreparedStatment(sql);
		ResultSet rs = null;
		try
		{
			setPreparedStatementParameters(prepStatement, params);
			rs = prepStatement.executeQuery();

			return (rs);
		}
		catch (Throwable t)
		{
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return fetchResultSetWithPreparedStatment(++retryCount, sql, params);
			}
		}
	}

	private ResultSet fetchResultSetWithStatment(int retryCount, String sql)
	{
		validateConnection();
		ResultSet rs = null;
		try
		{
			rs = statement.executeQuery(sql);
		}
		catch (Throwable t)
		{
			if (isInTransaction() || (retryCount >= MAX_TRIES))
			{
				throw new RuntimeException(t);
			}
			else
			{
				destroy();
				log.warn("Error: '{}'. Will try to execute database command one more time", t.getMessage());
				init();
				return fetchResultSetWithStatment(++retryCount, sql);
			}
		}
		return (rs);
	}

	private synchronized CallableStatement getCallableStatement(String spName, int param_lenght)
	{
		if (dbinfo.getUseCache())
		{
			synchronized (mutex)
			{
				String cacheKey = spName + param_lenght;

				CallableStatementEntry cse = call_statement_cache.get(cacheKey);

				if ((cse == null) || cse.isStale())
				{
					if (cse != null)
					{
						closeQuietly(cse.get());
					}

					CallableStatement cs = buildCallableStatement(spName, param_lenght);
					cse = new CallableStatementEntry(dbinfo.getTtl(), cs);
					call_statement_cache.put(cacheKey, cse);
				}

				return cse.get();
			}
		}
		else
		{
			return buildCallableStatement(spName, param_lenght);
		}
	}

	private PreparedStatement getPreparedStatment(String sql)
	{
		if (dbinfo.getUseCache())
		{
			synchronized (mutex)
			{
				PreparedStatementEntry pse = prep_statement_cache.get(sql);

				if ((pse == null) || pse.isStale())
				{
					if (pse != null)
					{
						closeQuietly(pse.get());
					}

					PreparedStatement cs = buildPreparedStatment(sql);
					pse = new PreparedStatementEntry(dbinfo.getTtl(), cs);
					prep_statement_cache.put(sql, pse);
				}

				return pse.get();
			}
		}
		else
		{
			return buildPreparedStatment(sql);
		}
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

		if ("oracle.jdbc.OracleDriver".equals(dbinfo.getDriverClass()))
		{
			isOracle = true;
		}
	}

	private boolean isInTransaction()
	{
		try
		{
			return !connection.getAutoCommit();
		}
		catch (SQLException e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
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
		catch (Throwable e)
		{
			Throwable r = ErrorAnalyser.findRootCause(e);
			log.error("Database Connection (in auto commit mode == false) is broken. Message: '{}'. Will try to create a new connection", r.getMessage());
			__closeQuietly(connection);
			init();
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
			log.error("Database Connection to the configured database is broken. Will try to create a new connection");
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

	protected void destroy()
	{
		closeQuietly(statement);

		Collection<PreparedStatementEntry> pres_lst = prep_statement_cache.values();
		for (PreparedStatementEntry entry : pres_lst)
		{
			closeQuietly(entry.get());
		}

		Collection<CallableStatementEntry> cs_lst = call_statement_cache.values();
		for (CallableStatementEntry entry : cs_lst)
		{
			closeQuietly(entry.get());
		}

		__closeQuietly(connection);
	}

	protected int executeCallableStatement(String spName)
	{
		return executeCallableStatement(0, spName, new Object[0]);
	}

	protected int executeCallableStatement(String spName, Object... params)
	{
		return executeCallableStatement(0, spName, params);
	}

	protected int executePreparedStatement(String sql, Object... params)
	{
		return executePreparedStatement(0, sql, params);
	}

	protected int executeStatement(String sql)
	{
		return executeStatement(0, sql);
	}

	public ResultSet fetchResultSetWithCallableStatement(String spName)
	{
		return fetchResultSetWithCallableStatement(spName, new Object[0]);
	}

	public ResultSet fetchResultSetWithCallableStatement(String spName, Object... params)
	{
		return fetchResultSetWithCallableStatement(0, spName, params);
	}

	public ResultSet fetchResultSetWithPreparedStatment(String sql, Object... params)
	{
		return fetchResultSetWithPreparedStatment(0, sql, params);
	}

	public ResultSet fetchResultSetWithStatment(String sql)
	{
		return fetchResultSetWithStatment(0, sql);
	}

	protected boolean isActive()
	{
		return isActive;
	}
}