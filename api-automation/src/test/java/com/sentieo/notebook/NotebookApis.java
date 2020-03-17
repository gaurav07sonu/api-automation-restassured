package com.sentieo.notebook;

import static com.sentieo.constants.Constants.*;

import java.io.File;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
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
import com.sentieo.heartbeat.Team;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;
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
	String URI = null;
	static String tagName = "";
	static String note_id = "";
	static String ticker = "";
	static String starNoteID = "";
	static String file_id = "";
	static String private_note_id="";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
	}

	@BeforeSuite(alwaysRun = true)
	public void setup() throws Exception {
		URI = USER_APP_URL + LOGIN_URL;
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("result").getString("temp_id"), tempId,
						"Verify temp id");
				verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createPrivateNote.json");
				verify.assertTrue(respJson.getJSONObject("result").getString("temp_id").equalsIgnoreCase(tempId),
						"Temp id should not be blank : ");
				verify.assertTrue(respJson.getJSONObject("result").getString("id") != null,
						"Note id should not be blank : ");
				if (respJson.getJSONObject("result").getString("id") != null)
					private_note_id = respJson.getJSONObject("result").getString("id");
				JSONObject noteData = getNoteDetail(note_id);
				if (noteData != null)
					verify.assertTrue(noteData.length() > 0, "Validate note details present");
				else
					verify.assertTrue(false, "Fetch note details failed");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Delete private note")
	public void deletePrivateNote() throws Exception {
		try {
			if (private_note_id == "")
				createPrivateNote();
			if (private_note_id != "") {
				// delete note
				HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
				deleteNoteParams.put("note_id", private_note_id);

				RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
				Response resp1 = RestOperationUtils.post(DELETE_NOTE, null, spec1, deleteNoteParams);
				APIResponse apiResp1 = new APIResponse(resp1);

				// validation
				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				if (apiResp1.getStatusCode() == 200) {
					JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

					verify.verifyResponseTime(resp1, 3000);
					verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
				int active = getNoteDetail(private_note_id).getJSONObject("result").getInt("active");
				verify.verifyEquals(active,0,"Verify note active status code should be zero");
				Thread.sleep(10000);	
				JSONArray noteList = getNoteList();
					boolean noteDeleted = true;
					if (noteList != null) {
						for (int i = 0; i < noteList.length(); i++) {
							if (noteList.getJSONObject(i).getString("note_id").equalsIgnoreCase(note_id)) {
								noteDeleted = false;
								verify.assertTrue(noteDeleted, "Note still present in list");
								if(active==0)
								{
									verify.assertTrue(false, "Note indexing not working");
								}
								break;
							}
						}
					}
					if (noteDeleted && noteList != null)
						verify.assertTrue(noteDeleted, "Note deleted : " + note_id);
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Note id is blank");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Email note")
	public void emailNote() throws Exception {
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				String emailId = SEND_NOTE_EMAIL;
				HashMap<String, String> emailParams = new HashMap<String, String>();
				emailParams.put("emailto", emailId);
				emailParams.put("id", note_id);

				RequestSpecification emailSpec = formParamsSpec(emailParams);
				Response emailResp = RestOperationUtils.post(EMAIL_NOTE, null, emailSpec, emailParams);
				APIResponse emailApiResp = new APIResponse(emailResp);

				verify.verifyStatusCode(emailApiResp.getStatusCode(), 200);
				if (emailApiResp.getStatusCode() == 200) {
					JSONObject emailRespJson = new JSONObject(emailApiResp.getResponseAsString());

					verify.verifyResponseTime(emailResp, 5000);
					verify.verifyEquals(emailRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(emailRespJson.getJSONObject("result").getJSONArray("allowed_addresses").get(0),
							emailId, "Verify email id");
					// verify.jsonSchemaValidation(emailResp, "notebook" + File.separator +
					// "emailNote.json");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Note id is blank");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				verify.verifyTrue(notelist.length(), "Verify note list present");
				JSONObject note;
				boolean notedata = true;
				for (int i = 0; i < notelist.length(); i++) {
					note = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(i);
					if (note == null || note.length() == 0) {
						verify.assertTrue(false, "Note data not present");
						notedata = false;
					}
				}
				if (notedata) {
					verify.assertTrue(notedata, "Data present for all notes");
				}
				if (notelist.length() > 0) {
					note_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id") != null, "Verify note id present");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("url") != null, "Verify note url present");
				}
				verify.verifyTrue(respJson.getJSONObject("result").getJSONObject("facets").length(),
						"Verify facet array in the API");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);

			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			String id = respJson.get("id").toString();

			if (id != null && apiResp.getStatusCode() == 200) {
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

				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				if (apiResp1.getStatusCode() == 200) {
					JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

					verify.verifyResponseTime(resp1, 3000);
					verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(respJson1.getJSONArray("result").getJSONObject(0).getString("file_id"), id);
					verify.verifyEquals(
							respJson1.getJSONArray("result").getJSONObject(0).getJSONArray("tickers").getString(0),
							tickers.get(0));
//					double timestamp = respJson1.getJSONArray("result").getJSONObject(0).getDouble("timestamp");
//					CommonUtil util = new CommonUtil();
//					verify.assertTrue(util.validateTimeStampIsTodaysDate(timestamp), "Verify attachment updation date");
				//	verify.assertTrue(!respJson1.getJSONArray("result").getJSONObject(0).getString("access_token").isEmpty(), "Verify access token present");

					
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
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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
			params.put("query", "test");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				verify.verifyTrue(notelist.length(), "Verify note list present");
				JSONObject note;
				boolean notedata = true;
				for (int i = 0; i < notelist.length(); i++) {
					note = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(i);
					if (note == null || note.length() == 0) {
						verify.assertTrue(false, "Note data not present");
						notedata = false;
					}
				}
				if (notedata) {
					verify.assertTrue(notedata, "Data present for all notes");
				}
				if (notelist.length() > 0) {
					note_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id") != null, "Verify note id present");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("url") != null, "Verify note url present");
				}
				verify.verifyTrue(respJson.getJSONObject("result").getJSONObject("facets").length(),
						"Verify facet array in the API");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with ticker")
	public void searchWithTicker() throws Exception {

		try {
			List<String> tickers = new ArrayList<String>();
			String ticker = "aapl";
			tickers.add(ticker);
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				verify.verifyTrue(notelist.length(), "Verify note list present");
				JSONObject note;
				boolean notedata = true;
				boolean isTickerPresent = false;
				for (int i = 0; i < notelist.length(); i++) {
					isTickerPresent = false;
					note = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(i);
					if (note == null || note.length() == 0) {
						verify.assertTrue(false, "Note data not present");
						notedata = false;
					}
					for (int j = 0; j < note.getJSONArray("tickers").length(); j++) {
						if (note.getJSONArray("tickers").getString(j).equalsIgnoreCase(ticker)) {
							isTickerPresent = true;
						}
					}
				}
				if (notedata) {
					verify.assertTrue(notedata, "Data present for all notes");
				}
				if (notelist.length() > 0) {
					note_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id") != null, "Verify note id present");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("url") != null, "Verify note url present");
					verify.assertTrue(isTickerPresent, "Verify note coming for ticker : " + ticker);
				}
				verify.verifyTrue(respJson.getJSONObject("result").getJSONObject("facets").length(),
						"Verify facet array in the API");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Search with tag")
	public void searchWithTag() throws Exception {
		try {
			List<String> tags = new ArrayList<String>();
			String tag = "devesh";
			tags.add(tag);
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				verify.verifyTrue(notelist.length(), "Verify note list present");
				JSONObject note;
				boolean notedata = true;
				boolean isTagPresent = false;
				for (int i = 0; i < notelist.length(); i++) {
					isTagPresent = false;
					note = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(i);
					if (note == null || note.length() == 0) {
						verify.assertTrue(false, "Note data not present");
						notedata = false;
					}
					for (int j = 0; j < note.getJSONArray("tags").length(); j++) {
						if (note.getJSONArray("tags").getString(j).equalsIgnoreCase(tag)) {
							isTagPresent = true;
						}
					}
				}
				if (notedata) {
					verify.assertTrue(notedata, "Data present for all notes");
				}
				if (notelist.length() > 0) {
					note_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("note_id") != null, "Verify note id present");
					verify.assertTrue(respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
							.getString("url") != null, "Verify note url present");
					verify.assertTrue(isTagPresent, "Verify note coming for tag : " + tag);
				}
				verify.verifyTrue(respJson.getJSONObject("result").getJSONObject("facets").length(),
						"Verify facet array in the API");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// verify.verifyEquals(respJson.getJSONObject("response").getString("msg"),
			// "success", "Verify the API Message");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
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

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
					"Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Create Thesis and verify")
	public void createThesis() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing")) {

				HashMap<String, String> thesisData = new HashMap<String, String>();
				String ticker = "dfkcy";
				thesisData.put("thesis_type", "thesis");
				thesisData.put("tickers", ticker);
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

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				if (apiResp.getStatusCode() == 200) {
					verify.verifyResponseTime(resp, 5000);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(respJson.getJSONObject("result").getString("message"), "Success",
							"verify template message");
					verify.verifyEquals(respJson.getJSONObject("result").getInt("status"), 1,
							"Verify the API Response Status");
					JSONObject res = respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0);
					if (res == null || res.length() == 0) {
						verify.assertTrue(false, "response array blank");
					}
					if (res.length() > 0) {
						double timestamp = respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0)
								.getDouble("updated_at");
						CommonUtil util = new CommonUtil();
						verify.assertTrue(util.validateTimeStampIsTodaysDate(timestamp), "Verify thesis creation date");
						verify.assertTrue(respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0)
								.getString("note_id") != null, "Thesis id should not be blank");
						verify.verifyEquals(respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0)
								.getString("entity_type"), "Thesis", "note type");
						verify.verifyEquals(respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0)
								.getJSONObject("ticker_details").getJSONObject(ticker).getString("label").toLowerCase(),
								ticker, "verify ticker");
					}
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP,
						"We are not supporting thesis creation on : " + USER_APP_URL);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Add Note Tag")
	public void addNoteTag() throws Exception {
		try {
			if (note_id.isEmpty()) {
				fetchNoteAllList();
			}
			if (!note_id.isEmpty()) {
				tagName = "tag" + new Date().getTime();
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("id", note_id);
				params.put("field", "tag");
				params.put("action", "add");
				params.put("term", tagName);

				RequestSpecification tagSpec = formParamsSpec(params);
				Response tagResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, tagSpec, params);
				APIResponse tagApiResp = new APIResponse(tagResp);

				verify.verifyStatusCode(tagApiResp.getStatusCode(), 200);
				verify.verifyResponseTime(tagResp, 5000);
				if (tagApiResp.getStatusCode() == 200) {
					JSONObject tagRespJson = new JSONObject(tagApiResp.getResponseAsString());

					verify.verifyEquals(tagRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(tagRespJson.getBoolean("result"), true, "Verify the Result Status");
					verify.jsonSchemaValidation(tagResp, "notebook" + File.separator + "addNoteTag.json");

					JSONArray tags = getNoteDetail(note_id).getJSONObject("result").getJSONArray("userTags");
					boolean tagadded = false;
					for (int i = 0; i < tags.length(); i++) {
						if (tags.getString(i).equalsIgnoreCase(tagName)) {
							tagadded = true;
							verify.assertTrue(tagadded, "tag added in note successfully");
							break;
						}
					}
					if (!tagadded) {
						verify.assertTrue(tagadded, "tag not added in note");
					}
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api fail, note id not present");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Remove Note Tag")
	public void removeNoteTag() throws Exception {
		try {
			// add tag
			if (tagName == "")
				addNoteTag();

			if (tagName != "" && note_id != "") {
				// remove tag
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("id", note_id);
				params.put("field", "tag");
				params.put("action", "remove");
				params.put("term", tagName);

				RequestSpecification removeTagSpec = formParamsSpec(params);
				Response tagResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, removeTagSpec, params);
				APIResponse tagApiResp = new APIResponse(tagResp);

				verify.verifyStatusCode(tagApiResp.getStatusCode(), 200);
				if(tagApiResp.getStatusCode()==200) {
				JSONObject tagRespJson = new JSONObject(tagApiResp.getResponseAsString());
				verify.verifyResponseTime(tagResp, 5000);

				verify.verifyEquals(tagRespJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.jsonSchemaValidation(tagResp, "notebook" + File.separator + "removeNoteTag.json");
				JSONArray tags = getNoteDetail(note_id).getJSONObject("result").getJSONArray("userTags");
				boolean tagadded = true;
				for (int i = 0; i < tags.length(); i++) {
					if (tags.getString(i).equalsIgnoreCase(tagName)) {
						tagadded = false;
						verify.assertTrue(tagadded, "tag still present in note");
						break;
					}
				}
				if (tagadded) {
					verify.assertTrue(tagadded, "tag successfully removed from note");
				}
			} 
		}else {
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Tag add api failed, tag name is blank " + tagName);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Add Note Ticker")
	public void addNoteTicker() throws Exception {
		try {
			if (note_id.isEmpty()) {
				fetchNoteAllList();
			}
			if (!note_id.isEmpty()) {
				ticker = "AAPL";
				// add ticker
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("id", note_id);
				params.put("field", "ticker");
				params.put("action", "add");
				params.put("term", ticker);

				RequestSpecification tickerSpec = formParamsSpec(params);
				Response tickerResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, tickerSpec, params);
				APIResponse tickerApiResp = new APIResponse(tickerResp);

				verify.verifyStatusCode(tickerApiResp.getStatusCode(), 200);
				verify.verifyResponseTime(tickerResp, 5000);
				if (tickerApiResp.getStatusCode() == 200) {
					JSONObject tickerRespJson = new JSONObject(tickerApiResp.getResponseAsString());

					verify.verifyEquals(tickerRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(tickerRespJson.getBoolean("result"), true, "Verify the Result Status");
					verify.jsonSchemaValidation(tickerResp, "notebook" + File.separator + "addNoteTicker.json");

					JSONArray tickers = getNoteDetail(note_id).getJSONObject("result").getJSONArray("userTickers");
					boolean tickeradded = false;
					for (int i = 0; i < tickers.length(); i++) {
						if (tickers.getString(i).equalsIgnoreCase(ticker) && tickers.length() > 0) {
							tickeradded = true;
							verify.assertTrue(tickeradded, "ticker added in note successfully");
							break;
						}
					}
					if (!tickeradded) {
						verify.assertTrue(tickeradded, "ticker not added in note");
					}
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api failed, note id not present");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Remove Note Ticker")
	public void removeNoteTicker() throws Exception {
		try {
			if (note_id.isEmpty() && ticker == "") {
				addNoteTicker();
			}
			if (!(note_id.isEmpty() && ticker == "")) {
				// add ticker

				HashMap<String, String> removeTickerParams = new HashMap<String, String>();
				removeTickerParams.put("id", note_id);
				removeTickerParams.put("field", "ticker");
				removeTickerParams.put("action", "remove");
				removeTickerParams.put("term", ticker);

				RequestSpecification removeSpec = formParamsSpec(removeTickerParams);
				Response removeResp = RestOperationUtils.post(UPDATE_TAG_TICKER, null, removeSpec, removeTickerParams);
				APIResponse removeApiResp = new APIResponse(removeResp);

				verify.verifyStatusCode(removeApiResp.getStatusCode(), 200);
				verify.verifyResponseTime(removeResp, 5000);
				if (removeApiResp.getStatusCode() == 200) {
					JSONObject removeRespJson = new JSONObject(removeApiResp.getResponseAsString());

					verify.verifyEquals(removeRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(removeRespJson.getBoolean("result"), true, "Verify the Result Status");
					verify.jsonSchemaValidation(removeResp, "notebook" + File.separator + "addNoteTicker.json");

					JSONArray tickers = getNoteDetail(note_id).getJSONObject("result").getJSONArray("userTickers");
					boolean tickeradded = true;
					for (int i = 0; i < tickers.length(); i++) {
						if (tickers.getString(i).equalsIgnoreCase(ticker)) {
							tickeradded = false;
							verify.assertTrue(tickeradded, "ticker added in note successfully");
							break;
						}
					}
					if (tickeradded) {
						verify.assertTrue(tickeradded, "ticker not added in note");
					}
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "create Thesis template")
	public void createThesisTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing")) {
				HashMap<String, String> templateDict = new HashMap<String, String>();
				String templateName = "autothesis" + new Date();
				templateDict.put("name", templateName);
				String templateDictJson = jsonUtils.toJson(templateDict);

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("create_default_children", Boolean.TRUE.toString());
				params.put("action", "create_thesis_template");
				params.put("template_dictionary", templateDictJson);

				RequestSpecification spec = formParamsSpec(params);
				Response resp = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec, params);
				APIResponse apiResp = new APIResponse(resp);

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyTrue(
							respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0).getString("id"),
							"verify template id should be blank");
					verify.verifyEquals(
							respJson.getJSONObject("result").getJSONArray("res").getJSONObject(0).getString("name"),
							templateName, "Verify template name");
					verify.verifyEquals(respJson.getJSONObject("result").getString("message"), "Success",
							"verify template message");
				
				// delete template
				JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
				String id = String.valueOf(res.get("id"));
				if(!id.isEmpty()) {
				HashMap<String, String> deleteParams = new HashMap<String, String>();
				deleteParams.put("template_id", id);
				deleteParams.put("action", "delete_template");

				RequestSpecification spec1 = formParamsSpec(deleteParams);
				RestOperationUtils.post(TEMPLATE_ENTITY, null, spec1, deleteParams);
				}else {
					verify.assertTrue(false, "Template is missing, can not perform delete");
				}
			} 
			}else {
				ExtentTestManager.getTest().log(LogStatus.SKIP,
						"We are not supporting thesis template on : " + USER_APP_URL);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "create tab template")
	public void createTabTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing")) {

				HashMap<String, String> templateDict = new HashMap<String, String>();
				templateDict.put("name", "autoTab" + new Date().getTime());

				String templateDictJson = jsonUtils.toJson(templateDict);

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("create_default_children", Boolean.TRUE.toString());
				params.put("action", "create_tab_template");
				params.put("template_dictionary", templateDictJson);

				RequestSpecification spec = formParamsSpec(params);
				Response resp = RestOperationUtils.post(TEMPLATE_ENTITY, null, spec, params);
				APIResponse apiResp = new APIResponse(resp);

				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createTabTemplate.json");

				// delete template
				JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
				String id = String.valueOf(res.get("id"));

				HashMap<String, String> deleteParams = new HashMap<String, String>();
				deleteParams.put("template_id", id);
				deleteParams.put("action", "delete_template");

				RequestSpecification spec1 = formParamsSpec(deleteParams);
				RestOperationUtils.post(TEMPLATE_ENTITY, null, spec1, deleteParams);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Delete thesis template")
	public void deleteThesisTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing")) {

				HashMap<String, String> templateDict = new HashMap<String, String>();
				templateDict.put("name", "autothesis" + new Date());

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

				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

				verify.verifyResponseTime(resp1, 5000);
				verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deleteThesisTemplate.json");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Star note")
	public void starNote() throws Exception {
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("noteid", note_id);

				RequestSpecification starSpec = formParamsSpec(params);
				Response starResp = RestOperationUtils.post(STAR_NOTE, null, starSpec, params);
				APIResponse starApiResp = new APIResponse(starResp);
				verify.verifyStatusCode(starApiResp.getStatusCode(), 200);
		
				if (starApiResp.getStatusCode() == 200) {
					JSONObject starRespJson = new JSONObject(starApiResp.getResponseAsString());

					verify.verifyResponseTime(starResp, 5000);
					verify.verifyEquals(starRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(starRespJson.get("result"), "starred", "Verify the API result");
					verify.jsonSchemaValidation(starResp, "notebook" + File.separator + "starNote.json");
					String notetemp = (String) starRespJson.get("result");
					JSONArray noteData = getNoteDetail(note_id).getJSONObject("result").getJSONArray("stars");
					verify.assertTrue(noteData.length()!=0 && noteData != null, "Star data present in note details : " + note_id);
					if(notetemp.equalsIgnoreCase("starred") && noteData!=null && noteData.length()!=0)
						starNoteID=note_id;
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api fail, note id not present");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "un-Star note")
	public void unstarNote() throws Exception {
		try {
			if (starNoteID == "")
				starNote();
			if (starNoteID != "") {
				Thread.sleep(5000);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("noteid", starNoteID);
				RequestSpecification unstarSpec = formParamsSpec(params);
				Response unstarResp = RestOperationUtils.post(UNSTAR_NOTE, null, unstarSpec, params);
				APIResponse unstarApiResp = new APIResponse(unstarResp);
				verify.verifyStatusCode(unstarApiResp.getStatusCode(), 200);
				if (unstarApiResp.getStatusCode() == 200) {
					JSONObject unstarRespJson = new JSONObject(unstarApiResp.getResponseAsString());
					verify.verifyResponseTime(unstarResp, 5000);
					verify.verifyEquals(unstarRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(unstarRespJson.get("result"), "unstarred", "Verify the API result");
					verify.jsonSchemaValidation(unstarResp, "notebook" + File.separator + "unstarNote.json");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Star note failed");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "Add attachment to a note")
	public void addAttachmentToANote() throws Exception {
		try {

			if (note_id == "")
				setNoteId();
			if (note_id != "") {
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
				verify.verifyStatusCode(uploadApiResp.getStatusCode(), 200);
				JSONObject uploadRespJson = new JSONObject(uploadApiResp.getResponseAsString());
				String id = uploadRespJson.get("id").toString();
				if (uploadApiResp.getStatusCode() == 200 && id != null) {
					verify.verifyResponseTime(uploadResp, 5000);
					verify.verifyEquals(uploadRespJson.get("success"), true, "Verify the API Response Status");
					verify.verifyEquals(folderName + uploadRespJson.get("filename"), fileName,
							"Verify the API Response Status");
					HashMap<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("file_id", id);
					dataMap.put("filename", fileName);
					dataMap.put("data_id", "1");
					dataMap.put("content_type",
							"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
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

					verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
					if (apiResp1.getStatusCode() == 200) {
						JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

						verify.verifyResponseTime(resp1, 3000);
						verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
								"Verify the API Response Status");
						verify.verifyEquals(respJson1.getJSONArray("result").getJSONObject(0).getString("file_id"), id,
								"Verify file id on note");
//						double timestamp = respJson1.getJSONArray("result").getJSONObject(0)
//								.getDouble("access_token_ttl");
//						CommonUtil util = new CommonUtil();
//						verify.assertTrue(util.validateTimeStampIsTodaysDate(timestamp), "Verify updation date");
						verify.assertTrue(!respJson1.getJSONArray("result").getJSONObject(0).getString("access_token").isEmpty(), "Verify access token present");
						verify.verifyEquals(respJson1.getJSONArray("result").getJSONObject(0).getString("note_id"),
								note_id,"verify note id");
						JSONArray attachment = getNoteDetail(note_id).getJSONObject("result")
								.getJSONArray("attachments");
						boolean isAttachmentPresent = false;
						if (attachment.length() > 0)
							for (int i = 0; i < attachment.length(); i++) {
								if (attachment.getJSONObject(i).getString("file_id").equalsIgnoreCase(id)) {
									isAttachmentPresent = true;
									verify.assertTrue(isAttachmentPresent, "verify attachment present in note");
									file_id = id;
									break;
								}
							}
						if (!isAttachmentPresent)
							verify.assertTrue(isAttachmentPresent, "attachment not added to note");
					}
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "Remove attachment from a note")
	public void removeAttachmentFromANote() throws Exception {
		try {

			if (file_id == "")
				addAttachmentToANote();
			if (file_id != "") {
				// Removing attachment
				HashMap<String, String> dataMapForRemove = new HashMap<String, String>();
				dataMapForRemove.put("file_id", file_id);
				dataMapForRemove.put("note_id", note_id);

				RequestSpecification spec2 = formParamsSpec(dataMapForRemove);
				Response resp2 = RestOperationUtils.post(REMOVE_ATTACHMENT, null, spec2, dataMapForRemove);
				APIResponse apiResp2 = new APIResponse(resp2);

				verify.verifyStatusCode(apiResp2.getStatusCode(), 200);
				if (apiResp2.getStatusCode() == 200) {
					JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());
					verify.verifyResponseTime(resp2, 3000);
					verify.verifyEquals(respJson2.getJSONObject("response").get("status"), true,
							"Verify the API Response Status");
					// verify.assertTrue(respJson2.getBoolean("success"), "Verify result response");
					JSONArray attachment = getNoteDetail(note_id).getJSONObject("result").getJSONArray("attachments");
					boolean isAttachmentPresent = true;
					if (attachment.length() > 0)
						for (int i = 0; i < attachment.length(); i++) {
							if (attachment.getJSONObject(i).getString("file_id").equalsIgnoreCase(file_id)) {
								isAttachmentPresent = false;
								verify.assertTrue(isAttachmentPresent, "verify attachment deleted from note");
								break;
							}
						}
					if (isAttachmentPresent)
						verify.assertTrue(isAttachmentPresent, "verify attachment deleted from note");
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "add users")
	public void addUserComments() throws Exception {
		try {
			if (note_id.isEmpty()) {
				fetchNoteAllList();
			}
			if (!note_id.isEmpty()) {

				// add comments **************
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

				verify.verifyStatusCode(commentApiResp.getStatusCode(), 200);
				verify.verifyResponseTime(commentResp, 5000);
				if (commentApiResp.getStatusCode() == 200) {
					JSONObject commentRespJson = new JSONObject(commentApiResp.getResponseAsString());

					verify.verifyEquals(commentRespJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(commentRespJson.getJSONObject("result").getString("message"),
							"Comment successfully added!");
					verify.assertTrue(!commentRespJson.getJSONObject("result").getString("comment_id").isEmpty(),
							"Verify comment id should not be null");

					double timestamp = commentRespJson.getJSONObject("result").getDouble("last_updated_at");
					CommonUtil utils = new CommonUtil();
					verify.assertTrue(utils.validateTimeStampIsTodaysDate(timestamp),
							"Validate comment date should be todays");
					verify.jsonSchemaValidation(commentResp, "notebook" + File.separator + "addUserComment.json");

				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api fail, note id not present");
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "delete user comments")
	public void deleteUserComments() throws Exception {
		try {

			// note creation
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

			// delete comment
			String json1 = jsonUtils.toJson(commentDictParams1);

			HashMap<String, String> params1 = new HashMap<String, String>();
			params1.put("action", "delete");
			params1.put("comment_dict", json1);

			RequestSpecification spec1 = formParamsSpec(params1);
			Response resp1 = RestOperationUtils.post(USER_COMMENTS, null, spec1, params1);
			APIResponse apiResp1 = new APIResponse(resp1);

			verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
			verify.verifyResponseTime(resp1, 5000);
			JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

			verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deleteUserComment.json");

			// delete note
			HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
			deleteNoteParams.put("note_id", note_id);

			RequestSpecification deleteSpec = formParamsSpec(deleteNoteParams);
			RestOperationUtils.post(DELETE_NOTE, null, deleteSpec, deleteNoteParams);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "Edit user comments")
	public void editUserComments() throws Exception {
		try {
			// note creation
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

			verify.verifyStatusCode(apiResp2.getStatusCode(), 200);
			verify.verifyResponseTime(resp2, 5000);
			JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());

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
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Fetch note data")
	public void fetchNoteHtml() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				parameters.put("id", note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(FETCH_NOTE_HTML, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
					verify.assertTrue(respJson.getJSONObject("result").getString("id").equalsIgnoreCase(note_id),
							"Note id should be equal to fetched note id : ");
					verify.assertTrue(respJson.getJSONObject("result").getString("url") != null,
							"Url should not be blank : ");
//			 verify.jsonSchemaValidation(resp, "notebook" + File.separator
//						 +"get_note_html.json");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api fail, note id not present");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "This will load the L1(filter section)")
	public void fetchNoteFacetHtml() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("notemode", "all");
			parameters.put("type", "all");
			parameters.put("all_contacts", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(FETCH_NOTE_FACET_AND_HTML, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
				JSONArray ticker_term = respJson.getJSONObject("result").getJSONObject("facets")
						.getJSONObject("tickers").getJSONArray("terms");
				if (ticker_term.length() == 0 || ticker_term == null)
					verify.assertTrue(false, "ticker_term array is empty");
				JSONObject user_fields = respJson.getJSONObject("result").getJSONObject("facets");
				if (user_fields.length() == 0 || user_fields == null)
					verify.assertTrue(false, "user_fields array is empty");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Fetch user notebook data")
	public void fetchNotebookData() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("note_type", "true");
			parameters.put("user_template", "true");
			parameters.put("user_groups", "true");
			parameters.put("user_fields", "true");
			parameters.put("user_email", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(FETCH_NOTE_DATA, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
				JSONArray noteType = respJson.getJSONObject("result").getJSONObject("note_type")
						.getJSONArray("static_note_type_list");
				if (noteType.length() == 0 || noteType == null)
					verify.assertTrue(false, "note type array is empty : ");
				JSONArray user_template = respJson.getJSONObject("result").getJSONArray("user_template");
				if (user_template.length() == 0 || user_template == null)
					verify.assertTrue(false, "user template array is empty : ");
				JSONArray user_groups = respJson.getJSONObject("result").getJSONArray("user_groups");
				if (user_groups.length() == 0 || user_groups == null)
					verify.assertTrue(false, "user group array is empty : ");
				JSONObject user_fields = respJson.getJSONObject("result").getJSONObject("user_fields");
				if (user_fields.length() == 0 || user_fields == null)
					verify.assertTrue(false, "user fields array is empty");
				JSONArray user_email = respJson.getJSONObject("result").getJSONArray("user_email");
				if (user_email.length() == 0 || user_email == null)
					verify.assertTrue(false, "user email array is empty : ");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Fetch user note history")
	public void fetchNoteHistory() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			if (note_id.isEmpty()) {
				setNoteId();
			}
			if (!note_id.isEmpty()) {
				parameters.put("id", note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(FETCH_NOTE_HISTORY, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
					verify.assertTrue(respJson.getJSONObject("result").getInt("total_versions") >= 1,
							"Total version should be greater than zero : ");
					JSONArray historyData = respJson.getJSONObject("result").getJSONArray("history");
					if (historyData.length() == 0 || historyData == null)
						verify.assertTrue(false, "history array is empty : ");
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	

	@Test(groups = { "sanity" }, description = "This will fetch notebook settings")
	public void fetchNoteSettings() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(FETCH_NOTE_SETTING, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");

				if (respJson.getJSONObject("result") == null || respJson.getJSONObject("result").length() == 0) {
					verify.assertTrue(false, "Result coming blank");
				}

				JSONObject evernote = respJson.getJSONObject("result").getJSONObject("evernote_sync_data");
				if (evernote == null || evernote.length() == 0) {
					verify.assertTrue(false, "Verify evernote data present");
				}

				JSONObject onenote = respJson.getJSONObject("result").getJSONObject("onenote_sync_data");
				if (onenote == null || onenote.length() == 0) {
					verify.assertTrue(false, "Verify onenote data present");
				}
				verify.assertTrue(respJson.getJSONObject("result").get("note_publishing") != null,
						"verify note_publishing key present");

				verify.jsonSchemaValidation(resp, "notebook" + File.separator + "notebookSetting.json");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Verify fetch note version api")
	public void fetch_note_version() throws CoreCommonException {
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				int version = getNoteDetail(note_id).getJSONObject("result").getInt("version");
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("id", note_id);
				parameters.put("version", Integer.toString(version));
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(FETCH_NOTE_VERSION, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
					verify.assertTrue(respJson.getJSONObject("result").get("conflict") != null,
							"verify conflict key present");
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Verify fetch note lock status")
	public void fetch_note_lock_status() throws CoreCommonException {
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				// int version =
				// getNoteDetail(note_id).getJSONObject("result").getInt("version");
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("noteid", note_id);
				parameters.put("set_lock", "1");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(FETCH_NOTE_LOCK_STATUS, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
					verify.assertTrue(respJson.getJSONObject("result").get("locked_by") != null,
							"verify locked_by key present");
					verify.assertTrue(respJson.getJSONObject("result").get("locked") != null,
							"verify locked key present");
					verify.verifyEquals(respJson.getJSONObject("result").get("noteid"), note_id, "verify note id");
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = { "sanity" }, description = "Verify highight present or not")
	public void fetch_note() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("docid", "5e68de142e808522f1a39820");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(FETCH_NOTE, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
				if (respJson.getJSONArray("result").getJSONObject(0).getBoolean("note_highlight") == false) {
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getJSONArray("highlight_list")
							.length() == 0, "Verify Highlight list should be blank");
				} else {
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getJSONArray("highlight_list")
							.length() > 0, "Verify Highlight list should not be blank");
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = { "sanity" }, description = "Verify thesis fields")
	public void fetch_thesis_fields() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(FETCH_THESIS_FIELDS, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status : ");
				String thesisFlag="";
				try {
					thesisFlag = getUserPortFolio().getJSONObject("result").getJSONObject("other_flags").getString("notebook_thesis");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(!thesisFlag.isEmpty()) {
				JSONArray fields = respJson.getJSONObject("result").getJSONArray("fields");
				if(fields == null || fields.length()==0)
					verify.assertTrue(false, "Verify fields should be present ");
				else 
					verify.assertTrue(true, "Verify fields present ");
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	public JSONObject getNoteDetail(String noteID) throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("id", noteID);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(FETCH_NOTE_HTML, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
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

	public void setNoteId() throws CoreCommonException {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			if (apiResp.getStatusCode() == 200) {
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				Random random = new Random();
				int index;
				if (notelist.length() > 0) {
					do {
						index = random.nextInt(notelist.length());
						if (respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(index)
								.getString("thesis_id").isEmpty()) {
							note_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(index)
									.getString("note_id");
						}
					} while (note_id.isEmpty());
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	public JSONArray getNoteList() throws CoreCommonException {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "50");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			int status = apiResp.getStatusCode();
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONArray notelist = respJson.getJSONObject("result").getJSONArray("notes");
				if (notelist.length() > 0)
					return notelist;
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
		return null;
	}
	
	public JSONObject getUserPortFolio() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("apiname", "fetch_user_portfolio_data");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(FETCH_INITIAL_LOADING, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response : ");
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

//	@AfterSuite(alwaysRun = true)
//	public void deletePrivateNote1(){
//		try {
//			if (note_id != "") {
//				// delete note
//				HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
//				deleteNoteParams.put("note_id", note_id);
//				RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
//				RestOperationUtils.post(DELETE_NOTE, null, spec1, deleteNoteParams,false);
//			}
//		} catch (JSONException | CoreCommonException je) {
//			je.printStackTrace();
//		} 
//	}
}
