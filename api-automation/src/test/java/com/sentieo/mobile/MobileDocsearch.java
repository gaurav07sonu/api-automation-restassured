package com.sentieo.mobile;

import java.io.File;

import org.json.JSONObject;

import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.utils.CoreCommonException;

public class MobileDocsearch extends APIDriver{
	
	APIAssertions verify = new APIAssertions();

	public void testFetchSearch(APIResponse apiResponse, Response resp) throws CoreCommonException {
		try {

			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testFetchSearch.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
}
