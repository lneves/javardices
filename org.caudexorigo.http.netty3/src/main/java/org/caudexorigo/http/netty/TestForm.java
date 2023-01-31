package org.caudexorigo.http.netty;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.util.List;
import java.util.Map;

public class TestForm extends HttpAction {

  @Override
  public void service(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
    HttpRequestWrapper req = new HttpRequestWrapper(request);

    // List<String, Mapz>
    Map<String, List<String>> params = req.getParameters();

    System.out.println(params);

  }

}
