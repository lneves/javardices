package db.tpce.driver;

import java.util.List;

import org.caudexorigo.Shutdown;
import org.caudexorigo.cli.ArgumentValidationException;
import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.text.StringUtils;
import org.slf4j.LoggerFactory;

import db.bench.CliArgs;
import db.bench.GenericLoadDriver;
import db.bench.Profile;
import db.bench.TxMixGenerator;
import db.tpce.gen.BrokerSelection;
import db.tpce.gen.CompanySelection;
import db.tpce.gen.CustomerIdSelection;
import db.tpce.gen.DailyMarketSelection;
import db.tpce.gen.IndustrySelection;
import db.tpce.gen.MaxTradeIdSelection;

public class LoadDriver
{
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(LoadDriver.class);

	public static void main(String[] args)
	{
		CliArgs cargs = null;
		try
		{
			cargs = CliFactory.parseArguments(CliArgs.class, args);
		}
		catch (ArgumentValidationException e)
		{
			System.err.println(e.getMessage());
			Shutdown.now();
		}

		int nclients = Math.max(0, cargs.getClients());
		int iterations = Math.max(0, cargs.getIterations());
		Profile profile = cargs.getProfile();
		String log_level = cargs.getLogLevel();
		int wu_iter = cargs.getWarmUpIterations();
		int bl_iter = cargs.getBaseLineIterations();
		List<String> ex_list = cargs.getExcludeList();

		System.setProperty("org.caudexorigo.jdbc.max.pool.size", Integer.toString(nclients));

		try
		{
			if (bl_iter == 0)
			{
				log.info("Initializing data base connection and populating lookup values");
				BrokerSelection.getRandom();
				CompanySelection.getRandom();
				CustomerIdSelection.getRandom();
				DailyMarketSelection.getRandom();
				IndustrySelection.getRandom();
				MaxTradeIdSelection.get();
			}
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}

		TxMixGenerator mix_gen = new TpceMixGenerator(profile, cargs.getExcludeList());
		GenericLoadDriver gen_driver = new GenericLoadDriver(nclients, iterations, log_level, wu_iter, bl_iter, mix_gen);
		
		
		if ((ex_list != null))
		{
			StringBuilder sb = new StringBuilder();

			for (String ex_test : ex_list)
			{
				if (StringUtils.isNotBlank(ex_test))
				{
					sb.append(" ");
					sb.append(ex_test);
				}
			}

			if (sb.length() > 0)
			{
				System.out.printf("Excluded tests:%s%n", sb.toString());
			}
		}

		gen_driver.run();

		Shutdown.now();
	}
}