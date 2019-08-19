package com.sentieo.plotter;

import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

public class IncomeStatement extends APIDriver {

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

	@Test(groups = "sanity", description = "Plotter Income Statement Series", dataProvider = "fetch_graph_data", dataProviderClass = DataProviderClass.class)
	public void incomeStatement(String headName, String subType, String dataSource) throws CoreCommonException {
		try {
			String URI = APP_URL + FETCH_GRAPH_DATA;
			for (int i = 0; i < tickers.size(); i++) {
				parameters.put("head_name", headName);
				parameters.put("pagetype", "plotter");
				parameters.put("graphtype", "financialData");
				parameters.put("subtype", subType);
				parameters.put("periodtype", "Quarterly");
				parameters.put("datasource", dataSource);
				parameters.put("stack", "0");
				parameters.put("dma", "0");
				parameters.put("median", "0");
				parameters.put("yoy_rt", "0");
				parameters.put("qoq_rt", "0");
				parameters.put("outliers", "0");
				parameters.put("day_dma", "0");
				parameters.put("getestimates", "false");
				parameters.put("yUnit", "millions");
				parameters.put("ticker", tickers.get(i));
				parameters.put("freq_set1", "");
				parameters.put("freq_type1", "mean");
				RequestSpecification spec = queryParamsSpec(parameters);
				Response resp = RestOperationUtils.get(URI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONObject getSeries = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0);
				String title = getSeries.getString("title").replaceAll(" ", "");
				String actualTitle = tickers.get(i).toUpperCase() + parameters.get("periodtype")
						+ parameters.get("head_name").replaceAll(" ", "") + parameters.get("datasource").toUpperCase();
				verify.verifyEquals(actualTitle, title, "Verify Series Title");
				verify.verifyAll();
			}
		} catch (Exception e) {
			throw new CoreCommonException(e.getMessage());
		}


	}
}
