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
import com.sentieo.utils.CoreCommonException;

public class Volume extends APIDriver {
	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();
	// public ArrayList<String> tickers = new
	// ArrayList<String>(Arrays.asList("aapl", "amzn"));
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

	//@Test(groups = "sanity", description = "Match stock price plotter series and stream call ")
	public void volume() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_CURRENT_STOCK_DATA;
			for (String[] row : tickers) {
				for (String cell : row) {
					parameters.put("summary", "true");
					parameters.put("yearly", "1");
					parameters.put("new_wl", "true");
					parameters.put("ticker", cell);
					RequestSpecification spec = queryParamsSpec(parameters);
					Response resp = RestOperationUtils.get(URI, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyStatusCode(apiResp.getStatusCode(), 200);
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					Double volume = respJson.getJSONObject("result").getDouble("volume");
					long currentstock_Volume = volume.longValue();

					String stock_URI = APP_URL + FETCH_GRAPH_DATA;
					HashMap<String, String> stock_Parameters = new HashMap<String, String>();
					stock_Parameters.put("head_name", "Volume");
					stock_Parameters.put("pagetype", "plotter");
					stock_Parameters.put("graphtype", "stock");
					stock_Parameters.put("volume_only", "true");
					stock_Parameters.put("stack", "0");
					stock_Parameters.put("dma", "0");
					stock_Parameters.put("median", "0");
					stock_Parameters.put("yoy_rt", "0");
					stock_Parameters.put("qoq_rt", "0");
					stock_Parameters.put("outliers", "0");
					stock_Parameters.put("day_dma", "0");
					stock_Parameters.put("yUnit", "undefined");
					stock_Parameters.put("ticker", cell);
					stock_Parameters.put("freq_set1", "");
					stock_Parameters.put("freq_type1", "mean");
					stock_Parameters.put("counter", "1");
					stock_Parameters.put("loc", "app");

					RequestSpecification specVolume = queryParamsSpec(stock_Parameters);
					Response resp2 = RestOperationUtils.get(stock_URI, specVolume, stock_Parameters);
					APIResponse apiRespVolume = new APIResponse(resp2);
					JSONObject respJsonVolume = new JSONObject(apiRespVolume.getResponseAsString());
					verify.verifyStatusCode(apiRespVolume.getStatusCode(), 200);
					verify.verifyEquals(respJsonVolume.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray values = respJsonVolume.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					Double value = (Double) values.getJSONArray(values.length() - 1).get(1);
					long plotterVolume = value.longValue();
					verify.verifyEquals(currentstock_Volume, plotterVolume, "Verify volume values ");

				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}
}
