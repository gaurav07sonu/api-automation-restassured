package com.sentieo.mobile;

import static com.sentieo.constants.Constants.ADD_WATCHLIST;
import static com.sentieo.constants.Constants.DELETE_WATCHLIST;
import static com.sentieo.constants.Constants.EDIT_WATCHLIST;
import static com.sentieo.constants.Constants.FETCH_USER_PORTFOLIO;
import static com.sentieo.constants.Constants.REMOVE_TICKERS_WATCHLIST;
import static com.sentieo.constants.Constants.USER_APP_URL;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
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

/**
 * 
 * @author akash
 *
 */
public class MobileWatchlistTest extends APIDriver {

	String id = null;
	String editedName = null;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		RestAssured.baseURI = USER_APP_URL;
	}

	@Test(groups = "mobileMainApp", description = "test Add watchlist", priority = 1)
	public void testAddWatchlist() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String editname = "watchlist" + split[0].toString();
			HashMap<String, String> addWatchlist = new HashMap<String, String>();
			addWatchlist.put("tickers", "aapl,fb");
			addWatchlist.put("w_name", editname);
			addWatchlist.put("loc", "ios");
			addWatchlist.put("new_wl", "1");

			RequestSpecification spec = formParamsSpecMobile(addWatchlist);
			Response resp = RestOperationUtils.post(ADD_WATCHLIST, null, spec, addWatchlist);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject results = (JSONObject) respJson.getJSONArray("result").get(0);
			id = results.getString("id");
			verify.verifyTrue(id != null, "checking if id is not null");
			JSONObject response = (JSONObject) respJson.get("response");
			verify.verifyEquals(response.getJSONArray("msg").get(0), "Watchlist successfully added", "Checking message");
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testAddWatchlist.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobileMainApp", description = "test edit watchlist", priority = 2)
	public void testEditWatchlist() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			editedName = "watchlist" + split[0].toString();
			HashMap<String, String> editWatchlist = new HashMap<String, String>();
			editWatchlist.put("id", id);
			editWatchlist.put("tickers", "tsla");
			editWatchlist.put("w_name", editedName);
			editWatchlist.put("loc", "ios");

			RequestSpecification spec = formParamsSpecMobile(editWatchlist);
			Response resp = RestOperationUtils.post(EDIT_WATCHLIST, null, spec, editWatchlist);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject results = (JSONObject) respJson.getJSONArray("result").get(0);
			String name = results.getString("name");
			verify.verifyEquals(name, editedName, "checking edited name");
			JSONArray tickers = results.getJSONArray("tickers");
			boolean tickerStatus = false;
			for (int i = 0; i < tickers.length(); i++) {
				if (tickers.get(i) == "intc") {
					tickerStatus = true;
				}
			}
			verify.verifyTrue(tickerStatus, "checking ticker is added or not");
			JSONObject response = (JSONObject) respJson.get("response");
			verify.verifyEquals(response.getJSONArray("msg").get(0), "Watchlist successfully edited", "checking message");
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testEditWatchlist.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobileMainApp", description = "test remove tickers from watchlist", priority = 3)
	public void testRemoveTickerFromWatchlist() throws Exception {
		try {
			HashMap<String, String> removeParams = new HashMap<String, String>();
			removeParams.put("tickers", "fb");
			removeParams.put("loc", "ios");
			removeParams.put("id", id);
			removeParams.put("w_name", editedName);

			RequestSpecification spec = formParamsSpecMobile(removeParams);
			Response resp = RestOperationUtils.post(REMOVE_TICKERS_WATCHLIST, null, spec, removeParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject results = (JSONObject) respJson.getJSONArray("result").get(0);
			JSONArray tickers = results.getJSONArray("tickers");
			boolean tickerStatus = false;
			for (int i = 0; i < tickers.length(); i++) {
				if (tickers.get(i) != "fb") {
					tickerStatus = true;
				}else {
					tickerStatus = false;
				}
			}
			verify.verifyTrue(tickerStatus, "checking ticker is removed or not");

			JSONObject response = (JSONObject) respJson.get("response");
			verify.verifyEquals(response.getJSONArray("msg").get(0), "Watchlist successfully edited", "checking message");
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testRemoveTickerFromWatchlist.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "mobileMainApp", description = "test delete watchlist", priority = 4)
	public void testDeleteWatchlist() throws Exception {
		try {
			HashMap<String, String> deleteWatchlist = new HashMap<String, String>();
			deleteWatchlist.put("loc", "ios");
			deleteWatchlist.put("id", id);
			
			RequestSpecification spec = formParamsSpecMobile(deleteWatchlist);
			Response resp = RestOperationUtils.post(DELETE_WATCHLIST, null, spec, deleteWatchlist);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			verify.verifyEquals(response.getJSONArray("msg").get(0), "Watchlist successfully deleted", "Checking message");
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testDeleteWatchlist.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobileMainApp", description = "get user watchlist data")
	public void userPortFolio() throws Exception {
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray watchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
			if (watchlists.length() == 0 || watchlists == null)
				assertTrue(false);
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
}
