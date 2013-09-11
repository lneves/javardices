package org.caudexorigo.jdbc;

class DbInfo
{
	private final String con_name;
	private final String driver_class;
	private final String driver_url;
	private final String user;
	private final String password;
	private final int ttl;

	public DbInfo(String conName, String driverClass, String driverUrl, String user, String password, int ttl)
	{
		super();
		this.con_name = conName;
		this.driver_class = driverClass;
		this.driver_url = driverUrl;
		this.user = user;
		this.password = password;
		this.ttl = ttl;
	}

	public String getConnectionGroupName()
	{
		return con_name;
	}

	public String getDriverClass()
	{
		return driver_class;
	}

	public String getDriverUrl()
	{
		return driver_url;
	}

	public String getUsername()
	{
		return user;
	}

	public String getPassword()
	{
		return password;
	}

	public int getTtl()
	{
		return ttl;
	}

	@Override
	public String toString()
	{
		return "DbInfo [con_name=" + con_name + ", driver_class=" + driver_class + ", driver_url=" + driver_url + ", ttl=" + ttl + "]";
	}

}
