package trade.api;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.caudexorigo.io.IOUtils;
import org.caudexorigo.jdbc.Db;
import org.caudexorigo.jdbc.DbFetcher;
import org.caudexorigo.jdbc.DbPool;
import org.caudexorigo.jdbc.DbRunner;
import org.caudexorigo.jdbc.DbType;
import org.caudexorigo.jdbc.ScalarBigDecimalHandler;
import org.caudexorigo.jdbc.ScalarDoubleHandler;
import org.caudexorigo.jdbc.ScalarIntegerHandler;
import org.caudexorigo.text.RandomStringUtils;
import org.caudexorigo.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trade.domain.AccountData;
import trade.domain.AccountProfileData;
import trade.domain.HoldingData;
import trade.domain.MarketSummaryData;
import trade.domain.OrderData;
import trade.domain.QuoteData;

public class TradeDirect implements Trade
{
	private static final Set<String> COMPLETED_STATUS = new HashSet<String>();

	private static final String DELETE_HOLDING_BY_ID = load("DELETE_HOLDING_BY_ID.sql");
	private static final String EMPTY_ARGUMENT = "Argument must not be empty";
	private static final String FETCH_ACCOUNT_BY_USERID = load("FETCH_ACCOUNT_BY_USERID.sql");
	private static final String FETCH_ACCOUNTPROFILE_BY_USERID = load("FETCH_ACCOUNTPROFILE_BY_USERID.sql");
	private static final String FETCH_CLOSED_ORDERS_BY_USERID = load("FETCH_CLOSED_ORDERS_BY_USERID.sql");
	private static final String FETCH_HOLDING_BY_ID = load("FETCH_HOLDING_BY_ID.sql");
	private static final String FETCH_HOLDINGS_BY_USERID = load("FETCH_HOLDINGS_BY_USERID.sql");
	private static final String FETCH_MARKETSUMMARY_GAINERS = load("FETCH_MARKETSUMMARY_GAINERS.sql");
	private static final String FETCH_MARKETSUMMARY_LOSERS = load("FETCH_MARKETSUMMARY_LOSERS.sql");
	private static final String FETCH_MARKETSUMMARY_OPENTSIA = load("FETCH_MARKETSUMMARY_OPENTSIA.sql");
	private static final String FETCH_MARKETSUMMARY_TSIA = load("FETCH_MARKETSUMMARY_TSIA.sql");
	private static final String FETCH_MARKETSUMMARY_VOLUME = load("FETCH_MARKETSUMMARY_VOLUME.sql");
	private static final String FETCH_ORDER_BY_ORDERID = load("FETCH_ORDER_BY_ORDERID.sql");
	private static final String FETCH_ORDERS_BY_USERID = load("FETCH_ORDERS_BY_USERID.sql");
	private static final String FETCH_QUOTE_BY_SYMBOL = load("FETCH_QUOTE_BY_SYMBOL.sql");
	private static final String FETCH_QUOTE_FOR_UPDATE;
	private static final String INSERT_ACCOUNT;
	private static final String INSERT_ACCOUNTPROFILE = load("INSERT_ACCOUNTPROFILE.sql");
	private static final String INSERT_HOLDING;
	private static final String INSERT_ORDER;
	private static final Logger log = LoggerFactory.getLogger(TradeDirect.class);
	private static boolean publishQuotePriceChange = false;
	private static final String UPDATE_ACCOUNT_BALANCE = load("UPDATE_ACCOUNT_BALANCE.sql");
	private static final String UPDATE_ACCOUNT_LOGIN = load("UPDATE_ACCOUNT_LOGIN.sql");
	private static final String UPDATE_ACCOUNT_LOGOUT = load("UPDATE_ACCOUNT_LOGOUT.sql");
	private static final String UPDATE_ACCOUNTPROFILE = load("UPDATE_ACCOUNTPROFILE.sql");
	private static final String UPDATE_HOLDINGSTATUS = load("UPDATE_HOLDINGSTATUS.sql");

	private static final String UPDATE_ORDER_STATUS = load("UPDATE_ORDER_STATUS.sql");
	private static final String UPDATE_QUOTE_PRICE_VOLUME = load("UPDATE_QUOTE_PRICE_VOLUME.sql");

