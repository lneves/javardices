package org.caudexorigo;

import java.util.concurrent.atomic.AtomicBoolean;

public class Shutdown {
  private static final String NO_SPACE_LEFT = "No space left on device".toLowerCase();
  private static final String ULIMIT_ERROR_MESSAGE = "Too many open files".toLowerCase();
  private static final String TIMER_CANCELED_ERROR_MESSAGE = "Timer already cancelled"
      .toLowerCase();
  private static final AtomicBoolean is_shuttingdown = new AtomicBoolean(false);

  public static void main(String[] args) {
    // NOP: ServiceWrapper artifact
  }

  public static void now() {
    System.out.println("\nExiting... ");
    exit(0);
  }

  public static void now(Throwable t) {
    Throwable root = ErrorAnalyser.findRootCause(t);
    root.printStackTrace();
    System.err.println("\nExiting... ");
    exit(-1);
  }

  public static boolean isShutingDown() {
    return is_shuttingdown.get();
  }

  private static void exit(int status) {
    if (is_shuttingdown.getAndSet(true)) {
      return;
    }

    while (true) {
      System.exit(status);
    }
  }

  public static void exitIfOOM(Throwable t) {
    Throwable r = ErrorAnalyser.findRootCause(t);
    if (r instanceof OutOfMemoryError) {
      Shutdown.now(r);
    }
  }

  public static void exitIfUlimit(Throwable t) {
    Throwable r = ErrorAnalyser.findRootCause(t);
    if (r.getMessage() != null) {
      String emsg = t.getMessage().toLowerCase();
      if (emsg.contains(ULIMIT_ERROR_MESSAGE)) {
        Shutdown.now(r);
      }
    }
  }

  public static void exitIfTimerCanceled(Throwable t) {
    Throwable r = ErrorAnalyser.findRootCause(t);
    if ((r instanceof java.lang.IllegalStateException) && (TIMER_CANCELED_ERROR_MESSAGE
        .equalsIgnoreCase(r.getMessage()))) {
      Shutdown.now(r);
    }
  }

  public static void exitIfNoSpaceLeft(Throwable t) {
    Throwable r = ErrorAnalyser.findRootCause(t);
    if ((r instanceof java.io.IOException) && (NO_SPACE_LEFT.equalsIgnoreCase(r.getMessage()))) {
      Shutdown.now(r);
    }
  }

  public static void exitIfCritical(Throwable t) {
    exitIfOOM(t);
    exitIfUlimit(t);
    exitIfTimerCanceled(t);
    exitIfNoSpaceLeft(t);
  }
}
