package org.caudexorigo.jpt;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public abstract class JptNode {
  private JptParentNode _parent;

  public JptNode() {
    _parent = null;
  }

  public abstract JptNode getChild(int i);

  public abstract int getChildCount();

  public final JptParentNode getParent() {
    return _parent;
  }

  boolean isConditionalNode() {
    return false;
  }

  boolean isDocument() {
    return false;
  }

  boolean isLoopNode() {
    return false;
  }

  boolean isMacroNode() {
    return false;
  }

  boolean isParentNode() {
    return false;
  }

  boolean isStaticFragment() {
    return false;
  }

  // public abstract void render(Object[] context, OutputStream outputstream) throws
  // IOException;
  public abstract void render(Map<String, Object> context, Writer out) throws IOException;

  final void setParent(JptParentNode parent) {
    _parent = parent;
  }

  public abstract boolean isInSlot();

  protected Throwable findRootCause(Throwable ex) {
    Throwable error_ex = new Exception(ex);
    while (error_ex.getCause() != null) {
      error_ex = error_ex.getCause();
    }
    return error_ex;
  }
}
