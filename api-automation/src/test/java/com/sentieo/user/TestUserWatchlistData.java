package com.sentieo.user;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import java.util.Iterator;
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

public class TestUserWatchlistData extends APIDriver {

	APIAssertions verify = new APIAssertions();
	JSONObject portfolio = new JSONObject();
	JSONObject watchlistPortfolio = new JSONObject();
	
	@BeforeMethod(alwaysRun=true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun=true)
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

	@Test(groups = { "sanity", "test" }, description = "fetch login 1")
	public void userPortfolio() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("recur", "0");
		tickerData.put("counter", "1");
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
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
			JSONArray watchlistKey = getWatchlists.getJSONArray(i);
			String watchlistName = watchlistKey.get(0).toString();
			if ((!watchlistName.contains("Recent Tickers")) && (!watchlistName.contains("Recent & Followed Tickers"))
					&& (!watchlistName.contains("Team Tickers")) && (!watchlistName.contains("My Tickers"))) {
				JSONArray watchlistValue = (JSONArray) watchlistKey.get(3);
				portfolio.put(watchlistName, watchlistValue);
			}

		}

		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "Watchlist manager call")
	public void userPortfolioWatchlistManager() throws Exception {
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		querydata.put("counter", "1");
		String URI = USER_APP_URL + FETCH_USERS_WATCHLIST;
		RequestSpecification spec = formParamsSpec(querydata);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getString("msg"), "Success", "Verify the API Message");
		JSONObject obj = respJson.getJSONObject("result");
		Iterator<String> keyItr = obj.keys();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			JSONObject watchlist = (JSONObject) obj.get(key);
			Iterator<String> keyItre = watchlist.keys();
			if (!key.contains("user_groups")) {
				while (keyItre.hasNext()) {
					String watchlistName = keyItre.next();
					JSONObject watchlistItem = watchlist.getJSONObject(watchlistName);
					JSONArray tickers = watchlistItem.getJSONArray("ticker");
					watchlistPortfolio.put(watchlistName, tickers);
					
				}
			}
		}
		verify.verifyEquals(portfolio.length(), watchlistPortfolio.length(), "");
		verify.verifyAll();
	}

}
