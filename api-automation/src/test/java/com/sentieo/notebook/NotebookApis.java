package com.sentieo.notebook;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
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
import com.sentieo.utils.CSVReaderUtil;
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
	static String private_note_id = "";
	static String thesis_id = "";
	static String shareUser = "devesh.arora";
	static String comment_id = "";
	static JSONArray noteList_Typed = null;
	static String HighlightNoteID = "";
	public static String doc_id_sentieoDrive = "";
	public static String doc_id_noncsv_sentieoDrive = "";

	static String[][] tickers;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
	}

	@BeforeClass(alwaysRun = true)
	public void setURI() {
		URI = USER_APP_URL;
	}
	
	@BeforeClass(alwaysRun = true)
	public void setTickers() {
		tickers = CSVReaderUtil.readAllDataAtOnce("notebook" + File.separator + "autocomplete_ticker_list.csv");
	}

	@Test(groups = "sanity", priority = 0, description = "Create private note")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + SET_NOTE_HTML, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("result").getString("temp_id"), tempId, "Verify temp id");
				verify.jsonSchemaValidation(resp, "notebook" + File.separator + "createPrivateNote.json");
				verify.verifyEquals(respJson.getJSONObject("result").getString("temp_id"), tempId,
						"Temp id should not be blank");
				verify.assertTrue(respJson.getJSONObject("result").getString("id") != null,
						"Note id should not be blank");
				if (respJson.getJSONObject("result").getString("id") != null) {
					note_id = respJson.getJSONObject("result").getString("id");
					private_note_id = respJson.getJSONObject("result").getString("id");
					JSONObject noteData = getNoteDetail(private_note_id);
					if (noteData != null)
						verify.assertTrue(noteData.length() > 0, "Validate note details present");
					else
						verify.assertTrue(false, "Fetch note details failed");

					// Validate indexing
					Thread.sleep(10000);
					boolean isNotePresent = false;
					JSONArray notelist_new = getNoteList();
					for (int i = 0; i < notelist_new.length(); i++) {
						if (notelist_new.getJSONObject(i).getString("id").equalsIgnoreCase(private_note_id)) {
							isNotePresent = true;
							break;
						}
					}
					verify.assertTrue(isNotePresent, "Verify note present/Check indexing");
				}
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 62, description = "Delete private note")
	public void deletePrivateNote() throws Exception {
		try {
			if (private_note_id == "")
				createPrivateNote();
			if (private_note_id != "") {
				// delete note
				HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
				deleteNoteParams.put("note_id", private_note_id);

				RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
				Response resp1 = RestOperationUtils.post(USER_APP_URL + DELETE_NOTE, null, spec1, deleteNoteParams);
				APIResponse apiResp1 = new APIResponse(resp1);

				// validation
				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				if (apiResp1.getStatusCode() == 200) {
					JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

					verify.verifyResponseTime(resp1, 3000);
					verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					int active = getNoteDetail(private_note_id).getJSONObject("result").getInt("active");
					verify.verifyEquals(active, 0, "Verify note active status code should be zero");
					Thread.sleep(10000);
					JSONArray noteList = getNoteList();
					boolean noteDeleted = true;
					if (noteList != null) {
						for (int i = 0; i < noteList.length(); i++) {
							if (noteList.getJSONObject(i).getString("note_id").equalsIgnoreCase(private_note_id)) {
								noteDeleted = false;
								verify.assertTrue(noteDeleted, "Note still present in list");
								if (active == 0) {
									verify.assertTrue(false, "Note indexing not working");
								}
								break;
							}
						}
					}
					if (noteDeleted && noteList != null)
						verify.assertTrue(noteDeleted, "Note deleted : " + private_note_id);
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

	@Test(groups = "sanity", priority = 1, description = "Email note")
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
				Response emailResp = RestOperationUtils.post(USER_APP_URL + EMAIL_NOTE, null, emailSpec, emailParams);
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

	@Test(groups = "sanity", priority = 2, description = "Fetch all notes")
	public void fetchNoteAllList() throws Exception {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "5");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
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

	@Test(groups = "sanity", priority = 3, description = "Upload Note", enabled = true)
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
			Response resp = RestOperationUtils.post(USER_APP_URL + UPLOAD_FILE, null, spec, params);
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
				Response resp1 = RestOperationUtils.post(USER_APP_URL + CREATE_ATTACHMENT_NOTE, null, spec1,
						attachParams);
				APIResponse apiResp1 = new APIResponse(resp1);

				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				if (apiResp1.getStatusCode() == 200) {
					JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

					verify.verifyResponseTime(resp1, 3000);
					verify.verifyEquals(respJson1.getJSONObject("response").get("status"), true,
							"Verify the API Response Status");
					verify.verifyEquals(respJson1.getJSONArray("result").getJSONObject(0).getString("file_id"),id,"verify file id");

					//temporary disabled below validation
//					if(respJson1.getJSONArray("result").getJSONObject(0).getJSONArray("tickers").length()>0)
//					verify.verifyEquals(
//							respJson1.getJSONArray("result").getJSONObject(0).getJSONArray("tickers").getString(0),
//							tickers.get(0),"verify ticker");
//					else
//						verify.assertTrue(respJson1.getJSONArray("result").getJSONObject(0).getJSONArray("tickers").length()>0, "ticker array is empty");

					
//					double timestamp = respJson1.getJSONArray("result").getJSONObject(0).getDouble("timestamp");
//					CommonUtil util = new CommonUtil();
//					verify.assertTrue(util.validateTimeStampIsTodaysDate(timestamp), "Verify attachment updation date");
					// verify.assertTrue(!respJson1.getJSONArray("result").getJSONObject(0).getString("access_token").isEmpty(),
					// "Verify access token present");

					// Delete note created
					String noteIdToBeDeleted = respJson1.getJSONObject("response").getJSONArray("note_id_arr").get(0)
							.toString();
					HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
					deleteNoteParams.put("note_id", noteIdToBeDeleted);

					RequestSpecification spec2 = formParamsSpec(deleteNoteParams);
					Response resp2 = RestOperationUtils.post(USER_APP_URL + DELETE_NOTE, null, spec2, deleteNoteParams);
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

	@Test(groups = "sanity", priority = 4, description = "Search with text")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
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

	@Test(groups = "sanity", priority = 5, description = "Search with ticker")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
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

	@Test(groups = "sanity", priority = 6, description = "Search with tag")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
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

	@Test(groups = "sanity", priority = 7, description = "Search with author")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 8, description = "Search with label")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 9, description = "Search with sectors")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
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

	@Test(groups = "sanity", priority = 10, description = "Search with region", enabled = true)
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 11, description = "Search with note category")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 12, description = "Search with noteType : email", enabled = true)
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 13, description = "Search with noteType : bookmark")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 14, description = "Search with noteType : clipped")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 15, description = "Search with noteType : attachment")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 16, description = "Search with noteType : plotter")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 17, description = "Search with noteType : highlight")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			if (respJson.getJSONObject("result").getJSONArray("notes").length() > 0
					&& respJson.getJSONObject("result").getJSONArray("notes") != null)
				HighlightNoteID = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
						.getString("id");

			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 18, description = "Search with noteType : thesis")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			if (respJson.getJSONObject("result").getJSONArray("notes").length() > 0) {
				thesis_id = respJson.getJSONObject("result").getJSONArray("notes").getJSONObject(0)
						.getString("thesis_id");
			}
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 19, description = "Search with noteType : typed")
	public void searchWithNoteTypeAsTyped() throws Exception {
		try {
			List<String> type = new ArrayList<String>();
			type.add("notebook");
			String typeJson = jsonUtils.toJson(type);

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "50");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");
			params.put("typ", typeJson);

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(),
						"Verify notes present");
				noteList_Typed = respJson.getJSONObject("result").getJSONArray("notes");
				// TODO: schema validation to be added
				// verify.jsonSchemaValidation(resp);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 20, description = "Search with noteType : charts and tables")
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
			APIResponse apiResp = new APIResponse(resp);

			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());

			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyTrue(respJson.getJSONObject("result").getJSONArray("notes").length(), "Verify notes present");
			// TODO: schema validation to be added
			// verify.jsonSchemaValidation(resp);
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 21, description = "Create Thesis and verify")
	public void createThesis() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing") || URI.contains("staging")) {

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
				Response resp = RestOperationUtils.post(USER_APP_URL + THESIS_ENTITY, null, spec, thesisParams);
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

	@Test(groups = "sanity", priority = 22, description = "Add Note Tag")
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
				Response tagResp = RestOperationUtils.post(USER_APP_URL + UPDATE_TAG_TICKER, null, tagSpec, params);
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

	@Test(groups = "sanity", priority = 23, description = "Remove Note Tag")
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
				Response tagResp = RestOperationUtils.post(USER_APP_URL + UPDATE_TAG_TICKER, null, removeTagSpec,
						params);
				APIResponse tagApiResp = new APIResponse(tagResp);

				verify.verifyStatusCode(tagApiResp.getStatusCode(), 200);
				if (tagApiResp.getStatusCode() == 200) {
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
			} else {
				ExtentTestManager.getTest().log(LogStatus.FAIL, "Tag add api failed, tag name is blank " + tagName);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 24, description = "Add Note Ticker")
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
				Response tickerResp = RestOperationUtils.post(USER_APP_URL + UPDATE_TAG_TICKER, null, tickerSpec,
						params);
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

	@Test(groups = "sanity", priority = 25, description = "Remove Note Ticker")
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
				Response removeResp = RestOperationUtils.post(USER_APP_URL + UPDATE_TAG_TICKER, null, removeSpec,
						removeTickerParams);
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
							verify.assertTrue(tickeradded, "ticker note remove from note");
							break;
						}
					}
					if (tickeradded) {
						verify.assertTrue(tickeradded, "ticker removed from note");
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

	@Test(groups = "sanity", priority = 26, description = "create Thesis template")
	public void createThesisTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing") || URI.contains("staging")) {
				HashMap<String, String> templateDict = new HashMap<String, String>();
				String templateName = "autothesis" + new Date();
				templateDict.put("name", templateName);
				String templateDictJson = jsonUtils.toJson(templateDict);

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("create_default_children", Boolean.TRUE.toString());
				params.put("action", "create_thesis_template");
				params.put("template_dictionary", templateDictJson);

				RequestSpecification spec = formParamsSpec(params);
				Response resp = RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec, params);
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
					if (!id.isEmpty()) {
						HashMap<String, String> deleteParams = new HashMap<String, String>();
						deleteParams.put("template_id", id);
						deleteParams.put("action", "delete_template");

						RequestSpecification spec1 = formParamsSpec(deleteParams);
						RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec1, deleteParams);
					} else {
						verify.assertTrue(false, "Template is missing, can not perform delete");
					}
				}
			} else {
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

	@Test(groups = "sanity", priority = 27, description = "create tab template")
	public void createTabTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing") || URI.contains("staging")) {

				HashMap<String, String> templateDict = new HashMap<String, String>();
				templateDict.put("name", "autoTab" + new Date().getTime());

				String templateDictJson = jsonUtils.toJson(templateDict);

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("create_default_children", Boolean.TRUE.toString());
				params.put("action", "create_tab_template");
				params.put("template_dictionary", templateDictJson);

				RequestSpecification spec = formParamsSpec(params);
				Response resp = RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec, params);
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
				RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec1, deleteParams);
			}
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 28, description = "Delete thesis template")
	public void deleteThesisTemplate() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("notebook") || URI.contains("app2") || URI.contains("testing") || URI.contains("staging")) {

				HashMap<String, String> templateDict = new HashMap<String, String>();
				templateDict.put("name", "autothesis" + new Date());

				String templateDictJson = jsonUtils.toJson(templateDict);

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("create_default_children", Boolean.TRUE.toString());
				params.put("action", "create_thesis_template");
				params.put("template_dictionary", templateDictJson);

				RequestSpecification spec = formParamsSpec(params);
				Response resp = RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec, params);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				JSONObject res = (JSONObject) respJson.getJSONObject("result").getJSONArray("res").get(0);
				String id = String.valueOf(res.get("id"));

				HashMap<String, String> deleteParams = new HashMap<String, String>();
				deleteParams.put("template_id", id);
				deleteParams.put("action", "delete_template");

				RequestSpecification spec1 = formParamsSpec(deleteParams);
				Response resp1 = RestOperationUtils.post(USER_APP_URL + TEMPLATE_ENTITY, null, spec1, deleteParams);
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

	@Test(groups = "sanity", priority = 29, description = "Star note")
	public void starNote() throws Exception {
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("noteid", note_id);

				RequestSpecification starSpec = formParamsSpec(params);
				Response starResp = RestOperationUtils.post(USER_APP_URL + STAR_NOTE, null, starSpec, params);
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
					verify.assertTrue(noteData.length() != 0 && noteData != null,
							"Star data present in note details : " + note_id);
					if (notetemp.equalsIgnoreCase("starred") && noteData != null && noteData.length() != 0)
						starNoteID = note_id;
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

	@SuppressWarnings("null")
	@Test(groups = "sanity", priority = 30, description = "un-Star note")
	public void unstarNote() throws Exception {
		try {
			if (!starNoteID.isEmpty()) {
				JSONArray noteData = getNoteDetail(starNoteID).getJSONObject("result").getJSONArray("stars");
				if (noteData == null && noteData.length() == 0)
					starNoteID = "";
			}
			if (starNoteID.isEmpty())
				starNote();

			if (!starNoteID.isEmpty()) {
				Thread.sleep(10000);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("noteid", starNoteID);
				RequestSpecification unstarSpec = formParamsSpec(params);
				Response unstarResp = RestOperationUtils.post(USER_APP_URL + UNSTAR_NOTE, null, unstarSpec, params);
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

	@Test(groups = "sanity", priority = 31, description = "Add attachment to a note")
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
				Response uploadResp = RestOperationUtils.post(USER_APP_URL + UPLOAD_FILE, null, uploadspec, params);
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
					Response resp1 = RestOperationUtils.post(USER_APP_URL + SAVE_ATTACHMENT, null, spec1, attachParams);
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
						verify.assertTrue(
								!respJson1.getJSONArray("result").getJSONObject(0).getString("access_token").isEmpty(),
								"Verify access token present");
						verify.verifyEquals(respJson1.getJSONArray("result").getJSONObject(0).getString("note_id"),
								note_id, "verify note id");
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

	@Test(groups = "sanity", priority = 32, description = "Remove attachment from a note")
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
				Response resp2 = RestOperationUtils.post(USER_APP_URL + REMOVE_ATTACHMENT, null, spec2,
						dataMapForRemove);
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

	@Test(groups = "sanity", priority = 33, description = "add users")
	public void addUserComments() throws Exception {
		try {
			if (note_id.isEmpty()) {
				setNoteId();
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
				Response commentResp = RestOperationUtils.post(USER_APP_URL + USER_COMMENTS, null, commentSpec,
						commentParams);
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
					if (!commentRespJson.getJSONObject("result").getString("comment_id").isEmpty()) {
						comment_id = commentRespJson.getJSONObject("result").getString("comment_id");
					}
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

	@Test(groups = "sanity", priority = 35, description = "delete user comments")
	public void deleteUserComments() throws Exception {
		try {
			if (comment_id.isEmpty()) {
				addUserComments();
			}
			if (!comment_id.isEmpty()) {

				HashMap<String, String> commentDictParams1 = new HashMap<String, String>();
				commentDictParams1.put("source", "note");
				commentDictParams1.put("comment_id", comment_id);

				// delete comment
				String json1 = jsonUtils.toJson(commentDictParams1);

				HashMap<String, String> params1 = new HashMap<String, String>();
				params1.put("action", "delete");
				params1.put("comment_dict", json1);

				RequestSpecification spec1 = formParamsSpec(params1);
				Response resp1 = RestOperationUtils.post(USER_APP_URL + USER_COMMENTS, null, spec1, params1);
				APIResponse apiResp1 = new APIResponse(resp1);
				verify.verifyStatusCode(apiResp1.getStatusCode(), 200);
				if (apiResp1.getStatusCode() == 200) {
					verify.verifyResponseTime(resp1, 5000);
					JSONObject respJson1 = new JSONObject(apiResp1.getResponseAsString());

					verify.verifyEquals(respJson1.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.jsonSchemaValidation(resp1, "notebook" + File.separator + "deleteUserComment.json");
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

	@Test(groups = "sanity", priority = 34, description = "Edit user comments")
	public void editUserComments() throws Exception {
		try {
			if (comment_id.isEmpty()) {
				setNoteId();
			}
			if (!comment_id.isEmpty()) {

				// Edit comment
				HashMap<String, String> editcommentDictParams = new HashMap<String, String>();
				editcommentDictParams.put("source", "note");
				editcommentDictParams.put("comment_id", comment_id);
				editcommentDictParams.put("new_comment", "Comment Edited");

				String editJson = jsonUtils.toJson(editcommentDictParams);

				HashMap<String, String> params2 = new HashMap<String, String>();
				params2.put("action", "edit");
				params2.put("comment_dict", editJson);

				RequestSpecification spec2 = formParamsSpec(params2);
				Response resp2 = RestOperationUtils.post(USER_APP_URL + USER_COMMENTS, null, spec2, params2);
				APIResponse apiResp2 = new APIResponse(resp2);

				verify.verifyStatusCode(apiResp2.getStatusCode(), 200);
				if (apiResp2.getStatusCode() == 200) {
					verify.verifyResponseTime(resp2, 5000);
					JSONObject respJson2 = new JSONObject(apiResp2.getResponseAsString());
					verify.verifyEquals(respJson2.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.jsonSchemaValidation(resp2, "notebook" + File.separator + "editUserComment.json");
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

	@Test(groups = "sanity", priority = 36, description = "Fetch note data")
	public void fetchNoteHtml() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			if (note_id == "")
				setNoteId();
			if (note_id != "") {
				parameters.put("id", note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_HTML, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
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

	@Test(groups = "sanity", priority = 37, description = "This will load the L1(filter section)")
	public void fetchNoteFacetHtml() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("notemode", "all");
			parameters.put("type", "all");
			parameters.put("all_contacts", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_FACET_AND_HTML, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				JSONArray ticker_term = respJson.getJSONObject("result").getJSONObject("facets")
						.getJSONObject("tickers").getJSONArray("terms");
				ExtentTestManager.getTest().log(LogStatus.INFO, ticker_term.toString());
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

	@Test(groups = "sanity", priority = 38, description = "Fetch user notebook data")
	public void fetchNotebookData() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("note_type", "true");
			parameters.put("user_template", "true");
			parameters.put("user_groups", "true");
			parameters.put("user_fields", "true");
			parameters.put("user_email", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_NOTEBOOK_DATA, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				JSONArray noteType = respJson.getJSONObject("result").getJSONObject("note_type")
						.getJSONArray("static_note_type_list");
				if (noteType.length() == 0 || noteType == null)
					verify.assertTrue(false, "note type array is empty : ");
				if (USER_APP_URL.contains("app")) {
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
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 39, description = "Fetch user note history")
	public void fetchNoteHistory() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			if (note_id.isEmpty()) {
				setNoteId();
			}
			if (!note_id.isEmpty()) {
				parameters.put("id", note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_NOTE_HISTORY, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
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

	@Test(groups = "sanity", priority = 40, description = "This will fetch notebook settings")
	public void fetchNoteSettings() throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_NOTE_SETTING, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");

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

				// verify.jsonSchemaValidation(resp, "notebook" + File.separator +
				// "notebookSetting.json");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 41, description = "Verify fetch note version api")
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
				Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_NOTE_VERSION, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
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

	@Test(groups = "sanity", priority = 42, description = "Verify fetch note lock status")
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
				Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LOCK_STATUS, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
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

	@Test(groups = "sanity", priority = 43, description = "Verify highight present or not")
	public void fetch_note() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("docid", "5e68de142e808522f1a39820");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_NOTE, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
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

	@Test(groups = "sanity", priority = 44, description = "Verify thesis fields")
	public void fetch_thesis_fields() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_THESIS_FIELDS, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				String thesisFlag = "";
				try {
					thesisFlag = getUserPortFolio().getJSONObject("result").getJSONObject("other_flags")
							.getString("notebook_thesis");
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!thesisFlag.isEmpty()) {
					JSONArray fields = respJson.getJSONObject("result").getJSONArray("fields");
					if (fields == null || fields.length() == 0)
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

	@Test(groups = "sanity", priority = 45, description = "Fetch recent notes")
	public void fetch_recent_notes() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("size", "15");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_RECENT_NOTES, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				JSONArray notelist = respJson.getJSONArray("result");
				verify.verifyTrue(notelist.length(), "Verify note list present");
				JSONObject note;
				boolean notedata = true;
				for (int i = 0; i < notelist.length(); i++) {
					note = respJson.getJSONArray("result").getJSONObject(i);
					if (note == null || note.length() == 0) {
						verify.assertTrue(false, "Note data not present");
						notedata = false;
					}
				}
				if (notedata) {
					verify.assertTrue(notedata, "Data present for all notes");
				}
				if (notelist.length() > 0) {
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getString("note_id") != null,
							"Verify note id present");
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getString("url") != null,
							"Verify note url present");
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

	@Test(groups = "sansity", priority = 46, description = "fetch group")
	public void get_hierarchy() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("only_folder_names", "true");
			parameters.put("owner_type", "user");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get(USER_APP_URL + GET_HIERARCHY, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				JSONArray grouplist = respJson.getJSONArray("result");
				verify.assertTrue(grouplist.length()>0, "Verify group list present");
				if (grouplist.length() > 0) {
					JSONObject group;
					boolean groupdata = true;
					for (int i = 0; i < grouplist.length(); i++) {
						group = respJson.getJSONArray("result").getJSONObject(i);
						if (group == null || group.length() == 0) {
							verify.assertTrue(false, "Note data not present");
							groupdata = false;
						}
					}
					if (groupdata) {
						verify.assertTrue(groupdata, "Data present for all notes");
					}
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getString("unique_id") != null,
							"Verify note id present");
					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getString("owner") != null,
							"Verify note url present");
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

	APIResponse apiResp = null;
	Response resp = null;

	@Test(groups = "sanity", priority = 47, description = "fetch Calendar")
	public void fetchCalendar() throws Exception {
		String URI = USER_APP_URL + FETCHCALENDAR;
		HashMap<String, String> parameters = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		int current_month = cal.get(Calendar.MONTH) + 1;
		int last_date = cal.getActualMaximum(Calendar.DATE);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		try {
			parameters.put("watch", "All Watchlist Tickers");
			parameters.put("startDate", year + "-" + current_month + "-1");
			parameters.put("endDate", year + "-" + current_month + "-" + last_date);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
		} catch (Error je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 48, description = "Get thesis list")
	public void get_thesis_list() throws Exception {
		try {
			if (thesis_id.isEmpty())
				searchWithNoteTypeAsThesis();
			if (!thesis_id.isEmpty()) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("thesis_id", thesis_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + GET_THESIS_LIST, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "Fetch note api fail, thesis id not present");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 49, description = "Share note with user")
	public void share_new_user_note() throws Exception {
		try {
			if (note_id.isEmpty())
				setNoteId();
			if (!note_id.isEmpty()) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("note_id", note_id);
				parameters.put("email_flag", "0");
				parameters.put("share_user", shareUser);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(USER_APP_URL + SHARE_NEW_USER_NOTE, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
					verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify result status");
					JSONArray users = respJson.getJSONObject("result").getJSONArray("allowed");
					boolean userPresent = false;
					if (users.length() > 0) {
						for (Object list : users) {
							if (list.equals(shareUser)) {
								userPresent = true;
								break;
							}
						}
						verify.assertTrue(userPresent, "verify user present in share list");
					} else
						verify.assertTrue(false, "user not added in sharelist");
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

	@Test(groups = "sanity", priority = 50, description = "Create new bookmark note")
	public void new_bookmark_note() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("doc_id", "5e789829f8bad52e4c6de0ad");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + NEW_BOOKMARK_NOTE, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify api status");
				int status_code = respJson.getJSONObject("result").getInt("status_code");
				if (status_code == 1)
					verify.verifyEquals(respJson.getJSONObject("result").getString("message"), "bookmark note created",
							"Verify message");
				else
					verify.verifyEquals(respJson.getJSONObject("result").getString("message"), "bookmark note updated",
							"Verify message");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 51, description = "Index user bookmard doc")
	public void index_user_bookmark_doc() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("doc_id", "5e789829f8bad52e4c6de0ad");
			parameters.put("bookmark", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + INDEX_USER_BOOKMARK_DOC, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				verify.assertTrue(respJson.getBoolean("result"), "verify result status");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 52, description = "Update user field")
	public void update_field_value() throws Exception {
		try {
			if (URI.contains("app") || URI.contains("app2") || URI.contains("staging")) {
				String key_id = "";
				String section_id = "";
				if (noteList_Typed == null)
					searchWithNoteTypeAsTyped();
				if (noteList_Typed != null) {
					Random random = new Random();
					for (int i = 0; i < noteList_Typed.length(); i++) {
						String noteID = noteList_Typed.getJSONObject(i).getString("note_id");
						JSONObject noteData = getNoteDetail(noteID);
						try {
							noteData.getJSONObject("result").getJSONArray("field_section");
						} catch (Exception e) {
							continue;
						}
						if (noteData.getJSONObject("result").getJSONArray("field_section") != null
								&& noteData.getJSONObject("result").getJSONArray("field_section").length() > 0) {
							JSONArray keys = noteData.getJSONObject("result").getJSONArray("field_section")
									.getJSONObject(0).getJSONArray("keys");
							if (keys.length() > 0 && keys != null) {
								int index = random.nextInt(keys.length());
								key_id = keys.getJSONObject(index).getString("id");
								section_id = noteData.getJSONObject("result").getJSONArray("field_section")
										.getJSONObject(0).getString("id");
								break;
							}
						}
					}
					if (!key_id.isEmpty() && !section_id.isEmpty()) {
						HashMap<String, String> key_data = new HashMap<String, String>();
						key_data.put(key_id, Integer.toString(random.nextInt(100)));
						String dataJson = jsonUtils.toJson(key_data);
						HashMap<String, String> parameters = new HashMap<String, String>();
						parameters.put("data_dict", dataJson);
						parameters.put("section_id", section_id);
						RequestSpecification spec = formParamsSpec(parameters);
						Response resp = RestOperationUtils.post(USER_APP_URL + UPDATE_FIELD_VALUE, null, spec,
								parameters);
						APIResponse apiResp = new APIResponse(resp);
						verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
						verify.verifyResponseTime(resp, 5000);
						if (apiResp.getStatusCode() == 200) {
							JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
							verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"),
									"verify api status");
							verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"),
									"verify result status");
							verify.verifyEquals(respJson.getJSONObject("result").getString("msg"), "Success",
									"verify result status");
						}
					} else {
						ExtentTestManager.getTest().log(LogStatus.SKIP, "Field section not present");
					}
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "We are not supporting : " + USER_APP_URL);
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 53, description = "test clipper note creation")
	public void new_clipper_note() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("source", "https://www.facebook.com/");
			parameters.put("update", "true");
			parameters.put("version", "1.0.6");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + NEW_CLIPPER_NOTE, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				verify.verifyEquals(respJson.getJSONObject("result").getString("type"), "clipper",
						"verify result status");
				verify.assertTrue(respJson.getJSONObject("result").getString("note_title").contains("Facebook"),
						"verify result status");
				verify.assertTrue(respJson.getJSONObject("result").getString("id") != null, "verify result status");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 54, description = "Update field")
	public void update_field() throws CoreCommonException {
		try {
			if (URI.contains("app") || URI.contains("app2") || URI.contains("staging")) {
				HashMap<String, String> other_flags = new HashMap<String, String>();
				other_flags.put("required", "false");
				other_flags.put("currency", "false");
				other_flags.put("percentage", "false");
				other_flags.put("multiple", "false");

				String dataJson = jsonUtils.toJson(other_flags);
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("name", "devesh test" + new Date().getTime());
				parameters.put("key", "devesh_test");
				parameters.put("type", "number");
				parameters.put("action", "update");
				parameters.put("id", "5e830d24a931f326a8edb263");
				parameters.put("other_flags", dataJson);

				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + UPDATE_FIELD, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
					verify.verifyEquals(respJson.getJSONObject("result").getString("msg"), "Success",
							"verify result status");
					verify.assertTrue(respJson.getJSONObject("result").getString("id") != null, "verify result status");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "We are not supporting  on : " + USER_APP_URL);
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 55, description = "add new section in note")
	public void new_section() throws CoreCommonException {
		try {
			if (note_id.isEmpty())
				setNoteId();
			if (!note_id.isEmpty()) {
				String[] data = new String[0];
				int[] dataInt = new int[] { 0, 0, 0 };
				HashMap<String, Object> section_data = new HashMap<String, Object>();
				section_data.put("keys", data);
				section_data.put("order", dataInt);
				section_data.put("id", "temp_" + new Date().getTime());
				String dataJson = jsonUtils.toJson(section_data);

				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("note_id", note_id);
				parameters.put("sections_data", "[" + dataJson + "]");

				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + NEW_SECTION, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");

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

	@Test(groups = "sanity", priority = 56, description = "Delete highlight note")
	public void delete_highlight() throws Exception {
		try {
			if (HighlightNoteID.isEmpty())
				searchWithNoteTypeAsHighlight();
			if (!HighlightNoteID.isEmpty()) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("id", HighlightNoteID);
				parameters.put("forever_delete", "false");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + DELETE_HIGHLIGHT, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
					verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify api status");
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

	@Test(groups = "sanity", priority = 57, description = "Delete a bookmark note")
	public void delete_user_bookmark() throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("doc_id", "5e789829f8bad52e4c6de0ad");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + DELETE_USER_BOOKMARK, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify api status");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 58, description = "Update note section")
	public void update_section() throws CoreCommonException {
		try {
			if (URI.contains("app") || URI.contains("app2") || URI.contains("staging")) {
				String[] data = new String[] { "5e8486c0f7322a4a3c768544" };
				int[] dataInt = new int[] { 1, 0, 0 };
				HashMap<String, Object> section_data = new HashMap<String, Object>();
				section_data.put("keys", data);
				section_data.put("order", dataInt);
				section_data.put("section_id", "5e8486b1a931f31d510117df");
				section_data.put("action", "update");

				String dataJson = jsonUtils.toJson(section_data);

				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("action_list", "[" + dataJson + "]");
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(USER_APP_URL + UPDATE_SECTION, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
					verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify api status");
				}
			} else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "We are not supporting  on : " + USER_APP_URL);
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 59, description = "Print note content")
	public void get_note_content_for_iframe() throws CoreCommonException {
		try {
			if (note_id.isEmpty())
				setNoteId();
			if (!note_id.isEmpty()) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("id", note_id);
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.get(USER_APP_URL + GET_NOTE_CONTENT_FOR_IFRAME, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
				verify.verifyResponseTime(resp, 5000);
				if (apiResp.getStatusCode() == 200) {
					String responseHtml = apiResp.getResponseAsString();
					verify.assertTrue(!responseHtml.isEmpty(), "verify response is not blank");
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

	@Test(groups = "sanity", priority = 60, description = "Consume citation link")
	public void consume_citation_link() throws CoreCommonException {

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("main_link", APP_URL
				+ "/#doc?backtab=true&title=JEFFERIES%3A%20Key%20takeaways%20from%20conference%20call&ticker=sime%3Amk&stamp=1585846200000&date=undefined&doc_id=5e85aaaff8bad5201be1a798&citation_id=1585824171929&scrollToid=3.14.0%231&owner=autotester&doc_type=rr&scroll_source=highlight");
		parameters.put("short_link", "http://snt.io/TeFHHEvyR\r\n");
		parameters.put("highlight_id", "5e85c1aef7322a753dafa603");
		RequestSpecification spec = formParamsSpec(parameters);
		try {
			Response resp = RestOperationUtils.post(USER_APP_URL + CONSUME_CITATION_LINK, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", priority = 61, description = "Get new free citation link")
	public void get_new_free_citation_link() throws CoreCommonException {
		HashMap<String, String> parameters = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(parameters);
		Response resp = RestOperationUtils.post(USER_APP_URL + GET_NEW_FREE_CITATION_LINK, null, spec, parameters);
		try {
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				verify.assertTrue(respJson.getJSONObject("result").getBoolean("status"), "verify result status");
				verify.assertTrue(!respJson.getJSONObject("result").getString("new_link").isEmpty(),
						"verify new_link url is not blank");
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}
	
	@Test(groups = "sanity", priority = 62, description = "fetch sentieo drive data")
	public void get_hierarchy_sentieoDrive() throws CoreCommonException {
		try {
			if(!APP_URL.contains("schroders")) {
			setUp();
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("resource_id", "root");
			parameters.put("owner_type", "user");
			parameters.put("owner", username);

			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.get( USER_APP_URL + GET_HIERARCHY, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
				verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
				JSONArray grouplist = respJson.getJSONArray("result");
				System.out.println(grouplist);
				verify.assertTrue(grouplist.length()>0, "Verify group list present");
				if (grouplist.length() > 0) {
					JSONObject group;
					boolean groupdata = true;
					for (int i = 0; i < grouplist.length(); i++) {
						group = respJson.getJSONArray("result").getJSONObject(i);
						if (group == null || group.length() == 0) {
							verify.assertTrue(false, "Note data not present");
							groupdata = false;
						}
					}
					if (groupdata) {
						verify.assertTrue(groupdata, "Data present for all notes");
					}
//					verify.assertTrue(respJson.getJSONArray("result").getJSONObject(0).getString("unique_id") != null,
//							"Verify note id present");
					
					JSONArray files_Data = grouplist.getJSONObject(0).getJSONArray("children");
					if(files_Data.length()>0)
					{
						for(int i=0;i<files_Data.length();i++) {
							if(files_Data.getJSONObject(i)==null)
								verify.assertTrue(false, "files data not present in sentieo drive");
							else {
								if(files_Data.getJSONObject(i).getString("resource_type").equalsIgnoreCase("file") && !files_Data.getJSONObject(i).getString("id").isEmpty()){
									doc_id_sentieoDrive = files_Data.getJSONObject(i).getString("id");
									verify.assertTrue(true, "file data present : " + files_Data.getJSONObject(i).toString());
									break;
								}
									//else
//									verify.assertTrue(false, "file data not present : " + files_Data.getJSONObject(i).toString());
							}
						}
						
						for(int i=0;i<files_Data.length();i++) {
							if(files_Data.getJSONObject(i)==null)
								verify.assertTrue(false, "files data not present in sentieo drive");
							else {
								System.out.println(files_Data.getJSONObject(i).getString("name"));
								if(files_Data.getJSONObject(i).getString("resource_type").equalsIgnoreCase("file")&& !files_Data.getJSONObject(i).getString("extension").equalsIgnoreCase("csv") && !files_Data.getJSONObject(i).getString("id").isEmpty()){
									doc_id_noncsv_sentieoDrive = files_Data.getJSONObject(i).getString("id");
									verify.assertTrue(true, "file data present : " + files_Data.getJSONObject(i).toString());
									break;
								}
									//else
//									verify.assertTrue(false, "file data not present : " + files_Data.getJSONObject(i).toString());
							}
						}
					}
				}

			}
		  }else {
				ExtentTestManager.getTest().log(LogStatus.SKIP, "not supported on : " + APP_URL);
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@SuppressWarnings("unused")
	@Test(groups = "checktest", description = "Check autocomplete api", dataProvider = "module-type", dataProviderClass = DataProviderClass.class)
	public void search_entities(String moduleType, String sentieoEntity) throws CoreCommonException, IOException {
		try {
		for (String[] row : tickers) {
				String tickername = "";
				String type = "";
				String status = "";
				try {
					for (String cell : row) {
						if (tickername.isEmpty()) {
							tickername = cell;
							continue;
						}
						if (type.isEmpty()) {
							type = cell;
							continue;
						}
						if (status.isEmpty()) {
							status = cell;
						}
					}
					if(moduleType.equalsIgnoreCase("EDT"))  //to print proper name in report
						moduleType="company";
					
					HashMap<String, String> parameters = new HashMap<String, String>();
					parameters.put("suggest", tickername);
					parameters.put("allow_pvt_company", "true");
					parameters.put("pagetype", moduleType);
					parameters.put("sentieoentity", sentieoEntity);
					RequestSpecification spec = formParamsSpec(parameters);
					Response resp = RestOperationUtils.get(APP_URL + SEARCH_ENTITIES, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					ExtentTestManager.getTest().log(LogStatus.INFO, "Ticker/Partial Search : " + tickername);
					if(!(apiResp.getStatusCode() == 200))	
					verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
					verify.verifyResponseTime(resp, 5000);
					if (apiResp.getStatusCode() == 200) {
						JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
						if(!respJson.getJSONObject("response").getBoolean("status"))
						verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"), "verify api status");
						if (type.equalsIgnoreCase("public")) {
							JSONArray companylist;
							if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company"))
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
							else
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("sentieoentity");
							if (null == companylist  || companylist.length() == 0) {
								verify.assertTrue(false, "Ticker not coming for search : " + tickername);
							}
							if (companylist.length() > 0) {
								JSONObject tickerData = companylist.getJSONObject(0);
								String token_label = tickerData.getString("token_label");
								String ticker_status = tickerData.getString("status");
								if (!tickerData.getString("name").toLowerCase().contains(tickername.toLowerCase()))
									verify.assertEqualsActualContainsExpected(
											tickerData.getString("name").toLowerCase(), tickername.toLowerCase(),
											"verify ticker name");
								if (ticker_status.isEmpty())
								verify.assertTrue(!ticker_status.isEmpty(), "verify ticker status");
								
								if (tickerData.getString("_id").isEmpty())
									verify.assertTrue(!tickerData.getString("_id").isEmpty(),
											"verify ticker _id present");
								if(sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
								if (!tickerData.getString("type").equalsIgnoreCase("company")) 
									verify.verifyEquals(tickerData.getString("type"), "company", "verify company type");
								}else {
									if (!tickerData.getString("type").equalsIgnoreCase("sentieoentity"))
										verify.verifyEquals(tickerData.getString("type"), "sentieoentity", "verify company type");									
								}
							}
						} else if (type.equalsIgnoreCase("private")) {
							JSONArray privcomp;
							if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company"))
								privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privcomp");
							else
								privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privateentity");
							if (null == privcomp || privcomp.length() == 0) {
								verify.assertTrue(false, "Ticker not coming for search : " + tickername);
							}
							if (privcomp.length() > 0) {
								JSONObject tickerData = privcomp.getJSONObject(0);
								String token_label = tickerData.getString("token_label");
								String ticker_status = tickerData.getString("status");
								if (!tickerData.getString("name").toLowerCase().contains(tickername.toLowerCase()))
									verify.assertEqualsActualContainsExpected(
											tickerData.getString("name").toLowerCase(), tickername.toLowerCase(),
											"verify ticker name");
								if (ticker_status.isEmpty())
										verify.assertTrue(!ticker_status.isEmpty(), "verify ticker status");
								
								if (tickerData.getString("_id").isEmpty())
									verify.assertTrue(!tickerData.getString("_id").isEmpty(),
											"verify ticker _id present");
								if(sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
									if (!tickerData.getString("type").equalsIgnoreCase("privcomp")) 
										verify.verifyEquals(tickerData.getString("type"), "privcomp", "verify company type");
									}else {
										if (!tickerData.getString("type").equalsIgnoreCase("privateentity"))
											verify.verifyEquals(tickerData.getString("type"), "privateentity", "verify company type");									
									}
							
								if(tickerData.getString("name").toLowerCase().contains(tickerData.getString("_id").toLowerCase()))
									verify.assertTrue(false, "Id appearing in name" + tickerData.getString("name"));
							}
						} else {// for partial text search
							if (sentieoEntity.equals("0") || moduleType.equalsIgnoreCase("company")) {
								JSONArray companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
								verify.assertTrue(companylist.length() > 0, "company data should be present");

								JSONArray privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privcomp");
								verify.assertTrue(privcomp.length() > 0, "privcomp data should be present");
								
								if (privcomp.length() > 0) {			
									if(privcomp.getJSONObject(0).getString("name").toLowerCase().contains(privcomp.getJSONObject(0).getString("_id").toLowerCase()))
										verify.assertTrue(false, "Id appearing in name" + privcomp.getJSONObject(0).getString("name"));
								}
								if(!moduleType.equalsIgnoreCase("company")) {
//								JSONArray crypto = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("crypto");
//								verify.assertTrue(crypto.length() > 0, "crypto data should be present");

								JSONArray entity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("entity");
								verify.assertTrue(entity.length() > 0, "entity data should be present");

//								if(!(tickername.equalsIgnoreCase("8") && tickername.equalsIgnoreCase("AA"))) {
//								JSONArray organization = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("organization");
//								verify.assertTrue(organization.length() > 0, "organization data should be present");
//								
//								
//								JSONArray debt = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("debt");
//								verify.assertTrue(debt.length() > 0, "debt data should be present");
								}
							} else {
								JSONArray privateentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privateentity");
								verify.assertTrue(privateentity.length() > 0, "privateentity data should be present");

								JSONArray sentieoentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("sentieoentity");
								verify.assertTrue(sentieoentity.length() > 0, "sentieoentity data should be present");

								JSONArray subsidiary = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("subsidiary");
								verify.assertTrue(subsidiary.length() > 0, "subsidiary data should be present");

								JSONArray cryptoentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("cryptoentity");
								verify.assertTrue(cryptoentity.length() > 0, "cryptoentity data should be present");

								JSONArray secentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("secentity");
								verify.assertTrue(secentity.length() > 0, "secentity data should be present");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					verify.assertTrue(false, "ticker : " + tickername + e.toString());
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

	public String getCurrentTime() {
		String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a z";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
		ZoneId fromTimeZone = ZoneId.of("Asia/Kolkata");

		LocalDateTime currentTime = LocalDateTime.now();
		ZonedDateTime currentISTime = currentTime.atZone(fromTimeZone);
		System.out.println(formatter.format(currentISTime));
		return formatter.format(currentISTime);
	}

	public JSONObject getNoteDetail(String noteID) throws CoreCommonException {
		try {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("id", noteID);
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_HTML, null, spec, parameters);
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

	public void setNoteId() throws CoreCommonException {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("filter", "all");
			params.put("start", "0");
			params.put("size", "15");
			params.put("order", "note_updated_date:desc");
			params.put("mode", "all");

			RequestSpecification spec = formParamsSpec(params);
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
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
			Response resp = RestOperationUtils.post(USER_APP_URL + FETCH_NOTE_LIST, null, spec, params);
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
			Response resp = RestOperationUtils.get(USER_APP_URL + FETCH_INITIAL_LOADING, spec, parameters);
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

//	@AfterSuite(alwaysRun = true)
//	public void deletePrivateNote1(){
//		try {
//			if (note_id != "") {
//				// delete note
//				HashMap<String, String> deleteNoteParams = new HashMap<String, String>();
//				deleteNoteParams.put("note_id", note_id);
//				RequestSpecification spec1 = formParamsSpec(deleteNoteParams);
//				RestOperationUtils.post( USER_APP_URL + DELETE_NOTE, null, spec1, deleteNoteParams,false);
//			}
//		} catch (JSONException | CoreCommonException je) {
//			je.printStackTrace();
//		} 
//	}
}
