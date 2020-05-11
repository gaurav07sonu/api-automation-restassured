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

public class CheckTickerSettings extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity", "test" }, description = "initial-loading")
	public void checkTickerSettings() throws Exception {
		try {
			String URI = USER_APP_URL + CHECK_TICKER_SETTINGS;
			HashMap<String, String> parameters = new HashMap<String, String>();
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			parameters.put("new_wl", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONObject series = respJson.getJSONObject("result").getJSONObject("result").getJSONObject(ticker);
				if (series.length() <= 2 || series == null || series.length() == 0)
					verify.assertTrue(false, "API shows blank data");
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}
}
