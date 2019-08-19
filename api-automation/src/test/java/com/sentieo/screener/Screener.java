package com.sentieo.screener;

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

public class Screener extends APIDriver {

	APIAssertions verify = new APIAssertions();

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

		RestAssured.baseURI = USER_APP_URL;
	}
	
	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "fetchscreenermodels")
	public void fetchscreenermodels() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("ptype", "screener_models");
		queryParams.put("fetch_mode", "all");
		RequestSpecification spec = queryParamsSpec(queryParams);

		Response resp = RestOperationUtils.get(FETCH_SCREENER_MODELS, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetchscreenfilters")
	public void fetchscreenfilters() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("fetch_mode", "dropdown");
		RequestSpecification spec = queryParamsSpec(queryParams);

		Response resp = RestOperationUtils.get(FETCH_SCREEN_FILTERS, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetchscreenerparamusage")
	public void fetchscreenerparamusage() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("mode", "[\"most_recent\",\"most_frequent\"]");

		RequestSpecification spec = queryParamsSpec(queryParams);

		Response resp = RestOperationUtils.get(FETCH_SCREENER_PARAM_USAGE, spec, queryParams);

		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyResponseTime(resp, 10000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetchfieldsinfo")
	public void fetchfieldsinfo() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("source_fields",
				"[\"2017_CY_S_curratio\",\"FQ_Q_S_ITLI-std\",\"FY-1_Y_S_sales\",\"FY_Y_S_gross_profit\",\"2014_Y_S_ev_ebitda\",\"year_change\",\"year_2_change\",\"year_change\",\"year_2_change\",\"FY_Y_S_mkt_cap\",\"FY_Y_S_mkt_cap\",\"01-Aug-2018_open_current_price\",\"01-Aug-2018_close_current_price\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"2017_Y_S_ebit\",\"mos_unified_jul-2018_m_yoy\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"FY-1_Y_S_ebit\",\"mos_unified_may-2018_m_yoy\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"2017_Y_S_ebit\",\"mos_unified_may-2018_m_yoy\",\"FY-1_Y_S_gross_profit\",\"FY-1_Y_S_sales\",\"FY-1_Y_S_ebit\",\"mos_unified_rsqd\",\"FY-1_Y_S_sales\",\"FY-1_CY_S_gross_profit\",\"mos_unified_fq1_yoy\",\"2017_Y_S_ebitda\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"FY_Y_S_sales\",\"FY-1_Y_S_sales\",\"03-11-2019_D_S_cogs####ltm#\",\"03-11-2019_D_S_sales####ltm#\",\"03-11-2019_D_S_cogs####ltm#\",\"03-11-2019_D_S_sales####ltm#\",\"FY_Y_S_sga\",\"FY_Y_S_sales\",\"FY-1_Y_S_ERAD-std\",\"employee_count\",\"FY-1_Y_S_sales\"]");
		queryParams.put("currency", "cad");
		RequestSpecification spec = formParamsSpec(queryParams);

		Response resp = RestOperationUtils.post(APP_URL + FETCH_FIELDS_INFO, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();

	}

	@Test(groups = "sanity", description = "fetchscreenersearch")
	public void fetchscreenersearch() throws Exception {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("source_fields",
				"[\"name\",\"mos_unified_fq1_accel\",\"mos_gtrends_fq1_accel\",\"mos_alexa_uv_fq1_accel\",\"mos_gnip_fq1_accel\",\"mos_revenue_fq1_accel\",\"FQ_Q_S_enddt\",\"mos_unified_revenue_rsqd\",\"12-4-2018_D_R_LTCL-std####ltm\",\"12-4-2018_D_R_LBDT-std####ltm\",\"12-4-2018_D_R_LTXP-std####ltm\",\"12-16-2018_D_S_roic####ltm\",\"12-16-2018_D_S_gross_profit####ltm\",\"12-16-2018_D_S_QRED-std####ltm\",\"12-16-2018_D_S_opex_adj####ltm\",\"mos_tsentiments_management_FQ0_base\",\"12-16-2018_D_S_ebit_intexp####ltm\",\"12-16-2018_D_S_VDEP-std####ltm\",\"12-16-2018_D_S_APPY-std####ltm\",\"shper\",\"1d_RD_S_cps_est####ntm\",\"12-16-2018_D_S_ufcfmargin####ltm\",\"currency_stock\",\"1d_RD_S_price_cashflow####ltm\",\"1d_RD_S_p_fcf####ltm\",\"12-18-2018_D_S_SDWS####ltm\",\"1d_RD_S_ebitda-margin_est####cfy\",\"12-20-2018_D_S_taxes_adj####ltm\",\"yprice\",\"12-20-2018_D_S_quickratio####ltm\",\"12-22-2018_D_S_VDEP-std####ltm\",\"mos_unified_FQ2_yoy\",\"12-22-2018_D_S_arturn####ltm\",\"1d_RD_S_ev_fcf####ltm\",\"52wk_high\",\"12-26-2018_D_S_LCAV-std####ltm\",\"12-26-2018_D_S_shares_issue_3####ltm\",\"12-26-2018_D_S_APPY-std####ltm\",\"12-26-2018_D_S_LOCL-std####ltm\",\"12-26-2018_D_S_VRXP-std####ltm\",\"12-26-2018_D_S_ERES-std####ltm\",\"12-26-2018_D_S_ASTI-std####ltm\",\"1-1-2019_D_S_pre_tax_adj####ltm\",\"1-1-2019_D_S_shares_issue_3####ltm\",\"1-1-2019_D_S_FCSN####ltm\",\"1-1-2019_D_S_quickratio####ltm\"]");
		queryParams.put("screener_search_settings", "{\"unique_str\":\"\",\"page_nav\":0,\"page_sort\":0}");
		queryParams.put("screener_user_settings",
				"{\"exclude_adrs\":false,\"exclude_otc_symbols\":false,\"exclude_non_primary\":false,\"exclude_adrs_non_primary\":false,\"exclude_non_cash_equity_securities\":false,\"exclude_inactive_tickers\":true}");
		queryParams.put("currency", "cad");
		queryParams.put("type", "company");
		queryParams.put("sort", "FY_Y_S_mkt_cap:desc");
		queryParams.put("fetchticker", "1");
		queryParams.put("pagetype", "custom");
		queryParams.put("screener_user_settings", "");
		RequestSpecification spec = formParamsSpec(queryParams);

		Response resp = RestOperationUtils.post(APP_URL + FETCH_SCREENER_SEARCH, null, spec, queryParams);
		System.out.println(resp.asString());
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyAll();
	}
}
