package org.caudexorigo.jpt;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.nu.xom.Attribute;
import org.unbescape.xml.XmlEscape;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JptAttributeNode extends JptNode {
  private static final char QUOTE = '"';

  private final char[] attributePrefix;

  private boolean _isInSlot;

  private final JptExpression jptExpression;

  private final boolean _escapeContent;

  JptAttributeNode(Attribute attribute, boolean isInSlot, boolean escapeContent) {
    _isInSlot = isInSlot;
    _escapeContent = escapeContent;

    String attributeName = attribute.getQualifiedName();
    String attrExp = attribute.getValue().replace("\'", "\"").trim();

    attributePrefix = (new StringBuilder())
        .append(" ")
        .append(attributeName)
        .append("=")
        .append(QUOTE).toString().toCharArray();

    jptExpression = new JptExpression(attrExp);

  }

  public int getChildCount() {
    return 0;
  }

  public JptNode getChild(int i) {
    throw new IndexOutOfBoundsException("Attributes do not have children");
  }

  public void render(Map<String, Object> context, Writer out) throws IOException {
    try {
      String result = jptExpression.evalAsString(context);

      if (StringUtils.isNotBlank(result)) {
        String sout = _escapeContent ? XmlEscape.escapeXml10Attribute(result) : result;

        out.write(attributePrefix, 0, attributePrefix.length);
        out.write(sout);
        out.write(QUOTE);
      }
    } catch (Throwable t) {
      Throwable r = findRootCause(t);
      String emsg = String.format(
          "Error processing JptAttributeNode:%nexpression: '%s';%ncontext: %s;%nmessage: '%s'",
          jptExpression.getExpression(), context, r.getMessage());
      throw new RuntimeException(emsg);
    }
  }

  public boolean isInSlot() {
    return _isInSlot;
  }
}
