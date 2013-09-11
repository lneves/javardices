package db.bench.tx;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.caudexorigo.ErrorAnalyser;

import db.bench.output.TxError;
import db.bench.output.TxOutput;

public abstract class TxBase implements Tx
{
	@Override
	public final TxOutput execute()
	{
		final long start = System.nanoTime();

		try
		{
			TxOutput tx_out = run();
			tx_out.tx_time = Math.abs(System.nanoTime() - start);
			return tx_out;
		}
		catch (Throwable t)
		{
			Throwable r = ErrorAnalyser.findRootCause(t);

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			r.printStackTrace(pw);
			pw.flush();

			TxError tx_error = new TxError(r.getMessage(), sw.toString());
			tx_error.tx_time = Math.abs(System.nanoTime() - start);

			return tx_error;
		}
	}

	protected abstract TxOutput run();

}