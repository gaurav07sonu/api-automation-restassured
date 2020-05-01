package com.sentieo.EDT;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class TestTradingMultiples extends APIDriver {

	HashMap<String, String> tickerData = new HashMap<String, String>();
	JSONArray appSeries;
	JSONArray app2Series;
	String errorMsgAPP = "";
	public ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("baba", "aapl", "700:hk", "7203:jp"));


	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	public void fetchGraphdataMultiplesapp(String ratio, String shift, String ticker) throws Exception {
		String financialPeriod = "";
		CommonUtil com = new CommonUtil();
		tickerData.put("ticker", ticker);
		tickerData.put("graphtype", "TradingMultiples");
		tickerData.put("ratio", ratio);
		tickerData.put("shift", shift);
		tickerData.put("ptype", "rolling");
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		int statusCode = apiResp.getStatusCode();
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		errorMsgAPP = respJson.getJSONObject("response").getJSONArray("msg").toString();
		if (statusCode == 200) {
			JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
					.getJSONArray("series");
			for (int i = 0; i < values.length(); i++) {
				if (values.length() - 1 != i) {

					Double firstValue = (Double) values.getJSONArray(i).get(1);
					Double secondValue = (Double) values.getJSONArray(i + 1).get(1);
					if (firstValue >= secondValue) {
						if (shift.contains("backward"))
							financialPeriod = "Next 12 Months";
						if (shift.contains("blended"))
							financialPeriod = "ntm-time weighted annual";
						if (firstValue == -999999.0 || secondValue == -999999.0)
							verify.assertTrue(false,
									"<b>" + "verify negative value : " + "<br/>" + "<b>" + " first value is: "
											+ firstValue + "<br/>" + "<b>" + " second value is : " + secondValue + "<b>"
											+ "<br/>" + " for ticker : " + ticker + "<b>" + " Financial Period is : "
											+ financialPeriod + "<br/>");

						Double postivePerChnage = com.getpostivePercentageChange(firstValue, secondValue);
						if (postivePerChnage > 25) {
							verify.assertTrue(false,
									"<b>" + "verify increment of value percentage : " + "<b>" + postivePerChnage
											+ "<br/>" + "<b>" + " for ticker : " + ticker + "<br/>"
											+ " first value is : " + firstValue + "<br/>" + "<b>"
											+ " second value is : " + secondValue+ "<b>" + " Financial Period is : "
											+ financialPeriod + "<br/>");
						}
					} else {
						if (firstValue == -999999.0 || secondValue == -999999.0)
							verify.assertTrue(false,
									"<b>" + "verify negative value : " + "<br/>" + "<b>" + " first value is: "
											+ firstValue + "<br/>" + "<b>" + " second value is : " + secondValue + "<b>"
											+ "<br/>" + " for ticker : " + ticker+ "<b>" + " Financial Period is : "
											+ financialPeriod + "<br/>");
						Double negativePerChnage = com.getnegativePercentageChange(firstValue, secondValue);
						if (negativePerChnage > 25) {
							verify.assertTrue(false,
									"<b>" + "verify decrement of value percentage : " + "<b>" + negativePerChnage
											+ "<br/>" + "<b>" + " for ticker : " + ticker + "<br/>"
											+ " first value is : " + firstValue + "<br/>" + "<b>"
											+ " second value is : " + secondValue+ "<b>" + " Financial Period is : "
											+ financialPeriod + "<br/>");
						}
					}
				}
			}
		}

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "tradingMultiplesLTMNTM", dataProviderClass = DataProviderClass.class)
	public void yearlyEstimateTest(String ratio, String shift) throws Exception {
		for (int i = 0; i < tickers.size(); i++) {
			fetchGraphdataMultiplesapp(ratio, shift, tickers.get(i));
		}
		verify.verifyAll();
	}

}
