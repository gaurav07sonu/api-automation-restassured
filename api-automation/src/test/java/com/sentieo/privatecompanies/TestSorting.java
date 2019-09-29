package com.sentieo.privatecompanies;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;

public class TestSorting extends APIDriver {

	int firstCount = 0;
	HashMap<String, String> parameters = new HashMap<String, String>();
	APIAssertions verify = new APIAssertions();
	boolean flag = true;

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

	public void getSortKey(String order, String URI, String ticker) throws Exception {
		CommonUtil commUtil=new CommonUtil();
		if (flag)
			commUtil.generateRandomTickers();
		flag = false;
		JSONArray shortExitsArray = null;
		String resultNode = "";
		parameters.put("num_rows", "all");
		parameters.put("ticker", ticker);
		parameters.put("sort_order", order);
		RequestSpecification spec = formParamsSpec(parameters);
		Response resp = RestOperationUtils.get(URI, spec, parameters);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		JSONArray mappingArray = respJson.getJSONObject("result").getJSONArray("mapping");
		for (int i = 0; i < mappingArray.length(); i++) {
			JSONObject json = mappingArray.getJSONObject(i);
			Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				String key = keys.next();
				if (key.contains("sort_key")) {
					List<BigInteger> sortingIntegerValue = new ArrayList<BigInteger>();
					List<String> sortingString = new ArrayList<String>();
					List<BigInteger> newList = new ArrayList<BigInteger>(sortingIntegerValue);
					List<String> newList2 = new ArrayList<String>(sortingString);
					Object obj = json.get(key);
					if (obj != JSONObject.NULL) {
						String seriesValue = (String) json.get(key);
						parameters.put("sort_key", seriesValue);
						spec = formParamsSpec(parameters);
						resp = RestOperationUtils.get(URI, spec, parameters);
						apiResp = new APIResponse(resp);
						respJson = new JSONObject(apiResp.getResponseAsString());
						verify.verifyStatusCode(apiResp.getStatusCode(), 200);
						verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
								"Verify the API Response Status");
						verify.verifyResponseTime(resp, 5000);
						if (URI.contains("/api/fetch_cb_exit_table/"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("exits");
						resultNode = "exits";
						if (URI.contains("/api/fetch_cb_fr_table/"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("funding_rounds");
						resultNode = "funding_rounds";
						if (URI.contains("/api/fetch_cb_acq_table/"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("acquisitions");
						resultNode = "acquisitions";
						if (URI.contains("/api/fetch_cb_funds_table/"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("funds");
						resultNode = "funds";
						if (URI.contains("/api/fetch_cb_key_investors/"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("investors");
						resultNode = "investors";
						if (URI.contains("fetch_cb_fr_table"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("funding_rounds");
						resultNode = "funding_rounds";

						if (URI.contains("fetch_cb_inv_table"))
							shortExitsArray = respJson.getJSONObject("result").getJSONArray("investments");
						resultNode = "investments";

						for (int j = 0; j < shortExitsArray.length(); j++) {
							String path = "result." + resultNode + "[" + j + "]." + seriesValue;
							// String path = "result." + resultNode + "[" + j + "]." +"money_raised_usd";

							String result = String.valueOf(apiResp.getNodeValue(path));
							if (result != null && !result.isEmpty()) {
								if (result != null && !result.isEmpty()) {
									if (CommonUtil.isNumber(result)) {
										BigInteger bigIntegerStr = new BigInteger(result);
										// int value = bigIntegerStr.intValue();
										// int value = Integer.parseInt(result);
										sortingIntegerValue.add(bigIntegerStr);
										newList = new ArrayList<BigInteger>(sortingIntegerValue);
										if (order.contains("asc"))
											Collections.sort(sortingIntegerValue);
										else
											Collections.sort(sortingIntegerValue, Collections.reverseOrder());
									} else {
										sortingString.add(result);
										newList2 = new ArrayList<String>(sortingString);
										if (order.contains("asc"))
											Collections.sort(sortingString);
										else
											Collections.sort(sortingString, Collections.reverseOrder());
									}
								}
							}

						}
						if (!sortingIntegerValue.isEmpty())
							verify.assertEquals(newList, sortingIntegerValue, "verify sorting" + seriesValue);
						if (!sortingString.isEmpty())
							verify.assertEquals(newList2, sortingString, "verify sorting" + seriesValue, true);
					}

				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "private tickers", dataProvider = "sortingData", dataProviderClass = DataProviderClass.class)
	public void performSorting(String sortOrder, String api) throws CoreCommonException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				String URI = APP_URL + api;
				getSortKey(sortOrder, URI, ticker);
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
}