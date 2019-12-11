package com.sentieo.plotter;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.sentieo.finance.FinanceApi;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class StockPrice50650 extends APIDriver {

	int dayofweek;
	String time = "";
	int hour;
	int mins;
	String systemDate;
	String morningTime = "";
	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();

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

	@Test(groups = "sanity", description = "Plotter stock price Series")
	public void stockPriceMissingValuesForGSPCTicker() throws CoreCommonException {
		try {
			getDateTime();
			if (dayofweek != 1 && dayofweek != 7) {
				JSONArray value = null;
				String URI = APP_URL + FETCH_GRAPH_DATA;
				String cell = "^gspc";
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
				int statusCode = apiResp.getStatusCode();
				if (statusCode == 200) {
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					if (values.length() != 0) {
						value = values.getJSONArray(values.length() - 1);
						double timestamp = value.getDouble(0);
						int digit = (int) (timestamp / 1000);
						CommonUtil util = new CommonUtil();
						String date = util.convertTimestampIntoDate(digit);
						FinanceApi fin = new FinanceApi();
						CommonUtil comm = new CommonUtil();
						if (hour >= 9 && hour<24)
							systemDate = comm.getCurrentDate();
						else
							systemDate = fin.dateValidationForHistoricalChart("");
						verify.compareDates(date, systemDate, "Verify the Current Date Point");
						verify.verifyAll();
					}
				} else {
					verify.assertTrue(false, "status code is : " + statusCode);
					verify.verifyAll();
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"Skip test because of data is not updated on  : " + dayofweek + "day");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	public void getDateTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("kk:mm aa");
		sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		time = sdf.format(calendar.getTime());
		if (time.contains("AM"))
			morningTime = time.replaceAll("AM", "");
		else
			morningTime = time.replaceAll("PM", "");
		String[] hourMin = morningTime.split(":");
		hour = Integer.parseInt(hourMin[0]);
		//mins = Integer.parseInt(hourMin[1]);
	}
}
