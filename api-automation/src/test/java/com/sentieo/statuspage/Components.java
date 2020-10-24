package com.sentieo.statuspage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import org.json.simple.JSONArray;

public class Components extends APIDriver {

	public void createComponent(String pageID, String description, String status, String componentName)
			throws CoreCommonException {
		String URI = "https://api.statuspage.io/v1/pages/" + pageID + "/components/";
		JSONObject child = new JSONObject();
		child.put("description", description);
		child.put("status", status);
		child.put("name", componentName);
		JSONObject parent = new JSONObject();
		parent.put("component", child);
		RequestSpecification spec = RequestSpecificationformParamsSpecStatusPage(parent.toString());
		Response resp = RestOperationUtils.post(URI, null, spec, null);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		System.out.println(respJson);

	}

	public void updateComponent(String pageID, String componentId, String description, String componentUpdatedStatus,
			String componentName) throws Exception {
		String URI = "https://api.statuspage.io/v1/pages/" + pageID + "/components/" + componentId;
		JSONObject child = new JSONObject();
		child.put("description", description);
		child.put("status", componentUpdatedStatus);
		child.put("name", componentName);
		child.put("only_show_if_degraded", true);
		child.put("showcase", true);
		JSONObject parent = new JSONObject();
		parent.put("component", child);
		try {
			RequestSpecification spec = RequestSpecificationformParamsSpecStatusPage(parent.toString());
			Response resp = RestOperationUtils.put(URI, null, spec, null);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
	}

	public void getListOFIncidents() {

	}

	@SuppressWarnings("unchecked")
	public String createIncident(String pageID, String componentId, String incidentName, String description)
			throws Exception {
		String incidentID = "";
		HashMap<String, String> queryParams = new HashMap<String, String>();
		String URI = "https://api.statuspage.io/v1/pages/" + pageID + "/incidents";
		JSONObject child = new JSONObject();
		JSONObject child2 = new JSONObject();
		JSONArray array = new JSONArray();
		child.put("name", incidentName);
		child.put("status", "investigating");
//		child.put("impact_override", "minor");
		child.put("deliver_notifications", true);
		child.put("body", description);
		child2.put("component_id", "operational");
		child.put("components", child2);
		child.put("component_id", "operational");
		array.add(componentId);
		child.put("component_ids", array);
		JSONObject parent = new JSONObject();
		parent.put("incident", child);
		try {
			RequestSpecification spec = RequestSpecificationformParamsSpecStatusPage(parent.toString());
			Response resp = RestOperationUtils.post(URI, null, spec, queryParams);
			APIResponse apiResp = new APIResponse(resp);
			JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
			System.out.println(respJson.toString());
			incidentID = respJson.getString("id");
			return incidentID;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return incidentID;
	}

	@SuppressWarnings("unchecked")
	public void updateIncident(String incident_id, String status, String componentId, String pageID, String body)
			throws CoreCommonException {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		String URI = "https://api.statuspage.io/v1/pages/" + pageID + "/incidents/" + incident_id;
		JSONObject child = new JSONObject();
		JSONObject child2 = new JSONObject();
		JSONArray array = new JSONArray();
		child.put("status", status);
		// child.put("impact_override", "minor");
		child.put("deliver_notifications", true);
		child.put("body", body);
		// child.put("incidentId", incident_id);
		child2.put("component_id", "operational");
		child.put("components", child2);
		array.add(componentId);
		child.put("component_ids", array);
		JSONObject parent = new JSONObject();
		parent.put("incident", child);
		RequestSpecification spec = RequestSpecificationformParamsSpecStatusPage(parent.toString());
		Response resp = RestOperationUtils.put(URI, null, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		System.out.println(respJson);

	}

	public String getIncidents(String page_id, String componentName) throws CoreCommonException {
		String incidentID = "";
		try {
			System.out.println();
			String URI = "https://api.statuspage.io/v1/pages/" + page_id + "/incidents/unresolved/";
			org.json.JSONArray arraydat = RequestSpecificationformParamsSpecStatusPage2(URI);
			for (int i = 0; i < arraydat.length(); i++) {
				org.json.JSONArray componentsList = arraydat.getJSONObject(i).getJSONArray("components");
				String component = componentsList.getJSONObject(0).getString("name");
				if (component.toLowerCase().equalsIgnoreCase(componentName.toLowerCase())) {
					incidentID = arraydat.getJSONObject(i).getString("id");
					System.out.println(incidentID);
					return incidentID;
				}

			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return incidentID;

	}

	public void getJobDetails(String pageID, String componentId, String componentName) throws Exception {
		List<String> offlineSlaves = new ArrayList<String>();
		String incidentID = "";
		String URI = "https://deploy-test.sentieo.com/computer/api/json";
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("pretty", "true");
		RequestSpecification spec = RequestSpecificationformParamsSpecJenkins(queryParams);
		Response resp = RestOperationUtils.get(URI, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		org.json.JSONArray arr = respJson.getJSONArray("computer");
		incidentID = getIncidents(pageID, componentName);
		for (int i = 0; i < arr.length(); i++) {
			String displayName = arr.getJSONObject(i).getString("displayName");
			boolean status = arr.getJSONObject(i).getBoolean("offline");
			if (status)
				offlineSlaves.add(displayName);
			if (offlineSlaves.size() != 0) {
				if (i == arr.length() - 1) {
					if (incidentID.isEmpty()) {
						incidentID = createIncident(pageID, componentId, offlineSlaves.toString() + " is offline ",
								"Description");
						updateComponent(pageID, componentId, offlineSlaves.toString() + " is offline ",
								"partial_outage", componentName);
					} else
						updateIncident(incidentID, "identified", componentId, pageID,
								"We are investigating reports of degraded performance.");
				}
			}
		}
		if (offlineSlaves.size() != 0) {
			incidentID = getIncidents(pageID, componentName);
			if (!incidentID.isEmpty()) {
				updateIncident(incidentID, "resolved", componentId, pageID, "This incident has been resolved.");
				updateComponent(pageID, componentId, offlineSlaves.toString(), "operational", componentName);
			}
		}

	}

	@Test(groups = "savedseries", description = "fetch_saved_series", priority = 1)
	public void jenkinsJobDetails() throws Exception {
		String pageID = "cq92sczrvxp0";
		String componentId = "tm3x5znyzd0k";
		String componentName = "Automation-Jenkins-Slaves";
		getJobDetails(pageID, componentId, componentName);
	}

	@Test(groups = "savedseries", description = "getRedisServiceStatus", priority = 2)
	public void getRedisServiceStatus() throws Exception {
		JedisPool pool = new JedisPool();
		try {
			String incidentID = "";
			System.out.println();
			pool.getResource();
			System.out.println("Is connected");
			pool.close();
			incidentID = getIncidents("cq92sczrvxp0", "Redis-Service-Status");
			if (!incidentID.isEmpty()) {
				updateIncident(incidentID, "resolved", "bm9g7lr0942d", "cq92sczrvxp0",
						"This incident has been resolved.");
				updateComponent("cq92sczrvxp0", "bm9g7lr0942d", " All services Operational", "operational",
						"Redis-Service-Status");
			}

		} catch (JedisConnectionException e) {
			System.out.println(" Not connected");
			System.out.println(e.toString());
			writeRedisstatus("cq92sczrvxp0", "bm9g7lr0942d", "Redis-Service-Status");
		}
	}

	public void writeRedisstatus(String pageID, String componentId, String componentName) throws Exception {
		String incidentID = "";
		incidentID = getIncidents(pageID, componentName);
		if (incidentID.isEmpty()) {
			incidentID = createIncident(pageID, componentId, "Redis-Servcie-Down", "Redis - Component is down : ");
			updateComponent(pageID, componentId, " is offline ", "partial_outage", componentName);
		} else
			updateIncident(incidentID, "identified", componentId, pageID,
					"We are investigating reports of degraded performance.");
	}
}
