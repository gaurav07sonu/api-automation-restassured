package com.sentieo.dashboard;

import static com.sentieo.constants.Constants.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.*;
import com.sentieo.docsearch.DocSearchRestApi;
import com.sentieo.plotter.LoadGraph;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.user.AddWatchlist;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class CreateDashboard extends APIDriver {

	public static ArrayList<String> expectedWidgets = new ArrayList<String>(Arrays.asList("SavedSearchWidget_1",
			"PlotterWidget_3", "PriceMonitorWidget_2", "DocumentWidget_1", "RSSWidget_1"));
	static String cloneDashboardID = "";
	static List<Integer> rssfeeds = new ArrayList<>();
	static String selectedWatchlist = "";
	String viewName = "Automation-Sharing" + new Date().getTime();
	String widgetList = "{\"SavedSearchWidget_1\":{\"configuration\":{\"settings\":{\"size\":15,\"count\":0,\"start\":0,\"endOfResult\":false,\"name\":\"Saved Search\",\"filterObj\":{}},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"SavedSearchWidget_1\"}},\"PlotterWidget_3\":{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"name\":\"Plotter\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"widgetTitle\":\"Plotter\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"PlotterWidget_3\"}},\"PriceMonitorWidget_2\":{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"wrapPreference\":\"dont-wrap\",\"infiniteScroll\":true,\"infiniteScrollWaiting\":500,\"restrictOuterScroll\":true,\"disableLinking\":true,\"name\":\"Price Monitor\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":2,\"driveBy\":\"local\",\"wrapPreference\":\"dont-wrap\",\"columnOption1\":\"edt-icon\",\"columnOption2\":\"docsearch-icon\",\"updateUserData\":{\"wl_mapping\":{},\"wl_id_mapping\":{},\"selectedRow\":\"{\\\"lastSelectedGroupID\\\":\\\"\\\",\\\"selectedTicker\\\":\\\"\\\"}\",\"watchlistsState\":{},\"viewData\":{},\"marketMonitorLoaded\":false},\"displayDensity\":\"compact\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"PriceMonitorWidget_2\"}},\"DocumentWidget_1\":{\"configuration\":{\"settings\":{\"size\":20,\"count\":0,\"start\":0,\"endOfResult\":false,\"filterObj\":{},\"defaultFilterObj\":{\"bd\":[],\"gbf\":[],\"rr\":[],\"ni\":[],\"tt\":[],\"ef\":[],\"jr\":[],\"ppt\":[],\"nw\":[],\"reg\":[],\"sd\":[]},\"pticker_setting\":true,\"name\":\"All Documents\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"DocumentWidget_1\"}},\"RSSWidget_1\":{\"configuration\":{\"settings\":{\"size\":20,\"count\":0,\"start\":0,\"endOfResult\":false,\"filterObj\":{\"rss\":{\"\":{\"\":{\"feed_id_599\":{\"value\":[599]}}}}},\"defaultFilterObj\":{\"\":{}},\"pticker_setting\":true,\"name\":\"RSS Feeds\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\"},\"data\":{\"tickers\":[],\"watchlists\":\"\"},\"widgetID\":\"RSSWidget_1\"}}}";
	String widget_order = "[\"SavedSearchWidget_1\",\"PlotterWidget_3\",\"PriceMonitorWidget_2\",\"DocumentWidget_1\",\"RSSWidget_1\"]";
	static String plotter_id = "";
	// static String plotter_name = "";
	static String widget_ID = "";
	static String savedSearchwidgetID = "";
	static String db_id = "";
	static String option = "";
	static String watchID = "";

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	@AfterClass
	public void after_class() throws Exception {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			login();
			obj.deleteDashboard(db_id, viewName);
			AddWatchlist objw = new AddWatchlist();
			objw.deleteUserWatchlist(watchID);
		} catch (CoreCommonException e) {
			verify.assertTrue(false, "In after_class catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "fv", description = "create dashboard", priority = 0)
	public void createNewDashboard() throws Exception {
		String URI = USER_APP_URL + CREATE_DASHBOARD;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		dashboardData.put("widget_list", widgetList);
		dashboardData.put("widget_order", widget_order);
		dashboardData.put("dashboard_name", viewName);
		dashboardData.put("token_list", "");
		dashboardData.put("active_token_list", "");
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
					}
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "In createNewDashboard catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "qw223", description = "create dashboard", priority = 1)
	@SuppressWarnings("unchecked")
	public void updateWatchlist() throws CoreCommonException {
		String URI = USER_APP_URL + UPDATE_DB_TOKEN_LIST;
		AddWatchlist obj = new AddWatchlist();
		DashboardCommonUtils com = new DashboardCommonUtils();
		List<String> tickers = new ArrayList<String>(Arrays.asList("aapl", "amzn", "tsla", "a", "b", "c", "d"));
		try {
			obj.createWatchlist(tickers);
			selectedWatchlist = AddWatchlist.watchName;
			watchID = AddWatchlist.watchID;
			String msg = "Tokens, Active Tokens,  Updated successfully";
			String watchName = "Update : [<font color=\"red\">" + selectedWatchlist + " watchlist in dashboard : ";
			watchName = "<span style=\"font-weight: bold;\">" + watchName + ": </span>";
			ExtentTestManager.getTest().log(LogStatus.INFO, watchName);
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			JSONObject child = new JSONObject();
			child.put("name", "W#" + selectedWatchlist + "");
			child.put("id", watchID);
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
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("result").getString("msg").trim(), msg,
						"Verify the API Message");
				rssfeeds = com.fetch_search_filters();
				verify.assertTrue(rssfeeds.size() != 0, "Verify RSS feeds data : ");
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in updateWatchlist catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "load", description = "fetch_trading_ratios", priority = 2)
	public void updatePlotterDashboardWidget() throws Exception {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String plotter_id = obj.getPlotterID();

			String plotter = "<font color=\"red\">" + DashboardCommonUtils.plotter_name;
			plotter = "<span style=\"font-weight: bold;\">" + plotter + ": </span>";
			ExtentTestManager.getTest().log(LogStatus.INFO, " Update plotter in dashboard  : " + plotter);

			HashMap<String, String> tickerData = new HashMap<String, String>();
			String value = "{\"configuration\":{\"settings\":{\"minSize\":\"s\",\"name\":\"Plotter\"},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"widgetTitle\":\"Plotter\",\"linkMode\":\"single\",\"plotter_id\":\""
					+ plotter_id
					+ "\",\"sentieo_plot\":false,\"sentieo_template\":false},\"data\":{\"tickers\":[]},\"widgetID\":\""
					+ widget_ID + "\"}}";
			tickerData.put("widgetConfig", value);
			tickerData.put("type", "singleWidget");
			tickerData.put("wid", widget_ID);
			tickerData.put("dashboard_id", db_id);
			obj.updateWidget(tickerData);
		} catch (Exception e) {
			verify.assertTrue(false, "in updatePlotterDashboardWidget catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "qwer", description = "fetch_trading_ratios", priority = 3)
	public void updatesavedSearchDashboardWidget() throws Exception {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String savedSearchID = obj.getRandomSaveSearch();
			if (savedSearchID.isEmpty() || DashboardCommonUtils.saveSearchName.isEmpty())
				savedSearchID = obj.getRandomSaveSearch();

			String search = "<font color=\"red\">" + DashboardCommonUtils.saveSearchName;
			search = "<span style=\"font-weight: bold;\">" + search + ": </span>";
			ExtentTestManager.getTest().log(LogStatus.INFO, " Update save search in dashboard  : " + search);

			String searchName = "\"" + DashboardCommonUtils.saveSearchName + "\"";
			savedSearchID = "\"" + savedSearchID + "\"";
			HashMap<String, String> tickerData = new HashMap<String, String>();
			String config = "{\"configuration\":{\"settings\":{\"size\":15,\"count\":0,\"start\":0,\"endOfResult\":false,\"name\":"
					+ searchName + ",\"filterObj\":{\"user_saved_search\":[" + savedSearchID
					+ "],\"uss_data\":{\"sector\":\"\",\"exp_avg_result\":null,\"table_search\":false,\"email_alert\":false,\"period\":null,\"watchlist_tickers\":\"\",\"query\":\"sales\",\"id\":"
					+ savedSearchID
					+ ",\"filter_obj\":{\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"other\":{},\"date\":{},\"ticker\":{}},\"size\":\"\",\"sort_option\":\"filing_date:desc\",\"source\":\"\",\"multi_field_bit\":0,\"sensitivity_setting\":null,\"sqs\":{},\"watchlist_ids\":[],\"etype\":[],\"table_search_bit\":0,\"search_id\":null,\"sort\":\"filing_date:desc\",\"notification_alert\":false,\"entity_info\":{},\"pticker_setting\":false,\"geography\":{},\"subsector\":\"\",\"email_alert_type\":0,\"name\":"
					+ searchName
					+ ",\"tickers_filters\":\"\",\"nw_pticker\":false,\"fqs\":{},\"tickers\":\"\"}},\"infiniteScroll\":true,\"restrictOuterScroll\":true,\"tokenList\":[],\"noDataAvailable\":false},\"configurable\":{\"resizeEnable\":true,\"deleteEnable\":true,\"settingEnable\":true,\"viewPreference\":\"small\",\"verticalFactor\":1,\"driveBy\":\"local\",\"linkMode\":\"single\"},\"data\":{\"tickers\":[]},\"widgetID\":\"SavedSearchWidget_1\"}}";
			tickerData.put("widgetConfig", config);
			tickerData.put("type", "singleWidget");
			tickerData.put("wid", "SavedSearchWidget_1");
			tickerData.put("dashboard_id", db_id);
			obj.updateWidget(tickerData);
		} catch (Exception e) {
			verify.assertTrue(false, "in updatesavedSearchDashboardWidget catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 4)
	public void shareDashboard() throws CoreCommonException {
		try {
			String URI = USER_APP_URL + DASHBOARD_SHARE;
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("entity_id", db_id);
			dashboardData.put("entity_type", "SentieoDashboard");
			dashboardData.put("share_with_list",
					"[{\"shared_with_type\":\"user\",\"shared_with_name\":\"bhaskar\",\"shared_with_display_name\":\"Bhaskar Sentieo\",\"access_level\":\"view\",\"state\":\"active\",\"email\":\"1\"}]");
			dashboardData.put("email", "1");
			dashboardData.put("email_img", "data:image/png;base64,iVBOR");
			dashboardData.put("dashboard_name", viewName);
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.post(URI, null, spec, dashboardData);
			APIResponse apiResp = new APIResponse(resp);
			int statuscode = apiResp.getStatusCode();
			verify.verifyStatusCode(statuscode, 200);
			if (statuscode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
				JSONObject result = respJson.getJSONObject("result");
				String shared_users = result.getJSONArray("shared_users").getJSONObject(0)
						.getString("shared_with_display_name");
				verify.assertEqualsActualContainsExpected(shared_users, "Bhaskar Sentieo", "Verify shared user ");
			}
		} catch (Exception e) {
			verify.assertTrue(false, "in shareDashboard catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 5)
	public void sharedUserAccountLogin() throws CoreCommonException {
		try {
			RestAssured.baseURI = APP_URL;
			String URI = USER_APP_URL + LOGIN_URL;
			HashMap<String, String> loginData = new HashMap<String, String>();
			loginData.put("email", "bhaskar@sentieo.com");
			loginData.put("password", "Sentieo.789");

			RequestSpecification spec = loginSpec(loginData);
			Response resp = RestOperationUtils.login(URI, null, spec, loginData);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			username = respJson.getJSONObject("result").getString("username");
			apid = resp.getCookie("apid");
			usid = resp.getCookie("usid");
			if (apid.isEmpty() || usid.isEmpty()) {
				System.out.println("Login failed");
				System.exit(1);
			}
		} catch (Exception e) {
			verify.assertTrue(false, "In testlogin catch " + e.toString());
		}
	}

	@Test(groups = "sanity", description = "get dashboard items", priority = 6)
	public void verifySharedDashboard() throws Exception {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/dd/yy");
			LocalDateTime now = LocalDateTime.now();
			String currentDate = dtf.format(now);
			CommonUtil util = new CommonUtil();
			String date = "";
			org.json.JSONArray shared_dashboards = obj.dashboardlist("shared");
			for (int i = 0; i < shared_dashboards.length(); i++) {
				String dashboard_name = shared_dashboards.getJSONObject(i).getString("dashboard_name").toLowerCase();
				if (dashboard_name.equalsIgnoreCase(viewName.toLowerCase())) {
					double timeStamp = shared_dashboards.getJSONObject(i).getDouble("created_ts");
					int digit = (int) (timeStamp / 1000);
					date = util.convertTimestampIntoDate(digit);
					verify.compareDates(date, currentDate, "Verify the created date for dahsboard");
					verify.assertTrue(true, viewName + "Verify shared dashboard view ");
					break;
				} else {
					if (i == shared_dashboards.length())
						verify.assertTrue(false, viewName + "Not found ");
				}
			}

		} catch (Exception e) {
			verify.assertTrue(false, "In verifySharedDashboard catch " + e.toString());
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
				DashboardCommonUtils obj = new DashboardCommonUtils();
				List<String> actualWidgest = obj.getWidgetList(widgetList);
				verify.assertEquals(actualWidgest, expectedWidgets, "Verify shared widgest", true);

			}
		} catch (Exception e) {
			verify.assertTrue(false, "In getSharedDashboardData catch " + e.toString());

		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "clone", description = "get dashboard items", priority = 8)
	public void clonedashboard() throws CoreCommonException {
		try {
			String cloneDashboard = viewName + " " + "- Copy";
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String URI = USER_APP_URL + CLONE_DASHBOARD;
			HashMap<String, String> dashboardData = new HashMap<String, String>();
			dashboardData.put("dashboard_id", db_id);
			dashboardData.put("dashboard_name", cloneDashboard);
			JSONObject jj = new JSONObject();
			dashboardData.put("clone_details", jj.toString());

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

				List<String> actualWidgest = obj.getWidgetList(widgetList);
				verify.assertEquals(actualWidgest, expectedWidgets, "Verify shared widgest", true);

				String dashboardName = result.getString("dashboard_name");
				String dash = viewName + " " + "-" + " Copy";
				verify.assertEqualsActualContainsExpected(dashboardName, dash, "Verify dashboard name after cloning");

				org.json.JSONArray token_list = result.getJSONArray("token_list");
				String tokenType = token_list.getJSONObject(0).getString("token_type").toLowerCase().trim();
				if (tokenType.equalsIgnoreCase("watchlist")) {
					String watchName = token_list.getJSONObject(0).getString("name");
					String watchExpected = selectedWatchlist + " " + "@" + " " + viewName;
					verify.assertEqualsActualContainsExpected(watchName.toLowerCase().trim(),
							watchExpected.toLowerCase().trim(), "Verify watchlist name after cloning dashboard");
				}
				List<String> tickers = obj.getTokenTickers(token_list);
				verify.assertEquals(tickers, AddWatchlist.watchTickers, "Verify watchlist tickers", true);
				org.json.JSONArray my_dash = obj.dashboardlist("");
				for (int i = 0; i < my_dash.length(); i++) {
					String name = my_dash.getJSONObject(i).getString("dashboard_name");
					if (name.equalsIgnoreCase(cloneDashboard)) {
						cloneDashboardID = my_dash.getJSONObject(i).getString("db_id");
						break;
					}
				}
			}
		} catch (Exception e) {
			verify.assertTrue(false, "In clonedashboard catch " + e.toString());
		} finally {
			verify.verifyAll();
		}

	}

	@Test(groups = "clone", description = "get dashboard items", priority = 9)
	public void verifyCloneWatchlist() throws CoreCommonException {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String option = selectedWatchlist + " @" + " " + viewName;
			boolean result = obj.verifyWatchlist(option);
			verify.assertTrue(result, "Verify Clone Watchlist : ");
		} catch (Exception e) {
			verify.assertTrue(false, "In verifyCloneWatchlist catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "clone", description = "get dashboard items", priority = 10)
	public void verifyCloneSavedSearch() throws CoreCommonException {
		try {
			DocSearchRestApi obj = new DocSearchRestApi();
			option = DashboardCommonUtils.saveSearchName + " @" + " " + viewName;
			org.json.JSONArray data = obj.load_userSearchs();
			for (int i = 0; i < data.length(); i++) {
				String saveSearch = data.getJSONObject(i).getString("name");
				if (saveSearch.equalsIgnoreCase(option)) {
					verify.assertEqualsActualContainsExpected(saveSearch, option, "Verify search after cloning ");
					break;
				} else {
					if (i == data.length() - 1)
						verify.assertTrue(false, "Save search not found after cloning dashboard : ");
				}

			}
		} catch (Exception e) {
			verify.assertTrue(false, "In verifyCloneSavedSearch catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "clone", description = "get dashboard items", priority = 11)
	public void verifyClonePlotter() throws CoreCommonException {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String option = DashboardCommonUtils.plotter_name + " @" + " " + viewName;
			boolean result = obj.loadGraphNew(option);
			verify.assertTrue(result, "Verify clone plotter is available ?:");
		} catch (Exception e) {
			verify.assertTrue(false, "In verifyClonePlotter catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "clone", description = "get dashboard items", priority = 12)
	public void verifyCloneRSSFEEDS() throws CoreCommonException {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			List<Integer> getrssfeeds = obj.fetch_search_filters();
			verify.assertInteger(getrssfeeds, rssfeeds, "Verify FEEDS data : ");
		} catch (Exception e) {
			verify.assertTrue(false, "In verifyCloneRSSFEEDS catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "deletes user`s saved searches", priority = 13)
	public void delete_saved_search() throws CoreCommonException {
		try {
			DashboardCommonUtils obj = new DashboardCommonUtils();
			String uss_ids = obj.getDeleteSaveSearchID(option);
			if (!uss_ids.isEmpty()) {
				String URI = USER_APP_URL + DELETE_SAVED_SEARCH;
				HashMap<String, String> queryParams = new HashMap<String, String>();
				queryParams.put("id", uss_ids);
				RequestSpecification spec = formParamsSpec(queryParams);
				Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 10000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
			} else
				verify.assertTrue(false, "Search not found");
			uss_ids = obj.getDeleteSaveSearchID(option);
			if (!uss_ids.isEmpty())
				verify.assertTrue(false, "Search is not deleted  : ");

		} catch (Exception e) {
			verify.assertTrue(false, "In delete_saved_search catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = "sanity", description = "deletes user`s saved searches", priority = 14)
	public void deleteWatchlist() throws CoreCommonException {
		try {
			DashboardCommonUtils dash = new DashboardCommonUtils();
			String watchExpected = selectedWatchlist + " " + "@" + " " + viewName;
			AddWatchlist obj = new AddWatchlist();
			String watchID = dash.getWatchlistID(watchExpected);
			if (!watchID.isEmpty()) {
				obj.deleteUserWatchlist(watchID);
				boolean result = dash.verifyWatchlist(watchExpected);
				verify.assertFalse(result, "Verify Deleted Watchlist : ");

			} else
				verify.assertTrue(false, "Watchlist not found : ");
			watchID = dash.getWatchlistID(watchExpected);
			if (!watchID.isEmpty())
				verify.assertTrue(false, "Watchlist is not deleted : ");
		} catch (Exception e) {
			verify.assertTrue(false, "In deleteWatchlist catch " + e.toString());
		} finally {
			verify.verifyAll();

		}
	}

	@Test(groups = "sanity", description = "Delete Plotter ", priority = 15)
	public void deletePlotter() {
		try {
			DashboardCommonUtils dash = new DashboardCommonUtils();
			String plotter_id = dash.getPlotterID();
			LoadGraph obj = new LoadGraph();
			obj.deleteGraph(plotter_id);
			boolean result = dash.loadGraphNew(option);
			verify.assertFalse(result, "Verify delete plotter :");
			verify.verifyAll();
		} catch (Exception e) {
			verify.assertTrue(false, "In deletePlotter catch " + e.toString());
		}
	}

	@Test(groups = "sanity", description = "Delete Plotter ", priority = 16)
	public void dashboardDelete() throws CoreCommonException {
		try {
			String cloneDashboard = viewName + " " + "- Copy";
			DashboardCommonUtils obj = new DashboardCommonUtils();
			obj.deleteDashboard(cloneDashboardID, cloneDashboard);
		} catch (Exception e) {
			verify.assertTrue(false, "In dashboardDelete catch " + e.toString());
		} finally {
			verify.verifyAll();
		}
	}
}
