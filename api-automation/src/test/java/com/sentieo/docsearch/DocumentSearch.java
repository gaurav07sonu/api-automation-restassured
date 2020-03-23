package com.sentieo.docsearch;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class DocumentSearch extends APIDriver {

	@BeforeClass(alwaysRun = true)
	public void setup() throws CoreCommonException {
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

	@Test(groups = "devesh", description = "Fetch search", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearch(String ticker, String query, String fillingtype, String filing_subtype, String sensivity,
			String facets_flag) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", fillingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("sensitivity_setting", sensivity);
			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			if (!facets_flag.equalsIgnoreCase("true")) {
				verify.verifyEquals(
						respJson.getJSONObject("result").getJSONArray("docs").getJSONObject(0).getString("ticker"),
						ticker, "Verify Entered ticker should visible in the doc");

			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "Fetch search ticker only", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchtickerOnly(String ticker, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			if (!facets_flag.equalsIgnoreCase("true")) {
				verify.verifyEquals(
						respJson.getJSONObject("result").getJSONArray("docs").getJSONObject(0).getString("ticker"),
						ticker, "Verify Entered ticker should visible in the doc");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Fetch search", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearch2(String no_docs, String facets_flag, String error) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("facets_flag", facets_flag);
			queryParams.put("no_docs", no_docs);

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);

			if (!StringUtils.isEmpty(error)) {
				String actualErorr = respJson.getJSONObject("response").getString("error");
				verify.verifyEquals(actualErorr, error, "Comparing error message");
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), false,
						"Verify the API Response Status");
			} else {
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "fetch_search_tickerOnly", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchSearchOnly(String query, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("query", query);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "Fetch search", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchDoctype(String fillingtype, String fillingsubtype, String facets_flag)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("filing_type", fillingtype);
			queryParams.put("fillingsubtype", fillingsubtype);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "Fetch search", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchSector(String sector, String subsector, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("sector", sector);
			queryParams.put("subsector", subsector);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "Fetch search", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchRegions(String geog_filter, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("geog_filter", geog_filter);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "Fetch search source", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchSource(String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("etype",
					"OrdinaryShares#$AmericanDepositoryReceipt#$Debt#$ExchangeTradedFund#$other#$FedRes#$CryptoCurrency#$");
			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "Fetch search source", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchDate(String period, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("period", period);
			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

	@Test(groups = "sanity", description = "fetchsearchContext", dataProvider = "SearchProvider", dataProviderClass = DataProviderClass.class)
	public void fetchsearchContext(String context, String facets_flag) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("context", context);
			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

//	Taking Doc type & sub types and Dates as filter combination.....

	@Test(groups = "sanity", description = "Taking Doc type and Date as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter1(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String period, String sensitivity, String facets_flag, String error)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("period", period);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

//	Taking Doc type & sub types and Region as filter combination.....

	@Test(groups = "sanity", description = "Taking Doc type and Region as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter2(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String geog_filter, String sensitivity, String facets_flag, String error)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("geog_filter", geog_filter);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

//	Taking Doc type & sub types and Sector as filter combination.....

	@Test(groups = "sanity", description = "Taking Doc type and Sector as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter3(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String sector, String subsector, String sensitivity, String facets_flag, String error)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("sector", sector);
			queryParams.put("subsector", subsector);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

//	Taking Doc type & sub types, Sector and Dates as filter combination.....	

	@Test(groups = "sanity", description = "Taking Doc type, Date and Sector as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter4(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String period, String sector, String subsector, String sensitivity, String facets_flag,
			String error) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("sector", sector);
			queryParams.put("subsector", subsector);
			queryParams.put("period", period);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

//	Taking Doc type & sub types, geog_filter(Region) and Dates as filter combination.....		

	@Test(groups = "sanity", description = "Taking Doc type, Date and Sector as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter5(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String period, String geog_filter, String sensitivity, String facets_flag, String error)
			throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("geog_filter", geog_filter);
			queryParams.put("period", period);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}

//	Taking Doc type & sub types, geog_filter(Region) and sector as filter combination.....		

	@Test(groups = "sanity", description = "Taking Doc type, Regions and Sector as filter combination", dataProvider = "fetch_search_SearchOnly", dataProviderClass = DataProviderClass.class)
	public void fetchsearchfilter6(String ticker, String query, String filingtype, String filing_subtype,
			String tt_category, String sector, String subsector, String geog_filter, String sensitivity,
			String facets_flag, String error) throws CoreCommonException {

		try {
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("tickers", ticker);
			queryParams.put("query", query);
			queryParams.put("filing_type", filingtype);
			queryParams.put("filing_subtype", filing_subtype);
			queryParams.put("tt_category", tt_category);
			queryParams.put("sensitivity_setting", sensitivity);
			queryParams.put("geog_filter", geog_filter);
			queryParams.put("sector", sector);
			queryParams.put("subsector", subsector);

			if (facets_flag.equalsIgnoreCase("true")) {
				queryParams.put("facets_flag", facets_flag);
				queryParams.put("no_docs", "6");
			} else {
				queryParams.put("facets_flag", facets_flag);
			}

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			//System.out.println(respJson.toString());
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

		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}

	}
}