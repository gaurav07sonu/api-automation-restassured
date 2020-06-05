package com.sentieo.plotter;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CSVReaderUtil;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class RevenueDataPoint extends APIDriver {
	String[][] tickers;

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun = true)
	public void setTickers() {
		tickers = CSVReaderUtil.readAllDataAtOnce("finance" + File.separator + "revenue_ticker_list.csv");
	}

	@Test(description = "verify data point", dataProvider = "revenue", dataProviderClass = DataProviderClass.class)
	public void checkReportedQuarter(String periodType) throws CoreCommonException {
		try {
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					CommonUtil util = new CommonUtil();
					int year = Calendar.getInstance().get(Calendar.YEAR) % 100;
					String currentDate = String.valueOf(year);
					HashMap<String, String> parameters = new HashMap<String, String>();
					String fetchGraphURI = APP_URL + FETCH_GRAPH_DATA;
					parameters.put("pagetype", "plotter");
					parameters.put("graphtype", "financialData");
					parameters.put("subtype", "sales");
					parameters.put("periodtype", periodType);
					parameters.put("datasource", "rf");
					parameters.put("stack", "0");
					parameters.put("dma", "0");
					parameters.put("median", "0");
					parameters.put("yoy_rt", "0");
					parameters.put("qoq_rt", "0");
					parameters.put("dod_rt", "0");
					parameters.put("wow_rt", "0");
					parameters.put("mom_rt", "0");
					parameters.put("outliers", "0");
					parameters.put("day_dma", "0");
					parameters.put("getestimates", "false");
					parameters.put("yUnit", "millions");
					parameters.put("ticker", cell.toLowerCase().trim());
					parameters.put("freq_set1", "");
					parameters.put("freq_type1", "mean");

					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(fetchGraphURI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray series = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					if (series.length() != 0) {
						for (int i = 0; i < series.length(); i++) {
							String reportedQuarter = series.getJSONObject(i).get("x").toString();
							double timestamp = Double.valueOf(reportedQuarter);
							int digit = (int) (timestamp / 1000);
							String date = util.convertTimestampIntoDate(digit);
							if (date.contains(currentDate)) {
								if (i != series.length() - 1) {
									String nextReportedQuarter = series.getJSONObject(i + 1).get("x").toString();
									timestamp = Double.valueOf(nextReportedQuarter);
									digit = (int) (timestamp / 1000);
									String nextReportedDate = util.convertTimestampIntoDate(digit);
									long time1 = Long.parseLong(reportedQuarter);
									long time2 = Long.parseLong(nextReportedQuarter);
									long diff = util.daysDifferenceBetweenTwoTimestamps(time1, time2);
									int daysDiff = (int) diff;
									if (periodType.equalsIgnoreCase("Quarterly")) {
										if (daysDiff <= 50)
											verify.assertTrue(false,
													"verify reported quarter :" + "first reported quarter is " + date
															+ " second reported quarter is " + nextReportedDate);
									} else {
										if (daysDiff <= 100)
											verify.assertTrue(false,
													"verify reported quarter :" + "first reported quarter is " + date
															+ " second reported quarter is " + nextReportedDate);
									}
								}
							}

						}
					} else
						verify.assertTrue(false, "series data is blank for " + cell.toLowerCase().trim());
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

}
