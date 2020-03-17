package com.sentieo.dashboard;

import static com.sentieo.constants.Constants.*;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
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

public class CreateDashboard extends APIDriver {

	String viewName = "Automation-View";
	String widget_order = "[\"PriceMonitorWidget_2\",\"DocumentWidget_1\"]";
	static String db_id = "";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

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

	@Test(groups = "sanity", description = "create dashboard", priority = 0)
	public void createNewDashboard() throws Exception {
		String URI = USER_APP_URL + CREATE_DASHBOARD;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		dashboardData.put("widget_list",
				"{\"PriceMonitorWidget_2\":{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"wrapPreference\":\"dont-wrap\",\"infiniteScroll\":true,\"infiniteScrollWaiting\":500,\"restrictOuterScroll\":true,\"disableLinking\":true,\"name\":\"Price Monitor\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":2,\"driveBy\":\"local\",\"wrapPreference\":\"dont-wrap\",\"columnOption1\":\"edt-icon\",\"columnOption2\":\"docsearch-icon\",\"updateUserData\":{\"tickers\":[],\"order\":[],\"wl_mapping\":{},\"wl_id_mapping\":{},\"addTickerWatchlistCount\":1,\"selectedRow\":\"{\\\"lastSelectedGroupID\\\":\\\"\\\",\\\"selectedTicker\\\":\\\"\\\"}\",\"watchlistsState\":{},\"viewData\":{\"header_fields\":[],\"header_fields_value\":[],\"thesis_fields\":[],\"sort\":\"\",\"custom_fields\":[],\"chosen_model_name\":\"\"},\"marketMonitorLoaded\":false},\"displayDensity\":\"compact\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"PriceMonitorWidget_2\"}},\"DocumentWidget_1\":{\"configuration\":{\"settings\":{\"size\":15,\"count\":0,\"start\":0,\"endOfResult\":false,\"filterObj\":{},\"defaultFilterObj\":{\"ef\":[],\"tt\":[],\"ppt\":[],\"ni\":[],\"gbf\":[],\"rr\":[],\"sd\":[],\"nw\":[]},\"pticker_setting\":false,\"name\":\"All Documents\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true"
						+ ",\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"}"
						+ ",\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"DocumentWidget_1\"}}}");

		dashboardData.put("widget_order", widget_order);
		dashboardData.put("dashboard_name", viewName);
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");
			JSONObject result = respJson.getJSONObject("result");
			if (result.length() == 0 || result == null)
				verify.assertTrue(false, "API shows blank data :");
			else {
				JSONObject embed = result.getJSONObject("embed");
				if (embed.length() == 0 || embed == null)
					verify.assertTrue(false, "embed object is null or blank");

				else {
					String dashboard_name = result.getJSONObject("embed").getString("dashboard_name");
					verify.assertEqualsActualContainsExpected(viewName.toLowerCase(), dashboard_name.toLowerCase(),
							"verify view name");
					db_id = respJson.getJSONObject("result").getJSONObject("embed").getString("db_id");
					ExtentTestManager.getTest().log(LogStatus.INFO," Dashboard id is : "+db_id);
				}

				JSONObject config = respJson.getJSONObject("result").getJSONObject("config");
				if (config.length() == 0 || config == null)
					verify.assertTrue(false, "embed object is null or blank");
				else {
					String order = respJson.getJSONObject("result").getJSONObject("config").getJSONArray("order")
							.toString();
					verify.assertEqualsActualContainsExpected(widget_order, order, "verify widget order");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 1)
	public void getDashboardList() throws Exception {
		String URI = USER_APP_URL + GET_DASHBOARD_LIST;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject result = respJson.getJSONObject("result");
				if (result.length() == 0 || result == null)
					verify.assertTrue(false, "Delete dashboard API shows blank data");
				else {
					JSONArray db_list = result.getJSONArray("db_list");
					for (int i = 0; i < db_list.length(); i++) {
						String dashboard_name = db_list.getJSONObject(i).getString("dashboard_name").toLowerCase();
						if (dashboard_name.equalsIgnoreCase(viewName.toLowerCase())) {
							verify.assertTrue(true, viewName + " available in dashboard list :  ");
							break;
						}
					}
					JSONObject first_object = result.getJSONObject("first_object");
					String order = first_object.getJSONArray("order").toString().trim().toLowerCase();
					verify.assertEqualsActualContainsExpected(widget_order.trim().toLowerCase(), order,
							"verify widget order");
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "sanity", description = "delete dashboard", priority = 2)
	public void deleteDashboard() throws Exception {
		boolean deleteView = true;
		String URI = USER_APP_URL + DELETE_DASHBOARD;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		dashboardData.put("dashboard_id", db_id);
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			Thread.sleep(1000);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");
			if (apiResp.getStatusCode() == 200) {
				
				JSONObject result = respJson.getJSONObject("result");
				if (result.length() == 0 || result == null)
					verify.assertTrue(false, "Delete dashboard API shows blank data");
				else {
					JSONArray db_list = result.getJSONArray("db_list");
					for (int i = 0; i < db_list.length(); i++) {
						String dashboard_name = db_list.getJSONObject(i).getString("dashboard_name").toLowerCase();
						if (dashboard_name.equalsIgnoreCase(viewName.toLowerCase())) {
							verify.assertTrue(false, viewName + " not delete from dashboard");
							deleteView = false;
						}
					}
					if (deleteView)
						verify.assertTrue(deleteView, "verify view is deleted");
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in deleteDashboard catch "+e.toString());
		} finally {
			verify.verifyAll();
		}

	}

}
