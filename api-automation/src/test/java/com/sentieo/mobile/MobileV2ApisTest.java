package com.sentieo.mobile;

import static com.sentieo.constants.Constants.*;


import java.io.File;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
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
import com.sentieo.utils.CoreCommonException;

/*
 * Author - Akash
 * 
 */
public class MobileV2ApisTest extends APIDriver {
	

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		RestAssured.baseURI = USER_APP_URL;
	}
	
	@Test(groups = "mobileMainApp", description = "Save fv market")
	public void testSaveFvMarket() throws Exception {
		try {
			
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("market", "xiu:cn");
			
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL  + SAVE_FV_MARKET , null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testSaveFvMarket.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	
	
	@Test(groups = "mobileMainApp", description = "Save fv watchlist")
	public void testSaveFvWatchlist() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("fetch", "1");
			parameters.put("wid", fetchWatchlistId());
			
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + SAVE_FV_WATCHLIST , spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testSaveFvWatchlist.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "mobileMainApp", description = "Feedback")
	public void testAddFeedback() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("feedback", "testFeedback from automation");
			parameters.put("page", "iphone_v2");
			
			
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + ADD_FEEDBACK ,null,  spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testAddFeedback.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "mobileMainApp", description = "fin wei mobile api")
	public void testWeiAnalysisApi() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("feedback", "testFeedback from automation");
			parameters.put("displayBy", "ticker");
			parameters.put("main_filter_value", "xlc,soy,amlp");
			parameters.put("currency", "usd");
			parameters.put("columns_type", "price");
			parameters.put("columns_type_value", "5");
			
			
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(APP_URL + FIN_WEI_ANALYSIS_MOBILE ,null,  spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("wei_analysis_groups") != null,
					"checking is data empty");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "mobileMainApp", description = "fin market analysis mobile")
	public void testFinMarketAnalysisMobile() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("ticker", "agt");
			
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(APP_URL + FIN_MARKET_ANALYSIS_MOBILE ,null,  spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "mobileMainApp", description = "relative price vs sp")
	public void testRelativePriceVsSp() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("loc", "ios");
			parameters.put("ticker", "aapl");
			
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(APP_URL + RELATIVE_PRICEVSSP ,null,  spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.verifyTrue(respJson.getJSONArray("result")!= null, "checking result is not empty");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	public String fetchWatchlistId() throws CoreCommonException {
		try {
			String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
			HashMap<String, String> tickerData = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONArray jsonArray = respJson.getJSONObject("result").getJSONArray("watchlists");
				JSONArray list = (JSONArray) jsonArray.get(0);
				String id = (String) list.get(2);
				return id;
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} catch (CoreCommonException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}
		return null;
	}

	
}
