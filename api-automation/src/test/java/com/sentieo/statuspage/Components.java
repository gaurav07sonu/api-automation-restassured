package com.sentieo.statuspage;

import java.util.HashMap;

import org.json.JSONObject;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

import org.json.simple.JSONArray;

public class Components extends APIDriver {
	String incidentID = "";

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
		// String pageID = "ymxz3l6qgd7y";
		// String componentId = "trp1ljjq75tp";
		// String componentUpdatedStatus="partial_outage";
		// String componentName="Sentieo login api";
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
		// String pageID = "ymxz3l6qgd7y";
//		String componentId = "trp1ljjq75tp";
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
			incidentID = respJson.getString("id");
			return incidentID;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.toString());
		}
		return incidentID;
	}

	@SuppressWarnings("unchecked")
	public void updateIncident(String name, String incident_id, String status) throws CoreCommonException {
		HashMap<String, String> queryParams = new HashMap<String, String>();
		String pageID = "ymxz3l6qgd7y";
		String URI = "https://api.statuspage.io/v1/pages/" + pageID + "/incidents/" + incident_id;
		JSONObject child = new JSONObject();
		JSONObject child2 = new JSONObject();
		JSONArray array = new JSONArray();
		String componentId = "trp1ljjq75tp";
		//child.put("name", name);
		child.put("status", status);
		// child.put("impact_override", "minor");
		child.put("deliver_notifications", true);
		child.put("body", "looking for the issue ");
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
		try {
			String incidentID = "";
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

	public void getJobDetails() throws Exception {
		String URI = "https://deploy-test.sentieo.com/computer/api/json";
		HashMap<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("pretty", "true");
		RequestSpecification spec = RequestSpecificationformParamsSpecJenkins(queryParams);
		Response resp = RestOperationUtils.get(URI, spec, queryParams);
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		System.out.println(respJson);
		org.json.JSONArray arr = respJson.getJSONArray("computer");
		for (int i = 0; i < arr.length(); i++) {
			String displayName = arr.getJSONObject(i).getString("displayName");
			boolean status = arr.getJSONObject(i).getBoolean("offline");
			if (status) {
				incidentID = createIncident("ymxz3l6qgd7y", "trp1ljjq75tp", "jenkins_Windows_slave", displayName);
				System.out.println(incidentID);
				updateComponent("ymxz3l6qgd7y", "w7f5gfgfxmy8", displayName, "partial_outage",
						"Jenkins-Slave-Component");
			} else {
				updateIncident("jenkins_Windows_slave", incidentID, "resolved");
				updateComponent("ymxz3l6qgd7y", "w7f5gfgfxmy8", displayName, "operational", "Jenkins-Slave-Component");

			}
		}

	}

	@Test(groups = "savedseries", description = "fetch_saved_series", priority = 1)
	public void getName() throws Exception {
//		String pageID = "ymxz3l6qgd7y";
//		String componentId = "trp1ljjq75tp";
//		// getJobDetails();
//		// updateComponent("Checking for status");
//		// String incidentID=createIncident("Checking for status", "", "description");
//		String headerData = "Checking for status" + " is down again";
//		// updateIncident(headerData,"b0b0lfcq4wj2","Monitoring");
//		updateComponent("ymxz3l6qgd7y", "trp1ljjq75tp", "testing method check", "operational", "Sentieo login api");
////		createComponent("ymxz3l6qgd7y", "created throught api", "operational", "Jenkins-Slave-Component");
//		//String id=createIncident(pageID, componentId, "Incident_api", "test description");
//		//updateIncident("Incident_api", "y657l91bq29d", "resolved");
		// getJobDetails();
		//String componentName = "Jenkins-Slave-Component";
		String componentName="Sentieo servers";
		String id=getIncidents("ymxz3l6qgd7y",componentName);
		updateIncident("", id, "resolved");
	}
}
