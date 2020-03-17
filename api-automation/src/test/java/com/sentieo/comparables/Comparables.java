package com.sentieo.comparables;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.finance.InputTicker;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class Comparables extends APIDriver {

	APIAssertions verify = new APIAssertions();
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

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

	@Test(groups = "sanity", description = "fetchscreenermodels")
	public void fetchscreenermodels() throws Exception {
		String URI = USER_APP_URL + FETCH_SCREENER_MODELS;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ptype", "risk_reward_models");
		queryParams.put("fetch_mode", "all");
		RequestSpecification spec = queryParamsSpec(queryParams);
		Response resp = RestOperationUtils.get(URI, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}
	

	@Test(groups = { "comp"}, description = "Check fetch live price")
	public void fetchFINSettings() throws CoreCommonException {
		String URI = USER_APP_URL + FIN_SETTINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result");
			if (result.length() == 0 || result == null)
				assertTrue(false);
			else
			{
				JSONArray plotter_features = result.getJSONArray("plotter_features");
				JSONObject options = result.getJSONObject("options");
				verify.assertTrue(plotter_features.length()!=0||plotter_features!=null, "verify plotter features");
				verify.assertTrue(options.length()!=0||options!=null, "verify options");
			}
		} catch (Error e) {
			
		}
		finally {
			verify.verifyAll();
		}
	}


	@Test(groups = "sanity", description = "comparable_search")
	public void comparablesearch() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				queryParams.put("tickers", cell);
				queryParams.put("pagetype", "company");
				queryParams.put("currency", "usd");
				queryParams.put("model_id", "company");
				queryParams.put("init", "1");
				queryParams.put("rival", "1");
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(COMPARABLE_SEARCH, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "managementinfo")
	public void managementinfo() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {

				queryParams.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(MANAGEMENT_INFO, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_institutional_holdings_snapshot")
	public void fetchinstitutionalholdings_snapshot() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				queryParams.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(FETCH_HOLDINGS, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_main_graph")
	public void fetchmaingraph() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				queryParams.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(FETCH_MAIN_GRAPH, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_company_events")
	public void fetchcompanyevents() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				queryParams.put("ticker", cell);
				queryParams.put("size", "40");
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(FETCH_COMPANY_EVENTS, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "managementinfo_new")
	public void managementinfo_new() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				queryParams.put("ticker", cell);
				RequestSpecification spec = queryParamsSpec(queryParams);
				Response resp = RestOperationUtils.get(MANAGEMENT_INFO_NEW, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
			}
		}
		verify.verifyAll();
	}
	@Test(groups = "sanity", description = "fetchscreenersearch")
	public void fetchscreenersearch() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("tickers", "aapl");
		queryParams.put("pagetype", "screener");
		queryParams.put("type", "company");
		queryParams.put("sort", "FY_Y_S_mkt_cap:desc");
		queryParams.put("currency", "usd");
		queryParams.put("screener_search_settings", "{\"unique_str\":\"\",\"page_nav\":0,\"page_sort\":0}");

		RequestSpecification spec = formParamsSpec(queryParams);

		Response resp = RestOperationUtils.post(APP_URL + FETCH_SCREENER_SEARCH, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		JSONObject values = respJson.getJSONObject("result");
		JSONObject ticker_currency = values.getJSONObject("ticker_currency");
		JSONArray ticker = values.getJSONArray("tickers");
		JSONObject fields_info = values.getJSONObject("fields_info");
		if (values.length() == 0)
			verify.verifyTrue(false, "verify result : ");
		if (ticker.length() == 0)
			verify.verifyTrue(false, "verify tickers : ");
		if (ticker_currency.length() == 0)
			verify.verifyTrue(false, "verify ticker :  ");
		if (fields_info.length() == 0)
			verify.verifyTrue(false, "verify fields info :  ");

		verify.verifyAll();
	}
}
