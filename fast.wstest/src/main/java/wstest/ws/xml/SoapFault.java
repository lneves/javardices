package wstest.ws.xml;

public class SoapFault {
  public String faultCode;

  public String faultString;

  public String detail;

  public SoapFault() {
    detail = "";
    faultCode = "";
    faultString = "";
  }

  @Override
  public String toString() {
    return String.format("SoapFault [faultCode=%s, faultString=%s]", faultCode, faultString);
  }
}
