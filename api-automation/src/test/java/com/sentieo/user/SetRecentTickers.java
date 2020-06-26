package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

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
import com.sentieo.utils.CoreCommonException;

public class SetRecentTickers extends APIDriver {

	public static ArrayList<String> tickers = new ArrayList<String>(
			Arrays.asList("qure", "lndc", "or:fp", "htgc", "bayn:gr", "awgi", "pmts", "eirl", "mrk:gr", "axsm", "jack",
					"ovbc", "fhn", "cmg", "psix", "tcbi", "ups", "blue", "nem", "gnc", "nee", "lb", "bluu", "tipt",
					"med", "hmta", "atec", "pnqi", "wnrp", "amswa", "met", "hmtv", "hm-b:ss", "tgls", "ssa:ln", "ghdx",
					"aks", "k", "drw", "dri", "drh", "ect", "drn", "tglo", "drd", "glw", "ads:gr", "qcom", "gpor",
					"cohr", "cohu", "apam", "plow", "bdsi", "call", "type", "hwbk", "nke", "yamcy", "aeex", "yahoy",
					"td", "md", "mg", "ma", "mc", "mb", "atkr", "mo", "mn", "mu", "mt", "mx", "czz", "czr"));

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity", "test" }, description = "delete watchlist")
	public void setRecentTickers() throws CoreCommonException {
		String URI = USER_APP_URL + SET_RECENT_TICKERS;
		CommonUtil obj = new CommonUtil();
		Random rand = new Random();
		int rand_int1 = rand.nextInt(tickers.size());
		String ticker = tickers.get(rand_int1);
		String time=obj.getCurrentTimeStamp();
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("tickers", ticker.toLowerCase());
			tickerData.put("time", time);
			tickerData.put("source","streamRender");
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").get("msg"), "Success", "Verify the API Message");
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONArray rTickers = respJson.getJSONObject("result").getJSONArray("rtickers");
				if (rTickers.length() != 0 || rTickers != null) {
					String tickerName = rTickers.getString(0);
					verify.assertEqualsActualContainsExpected(ticker, tickerName, "verify recent ticker");
				}
				String setTicker=userPortfolio("Recent Tickers");
				verify.assertEqualsActualContainsExpected(ticker, setTicker, "verify recent ticker in portfolio call");
			}

		} catch (Exception e) {
			verify.assertTrue(false, e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	public String userPortfolio(String watchName) throws CoreCommonException {
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		String ticker = "";
		HashMap<String, String> tickerData = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		int status = apiResp.getStatusCode();
		if (status == 200) {
			JSONArray getWatchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
			if (getWatchlists.length() != 0 || getWatchlists != null) {
				for (int i = 0; i < getWatchlists.length(); i++) {
					JSONArray allWatchlist = getWatchlists.getJSONArray(i);
					String watchlistName = allWatchlist.getString(0);
					if (watchlistName.equalsIgnoreCase(watchName)) {
						ticker = getWatchlists.getJSONArray(i).getJSONArray(3).get(0).toString();
						return ticker;
					}
				}
			}
		}
		return ticker;
	}

}
