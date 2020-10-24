package com.sentieo.user;

import static com.sentieo.constants.Constants.*;
import static com.sentieo.utils.FileUtil.RESOURCE_PATH;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeMethod;
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

public class MyAlertsTickersUpdateDelete extends APIDriver {

	String ticker = "aapl";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "follow-group", description = "follow tickers for alerts", priority = 1)
	public void verifyFollowTicker_GetWatchlistData() throws Exception {
		try {
			JSONObject respJson = getUserWatchlistsData();
			JSONObject my_alerts = respJson.getJSONObject("result").getJSONObject("my_alerts");
			JSONObject settings = my_alerts.getJSONObject(ticker.toLowerCase());
			JSONObject alertSettings = readJSON();

			String pressSetting = alertSettings.getJSONArray("press_release").toString();

			String transcriptSettings = alertSettings.getJSONArray("transcript").toString();

			String presentationSettings = alertSettings.getJSONArray("presentation").toString();

			String getPressSetting = settings.getJSONArray("press_release").toString();
			String gettTranscriptSettings = settings.getJSONArray("transcript").toString();

			String getPresentationSettings = settings.getJSONArray("presentation").toString();

			verify.assertEqualsActualContainsExpected(getPressSetting, pressSetting, "verify press release settings");
			verify.assertEqualsActualContainsExpected(gettTranscriptSettings, transcriptSettings,
					"verify transcripts settings");
			verify.assertEqualsActualContainsExpected(getPresentationSettings, presentationSettings,
					"verify presenation settings");

		} catch (Error e) {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"in verifyFollowTicker_GetWatchlistData ticker catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();

		}
	}

	@Test(groups = "follow-group", description = "follow tickers for alerts", priority = 2)
	public void unfollowTicker() throws CoreCommonException {
		String URI = USER_APP_URL + UNFOLLOW_TICKER;
		HashMap<String, String> querydata = new HashMap<String, String>();
		try {
			querydata.put("ticker", "aapl");
			querydata.put("new_wl", "true");
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.post(URI, null, spec, querydata);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				boolean unfollow_ticker = respJson.getJSONObject("result").getBoolean("unfollow_flag");
				verify.assertTrue(unfollow_ticker, "verify ticker unfollow status : ");
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in unfollow ticker catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "follow-group", description = "follow tickers for alerts", priority = 0)
	public void followTicker() throws CoreCommonException {
		String URI = USER_APP_URL + FOLLOW;
		HashMap<String, String> querydata = new HashMap<String, String>();
		querydata.put("ticker", ticker);
		querydata.put("new_wl", "true");
		try {
			JSONObject alertSettings = readJSON();
			querydata.put("alert_settings", alertSettings.toString());
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.post(URI, null, spec, querydata);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				boolean unfollow_ticker = respJson.getJSONObject("result").getBoolean("follow_flag");
				verify.assertTrue(unfollow_ticker, "verify ticker follow status : ");
				JSONObject settings = respJson.getJSONObject("result").getJSONObject("ticker_settings");
				verify.assertTrue(alertSettings.toString(), settings.toString(), "verify ticker settings : ");
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in follow ticker catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "follow-group", description = "verify un-follow ticker in alerts", priority = 3)
	public void verifyUnFollowTicker_GetWatchlistData() throws Exception {
		boolean final_status = true;
		try {
			JSONObject respJson = getUserWatchlistsData();
			JSONObject my_alerts = respJson.getJSONObject("result").getJSONObject("my_alerts");
			Iterator<String> keys = my_alerts.keys();

			while (keys.hasNext()) {
				String key = keys.next();
				if (my_alerts.get(key).toString().equalsIgnoreCase(ticker.trim())) {
					verify.assertTrue(false, "Ticker is still exists in alerts : ");
					final_status = false;
				}
			}
			if (final_status)
				verify.assertTrue(true, "verify tickershould not visible in alerts secton after unfollow : ");

		} catch (Error e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in verify unfollow ticker catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();

		}
	}

	@Test(groups = "follow-group", description = "verify un-follow ticker in alerts", priority = 4)
	public void verifyCheckTickerSettings() throws Exception {
		String URI = USER_APP_URL + CHECK_TICKER_SETTINGS;
		try {
			JSONObject alertSettings = readJSON();
			HashMap<String, String> querydata = new HashMap<String, String>();
			querydata.put("tickers", ticker);
			RequestSpecification spec = formParamsSpec(querydata);
			Response resp = RestOperationUtils.get(URI, spec, querydata);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONObject fetch_alertSettings = respJson.getJSONObject("result").getJSONObject("result")
						.getJSONObject(ticker.toLowerCase());
				String pressSetting = fetch_alertSettings.getJSONArray("press_release").toString();

				String transcriptSettings = fetch_alertSettings.getJSONArray("transcript").toString();

				String presentationSettings = fetch_alertSettings.getJSONArray("presentation").toString();

				String getPressSetting = alertSettings.getJSONArray("press_release").toString();

				String getTranscriptSettings = alertSettings.getJSONArray("transcript").toString();

				String getPresentationSettings = alertSettings.getJSONArray("presentation").toString();

				verify.assertEqualsActualContainsExpected(getPressSetting, pressSetting,
						"verify press release settings");
				verify.assertEqualsActualContainsExpected(getTranscriptSettings, transcriptSettings,
						"verify transcripts settings");
				verify.assertEqualsActualContainsExpected(getPresentationSettings, presentationSettings,
						"verify presenation settings");

			}
		} catch (Error e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in verifyCheckTickerSettings catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();

		}
	}

	@Test(groups = "follow-group", description = "verify un-follow ticker in alerts", priority = 5)
	public void alertNotificationClick() throws Exception {
		HashMap<String, String> querydata = new HashMap<String, String>();
		String URI = USER_APP_URL + ALERT_NOTIFICATION_CLICK;
		try {
			if (URI.contains("app") || URI.contains("staging") || URI.contains("app2")) {
				CommonUtil util = new CommonUtil();
				String time = util.getCurrentTimeStamp();
				querydata.put("timestamp", time);
				RequestSpecification spec = formParamsSpec(querydata);
				Response resp = RestOperationUtils.get(URI, spec, querydata);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
							"Verify the API Message");
				}
			}
		} catch (Error e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in alertNotificationClick catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}

	}

	public JSONObject getUserWatchlistsData() throws Exception {
		String URI = USER_APP_URL + GET_USER_WATCHLIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		JSONObject respJson = null;
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				JSONObject entity_info = respJson.getJSONObject("result").getJSONObject("entity_info");
				if (entity_info.length() == 0 || entity_info == null)
					verify.assertTrue(false, "entity_info shows blank");
				JSONArray individual_watchlist = respJson.getJSONObject("result").getJSONArray("individual_watchlist");
				if (individual_watchlist.length() == 0 || individual_watchlist == null)
					verify.assertTrue(false, "individual watchlist size is zero ");
				JSONArray all_tickers = respJson.getJSONObject("result").getJSONArray("all_tickers");
				if (all_tickers.length() == 0 || all_tickers == null)
					verify.assertTrue(false, "All tickers size is zero ");
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in getUserWatchlistsData catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		}
		return respJson;
	}

	public JSONObject readJSON() throws IOException, ParseException {
		String file = "user" + File.separator + "Settings.json";
		JSONParser jsonParser = new JSONParser();
		JSONObject employeeList = null;
		try (FileReader reader = new FileReader(RESOURCE_PATH + File.separator + file)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			employeeList = (JSONObject) new JSONTokener(obj.toString()).nextValue();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return employeeList;
	}
}