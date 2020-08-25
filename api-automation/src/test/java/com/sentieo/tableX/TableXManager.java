package com.sentieo.tableX;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class TableXManager extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity",
			"test" }, description = "initial-loading", dataProvider = "chainUser", dataProviderClass = DataProviderClass.class)
	public void fetchChainUserData(String doc_types, String tickers) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String URI = USER_APP_URL + CHAIN_USER_DATA;
			doc_types="["+ "\""+doc_types+ "\""+"]";
			tickers="["+ "\""+tickers+"\""+"]";
			parameters.put("tab_name", "search");
			parameters.put("doc_types", doc_types);
			parameters.put("tickers", tickers);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray chain_data = respJson.getJSONObject("result").getJSONArray("chain_data");
				JSONArray periods = respJson.getJSONObject("result").getJSONArray("periods");
				if (chain_data.length() == 0 || chain_data == null)
					verify.assertTrue(false, "chain_data series shows blank data : " + chain_data);

				if (periods.length() == 0 || periods == null)
					verify.assertTrue(false, "periods series shows blank data : " + periods);
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

}
