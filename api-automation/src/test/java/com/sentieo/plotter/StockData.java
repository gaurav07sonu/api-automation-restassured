package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class StockData extends APIDriver {
	APIAssertions verify = new APIAssertions();
	HashMap<String, String> parameters = new HashMap<String, String>();
	public ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("aapl", "amzn"));

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

	@Test(groups = "sanity", description = "Match stock price plotter series and stream call ")
	public void currentStockData() throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_CURRENT_STOCK_DATA;
			for (int i = 0; i < tickers.size(); i++) {
				parameters.put("summary", "true");
				parameters.put("yearly", "1");
				parameters.put("new_wl", "true");
				parameters.put("ticker", tickers.get(i));
				RequestSpecification spec = queryParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray intraday = respJson.getJSONObject("result").getJSONArray("intraday");
				String intradayValue = intraday.getJSONArray(intraday.length() - 1).toString();
				JSONArray currentstockValue = intraday.getJSONArray(intraday.length() - 1);
				String[] value_Parts = intradayValue.split(",");
				String current_StockValue = value_Parts[1].replaceAll("]", "");
				double timestamp = currentstockValue.getDouble(0);
				int digit = (int) (timestamp / 1000);
				CommonUtil util = new CommonUtil();
				String currentStockDAte = util.convertTimestampIntoDate(digit);
				value_Parts = currentStockDAte.split(" ");
				String current_StockDate = value_Parts[0];
				String tickerName = respJson.getJSONObject("result").getString("ticker_list");
				verify.assertEqualsActualContainsExpected(tickerName, tickers.get(i), "Verify Ticker");

				String stock_URI = APP_URL + FETCH_GRAPH_DATA;
				HashMap<String, String> stock_Parameters = new HashMap<String, String>();
				stock_Parameters.put("head_name", "Stock Price");
				stock_Parameters.put("pagetype", "plotter");
				stock_Parameters.put("graphtype", "stock");
				stock_Parameters.put("stack", "0");
				stock_Parameters.put("dma", "0");
				stock_Parameters.put("median", "0");
				stock_Parameters.put("yoy_rt", "0");
				stock_Parameters.put("qoq_rt", "0");
				stock_Parameters.put("outliers", "0");
				stock_Parameters.put("day_dma", "0");
				stock_Parameters.put("yUnit", "undefined");
				stock_Parameters.put("ticker", tickers.get(i));
				stock_Parameters.put("freq_set1", "");
				stock_Parameters.put("freq_type1", "mean");
				stock_Parameters.put("counter", "1");
				stock_Parameters.put("loc", "app");

				RequestSpecification spec2 = queryParamsSpec(stock_Parameters);
				Response resp2 = RestOperationUtils.get(stock_URI, spec2, stock_Parameters);
				APIResponse apiRespStock = new APIResponse(resp2);
				JSONObject respJsonStock = new JSONObject(apiRespStock.getResponseAsString());
				verify.verifyStatusCode(apiRespStock.getStatusCode(), 200);
				verify.verifyEquals(respJsonStock.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONArray values = respJsonStock.getJSONObject("result").getJSONArray("series").getJSONObject(0)
						.getJSONArray("series");
				JSONArray value = values.getJSONArray(values.length() - 1);
				value_Parts = intradayValue.split(",");
				String plotter_StockValue = value_Parts[1].replaceAll("]", "");
				timestamp = value.getDouble(0);
				digit = (int) (timestamp / 1000);
				String plotterDate = util.convertTimestampIntoDate(digit);
				value_Parts = plotterDate.split(" ");
				String stock_Date = value_Parts[0];
				verify.assertEqualsActualContainsExpected(current_StockDate, stock_Date, "Verify Stock dates");
				verify.verifyEquals(current_StockValue, plotter_StockValue, "Verify stock values ");
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}

	}

}
