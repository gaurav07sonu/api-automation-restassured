package com.sentieo.stream;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_LIVE_PRICE;
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
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class FetchLivePrice extends APIDriver {

	APIResponse apiResp = null;
	Response resp = null;
	public static ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr", "awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack",
					"ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue", "nem", "gnc", "nee", "lb", "bluu", "tipt",
					"med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv", "hm-b:ss", "tgls", "ssa:ln", "ghdx",
					"aks", "k", "drw", "dri", "drh", "ect", "drn", "tglo", "drd", "glw", "ads:gr", "qcom", "gpor",
					"cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
					"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));

//	public static ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("qure", "asia"));


	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch live price")
	public void fetchLivePrice() throws CoreCommonException {
		int i = 0;
		String URI = APP_URL + FETCH_LIVE_PRICE;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = tickers.toString();
			ticker = ticker.replaceAll("\\[", "").replaceAll("\\]", "");
			parameters.put("tickers", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result").getJSONObject("result");
			if (result.length() == 0 || result == null)
				verify.assertTrue(false, "API shows blank data");
			else {
				try {
					for (i = 0; i < tickers.size(); i++) {
						JSONArray tickerData = respJson.getJSONObject("result").getJSONObject("result")
								.getJSONArray(tickers.get(i).toUpperCase());
						if (tickerData.length() == 0 || tickerData == null)
							verify.assertTrue(false, "verify ticker data");
					}
				} catch (Exception e) {
					verify.assertTrue(false, tickers.get(i) + " ticker not found");
				}
			}
		} catch (Error e) {
			verify.assertTrue(false, tickers.get(i) + " ticker not found");

		} finally {
			verify.verifyAll();
		}

	}
}
