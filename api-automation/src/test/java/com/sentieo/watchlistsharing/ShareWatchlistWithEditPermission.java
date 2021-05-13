package com.sentieo.watchlistsharing;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
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
import com.sentieo.user.AddDeleteTickerInWatchlist;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class ShareWatchlistWithEditPermission extends APIDriver {
	Properties prop;
	String watchName ="";
	FileReader reader;
	String watchID = "";
	List<String> randomTickers=new ArrayList<String>();
	static ArrayList<String> watchTickers = new ArrayList<>();
	static ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr",
			"awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack", "ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue",
			"nem", "nee", "lb", "bluu", "tipt", "med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv",
			"hm-b:ss", "tgls", "ssa:ln", "ghdx", "aks", "k", "drw", "dri", "drh", "drn", "tglo", "drd", "glw", "ads:gr",
			"qcom", "cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
			"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));
	String locMobile = "";
	static List<String> allTicker;

	public ShareWatchlistWithEditPermission() {
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

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 0)
	public void addWatchlist() throws Exception {
		try {
			CommonUtil obj = new CommonUtil();
			watchName = obj.getRandomString();
			randomTickers = obj.pickNRandomItems(tickers, 5);
			List<String>addedTickers=createWatchlist(randomTickers,watchName);
			verify.assertEquals(addedTickers, randomTickers, "verify added tickers in watchlist", true);
			boolean addedWatchStatus = userPortfolio(watchName);
			verify.assertTrue(addedWatchStatus, "verify watchlist added or not?");
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 1)
	public void shareWatchlist() throws CoreCommonException {
		try {
			if (USER_APP_URL.contains("testing") || USER_APP_URL.contains("platform"))
				shareWatchlist(watchID, prop.getProperty("shared_with_name_testing1"),
						prop.getProperty("shared_with_display_name_testing1"), "edit", false);
			else if (USER_APP_URL.contains("app") || USER_APP_URL.contains("app2") || USER_APP_URL.contains("staging"))
				shareWatchlist(watchID, prop.getProperty("shared_with_name_app1"),
						prop.getProperty("shared_with_display_name_app1"), "edit", false);
			else
				shareWatchlist(watchID, prop.getProperty("shared_with_name_global1"),
						prop.getProperty("shared_with_display_name_global1"), "edit", false);

		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 2)
	public void verifyShareWatchlist() throws CoreCommonException {
		try {
			RestAssured.baseURI = APP_URL;
			String URI = USER_APP_URL + LOGIN_URL;
			HashMap<String, String> loginData = new HashMap<String, String>();
			if (USER_APP_URL.contains("testing") || USER_APP_URL.contains("platform")) {
				loginData.put("email", prop.getProperty("shareDashboard"));
				loginData.put("password", prop.getProperty("shareDashboardPassword"));
			} else if (USER_APP_URL.contains("app") || USER_APP_URL.contains("app2")
					|| USER_APP_URL.contains("staging")) {
				loginData.put("email", prop.getProperty("appUserName"));
				loginData.put("password", prop.getProperty("appPassword"));
			} else {
				loginData.put("email", prop.getProperty("globalUser"));
				loginData.put("password", prop.getProperty("globalPass"));
			}
			RequestSpecification spec = loginSpec(loginData);
			Response resp = RestOperationUtils.login(URI, null, spec, loginData);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			username = respJson.getJSONObject("result").getString("username");
			apid = resp.getCookie("apid");
			usid = resp.getCookie("usid");
			if (apid.isEmpty() || usid.isEmpty()) {
				System.out.println("Login failed");
				System.exit(1);
			}
			boolean addedWatchStatus = userPortfolio(watchName);
			verify.assertTrue(addedWatchStatus, "Verify shared watchlist is visible ??");
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 3)
	public void addTickerInShareWatchlist() throws Exception {
		List<String> ticker = new ArrayList<String>();
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		HashMap<String, String> querydata = new HashMap<String, String>();
		ArrayList<String> updatedTicker = new ArrayList<String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + EDIT_WATCHLIST;
		try {
			CommonUtil obj = new CommonUtil();
			allTicker = obj.getDay("finance" + File.separator + "MosaicDataSet.csv");
			// List<String> ticker = obj.pickNRandomItems(allTicker, 70);

			List<String> privateTickers = obj.readFile("finance" + File.separator + "private-tickers.csv");
			ticker.addAll(privateTickers);
			ticker.addAll(allTicker);
			String addTicker = ticker.toString();
			addTicker = addTicker.replaceAll("\\[", "").replaceAll("\\]", "");
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b>" + "Edit Watchlist: -" + watchName);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"<b>" + "Add " + addTicker + " tickers in : -" + watchName + " watchlist");
			querydata.put("tickers", addTicker);
			querydata.put("id", watchID);
			querydata.put("w_name", watchName);
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
					"Watchlist successfully edited", "Verify the API Message");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {

				JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
				List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName, watchID);
				if (watchTicker != null) {
					for (int k = 0; k < watchTicker.length(); k++) {
						updatedTicker.add(watchTicker.getString(k));
					}
				}
				Collections.sort(updatedTicker);
				Collections.sort(updatedTickerPortFolio);
				verify.assertEquals(updatedTicker, updatedTickerPortFolio, "Verify added ticker", true);

				addTicker = addTicker.replaceAll("\\s", "");
				String[] arrSplit = addTicker.split(",");
				for (int i = 0; i < arrSplit.length; i++) {
					watchTickers.add(arrSplit[i].trim().toLowerCase());
				}
				watchTickers.addAll(randomTickers);
				Collections.sort(watchTickers);
				verify.assertEquals(watchTickers, updatedTickerPortFolio, "Verify added ticker with previous tickers",
						true);
				verify.assertEquals(watchTickers, updatedTicker, "Verify added ticker with edit tickers", true);
				verify.verifyAll();
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in add Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 4)
	public void removeTickerFromShareWatchlist() throws CoreCommonException {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		Random rand = new Random();
		int rand_int1 = rand.nextInt(watchTickers.size());
		String deleteTicker = watchTickers.get(rand_int1);
		ArrayList<String> updatedTicker = new ArrayList<String>();
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + REMOVE_TICKERS_WATCHLIST;
		try {
			querydata.put("tickers", deleteTicker);
			querydata.put("id", watchID);
			querydata.put("w_name", watchName);

			if (locMobile.equals("ios")) {
				querydata.put("loc", "ios");
			}
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"<b>" + "Remove: -" + deleteTicker + " from " + watchName + " watchlist");
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
					"Watchlist successfully edited", "Verify the API Message");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
				List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName, watchID);
				if (watchTicker != null) {
					for (int k = 0; k < watchTicker.length(); k++) {
						updatedTicker.add(watchTicker.getString(k));
					}
				}
				Collections.sort(updatedTicker);
				Collections.sort(updatedTickerPortFolio);
				watchTickers.remove(deleteTicker);
				Collections.sort(watchTickers);
				verify.assertEquals(watchTickers, updatedTickerPortFolio, "Verify deleted tickers with portfolio",
						true);
				verify.assertEquals(watchTickers, updatedTicker, "Verify deleted tickers with delet call tickers",
						true);
				if (locMobile.equals("ios")) {
					verify.jsonSchemaValidation(resp,
							"mobileApis" + File.separator + "testRemoveTickerFromWatchlist.json");
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in remove Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 5)
	public void shareSharedWatchlist() throws CoreCommonException {
		try {
			if (USER_APP_URL.contains("testing") || USER_APP_URL.contains("platform"))
				shareWatchlist(watchID, prop.getProperty("shared_with_name_testing2"),
						prop.getProperty("shared_with_display_name_testing2"), "edit", false);
			else if (USER_APP_URL.contains("app") || USER_APP_URL.contains("app2") || USER_APP_URL.contains("staging"))
				shareWatchlist(watchID, prop.getProperty("shared_with_name_app2"),
						prop.getProperty("shared_with_display_name_app2"), "edit", false);
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 6)
	public void verifyShareSharedWatchlist() throws CoreCommonException {
		try {
			RestAssured.baseURI = APP_URL;
			String URI = USER_APP_URL + LOGIN_URL;
			HashMap<String, String> loginData = new HashMap<String, String>();
			if (USER_APP_URL.contains("testing") || USER_APP_URL.contains("platform")) {
				loginData.put("email", prop.getProperty("testing2user"));
				loginData.put("password", prop.getProperty("testing2userpass"));
			} else if (USER_APP_URL.contains("app") || USER_APP_URL.contains("app2")
					|| USER_APP_URL.contains("staging")) {
				loginData.put("email", prop.getProperty("app2user"));
				loginData.put("password", prop.getProperty("app2userpass"));
			}
			RequestSpecification spec = loginSpec(loginData);
			Response resp = RestOperationUtils.login(URI, null, spec, loginData);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			username = respJson.getJSONObject("result").getString("username");
			apid = resp.getCookie("apid");
			usid = resp.getCookie("usid");
			if (apid.isEmpty() || usid.isEmpty()) {
				System.out.println("Login failed");
				System.exit(1);
			}
			boolean addedWatchStatus = userPortfolio(watchName);
			verify.assertTrue(addedWatchStatus, "Verify shared watchlist is visible ??");
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 7)
	public void addTickerInShareSharedWatchlist() throws Exception {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		HashMap<String, String> querydata = new HashMap<String, String>();
		ArrayList<String> updatedTicker = new ArrayList<String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + EDIT_WATCHLIST;
		try {
			CommonUtil obj = new CommonUtil();
			allTicker = obj.getDay("finance" + File.separator + "ReturnsFrequency.csv");
			List<String> ticker = obj.pickNRandomItems(allTicker, 3);
			String addTicker = ticker.toString();
			addTicker = addTicker.replaceAll("\\[", "").replaceAll("\\]", "");
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b>" + "Edit Watchlist: -" + watchName);
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"<b>" + "Add " + addTicker + " tickers in : -" + watchName + " watchlist");
			querydata.put("tickers", addTicker);
			querydata.put("id", watchID);
			querydata.put("w_name", watchName);
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
					"Watchlist successfully edited", "Verify the API Message");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {

				JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
				List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName, watchID);
				if (watchTicker != null) {
					for (int k = 0; k < watchTicker.length(); k++) {
						updatedTicker.add(watchTicker.getString(k));
					}
				}
				Collections.sort(updatedTicker);
				Collections.sort(updatedTickerPortFolio);
				verify.assertEquals(updatedTicker, updatedTickerPortFolio, "Verify added ticker", true);

				addTicker = addTicker.replaceAll("\\s", "");
				String[] arrSplit = addTicker.split(",");
				for (int i = 0; i < arrSplit.length; i++) {
					watchTickers.add(arrSplit[i].trim().toLowerCase());
				}
				Collections.sort(watchTickers);
				verify.assertEquals(watchTickers, updatedTickerPortFolio,
						"Verify added ticker with portfolio call for share shared watchlist", true);
				verify.assertEquals(watchTickers, updatedTicker,
						"Verify added ticker with edit watchlist tickers for share shared watchlist ", true);
				verify.verifyAll();
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in add Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 8)
	public void removeTickerFromShareSharedWatchlist() throws CoreCommonException {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		Random rand = new Random();
		int rand_int1 = rand.nextInt(watchTickers.size());
		String deleteTicker = watchTickers.get(rand_int1);
		ArrayList<String> updatedTicker = new ArrayList<String>();
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + REMOVE_TICKERS_WATCHLIST;
		try {
			querydata.put("tickers", deleteTicker);
			querydata.put("id", watchID);
			querydata.put("w_name", watchName);

			if (locMobile.equals("ios")) {
				querydata.put("loc", "ios");
			}
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"<b>" + "Remove: -" + deleteTicker + " from " + watchName + " watchlist");
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
					"Watchlist successfully edited", "Verify the API Message");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				JSONArray watchTicker = respJson.getJSONArray("result").getJSONObject(0).getJSONArray("tickers");
				List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName, watchID);
				if (watchTicker != null) {
					for (int k = 0; k < watchTicker.length(); k++) {
						updatedTicker.add(watchTicker.getString(k));
					}
				}
				Collections.sort(updatedTicker);
				Collections.sort(updatedTickerPortFolio);
				watchTickers.remove(deleteTicker);
				Collections.sort(watchTickers);
				verify.assertEquals(watchTickers, updatedTickerPortFolio, "Verify deleted tickers with portfolio",
						true);
				verify.assertEquals(watchTickers, updatedTicker, "Verify deleted tickers with delet call tickers",
						true);
				if (locMobile.equals("ios")) {
					verify.jsonSchemaValidation(resp,
							"mobileApis" + File.separator + "testRemoveTickerFromWatchlist.json");
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in remove Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 9)
	public void verifyaddedTickersForWatchlistOwner() throws CoreCommonException {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		try {
			login();
			List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName, watchID);
			verify.assertEquals(watchTickers, updatedTickerPortFolio, "Verify added ticker for owner", true);
		} catch (CoreCommonException e) {
			verify.assertTrue(false, "in add Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "delete watchlist", priority = 10)
	public void deleteWatchlist() throws CoreCommonException {
		try {
			deleteUserWatchlist(watchID, true,watchName);
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	public void deleteUserWatchlist(String watchID, boolean msg,String watchName) throws CoreCommonException {
		String URI = USER_APP_URL + DELETE_WATCHLIST;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("id", watchID);

			if (locMobile.equals("ios")) {
				tickerData.put("loc", "ios");
			}
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			if (msg)
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0),
						"Watchlist successfully deleted", "Verify the API Message");
			else
				verify.verifyEquals(respJson.getJSONObject("response").getString("msg"),
						"Watchlist unshared successfully.", "Verify the API Message");
			int status = apiResp.getStatusCode();
			if (status == 200) {
				if (msg) {
					String id = respJson.getJSONArray("result").getJSONObject(0).getString("id");
					verify.assertEqualsActualContainsExpected(watchID, id, "Verify watchlist id");
				}
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
								watchName + " watchlist found in " + username + " account");
						return true;
					}
				}
			}
		}
		return false;
	}

	public ArrayList<String> createWatchlist(List<String> tickers,String watchName) throws Exception {
		ArrayList<String> createWatchTickers = new ArrayList<>();
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
					createWatchTickers.add(tickerName);
				}
			/*
				 * if(locMobile.equals("ios")) { verify.jsonSchemaValidation(resp, "mobileApis"
				 * + File.separator + "addWatchlist.json"); }
				 */
				return createWatchTickers;
			}

		}
		return createWatchTickers;
	}

	public void shareWatchlist(String watchlist_id, String shared_with_name, String shared_with_display_name,
			String permission, boolean shareWithCopy) {
		try {
			String URI = "";
			shared_with_name = ("\"" + shared_with_name + "\"");
			shared_with_display_name = ("\"" + shared_with_display_name + "\"");
			permission = ("\"" + permission + "\"");
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("watchlist_id", watchlist_id);

			if (!shareWithCopy) {
				URI = USER_APP_URL + SHARE_WATCHLIST;
				parameters.put("share_with_list", " [{\"shared_with_type\":\"user\",\"shared_with_name\":"
						+ shared_with_name + ",\"shared_with_display_name\":" + shared_with_display_name
						+ ",\"access_level\":" + permission + ",\"value\":" + shared_with_name + ",\"id\":"
						+ shared_with_name
						+ ",\"state\":\"active\",\"strategy\":{\"itemValueKey\":\"value\",\"itemNameKey\":\"name\",\"itemIdKey\":\"value\",\"type\":\"users\",\"autoHighlight\":true,\"sortOrder\":[\"groups\",\"users\"],\"menuCategories\":{\"groups\":\"Teams\",\"users\":\"Users\",\"All\":\"All\"},\"sortKey\":\"type\",\"tokenCss\":\"\",\"itemProps\":{},\"autoCompleteInput\":null,\"selectionKeys\":[13,9,188],\"inputProps\":{\"tokenProps\":{\"className\":\"text-token user-token\"}},\"limitItems\":0,\"removeToken\":true},\"email\":\"1\"}]");
			} else {
				URI = USER_APP_URL + CLONE_WATCHLIST;
				parameters.put("copy_for_list", " [{\"shared_with_type\":\"user\",\"shared_with_name\":"
						+ shared_with_name + ",\"shared_with_display_name\":" + shared_with_display_name
						+ ",\"access_level\":" + permission + ",\"value\":" + shared_with_name + ",\"id\":"
						+ shared_with_name
						+ ",\"state\":\"active\",\"strategy\":{\"itemValueKey\":\"value\",\"itemNameKey\":\"name\",\"itemIdKey\":\"value\",\"type\":\"users\",\"autoHighlight\":true,\"sortOrder\":[\"groups\",\"users\"],\"menuCategories\":{\"groups\":\"Teams\",\"users\":\"Users\",\"All\":\"All\"},\"sortKey\":\"type\",\"tokenCss\":\"\",\"itemProps\":{},\"autoCompleteInput\":null,\"selectionKeys\":[13,9,188],\"inputProps\":{\"tokenProps\":{\"className\":\"text-token user-token\"}},\"limitItems\":0,\"removeToken\":true},\"email\":\"1\"}]");

			}
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				if (!shareWithCopy)
					verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
							"Verify the API Message");
				else {
					String res = respJson.getJSONObject("result").getJSONArray("copy_status").get(0).toString();
					if (!res.contains("Watchlist successfully created"))
						verify.assertTrue(false, "Watchlist copy not created ");
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		}
	}

	@BeforeClass(alwaysRun = true)
	public void loadFile() throws IOException {
		reader = new FileReader("config.properties");
		prop = new Properties();
		prop.load(reader);
	}
}