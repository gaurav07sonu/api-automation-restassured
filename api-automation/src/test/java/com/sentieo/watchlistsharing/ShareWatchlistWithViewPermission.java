package com.sentieo.watchlistsharing;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EDIT_WATCHLIST;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
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
import com.sentieo.dashboard.DashboardCommonUtils;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.user.AddDeleteTickerInWatchlist;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class ShareWatchlistWithViewPermission extends APIDriver {

	Properties prop;
	FileReader reader;
	static String sharedWatchlist_ID = "";
	static String watchID="";
	String watchName ="";
	static ArrayList<String> watchTickers = new ArrayList<>();
	static ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr",
			"awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack", "ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue",
			"nem", "nee", "lb", "bluu", "tipt", "med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv",
			"hm-b:ss", "tgls", "ssa:ln", "ghdx", "aks", "k", "drw", "dri", "drh", "drn", "tglo", "drd", "glw", "ads:gr",
			"qcom", "cohr", "cohu", "apam", "plow", "bdsi", "call", "hwbk", "nke", "yamcy", "aeex", "yahoy",
			"td", "md", "mg", "mc", "mb", "atkr", "mn", "mt", "mx", "czz"));
	String locMobile = "";
	static List<String> allTicker;
	static List<String> ticker = new ArrayList<String>();

	public ShareWatchlistWithViewPermission() {
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
			DashboardCommonUtils das=new DashboardCommonUtils();
			ShareWatchlistWithEditPermission edit = new ShareWatchlistWithEditPermission();
			CommonUtil obj = new CommonUtil();
			watchName = obj.getRandomString();
			ticker = obj.pickNRandomItems(tickers, 5);
			List<String>addedTickers=edit.createWatchlist(ticker,watchName);
			verify.assertEquals(addedTickers, ticker, "verify added tickers in watchlist", true);
			boolean addedWatchStatus = edit.userPortfolio(watchName);
			verify.assertTrue(addedWatchStatus, "verify watchlist added or not?");
			watchID=das.getWatchlistID(watchName);
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 1)
	public void shareWatchlist() throws CoreCommonException {
		try {
			ShareWatchlistWithEditPermission edit = new ShareWatchlistWithEditPermission();
			if (USER_APP_URL.contains("testing") || USER_APP_URL.contains("platform"))
				edit.shareWatchlist(watchID,
						prop.getProperty("shared_with_name_testing1"),
						prop.getProperty("shared_with_display_name_testing1"), "view", false, "");
			else if (USER_APP_URL.contains("app") || USER_APP_URL.contains("app2") || USER_APP_URL.contains("staging"))
				edit.shareWatchlist(watchID, prop.getProperty("shared_with_name_app1"),
						prop.getProperty("shared_with_display_name_app1"), "view", false, "");
			else
				edit.shareWatchlist(watchID,
						prop.getProperty("shared_with_name_global1"),
						prop.getProperty("shared_with_display_name_global1"), "view", false, "");

		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 2)
	public void verifyShareWatchlist() throws CoreCommonException {
		try {
			ShareWatchlistWithEditPermission edit = new ShareWatchlistWithEditPermission();
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
			boolean addedWatchStatus = edit.userPortfolio(watchName);
			verify.assertTrue(addedWatchStatus, "Verify shared watchlist is visible ??");
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 3)
	public void addTickerInShareWatchlist() throws Exception {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("new_wl", "true");
		String URI = USER_APP_URL + EDIT_WATCHLIST;
		try {
			CommonUtil obj = new CommonUtil();
			allTicker = obj.getDay("finance" + File.separator + "MosaicDataSet.csv");
			List<String> tickers = obj.pickNRandomItems(allTicker, 1);
			tickers.addAll(allTicker);
			String addTicker = tickers.toString();
			addTicker = addTicker.replaceAll("\\[", "").replaceAll("\\]", "");
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"<b>" + "Edit Watchlist: -" + watchName);
			ExtentTestManager.getTest().log(LogStatus.INFO, "<b>" + "Add " + addTicker + " tickers in : -"
					+ watchName + " watchlist");
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
			verify.verifyEquals(respJson.getJSONObject("response").getString("msg"), "Watchlist not found.",
					"Verify the API Message");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName,
						watchID);
				verify.assertEquals(ticker, updatedTickerPortFolio, "Verify added ticker with previous tickers", true);
				verify.verifyAll();
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in add Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "initial-loading", priority = 4)
	public void verifyaddedTickersForWatchlistOwner() throws CoreCommonException {
		AddDeleteTickerInWatchlist addDel = new AddDeleteTickerInWatchlist();
		try {
			login();
			List<String> updatedTickerPortFolio = addDel.userPortfolio(watchName,
					watchID);
			verify.assertEquals(ticker, updatedTickerPortFolio, "Verify added ticker for owner", true);
		} catch (CoreCommonException e) {
			verify.assertTrue(false, "in add Ticker Watchlist catch : " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity", "test", "mobileMainApp" }, description = "delete watchlist", priority = 5)
	public void deleteWatchlist() throws CoreCommonException {
		try {
			ShareWatchlistWithEditPermission obj = new ShareWatchlistWithEditPermission();
			obj.deleteUserWatchlist(watchID, true,watchName);
		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@BeforeClass(alwaysRun = true)
	public void loadFile() throws IOException {
		reader = new FileReader("config.properties");
		prop = new Properties();
		prop.load(reader);
	}
}
