package com.sentieo.docsearch;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class DocSearchRestApi extends APIDriver {

	APIAssertions verify = new APIAssertions();

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

		RestAssured.baseURI = USER_APP_URL;
	}

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

//	Fetch saved filters......

	@Test(groups = "sanity", description = "Fetch saved filters")
	public void fetchsavedfilters() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(FETCH_SAVED_FILTERS, null, spec, queryParams);
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

//	Test Download/Export 	

	@Test(groups = "sanity", description = "Test Download/Export ", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void bulk_download(String doc_ids, String email) throws CoreCommonException {
		try {
			String URI = APP_URL + BULK_DOWNLOAD;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_ids", doc_ids);
			queryParams.put("email", email);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
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

//	Fetching doc info	

	@Test(groups = "sanity", description = "Fetching doc info", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_docs_meta_data(String doc_ids) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_DOCS_META_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_ids", doc_ids);
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

	@Test(groups = "sanity", description = "fetch_note_filters")
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
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_searchlibrary")
	public void fetchsearchlibrary() throws CoreCommonException {
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
		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_custom_doc_diff", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_custom_doc_diff(String doc_id, String custom_doc_id) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_CUSTOM_DOC_DIFF;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("doc_id", doc_id);
			queryParams.put("custom_doc_id", custom_doc_id);
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

	@Test(groups = "sanity", description = "fetch_impact_score", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_impact_score(String did, String query) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_IMPACT_SCORE;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("did", did);
			queryParams.put("query", query);
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

	@Test(groups = "sanity", description = "load_saved_search_data", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void load_saved_search_data(String uss_ids, String tickers) throws CoreCommonException {

		try {
			String URI = APP_URL + LOAD_SAVED_SEARCH_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("uss_ids", uss_ids);
			queryParams.put("tickers", tickers);
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

	@Test(groups = "sanity", description = "fetch_company_docs", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_company_docs(String ticker) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_COMPANY_DOCS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("ticker", ticker);
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

	@Test(groups = "sanity", description = "Fetch Sections")
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
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "fetch_pdf_flag", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_pdf_flag(String id) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_PDF_FLAG;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
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

	@Test(groups = "sanity", description = "fetch_searchlibrary")
	public void fetch_searchlibrary() throws CoreCommonException {
		try {
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(APP_URL + FETCH_SEARCHLIBRARY, spec, queryParams);
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

	@Test(groups = "sanity", description = "fetch_exhibits", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_exhibits(String docid, String error) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_EXHIBITS;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("docid", docid);
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

	@Test(groups = "sanity", description = "fetch_pagelink", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetch_pagelink(String id, String error) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_PAGELINK;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("id", id);
			// queryParams.put("type", type);
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

	@Test(groups = "sanity", description = "pub_doc_viewer", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
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
	public void get_doc_pdf(String doc_id, String viewer) throws CoreCommonException {
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

		} catch (JSONException e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "query_suggest_autocomplete", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void query_suggest_autocomplete(String text, String tickers) throws CoreCommonException {
		try {
			String URI = APP_URL + QUERY_SUGGEST_AUTOCOMPLETE;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("text", text);
			queryParams.put("tickers", tickers);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);

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
	
	
	
	@Test(groups = "sanity", description = "search with query and getting snippets count", dataProvider = "fetch_search_term_count", dataProviderClass = DataProviderClass.class)
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
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
		
			
						
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}
	
	
	@Test(groups = "sanity", description = "used to view document when user clicks doc from search result", dataProvider = "fetch_user_viewed_docs", dataProviderClass = DataProviderClass.class)
	public void fetch_user_viewed_docs(String did) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_USER_VIEWED_DOCS;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("did", did);
			
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
	
	
	@Test(groups = "sanity", description = "used to view document when user clicks doc from search result", dataProvider = "index_user_viewed_doc", dataProviderClass = DataProviderClass.class)
	public void index_user_viewed_doc(String did) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + INDEX_USER_VIEWED_DOC;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("did", did);
			
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
	
	
	@Test(groups = "sanity", description = "fetches all info at user`s search landing page")
	public void fetch_landing_page_data() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_LANDING_PAGE_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("counter", "1");
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
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
	
	
	@Test(groups = "sanity", description = "saves user searches")
	public void save_user_search() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + SAVE_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("counter", "1");
			queryParams.put("query", "newyork");
			queryParams.put("filters", "{\"ticker\":{},\"doctype\":{},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			queryParams.put("force_save", "true");
			queryParams.put("name", "newyork");
			
			
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
	
	
	@Test(groups = "sanity", description = "fetches meta info like date file type etc.", dataProvider = "fetch_files_meta_data", dataProviderClass = DataProviderClass.class)
	public void fetch_files_meta_data(String id) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_FILES_META_DATA;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("id", id);		
			
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
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
	
	
	@Test(groups = "sanity", description = "loads file contents", dataProvider = "fetch_file_content", dataProviderClass = DataProviderClass.class)
	public void fetch_file_content(String id) throws CoreCommonException {

		try {
			String URI = USER_APP_URL + FETCH_FILE_CONTENT;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			queryParams.put("id", id);
			
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
	
	
	@Test(groups = "sanity", description = "saves user searches")
	public void load_user_search() throws CoreCommonException {

		try {
			String URI = USER_APP_URL + LOAD_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();	
			
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
	
	
//	@Test(groups = "sanity", description = "fetching note results in Single tenants only", dataProvider = "fetch_note_search", dataProviderClass = DataProviderClass.class)
	public void fetch_note_search(String note_type, String filing_type, String filters) throws CoreCommonException {

		if (USER_APP_URL.contains("citadel") || USER_APP_URL.contains("balyasny") || USER_APP_URL.contains("schroders") || USER_APP_URL.contains("tigerglobal") || USER_APP_URL.contains("psp")) {

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
				System.out.println(respJson.toString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");

				int total_results = respJson.getJSONObject("result").getInt("total_results");
				verify.verifyTrue(total_results > 0, "Verify the search result count is more than 0");

		}

			catch (Exception e) {
				throw new CoreCommonException(e);
			} finally {
				verify.verifyAll();
			}
		} else {
		}
	}

	
	@Test(groups = "sanity", description = "pdf view for note documents", dataProvider = "get_docnote_pdf", dataProviderClass = DataProviderClass.class)
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
	
 }
