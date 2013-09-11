package trade.datagen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.caudexorigo.Shutdown;
import org.caudexorigo.cli.ArgumentValidationException;
import org.caudexorigo.cli.CliFactory;
import org.caudexorigo.text.RandomStringUtils;
import org.caudexorigo.text.StringUtils;

public class DataGenerator
{
	private final List<String> lst_first_name;
	private final List<String> lst_last_name;
	private final List<String> lst_place;
	private final List<String> lst_place_suffix;
	private final List<String> lst_term;
	private final List<String> lst_be;
	private final Random rnd;
	private final String output_path;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

	public DataGenerator(String input_path, String output_path)
	{
		this.output_path = output_path;

		lst_first_name = loadFromFile(input_path + File.separator + "first_name.txt");
		lst_last_name = loadFromFile(input_path + File.separator + "first_name.txt");
		lst_place = loadFromFile(input_path + File.separator + "place.txt");
		lst_place_suffix = loadFromFile(input_path + File.separator + "place_suffix.txt");
		lst_term = loadFromFile(input_path + File.separator + "term.txt");
		lst_be = loadFromFile(input_path + File.separator + "be.txt");
		rnd = new Random();
	}

	public GenAccount getNextGenAccount()
	{
		String fname = lst_first_name.get(rnd.nextInt(lst_first_name.size())); // random first name;
		String lname = lst_last_name.get(rnd.nextInt(lst_first_name.size()));// random last name;
		String domain = lst_term.get(rnd.nextInt(lst_term.size())) + ".com";// random domain name;

		return new GenAccount(fname, lname, domain);
	}

	public String getNextAddress()
	{
		String nr = RandomStringUtils.randomNumeric(3);
		String p = lst_place.get(rnd.nextInt(lst_place.size()));
		String s = lst_place_suffix.get(rnd.nextInt(lst_place_suffix.size()));
		String c = lst_place.get(rnd.nextInt(lst_place.size()));
		String st = RandomStringUtils.randomAlphabetic(2).toUpperCase();
		String z = RandomStringUtils.randomNumeric(5);
		return String.format("%s %s %s, %s, %s %s", nr, p, s, c, st, z);
	}

	public String getNextCompanyName()
	{
		String cname = StringUtils.capitalize(lst_term.get(rnd.nextInt(lst_term.size()))) + lst_term.get(rnd.nextInt(lst_term.size())) + " " + lst_be.get(rnd.nextInt(lst_be.size()));

		return cname;
	}

	private List<String> loadFromFile(String fileName)
	{
		try
		{
			List<String> lst = new ArrayList<String>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));

			String strLine;
			// Read File Line By Line
			while ((strLine = reader.readLine()) != null)
			{
				lst.add(strLine);
			}

