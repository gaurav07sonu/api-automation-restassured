package com.sentieo.securitymaster;

import static com.sentieo.constants.Constants.CODE;
import static com.sentieo.constants.Constants.ENTITY;
import static com.sentieo.constants.Constants.CHILD_ENTITIES;
import static com.sentieo.constants.Constants.CHILD_QUOTES;
import static com.sentieo.constants.Constants.SECURITIES;
import static com.sentieo.constants.Constants.SEARCH_QUOTE;
import static com.sentieo.constants.Constants.SECURITY_SEARCH;
import static com.sentieo.constants.Constants.SECURITY_MAP_SEARCH;
import static com.sentieo.constants.Constants.QUOTES;
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getString("short_name"), shortName);
			verify.verifyResponseTime(resp, 5000);
			verify.assertTrue(respJson.getString("id").contains("e!"), "Verify entity id format");
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
			// headerParams.put(XAPIKEY, X_API_KEY);
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

			verify.assertTrue(respJson != null, "Checking if response is null or not");
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
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

	@Test(description = "Verify Error when short name value is more than 20 char")
	public void testErrorForShortNameMoreThanTwentyChar() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "Sentieoname");
			formParams.put("short_name", "Sentieoname123name123112312");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(ENTITY, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			JSONObject error = (JSONObject) respJson.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");
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
			verify.assertTrue(respJson != null, "Checking if response is null or not");
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstEntry = (JSONObject) entries.get(0);
			String entityId = (String) firstEntry.get("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(ENTITY + "/" + entityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 404);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject errorObject = (JSONObject) respJson.get("error");

			verify.verifyEquals(errorObject.get("message"), MESSAGE);
			verify.verifyEquals(errorObject.get("code"), CODE);
			verify.verifyResponseTime(resp, 5000);
			verify.assertTrue(respJson != null, "Checking if response is null or not");

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
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String parentEntityId = (String) respJson.get("id");

			String shortnameForChildEntity = "C" + shortName;
			HashMap<String, Object> formParamForChildEntity = new HashMap<String, Object>();
			formParamForChildEntity.put("name", "API-ChildEntity" + String.valueOf(new Date().getTime()));
			formParamForChildEntity.put("short_name", shortnameForChildEntity);
			formParamForChildEntity.put("parent_entity_id", parentEntityId);
			String jsonForChildEntity = jsonUtils.toJson(formParamForChildEntity);

			RequestSpecification specForChildEntity = requestHeadersFormSpecForPublicApis(jsonForChildEntity,
					headerParams);
			Response respForChildEntity = RestOperationUtils.post(ENTITY, null, specForChildEntity, formParams);
			APIResponse apiRespForChildEntity = new APIResponse(respForChildEntity);
			verify.verifyStatusCode(apiRespForChildEntity.getStatusCode(), 201);
			JSONObject respJsonForChildEntity = new JSONObject(apiRespForChildEntity.getResponseAsString());
			String childEntityId = (String) respJsonForChildEntity.get("id");
			verify.verifyEquals(respJsonForChildEntity.getString("short_name"), shortnameForChildEntity);
			verify.verifyResponseTime(respForChildEntity, 5000);

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(ENTITY + "/" + childEntityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
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

	@Test(description = "fetch immediate Child Entities")
	public void fetchImmediateChildEntitiesOfAnEntity() throws Exception {
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String parentEntityId = (String) respJson.get("id");

			String shortnameForChildEntity = "C" + shortName;
			HashMap<String, Object> formParamForChildEntity = new HashMap<String, Object>();
			formParamForChildEntity.put("name", "API-ChildEntity" + String.valueOf(new Date().getTime()));
			formParamForChildEntity.put("short_name", shortnameForChildEntity);
			formParamForChildEntity.put("parent_entity_id", parentEntityId);
			String jsonForChildEntity = jsonUtils.toJson(formParamForChildEntity);

			RequestSpecification specForChildEntity = requestHeadersFormSpecForPublicApis(jsonForChildEntity,
					headerParams);
			Response respForChildEntity = RestOperationUtils.post(ENTITY, null, specForChildEntity, formParams);
			APIResponse apiRespForChildEntity = new APIResponse(respForChildEntity);
			verify.verifyStatusCode(apiRespForChildEntity.getStatusCode(), 201);
			JSONObject respJsonForChildEntity = new JSONObject(apiRespForChildEntity.getResponseAsString());
			verify.verifyEquals(respJsonForChildEntity.getString("short_name"), shortnameForChildEntity);
			verify.verifyResponseTime(respForChildEntity, 5000);

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(ENTITY + "/" + parentEntityId + CHILD_ENTITIES, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONArray entries = respJson1.getJSONArray("entries");
			JSONObject result = (JSONObject) entries.get(0);
			verify.verifyEquals(result.get("parent_entity_id"), parentEntityId);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master Entity")
	public void updateAnEntity() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

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

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(ENTITY + "/" + entityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyEquals(respJson1.getString("short_name"), shortName);
			verify.verifyResponseTime(resp1, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master Entity with Invalid Id")
	public void updateAnEntityWithInvalidId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

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

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(ENTITY + "/" + 123, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}


	@Test(description = "Update a security master Entity with blank name")
	public void updateAnEntityWithBlankName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(ENTITY + "/" + entityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master Entity with blank short name")
	public void updateAnEntityWithBlankShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(ENTITY + "/" + entityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "short_name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master Entity with more than 20chars short name")
	public void updateAnEntityWithTwentyCharShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String shortName = "ShortName" + uuid.toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(ENTITY + "/" + entityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Deletin a security master Entity")
	public void deletingAnEntity() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(ENTITY + "/" + entityId, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 204);
			verify.verifyResponseTime(resp1, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Deleting a security master Entity with invalid Id")
	public void deletingAnEntityWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(ENTITY + "/" + 123, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security")
	public void testCreationOfASecurity() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 201);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			String id = (String) respJson1.get("id");
			verify.assertTrue(id.contains("s!"), "Verify security id format");
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with 20chas shortname ")
	public void testCreationOfASecurityWithTwentyCharsInShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String shortName = "ShortName" + uuid;
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with out short name")
	public void testCreationOfASecurityWithoutShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			// formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"short_name\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with out security_type")
	public void testCreationOfASecurityWithoutSecurityType() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			// formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"security_type\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with out Name")
	public void testCreationOfASecurityWithoutName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			// formParams.put("name", "API-Security" + String.valueOf(new
			// Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"name\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with out issuer Id")
	public void testCreationOfASecurityWithoutIssuerId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			// formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"issuer_id\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with invalid issuerId")
	public void testCreationOfASecurityInvalidIssuerId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", "12345");
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = respJson1.getJSONObject("error");
			verify.verifyEquals(error.get("message"), "Error Issuer Id access denied");
			verify.verifyEquals(error.get("code"), "Forbidden");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with used shortname")
	public void testCreationOfASecurityWithAlreadyUsedShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", "usedshortname");
			formParams.put("security_type", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			
			
			RequestSpecification spec2 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp2 = RestOperationUtils.post(SECURITIES, null, spec2, formParams);
			APIResponse apiResp2 = new APIResponse(resp2);
			verify.verifyStatusCode(apiResp2.getStatusCode(), 400);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());
			System.out.println(respJson2);
			verify.verifyResponseTime(resp2, 5000);
			JSONObject error = (JSONObject) respJson2.get("error");
			verify.verifyEquals(error.get("message"), "Unique Constraint violates on field short_name");
			verify.verifyEquals(error.get("code"), "Conflict");
			verify.verifyEquals(error.getJSONObject("detail").get("short_name"),
					"Security with given short_name already exists");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a security with all the params")
	public void testCreationOfASecurityWithAllParams() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(ENTITY, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String entityId = firstResult.getString("id");

			List<String> othernames = new ArrayList<String>();
			othernames.add("abc");
			othernames.add("xyz");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", entityId);
			formParams.put("name", "API-Security" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("security_type", "Equity");
			formParams.put("is_primary_flag", false);
			formParams.put("isin", "US30303M1027");
			formParams.put("cusip_cins", "123");
			formParams.put("primary_ric", "235235");
			formParams.put("rkd_code", 123);
			formParams.put("rkd_issue_code", 124);
			formParams.put("tr_sec_master_sec_code", 1233);
			formParams.put("ciq_security_id", 214);
			formParams.put("fact_set_security_id", "00991");
			formParams.put("datastream_ds_scty_code", "990011");
			formParams.put("datastream_ds_sec_code", 88773);
			formParams.put("tr_instrument_perm_id", 3422);
			formParams.put("ibes_code", 124532);
			formParams.put("ibes_s_ticker", "Equ");
			formParams.put("ibes_i_ticker", "Equi");
			formParams.put("primary_bbg_symbol", "polo");
			formParams.put("primary_fact_set_symbol", "goal");
			formParams.put("primary_ciq_symbol", "yell");
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(SECURITIES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 201);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			String id = (String) respJson1.get("id");
			verify.assertTrue(id.contains("s!"), "Verify security id format");
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All securities")
	public void testFetchAllSecurities() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");

			verify.assertTrue(respJson != null, "Checking if response is null or not");
			verify.assertTrue(entries.length() != 0, "Verify length");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All securities without XapiKey")
	public void testFetchAllSecuritiesWithoutXApiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
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

	@Test(description = "fetch All securities with invalid XapiKey")
	public void testFetchAllSecuritiesWithInvalidXApiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, "1234");
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");
			verify.verifyEquals(response.get("status"), true);
			verify.verifyEquals(msg.get(0), "Authorizer Failed");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific security")
	public void fetchASpecificSecurity() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.assertTrue(respJson != null, "Checking if response is null or not");
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstEntry = (JSONObject) entries.get(0);
			String securityId = (String) firstEntry.get("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(SECURITIES + "/" + securityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
			verify.verifyEquals(respJson1.get("id"), securityId);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific security with invalid Id")
	public void fetchASpecificSecurityWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String securityId = "123";

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(SECURITIES + "/" + securityId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific security without xapikey")
	public void fetchASpecificSecurityWithoutXapikey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			// headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String securityId = "123";

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES + "/" + securityId, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");
			verify.verifyEquals(response.get("status"), true);
			verify.verifyEquals(msg.get(0), "Unauthorized");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master security")
	public void updateASecurity() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			List<String> othernames = new ArrayList<String>();
			othernames.add("abc");
			othernames.add("xyz");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);
			formParams.put("security_type", "etf");
			formParams.put("is_primary_flag", false);
			formParams.put("isin", "US30303M1027");
			formParams.put("cusip_cins", "123");
			formParams.put("primary_ric", "235235");
			formParams.put("rkd_code", 123);
			formParams.put("rkd_issue_code", 124);
			formParams.put("tr_sec_master_sec_code", 1233);
			formParams.put("ciq_security_id", 214);
			formParams.put("fact_set_security_id", "00991");
			formParams.put("datastream_ds_scty_code", "990011");
			formParams.put("datastream_ds_sec_code", 88773);
			formParams.put("tr_instrument_perm_id", 3422);
			formParams.put("ibes_code", 124532);
			formParams.put("ibes_s_ticker", "Equ");
			formParams.put("ibes_i_ticker", "Equi");
			formParams.put("primary_bbg_symbol", "polo");
			formParams.put("primary_fact_set_symbol", "goal");
			formParams.put("primary_ciq_symbol", "yell");
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			verify.verifyResponseTime(resp1, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security with 20char short name")
	public void updateASecurityWithtwentyShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String shortName = "ShortName" + uuid.toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security with blank short name")
	public void updateASecurityWithBlankShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "short_name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "This field may not be blank.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security with used short name")
	public void updateASecurityWithUsedShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "usedShortName");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "Unique Constraint violates on field short_name");
			verify.verifyEquals(error.get("code"), "Conflict");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master security with invalid ID")
	public void updateASecurityWithInvalidId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String securityId = "s123";

			List<String> othernames = new ArrayList<String>();
			othernames.add("abc");
			othernames.add("xyz");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);
			formParams.put("security_type", "etf");
			formParams.put("is_primary_flag", false);
			formParams.put("isin", "US30303M1027");
			formParams.put("cusip_cins", "123");
			formParams.put("primary_ric", "235235");
			formParams.put("rkd_code", 123);
			formParams.put("rkd_issue_code", 124);
			formParams.put("tr_sec_master_sec_code", 1233);
			formParams.put("ciq_security_id", 214);
			formParams.put("fact_set_security_id", "00991");
			formParams.put("datastream_ds_scty_code", "990011");
			formParams.put("datastream_ds_sec_code", 88773);
			formParams.put("tr_instrument_perm_id", 3422);
			formParams.put("ibes_code", 124532);
			formParams.put("ibes_s_ticker", "Equ");
			formParams.put("ibes_i_ticker", "Equi");
			formParams.put("primary_bbg_symbol", "polo");
			formParams.put("primary_fact_set_symbol", "goal");
			formParams.put("primary_ciq_symbol", "yell");
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master security with invalid issuer ID")
	public void updateASecurityWithInvalidIssuerId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("issuer_id", "12123");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "Error Issuer Id access denied");
			verify.verifyEquals(error.get("code"), "Forbidden");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security with blank name")
	public void updateASecurityWithBlankName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security with blank security type")
	public void updateASecurityWithBlankSecurityType() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_type", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(SECURITIES + "/" + securityId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "security_type : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}


	@Test(description = "Deleting a security master secuirty")
	public void deletingAnSecurity() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(SECURITIES + "/" + securityId, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 204);
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Deleting a security master security with invalid Id")
	public void deletingASecurityWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(SECURITIES + "/" + 123, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	

	@Test(description = "Create a quote")
	public void testCreationOfAQuote() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "QShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("short_name", shortName);
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));
			formParams.put("exchange_code", "Equity");
			

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 201);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			String id = (String) respJson1.get("id");
			verify.assertTrue(id.contains("q!"), "Verify quote id format");
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with 20chars shortname")
	public void testCreationOfAQuoteWithTwentyCharsInShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String shortName = "QShortName" + uuid;
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with out short name")
	public void testCreationOfAQuoteWithoutShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			//formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"short_name\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with out exchange code")
	public void testCreationOfAQuoteWithoutExchangeCode() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("short_name", shortName);
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));
			//formParams.put("exchange_code", "Equity");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"exchange_code\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}


	@Test(description = "Create a quote with out security Id")
	public void testCreationOfAQuoteWithoutSecurityId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			//formParams.put("security_id", securityId);
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONArray jArray = respJson1.getJSONObject("response").getJSONArray("msg");
			verify.verifyEquals(jArray.get(0), "Bad Request Body");
			verify.verifyEquals(jArray.get(1), "[object has missing required properties ([\"security_id\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Create a quote with out name")
	public void testCreationOfAQuoteWithoutName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			//formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "name : This field is required. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("name");
			verify.verifyEquals(jArray.get(0), "This field is required.");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with invalid security id")
	public void testCreationOfAQuoteInvalidSecurityId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", "123");
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = respJson1.getJSONObject("error");
			verify.verifyEquals(error.get("message"), "Error Security Id access denied");
			verify.verifyEquals(error.get("code"), "Forbidden");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with used shortname")
	public void testCreationOfAQuoteWithAlreadyUsedShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("short_name", "usedShortName");
			formParams.put("exchange_code", "NASDAQ");
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			
			
			RequestSpecification spec2 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp2 = RestOperationUtils.post(QUOTES, null, spec2, formParams);
			APIResponse apiResp2 = new APIResponse(resp2);
			verify.verifyStatusCode(apiResp2.getStatusCode(), 400);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());
			System.out.println(respJson2);
			verify.verifyResponseTime(resp2, 5000);
			JSONObject error = (JSONObject) respJson2.get("error");
			verify.verifyEquals(error.get("message"), "Unique Constraint violates on field short_name");
			verify.verifyEquals(error.get("code"), "Conflict");
			verify.verifyEquals(error.getJSONObject("detail").get("short_name"),
					"Quote with given short_name already exists");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a quote with all the params")
	public void testCreationOfAQuoteWithAllParams() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String securityId = firstResult.getString("id");

			List<String> othernames = new ArrayList<String>();
			othernames.add("a");
			othernames.add("m");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", securityId);
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("exchange_mic_code", "US30303M1027");
			formParams.put("exchange_region", "ind");
			formParams.put("is_primary_flag", false);
			formParams.put("exch_symbol", "");
			formParams.put("sedol", "");
			formParams.put("ric", "");
			formParams.put("ciq_symbol", "");
			formParams.put("fact_set_symbol", "");
			formParams.put("bbg_symbol", "");
			formParams.put("datastream_infocode", "");
			formParams.put("datastream_infocode_exchange", "");
			formParams.put("tr_quote_perm_id", 213);
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.post(QUOTES, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 201);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			String id = (String) respJson1.get("id");
			verify.assertTrue(id.contains("q!"), "Verify quote id format");
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All quotes")
	public void testFetchAllQuotes() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");

			verify.assertTrue(respJson != null, "Checking if response is null or not");
			verify.assertTrue(entries.length() != 0, "Verify length");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch All quotes without XapiKey")
	public void testFetchAllQuotesWithoutXApiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
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

	@Test(description = "fetch All quotes with invalid XapiKey")
	public void testFetchAllQuotesWithInvalidXApiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, "1234");
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");
			verify.verifyEquals(response.get("status"), true);
			verify.verifyEquals(msg.get(0), "Authorizer Failed");
			verify.verifyResponseTime(resp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific quote")
	public void fetchASpecificQuote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.assertTrue(respJson != null, "Checking if response is null or not");
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstEntry = (JSONObject) entries.get(0);
			String quoteId = (String) firstEntry.get("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(QUOTES + "/" + quoteId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
			verify.verifyEquals(respJson1.get("id"), quoteId);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific quote with invalid Id")
	public void fetchASpecificQuoteWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String quoteId = "123";

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(QUOTES + "/" + quoteId, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a specific quote without xapikey")
	public void fetchASpecificQuoteWithoutXapikey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			// headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String quoteId = "123";

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES + "/" + quoteId, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");
			verify.verifyEquals(response.get("status"), true);
			verify.verifyEquals(msg.get(0), "Unauthorized");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a security master Quote")
	public void updateAQuote() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "UShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String quoteId = firstResult.getString("id");
			
			
			RequestSpecification specs = requestHeadersSpecForPublicApis(headerParams);
			Response resps = RestOperationUtils.get(SECURITIES, specs, null);
			APIResponse apiResps = new APIResponse(resps);
			verify.verifyStatusCode(apiResps.getStatusCode(), 200);
			JSONObject respJsons = new JSONObject(apiResps.getResponseAsString());
			JSONArray entriess = respJsons.getJSONArray("entries");
			JSONObject firstResults = (JSONObject) entriess.get(0);
			String secId = firstResults.getString("id");

			List<String> othernames = new ArrayList<String>();
			othernames.add("a");
			othernames.add("m");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);
			formParams.put("name", "API-quote" + String.valueOf(new Date().getTime()));
			formParams.put("security_id", secId);
			formParams.put("exchange_code", "Equity");
			formParams.put("exchange_mic_code", "US30303M1027");
			formParams.put("exchange_region", "ind");
			formParams.put("is_primary_flag", false);
			formParams.put("exchange_symbol", "updatedSymbol");
			formParams.put("sedol", "upd");
			formParams.put("ric", "12CCBB");
			formParams.put("ciq_symbol", "");
			formParams.put("fact_set_symbol", "");
			formParams.put("bbg_symbol", "");
			formParams.put("datastream_infocode", "");
			formParams.put("datastream_infocode_exchange", "");
			formParams.put("tr_quote_perm_id", 1111);
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + quoteId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyEquals(respJson1.getString("short_name"), shortName.toLowerCase());
			verify.verifyEquals(respJson1.getString("security_id"), secId);
			verify.verifyResponseTime(resp1, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a quote with 20char short name")
	public void updateAQuoteWithtwentyShortName() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String shortName = "ShortName" + uuid.toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String quoteId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + quoteId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"),
					"short_name : Ensure this field has no more than 20 characters. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "Ensure this field has no more than 20 characters.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a quote with blank short name")
	public void updateAQuoteWithBlankShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String quoteId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + quoteId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "short_name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("short_name");
			verify.verifyEquals(jArray.get(0), "This field may not be blank.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Update a quote with blank name")
	public void updateAQuoteWithBlankName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String quoteId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("name", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + quoteId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "name : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
			JSONArray jArray = error.getJSONObject("detail").getJSONArray("name");
			verify.verifyEquals(jArray.get(0), "This field may not be blank.");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a Quote with used short name")
	public void updateAQuoteWithUsedShortName() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String qId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", "usedShortName");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + qId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "Unique Constraint violates on field short_name");
			verify.verifyEquals(error.get("code"), "Conflict");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a quote with invalid ID")
	public void updateAQuoteWithInvalidId() throws Exception {
		try {
			UUID uuid = UUID.randomUUID();
			String[] split = uuid.toString().split("-", 10);
			String shortName = "ShortName" + split[0].toString();
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String qID = "s123";

			List<String> othernames = new ArrayList<String>();
			othernames.add("a");
			othernames.add("m");
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("short_name", shortName);
			formParams.put("exchange_code", "Equity");
			formParams.put("exchange_mic_code", "US30303M1027");
			formParams.put("exchange_region", "ind");
			formParams.put("is_primary_flag", false);
			formParams.put("exchange_symbol", "updatedSymbol");
			formParams.put("sedol", "upd");
			formParams.put("ric", "12CCBB");
			formParams.put("ciq_symbol", "");
			formParams.put("fact_set_symbol", "");
			formParams.put("bbg_symbol", "");
			formParams.put("datastream_infocode", "");
			formParams.put("datastream_infocode_exchange", "");
			formParams.put("tr_quote_perm_id", 23);
			formParams.put("other_names", othernames);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + qID, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a quote with invalid security ID")
	public void updateAQuoteWithInvalidSecurityId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String qId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", "12123");
			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + qId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "Error Security Id access denied");
			verify.verifyEquals(error.get("code"), "Forbidden");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}


	@Test(description = "Update a quote with blank exchange code")
	public void updateAQuoteWithBlankExhangeCode() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String qId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("exchange_code", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + qId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			System.out.println(respJson1);
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Update a quote with blank security Id")
	public void updateAQuoteWithBlankSecurityId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String qId = firstResult.getString("id");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("security_id", "");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec1 = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp1 = RestOperationUtils.patch(QUOTES + "/" + qId, null, spec1, formParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyEquals(error.get("message"), "security_id : This field may not be blank. ");
			verify.verifyEquals(error.get("code"), "Invalid Request");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Deleting a quote")
	public void deletingAQuote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(QUOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstResult = (JSONObject) entries.get(0);
			String qId = firstResult.getString("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(QUOTES + "/" + qId, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 204);
			verify.verifyResponseTime(resp1, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Deleting a quote with invalid Id")
	public void deletingAQuoteWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.delete(QUOTES + "/" + 123, null, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 404);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONObject error = (JSONObject) respJson1.get("error");
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(error.get("message"), "No object matches the given query");
			verify.verifyEquals(error.get("code"), "Not Found");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Fetch Child quotes")
	public void fetchChildQuotes() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(SECURITIES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.assertTrue(respJson != null, "Checking if response is null or not");
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstEntry = (JSONObject) entries.get(0);
			String securityId = (String) firstEntry.get("id");

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(SECURITIES + "/" + securityId + CHILD_QUOTES, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Fetch Child quotes with invalid Id")
	public void fetchChildQuotesWithInvalidID() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String securityId = "akjfh";

			RequestSpecification spec1 = requestHeadersSpecForPublicApis(headerParams);
			Response resp1 = RestOperationUtils.get(SECURITIES + "/" + securityId + CHILD_QUOTES, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			JSONArray entries =  respJson1.getJSONArray("entries");
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1!= null, "Checking if response is null");
			verify.verifyEquals(entries.length(), 0);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Search Quote")
	public void SearchQuote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("autocomplete", "aapl");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SEARCH_QUOTE, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Sentieo Security Search")
	public void SentieoSecuritySearch() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ciq_tickers", "NasdaqGS:AAPL");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Sentieo Security Search without params")
	public void SentieoSecuritySearchWithOutParams() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			//params.put("ciq_tickers", "NasdaqGS:AAPL");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(respJson1.get("message"), "At least one parameter(isin, ric, company_name, sentieo_ticker, ciq_tickers, old_ciq_tickers, old_market_symbols, figi, bloomberg_ticker) is needed");
			verify.verifyEquals(respJson1.get("code"), "Missing Parameters");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Sentieo Security Map Search with ciq_tickers")
	public void SentieoSecurityMapSearchWithCiqTickers() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ciq_tickers", "NasdaqGS:AAPL");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_MAP_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Sentieo Security Map Search with figi")
	public void SentieoSecurityMapSearchWithFigi() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("figi", "BBG000BPH459,BBG000B9XRY4");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_MAP_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Sentieo Security Map Search with bloombergTicker")
	public void SentieoSecurityMapSearchWithBloombergTicker() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("bloomberg_ticker", "ADP FP,VOW GR");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_MAP_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			verify.assertTrue(respJson1 != null, "Checking if response is null or not");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Sentieo Security Map Search with two params")
	public void SentieoSecurityMapSearchWithTwoParams() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("bloomberg_ticker", "ADP FP,VOW GR");
			params.put("figi", "BBG000BPH459,BBG000B9XRY4");
			
			RequestSpecification spec1 = queryParamsSpecForPublicApis(params,headerParams);
			Response resp1 = RestOperationUtils.get(SECURITY_MAP_SEARCH, spec1, null);
			APIResponse apiResp1 = new APIResponse(resp1);
			verify.verifyStatusCode(apiResp1.getStatusCode(), 400);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyResponseTime(resp1, 5000);
			JSONObject status = respJson1.getJSONObject("status");
			JSONArray jsonArray = status.getJSONArray("msg");
			verify.verifyEquals(jsonArray.get(0), "Invalid Parameters");
			verify.verifyEquals(jsonArray.get(1), "only one of these parameters is allowed - ciq_tickers, figi, bloomberg_ticker");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
}