	static
	{
		if (DbPool.getDbType() == DbType.MSSQL)
		{
			INSERT_ACCOUNT = load("MSSQL_INSERT_ACCOUNT.sql");
			INSERT_HOLDING = load("MSSQL_INSERT_HOLDING.sql");
			INSERT_ORDER = load("MSSQL_INSERT_ORDER.sql");
			FETCH_QUOTE_FOR_UPDATE = load("MSSQL_FETCH_QUOTE_FOR_UPDATE.sql");
		}
		else if (DbPool.getDbType() == DbType.PGSQL)
		{
			INSERT_ACCOUNT = load("PGSQL_INSERT_ACCOUNT.sql");
			INSERT_HOLDING = load("PGSQL_INSERT_HOLDING.sql");
			INSERT_ORDER = load("PGSQL_INSERT_ORDER.sql");
			FETCH_QUOTE_FOR_UPDATE = load("PGSQL_FETCH_QUOTE_FOR_UPDATE.sql");
		}
		else
		{
			INSERT_ACCOUNT = "";
			INSERT_HOLDING = "";
			INSERT_ORDER = "";
			FETCH_QUOTE_FOR_UPDATE = "";
		}

		COMPLETED_STATUS.add("completed");
		COMPLETED_STATUS.add("alertcompleted");
		COMPLETED_STATUS.add("cancelled");
	}

	private static String load(String sql_file)
	{
		try
		{
			InputStream in = TradeDirect.class.getResourceAsStream("/trade/api/dml/" + sql_file);

			if (in == null)
			{
				throw new RuntimeException("Invalid file or path");
			}
			else
			{
				return IOUtils.toString(in);
			}
		}
		catch (Throwable t)
		{
			String message = String.format("Could not load file '%s'. Reason: %s", sql_file, t.getMessage());
			throw new RuntimeException(message);
		}
	}

