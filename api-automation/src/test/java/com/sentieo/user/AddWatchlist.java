package com.sentieo.user;

import static com.sentieo.constants.Constants.ADD_WATCHLIST;
import static com.sentieo.constants.Constants.DELETE_WATCHLIST;
import static com.sentieo.constants.Constants.FETCH_USER_PORTFOLIO;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

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

	public static String watchID = "";
	public static String watchName = "";
	public static ArrayList<String> watchTickers = new ArrayList<>();
	public static ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr", "awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack",
					"ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue", "nem", "nee", "lb", "bluu", "tipt",
					"med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv", "hm-b:ss", "tgls", "ssa:ln", "ghdx",
					"aks", "k", "drw", "dri", "drh", "ect", "drn", "tglo", "drd", "glw", "ads:gr", "qcom",
					"cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
					"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));
	String locMobile = "";

	public AddWatchlist() {
		setUp();
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "loc" })
	public void getLoc(@Optional("loc") String loc) {
		locMobile = loc;
	}
	
	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity", "test" ,"mobileMainApp"}, description = "initial-loading")
	public void addWatchlist() throws Exception {
		try {
			CommonUtil obj = new CommonUtil();
			List<String> ticker = obj.pickNRandomItems(tickers, 5);
			createWatchlist(ticker);
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

	@Test(groups = { "sanity", "test", "mobileMainApp"}, description = "delete watchlist")
	public void deleteWatchlist() throws CoreCommonException {
		try {
			deleteUserWatchlist(watchID);
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	public void deleteUserWatchlist(String watchID) throws CoreCommonException {
		String URI = USER_APP_URL + DELETE_WATCHLIST;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("id", watchID);
			
			if(locMobile.equals("ios")) {
				tickerData.put("loc","ios");
			}
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
					"Watchlist successfully deleted", "Verify the API Message");
			int status = apiResp.getStatusCode();
			if (status == 200) {
				String id = respJson.getJSONArray("result").getJSONObject(0).getString("id");
				verify.assertEqualsActualContainsExpected(watchID, id, "Verify watchlist id");
				boolean addedWatchStatus = userPortfolio(watchName);
				verify.assertFalse(addedWatchStatus, "Verify watchlist deleted or not?");
				/*
				 * if(locMobile.equals("ios")) { verify.jsonSchemaValidation(resp, "mobileApis"
				 * + File.separator + "deleteUserWatchlist.json"); }
				 */
			}

		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	public void createWatchlist(List<String> tickers) throws Exception {
		CommonUtil obj = new CommonUtil();
		watchName = obj.getRandomString();
		String URI = USER_APP_URL + ADD_WATCHLIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String ticker = tickers.toString();
		ticker = ticker.replaceAll("\\[", "").replaceAll("\\]", "");
		parameters.put("tickers", ticker);
		parameters.put("w_name", watchName);

		if (locMobile.equals("ios")) {
			parameters.put("loc", "ios");
		}
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
				JSONArray watchlistTickers = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
				watchID = respJson.getJSONArray("result").getJSONObject(0).getString("id");
				for (int i = 0; i < watchlistTickers.length(); i++) {
					String tickerName = watchlistTickers.getString(i);
					watchTickers.add(tickerName);
				}
				verify.assertEquals(watchTickers, tickers, "verify added tickers in watchlist", true);
				boolean addedWatchStatus = userPortfolio(watchName);
				verify.assertTrue(addedWatchStatus, "verify watchlist added or not?");
				/*
				 * if(locMobile.equals("ios")) { verify.jsonSchemaValidation(resp, "mobileApis"
				 * + File.separator + "addWatchlist.json"); }
				 */
			}

		}
	}
}