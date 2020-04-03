package com.sentieo.notebookpublicapis;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
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
import com.sentieo.utils.FileUtil;
import com.sentieo.utils.JSONUtils;

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
		RestAssured.baseURI = APPURL;
	}

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

	@Test(description = "Fetch all notes in notebook")
	public void fetchAllNotes() throws Exception {
		try {
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyTrue(respJson.getJSONArray("entries").length() != 0, "Verify the API Response");
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
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("type"), "typed", "Verify the note_category");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "createANote.json");
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
			JSONObject respJsonForNote = new JSONObject(apiRespForNote.getResponseAsString());

			verify.verifyStatusCode(apiRespForNote.getStatusCode(), 200);
			verify.verifyResponseTime(respForNote, 5000);
			verify.verifyEquals(respJsonForNote.get("type"), "attachment", "Verify the API note type");
			verify.jsonSchemaValidation(respForNote, "notebookPublicApi" + File.separator + "createAnAttachment.json");
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
			String highlightNoteId = "5e20262ae8f6c21b5f287c32";
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

			RequestSpecification fetchSpec = requestHeadersSpecForPublicApis(headerParams);
			Response fetchResp = RestOperationUtils.get(NOTES + "/" + highlightNoteId, fetchSpec, null);
			APIResponse fetchApiResp = new APIResponse(fetchResp);
			JSONObject fetchRespJson = new JSONObject(fetchApiResp.getResponseAsString());

			verify.verifyStatusCode(fetchApiResp.getStatusCode(), 200);
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

	@Test(description = "Update a specific note", enabled = false)
	public void updatingASpecificNote() throws Exception {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.get("category"), "meeting", "Verify the note_category");

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
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("id"), noteId, "Verify the message");
			verify.verifyEquals(updateRespJson.get("title"), updatedTitle, "Verify the message");
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "updatingASpecificNote.json");
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

			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.delete(NOTES + "/" + noteId, null, updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 204);
			verify.verifyResponseTime(updateResp, 5000);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

	@Test(description = "Add and delete ticker in a note", enabled = false)
	public void addingDeletingTickerInNote() throws Exception {
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

			// Adding Ticker
			RequestSpecification updateSpec = requestHeadersSpecForPublicApis(headerParams);
			Response updateResp = RestOperationUtils.post(NOTES + "/" + noteId + "/" + "tickers" + "/" + "fb", null,
					updateSpec, null);
			APIResponse updateApiResp = new APIResponse(updateResp);
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
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

	@Test(description = "Add and delete tag in a note", enabled = false)
	public void addingDeletingTagInNote() throws Exception {
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
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
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

	@Test(description = "Add attachment in a note")
	public void addingAttachmentInaNote() throws Exception {
		try {
			// note Creation
			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY, X_API_KEY);
			headerParams.put(XUSERKEY, X_USER_KEY);

		
			String noteId = "5e86d2fde8f6c2082c9869eb";

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
			JSONObject updateRespJson = new JSONObject(updateApiResp.getResponseAsString());

			verify.verifyStatusCode(updateApiResp.getStatusCode(), 200);
			verify.verifyResponseTime(updateResp, 5000);
			verify.verifyEquals(updateRespJson.get("message"), "Attachments Added Successfully", "Verify the message");
			verify.jsonSchemaValidation(updateResp,
					"notebookPublicApi" + File.separator + "addingAttachmentInaNote.json");
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

			String bookmarkNoteId = "5e1c0f6be8f6c262915e7c78";

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + bookmarkNoteId + "/" + "comments", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONArray("entries").length(), 1, "Verify Comments length");
			verify.jsonSchemaValidation(resp, "notebookPublicApi" + File.separator + "fetchCommentAddedInANote.json");
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

			String noteId = "5dd3e573e8f6c23c67a8d1a7";

			RequestSpecification spec = requestHeadersSpecForPublicApis(headerParams);
			Response resp = RestOperationUtils.get(NOTES + "/" + noteId + "/" + "highlights", spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONArray("entries").length(), 4, "Verify Highlights length");
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}

}
