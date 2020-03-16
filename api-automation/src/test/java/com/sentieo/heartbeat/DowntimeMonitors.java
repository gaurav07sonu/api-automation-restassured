
package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.googlesheet.GoogleSheetUtil;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class DowntimeMonitors extends APIDriverHeartbeat {

	APIResponse apiResp = null;
	Response resp = null;
	
	static HashMap<String, String> failedMap = new HashMap<String, String>();
	static HashMap<String, HashSet<String>> failedMap_Team_APIS = new HashMap<String, HashSet<String>>();
	static HashMap<String, String> passedMap = new HashMap<String, String>();
	
	@BeforeClass(alwaysRun = true)
	public void setup() throws Exception {
		String URI = USER_APP_URL + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", EMAIL);
		loginData.put("password", PASSWORD);
		RequestSpecification spec = loginSpec(loginData);
		Response resp = RestOperationUtils.login(URI, null, spec, loginData);
		apid = resp.getCookie("apid");
		usid = resp.getCookie("usid");
		RestAssured.baseURI = APP_URL;
	}

	@Test(groups = { "downtime" }, description = "Check fetch graph data")
	public void fetchGraphDataYearlyEstimate() throws Exception {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_GRAPH_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("graphtype", "yearlyEstimate");
			parameters.put("subtype", "TotalRevenue");
			parameters.put("periodtype", "Quarterly");
			parameters.put("source", "summary");
			parameters.put("startyear", "2014");
			parameters.put("endyear", "2022");
			parameters.put("getstock", "true");
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "downtime" }, description = "get tracker mappings")
	public void getTrackerMappings() throws CoreCommonException {
		Team team = Team.Graph;
		String URI = APP_URL + GET_TRACKER_MAPPINGS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "TSLA");
			parameters.put("termtype", "ticker");
			parameters.put("source", "summary");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}


	@Test(groups = { "downtime"}, description = "Check fetch live price")
	public void fetchSearch() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_SEARCH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			parameters.put("query", "sales");
			parameters.put("filters", "{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			parameters.put("facets_flag", "false");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}
	
	@Test(groups = {"downtime"} , description = "Fetching doc info")
	public void fetch_docs_meta_data() throws CoreCommonException {
		Team team = Team.Search;
		String URI = APP_URL + FETCH_DOCS_META_DATA;
		String doc_id = getDocsId();
		if(StringUtils.isEmpty(doc_id)) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			try {
				parameters.put("doc_ids", doc_id);
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
				Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
				passedMap.put(team.toString(), getCurrentTime());
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			}
			catch (Error e) {
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				addLastFailedAPIsForTeam(team.toString(), URI);
				failedMap.put(team.toString(), getCurrentTime());
				Assert.fail();
				Assert.fail();
			} catch (Exception e) {
				updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
				addLastFailedAPIsForTeam(team.toString(), URI);
				failedMap.put(team.toString(), getCurrentTime());
				Assert.fail();
			}
		}

	}
	
	public String getDocsId() {
		String URI = APP_URL + FETCH_SEARCH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("tickers", "aapl");
			parameters.put("query", "sales");
			parameters.put("filters", "{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}");
			parameters.put("facets_flag", "false");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null,  spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(apiResp.getResponseAsString());
			JSONArray docsList = respJson.getJSONObject("result").getJSONArray("docs");
			System.out.println(docsList.getJSONObject(0).getString("id"));
			return docsList.getJSONObject(0).getString("id");
		} catch (Exception e) {	
			return "";
		} 
	}

	@Test(groups = { "downtime" }, description = "Check fetch live price")
	public void fetchUnifiedStreamAllDocs() throws CoreCommonException {
		Team team = Team.Stream;
		String URI = APP_URL + FETCH_UNIFIED_STREAM;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("client_type_status", "low");
			parameters.put("dashboard", "news_stream");
			parameters.put("doc_type", "alldocs");
			parameters.put("tickers", "aapl");
			parameters.put("tags", "");
			parameters.put("social_reach", "sentieo");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}


	@Test(groups = { "downtime"}, description = "FETCH_NEW_MODEL_DATA")
	public void fetchNewModelData() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_NEW_MODEL_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("model_source", "vpt");
			parameters.put("ticker", "aapl");
			parameters.put("ptype", "fq");
			parameters.put("report_currency", "usd");
			parameters.put("units", "T");
			parameters.put("historical_periods", "3year");
			parameters.put("forecast_periods", "3year");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "downtime" }, description = "fetchmaingraph")
	public void fetchMainGraphComparables() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_MAIN_GRAPH;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("ticker", "aapl");
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "downtime" }, description = "Fetch Firewall")
	public void fetchFirewall() throws Exception {
		String URI = USER_APP_URL + FETCH_FIREWALL_TEST;
		Team team = Team.User;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, null);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}
	

	@Test(groups = { "downtime" }, description = "verify mosaic summary data")
	public void mosaicSummaryData() throws CoreCommonException {
		Team team = Team.Graph;
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + NEW_FETCH_MOSAIC_SUMMARY_DATA;
		try {
			parameters.put("selection", "Revenue_corrScore");
			parameters.put("termtype", "ticker");
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}


	@Test(groups = { "downtime" }, description = "fetch Calendar")
	public void fetchCalendar() throws Exception {
		Team team = Team.Calendar;
		String URI = USER_APP_URL + FETCHCALENDAR;
		HashMap<String, String> parameters = new HashMap<String, String>();
		Calendar cal = Calendar.getInstance();
		int current_month = cal.get(Calendar.MONTH)+1;
		int last_date = cal.getActualMaximum(Calendar.DATE);
		int year = Calendar.getInstance().get(Calendar.YEAR); 
		try {
			parameters.put("watch", "All Watchlist Tickers");
			parameters.put("startDate",year+"-"+ current_month + "-1");
			parameters.put("endDate",year+"-"+current_month+"-"+last_date);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	
	@Test(groups = { "downtime"}, description = "Fetch user notebook data",priority = 3)
	public void fetchNotebookData() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + FETCH_NOTE_DATA;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try { 
			parameters.put("note_type","true");
			parameters.put("user_template","true");
			parameters.put("user_groups", "true");
			parameters.put("user_fields", "true");
			parameters.put("user_email", "true");
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			passedMap.put(team.toString(), getCurrentTime());
			updatePassResult(URI, team.toString(), "200", resp, parameters);
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "downtime"}, description = "Fetch note data",priority = 4)
	public void fetchNoteHtml() throws Exception {
		Team team = Team.Notebook;
		String URI = USER_APP_URL + FETCH_NOTE_HTML;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String noteId = getNoteId();
			if(!StringUtils.isEmpty(noteId)) {
				parameters.put("id",noteId);
				RequestSpecification spec = formParamsSpec(parameters);
				resp = RestOperationUtils.post(URI, null, spec, parameters);
				apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				Assert.assertTrue(apiResp.getStatusCode()==200 || apiResp.getStatusCode()==504, "API status code not equal to 200 or 504");
				assertTrue(respJson.getJSONObject("response").getBoolean("status"),"verify api status");
				assertTrue(respJson.getJSONObject("result").getString("id").equalsIgnoreCase(noteId),"Note id should be equal to fetched note id : ");
				assertTrue(respJson.getJSONObject("result").getString("url")!=null,"Url should not be blank : ");
				passedMap.put(team.toString(), getCurrentTime());
				updatePassResult(URI, team.toString(), "200", resp, parameters);
			}
			
		} catch (Error e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			updateFailResult(URI, team.toString(), String.valueOf(apiResp.getStatusCode()), resp, parameters, e.getMessage());
			addLastFailedAPIsForTeam(team.toString(), URI);
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}
	
	public String getNoteId() {
		String URI = USER_APP_URL + FETCH_NOTE_LIST;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("filter", "all");
			parameters.put("mode","all");
			parameters.put("order", "note_updated_date:desc");
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(apiResp.getResponseAsString());
			JSONArray notesList = respJson.getJSONObject("result").getJSONArray("notes");
			System.out.println(notesList.getJSONObject(0).getString("note_id"));
			return notesList.getJSONObject(0).getString("note_id");
		} catch (Exception e) {	
			return "";
		} 
	}

	
		
	@AfterClass(alwaysRun = true)
	public void updateGoogleSheet() {
		generateHTML();
		updateGoogleSheetsForDowntime();
		updateGoogleSheetsForDowntimeHistory();
	}
	
	public void updateGoogleSheetsForDowntime() {
		try {
			for (Entry<String, String> entry : failedMap.entrySet()) {
				System.out.println("failed hmap");
				GoogleSheetUtil.updateDowntimeData(entry.getKey(), false, entry.getValue(), failedMap_Team_APIS.get(entry.getKey()));
			}
			
			for (Entry<String, String> entry : passedMap.entrySet()) {
				System.out.println("passed hmap");
				if(failedMap.containsKey(entry.getKey())) {
					//do nothing
				}
				else {
					GoogleSheetUtil.updateDowntimeData(entry.getKey(), true, entry.getValue(), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateGoogleSheetsForDowntimeHistory() {
		try {
			for (Entry<String, String> entry : failedMap.entrySet()) {
				System.out.println("failed hmap");
				GoogleSheetUtil.updateDowntimeHistoryData(entry.getKey() + "_HISTORY", entry.getValue(), failedMap_Team_APIS.get(entry.getKey()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateHTML() {
		String content = readHTMLHeader() + sbFail.toString() + sbPass.toString() + readHTMLFooter();
		try {
			FileUtils.deleteQuietly(new File("heartbeat.html"));
			Files.write(Paths.get("heartbeat.html"), content.getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			
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
	
	public void addLastFailedAPIsForTeam(String team, String path) {
		String failedAPI = path.substring(path.lastIndexOf("api"), path.length());
		HashSet<String> set = new HashSet<String>();
		if(failedMap_Team_APIS.containsKey(team)) {
			set.addAll(failedMap_Team_APIS.get(team));
			set.add(failedAPI);
			failedMap_Team_APIS.put(team, set);
		}
		else {
			set.add(failedAPI);
			failedMap_Team_APIS.put(team, set);
		}
	}
}
