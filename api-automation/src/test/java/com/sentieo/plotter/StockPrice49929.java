package com.sentieo.plotter;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.util.HashMap;

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

public class StockPrice49929 extends APIDriver {

	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();

	@BeforeClass(alwaysRun=true)
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

	@BeforeMethod(alwaysRun=true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(description = "Plotter stock price Series")
	public void stockPriceShouldReturnDataForOneTicker() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_GRAPH_DATA;
			String cell = "^ixic,aapl,shs";
			cell=cell.toLowerCase();
			parameters.put("head_name", "Stock Price");
			parameters.put("graphtype_original", "stock");
			parameters.put("graphtype", "stock");
			parameters.put("periodtype", "Quarterly");
			parameters.put("ticker", cell);

			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONObject firstTickerSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0);
			JSONObject secondTickerSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1);
			JSONObject thirdTickerSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(2);

			String firstSeriesTitle = firstTickerSeries.getString("title").replaceAll(" ", "");
			String secondSeriesTitle = secondTickerSeries.getString("title").replaceAll(" ", "");
			String thirdSeriesTitle = thirdTickerSeries.getString("title").replaceAll(" ", "");

			String actualFirstSeriesTitle = cell.toUpperCase() + parameters.get("head_name").replaceAll(" ", "");
			String actualSecondSeriesTitle = cell.toUpperCase() + parameters.get("head_name").replaceAll(" ", "");
			String actualThirdSeriesTitle = cell.toUpperCase() + parameters.get("head_name").replaceAll(" ", "");

			String actualFirst = actualFirstSeriesTitle.replaceAll("AAPL", "");
			actualFirst = actualFirst.replaceAll("SHS", "");
			actualFirst = actualFirst.replaceAll(",", "");

			String actualSecond = actualSecondSeriesTitle.replaceAll("IXIC", "");
			actualSecond = actualSecond.replaceAll("SHS", "");
			actualSecond = actualSecond.replaceAll("\\^", "");
			actualSecond = actualSecond.replaceAll(",", "");

			String actualThird = actualThirdSeriesTitle.replaceAll("AAPL", "");
			actualThird = actualThird.replaceAll("IXIC", "");
			actualThird = actualThird.replaceAll("\\^", "");
			actualThird = actualThird.replaceAll(",", "");

			verify.verifyEquals(actualSecond, secondSeriesTitle, "Verify Series Title");
			verify.verifyEquals(actualFirst, firstSeriesTitle, "Verify Series Title");
			verify.verifyEquals(actualThird, thirdSeriesTitle, "Verify Series Title");

			if (secondTickerSeries.length() == 0)
				verify.assertTrue(false, "series have no data : ");
			verify.verifyAll();

		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

}
