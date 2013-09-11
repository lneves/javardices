package trade.srv;

import java.util.HashMap;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;

public class HttpServiceResponse
{

	private int httpStatus;

	private ChannelBuffer result;

	private final Map<String, String> headers;

	public HttpServiceResponse()
	{
		super();
		headers = new HashMap<String, String>();
		httpStatus = 500;
	}

	public int getHttpStatus()
	{
		return httpStatus;
	}

	public ChannelBuffer getResult()
	{
		return result;
	}

	public void setHttpStatus(int httpResponseCode)
	{
		this.httpStatus = httpResponseCode;
	}

	public void setResult(ChannelBuffer result)
	{
		this.result = result;
	}

	public void addHeader(String name, String value)
	{
		headers.put(name, value);
	}

	public Map<String, String> getHeaders()
	{
		return headers;
	}

	public boolean hasHeaders()
	{
		return !headers.isEmpty();
	}

	public String getHeader(String header)
	{
		return headers.get(header);
	}
}