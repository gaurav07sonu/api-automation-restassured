package com.sentieo.privatecompanies;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.sentieo.constants.Constants.*;
import com.sentieo.assertion.APIAssertions.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class TestFetchPvtHeaderData extends APIDriver {

	HashMap<String, String> parameters = new HashMap<String, String>();
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
		RestAssured.baseURI = APP_URL;
	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

//	@Test(groups = "sanity", description = "private tickers", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
//	public void fetchCBExitTable(String paginationRequestCount) throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_EXIT_TABLE;
//			parameters.put("num_rows", paginationRequestCount);
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONArray mappingArray = respJson.getJSONObject("result").getJSONArray("mapping");
//			for (int i = 0; i < mappingArray.length(); i++) {
//			    JSONObject json = mappingArray.getJSONObject(i);
//			    Iterator<String> keys = json.keys();
//			    while (keys.hasNext()) {
//			        String key = keys.next();
//			        if(key.contains("sort_key"))
//			        {
//			        	String seriesValue = (String) json.get(key);
//					        System.out.println("Key :" + key + "  Value :" + json.get(key));
//			        }
//			       
//			    }
//
//			}
//
//			JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("exits");
//			int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
//			int exitsArrayLength = exitsArray.length();
//			if (paginationRequestCount.equals("all")) {
//				verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
//			} else {
//				int paginationCount = Integer.parseInt(paginationRequestCount);
//				if (paginationCount > totalExitsCount) // 50>40
//					verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
//				else
//					verify.verifyEquals(paginationCount, exitsArrayLength, "");
//			}
//
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}

//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCFRTable() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_FR_TABLE;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBFRhighlights() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_FR_HIGHLIGHTS;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBFRCumulative() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_FR_cumulative;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
	@Test(groups = "sanity", description = "private tickers")
	public void fetchCBINVTable() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_CB_INV_TABLE;
			//parameters.put("num_rows", "all");
			//parameters.put("sort_key", "money_raised_usd");
			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONObject values = respJson.getJSONObject("result");
			verify.verifyAll();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new CoreCommonException(e.getMessage());
		}
	}
//
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBINVQuarterwise() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_INV_QUARTERWISE;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBINVCategory() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_INV_CATEGORY;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//	
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBKeyInvestors() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_KEY_INVESTORS;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBexitCategory() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_EXIT_CATEGORY;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//	
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBacqTable() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_ACQ_TABLE;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchCBFundsTable() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_CB_FUNDS_TABLE;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
//
//	@Test(groups = "sanity", description = "private tickers")
//	public void fetchHeaderData() throws CoreCommonException {
//		try {
//			String URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
//			parameters.put("num_rows", "all");
//			parameters.put("sort_key", "money_raised_usd");
//			parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//			RequestSpecification spec = formParamsSpec(parameters);
//			Response resp = RestOperationUtils.get(URI, spec, parameters);
//			APIResponse apiResp = new APIResponse(resp);
//			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//					"Verify the API Response Status");
//			verify.verifyResponseTime(resp, 5000);
//			JSONObject values = respJson.getJSONObject("result");
//			verify.verifyAll();
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//			throw new CoreCommonException(e.getMessage());
//		}
//	}
//
//	public void getSortKey(String paginationRequestCount) throws Exception
//	{
//		String URI = APP_URL + FETCH_CB_EXIT_TABLE;
//		parameters.put("num_rows", paginationRequestCount);
//		parameters.put("sort_key", "money_raised_usd");
//		parameters.put("ticker", "aefa3a84519300c35cbfec5ec2645672-cb:pvt");
//		RequestSpecification spec = formParamsSpec(parameters);
//		Response resp = RestOperationUtils.get(URI, spec, parameters);
//		APIResponse apiResp = new APIResponse(resp);
//		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//		JSONArray mappingArray = respJson.getJSONObject("result").getJSONArray("mapping");
//		for (int i = 0; i < mappingArray.length(); i++) {
//		    JSONObject json = mappingArray.getJSONObject(i);
//		    Iterator<String> keys = json.keys();
//		    while (keys.hasNext()) {
//		        String key = keys.next();
//		        JSONObject seriesValue = (JSONObject) json.get(key);
//		        System.out.println("Key :" + key + "  Value :" + json.get(key));
//		    }
//
//		}
//	}

}
