package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.finance.InputTicker;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class DocumentData extends APIDriver {

	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();
	// public ArrayList<String> tickers = new
	// ArrayList<String>(Arrays.asList("aapl", "lb", "amzn"));
	String documentDate;
	String plotterDate;
	double EXPECTEDDAYS = 1;
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeClass
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

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "Plotter Web and Social Data Series")
	public void transcriptSentiment() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_GRAPH_DATA;
			for (String[] row : tickers) {
				for (String cell : row) {
					parameters.put("head_name", "Transcript Sentiment Scores");
					parameters.put("pagetype", "plotter");
					parameters.put("graphtype", "transcriptSentiment");
					parameters.put("type", "Non-Management");
					parameters.put("ticker", cell);
					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(URI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					JSONArray value = values.getJSONArray(values.length() - 1);
					double timestamp = value.getDouble(0);
					int digit = (int) (timestamp / 1000);
					CommonUtil util = new CommonUtil();
					plotterDate = util.convertTimestampIntoDate(digit);
					fetchCompanyDocs(cell);
					verify.assertEqualsActualContainsExpected(plotterDate, documentDate,
							"Match Document Search date and Plotter date");
				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}
	}

	public void fetchCompanyDocs(String cell) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_COMPANY_DOCS;
			parameters.put("ticker", cell);
			parameters.put("size", "10");
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			// JSONObject values =
			// respJson.getJSONObject("result").getJSONArray("transcripts").getJSONObject(0);
			JSONArray values = respJson.getJSONObject("result").getJSONArray("transcripts");
			for (int j = 0; j < values.length(); j++) {
				JSONObject object = values.getJSONObject(j);
				if ((Boolean) object.get("has_intel") == true) {
					String timeStamp = object.get("timestamp").toString();
					double timestamp = Double.parseDouble(timeStamp);
					int digit = (int) (timestamp / 1000);
					CommonUtil util = new CommonUtil();
					documentDate = util.convertTimestampIntoDate(digit);
					break;

				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

}