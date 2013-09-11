package org.caudexorigo.jdbc;

public class DbExecutor
{

	public static int runActionPreparedStatement(String sql)
	{
		return runActionPreparedStatement(sql, new Object[0]);
	}

	public static int runActionPreparedStatement(String sql, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.obtain();
			return db.runActionPreparedStatement(sql, params);
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

	public static int runActionStoredProcedure(String proc)
	{
		return runActionStoredProcedure(proc, new Object[0]);
	}

	public static int runActionStoredProcedure(String proc, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.obtain();
			return db.runActionStoredProc(proc, params);
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
}