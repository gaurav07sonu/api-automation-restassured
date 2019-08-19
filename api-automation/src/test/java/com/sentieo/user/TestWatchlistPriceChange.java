package com.sentieo.user;

import static com.sentieo.constants.Constants.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class TestWatchlistPriceChange extends APIDriver {
	String priceChangeValue = "";
	String watchlistName = "";
	double watchlistChangeValue = 0;
	List<Double> tickerValues = new ArrayList<Double>();
	List<String> watchlistTickers = new ArrayList<String>();
	APIAssertions verify = new APIAssertions();
	JSONObject portfolio = new JSONObject();
	JSONObject watchlistPortfolio = new JSONObject();

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

	@BeforeClass
	public void setup() throws Exception {
		String URI = USER_APP_URL + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", EMAIL);
		loginData.put("password", PASSWORD);
		RequestSpecification spec = loginSpec(loginData);
		Response resp = RestOperationUtils.login(URI, null, spec, loginData);
		apid = resp.getCookie("apid");
		usid = resp.getCookie("usid");
		RestAssured.baseURI = APP_URL;
	}

	@Test(groups = "sanity", description = "fetch login 1", priority = 0)
	public void testWatchlistPercentageChange() throws Exception {
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
				"Verify the API Message");
		JSONArray getWatchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
		for (int i = 0; i < getWatchlists.length(); i++) {
			JSONArray allWatchlist = getWatchlists.getJSONArray(i);
			watchlistName = allWatchlist.getString(0);
			// if(git sttwatchlistName.contains("sdeff"))
			priceChangeValue = allWatchlist.getString(1).replaceAll("%", "");
			watchlistChangeValue = Double.parseDouble(priceChangeValue);
			JSONArray allWatchList = (JSONArray) allWatchlist.get(3);
			if(allWatchList.length()==0)
			{
				
				verify.verifyTrue(false, watchlistName+" Watchlist has no tickers : "+allWatchList);
			}
			watchlistTickers
					.add(allWatchList.toString().replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", " "));
			calculatePercentChange();
		}
		verify.verifyAll();
	}

	public void calculatePercentChange() throws Exception {

		double totalsum = 0;
		double avg_Pct_Change = 0;
		double final_Change = 0;
		double expected = 1;
		for (int i = 0; i < watchlistTickers.size(); i++) {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("tickers", watchlistTickers.get(i));
			String URI = APP_URL + FETCH_TICKER_QUOTE;
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject obj = respJson.getJSONObject("result");
			Iterator<String> keyItr = obj.keys();
			while (keyItr.hasNext()) {
				String key = keyItr.next();
				JSONArray value = obj.getJSONArray(key);
				if (!StringUtils.isEmpty(value.get(2).toString())) {
					double tickerChangeValue = value.getDouble(2);
					tickerValues.add(tickerChangeValue);
					totalsum += tickerChangeValue;
				}

			}
			if (tickerValues.size() != 0) {
				avg_Pct_Change = totalsum / tickerValues.size();
				DecimalFormat df2 = new DecimalFormat("#.##");
				avg_Pct_Change = Double.valueOf(df2.format(avg_Pct_Change));
				final_Change = watchlistChangeValue - avg_Pct_Change;
			}

		}
		watchlistTickers.removeAll(watchlistTickers);
		tickerValues.removeAll(tickerValues);
		verify.verifyEquals(final_Change, expected, "Watchlist is :  " + watchlistName + "  Watchlist Change Value:  "
				+ watchlistChangeValue + "    Calculated Value:  " + avg_Pct_Change);

	}
}
