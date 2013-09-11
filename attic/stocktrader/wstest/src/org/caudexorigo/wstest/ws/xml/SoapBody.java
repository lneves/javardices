package org.caudexorigo.wstest.ws.xml;

import org.caudexorigo.wstest.ws.api.EchoList;
import org.caudexorigo.wstest.ws.api.EchoListResponse;
import org.caudexorigo.wstest.ws.api.EchoStruct;
import org.caudexorigo.wstest.ws.api.EchoStructResponse;
import org.caudexorigo.wstest.ws.api.EchoSynthetic;
import org.caudexorigo.wstest.ws.api.EchoSyntheticResponse;
import org.caudexorigo.wstest.ws.api.EchoVoid;
import org.caudexorigo.wstest.ws.api.EchoVoidResponse;
import org.caudexorigo.wstest.ws.api.GetOrder;
import org.caudexorigo.wstest.ws.api.GetOrderResponse;

public class SoapBody
{
	public SoapFault fault;
	public EchoList echoList;
	public EchoListResponse echoListResponse;
	public EchoStruct echoStruct;
	public EchoStructResponse echoStructResponse;
	public EchoSynthetic echoSynthetic;
	public EchoSyntheticResponse echoSyntheticResponse;
	public EchoVoid echoVoid;
	public EchoVoidResponse echoVoidResponse;
	public GetOrder getOrder;
	public GetOrderResponse getOrderResponse;
}