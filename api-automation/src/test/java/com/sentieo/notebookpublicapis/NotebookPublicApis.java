package com.sentieo.notebookpublicapis;

import static com.sentieo.constants.Constants.FETCH_NOTE_HTML;
import static com.sentieo.constants.Constants.FILE_UPLOAD;
import static com.sentieo.constants.Constants.LARGE_FILE_UPLOAD;
import static com.sentieo.constants.Constants.NOTES;
import static com.sentieo.constants.Constants.PUBLIC_API_URL;
import static com.sentieo.constants.Constants.USER_APP_URL;
import static com.sentieo.constants.Constants.XAPIKEY;
import static com.sentieo.constants.Constants.XUSERKEY;
import static com.sentieo.constants.Constants.X_API_KEY;
import static com.sentieo.constants.Constants.X_USER_KEY;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
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
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;
import com.sentieo.utils.FileUtil;
import com.sentieo.utils.JSONUtils;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 
 * @author Akash
 *
 */

public class NotebookPublicApis extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		RestAssured.baseURI = PUBLIC_API_URL;
	}

	/*
	 * 
	 * This upload has /files as a URL
	 */
	@Test(description = "Upload File")
	public void uploadAFile() throws Exception {
		try {

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "pptfile.pptx";
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "uploadFile.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Upload File without xapikey")
	public void uploadAFileWithoutXApikey() throws Exception {
		try {

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "pptfile.pptx";
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, "");
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Unauthorized");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch all notes in notebook")
	public void fetchAllNotes() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.assertTrue(respJson.getJSONArray("entries").length() != 0, "Verify the API Response");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch all notes without xapikey")
	public void fetchAllNotesWithoutXapiKey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Unauthorized");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch all notes with filters")
	public void fetchAllNotesWithFilters() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("offset", "1");
			filters.put("limit", "2");
			filters.put("order", "created_at:asc");
			filters.put("filter", "note_type eq typed");
			filters.put("term", "private");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONArray("entries").length(), 2, "Verify the API Response length");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note")
	public void createANote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note without refId")
	public void createANoteWithoutXapikey() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 401);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Unauthorized");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note without refId")
	public void createANoteWithOutRefID() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with already used refId")
	public void createANoteWithAlreadyUsedRefId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", "usedRef");
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 409);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("error");
			String message = (String) response.get("message");
			String code = (String) response.get("code");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyTrue(message.startsWith("Note"), "Start message");
			verify.verifyTrue(message.endsWith("given ref usedRef"), "End message");
			verify.verifyEquals(code, "CONFLICT ALREADY EXIST");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note without Type")
	public void createANoteWithoutType() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", "usedRef");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Bad Request Body");
			verify.verifyEquals(msg.get(1).toString(), "[object has missing required properties ([\"type\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with type as blank")
	public void createANoteWithTypeAsBlank() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", "usedRef");
			formParams.put("type", "");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Bad Request Body");
			verify.verifyEquals(msg.get(1).toString(),
					"[instance value (\"\") not found in enum (possible values: [\"typed\",\"attachment\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with type has incorrect value")
	public void createANoteWithTypeIncorrectValue() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", "usedRef");
			formParams.put("type", "abc");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Bad Request Body");
			verify.verifyEquals(msg.get(1).toString(),
					"[instance value (\"abc\") not found in enum (possible values: [\"typed\",\"attachment\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with type in Capital case")
	public void createANoteWithTypeInCapitalCase() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", "usedRef");
			formParams.put("type", "TYPED");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 400);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJson.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Bad Request Body");
			verify.verifyEquals(msg.get(1).toString(),
					"[instance value (\"TYPED\") not found in enum (possible values: [\"typed\",\"attachment\"])]");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note without title")
	public void createANoteWithoutTitle() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.verifyEquals(respJson.get("title"), "", "Verify title of the note");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with non ascii char in title")
	public void createANoteWithNonAsciiCharacterinTitle() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "ÄÂ¿Ð");
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.verifyEquals(respJson.get("title"), "ÄÂ¿Ð", "Verify title of the note");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note without content Param")
	public void createANoteWithoutContentParam() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create a note with created_at as old date")
	public void createANoteWithcreatedAtAsOldDate() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "Hello");
			formParams.put("category", "meeting");
			formParams.put("created_at", "2010-04-14T07:55:41");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.verifyEquals(respJson.get("created_at"), "2010-04-14T07:55:41", "Verify created_at");
			verify.verifyEquals(respJson.get("updated_at"), "2010-04-14T07:55:41", "Verify created_at");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
			
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Create an attachment note")
	public void createAnAttachmentNote() throws Exception {
		try {

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "wordFile.docx";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fileId = respJson.get("id").toString();
			String contentType = respJson.get("content_type").toString();

			// Creating an attachment note
			HashMap<String, String> headerParamsForNote = new HashMap<String, String>();
			headerParamsForNote.put(XAPIKEY, X_API_KEY);
			headerParamsForNote.put(XUSERKEY, X_USER_KEY);

			List<String> tickers = new ArrayList<String>();
			tickers.add("appl");
			tickers.add("fb");

			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("type", "attachment");
			formParams.put("category", "meeting");
			formParams.put("file", listOfFileParams);
			formParams.put("tickers", tickers);

			String jsonForRequest = jsonUtils.toJson(formParams);

			RequestSpecification specForNote = requestHeadersFormSpecForPublicApis(jsonForRequest, headerParamsForNote);
			Response respForNote = RestOperationUtils.post(NOTES, null, specForNote, formParams);
			APIResponse apiRespForNote = new APIResponse(respForNote);
			verify.verifyStatusCode(apiRespForNote.getStatusCode(), 201);
			JSONObject respJsonForNote = new JSONObject(apiRespForNote.getResponseAsString());

			verify.verifyResponseTime(respForNote, 5000);
			verify.verifyEquals(respJsonForNote.get("type"), "attachment", "Verify the API note type");
			verify.jsonSchemaValidation(respForNote, "notebookPublicApi" + File.separator + "createAnAttachment.json");
			
			
			String noteId = (String) respJsonForNote.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Test attachment note creation with invalid file id")
	public void createAnAttachmentNoteWithInvalidFileId() throws Exception {
		try {

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "wordFile.docx";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fileId = respJson.get("id").toString();
			String contentType = respJson.get("content_type").toString();

			// Creating an attachment note
			HashMap<String, String> headerParamsForNote = new HashMap<String, String>();
			headerParamsForNote.put(XAPIKEY, X_API_KEY);
			headerParamsForNote.put(XUSERKEY, X_USER_KEY);

			List<String> tickers = new ArrayList<String>();
			tickers.add("appl");
			tickers.add("fb");

			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", "123");
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("type", "attachment");
			formParams.put("category", "meeting");
			formParams.put("file", listOfFileParams);
			formParams.put("tickers", tickers);

			String jsonForRequest = jsonUtils.toJson(formParams);

			RequestSpecification specForNote = requestHeadersFormSpecForPublicApis(jsonForRequest, headerParamsForNote);
			Response respForNote = RestOperationUtils.post(NOTES, null, specForNote, formParams);
			APIResponse apiRespForNote = new APIResponse(respForNote);
			verify.verifyStatusCode(apiRespForNote.getStatusCode(), 400);
			JSONObject respJsonForNote = new JSONObject(apiRespForNote.getResponseAsString());
			JSONObject error = (JSONObject) respJsonForNote.get("error");

			verify.verifyResponseTime(respForNote, 5000);
			verify.verifyEquals(error.get("code"), "INVALID REQUEST");
			verify.verifyEquals(error.get("message"), "Invalid file_id");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Test attachment note creation with different content type")
	public void createAnAttachmentNoteWithDifferentContentType() throws Exception {
		try {

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "wordFile.docx";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fileId = respJson.get("id").toString();

			// Creating an attachment note
			HashMap<String, String> headerParamsForNote = new HashMap<String, String>();
			headerParamsForNote.put(XAPIKEY, X_API_KEY);
			headerParamsForNote.put(XUSERKEY, X_USER_KEY);

			List<String> tickers = new ArrayList<String>();
			tickers.add("appl");
			tickers.add("fb");

			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", "application/pdf");
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("type", "attachment");
			formParams.put("category", "meeting");
			formParams.put("file", listOfFileParams);
			formParams.put("tickers", tickers);

			String jsonForRequest = jsonUtils.toJson(formParams);

			RequestSpecification specForNote = requestHeadersFormSpecForPublicApis(jsonForRequest, headerParamsForNote);
			Response respForNote = RestOperationUtils.post(NOTES, null, specForNote, formParams);
			APIResponse apiRespForNote = new APIResponse(respForNote);
			verify.verifyStatusCode(apiRespForNote.getStatusCode(), 400);
			JSONObject respJsonForNote = new JSONObject(apiRespForNote.getResponseAsString());
			JSONObject error = (JSONObject) respJsonForNote.get("error");

			verify.verifyResponseTime(respForNote, 5000);
			verify.verifyEquals(error.get("code"), "INVALID REQUEST");
			verify.verifyEquals(error.get("message"), "Content-type mismatch with uploaded file");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a note")
	public void fetchSingleNote() throws Exception {
		try {
			// note creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			
			String noteId = (String) respJson.get("id");

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + noteId, fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			
			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(fetchResp, 5000);
			verify.jsonSchemaValidation(fetchResp, "notebookPublicApi" + File.separator + "fetchSingleNote.json");
			
			
			
			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a note without xuserkey")
	public void fetchSingleNoteWithoutXapiKey() throws Exception {
		try {
			// note creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + "", fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 401);
			JSONObject respJsonForNote = new JSONObject(fetchApiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJsonForNote.get("response");
			JSONArray msg = response.getJSONArray("msg");

			verify.verifyResponseTime(fetchResp, 5000);
			verify.verifyEquals(msg.get(0).toString(), "Unauthorized");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a note with invalid noteID")
	public void fetchSingleNoteWithInvalidNoteID() throws Exception {
		try {
			// note creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);
			headerParams.put(XAPIKEY, X_API_KEY);

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + "112312321", fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 404);
			JSONObject respJsonForNote = new JSONObject(fetchApiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJsonForNote.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyResponseTime(fetchResp, 5000);
			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch a note with valid noteID but of different account")
	public void fetchSingleNoteWithValidNoteIDOfAnotherAccount() throws Exception {
		try {
			// note creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XUSERKEY, X_USER_KEY);
			headerParams.put(XAPIKEY, X_API_KEY);

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + "5e9415a2c0af3116e3704deb", fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 404);
			JSONObject respJsonForNote = new JSONObject(fetchApiResp.getResponseAsString());
			JSONObject response = (JSONObject) respJsonForNote.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyResponseTime(fetchResp, 5000);
			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch a highlight note")
	public void fetchHighlightNote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("term", "Schweizerische");

			RequestSpecification spe = queryParamsSpecForPublicApis(filters, headerParams);
			Response res = RestOperationUtils.get(NOTES, spe, null);
			APIResponse apiRes = new APIResponse(res);
			JSONObject resJson = new JSONObject(apiRes.getResponseAsString());
			JSONArray arr = resJson.getJSONArray("entries");
			JSONObject objId = (JSONObject) arr.get(0);
			String highlightNoteId = (String) objId.get("id");

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + highlightNoteId, fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 200);
			JSONObject fetchRespJson = new JSONObject(fetchApiResp.getResponseAsString());

			verify.verifyResponseTime(fetchResp, 5000);
			verify.verifyEquals(fetchRespJson.get("type"), "highlight", "Verify note type");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update a private note schroders")
	public void updatingAPrivateNoteCreatedViaAPISchroders() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Note Updation
			List<String> tags = new ArrayList<String>();
			tags.add("abc");

			List<String> tickers = new ArrayList<String>();
			tickers.add("aapl");
			tickers.add("fb");

			String updatedTitle = "Updated Title of Note - " + noteId;
			HashMap<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("title", updatedTitle);
			updateParams.put("content", "<p>THIS IS a typed note. And We are now editing this note</p>");
			updateParams.put("category", "general");
			updateParams.put("tags", tags);
			updateParams.put("tickers", tickers);

			String updateJson = jsonUtils.toJson(updateParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId, null, updateSpec, updateParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 403);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(code, "ACCESS DENIED");
			verify.verifyEquals(message, "Update not allowed Note Read only");

			

			// Note Deleting

//			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Update a private note")
	public void updatingAPrivateNoteCreatedViaAPI() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Note Updation
			List<String> tags = new ArrayList<String>();
			tags.add("abc");

			List<String> tickers = new ArrayList<String>();
			tickers.add("aapl");
			tickers.add("fb");

			String updatedTitle = "Updated Title of Note - " + noteId;
			HashMap<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("title", updatedTitle);
			updateParams.put("content", "<p>THIS IS a typed note. And We are now editing this note</p>");
			updateParams.put("category", "general");
			updateParams.put("tags", tags);
			updateParams.put("tickers", tickers);

			String updateJson = jsonUtils.toJson(updateParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId, null, updateSpec, updateParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			
			
			//note deletion
//			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update an attachment note")
	public void updatingAnAttachmentNote() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);
			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("filter", "type eq attachment");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstElment = (JSONObject) entries.get(0);
			String noteId = (String) firstElment.get("id");

			// Note Updation
			List<String> tags = new ArrayList<String>();
			tags.add("abc");

			List<String> tickers = new ArrayList<String>();
			tickers.add("ssl");
			tickers.add("plse");

			String updatedTitle = "Updated Title of Note - " + noteId;
			HashMap<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("title", updatedTitle);
			updateParams.put("content", "<p>THIS IS a typed note. And We are now editing this note</p>");
			updateParams.put("category", "general");
			updateParams.put("tags", tags);
			updateParams.put("tickers", tickers);

			String updateJson = jsonUtils.toJson(updateParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId, null, updateSpec, updateParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			JSONArray tickersArray = updateRespJson.getJSONArray("tickers");
			JSONArray tagsArray = updateRespJson.getJSONArray("tags");

			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("id"), noteId, "Verify the id");
			verify.verifyEquals(tickersArray.get(0), tickers.get(0).toUpperCase());
			verify.verifyEquals(tickersArray.get(1), tickers.get(1).toUpperCase());
			verify.verifyEquals(tagsArray.get(0), tags.get(0));
			verify.verifyEquals(updateRespJson.get("title"), updatedTitle, "Verify the note title");
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "updatingASpecificNote.json");
			

			//note deletion
//			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteResp, 5000);


		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Update an attachment note and do file versioning")
	public void updatingAnAttachmentNoteAndDoFileVersion() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "wordFile.docx";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification spec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp = RestOperationUtils.post(FILE_UPLOAD, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String fileId = respJson.get("id").toString();
			String contentType = respJson.get("content_type").toString();

			// Creating an attachment note
			HashMap<String, String> headerParamsForNote = new HashMap<String, String>();
			headerParamsForNote.put(XAPIKEY, X_API_KEY);
			headerParamsForNote.put(XUSERKEY, X_USER_KEY);

			List<String> tickers = new ArrayList<String>();
			tickers.add("appl");
			tickers.add("fb");

			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("type", "attachment");
			formParams.put("category", "meeting");
			formParams.put("file", listOfFileParams);
			formParams.put("tickers", tickers);

			String jsonForRequest = jsonUtils.toJson(formParams);

			RequestSpecification specForNote = requestHeadersFormSpecForPublicApis(jsonForRequest, headerParamsForNote);
			Response respForNote = RestOperationUtils.post(NOTES, null, specForNote, formParams);
			APIResponse apiRespForNote = new APIResponse(respForNote);
			verify.verifyStatusCode(apiRespForNote.getStatusCode(), 201);
			JSONObject respJsonForNote = new JSONObject(apiRespForNote.getResponseAsString());
			String noteId = (String) respJsonForNote.get("id");

			// Note Updation

			RequestSpecification spec1 = multipartParamSpecForPublicApis(params, headerParams, file);
			Response resp1 = RestOperationUtils.post(FILE_UPLOAD, null, spec1, params);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			String fileId1 = respJson1.get("id").toString();

			HashMap<String, String> fileParams1 = new HashMap<String, String>();
			fileParams1.put("file_id", fileId1);
			fileParams1.put("content_type", contentType);
			fileParams1.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams1 = new ArrayList<Map<String, String>>();
			listOfFileParams1.add(fileParams1);
			HashMap<String, Object> updateParams = new HashMap<String, Object>();

			updateParams.put("file", listOfFileParams1);

			String updateJson = jsonUtils.toJson(updateParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId, null, updateSpec, updateParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(updateResp, 5000);
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "updatingASpecificNote.json");
			

			//note deletion
//			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Update a note with invalid Id")
	public void updatingANoteWithInvalidId() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			String noteId = "123";

			// Note Updation
			String updatedTitle = "Updated Title of Note - " + noteId;
			HashMap<String, Object> updateParams = new HashMap<String, Object>();
			updateParams.put("title", updatedTitle);
			updateParams.put("content", "<p>THIS IS a typed note. And We are now editing this note</p>");
			updateParams.put("category", "general");

			String updateJson = jsonUtils.toJson(updateParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(updateJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId, null, updateSpec, updateParams);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 404);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(code, "NOTE NOT FOUND");
			verify.verifyEquals(message, "Note requested not found");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Delete a note")
	public void deletingANote() throws Exception {
		try {

			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Note Deleting

//			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
//			APIResponse updateApiResp = new APIResponse(updateResp);
//
//			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Delete a note with invalid id")
	public void deletingANoteWithInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.delete(NOTES + "/" + "1233", null, updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyEquals(updateRespJson.getJSONObject("error").get("code"), "NOTE NOT FOUND");
			verify.verifyEquals(updateRespJson.getJSONObject("error").get("message"), "Note requested not found");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete ticker in a private note schroders")
	public void addingDeletingTickerInPrivateNoteSchroders() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);
			
			List<String> tickers = new ArrayList<String>();
			tickers.add("fb");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");
			formParams.put("tickers", tickers);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding Ticker
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 403);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(code, "ACCESS DENIED");
			verify.verifyEquals(message, "Update not allowed Note Read only");
			
			
			//deleting a ticker
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);

		
			JSONObject deleteRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 403);
			verify.verifyResponseTime(deleteResp, 5000);
			JSONObject responseDelete = (JSONObject) deleteRespJson.get("error");
			Object deleteMessage = responseDelete.get("message");
			Object deleteCode = responseDelete.get("code");

			verify.verifyEquals(deleteCode, "ACCESS DENIED");
			verify.verifyEquals(deleteMessage, "Update not allowed Note Read only");
			
			

			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);
//

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Add and delete ticker in a private note")
	public void addingDeletingTickerInPrivateNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);
			
			List<String> tickers = new ArrayList<String>();
			tickers.add("fb");

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");
			formParams.put("tickers", tickers);

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding Ticker
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONArray tickerArray =  updateRespJson.getJSONArray("tickers");
			verify.verifyEquals(tickerArray.get(0), "fb");
			
	
			
			//deleting a ticker
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			JSONObject deleteRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);


		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}



	@Test(description = "Add and delete ticker in a note in attachment note")
	public void addingDeletingTickerInAttachmentNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("filter", "type eq attachment");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstElment = (JSONObject) entries.get(0);
			String noteId = (String) firstElment.get("id");

			// Adding Ticker
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("parent_note_id"), noteId, "Verify the message");
			verify.jsonSchemaValidation(updateResp, "notebookPublicApi" + File.separator + "addingTickerToNote.json");

			// deleting Ticker
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);

			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete ticker with invalid Id")
	public void addingDeletingTickerWithInvalidId() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			// Adding Ticker
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + 123 + "/" + "tickers" + "/" + "fb", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 404);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");

			// deleting Ticker
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + 131 + "/" + "tickers" + "/" + "fb", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);

			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(deleteResp, 5000);
			JSONObject deleteRespJson = new JSONObject(deleteApiResp.getResponseAsString());
			JSONObject response1 = (JSONObject) deleteRespJson.get("error");
			Object message1 = response1.get("message");
			Object code1 = response1.get("code");

			verify.verifyEquals(message1, "Note requested not found");
			verify.verifyEquals(code1, "NOTE NOT FOUND");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete tag in a private note schroders")
	public void addingDeletingTagInPrivateNoteSchroders() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding Tag
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 403);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(code, "ACCESS DENIED");
			verify.verifyEquals(message, "Update not allowed Note Read only");
			
			//deleting tag
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			JSONObject deleteRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 403);
			verify.verifyResponseTime(deleteResp, 5000);
			JSONObject responseDelete = (JSONObject) deleteRespJson.get("error");
			Object deleteMessage = responseDelete.get("message");
			Object deleteCode = responseDelete.get("code");

			verify.verifyEquals(deleteCode, "ACCESS DENIED");
			verify.verifyEquals(deleteMessage, "Update not allowed Note Read only");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

			
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Add and delete tag in a private note")
	public void addingDeletingTagInPrivateNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + new Date().getTime());
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding Tag
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONArray tagsArray =  updateRespJson.getJSONArray("tags");
			verify.verifyEquals(tagsArray.get(0), "mynote");
			
			
			//deleting a tag
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete tag in a Attachment note")
	public void addingDeletingTagInAttachmentNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("filter", "type eq attachment");

			RequestSpecification spec = queryParamsSpecForPublicApis(filters, headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray entries = respJson.getJSONArray("entries");
			JSONObject firstElment = (JSONObject) entries.get(0);
			String noteId = (String) firstElment.get("id");

			// Adding Tag
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("parent_note_id"), noteId, "Verify the message");
			verify.jsonSchemaValidation(updateResp, "notebookPublicApi" + File.separator + "addingTagToNote.json");

			// deleting Tag
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + noteId + "/" + "tags" + "/" + "mynote", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);

			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(deleteResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete tag in with invalid note id")
	public void addingAndDeletingTagWithInvalidId() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			// Adding Tag
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + 123 + "/" + "tags" + "/" + "mynote", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 404);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");

			// deleting Tag
			RequestSpecification deleteSpec = requestHeadersSpecForPublicApis(headerParams);
			Response deleteResp = RestOperationUtils.delete(NOTES + "/" + 123 + "/" + "tags" + "/" + "mynote", null,
					deleteSpec, null);
			APIResponse deleteApiResp = new APIResponse(deleteResp);

			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 404);
			verify.verifyResponseTime(deleteResp, 5000);
			JSONObject deleteRespJson = new JSONObject(deleteApiResp.getResponseAsString());
			JSONObject response1 = (JSONObject) deleteRespJson.get("error");
			Object message1 = response1.get("message");
			Object code1 = response1.get("code");

			verify.verifyEquals(message1, "Note requested not found");
			verify.verifyEquals(code1, "NOTE NOT FOUND");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note")
	public void addingAttachmentInaNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("message"), "Attachments Added Successfully", "Verify the message");
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "addingAttachmentInaNote.json");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with created and updated date")
	public void addingAttachmentInaNoteWithCreatedAndUpdatedDate() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);
			fileParams.put("created_at", "2018-11-19T15:29:32");
			fileParams.put("updated_at", "2017-11-19T15:29:32");

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("message"), "Attachments Added Successfully", "Verify the message");
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "addingAttachmentInaNote.json");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with Incorrect date format")
	public void addingAttachmentInaNoteWithIncorrectDate() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);
			fileParams.put("created_at", "2017-:29:32");
			fileParams.put("updated_at", "2017-11-19T15:29:32");

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 400);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Bad Format of Date try sending in format %Y-%m-%dT%H:%M:%S");
			verify.verifyEquals(code, "INVALID REQUEST");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with invalid note id")
	public void addingAttachmentInaNoteWithInvalidNoteId() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + 123 + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 404);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with invalid File Id")
	public void addingAttachmentInaNoteUsingInvalidFileId() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", "123");
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 400);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Invalid file_id");
			verify.verifyEquals(code, "INVALID REQUEST");
			
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with contenttype mismatch")
	public void addingAttachmentInaNoteContenTypeMismatch() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = "text/html";

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + fileExtension);

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 400);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Content-type mismatch with uploaded file");
			verify.verifyEquals(code, "INVALID REQUEST");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add attachment in a note with file extension mismatch")
	public void addingAttachmentInaNoteFileExtensionMismatch() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(4));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");

			// Adding attachment
			String folderName = "notebookPublicApi" + File.separator;
			String fileName = folderName + "samplePdf.pdf";
			String fileExtension = FilenameUtils.getExtension(fileName);
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);

			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("files", file);
			params.put("file", fileName);

			RequestSpecification Filespec = multipartParamSpecForPublicApis(params, headerParams, file);
			Response fileResp = RestOperationUtils.post(FILE_UPLOAD, null, Filespec, params);
			APIResponse fileApiResp = new APIResponse(fileResp);
			JSONObject fileRespJson = new JSONObject(fileApiResp.getResponseAsString());
			String fileId = fileRespJson.get("id").toString();
			String contentType = fileRespJson.get("content_type").toString();

			// Uploading it to note
			HashMap<String, String> fileParams = new HashMap<String, String>();
			fileParams.put("file_id", fileId);
			fileParams.put("content_type", contentType);
			fileParams.put("filename", "fileName" + new Date().getTime() + "." + "txt");

			List<Map<String, String>> listOfFileParams = new ArrayList<Map<String, String>>();
			listOfFileParams.add(fileParams);

			HashMap<String, Object> uploadAttachParams = new HashMap<String, Object>();
			uploadAttachParams.put("files", listOfFileParams);

			String dataDictJson = jsonUtils.toJson(uploadAttachParams);

			RequestSpecification updateSpec = requestHeadersFormSpecForPublicApis(dataDictJson, headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "attachments", null, updateSpec,
					null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			verify.verifyStatusCode(updateApiResp.getStatusCode(), 400);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());
			verify.verifyResponseTime(updateResp, 5000);
			JSONObject response = (JSONObject) updateRespJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "File extension mismatch with uploaded file");
			verify.verifyEquals(code, "INVALID REQUEST");
			
			//note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch all the comments from a note")
	public void fetchCommentsAddedInANote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("term", "Rio-Tokyo");
			filters.put("filter", "type eq typed");

			RequestSpecification spe = queryParamsSpecForPublicApis(filters, headerParams);
			Response res = RestOperationUtils.get(NOTES, spe, null);
			APIResponse apiRes = new APIResponse(res);
			JSONObject resJson = new JSONObject(apiRes.getResponseAsString());
			JSONArray arr = resJson.getJSONArray("entries");
			JSONObject objId = (JSONObject) arr.get(0);
			String noteId = (String) objId.get("id");

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + noteId + "/" + "comments", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONArray("entries").length(), 2, "Verify Comments length");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "fetchCommentAddedInANote.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Fetch all the comments from a note for invalid Id")
	public void fetchCommentsAddedInANoteForInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + 134 + "/" + "comments", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 404);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			JSONObject response = (JSONObject) respJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch Highlight created in a note")
	public void fetchHighlightsCreatedInANote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			HashMap<String, String> filters = new HashMap<String, String>();
			filters.put("limit", "1");
			filters.put("term", "Schweizerische");

			RequestSpecification spe = queryParamsSpecForPublicApis(filters, headerParams);
			Response res = RestOperationUtils.get(NOTES, spe, null);
			APIResponse apiRes = new APIResponse(res);
			JSONObject resJson = new JSONObject(apiRes.getResponseAsString());
			JSONArray arr = resJson.getJSONArray("entries");
			JSONObject objId = (JSONObject) arr.get(0);
			String noteId = (String) objId.get("id");

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + noteId + "/" + "highlights", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONArray("entries").length(), 1, "Verify Highlights length");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "fetch Highlight created in a note for invalid id")
	public void fetchHighlightsCreatedInANoteForInvalidId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + 1233 + "/" + "highlights", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 404);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			JSONObject response = (JSONObject) respJson.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Note requested not found");
			verify.verifyEquals(code, "NOTE NOT FOUND");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Add Gif,Png,Jpeg,Bitmap image in a note Content", dataProvider = "notebookImageData", dataProviderClass = DataProviderClass.class)
	public void addImageInNoteContentAndDownload(String imageData[]) throws Exception {
		try {
			
			String contentType = imageData[0];
			String imageName = imageData[1];
			String extension = imageData[2];
			
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources("notebookPublicApi" + File.separator + imageName);
			String encodeFileToBase64Binary = CommonUtil.encodeFileToBase64Binary(file);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title",
					"Typed note With " + contentType + " image in content" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<img src='data:" + contentType +";base64," + encodeFileToBase64Binary
					+ "'> <br><p>This is a typed note with " + contentType + "</p>");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = respJson.getString("id");
			verify.verifyResponseTime(resp, 5000);
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");

			// Download image from content
			JSONObject noteDetail = getNoteDetail(noteId);
			JSONObject result = noteDetail.getJSONObject("result");
			String content = result.getString("content");
			String[] splitKey = content.split("key");
			String[] splitImage = splitKey[1].split(extension);
			String imageId = splitImage[0].replace("=", "");

			RequestSpecification imageSpec = requestHeadersSpecForPublicApis(headerParams);
			Response respimage = RestOperationUtils
					.get(NOTES + "/" + noteId + "/" + "images" + "/" + imageId + "/" + "content", imageSpec, null);
			APIResponse apiRespImage = new APIResponse(respimage);
			verify.verifyStatusCode(apiRespImage.getStatusCode(), 200);
			verify.verifyResponseTime(respimage, 5000);
			verify.verifyEquals(apiRespImage.getContentType(), contentType);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	
	
	@Test(description = "Verify error for invalid image id")
	public void testErrorMessageForInvalidImageId() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources("notebookPublicApi" + File.separator + "tenor.gif");
			String encodeFileToBase64Binary = CommonUtil.encodeFileToBase64Binary(file);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title",
					"Typed note With gif image in content" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<img src='data:image/gif;base64," + encodeFileToBase64Binary
					+ "'> <br><p>This is a typed note with gif image</p>");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = respJson.getString("id");
			verify.verifyResponseTime(resp, 5000);
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");

			// Download image from content
			JSONObject noteDetail = getNoteDetail(noteId);
			JSONObject result = noteDetail.getJSONObject("result");
			String content = result.getString("content");
			String[] splitKey = content.split("key");
			String[] splitImage = splitKey[1].split(".gif");
			String imageId = splitImage[0].replace("=", "");

			RequestSpecification imageSpec = requestHeadersSpecForPublicApis(headerParams);
			Response respimage = RestOperationUtils
					.get(NOTES + "/" + noteId + "/" + "images" + "/" + "2123" + "/" + "content", imageSpec, null);
			APIResponse apiRespImage = new APIResponse(respimage);
			verify.verifyStatusCode(apiRespImage.getStatusCode(), 400);
			JSONObject respJsonImage = new JSONObject(apiRespImage.getResponseAsString());
			verify.verifyResponseTime(respimage, 5000);
			JSONObject response = (JSONObject) respJsonImage.get("error");
			Object message = response.get("message");
			Object code = response.get("code");

			verify.verifyEquals(message, "Invalid file_id");
			verify.verifyEquals(code, "INVALID REQUEST");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	
	@Test(description = "Test Large File upload")
	public void testLargeFileUpload() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources("notebookPublicApi" + File.separator + "largeFile.pdf");
			
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("filename", "samplePdf.pdf");
			String json = jsonUtils.toJson(formParams);
			
			//hitting file_url API
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(LARGE_FILE_UPLOAD, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			String upload_url = respJson.getString("upload_url");
			String[] split = upload_url.split("/attachments");
			String id =  respJson.getString("id");
			String filename =  respJson.getString("filename");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(filename, "samplePdf.pdf");
			
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			MediaType mediaType = MediaType.parse("application/pdf");
			RequestBody body = RequestBody.create(mediaType, file);
			Request request = new Request.Builder().url(upload_url).method("PUT", body)
					.addHeader("Content-Type", "application/pdf").build();
			client.newCall(request).execute();
			verify.verifyStatusCode(client.newCall(request).execute().networkResponse().code(), 200);
		}catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	@Test(description = "Soft delete and Restore a note")
	public void testSoftDeleteAndRestoreOfNote() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);
			
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("ref", new Date().toString().trim());
			formParams.put("type", "typed");
			formParams.put("title", "PrivateNote" + String.valueOf(new Date().getTime()).indexOf(6));
			formParams.put("content", "<p>THIS IS a typed note</p>");
			formParams.put("category", "meeting");

			String json = jsonUtils.toJson(formParams);

			RequestSpecification spec = requestHeadersFormSpecForPublicApis(json, headerParams);
			Response resp = RestOperationUtils.post(NOTES, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 201);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String noteId = (String) respJson.get("id");
			
			//active : 0 (soft delete)
			HashMap<String, Object> deleteParams = new HashMap<String, Object>();
			deleteParams.put("active", 0);
			String deleteJson = jsonUtils.toJson(deleteParams);
			
			RequestSpecification deleteSpec = requestHeadersFormSpecForPublicApis(deleteJson, headerParams);
			Response deleteResp = RestOperationUtils.post(NOTES + "/" + noteId , null, deleteSpec, deleteParams);
			APIResponse deleteApiResp = new APIResponse(deleteResp);
			verify.verifyStatusCode(deleteApiResp.getStatusCode(), 200);
			JSONObject deleteRespJson = new JSONObject(deleteApiResp.getResponseAsString());
			verify.verifyEquals(deleteRespJson.get("active"), 0);
			System.out.println(deleteRespJson);
			
			//active : 1 (restoring note)
			HashMap<String, Object> restoreParams = new HashMap<String, Object>();
			restoreParams.put("active", 1);
			String restoreJson = jsonUtils.toJson(restoreParams);
			
			RequestSpecification restoreSpec = requestHeadersFormSpecForPublicApis(restoreJson, headerParams);
			Response restoreResp = RestOperationUtils.post(NOTES + "/" + noteId , null, restoreSpec, restoreParams);
			APIResponse restoreApiResp = new APIResponse(restoreResp);
			verify.verifyStatusCode(restoreApiResp.getStatusCode(), 200);
			JSONObject restoreRespJson = new JSONObject(restoreApiResp.getResponseAsString());
			verify.verifyEquals(restoreRespJson.get("active"), 1);
			System.out.println(restoreRespJson);
			
			//permanent note deletion
//			RequestSpecification deleteNoteSpec = requestHeadersSpecForPublicApis(headerParams);
//			Response deleteNoteResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, deleteNoteSpec, null);
//			APIResponse deletedApiResp = new APIResponse(deleteNoteResp);
//
//			verify.verifyStatusCode(deletedApiResp.getStatusCode(), 204);
//			verify.verifyResponseTime(deleteNoteResp, 5000);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
	
	

	public JSONObject getNoteDetail(String noteID) throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("id", noteID);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post( USER_APP_URL + FETCH_NOTE_HTML, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			if (apiResp.getStatusCode() == 200) {
				return respJson;
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
			return null;
		} finally {
			verify.verifyAll();
		}
		return null;
	}
	
	
}
