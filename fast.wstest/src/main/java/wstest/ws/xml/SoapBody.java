package wstest.ws.xml;

import wstest.srv.actors.EchoList;
import wstest.srv.actors.EchoListResponse;
import wstest.srv.actors.EchoStruct;
import wstest.srv.actors.EchoStructResponse;
import wstest.srv.actors.EchoSynthetic;
import wstest.srv.actors.EchoSyntheticResponse;
import wstest.srv.actors.EchoVoid;
import wstest.srv.actors.EchoVoidResponse;
import wstest.srv.actors.GetOrder;
import wstest.srv.actors.GetOrderResponse;

public class SoapBody {
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
