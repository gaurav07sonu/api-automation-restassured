package com.sentieo.plotter;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.EMAIL;
import static com.sentieo.constants.Constants.FETCH_GRAPH_DATA;
import static com.sentieo.constants.Constants.LOGIN_URL;
import static com.sentieo.constants.Constants.PASSWORD;
import static com.sentieo.constants.Constants.USER_APP_URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.finance.FinanceApi;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class TestPENotUpdatingBug53404 extends APIDriver {

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

	@Test(description = "Check latest data points for daily series", dataProvider = "s&pPE", dataProviderClass = DataProviderClass.class)
	public void PEIsNotUpdated(String shift, String ticker) throws Exception {
		// String ticker = "lulu,sp500";
		String expectedTitle = "";
		String expectedTitleSP = "";
		String systemDate;
		JSONArray peNTMValue = null;
		JSONArray SP500NTMValue = null;
		String seriesTitleSP = "";
		JSONArray SP500NTM = null;
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			String URI = APP_URL + FETCH_GRAPH_DATA;
			ticker = ticker.toLowerCase();
			parameters.put("head_name", "P/E");
			parameters.put("graphtype_original", "p_eps");
			parameters.put("graphtype", "newratioestimate");
			parameters.put("shift", shift);
			parameters.put("ptype", "rolling");
			parameters.put("ratio_name", "NTM Rolling");
			parameters.put("ratio", "p_eps");
			parameters.put("datasource", "rf");
			parameters.put("ticker", ticker);
			RequestSpecification spec = queryParamsSpec(parameters);
			Response resp = RestOperationUtils.get(URI, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray PENTM = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
						.getJSONArray("series");

				String seriesTitle = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
						.getString("title");

				String splitTicker[] = ticker.split(",");

				if (shift.contains("blended")) {
					expectedTitle = splitTicker[0].toUpperCase() + " NTM - TWA P/E";
					SP500NTM = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(2)
							.getJSONArray("series");

					seriesTitleSP = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(2)
							.getString("title");
				}

				else {
					expectedTitle = splitTicker[0].toUpperCase() + " NTM P/E";
					SP500NTM = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
							.getJSONArray("series");

					seriesTitleSP = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
							.getString("title");
				}

				verify.assertEqualsActualContainsExpected(seriesTitle, expectedTitle, "verify added series title");

				if (shift.contains("blended"))
					expectedTitleSP = "S&P 500 NTM - TWA P/E";

				else
					expectedTitleSP = "S&P 500 NTM P/E";

				verify.assertEqualsActualContainsExpected(seriesTitleSP, expectedTitleSP, "verify added series title");

				if (PENTM.length() != 0 || SP500NTM.length() != 0 || SP500NTM != null || PENTM != null) {
					peNTMValue = PENTM.getJSONArray(PENTM.length() - 1);
					SP500NTMValue = SP500NTM.getJSONArray(SP500NTM.length() - 1);
					double timestamp = peNTMValue.getDouble(0);
					int digit = (int) (timestamp / 1000);
					CommonUtil util = new CommonUtil();
					String date = util.convertTimestampIntoDate(digit);

					FinanceApi fin = new FinanceApi();

					systemDate = fin.dateValidationForHistoricalChart("fetch_main_graph", ticker);
					verify.compareDates(date, systemDate, "Verify the Current Date Point for P/E series");

					timestamp = SP500NTMValue.getDouble(0);
					digit = (int) (timestamp / 1000);
					util = new CommonUtil();
					date = util.convertTimestampIntoDate(digit);

					systemDate = fin.dateValidationForHistoricalChart("fetch_main_graph", ticker);
					verify.compareDates(date, systemDate, "Verify the Current Date Point for PS&P 500 NTM - TWA P/E");

				}
			}
		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"Skip test because of data is not updated on  : " + Calendar.DAY_OF_WEEK + "day");
		}
		verify.verifyAll();
	}
}
