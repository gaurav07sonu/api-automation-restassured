package com.sentieo.docsearch;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONObject;
import org.json.JSONArray;
import org.testng.annotations.BeforeMethod;
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
import com.sentieo.utils.CoreCommonException;

public class DocumentSearch extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "doc search with queries", dataProvider = "test_doctype_query", dataProviderClass = DataProviderClass.class)
	public void test_doctype_query(String ticker, String query, String filters) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			// queryParams.put("filters", filters);
			queryParams.put("facets_flag", "false");

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			if (apiResp.getStatusCode() == 200) {
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");

				int total_results = respJson.getJSONObject("result").getInt("total_results");
				verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

				JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
				boolean tickerCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
							System.out.println(tickers.toString().contains(ticker.toLowerCase()));
							if (!tickers.toString().contains(ticker.toLowerCase()))
								tickerCheck = false;
						}

						verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
					}
				}

				boolean doctypeCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
							System.out.println(doc_type.toString().contains(docType.toLowerCase()));
							if (!doc_type.toString().contains(docType.toLowerCase()))
								doctypeCheck = false;
						}

						verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
					}
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "doc search with queries", dataProvider = "test_doctype_watchlist", dataProviderClass = DataProviderClass.class)
	public void test_doctype_watchlist(String watchlist_tickers, String filters) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("watchlist_tickers", watchlist_tickers);
			queryParams.put("filters", filters);
			queryParams.put("facets_flag", "false");

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			Boolean code_status = verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (code_status.equals(true)) {
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");

				int total_results = respJson.getJSONObject("result").getInt("total_results");
				verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

				JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
				boolean tickerCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
							tickerCheck = false;
						}

						verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
					}
				}

				boolean doctypeCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
							System.out.println(doc_type.toString().contains(docType.toLowerCase()));
							if (!doc_type.toString().contains(docType.toLowerCase()))
								doctypeCheck = false;
						}

						verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
					}
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "applying doc type filters", dataProvider = "test_doctype_Filter", dataProviderClass = DataProviderClass.class)
	public void fetchsearch_doctype(String ticker, String filters) throws CoreCommonException {
		try {
			if (!APP_URL.contains("app") && filters.contains("note")) {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "We are not supporting on : " + APP_URL);
			} else {

				String URI = APP_URL + FETCH_SEARCH;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("tickers", ticker);
				queryParams.put("applied_filter", "doctype");
				queryParams.put("facets_flag", "false");
				queryParams.put("filters", filters);

				JSONObject json = new JSONObject(filters);
				System.out.println(json.getJSONObject("doctype"));
				String docType = "";
				Iterator<String> keys = json.getJSONObject("doctype").keys();
				while (keys.hasNext()) {
					docType = keys.next();
					System.out.println(docType);

					RequestSpecification spec = formParamsSpec(queryParams);
					Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					System.out.println(respJson.toString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 10000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");

					int total_results = respJson.getJSONObject("result").getInt("total_results");
					verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

					JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
					boolean tickerCheck = true;
					if (total_results != 0) {
						if (documentResults_size.length() != 0) {
							for (int i = 0; i < documentResults_size.length(); i++) {
								JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
								System.out.println(tickers.toString().contains(ticker.toLowerCase()));
								if (!tickers.toString().contains(ticker.toLowerCase()))
									tickerCheck = false;
							}

							verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
						}
					}

					boolean doctypeCheck = true;
					if (total_results != 0) {
						if (documentResults_size.length() != 0) {
							for (int i = 0; i < documentResults_size.length(); i++) {
								String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
								System.out.println(doc_type.toString().contains(docType.toLowerCase()));
								if (!doc_type.toString().contains(docType.toLowerCase()))
									doctypeCheck = false;
							}

							verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
						}
					}
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "ssafsfsa", description = "doc type and date as filter combinations", dataProvider = "doctype_date_filters_combination", dataProviderClass = DataProviderClass.class)
	public void docsearch_date_filter(String ticker, String filters) throws CoreCommonException {
		try {
			if (!APP_URL.contains("app") && filters.contains("note")) {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "We are not supporting on : " + APP_URL);
			} else {
				String URI = APP_URL + FETCH_SEARCH;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("tickers", ticker);
				queryParams.put("applied_filter", "doctype");
				queryParams.put("facets_flag", "false");
				queryParams.put("filters", filters);	
				String filter = filters.replaceAll("\"date\":{\"\":{\"\":", "\"date\":{\"one\":{\"two\":");
				JSONObject json = new JSONObject(filter);
				System.out.println(json);
				System.out.println(json.getJSONObject("doctype"));
				String docType = "";
				Iterator<String> keys = json.getJSONObject("doctype").keys();
				while (keys.hasNext()) {
					docType = keys.next();
					System.out.println(docType);
				}
				
				String date = json.getJSONObject("date").getJSONArray("values").getString(0);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson.toString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");

				int total_results = respJson.getJSONObject("result").getInt("total_results");
				verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
				JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
				boolean tickerCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
							System.out.println(tickers.toString().contains(ticker.toLowerCase()));
							if (!tickers.toString().contains(ticker.toLowerCase()))
								tickerCheck = false;
						}

						verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
					}
				}

				boolean doctypeCheck = true;
				if (total_results != 0) {
					if (documentResults_size.length() != 0) {
						for (int i = 0; i < documentResults_size.length(); i++) {
							String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
							System.out.println(doc_type.toString().contains(docType.toLowerCase()));
							if (!doc_type.toString().contains(docType.toLowerCase()))
								doctypeCheck = false;
						}

						verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "doc type and custom date as filter combinations", dataProvider = "doctype_customdate_filters_combination", dataProviderClass = DataProviderClass.class)
	public void docsearch_customdate_filter(String ticker, String filters) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("applied_filter", "doctype");
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

			int total_results = respJson.getJSONObject("result").getInt("total_results");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						System.out.println(tickers.toString().contains(ticker.toLowerCase()));
						if (!tickers.toString().contains(ticker.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type.toString().contains(docType.toLowerCase()));
						if (!doc_type.toString().contains(docType.toLowerCase()))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "doc type and language as filter combinations", dataProvider = "doctype_language_filters_combination", dataProviderClass = DataProviderClass.class)
	public void docsearch_language_filter(String ticker, String filters) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("applied_filter", "[\"language\"]");
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

			int total_results = respJson.getJSONObject("result").getInt("total_results");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						System.out.println(tickers.toString().contains(ticker.toLowerCase()));
						if (!tickers.toString().contains(ticker.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type.toString().contains(docType.toLowerCase()));
						if (!doc_type.toString().contains(docType.toLowerCase()))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "doc type and sector as filter combinations", dataProvider = "doctype_sector_filters_combination", dataProviderClass = DataProviderClass.class)
	public void docsearch_sector_filter(String ticker, String filters) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

			int total_results = respJson.getJSONObject("result").getInt("total_results");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						System.out.println(tickers.toString().contains(ticker.toLowerCase()));
						if (!tickers.toString().contains(ticker.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type.toString().contains(docType.toLowerCase()));
						if (!doc_type.toString().contains(docType.toLowerCase()))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "doc type and region as filter combinations", dataProvider = "test_doctype_region_Filter", dataProviderClass = DataProviderClass.class)
	public void docsearch_region_filter(String ticker, String filters) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

			int total_results = respJson.getJSONObject("result").getInt("total_results");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						System.out.println(tickers.toString().contains(ticker.toLowerCase()));
						if (!tickers.toString().contains(ticker.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type.toString().contains(docType.toLowerCase()));
						if (!doc_type.toString().contains(docType.toLowerCase()))
							doctypeCheck = false;
					}

					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "search with Context filter", dataProvider = "test_context_Filter", dataProviderClass = DataProviderClass.class)
	public void docsearch_context_filter(String ticker, String sections_filter, String filters)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("sections_filter", sections_filter);
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);

			JSONObject json = new JSONObject(filters);
			System.out.println(json.getJSONObject("doctype"));
			String docType = "";
			Iterator<String> keys = json.getJSONObject("doctype").keys();
			while (keys.hasNext()) {
				docType = keys.next();
				System.out.println(docType);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

			int total_results = respJson.getJSONObject("result").getInt("total_results");
			verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");
			JSONArray documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
			boolean tickerCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						JSONArray tickers = documentResults_size.getJSONObject(i).getJSONArray("tickers");
						System.out.println(tickers.toString().contains(ticker.toLowerCase()));
						if (!tickers.toString().contains(ticker.toLowerCase()))
							tickerCheck = false;
					}

					verify.assertTrue(tickerCheck, "verifying ticker visibility in doc ");
				}
			}

			boolean doctypeCheck = true;
			if (total_results != 0) {
				if (documentResults_size.length() != 0) {
					for (int i = 0; i < documentResults_size.length(); i++) {
						String doc_type = documentResults_size.getJSONObject(i).getString("doc_type");
						System.out.println(doc_type.toString().contains(docType.toLowerCase()));
						if (!doc_type.toString().contains(docType.toLowerCase()))
							doctypeCheck = false;
					}
					verify.assertTrue(doctypeCheck, "verifying doctype visibility in doc ");
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "for getting form count for doc subtypes")
	public void fetchsearch_nodoc_1() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "1");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Ticker pagination from ticker filter ")
	public void fetchsearch_nodoc_2() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "2");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "ticker with no pagination ")
	public void fetchsearch_nodoc_4() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "4");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanisafswty", description = "save as spreadsheet from action menu ")
	public void fetchsearch_nodoc_5() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "5");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONArray csv = respJson.getJSONObject("result").getJSONArray("csv");
			if(csv.length()==0 && csv==null)
				verify.assertTrue(false, "csv result not fetched");
			verify.verifyTrue(respJson.getJSONObject("result").getString("filename"), "filename should be present");
			
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "getting facets count")
	public void fetchsearch_nodoc_6() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"date\":{},\"other\":{},\"rss\":{\"\":{\"\":{\"param\":\"feed_id\",\"values\":[599]}}}}");
			queryParams.put("no_docs", "6");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONObject facets = respJson.getJSONObject("result").getJSONObject("facets");
				verify.assertTrue(facets.length() > 0 && facets != null, "facets object is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "for screener tickers ")
	public void fetchsearch_nodoc_7() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "7");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "for export hits from action menu ")
	public void fetchsearch_nodoc_8() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "8");
			queryParams.put("query", "sales");
			queryParams.put("email", "gaurav.anand@sentieo.com");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "for score debug spreadsheet ")
	public void fetchsearch_nodoc_9() throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", "true");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("no_docs", "9");
			queryParams.put("query", "sales");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	public JSONArray fetch_docid() throws CoreCommonException {
		JSONArray documentResults_size = null;
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("query", "sales");
			queryParams.put("size", "30");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			if (apiResp.getStatusCode() == 200) {
				JSONObject total_results = respJson.getJSONObject("result");
				if (total_results.length() != 0) {
					documentResults_size = respJson.getJSONObject("result").getJSONArray("docs");
					return documentResults_size;
				} else
					verify.assertTrue(false, "Search shows blank data");
			} else
				verify.assertTrue(false, "status code is : " + apiResp.getStatusCode());
		}

		catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return documentResults_size;
	}

}