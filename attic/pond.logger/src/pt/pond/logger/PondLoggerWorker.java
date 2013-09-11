package pt.pond.logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.caudexorigo.Shutdown;
import org.caudexorigo.concurrent.CustomExecutors;
import org.caudexorigo.concurrent.Sleep;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.com.broker.client.BrokerClient;
import pt.com.broker.client.messaging.BrokerListener;
import pt.com.broker.types.NetAction.DestinationType;
import pt.com.broker.types.NetNotification;
import pt.com.broker.types.NetSubscribe;

public class PondLoggerWorker implements BrokerListener, Runnable
{
	private static final ExecutorService exec = CustomExecutors.newThreadPool(8, "solr-executor");

	private static final Logger log = LoggerFactory.getLogger(PondLoggerWorker.class);
	private final BrokerClient bk;

	private CommonsHttpSolrServer solrServer;
	private final String destination;
	private final DestinationType dtype;

	private long counter = 0;

	public PondLoggerWorker(BrokerClient brokerClient, String destination, String destinationType, String solrUrl)
	{
		this.bk = brokerClient;
		this.destination = destination;
		this.dtype = DestinationType.valueOf(destinationType);
		try
		{
			solrServer = new CommonsHttpSolrServer(new URL(solrUrl));
			solrServer.setRequestWriter(new BinaryRequestWriter());
		}
		catch (MalformedURLException e)
		{
			Shutdown.now(e);
		}
	}

	public void start()
	{
		NetSubscribe subscribe = new NetSubscribe(destination, dtype);
		try
		{
			bk.addAsyncConsumer(subscribe, this);
		}
		catch (Throwable e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public void commit()
	{
		try
		{
			log.info("Running Solr Commit");

			counter++;

			log.info("Unsubscribe from broker destination: '{}'", destination);
			bk.unsubscribe(dtype, destination);

			Sleep.time(1000);

			log.info("Send delete query to solr: '{}'", "Timestamp:[* TO NOW/DAY-7DAY]");
			solrServer.deleteByQuery("Timestamp:[* TO NOW/DAY-7DAY]");

			log.info("Send delete query to solr: '{}'", "Host:pond-conn* AND Timestamp:[* TO NOW/DAY-2DAY]");
			solrServer.deleteByQuery("Host:pond-conn* AND Timestamp:[* TO NOW/DAY-2DAY]");

			if ((counter % 50) == 0)
			{
				log.info("Send 'optimize' command to solr");
				solrServer.optimize();
			}
			else
			{
				log.info("Send 'commit' command to solr");
				solrServer.commit();
			}

			start();
		}
		catch (Throwable t)
		{
			Alert.publishError(bk, PondLogger.APP_NAME, t);
		}
	}

	@Override
	public void run()
	{
		commit();
	}

	@Override
	public boolean isAutoAck()
	{
		return true;
	}

	@Override
	public void onMessage(final NetNotification notification)
	{
		try
		{
			final Runnable solr_action = new Runnable()
			{
				public void run()
				{
					try
					{
						JSONObject j = new JSONObject(new String(notification.getMessage().getPayload()));

						SolrInputDocument sdoc = new SolrInputDocument();
						sdoc.addField("Level", j.optString("level"));
						sdoc.addField("Timestamp", new Date(j.getLong("timestamp")));
						sdoc.addField("Stacktrace", j.optString("stacktrace"));
						sdoc.addField("Host", j.optString("host"));
						sdoc.addField("Thread", j.optString("thread"));
						sdoc.addField("Message", j.optString("message"));
						sdoc.addField("Class", j.optString("class"));

						solrServer.add(sdoc);
					}
					catch (Throwable e)
					{
						log.error(e.getMessage(), e);
					}
				}
			};

			exec.execute(solr_action);
		}
		catch (Throwable e)
		{
			log.error(e.getMessage(), e);
		}
	}
}