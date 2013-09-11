package trade.xml;

import trade.ws.Buy;
import trade.ws.BuyResponse;
import trade.ws.CancelOrder;
import trade.ws.CancelOrderResponse;
import trade.ws.CompleteOrder;
import trade.ws.CompleteOrderResponse;
import trade.ws.CreateQuote;
import trade.ws.CreateQuoteResponse;
import trade.ws.GetAccountData;
import trade.ws.GetAccountDataResponse;
import trade.ws.GetAccountProfileData;
import trade.ws.GetAccountProfileDataResponse;
import trade.ws.GetAllQuotes;
import trade.ws.GetAllQuotesResponse;
import trade.ws.GetClosedOrders;
import trade.ws.GetClosedOrdersResponse;
import trade.ws.GetHolding;
import trade.ws.GetHoldingResponse;
import trade.ws.GetHoldings;
import trade.ws.GetHoldingsResponse;
import trade.ws.GetMarketSummary;
import trade.ws.GetMarketSummaryResponse;
import trade.ws.GetOrders;
import trade.ws.GetOrdersResponse;
import trade.ws.GetQuote;
import trade.ws.GetQuoteResponse;
import trade.ws.Login;
import trade.ws.LoginResponse;
import trade.ws.Logout;
import trade.ws.LogoutResponse;
import trade.ws.OrderCompleted;
import trade.ws.OrderCompletedResponse;
import trade.ws.QueueOrder;
import trade.ws.QueueOrderResponse;
import trade.ws.Register;
import trade.ws.RegisterResponse;
import trade.ws.ResetTrade;
import trade.ws.ResetTradeResponse;
import trade.ws.Sell;
import trade.ws.SellResponse;
import trade.ws.UpdateAccountProfile;
import trade.ws.UpdateAccountProfileResponse;
import trade.ws.UpdateQuotePriceVolume;
import trade.ws.UpdateQuotePriceVolumeResponse;

public class SoapBody
{
	public SoapFault fault;

	public Buy buyRequest;
	public BuyResponse buyResponse;

	public CancelOrder cancelOrderRequest;
	public CancelOrderResponse cancelOrderResponse;

	public CompleteOrder completeOrderRequest;
	public CompleteOrderResponse completeOrderResponse;

	public CreateQuote createQuoteRequest;
	public CreateQuoteResponse createQuoteResponse;

	public GetAccountData getAccountDataRequest;
	public GetAccountDataResponse getAccountDataResponse;

	public GetAccountProfileData getAccountProfileDataRequest;
	public GetAccountProfileDataResponse getAccountProfileDataResponse;

	public GetAllQuotes getAllQuotesRequest;
	public GetAllQuotesResponse getAllQuotesResponse;

	public GetClosedOrders getClosedOrdersRequest;
	public GetClosedOrdersResponse getClosedOrdersResponse;

	public GetHolding getHoldingRequest;
	public GetHoldingResponse getHoldingResponse;

	public GetHoldings getHoldingsRequest;
	public GetHoldingsResponse getHoldingsResponse;

	public GetMarketSummary getMarketSummaryRequest;
	public GetMarketSummaryResponse getMarketSummaryResponse;

	public GetOrders getOrdersRequest;
	public GetOrdersResponse getOrdersResponse;

	public GetQuote getQuoteRequest;
	public GetQuoteResponse getQuoteResponse;

	public Login loginRequest;
	public LoginResponse loginResponse;

	public Logout logoutRequest;
	public LogoutResponse logoutResponse;

	public OrderCompleted orderCompletedRequest;
	public OrderCompletedResponse orderCompletedResponse;

	public QueueOrder queueOrderRequest;
	public QueueOrderResponse queueOrderResponse;

	public Register registerRequest;
	public RegisterResponse registerResponse;

	public ResetTrade resetTradeRequest;
	public ResetTradeResponse resetTradeResponse;

	public Sell sellRequest;
	public SellResponse sellResponse;

	public UpdateAccountProfile updateAccountProfileRequest;
	public UpdateAccountProfileResponse updateAccountProfileResponse;

	public UpdateQuotePriceVolume updateQuotePriceVolumeRequest;
	public UpdateQuotePriceVolumeResponse updateQuotePriceVolumeResponse;
}