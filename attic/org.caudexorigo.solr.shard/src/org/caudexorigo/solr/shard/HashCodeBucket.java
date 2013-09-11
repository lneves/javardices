package org.caudexorigo.solr.shard;

/**
 * Represents a 'bucket' or 'range' of hash code values that should be allocated to one specific resource in a pool
 * 
 * @author mkvalsvik
 * 
 */
public class HashCodeBucket
{

	private int lowerLimit;

	private int upperLimit;

	public HashCodeBucket(int lowerLimit, int upperLimit)
	{
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("bucket from ");
		sb.append(lowerLimit);
		sb.append(" to ");
		sb.append(upperLimit);
		return sb.toString();
	}

	public int getLowerLimit()
	{
		return lowerLimit;
	}

	public int getUpperLimit()
	{
		return upperLimit;
	}

	public boolean isMatch(int hashCode)
	{
		if (hashCode <= upperLimit && hashCode >= lowerLimit)
		{
			return true;
		}
		return false;
	}
}