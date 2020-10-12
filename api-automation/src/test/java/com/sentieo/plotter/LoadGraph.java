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
			JSONArray graphobj = null;
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("sort_flag", sort_flag);
			try {
				graphobj = loadGraphNew(tickerData);
				if (graphobj.length() == 0 || graphobj == null)
					verify.assertTrue(false, "Data is blank for : " + sort_flag);

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

	public void deleteGraph(String id) {
		try {
			String URI = USER_APP_URL + DELETE_GRAPH;
			HashMap<String, String> plotterData = new HashMap<String, String>();
			plotterData.put("plotter_id", id);
			RequestSpecification spec = formParamsSpec(plotterData);
			Response resp = RestOperationUtils.post(URI, null, spec, plotterData);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public JSONArray loadGraphNew(HashMap<String, String> parameters) throws Exception {
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		org.json.JSONArray graphobj = null;
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			System.out.println(apiResp.getStatusCode());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			graphobj = respJson.getJSONObject("result").getJSONArray("graphobj");
			return graphobj;

		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
		verify.verifyAll();
		return graphobj;
	}

}
