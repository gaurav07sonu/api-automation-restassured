
package com.sentieo.heartbeat;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
import com.sentieo.utils.JSONUtils;

public class APIDowntimeMonitors extends APIDriverHeartbeat {

	APIResponse apiResp = null;
	Response resp = null;
	static String note_id = "";
	JSONUtils jsonUtils = null;
	static String noteID_Thesis="";
	static HashMap<String, String> failedMap = new HashMap<String, String>();
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

	@Test(groups = { "heart-beat" }, description = "Check fetch graph data")
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
			Assert.assertEquals(apiResp.getStatusCode(), 2000 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			JSONArray getSeries = respJson.getJSONObject("result").getJSONArray("series");
			if (getSeries.length() == 0 || getSeries == null)
				Assert.assertTrue(false, "Series array is empty");
			for (int i = 0; i < getSeries.length(); i++) {
				String path = "result." + "series" + "[" + i + "]." + "title";
				String seriesTitle = String.valueOf(apiResp.getNodeValue(path));
				if (seriesTitle.contains("Total Revenue-2021")) {
					JSONArray revenue2021Estimates = getSeries.getJSONObject(i).getJSONArray("series");
					if (revenue2021Estimates.length() == 0 || revenue2021Estimates == null)
						Assert.assertTrue(false, "revenue2021Estimates array is empty : ");
				}
			}
			passedMap.put(team.toString(), getCurrentTime());
		} catch (Error e) {
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat" }, description = "Check fetch company status")
	public void fetchCompamyStatus() throws CoreCommonException {
		Team team = Team.FIN;
		String URI = APP_URL + FETCH_COMPANY_STATUS;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			String ticker = "aapl";
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			resp = RestOperationUtils.get(URI, spec, parameters);
			apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			String stock_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("stock_flag").toString();
			if (!stock_flag.contains("true"))
				assertTrue(false,"Stock flag should be true : ");
			String ih_flag = respJson.getJSONObject("result").getJSONObject(ticker).get("ih_flag").toString();
			if (!ih_flag.contains("true"))
				assertTrue(false,"ih_flag should be true : ");
			String report_currency = respJson.getJSONObject("result").getJSONObject(ticker).get("report_currency")
					.toString();
			if (!report_currency.contains("usd"))
				assertTrue(false,"report_currency should be in usd : ");
			String trading_status = respJson.getJSONObject("result").getJSONObject(ticker).get("trading_status")
					.toString();
			if (!trading_status.contains("Success"))
				assertTrue(false,"Trading status contains success : ");
			String allowed_edt = respJson.getJSONObject("result").getJSONObject(ticker).get("allowed_edt").toString();
			if (!allowed_edt.contains("true"))
				assertTrue(false, "allowed_edt should be true : ");
			passedMap.put(team.toString(), getCurrentTime());
		} catch (Error e) {
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		} catch (Exception e) {
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}

	@Test(groups = { "heart-beat", "test" }, description = "Fetch Firewall")
	public void checkDomain() throws Exception {
		Team team = Team.User;
		String URI = APP_URL + CHECK_DOMAIN;
		HashMap<String, String> parameters = new HashMap<String, String>();
		try {
			parameters.put("email", EMAIL);
			RequestSpecification spec = formParamsSpec(parameters);
			resp = RestOperationUtils.post(URI, null, spec, parameters);
			apiResp = new APIResponse(resp);
			Assert.assertEquals(apiResp.getStatusCode(), 200 , "Api response : ");
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			Assert.assertEquals(respJson.getJSONObject("response").getBoolean("status"), true, "Response status is not equal to true : ");
			passedMap.put(team.toString(), getCurrentTime());
		} catch (Error e) {
			failedMap.put(team.toString(), getCurrentTime());			
			Assert.fail();
		} catch (Exception e) {
			failedMap.put(team.toString(), getCurrentTime());
			Assert.fail();
		}
	}
		
	@AfterClass(alwaysRun = true)
	public void generateHTML() {
		try {
			for (Entry<String, String> entry : failedMap.entrySet()) {
				System.out.println("failed hmap");
				GoogleSheetUtil.updateDowntimeData(entry.getKey(), false, entry.getValue());
			}
			
			for (Entry<String, String> entry : passedMap.entrySet()) {
				System.out.println("passed hmap");
				if(failedMap.containsKey(entry.getKey())) {
					//do nothing
				}
				else {
					GoogleSheetUtil.updateDowntimeData(entry.getKey(), true, entry.getValue());
				}
			}
		} catch (Exception e) {
			
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
}
