package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;

import org.json.JSONArray;
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

public class FetchSavedSeries extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "savedseries", description = "fetch_saved_series")
	public void fetchSavedSeries() throws Exception {
		boolean result = true;
		String URI = USER_APP_URL + FETCH_SAVED_SERIES;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("series_type", "all");
		tickerData.put("type", "plotter");
		try {
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONArray names_list = respJson.getJSONObject("result").getJSONArray("names_list");
			if (names_list.length() == 0 || names_list == null) {
				verify.assertTrue(false, " API shows blank data for saved series ");
				result = false;
			} else if (result)
				verify.assertTrue(true, "verify saved series");

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}
}
