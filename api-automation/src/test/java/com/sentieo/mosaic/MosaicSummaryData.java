package com.sentieo.mosaic;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

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
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class MosaicSummaryData extends APIDriver {

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

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "SCORES_TABLE" }, description = "Fetch " + SCORES_TABLE)
	public void siScoresTable() throws Exception {
		String URI = APP_URL + SCORES_TABLE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		boolean scoreKeyStatus = true;
		boolean scoreStatus = true;
		String seriesKeys ="";
		String key="";
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONObject scores = respJson.getJSONObject("result").getJSONObject("scores");
			Iterator<String> keys = scores.keys();
			while (keys.hasNext()) {
				key = keys.next();
				JSONObject keyData = scores.getJSONObject(key);
				if (keyData.length() == 0 || keyData == null) {
					scoreKeyStatus = false;
					verify.assertTrue(false, "shows blank data for : " + key);
				} else {
					Iterator<String> scoreKeys = keyData.keys();
					while (scoreKeys.hasNext()) {
						 seriesKeys = scoreKeys.next();
						JSONObject seriesData = keyData.getJSONObject(seriesKeys);
						if (seriesData.length() == 0 || seriesData == null) {
							scoreStatus = false;
							verify.assertTrue(false, "shows blank data for : " + seriesKeys + " for " + key);
						}
					}
				}
			}
			if (scoreKeyStatus)
				verify.assertTrue(scoreKeyStatus, "verify scores series :");

			if (scoreStatus)
				verify.assertTrue(scoreStatus, "verify scores sub-type series");

		} catch (Exception e) {

		}
	}

	@Test(groups = { "bonding",
			"test" }, description = "Fetch Firewall", dataProvider = "searchEstimates", dataProviderClass = DataProviderClass.class)
	public void gnipSearchEstimate(String query, String ticker) throws Exception {
		String URI = APP_URL + GNIP_SEARCH_ESTIMATE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		boolean gtrendsSeries = true;
		try {
			parameters.put("ticker", ticker);
			parameters.put("query", query);
			parameters.put("pagetype", "mosaic");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
			if (series.length() == 0 || series == null) {
				verify.assertTrue(false, "series shows blank data");
			}

			else {
				for (int i = 0; i < series.length(); i++) {
					JSONArray trendsSeries = series.getJSONObject(0).getJSONArray("series");
					if (trendsSeries.length() == 0 || trendsSeries == null) {
						verify.assertTrue(false, "gtrends series shows no data : ");
						gtrendsSeries = false;
					}
				}
				if (gtrendsSeries)
					verify.assertTrue(gtrendsSeries, "verify gtrends series for ticker : " + ticker);
			}
		} catch (Error e) {
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gnipAlexa() throws Exception {
		String URI = APP_URL + ALEXA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("url", "apple.com");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
		} catch (Error e) {

		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "verify mosaic summary data")
	public void mosaicSummaryData() throws CoreCommonException {
		JSONArray revenuSeries = null;
		JSONArray kpiSeries = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + NEW_FETCH_MOSAIC_SUMMARY_DATA;
		try {
			parameters.put("selection", "KPI_corrScore");
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			String selection = respJson.getJSONObject("result").getString("selection").toString().trim();
			if (selection.contains("Revenue_corrScore_all")) {
				revenuSeries = respJson.getJSONObject("result").getJSONArray("Revenue").getJSONObject(0)
						.getJSONArray("series");
				if (revenuSeries.length() == 0 || revenuSeries == null)
					assertTrue(false, "Revenue_corrScore_all is empty : ");
			} else {
				kpiSeries = respJson.getJSONObject("result").getJSONArray("KPI").getJSONObject(0)
						.getJSONArray("series");
				if (kpiSeries.length() == 0 || kpiSeries == null)
					assertTrue(false, "kpiSeries is empty : ");
			}
			JSONArray sentieoIndex = respJson.getJSONObject("result").getJSONArray("sentieoIndex").getJSONObject(0)
					.getJSONArray("series");
			if (sentieoIndex.length() == 0 || sentieoIndex == null)
				assertTrue(false, "sentieoIndex is empty : ");
		} catch (Error e) {

		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Check unfied tracker table")
	public void fetchUnifiedTable() throws CoreCommonException {
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + UNIFIEDTRACKERTABLE;
		try {
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONArray main = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("main");
			if (main.length() == 0 || main == null)
				assertTrue(false, "Main array is empty : ");
			JSONArray data = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("data");
			if (data.length() == 0 || data == null)
				assertTrue(false, "data array is empty : ");
		} catch (Error e) {

		}
	}

}