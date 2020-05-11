package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class AddWatchlist extends APIDriver {

	static String watchID = "";
	String watchName = "";
	public static ArrayList<String> watchTickers = new ArrayList<>();
	public static ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr", "awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack",
					"ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue", "nem", "gnc", "nee", "lb", "bluu", "tipt",
					"med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv", "hm-b:ss", "tgls", "ssa:ln", "ghdx",
					"aks", "k", "drw", "dri", "drh", "ect", "drn", "tglo", "drd", "glw", "ads:gr", "qcom", "gpor",
					"cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
					"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun = true)
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

	@Test(groups = { "sanity", "test" }, description = "initial-loading")
	public void addWatchlist() throws Exception {
		try {
			CommonUtil obj = new CommonUtil();
			watchName = obj.getRandomString();
			String URI = USER_APP_URL + ADD_WATCHLIST;
			HashMap<String, String> parameters = new HashMap<String, String>();
			List<String> ticker = obj.pickNRandomItems(tickers, 5);
			String tickers = ticker.toString();
			tickers = tickers.replaceAll("\\[", "").replaceAll("\\]", "");
			parameters.put("tickers", tickers);
			parameters.put("w_name", watchName);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				String watchlistStatus = respJson.getJSONObject("response").getJSONArray("msg").toString();
				watchlistStatus = watchlistStatus.replaceAll("\\[", "").replaceAll("\\]", "").replace("\"", "").trim();
				String alreadyExists = "Watchlist " + watchName + " already exits";
				if (watchlistStatus.contains(alreadyExists))
					ExtentTestManager.getTest().log(LogStatus.INFO, "Watchlist already exits: -" + watchName);
				else
					verify.assertEqualsActualContainsExpected(watchlistStatus, "Watchlist successfully added",
							"verify watchlist is created?");
				if (watchlistStatus.contains("Watchlist successfully added")) {
					JSONArray watchlistTickers = respJson.getJSONArray("result").getJSONObject(0)
							.getJSONArray("tickers");
					watchID = respJson.getJSONArray("result").getJSONObject(0).getString("id");
					for (int i = 0; i < watchlistTickers.length(); i++) {
						String tickerName = watchlistTickers.getString(i);
						watchTickers.add(tickerName);
					}
					verify.assertEquals(watchTickers, ticker, "verify added tickers in watchlist", true);
					boolean addedWatchStatus = userPortfolio(watchName);
					verify.assertTrue(addedWatchStatus, "verify watchlist added or not?");
				}

			}
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	public boolean userPortfolio(String watchName) throws CoreCommonException {
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("id", watchID);
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		int status = apiResp.getStatusCode();
		if (status == 200) {
			JSONArray getWatchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
			if (getWatchlists.length() != 0 || getWatchlists != null) {
				for (int i = 0; i < getWatchlists.length(); i++) {
					JSONArray allWatchlist = getWatchlists.getJSONArray(i);
					String watchlistName = allWatchlist.getString(0);
					if (watchlistName.equalsIgnoreCase(watchName)) {
						ExtentTestManager.getTest().log(LogStatus.INFO,
								"Watchlist created successfully: -" + watchName);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Test(groups = { "sanity", "test" }, description = "delete watchlist")
	public void deleteWatchlist() throws CoreCommonException {
		String URI = USER_APP_URL + DELETE_WATCHLIST;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("id", watchID);
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "Watchlist successfully deleted",
					"Verify the API Message");
			int status = apiResp.getStatusCode();
			if (status == 200) {
				String id = respJson.getJSONArray("result").getJSONObject(0).getString("id");
				verify.assertEqualsActualContainsExpected(watchID, id, "verify watchlist id");
				boolean addedWatchStatus = userPortfolio(watchName);
				verify.assertFalse(addedWatchStatus, "verify watchlist deleted or not?");
			}

		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}

	}
}