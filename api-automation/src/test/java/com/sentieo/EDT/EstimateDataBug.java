package com.sentieo.EDT;

import static com.sentieo.constants.Constants.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.sentieo.utils.CoreCommonException;

public class EstimateDataBug extends APIDriver {
	HashMap<String, String> tickerData = new HashMap<String, String>();
	HashMap<String, String> parameters = new HashMap<String, String>();
	int EstimateDateInInteger;
	String estimateDate;
	String date;
	int stockStartYear;
	String expectedOutcome;
	int yearIncrease = 0;
	String seriesTitle;
	int seriesTitleIndex = 0;
	int seriesTitleIndexForTestTwo = 0;

	static String ticker;
	boolean flag = true;
	boolean flagTest = true;
	boolean thirdTest = true;
	boolean fourthTest = true;
	boolean fifthTest = true;

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

	public void fetchGraphdataYearlyEstimatesapp(String cell, String seriesName, int seriesIndex) throws Exception {
		String URI = APP_URL + FETCH_GRAPH_DATA;
		cell = cell.toLowerCase();
		tickerData.put("ticker", cell);
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
		if (getSeries.length() != 0) {
			String path = "result." + "series" + "[" + seriesIndex + "]." + "title";
			String length = "result." + "series" + "[" + seriesIndex + "]." + "series";
			seriesTitle = String.valueOf(apiResp.getNodeValue(path));
			String seriesLength = String.valueOf(apiResp.getNodeValue(length));
			JSONArray seriesSize = respJson.getJSONObject("result").getJSONArray("series");
			JSONArray estimates = seriesSize.getJSONObject(seriesIndex).getJSONArray("series");
			if (seriesSize.length() == 0)
				verify.assertTrue(false, "series size is :" + seriesSize + "for ticker :" + cell);

			if (estimates.length() == 0)
				verify.assertTrue(false, "estimates series size is :" + estimates + "for ticker :" + cell);

			boolean value = (seriesLength.length() != 0);
			if (!value)
				verify.assertTrue(value, "verify series length" + "series length is : " + seriesLength.length());

			if (seriesLength.length() == 1 || seriesLength.length() == 0)
				verify.assertTrue(false, "verify series length" + "series length is : " + seriesLength.length()
						+ "estimate series is : " + estimates.length() + "for ticker : " + cell);
			verify.assertEqualsActualContainsExpected(seriesTitle, seriesName, "verify series title");
		}
		verify.verifyAll();

	}

	public void returnStockStartDate(String cell, int count) throws CoreCommonException {
		String URI = APP_URL + FETCH_GRAPH_DATA;
		cell = cell.toLowerCase();
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
		JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
				.getJSONArray("series");
		JSONArray value = values.getJSONArray(0);
		double timestamp = value.getDouble(0);
		int digit = (int) (timestamp / 1000);
		CommonUtil util = new CommonUtil();
		stockStartYear = util.getYearFromTimeStamp(timestamp);
		date = util.convertTimestampIntoDate(digit);
		estimateDate = new SimpleDateFormat("m/d/yy").format(Calendar.getInstance().getTime());
		String estimateYear = new SimpleDateFormat("yy").format(Calendar.getInstance().getTime());
		EstimateDateInInteger = Integer.parseInt(estimateYear);
		EstimateDateInInteger = EstimateDateInInteger - 5;
		if (count == 0)
			expectedOutcome = "Diluted EPS-" + stockStartYear;
		else {
			stockStartYear = stockStartYear + count;
			expectedOutcome = "Diluted EPS-" + stockStartYear;
		}
	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "on-22", dataProviderClass = DataProviderClass.class, priority = 0)
	public void startAndEntDate(String seriesName, String cell) throws Exception {
		if (seriesTitleIndex == 0)
			ticker = cell;
		if (cell.contains(ticker)) {
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndex);
			seriesTitleIndex++;
		} else {
			if (flag) {
				seriesTitleIndex = 0;
				flag = false;
			}
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndex);
			seriesTitleIndex++;
		}

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "EDTEstimates14-21", dataProviderClass = DataProviderClass.class, priority = 1)
	public void startAndEntDateTest14To21(String seriesName, String cell) throws Exception {
		cell = cell.toLowerCase().trim();
		if (seriesTitleIndexForTestTwo == 0)
			ticker = cell;
		if (cell.contains(ticker)) {
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		} else {
			if (flagTest) {
				seriesTitleIndexForTestTwo = 0;
				flagTest = false;
			}
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		}

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "EDTEstimates19-21", dataProviderClass = DataProviderClass.class, priority = 2)
	public void startAndEntDateTest(String seriesName, String cell) throws Exception {
		cell = cell.toLowerCase().trim();
		if (seriesTitleIndexForTestTwo == 0)
			ticker = cell;
		if (cell.contains(ticker)) {
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		} else {
			if (thirdTest) {
				seriesTitleIndexForTestTwo = 0;
				thirdTest = false;
			}
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		}

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "EDTEstimates16-21", dataProviderClass = DataProviderClass.class, priority = 3)
	public void startAndEntDateTest16to21(String seriesName, String cell) throws Exception {
		cell = cell.toLowerCase().trim();
		if (seriesTitleIndexForTestTwo == 0)
			ticker = cell;
		if (cell.contains(ticker)) {
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		} else {
			if (fourthTest) {
				seriesTitleIndexForTestTwo = 0;
				fourthTest = false;
			}
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		}

	}

	@Test(groups = "sanity", description = "fetch yearly estimates", dataProvider = "EDTEstimates14-20", dataProviderClass = DataProviderClass.class, priority = 4)
	public void startAndEntDateTest14to20(String seriesName, String cell) throws Exception {
		cell = cell.toLowerCase().trim();
		if (seriesTitleIndexForTestTwo == 0)
			ticker = cell;
		if (cell.contains(ticker)) {
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		} else {
			if (fifthTest) {
				seriesTitleIndexForTestTwo = 0;
				fifthTest = false;
			}
			fetchGraphdataYearlyEstimatesapp(cell, seriesName, seriesTitleIndexForTestTwo);
			seriesTitleIndexForTestTwo++;
		}

	}

}
