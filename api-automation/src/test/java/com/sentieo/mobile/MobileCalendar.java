package com.sentieo.mobile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import com.jayway.restassured.response.Response;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;

public class MobileCalendar extends APIDriver {

	Calendar cal = Calendar.getInstance();
	DateFormat dateformat = new SimpleDateFormat("d-MMM-yyyy");

	APIAssertions verify = new APIAssertions();
	
	public void testCalendarAssertions(APIResponse apiResp, Response resp) throws Exception {
		try {
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson.getJSONArray("result").length() != 0, "checking results array");
			//verify.jsonSchemaValidation(resp, "mobileApis" + File.separator + "testCalendar.json");
		} catch (Exception je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

}
