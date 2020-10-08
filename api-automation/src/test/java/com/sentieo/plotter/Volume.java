package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
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

public class Volume extends APIDriver {
	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(description = "Match stock price plotter series and stream call ")
	public void volume() throws CoreCommonException {
		try {
			CommonUtil obj = new CommonUtil();
			String expectedDate = obj.getDate(0, "");
			Calendar calNewYork = Calendar.getInstance();
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek != 1 && dayofweek != 7) {
				JSONArray value = null;
				String stock_URI = APP_URL + FETCH_GRAPH_DATA;
				for (String[] row : tickers) {
					for (String cell : row) {
						cell = cell.toLowerCase();
						parameters.put("summary", "true");
						parameters.put("yearly", "1");
						parameters.put("new_wl", "true");
						parameters.put("ticker", cell);
						HashMap<String, String> stock_Parameters = new HashMap<String, String>();
						stock_Parameters.put("head_name", "Volume");
						stock_Parameters.put("pagetype", "plotter");
						stock_Parameters.put("graphtype", "stock");
						stock_Parameters.put("volume_only", "true");
						stock_Parameters.put("stack", "0");
						stock_Parameters.put("dma", "0");
						stock_Parameters.put("median", "0");
						stock_Parameters.put("yoy_rt", "0");
						stock_Parameters.put("qoq_rt", "0");
						stock_Parameters.put("outliers", "0");
						stock_Parameters.put("day_dma", "0");
						stock_Parameters.put("yUnit", "undefined");
						stock_Parameters.put("ticker", cell);
						stock_Parameters.put("freq_set1", "");
						stock_Parameters.put("freq_type1", "mean");
						stock_Parameters.put("counter", "1");
						stock_Parameters.put("loc", "app");
						RequestSpecification specVolume = queryParamsSpec(stock_Parameters);
						Response resp = RestOperationUtils.get(stock_URI, specVolume, stock_Parameters);
						APIResponse apiRespVolume = new APIResponse(resp);
						verify.verifyStatusCode(apiRespVolume.getStatusCode(), 200);
						if (apiRespVolume.getStatusCode() == 200) {
							JSONObject respJsonVolume = new JSONObject(apiRespVolume.getResponseAsString());
							verify.verifyEquals(respJsonVolume.getJSONObject("response").getBoolean("status"), true,
									"Verify the API Response Status");
							verify.verifyResponseTime(resp, 5000);
							JSONArray values = respJsonVolume.getJSONObject("result").getJSONArray("series")
									.getJSONObject(0).getJSONArray("series");
							if (values.length() != 0) {
								value = values.getJSONArray(values.length() - 1);
								double timestamp = value.getDouble(0);
								int digit = (int) (timestamp / 1000);
								CommonUtil util = new CommonUtil();
								String date = util.convertTimestampIntoDate(digit);

								if (!date.contains(expectedDate))
									expectedDate = obj.getDate(-1, "keyMultiples");

								if (!date.contains(expectedDate))
									expectedDate = obj.getDate(-2, "keyMultiples");

								if (!date.contains(expectedDate))
									expectedDate = obj.getDate(-3, "keyMultiples");

								if (!date.contains(expectedDate))
									expectedDate = obj.getDate(-4, "keyMultiples");

								verify.compareDates(date, expectedDate, "Verify the Current Date Point ");

							} else
								verify.assertTrue(false, "Series shows blank data for " + cell);
						} else
							verify.assertTrue(false, "Status code is not 200 ");
					}
				}
			} else
				ExtentTestManager.getTest().log(LogStatus.INFO,
						"Skip test because of data is not updated on  : " + dayofweek + "day");
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}
}