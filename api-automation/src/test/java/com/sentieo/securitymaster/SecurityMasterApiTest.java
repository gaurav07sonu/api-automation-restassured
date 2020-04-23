package com.sentieo.securitymaster;

import static com.sentieo.constants.Constants.CODE;
import static com.sentieo.constants.Constants.ENTITY;
import static com.sentieo.constants.Constants.MESSAGE;
import static com.sentieo.constants.Constants.PUBLIC_API_URL;
import static com.sentieo.constants.Constants.XAPIKEY;
import static com.sentieo.constants.Constants.XUSERKEY;
import static com.sentieo.constants.Constants.X_API_KEY;
import static com.sentieo.constants.Constants.X_USER_KEY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

/**
 * 
 * @author Akash
 *
 */
public class SecurityMasterApiTest extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;
	String URI = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		RestAssured.baseURI = PUBLIC_API_URL;
	}

	@Test(description = "Creating an Entity", enabled = true)
	public void testCreationOfAnEntity() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "API-Entity" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getString("short_name"), shortName);
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Creating an Entity", enabled = true)
	public void testEntityWithoutAPIkey() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			//headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "API-Entity" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");
			verify.verifyEquals(response.get("status"), true);
			verify.verifyEquals(msg.get(0), "Unauthorized");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All Entities", enabled = true)
	public void testFetchAllEntities() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyTrue(respJson != null, "Checking if response is null or not");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create Entity with all the non mandatory params", enabled = true)
	public void testCreationOfEntitiesWithAllParams() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			List<String> otherNames = new ArrayList<String>();
			otherNames.add("abc");
			otherNames.add("xyz");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "API-Entity" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("sentieo_entity_id", 399);
			formParams.put("datastream_ds_cmpy_code", 32);
			formParams.put("datastream_ds_comp_code", "");
			formParams.put("cap_iq_company_id", 1);
			formParams.put("rkd_org_org_id", "");
			formParams.put("tr_issuer_code", 213);
			formParams.put("tr_company_perm_id", 221);
			formParams.put("crunchbase_uuid", "qw");
			formParams.put("crunchbase_permalink", "qw2");
			formParams.put("fds_entity_id", "qwr2");
			formParams.put("cik_code", 21);
			formParams.put("lei_code", "qwr32");
			formParams.put("client_entity_id", "23r3");
			formParams.put("other_names", otherNames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getString("short_name"), shortName);
			verify.verifyResponseTime(resp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verify Error when short name is blank", enabled = true)
	public void testErrorForBlankShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "API-Entity" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", "");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			JSONArray jArray = respJson.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[string \"\" is too short (length: 0, required minimum: 1)]");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verify Error when name is blank", enabled = true)
	public void testErrorForBlankName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "");
			formParams.put("short_name", "Shortname");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			JSONArray jArray = respJson.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[string \"\" is too short (length: 0, required minimum: 1)]");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verify Error when name param is not sent", enabled = true)
	public void testErrorForNameNotSent() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "Shortname");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			JSONArray jArray = respJson.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"name\"])]");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Verify Error when short name param is not sent", enabled = true)
	public void testErrorForShortNameNotSent() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "name123");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			JSONArray jArray = respJson.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"short_name\"])]");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific entity")
	public void fetchASpecificEntity() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyTrue(respJson != null, "Checking if response is null or not");
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstEntry = (JSONObject) entries.get(0);
			String entityId = (String) firstEntry.get("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(ENTITY + "/" + entityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyTrue(respJson1 != null, "Checking if response is null or not");
			verify.verifyEquals(respJson1.get("id"), entityId);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific entity with invalid id")
	public void fetchASpecificEntityWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String invalidId = "123abc";

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY + "/" + invalidId, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject errorObject = (JSONObject) respJson.get("error");

			verify.verifyEquals(errorObject.get("message"), MESSAGE);
			verify.verifyEquals(errorObject.get("code"), CODE);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyTrue(respJson != null, "Checking if response is null or not");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create Child Entity and verify")
	public void testForChildEntiy() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "API-Entity" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			String json = jsonUtils.toJson(formParams);
			
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String parentEntityId = (String) respJson.get("id");
			
			String shortnameForChildEntity = "C" + shortName;
			HashMap<String, Object> formParamForChildEntity = new HashMap<String, Object>();
			formParamForChildEntity.put("name", "API-ChildEntity" + String.valueOf(new Date().getTime()));
			formParamForChildEntity.put("short_name", shortnameForChildEntity);
			formParamForChildEntity.put("parent_entity_id", parentEntityId);
			String jsonForChildEntity = jsonUtils.toJson(formParamForChildEntity);
			
			RequestSpecification specForChildEntity = requestHeadersFormSpecForPublicApis(jsonForChildEntity, headerParams);
			Response respForChildEntity = RestOperationUtils.post(ENTITY, null, specForChildEntity, formParams);
			APIResponse apiRespForChildEntity = new APIResponse(respForChildEntity);
			verify.verifyStatusCode(apiRespForChildEntity.getStatusCode(), 200);
			JSONObject respJsonForChildEntity = new JSONObject(apiRespForChildEntity.getResponseAsString());
			String childEntityId = (String) respJsonForChildEntity.get("id");
			verify.verifyEquals(respJsonForChildEntity.getString("short_name"), shortnameForChildEntity);
			verify.verifyResponseTime(respForChildEntity, 5000);
			
			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(ENTITY + "/" + childEntityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiRespForChildEntity.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyEquals(respJson1.getString("parent_entity_id"), parentEntityId);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

}
