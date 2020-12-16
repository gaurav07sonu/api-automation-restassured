package com.sentieo;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class MapTicker extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "test-group", description = "Plotter Income Statement Series")
	public void tmMapTicker() throws CoreCommonException {
		try {
			String URI = APP_URL + TM_MAP_TICKER;
			String cusip = "755111507";
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("cusip", cusip);
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("status").getJSONArray("msg").get(0), "Success",
						"Verify the API Message");
				JSONObject result = respJson.getJSONObject("response").getJSONObject(cusip);
				String ticker = result.getString("ticker");
				verify.assertEqualsActualContainsExpected(ticker.toLowerCase(), "rtn", "Verify ticker");
				int sentieoID = result.getInt("sentieo_id");
				ExtentTestManager.getTest().log(LogStatus.INFO, "Sentieo ID is " + sentieoID);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			verify.verifyAll();
		}

	}

}
