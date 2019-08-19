package com.sentieo.rest.base;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

public class APIResponse {
	protected final Response res;
	private static final String BREAK_LINE = "</br>";

	private static final String HTTP_INFO_STYLE = "<span style=\"font: bold 12px/30px Georgia, serif;margin-right:5px\" >";
	
	public APIResponse(Response res) {
		this.res = res;
	}
	
	public String getResponseAsString() throws Exception
	{
		String infoMessage = "";
		String response = "";
		try {
			infoMessage = "Error while trying to get response as string.";
			if (res != null)
				response = this.res.asString();
		}
		catch(Exception e)
		{
		}
		return response;
	}

	public int getStatusCode() throws Exception {
		int actualStatusCode = 0;
		String infoMessage = "";
		try {
			actualStatusCode = res.getStatusCode();
			infoMessage = infoMessage + BREAK_LINE + " A: " + actualStatusCode;
		} catch (Exception e) {
		}
		return actualStatusCode;
	}
	
	public String getResponseHeaderValue(String headerName) throws Exception {
		System.out.println("++++++" + res.getHeaders().toString());
		return res.header(headerName);
	}
	
	public Object getNodeValue(String jsonPath) throws Exception {
		Object obj = "";
		String infoMessage = "getNodevalue " + BREAK_LINE + " JsonPath: " + jsonPath;
		try {
			String json = res.asString();
			obj = JsonPath.with(json).get(jsonPath);
			infoMessage = infoMessage + BREAK_LINE + " Received value: " + obj;
		}catch (IllegalArgumentException e){
			return "";
		} catch (Exception e) {
		}
		return obj==null?"":obj;
	}
	
	public boolean isNodePresent(String jsonPath) throws Exception {
		JsonPath j = new JsonPath(res.asString());
		Boolean value = false;
		String infoMessage = "isNodePresent " + BREAK_LINE + "JsonPath:" + jsonPath;
		try {
			if (j.get(jsonPath).getClass().equals(ArrayList.class)) {

				List<Object> jsonList = j.getJsonObject(jsonPath);
				for (Object obj : jsonList) {
					if (obj != null) {
						value = true;
						break;
					} else
						value = false;
				}
			} else {
				value = (j.get(jsonPath).toString() != null);
			}
		} catch (Exception e) {
			value = false;
		}
		return value;
	}
	
	   
	public void readSchemaFile(String jsonFileName) {
		try {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(jsonFileName);
        Object obj = jsonParser.parse(reader);
        System.out.println(obj.toString());
	             
		} catch (Exception e) {
			e.printStackTrace();
	}
}

	
	public boolean contains(String valueToFind) throws Exception {
		boolean containString = false;
		String infoMessage = "";
		try {
			String response = res.asString();
			if (StringUtils.contains(response, valueToFind) && StringUtils.containsIgnoreCase(response, valueToFind)) {
				infoMessage = "ContainsString " + BREAK_LINE + "Contains expected string in response : " + valueToFind;
				containString = true;
			} else {
				infoMessage = "Containsstring " + BREAK_LINE + "Does not Contains string in response : " + valueToFind;
			}
		} catch (Exception e) {
		}
		return containString;
	}
}
