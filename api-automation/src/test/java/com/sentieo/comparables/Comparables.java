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
	static String VIEW = "Automation_View";
	static String cid = "";

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

	public void fetchscreenermodels(boolean status) throws Exception {
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
		JSONArray result = respJson.getJSONArray("result");
		for (int i = 0; i < result.length(); i++) {
			JSONObject objects = result.getJSONObject(i);
			String modelName = objects.getString("name");
			if (status) {
				if (modelName.equalsIgnoreCase(VIEW)) {
					cid = objects.getString("model_id");
					verify.assertTrue(true, "View created succefully");
					break;
				} else {
					if (i == result.length() - 1)
						verify.assertTrue(false, "View not created");
				}
			} else {
				if (modelName.equalsIgnoreCase(VIEW))
					verify.assertTrue(false, "View not deleted ");
				else {
					if (i == result.length() - 1)
						verify.assertTrue(true, "View  deleted ");
				}
			}

		}
	}

	 @Test(groups = { "comp"}, description = "Check fetch live price",priority=0)
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
			else {
				JSONArray plotter_features = result.getJSONArray("plotter_features");
				JSONObject options = result.getJSONObject("options");
				verify.assertTrue(plotter_features.length() != 0 || plotter_features != null,
						"verify plotter features");
				verify.assertTrue(options.length() != 0 || options != null, "verify options");
			}
		} catch (Error e) {

		} finally {
			verify.verifyAll();
		}
	}

	 @Test(groups = "sanity", description = "comparable_search",priority=1)
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

	 @Test(groups = "sanity", description = "managementinfo",priority=2)
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

	 @Test(groups = "sanity", description = "fetch_institutional_holdings_snapshot",priority=3)
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

	@Test(groups = "sanity", description = "fetch_main_graph",priority=4)
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

	@Test(groups = "sanity", description = "fetch_company_events",priority=5)
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

	@Test(groups = "sanity", description = "managementinfo_new",priority=6)
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

	@Test(groups = "sanity", description = "fetchscreenersearch",priority=7)
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

	@Test(groups = "sanity", description = "save views", priority = 8)
	public void saveRiskRewardViews() throws CoreCommonException {
		try {
			String URL = USER_APP_URL + SAVE_RISK_REWARD_VIEWS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("cid", "");
			queryParams.put("allowed", "[[\"name\",\"comp_para\"],[\"1d_RD_S_mkt_cap_daily####\""
					+ ",\"finc_para\"],[\"1d_RD_S_ev_daily####\",\"finc_para\"]"
					+ ",[\"year_change\",\"stck_para\"],[\"year_2_change\",\"stck_para\"],[\"year_5_change\",\"stck_para\"]"
					+ ",[\"1d_RD_S_p_eps####ltm\",\"finc_para\"],[\"1d_RD_S_p_eps####cfy\",\"finc_para\"],[\"1d_RD_S_ev_ebitda####ltm\",\"finc_para\"],[\"1d_RD_S_ev_ebitda####cfy\",\"finc_para\"]"
					+ ",[\"2017_CY_S_sales#pct_chg#cy2018##y\",\"finc_para\"],[\"2017_CY_S_eps#pct_chg#cy2018##y\",\"finc_para\"]]");
			queryParams.put("name", VIEW);
			queryParams.put("default", "");
			queryParams.put("ptype", "risk_reward_models");
			queryParams.put("description", "");
			queryParams.put("share_with", "[]");
			queryParams.put("share_with_group", "[]");
			queryParams.put("fetch_mode", "dropdown");

			RequestSpecification spec = formParamsSpec(queryParams);

			Response resp = RestOperationUtils.post(URL, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			boolean status = respJson.getJSONObject("response").getBoolean("status");
			if (status)
				fetchscreenermodels(true);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			verify.verifyAll();

		}

	}

	@Test(groups = "sanity", description = "delete saved views", priority = 9)
	public void updateRiskRewardViews() throws CoreCommonException {
		try {
			String URL = USER_APP_URL + UPDATE_SCREENER_VIEWS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("cid", cid);
			queryParams.put("field_value", "{\"last_open_date\":\"now\"}");

			RequestSpecification spec = formParamsSpec(queryParams);

//			Response resp = RestOperationUtils.get(URL, spec, queryParams);
			Response resp = RestOperationUtils.post(URL, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			String result = respJson.getString("result");
			verify.assertEqualsActualContainsExpected(result, cid, "verify cid");
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			verify.verifyAll();

		}

	}

	@Test(groups = "sanity", description = "delete saved views", priority = 10)
	public void deleteRiskRewardViews() throws CoreCommonException {
		try {
			String URL = USER_APP_URL + DELETE_RISK_REWARD_VIEWS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("cid", cid);
			queryParams.put("ptype", "risk_reward_models");
			queryParams.put("fetch_mode", "dropdown");

			RequestSpecification spec = formParamsSpec(queryParams);

			Response resp = RestOperationUtils.post(URL, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			boolean status = respJson.getJSONObject("response").getBoolean("status");
			if (status)
				fetchscreenermodels(false);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			verify.verifyAll();

		}

	}
}
