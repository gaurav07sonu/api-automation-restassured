package com.sentieo.tp;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.googlesheet.GoogleSheetUtil;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.RestOperationUtils;
import static com.jayway.restassured.path.xml.XmlPath.*;

public class TestPlanRunData extends APIDriver{
	
	List<String> googleSheetTPLinkList = new ArrayList<String>();
	HashMap<String, Integer> hm = new HashMap<String, Integer>();
	int counter = 2;
	
	@BeforeClass
	public void setUp() throws IOException, GeneralSecurityException {
		GoogleSheetUtil.spreadsheetId = "1B90FnGbUgxTyVGHQBzq-nFw51GpfJ5DqXmP7RtYQTaM";
		googleSheetTPLinkList = GoogleSheetUtil.getData();
		hm.put("total", 0);
		hm.put("pass", 0);
		hm.put("fail", 0);
		hm.put("notrun", 0);
	}
	

	@Test
	public void getData() throws Exception {
		if(googleSheetTPLinkList.isEmpty()) {
		}
		else {
			for (String testPlanLink : googleSheetTPLinkList) {
				
				if(!testPlanLink.contains("http")){
					counter = counter+1;
					continue;
				}
				try {
					String testPlanId = getTestPlanId(testPlanLink);
					String URI = "https://sentieo.tpondemand.com/api/v1/TestPlans/" + testPlanId + "?include=[TestPlanRuns]";
					RequestSpecification spec = targetProcess();
					Response resp = RestOperationUtils.get(URI, spec, new HashMap<String, String>());
					String response = resp.getBody().asString();
					System.out.println(response);
					XmlPath xmlPath = new XmlPath(response);
					xmlPath.setRoot("TestPlan.TestPlanRuns");	
					int latestRun = getLatestRun(response);
					
					URI = "https://sentieo.tpondemand.com/api/v1/TestPlanRuns/" + latestRun;
					resp = RestOperationUtils.get(URI, spec, new HashMap<String, String>());
					String xml = resp.getBody().asString();
					xmlPath = new XmlPath(xml);
					xmlPath.setRoot("TestPlanRun");	
					
					String notRunCount = xmlPath.get("NotRunCount");
					System.out.println(notRunCount);
					
					String passedCount = xmlPath.get("PassedCount");
					System.out.println(passedCount);
					
					String failedCount = xmlPath.get("FailedCount");
					System.out.println(failedCount);
					
					String onHoldCount = xmlPath.get("OnHoldCount");
					System.out.println(onHoldCount);
					int total = Integer.parseInt(notRunCount) + Integer.parseInt(passedCount) + Integer.parseInt(failedCount) + Integer.parseInt(onHoldCount);
					GoogleSheetUtil.setValue("C" + String.valueOf(counter),"C", total);
					GoogleSheetUtil.setValue("D" + String.valueOf(counter),"D", Integer.parseInt(passedCount));	
					GoogleSheetUtil.setValue("E" + String.valueOf(counter),"E", Integer.parseInt(failedCount));	
					GoogleSheetUtil.setValue("F" + String.valueOf(counter),"F", Integer.parseInt(notRunCount));	
					hm.put("total", hm.get("total") + total);
					hm.put("pass", hm.get("pass") + Integer.parseInt(passedCount));
					hm.put("fail", hm.get("fail") + Integer.parseInt(failedCount));
					hm.put("notrun", hm.get("notrun") + Integer.parseInt(notRunCount));
					
				} catch (Exception e) {
				e.printStackTrace();	
				}
				counter = counter+1;
			}

		}
		
	}
	
	public String getTestPlanId(String id) {
		String value = id.substring(id.indexOf("entity/"), id.length());
		String[] data = value.split("-");
		System.out.println(data[0]);
		return data[0].replace("entity/", "");
		
	}
	
	public int getLatestRun(String xml) {
		List<Integer> list = new ArrayList<Integer>();
		XmlPath xmlPath = new XmlPath(xml);
		xmlPath.setRoot("TestPlan.TestPlanRuns");	
		int items = with(xml).get("TestPlan.TestPlanRuns.TestPlanRun.size()");
		
		for (int i=0;i<items; i++) {
			String iteration = xmlPath.get("TestPlanRun[" + i + "].@Id");
			list.add(Integer.parseInt(iteration));
		}
		list.sort(Comparator.naturalOrder());
		System.out.println("List: " + list + " & latest is: " + list.get(list.size()-1));
		return list.get(list.size()-1);
	}

}