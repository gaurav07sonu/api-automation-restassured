package com.sentieo.mobile;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;

/**
 * 
 * @author akash
 *
 */
public class MobileAlertNotification extends APIDriver {
	
	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	//Assertions of case
	public void testNewAlertNotification(APIResponse apiResponse, Response resp, String alertType) throws Exception {
		try {
			verify.verifyStatusCode(apiResponse.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResponse.getResponseAsString());
			JSONObject result = respJson.getJSONObject("result");
			verify.verifyEquals(result.get("alert_type"), alertType);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success");
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testNewAlertNotification.json");
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
}
