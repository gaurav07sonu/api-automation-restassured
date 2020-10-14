package com.sentieo.dashboard;

import static com.sentieo.constants.Constants.APP_URL;
import static com.sentieo.constants.Constants.DELETE_DASHBOARD;
import static com.sentieo.constants.Constants.FETCH_SEARCH;
import static com.sentieo.constants.Constants.GET_DASHBOARD_LIST;
import static com.sentieo.constants.Constants.LOADGRAPH_NEW;
import static com.sentieo.constants.Constants.UPDATE_DASHBOARD_WIDGET;
import static com.sentieo.constants.Constants.USER_APP_URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.testng.annotations.BeforeMethod;

import com.google.common.collect.Lists;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.docsearch.DocSearchRestApi;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.user.AddWatchlist;
import com.sentieo.user.TestUserWatchlistData;
import com.sentieo.utils.CoreCommonException;

public class DashboardCommonUtils extends APIDriver {
	static int feed_id = 0;
	static int feedCount = 0;
	static String watchlistID = "";
	static String saveSearchName = "";
	static String plotter_name = "";

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
				saveSearchName = data.getJSONObject(rand_int1).getString("name");
				saveSearchID = data.getJSONObject(rand_int1).getString("id");
				return saveSearchID;
			} else
				obj.save_user_search();
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

	public String getPlotterID() throws Exception {
		String plotterid = "";
		org.json.JSONArray loadData = null;
		String URI = USER_APP_URL + LOADGRAPH_NEW;
		HashMap<String, String> tickerData = new HashMap<String, String>();
		tickerData.put("sort_flag", "recent");
		try {
			Random rand = new Random();
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
				int rand_int1 = rand.nextInt(loadData.length());
				String plotter_id = loadData.getJSONObject(rand_int1).getString("plotter_id");
				plotter_name = loadData.getJSONObject(rand_int1).getString("name");
				String plotter = "<font color=\"red\">" + plotter_name;
				plotter = "<span style=\"font-weight: bold;\">" + plotter + ": </span>";
				ExtentTestManager.getTest().log(LogStatus.INFO, " Update plotter in dashboard  : " + plotter);

				plotterid = plotter_id;
				return plotterid;
			}

		} catch (Exception e) {
			verify.verificationFailures.add(e);
			ExtentTestManager.getTest().log(LogStatus.FAIL, e.getMessage());
		}
		verify.verifyAll();
		return plotterid;
	}

	public List<Integer> fetch_search_filters() throws CoreCommonException {
		List<Integer> rssID = new ArrayList<>();
		String tickers = "";
		try {
			if (AddWatchlist.watchTickers.size() > 20) {
				List<List<String>> lists = Lists.partition(AddWatchlist.watchTickers, 20);
				tickers = lists.get(1).toString();
			} else
				tickers = AddWatchlist.watchTickers.toString();
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
							feed_id = rss.getJSONObject(i).getInt("feed_id");
							rssID.add(feed_id);
						}
						return rssID;
					}
				}
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		} finally {
			verify.verifyAll();
		}
		return rssID;
	}

	@SuppressWarnings("unused")
	public org.json.JSONArray dashboardlist(String option) {
		org.json.JSONArray shared_dashboards = null;
		org.json.JSONArray my_dashboards = null;
		String URI = USER_APP_URL + GET_DASHBOARD_LIST;
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

	public void updateWidget(HashMap<String, String> tickerData) throws CoreCommonException {
		String URI = USER_APP_URL + UPDATE_DASHBOARD_WIDGET;
		try {
			RequestSpecification spec = formParamsSpec(tickerData);
			Response resp = RestOperationUtils.post(URI, null, spec, tickerData);
			APIResponse apiResp = new APIResponse(resp);
			verify.verifyStatusCode(apiResp.getStatusCode(), 200);
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

}