package trade.srv;

import java.util.Collections;
import java.util.List;

import trade.api.Trade;
import trade.api.TradeDirect;
import trade.domain.QuoteData;
import trade.ws.Buy;
import trade.ws.BuyResponse;
import trade.ws.CancelOrderResponse;
import trade.ws.CompleteOrderResponse;
import trade.ws.CreateQuoteResponse;
import trade.ws.GetAccountData;
import trade.ws.GetAccountDataResponse;
import trade.ws.GetAccountProfileData;
import trade.ws.GetAccountProfileDataResponse;
import trade.ws.GetAllQuotesResponse;
import trade.ws.GetClosedOrders;
import trade.ws.GetClosedOrdersResponse;
import trade.ws.GetHolding;
import trade.ws.GetHoldingResponse;
import trade.ws.GetHoldings;
import trade.ws.GetHoldingsResponse;
import trade.ws.GetMarketSummaryResponse;
import trade.ws.GetOrders;
import trade.ws.GetOrdersResponse;
import trade.ws.GetQuote;
import trade.ws.GetQuoteResponse;
import trade.ws.Login;
import trade.ws.LoginResponse;
import trade.ws.Logout;
import trade.ws.LogoutResponse;
import trade.ws.OrderCompletedResponse;
import trade.ws.QueueOrderResponse;
import trade.ws.Register;
import trade.ws.RegisterResponse;
import trade.ws.ResetTradeResponse;
import trade.ws.Sell;
import trade.ws.SellResponse;
import trade.ws.UpdateAccountProfile;
import trade.ws.UpdateAccountProfileResponse;
import trade.ws.UpdateQuotePriceVolume;
import trade.ws.UpdateQuotePriceVolumeResponse;
import trade.xml.SoapEnvelope;

public class TradeOperationHandlers
{
	private final static Trade srv = new TradeDirect();

	private static final SoapOperationHandler getMarketSummaryHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetMarketSummaryResponse response = new GetMarketSummaryResponse();
			response.setMarketSummaryReturn(srv.getMarketSummary());
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getMarketSummaryResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler buyHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Buy req = soap_in.body.buyRequest;
			BuyResponse response = new BuyResponse();
			response.setBuyReturn(srv.buy(req.getUserId(), req.getSymbol(), req.getQuantity(), req.getOrderProcessingMode()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.buyResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler cancelOrderHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.cancelOrderResponse = new CancelOrderResponse();
			return soap_out;
		}
	};

