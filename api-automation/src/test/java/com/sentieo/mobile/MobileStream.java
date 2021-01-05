package com.sentieo.mobile;

import java.io.File;

import org.json.JSONObject;

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
public class MobileStream extends APIDriver {

	APIAssertions verify = new APIAssertions();

	public void testFetchUsersTickersValue(APIResponse apiResponse, Response resp) throws Exception {
		try {

			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testFetchUsersTickersValue.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

}
