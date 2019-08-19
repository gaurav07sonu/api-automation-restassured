package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class KeyMultiples extends APIDriver {

	APIAssertions verify = new APIAssertions();
	public ArrayList<String> tickers = new ArrayList<String>(Arrays.asList("aapl", "amzn"));
	List<String> l1 = new ArrayList<String>();
	List<String> l2 = new ArrayList<String>();
	List<String> l3 = new ArrayList<String>();
	List<String> l4 = new ArrayList<String>();

	String peValue;
	String evSalesValue;
	String evEbitdaValue;
	String fetchGraphEbitdaValue;
	String fetchGraphEvSalesValue;
	String fetchGraphPEValue;
	String gethHeadName = null;

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

	@Test(groups = "sanity", description = "Match stock price plotter series and stream call ", dataProvider = "fetch_data", dataProviderClass = DataProviderClass.class)
	public void fetchGraph(String headName, String ratio, String dataSource) throws CoreCommonException {
		try {
			gethHeadName = headName;
			HashMap<String, String> parameters = new HashMap<String, String>();
			String fetchGraphURI = APP_URL + FETCH_GRAPH_DATA;
			for (int i = 0; i < tickers.size(); i++) {
				parameters.put("head_name", headName);
				parameters.put("pagetype", "plotter");
				parameters.put("graphtype", "newratioestimate");
				parameters.put("ptype", "carry");
				parameters.put("ratio_name", "Current Year");
				parameters.put("datasource", "rf");
				parameters.put("stack", "0");
				parameters.put("dma", "0");
				parameters.put("median", "0");
				parameters.put("yoy_rt", "0");
				parameters.put("qoq_rt", "0");
				parameters.put("outliers", "0");
				parameters.put("day_dma", "0");
				parameters.put("yUnit", "xa");
				parameters.put("freq_set1", "");
				parameters.put("freq_type1", "mean");
				parameters.put("ratio", ratio);
				parameters.put("ticker", tickers.get(i));

				RequestSpecification spec = queryParamsSpec(parameters);
				Response resp = RestOperationUtils.get(fetchGraphURI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);

				if (headName.contains("EV/EBITDA")) {
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					Double ebitdaValue = (Double) values.getJSONArray(values.length() - 1).get(1);
					fetchGraphEbitdaValue = new Double(ebitdaValue).toString();
					testKeyMultiples(i);
					verify.verifyEquals(evEbitdaValue, fetchGraphEbitdaValue, "Verify EV/EBITDA values ");
				}
				if (headName.contains("EV/Sales")) {
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					Double evValue = (Double) values.getJSONArray(values.length() - 1).get(1);
					DecimalFormat df2 = new DecimalFormat("#.#");
					evValue = Double.valueOf(df2.format(evValue));
					fetchGraphEvSalesValue = new Double(evValue).toString();
					testKeyMultiples(i);
					verify.verifyEquals(evSalesValue, fetchGraphEvSalesValue, "Verify EV Sales values ");

				}
			}
			verify.verifyAll();
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}


	}

	public void testKeyMultiples(int i) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		String URI = APP_URL + FETCH_CURRENT_STOCK_DATA;
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

		JSONObject values = respJson.getJSONObject("result").getJSONObject("summary").getJSONObject("data");
		int year = Calendar.getInstance().get(Calendar.YEAR);

		Iterator<String> keyItr = values.keys();
		while (keyItr.hasNext()) {
			String key = keyItr.next();

			if (key.equalsIgnoreCase("ev_sales") && (gethHeadName.contains("EV/Sales"))) {
				JSONObject seriesValue = (JSONObject) values.get(key);
				double currentYearValueAsDouble = seriesValue.getDouble(String.valueOf(year));
				DecimalFormat df2 = new DecimalFormat("#.#");
				currentYearValueAsDouble = Double.valueOf(df2.format(currentYearValueAsDouble));
				evSalesValue = new Double(currentYearValueAsDouble).toString();

			}

			if (key.equalsIgnoreCase("ev_ebitda") && (gethHeadName.contains("EV/EBITDA"))) {
				JSONObject seriesValue = (JSONObject) values.get(key);
				double currentYearValueAsDouble = seriesValue.getDouble(String.valueOf(year));
				DecimalFormat df2 = new DecimalFormat("#.##");
				currentYearValueAsDouble = Double.valueOf(df2.format(currentYearValueAsDouble));
				evEbitdaValue = new Double(currentYearValueAsDouble).toString();
			}
		}
	}
}
