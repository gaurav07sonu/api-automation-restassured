package com.sentieo.user;

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
import com.sentieo.utils.CoreCommonException;

public class GetUserWatchlistData extends APIDriver {
	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}


	@Test(groups = { "User", "user-watchlistData" }, description = "get user watchlist data")
	public void getUserWatchlistsData() throws Exception {
		String URI = USER_APP_URL + GET_USER_WATCHLIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			int statusCode = apiResp.getStatusCode();
			if (statusCode == 200) {
				JSONObject entity_info = respJson.getJSONObject("result").getJSONObject("entity_info");
				if (entity_info.length() == 0 || entity_info == null)
					verify.assertTrue(false, "entity_info shows blank");
				JSONArray individual_watchlist = respJson.getJSONObject("result").getJSONArray("individual_watchlist");
				if (individual_watchlist.length() == 0 || individual_watchlist == null)
					verify.assertTrue(false, "individual watchlist size is zero ");
				JSONArray all_tickers = respJson.getJSONObject("result").getJSONArray("all_tickers");
				if (all_tickers.length() == 0 || all_tickers == null)
					verify.assertTrue(false, "All tickers size is zero ");
			}
		} catch (Error e) {
			throw new CoreCommonException(e.getMessage());

		} finally {
			verify.verifyAll();

		}
	}
}
