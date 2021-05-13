package com.sentieo.dashboard;

import static com.sentieo.constants.Constants.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.BeforeMethod;

import com.google.common.collect.Lists;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.mongodb.util.JSON;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.docsearch.DocSearchRestApi;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.user.TestUserWatchlistData;
import com.sentieo.utils.CoreCommonException;
import com.sentieo.watchlistsharing.ShareWatchlistWithEditPermission;

public class DashboardCommonUtils extends APIDriver {
	static String docID = "";
	static int feedCount = 0;
	static String watchlistID = "";
	static String saveSearchName = "";
	org.json.JSONArray shared_dashboards = null;

	public DashboardCommonUtils() {
		setUp();
	}

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		verify = new APIAssertions();
	}

	public boolean verifyWatchlist(String option) {
		String watchlistName = "";
		try {
			TestUserWatchlistData obj = new TestUserWatchlistData();
			org.json.JSONArray getWatchlists = obj.getUserWatchlists();
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

	public String getDeleteSaveSearchID(String option) {
		String saveSearchID = "";
		try {
			DocSearchRestApi obj = new DocSearchRestApi();
			org.json.JSONArray data = obj.load_userSearchs();
			for (int i = 0; i < data.length(); i++) {
				String saveSearchName = data.getJSONObject(i).getString("name");
				if (saveSearchName.toLowerCase().trim().equalsIgnoreCase(option.toLowerCase().trim())) {
					saveSearchID = data.getJSONObject(i).getString("id");
					return saveSearchID;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return saveSearchID;
	}

	public String getRandomSaveSearch() throws CoreCommonException {
		String saveSearchID = "";
		DocSearchRestApi obj = new DocSearchRestApi();
		try {
			org.json.JSONArray data = obj.load_userSearchs();
			Random rand = new Random();
			if (data.length() > 0) {
				int rand_int1 = rand.nextInt(data.length());
				if (data.getJSONObject(rand_int1).has("shared_by")) {
					do {
						rand_int1 = rand.nextInt(data.length());
						saveSearchName = data.getJSONObject(rand_int1).getString("name");
						saveSearchID = data.getJSONObject(rand_int1).getString("id");
					} while (data.getJSONObject(rand_int1).has("shared_by"));
				} else {
					saveSearchName = data.getJSONObject(rand_int1).getString("name");
					saveSearchID = data.getJSONObject(rand_int1).getString("id");
				}
				return saveSearchID;
			} else
				obj.perform_user_save_search();
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return saveSearchID;
	}

	public String getRandomShareSearch() throws CoreCommonException {
		String saveSearchID = "";
		int counter = 0;
		DocSearchRestApi obj = new DocSearchRestApi();
		try {
			org.json.JSONArray data = obj.load_userSearchs();
			Random rand = new Random();
			if (data.length() > 0) {
				int rand_int1 = rand.nextInt(data.length());
				if (!data.getJSONObject(rand_int1).has("shared_by")) {
					do {
						rand_int1 = rand.nextInt(data.length());
						if (data.getJSONObject(rand_int1).has("shared_by")) {
							saveSearchName = data.getJSONObject(rand_int1).getString("name");
							saveSearchID = data.getJSONObject(rand_int1).getString("id");
						}
						counter++;
					} while (!data.getJSONObject(rand_int1).has("shared_by") && counter != 10);

				} else {
					saveSearchName = data.getJSONObject(rand_int1).getString("name");
					saveSearchID = data.getJSONObject(rand_int1).getString("id");

				}
				return saveSearchID;
			} else
				obj.perform_user_save_search();
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return saveSearchID;

	}

	public String getWatchlistID(String option) throws CoreCommonException {
		String watchlistID = "";
		try {
			TestUserWatchlistData obj = new TestUserWatchlistData();
			org.json.JSONArray getWatchlists = obj.getUserWatchlists();
			for (int i = 0; i < getWatchlists.length(); i++) {
				org.json.JSONArray allWatchlist = getWatchlists.getJSONArray(i);
				String watchlistName = allWatchlist.getString(0).trim().toLowerCase();
				if (watchlistName.equalsIgnoreCase(option.toLowerCase().trim())) {
					watchlistID = allWatchlist.getString(2);
					return watchlistID;
				}
			}

		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return watchlistID;

	}

	public List<String> getTokenTickers(org.json.JSONArray token_list) throws CoreCommonException {
		List<String> allTickers = new ArrayList<String>();
		try {
			org.json.JSONArray tickers = token_list.getJSONObject(0).getJSONArray("tickers");
			for (int i = 0; i < tickers.length(); i++) {
				String tickerName = tickers.getString(i).toLowerCase().trim();
				if (!tickerName.isEmpty())
					allTickers.add(tickerName);
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
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

	public List<String> getWidgetOrder(org.json.JSONArray order) {
		List<String> actualWidgestOrder = new ArrayList<String>();
		for (int i = 0; i < order.length(); i++) {
			String widget = order.get(i).toString();
			if (!widget.isEmpty())
				actualWidgestOrder.add(widget);
		}
		return actualWidgestOrder;

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

	public boolean loadGraphNew(String option) throws Exception {
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		org.json.JSONArray graphobj = null;
		try {
			HashMap<String, String> tickerData = new HashMap<String, String>();
			tickerData.put("sort_flag", "recent");
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			System.out.println(apiResp.getStatusCode());
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			graphobj = respJson.getJSONObject("result").getJSONArray("graphobj");
			try {
				for (int i = 0; i < graphobj.length(); i++) {
					String name = graphobj.getJSONObject(i).getString("name");
					if (name.equalsIgnoreCase(option))
						return true;
				}

				return false;

			} catch (Exception e) {
				verify.verificationFailures.add(e);
				ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
			}
			return false;
		} finally {
			verify.verifyAll();

		}

	}

	public JSONArray getPlotteData() throws Exception {
		org.json.JSONArray loadData = null;
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("sort_flag", "recent");
		try {
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			int statuscode = apiResp.getStatusCode();
			verify.verifyStatusCode(statuscode, 200);
			if (statuscode == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				loadData = respJson.getJSONObject("result").getJSONArray("graphobj");
				return loadData;
			}
		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		} finally {
			verify.verifyAll();
		}
		return loadData;
	}

	public String getPlotterID(String option) throws Exception {
		String plotterid = "";
		org.json.JSONArray loadData = getPlotteData();
		for (int i = 0; i < loadData.length(); i++) {
			String plotter_name = loadData.getJSONObject(i).getString("name");
			if (plotter_name.toLowerCase().trim().equalsIgnoreCase(option.toLowerCase().trim())) {
				plotterid = loadData.getJSONObject(i).getString("plotter_id");
				return plotterid;
			}
		}
		return plotterid;

	}

	public String getRandomPlotter() throws Exception {
		org.json.JSONArray loadData = getPlotteData();
		Random rand = new Random();
		int rand_int1 = rand.nextInt(loadData.length());
		String plotter_name = loadData.getJSONObject(rand_int1).getString("name");
		return plotter_name;
	}

	public List<String> fetch_search_filters() throws CoreCommonException {
		List<String> doc_ID = new ArrayList<String>();
		// List<Integer> rssID = new ArrayList<>();
		String tickers = "";
		try {
			if (ShareWatchlistWithEditPermission.watchTickers.size() > 20) {
				List<List<String>> lists = Lists.partition(ShareWatchlistWithEditPermission.watchTickers, 20);
				tickers = lists.get(1).toString();
			} else
				tickers = ShareWatchlistWithEditPermission.watchTickers.toString();
			tickers = tickers.replaceAll("\\[", "").replaceAll("\\]", "").trim();
			tickers = tickers.replaceAll("( )+", " ");
			String URI = APP_URL + FETCH_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			queryParams.put("allow_entity", "true");
			queryParams.put("call_from", "dashboard");
			queryParams.put("start", "0");
			queryParams.put("size", "20");
			queryParams.put("tickers", tickers);
			queryParams.put("filters",
					"{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"regions\":{},\"source\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"all\"]}}},\"other\":{},\"rss\":{\"\":{\"\":{\"param\":\"feed_id\",\"values\":[599]}}}}");
			queryParams.put("applied_filters", "[\"rss\"]");
			queryParams.put("pticker_setting", "true");

			RequestSpecification spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			verify.verifyResponseTime(resp, 10000);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				if (respJson.getJSONObject("result") != null) {
					org.json.JSONArray rss = respJson.getJSONObject("result").getJSONArray("docs");
					if (rss != null) {
						feedCount = rss.length();
						for (int i = 0; i < rss.length(); i++) {
							docID = rss.getJSONObject(i).getString("id");
							doc_ID.add(docID);
						}
						return doc_ID;
					}
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
		return doc_ID;
	}

	public org.json.JSONArray dashboardlist(String option) {
		org.json.JSONArray my_dashboards = null;
		RequestSpecification spec;
		String URI = USER_APP_URL + GET_DASHBOARD_LIST;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		try {
			spec = formParamsSpec(dashboardData);
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
					if (option.equalsIgnoreCase("shared"))
						return shared_dashboards = result.getJSONArray("shared_dashboards");
					else
						return my_dashboards = result.getJSONArray("my_dashboards");
				}
			}
		} catch (Exception e) {
			System.out.println();
		}
		return my_dashboards;
	}

	public String getWidgetName(String option, String dashID) {
		String widgetName = "";
		String URI = USER_APP_URL + GET_DASHBOARD_LIST;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		try {
			dashboardData.put("dashboard_id", dashID);
			dashboardData.put("initial", "1");
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
				JSONObject result = respJson.getJSONObject("result").getJSONObject("first_object");
				if (result.length() == 0 || result == null)
					verify.assertTrue(false, "API shows blank data");
				else {
					JSONObject list = result.getJSONObject("widgetlist");
					for (int i = 0; i < list.names().length(); i++) {
						String widget = list.names().get(i).toString();
						if (widget.contains(option)) {
							widgetName = list.getJSONObject(widget).getJSONObject("configuration")
									.getJSONObject("settings").getString("name");
							return widgetName;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return widgetName;
	}

	public void updateWidget(HashMap<String, String> tickerData) throws CoreCommonException {
		String URI = USER_APP_URL + UPDATE_DASHBOARD_WIDGET;
		try {
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			int status = apiResp.getStatusCode();
			verify.verifyStatusCode(status, 200);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			verify.verifyResponseTime(resp, 5000);
			verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
					"Verify the API Response Status");
			verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
					"Verify the API Message");
		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		} finally {
			verify.verifyAll();

		}
	}

	public void deleteDashboard(String dashboardID, String viewName) throws Exception {
		boolean deleteView = true;
		String URI = USER_APP_URL + DELETE_DASHBOARD;
		HashMap<String, String> dashboardData = new HashMap<String, String>();
		dashboardData.put("dashboard_id", dashboardID);
		try {
			RequestSpecification spec = formParamsSpec(dashboardData);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
			if (apiResp.getStatusCode() == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getJSONArray("msg").get(0), "success",
						"Verify the API Message");
				JSONObject result = respJson.getJSONObject("result");
				if (result.length() == 0 || result == null)
					verify.assertTrue(false, "Delete dashboard API shows blank data");
				else {
					org.json.JSONArray db_list = result.getJSONArray("my_dashboards");
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

	public org.json.JSONArray load_userSearchs(String ap_id, String us_id) {
		org.json.JSONArray data = null;
		try {
			RequestSpecification spec;
			String URI = USER_APP_URL + LOAD_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			if (!ap_id.isEmpty() && !us_id.isEmpty())
				spec = formParamsSpec(queryParams, ap_id, us_id);
			else
				spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.get(URI, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			int status = apiResp.getStatusCode();
			verify.verifyStatusCode(status, 200);
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				data = respJson.getJSONObject("result").getJSONArray("userss");
				return data;
			} else
				verify.assertTrue(false, "Status code is " + status);
		} catch (Exception e) {

		}
		return data;
	}

	public String getLoadSearchID(String searchName, String ap_id, String us_id) {
		JSONArray data = load_userSearchs(ap_id, us_id);
		for (int i = 0; i < data.length(); i++) {
			String name = data.getJSONObject(i).getString("name");
			if (name.toLowerCase().trim().equalsIgnoreCase(searchName.toLowerCase().trim())) {
				String search_ids = data.getJSONObject(i).getString("id");
				return search_ids;
			}
		}
		return "";
	}

	public void save_user_search(String query, String tickers, String searchName, String filters,
			boolean watchlistSearch, String watchID, String saveSearchID, String apid, String usid) {
		try {
			RequestSpecification spec;
			String URI = USER_APP_URL + SAVE_USER_SEARCH;
			HashMap<String, String> queryParams = new HashMap<String, String>();
			tickers = tickers.toString().replace("[", "").replace("]", "").trim();
			tickers = tickers.replace("\"", "");
			if (!tickers.isEmpty() && !watchlistSearch)
				queryParams.put("tickers", tickers);

			if (!saveSearchID.isEmpty()) {
				queryParams.put("id", saveSearchID);
				queryParams.put("overwrite", "2");
			}

			if (watchlistSearch) {
				queryParams.put("watchlist_ids", watchID);
				queryParams.put("watchlist_tickers", tickers);
			}

			queryParams.put("query", query);
			queryParams.put("filters", filters);
			queryParams.put("force_save", "true");
			queryParams.put("pticker_setting", "true");
			queryParams.put("name", searchName);
			queryParams.put("synonym_setting", "true");

			if (!apid.isEmpty() && !usid.isEmpty())
				spec = formParamsSpec(queryParams, apid, usid);

			else
				spec = formParamsSpec(queryParams);
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			int status = apiResp.getStatusCode();
			verify.verifyStatusCode(status, 200);
			if (status == 200) {
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				System.out.println(respJson.toString());
			} else
				verify.assertTrue(false, "Status code is " + status);
		} catch (Exception e) {
			System.out.println(e.toString());

		}
	}

	public void sharedSearch(String id, String us_id, String ap_id) throws CoreCommonException {
		String URI = USER_APP_URL + SHARED_USER_SEARCH;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("uss_id", id);
		queryParams.put("share_with_list",
				"[{\"shared_with_type\":\"user\",\"shared_with_name\":\"bhaskar\",\"shared_with_display_name\":\"Bhaskar Sentieo\",\"access_level\":\"clone\",\"state\":\"active\"");
		RequestSpecification spec = formParamsSpec(queryParams, us_id, ap_id);
		Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		int status = apiResp.getStatusCode();
		verify.verifyStatusCode(status, 200);
		if (status == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
		} else
			verify.assertTrue(false, "Status code is " + status);

	}

	public List<String> getloadsearchdata(String search_id, String search, String displayName)
			throws CoreCommonException {
		List<String> titles = new ArrayList<String>();
		// String id = ("\"" + search_id + "\"");
		String search_name = ("\"" + search + "\"");
		String name = ("\"" + displayName + "\"");
		String searh_idw = ("\"" + search_id + "\"");

		String config = "{\"sector\":\"\",\"exp_avg_result\":null,\"table_search\":false,\"email_alert\":false,\"period\":null,\"watchlist_tickers\":\"\",\"query\":\"change in tax\",\"search_count\":{},\"id\":"
				+ search_id
				+ ",\"filter_obj\":{\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"other\":{},\"date\":{},\"ticker\":{}},\"size\":\"\",\"sort_option\":\"filing_date:desc\",\"synonym_setting\":true,\"source\":\"\",\"multi_field_bit\":0,\"sensitivity_setting\":null,\"watchlist_ids\":[],\"etype\":[],\"table_search_bit\":0,\"search_id\":null,\"sort\":\"filing_date:desc\",\"sqs\":{},\"entity_info\":{\"fb\":\"FB\",\"amzn\":\"AMZN\",\"tsla\":\"TSLA\",\"aapl\":\"AAPL\"},\"pticker_setting\":true,\"updated_at\":\"06-Apr-2021\",\"geography\":{},\"subsector\":\"\",\"email_alert_type\":0,\"notification_alert\":false,\"name\":"
				+ search_name
				+ ",\"tickers_filters\":\"\",\"nw_pticker\":false,\"fqs\":{},\"tickers\":\"aapl,amzn,tsla,fb\",\"shared_by\":{\"access_level\":\"clone\",\"shared_by_display_name\":"
				+ name + "}}";

		String config2 = " {\"sector\":\"\",\"exp_avg_result\":null,\"table_search\":false,\"email_alert\":false,\"period\":null,\"watchlist_tickers\":\"\",\"query\":\"change in tax\",\"search_count\":{},\"id\":\"606b23b6f8fe3674ef1e405b\",\"filter_obj\":{\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{},\"regions\":{},\"source\":{},\"other\":{},\"date\":{},\"ticker\":{}},\"size\":\"\",\"sort_option\":\"filing_date:desc\",\"synonym_setting\":true,\"source\":\"\",\"multi_field_bit\":0,\"sensitivity_setting\":null,\"watchlist_ids\":[],\"etype\":[],\"table_search_bit\":0,\"search_id\":null,\"sort\":\"filing_date:desc\",\"sqs\":{},\"entity_info\":{\"fb\":\"FB\",\"amzn\":\"AMZN\",\"tsla\":\"TSLA\",\"aapl\":\"AAPL\"},\"pticker_setting\":true,\"updated_at\":\"06-Apr-2021\",\"geography\":{},\"subsector\":\"\",\"email_alert_type\":0,\"notification_alert\":false,\"name\":\"Query share & search with ticker@user.test9\",\"tickers_filters\":\"\",\"nw_pticker\":false,\"fqs\":{},\"tickers\":\"aapl,amzn,tsla,fb\",\"shared_by\":{\"access_level\":\"clone\",\"shared_by_display_name\":\"User Test\"}}";

		String URI = APP_URL + LOAD_SAVED_SEARCH_DATA;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("start", "0");
		queryParams.put("size", "15");
		queryParams.put("uss_ids", "[" + search_id + "]");
		queryParams.put("uss_data", config);
		queryParams.put("all_tickers", "[]");
		queryParams.put("tickers", "[]");
		queryParams.put("watchlistIds", "[]");
		queryParams.put("watchlist_tickers", "[]");

		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		int status = apiResp.getStatusCode();
		if (status == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			try {
				JSONArray docs = respJson.getJSONObject("result").getJSONArray("uss_data");
				for (int i = 0; i < docs.length(); i++) {
					String title = docs.getJSONObject(i).getString("title").trim().toLowerCase();
					titles.add(title);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
			return titles;

		}
		return titles;
	}

	public List<String> fetchSearch(String query, String tickers, String filters, String searchTitle,
			boolean watchSearch, String watchlist_ids) throws Exception {
		tickers = tickers.toString().replace("[", "").replace("]", "").trim();
		tickers = tickers.replace("\"", "");
		List<String> titles = new ArrayList<String>();
		String URI = APP_URL + FETCH_SEARCH;
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("title", searchTitle);
		queryParams.put("id", "4");
		queryParams.put("size", "30");
		queryParams.put("query", query);
		if (watchSearch) {
			queryParams.put("watchlist_tickers", tickers);
			queryParams.put("tickers", "");
			queryParams.put("watchlist_ids", watchlist_ids);
		} else
			queryParams.put("tickers", tickers);
		queryParams.put("original_query", query);
		queryParams.put("default_sort", "date");
		queryParams.put("sort", "filing_date:desc");
		queryParams.put("snippet_fragment_size", "400");
		queryParams.put("original_query", query);
		queryParams.put("facets_flag", "false");
		queryParams.put("allow_entity", "true");
		queryParams.put("synonym_setting", "true");
		queryParams.put("call_from", "docsearch");
		queryParams.put("filters", filters);

		RequestSpecification spec = formParamsSpec(queryParams);
		Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		int status = apiResp.getStatusCode();
		if (status == 200) {
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			JSONArray docs = respJson.getJSONObject("result").getJSONArray("docs");
			for (int i = 0; i < docs.length(); i++) {
				String title = docs.getJSONObject(i).getString("title").trim().toLowerCase();
				titles.add(title);
				if (titles.size() == 15)
					return titles;
			}
		}
		return titles;

	}

	public String getSaveSearchSharedUser(String env) {
		env = env.replaceAll("https://", "");
		String extensionRemoved = env.split("\\.")[0];
		String uname = "";
		switch (extensionRemoved) {
		case "app":
			uname = "user.test9";
			break;
		case "balyasny":
			// code block
			break;
		}
		return uname;
	}
}