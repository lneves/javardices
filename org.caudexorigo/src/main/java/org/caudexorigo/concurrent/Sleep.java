package org.caudexorigo.concurrent;

public class Sleep {

  public static void time(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(ie);
    }
  }
}
