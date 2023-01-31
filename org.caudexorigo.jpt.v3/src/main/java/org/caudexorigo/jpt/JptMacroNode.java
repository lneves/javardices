package org.caudexorigo.jpt;

import org.apache.commons.lang3.StringUtils;
import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JptMacroNode extends JptParentNode {
  private static final Logger log = LoggerFactory.getLogger(JptMacroNode.class);

  private boolean _isInSlot;

  private final String _ctx_object_type;

  private final Map<String, String> _macroParams;

  private final URI _muri;

  private final String _tmpl;

  public JptMacroNode(URI muri, String ctx_object_type, boolean isInSlot,
      Map<String, String> macroParams) {
    _muri = muri;
    _ctx_object_type = ctx_object_type;
    _isInSlot = isInSlot;
    _macroParams = macroParams;
    _tmpl = StringUtils.substringAfterLast(StringUtils.substringBefore(_muri.toString(), "?"), "/");
  }

  public boolean isInSlot() {
    return _isInSlot;
  }

  boolean isMacroNode() {
    return true;
  }

  public void render(Map<String, Object> context, Writer out) throws IOException {
    Map<String, Object> originalContext = new HashMap<String, Object>();

    originalContext.putAll(context);

    Map<String, Object> macroContext = new HashMap<String, Object>();

    // System.out.printf("JptMacroNode template: %s # _ctx_object_type -> %s%n", _tmpl,
    // _ctx_object_type);
    // System.out.printf("JptMacroNode template: %s # isInSlot -> %s%n", _tmpl,
    // _isInSlot);
    // System.out.printf("JptMacroNode template: %s # renderContext -> %s%n", _tmpl,
    // context.toString());
    // System.out.printf("JptMacroNode template: %s # macroParams -> %s%n", _tmpl,
    // _macroParams.toString());

    Set<Entry<String, String>> param_entries = _macroParams.entrySet();

    for (Entry<String, String> entry : param_entries) {
      if ("macro".equals(entry.getKey())) {
        continue;
      }

      // System.out.printf("JptMacroNode params: %s -> %s%n", entry.getKey(),
      // entry.getValue());

      try {
        Object value = MVEL.eval(entry.getValue().toString(), context);

        // System.out.printf("JptMacroNode parsedParamValue: %s -> %s%n",
        // entry.getValue().toString(), value.toString());

        macroContext.put(entry.getKey(), value);
      } catch (org.mvel2.PropertyAccessException t) {
        log.warn(String.format(
            "%nPropertyAccessException for parameter value: '%s' in template '%s'. Using the value as string.%nRender context: %s%n%n",
            entry.getValue(), _tmpl, context.toString()));
        macroContext.put(entry.getKey(), entry.getValue());
      }
    }

    originalContext.putAll(macroContext);

    Set<String> keys = context.keySet();

    for (String key : keys) {
      if (key.equals("$this")) {
        macroContext.put("$parent", context.get(key));
      } else {
        macroContext.put(key, context.get(key));
      }
    }

    try {
      Object oCtx = Class.forName(_ctx_object_type).newInstance();
      macroContext.put("$this", oCtx);
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }

    // System.out.printf("JptMacroNode template: %s # context: %s%n", _tmpl,
    // originalContext);
    // System.out.printf("JptMacroNode template: %s # macroContext: %s%n", _tmpl,
    // macroContext);

    try {
      int child_count = getChildCount();
      for (int i = 0; i < child_count; i++) {
        JptNode jpt_node = getChild(i);

        // System.out.printf("JptMacroNode.render.jpt_node.isInSlot: %s%n",
        // jpt_node.isInSlot());

        if (jpt_node.isInSlot())
          jpt_node.render(originalContext, out);
        else
          jpt_node.render(macroContext, out);

      }
    } catch (Throwable t) {
      String s = String.format("Error during the processing of '%s'", _muri);
      log.error(s);
      throw new RuntimeException(s, t);
    }
  }

  public String getMacroTemplate() {
    return _tmpl;
  }
}
