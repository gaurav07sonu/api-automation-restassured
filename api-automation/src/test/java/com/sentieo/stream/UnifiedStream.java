package com.sentieo.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static com.sentieo.constants.Constants.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class UnifiedStream extends APIDriver {

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

		RestAssured.baseURI = USER_APP_URL;
	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "fetch screener models", dataProvider = "fetchUnifiedStream", dataProviderClass = DataProviderClass.class)
	public void fetchUnifiedStream(String client_type, String doc_type, String tickers, String social_reach)
			throws Exception {
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		List<String> tickerList = new ArrayList<>();
		//tickerList.add("goog");
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("social_reach", social_reach);
		tickerData.put("doc_type", doc_type);
		tickerData.put("tickers", tickers);
		tickerData.put("dashboard", "news_stream");
		tickerData.put("client_type_status", client_type);
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		String[] words = tickers.split(",");
		for (String w : words) {
			tickerList.add(w);
		}
		try {
			JSONArray value = respJson.getJSONObject("result").getJSONArray("data");
			if (value.length() != 0) {
				for (int i = 0; i < value.length(); i++) {
					List<String> getTickerFromDoc = new ArrayList<>();
					JSONArray getTicker = value.getJSONObject(i).getJSONArray("tickers");
					if (getTicker.length() != 0) {
						for (int j = 0; j < getTicker.length(); j++) {
							String ticker = getTicker.getString(j);
							getTickerFromDoc.add(ticker);
						}
						boolean noElementsInCommon = Collections.disjoint(tickerList, getTickerFromDoc);
						if (noElementsInCommon) {
							verify.verificationFailures.add(new Exception());
							ExtentTestManager.getTest().log(LogStatus.FAIL, "Ticker not Present in Main TickersList"
									+ "\n" + tickerList + "\n" + getTickerFromDoc);
						}
					} else
						verify.assertTrue(false, "tickers array is blank");
				}
			} else
				verify.assertTrue(false, "API shows blank data");
		} catch (JSONException e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
		finally {
			verify.verifyAll();
		}
	}

}
