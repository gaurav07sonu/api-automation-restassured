package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

public class AddDeleteTickerInWatchlist extends APIDriver {

	static String watchName = "";
	static String watchId = "";
	static String addTicker = "";
	public static ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr", "awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack",
					"ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue", "nem", "gnc", "nee", "lb", "bluu", "tipt",
					"med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv", "hm-b:ss", "tgls", "ssa:ln", "ghdx",
					"aks", "k", "drw", "dri", "drh", "ect", "drn", "tglo", "drd", "glw", "ads:gr", "qcom", "gpor",
					"cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
					"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));

	static List<String> watchlistTickers = new ArrayList<String>();
	Random rand = new Random();
	String locMobile = "";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity", "test","mobileMainApp" }, description = "initial-loading")
	public void addTickerWatchlist() throws Exception {
		HashMap<String, String> querydata = new HashMap<String, String>();
		ArrayList<String> updatedTicker = new ArrayList<String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + EDIT_WATCHLIST;
		try {
			CommonUtil obj = new CommonUtil();
			List<String> ticker = obj.pickNRandomItems(tickers, 2);
			addTicker = ticker.toString();
			addTicker = addTicker.replaceAll("\\[", "").replaceAll("\\]", "");
			fetchUserWatchlist();
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b>"+"Edit Watchlist: -" + watchName);
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b>"+"Add "+addTicker+" tickers in : -" +watchName+" watchlist");
			querydata.put("tickers", addTicker);
			querydata.put("id", watchId);
			querydata.put("w_name", watchName);
			
			if(locMobile.equals("ios")) {
				querydata.put("loc", "ios");
			}
				RequestSpecification spec = formParamsSpec(querydata);
				Response resp = RestOperationUtils.get(URI, spec, null);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int statusCode = apiResp.getStatusCode();
				if (statusCode == 200) {
					JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
					List<String> updatedTickerPortFolio = userPortfolio(watchName);
					if (watchTicker != null) {
						for (int k = 0; k < watchTicker.length(); k++) {
							updatedTicker.add(watchTicker.getString(k));
						}
					}
					Collections.sort(updatedTicker);
					Collections.sort(updatedTickerPortFolio);
					verify.assertEquals(updatedTicker, updatedTickerPortFolio, "verify added ticker", true);
					verify.verifyAll();
					if(locMobile.equals("ios")) {
						verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testEditWatchlist.json");
					}
			}
		} catch (Exception e) {
			verify.assertFalse(false,"in add Ticker Watchlist catch : "+e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp"}, description = "initial-loading",priority=1)
	public void removeTickerWatchlist() throws CoreCommonException {
		ArrayList<String> updatedTicker = new ArrayList<String>();
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + REMOVE_TICKERS_WATCHLIST;
		try {
			querydata.put("tickers", addTicker);
			querydata.put("id", watchId);
			querydata.put("w_name", watchName);
			
			if(locMobile.equals("ios")) {
				querydata.put("loc", "ios");
			} 
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"<b>" + "Remove: -" + addTicker + " from " + watchName + " watchlist");
				RequestSpecification spec = formParamsSpec(querydata);
				Response resp = RestOperationUtils.get(URI, spec, null);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int statusCode = apiResp.getStatusCode();
				if (statusCode == 200) {
					JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
					List<String> updatedTickerPortFolio = userPortfolio(watchName);
					if (watchTicker != null) {
						for (int k = 0; k < watchTicker.length(); k++) {
							updatedTicker.add(watchTicker.getString(k));
						}
					}
					Collections.sort(updatedTicker);
					Collections.sort(updatedTickerPortFolio);
					verify.assertEquals(updatedTicker, updatedTickerPortFolio, "verify added ticker", true);
					if(locMobile.equals("ios")) {
						verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testRemoveTickerFromWatchlist.json");
					}
			}
		} catch (Exception e) {
			verify.assertFalse(false,"in remove Ticker Watchlist catch : "+e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	public JSONArray fetchUserWatchlist() throws Exception {
		JSONArray watchTickers = null;
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		querydata.put("counter", "1");
		String URI = USER_APP_URL + FETCH_USERS_WATCHLIST;
		try {
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getString("msg"), "Success",
					"Verify the API Message");
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				JSONObject userWatchlist = respJson.getJSONObject("result").getJSONObject("my_watchlists");
				JSONArray jsonArray = new JSONArray();
				Iterator<String> x = userWatchlist.keys();
				while (x.hasNext()) {
					String key = (String) x.next();
					jsonArray.put(key);
				}

				int rand_int1 = rand.nextInt(userWatchlist.length());
				watchName = jsonArray.get(rand_int1).toString();
				if (watchName.contains("Benchmarks")) {
					do {
						rand_int1 = rand.nextInt(userWatchlist.length());
						watchName = jsonArray.get(rand_int1).toString();
					} while (watchName.contains("Benchmarks"));

				}
				watchTickers = userWatchlist.getJSONObject(watchName).getJSONArray("ticker");
				watchId = userWatchlist.getJSONObject(watchName).getString("id");
				return watchTickers;
			} else
				verify.assertFalse(false, "status code is  : " + statusCode);

		} catch (Exception e) {
			verify.assertFalse(false, "in catch" + e.toString());
		} finally {
			verify.verifyAll();
		}
		return watchTickers;

	}

	public ArrayList<String> userPortfolio(String watchName) throws CoreCommonException {
		JSONArray tickers = null;
		ArrayList<String> listdata = new ArrayList<String>();
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		try {
		tickerData.put("id", watchId);
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
						tickers = allWatchlist.getJSONArray(3);
						if (tickers != null) {
							for (int k = 0; k < tickers.length(); k++) {
								listdata.add(tickers.getString(k));
							}
							return listdata;
						}
					}
				}
			}
		}
		} catch (Exception e) {
			verify.assertFalse(false,"in userPortfolio catch : "+e.toString());
		} finally {
			verify.verifyAll();
		}
		return listdata;
	}
}