	private static final SoapOperationHandler completeOrderHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.completeOrderResponse = new CompleteOrderResponse();
			return soap_out;
		}
	};

	private static final SoapOperationHandler createQuoteHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.createQuoteResponse = new CreateQuoteResponse();
			return soap_out;
		}
	};

	private static SoapOperationHandler getAccountDataHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetAccountData req = soap_in.body.getAccountDataRequest;
			GetAccountDataResponse response = new GetAccountDataResponse();
			response.setAccountDataReturn(srv.getAccountData(req.getUserId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getAccountDataResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler getAccountProfileDataHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetAccountProfileData req = soap_in.body.getAccountProfileDataRequest;
			GetAccountProfileDataResponse response = new GetAccountProfileDataResponse();
			response.setAccountProfileDataReturn(srv.getAccountProfileData(req.getUserId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getAccountProfileDataResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler getAllQuotesHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getAllQuotesResponse = new GetAllQuotesResponse();
			List<QuoteData> ret = Collections.emptyList();
			soap_out.body.getAllQuotesResponse.setAllQuotesReturn(ret);
			return soap_out;
		}
	};

	private static SoapOperationHandler getClosedOrdersHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetClosedOrders req = soap_in.body.getClosedOrdersRequest;
			GetClosedOrdersResponse response = new GetClosedOrdersResponse();
			response.setClosedOrdersReturn(srv.getClosedOrders(req.getUserId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getClosedOrdersResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler getHoldingHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetHolding req = soap_in.body.getHoldingRequest;
			GetHoldingResponse response = new GetHoldingResponse();
			response.setHoldingReturn(srv.getHolding(req.getHoldingId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getHoldingResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler getHoldingsHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetHoldings req = soap_in.body.getHoldingsRequest;
			GetHoldingsResponse response = new GetHoldingsResponse();
			response.setHoldingsReturn(srv.getHoldings(req.getUserId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getHoldingsResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler getOrdersHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetOrders req = soap_in.body.getOrdersRequest;
			GetOrdersResponse response = new GetOrdersResponse();
			response.setOrdersReturn(srv.getOrders(req.getUserId()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getOrdersResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler getQuoteHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			GetQuote req = soap_in.body.getQuoteRequest;
			GetQuoteResponse response = new GetQuoteResponse();
			response.setQuoteReturn(srv.getQuote(req.getSymbol()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.getQuoteResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler loginHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Login req = soap_in.body.loginRequest;
			LoginResponse response = new LoginResponse();
			response.setLoginReturn(srv.login(req.getUserId(), req.getPassword()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.loginResponse = response;
			return soap_out;
		}
	};

	private static SoapOperationHandler logoutHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Logout req = soap_in.body.logoutRequest;
			LogoutResponse response = new LogoutResponse();
			srv.logout(req.getUserId());
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.logoutResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler orderCompletedHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.orderCompletedResponse = new OrderCompletedResponse();
			return soap_out;
		}
	};

	private static final SoapOperationHandler queueOrderHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.queueOrderResponse = new QueueOrderResponse();
			return soap_out;
		}
	};

	private static final SoapOperationHandler registerHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Register req = soap_in.body.registerRequest;
			RegisterResponse response = new RegisterResponse();
			response.setRegisterReturn(srv.register(req.getUserId(), req.getPassword(), req.getFullname(), req.getAddress(), req.getEmail(), req.getCreditcard(), req.getOpenBalance()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.registerResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler resetTradeHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{ // TODO: NOP
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.resetTradeResponse = new ResetTradeResponse();
			return soap_out;
		}
	};

	private static final SoapOperationHandler sellHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			Sell req = soap_in.body.sellRequest;
			SellResponse response = new SellResponse();
			response.setSellReturn(srv.sell(req.getUserId(), req.getHoldingId(), req.getOrderProcessingMode()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.sellResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler updateAccountProfileHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			UpdateAccountProfile req = soap_in.body.updateAccountProfileRequest;
			UpdateAccountProfileResponse response = new UpdateAccountProfileResponse();
			response.setUpdateAccountProfileReturn(srv.updateAccountProfile(req.getProfileData()));
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.updateAccountProfileResponse = response;
			return soap_out;
		}
	};

	private static final SoapOperationHandler updateQuotePriceVolumeHandler = new SoapOperationHandler()
	{
		@Override
		public SoapEnvelope handleMessage(SoapEnvelope soap_in)
		{
			UpdateQuotePriceVolume req = soap_in.body.updateQuotePriceVolumeRequest;
			UpdateQuotePriceVolumeResponse response = new UpdateQuotePriceVolumeResponse();
			response.setUpdateQuotePriceVolumeReturn(srv.updateQuotePriceVolume(req.getSymbol(), req.getNewPrice(), req.getSharesTraded())); // TODO: changeFactor != newPrice ?
			SoapEnvelope soap_out = new SoapEnvelope();
			soap_out.body.updateQuotePriceVolumeResponse = response;
			return soap_out;
		}
	};

	public static SoapOperationHandler getOperationHandler(SoapEnvelope soap_in)
	{
		if (soap_in.body.buyRequest != null)
		{
			return buyHandler;
		}
		else if (soap_in.body.cancelOrderRequest != null)
		{
			return cancelOrderHandler;
		}
		else if (soap_in.body.completeOrderRequest != null)
		{
			return completeOrderHandler;
		}
		else if (soap_in.body.createQuoteRequest != null)
		{
			return createQuoteHandler;
		}
		else if (soap_in.body.getAccountDataRequest != null)
		{
			return getAccountDataHandler;
		}
		else if (soap_in.body.getAccountProfileDataRequest != null)
		{
			return getAccountProfileDataHandler;
		}
		else if (soap_in.body.getAllQuotesRequest != null)
		{
			return getAllQuotesHandler;
		}
		else if (soap_in.body.getClosedOrdersRequest != null)
		{
			return getClosedOrdersHandler;
		}
		else if (soap_in.body.getHoldingRequest != null)
		{
			return getHoldingHandler;
		}
		else if (soap_in.body.getHoldingsRequest != null)
		{
			return getHoldingsHandler;
		}
		else if (soap_in.body.getMarketSummaryRequest != null)
		{
			return TradeOperationHandlers.getMarketSummaryHandler;
		}
		else if (soap_in.body.getOrdersRequest != null)
		{
			return getOrdersHandler;
		}
		else if (soap_in.body.getQuoteRequest != null)
		{
			return getQuoteHandler;
		}
		else if (soap_in.body.loginRequest != null)
		{
			return loginHandler;
		}
		else if (soap_in.body.logoutRequest != null)
		{
			return logoutHandler;
		}
		else if (soap_in.body.orderCompletedRequest != null)
		{
			return orderCompletedHandler;
		}
		else if (soap_in.body.queueOrderRequest != null)
		{
			return queueOrderHandler;
		}
		else if (soap_in.body.registerRequest != null)
		{
			return registerHandler;
		}
		else if (soap_in.body.resetTradeRequest != null)
		{
			return resetTradeHandler;
		}
		else if (soap_in.body.sellRequest != null)
		{
			return sellHandler;
		}
		else if (soap_in.body.updateAccountProfileRequest != null)
		{
			return updateAccountProfileHandler;
		}
		else if (soap_in.body.updateQuotePriceVolumeRequest != null)
		{
			return updateQuotePriceVolumeHandler;
		}
		else
		{
			throw new IllegalArgumentException("Unknown operation");
		}
	}
}