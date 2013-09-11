package org.caudexorigo.jdbc;

public class DbRunner
{
	public int executeCallableStatement(Db db, String proc)
	{
		return executeCallableStatement(db, proc, new Object[0]);
	}

	public int executeCallableStatement(Db db, String proc, Object... params)
	{
		return db.executeCallableStatement(proc, params);
	}

	public int executeCallableStatement(String proc)
	{
		return executeCallableStatement(proc, new Object[0]);
	}

	public int executeCallableStatement(String proc, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.obtain();
			return executeCallableStatement(db, proc, params);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	public int executePreparedStatement(Db db, String sql, Object... params)
	{
		if ((params != null) && (params.length > 0))
		{
			return db.executePreparedStatement(sql, params);
		}
		else
		{
			return db.executeStatement(sql);
		}
	}

	public int executePreparedStatement(String sql, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.obtain();
			return executePreparedStatement(db, sql, params);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	public int executeStatement(Db db, String sql)
	{
		return executePreparedStatement(db, sql, new Object[0]);
	}

	public int executeStatement(String sql)
	{
		return executePreparedStatement(sql, new Object[0]);
	}
}