package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.mongodb.util.JSONParseException;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.Reporter;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class HeartbeatMonitors extends APIDriver {
	enum Team {
		FIN, Search, Notebook, User, Graph;
	}

	private static final String BREAK_LINE = "</br>";
	HashMap<String, String> parameters = new HashMap<String, String>();
	static StringBuffer sbPass = new StringBuffer();
	static StringBuffer sbFail = new StringBuffer();
	String time = "";
	String header = "";
	String footer = "";

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
		setHTMLContent();
	}

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch graph data")
	public void fetchGraphDataYearlyEstimate() throws Exception {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_GRAPH_DATA;
		try {
			String ticker = "aapl";
			parameters.put("graphtype", "yearlyEstimate");
			parameters.put("subtype", "TotalRevenue");
			parameters.put("periodtype", "Quarterly");
			parameters.put("source", "summary");
			parameters.put("startyear", "2014");
			parameters.put("endyear", "2022");
			parameters.put("getstock", "true");
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONArray getSeries = respJson.getJSONObject("result").getJSONArray("series");
			if (getSeries.length() == 0 || getSeries == null)
				assertTrue(false);
			for (int i = 0; i < getSeries.length(); i++) {
				String path = "result." + "series" + "[" + i + "]." + "title";
				String seriesTitle = String.valueOf(apiResp.getNodeValue(path));
				if (seriesTitle.contains("Total Revenue-2021")) {
					JSONArray revenue2021Estimates = getSeries.getJSONObject(i).getJSONArray("series");
					if (revenue2021Estimates.length() == 0 || revenue2021Estimates == null)
						assertTrue(false);
				}
			}
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company status")
	public void fetchCompamyStatus() throws CoreCommonException {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_COMPANY_STATUS;
		try {
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			String stock_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("stock_flag").toString();
			if (!stock_flag.contains("true"))
				assertTrue(false);
			String ih_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("ih_flag").toString();
			if (!ih_flag.contains("true"))
				assertTrue(false);
			String report_currency = respJson.getJSONObject("result").getJSONObject(ticker).get("report_currency")
					.toString();
			if (!report_currency.contains("usd"))
				assertTrue(false);
			String trading_status = respJson.getJSONObject("result").getJSONObject(ticker).get("trading_status")
					.toString();
			if (!trading_status.contains("Success"))
				assertTrue(false);
			String allowed_edt = respJson.getJSONObject("result").getJSONObject(ticker).get("allowed_edt").toString();
			if (!allowed_edt.contains("true"))
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company header data")
	public void fetchCompamyHeaderData() throws CoreCommonException {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_COMPANY_STATUS;
		try {
			String ticker = "aapl";
			URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONArray intradaySeries = respJson.getJSONObject("result").getJSONArray("intraday");
			JSONObject fin_summary = respJson.getJSONObject("result").getJSONObject("fin_summary");
			if (intradaySeries.length() == 0 || intradaySeries == null)
				assertTrue(false);
			if (fin_summary.length() == 0 || fin_summary == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check company summary table")
	public void companySummaryTable() throws CoreCommonException {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + "/api/testfailure/";
		try {
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONObject result = respJson.getJSONObject("result").getJSONObject("data").getJSONObject("sales");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int lastYear = currentYear - 1;
			int secondLastYear = currentYear - 2;
			int futureYear = currentYear + 1;

			String currentyearInString = Integer.toString(currentYear);
			String lastYearInString = Integer.toString(lastYear);
			String secondLastYearInString = Integer.toString(secondLastYear);
			String futureYearInString = Integer.toString(futureYear);

			String currentYearValue = result.get(currentyearInString).toString();
			String lastYearValue = result.get(lastYearInString).toString();
			String secondLastYearValue = result.get(secondLastYearInString).toString();
			String futureYearValue = result.get(futureYearInString).toString();

			if (currentYearValue.isEmpty() || currentYearValue == null || lastYearValue.isEmpty()
					|| lastYearValue == null || secondLastYearValue.isEmpty() || secondLastYearValue == null
					|| futureYearValue.isEmpty() || futureYearValue == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchLivePrice() throws CoreCommonException {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + "/api/justatest/";
		try {
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONObject result = respJson.getJSONObject("result").getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetchscreenersearch")
	public void fetchscreenersearch() throws Exception {
		Team team = Team.FIN;
		HashMap<String, String> parameters = new HashMap<String, String>();
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_SCREENER_SEARCH;
		try {
			parameters.put("tickers", "");
			parameters.put("pagetype", "screener");
			parameters.put("type", "company");
			parameters.put("sort", "FY_Y_S_mkt_cap:desc");
			parameters.put("currency", "usd");
			parameters.put("screener_search_settings", "{\"unique_str\":\"\",\"page_nav\":0,\"page_sort\":0}");

			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONObject values = respJson.getJSONObject("result");
			if (values == null || values.length() == 0 || values.toString().equals("{}"))
				assertTrue(false);
			JSONArray tickers = values.getJSONArray("tickers");
			if (tickers == null || tickers.length() == 0 || tickers.toString().equals("{}"))
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchScreenerModels() throws CoreCommonException {
		Team team = Team.FIN;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = USER_APP_URL + FETCH_SCREENER_MODELS;
		try {
			parameters.put("ptype", "screener_models");
			parameters.put("fetch_mode", "all");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONArray result = respJson.getJSONArray("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchSearch() throws CoreCommonException {
		Team team = Team.Search;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_SEARCH;
		try {
			parameters.put("tickers", "aapl");
			parameters.put("query", "");
			parameters.put("filing_type", "companys sales OR sales");
			parameters.put("original_query", "companys sales OR sales");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void fetchFirewall() throws Exception {
		String URI = USER_APP_URL + FETCH_FIREWALL_TEST;
		Team team = Team.User;
		APIResponse apiResp = null;
		Response resp = null;
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, null);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void fetchTickerQuote() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.User;
		String URI = APP_URL + FETCH_TICKER_QUOTE;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("tickers", "aapl");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void checkDomain() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.User;
		String URI = APP_URL + CHECK_DOMAIN;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("email", EMAIL);
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void getGtrends() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.Graph;
		String URI = APP_URL + GET_GTRENDS;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("ticker", "aapl");
			tickerData.put("termtype", "ticker");
			tickerData.put("type", "normal");
			tickerData.put("rts", "true");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void siScoresTable() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.Graph;
		String URI = APP_URL + SCORES_TABLE;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("ticker", "aapl");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gnipSearchEstimate() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.Graph;
		String URI = APP_URL + GNIP_SEARCH_ESTIMATE;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("ticker", "aapl");
			tickerData.put("query",
					"ipad , Airpods , \"apple watch\" OR applewatch , \"apple tv\" OR appletv , \"Apple Music\" OR applemusic , iphone");
			tickerData.put("pagetype", "mosaic");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gnipAlexa() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.Graph;
		String URI = APP_URL + ALEXA;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("url", "apple.com");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gtrendsAutocomplete() throws Exception {
		APIResponse apiResp = null;
		Response resp = null;
		Team team = Team.Graph;
		String URI = APP_URL + GTRENDSAUTOCOMPLETE;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("word", "apple.com");
			RequestSpecification spec = formParamsSpec(tickerData);
			resp = RestOperationUtils.post(URI, null, spec, tickerData);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchUnifiedStreamAllDocs() throws CoreCommonException {
		Team team = Team.User;
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		List<String> docType = new ArrayList<String>();
		try {
			docType.add("tweets");
			docType.add("articles");
			docType.add("alldocs");
			docType.add("tt");
			docType.add("ppt");
			docType.add("ni");
			docType.add("tt");
			docType.add("nw");
			docType.add("rr");
			docType.add("fi");
			for (int i = 0; i < docType.size(); i++) {
				parameters.put("client_type_status", "low");
				parameters.put("dashboard", "news_stream");
				parameters.put("doc_type", docType.get(i));
				parameters.put("tickers", "aapl");
				parameters.put("tags", "");
				parameters.put("social_reach", "sentieo");
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				assert apiResp.getStatusCode() == 200;
				assert respJson.getJSONObject("response").getBoolean("status") == true;
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			}
		} catch (AssertionError e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	public void updatePassResult(String path, String statusCode, Response resp) {
		sbPass.append("<tr>");

		sbPass.append("<td>");
		sbPass.append(path);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(statusCode);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(generateFormatedResponse(resp, parameters));
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append("<span>&#9989;</span>");
		sbPass.append("</td>");

		sbPass.append("</tr>");
	}

	public void updateFailResult(String path, String statusCode, Response resp) {
		sbPass.append("<tr>");

		sbPass.append("<td>");
		sbPass.append(path);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(statusCode);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(generateFormatedResponse(resp, parameters));
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append("<span>&#10060;</span>");
		sbPass.append("</td>");

		sbPass.append("</tr>");
	}

	public void setHTMLContent() {
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
		time = f.format(new Date());
		header = "<!DOCTYPE html>\n" + "<html>\n" + "<head>\n" + "<title>This is API health check</title>" + "<style>\n"
				+ "table, th, td {\n" + "  border: 1px solid black;\n" + "  border-collapse: collapse;\n" + "}\n"
				+ "th, td {\n" + "  padding: 5px;\n" + "  text-align: left;\n" + "}\n" + "body {\n"
				+ "  background-image: url('https://cdn.crunchify.com/bg.png');\n" + "}" + "</style>\n" + "</head>\n"
				+ "<body>\n" + "\n" + "<h2>API HEARTBEAT (last executed: " + time + ")</h2>\n" + "\n"
				+ "<table style=\"width:100%\">\n" + "  <caption>API health check</caption>\n" + "  <tr>\n"
				+ "    <th>API path</th>\n" + "    <th>Status code</th>\n" + "    <th>Response body</th>\n"
				+ "    <th>Status</th>\n" + "  </tr>";

		footer = "</table>\n" + "\n" + "</body>\n" + "</html>";
	}

	public static void updatePassResult(String path, String team, String statusCode, Response resp,
			HashMap<String, String> parameters) {
		sbPass.append("<tr class=\"item\">");

		sbPass.append("<td>");
		sbPass.append(path);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(team);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(statusCode);
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append(generateFormatedResponse(resp, parameters));
		sbPass.append("</td>");

		sbPass.append("<td>");
		sbPass.append("<span>&#9989;</span>");
		sbPass.append("</td>");

		sbPass.append("</tr>");
	}

	public void updateFailResult(String path, String team, String statusCode, Response resp,
			HashMap<String, String> parameters) {
		sbFail.append("<tr class=\"item\">");

		sbFail.append("<td>");
		sbFail.append(path);
		sbFail.append("</td>");

		sbFail.append("<td>");
		sbFail.append(team);
		sbFail.append("</td>");

		sbFail.append("<td>");
		sbFail.append(statusCode);
		sbFail.append("</td>");

		sbFail.append("<td>");
		sbFail.append(generateFormatedResponse(resp, parameters));
		sbFail.append("</td>");

		sbFail.append("<td>");
		sbFail.append("<span>&#10060;</span>");
		sbFail.append("</td>");

		sbFail.append("</tr>");
	}

	public static String readHTMLHeader() {
		StringBuilder sb = new StringBuilder();
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
		String time = f.format(new Date());

		try (BufferedReader br = Files.newBufferedReader(Paths.get("src/test/resources/api-heartbeat/header.txt"))) {

			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		return sb.toString().replaceAll("TIME_PLACEHOLDER", time);
	}

	public static String readHTMLFooter() {
		StringBuilder sb = new StringBuilder();

		try (BufferedReader br = Files.newBufferedReader(Paths.get("src/test/resources/api-heartbeat/footer.txt"))) {

			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		return sb.toString();
	}

	public static String generateFormatedResponse(Response res, HashMap<String, String> parameters) {
		JSONObject json = new JSONObject(parameters);
		return generateFormatedPayload(json.toString()) + BREAK_LINE + generateFormatedResponse(res.asString());

	}

	public static String generateFormatedPayload(String payload) {
		try {
			String prettyPayload = "";
			if (payload == null)
				prettyPayload = "No Payload Body";
			else if (payload.trim().isEmpty())
				prettyPayload = "No Payload Body";
			else if (payload.trim().startsWith("{") || payload.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ObjectMapper().readTree(payload));
			else
				prettyPayload = payload;

			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Payload <u> <font size=\"2\" color=\"blue\">(Click to Expand / Collapse)</font> </u></a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Payload (Click to Expand / Collapse)</a><xmp style=\"display:none\">"
					+ payload + "</xmp></>";
		}
	}

	public static String generateFormatedResponse(String payload) {
		try {
			String prettyPayload = "";
			if (payload == null)
				prettyPayload = "No Payload Body";
			else if (payload.trim().isEmpty())
				prettyPayload = "No Payload Body";
			else if (payload.trim().startsWith("{") || payload.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter()
						.writeValueAsString(new ObjectMapper().readTree(payload));
			else
				prettyPayload = payload;

			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Response body <u> <font size=\"2\" color=\"blue\">(Click to Expand / Collapse) </font> </u> </a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Response body (Click to Expand / Collapse)</a><xmp style=\"display:none\">"
					+ payload + "</xmp></>";
		}
	}

	@AfterClass(alwaysRun = true)
	public void generateHTML() {
		String content = readHTMLHeader() + sbFail.toString() + sbPass.toString() + readHTMLFooter();
		try {
			FileUtils.deleteQuietly(new File("heartbeat.html"));
			Files.write(Paths.get("heartbeat.html"), content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
