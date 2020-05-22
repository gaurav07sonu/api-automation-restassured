package com.sentieo.docsearch;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.api.client.util.Data;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.notebook.NotebookApis;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class DocSearchRestApi extends APIDriver {

	public static String doc_id = "";
	public static String doc_type = "";
	public static String title = "";
	public static String filingDate = "";
	APIAssertions verify = new APIAssertions();
	static String uss_ids = "";
	static JSONObject uss_data = null;
	static String feed_name = "";
	static String feed_url = "";
	static String feed_req_id = "";
	static String did = "";
	static JSONArray doc_arr; // doc list 30size

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun = true)
	public void setDoc_ID() throws CoreCommonException {
		DocumentSearch obj = new DocumentSearch();
		doc_arr = obj.fetch_docid();
		Random rand = new Random();
		int rand_int1 = rand.nextInt(doc_arr.length());
		JSONObject result = doc_arr.getJSONObject(rand_int1);
		doc_id = result.getString("id");
		doc_type = result.getString("doc_type");
		title = result.getString("title");
		filingDate = result.getString("filingdate");
	}

//	Fetch saved filters......

	@Test(groups = "ssanisty", description = "Fetch saved filters")
	public void fetchsavedfilters() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_SAVED_FILTERS, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (respJson.getJSONArray("result").length() > 0) {
					verify.assertTrue(!respJson.getJSONArray("result").getJSONObject(0).getString("id").isEmpty(),
							"Verify id should be present for saved filter");
					verify.assertTrue(!respJson.getJSONArray("result").getJSONObject(0).getString("name").isEmpty(),
							"Verify id should be present for saved filter");
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

//	Test Download/Export 	

	@Test(groups = { "bonding22" }, description = "Test Download/Export ")
	public void bulk_download() throws CoreCommonException {
		try {
			String URI = APP_URL + BULK_DOWNLOAD;
			if (!URI.contains("schroders")) {
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("doc_ids", doc_id);
				queryParams.put("email", "gaurav.anand@sentieo.com");
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.assertTrue(!respJson.getJSONObject("result").getString("output").isEmpty(),
							"verify output is not empty");
				}
			} else
				ExtentTestManager.getTest().log(LogStatus.SKIP,
						"test skipped because user is not allowed to download docs on " + URI);
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

//	Fetching doc info	

	@Test(groups = "bonding", description = "Fetching doc info")
	public void fetch_docs_meta_data() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_DOCS_META_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			String docID = "[" + "\"" + doc_id + "\"" + "]";
			queryParams.put("doc_ids", docID);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result").getJSONObject(doc_id);
			String docType = result.get("doc_type").toString();
			String docid = result.getString("doc_id").toString();
			String docTitle = result.getString("title");
			String date = result.getString("filingdate");

			verify.assertEqualsActualContainsExpected(doc_type, docType, "verify docType");
			verify.assertEqualsActualContainsExpected(doc_id, docid, "verify docID");
			boolean status = false;
			if (docTitle.contains(title) && !docTitle.isEmpty() && !title.isEmpty())
				status = true;
			else if (title.contains(docTitle) && !docTitle.isEmpty() && !title.isEmpty())
				status = true;
			if (!status) {
				ExtentTestManager.getTest().log(LogStatus.INFO, "actual : " + docTitle);
				ExtentTestManager.getTest().log(LogStatus.INFO, "Expected : " + title);
			}
			verify.assertTrue(status, "verify document title");
			verify.assertEqualsActualContainsExpected(filingDate, date, "verify document date");

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanityssssss", description = "fetch_note_filters")
	public void fetchnotefilters() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_NOTE_FILTERS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result"), "Data should be present");
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "ssafsf", description = "fetch_searchlibrary")
	public void fetchsearchlibrary() throws Exception {
		try {
			String URI = APP_URL + FETCH_SEARCH_LIBRARY;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = queryParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.assertTrue(!respJson.getJSONObject("result").toString().isEmpty(), "Reponse should be present");
			verify.verifyEquals(apiResp.getResponseHeaderValue("content-encoding"), "gzip", "Verify response encoding");
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanitsssssy", description = "fetch_custom_doc_diff")
	public void fetch_custom_doc_diff() throws CoreCommonException {

		try {
			String docid = "";
			String custom_doc_id = "";
			// Fetch 10k docs
			HashMap<String, String> getDocParams = new HashMap<String, String>();
			getDocParams.put("tickers", "TSLA");
			getDocParams.put("facets_flag", "false");
			getDocParams.put("filters",
					"{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"regions\":{},\"source\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"all\"]}}},\"other\":{}}");
			getDocParams.put("default_sort", "date");
			getDocParams.put("sort", "filing_date:desc");

			RequestSpecification specDoc = formParamsSpec(getDocParams);
			Response respDoc = RestOperationUtils.post(APP_URL + FETCH_SEARCH, null, specDoc, getDocParams);
			APIResponse apiRespDoc = new APIResponse(respDoc);
			JSONObject respJsonDoc = new JSONObject(apiRespDoc.getResponseAsString());
			verify.verifyStatusCode(apiRespDoc.getStatusCode(), 200);
			verify.verifyResponseTime(respDoc, 5000);
			verify.verifyEquals(respJsonDoc.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			if (apiRespDoc.getStatusCode() == 200) {
				if (respJsonDoc.getJSONObject("result").getInt("total_results") > 1) {
					JSONArray docs = respJsonDoc.getJSONObject("result").getJSONArray("docs");
					for (int i = 0; i < docs.length(); i++) {
						if (docs.getJSONObject(i).getString("doc_subtype").equalsIgnoreCase("10-K")) {
							docid = docs.getJSONObject(i).getString("id");
							for (int j = i + 1; j < docs.length(); j++) {
								if (docs.getJSONObject(j).getString("doc_subtype").equalsIgnoreCase("10-K")) {
									custom_doc_id = docs.getJSONObject(j).getString("id");
									break;
								}
							}
							break;
						}
					}
				}
			}
			// custom doc diff call
			if (!docid.isEmpty() && !custom_doc_id.isEmpty()) {
				String URI = APP_URL + FETCH_CUSTOM_DOC_DIFF;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("doc_id", docid);
				queryParams.put("custom_doc_id", custom_doc_id);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyTrue(respJson.getJSONObject("result"), "Result should not be empty");
				verify.assertTrue(respJson.getJSONObject("result").getDouble("diff_percent") > 0,
						"Percentage should be more than zero");
			} else
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Subsequent 10K doc not available for ticker");

		} catch (JSONException e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sasafsnity", description = "fetch_impact_score")
	public void fetch_impact_score() throws CoreCommonException {
		try {
			if (doc_arr.length() > 0) {
				did = did + doc_arr.getJSONObject(0).getString("id");
				for (int i = 1; i < doc_arr.length(); i++)
					did = did + "," + doc_arr.getJSONObject(i).getString("id");
			}
			System.out.println(did);
			String URI = APP_URL + FETCH_IMPACT_SCORE;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("did", did);
			queryParams.put("query", "sales");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray result = respJson.getJSONArray("result");
				boolean datacheck = true;
				if (result.length() != 0) {
					for (int i = 0; i < result.length(); i++) {
						JSONArray subData = result.getJSONArray(i);
						if (subData.getString(0).isEmpty() && subData.getInt(1) > 0) {
							datacheck = false;
							verify.assertTrue(datacheck, "verifying data present in result array :  "
									+ subData.getString(0) + "," + subData.getInt(1));
						}
					}
					if (datacheck)
						verify.assertTrue(datacheck, "Valid data present in result array");
				}
			}
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "ssssss", description = "load_saved_search_data")
	public void load_saved_search_data() throws CoreCommonException {
		try {
			if (uss_ids.isEmpty())
				load_user_search();
			if (!uss_ids.isEmpty()) {
				String URI = APP_URL + LOAD_SAVED_SEARCH_DATA;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				String ussID = "[" + "\"" + uss_ids + "\"" + "]";
				queryParams.put("uss_ids", ussID);
				queryParams.put("uss_data", uss_data.toString());
				queryParams.put("start", "0");
				queryParams.put("size", "5");
				queryParams.put("all_tickers", "[]");
				queryParams.put("tickers", "[]");
				queryParams.put("watchlistIds", "[]");
				queryParams.put("watchlist_tickers", "[]");
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyResponseTime(resp, 5000);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");

					JSONArray ussData = respJson.getJSONObject("result").getJSONArray("uss_data");
					verify.assertTrue(ussData != null || uss_data.length() > 0, "uss data array should not be empty");
				}
			} else
				ExtentTestManager.getTest().log(LogStatus.SKIP, "saved search not present");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanitsay", description = "fetch_company_docs", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_company_docs(String ticker) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_COMPANY_DOCS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("ticker", ticker);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONObject result = respJson.getJSONObject("result");
				System.out.println(result);
				String key;
				Iterator<String> keys = result.keys();
				while (keys.hasNext() && result != null) {
					key = keys.next();
					System.out.println(key);
					JSONArray keydata = result.getJSONArray(key);
					if (keydata.length() != 0) {
						boolean datacheck = true;
						for (int i = 0; i < keydata.length(); i++) {
							if (key.equalsIgnoreCase("other_facets")) {
								if (keydata.getString(i).isEmpty()) {
									datacheck = false;
									verify.assertTrue(datacheck, "data not present in key :  " + key);
								}
							} else {
								if (keydata.getJSONObject(i).length() == 0) {
									datacheck = false;
									verify.assertTrue(datacheck, "data not present in key :  " + key);
								}
							}
						}
						if (datacheck)
							verify.assertTrue(datacheck, "Valid data present for : " + key);
					}
				}
			}
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "afafs", description = "Fetch Sections")
	public void fetch_sections() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(APP_URL + FETCH_SECTIONS, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result").getJSONObject("sections");
			System.out.println(result);
			String key;
			Iterator<String> keys = result.keys();
			while (keys.hasNext() && result != null) {
				key = keys.next();
				System.out.println(key);
				JSONArray keydata = result.getJSONArray(key);
				if (keydata.length() != 0) {
					boolean datacheck = true;
					for (int i = 0; i < keydata.length(); i++) {
						if (keydata.getJSONObject(i).length() == 0) {
							datacheck = false;
							verify.assertTrue(datacheck, "data not present in key :  " + key);
						}
					}
					if (datacheck)
						verify.assertTrue(datacheck, "Valid data present for : " + key);
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanidty", description = "fetch_pdf_flag")
	public void fetch_pdf_flag() throws CoreCommonException {
		try {
			NotebookApis notebook = new NotebookApis();
			if (NotebookApis.doc_id_noncsv_sentieoDrive.isEmpty())
				notebook.get_hierarchy_sentieoDrive();
			if (!NotebookApis.doc_id_noncsv_sentieoDrive.isEmpty()) {
				String URI = USER_APP_URL + FETCH_PDF_FLAG;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("id", NotebookApis.doc_id_noncsv_sentieoDrive);
				queryParams.put("doctype", "file");
				queryParams.put("counter", "1");
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.get(URI, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					System.out.println(respJson);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyTrue(respJson.getJSONObject("result").get("pdf_download"),
							"pdf_download check should be present");
					verify.verifyTrue(respJson.getJSONObject("result").get("pdf_view"),
							"pdf_view check should be present");
				}
			}
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_note_doc", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_note_doc(String doc_id, String error) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_NOTE_DOC;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);

			if (!StringUtils.isEmpty(error)) {
				String actualErorr = respJson.getJSONObject("response").getString("error");
				verify.verifyEquals(actualErorr, error, "Comparing error message");
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), false,
						"Verify the API Response Status");
			} else {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_transform_doc_content", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_transform_doc_content(String id) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_TRANSFORM_DOC_CONTENT;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			// JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			// verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"),
			// true,
			// "Verify the API Response Status");
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_exhibits")
	public void fetch_exhibits() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_EXHIBITS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("docid", doc_id);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_pagelink")
	public void fetch_pagelink() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_PAGELINK;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", doc_id);
			// queryParams.put("type", type);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	// @Test(groups = "sanity", description = "pub_doc_viewer", dataProvider =
	// "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void pub_doc_viewer(String id, String free_call) throws CoreCommonException {
		try {
			String URI = APP_URL + PUB_DOC_VIEWER;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
			queryParams.put("free_call", free_call);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			// JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "pub_doc_viewer", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void pub_doc_viewer1(String id, String free_call, String error) throws CoreCommonException {
		try {
			String URI = APP_URL + PUB_DOC_VIEWER;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
			queryParams.put("free_call", free_call);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (!StringUtils.isEmpty(error)) {
				String actualErorr = respJson.getJSONObject("response").getString("error");
				verify.verifyEquals(actualErorr, error, "Comparing error message");
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), false,
						"Verify the API Response Status");
			}

			else {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get_bulk_download_doc", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void get_bulk_download_doc(String id, String viewer) throws CoreCommonException {
		try {
			String URI = APP_URL + GET_BULK_DOWNLOAD_DOC;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
			queryParams.put("viewer", viewer);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get_doc_pdf", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void get_doc_pdf(String doc_id, String viewer) throws Exception {
		try {
			String URI = APP_URL + GET_DOC_PDF;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);
			queryParams.put("viewer", viewer);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(!apiResp.getResponseAsString().isEmpty(), "response should not be empty");
				verify.verifyEquals(apiResp.getResponseHeaderValue("content-type"), "application/pdf",
						"verify content type");
			}
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "bonding", description = "query_suggest_autocomplete", dataProvider = "autocomplete", dataProviderClass = DataProviderClass.class)
	public void query_suggest_autocomplete(String text, String tickers) throws CoreCommonException {
		try {
			String URI = APP_URL + QUERY_SUGGEST_AUTOCOMPLETE;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("text", text);
			queryParams.put("tickers", tickers);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray autocompleteResult = respJson.getJSONArray("result");
			verify.assertTrue(autocompleteResult.length() != 0, "Verify Query Autocomple ");
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get_user_downloaded_docs_status ")
	public void get_user_downloaded_docs_status() throws CoreCommonException {
		try {
			String URI = APP_URL + GET_USER_DOWNLOADED_DOCS_STATUS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyAll();
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_snippets", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_snippets(String doc_id, String tickers, String query, String error) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SNIPPETS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);
			queryParams.put("tickers", tickers);
			queryParams.put("query", query);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (!StringUtils.isEmpty(error)) {
				String actualErorr = respJson.getJSONObject("response").getString("error");
				verify.verifyEquals(actualErorr, error, "Comparing error message");
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), false,
						"Verify the API Response Status");
			}

			else {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanitsssy", description = "search with query and getting snippets count", dataProvider = "fetch_search_term_count", dataProviderClass = DataProviderClass.class)
	public void fetch_search_term_count(String ticker, String filters, String size) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH_TERM_COUNT;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("applied_filter", "doctype");
			queryParams.put("facets_flag", "false");
			queryParams.put("filters", filters);
			queryParams.put("size", size);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			if (apiResp.getStatusCode() == 200) {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray search_data = respJson.getJSONObject("result").getJSONArray("search_data");
				verify.assertTrue(search_data.length() > 0, "Search data array should be present");

				boolean datacheck = true;
				if (search_data.length() != 0) {
					for (int i = 0; i < search_data.length(); i++) {
						JSONArray subData = search_data.getJSONArray(i);
						System.out.println(subData.getDouble(0));
						System.out.println(subData.getInt(1));
						if (subData.getDouble(0) > 0 && subData.getInt(1) > 0)
							datacheck = false;
					}
					if (!datacheck)
						verify.assertTrue(datacheck, "verifying data present in search_data array");
				}

				JSONArray normal = respJson.getJSONObject("result").getJSONArray("normal");
				verify.assertTrue(normal.length() > 0, "Normal data array should be present");

				datacheck = true;
				if (normal.length() != 0) {
					for (int i = 0; i < normal.length(); i++) {
						JSONArray subData = normal.getJSONArray(i);
						System.out.println(subData.getDouble(0));
						System.out.println(subData.getInt(1));
						if (subData.getDouble(0) > 0 && subData.getInt(1) > 0)
							datacheck = false;
					}
					if (!datacheck)
						verify.assertTrue(datacheck, "verifying data present in normal array");
				}

			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "cd", description = "used to view document when user clicks doc from search result", priority = 1)
	public void fetch_user_viewed_docs() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_USER_VIEWED_DOCS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("did", doc_id);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONArray("result").getJSONObject(0);
			String fetchID = result.getString("doc_id");
			verify.assertEqualsActualContainsExpected(doc_id, fetchID, "Verify document id");
			boolean viewedStatus = result.getBoolean("viewed");
			verify.assertTrue(viewedStatus, "verify doc view status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "cd", description = "used to view document when user clicks doc from search result", priority = 0)
	public void index_user_viewed_doc() throws CoreCommonException {
		try {
			String URI = USER_APP_URL + INDEX_USER_VIEWED_DOC;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			boolean status = respJson.getJSONObject("result").getBoolean(doc_id);
			verify.assertTrue(status, "Verify Index user viewed docs");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "bonding", description = "fetches all info at user`s search landing page")
	public void fetch_landing_page_data() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_LANDING_PAGE_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			JSONObject result = respJson.getJSONObject("result");
			JSONArray latest_filing_list = result.getJSONArray("latest_filing_list");
			JSONArray recent_open_docs = result.getJSONArray("recent_open_docs");
			JSONArray search_list = result.getJSONArray("search_list");
			JSONArray recent_search_tickers = result.getJSONArray("recent_search_tickers");
			JSONObject recent_ticker_entity_info = result.getJSONObject("recent_ticker_entity_info");
			verify.assertTrue(latest_filing_list.length() != 0,
					"verify latest_filing_list is not blank on landing page");
			verify.assertTrue(recent_open_docs.length() != 0, "verify recent_open_docs is not blank on landing page");

			verify.assertTrue(search_list.length() != 0, "verify search_list is not blank on landing page");
			verify.assertTrue(recent_search_tickers.length() != 0,
					"verify recent_search_tickers is not blank on landing page");
			verify.assertTrue(recent_ticker_entity_info.length() != 0,
					"verify recent_ticker_entity_info is not blank on landing page");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "saves user searches")
	public void save_user_search() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + SAVE_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			queryParams.put("query", "newyork");
			queryParams.put("filters",
					"{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("force_save", "true");
			queryParams.put("name", "newyork");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "adsf", description = "fetches meta info like date, file type etc.")
	public void fetch_files_meta_data() throws CoreCommonException {
		try {
			NotebookApis notebook = new NotebookApis();
			if (NotebookApis.doc_id_sentieoDrive.isEmpty())
				notebook.get_hierarchy_sentieoDrive();
			if (!NotebookApis.doc_id_sentieoDrive.isEmpty()) {
				String URI = USER_APP_URL + FETCH_FILES_META_DATA;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("doc_ids", "[\"" + NotebookApis.doc_id_sentieoDrive + "\"]");

				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.get(URI, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					System.out.println(respJson);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(
							respJson.getJSONObject("result").getJSONObject(NotebookApis.doc_id_sentieoDrive)
									.getJSONObject("docError").getBoolean("error"),
							false, "Doc should not have any error");
					verify.assertTrue(!respJson.getJSONObject("result").getJSONObject(NotebookApis.doc_id_sentieoDrive)
							.getString("doc_type").isEmpty(), "doc type should be present");
					verify.verifyEquals(
							respJson.getJSONObject("result").getJSONObject(NotebookApis.doc_id_sentieoDrive)
									.getString("doc_id"),
							NotebookApis.doc_id_sentieoDrive, "Doc id should be same as fetched doc");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanssity", description = "load file contents")
	public void fetch_file_content() throws CoreCommonException {
		try {
			NotebookApis notebook = new NotebookApis();
			if (NotebookApis.doc_id_sentieoDrive.isEmpty())
				notebook.get_hierarchy_sentieoDrive();
			if (!NotebookApis.doc_id_sentieoDrive.isEmpty()) {
				String URI = USER_APP_URL + FETCH_FILE_CONTENT;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("id", NotebookApis.doc_id_sentieoDrive);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.get(URI, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				String responseHtml = apiResp.getResponseAsString();
				verify.assertTrue(!responseHtml.isEmpty(), "verify response is not blank");
				verify.verifyEquals(apiResp.getResponseHeaderValue("content-type"), "application/pdf",
						"verify content type");
			} else
				ExtentTestManager.getTest().log(LogStatus.SKIP, "no data in sentieo drive");
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "saves user searches")
	public void load_user_search() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + LOAD_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (respJson.getJSONObject("result").getJSONArray("userss").length() > 0) {
					uss_ids = respJson.getJSONObject("result").getJSONArray("userss").getJSONObject(0).getString("id");
					uss_data = respJson.getJSONObject("result").getJSONArray("userss").getJSONObject(0);
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "deletes user`s saved searches")
	public void delete_saved_search() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + DELETE_SAVED_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", "5e58457a5133ce1c2e3cf8c9");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

//	@Test(groups = "sanity", description = "pdf view for note documents", dataProvider = "get_docnote_pdf", dataProviderClass = DataProviderClass.class)
	public void get_docnote_pdf(String doc_id) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + GET_DOCNOTE_PDF;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanitysss", description = "Unsubscribe existing RSS Feed")
	public void ra__unsubscribe_feed() throws CoreCommonException {
		try {
			if (feed_req_id.isEmpty())
				fetch_search_filters();
			if (!feed_req_id.isEmpty()) {
				String URI = APP_URL + UNSUBSCRIBE_FEED;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("feed_req_id", feed_req_id);

				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson.toString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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

	@Test(groups = "sanitysss", description = "Requesting for RSS Feed")
	public void request_feed() throws CoreCommonException {
		try {
			if (feed_req_id.isEmpty())
				fetch_search_filters();
			if (!feed_req_id.isEmpty()) {
				String URI = APP_URL + REQUEST_FEED;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("feed_name", feed_name);
				queryParams.put("feed_url", feed_url);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson.toString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				feed_req_id = respJson.getJSONObject("result").getString("feed_req_id");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanitysss", description = "Updating existing RSS Feed")
	public void update_feed() throws CoreCommonException {

		try {
			if (feed_req_id.isEmpty())
				fetch_search_filters();
			if (!feed_req_id.isEmpty()) {
				String URI = APP_URL + UPDATE_FEED;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("feed_req_id", feed_req_id);
				queryParams.put("feed_name", feed_name);
				queryParams.put("feed_url", feed_url);

				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson.toString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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

	@Test(groups = "sanity", description = "fetching note info from doc", dataProvider = "fetch_document_note_info", dataProviderClass = DataProviderClass.class)
	public void fetch_document_note_info(String doc_ids, String note_ids) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_DOCUMENT_NOTE_INFO;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_ids", doc_ids);
			queryParams.put("note_ids", note_ids);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetching saved filters")
	public void fetch_search_filters() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH_FILTERS;
			HashMap<String, String> queryParams = new HashMap<String, String>();

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (respJson.getJSONObject("result") != null) {
					JSONObject rss = respJson.getJSONObject("result").getJSONObject("filters").getJSONArray("rss")
							.getJSONObject(0);
					if (rss != null) {
						feed_name = rss.getJSONArray("categories").getJSONObject(0).getJSONArray("items")
								.getJSONObject(0).getString("label");
						feed_url = rss.getJSONArray("categories").getJSONObject(0).getJSONArray("items")
								.getJSONObject(0).getString("url");
						;
						feed_req_id = rss.getJSONArray("categories").getJSONObject(0).getJSONArray("items")
								.getJSONObject(0).getString("feed_req_id");
						;
					}
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetching saved search setting")
	public void fetch_search_settings() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_SEARCH_SETTINGS;
			HashMap<String, String> queryParams = new HashMap<String, String>();

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetching note results in Single tenants only", dataProvider = "fetch_note_search", dataProviderClass = DataProviderClass.class)
	public void fetch_note_search(String note_type, String filing_type, String filters) throws CoreCommonException {

		if (USER_APP_URL.contains("app") || USER_APP_URL.contains("testing") || USER_APP_URL.contains("app2")) {
			ExtentTestManager.getTest().log(LogStatus.SKIP, "test skipped because this api is valid for STs only ");
		} else {
			try {
				String URI = USER_APP_URL + FETCH_NOTE_SEARCH;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("note_type", note_type);
				queryParams.put("filing_type", filing_type);
				queryParams.put("facets_flag", "false");
				queryParams.put("filters", filters);

				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");

				int total_results = respJson.getJSONObject("result").getInt("total_results");
				verify.assertTrue(total_results > 0, "Verify the search result count is more than 0");

			}

			catch (Exception e) {
				throw new CoreCommonException(e);
			} finally {
				verify.verifyAll();
			}
		}

	}

	@Test(groups = "sanity", description = "Showing content of note docs in Single tenants only", dataProvider = "fetch_transform_note_content", dataProviderClass = DataProviderClass.class)
	public void fetch_transform_note_content(String doc_type, String id) throws CoreCommonException {

		if (USER_APP_URL.contains("app") || USER_APP_URL.contains("testing") || USER_APP_URL.contains("app2")) {
			ExtentTestManager.getTest().log(LogStatus.SKIP, "test skipped because this api is valid for STs only ");
		} else {

			try {
				String URI = USER_APP_URL + FETCH_TRANSFORM_NOTE_CONTENT;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("doc_type", doc_type);
				queryParams.put("id", id);
				queryParams.put("facets_flag", "false");

				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
			}

			catch (Exception e) {
				throw new CoreCommonException(e);
			} finally {
				verify.verifyAll();
			}
		}
	}
}
