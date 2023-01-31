package org.caudexorigo.http.netty4.reporting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBody {
  private static final Map<Integer, Boolean> status = new ConcurrentHashMap<Integer, Boolean>();

  static {
    status.put(100, true);
    status.put(101, true);
    status.put(200, true);
    status.put(201, true);
    status.put(202, true);
    status.put(203, true);
    status.put(204, false);
    status.put(205, false);
    status.put(206, true);
    status.put(300, true);
    status.put(301, true);
    status.put(302, true);
    status.put(303, true);
    status.put(304, false);
    status.put(305, true);
    status.put(307, true);
    status.put(400, true);
    status.put(401, true);
    status.put(402, true);
    status.put(403, true);
    status.put(404, true);
    status.put(405, true);
    status.put(406, true);
    status.put(407, true);
    status.put(408, true);
    status.put(409, true);
    status.put(411, true);
    status.put(412, true);
    status.put(413, true);
    status.put(414, true);
    status.put(415, true);
    status.put(416, true);
    status.put(417, true);
    status.put(500, true);
    status.put(501, true);
    status.put(502, true);
    status.put(503, true);
    status.put(504, true);
    status.put(505, true);
  }

  public static boolean allow(int code) {
    return status.get(code);
  }
}
