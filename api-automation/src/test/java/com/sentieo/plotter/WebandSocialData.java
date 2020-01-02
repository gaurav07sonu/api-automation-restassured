package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.finance.InputTicker;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class WebandSocialData extends APIDriver {
	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();
	// public ArrayList<String> tickers = new
	// ArrayList<String>(Arrays.asList("aapl", "amzn"));
	String actualMSG = "Success";
	String yAxisActual = "Alexa Reach (per million internet users)";
	String gtrendsYAxisActual = "Google Trends";
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();
	String no_mapping = "";

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

	 @Test(description = "Plotter Web and Social Data Series")
	public void googleTrends() throws CoreCommonException {
		try {
			Calendar calNewYork = Calendar.getInstance();
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek != 1 && dayofweek != 2 && dayofweek != 3 && dayofweek != 7) {
				String URI = APP_URL + GET_GTRENDS;
				for (String[] row : tickers) {
					for (String cell : row) {
						cell = cell.toLowerCase();
						String isMapping = getMapping(cell);
						if (!isMapping.contains("true")) {
							parameters.put("head_name", "Google Trends");
							parameters.put("pagetype", "plotter");
							parameters.put("graphtype", "gtrends");
							parameters.put("ticker", cell);
							RequestSpecification spec = queryParamsSpec(parameters);
							Response resp = RestOperationUtils.get(URI, spec, parameters);
							APIResponse apiResp = new APIResponse(resp);
							JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
							verify.verifyStatusCode(apiResp.getStatusCode(), 200);
							verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
									"Verify the API Response Status");
							verify.verifyResponseTime(resp, 5000);
							JSONObject getSeries = respJson.getJSONObject("result").getJSONArray("series")
									.getJSONObject(0);
							String gtrendsYAxis = getSeries.getString("yaxis");
							verify.assertEqualsActualContainsExpected(gtrendsYAxisActual, gtrendsYAxis,
									"match series name");
							JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
									.getJSONArray("series");
							JSONArray value = values.getJSONArray(values.length() - 1);
							double timestamp = value.getDouble(0);
							int digit = (int) (timestamp / 1000);
							CommonUtil util = new CommonUtil();
							String date = util.convertTimestampIntoDate(digit);
							String currentDate = util.dateValidationForChart();
							verify.assertEqualsActualContainsExpected(date, currentDate, "");
						}
					}
				}
				verify.verifyAll();

			}

			else {
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"Skip test because of data is not updated on  : " + dayofweek + "day");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

	 @Test( description = "Plotter Web and Social Data Series")
	public void websiteTraffic() throws CoreCommonException {
		try {
			String URI = APP_URL + WEBSITETRAFFIC;
			HashMap<String, String> parameters = new HashMap<String, String>();
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					parameters.put("url", "");
					parameters.put("ticker", cell);
					parameters.put("pagetype", "plotter");
					parameters.put("datatype", "page_views");
					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(URI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					String msg = respJson.getJSONObject("response").get("msg").toString().replaceAll("\\[", "")
							.replaceAll("\\]", "").replace("\"", " ");
					verify.assertEqualsActualContainsExpected(msg, actualMSG, "match response msg");
					verify.verifyResponseTime(resp, 5000);
					JSONObject getSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0);
					String yAxis = getSeries.getString("yaxis");
					verify.assertEqualsActualContainsExpected(yAxisActual, yAxis, "match series name");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

	public String getMapping(String cell) throws CoreCommonException {
		try {
			HashMap<String, String> gtrends = new HashMap<String, String>();
			gtrends.put("ticker", cell);
			gtrends.put("rds", "true");
			gtrends.put("termtype", "ticker");
			RequestSpecification spec = formParamsSpec(gtrends);
			Response resp = RestOperationUtils.post(MAPPING, null, spec, gtrends);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			try {
				no_mapping = respJson.getJSONObject("result").get("no_mapping").toString();
				return no_mapping;
			} catch (Exception e) {
				no_mapping = "false";
				return no_mapping;
			}
		} catch (Exception e) {
			// throw new CoreCommonException(e.getMessage());
			// System.out.println(e.toString());
		}
		return no_mapping;
	}
}
