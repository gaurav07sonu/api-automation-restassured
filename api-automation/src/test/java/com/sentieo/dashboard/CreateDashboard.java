package com.sentieo.dashboard;

import static com.sentieo.constants.Constants.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.*;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class CreateDashboard extends APIDriver {

	public static ArrayList<String> expectedWidgets = new ArrayList<String>(Arrays.asList("SavedSearchWidget_1",
			"PlotterWidget_3", "PriceMonitorWidget_2", "DocumentWidget_1", "RSSWidget_1"));

	static String selectedWatchlist = "";
	List<String> aa = new ArrayList<String>();
//	String viewName = "Automation-View";
	String viewName = "PMTEST";
	String widgetList = "{\"SavedSearchWidget_1\":{\"configuration\":{\"settings\":{\"size\":15,\"count\":0,\"start\":0,\"endOfResult\":false,\"name\":\"Saved Search\",\"filterObj\":{}},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"SavedSearchWidget_1\"}},\"PlotterWidget_3\":{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"name\":\"Plotter\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"widgetTitle\":\"Plotter\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"PlotterWidget_3\"}},\"PriceMonitorWidget_2\":{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"wrapPreference\":\"dont-wrap\",\"infiniteScroll\":true,\"infiniteScrollWaiting\":500,\"restrictOuterScroll\":true,\"disableLinking\":true,\"name\":\"Price Monitor\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":2,\"driveBy\":\"local\",\"wrapPreference\":\"dont-wrap\",\"columnOption1\":\"edt-icon\",\"columnOption2\":\"docsearch-icon\",\"updateUserData\":{\"wl_mapping\":{},\"wl_id_mapping\":{},\"selectedRow\":\"{\\\"lastSelectedGroupID\\\":\\\"\\\",\\\"selectedTicker\\\":\\\"\\\"}\",\"watchlistsState\":{},\"viewData\":{},\"marketMonitorLoaded\":false},\"displayDensity\":\"compact\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"PriceMonitorWidget_2\"}},\"DocumentWidget_1\":{\"configuration\":{\"settings\":{\"size\":20,\"count\":0,\"start\":0,\"endOfResult\":false,\"filterObj\":{},\"defaultFilterObj\":{\"bd\":[],\"gbf\":[],\"rr\":[],\"ni\":[],\"tt\":[],\"ef\":[],\"jr\":[],\"ppt\":[],\"nw\":[],\"reg\":[],\"sd\":[]},\"pticker_setting\":true,\"name\":\"All Documents\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"DocumentWidget_1\"}},\"RSSWidget_1\":{\"configuration\":{\"settings\":{\"size\":20,\"count\":0,\"start\":0,\"endOfResult\":false,\"filterObj\":{\"rss\":{\"\":{\"\":{\"feed_id_599\":{\"value\":[599]}}}}},\"defaultFilterObj\":{\"\":{}},\"pticker_setting\":true,\"name\":\"RSS Feeds\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"RSSWidget_1\"}}}";
	String widget_order = "[\"SavedSearchWidget_1\",\"PlotterWidget_3\",\"PriceMonitorWidget_2\",\"DocumentWidget_1\",\"RSSWidget_1\"]";
	static String plotter_id = "";
	static String plotter_name = "";
	static String widget_ID = "";
	static String savedSearchwidgetID = "";
	static String db_id = "";
	static String watchlistID = "";
	static String saveSearchName = "sales" + new Date().getTime();
	static org.json.JSONArray watchlistTickers = null;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "lol", description = "create dashboard", priority = 0)
	public void createNewDashboard() throws Exception {
		String URI = USER_APP_URL + CREATE_DASHBOARD;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		dashboardData.put("widget_list", widgetList);
		dashboardData.put("widget_order", widget_order);
		dashboardData.put("dashboard_name", viewName);
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
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
						ExtentTestManager.getTest().log(LogStatus.INFO, " Dashboard id is : " + db_id);
					}

					JSONObject config = respJson.getJSONObject("result").getJSONObject("config");
					if (config.length() == 0 || config == null)
						verify.assertTrue(false, "embed object is null or blank");
					else {
						String order = config.getJSONArray("order").toString();
						verify.assertEqualsActualContainsExpected(widget_order, order, "verify widget order");
						JSONObject widgetlist = config.getJSONObject("widgetlist");
						org.json.JSONArray widgets = widgetlist.toJSONArray(widgetlist.names());

						for (int i = 0; i < widgets.length(); i++) {
							JSONObject configuration = widgets.getJSONObject(i).getJSONObject("configuration");
							JSONObject configurable = configuration.getJSONObject("configurable");
							if (configurable.has("widgetTitle")) {
								String widgetTitle = configurable.getString("widgetTitle").toLowerCase().trim();
								if (widgetTitle.equalsIgnoreCase("plotter")) {
									widget_ID = configuration.getString("widgetID");
									break;
								}

							}
						}
						System.out.println(widget_ID);
					}
				}
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in createNewDashboard catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "update", description = "create dashboard", priority = 1)
	@SuppressWarnings("unchecked")
	public void updateWatchlist() {
		String URI = USER_APP_URL + UPDATE_DB_TOKEN_LIST;
		try {
			selectedWatchlist = getRandomWatchlist();
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			JSONObject child = new JSONObject();
			child.put("name", "W#" + selectedWatchlist + "");
			child.put("id", watchlistID);
			child.put("type", "watchlist");
			child.put("token_type", "watchlist");
			child.put("label", selectedWatchlist);
			JSONArray array = new JSONArray();
			array.add(child);
			dashboardData.put("update_active", "1");
			dashboardData.put("dashboard_id", db_id);
			dashboardData.put("update_token", "1");
			dashboardData.put("active_token_list", array.toString());
			dashboardData.put("token_list", array.toString());
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println("update " + respJson);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test(groups = "load", description = "fetch_trading_ratios", priority = 2)
	public void updatePlotterDashboardWidget() throws Exception {
		getPlotterID();
		HashMap<String, String> tickerData = new HashMap<String, String>();
		String value = "{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"name\":\"Plotter\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"widgetTitle\":\"Plotter\",\"linkMode\":\"single\",\"plotter_id\":\""
				+ plotter_id
				+ "\",\"sentieo_plot\":false,\"sentieo_template\":false},\"data\":{\"tickers\":[]},\"widgetID\":\""
				+ widget_ID + "\"}}";
		tickerData.put("widgetConfig", value);
		tickerData.put("type", "singleWidget");
		tickerData.put("wid", widget_ID);
		tickerData.put("dashboard_id", db_id);
		updateWidget(tickerData);
	}

	@Test(groups = "qwer", description = "fetch_trading_ratios", priority = 3)
	public void updatesavedSearchDashboardWidget() throws Exception {
		save_user_search(saveSearchName);
		String savedSearchID = load_user_search(saveSearchName);
		saveSearchName = "\"" + saveSearchName + "\"";

		savedSearchID = "\"" + savedSearchID + "\"";
		HashMap<String, String> tickerData = new HashMap<String, String>();
		String config = "{\"configuration\":{\"settings\":{\"size\":15,\"count\":0,\"start\":0,\"endOfResult\":false,\"name\":"
				+ saveSearchName + ",\"filterObj\":{\"user_saved_search\":[" + savedSearchID
				+ "],\"uss_data\":{\"sector\":\"\",\"exp_avg_result\":null,\"table_search\":false,\"email_alert\":false,\"period\":null,\"watchlist_tickers\":\"\",\"query\":\"sales\",\"id\":"
				+ savedSearchID
				+ ",\"filter_obj\":{\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"other\":{},\"date\":{},\"ticker\":{}},\"size\":\"\",\"sort_option\":\"filing_date:desc\",\"source\":\"\",\"multi_field_bit\":0,\"sensitivity_setting\":null,\"sqs\":{},\"watchlist_ids\":[],\"etype\":[],\"table_search_bit\":0,\"search_id\":null,\"sort\":\"filing_date:desc\",\"notification_alert\":false,\"entity_info\":{},\"pticker_setting\":false,\"geography\":{},\"subsector\":\"\",\"email_alert_type\":0,\"name\":"
				+ saveSearchName
				+ ",\"tickers_filters\":\"\",\"nw_pticker\":false,\"fqs\":{},\"tickers\":\"\"}},\"infiniteScroll\":true,\"restrictOuterScroll\":true,\"tokenList\":[],\"noDataAvailable\":false},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"linkMode\":\"single\"},\"data\":{\"tickers\":[]},\"widgetID\":\"SavedSearchWidget_1\"}}";
		tickerData.put("widgetConfig", config);
		tickerData.put("type", "singleWidget");
		tickerData.put("wid", "SavedSearchWidget_1");
		tickerData.put("dashboard_id", db_id);
		updateWidget(tickerData);
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 4)
	public void shareDashboard() {
		try {
			String URI = USER_APP_URL + DASHBOARD_SHARE;
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("entity_id", db_id);
			dashboardData.put("entity_type", "SentieoDashboard");
			dashboardData.put("share_with_list",
					"[{\"shared_with_type\":\"user\",\"shared_with_name\":\"devesh.arora\",\"shared_with_display_name\":\"Devesh Arora\",\"access_level\":\"view\",\"state\":\"active\",\"email\":\"1\"}]");
			dashboardData.put("email", "1");
			dashboardData.put("email_img", "data:image/png;base64,iVBOR");
			dashboardData.put("dashboard_name", viewName);
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 5)
	public void testLogin() throws CoreCommonException {
		RestAssured.baseURI = APP_URL;
		String URI = USER_APP_URL + LOGIN_URL;
		HashMap<String, String> loginData = new HashMap<String, String>();
		loginData.put("email", "devesh.arora@sentieo.com");
		loginData.put("password", "Tester@1234");

		RequestSpecification spec = loginSpec(loginData);
		Response resp = RestOperationUtils.login(URI, null, spec, loginData);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		System.out.println(respJson);
		username = respJson.getJSONObject("result").getString("username");
		apid = resp.getCookie("apid");
		usid = resp.getCookie("usid");
		if (apid.isEmpty() || usid.isEmpty()) {
			System.out.println("Login failed");
			System.exit(1);
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 6)
	public void verifySharedDashboard() throws Exception {
		String URI = USER_APP_URL + GET_DASHBOARD_LIST;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/dd/yy");
		LocalDateTime now = LocalDateTime.now();
		String currentDate = dtf.format(now);
		CommonUtil util = new CommonUtil();
		String date = "";
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONObject result = respJson.getJSONObject("result");
				if (result.length() == 0 || result == null)
					verify.assertTrue(false, "API shows blank data");
				else {
					org.json.JSONArray shared_dashboards = result.getJSONArray("shared_dashboards");
					for (int i = 0; i < shared_dashboards.length(); i++) {
						String dashboard_name = shared_dashboards.getJSONObject(i).getString("dashboard_name")
								.toLowerCase();
						if (dashboard_name.equalsIgnoreCase(viewName.toLowerCase())) {
							double timeStamp = shared_dashboards.getJSONObject(i).getDouble("created_ts");
							int digit = (int) (timeStamp / 1000);
							date = util.convertTimestampIntoDate(digit);
							System.out.println(date);
							verify.compareDates(date, currentDate, "Verify the created date for dahsboard ");
							verify.assertTrue(true, viewName + "Verify shared dashboard view ");
							break;
						} else {
							if (i == shared_dashboards.length())
								verify.assertTrue(false, viewName + "Not found ");
						}
					}

				}
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in getDashboardList catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "share", description = "get dashboard items", priority = 7)
	public void getSharedDashboardData() throws Exception {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/dd/yy");
			LocalDateTime now = LocalDateTime.now();
			String currentDate = dtf.format(now);
			CommonUtil util = new CommonUtil();
			String date = "";
			String URI = USER_APP_URL + DASHBOARD_DATA;
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("dashboard_id", db_id);
			// dashboardData.put("dashboard_id", db_id);
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
				JSONObject result = respJson.getJSONObject("result");
				String dashboardName = result.getString("dashboard_name");
				verify.assertEqualsActualContainsExpected(dashboardName, viewName, "Verify shared view name");
				double timeStamp = result.getDouble("created_ts");
				int digit = (int) (timeStamp / 1000);
				date = util.convertTimestampIntoDate(digit);
				verify.compareDates(date, currentDate, "Verify the created date for dahsboard ");
				JSONObject widgetList = result.getJSONObject("widgetlist");
				List<String> actualWidgest = getWidgetList(widgetList);
				verify.assertEquals(actualWidgest, expectedWidgets, "Verify shared widgest", true);

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "clone", description = "get dashboard items", priority = 8)
	public void clonedashboard() {
		try {
			String URI = USER_APP_URL + CLONE_DASHBOARD;
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("dashboard_id", db_id);
			dashboardData.put("dashboard_name", viewName + " " + "- Copy");
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			verify.verifyStatusCode(statusCode, 200);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				JSONObject result = respJson.getJSONObject("result");

				JSONObject widgetList = result.getJSONObject("widgetlist");

				List<String> actualWidgest = getWidgetList(widgetList);
				verify.assertEquals(actualWidgest, expectedWidgets, "Verify shared widgest", true);

				// org.json.JSONArray order = result.getJSONArray("order");
//				List<String> widgetOrder = getWidgetOrder(order);

				String dashboardName = result.getString("dashboard_name");
				String dash = viewName + " " + "-" + "Copy";
				verify.assertEqualsActualContainsExpected(dashboardName, dash, "Verify dashboard name after cloning");

				org.json.JSONArray token_list = result.getJSONArray("token_list");
				String tokenType = token_list.getJSONObject(0).getString("token_type").toLowerCase().trim();
				if (tokenType.equalsIgnoreCase("watchlist")) {
					String watchName = token_list.getJSONObject(0).getString("name");
					String watchExpected = selectedWatchlist + " " + "-" + " " + "@" + " " + viewName + " " + "-" + " "
							+ "Copy";
					verify.assertEqualsActualContainsExpected(watchName, watchExpected,
							"Verify watchlist name after cloning dashboard");
				}
				List<String> tickers = getTokenTickers(token_list);
				List<String> watchlistTic = convertJSONArray(watchlistTickers);

				verify.assertEquals(tickers, watchlistTic, "Verify watchlist tickers", true);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	@Test(groups = "clone", description = "get dashboard items", priority = 9)
	public void verifyCloneWatchlist() {
		try {
			String option = selectedWatchlist + " @" + " Copy Of " + viewName;
			boolean result = verifyWatchlist(option);
			verify.assertTrue(result, "Verify Clone Watchlist : ");
			verify.verifyAll();
		} catch (Exception e) {

		}
	}

//	@Test(groups = "sanity", description = "delete dashboard", priority = 2)
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
					org.json.JSONArray db_list = result.getJSONArray("db_list");
					for (int i = 0; i < db_list.length(); i++) {
						String dashboard_name = db_list.getJSONObject(i).getString("dashboard_name").toLowerCase();
						if (dashboard_name.equalsIgnoreCase(viewName.toLowerCase())) {
							ExtentTestManager.getTest().log(LogStatus.INFO,
									"db_id is : " + db_list.getJSONObject(i).getString("db_id").toString());
							verify.assertTrue(false, viewName + " not delete from dashboard");
							deleteView = false;
						}
					}
					if (deleteView)
						verify.assertTrue(deleteView, "verify view is deleted");
				}
			}
		} catch (Exception e) {
			ExtentTestManager.getTest().log(LogStatus.INFO, "in getDashboardList catch " + e.toString());
			throw new CoreCommonException(e.getMessage());
		} finally {
			verify.verifyAll();
		}

	}

	public org.json.JSONArray getUserWatchlists() throws Exception {
		org.json.JSONArray getWatchlists = null;
		String URI = USER_APP_URL + FETCH_USER_PORTFOLIO;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		RequestSpecification spec = formParamsSpec(tickerData);
		Response resp = RestOperationUtils.get(URI, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		int statusCode = apiResp.getStatusCode();
		verify.verifyStatusCode(statusCode, 200);
		if (statusCode == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");
			getWatchlists = respJson.getJSONObject("result").getJSONArray("watchlists");
		}
		verify.verifyAll();
		return getWatchlists;
	}

	public void save_user_search(String saveSearchName) throws CoreCommonException {
		try {
			String URI = USER_APP_URL + SAVE_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("counter", "1");
			queryParams.put("query", "sales");
			queryParams.put("filters",
					"{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"date\":{},\"other\":{}}");
			queryParams.put("force_save", "true");
			queryParams.put("name", saveSearchName);
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			int statusCode = apiResp.getStatusCode();
			System.out.println(statusCode);
			verify.verifyStatusCode(statusCode, 200);
			verify.verifyResponseTime(resp, 10000);
			if (statusCode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyEquals(respJson.getJSONObject("result").getInt("save_status"), 1, "verify saved status");
				verify.verifyEquals(respJson.getJSONObject("result").getString("message"), "Successfully Saved",
						"Verify status message");
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
	}

	public String load_user_search(String option) throws CoreCommonException {
		String saveSearchID = "";
		try {
			String URI = USER_APP_URL + LOAD_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (respJson.getJSONObject("result").getJSONArray("userss").length() > 0) {
					org.json.JSONArray data = respJson.getJSONObject("result").getJSONArray("userss");
					for (int i = 0; i < data.length(); i++) {
						String name = data.getJSONObject(i).getString("name");
						if (option.equalsIgnoreCase(name)) {
							saveSearchID = data.getJSONObject(i).getString("id");
							return saveSearchID;
						}

					}

				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
		return saveSearchID;
	}

	public void updateWidget(HashMap<String, String> tickerData) throws CoreCommonException {
		String URI = USER_APP_URL + UPDATE_DASHBOARD_WIDGET;
		try {
			System.out.println();
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
//			String id = respJson.getJSONObject("result").getJSONObject("graphobj").getString("plotter_id");
//			verify.assertEqualsActualContainsExpected(id, plotter_id, "Verify Plotter ID : ");
//			String name = respJson.getJSONObject("result").getJSONObject("graphobj").getString("name");
//			verify.assertEqualsActualContainsExpected(name, plotter_name, "Verify Plotter name: ");
		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		} finally {
			verify.verifyAll();

		}
	}

	public List<String> getWidgetList(JSONObject widgetList) {
		List<String> actualWidgest = new ArrayList<String>();
		org.json.JSONArray widgets = widgetList.toJSONArray(widgetList.names());
		for (int i = 0; i < widgets.length(); i++) {
			String widgetID = widgets.getJSONObject(i).getJSONObject("configuration").getString("widgetID");
			if (!widgetID.isEmpty())
				actualWidgest.add(widgetID);
		}
		return actualWidgest;

	}

	public List<String> getWidgetOrder(org.json.JSONArray order) {
		List<String> actualWidgestOrder = new ArrayList<String>();
		for (int i = 0; i < order.length(); i++) {
			String widget = order.get(i).toString();
			if (!widget.isEmpty())
				actualWidgestOrder.add(widget);
		}
		return actualWidgestOrder;

	}

	public List<String> getTokenTickers(org.json.JSONArray token_list) {
		List<String> allTickers = new ArrayList<String>();
		org.json.JSONArray tickers = token_list.getJSONObject(0).getJSONArray("tickers");
		for (int i = 0; i < tickers.length(); i++) {
			String tickerName = tickers.getString(i).toLowerCase().trim();
			if (!tickerName.isEmpty())
				allTickers.add(tickerName);
		}
		return allTickers;
	}

	public ArrayList<String> convertJSONArray(org.json.JSONArray jArray) {
		ArrayList<String> listdata = new ArrayList<String>();
		if (jArray != null) {
			for (int i = 0; i < jArray.length(); i++) {
				listdata.add(jArray.getString(i));
			}
			return listdata;
		}
		return listdata;
	}

	public void getPlotterID() throws Exception {
		org.json.JSONArray loadData = null;
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("sort_flag", "recent");
		try {
			Random rand = new Random();
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			loadData = respJson.getJSONObject("result").getJSONArray("graphobj");
			int rand_int1 = rand.nextInt(loadData.length());
			plotter_id = loadData.getJSONObject(rand_int1).getString("plotter_id");
			plotter_name = loadData.getJSONObject(rand_int1).getString("name");

		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
		verify.verifyAll();
	}

	public String getRandomWatchlist() {
		String watchlistName = "";
		try {
			org.json.JSONArray getWatchlists = getUserWatchlists();
			Random rand = new Random();
			int rand_int1 = rand.nextInt(getWatchlists.length());
			org.json.JSONArray allWatchlist = getWatchlists.getJSONArray(rand_int1);
			watchlistName = allWatchlist.getString(0);
			if (watchlistName.contains("Followed Tickers") || watchlistName.contains("Recent Tickers")
					|| watchlistName.contains("Recent & Followed Tickers")) {
				do {
					rand_int1 = rand.nextInt(getWatchlists.length());
					allWatchlist = getWatchlists.getJSONArray(rand_int1);
					watchlistName = allWatchlist.getString(0);
					watchlistID = allWatchlist.getString(2);
				} while (!(watchlistName.contains("Followed Tickers")) && !(watchlistName.contains("Recent Tickers"))
						&& watchlistName.contains("Recent & Followed Tickers"));

			} else {
				watchlistID = allWatchlist.getString(2);
				watchlistTickers = allWatchlist.getJSONArray(3);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return watchlistName;
	}

	public boolean verifyWatchlist(String option) {
		String watchlistName = "";
		try {
			org.json.JSONArray getWatchlists = getUserWatchlists();
			for (int i = 0; i < getWatchlists.length(); i++) {
				org.json.JSONArray allWatchlist = getWatchlists.getJSONArray(i);
				watchlistName = allWatchlist.getString(0);
				if (option.equalsIgnoreCase(watchlistName))
					return true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}
}
