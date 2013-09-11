package org.caudexorigo.jdbc;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbTemplate<T>
{

	public List<T> fetchListWithPreparedStatment(Db dbexec, String sql, ResultSetExtract<T> extractor)
	{
		return fetchListWithPreparedStatment(dbexec, sql, extractor, new Object[0]);
	}

	public List<T> fetchListWithPreparedStatment(Db db, String sql, ResultSetExtract<T> extractor, Object... params)
	{
		ResultSet rs = null;

		if (db == null)
		{
			throw new NullPointerException("Using a 'null' Db");
		}

		try
		{
			rs = db.runRetrievalPreparedStatement(sql, params);

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

	public List<T> fetchListWithPreparedStatment(String sql, ResultSetExtract<T> extractor)
	{
		return fetchListWithPreparedStatment(sql, extractor, new Object[0]);
	}

	public List<T> fetchListWithPreparedStatment(String sql, ResultSetExtract<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.pick();
			return fetchListWithPreparedStatment(db, sql, extractor, params);
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

	public List<T> fetchListWithStatment(Db db, String sql, ResultSetExtract<T> extractor)
	{
		ResultSet rs = null;

		if (db == null)
		{
			throw new NullPointerException("Using a 'null' Db");
		}

		try
		{
			rs = db.runRetrievalStatement(sql);

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

	public List<T> fetchListWithStatment(String sql, ResultSetExtract<T> extractor)
	{
		{
			Db db = null;

			try
			{
				db = DbPool.pick();
				return fetchListWithStatment(db, sql, extractor);
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

	public T fetchObjectWithPreparedStatment(Db db, String sql, ResultSetExtract<T> extractor, Object... params)
	{
		ResultSet rs = null;

		try
		{
			rs = db.runRetrievalPreparedStatement(sql, params);

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

	public T fetchObjectWithPreparedStatment(String sql, ResultSetExtract<T> extractor)
	{
		return fetchObjectWithPreparedStatment(sql, extractor, new Object[0]);
	}

	public T fetchObjectWithPreparedStatment(String sql, ResultSetExtract<T> extractor, Object... params)
	{
		Db db = null;

		try
		{
			db = DbPool.pick();
			return fetchObjectWithPreparedStatment(db, sql, extractor, params);
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