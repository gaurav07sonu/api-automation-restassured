package com.sentieo.EDT;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.finance.InputTicker;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class EstimateDataBug extends APIDriver {
	HashMap<String, String> tickerData = new HashMap<String, String>();
	ArrayList<String> seriesName = new ArrayList<>();
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

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

	@Test(groups = "sanity", description = "private tickers")
	public void fetchGraphdataYearlyEstimatesapp() throws Exception {
		seriesName.add("Diluted EPS-2014");
		seriesName.add("Diluted EPS-2015");
		seriesName.add("Diluted EPS-2016");
		seriesName.add("Diluted EPS-2017");
		seriesName.add("Diluted EPS-2018");
		seriesName.add("Diluted EPS-2019");
		seriesName.add("Diluted EPS-2020");
		seriesName.add("Diluted EPS-2021");
		seriesName.add("Diluted EPS-2022");

		String URI = APP_URL + FETCH_GRAPH_DATA;
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();

		tickerData.put("ticker",cell);
		tickerData.put("graphtype", "yearlyEstimate");
		tickerData.put("subtype", "DilutedEPSTotal");
		tickerData.put("startyear", "2014");
		tickerData.put("endyear", "2022");
		tickerData.put("startyear", "2014");
		tickerData.put("endyear", "2022");
		tickerData.put("getstock", "true");
		tickerData.put("ptype", "q5");
		tickerData.put("next4", "true");
		RequestSpecification spec = queryParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, tickerData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
		JSONArray getSeries = respJson.getJSONObject("result").getJSONArray("series");
		if (getSeries.length() != 0)
			for (int j = 0; j < getSeries.length(); j++) {
				String path = "result." + "series" + "[" + j + "]." + "title";
				String length = "result." + "series" + "[" + j + "]." + "series";
				String seriesTitle = String.valueOf(apiResp.getNodeValue(path));
				String seriesLength = String.valueOf(apiResp.getNodeValue(length));
				JSONArray seriesSize = respJson.getJSONObject("result").getJSONArray("series");
				JSONArray estimates = seriesSize.getJSONObject(j).getJSONArray("series");
				if (seriesSize.length() == 0)
					verify.assertTrue(false, "series size is :" + seriesSize + "for ticker :"+cell);
				
				if(estimates.length()==0)
					verify.assertTrue(false, "estimates series size is :" + estimates + "for ticker :"+cell);

				boolean value = (seriesLength.length() != 0);
				if (!value)
					verify.assertTrue(value, "verify series length" + "series length is : " + seriesLength.length());

				if (seriesLength.length() == 1 || seriesLength.length() == 0)
					verify.assertTrue(false, "verify series length" + "series length is : "  + seriesLength.length() +"estimate series is : " + estimates.length() +"for ticker : "+cell);

				if (seriesTitle.contains("Diluted EPS-2014"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2014", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2015"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2015", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2016"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2016", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2017"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2017", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2018"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2018", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2019"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2019", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2020"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2020", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2021"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2021", "verify series title");
				if (seriesTitle.contains("Diluted EPS-2022"))
					verify.assertEqualsActualContainsExpected(seriesTitle, "Diluted EPS-2022", "verify series title");
			}
		else
			verify.assertTrue(false, "seires length is : " + getSeries.length());
			}
		}
		verify.verifyAll();

	}

}
