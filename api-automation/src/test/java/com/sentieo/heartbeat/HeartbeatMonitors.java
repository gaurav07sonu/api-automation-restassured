package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;

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
	HashMap<String, String> parameters = new HashMap<String, String>();
	protected static final Reporter reporter = Reporter.getInstance();
	StringBuffer sbPass = new StringBuffer();
	StringBuffer sbFail = new StringBuffer();
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company status")
	public void fetchCompamyStatus() throws CoreCommonException {
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company header data")
	public void fetchCompamyHeaderData() throws CoreCommonException {
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check company summary table")
	public void companySummaryTable() throws CoreCommonException {
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_COMPANY_SUMMARY_TABLE;
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchLivePrice() throws CoreCommonException {
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_LIVE_PRICE;
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "fetchscreenersearch")
	public void fetchscreenersearch() throws Exception {
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

			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchScreenerModels() throws CoreCommonException {
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
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchSearch() throws CoreCommonException {
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
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			assert apiResp.getStatusCode() == 200;
			assert respJson.getJSONObject("response").getBoolean("status") == true;
			updatePassResult(URI, "200", resp);
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchUnifiedStreamAllDocs() throws CoreCommonException {
		APIResponse apiResp = null;
		Response resp = null;
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		List<String>docType=new ArrayList<String>();
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
			updatePassResult(URI, "200", resp);
			}
		} catch (AssertionError e) {
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONParseException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (JSONException e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
		} catch (Exception e) {
			e.printStackTrace();
			updateFailResult(URI, String.valueOf(apiResp.getStatusCode()), resp);
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
		sbPass.append(reporter.generateFormatedResponse(resp));
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
		sbPass.append(reporter.generateFormatedResponse(resp));
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

	@AfterClass(alwaysRun = true)
	public void generateHTML() {
		String content = header + sbFail.toString() + sbPass.toString() + footer;
		try {
			Files.write(Paths.get("heartbeat.html"), content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
