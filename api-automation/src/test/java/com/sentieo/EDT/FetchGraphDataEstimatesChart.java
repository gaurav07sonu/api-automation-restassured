package com.sentieo.EDT;

import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class FetchGraphDataEstimatesChart extends APIDriver {
	CommonUtil util = new CommonUtil();
	HashMap<String, String> tickerData = new HashMap<String, String>();
	JSONArray appSeries;
	JSONArray app2Series;
	String errorMsgAPP = "";
	String errorMsgAPP2 = "";
	String loc2 = "";

	@BeforeTest(alwaysRun = true)
	@Parameters({ "loc" })
	public void getLoc(@Optional("loc") String loc) {
		loc2 = loc;
		System.out.println(loc2);
	}

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	public void fetchGraphdataYearlyEstimatesapp(String subType, String ticker) throws Exception {
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			if (loc2.equals("ios")) {
				if (subType.contains("GrossProfit"))
					subType = "gross_profit";
				if (subType.contains("EbitdaMargin"))
					subType = "ebitda-margin";
				if (subType.contains("EbitMargin"))
					subType = "ebit-margin";
				tickerData.put("loc", "ios");
			} else {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			appSeries = respJson.getJSONObject("result").getJSONArray("series");
			errorMsgAPP = respJson.getJSONObject("response").getJSONArray("msg").toString();
			if (loc2.equals("ios")) {

				util.verifykeyAvailable(
						respJson.getJSONObject("result").getJSONObject("custom").getJSONObject("periods"),
						"interim_type", "java.lang.Integer");
				util.verifykeyAvailable(respJson.getJSONObject("result"), "series", "org.json.JSONArray");

				JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("series", "org.json.JSONArray");
				keyMap.put("title", "java.lang.String");

				if (series.length() != 0 || series != null) {
					for (int i = 0; i < series.length(); i++) {
						for (Map.Entry<String, String> set : keyMap.entrySet()) {
							util.verifykeyAvailable(series.getJSONObject(i), set.getKey(), set.getValue());
							System.out.println(set.getKey() + " = " + set.getValue());
						}
					}
				}
			}
		} catch (Exception je) {
			verify.assertTrue(false, je.toString());
		}

	}

	@Test(groups = { "sanity",
			"mobile" }, description = "fetch yearly estimates", dataProvider = "graphDataYearlyEstimate", dataProviderClass = DataProviderClass.class)

	public void yearlyEstimateTest(String subType) throws Exception {
		try {
			CommonUtil commUtil = new CommonUtil();
			List<String[]> tickers = commUtil.readTickerCSV("sanity.csv");
			for (String[] row : tickers) {
				for (String tickerName : row) {
					tickerName = tickerName.toLowerCase();
					fetchGraphdataYearlyEstimatesapp(subType, tickerName);
				}
			}
		} catch (Exception je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();
		}
	}

}
