package db.bench.output;

public class TxError extends TxOutput
{
	private final String error_message;
	private final String stack_trace;

	public TxError(String error_message, String stack_trace)
	{
		super(-1);
		this.error_message = error_message;
		this.stack_trace = stack_trace;
	}

	@Override
	public String toString()
	{
		return String.format("TxError [%nerror_message=%s%n, stack_trace=%s%n, status=%s%n, tx_time=%s%n]", error_message, stack_trace, status, tx_time);
	}
}