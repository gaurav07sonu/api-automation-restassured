package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.finance.FinanceApi;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class TestDailySeriesData extends APIDriver {

//	public ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("nvt", "mdlz", "bk", "syy", "ge", "pg", "lm", "ppg", "wen", "aapl"));
//	public ArrayList<String> tickers = new ArrayList<String>(
//			Arrays.asList("wen", "blue", "eqt", "tcl:au", "wy", "gmg:au"));
	String systemDate;

	@BeforeMethod(alwaysRun = true)
	public void initVerify(Method testMethod) {
		verify = new APIAssertions();
		CommonUtil commUtil = new CommonUtil();
		commUtil.generateRandomTickers(testMethod);
	}

	public void keyMultiples(String headName, String graphType, String ticker) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yy");
		Date cal = addDays(new Date(), 0);
		String expectedDate = formatter.format(cal.getTime());
		JSONArray value = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + FETCH_GRAPH_DATA;
		ticker = ticker.toLowerCase();
		parameters.put("head_name", headName);
		parameters.put("graphtype_original", graphType);
		parameters.put("ratio", graphType);
		parameters.put("ticker", ticker);

		if (headName.contains("Enterprise Value") || headName.contains("Market Cap"))
			parameters.put("graphtype", "newratio");

		else if (headName.contains("P/Cash Flow")) {
			parameters.put("ratio_name", "NTM Rolling");
			parameters.put("shift", "blended");
			parameters.put("ptype", "rolling");
			parameters.put("ratio", graphType);
			parameters.put("datasource", "rf");
			parameters.put("graphtype", "newratioestimate");
		}

		else if (headName.contains("P/Tangible Book Value")) {
			parameters.put("shift", "blended");
			parameters.put("graphtype", "newratioestimate");
			parameters.put("ptype", "rolling");
			parameters.put("datasource", "rf");
			parameters.put("graphtype_original", "tang_bookvalue");

		}

		else if (headName.contains("EV/(EBITDA-CAPEX)")) {
			parameters.put("shift", "blended");
			parameters.put("graphtype_original", "ev_ebitdacapex");
			parameters.put("ptype", "rolling");
			parameters.put("ratio", "ev_ebitdacapex");
			parameters.put("ratio_name", "NTM Rolling");
			parameters.put("datasource", "rf");
			parameters.put("graphtype", graphType);
		}

		else {
			parameters.put("shift", "backward");
			parameters.put("ptype", "rolling");
			parameters.put("ratio_name", "NTM");
			parameters.put("graphtype", "newratioestimate");
		}
		RequestSpecification spec = queryParamsSpec(parameters);
		Response resp = RestOperationUtils.get(URI, spec, parameters);
		APIResponse apiResp = new APIResponse(resp);
		int statusCode = apiResp.getStatusCode();
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
		verify.verifyResponseTime(resp, 5000);
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
				if (!date.contains(expectedDate)) {
					cal = addDays(new Date(), -1);
					expectedDate = formatter.format(cal.getTime());
					if (!date.contains(expectedDate)) {
						cal = addDays(new Date(), -2);
						expectedDate = formatter.format(cal.getTime());
						verify.compareDates(date, expectedDate, "Verify the Current Date Point");
					}

				} else {
					systemDate = fin.dateValidationForHistoricalChart("fetch_main_graph", ticker);
					verify.compareDates(date, systemDate, "Verify the Current Date Point");
				}
			}
		} else {
			verify.assertTrue(false, "status code is not 200 " + statusCode);
		}
	}

	@Test(description = "Check latest data points for daily series", dataProvider = "plotterDailySeries", dataProviderClass = DataProviderClass.class, priority = 0)
	public void keyMultiplesNTM(String headName, String graphType) throws Exception {
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String ticker = tickerValue.getValue();
				ticker = ticker.toLowerCase();
				keyMultiples(headName, graphType, ticker);
			}
			verify.verifyAll();
		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"Skip test because of data is not updated on  : " + Calendar.DAY_OF_WEEK + "day");
		}
	}

	@Test(description = "Check latest data points for Tangible Book Value perShare", priority = 1)
	public void keyMultiplesTangibleBookValueNTM() throws Exception {
		for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
			String ticker = tickerValue.getValue();
			ticker = ticker.toLowerCase();
			keyMultiples("P/Tangible Book Value", "tang_bookvalue", ticker);
		}
		verify.verifyAll();
	}

	@Test(description = "Check latest data points for EV/GROSS PROFIT", priority = 2)
	public void keyMultiplesEVGROSSPROFIT() throws Exception {
		for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
			String ticker = tickerValue.getValue();
			ticker = ticker.toLowerCase();
			keyMultiples("EV/GROSS PROFIT", "ev_grossprofit", ticker);
		}
		verify.verifyAll();
	}

	@Test(description = "Check latest data points for EV/(EBITDA-CAPEX)", priority = 3)
	public void keyMultiplesEVEBITDA_CAPEX() throws Exception {
		for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
			String ticker = tickerValue.getValue();
			ticker = ticker.toLowerCase();
			keyMultiples("EV/(EBITDA-CAPEX)", "newratioestimate", ticker);
		}
		verify.verifyAll();
	}

	@Test(description = "Check latest data points for P/Book Value", priority = 4)
	public void keyMultiplesP_BookValue() throws Exception {
		for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
			String ticker = tickerValue.getValue();
			ticker = ticker.toLowerCase();
			keyMultiples("P/Book Value", "price_bookvalue", ticker);
		}
		verify.verifyAll();
	}

	public Date addDays(Date date, Integer days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 7)
			cal.add(Calendar.DAY_OF_MONTH, -1);

		if (dayOfWeek == 1)
			cal.add(Calendar.DAY_OF_MONTH, -2);
		return cal.getTime();

	}

}
