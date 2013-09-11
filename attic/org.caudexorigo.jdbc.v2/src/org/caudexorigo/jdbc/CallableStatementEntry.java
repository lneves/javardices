package org.caudexorigo.jdbc;

import java.sql.CallableStatement;

class CallableStatementEntry
{
	private final long expires;

	private final CallableStatement entry;

	public CallableStatementEntry(int ttl, CallableStatement entry)
	{
		super();
		this.expires = System.currentTimeMillis() + (ttl * 1000);
		this.entry = entry;
	}

	protected final boolean isStale()
	{
		return System.currentTimeMillis() > expires;
	}

	protected final CallableStatement get()
	{
		return entry;
	}
}