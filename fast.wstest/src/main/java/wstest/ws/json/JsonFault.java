package wstest.ws.json;

public class JsonFault
{
	private String faultCode;

	private String faultString;

	private String detail;
	
	public JsonFault()
	{
		detail = "";
		faultCode = "";
		faultString = "";
	}

	public final String getFaultCode()
	{
		return faultCode;
	}

	public final void setFaultCode(String faultCode)
	{
		this.faultCode = faultCode;
	}

	public final String getFaultString()
	{
		return faultString;
	}

	public final void setFaultString(String faultString)
	{
		this.faultString = faultString;
	}

	public final String getDetail()
	{
		return detail;
	}

	public final void setDetail(String detail)
	{
		this.detail = detail;
	}
}
