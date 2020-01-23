package com.sentieo.mosaic;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.heartbeat.HeartbeatMonitors;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class MosaicSummaryData extends APIDriver {

	@BeforeClass(alwaysRun = true)
	public void setup() throws Exception {
		String URI = USER_APP_URL + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", EMAIL);
		loginData.put("password", PASSWORD);
		RequestSpecification spec = loginSpec(loginData);
		Response resp = RestOperationUtils.login(URI, null, spec, loginData);
		apid = resp.getCookie("apid");
		usid = resp.getCookie("usid");
		RestAssured.baseURI = APP_URL;

	}

	@Test(description = "Check unfied tracker table")
	public void fetchUnifiedTable() throws CoreCommonException {
		String team="";
		APIResponse apiResp = null;
		Response resp = null;
		HeartbeatMonitors heart = new HeartbeatMonitors();
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + UNIFIEDTRACKERTABLE;
		try {
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			assert apiResp.getStatusCode() == 200;
			HeartbeatMonitors.updatePassResult(URI, team.toString(), "200", resp, parameters);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray main = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("main");
			if (main.length() == 0 || main == null)
				assertTrue(false);
			JSONArray data = respJson.getJSONObject("result").getJSONArray("data").getJSONObject(0)
					.getJSONArray("data");
			if (data.length() == 0 || data == null)
				assertTrue(false);
		} catch (Error e) {
			e.printStackTrace();
			heart.updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		} catch (Exception e) {
			e.printStackTrace();
			heart.updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters);
			Assert.fail();
		}
	}
}