package com.sentieo.notebookpublicapis;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.JSONUtils;

public class FieldsPublicApis extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		RestAssured.baseURI = PUBLIC_API_URL;
	}

	@Test(description = "fetch All custom fields", priority = 1)
	public void fetchAllCustomFields() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(FIELDS, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyTrue(respJson.getJSONArray("entries") != null, "Checking json is not empty");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch All custom fields with params", priority = 2)
	public void fetchAllCustomFieldsWithParams() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);
			
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("offset", "1");
			filters.put("limit", "2");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(FIELDS, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson.getJSONArray("entries") !=null, "Checking json is not empty");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch All custom fields with ref param", priority = 3)
	public void fetchAllCustomFieldsWithRef() throws Exception {
		try {
			
			//creating field
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", "text-" + uuid.toString().substring(0, 4));
			formParams.put("type", "text");
			formParams.put("ref", "restapi");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fieldId = (String) respJson.get("id");
			HashMap<String, String> headerParamsForFetch = new HashMap<String, String>();
			headerParamsForFetch.put(XAPIKEY, X_API_KEY);
			headerParamsForFetch.put(XUSERKEY, X_USER_KEY);
			
			
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("ref", "restapi");

			RequestSpecification specForFetch = queryParamsSpecForPublicApis(filters, headerParamsForFetch);
			Response respForFetch = RestOperationUtils.get(FIELDS, specForFetch, null);
			APIResponse apiRespForFetch = new APIResponse(respForFetch);
			verify.verifyStatusCode(apiRespForFetch.getStatusCode(), 200);
			verify.verifyResponseTime(respForFetch, 5000);
			JSONObject respJsonForFetch = new JSONObject(apiRespForFetch.getResponseAsString());
			verify.verifyTrue(respJsonForFetch.getJSONArray("entries") !=null, "Checking json is not empty");
			JSONObject firstElement = (JSONObject) respJsonForFetch.getJSONArray("entries").get(0);
			verify.verifyEquals(firstElement.get("id"), fieldId, "Checking field id");
			
			
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch All custom fields with search param", priority = 4)
	public void fetchAllCustomFieldsWithExactSearch() throws Exception {
		try {
			
			//creating field
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String name = "text-" + uuid.toString().substring(0, 4);
			
			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", name);
			formParams.put("type", "text");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fieldId = (String) respJson.get("id");
			HashMap<String, String> headerParamsForFetch = new HashMap<String, String>();
			headerParamsForFetch.put(XAPIKEY, X_API_KEY);
			headerParamsForFetch.put(XUSERKEY, X_USER_KEY);
			
			
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("search", name);

			RequestSpecification specForFetch = queryParamsSpecForPublicApis(filters, headerParamsForFetch);
			Response respForFetch = RestOperationUtils.get(FIELDS, specForFetch, null);
			APIResponse apiRespForFetch = new APIResponse(respForFetch);
			verify.verifyStatusCode(apiRespForFetch.getStatusCode(), 200);
			verify.verifyResponseTime(respForFetch, 5000);
			JSONObject respJsonForFetch = new JSONObject(apiRespForFetch.getResponseAsString());
			verify.verifyTrue(respJsonForFetch.getJSONArray("entries") !=null, "Checking json is not empty");
			JSONObject firstElement = (JSONObject) respJsonForFetch.getJSONArray("entries").get(0);
			verify.verifyEquals(firstElement.get("name"), name, "Checking name is present or not");
			
			
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch All custom fields with search param", priority = 5)
	public void fetchAllCustomFieldsWithPartialSearch() throws Exception {
		try {
			
			//creating field
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String name = uuid.toString().substring(0, 4) + "-test";
			
			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", name);
			formParams.put("type", "text");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fieldId = (String) respJson.get("id");
			HashMap<String, String> headerParamsForFetch = new HashMap<String, String>();
			headerParamsForFetch.put(XAPIKEY, X_API_KEY);
			headerParamsForFetch.put(XUSERKEY, X_USER_KEY);
			
			
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("search", name.split("-")[0]);

			RequestSpecification specForFetch = queryParamsSpecForPublicApis(filters, headerParamsForFetch);
			Response respForFetch = RestOperationUtils.get(FIELDS, specForFetch, null);
			APIResponse apiRespForFetch = new APIResponse(respForFetch);
			verify.verifyStatusCode(apiRespForFetch.getStatusCode(), 200);
			verify.verifyResponseTime(respForFetch, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJsonForFetch = new JSONObject(apiRespForFetch.getResponseAsString());
			verify.verifyTrue(respJsonForFetch.getJSONArray("entries") !=null, "Checking json is not empty");
			JSONObject firstElement = (JSONObject) respJsonForFetch.getJSONArray("entries").get(0);
			verify.verifyEquals(firstElement.get("name"), name, "Checking name is present");
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			}}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "fetch a custom field", priority = 6)
	public void fetchACustomField() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(FIELDS, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			verify.verifyTrue(entries !=null, "Checking json is not empty");
			JSONObject field = (JSONObject) entries.get(0);
			String fieldId = (String) field.get("id");
			
			Response fieldResp = RestOperationUtils.get(FIELDS + fieldId , spec, null);
			APIResponse fieldApiResp = new APIResponse(fieldResp);
			verify.verifyStatusCode(fieldApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(fieldResp, 5000);
			JSONObject fieldJson = new JSONObject(fieldApiResp.getResponseAsString());
			verify.verifyTrue(fieldJson.get("id") == fieldId, "checking id of the field");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch a custom field with invalid id", priority = 7)
	public void fetchACustomFieldWIthInvalidID() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response fieldResp = RestOperationUtils.get(FIELDS + 123 , spec, null);
			APIResponse fieldApiResp = new APIResponse(fieldResp);
			verify.verifyStatusCode(fieldApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(fieldResp, 5000);
			JSONObject fieldJson = new JSONObject(fieldApiResp.getResponseAsString());
			verify.verifyEquals(fieldJson.getJSONObject("error").get("message"), "Field Resource 123 not found", "Checking message details");
			verify.verifyEquals(fieldJson.getJSONObject("error").get("code"), "not_found", "Checking code");
			
			
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "fetch a custom field of other account", priority = 8)
	public void fetchACustomFieldOfOtherAccount() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response fieldResp = RestOperationUtils.get(FIELDS + "5efe2b493ef93673484f46f6" , spec, null);
			APIResponse fieldApiResp = new APIResponse(fieldResp);
			verify.verifyStatusCode(fieldApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(fieldResp, 5000);
			JSONObject fieldJson = new JSONObject(fieldApiResp.getResponseAsString());
			verify.verifyEquals(fieldJson.getJSONObject("error").get("message"), "Not found.", "Checking message");
			verify.verifyEquals(fieldJson.getJSONObject("error").get("code"), "not_found", "Checking code");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "create a custom field", priority = 9)
	public void createATextField() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", "text-" + uuid.toString().substring(0, 4));
			formParams.put("type", "text");
			formParams.put("description", "this is a text field");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson.get("owner"), "Checking created field");
			verify.jsonSchemaValidation(resp, "fieldsAPI" + File.separator + "createATextField.json");
			String fieldId = (String) respJson.get("id");
			
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "update a custom field", priority = 10)
	public void updateAField() throws Exception {
		try {
			//creating a field
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", "text-" + uuid.toString().substring(0, 4));
			formParams.put("type", "text");
			formParams.put("description", "this is a text field");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson.get("owner"), "Checking created field");
			
			String fieldId = (String) respJson.get("id");
			
			//updating the field
			String updatedName = "updated-" + uuid.toString().substring(0, 4);
			HashMap<String, String> updateFormParams = new HashMap<String, String>();
			updateFormParams.put("name", updatedName);
			updateFormParams.put("description", "updated -this is a text field");
			String updateJson = jsonUtils.toJson(updateFormParams);
			
			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.patch(FIELDS + fieldId, null, updateSpec, updateFormParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyEquals(updateRespJson.get("description"), "updated -this is a text field", "Checking updated field description");
			verify.verifyEquals(updateRespJson.get("name"), updatedName, "Checking updated name");
			//verify.verifyEquals(updateRespJson.get("key"), updatedName); //bug 77590
			
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			
			//verify in fetch a field
			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			
			Response fieldResp = RestOperationUtils.get(FIELDS + fieldId , fetchSpec, null);
			APIResponse fieldApiResp = new APIResponse(fieldResp);
			verify.verifyStatusCode(fieldApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(fieldResp, 5000);
			JSONObject fieldJson = new JSONObject(fieldApiResp.getResponseAsString());
			verify.verifyTrue(fieldJson.getJSONObject("error")!= null, "checking error exists or not");
		} }catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "delete a custom field", priority = 11)
	public void deleteAField() throws Exception {
		try {
			//creating a field
			UUID uuid = UUID.randomUUID();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> formParams = new HashMap<String, String>();
			formParams.put("name", "text-" + uuid.toString().substring(0, 4));
			formParams.put("type", "text");
			formParams.put("description", "this is a text field");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(FIELDS, null, spec, formParams);
 			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 201) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson.get("owner"), "Checking created field");
			
			String fieldId = (String) respJson.get("id");
			
			//deletion of field
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(FIELDS + fieldId, null, deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			
			//verify in fetch a field
			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			
			Response fieldResp = RestOperationUtils.get(FIELDS + fieldId , fetchSpec, null);
			APIResponse fieldApiResp = new APIResponse(fieldResp);
			verify.verifyStatusCode(fieldApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(fieldResp, 5000);
			JSONObject fieldJson = new JSONObject(fieldApiResp.getResponseAsString());
			verify.verifyTrue(fieldJson.getJSONObject("error")!= null, "checking error exists or not");
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
