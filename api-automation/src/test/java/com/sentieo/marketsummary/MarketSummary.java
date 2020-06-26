package com.sentieo.marketsummary;

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
import com.sentieo.utils.CoreCommonException;

public class MarketSummary extends APIDriver {

	APIAssertions verify = new APIAssertions();
	
	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "fdsindiceschange")
	public void fdsindiceschange() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("weights", "true");
			RequestSpecification spec = queryParamsSpec(queryParams);

			Response resp = RestOperationUtils.get(FDS_INDICES_CHANGE, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

	@Test(groups = "sanity", description = "getspotfxdata")
	public void getspotfxdata() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("weights", "true");
			RequestSpecification spec = queryParamsSpec(queryParams);

			Response resp = RestOperationUtils.get(GET_SPOTFX_DATA, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

	@Test(groups = "sanity", description = "getratesapi")
	public void getratesapi() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("weights", "true");
			RequestSpecification spec = queryParamsSpec(queryParams);

			Response resp = RestOperationUtils.get(GET_RATES_API, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

	@Test(groups = "sanity", description = "indexchartapi")
	public void indexchartapi() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("weights",
					"{\"180554\":\"75.00\",\"891800\":\"93.00\",\"990100\":\"92.00\",\"990300\":\"94.00\",\"SP50\":\"90.00\",\"R.2000\":\"86.00\",\"LHMN0001\":\"34.00\",\"LHMN0735\":\"0.00\",\"NEIXCTAT00000\":\"0.00\",\"HFRXEW\":\"123.00\",\"HFRXEH\":\"10.00\",\"HFRXRVMS\":\"123.00\",\"HFRXED\":\"324.00\",\"SPLPEQTY\":\"4.50\",\"SPRAUT\":\"4.50\"}");
			queryParams.put("type", "amg-index");
			RequestSpecification spec = queryParamsSpec(queryParams);

			Response resp = RestOperationUtils.get(INDEX_CHART_API, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 9000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}
}
