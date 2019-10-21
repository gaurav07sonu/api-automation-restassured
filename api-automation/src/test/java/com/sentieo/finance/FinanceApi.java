package com.sentieo.finance;

import static com.sentieo.constants.Constants.*;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class FinanceApi extends APIDriver {

	APIAssertions verify = new APIAssertions();
	public static HashMap<String, String> fetchcompanysummarytable = new HashMap<String, String>();
	public static HashMap<String, String> fetchnewcompanyheaderdata = new HashMap<String, String>();
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

	@Test(groups = "sanity", description = "fetch current stock data")
	public void fetchCurrentStockData() throws Exception {
		for (String[] row : tickers) {
			for (String cell : row) {
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_CURRENT_STOCK_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("result").getString("ticker"), cell.toUpperCase(),
						"Verify that Requested ticker Visible in the API");
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
				// verify.jsonSchemaValidation(resp, "finance" + File.separator +
				// "fetchCurrentStockData.json");

			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch capital events")
	public void fetchCapitalEventsData() throws Exception {
		for (String[] row : tickers) {
			for (String cell : row) {
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				tickerData.put("sort_key", "announceddate");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_CAPITAL_EVENTS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				tickerData.put("termtype", "ticker");
				tickerData.put("source", "summary");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(GET_TRACKER_MAPPINGS, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				tickerData.put("ticker", cell);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(GET_COMPANY_RETURN, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
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
				tickerData.put("ticker", cell);
				tickerData.put("sort_key", "AnnouncedDate");
				tickerData.put("sort_state", "desc");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_CAPITAL_EVENTS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
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
				tickerData.put("ticker", cell);
				tickerData.put("sections", "research");
				tickerData.put("ticker", "aapl");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_COMPANY_DOCS, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
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
				tickerData.put("ticker", cell);
				tickerData.put("selection", "Revenue_corrScore");
				tickerData.put("termtype", "ticker");
				tickerData.put("first_call", "true");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(NEW_FETCH_MOSAIC_SUMMARY_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				tickerData.put("ticker", cell);
				tickerData.put("summary", "true");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_COMPANY_SUMARY_TABLE, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_ANALYST_RECOMMEND, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_trading_ratios")
	public void fetchtradingratios() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_TRADING_RATIO, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_COMPANY_STATUS, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
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
				tickerData.put("ticker", cell);
				tickerData.put("new_wl", "true");
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_NEW_COMPANY_HEADER_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
				tickerData.put("ticker", cell);
				tickerData.put("datatype", datatype);
				tickerData.put("periodtype", periodtype);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(EARNINGS_SURPRISES_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_past_intra")
	public void fetchpastintra() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				tickerData.put("ticker", cell);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_PAST_INTRADAY, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				try {
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					String systemDate = dateValidationForHistoricalChart("fetchpastintra");
					systemDate = systemDate.replaceAll("/", "");
					JSONArray values = respJson.getJSONObject("result").getJSONObject("past_intra")
							.getJSONArray(systemDate);
					if (values != null) {
						JSONArray value = values.getJSONArray(values.length() - 1);
						while (isMarketClosed()) {
							double timestamp = value.getDouble(0);
							int digit = (int) (timestamp / 1000);
							String date = convertTimestampIntoDate(digit);
							String systemDate1 = dateValidationForHistoricalChart("");
							verify.compareDates(date, systemDate1, "Verify the Current Date Point");
							break;
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

	@Test(groups = "sanity", description = "fetch_main_graph")
	public void fetchmaingraph() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				tickerData.put("ticker", cell);
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_MAIN_GRAPH, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
						String systemDate = dateValidationForHistoricalChart("fetch_main_graph");
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
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			for (String[] row : tickers) {
				for (String cell : row) {
					tickerData.put("ticker", cell);
					tickerData.put("graphtype", "TradingMultiples");
					tickerData.put("ratio", ratio);
					tickerData.put("sp_rel", "true");
					tickerData.put("ptype", "rolling");
					tickerData.put("shift", "backward");

					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
							while (isMarketClosed()) {
								double timestamp = value.getDouble(0);
								int digit = (int) (timestamp / 1000);
								String date = convertTimestampIntoDate(digit);
								String systemDate = getCurrentUSDate();
								verify.compareDates(date, systemDate, "Verify the Current Date Point");
								break;
							}
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

	@Test(groups = "sanity", description = "fetch_graph_data1", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchgraphdata1(String subtype) throws Exception {
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			for (String[] row : tickers) {
				for (String cell : row) {
					tickerData.put("ticker", cell);
					tickerData.put("graphtype", "yearlyEstimate");
					tickerData.put("subtype", subtype);
					tickerData.put("getstock", "true");
					tickerData.put("ptype", "q5");
					tickerData.put("next4", "true");
					RequestSpecification spec = queryParamsSpec(tickerData);
					Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					try {
						JSONArray verifyTickerName = respJson.getJSONObject("result").getJSONArray("series");
						String name = verifyTickerName.getJSONObject(verifyTickerName.length() - 1).getString("title");
						if (name.contains("NTM")) {
							JSONArray values = respJson.getJSONObject("result").getJSONArray("series")
									.getJSONObject(verifyTickerName.length() - 1).getJSONArray("series");
							JSONArray value = values.getJSONArray(values.length() - 1);
							while (isMarketClosed()) {
								double timestamp = value.getDouble(0);
								int digit = (int) (timestamp / 1000);
								String date = convertTimestampIntoDate(digit);
								String systemDate = getCurrentUSDate();
								verify.compareDates(date, systemDate, "Verify the Current Date Point");
								break;
							}
						}

					} catch (JSONException je) {
						verify.verificationFailures.add(je);
						ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
					} catch (AssertionError ae) {
						verify.verificationFailures.add(ae);
						ExtentTestManager.getTest().log(LogStatus.FAIL, ae.getMessage());
					}
				}
			}
			verify.verifyAll();
		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"Skip test because of  Current Date Point is not available on  : " + Calendar.DAY_OF_WEEK + "day");
		}
	}

	@Test(groups = "sanity", description = "fetch_graph_data2", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchgraphdata2(String subtype) throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				tickerData.put("ticker", cell);
				tickerData.put("graphtype", "financialData");
				tickerData.put("subtype", subtype);
				tickerData.put("ttmind", "true");
				tickerData.put("getestimates", "true");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_GRAPH_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
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
				tickerData.put("ticker", cell);
				tickerData.put("type", subtype);
				tickerData.put("report_currency", currency);
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_VALUE_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		}
		verify.verifyAll();
	}

	@Test(groups = "sanity", description = "fetch_institutional_holdings_data3")
	public void fetchinstitutionalholdingsdata() throws Exception {
		HashMap<String, String> tickerData = new HashMap<String, String>();
		for (String[] row : tickers) {
			for (String cell : row) {
				tickerData.put("ticker", cell);
				tickerData.put("period", "2018-12-31");
				RequestSpecification spec = queryParamsSpec(tickerData);
				Response resp = RestOperationUtils.get(FETCH_HOLDINGS_DATA, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
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

	public String dateValidationForHistoricalChart(String testName) {

		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		if (testName.contains("fetchpastintra")) {
			dateformat = new SimpleDateFormat("yyy/MM/dd");
		} else {
			dateformat = new SimpleDateFormat("M/dd/yy");
		}
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 2 && (dayofweek != 1 || dayofweek != 7)) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -3);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else {
			calNewYork.add(Calendar.DAY_OF_MONTH, -1);
			String str = dateformat.format(calNewYork.getTime());
			return str;
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
