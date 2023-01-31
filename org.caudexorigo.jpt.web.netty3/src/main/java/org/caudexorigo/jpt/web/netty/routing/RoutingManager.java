package org.caudexorigo.jpt.web.netty.routing;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.http.netty.HttpAction;
import org.caudexorigo.http.netty.HttpRequestWrapper;
import org.caudexorigo.http.netty.WebException;
import org.caudexorigo.jpt.web.netty.NettyWebJptAction;
import org.caudexorigo.jpt.web.netty.routing.namedregexp.NamedMatcher;
import org.caudexorigo.jpt.web.netty.routing.namedregexp.NamedPattern;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * RoutingManager For more information read routing.readme
 *
 */
public class RoutingManager {
  private static final Logger log = LoggerFactory.getLogger(RoutingManager.class);

  private static Map<HttpMethod, Map<NamedPattern, String>> routes =
      new HashMap<HttpMethod, Map<NamedPattern, String>>();
  private static Map<NamedPattern, String> reverseRoutes =
      new LinkedHashMap<NamedPattern, String>();

  private List<HttpMethod> supportedMethods = new ArrayList<HttpMethod>();

  private ArrayList<Pattern> inaccessiblePatterns = new ArrayList<Pattern>();

  private final String basePath;

  private final String baseUrl;

  /**
   * 
   * @param routesFile Refers to the configuration filepath
   * @param basePath Is the file system base path (eg.
   *        /servers/alerts/frontend/wwwroot/template)
   * @param baseUrl
   */
  public RoutingManager(String routesFile, String basePath, String baseUrl) {
    this(SafeFileReader.getFileReader(routesFile), basePath, baseUrl);
  }

  /**
   * 
   * @param reader Refers to the configuration 'file'
   * @param basePath The file system base path (eg.
   *        /servers/example_app/frontend/wwwroot/template)
   * @param baseUrl The web application base url (eg. http://www.example.com)
   */
  public RoutingManager(Reader reader, String basePath, String baseUrl) {
    this.basePath = basePath;
    this.baseUrl = baseUrl;

    supportedMethods = new ArrayList<HttpMethod>(5);
    supportedMethods.add(HttpMethod.GET);
    supportedMethods.add(HttpMethod.POST);
    supportedMethods.add(HttpMethod.DELETE);
    supportedMethods.add(HttpMethod.PUT);
    supportedMethods.add(HttpMethod.HEAD);

    try {
      BufferedReader in = new BufferedReader(reader);
      String line;
      while ((line = in.readLine()) != null) {
        if (!StringUtils.isBlank(line)) {
          processLine(line);
        }
      }
      in.close();
    } catch (IOException ioe) {
      log.error("Error processing routing file.", ioe);
    }
  }

  private void processLine(String line) {
    // Is comment?
    if (line.startsWith("#")) {
      return;
    }
    // Split
    String[] parts = line.split("\\s+");

    // 3parts or denny
    if (parts.length != 3) {
      if ((parts.length == 2) && (parts[0].equals("DENY"))) {
        // DENY entry
        inaccessiblePatterns.add(Pattern.compile(parts[1]));
        return;
      }

      log.error("Invalid route '{}'", line);
      return;
    }

    // Create uri pattern
    String uriPattern = parts[1];

    uriPattern = makePattern(uriPattern);
    if (uriPattern == null) {
      log.error("Invalid uri pattern '{}'", parts[1]);
      return;
    }

    // Create template relative path pattern
    String templatePattern = parts[2];
    templatePattern = makePattern(templatePattern);
    if (uriPattern == null) {
      log.error("Invalid template pattern '{}'", parts[2]);
      return;
    }

    // Methods
    List<HttpMethod> methods = null;
    if (parts[0].equals("*")) {
      methods = supportedMethods;
    } else {
      methods = new ArrayList<HttpMethod>(0);
      methods.add(HttpMethod.valueOf(parts[0]));
    }

    // Add to routes and reverseRoutes
    for (HttpMethod method : methods) {
      Map<NamedPattern, String> route = routes.get(method);
      if (route == null) {
        route = new LinkedHashMap<NamedPattern, String>();
        routes.put(method, route);
      }
      route.put(NamedPattern.compile(uriPattern), parts[2]);
    }
    reverseRoutes.put(NamedPattern.compile(templatePattern), parts[1]);
  }

