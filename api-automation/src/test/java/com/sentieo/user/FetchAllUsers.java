package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;

import org.json.JSONArray;
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

public class FetchAllUsers extends APIDriver {

	@BeforeMethod(alwaysRun = true)
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

	@Test(groups = "sanity", description = "fetch all user", priority = 0)
	public void testFetchAllUsers() throws Exception {
		try {
		String URI = USER_APP_URL + FETCH_ALL_USERS;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").get("status"), "success",
				"Verify the API Message");
		JSONArray getUsers = respJson.getJSONArray("result");
		if (getUsers.length() == 0 || getUsers == null)
			verify.assertTrue(false, "all users shows blank data");
		verify.verifyAll();
	}
	catch (Exception e) {
		verify.assertFalse(false,e.toString());
	}
	}
}
