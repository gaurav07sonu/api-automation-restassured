package com.sentieo.comparables;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class Comparables extends APIDriver {

	APIAssertions verify = new APIAssertions();
	
	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

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

	@Test(groups = "sanity", description = "comparable_search")
	public void comparablesearch() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("tickers", "aapl");
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
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "managementinfo")
	public void managementinfo() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ticker", "aapl");
		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(MANAGEMENT_INFO, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_institutional_holdings_snapshot")
	public void fetchinstitutionalholdings_snapshot() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ticker", "aapl");
		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(FETCH_HOLDINGS, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_main_graph")
	public void fetchmaingraph() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ticker", "aapl");
		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(FETCH_MAIN_GRAPH, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_company_events")
	public void fetchcompanyevents() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ticker", "aapl");
		queryParams.put("size", "40");
		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(FETCH_COMPANY_EVENTS, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "managementinfo_new")
	public void managementinfo_new() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ticker", "aapl");
		RequestSpecification spec = queryParamsSpec(queryParams);
		Response resp = RestOperationUtils.get(MANAGEMENT_INFO_NEW, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyAll();
	}
}
