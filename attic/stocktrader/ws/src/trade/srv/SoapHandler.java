package trade.srv;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.text.StringBuilderWriter;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.xml.SoapEnvelope;
import trade.xml.SoapFault;
import trade.xml.SoapJibxSerializer;

public class SoapHandler extends BaseHandler
{
	private static final Logger log = LoggerFactory.getLogger(SoapHandler.class);
	private static final HttpMethod[] ALLOWED_METHODS = { HttpMethod.POST };
	private static final String CONTENT_TYPE = "text/xml; charset=utf-8";

	public SoapHandler()
	{
		super();
	}

	@Override
	protected HttpServiceResponse handleRequest(HttpRequest request) throws Throwable
	{
		SoapEnvelope soap_request = getSoapRequest(request);

		SoapOperationHandler op_handler = TradeOperationHandlers.getOperationHandler(soap_request);

		if (op_handler == null)
		{
			String msg = String.format("Unknown operation");
			throw new WsException(new IllegalArgumentException(msg), 400);
		}

		SoapEnvelope soap_response = op_handler.handleMessage(soap_request);

		ChannelBuffer bbf = ChannelBuffers.dynamicBuffer();
		OutputStream bout = new ChannelBufferOutputStream(bbf);

		SoapJibxSerializer.toXml(soap_response, bout);

		HttpServiceResponse srv_response = new HttpServiceResponse();

		srv_response.setHttpStatus(200);
		srv_response.setResult(bbf);
		return srv_response;
	}

	private SoapEnvelope getSoapRequest(HttpRequest request)
	{
		ChannelBuffer bb = request.getContent();

		ChannelBufferInputStream input = new ChannelBufferInputStream(bb);

		SoapEnvelope soap_request = SoapJibxSerializer.fromXml(input);
		return soap_request;
	}

	@Override
	protected HttpMethod[] getAllowedMethods(HttpRequest request)
	{
		return ALLOWED_METHODS;
	}

	@Override
	public String getContentType()
	{
		return CONTENT_TYPE;
	}

	@Override
	public HttpServiceResponse buildFaultMessageResponse(HttpRequest request, String faultCode, Throwable ex)
	{
		// Process error
		HttpServiceResponse srvResponse = new HttpServiceResponse();
		SoapFault fault = buildFaultMessage(faultCode, ex);
		SoapEnvelope soap = new SoapEnvelope();
		soap.body.fault = fault;

		ChannelBuffer bbf = ChannelBuffers.dynamicBuffer();
		OutputStream bout = new ChannelBufferOutputStream(bbf);

		try
		{
			SoapJibxSerializer.toXml(soap, bout);
		}
		catch (Throwable e)
		{
			log.error(e.getMessage(), e);
		}

		if (ex instanceof WsException)
		{
			WsException ws_ex = (WsException) ex;
			int status_code = ws_ex.getHttpStatusCode();

			if (status_code == 405)
			{
				String methods = Arrays.toString(getAllowedMethods(request));
				String message = String.format("Allowed HTTP Methods are: %s", methods);
				soap.body.fault.faultString = message;
				srvResponse.addHeader("Allow", methods);
				srvResponse.setResult(bbf);
			}

			srvResponse.setHttpStatus(ws_ex.getHttpStatusCode());
		}
		else
		{
			srvResponse.setHttpStatus(500);
		}

		srvResponse.setResult(bbf);

		return srvResponse;
	}

	private SoapFault buildFaultMessage(String faultCode, Throwable ex)
	{
		Throwable rootCause = ErrorAnalyser.findRootCause(ex);

		StringBuilderWriter sw = new StringBuilderWriter();
		PrintWriter pw = new PrintWriter(sw);

		rootCause.printStackTrace(pw);
		String reason = rootCause.getMessage();
		String detail = sw.toString();

		SoapFault sfault = new SoapFault();
		sfault.faultCode = faultCode;
		sfault.faultString = reason;
		sfault.detail = detail;
		return sfault;
	}
}