package trade.api;

import java.math.BigDecimal;
import java.util.List;

import trade.domain.AccountData;
import trade.domain.AccountProfileData;
import trade.domain.HoldingData;
import trade.domain.MarketSummaryData;
import trade.domain.OrderData;
import trade.domain.QuoteData;

public interface Trade
{
	/**
	 * Purchase a stock and create a new holding for the given user. Given a stock symbol and quantity to purchase, retrieve the current quote price, debit the user's account
	 * balance, and add holdings to user's portfolio. buy/sell are asynchronous, using J2EE messaging, A new order is created and submitted for processing to the TradeBroker
	 * 
	 * @param userId
	 *            the customer requesting the stock purchase
	 * @param symbol
	 *            the symbol of the stock being purchased
	 * @param quantity
	 *            the quantity of shares to purchase
	 * @return OrderData providing the status of the newly created buy order
	 */

	public OrderData buy(String userId, String symbol, double quantity, int orderProcessingMode);

	/**
	 * Return an AccountData object for userId describing the account
	 * 
	 * @param userId
	 *            the account userId to lookup
	 * @return User account data in AccountData
	 */
	public AccountData getAccountData(String userId);

	/**
	 * Return an AccountProfileData for userId providing the users profile
	 * 
	 * @param userId
	 *            the account userId to lookup
	 * @param User
	 *            account profile data in AccountProfileData
	 */
	public AccountProfileData getAccountProfileData(String userId);

	/**
	 * Get the collection of completed orders for a given account that need to be alerted to the user
	 * 
	 * @param userId
	 *            the customer account to retrieve orders for
	 * @return Collection OrderDatas providing detailed order information
	 */
	public List<OrderData> getClosedOrders(String userId);

	/**
	 * Return a specific user stock holding identifed by the holdingId
	 * 
	 * @param holdingId
	 *            the holdingId to return
	 * @return a HoldingData describing the holding
	 */
	public HoldingData getHolding(int holdingId);

	/**
	 * Return the portfolio of stock holdings for the specified customer as a collection of HoldingDatas
	 * 
	 * @param userId
	 *            the customer requesting the portfolio
	 * @return Collection of the users portfolio of stock holdings
	 */
	public List<HoldingData> getHoldings(String userId);

	/**
	 * Compute and return a snapshot of the current market conditions This includes the TSIA - an index of the price of the top 100 Trade stock quotes The openTSIA ( the index at
	 * the open) The volume of shares traded, Top Stocks gain and loss
	 * 
	 * @return A snapshot of the current market summary
	 */
	public MarketSummaryData getMarketSummary();

	/**
	 * Get the collection of all orders for a given account
	 * 
	 * @param userId
	 *            the customer account to retrieve orders for
	 * @return Collection OrderDatas providing detailed order information
	 */
	public List<OrderData> getOrders(String userId);

	/**
	 * Return a {@link QuoteData} describing a current quote for the given stock symbol
	 * 
	 * @param symbol
	 *            the stock symbol to retrieve the current Quote
	 * @return the QuoteData
	 */
	public QuoteData getQuote(String symbol);

	/**
	 * Attempt to authenticate and login a user with the given password
	 * 
	 * @param userId
	 *            the customer to login
	 * @param password
	 *            the password entered by the customer for authentication
	 * @return User account data in AccountData
	 */
	public AccountData login(String userId, String password);

	/**
	 * Logout the given user
	 * 
	 * @param userId
	 *            the customer to logout
	 * @return the login status
	 */

	public void logout(String userId);

	/**
	 * Register a new Trade customer. Create a new user profile, user registry entry, account with initial balance, and empty portfolio.
	 * 
	 * @param userId
	 *            the new customer to register
	 * @param password
	 *            the customers password
	 * @param fullname
	 *            the customers fullname
	 * @param address
	 *            the customers street address
	 * @param email
	 *            the customers email address
	 * @param creditcard
	 *            the customers creditcard number
	 * @param initialBalance
	 *            the amount to charge to the customers credit to open the account and set the initial balance
	 * @return the userId if successful, null otherwise
	 */
	public AccountData register(String userId, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance);

	/**
	 * Sell a stock holding and remove the holding for the given user. Given a Holding, retrieve current quote, credit user's account, and reduce holdings in user's portfolio.
	 * 
	 * @param userId
	 *            the customer requesting the sell
	 * @param holdingId
	 *            the users holding to be sold
	 * @return OrderData providing the status of the newly created sell order
	 */
	public OrderData sell(String userId, int holdingId, int orderProcessingMode);

	public void submitOrder(OrderData order);

	/**
	 * Update userId's account profile information using the provided AccountProfileData object
	 * 
	 * @param userId
	 *            the account userId to lookup
	 * @param User
	 *            account profile data in AccountProfileData
	 */
	public AccountProfileData updateAccountProfile(AccountProfileData profileData);

	public QuoteData updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded);
}