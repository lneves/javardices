package org.caudexorigo.jpt.sample;

import java.util.Random;

public class XorShift extends Random {
  private static final long serialVersionUID = 6208727693524452904L;
  private long seed;

  /**
   * Creates a new pseudo random number generator. The seed is initialized to the current
   * time, as if by <code>setSeed(System.currentTimeMillis());</code>.
   */
  public XorShift() {
    this(System.nanoTime());
  }

  /**
   * Creates a new pseudo random number generator, starting with the specified seed, using
   * <code>setSeed(seed);</code>.
   * 
   * @param seed the initial seed
   */
  public XorShift(long seed) {
    this.seed = seed;
  }

  /**
   * Returns the current state of the seed, can be used to clone the object
   * 
   * @returns the current seed
   */
  public synchronized long getSeed() {
    return seed;
  }

  /**
   * Sets the seed for this pseudo random number generator. As described above, two
   * instances of the same random class, starting with the same seed, produce the same
   * results, if the same methods are called.
   * 
   * @param s the new seed
   */
  public synchronized void setSeed(long seed) {
    this.seed = seed;
    super.setSeed(seed);
  }

  /**
   * Returns an XSRandom object with the same state as the original
   */
  public XorShift clone() {
    return new XorShift(getSeed());
  }

  /**
   * Implementation of George Marsaglia's elegant Xorshift random generator 30% faster and
   * better quality than the built-in java.util.random, see also see
   * http://www.javamex.com/tutorials/random_numbers/xorshift.shtml
   */
  protected int next(int nbits) {
    long x = seed;
    x ^= (x << 21);
    x ^= (x >>> 35);
    x ^= (x << 4);
    seed = x;
    x &= ((1L << nbits) - 1);
    return (int) x;
  }
}