  private String makePattern(String expression) {
    boolean searching = true;
    int strIndex = 0;

    StringBuilder sb = new StringBuilder();

    while (searching) {
      int beginIndex = expression.indexOf('<', strIndex);
      if (beginIndex == -1) {
        break;
      }
      int endIndex = expression.indexOf('>', strIndex);
      if (endIndex == -1) {
        // Incorrect format
        return null;
      }
      sb.append(expression.substring(strIndex, beginIndex)); // text prior
      // to '<'

      String partId = expression.substring(beginIndex + 1, endIndex);

      // sb.append(String.format("(?<%s>(\\d|\\w)+)", partId));
      sb.append(String.format("(?<%s>\\w+?)", partId));

      strIndex = endIndex + 1;
      if (strIndex >= expression.length()) {
        searching = false;
      }
    }
    sb.append(expression.substring(strIndex, expression.length()));
    return sb.toString();
  }

  /**
   * Given a file path template, returns a URL.
   * 
   * @param template File path template (eg. file.jpt)
   * @return an URL (eg. http://www.example.com/file)
   */
  public String reverse(String template) {
    return reverse(template, Collections.EMPTY_MAP);
  }

  /**
   * Given a file path template, returns a URL.
   * 
   * @param template File path template (eg. file.jpt)
   * @param params Extra parameters (eg. action->editar)
   * @return An URL (eg. http://www.example.com/subscricao/editar/tempo). Note: if params
   *         was not defined the returned value whould be
   *         http://www.example.com/subscricao/<action>/tempo
   */
  public String reverse(String template, Map<String, String> params) {

    HashMap<String, String> localDefaultValues = new HashMap<String, String>(params);

    String uriPath = null;
    for (NamedPattern uriPattern : reverseRoutes.keySet()) {
      NamedMatcher uriMatcher = uriPattern.matcher(template);

      if (uriMatcher.matches()) {
        // Start by replacing template path
        uriPath = reverseRoutes.get(uriPattern);
        for (String groupName : uriPattern.groupNames()) {
          String replacement = uriMatcher.group(groupName);
          uriPath = StringUtils.replace(uriPath, String.format("<%s>", groupName), replacement);

          template = StringUtils.replace(template, String.format("<%s>", groupName), replacement);

          localDefaultValues.remove(groupName);
        }

        break;
      }
    }
    for (String key : localDefaultValues.keySet()) {
      uriPath = StringUtils.replace(uriPath, String.format("<%s>", key), localDefaultValues.get(
          key));
    }

    return (uriPath != null) ? baseUrl + uriPath : null;
  }

  /**
   * Maps a resource request to a HttpAction (NettyWebJptAction) or null. Parameters
   * extracted from the the uri are added to the HttpRequestWrapper.
   * 
   * @param method HTTP Verb
   * @param uri Requested (eg. resource detalhes/astrologia)
   * @param request HttpRequestWrapper
   * @return HttpAction (NettyWebJptAction) or null
   */
  public HttpAction map(HttpMethod method, String uri, HttpRequestWrapper request) {
    for (Pattern inaccessibleUri : inaccessiblePatterns) {
      Matcher matcher = inaccessibleUri.matcher(uri);
      if (matcher.matches()) {
        log.info("Forbidding access to '{}'", uri);
        throw new WebException(new RuntimeException("Access denied"), 403);
      }
    }

    Map<NamedPattern, String> patterns = routes.get(method);
    if (patterns == null) {
      return null;
    }

    String templatePath = null;
    for (NamedPattern uriPattern : patterns.keySet()) {
      NamedMatcher uriMatcher = uriPattern.matcher(uri);

      if (uriMatcher.matches()) {
        // Start by replacing template path

        templatePath = patterns.get(uriPattern);
        for (String groupName : uriPattern.groupNames()) {
          String replacement = uriMatcher.group(groupName);
          templatePath = StringUtils.replace(templatePath, String.format("<%s>", groupName),
              replacement);

          uri = StringUtils.replace(uri, String.format("<%s>", groupName), replacement);
        }

        // Use group names to added them to request parameters

        for (String gn : uriPattern.groupNames()) {
          String value = uriMatcher.group(gn);
          if (request != null) {
            request.addParameter(gn, value);
          }
        }

        break;
      }

    }
    if (templatePath != null) {
      URI p_uri = URI.create(basePath + templatePath);
      NettyWebJptAction action = new NettyWebJptAction(p_uri);

      action.setRequest(request);
      return action;
    }

    return null;
  }
}


class SafeFileReader {
  static FileReader getFileReader(String fileName) {
    try {
      return new FileReader(fileName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
