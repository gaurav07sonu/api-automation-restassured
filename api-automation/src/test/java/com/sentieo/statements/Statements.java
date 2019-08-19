package com.sentieo.statements;

import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
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

public class Statements extends APIDriver {

	APIAssertions verify = new APIAssertions();

	static String getName = null;
	static String getURl = null;
	static String getDocID = null;
	static String ticker = "aapl";

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

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "fetch screener models")
	public void fetchallxbrltables() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("ticker", ticker);
		RequestSpecification spec = queryParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(FETCH_ALL_XBRL_TABLES, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);

		try {
			if (respJson.get("result") instanceof JSONObject) {
				JSONObject completeAPIResult = respJson.getJSONObject("result");
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");

				JSONArray getFinancialTables = completeAPIResult.getJSONArray("financial_tables");
				for (int i = 0; i < getFinancialTables.length(); i++) {
					JSONArray getFinancialKeys = getFinancialTables.getJSONArray(i);
					getName = String.valueOf(getFinancialKeys.get(0));
					getURl = String.valueOf(getFinancialKeys.get(1));
					break;
				}

				JSONArray getQuarterResults = completeAPIResult.getJSONArray("quarter_results");
				for (int j = 0; j < getQuarterResults.length(); j++) {
					JSONArray getDocKey = getQuarterResults.getJSONArray(j);
					getDocID = String.valueOf(getDocKey.get(1));
					break;
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.INFO, "Statement not available for ticker : -" + ticker);
			}
			getxbrldatatable(getURl, getName, getDocID);
		} catch (JSONException je) {
			verify.verificationFailures.add(je);
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
		}
		verify.verifyAll();
	}

	public void getxbrldatatable(String getURl, String getName, String getDocID) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		if (getURl != null && getName != null && getDocID != null) {
			tickerData.put("url", "http:www.sec.gov" + getURl);
			tickerData.put("key", getName);
			tickerData.put("doc_id", getDocID);
			RequestSpecification spec = queryParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(GET_XBRL_DATA_TABLE, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");

		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO, "Statement not available for ticker : -" + ticker);
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch screener models", dataProvider = "fetchUnifiedStream")
	public void fetchUnifiedStream(String client_type, String doc_type, String tickers, String social_reach)
			throws Exception {
		List<String> tickerList = new ArrayList<>();
		tickerList.add("goog");
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("social_reach", social_reach);
		tickerData.put("doc_type", doc_type);
		tickerData.put("tickers", tickers);
		tickerData.put("dashboard", "news_stream");
		tickerData.put("client_type_status", client_type);
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post(FETCH_UNIFIED_STREAM, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 3000);
		String[] words = tickers.split(",");
		for (String w : words) {
			tickerList.add(w);
		}
		try {
			JSONArray value = respJson.getJSONObject("result").getJSONArray("data");
			if (value.length() != 0) {
				for (int i = 0; i < value.length(); i++) {
					JSONObject interate = value.getJSONObject(i);
					List<String> getTickerFromDoc = new ArrayList<>();
					JSONArray getTicker = interate.getJSONArray("tickers");
					for (int j = 0; j < getTicker.length(); j++) {
						String ticker = getTicker.getString(j);
						getTickerFromDoc.add(ticker);
					}
					boolean noElementsInCommon = Collections.disjoint(tickerList, getTickerFromDoc);
					if (noElementsInCommon) {
						verify.verificationFailures.add(new Exception());
						ExtentTestManager.getTest().log(LogStatus.FAIL,
								"Ticker not Present in Main TickersList" + "\n" + tickerList + "\n" + getTickerFromDoc);
					}
				}
			}
			verify.verifyAll();
		} catch (JSONException e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
	}

	@DataProvider(name = "fetchUnifiedStream")
	public Object[][] fetchUnifiedStream() {
		return new Object[][] { { "low", "articles", "aapl,msft,googl", "sentieo" },
				{ "low", "fi", "aapl", "whitelist" }, { "low", "tweets", "aapl,msft,googl", "all" }, };
	}

}
