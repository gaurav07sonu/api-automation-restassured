package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;

public class TestPENotUpdatingBug53404 extends APIDriver {

	@Test(description = "Check latest data points for daily series", dataProvider = "s&pPE", dataProviderClass = DataProviderClass.class)
	public void PEIsNotUpdated(String shift, String ticker) throws Exception {
		CommonUtil obj = new CommonUtil();
		double timestamp;
		int digit;
		String expectedDate = "";
		CommonUtil util = new CommonUtil();
		String date = "";
		String expectedTitle = "";
		String expectedTitleSP = "";
		JSONArray peNTMValue = null;
		JSONArray SP500NTMValue = null;
		String seriesTitleSP = "";
		JSONArray SP500NTM = null;
		Calendar calNewYork = Calendar.getInstance();
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek != 1 && dayofweek != 7) {
			if (dayofweek == 2)
				expectedDate = obj.getDate(-3);
			else
				expectedDate = obj.getDate(0);
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
			parameters.put("pagetype", "plotter");
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
				SP500NTM = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
						.getJSONArray("series");
				seriesTitleSP = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(1)
						.getString("title");

				if (shift.contains("blended")) {
					expectedTitle = splitTicker[0].toUpperCase() + " NTM - TWA P/E";
					expectedTitleSP = "SP500 NTM - TWA P/E";
				}

				else {
					expectedTitle = splitTicker[0].toUpperCase() + " NTM P/E";
					expectedTitleSP = "SP500 NTM P/E";
				}

				verify.assertEqualsActualContainsExpected(seriesTitle, expectedTitle, "verify added series title");
				verify.assertEqualsActualContainsExpected(seriesTitleSP, expectedTitleSP, "verify added series title");
				if (PENTM.length() != 0 || PENTM != null) {
					peNTMValue = PENTM.getJSONArray(PENTM.length() - 1);
					timestamp = peNTMValue.getDouble(0);
					digit = (int) (timestamp / 1000);
					date = util.convertTimestampIntoDate(digit);
					if (!date.contains(expectedDate))
						expectedDate = obj.getDate(-1);
					verify.compareDates(date, expectedDate, "Verify the Current Date Point for P/E series");

				}
				if ((SP500NTM.length() != 0) && (SP500NTM != null)) {
					SP500NTMValue = SP500NTM.getJSONArray(SP500NTM.length() - 1);
					timestamp = SP500NTMValue.getDouble(0);
					digit = (int) (timestamp / 1000);
					util = new CommonUtil();
					date = util.convertTimestampIntoDate(digit);
					if (!date.contains(expectedDate))
						expectedDate = obj.getDate(-1);
					
					else if (!date.contains(expectedDate))
						expectedDate = obj.getDate(0);
					else
						verify.compareDates(date, expectedDate,
								"Verify the Current Date Point for PS&P 500 NTM - TWA P/E");
				}

			}
		} else {
			ExtentTestManager.getTest().log(LogStatus.INFO,
					"Skip test because of data is not updated on  : " + Calendar.DAY_OF_WEEK + "day");
		}
		verify.verifyAll();
	}

}
