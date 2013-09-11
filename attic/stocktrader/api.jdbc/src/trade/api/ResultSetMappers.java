package trade.api;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.caudexorigo.jdbc.ResultSetHandler;

import trade.domain.AccountData;
import trade.domain.AccountProfileData;
import trade.domain.HoldingData;
import trade.domain.OrderData;
import trade.domain.QuoteData;

public class ResultSetMappers
{
	public static final ResultSetHandler<AccountData> AccountDataHandler = new ResultSetHandler<AccountData>()
	{
		@Override
		public AccountData process(ResultSet rs)
		{
			try
			{
				AccountData obj = new AccountData();

				obj.setAccountId(rs.getInt("accountid"));
				obj.setBalance(rs.getBigDecimal("balance"));
				obj.setCreationDate(rs.getTimestamp("creationdate"));
				obj.setLastLogin(rs.getTimestamp("lastlogin"));
				obj.setLoginCount(rs.getInt("logincount"));
				obj.setLogoutCount(rs.getInt("logoutcount"));
				obj.setOpenBalance(rs.getBigDecimal("openbalance"));
				obj.setProfileId(rs.getString("profile_userid"));

				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static final ResultSetHandler<AccountProfileData> AccountProfileDataHandler = new ResultSetHandler<AccountProfileData>()
	{
		@Override
		public AccountProfileData process(ResultSet rs)
		{
			try
			{
				AccountProfileData obj = new AccountProfileData();

				obj.setAddress(rs.getString("address"));
				obj.setCreditCard(rs.getString("creditcard"));
				obj.setEmail(rs.getString("email"));
				obj.setFullName(rs.getString("fullname"));
				obj.setPassword(rs.getString("password"));
				obj.setSalt(rs.getString("salt"));
				obj.setUserId(rs.getString("userid"));

				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static final ResultSetHandler<OrderData> OrderDataHandler = new ResultSetHandler<OrderData>()
	{
		@Override
		public OrderData process(ResultSet rs)
		{
			try
			{
				OrderData obj = new OrderData();
				obj.setAccountId(rs.getInt("account_accountid"));
				obj.setHoldingId(rs.getInt("holding_holdingid"));
				obj.setOrderId(rs.getInt("orderid"));
				obj.setSymbol(rs.getString("quote_symbol"));
				obj.setOrderType(rs.getString("ordertype"));
				obj.setOrderStatus(rs.getString("orderstatus"));
				obj.setPrice(rs.getBigDecimal("price"));
				obj.setQuantity(rs.getDouble("quantity"));
				obj.setOpenDate(rs.getTimestamp("opendate"));
				obj.setCompletionDate(rs.getTimestamp("completiondate"));
				obj.setOrderFee(rs.getBigDecimal("price"));
				
				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static final ResultSetHandler<QuoteData> QuoteDataHandler = new ResultSetHandler<QuoteData>()
	{
		@Override
		public QuoteData process(ResultSet rs)
		{
			try
			{
				QuoteData obj = new QuoteData();

				obj.setChange(rs.getDouble("change1"));
				obj.setCompanyName(rs.getString("companyname"));
				obj.setHigh(rs.getBigDecimal("high"));
				obj.setLow(rs.getBigDecimal("low"));
				obj.setOpen(rs.getBigDecimal("open1"));
				obj.setPrice(rs.getBigDecimal("price"));
				obj.setSymbol(rs.getString("symbol"));
				obj.setVolume(rs.getDouble("volume"));

				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};

	public static final ResultSetHandler<HoldingData> HoldingDataHandler = new ResultSetHandler<HoldingData>()
	{
		@Override
		public HoldingData process(ResultSet rs)
		{
			try
			{
				HoldingData obj = new HoldingData();
				obj.setHoldingId(rs.getInt("holdingid"));
				obj.setPurchaseDate(rs.getTimestamp("purchasedate"));
				obj.setPurchasePrice(rs.getBigDecimal("purchaseprice"));
				obj.setQuantity(rs.getDouble("quantity"));
				obj.setQuoteId(rs.getString("quote_symbol"));

				return obj;
			}
			catch (SQLException e)
			{
				throw new RuntimeException(e);
			}
		}
	};
}