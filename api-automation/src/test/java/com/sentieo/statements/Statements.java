package com.sentieo.statements;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.finance.InputTicker;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class Statements extends APIDriver {

	APIAssertions verify = new APIAssertions();
	CommonUtil util = new CommonUtil();
	static String getName = null;
	static String getURl = null;
	static String getDocID = null;
	static String ticker = "";
	Object ticker1 = "";
	String loc2 = "";
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "loc" })
	public void getLoc(@Optional("loc") String loc) {
		loc2 = loc;
	}

	@Test(groups = { "sanity", "mobile" }, description = "fetch screener models")
	public void fetchallxbrltables() throws Exception {
		try {
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					HashMap<String, String> tickerData = new HashMap<String, String>();
					Response resp;
					tickerData.put("ticker", cell);
					ticker = cell;
					if (loc2.equals("ios")) {
						tickerData.put("loc", "ios");
						RequestSpecification spec = formParamsSpec(tickerData);
						resp = RestOperationUtils.post(FETCH_ALL_XBRL_TABLES, null, spec, tickerData);

					} else {
						RequestSpecification spec = queryParamsSpec(tickerData);
						resp = RestOperationUtils.get(FETCH_ALL_XBRL_TABLES, spec, tickerData);
					}
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");

					if (respJson.get("result") instanceof JSONObject) {
						verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
								"Verify the API Message");
						JSONObject completeAPIResult = respJson.getJSONObject("result");
						if (loc2.equals("ios")) {
							HashMap<String, String> keyMap = new HashMap<String, String>();
							keyMap.put("financial_tables", "org.json.JSONArray");
							keyMap.put("quarter_results", "org.json.JSONArray");

							for (Map.Entry<String, String> set : keyMap.entrySet()) {
								util.verifykeyAvailable(completeAPIResult, set.getKey(), set.getValue());
							}
						}
						JSONArray getFinancialTables = completeAPIResult.getJSONArray("financial_tables");

						for (int i = 0; i < getFinancialTables.length(); i++) {
							JSONArray getFinancialKeys = getFinancialTables.getJSONArray(i);
							getName = String.valueOf(getFinancialKeys.get(0));
							getURl = String.valueOf(getFinancialKeys.get(1));
							if (loc2.equals("ios")) {
								getXbrlDataTableForMobile(getURl, getName);
							} else {
								JSONArray getQuarterResults = completeAPIResult.getJSONArray("quarter_results");

								JSONArray getDocKey = getQuarterResults.getJSONArray(0);
								getDocID = String.valueOf(getDocKey.get(0));
								getxbrldatatable(getURl, getName, getDocID);
							}
						}
					} else {
						ExtentTestManager.getTest().log(LogStatus.INFO,
								"Statement not available for ticker : -" + cell);
						getName = null;
						getURl = null;
					}
				}
			}
		} catch (JSONException je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();
		}
	}

	public void getxbrldatatable(String getURl, String getName, String getDocID) throws Exception {
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			if (getURl != null && getName != null && getDocID != null) {
				System.out.println("ticker name is:" + ticker);
				System.out.println("url is:" + getURl);
				System.out.println("name is: " + getName);

				tickerData.put("url", "http://www.sec.gov" + getURl);
				tickerData.put("key", getName);
				tickerData.put("doc_id", getDocID);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(GET_XBRL_DATA_TABLE, spec, tickerData);

				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
			} else {
				ExtentTestManager.getTest().log(LogStatus.INFO, "Statement not available for ticker : -" + ticker);
			}
		} catch (JSONException je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();
		}
	}

	public void getXbrlDataTableForMobile(String getURl, String getName) throws Exception {
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			if (getURl != null && getName != null) {

				tickerData.put("url", "http://www.sec.gov" + getURl);
				tickerData.put("key", getName);
				tickerData.put("loc", "ios");

				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(XBRL_TABLE_WITH_CHANGE, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");

				JSONObject result = respJson.getJSONObject("result");

				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("table", "org.json.JSONArray");
				keyMap.put("header_rows", "java.lang.Integer");
				keyMap.put("merge_list", "org.json.JSONArray");

				for (Map.Entry<String, String> set : keyMap.entrySet()) {
					util.verifykeyAvailable(result, set.getKey(), set.getValue());
					// System.out.println(set.getKey() + " = " + set.getValue());
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.INFO, "Statement not available for ticker : -" + ticker);
			}
		} catch (JSONException je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();
		}
	}

}
