package com.sentieo.plotter;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.lang.reflect.Method;

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
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class TestpercentageChange extends APIDriver {

	@BeforeMethod(alwaysRun=true)
	public void initVerify(Method testMethod) {
		verify = new APIAssertions();
		CommonUtil commUtil = new CommonUtil();
		commUtil.generateRandomTickers(testMethod);
	}
	public void keyMultiples(String headName, String graphType, String ticker) throws Exception {
		CommonUtil com=new CommonUtil();
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + FETCH_GRAPH_DATA;
		parameters.put("head_name", headName);
		parameters.put("graphtype_original", graphType);
		parameters.put("ratio", graphType);
		parameters.put("ticker", ticker);
		if (headName.contains("Enterprise Value") || headName.contains("Market Cap")) {
			parameters.put("graphtype", "newratio");
		}
		if (headName.contains("P/Tangible Book Value")) {
			parameters.put("shift", "backward");
			parameters.put("graphtype", "newratioestimate");
			parameters.put("ptype", "rolling");
		} else {
			parameters.put("shift", "backward");
			parameters.put("ptype", "rolling");
			parameters.put("ratio_name", "NTM");
			parameters.put("graphtype", "newratioestimate");
		}
		RequestSpecification spec = queryParamsSpec(parameters);
		Response resp = RestOperationUtils.get(URI, spec, parameters);
		APIResponse apiResp = new APIResponse(resp);
		int statusCode = apiResp.getStatusCode();
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		if (statusCode == 200) {
			JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
					.getJSONArray("series");
			if (values.length() != 0) {
				for (int i = 0; i < values.length(); i++) {
					if(values.length()-1!=i)
					{
					Double firstValue = (Double) values.getJSONArray(i).get(1);
					Double secondValue = (Double) values.getJSONArray(i + 1).get(1);
					if (firstValue >= secondValue) {
						Double postivePerChnage = com.getpostivePercentageChange(firstValue,secondValue);
						if (postivePerChnage > 49) {
							verify.assertTrue(false,
									"<b>" + "verify increment of value percentage : " + "<b>" + postivePerChnage + "<br/>"
											+ "<b>" + " for ticker : " + ticker + "<br/>" + " first value is : "
											+ firstValue + "<br/>" + "<b>" + " second value is : " + secondValue);
						}
					} else {
						Double negativePerChnage = com.getnegativePercentageChange(firstValue,secondValue);
						if (negativePerChnage > 49) {
							verify.assertTrue(false,
									"<b>" + "verify decrement of value percentage : " + "<b>" + negativePerChnage + "<br/>"
											+ "<b>" + " for ticker : " + ticker + "<br/>" + " first value is : "
											+ firstValue + "<br/>" + "<b>" + " second value is : " + secondValue);
						}
					}
				}
				}
			
			}
		} else {
			verify.assertTrue(false, "status code is not 200 " + statusCode);
		}
	}

	@Test(description = "Check latest data points for P/Book Value")
	public void keyMultiplesP_CashFlow() throws Exception {
	for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
		String ticker = tickerValue.getValue();
		ticker = ticker.toLowerCase();
			keyMultiples("P/Cash Flow","price_cashflow", ticker);
		}
		verify.verifyAll();
	}
}
