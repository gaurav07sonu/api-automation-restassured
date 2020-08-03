package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CSVReaderUtil;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class InstagramMentions extends APIDriver {
	HashMap<String, String> parameters = new HashMap<String, String>();

	String[][] tickers;

	@BeforeMethod(alwaysRun = true)
	public void initVerify() {
		verify = new APIAssertions();
	}

	@BeforeClass(alwaysRun = true)
	public void setTickers() {
		tickers = CSVReaderUtil.readAllDataAtOnce("finance" + File.separator + "MosaicDataSet.csv");
	}

	@Test(description = "Plotter Web and Social Data Series", groups = { "instagram", "strong_ties" })
	public void instagram() throws CoreCommonException {
		try {
			WebandSocialData data = new WebandSocialData();
			Calendar calNewYork = Calendar.getInstance();
			calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			String URI = APP_URL + FETCH_GRAPH_DATA;
			for (String[] row : tickers) {
				for (String cell : row) {
					cell = cell.toLowerCase();
					parameters.put("singleapi", "false");
					parameters.put("head_name", "Instagram Mentions");
					parameters.put("pagetype", "plotter");
					parameters.put("graphtype_original", "instagram");
					parameters.put("graphtype", "instagram");
					parameters.put("query", "All Brands");
					parameters.put("paramtype", "all_list");
					parameters.put("query_insta", "All Brands");
					parameters.put("ticker", cell);

					RequestSpecification spec = formParamsSpec(parameters);
					Response resp = RestOperationUtils.post(URI, null, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray allSeries = respJson.getJSONObject("result").getJSONArray("series");
					for (int i = 0; i < allSeries.length(); i++) {
						JSONArray series = allSeries.getJSONObject(i).getJSONArray("series");
						String seriesName = allSeries.getJSONObject(i).getString("title");
						JSONArray value = series.getJSONArray(series.length() - 1);
						double timestamp = value.getDouble(0);
						int digit = (int) (timestamp / 1000);
						CommonUtil util = new CommonUtil();
						String date = util.convertTimestampIntoDate(digit);
						// String currentDate = util.dateValidationForChart();
						String str = data.getDate(2);
						if (!date.contains(str))
							str = data.getDate(3);
						if (!date.contains(str))
							str = data.getDate(4);

						if (!date.contains(str))
							str = data.getDate(7);

						verify.assertEqualsActualContainsExpected(date, str, seriesName);
					}
				}
			}
			verify.verifyAll();

		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

}
