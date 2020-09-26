package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class LoadGraph extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "load", description = "fetch_trading_ratios", dataProvider = "loadGraph", dataProviderClass = DataProviderClass.class)
	public void loadGraphNew(String sort_flag) throws Exception {
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		if (URI.contains("app") || URI.contains("staging") || URI.contains("app2")) {
			boolean load = true;
			JSONObject loadData = null;
			JSONArray graphobj = null;
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("sort_flag", sort_flag);
			try {
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (sort_flag.equalsIgnoreCase("tagged_series"))
					loadData = respJson.getJSONObject("result").getJSONObject("graphobj");

				else
					graphobj = respJson.getJSONObject("result").getJSONArray("graphobj");

				if (sort_flag.equalsIgnoreCase("tagged_series")) {
					if (loadData.length() == 0 || loadData == null) {
						verify.assertTrue(false, "graphobj data is blank for : " + sort_flag);
						load = false;
					}
				} else {
					if (graphobj.length() == 0 || graphobj == null) {
						verify.assertTrue(false, "graphobj data is blank for : " + sort_flag);
						load = false;
					}
				}

				if (load)
					verify.assertTrue(load, "verify data for : " + sort_flag);
			} catch (Exception e) {
				verify.verificationFailures.add(e);
				ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
			}
			verify.verifyAll();
		}
	}

	@Test(groups = "load2", description = "fetch_trading_ratios")
	public void loadTemplateSentieo() throws Exception {
		boolean load = true;
		String URI = APP_URL + LOADTEMPLATE_SENTIEO;
		if (URI.contains("app") || URI.contains("staging") || URI.contains("app2")) {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("sort_flag", "recent");
			try {
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray graphobj = respJson.getJSONObject("result").getJSONArray("graphobj");
				if (graphobj.length() == 0 || graphobj == null) {
					verify.assertTrue(false, "Sentieo templates shows blank data : ");
					load = false;
				}
				if (load)
					verify.assertTrue(load, "Verify sentieo template data : ");
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
