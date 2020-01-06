package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
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

public class HeartbeatMonitors extends APIDriverHeartbeat {
	
	APIResponse apiResp = null;
	Response resp = null;

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
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} 
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch company status")
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
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} 
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch company header data")
	public void fetchCompamyHeaderData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_COMPANY_STATUS;
		HashMap<String, String> parameters = new HashMap<String, String>();
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
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "Check company summary table")
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
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch live price")
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
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONObject result = respJson.getJSONObject("result").getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "fetchscreenersearch")
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
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONObject values = respJson.getJSONObject("result");
			if (values == null || values.length() == 0 || values.toString().equals("{}"))
				assertTrue(false);
			JSONArray tickers = values.getJSONArray("tickers");
			if (tickers == null || tickers.length() == 0 || tickers.toString().equals("{}"))
				assertTrue(false);

			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch live price")
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
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			JSONArray result = respJson.getJSONArray("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchSearch() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			parameters.put("query", "");
			parameters.put("filing_type", "companys sales OR sales");
			parameters.put("original_query", "companys sales OR sales");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	//@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchUnifiedStreamAllDocs() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		List<String>docType=new ArrayList<String>();
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
			for(int i=0;i<docType.size();i++)
			{
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
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	
	@Test(groups = { "heart-beat" }, description = "comparable_search")
	public void comparablesearch() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + COMPARABLE_SEARCH;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		try {
		queryParams.put("tickers", "aapl");
		queryParams.put("pagetype", "company");
		queryParams.put("currency", "usd");
		queryParams.put("model_id", "company");
		queryParams.put("init", "1");
		queryParams.put("rival", "1");
		RequestSpecification spec = formParamsSpec(queryParams);
		resp = RestOperationUtils.post(COMPARABLE_SEARCH, null, spec, queryParams);
		apiResp = new APIResponse(resp);
		assert apiResp.getStatusCode() == 200;
		updatePassResult(URI, team.toString(), "200", resp, queryParams);
		} catch (Exception e) {
				e.printStackTrace();
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, queryParams);
				Assert.fail();
			}
	}

	//@Test(groups = { "heart-beat" }, description = "FETCH_NEW_MODEL_DATA")
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
		assert apiResp.getStatusCode() == 200;
		assert respJson.getJSONObject("response").getBoolean("status") == true;
		updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	
	//@Test(groups = { "heart-beat" }, description = "fetchmaingraph")
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
		assert apiResp.getStatusCode() == 200;
		assert respJson.getJSONObject("response").getBoolean("status") == true;
		updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	
	//@Test(groups = { "heart-beat" }, description = "fetch_transform_doc_content")
	public void fetch_transform_doc_content() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_TRANSFORM_DOC_CONTENT;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("id", "5b646fcc6681140bcb000c8f");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
		 	apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	//@Test(groups = { "heart-beat" }, description = "fetch_snippets")
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
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	
	//@Test(groups = { "heart-beat" }, description = "fetch_sections")
	public void fetch_sections() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SECTIONS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("counter", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(APP_URL + FETCH_SECTIONS, spec, parameters);
		 	apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	
	//@Test(groups = { "heart-beat" }, description = "fetch_searchlibrary")
	public void fetch_searchlibrary() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCHLIBRARY;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("counter", "1");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(APP_URL + FETCH_SEARCHLIBRARY, spec, parameters);
		 	apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	//@Test(groups = { "heart-beat" }, description = "fetch_search_term_count")
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
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
	
	//@Test(groups = { "heart-beat" }, description = "fetch_note_doc")
	public void fetch_note_doc() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_NOTE_DOC;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("doc_id", "5db6f8629ce7ad786e001a75");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
		 	apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
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
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, null);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	
	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void getGtrends() throws Exception {
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
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
			assert apiResp.getStatusCode() == 200;
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}

	
	
	
	@AfterClass (alwaysRun=true)
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
