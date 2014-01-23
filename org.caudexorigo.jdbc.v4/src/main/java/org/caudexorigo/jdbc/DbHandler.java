package org.caudexorigo.jdbc;

import java.sql.ResultSet;

public class DbHandler
{
	private final DbPool dbPool;

	public DbHandler(final DbPool dbPool)
	{
		super();
		this.dbPool = dbPool;
	}

	public void handle(final String sql, final RowHandler row_handler, Object... params)
	{
		Db db = null;

		try
		{
			db = dbPool.pick();
			handle(db, sql, row_handler, params);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			dbPool.release(db);
		}
	}

	public void handle(final Db db, final String sql, final RowHandler row_handler, final Object... params)
	{
		ResultSet rs = null;

		try
		{
			rs = db.fetchResultSetWithPreparedStatment(sql, params);

			row_handler.beforeFirst(rs);

			while (rs.next())
			{
				row_handler.process(rs);
			}

			row_handler.afterLast(rs);
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			Db.closeQuietly(rs);
		}
	}
}