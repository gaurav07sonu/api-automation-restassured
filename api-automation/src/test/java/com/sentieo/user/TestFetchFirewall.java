package com.sentieo.user;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class TestFetchFirewall extends APIDriver {

	APIAssertions verify = new APIAssertions();

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

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

	@Test(groups = "sanity", description = "Fetch Firewall")
	public void fetchFirewall() throws Exception {
		String URI = USER_APP_URL + FETCH_FIREWALL_TEST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(parameters);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "Fetch Firewall test for IOS")
	public void fetchFirewallIOS() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("loc", "ios");
		String URI = USER_APP_URL + FETCH_FIREWALL_TEST;
		RequestSpecification spec = formParamsSpec(parameters);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		JSONObject getResult = respJson.getJSONObject("result");
		if (getResult.has("apid")) {
			String apid = respJson.getJSONObject("result").getString("apid").toString();
			verify.assertTrue(true, " apid is :" + apid);
		} else {
			verify.assertTrue(false, "apid not found");
		}
		verify.verifyAll();
	}

}
