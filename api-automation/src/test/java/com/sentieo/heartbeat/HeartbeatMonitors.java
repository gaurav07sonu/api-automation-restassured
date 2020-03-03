
package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;
import com.sentieo.utils.JSONUtils;

public class HeartbeatMonitors extends APIDriverHeartbeat {

	APIResponse apiResp = null;
	Response resp = null;
	static String note_id = "";
	JSONUtils jsonUtils = null;
	static String noteID_Thesis="";
	
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
		
		users.put(Team.FIN.toString(), " @sanjay @bhaskar ");
		//users.put(Team.Search.toString(), " @devesh @atish ");
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch graph data")
	public void fetchGraphDataYearlyEstimate() throws Exception {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_GRAPH_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
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
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray getSeries = respJson.getJSONObject("result").getJSONArray("series");
			if (getSeries.length() == 0 || getSeries == null)
				Assert.assertTrue(false, "Series array is empty");
			for (int i = 0; i < getSeries.length(); i++) {
				String path = "result." + "series" + "[" + i + "]." + "title";
				String seriesTitle = String.valueOf(apiResp.getNodeValue(path));
				if (seriesTitle.contains("Total Revenue-2021")) {
					JSONArray revenue2021Estimates = getSeries.getJSONObject(i).getJSONArray("series");
					if (revenue2021Estimates.length() == 0 || revenue2021Estimates == null)
						Assert.assertTrue(false, "revenue2021Estimates array is empty : ");
				}
			}
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company status")
	public void fetchCompamyStatus() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_COMPANY_STATUS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			String stock_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("stock_flag").toString();
			if (!stock_flag.contains("true"))
				assertTrue(false,"Stock flag should be true : ");
			String ih_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("ih_flag").toString();
			if (!ih_flag.contains("true"))
				assertTrue(false,"ih_flag should be true : ");
			String report_currency = respJson.getJSONObject("result").getJSONObject(ticker).get("report_currency")
					.toString();
			if (!report_currency.contains("usd"))
				assertTrue(false,"report_currency should be in usd : ");
			String trading_status = respJson.getJSONObject("result").getJSONObject(ticker).get("trading_status")
					.toString();
			if (!trading_status.contains("Success"))
				assertTrue(false,"Trading status contains success : ");
			String allowed_edt = respJson.getJSONObject("result").getJSONObject(ticker).get("allowed_edt").toString();
			if (!allowed_edt.contains("true"))
				assertTrue(false, "allowed_edt should be true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company header data")
	public void fetchCompamyHeaderData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray intradaySeries = respJson.getJSONObject("result").getJSONArray("intraday");
			JSONObject fin_summary = respJson.getJSONObject("result").getJSONObject("fin_summary");
			if (intradaySeries.length() == 0 || intradaySeries == null)
				assertTrue(false);
			if (fin_summary.length() == 0 || fin_summary == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check company summary table")
	public void companySummaryTable() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_COMPANY_SUMMARY_TABLE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
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
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchLivePrice() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_LIVE_PRICE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONObject result = respJson.getJSONObject("result").getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "Fin","currentstock" }, description = "Check fetch live price")
	public void fetchCurrentSTockData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_CURRENT_STOCK_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray result = respJson.getJSONObject("result").getJSONArray("yearly");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetchscreenersearch")
	public void fetchscreenersearch() throws Exception {
		Team team = Team.FIN;
		HashMap<String, String> parameters = new HashMap<String, String>();
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
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONObject values = respJson.getJSONObject("result");
			if (values == null || values.length() == 0 || values.toString().equals("{}"))
				assertTrue(false);
			JSONArray tickers = values.getJSONArray("tickers");
			if (tickers == null || tickers.length() == 0 || tickers.toString().equals("{}"))
				assertTrue(false);

			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchScreenerModels() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = USER_APP_URL + FETCH_SCREENER_MODELS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ptype", "screener_models");
			parameters.put("fetch_mode", "all");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray result = respJson.getJSONArray("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "FIN", "setting" }, description = "Check fetch live price")
	public void fetchFINSettings() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = USER_APP_URL + FIN_SETTINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONObject result = respJson.getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "FIN", "metadata" }, description = "Check fetch matadata")
	public void fetchMetaData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + METADATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONObject result = respJson.getJSONObject("result").getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchSearch() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			parameters.put("query", "sales");
			parameters.put("filters", "{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			parameters.put("facets_flag", "false");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchUnifiedStreamAllDocs() throws CoreCommonException {
		Team team = Team.Stream;
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		List<String> docType = new ArrayList<String>();
		HashMap<String, String> parameters = new HashMap<String, String>();
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
				Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
				Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			}
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "comparable_search")
	public void comparablesearch() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + COMPARABLE_SEARCH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			parameters.put("pagetype", "company");
			parameters.put("currency", "usd");
			parameters.put("model_id", "company");
			parameters.put("init", "1");
			parameters.put("rival", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(COMPARABLE_SEARCH, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "FETCH_NEW_MODEL_DATA")
	public void fetchNewModelData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_NEW_MODEL_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("model_source", "vpt");
			parameters.put("ticker", "aapl");
			parameters.put("ptype", "fq");
			parameters.put("report_currency", "usd");
			parameters.put("units", "T");
			parameters.put("historical_periods", "3year");
			parameters.put("forecast_periods", "3year");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(FETCH_NEW_MODEL_DATA, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetchmaingraph")
	public void fetchMainGraphComparables() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_MAIN_GRAPH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(FETCH_MAIN_GRAPH, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_transform_doc_content")
	public void fetch_transform_doc_content() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_TRANSFORM_DOC_CONTENT;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("id", "5b646fcc6681140bcb000c8f");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_snippets")
	public void fetch_snippets() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SNIPPETS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("doc_id", "5dbab2426681142318000d6c");
			parameters.put("tickers", "aapl");
			parameters.put("query", "sales");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_sections")
	public void fetch_sections() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SECTIONS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("counter", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(APP_URL + FETCH_SECTIONS, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_searchlibrary")
	public void fetch_searchlibrary() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCHLIBRARY;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("counter", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(APP_URL + FETCH_SEARCHLIBRARY, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_search_term_count")
	public void fetch_search_term_count() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCH_TERM_COUNT;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("size", "30");
			parameters.put("tickers", "msft");
			parameters.put("query", "sales");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetch_note_doc")
	public void fetch_note_doc() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_NOTE_DOC;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("doc_id", "5db6f8629ce7ad786e001a75");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void fetchFirewall() throws Exception {
		String URI = USER_APP_URL + FETCH_FIREWALL_TEST;
		Team team = Team.User;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, null);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void fetchTickerQuote() throws Exception {
		Team team = Team.User;
		String URI = APP_URL + FETCH_TICKER_QUOTE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void checkDomain() throws Exception {
		Team team = Team.User;
		String URI = APP_URL + CHECK_DOMAIN;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("email", EMAIL);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "User", "settingsTicker" }, description = "check ticker settings")
	public void checkTickerSettings() throws Exception {
		Team team = Team.User;
		String URI = USER_APP_URL + CHECK_TICKER_SETTINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			parameters.put("new_wl", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject series = respJson.getJSONObject("result").getJSONObject("result").getJSONObject(ticker);
			if (series.length() <= 2 || series == null)
				assertTrue(false);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "User", "user-watchlistData" }, description = "get user watchlist data")
	public void getUserWatchlistsData() throws Exception {
		Team team = Team.User;
		String URI = USER_APP_URL + GET_USER_WATCHLIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200, "Incorrect response code");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONArray individual_watchlist = respJson.getJSONObject("result").getJSONArray("individual_watchlist");
			if (individual_watchlist.length() == 0 || individual_watchlist == null)
				assertTrue(false);
			JSONArray all_tickers = respJson.getJSONObject("result").getJSONArray("all_tickers");
			if (all_tickers.length() == 0 || all_tickers == null)
				assertTrue(false);
		} catch (Error e) {
			//
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.toString());
			Assert.fail();
		} catch (Exception e) {
			//
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "User", "user-watchlist" }, description = "get user watchlist data")
	public void getUserWatchlists() throws Exception {
		Team team = Team.User;
		String URI = USER_APP_URL + FETCH_USERS_WATCHLIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("new_wl", "true");
			parameters.put("counter", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject watchlists = respJson.getJSONObject("result");
			if (watchlists.length() == 0 || watchlists == null)
				assertTrue(false);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(groups = { "User", "user-portfolio" }, description = "get user watchlist data")
	public void userPortFolio() throws Exception {
		Team team = Team.User;
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONArray watchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
			if (watchlists.length() == 0 || watchlists == null)
				assertTrue(false);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	
	@Test(groups = { "heart-beat", "ticker" }, description = "get gtrends for ticker")
	public void getGtrendsForTicker() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + GET_GTRENDS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			parameters.put("termtype", "ticker");
			parameters.put("type", "normal");
			parameters.put("rts", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONArray series = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
					.getJSONArray("series");
			if (series.length() == 0 || series == null)
				assertTrue(false);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "get gtrends for ticker")
	public void getGtrendsForQuery() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + GET_GTRENDS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("query_name",
					"iPhone + iPad + Apple TV + Apple Watch + iPod + iMac + AirPods + Apple Music");
			parameters.put("rts", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void siScoresTable() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + SCORES_TABLE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gnipSearchEstimate() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + GNIP_SEARCH_ESTIMATE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {

			parameters.put("ticker", "aapl");
			parameters.put("query",
					"ipad , Airpods , \"apple watch\" OR applewatch , \"apple tv\" OR appletv , \"Apple Music\" OR applemusic , iphone");
			parameters.put("pagetype", "mosaic");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gnipAlexa() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + ALEXA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("url", "apple.com");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void gtrendsAutocomplete() throws Exception {
		Team team = Team.Graph;
		String URI = APP_URL + GTRENDSAUTOCOMPLETE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("word", "apple.com");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "verify mosaic summary data")
	public void mosaicSummaryData() throws CoreCommonException {
		Team team = Team.Graph;
		JSONArray revenuSeries = null;
		JSONArray kpiSeries = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + NEW_FETCH_MOSAIC_SUMMARY_DATA;
		try {
			parameters.put("selection", "Revenue_corrScore");
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			HeartbeatMonitors.updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String selection = respJson.getJSONObject("result").getString("selection").toString().trim();
			if (selection.contains("Revenue_corrScore_all")) {
				revenuSeries = respJson.getJSONObject("result").getJSONArray("Revenue").getJSONObject(0)
						.getJSONArray("series");
				if (revenuSeries.length() == 0 || revenuSeries == null)
					assertTrue(false,"Revenue_corrScore_all is empty : ");
			} else {
				kpiSeries = respJson.getJSONObject("result").getJSONArray("KPI").getJSONObject(0)
						.getJSONArray("series");
				if (kpiSeries.length() == 0 || kpiSeries == null)
					assertTrue(false,"kpiSeries is empty : ");
			}
			JSONArray sentieoIndex = respJson.getJSONObject("result").getJSONArray("sentieoIndex").getJSONObject(0)
					.getJSONArray("series");
			if (sentieoIndex.length() == 0 || sentieoIndex == null)
				assertTrue(false, "sentieoIndex is empty : ");
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Check unfied tracker table")
	public void fetchUnifiedTable() throws CoreCommonException {
		Team team = Team.Graph;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + UNIFIEDTRACKERTABLE;
		try {
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			HeartbeatMonitors.updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray main = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("main");
			if (main.length() == 0 || main == null)
				assertTrue(false,"Main array is empty : ");
			JSONArray data = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("data");
			if (data.length() == 0 || data == null)
				assertTrue(false,"data array is empty : ");
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	//@Test(groups = { "Graph", "tracker" }, description = "Check  tracker table")
	public void fetchTrackerTable() throws CoreCommonException {
		Team team = Team.Graph;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + FETCHTRACKERTABLE;
		try {
			parameters.put("tracker_type", "unified");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			HeartbeatMonitors.updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray main = respJson.getJSONObject("result").getJSONArray("data");
			if (main.length() == 0 || main == null)
				assertTrue(false,"Main array is empty : ");
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch_institutional_holdings_data3")
	public void fetchinstitutionalholdingsdata() throws Exception {

		Team team = Team.Shareholders;
		String URI = APP_URL + FETCH_HOLDINGS_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			parameters.put("period", "2019-12-31");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch_institutional_holdings_snapshot")
	public void fetchinstitutionalholdings_snapshot() throws Exception {

		Team team = Team.Shareholders;
		String URI = APP_URL + FETCH_HOLDINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();

		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}

	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch_company_events")
	public void fetchcompanyevents() throws Exception {

		Team team = Team.FIN;
		String URI = APP_URL + FETCH_COMPANY_EVENTS;
		HashMap<String, String> parameters = new HashMap<String, String>();

		try {
			parameters.put("ticker", "aapl");
			parameters.put("size", "40");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetchfieldsinfo")
	public void fetchfieldsinfo() throws Exception {

		Team team = Team.General;
		String URI = APP_URL + FETCH_FIELDS_INFO;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("source_fields",
					"[\"2017_CY_S_curratio\",\"FQ_Q_S_ITLI-std\",\"FY-1_Y_S_sales\",\"FY_Y_S_gross_profit\",\"2014_Y_S_ev_ebitda\",\"year_change\",\"year_2_change\",\"year_change\",\"year_2_change\",\"FY_Y_S_mkt_cap\",\"FY_Y_S_mkt_cap\",\"01-Aug-2018_open_current_price\",\"01-Aug-2018_close_current_price\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"2017_Y_S_ebit\",\"mos_unified_jul-2018_m_yoy\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"FY-1_Y_S_ebit\",\"mos_unified_may-2018_m_yoy\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"2017_Y_S_ebit\",\"mos_unified_may-2018_m_yoy\",\"FY-1_Y_S_gross_profit\",\"FY-1_Y_S_sales\",\"FY-1_Y_S_ebit\",\"mos_unified_rsqd\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"mos_unified_fq1_yoy\",\"2017_Y_S_ebitda\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"03-11-2019_D_S_cogs####ltm#\",\"03-11-2019_D_S_sales####ltm#\",\"03-11-2019_D_S_cogs####ltm#\",\"03-11-2019_D_S_sales####ltm#\",\"FY_Y_S_sga\",\"FY_Y_S_sales\",\"FY-1_Y_S_ERAD-std\",\"employee_count\",\"FY-1_Y_S_sales\"]");
			parameters.put("currency", "usd");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch Insider")
	public void fetchInsider() throws Exception {

		Team team = Team.Shareholders;
		String URI = APP_URL + FETCHINSIDER;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch Analyst Recommend")
	public void fetchAnalystRecommend() throws Exception {

		Team team = Team.General;
		String URI = APP_URL + FETCHANALYSTRECOMMEND;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch ETF holdings")
	public void fetchETFHoldings() throws Exception {

		Team team = Team.Shareholders;
		String URI = APP_URL + FETCHETFHOLDINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "spy");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch Calendar")
	public void fetchCalendar() throws Exception {

		Team team = Team.Calendar;
		String URI = USER_APP_URL + FETCHCALENDAR;
		HashMap<String, String> parameters = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		int current_month = cal.get(Calendar.MONTH)+1;
		int last_date = cal.getActualMaximum(Calendar.DATE);
		int year = Calendar.getInstance().get(Calendar.YEAR); 
		try {
			parameters.put("watch", "All Watchlist Tickers");
			parameters.put("startDate",year+"-"+ current_month + "-1");
			parameters.put("endDate",year+"-"+current_month+"-"+last_date);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch trading ratio")
	public void fetchTradingRatio() throws Exception {

		Team team = Team.FIN;
		String URI = APP_URL + FETCH_TRADING_RATIO;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");

			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch Share holders")
	public void fetchShareHolders() throws Exception {

		Team team = Team.Shareholders;
		String URI = APP_URL + FETCHSHAREHOLDERS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			parameters.put("sColumns",
					"institution,Type,value,sharesPrn,percShares,percPortfolio,change,percChange,putCall,filingDate,source");

			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "fetch Company return")
	public void fetchCompanyReturn() throws Exception {

		Team team = Team.FIN;
		String URI = APP_URL + GET_COMPANY_RETURN;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");

			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(groups = { "heart-beat"}, description = "used to save the note or create a note",priority = 0)
	public void setNotehtml() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + SET_NOTE_HTML;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String tempId = "quill" + new Date().getTime();
			parameters.put("ts", tempId);
			parameters.put("title", "privateApiNote" + new Date());
			parameters.put("private_note", "true");
			parameters.put("version", "1");
			parameters.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
			assertTrue(respJson.getJSONObject("result").getString("temp_id").equalsIgnoreCase(tempId),"Temp id should not be blank : ");
			assertTrue(respJson.getJSONObject("result").getString("id")!=null,"Note id should not be blank : ");
			note_id=respJson.getJSONObject("result").getString("id");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(groups = { "heart-beat"}, description = "Fetch user notes",priority = 1)
	public void fetchNoteList() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + FETCH_NOTE_LIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("filter", "all");
			parameters.put("mode","all");
			parameters.put("order", "note_updated_date:desc");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
			assertTrue(respJson.getJSONObject("result").getInt("total")>0,"Total should be greater than zero : ");
			JSONArray notesData = respJson.getJSONObject("result").getJSONArray("notes");
			if(notesData.length() == 0 || notesData == null)
				assertTrue(false,"note data array is empty : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(groups = { "heart-beat"}, description = "Fetch user note history",priority = 2)
	public void fetchNoteHistory() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + FETCH_NOTE_HISTORY;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("id", note_id);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI,spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
			assertTrue(respJson.getJSONObject("result").getInt("total_versions")>=1,"Total version should be greater than zero : ");
			JSONArray historyData = respJson.getJSONObject("result").getJSONArray("history");
			if(historyData.length() == 0 || historyData == null)
				assertTrue(false,"history array is empty : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
		
	@Test(groups = { "heart-beat"}, description = "delete a note",priority = 7)
	public void deleteNote() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + DELETE_NOTE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("note_id", note_id);
			RequestSpecification spec1 = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec1, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}
	
	@Test(groups = { "heart-beat"}, description = "Fetch user notebook data",priority = 3)
	public void fetchNotebookData() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + FETCH_NOTE_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try { 
			parameters.put("note_type","true");
			parameters.put("user_template","true");
			parameters.put("user_groups", "true");
			parameters.put("user_fields", "true");
			parameters.put("user_email", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
			JSONArray noteType = respJson.getJSONObject("result").getJSONObject("note_type").getJSONArray("static_note_type_list");
			if(noteType.length() == 0 || noteType == null)
				assertTrue(false,"note type array is empty : ");
			JSONArray user_template = respJson.getJSONObject("result").getJSONArray("user_template");
			if(user_template.length() == 0 || user_template == null)
				assertTrue(false,"user template array is empty : ");
			JSONArray user_groups = respJson.getJSONObject("result").getJSONArray("user_groups");
			if(user_groups.length() == 0 || user_groups == null)
				assertTrue(false,"user group array is empty : ");
//			JSONObject user_fields = respJson.getJSONObject("result").getJSONObject("user_fields");
//			if(user_fields.length() == 0 || user_fields == null)
//				assertTrue(false,"user fields array is empty");
			JSONArray user_email = respJson.getJSONObject("result").getJSONArray("user_email");
			if(user_email.length() == 0 || user_email == null)
				assertTrue(false,"user email array is empty : ");
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		} catch (Exception e) {
			
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			Assert.fail();
		}
	}

		@Test(groups = { "heart-beat"}, description = "Fetch note data",priority = 4)
		public void fetchNoteHtml() throws Exception {
			Team team = Team.Notebook;
			String URI = USER_APP_URL + FETCH_NOTE_HTML;
			HashMap<String, String> parameters = new HashMap<String, String>();
			try {
				parameters.put("id",note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
				assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status");
				assertTrue(respJson.getJSONObject("result").getString("id").equalsIgnoreCase(note_id),"Note id should be equal to fetched note id : ");
				assertTrue(respJson.getJSONObject("result").getString("url")!=null,"Url should not be blank : ");
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			} catch (Error e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			} catch (Exception e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			}
		}
		
		//@Test(groups = { "heart-beat"}, description = "used to create a thesis or update a thesis",priority = 5)
		public void thesisEntity() throws Exception {
			Team team = Team.Notebook;
			String URI = USER_APP_URL + THESIS_ENTITY;
			jsonUtils = new JSONUtils();
			HashMap<String, String> parameters = new HashMap<String, String>();
			HashMap<String, String> thesisData = new HashMap<String, String>();
			try {
				thesisData.put("thesis_type", "thesis");
				thesisData.put("tickers", "dfkcy");
				thesisData.put("name", "DFKCY Thesis");
				thesisData.put("tab_name", "Overview");

				String dataJson = jsonUtils.toJson(thesisData);
				parameters.put("action", "create_thesis_and_tab");
				parameters.put("thesis_dictionary", dataJson);
				parameters.put("create_default_children", Boolean.TRUE.toString());
				
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
				assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
				assertTrue(respJson.getJSONObject("result").getInt("status")==1,"Verify result status should be 1 : ");
				noteID_Thesis = respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0).getString("note_id");
				if(noteID_Thesis==null)
					assertTrue(false,"note id is blank : ");
				assert respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0).getString("thesis_type").equalsIgnoreCase("thesis");
				updatePassResult(URI, team.toString(), "200", resp, parameters);
				
				// delete note
				HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
				deleteNoteParams.put("note_id", noteID_Thesis);
				RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
				RestOperationUtils.post(USER_APP_URL + DELETE_NOTE, null, spec1, deleteNoteParams);				
			} catch (Error e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			} catch (Exception e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			}
		}	
	
		@Test(groups = { "heart-beat","devesh"}, description = "This will load the L1(filter section)",priority = 6)
		public void fetchNoteFacetHtml() throws Exception {
			Team team = Team.Notebook;
			String URI = USER_APP_URL + FETCH_NOTE_FACET_AND_HTML;
			HashMap<String, String> parameters = new HashMap<String, String>();
			try { 
				parameters.put("notemode","all");
				parameters.put("type","all");
				parameters.put("all_contacts", "true");
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
				assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status : ");
				JSONArray ticker_term = respJson.getJSONObject("result").getJSONObject("facets").getJSONObject("tickers").getJSONArray("terms");
				if(ticker_term.length() == 0 || ticker_term == null)
					assertTrue(false,"ticker_term array is empty");
				JSONObject user_fields = respJson.getJSONObject("result").getJSONObject("facets");
				if(user_fields.length() == 0 || user_fields == null)
					assertTrue(false,"user_fields array is empty");
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			} catch (Error e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			} catch (Exception e) {
				
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				Assert.fail();
			}
		}
	
		
	@AfterClass(alwaysRun = true)
	public void generateHTML() {
		String content = readHTMLHeader() + sbFail.toString() + sbPass.toString() + readHTMLFooter();
		try {
			FileUtils.deleteQuietly(new File("heartbeat.html"));
			Files.write(Paths.get("heartbeat.html"), content.getBytes(), StandardOpenOption.CREATE);
			
			StringBuffer sb = new StringBuffer();
			String[] failureData = (String[]) failedAPIData.toArray(new String[failedAPIData.size()]);
			for (String s : failureData) {
				sb.append(s);
				sb.append(System.lineSeparator());
			}
			FileUtils.deleteQuietly(new File("failuresummary.txt"));
			Files.write(Paths.get("failuresummary.txt"), sb.toString().getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			
		}
	}
}
