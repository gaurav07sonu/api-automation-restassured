package com.sentieo.user;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;

public class AlertNotifiction extends APIDriver {

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = { "sanity",
			"test" }, description = "initial-loading", dataProvider = "alertType", dataProviderClass = DataProviderClass.class)
	public void alertNotification(String alertType) throws Exception {
		try {
			String URI = USER_APP_URL + NEW_ALERT_NOTIFICATION;
			if (URI.contains("app") || URI.contains("staging") || URI.contains("app2")) {
				HashMap<String, String> parameters = new HashMap<String, String>();
				parameters.put("alert_type", alertType);
				parameters.put("start", "0");
				parameters.put("start_new", "0");
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
