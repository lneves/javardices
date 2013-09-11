package pt.pond.logger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.caudexorigo.concurrent.CustomExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;

public class PondLogger
{
	public static final String APP_NAME = PondLogger.class.getCanonicalName() + ".v1";
	private static final Logger log = LoggerFactory.getLogger(PondLogger.class);
	private static final ScheduledExecutorService sched_exec = CustomExecutors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(), "SchedExec");

	public static void main(String[] args) throws Throwable
	{
		String host = AppConf.getString("broker.host");
		int port = Integer.parseInt(AppConf.getString("broker.port"));
		String broker_destination = AppConf.getString("broker.destination");
		String broker_destination_type = AppConf.getString("broker.destination-type");
		String solr_url = AppConf.getString("solr.url");

		int commit_delay = Integer.parseInt(AppConf.getString("commit.delay"));

		PondLoggerWorker log_consumer = new PondLoggerWorker(new BrokerClient(host, port, "tcp://pondapp.com/logger0"), broker_destination, broker_destination_type, solr_url);
		log_consumer.start();
		sched_exec.scheduleWithFixedDelay(log_consumer, commit_delay, commit_delay, TimeUnit.MINUTES);
		log.info("{} started.", PondLogger.class.getCanonicalName());

	}

}
