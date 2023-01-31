package org.caudexorigo.jpt;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JptInstance {
  private JptDocument _jptDocument;

  private ArrayList<Dependency> _dependecies;

  public JptInstance(final JptConfiguration jptConf, URI templateUri) {
    org.caudexorigo.nu.xom.Document doc;
    try {
      doc = XomDocumentBuilder.getDocument(templateUri);
    } catch (Throwable t) {
      throw new JptException(t);
    }
    JptNodeBuilder nbuilder = new JptNodeBuilder(jptConf);
    nbuilder.process(doc, templateUri);
    _dependecies = nbuilder.getDependecies();

    Dependency d = new Dependency(templateUri);
    _dependecies.add(d);
    _jptDocument = nbuilder.getJptDocument();
  }

  public JptInstance(final JptConfiguration jptConf, InputStream instream, String resourcePath) {
    org.caudexorigo.nu.xom.Document doc;
    try {
      doc = XomDocumentBuilder.getDocument(instream);
    } catch (Exception e) {
      throw new JptException(e);
    }
    URI resourceUri = URI.create(resourcePath);
    JptNodeBuilder nbuilder = new JptNodeBuilder(jptConf);
    nbuilder.process(doc, resourceUri);
    _jptDocument = nbuilder.getJptDocument();
  }

  public void render(Writer out) throws Throwable {
    String obj_type_name = StringUtils.isBlank(_jptDocument.getCtxObjectType()) ? "java.lang.Object"
        : _jptDocument.getCtxObjectType();
    Object oCtx = Class.forName(obj_type_name).newInstance();
    Map<String, Object> renderContext = new HashMap<String, Object>();
    renderContext.put("$this", oCtx);

    _jptDocument.render(renderContext, out);
  }

  public void render(Map<String, Object> renderContext, Writer out) throws Throwable {
    _jptDocument.render(renderContext, out);
  }

  public String getCtxObjectType() {
    return _jptDocument.getCtxObjectType();
  }

  public ArrayList<Dependency> getDependecies() {
    return _dependecies;
  }

  public JptDocument getJptDocument() {
    return _jptDocument;
  }
}
