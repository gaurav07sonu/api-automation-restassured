  package com.sentieo.EDT;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.*;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
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
	String errorMsgAPP = "";
	String errorMsgAPP2 = "";
	String loc2 = "";
	
	@BeforeTest(alwaysRun = true)
	@Parameters({"loc"})
	public void getLoc(@Optional("loc")String loc) {
		loc2 = loc;
		System.out.println(loc2);
	}
	
	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}
	
	
	
	public void fetchGraphdataYearlyEstimatesapp(String subType, String ticker) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();	
		if(loc2.equals("ios"))
		{
			if (subType.contains("GrossProfit"))
				subType = "gross_profit";
			if (subType.contains("EbitdaMargin"))
				subType = "ebitda-margin";
			if (subType.contains("EbitMargin"))
				subType = "ebit-margin";			
			tickerData.put("loc", "ios");
		}
		else
		{
			if (subType.contains("GrossProfit"))
				subType = "gross_profit";
			if (subType.contains("EbitdaMargin"))
				subType = "ebitda-per";
			if (subType.contains("EbitMargin"))
				subType = "ebit-per";
			tickerData.put("source", "summary");

		}
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "yearlyEstimate");
		tickerData.put("subtype", subType);
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
		errorMsgAPP = respJson.getJSONObject("response").getJSONArray("msg").toString();
		if(loc2.equals("ios"))
		{
						
			verifykeyAvailable(respJson.getJSONObject("result").getJSONObject("custom").getJSONObject("periods"), "interim_type", "java.lang.Integer");
			verifykeyAvailable(respJson.getJSONObject("result"), "series", "org.json.JSONArray");

			JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
			HashMap<String, String> keyMap = new HashMap<String, String>();
			keyMap.put("series", "org.json.JSONArray");
			keyMap.put("title", "java.lang.String");
			
//			if (series.length() != 0 || series != null)
//			{
//				for(int i=0; i<series.length(); i++)
//				{
//					for (Map.Entry<String, String> set : keyMap.entrySet()) {
//						verifykeyAvailable(series.getJSONObject(i), set.getKey(), set.getValue());
//						System.out.println(set.getKey() + " = " + set.getValue());
//					}
//					verify.verifyEquals(series.getJSONObject(i).get("series").getClass().getName(), "org.json.JSONArray",
//							"Verify data type for key :series -> series");
//					verify.verifyEquals(series.getJSONObject(i).get("title").getClass().getName(),  "java.lang.String",
//							"Verify data type for key :series -> title");
				//}
			//}
		}
	}

//	public void fetchGraphdataYearlyEstimatesapp2(String subType, String ticker) throws CoreCommonException {
//		tickerData.put("ticker", ticker);
//		tickerData.put("graphtype", "yearlyEstimate");
//		tickerData.put("subtype", subType);
//		tickerData.put("source", "summary");
//		tickerData.put("startyear", "2014");
//		tickerData.put("endyear", "2022");
//		tickerData.put("next4", "true");
//		RequestSpecification spec = formParamsSpec(tickerData);
//		Response resp = RestOperationUtils.post("https://app2.sentieo.com" + FETCH_GRAPH_DATA, null, spec, tickerData);
//		APIResponse apiResp = new APIResponse(resp);
//		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//		verify.verifyResponseTime(resp, 5000);
//		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//				"Verify the API Response Status");
//		app2Series = respJson.getJSONObject("result").getJSONArray("series");
//		errorMsgAPP2 = respJson.getJSONObject("response").getJSONArray("msg").toString();
//	}

	@Test(groups = {"sanity","mobile"}, description = "fetch yearly estimates", dataProvider = "graphDataYearlyEstimate", dataProviderClass = DataProviderClass.class)
	
	public void yearlyEstimateTest(String subType) throws Exception {
		CommonUtil commUtil = new CommonUtil();
		List<String[]> tickers = commUtil.readTickerCSV("EDTTicker.csv");
		for (String[] row : tickers) {
			for (String tickerName : row) {
				tickerName = tickerName.toLowerCase();
				fetchGraphdataYearlyEstimatesapp(subType, tickerName);
				//fetchGraphdataYearlyEstimatesapp2(subType, tickerName);
				
			}
		}
		verify.verifyAll();
	}
	
	 public void verifykeyAvailable(JSONObject result, String key, String type) {
			if (result.has(key)) {
				verify.verifyEquals(result.get(key).getClass().getName(), type,
						"Verify data type for key: "+key );
			} else
				verify.assertTrue(false, key + " :key not found");
		}

}
