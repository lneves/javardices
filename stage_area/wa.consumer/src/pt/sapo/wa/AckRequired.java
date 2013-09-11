package pt.sapo.wa;

public enum AckRequired
{
	YES(true), NO(false);

	private final boolean v;

	AckRequired(boolean v)
	{
		this.v = v;
	}

	public boolean get()
	{
		return this.v;
	}
}