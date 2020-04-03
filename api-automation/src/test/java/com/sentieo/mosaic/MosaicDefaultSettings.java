package com.sentieo.mosaic;

import static com.sentieo.constants.Constants.*;
import java.util.HashMap;

import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class MosaicDefaultSettings extends APIDriver {

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

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = {
			"SCORES_TABLE" }, description = "set default setting", dataProvider = "mosaicsetting", dataProviderClass = DataProviderClass.class)
	public void setDefaultSetting(String metric, String period) throws Exception {
		HashMap<String, String> parameters = new HashMap<String, String>();
		HashMap<String, String> blank = new HashMap<String, String>();
		try {
			parameters.put("get_or_set", "set");
			parameters.put("default_si_metric", metric);
			parameters.put("default_ma_settings", period);
			parameters.put("default_index_weight_metric", "corrScore");
			parameters.put("default_alignment_mode", "ma");
			JSONObject resp=defaultSetting(parameters);
			
			 resp=defaultSetting(blank);
			
			String default_index_weight_metric=resp.getJSONObject("result").getString("default_index_weight_metric");
			String default_si_metric=resp.getJSONObject("result").getString("default_si_metric");
			String default_ma_settings=resp.getJSONObject("result").getString("default_ma_settings");
			String default_alignment_mode=resp.getJSONObject("result").getString("default_alignment_mode");
			
			
			String actualWeightMetrci=parameters.get("default_index_weight_metric");
			String actualSI=parameters.get("default_si_metric");

			String actualMA=parameters.get("default_ma_settings");

			String actualAlignmentMode=parameters.get("default_alignment_mode");

			verify.assertEqualsActualContainsExpected(actualWeightMetrci, default_index_weight_metric, "verify default index setting");
			verify.assertEqualsActualContainsExpected(actualSI, default_si_metric, "verify SI Metric");

			verify.assertEqualsActualContainsExpected(actualMA, default_ma_settings, "verify MA period");

			verify.assertEqualsActualContainsExpected(actualAlignmentMode, default_alignment_mode, "verify alignment mode");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public JSONObject defaultSetting(HashMap<String, String> parameters) throws CoreCommonException {
		JSONObject respJson=null;
		try {
			String URI = USER_APP_URL + SET_MOSAIC_DEFAULT_SETTINGS;
			RequestSpecification spec = formParamsSpec(parameters);
			Response resp = RestOperationUtils.post(URI, null, spec, parameters);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			return respJson;
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in mosaic default settings  catch " + e.toString());
		}
		
		return respJson;

	}

}
