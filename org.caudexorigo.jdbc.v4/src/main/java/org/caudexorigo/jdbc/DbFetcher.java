package org.caudexorigo.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbFetcher<T>
{
	private final DbPool dbPool;

	public DbFetcher(final DbPool dbPool)
	{
		super();
		this.dbPool = dbPool;
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
			ps = db.getPreparedStatment(sql);
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

	public List<T> fetchList(final String sql, final RowConverter<T> extractor, Object... params)
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

	public List<T> fetchList(final String sql, final RowConverter<T> extractor)
	{
		return fetchList(sql, extractor, new Object[0]);
	}

	public T fetchObject(final String sql, final RowConverter<T> extractor, Object... params)
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

	public T fetchObject(final String sql, final RowConverter<T> extractor)
	{
		return fetchObject(sql, extractor, new Object[0]);
	}

	public T fetchObject(final Db db, final String sql, final RowConverter<T> extractor, Object... params)
	{
		ResultSet rs = null;
		PreparedStatement ps = null;

		try
		{
			ps = db.getPreparedStatment(sql);
			rs = db.fetchResultSetWithPreparedStatment(ps, params);

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
			if (!db.useStatementCache())
			{
				Db.closeQuietly(ps);
			}
		}
	}
}