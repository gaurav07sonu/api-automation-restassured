package com.sentieo.docsearchpublicapis;

import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
import com.sentieo.utils.CoreCommonException;
import com.sentieo.utils.JSONUtils;

public class docsearchpublicapis extends APIDriver {

	static APIAssertions verify = null;
	static JSONUtils jsonUtils = null;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
	}

	@Test(description = "Verifying doc types, sub-types, count and Ticker (excluding notes)", dataProvider = "test_doctype_ticker_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_doctype_ticker_publicapi(String doc_type, String docsub_type, String tickers) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			List<String> docSubTypeList = new ArrayList<String>();
			docSubTypeList.add(docsub_type);

			if (docsub_type == "10-k") { // accepting 10-q in list of doc sub-type if doc sub-type is 10-k//
				docSubTypeList.add("10-q");
			}
			;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", doc_type);
			docTypeParams.put("subtypes", docSubTypeList);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", tickers);
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray ticker = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						if (!ticker.toString().contains(tickers.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type1 = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type1.toString().contains(doc_type.toLowerCase()));
						if (!doc_type1.toString().contains(doc_type.toLowerCase()))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying RR doc types, with rr_ctbids, rr_style and rr_reasons", dataProvider = "test_doctype_rr_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_doctype_rr_publicapi(String ctbids, String reasons, String styles) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			List<String> ctbidsList = new ArrayList<String>();
			ctbidsList.add(ctbids);
			List<String> reasonsList = new ArrayList<String>();
			reasonsList.add(reasons);
			List<String> stylesList = new ArrayList<String>();
			stylesList.add(styles);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "rr");
			docTypeParams.put("ctbids", ctbidsList);
			docTypeParams.put("reasons", reasonsList);
			docTypeParams.put("styles", stylesList);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", "");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type1 = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type1.toString().contains("rr"));
						if (!doc_type1.toString().contains("rr"))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

			boolean ctbidsCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String rr_ctbids = documentResults_size.getJSONObject(i).getString("rr_ctbids");
						System.out.println(rr_ctbids.toString().contains(ctbids.toLowerCase()));
						if (!rr_ctbids.toString().contains(ctbids.toLowerCase()))
							ctbidsCheck = false;
					}

					verify.assertTrue(ctbidsCheck, "verifying ctbids visibility in doc ");
				}
			}

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying Note docs with doc type ,owner and categories", dataProvider = "test_doctype_notes_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_doctype_notes_publicapi(String subtypes, String authors) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			List<String> subtypeList = new ArrayList<String>();
			subtypeList.add(subtypes);
			if (subtypes == "attachment") { // accepting type notes in list of doc sub-type if doc sub-type is
											// attachment//
				subtypeList.add("in:note");
			}
			;

			List<String> categoriesList = new ArrayList<String>();
			categoriesList.add("General");

			List<String> authorsList = new ArrayList<String>();
			authorsList.add(authors);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("subtypes", subtypeList);
			docTypeParams.put("authors", authorsList);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "700");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", "");
			formParams.put("doc_type", docTypeList);
			formParams.put("query", "note");

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type1 = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type1.toString().contains("note"));
						if (!doc_type1.toString().contains("note"))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying invalid doc types, sub-types and Ticker (excluding notes)", dataProvider = "invalid_test_doctype_ticker_publicapi", dataProviderClass = DataProviderClass.class)
	public void invalid_test_doctype_ticker_publicapi(String doc_type, String docsub_type, String tickers)
			throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			List<String> docSubTypeList = new ArrayList<String>();
			docSubTypeList.add(docsub_type);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", doc_type);
			docTypeParams.put("subtypes", docSubTypeList);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", tickers);
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results == 0, "Verify the search result count is 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying different types of Queries (excluding notes)", dataProvider = "test_query_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_query_publicapi(String doc_type, String query, String tickers) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", doc_type);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("query", query);
			formParams.put("tickers", tickers);
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is greater than 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying invalid Queries (excluding notes)")
	public void invalid_test_query_publicapi() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "nw");

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("query", "abcd1111111112222334455667788");
			formParams.put("tickers", "");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results == 0, "Verify the search result count is 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying date from param", dataProvider = "test_datefrom_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_datefrom_publicapi(String doc_type, String date_range_from) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", doc_type);

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("date_range_from", date_range_from);
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is greater than 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying invalid date from param")
	public void invalid_test_datefrom_publicapi() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "ef");

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("date_range_from", "16-Aug-2022");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results == 0, "Verify the search result count is 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying valid date range")
	public void test_daterange_publicapi() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "ef");

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("date_range_from", "01-Aug-2020");
			formParams.put("date_range_to", "16-Aug-2020");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is greater than 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying invalid date range")
	public void invalid_test_daterange_publicapi() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "ef");

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("date_range_from", "16-Aug-2020");
			formParams.put("date_range_to", "01-Aug-2020");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results == 0, "Verify the search result count is 0");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying start and size of result", dataProvider = "test_start_size_publicapi", dataProviderClass = DataProviderClass.class)
	public void test_start_size_publicapi(String start, String size) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "ef");

			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", size);
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", start);
			formParams.put("date_range_from", "01-Aug-2020");
			formParams.put("date_range_to", "16-Aug-2020");
			formParams.put("doc_type", docTypeList);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);

			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(URI, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);

			int total_results = respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(total_results > 0, "Verify the search result count is greater than 5");

			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			verify.assertTrue(documentResults_size.length() == 5, "Verify the search result count is 5");

			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_id = documentResults_size.getJSONObject(i).getString("doc_id");
						System.out.println(doc_id);
					}
				}
			}

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
}