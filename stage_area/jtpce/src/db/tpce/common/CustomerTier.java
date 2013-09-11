package db.tpce.common;

public enum CustomerTier
{
	ONE(1), TWO(2), THREE(3);

	private final int code;

	CustomerTier(int code)
	{
		this.code = code;
	}

	public int getCode()
	{
		return code;
	}
}