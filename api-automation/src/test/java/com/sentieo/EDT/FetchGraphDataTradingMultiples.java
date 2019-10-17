package com.sentieo.EDT;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.util.HashMap;
import java.util.List;
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

public class FetchGraphDataTradingMultiples extends APIDriver {

	HashMap<String, String> tickerData = new HashMap<String, String>();
	JSONArray appSeries;
	JSONArray app2Series;

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

	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	public void fetchGraphdataMultiplesapp(String ratio, String pType, String rationName, String ticker)
			throws Exception {
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "TradingMultiples");
		tickerData.put("ratio", ratio);
		tickerData.put("ratio_name", rationName);
		tickerData.put("ptype", pType);
		tickerData.put("shift", "backward");
		tickerData.put("sp_rel", "true");
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		appSeries = respJson.getJSONObject("result").getJSONArray("series");

	}

	public void fetchGraphdataMultiplesapp2(String ratio, String pType, String rationName, String ticker)
			throws Exception {
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "TradingMultiples");
		tickerData.put("ratio", ratio);
		tickerData.put("ratio_name", rationName);
		tickerData.put("ptype", pType);
		tickerData.put("shift", "backward");
		tickerData.put("sp_rel", "true");
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post("https://app2.sentieo.com" + FETCH_GRAPH_DATA, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		app2Series = respJson.getJSONObject("result").getJSONArray("series");

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "tradingMultiplesCombination", dataProviderClass = DataProviderClass.class)
	public void yearlyEstimateTest(String ratio, String pType, String rationName) throws Exception {
		try {
			CommonUtil commUtil = new CommonUtil();
			List<String[]> tickers = commUtil.readTickerCSV();
			for (String[] row : tickers) {
				for (String tickerName : row) {
					tickerName = tickerName.toLowerCase();
					fetchGraphdataMultiplesapp2(ratio, pType, rationName, tickerName);
					fetchGraphdataMultiplesapp(ratio, pType, rationName, tickerName);
					int appSeriesLength = appSeries.length();
					int app2SeriesLength = app2Series.length();
					verify.verifyEquals(app2SeriesLength, appSeriesLength, "verify series length" + "  app series  "
							+ appSeriesLength + "  app2 series  " + app2SeriesLength);
				}
			}
			

		} catch (Exception e) {
		}

	}

}
