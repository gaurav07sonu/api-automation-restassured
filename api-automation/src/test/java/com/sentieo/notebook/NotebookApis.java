package com.sentieo.notebook;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
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
import com.sentieo.utils.FileUtil;
import com.sentieo.utils.JSONUtils;

/**
 * 
 * @author Akash
 *
 */

public class NotebookApis extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
	}

	@BeforeSuite
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

	@Test(groups = "sanity", description = "Create private note")
	public void createPrivateNote() throws Exception {
		try {
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("result").getString("temp_id"), tempId,
					"Verify that Requested ticker Visible in the API");
			verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createPrivateNote.json");

			// delete note
			String note_id = respJson.getJSONObject("result").getString("id");
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
			Response resp1 = RestOperationUtils.post(DELETE_NOTE, null, spec1, deleteNoteParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());
			verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Delete private note")
	public void deletePrivateNote() throws Exception {
		try {
			// Note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("verion", "1");
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");

			// delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
			Response resp1 = RestOperationUtils.post(DELETE_NOTE, null, spec1, deleteNoteParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			// validation
			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 3000);
			verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson1.getJSONObject("result").getInt("status"), 1,
					"Verify that Requested ticker Visible in the API");
			verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deletePrivateNote.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Email note")
	public void emailNote() throws Exception {
		try {
			
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("verion", "1");
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			String emailId = SEND_NOTE_EMAIL;
			HashMap<String, String> emailParams = new HashMap<String, String>();
			emailParams.put("emailto", emailId);
			emailParams.put("id", note_id);

			RequestSpecification emailSpec = formParamsSpec(emailParams);
			Response emailResp = RestOperationUtils.post(EMAIL_NOTE, null, emailSpec, emailParams);
			APIResponse emailApiResp = new APIResponse(emailResp);
			JSONObject emailRespJson = new JSONObject(emailApiResp.getResponseAsString());

			verify.verifyStatusCode(emailApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(emailResp, 5000);
			verify.verifyEquals(emailRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(emailRespJson.getJSONObject("result").getJSONArray("allowed_addresses").get(0), emailId,
					"Verify that Requested ticker Visible in the API");
			verify.jsonSchemaValidation(emailResp, "notebook" + File.separator + "emailNote.json");
			
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Fetch all notes")
	public void fetchNoteAllList() throws Exception {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "5");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp, "notebook" + File.separator +
			// "fetchNoteAllList.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Upload Note", enabled = true)
	public void uploadNote() throws Exception {
		try {
			String folderName = "notebook" + File.separator;
			String fileName = folderName + "pptfile.pptx";
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attachments", file);
			params.put("qqfile", fileName);

			RequestSpecification spec = multipartParamSpec(params, file);
			Response resp = RestOperationUtils.post(UPLOAD_FILE, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			String id = respJson.get("id").toString();
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("success"), true, "Verify the API Response Status");
			verify.verifyEquals(folderName + respJson.get("filename"), fileName, "Verify the API Response Status");
			verify.jsonSchemaValidation(resp, "notebook" + File.separator + "uploadNote.json");

			// Create note for attachments
			HashMap<String, String> attachParams = new HashMap<String, String>();
			List<String> tickers = new ArrayList<String>();
			tickers.add("AAPL");

			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("file_id", id);
			dataMap.put("filename", fileName);
			dataMap.put("data_id", "1");
			dataMap.put("content_type", "application/vnd.ms-powerpoint");

			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			dataList.add(dataMap);
			String dataJson = jsonUtils.toJson(dataList);
			String tickerJson = jsonUtils.toJson(tickers);

			attachParams.put("added_tickers", tickerJson);
			attachParams.put("dataDict", dataJson);

			RequestSpecification spec1 = formParamsSpec(attachParams);
			Response resp1 = RestOperationUtils.post(CREATE_ATTACHMENT_NOTE, null, spec1, attachParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 3000);
			verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
					"Verify the API Response Status");

			// Delete note created
			String noteIdToBeDeleted = respJson1.getJSONObject("response").getJSONArray("note_id_arr").get(0)
					.toString();
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", noteIdToBeDeleted);

			RequestSpecification spec2 = formParamsSpec(deleteNoteParams);
			Response resp2 = RestOperationUtils.post(DELETE_NOTE, null, spec2, deleteNoteParams);
			APIResponse apiResp2 = new APIResponse(resp2);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());

			verify.verifyEquals(respJson2.getJSONObject("response").get("status"), true,
					"Verify the API Response Status");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with text")
	public void searchWithText() throws Exception {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "mine");
			params.put("query", "add image");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// verify.verifyEquals(respJson.getJSONObject("response").getString("msg"),
			// "success", "Verify the API Message");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with ticker")
	public void searchWithTicker() throws Exception {

		try {
			List<String> tickers = new ArrayList<String>();
			tickers.add("aapl");
			String tickerJson = jsonUtils.toJson(tickers);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "10");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("tickers", tickerJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with tag")
	public void searchWithTag() throws Exception {
		try {
			List<String> tags = new ArrayList<String>();
			tags.add("yahoo");
			String tagsJson = jsonUtils.toJson(tags);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("tags", tagsJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with author")
	public void searchWithAuthor() throws Exception {
		try {
			List<String> users = new ArrayList<String>();
			users.add("anurag");
			String usersJson = jsonUtils.toJson(users);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("users", usersJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with label")
	public void searchWithLabel() throws Exception {
		try {
			List<String> labels = new ArrayList<String>();
			labels.add("testing");
			String labelsJson = jsonUtils.toJson(labels);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("labels", labelsJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with sectors")
	public void searchWithSectors() throws Exception {
		try {
			List<String> sectors = new ArrayList<String>();
			sectors.add("Energy");
			String sectorsJson = jsonUtils.toJson(sectors);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("gics_sector", sectorsJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// verify.verifyEquals(respJson.getJSONObject("response").getString("msg"),
			// "success", "Verify the API Message");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with region", enabled = true)
	public void searchWithRegion() throws Exception {
		try {
			List<String> countryCode = new ArrayList<String>();
			countryCode.add("na");
			String countryCodeJson = jsonUtils.toJson(countryCode);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("countrycode", countryCodeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with note category")
	public void searchWithNoteCategory() throws Exception {
		try {
			List<String> category = new ArrayList<String>();
			category.add("Meeting");
			String categoryJson = jsonUtils.toJson(category);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("kind", categoryJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : email", enabled = true)
	public void searchWithNoteTypeAsEmail() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("email");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : bookmark")
	public void searchWithNoteTypeAsBookmark() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("bookmark");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : clipped")
	public void searchWithNoteTypeAsClipped() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("clipper");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : attachment")
	public void searchWithNoteTypeAsAttachment() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("attachment");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : plotter")
	public void searchWithNoteTypeAsPlotter() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("plotter");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : highlight")
	public void searchWithNoteTypeAsHighlight() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("highlight");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : thesis")
	public void searchWithNoteTypeAsThesis() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("thesis");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : typed")
	public void searchWithNoteTypeAsTyped() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("notebook");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with noteType : charts and tables")
	public void searchWithNoteTypeAsChartsAndTables() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("sntclipper");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify that Requested ticker Visible in the API");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Create Thesis and verify", enabled = false)
	public void createThesis() throws Exception {
		try {
			HashMap<String, String> thesisData = new HashMap<String, String>();
			thesisData.put("thesis_type", "thesis");
			thesisData.put("tickers", "dfkcy");
			thesisData.put("name", "DFKCY Thesis");
			thesisData.put("tab_name", "Overview");

			String dataJson = jsonUtils.toJson(thesisData);

			HashMap<String, String> thesisParams = new HashMap<String, String>();
			thesisParams.put("action", "create_thesis_and_tab");
			thesisParams.put("thesis_dictionary", dataJson);
			thesisParams.put("create_default_children", Boolean.TRUE.toString());

			RequestSpecification spec = formParamsSpec(thesisParams);
			Response resp = RestOperationUtils.post(THESIS_ENTITY, null, spec, thesisParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			// verify.jsonSchemaValidation(resp, "notebook" + File.separator +
			// "createThesis.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	

	@Test(groups = "sanity", description = "Add Note Tag")
	public void addNoteTag() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("id", note_id);
			params.put("field", "tag");
			params.put("action", "add");
			params.put("term", "aa");

			RequestSpecification tagSpec = formParamsSpec(params);
			Response tagResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, tagSpec, params);
			APIResponse tagApiResp = new APIResponse(tagResp);
			JSONObject tagRespJson = new JSONObject(tagApiResp.getResponseAsString());

			verify.verifyStatusCode(tagApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(tagResp, 5000);
			verify.verifyEquals(tagRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(tagResp, "notebook" + File.separator + "addNoteTag.json");
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Remove Note Tag")
	public void removeNoteTag() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			//add tag
			HashMap<String, String> addparams = new HashMap<String, String>();
			addparams.put("id", note_id);
			addparams.put("field", "tag");
			addparams.put("action", "add");
			addparams.put("term", "aa");

			RequestSpecification tagSpec = formParamsSpec(addparams);
			RestOperationUtils.post(UPDATE_TAG_TICKER, null, tagSpec, addparams);
			
			//remove tag
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("id", note_id);
			params.put("field", "tag");
			params.put("action", "remove");
			params.put("term", "aa");

			RequestSpecification removeTagSpec = formParamsSpec(params);
			Response tagResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, removeTagSpec, params);
			APIResponse tagApiResp = new APIResponse(tagResp);
			JSONObject tagRespJson = new JSONObject(tagApiResp.getResponseAsString());

			verify.verifyStatusCode(tagApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(tagResp, 5000);
			verify.verifyEquals(tagRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(tagResp, "notebook" + File.separator + "removeNoteTag.json");
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Add Note Ticker")
	public void addNoteTicker() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
		
			//add ticker
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("id", note_id);
			params.put("field", "ticker");
			params.put("action", "add");
			params.put("term", "aapl");

			RequestSpecification tickerSpec = formParamsSpec(params);
			Response tickerResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, tickerSpec, params);
			APIResponse tickerApiResp = new APIResponse(tickerResp);
			JSONObject tickerRespJson = new JSONObject(tickerApiResp.getResponseAsString());

			verify.verifyStatusCode(tickerApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(tickerResp, 5000);
			verify.verifyEquals(tickerRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(tickerResp, "notebook" + File.separator + "addNoteTag.json");
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
			
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Remove Note Ticker")
	public void removeNoteTicker() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
		
			//add ticker
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("id", note_id);
			params.put("field", "ticker");
			params.put("action", "add");
			params.put("term", "aapl");

			RequestSpecification tickerSpec = formParamsSpec(params);
			RestOperationUtils.post(UPDATE_TAG_TICKER, null, tickerSpec, params);
			
			
			HashMap<String, String> removeTickerParams = new HashMap<String, String>();
			removeTickerParams.put("id", note_id);
			removeTickerParams.put("field", "tag");
			removeTickerParams.put("action", "remove");
			removeTickerParams.put("term", "aapl");

			RequestSpecification removeSpec = formParamsSpec(removeTickerParams);
			Response removeResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, removeSpec, removeTickerParams);
			APIResponse removeApiResp = new APIResponse(removeResp);
			JSONObject removeRespJson = new JSONObject(removeApiResp.getResponseAsString());

			verify.verifyStatusCode(removeApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(removeResp, 5000);
			verify.verifyEquals(removeRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(removeResp, "notebook" + File.separator + "removeNoteTag.json");
			
			//delete note
			
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	
	@Test(groups = "sanity", description = "create Thesis template", enabled = false)
	public void createThesisTemplate() throws Exception {
		try {
			
			HashMap<String, String> templateDict = new HashMap<String, String>();
			templateDict.put("name", "autothesis"+ new Date());
			
			String templateDictJson = jsonUtils.toJson(templateDict);
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("create_default_children", Boolean.TRUE.toString());
			params.put("action", "create_thesis_template");
			params.put("template_dictionary", templateDictJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			//verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createThesisTemplate.json");
			
			//delete template
			JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
			String id = String.valueOf(res.get("id"));
			
			
			HashMap<String, String> deleteParams = new HashMap<String, String>();
			deleteParams.put("template_id", id);
			deleteParams.put("action", "delete_template");

			RequestSpecification spec1 = formParamsSpec(deleteParams);
			RestOperationUtils.post(TEMPLATE_ENTITY, null, spec1, deleteParams);

		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "sanity", description = "create tab template", enabled = false)
	public void createTabTemplate() throws Exception {
		try {
			
			HashMap<String, String> templateDict = new HashMap<String, String>();
			templateDict.put("name", "autoTab"+ new Date().getTime());
			
			String templateDictJson = jsonUtils.toJson(templateDict);
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("create_default_children", Boolean.TRUE.toString());
			params.put("action", "create_tab_template");
			params.put("template_dictionary", templateDictJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createTabTemplate.json");
			
			//delete template
			JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
			String id = String.valueOf(res.get("id"));
			
			
			HashMap<String, String> deleteParams = new HashMap<String, String>();
			deleteParams.put("template_id", id);
			deleteParams.put("action", "delete_template");

			RequestSpecification spec1 = formParamsSpec(deleteParams);
			RestOperationUtils.post(TEMPLATE_ENTITY, null, spec1, deleteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "sanity", description = "Delete thesis template", enabled = false)
	public void deleteThesisTemplate() throws Exception {
		try {
			
			HashMap<String, String> templateDict = new HashMap<String, String>();
			templateDict.put("name", "autothesis"+ new Date());
			
			String templateDictJson = jsonUtils.toJson(templateDict);
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("create_default_children", Boolean.TRUE.toString());
			params.put("action", "create_thesis_template");
			params.put("template_dictionary", templateDictJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
			String id = String.valueOf(res.get("id"));
			
			
			HashMap<String, String> deleteParams = new HashMap<String, String>();
			deleteParams.put("template_id", id);
			deleteParams.put("action", "delete_template");

			RequestSpecification spec1 = formParamsSpec(deleteParams);
			Response resp1 = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec1, deleteParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deleteThesisTemplate.json");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	
	@Test(groups = "sanity", description = "Star note")
	public void starNote() throws Exception {
		try {
			
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("noteid", note_id);
			
			RequestSpecification starSpec = formParamsSpec(params);
			Response starResp = RestOperationUtils.post(STAR_NOTE, null, starSpec, params);
			APIResponse starApiResp = new APIResponse(starResp);
			JSONObject starRespJson = new JSONObject(starApiResp.getResponseAsString());

			verify.verifyStatusCode(starApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(starResp, 5000);
			verify.verifyEquals(starRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(starRespJson.get("result"), "starred",
					"Verify the API result");
			verify.jsonSchemaValidation(starResp, "notebook" + File.separator + "starNote.json");
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}

	}
	
	@Test(groups = "sanity", description = "un-Star note")
	public void unstarNote() throws Exception {
		try {
			
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("noteid", note_id);
			
			RequestSpecification unstarSpec = formParamsSpec(params);
			RestOperationUtils.post(STAR_NOTE, null, unstarSpec, params);
			Response unstarResp = RestOperationUtils.post(UNSTAR_NOTE, null, unstarSpec, params);
			APIResponse unstarApiResp = new APIResponse(unstarResp);
			JSONObject unstarRespJson = new JSONObject(unstarApiResp.getResponseAsString());

			verify.verifyStatusCode(unstarApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(unstarResp, 5000);
			verify.verifyEquals(unstarRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(unstarRespJson.get("result"), "unstarred",
					"Verify the API result");
			verify.jsonSchemaValidation(unstarResp, "notebook" + File.separator + "unstarNote.json");
			
			//Delete note
			
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}

	}
	
	
	@Test(groups = "sanity", description = "Add attachment to a note")
	public void addAttachmentToANote() throws Exception {
		try {
			
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			
			String folderName = "notebook" + File.separator;
			String fileName = folderName + "wordFile.docx";
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attachments", file);
			params.put("qqfile", fileName);

			RequestSpecification uploadspec = multipartParamSpec(params, file);
			Response uploadResp = RestOperationUtils.post(UPLOAD_FILE, null, uploadspec, params);
			APIResponse uploadApiResp = new APIResponse(uploadResp);
			JSONObject uploadRespJson = new JSONObject(uploadApiResp.getResponseAsString());

			String id = uploadRespJson.get("id").toString();
			verify.verifyStatusCode(uploadApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(uploadResp, 5000);
			
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("file_id", id);
			dataMap.put("filename", fileName);
			dataMap.put("data_id", "1");
			dataMap.put("content_type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			dataMap.put("note_id", note_id);
			dataMap.put("s3_id", id + ".docx");

			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			dataList.add(dataMap);
			String dataJson = jsonUtils.toJson(dataList);
			
			HashMap<String, String> attachParams = new HashMap<String, String>();
			attachParams.put("dataDict", dataJson);
			
			RequestSpecification spec1 = formParamsSpec(attachParams);
			Response resp1 = RestOperationUtils.post(SAVE_ATTACHMENT, null, spec1, attachParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 3000);
			verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
					"Verify the API Response Status");
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);
			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "sanity", description = "Remove attachment from a note")
	public void removeAttachmentFromANote() throws Exception {
		try {
			
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> noteParams = new HashMap<String, String>();
			noteParams.put("ts", tempId);
			noteParams.put("title", "privateApiNote" + new Date());
			noteParams.put("verion", "1");
			noteParams.put("private_note", "true");
			noteParams.put("version", "1");
			noteParams.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(noteParams);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, noteParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			//Adding attachement
			String folderName = "notebook" + File.separator;
			String fileName = folderName + "wordFile.docx";
			FileUtil fileUtil = new FileUtil();
			File file = fileUtil.getFileFromResources(fileName);
			
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("attachments", file);
			params.put("qqfile", fileName);

			RequestSpecification attachSpec = multipartParamSpec(params, file);
			Response attachResp = RestOperationUtils.post(UPLOAD_FILE, null, attachSpec, params);
			APIResponse attachApiResp = new APIResponse(attachResp);
			JSONObject attachRespJson = new JSONObject(attachApiResp.getResponseAsString());

			String id = attachRespJson.get("id").toString();
			verify.verifyStatusCode(attachApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(attachResp, 5000);
			
			HashMap<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("file_id", id);
			dataMap.put("filename", fileName);
			dataMap.put("data_id", "1");
			dataMap.put("content_type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			dataMap.put("note_id", note_id);
			dataMap.put("s3_id", id + ".docx");

			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			dataList.add(dataMap);
			String dataJson = jsonUtils.toJson(dataList);
			
			HashMap<String, String> attachParams = new HashMap<String, String>();
			attachParams.put("dataDict", dataJson);
			
			RequestSpecification spec1 = formParamsSpec(attachParams);
			Response resp1 = RestOperationUtils.post(SAVE_ATTACHMENT, null, spec1, attachParams);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 3000);
			verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
					"Verify the API Response Status");
			
			
			//Removing attachment
			HashMap<String, String> dataMapForRemove = new HashMap<String, String>();
			dataMapForRemove.put("file_id", id);
			dataMapForRemove.put("note_id", note_id);
			
			RequestSpecification spec2 = formParamsSpec(dataMapForRemove);
			Response resp2 = RestOperationUtils.get(REMOVE_ATTACHMENT, spec2, dataMapForRemove);
			APIResponse apiResp2 = new APIResponse(resp2);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());
			
			
			verify.verifyStatusCode(apiResp2.getStatusCode(), 200);
			verify.verifyResponseTime(resp2, 3000);
			verify.verifyEquals(respJson2.getJSONObject("response").get("status"), true,
					"Verify the API Response Status");
			
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "sanity", description = "add user comments")
	public void addUserComments() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("verion", "1");
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			//add comments
			HashMap<String, String> commentDictParams = new HashMap<String, String>();
			commentDictParams.put("source", "note");
			commentDictParams.put("reference_id", note_id);
			commentDictParams.put("comment", "Comment added");

			String json = jsonUtils.toJson(commentDictParams);

			HashMap<String, String> commentParams = new HashMap<String, String>();
			commentParams.put("action", "add");
			commentParams.put("comment_dict", json);

			RequestSpecification commentSpec = formParamsSpec(commentParams);
			Response commentResp = RestOperationUtils.post(USER_COMMENTS, null, commentSpec, commentParams);
			APIResponse commentApiResp = new APIResponse(commentResp);
			JSONObject commentRespJson = new JSONObject(commentApiResp.getResponseAsString());

			verify.verifyStatusCode(commentApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(commentResp, 5000);
			verify.verifyEquals(commentRespJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(commentResp, "notebook" + File.separator + "addUserComment.json");
			
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, spec1, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "delete user comments")
	public void deleteUserComments() throws Exception {
		try {
			
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("verion", "1");
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			HashMap<String, String> commentDictParams = new HashMap<String, String>();
			commentDictParams.put("source", "note");
			commentDictParams.put("reference_id", note_id);
			commentDictParams.put("comment", "Comment added");

			String json = jsonUtils.toJson(commentDictParams);

			HashMap<String, String> commentParams = new HashMap<String, String>();
			commentParams.put("action", "add");
			commentParams.put("comment_dict", json);

			RequestSpecification commentSpec = formParamsSpec(commentParams);
			Response commentResp = RestOperationUtils.post(USER_COMMENTS, null, commentSpec, commentParams);
			APIResponse commentApiResp = new APIResponse(commentResp);
			JSONObject commentRespJson = new JSONObject(commentApiResp.getResponseAsString());

			String commentId = commentRespJson.getJSONObject("result").getString("comment_id");

			HashMap<String, String> commentDictParams1 = new HashMap<String, String>();
			commentDictParams1.put("source", "note");
			commentDictParams1.put("comment_id", commentId);

			
			//delete comment
			String json1 = jsonUtils.toJson(commentDictParams1);

			HashMap<String, String> params1 = new HashMap<String, String>();
			params1.put("action", "delete");
			params1.put("comment_dict", json1);

			RequestSpecification spec1 = formParamsSpec(params1);
			Response resp1 = RestOperationUtils.post(USER_COMMENTS, null, spec1, params1);
			APIResponse apiResp1 = new APIResponse(resp1);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 5000);
			verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deleteUserComment.json");
			
			
			//delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "Edit user comments")
	public void editUserComments() throws Exception {
		try {
			//note creation
			String tempId = "quill" + new Date().getTime();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("ts", tempId);
			params.put("title", "privateApiNote" + new Date());
			params.put("verion", "1");
			params.put("private_note", "true");
			params.put("version", "1");
			params.put("note", "<p>Hello world!!</p>");
			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			String note_id = respJson.getJSONObject("result").getString("id");
			
			HashMap<String, String> commentDictParams = new HashMap<String, String>();
			commentDictParams.put("source", "note");
			commentDictParams.put("reference_id", note_id);
			commentDictParams.put("comment", "Comment added");

			String json = jsonUtils.toJson(commentDictParams);

			HashMap<String, String> commentParams = new HashMap<String, String>();
			commentParams.put("action", "add");
			commentParams.put("comment_dict", json);

			RequestSpecification commentSpec = formParamsSpec(commentParams);
			Response commentResp = RestOperationUtils.post(USER_COMMENTS, null, commentSpec, commentParams);
			APIResponse commentApiResp = new APIResponse(commentResp);
			JSONObject commentRespJson = new JSONObject(commentApiResp.getResponseAsString());

			String commentId = commentRespJson.getJSONObject("result").getString("comment_id");

			// Edit comment

			HashMap<String, String> editcommentDictParams = new HashMap<String, String>();
			editcommentDictParams.put("source", "note");
			editcommentDictParams.put("comment_id", commentId);
			editcommentDictParams.put("new_comment", "Comment Edited");

			String editJson = jsonUtils.toJson(editcommentDictParams);

			HashMap<String, String> params2 = new HashMap<String, String>();
			params2.put("action", "edit");
			params2.put("comment_dict", editJson);

			RequestSpecification spec2 = formParamsSpec(params2);
			Response resp2 = RestOperationUtils.post(USER_COMMENTS, null, spec2, params2);
			APIResponse apiResp2 = new APIResponse(resp2);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());

			verify.verifyStatusCode(apiResp2.getStatusCode(), 200);
			verify.verifyResponseTime(resp2, 5000);
			verify.verifyEquals(respJson2.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(resp2, "notebook" + File.separator + "editUserComment.json");

			// delete Note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		}finally {
			verify.verifyAll();
		}

	}
	
}
