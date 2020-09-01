package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
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

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(description = "Plotter stock price Series")
	public void stockPriceMissingValuesForGSPCTicker() throws CoreCommonException {
		try {
			CommonUtil obj = new CommonUtil();
			getDateTime();
			if (dayofweek != 1 && dayofweek != 7) {
				JSONArray value = null;
				String URI = APP_URL + FETCH_GRAPH_DATA;
				String cell = "^gspc";
				cell = cell.toLowerCase();
				parameters.put("head_name", "Stock Price");
				parameters.put("graphtype_original", "stock");
				parameters.put("graphtype", "stock");
				parameters.put("periodtype", "Quarterly");
				parameters.put("ticker", cell);

				RequestSpecification spec = queryParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					if (values.length() != 0) {
						value = values.getJSONArray(values.length() - 1);
						double timestamp = value.getDouble(0);
						int digit = (int) (timestamp / 1000);
						CommonUtil util = new CommonUtil();
						String date = util.convertTimestampIntoDate(digit);
						String str = obj.getDate(0);
						if (!date.contains(str))
							str = obj.getDate(-1);
						verify.compareDates(date, str, "Verify the Current Date Point");
						verify.verifyAll();
					}
				}
				verify.verifyAll();
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
		// mins = Integer.parseInt(hourMin[1]);
	}
}
