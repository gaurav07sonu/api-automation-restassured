package com.sentieo.EDT;


import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.*;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;
import java.util.HashMap;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class FetchGraphDataEstimatesChart extends APIDriver {
	HashMap<String, String> tickerData = new HashMap<String, String>();
	JSONArray appSeries;
	JSONArray app2Series;
	String errorMsgAPP="";
	String errorMsgAPP2="";

	@BeforeClass
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
		CommonUtil commUtil = new CommonUtil();
		commUtil.generateRandomTickers("EDTTicker.csv");
	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	public void fetchGraphdataYearlyEstimatesapp(String subType,String ticker) throws Exception {
		if (subType.contains("GrossProfit"))
			subType = "gross_profit";
		if (subType.contains("EbitdaMargin"))
			subType = "ebitda-per";
		if (subType.contains("EbitMargin"))
			subType = "ebit-per";
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "yearlyEstimate");
		tickerData.put("subtype", subType);
		tickerData.put("source", "summary");
		tickerData.put("startyear", "2014");
		tickerData.put("endyear", "2022");
		tickerData.put("next4", "true");
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		appSeries = respJson.getJSONObject("result").getJSONArray("series");
		errorMsgAPP=respJson.getJSONObject("response").getJSONArray("msg").toString();
	}

	public void fetchGraphdataYearlyEstimatesapp2(String subType,String ticker) throws CoreCommonException {
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "yearlyEstimate");
		tickerData.put("subtype", subType);
		tickerData.put("source", "summary");
		tickerData.put("startyear", "2014");
		tickerData.put("endyear", "2022");
		tickerData.put("next4", "true");
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post("https://app2.sentieo.com" + FETCH_GRAPH_DATA, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		app2Series = respJson.getJSONObject("result").getJSONArray("series");
		errorMsgAPP2=respJson.getJSONObject("response").getJSONArray("msg").toString();
	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "graphDataYearlyEstimate", dataProviderClass = DataProviderClass.class)
	public void yearlyEstimateTest(String subType) throws Exception {
		for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
			String ticker = tickerValue.getValue();
			ticker=ticker.toLowerCase();
			fetchGraphdataYearlyEstimatesapp(subType,ticker);
			fetchGraphdataYearlyEstimatesapp2(subType,ticker);
			int appSeriesLength = appSeries.length();
			int app2SeriesLength = app2Series.length();
			verify.verifyEquals(appSeriesLength, app2SeriesLength,
					"verify series length" + "  app series  " + appSeriesLength + "  app2 series  " + app2SeriesLength + " for series "+subType);
			verify.assertEqualsActualContainsExpected(errorMsgAPP,"success","verify app message");
			verify.assertEqualsActualContainsExpected(errorMsgAPP2,"success","verify app2 message");
		}
		verify.verifyAll();
	}

}
