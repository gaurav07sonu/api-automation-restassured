package com.sentieo.rest.base;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.JSONParser;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.sentieo.utils.CoreCommonException;

public class APIResponse {
	protected final Response res;
	private static final String BREAK_LINE = "</br>";

	public APIResponse(Response res) {
		this.res = res;
	}
	
	public String getResponseAsString() throws CoreCommonException
	{
		String response = "";
		try {
			if (res != null)
				response = this.res.asString();
		}
		catch(Exception e)
		{
			throw new CoreCommonException(e);
		}
		return response;
	}

	public int getStatusCode() throws CoreCommonException {
		int actualStatusCode = 0;
		String infoMessage = "";
		try {
			actualStatusCode = res.getStatusCode();
			infoMessage = infoMessage + BREAK_LINE + " A: " + actualStatusCode;
		} catch (Exception e) {
			throw new CoreCommonException(e);
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
		try {
			String response = res.asString();
			if (StringUtils.contains(response, valueToFind) && StringUtils.containsIgnoreCase(response, valueToFind)) {
				containString = true;
			} else {
			}
		} catch (Exception e) {
		}
		return containString;
	}
	
	public String getContentType() {
		return res.getContentType();
	}
	
}
