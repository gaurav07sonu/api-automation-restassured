package com.sentieo.mobile;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.CHECK_AUTOUPDATE;
import static com.sentieo.constants.Constants.CHECK_DOMAIN;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.SET_CSRF_COOKIE;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

public class MobileLoginTest extends APIDriver {
	String userName;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		RestAssured.baseURI = USER_APP_URL;
	}

	@Test(groups = "mobile", description = "set csrf token", priority = 1)
	public void testSetCsrfToken() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(APP_URL + SET_CSRF_COOKIE, spec, null);
			Map<String, String> cookies = resp.getCookies();
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			if (cookies.containsKey("csrftoken")) {
				verify.assertTrue(!cookies.values().isEmpty(), "checking csrftoken is present");
			} else {
				verify.verifyTrue(false, "Csrftoken is not present");
			}
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testCsrfToken.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobile", description = "Login test", priority = 2)
	public void testMobileLogin() throws Exception {
		try {
			HashMap<String, String> loginData = new HashMap<String, String>();
			loginData.put("email", EMAIL);
			loginData.put("password", PASSWORD);
			loginData.put("loc", "ios");
			
			RequestSpecification spec = formParamsSpecMobile(loginData);
			Response resp = RestOperationUtils.post(LOGIN_URL, null, spec, loginData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			String msg = respJson.getJSONObject("response").getString("msg").toString();
			if (msg.contains("Login successful")) {
				userName = respJson.getJSONObject("result").getString("email").toString();
				verify.assertEqualsActualContainsExpected(EMAIL, userName, msg);
			} else {
				verify.assertTrue(false, msg);
			}
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testMobileLogin.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobile", description = "test auto update", priority = 3)
	public void testAutoUpdate() throws Exception {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("device", "iphone");
			parameters.put("version", "7.2");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(APP_URL + CHECK_AUTOUPDATE, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			verify.verifyEquals(respJson.getJSONObject("response").get("status"), true);
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testAutoUpdate.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "mobile", description = "Check Domain", priority = 4)
	public void testCheckDomain() throws Exception {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("email", EMAIL);
			
			RequestSpecification spec = formParamsSpecMobile(params);
			Response resp = RestOperationUtils.post(APP_URL + CHECK_DOMAIN, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Verify the API Response Status");
			verify.verifyEquals(respJson.get("domain"), APP_URL.replace("https://", ""));
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testCheckDomain.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
}
