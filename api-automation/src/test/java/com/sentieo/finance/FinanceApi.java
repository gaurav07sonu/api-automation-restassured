package com.sentieo.finance;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EARNINGS_SURPRISES_DATA;
import static com.sentieo.constants.Constants.FETCH_ANALYST_RECOMMEND;
import static com.sentieo.constants.Constants.FETCH_CAPITAL_EVENTS;
import static com.sentieo.constants.Constants.FETCH_COMPANY_DOCS;
import static com.sentieo.constants.Constants.FETCH_COMPANY_LIST;
import static com.sentieo.constants.Constants.FETCH_COMPANY_STATUS;
import static com.sentieo.constants.Constants.FETCH_COMPANY_SUMARY_TABLE;
import static com.sentieo.constants.Constants.FETCH_CURRENT_STOCK_DATA;
import static com.sentieo.constants.Constants.FETCH_DOCS;
import static com.sentieo.constants.Constants.FETCH_EARNING_HEADER_DATA;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.FETCH_HOLDINGS_DATA;
import static com.sentieo.constants.Constants.FETCH_INTRA_HEADER_DATA;
import static com.sentieo.constants.Constants.FETCH_MAIN_GRAPH;
import static com.sentieo.constants.Constants.FETCH_NEW_COMPANY_HEADER_DATA;
import static com.sentieo.constants.Constants.FETCH_NEW_MODEL_DATA;
import static com.sentieo.constants.Constants.FETCH_PAST_INTRADAY;
import static com.sentieo.constants.Constants.FETCH_STOCK_META;
import static com.sentieo.constants.Constants.FETCH_TRADING_RATIO;
import static com.sentieo.constants.Constants.FETCH_USER_TICKER_VALUES;
import static com.sentieo.constants.Constants.FETCH_VALUE_DATA;
import static com.sentieo.constants.Constants.FETCH_YEARLY_DATA;
import static com.sentieo.constants.Constants.GET_COMPANY_AUTOCOMPLETE;
import static com.sentieo.constants.Constants.GET_COMPANY_RETURN;
import static com.sentieo.constants.Constants.GET_TRACKER_MAPPINGS;
import static com.sentieo.constants.Constants.MOBILE_STOCK_DATA;
import static com.sentieo.constants.Constants.NEW_FETCH_MOSAIC_SUMMARY_DATA;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class FinanceApi extends APIDriver {

	String watchName = "";
	Object ticker = "";
	String loc2 = "";

	CommonUtil util = new CommonUtil();
	APIAssertions verify = new APIAssertions();
	public static HashMap<String, String> fetchcompanysummarytable = new HashMap<String, String>();
	public static HashMap<String, String> fetchnewcompanyheaderdata = new HashMap<String, String>();
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "loc" })
	public void getLoc(@Optional("loc") String loc) {
		loc2 = loc;
	}

	@Test(groups = { "sanity", "mobile" }, description = "fetch current stock data")
	public void fetchCurrentStockData() throws Exception {
		try {
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					Response resp;
					HashMap<String, String> tickerData = new HashMap<String, String>();
					tickerData.put("ticker", cell);
					RequestSpecification spec = formParamsSpec(tickerData);
					if (loc2.equals("ios")) {
						tickerData.put("loc", "ios");
						resp = RestOperationUtils.post(MOBILE_STOCK_DATA, null, spec, tickerData);
					} else
						resp = RestOperationUtils.post(FETCH_CURRENT_STOCK_DATA, null, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 5000);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					if (loc2.equals("ios")) {
						JSONObject result = respJson.getJSONObject("result");
						JSONObject intraday = result.getJSONObject("intraday");
						verify.verifyEquals(intraday.getString("ticker"), cell.toUpperCase(),
								"Verify that Requested ticker Visible in the API");
						HashMap<String, String> keyMap = new HashMap<String, String>();
						keyMap.put("currency", "java.lang.String");
						keyMap.put("found", "java.lang.Boolean");
						keyMap.put("stock-flag", "java.lang.Boolean");
						keyMap.put("m_open", "java.lang.String");
						keyMap.put("duration", "java.lang.Double");
						keyMap.put("current_price", "java.lang.Double");
						keyMap.put("perc_change", "java.lang.String");
						keyMap.put("change", "java.lang.String");
						HashMap<String, String> keyMap2 = new HashMap<String, String>();
						keyMap2.put("summary", "org.json.JSONObject");
						keyMap2.put("powergraph", "org.json.JSONArray");
						keyMap2.put("trading_ratios", "org.json.JSONObject");
						keyMap2.put("intraday", "org.json.JSONObject");
						for (Map.Entry<String, String> set : keyMap.entrySet()) {
							util.verifykeyAvailable(intraday, set.getKey(), set.getValue());
						}
						for (Map.Entry<String, String> set : keyMap2.entrySet()) {
							util.verifykeyAvailable(result, set.getKey(), set.getValue());
						}
					} else {
						verify.verifyEquals(respJson.getJSONObject("result").getString("ticker"), cell.toUpperCase(),
								"Verify that Requested ticker Visible in the API");
						verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
								"Verify the API Message");
						//verify.jsonSchemaValidation(resp, "finance" + File.separator + "fetchCurrentStockData.json");
					}
				}
			}
		} catch (Exception je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch capital events")
	public void fetchCapitalEventsData() throws Exception {
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				tickerData.put("sort_key", "announceddate");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_CAPITAL_EVENTS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				// verify.jsonSchemaValidation(resp, "finance" + File.separator +
				// "fetchCapitalEventsData.json");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "get tracker mappings")
	public void getTrackerMappings() throws Exception {
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				tickerData.put("termtype", "ticker");
				tickerData.put("source", "summary");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(GET_TRACKER_MAPPINGS, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "get company return")
	public void getCompanyReturn() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(GET_COMPANY_RETURN, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch dividend events")
	public void fetchDividendEvents() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("sort_key", "AnnouncedDate");
				tickerData.put("sort_state", "desc");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_CAPITAL_EVENTS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_company_docs")
	public void fetchCompanyDocs() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("sections", "research");
				tickerData.put("ticker", "aapl");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_COMPANY_DOCS, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "new_fetch_mosaic_summary_data")
	public void newFetchMosaicSummaryData() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("selection", "Revenue_corrScore");
				tickerData.put("termtype", "ticker");
				tickerData.put("first_call", "true");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(NEW_FETCH_MOSAIC_SUMMARY_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_company_summary_table")
	public void fetchcompanysummarytable() throws Exception {
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String year = String.valueOf(calNewYork.get(Calendar.YEAR));
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("summary", "true");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_COMPANY_SUMARY_TABLE, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				try {
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray value = respJson.getJSONObject("result").getJSONArray("orderlist");
					for (int i = 0; i < value.length(); i++) {
						String aa = value.getString(i);
						JSONObject value1 = respJson.getJSONObject("result").getJSONObject("data");
						Iterator<String> KeyItr = value1.keys();
						while (KeyItr.hasNext()) {
							String key = KeyItr.next();
							if (aa.equalsIgnoreCase(key)) {
								JSONObject abcd = (JSONObject) value1.get(key);
								Iterator<String> KeyItre = abcd.keys();
								while (KeyItre.hasNext()) {
									String name = KeyItre.next();
									if (name.equalsIgnoreCase(year)) {
										String getValue = String.valueOf(abcd.get(name));
										fetchcompanysummarytable.put(key, getValue);
									}
								}
							}
						}
					}

				} catch (JSONException je) {
					verify.verificationFailures.add(je);
					ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_analyst_recommend")
	public void fetchanalystrecommend() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_ANALYST_RECOMMEND, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_yearly_data", dataProvider = "fetch_yearly_data1", dataProviderClass = DataProviderClass.class)
	public void fetchyearlydata(String ticker, String model, String historical_periods, String forecast_periods,
			String report_currency) throws Exception {
		String url = USER_APP_URL;
		if (url.equals("user-app2.sentieo.com") && (!url.contains("user-dev.sentieo.com"))) {
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					HashMap<String, String> tickerData = new HashMap<String, String>();
					tickerData.put("ticker", cell);
					tickerData.put("model", model);
					tickerData.put("historical_periods", historical_periods);
					tickerData.put("forecast_periods", forecast_periods);
					tickerData.put("report_currency", report_currency);
					RequestSpecification spec = formParamsSpec(tickerData);
					Response resp = RestOperationUtils.post(FETCH_YEARLY_DATA, null, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 5000);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(respJson.getJSONObject("response").getString("msg"), "success",
							"Verify the API Message");

				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_trading_ratios")
	public void fetchtradingratios() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_TRADING_RATIO, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_company_status")
	public void fetchcompanystatus() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_COMPANY_STATUS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();

	}

	@Test(groups = "sanity", description = "fetch_new_company_header_data")
	public void fetchnewcompanyheaderdata() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("new_wl", "true");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_NEW_COMPANY_HEADER_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				try {
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray value = respJson.getJSONObject("result").getJSONArray("orderlist");
					for (int i = 0; i < value.length(); i++) {
						String aa = value.getString(i);
						JSONObject value1 = respJson.getJSONObject("result").getJSONObject("fin_summary");
						Iterator<String> KeyItr = value1.keys();
						while (KeyItr.hasNext()) {
							String key = KeyItr.next();
							if (aa.equalsIgnoreCase(key)) {
								String getValue = String.valueOf(value1.get(key));
								fetchnewcompanyheaderdata.put(key, getValue);
							}
						}
					}
					verifyValuesInBothViews(fetchcompanysummarytable, fetchnewcompanyheaderdata);
				} catch (JSONException je) {
					verify.verificationFailures.add(je);
					ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "earnings_surprises_data", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void earningssurprisesdata(String datatype, String periodtype) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("datatype", datatype);
				tickerData.put("periodtype", periodtype);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(EARNINGS_SURPRISES_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = { "fetch", "mobile" }, description = "fetch_past_intra")
	public void fetchpastintra() throws Exception {
		try {
			String systemDate = "";
			HashMap<String, String> tickerData = new HashMap<String, String>();
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					tickerData.put("ticker", cell);
					Response resp;
					if (loc2.equals("ios")) {
						tickerData.put("loc", "ios");
						RequestSpecification spec = formParamsSpec(tickerData);
						resp = RestOperationUtils.post(FETCH_PAST_INTRADAY, null, spec, tickerData);
					} else {
						RequestSpecification spec = queryParamsSpec(tickerData);
						resp = RestOperationUtils.get(FETCH_PAST_INTRADAY, spec, tickerData);
					}
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 5000);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					systemDate = dateValidationForHistoricalChart("fetchpastintra", cell);
					systemDate = systemDate.replaceAll("/", "");
					JSONObject pastIntraObject = respJson.getJSONObject("result").getJSONObject("past_intra");
					if (pastIntraObject.has(systemDate)) {
						JSONArray values = pastIntraObject.getJSONArray(systemDate);
						if (values != null) {
							JSONArray value = values.getJSONArray(values.length() - 1);
							while (isMarketClosed()) {
								double timestamp = value.getDouble(0);
								int digit = (int) (timestamp / 1000);
								String date = convertTimestampIntoDate(digit);
								String systemDate1 = dateValidationForHistoricalChart("", cell);
								verify.compareDates(date, systemDate1, "Verify the Current Date Point");
								break;
							}
						}
					}
					if (loc2.equals("ios")) {
						util.verifykeyAvailable(respJson.getJSONObject("result"), "past_intra", "org.json.JSONObject");
					}
				}
			}

		} catch (JSONException je) {
			verify.assertTrue(false, je.toString());
		} finally {
			verify.verifyAll();

		}
	}

	@Test(groups = "fetch2", description = "fetch_main_graph")
	public void fetchmaingraph() throws Exception {
		CommonUtil obj = new CommonUtil();
		String systemDate = "";
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_MAIN_GRAPH, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				try {
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray values = respJson.getJSONObject("result").getJSONArray("stockdata");
					JSONArray value = values.getJSONArray(values.length() - 1);
					while (isMarketClosed()) {
						double timestamp = value.getDouble(0);
						int digit = (int) (timestamp / 1000);
						String date = convertTimestampIntoDate(digit);
						systemDate = obj.getDate(-1);
						verify.compareDates(date, systemDate, "Verify the Current Date Point");
						break;
					}
				} catch (JSONException je) {
					verify.verificationFailures.add(je);
					ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_graph_data", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchgraphdata(String ratio) throws Exception {
		CommonUtil obj = new CommonUtil();
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					tickerData.put("ticker", cell);
					tickerData.put("graphtype", "TradingMultiples");
					tickerData.put("ratio", ratio);
					tickerData.put("sp_rel", "true");
					tickerData.put("ptype", "rolling");
					tickerData.put("shift", "backward");

					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);

					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					try {
						String verifyTickerName = respJson.getJSONObject("result").getJSONArray("series")
								.getJSONObject(0).getString("title");
						JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
								.getJSONArray("series");
						JSONArray value = values.getJSONArray(values.length() - 1);
						if (verifyTickerName.contains(cell.toUpperCase())) {
//							while (isMarketClosed()) {
								double timestamp = value.getDouble(0);
								int digit = (int) (timestamp / 1000);
								String date = convertTimestampIntoDate(digit);
								String systemDate = obj.getDate(-1);
								verify.compareDates(date, systemDate, "Verify the Current Date Point");
								break;
							//}
						}
					} catch (JSONException je) {
						verify.verificationFailures.add(je);
						ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
					}
				}
			}
			verify.verifyAll();
		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"Skip test because of  Current Date Point is not available on  : " + Calendar.DAY_OF_WEEK + "day");
		}

	}

//	@Test(groups = "sanity", description = "fetch_graph_data1", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
//	public void fetchgraphdata1(String subtype) throws Exception {
//		Calendar calNewYork = Calendar.getInstance();
//		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
//		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
//		if (dayofweek != 1 && dayofweek != 7) {
//			HashMap<String, String> tickerData = new HashMap<String, String>();
//			for (String[] row : tickers) {
//				for (String cell : row) {
//					tickerData.put("ticker", cell);
//					tickerData.put("graphtype", "yearlyEstimate");
//					tickerData.put("subtype", subtype);
//					tickerData.put("getstock", "true");
//					tickerData.put("ptype", "q5");
//					tickerData.put("next4", "true");
//					RequestSpecification spec = queryParamsSpec(tickerData);
//					Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
//					APIResponse apiResp = new APIResponse(resp);
//					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
//					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
//					verify.verifyResponseTime(resp, 5000);
//					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
//							"Verify the API Response Status");
//					try {
//						JSONArray verifyTickerName = respJson.getJSONObject("result").getJSONArray("series");
//						String name = verifyTickerName.getJSONObject(verifyTickerName.length() - 1).getString("title");
//						if (name.contains("NTM")) {
//							JSONArray values = respJson.getJSONObject("result").getJSONArray("series")
//									.getJSONObject(verifyTickerName.length() - 1).getJSONArray("series");
//							JSONArray value = values.getJSONArray(values.length() - 1);
//							while (isMarketClosed()) {
//								double timestamp = value.getDouble(0);
//								int digit = (int) (timestamp / 1000);
//								String date = convertTimestampIntoDate(digit);
//								String systemDate = getCurrentUSDate();
//								verify.compareDates(date, systemDate, "Verify the Current Date Point");
//								break;
//							}
//						}
//
//					} catch (JSONException je) {
//						verify.verificationFailures.add(je);
//						ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
//					} catch (AssertionError ae) {
//						verify.verificationFailures.add(ae);
//						ExtentTestManager.getTest().log(LogStatus.FAIL, ae.getMessage());
//					}
//				}
//			}
//			verify.verifyAll();
//		} else {
//			ExtentTestManager.getTest().log(LogStatus.INFO,
//					"Skip test because of  Current Date Point is not available on  : " + Calendar.DAY_OF_WEEK + "day");
//		}
//	}

	@Test(groups = "sanity", description = "fetch_graph_data2", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchgraphdata2(String subtype) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("graphtype", "financialData");
				tickerData.put("subtype", subtype);
				tickerData.put("ttmind", "true");
				tickerData.put("getestimates", "true");

				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_value_table", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchvaluetable(String subtype, String currency) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("type", subtype);
				tickerData.put("report_currency", currency);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_VALUE_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "new_model_data", description = "FETCH_NEW_MODEL_DATA")
	public void fetchNewModelData() throws Exception {
		String url = APP_URL + FETCH_NEW_MODEL_DATA;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("model_source", "vpt");
				tickerData.put("ticker", cell);
				tickerData.put("ptype", "fq");
				tickerData.put("report_currency", "usd");
				tickerData.put("units", "M");
				try {
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(url, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONObject result = respJson.getJSONObject("result");
					if (result.length() == 0 || result == null)
						verify.assertTrue(false, "API shows blank data");
					else
						verify.assertTrue(true, "Verify new model data");
				} catch (Exception e) {
					verify.assertTrue(false, e.toString() + " for ticker " + cell);
				}
			}
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_institutional_holdings_data3")
	public void fetchinstitutionalholdingsdata() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("period", "2018-12-31");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_HOLDINGS_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = {"sanity2","mobile"}, description = "fetch_institutional_holdings_data3")
	public void fetchTickerValues() throws Exception {
		String URI = APP_URL + FETCH_USER_TICKER_VALUES;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("seriesTicker", cell);
				try {
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(URI, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					String seriesTicker = respJson.getJSONObject("result").get("seriesTicker").toString();
					verify.assertEqualsActualContainsExpected(cell.toLowerCase(), seriesTicker, "verify series ticker");
					JSONArray intradayData = respJson.getJSONObject("result").getJSONArray("intradayData");
					if (intradayData.length() == 0 || intradayData == null)
						verify.assertTrue(false, "Intraday data is blank : ");
					else
						verify.assertTrue(true, "Verify Intraday data : ");
					if (!cell.contains(":")) {
						JSONArray rel_sp = respJson.getJSONObject("result").getJSONArray("rel_sp");
						if (rel_sp.length() == 0 || rel_sp == null)
							verify.assertTrue(false, "rel_sp data is blank : ");
						else
							verify.assertTrue(true, "Verify rel_sp data : ");
					}
				} catch (Exception e) {
					verify.verificationFailures.add(e);
					ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity22", description = "fetch_institutional_holdings_data3")
	public void fetchEarningHeaderData() throws Exception {
		String URI = APP_URL + FETCH_EARNING_HEADER_DATA;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("ticker", cell);
				tickerData.put("summary", "true");
				try {
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(URI, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray earning_date = respJson.getJSONObject("result").getJSONArray("earning_date");
					if (earning_date.length() == 0 || earning_date == null)
						verify.assertTrue(false, "earning_date data is blank : ");
					else
						verify.assertTrue(true, "Verify earning_date data : ");
				} catch (Exception e) {
					verify.verificationFailures.add(e);
					ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "docs", description = "fetch_institutional_holdings_data3")
	public void fetchDocs() throws Exception {
		String URI = APP_URL + FETCH_DOCS;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("tickers", cell);
				tickerData.put("doc_type", "all_docs");
				tickerData.put("size_docs", "6");
				tickerData.put("size_tweets", "15");
				tickerData.put("split", "true");
				try {
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(URI, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray result = respJson.getJSONObject("result").getJSONObject("all_docs")
							.getJSONArray("result");
					if (result.length() == 0 || result == null)
						verify.assertTrue(false, "docs data is blank for : " + cell);
					else
						verify.assertTrue(true, "Verify all docs data for : " + cell);

				} catch (Exception e) {
					verify.verificationFailures.add(e);
					ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "autocomplete", description = "fetch_institutional_holdings_data3")
	public void fetchCompanyAutocomplete() throws Exception {
		String URI = APP_URL + GET_COMPANY_AUTOCOMPLETE;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				tickerData.put("tickers", cell);
				tickerData.put("doc_type", "all_docs");
				tickerData.put("size_docs", "6");
				tickerData.put("size_tweets", "15");
				tickerData.put("split", "true");
				try {
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(URI, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray result = respJson.getJSONArray("result");
					if (result.length() == 0 || result == null)
						verify.assertTrue(false, "Autocomplete is blank for ticker : " + cell);
					else
						verify.assertTrue(true, "verify Autocomplete data for ticker: " + cell);
				} catch (Exception e) {
					verify.verificationFailures.add(e);
					ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
				}
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "company", description = "fetch_institutional_holdings_data3")
	public void fetchCompanyList() throws Exception {
		String URI = APP_URL + FETCH_COMPANY_LIST;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		try {
			RequestSpecification spec = queryParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONArray result = respJson.getJSONArray("result");
			if (result.length() == 0 || result == null)
				verify.assertTrue(false, "Result shows blank data : ");
			else
				verify.assertTrue(true, "verify company list result : ");
		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}

		verify.verifyAll();
	}

	@Test(groups = "company3", description = "fetch_institutional_holdings_data3")
	public void fetchStockMeta() throws Exception {
		String URI = APP_URL + FETCH_STOCK_META;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		try {
			tickerData.put("ticker_groups",
					"[{\"priceMonitor_watchlistGroup_followed_tickers\":[\"med\",\"ma\",\"el\",\"dri\",\"fb\",\"qcom\",\"lb\",\"asna\"]}]");
			tickerData.put("source_fields",
					"[\"current_price\",\"day_change\",\"ytd_change\",\"week_change\",\"month_change\",\"trade_time\"]");
			RequestSpecification spec = queryParamsSpec(tickerData);
			Response resp = RestOperationUtils.get(URI, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result").getJSONObject("stock_meta")
					.getJSONObject("priceMonitor_watchlistGroup_followed_tickers");
			if (result.length() == 0 || result == null)
				verify.assertTrue(false, "Result shows blank data : ");
			else
				verify.assertTrue(true, "verify stock_meta : ");

		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}

		verify.verifyAll();
	}

	@Test(groups = "fetchintra", description = "fetch_institutional_holdings_data3")
	public void fetchIntraHeader() throws Exception {
		String URI = APP_URL + FETCH_INTRA_HEADER_DATA;
		boolean intradayFlag = true;
		boolean rel_spFlag = true;

		HashMap<String, String> tickerData = new HashMap<String, String>();
		try {
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					tickerData.put("tickers", cell);
					tickerData.put("source", "summary");
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(URI, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					JSONArray rel_sp = respJson.getJSONObject("result").getJSONArray("rel_sp");
					if (!cell.contains(":")) {
						if (rel_sp.length() == 0 || rel_sp == null) {
							verify.assertTrue(false, "rel_sp shows blank data for: " + cell);
							rel_spFlag = false;
						}
					}
					JSONArray intraday = respJson.getJSONObject("result").getJSONArray("intraday");
					if (intraday.length() == 0 || intraday == null) {
						verify.assertTrue(false, "intraday shows blank data for : " + cell);
						intradayFlag = false;
					}
				}
			}
			if (intradayFlag)
				verify.assertTrue(intradayFlag, "verify intraday data is not blank : ");

			if (rel_spFlag)
				verify.assertTrue(intradayFlag, "verify rel_sp data is not blank : ");
		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
		verify.verifyAll();

	}

	public String convertTimestampIntoDate(int time) {

		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
				.withZone(ZoneId.systemDefault());
		long epoch = time;
		Instant instant = Instant.ofEpochSecond(epoch);
		String output = formatter.format(instant);
		return output;
	}

	public String getCurrentUSDate() {

		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat = new SimpleDateFormat("M/dd/yy");
		dateformat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String str = dateformat.format(calNewYork.getTime());
		return str;
	}

	public boolean isMarketClosed() {

		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		// Dayofweek check 1=sunday or 7 = saturday
		if (dayofweek <= 1 || dayofweek >= 7) {
			String day = null;
			switch (dayofweek) {
			case 1:
				day = "Sunday";
				break;
			case 2:
				day = "Saturday";
				break;
			default:
				day = "Invalid Day: " + day;
			}
			return true;
		}
		int hourofday = calNewYork.get(Calendar.HOUR_OF_DAY);
		// Market Opens from 9AM to 4PM
		if (hourofday > 15 || hourofday < 9) {
			return true;
		}
		return false;
	}

	public String dateValidationForHistoricalChart(String testName, String ticker) {
		String str = "";
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		if (testName.contains("fetchpastintra")) {
			dateformat = new SimpleDateFormat("yyy/MM/dd");
		} else {
			dateformat = new SimpleDateFormat("M/dd/yy");
		}

		if (testName.contains("fetchpastintra")) {
			if (ticker.contains(":au") || ticker.contains(":jp") || ticker.contains(":na") || ticker.contains(":gr")) {
				calNewYork.setTimeZone(TimeZone.getTimeZone("Australia/Perth"));
				int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
				if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
					calNewYork.add(Calendar.DAY_OF_MONTH, 0);
					str = dateformat.format(calNewYork.getTime());
					return str;
				} else {
					calNewYork.add(Calendar.DAY_OF_MONTH, 0);
					str = dateformat.format(calNewYork.getTime());
					return str;
				}
			}
		}
		if (testName.isEmpty()) {
			if (ticker.contains(":au") || ticker.contains(":jp") || ticker.contains(":na") || ticker.contains(":gr")) {
				calNewYork.setTimeZone(TimeZone.getTimeZone("Australia/Perth"));
				int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
				if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
					calNewYork.add(Calendar.DAY_OF_MONTH, 0);
					str = dateformat.format(calNewYork.getTime());
					return str;
				} else {
					calNewYork.add(Calendar.DAY_OF_MONTH, 0);
					str = dateformat.format(calNewYork.getTime());
					return str;
				}
			}
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
				calNewYork.add(Calendar.DAY_OF_MONTH, -3);
				str = dateformat.format(calNewYork.getTime());
				return str;
			} else {
				calNewYork.add(Calendar.DAY_OF_MONTH, -1);
				str = dateformat.format(calNewYork.getTime());
				return str;
			}
		} else if (ticker.contains(":au") || ticker.contains(":jp")) {
			calNewYork.setTimeZone(TimeZone.getTimeZone("Australia/Perth"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
				calNewYork.add(Calendar.DAY_OF_MONTH, 0);
				str = dateformat.format(calNewYork.getTime());
				return str;
			} else {
				calNewYork.add(Calendar.DAY_OF_MONTH, 0);
				str = dateformat.format(calNewYork.getTime());
				return str;
			}
		} else {
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
				calNewYork.add(Calendar.DAY_OF_MONTH, -3);
				str = dateformat.format(calNewYork.getTime());
				return str;
			} else {
				calNewYork.add(Calendar.DAY_OF_MONTH, -1);
				str = dateformat.format(calNewYork.getTime());
				return str;
			}
		}
	}

	public void verifyValuesInBothViews(Map<String, String> first, Map<String, String> second) {
		for (String key : first.keySet()) {
			if (second.containsKey(key)) {
				String getValue = String.valueOf(first.get(key));
				double result = Double.parseDouble(getValue);
				int a = (int) Math.round(result);
				String getValue1 = String.valueOf(second.get(key));
				double result1 = Double.parseDouble(getValue1);
				int b = (int) Math.round(result1);
				if (a != b) {
					verify.assertNotEquals(a, b, "Values not matched in EDT header vs Valution Model");
				}
			}
		}
	}

}
