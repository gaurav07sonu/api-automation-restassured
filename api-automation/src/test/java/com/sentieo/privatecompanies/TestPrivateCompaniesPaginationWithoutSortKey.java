package com.sentieo.privatecompanies;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.sentieo.constants.Constants.*;
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

public class TestPrivateCompaniesPaginationWithoutSortKey extends APIDriver {

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
		CommonUtil commUtil = new CommonUtil();
		commUtil.generateRandomTickers();
	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "fetch cb exit table without sort key and sort order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBExitTableWithoutSortKeyAndOrder(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("exits");
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}

			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "fetch cb exit table without sort key and sort order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBExitTableWithoutSortKeyAndSortOrderIsASC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("sort_order", "asc");
				parameters.put("ticker", ticker);
				
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("exits");
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}

			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "fetch cb exit table without sort key and sort order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBExitTableWithoutSortKeyAndSortOrderIsDESC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("sort_order", "desc");
				parameters.put("ticker", ticker);
				
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("exits");
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}

			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}


	@Test(groups = "sanity", description = "cgeck fetch cbfr table witohut sort key and order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFRTableWithoutSortKeyAndSortOrder(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funding_rounds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "Check fetch cbfr table without sort key and sort order is asc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFRTableWithoutSortKeyAndSortOrderIsASC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("sort_order", "asc");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funding_rounds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "Check fetch cbfr table without sort key and sort order is desc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFRTableWithoutSortKeyAndSortOrderIsDESC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("sort_order", "desc");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funding_rounds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "verify fetch cbfr highlights without sort key and sort order")
	public void fetchCBFRhighlightsWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_HIGHLIGHTS;
				parameters.put("num_rows","all");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "verify fetch cbfr highlights without sort key and sort order is asc")
	public void fetchCBFRhighlightsWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_HIGHLIGHTS;
				parameters.put("sort_order", "asc");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "verify fetch cbfr highlights without sort key and sort order is desc")
	public void fetchCBFRhighlightsWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_HIGHLIGHTS;
				parameters.put("num_rows", "5");
				parameters.put("sort_order", "desc");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	
	@Test(groups = "sanity", description = "check fetch cbfr cumulative without sort key and order")
	public void fetchCBFRCumulativeWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_cumulative;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "check fetch cbfr cumulative without sort key and order is asc")
	public void fetchCBFRCumulativeWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_cumulative;
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "check fetch cbfr cumulative without sort key and order is desc")
	public void fetchCBFRCumulativeWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FR_cumulative;
				parameters.put("num_rows", "3");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch CBINVT table without sort key and order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBINVTableWithoutSortKeyAndOrder(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investments");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "check fetch CBINVT table without sort key and order is asc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBINVTableWithoutSortKeyAndOrderIsASC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investments");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	

	@Test(groups = "sanity", description = "check fetch CBINVT table without sort key and order is desc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBINVTableWithoutSortKeyAndOrderIsDESC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investments");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbinv quarterwise without sort key and order")
	public void fetchCBINVQuarterwiseWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_QUARTERWISE;
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch cbinv quarterwise without sort key and order is asc")
	public void fetchCBINVQuarterwiseWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_QUARTERWISE;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch cbinv quarterwise without sort key and order is desc")
	public void fetchCBINVQuarterwiseWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_QUARTERWISE;
				parameters.put("num_rows", "7");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbinv category without sort key and order")
	public void fetchCBINVCategoryWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_CATEGORY;
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "check fetch cbinv category without sort key and order is asc")
	public void fetchCBINVCategoryWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_CATEGORY;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "check fetch cbinv category without sort key and order is desc")
	public void fetchCBINVCategoryWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_INV_CATEGORY;
				parameters.put("num_rows", "1");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbkey investors without sorting key and order",dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBKeyInvestorsWithoutSortKeyAndOrder(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_KEY_INVESTORS;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investors");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch cbkey investors without sorting key and order is asc",dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBKeyInvestorsWithoutSortKeyAndOrderIsASC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_KEY_INVESTORS;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investors");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch cbkey investors without sorting key and order is desc",dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBKeyInvestorsWithoutSortKeyAndOrderIsDESC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_KEY_INVESTORS;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("investors");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cb exit category without sorting key and order")
	public void fetchCBExitCategoryWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_CATEGORY;
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cb exit category without sorting key and order is asc")
	public void fetchCBExitCategoryWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_CATEGORY;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cb exit category without sorting key and order is desc")
	public void fetchCBExitCategoryWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_EXIT_CATEGORY;
				parameters.put("num_rows", "9");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbacq table without sorting key and order")
	public void fetchCBACQTableWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_ACQ_TABLE;
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbacq table without sorting key and order is asc")
	public void fetchCBACQTableWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_ACQ_TABLE;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch cbacq table without sorting key and order is desc")
	public void fetchCBACQTableWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_ACQ_TABLE;
				parameters.put("num_rows", "3");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "Check fetch cbfunds table without sortkey and order", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFundsTableWithoutSortKeyAndOrder(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FUNDS_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "Check fetch cbfunds table without sortkey and order is asc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFundsTableWithoutSortKeyAndOrderIsASC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FUNDS_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
				
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	
	@Test(groups = "sanity", description = "Check fetch cbfunds table without sortkey and order is desc", dataProvider = "numberof_rows", dataProviderClass = DataProviderClass.class)
	public void fetchCBFundsTableWithoutSortKeyAndOrderIsDESC(String paginationRequestCount) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_CB_FUNDS_TABLE;
				parameters.put("num_rows", paginationRequestCount);
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				int totalExitsCount = respJson.getJSONObject("result").getInt("total_exits_count");
				JSONArray exitsArray = respJson.getJSONObject("result").getJSONArray("funds");
				int exitsArrayLength = exitsArray.length();
				if (paginationRequestCount.equals("all")) {
					verify.verifyEquals(exitsArrayLength, totalExitsCount, "");
				} else {
					int paginationCount = Integer.parseInt(paginationRequestCount);
					if (paginationCount > totalExitsCount) // 50>40
						verify.verifyEquals(totalExitsCount, exitsArrayLength, "");
					else
						verify.verifyEquals(paginationCount, exitsArrayLength, "");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	@Test(groups = "sanity", description = "check fetch header data without sort key and order")
	public void fetchHeaderDataWithoutSortKeyAndOrder() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
				parameters.put("ticker", ticker);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch header data without sort key and order")
	public void fetchHeaderDataWithoutSortKeyAndOrderIsASC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
				parameters.put("num_rows", "all");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "asc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
	@Test(groups = "sanity", description = "check fetch header data without sort key and order is desc")
	public void fetchHeaderDataWithoutSortKeyAndOrderIsDESC() throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + FETCH_NEW_COMPANY_HEADER_DATA;
				parameters.put("num_rows", "10");
				parameters.put("ticker", ticker);
				parameters.put("sort_order", "desc");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}


}
