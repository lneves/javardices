package org.caudexorigo.solr.shard;

import java.util.ArrayList;
import java.util.List;

import org.caudexorigo.cryto.MD5;

/**
 * A pool of resources. The location of each resource is determined by computing a pseudo hashcode for a string key, then matching that hashcode with a bucket.
 * 
 * We are using the 24 least significant bits of a MD5 hash to compute the pseudo hashcode. @see NumberUtils.stringToUnsignedIntegerCode for details.
 * 
 * @see http ://www.tomkleinpeter.com/2008/03/17/programmers-toolbox-part-3-consistent -hashing/
 * 
 * @author mkvalsvik
 * 
 */
public class ConsistentHashingResourcePool
{

	private List<HashCodeBucket> buckets = new ArrayList<HashCodeBucket>();

	public static int MAX_HASH_VALUE = (int) Math.pow(2, 24);

	public ConsistentHashingResourcePool(int numberOfResources)
	{

		int range = MAX_HASH_VALUE / numberOfResources;

		// Create buckets
		for (int i = 1; i < (numberOfResources + 1); i++)
		{

			int lowerLimit = (i - 1) * range;
			int upperLimit = (i * range) - 1;

			// Last loop?
			if (i == numberOfResources)
			{

				// Make up for rounding errors
				int diff = MAX_HASH_VALUE - upperLimit;
				upperLimit = upperLimit + diff;
			}

			HashCodeBucket bucket = new HashCodeBucket(lowerLimit, upperLimit);
			buckets.add(bucket);
		}
	}

	public int getSize()
	{
		return buckets.size();
	}

	/**
	 * Looks up the resource that is assigned to handle data represented by the given key
	 * 
	 * @param key
	 *            a string representing a unique data record
	 * @return a resource index from zero to (numberOfResources-1)
	 */
	public int locateResource(String key)
	{
		int keyHashCode = computeKeyHash(key);
		return locateResourceByKeyHash(keyHashCode);
	}

	public int locateResourceByKeyHash(int keyHashCode)
	{
		for (int i = 0; i < buckets.size(); i++)
		{
			HashCodeBucket bucket = buckets.get(i);
			if (bucket.isMatch(keyHashCode))
			{
				return i;
			}
		}

		// This should never happen
		throw new RuntimeException("Failed to locate resource for key hash" + keyHashCode);
	}

	public int computeKeyHash(String key)
	{
		// return NumberUtils.stringToUnsignedIntegerCode(key);
		return stringToUnsignedIntegerCode(key);
	}

	public HashCodeBucket getBucketByIndex(int bucketIndex)
	{
		return buckets.get(bucketIndex);
	}

	public HashCodeBucket getBucketForKey(String key)
	{
		int index = this.locateResource(key);
		return getBucketByIndex(index);
	}

	public static int stringToUnsignedIntegerCode(String s)
	{
		String md5 = MD5.getHashString(s);
		String md5Truncated = md5.substring(26, md5.length());

		// try {
		// Hex.decodeHex(md5Truncated.toCharArray());
		// } catch (DecoderException e) {
		// throw new RuntimeException("Error decoding string value: " + s, e);
		// }

		// System.out.println("size: " + b.length);

		System.out.println("" + md5 + " -> " + md5Truncated);

		int n = Integer.parseInt(md5Truncated, 16);

		return n;
	}

}
