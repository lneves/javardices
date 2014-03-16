package org.caudexorigo.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbFetcher<T>
{
	public DbFetcher()
	{
		super();
	}

	public List<T> fetchList(final Db db, final String sql, RowConverter<T> extractor)
	{
		return fetchList(db, sql, extractor, new Object[0]);
	}

	public List<T> fetchList(final Db db, final String sql, RowConverter<T> extractor, Object... params)
	{
		ResultSet rs = null;
		PreparedStatement ps = null;

		if (db == null)
		{
			throw new NullPointerException("Using a 'null' Db");
		}

		try
		{
			ps = db.getPreparedStatement(sql);
			rs = db.fetchResultSetWithPreparedStatment(ps, params);

			List<T> lst = new ArrayList<T>();

			while (rs.next())
			{
				T obj = extractor.process(rs);
				lst.add(obj);
			}

			return lst;
		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			Db.closeQuietly(rs);
			if (!db.useStatementCache())
			{
				Db.closeQuietly(ps);
			}
		}
	}

	public List<T> fetchList(final DbPool dbPool, final String sql, final RowConverter<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = dbPool.pick();
			return fetchList(db, sql, extractor, params);
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

	public List<T> fetchList(final DbPool dbPool, final String sql, final RowConverter<T> extractor)
	{
		return fetchList(dbPool, sql, extractor, new Object[0]);
	}

	public T fetchObject(final DbPool dbPool, final String sql, final RowConverter<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = dbPool.pick();
			return fetchObject(db, sql, extractor, params);
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

	public T fetchObject(final DbPool dbPool, final String sql, final RowConverter<T> extractor)
	{
		return fetchObject(dbPool, sql, extractor, new Object[0]);
	}

	public T fetchObject(final Db db, final String sql, final RowConverter<T> extractor, Object... params)
	{
		PreparedStatement ps = null;

		try
		{
			ps = db.getPreparedStatement(sql);
			return fetchObject(db, ps, extractor, params);

		}
		catch (Throwable ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			if (!db.useStatementCache())
			{
				Db.closeQuietly(ps);
			}
		}
	}

	public T fetchObject(final Db db, final PreparedStatement pstmt, final RowConverter<T> extractor, Object... params)
	{
		ResultSet rs = null;

		try
		{
			rs = db.fetchResultSetWithPreparedStatment(pstmt, params);

			if (rs.next())
			{
				T obj = extractor.process(rs);
				return obj;
			}
			else
			{
				return null;
			}
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