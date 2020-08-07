package com.sentieo.docsearchpublicapis;
import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.JSONUtils;

public class docsearchpublicapis extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		//RestAssured.baseURI = PUBLIC_API_URL;
	}
	

	
	//@Test(description = "search 1")
	public void search1() throws Exception {
		try {
			HashMap<String, Object> docTypeParams = new HashMap<String, Object>();
			docTypeParams.put("name", "ef");
			List<HashMap<String, Object>> docTypeList = new ArrayList<>();
			docTypeList.add(docTypeParams);
			HashMap<String, Object> formParams = new HashMap<String, Object>();
			formParams.put("size", "200");
			formParams.put("sort", "filing_date:desc");
			formParams.put("start", "0");
			formParams.put("tickers", "");
			formParams.put("doc_type", docTypeList);
			formParams.put("query", "");

			HashMap<String, String> headerParams = new HashMap<String, String>();
			headerParams.put(XAPIKEY1, X_API_KEY);
			headerParams.put(XUSERKEY1, X_USER_KEY);
			
			String change = jsonUtils.toJson(formParams);
			RequestSpecification spec = requestHeadersFormSpecForPublicApis(change, headerParams);
			Response resp = RestOperationUtils.post(SEARCH, null, spec, formParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 5000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
			System.out.println();
		} catch (JSONException je) {
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
			Thread.sleep(1000);
		}
	}
}
