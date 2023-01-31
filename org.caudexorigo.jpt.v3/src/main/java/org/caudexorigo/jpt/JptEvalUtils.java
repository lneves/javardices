package org.caudexorigo.jpt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public final class JptEvalUtils {
  private JptEvalUtils() {}

  public static Object[] evaluateToArray(Object collection) {
    if (collection == null)
      return new Object[0];
    if (collection.getClass().isArray())
      return (Object[]) collection;
    if (collection instanceof Collection)
      return ((Collection) collection).toArray();
    if (collection instanceof Iterator) {
      List<Object> content_list = new ArrayList<Object>();
      for (Object o : content_list) {
        content_list.add(o);
      }
      return content_list.toArray();
    } else {
      return new Object[0];
    }
  }
}
