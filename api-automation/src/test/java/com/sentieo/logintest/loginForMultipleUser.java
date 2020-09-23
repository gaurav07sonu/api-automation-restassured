package com.sentieo.logintest;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class loginForMultipleUser extends APIDriver {

	APIAssertions verify = new APIAssertions();
	String userName;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@AfterClass(alwaysRun = true)
	public void setCookieAgain() throws CoreCommonException {
		APIDriver ap = new APIDriver();
		ap.login();
	}

	@Test(groups = "sanity", description = "check", dataProvider = "users", dataProviderClass = DataProviderClass.class)
	public void verifyLogin(String email, String password) throws Exception {
		apid = "";
		usid = "";
		String URI = USER_APP_URL + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", email);
		loginData.put("password", password);
		RequestSpecification spec = formParamsSpec(loginData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		int statuscode=apiResp.getStatusCode();
		verify.verifyStatusCode(statuscode, 200);
		if(statuscode==200) {
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		String msg = respJson.getJSONObject("response").getString("msg").toString();
		if (msg.contains("Login successful")) {
			userName = respJson.getJSONObject("result").getString("email").toString();
			verify.assertEqualsActualContainsExpected(email, userName, msg);
		} else {
			verify.assertTrue(false, msg);
		}
		}
		verify.verifyAll();
	}
}
