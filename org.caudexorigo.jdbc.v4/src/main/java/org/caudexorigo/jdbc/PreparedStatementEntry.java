package org.caudexorigo.jdbc;

import java.sql.PreparedStatement;

class PreparedStatementEntry
{
	private final long expires;

	private final PreparedStatement entry;

	public PreparedStatementEntry(int ttl, PreparedStatement entry)
	{
		super();
		this.expires = System.currentTimeMillis() + (ttl * 1000);
		this.entry = entry;
	}

	protected final boolean isStale()
	{
		return System.currentTimeMillis() > expires;
	}

	protected final PreparedStatement get()
	{
		return entry;
	}
}