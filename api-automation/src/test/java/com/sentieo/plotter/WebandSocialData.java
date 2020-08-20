package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
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
	String no_mapping = "";
	static List<String> tickers;

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun = true)
	public void setTickers() {
		CommonUtil common = new CommonUtil();
		tickers = common.getDay("finance" + File.separator + "MosaicDataSet.csv");
	}

	@Test(description = "Plotter Web and Social Data Series", groups = { "gtrends", "strong_ties" })
	public void googleTrends() throws CoreCommonException {
		try {
			Calendar calNewYork = Calendar.getInstance();
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
			if (dayofweek != 1 && dayofweek != 2 && dayofweek != 3 && dayofweek != 7) {
				String URI = APP_URL + GET_GTRENDS;
				for (int i = 0; i < tickers.size(); i++) {
					String cell = tickers.get(i).toLowerCase();
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
						int statuscode = apiResp.getStatusCode();
						verify.verifyStatusCode(statuscode, 200);
						if (statuscode == 200) {
							JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
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
							if (!date.contains(currentDate))
								verify.assertEqualsActualContainsExpected(date, currentDate,
										"gtrends not updated for " + cell);
						} else
							verify.assertTrue(false, "status code is : " + statuscode + " for " + cell);
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

	@Test(description = "Plotter Web and Social Data Series", groups = { "web", "strong_ties2" })
	public void websiteTraffic() throws CoreCommonException {
		try {
			String cell = "";
			String URI = APP_URL + ALEXA;
			HashMap<String, String> parameters = new HashMap<String, String>();
			for (int i = 0; i < tickers.size(); i++) {
				cell = tickers.get(i).toLowerCase();
				String isMapping = getMapping(cell);
				if (!isMapping.contains("true")) {
					parameters.put("url", "");
					parameters.put("ticker", cell);
					parameters.put("pagetype", "plotter");
					parameters.put("datatype", "page_views");
					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(URI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					int statuscode = apiResp.getStatusCode();
					verify.verifyStatusCode(statuscode, 200);
					if (statuscode == 200) {
						JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
						verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
								"Verify the API Response Status");
						String msg = respJson.getJSONObject("response").get("msg").toString().replaceAll("\\[", "")
								.replaceAll("\\]", "").replace("\"", " ");
						verify.assertEqualsActualContainsExpected(msg, actualMSG, "match response msg");
						verify.verifyResponseTime(resp, 5000);
						JSONObject getSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0);
						String yAxis = getSeries.getString("yaxis");
						verify.assertEqualsActualContainsExpected(yAxisActual, yAxis, "match series name");
						JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
								.getJSONArray("series");
						if(values.length()!=0 && values!=null)
						{
						JSONArray value = values.getJSONArray(values.length() - 1);
						double timestamp = value.getDouble(0);
						int digit = (int) (timestamp / 1000);
						CommonUtil util = new CommonUtil();
						String date = util.convertTimestampIntoDate(digit);
						String str = getDate(2);
						if (!date.contains(str))
							str = getDate(3);
						if (!date.contains(str))
							str = getDate(4);
						if (!date.contains(str))
							str = getDate(5);

						verify.assertEqualsActualContainsExpected(date, str,
								"verify website-traffic latest point for " + cell);
					} else
						verify.assertTrue(false, "status code is : " + statuscode + " for " + cell);
				
					}} else
					ExtentTestManager.getTest().log(LogStatus.INFO, cell + " not mapped in Mosaic");

			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	//@Test(description = "Plotter instagram", groups = { "insta","strong_ties" }, dataProvider = "instagram", dataProviderClass = DataProviderClass.class)
	public void instagramMention(String metric) throws CoreCommonException {
		String cell = "";
		try {
			String URI = APP_URL + FETCH_GRAPH_DATA;
			HashMap<String, String> parameters = new HashMap<String, String>();
			for (int i = 0; i < tickers.size(); i++) {
				cell = tickers.get(i).toLowerCase();
				JSONArray query = instagramMapping(cell);
				for (int j = 0; j < query.length(); j++) {
					String query_param = query.getString(j);
					parameters.put("singleapi", "false");
					parameters.put("ticker", cell);
					parameters.put("head_name", "Instagram Mentions");
					parameters.put("pagetype", "plotter");
					parameters.put("graphtype_original", "instagram");
					parameters.put("graphtype", "instagram");
					parameters.put("query", query_param);
					parameters.put("paramtype", metric);
					parameters.put("query_insta", query_param);
					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(URI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					int statuscode = apiResp.getStatusCode();
					verify.verifyStatusCode(statuscode, 200);
					if (statuscode == 200) {
						JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
						verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
								"Verify the API Response Status");
						JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
						if (series.length() != 1 && series != null) {
							JSONArray values = series.getJSONObject(series.length() - 1).getJSONArray("series");
							JSONArray value = values.getJSONArray(values.length() - 1);
							double timestamp = value.getDouble(0);
							int digit = (int) (timestamp / 1000);
							CommonUtil util = new CommonUtil();
							String date = util.convertTimestampIntoDate(digit);
							String str = getDate(2);
							if (!date.contains(str))
								str = getDate(3);
							if (!date.contains(str))
								str = getDate(6);

							if (!date.contains(str))
								str = getDate(7);

							if (!date.contains(str))
								str = getDate(8);

							verify.assertEqualsActualContainsExpected(date, str,
									"verify instagram latest point for query " + query_param + " and ticker is "
											+ cell);
						}
					}
				}
			}

			verify.verifyAll();
		} catch (

		Exception e) {
			System.out.println(e.toString() + " " + cell);
			// throw new CoreCommonException(e.getMessage());
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

	@Test(groups = { "gtrends-autocomplete",
			"test" }, description = "gtrends autocomplete check", dataProvider = "searchAutocomplete", dataProviderClass = DataProviderClass.class)
	public void gtrendsAutocomplete(String query) throws Exception {
		String URI = APP_URL + GTRENDSAUTOCOMPLETE;
		boolean status = true;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("word", query);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			JSONArray result = respJson.getJSONArray("result");
			if (result.length() == 0 || result == null)
				verify.assertTrue(false, "Autocomplete shows blank data");
			else {
				for (int i = 0; i < result.length(); i++) {
					JSONObject resultAutocomplete = result.getJSONObject(i);
					if (resultAutocomplete.length() == 0 || resultAutocomplete == null) {
						verify.assertTrue(false, "Autocomplete is blank : ");
						status = false;
					}
				}
				if (status)
					verify.assertTrue(status, "Verify Autocomplete: ");
			}

		} catch (Error e) {

		}
	}

	public String getDate(int days) {
		String str = "";
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		dateformat = new SimpleDateFormat("M/d/yy");
		calNewYork.setTimeZone(TimeZone.getTimeZone("Australia/Perth"));
		calNewYork.add(Calendar.DAY_OF_MONTH, -days);
		str = dateformat.format(calNewYork.getTime());
		return str;
	}

	public JSONArray instagramMapping(String ticker) throws CoreCommonException {
		String URI = APP_URL + INSTAGRAM_MAPPINGS;
		JSONArray result = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", ticker.toLowerCase());
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				result = respJson.getJSONObject("result").getJSONArray("query_list");
				return result;
			} else
				verify.assertTrue(false, "status code is : " + statusCode);
		} catch (Error e) {

		}
		return result;

	}
}