			return lst;
		}
		catch (Throwable t)
		{
			throw new RuntimeException(t);
		}
	}

	public synchronized void generateAccountData(int start, int numAccounts, int numQuotes, int numberOfHoldings)
	{
		try
		{
			BufferedWriter acc_stream = new BufferedWriter(new FileWriter(output_path + File.separator + "account.txt", false));
			BufferedWriter profile_stream = new BufferedWriter(new FileWriter(output_path + File.separator + "account_profile.txt", false));
			BufferedWriter holding_stream = new BufferedWriter(new FileWriter(output_path + File.separator + "holding.txt", false));
			BufferedWriter order_stream = new BufferedWriter(new FileWriter(output_path + File.separator + "order.txt", false));

			String now = sdf.format(new Date());
			int quoteNumber = 1;
			int counterAccount = 0;
			int counterOrders = 0;

			int p0 = numAccounts / 10;
			int p1 = numAccounts / 5;

			for (int i = start; i <= numAccounts; i++)
			{
				String userId = "uid:" + i;
				if (i % p0 == 0)
				{
					System.out.printf("%s accounts generated%n", i);
				}

				GenAccount acc = getNextGenAccount();
				String password = "xxx";
				String salt = RandomStringUtils.randomAlphanumeric(20);
				String hashedpass = GenUtil.getHashedPassword(password, salt);

				String address = getNextAddress();
				String creditcard = RandomStringUtils.randomNumeric(16);
				BigDecimal balance = new BigDecimal("100000");

				acc_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", i, userId, balance, 200000, now, now, 1, 1));
				profile_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s%n", userId, acc.getEmail(), acc.getFullName(), hashedpass, salt, address, creditcard));

				for (int j = 1; j <= numberOfHoldings; j++)
				{
					counterOrders++;
					int holdingid = counterOrders;

					if (holdingid % p1 == 0)
					{
						System.out.printf("%s holdings generated%n", holdingid);
					}
					String symbol = "s:" + Integer.toString(quoteNumber);
					float buyQuantity = 200;
					BigDecimal orderfee = new BigDecimal("24.50");
					BigDecimal price = new BigDecimal("100");

					holding_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s%n", holdingid, i, symbol, price, buyQuantity, now));
					order_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", holdingid, i, symbol, holdingid, "buy", "completed", orderfee, price, buyQuantity, now, now));

					quoteNumber++;
					if (quoteNumber >= numQuotes)
						quoteNumber = 1;
				}
				counterAccount++;
			}

			close(acc_stream);
			close(profile_stream);
			close(holding_stream);
			close(order_stream);
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	public synchronized void generateQuoteData(int start, int numQuotes, boolean doOnce)
	{
		try
		{
			BufferedWriter quote_stream = new BufferedWriter(new FileWriter(output_path + File.separator + "quote.txt", false));

			int p1 = numQuotes / 5;

			for (int i = start; i <= numQuotes; i++)
			{
				if (i % p1 == 0)
				{
					System.out.printf("%s quotes generated%n", i);
				}

				String symbol = "s:" + i;
				String companyName = getNextCompanyName();
				BigDecimal price = new BigDecimal(RandomStringUtils.randomNumeric(3) + ".00");
				BigDecimal open1 = price;
				BigDecimal high = price;
				BigDecimal low = price;
				float change = (float) 0;
				float volume = (float) 0;

				quote_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", symbol, companyName, price, open1, volume, low, high, change));
			}

			if (doOnce)
			{
				BigDecimal priceacid = new BigDecimal("100.00");
				BigDecimal openacid = priceacid;
				BigDecimal highacid = priceacid;
				BigDecimal lowacid = priceacid;
				float changeacid = (float) 0;
				float volumeacid = (float) 0;
				String symbolacid = "ACIDBUY";
				String companyNameacid = "BUY THIS STOCK TO TEST  DISTRIBUTED TX. IT WILL ALWAYS TRIGGER AN EXCEPTION AT LAST STEP OF **BUY** OPERATION.";

				quote_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", symbolacid, companyNameacid, openacid, openacid, volumeacid, lowacid, highacid, changeacid));

				symbolacid = "ACIDSELL";
				companyNameacid = "BUY THIS STOCK TO TEST. IT WILL ALWAYS TRIGGER AN EXCEPTION AT LAST STEP OF **SELL** OPERATION.";

				quote_stream.append(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s%n", symbolacid, companyNameacid, openacid, openacid, volumeacid, lowacid, highacid, changeacid));
			}

			close(quote_stream);
		}
		catch (Throwable t)
		{
			Shutdown.now(t);
		}
	}

	private void close(Writer w) throws IOException
	{
		w.flush();
		w.close();
	}

	public static void main(String[] args)
	{
		GenCliArgs cargs = null;

		try
		{
			cargs = CliFactory.parseArguments(GenCliArgs.class, args);
		}
		catch (ArgumentValidationException ave)
		{
			System.err.printf("%s%n", ave.getMessage());
			Shutdown.now();
		}

		String input_path = cargs.getInputPath();
		String output_path = cargs.getOutputPath();

		validatePath(input_path);
		validatePath(output_path);

		String in_path = null;
		String out_path = null;
		try
		{
			in_path = (new File(input_path)).getCanonicalPath();
			out_path = (new File(output_path)).getCanonicalPath();
		}
		catch (IOException e)
		{
			Shutdown.now(e); // Should not happen.
		}

		DataGenerator n = new DataGenerator(in_path, out_path);

		int start = 1;
		int numAccounts = cargs.getNumberOfAccounts();
		int numQuotes = cargs.getNumberOfQuotes();
		int numberOfHoldings = cargs.getNumberOfHoldingsPerAccount();

		n.generateAccountData(start, numAccounts, numQuotes, numberOfHoldings);
		n.generateQuoteData(start, numQuotes, true);

		System.out.printf("Done! Generated files are in %s%n", out_path);
	}

	private static void validatePath(String path)
	{
		if (StringUtils.isBlank(path))
		{
			throw new IllegalArgumentException(String.format("Must provide a non null path", path));
		}

		File d = new File(path);

		if (!d.exists())
		{
			throw new IllegalArgumentException(String.format("Path '%s' doesn't exist", path));
		}

		if (!d.isDirectory())
		{
			throw new IllegalArgumentException(String.format("Path '%s' is not a directory", path));
		}
	}
}