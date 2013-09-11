package db.bench;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.Shutdown;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class GenericLoadDriver
{
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(GenericLoadDriver.class);

	private final int nclients;
	private final int iterations;
	private final int wu_iter;
	private final TxMixGenerator mix_gen;

	private final int bl_iter;

	public GenericLoadDriver(int nclients, int iterations, String log_level, int wu_iter, int bl_iter, TxMixGenerator mix_gen)
	{
		super();
		this.nclients = nclients;
		this.iterations = iterations;
		this.wu_iter = wu_iter;
		this.bl_iter = bl_iter;
		this.mix_gen = mix_gen;

		System.out.printf("%nStarting Driver%nProfile: %s%nClients: %s%nIterations per Client:%s%nWarmUp Iterations:%s%nDebug Level: %s%n%n", mix_gen.profile(), nclients, iterations, wu_iter, log_level);

		ch.qos.logback.classic.Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(Level.valueOf(log_level));
	}

	public void run()
	{
		if (wu_iter > 0)
		{
			doWarmUp(nclients, wu_iter);
		}
		
		if (bl_iter > 0)
		{
			doBaseLine(nclients, bl_iter);
			return;
		}

		log.info(String.format("Begin main test, %s clients, %s iterations", nclients, iterations));

		CountDownLatch latch = new CountDownLatch(nclients);
		ExecutorService exec = Executors.newFixedThreadPool(nclients);

		final List<ClientDriver> lst_clients = new ArrayList<ClientDriver>();
		final long start_ts = System.currentTimeMillis();

		for (int i = 1; i <= nclients; i++)
		{
			String client_name = String.format("Client #%s", i);
			ClientDriver client = new ClientDriver(client_name, latch, iterations, mix_gen);
			lst_clients.add(client);
			exec.execute(client);
		}

		long interval = 10L;
		ScheduledExecutorService sched_exec = Executors.newScheduledThreadPool(1);
		sched_exec.scheduleAtFixedRate(new RuntimeCounter((double) interval, lst_clients, iterations), 0L, interval, TimeUnit.SECONDS);

		Report rpt = new Report(start_ts, lst_clients, mix_gen.transactionNames());
		Runtime.getRuntime().addShutdownHook(rpt);

		try
		{
			latch.await();
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	private void doBaseLine(int nclients, int iterations)
	{
		log.info(String.format("Begin baseline test, %s clients, %s iterations", nclients, iterations));

		try
		{
			CountDownLatch latch = new CountDownLatch(nclients);
			ExecutorService exec = Executors.newFixedThreadPool(nclients);

			List<ClientDriver> lst_clients = new ArrayList<ClientDriver>();

			final long start_ts = System.currentTimeMillis();
			
			BaseLineMixGenerator bl_mix = new BaseLineMixGenerator();

			for (int i = 1; i <= nclients; i++)
			{
				String client_name = String.format("Baseline Client #%s", i);
				ClientDriver client = new ClientDriver(client_name, latch, iterations, bl_mix);
				lst_clients.add(client);
				exec.execute(client);
			}

			latch.await();

			log.info("End Baseline Test");

			Report rpt = new Report(start_ts, lst_clients, bl_mix.transactionNames());
			
			rpt.run();
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}

	}

	private void doWarmUp(int nclients, int iterations)
	{
		log.info(String.format("Begin warmup, %s clients, %s iterations", nclients, iterations));

		try
		{
			CountDownLatch latch = new CountDownLatch(nclients);
			ExecutorService exec = Executors.newFixedThreadPool(nclients);

			for (int i = 1; i <= nclients; i++)
			{
				String client_name = String.format("Warmup Client #%s", i);
				ClientDriver client = new ClientDriver(client_name, latch, iterations, mix_gen);
				exec.execute(client);
			}

			latch.await();

			log.info("End warmup");
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}
}