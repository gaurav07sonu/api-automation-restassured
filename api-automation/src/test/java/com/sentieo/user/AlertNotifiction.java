package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class AlertNotifiction extends APIDriver {

	String locMobile = "";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@BeforeTest(alwaysRun = true)
	@Parameters({ "loc" })
	public void getLoc(@Optional("loc") String loc) {
		locMobile = loc;
	}

	@Test(groups = { "sanity", "test",
			"mobileMainApp" }, description = "initial-loading", dataProvider = "alertType", dataProviderClass = DataProviderClass.class)
	public void alertNotification(String alertType) throws Exception {
		try {
			String URI = USER_APP_URL + NEW_ALERT_NOTIFICATION;
			if (URI.contains("app") || URI.contains("staging") || URI.contains("app2")) {
				HashMap<String, String> parameters = new HashMap<String, String>();

				parameters.put("alert_type", alertType);
				parameters.put("start", "0");
				parameters.put("start_new", "0");
				if (locMobile.equals("ios")) {
					parameters.put("loc", "ios");
				}
				RequestSpecification spec = formParamsSpec(parameters);
				Response resp = RestOperationUtils.post(URI, null, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				String aler_type = respJson.getJSONObject("result").getString("alert_type");
				verify.assertEqualsActualContainsExpected(aler_type.toLowerCase(), alertType.toLowerCase(),
						"verify alert type");
				JSONArray res = respJson.getJSONObject("result").getJSONArray("res");
				if (!alertType.contains("news")) {
					if (res.length() == 0 || res == null)
						verify.assertTrue(false, "shows blank data for " + alertType);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		finally {
			verify.verifyAll();
		}
	}
}
