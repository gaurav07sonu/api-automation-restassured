package com.sentieo.notebookpublicapis;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.sentieo.utils.JSONUtils;

public class EmailHistory extends APIDriver {
	APIAssertions verify = null;
	JSONUtils jsonUtils = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		RestAssured.baseURI = PUBLIC_API_URL;
	}

	@Test(description = "fetch All email history", priority = 1)
	public void fetchAllEmailHistory() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyTrue(respJson.getJSONArray("entries") != null, "Checking json is not empty");
				verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "fetchAllEmailHistory.json");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history with api key", priority = 2)
	public void fetchAllEmailHistoryWIthoutXApiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 401) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONObject response = respJson.getJSONObject("response");
				JSONArray jsonArray = response.getJSONArray("msg");
				verify.verifyEquals(jsonArray.get(0), "Unauthorized");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history with start date", priority = 3)
	public void fetchAllEmailHistoryWithStartDate() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("start_date", "2020-03-19T12:02:23");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date date1 = sdf.parse("2020-03-19T12:02:23");
				for (int i = 0; i < entries.length(); i++) {
					JSONObject actualDate = entries.getJSONObject(i);
					Date date2 = sdf.parse(actualDate.getString("date"));
					verify.verifyTrue(date2.after(date1), "Checking date is greater than start date");
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history and check subject", priority = 4)
	public void fetchAllEmailHistoryAndCheckSubjectIsNonEmpty() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				for (int i = 0; i < entries.length(); i++) {
					JSONObject subject = entries.getJSONObject(i);
					verify.verifyTrue(subject != null, "checking subject is not empty");
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history with limit", priority = 5)
	public void fetchAllEmailHistoryWithLimit() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "2");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				verify.verifyTrue(entries.length() == 2, "Checking length");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history with offset", priority = 6)
	public void fetchAllEmailHistoryWithOffset() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("offset", "6");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				verify.verifyEquals(respJson.get("offset"), 6);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history from Email", priority = 7)
	public void fetchAllEmailHistoryFromEmail() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("from_email", "no-reply@mail.sentieo.com");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				for (int i = 0; i < entries.length(); i++) {
					JSONObject fromEmail = entries.getJSONObject(i);
					verify.verifyTrue(fromEmail.equals("no-reply@mail.sentieo.com"), "checking subject is not empty");
				}

			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history from Email", priority = 8)
	public void fetchAllEmailHistoryFromEmailSchroders() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("from_email", "research@schrodersmail.sentieo.com");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONArray entries = respJson.getJSONArray("entries");
				verify.verifyTrue(entries != null, "Checking json is not empty");
				verify.verifyTrue(entries.length() == 2, "Checking lenght");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All email history invalid fromEmail", priority = 9)
	public void fetchAllEmailHistoryInvalidFromEmail() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String invalidEmail = "cx@sentieo";
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("from_email", invalidEmail);

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(EMAIL_HISTORY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 400) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONObject error = respJson.getJSONObject("error");
				JSONArray message = error.getJSONArray("message");
				verify.verifyEquals(message.get(0), "Invalid from email " + invalidEmail);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
}