	@Override
	public OrderData buy(String userId, String symbol, double quantity, int orderProcessingMode)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		OrderData orderData = doBuy(userId, symbol, quantity);
		updateQuotePriceVolume(orderData.getSymbol(), TradeConfig.getRandomPriceChangeFactor(), orderData.getQuantity());
		return orderData;
	}

	@Override
	public AccountData getAccountData(String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbFetcher<AccountData> db = new DbFetcher<AccountData>();
		return db.fetchObject(FETCH_ACCOUNT_BY_USERID, ResultSetMappers.AccountDataHandler, userId);
	}

	@Override
	public AccountProfileData getAccountProfileData(String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbFetcher<AccountProfileData> db = new DbFetcher<AccountProfileData>();
		return db.fetchObject(FETCH_ACCOUNTPROFILE_BY_USERID, ResultSetMappers.AccountProfileDataHandler, userId);
	}

	@Override
	public List<OrderData> getClosedOrders(String userId)
	{
		DbFetcher<OrderData> db = new DbFetcher<OrderData>();
		return db.fetchList(FETCH_CLOSED_ORDERS_BY_USERID, ResultSetMappers.OrderDataHandler, userId);
	}

	@Override
	public HoldingData getHolding(int holdingId)
	{
		DbFetcher<HoldingData> db = new DbFetcher<HoldingData>();
		return db.fetchObject(FETCH_HOLDING_BY_ID, ResultSetMappers.HoldingDataHandler, holdingId);
	}

	@Override
	public List<HoldingData> getHoldings(String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbFetcher<HoldingData> db = new DbFetcher<HoldingData>();
		return db.fetchList(FETCH_HOLDINGS_BY_USERID, ResultSetMappers.HoldingDataHandler, userId);
	}

	@Override
	public MarketSummaryData getMarketSummary()
	{
		DbFetcher<QuoteData> db_qd = new DbFetcher<QuoteData>();
		DbFetcher<BigDecimal> db_bd = new DbFetcher<BigDecimal>();
		DbFetcher<Double> db_d = new DbFetcher<Double>();

		MarketSummaryData msd = new MarketSummaryData();
		msd.setOpenTSIA(db_bd.fetchObject(FETCH_MARKETSUMMARY_OPENTSIA, new ScalarBigDecimalHandler(1)));
		msd.setTSIA(db_bd.fetchObject(FETCH_MARKETSUMMARY_TSIA, new ScalarBigDecimalHandler(1)));
		msd.setTopGainers(db_qd.fetchList(FETCH_MARKETSUMMARY_GAINERS, ResultSetMappers.QuoteDataHandler));
		msd.setTopLosers(db_qd.fetchList(FETCH_MARKETSUMMARY_LOSERS, ResultSetMappers.QuoteDataHandler));
		msd.setVolume(db_d.fetchObject(FETCH_MARKETSUMMARY_VOLUME, new ScalarDoubleHandler(1)));
		msd.setSummaryDate(new Date());

		return msd;
	}

	@Override
	public List<OrderData> getOrders(String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbFetcher<OrderData> db = new DbFetcher<OrderData>();
		return db.fetchList(FETCH_ORDERS_BY_USERID, ResultSetMappers.OrderDataHandler, userId);
	}

	@Override
	public QuoteData getQuote(String symbol)
	{
		if (StringUtils.isBlank(symbol))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbFetcher<QuoteData> db = new DbFetcher<QuoteData>();
		return db.fetchObject(FETCH_QUOTE_BY_SYMBOL, ResultSetMappers.QuoteDataHandler, symbol);
	}

	@Override
	public AccountData login(String userId, String password)
	{
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(password))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		AccountProfileData profile = getAccountProfileData(userId);
		String pwd_hash = HashUtil.getHashedPassword(password, profile.getSalt());

		if (profile.getPassword().equals(pwd_hash))
		{
			AccountData acc = getAccountData(userId);
			DbRunner dbrunner = new DbRunner();
			dbrunner.executePreparedStatement(UPDATE_ACCOUNT_LOGIN, userId);

			return acc;
		}

		return null;
	}

	@Override
	public void logout(String userId)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}
		DbRunner dbrunner = new DbRunner();
		dbrunner.executePreparedStatement(UPDATE_ACCOUNT_LOGOUT, userId);
	}

	@Override
	public AccountData register(String userId, String password, String fullname, String address, String email, String creditcard, BigDecimal openBalance)
	{
		if (StringUtils.isBlank(userId) || StringUtils.isBlank(password))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		Db db = null;
		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			DbFetcher<Integer> db_acc = new DbFetcher<Integer>();
			DbRunner dbrunner = new DbRunner();

			String salt = RandomStringUtils.randomAlphanumeric(20);
			String passwd = HashUtil.getHashedPassword(password, salt);

			dbrunner.executePreparedStatement(db, INSERT_ACCOUNTPROFILE, userId, email, fullname, passwd, salt, address, creditcard);

			int acc_id = db_acc.fetchObject(db, INSERT_ACCOUNT, new ScalarIntegerHandler(1), userId, openBalance, openBalance);

			db.commitTransaction();

			AccountData obj = new AccountData();
			Date now = new Date();
			obj.setAccountId(acc_id);
			obj.setBalance(openBalance);
			obj.setCreationDate(now);
			obj.setLastLogin(now);
			obj.setLoginCount(1);
			obj.setLogoutCount(1);
			obj.setOpenBalance(openBalance);
			obj.setProfileId(userId);

			return obj;
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	@Override
	public OrderData sell(String userId, int holdingId, int orderProcessingMode)
	{
		if (StringUtils.isBlank(userId))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		OrderData orderData = null;
		BigDecimal total;
		Db db = null;

		DbFetcher<AccountData> db_acc = new DbFetcher<AccountData>();
		DbFetcher<HoldingData> db_holding = new DbFetcher<HoldingData>();
		DbFetcher<QuoteData> db_quote = new DbFetcher<QuoteData>();
		DbRunner dbrunner = new DbRunner();

		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			AccountData accountData = db_acc.fetchObject(db, FETCH_ACCOUNT_BY_USERID, ResultSetMappers.AccountDataHandler, userId);
			HoldingData holdingData = db_holding.fetchObject(db, FETCH_HOLDING_BY_ID, ResultSetMappers.HoldingDataHandler, holdingId);
			QuoteData quoteData = null;

			if (holdingData != null)
			{
				quoteData = db_quote.fetchObject(db, FETCH_QUOTE_BY_SYMBOL, ResultSetMappers.QuoteDataHandler, holdingData.getQuoteId());
			}

			if ((accountData == null) || (holdingData == null) || (quoteData == null))
			{
				throw new IllegalArgumentException("Unable to find account, holding, quote or user records");
			}

			double quantity = holdingData.getQuantity();
			orderData = createOrder(db, accountData, quoteData, holdingData.getHoldingId(), "sell", quantity);
			dbrunner.executePreparedStatement(db, UPDATE_HOLDINGSTATUS, new Date(), holdingData.getHoldingId()); // updateHoldingStatus
			BigDecimal price = quoteData.getPrice();
			BigDecimal orderFee = orderData.getOrderFee();
			total = (new BigDecimal(quantity).multiply(price)).subtract(orderFee);
			creditAccountBalance(db, accountData, total);

			orderData = completeOrder(db, orderData.getOrderId());
			checkAcidOrder(orderData);

			db.commitTransaction();
			return orderData;
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	@Override
	public void submitOrder(OrderData order)
	{
		if (order == null)
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		Db db = null;
		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			completeOrder(db, order.getOrderId());
			checkAcidOrder(order);

			db.commitTransaction();
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	@Override
	public AccountProfileData updateAccountProfile(AccountProfileData accP)
	{
		if (accP == null)
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		if (StringUtils.isBlank(accP.getUserId()) || StringUtils.isBlank(accP.getPassword()))
		{
			throw new IllegalArgumentException(EMPTY_ARGUMENT);
		}

		DbRunner dbrunner = new DbRunner();

		String salt = RandomStringUtils.randomAlphanumeric(20);
		String pwd_hash = HashUtil.getHashedPassword(accP.getPassword(), salt);

		dbrunner.executePreparedStatement(UPDATE_ACCOUNTPROFILE, accP.getEmail(), accP.getFullName(), pwd_hash, salt, accP.getAddress(), accP.getCreditCard(), accP.getUserId());

		accP.setSalt(salt);
		accP.setPassword(pwd_hash);
		return accP;
	}

	@Override
	public QuoteData updateQuotePriceVolume(String symbol, BigDecimal changeFactor, double sharesTraded)
	{
		return updateQuotePriceVolumeInt(symbol, changeFactor, sharesTraded, publishQuotePriceChange);
	}

	private void checkAcidOrder(OrderData order)
	{
		if ("buy".equals(order.getOrderType()) && "ACIDBUY".equals(order.getSymbol()))
		{
			throw new RuntimeException("ACID BUY EXCEPTION THROWN!");
		}
		else if ("sell".equals(order.getOrderType()) && "ACIDSELL".equals(order.getSymbol()))
		{
			throw new RuntimeException("ACID SELL EXCEPTION THROWN!");
		}
	}

	private OrderData completeOrder(Db db, int orderId)
	{
		DbFetcher<OrderData> db_order = new DbFetcher<OrderData>();
		DbRunner dbrunner = new DbRunner();
		OrderData o = db_order.fetchObject(db, FETCH_ORDER_BY_ORDERID, ResultSetMappers.OrderDataHandler, orderId);

		if (o == null)
		{
			log.error("Unable to find order for orderid: '{}'", orderId);
			return o;
		}

		String orderType = o.getOrderType();
		String orderStatus = o.getOrderStatus();

		if (COMPLETED_STATUS.contains(orderStatus))
		{
			throw new IllegalStateException("Attempt to complete Order that is already completed");
		}

		int holdingId = o.getHoldingId();

		if ("sell".equalsIgnoreCase(orderType))
		{
			dbrunner.executePreparedStatement(db, DELETE_HOLDING_BY_ID, holdingId);
		}
		o.setCompletionDate(new Date());
		dbrunner.executePreparedStatement(db, UPDATE_ORDER_STATUS, "closed", o.getCompletionDate(), orderId);

		return o;
	}

	private OrderData createOrder(Db db, AccountData accountData, QuoteData quoteData, int holdingid, String ordertype, double quantity)
	{
		DbFetcher<Integer> db_order = new DbFetcher<Integer>();

		int acc_id = accountData.getAccountId();

		BigDecimal p = quoteData.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal f = new BigDecimal(12);
		Date now = new Date();
		int orderId = db_order.fetchObject(db, INSERT_ORDER, new ScalarIntegerHandler(1), acc_id, quoteData.getSymbol(), holdingid, ordertype, "open", f, p, quantity, now);

		OrderData obj = new OrderData();
		obj.setAccountId(acc_id);
		obj.setHoldingId(holdingid);
		obj.setOrderId(orderId);
		obj.setSymbol(quoteData.getSymbol());
		obj.setOrderType(ordertype);
		obj.setOrderStatus("open");
		obj.setPrice(p);
		obj.setQuantity(quantity);
		obj.setOpenDate(now);
		obj.setOrderFee(f);

		return obj;
	}

	private void creditAccountBalance(Db db, AccountData accountData, BigDecimal value)
	{
		DbRunner dbrunner = new DbRunner();
		dbrunner.executePreparedStatement(db, UPDATE_ACCOUNT_BALANCE, value, accountData.getAccountId());
	}

	private OrderData doBuy(String userId, String symbol, double quantity)
	{
		BigDecimal total;
		Db db = null;
		DbFetcher<AccountData> db_acc = new DbFetcher<AccountData>();
		DbFetcher<QuoteData> db_quote = new DbFetcher<QuoteData>();
		DbFetcher<Integer> db_int = new DbFetcher<Integer>();

		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			AccountData accountData = db_acc.fetchObject(db, FETCH_ACCOUNT_BY_USERID, ResultSetMappers.AccountDataHandler, userId);
			QuoteData quoteData = db_quote.fetchObject(db, FETCH_QUOTE_BY_SYMBOL, ResultSetMappers.QuoteDataHandler, symbol);

			if ((accountData == null) || (quoteData == null))
			{
				throw new IllegalArgumentException("Unable to find account or quote records");
			}

			BigDecimal price = quoteData.getPrice();

			int hid = db_int.fetchObject(db, INSERT_HOLDING, new ScalarIntegerHandler(1), accountData.getAccountId(), symbol, price, quantity);

			OrderData orderData = createOrder(db, accountData, quoteData, hid, "buy", quantity);

			BigDecimal orderFee = orderData.getOrderFee();
			total = (new BigDecimal(quantity).multiply(price)).add(orderFee);
			creditAccountBalance(db, accountData, total.negate());
			orderData = completeOrder(db, orderData.getOrderId());
			checkAcidOrder(orderData);

			db.commitTransaction();

			return orderData;
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}

	private QuoteData updateQuotePriceVolumeInt(String symbol, BigDecimal changeFactor, double sharesTraded, boolean publishQuotePriceChange)
	{
		Db db = null;
		DbFetcher<QuoteData> db_quote = new DbFetcher<QuoteData>();
		DbRunner dbrunner = new DbRunner();

		try
		{
			db = DbPool.obtain();
			db.beginTransaction();

			QuoteData quoteData = db_quote.fetchObject(db, FETCH_QUOTE_FOR_UPDATE, ResultSetMappers.QuoteDataHandler, symbol);

			BigDecimal oldPrice = quoteData.getPrice();
			double newVolume = quoteData.getVolume() + sharesTraded;

			if (oldPrice.equals(TradeConfig.PENNY_STOCK_PRICE))
			{
				changeFactor = TradeConfig.PENNY_STOCK_RECOVERY_MIRACLE_MULTIPLIER;
			}

			BigDecimal newPrice = changeFactor.multiply(oldPrice).setScale(2, BigDecimal.ROUND_HALF_UP);

			dbrunner.executePreparedStatement(db, UPDATE_QUOTE_PRICE_VOLUME, newPrice, newPrice, newVolume, quoteData.getSymbol());
			quoteData = db_quote.fetchObject(db, FETCH_QUOTE_BY_SYMBOL, ResultSetMappers.QuoteDataHandler, symbol);

			db.commitTransaction();

			return quoteData;
		}
		catch (Throwable ex)
		{
			db.rollbackTransaction();
			throw new RuntimeException(ex);
		}
		finally
		{
			DbPool.release(db);
		}
	}
}