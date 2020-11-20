package com.sentieo.docsearchpublicapis;

import static com.sentieo.constants.Constants.PUBLIC_API_URL;
import static com.sentieo.constants.Constants.SEARCH;
import static com.sentieo.constants.Constants.XAPIKEY1;
import static com.sentieo.constants.Constants.XUSERKEY1;
import static com.sentieo.constants.Constants.X_API_KEY;
import static com.sentieo.constants.Constants.X_USER_KEY;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.sentieo.utils.JSONUtils;
public class entityfieldsearchpublicapi extends APIDriver{
	static APIAssertions verify = null;
	static JSONUtils jsonUtils = null;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_numbersearch_valid", dataProviderClass = DataProviderClass.class)
	public void test_entity_numbersearch_valid(String number_test) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("number_test", number_test );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", "");
			formParams.put("notes_attach_setting", "false");
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==1, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_numbersearch_invalid", dataProviderClass = DataProviderClass.class)
	public void test_entity_numbersearch_invalid(String number_test) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("number_test", number_test );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "0 notes due to invalid input data");
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with valid number range having greater than equal to and less than equal to format", dataProvider = "TestEntityNumberRangeValid", dataProviderClass = DataProviderClass.class)
	public void TestEntityNumberRangeValid(String gte, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", gte);
			number_test.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with invalid number range having greater than equal to and less than equal to format", dataProvider = "TestEntityNumberRangeInvalid", dataProviderClass = DataProviderClass.class)
	public void TestEntityNumberRangeInvalid(String gte, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", gte);
			number_test.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with number field having valid values in greater than equal to and less than format", dataProvider = "EntityNumberRangeScenarioValid1", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioValid1(String gte, String lt) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", gte);
			number_test.put("lt", lt);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in less than and greater than equal to format", dataProvider = "EntityNumberRangeScenarioInvalid1", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioInvalid1(String gte, String lt) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", gte);
			number_test.put("lt", lt);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with number field having valid values in greater than and less than equal to format", dataProvider = "EntityNumberRangeScenarioValid2", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioValid2(String gt, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gt", gt);
			number_test.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in greater than and less than equal to format", dataProvider = "EntityNumberRangeScenarioInvalid1", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioInvalid2(String gt, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gt", gt);
			number_test.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying notes with number field having valid values in greater than and equal to format", dataProvider = "EntityNumberRangeScenarioValid3", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioValid3(String gte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", gte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==1 || count==2 || count==3) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in greater than and equal to format")
	public void EntityNumberRangeScenarioInvalid3() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gte", "1000");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying notes with number field having valid values in less than and equal to format", dataProvider = "EntityNumberRangeScenarioValid4", dataProviderClass = DataProviderClass.class)
	public void EntityNumberRangeScenarioValid4(String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==1 || count==2 || count==3) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in less than and equal to format")
	public void EntityNumberRangeScenarioInvalid4() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("lte", "-500");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with number field having valid values in greater than format")
	public void EntityNumberRangeScenarioValid5() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gt", "-500");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==1 || count==2 || count==3) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in greater than format")
	public void EntityNumberRangeScenarioInvalid5() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("gt", "1000");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verifying notes with number field having valid values in less than format")
	public void EntityNumberRangeScenarioValid6() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("lt", "1000");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==1 || count==2 || count==3) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}	
	
	@Test(description = "Verifying notes with number field having invalid values in less than format")
	public void EntityNumberRangeScenarioInvalid6() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> number_test = new HashMap<String, String>();
			number_test.put("lt", "-500");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("number_test", number_test);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_datesearch_valid", dataProviderClass = DataProviderClass.class)
	public void test_entity_datesearch_valid(String datesearch) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("datesearch", datesearch );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_datesearch_invalid", dataProviderClass = DataProviderClass.class)
	public void test_entity_datesearch_invalid(String datesearch) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("datesearch", datesearch );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "0 notes due to invalid input data");
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with valid date range having greater than equal to and less than equal to format", dataProvider = "TestEntityDateRangeValid", dataProviderClass = DataProviderClass.class)
	public void TestEntityDateRangeValid(String gte, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("gte", gte);
			datesearch.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with invalid date range having greater than equal to and less than equal to format", dataProvider = "TestEntityDateRangeInvalid", dataProviderClass = DataProviderClass.class)
	public void TestEntityDateRangeInvalid(String gte, String lte) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("gte", gte);
			datesearch.put("lte", lte);	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with valid date having greater than format")
	public void TestEntityDateRangeValid1() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("gt", "2012-01-01T00:00:00");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with invalid date having greater than format")
	public void TestEntityDateRangeInvalid1() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("gt", "2022-01-01T00:00:00");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==0) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with valid date having less than format")
	public void TestEntityDateRangeValid2() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("lt", "2022-01-01T00:00:00");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue((count==2 || count==1) , "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with valid date having less than format")
	public void TestEntityDateRangeInvalid2() throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;
			
			HashMap<String, String> datesearch = new HashMap<String, String>();
			datesearch.put("lt", "2012-01-01T00:00:00");	
			
			HashMap<String, Object> sectionvalueList = new HashMap<String, Object>();
			sectionvalueList.put("datesearch", datesearch);
		
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);
			
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
			
			int count= respJson.getJSONObject("result").getInt("total");
			verify.assertTrue(count==0, "Note count match");
			
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_singlelinesearch_valid", dataProviderClass = DataProviderClass.class)
	public void test_entity_singlelinesearch_valid(String singleline_test) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("singleline_test", singleline_test );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_multilinesearch_valid", dataProviderClass = DataProviderClass.class)
	public void test_entity_multilinesearch_valid(String multiline_test) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("multiline_test", multiline_test );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Verifying notes with single entity field", dataProvider = "test_entity_dropdownsearch_valid", dataProviderClass = DataProviderClass.class)
	public void test_entity_dropdownsearch_valid(String dropdown_test) throws Exception {
		try {
			String URI = PUBLIC_API_URL + SEARCH;

			HashMap<Object, Object> sectionvalueList = new HashMap<Object, Object>();
			sectionvalueList.put("dropdown_test", dropdown_test );
			
			List<Object> sectionsList = new ArrayList<Object>();
			sectionsList.add(sectionvalueList);

			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "note");
			docTypeParams.put("sections", sectionsList);

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
	
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
}
