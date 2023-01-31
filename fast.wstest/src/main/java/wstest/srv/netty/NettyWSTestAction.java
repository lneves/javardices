package wstest.srv.netty;

import org.apache.commons.lang3.StringUtils;
import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.http.netty4.HttpAction;
import org.caudexorigo.io.UnsynchronizedStringWriter;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.JiBXParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import wstest.srv.IWSTest;
import wstest.srv.WSTestImpl;
import wstest.srv.actors.COfTestStruct;
import wstest.srv.actors.EchoListResponse;
import wstest.srv.actors.EchoStructResponse;
import wstest.srv.actors.EchoSyntheticResponse;
import wstest.srv.actors.EchoVoidResponse;
import wstest.srv.actors.GetOrder;
import wstest.srv.actors.GetOrderResponse;
import wstest.srv.actors.Synthetic;
import wstest.srv.actors.TestNode;
import wstest.ws.xml.SoapEnvelope;
import wstest.ws.xml.SoapFault;
import wstest.ws.xml.SoapSerializer;

public class NettyWSTestAction extends HttpAction {
  private static final String content_type = "text/xml; charset=utf-8";

  private static final Logger log = LoggerFactory.getLogger(NettyWSTestAction.class);

  private static final IWSTest _srv = new WSTestImpl();

  @Override
  public void service(ChannelHandlerContext ctx, FullHttpRequest request,
      FullHttpResponse response) {
    validateRequest(request, response);

    SoapEnvelope s = new SoapEnvelope();

    try {
      SoapEnvelope req_message = SoapSerializer.fromXml(new ByteBufInputStream(request.content()));

      if (req_message.body.echoList != null) {
        TestNode tn = req_message.body.echoList.getList();
        EchoListResponse elr = new EchoListResponse();
        elr.setEchoListResult(_srv.echoList(tn));
        s.body.echoListResponse = elr;
      } else if (req_message.body.echoStruct != null) {
        COfTestStruct cots = req_message.body.echoStruct.getArray();
        EchoStructResponse esr = new EchoStructResponse();
        esr.setEchoStructResult(_srv.echoStruct(cots));
        s.body.echoStructResponse = esr;
      } else if (req_message.body.echoSynthetic != null) {
        Synthetic synth = req_message.body.echoSynthetic.getSynth();
        EchoSyntheticResponse esr = new EchoSyntheticResponse();
        esr.setEchoSyntheticResult(_srv.echoSynthetic(synth));
        s.body.echoSyntheticResponse = esr;
      } else if (req_message.body.echoVoid != null) {
        EchoVoidResponse evr = new EchoVoidResponse();
        s.body.echoVoidResponse = evr;
      } else if (req_message.body.getOrder != null) {
        GetOrderResponse gor = new GetOrderResponse();
        GetOrder go = req_message.body.getOrder;
        gor.setGetOrderResult(_srv.getOrder(go.getOrderId(), go.getCustomerId(), go
            .getMessageSize()));
        s.body.getOrderResponse = gor;
      }
    } catch (Throwable t) {
      fault(s, t);
    }

    response.headers().set(HttpHeaders.Names.CONTENT_TYPE, content_type);

    OutputStream out = new ByteBufOutputStream(response.content());

    SoapSerializer.toXml(s, out);

    try {
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void fault(SoapEnvelope s, Throwable t) {
    Throwable r = ErrorAnalyser.findRootCause(t);
    s.body.fault = new SoapFault();

    if ((r instanceof JiBXException) || (r instanceof JiBXParseException)) {
      s.body.fault.faultCode = "Client";
    } else {
      s.body.fault.faultCode = "Server";
    }

    s.body.fault.faultString = r.getMessage();

    s.body.fault.detail = buildStackTrace(r);

    log.error(s.body.fault.toString());
  }

  private void validateRequest(HttpRequest request, HttpResponse response) {
    if (request.getMethod() != HttpMethod.POST) {
      response.setStatus(HttpResponseStatus.METHOD_NOT_ALLOWED);
      throw new IllegalArgumentException(HttpResponseStatus.METHOD_NOT_ALLOWED.reasonPhrase());
    }

    if (HttpHeaders.isTransferEncodingChunked(request)) {
      response.setStatus(HttpResponseStatus.BAD_REQUEST);
      throw new IllegalArgumentException(HttpResponseStatus.BAD_REQUEST.reasonPhrase());
    }

    String path = request.getUri();

    if (StringUtils.isBlank(path)) {
      response.setStatus(HttpResponseStatus.FORBIDDEN);
      throw new IllegalArgumentException(HttpResponseStatus.FORBIDDEN.reasonPhrase());
    }
  }

  private static String buildStackTrace(Throwable ex) {
    UnsynchronizedStringWriter sw = new UnsynchronizedStringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    return sw.toString();
  }
}
