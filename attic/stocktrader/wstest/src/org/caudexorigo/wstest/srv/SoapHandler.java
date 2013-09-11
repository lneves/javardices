package org.caudexorigo.wstest.srv;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.caudexorigo.ErrorAnalyser;
import org.caudexorigo.text.StringBuilderWriter;
import org.caudexorigo.text.StringUtils;
import org.caudexorigo.wstest.domain.COfTestStruct;
import org.caudexorigo.wstest.domain.Synthetic;
import org.caudexorigo.wstest.domain.TestNode;
import org.caudexorigo.wstest.ws.api.EchoListResponse;
import org.caudexorigo.wstest.ws.api.EchoStructResponse;
import org.caudexorigo.wstest.ws.api.EchoSyntheticResponse;
import org.caudexorigo.wstest.ws.api.EchoVoidResponse;
import org.caudexorigo.wstest.ws.api.GetOrder;
import org.caudexorigo.wstest.ws.api.GetOrderResponse;
import org.caudexorigo.wstest.ws.api.IWSTest;
import org.caudexorigo.wstest.ws.api.WSTestImpl;
import org.caudexorigo.wstest.ws.xml.SoapEnvelope;
import org.caudexorigo.wstest.ws.xml.SoapFault;
import org.caudexorigo.wstest.ws.xml.SoapJibxSerializer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapHandler extends BaseHandler
{
	private static final Logger log = LoggerFactory.getLogger(SoapHandler.class);
	private static final HttpMethod[] ALLOWED_METHODS = { HttpMethod.POST };
	private static final String CONTENT_TYPE = "text/xml; charset=utf-8";

	private final Map<String, SoapOperationHandler> operationHandlers = new HashMap<String, SoapOperationHandler>();

	private final IWSTest srv = new WSTestImpl();

	private final SoapOperationHandler echo_list_handler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			TestNode tn = soap_in.body.echoList.getList();
			EchoListResponse elr = new EchoListResponse();
			elr.setEchoListResult(srv.echoList(tn));

			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.echoListResponse = elr;
			return soap_out;
		}
	};

	private final SoapOperationHandler echo_struct_handler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			COfTestStruct cots = soap_in.body.echoStruct.getArray();
			EchoStructResponse esr = new EchoStructResponse();
			esr.setEchoStructResult(srv.echoStruct(cots));

			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.echoStructResponse = esr;
			return soap_out;
		}
	};

	private final SoapOperationHandler echo_synthetic_handler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Synthetic synth = soap_in.body.echoSynthetic.getSynth();
			EchoSyntheticResponse esr = new EchoSyntheticResponse();
			esr.setEchoSyntheticResult(srv.echoSynthetic(synth));

			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.echoSyntheticResponse = esr;
			return soap_out;
		}
	};

	private final SoapOperationHandler echo_void_handler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.echoVoidResponse = new EchoVoidResponse();
			return soap_out;
		}
	};

	private final SoapOperationHandler get_order_handler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetOrderResponse gor = new GetOrderResponse();
			GetOrder go = soap_in.body.getOrder;
			gor.setGetOrderResult(srv.getOrder(go.getOrderId(), go.getCustomerId(), go.getMessageSize()));

			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getOrderResponse = gor;
			return soap_out;
		}
	};

	public SoapHandler()
	{
		super();
		operationHandlers.put("\"uri:WSTestWeb-TestService/EchoList\"", echo_list_handler);
		operationHandlers.put("\"uri:WSTestWeb-TestService/EchoStruct\"", echo_struct_handler);
		operationHandlers.put("\"uri:WSTestWeb-TestService/EchoSynthetic\"", echo_synthetic_handler);
		operationHandlers.put("\"uri:WSTestWeb-TestService/EchoVoid\"", echo_void_handler);
		operationHandlers.put("\"uri:WSTestWeb-TestService/GetOrder\"", get_order_handler);
	}

	@Override
	protected HttpServiceResponse handleRequest(HttpRequest request) throws Throwable
	{
		String action = request.getHeader("SOAPAction");

		if (StringUtils.isBlank(action))
		{
			throw new WsException(new IllegalArgumentException("Missing required Header 'SOAPAction'"), 400);
		}

		SoapOperationHandler op_handler = operationHandlers.get(action);

		if (op_handler == null)
		{
			String msg = String.format("Unknown operation: '%s'", action);
			throw new WsException(new IllegalArgumentException(msg), 400);
		}

		SoapEnvelope soap_request = getSoapRequest(request);
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