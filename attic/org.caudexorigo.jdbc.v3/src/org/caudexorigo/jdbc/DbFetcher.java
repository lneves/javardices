package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbFetcher<T>
{
	public List<T> fetchList(Db db, String sql, ResultSetHandler<T> extractor)
	{
		return fetchList(db, sql, extractor, new Object[0]);
	}

	public List<T> fetchList(Db db, String sql, ResultSetHandler<T> extractor, Object... params)
	{
		ResultSet rs = null;

		if (db == null)
		{
			throw new NullPointerException("Using a 'null' Db");
		}

		try
		{
			rs = db.fetchResultSetWithPreparedStatment(sql, params);

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
		}
	}

	public List<T> fetchList(String sql, ResultSetHandler<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.pick();
			return fetchList(db, sql, extractor, params);
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

	public List<T> fetchList(String sql, ResultSetHandler<T> extractor)
	{
		return fetchList(sql, extractor, new Object[0]);
	}

	public T fetchObject(String sql, ResultSetHandler<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.pick();
			return fetchObject(db, sql, extractor, params);
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

	public T fetchObject(String sql, ResultSetHandler<T> extractor)
	{
		return fetchObject(sql, extractor, new Object[0]);
	}

	public T fetchObject(Db db, String sql, ResultSetHandler<T> extractor, Object... params)
	{
		ResultSet rs = null;

		try
		{
			rs = db.fetchResultSetWithPreparedStatment(sql, params);

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