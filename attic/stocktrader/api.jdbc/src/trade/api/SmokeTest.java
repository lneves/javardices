package trade.api;

import java.math.BigDecimal;

import org.caudexorigo.Shutdown;

import trade.api.Trade;
import trade.domain.AccountProfileData;

public class SmokeTest
{
	public static void main(String[] args)
	{
		try
		{
			Trade t = new TradeDirect();

			System.out.println("\n-----------------  getOrders ---------------------------");
			System.out.println(t.getOrders("uid:1"));

			System.out.println("\n-----------------  getClosedOrders ---------------------");
			System.out.println(t.getClosedOrders("uid:1"));

			System.out.println("\n-----------------  getQuote ----------------------------");
			System.out.println(t.getQuote("s:1"));

			System.out.println("\n-----------------  getAccountData ----------------------");
			System.out.println(t.getAccountData("uid:1"));

			System.out.println("\n-----------------  getAccountProfileData ---------------");
			System.out.println(t.getAccountProfileData("uid:1"));

			System.out.println("\n-----------------  getHolding --------------------------");
			System.out.println(t.getHolding(1));

			System.out.println("\n-----------------  getHoldings -------------------------");
			System.out.println(t.getHoldings("uid:1"));

			System.out.println("\n-----------------  getMarketSummary --------------------");
			System.out.println(t.getMarketSummary());

			System.out.println("\n-----------------  login -------------------------------");
			System.out.println(t.login("uid:1", "xxx"));

			System.out.println("\n-----------------  logout ------------------------------");
			t.logout("uid:1");
			System.out.println(t.getAccountData("uid:1"));

			String uid = "u:x-" + System.currentTimeMillis();
			System.out.println("\n-----------------  register -> uid: " + uid + " --");

			System.out.println(t.register(uid, "yyy", "Bugs Bunny", "Loney Avenue, 111", "bugs@acme.com", "1234567890", new BigDecimal("54321.98")));

			AccountProfileData accp = t.getAccountProfileData(uid);
			System.out.println(accp);

			System.out.println("\n-----------------  updateAccountProfile ----------------");
			accp.setEmail("bugs.bunny@acme.com");
			System.out.println(t.updateAccountProfile(accp));
			System.out.println(t.getAccountProfileData(uid));

			System.out.println("\n-----------------  sell --------------------------------");
			System.out.println(t.sell("uid:1", 1, 0));

			System.out.println("\n-----------------  buy ---------------------------------");
			System.out.println(t.buy("uid:1", "s:1", 1, 0));

		}
		catch (Throwable e)
		{
			Shutdown.now(e);
		}
	}
}