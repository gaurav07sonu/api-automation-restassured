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
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class InitialLoading extends APIDriver {

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

	@Test(groups = { "sanity", "test" }, description = "initial-loading")
	public void initialLoading() throws Exception {
		try {
			String URI = USER_APP_URL + INITIAL_LOADING;
			HashMap<String, String> parameters = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONObject getResult = respJson.getJSONObject("result");
				if (getResult.length() == 0 || getResult == null)
					verify.assertTrue(false, "API shows blank data");
			} else
				verify.assertTrue(false, "status code is not equal to 200 ");

		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}
}