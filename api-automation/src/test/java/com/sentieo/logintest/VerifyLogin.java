package com.sentieo.logintest;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class VerifyLogin extends APIDriver {
	String appUSerName = "autotester";
	String citadelUserName = "atish1";
	String bamsUserName = "atish2";
	String userName;
	APIAssertions verify = new APIAssertions();

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "check", dataProvider = "URLNames")
	public void verifyLogin(String url, String email, String password) throws Exception {
		String URI = url + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", email);
		loginData.put("password", password);
		RequestSpecification spec = formParamsSpec(loginData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		String msg = respJson.getJSONObject("response").getString("msg").toString();
		if (msg.contains("Login successful")) {
			userName = respJson.getJSONObject("result").getString("email").toString();
			verify.assertEqualsActualContainsExpected(email, userName, msg);
		}

		else {
			verify.assertTrue(false, msg);
		}

		verify.verifyAll();
	}

	@DataProvider(name = "URLNames")
	public static String[][] redataProviderMethod() {
		return new String[][] { { "https://user-app.sentieo.com", "alphagani35@gmail.com", "DGL=14412jg" },
				{ "https://user-balyasny.sentieo.com", "balyasny.alertcheck@sentieo.com", "Sentieo@123" },
				{ "https://user-citadel.sentieo.com", "alphagani35@gmail.com", "DGL=14412jg" } };
	}
}
