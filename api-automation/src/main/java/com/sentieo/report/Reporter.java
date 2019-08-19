package com.sentieo.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.FilterableRequestSpecification;
import com.jayway.restassured.specification.RequestSpecification;


public class Reporter {

	private static Reporter instance;

	private static final String BREAK_LINE = "</br>";
	private static final String HTTP_INFO_STYLE = "<span style=\"font: bold 12px/30px Georgia, serif;margin-right:5px\" >";


	/**
	 * Creates an instance of Reporter
	 * @return Reporter
	 */
	public static Reporter getInstance() {
		// Double checked locking principle is used
		if (instance == null) {
			synchronized (Reporter.class) {
				if (instance == null) {
					instance = new Reporter();
				}
			}
		}
		return instance;
	}



	/**
	 * Generated user friendly format of the json payload
	 * @param payload
	 * @return
	 */
	public String generateFormatedResponse(Response res) {
		return "Response: </br><a style=\"cursor:pointer\" onclick=\"$(this).next('div').toggle()\"> HTTP Info (Click to Expand)</a>"+
				"<div style=\"display:none\">"+
				HTTP_INFO_STYLE + " Status: </span><span> " + res.getStatusLine()+"</span>" +  BREAK_LINE + 
				HTTP_INFO_STYLE + " Content Type: </span><span>" + res.getContentType()+"</span>" +  BREAK_LINE + 
				HTTP_INFO_STYLE + " Content Length: </span><span>" + res.getHeader("Content-Length")+"</span>" +  BREAK_LINE + 
				HTTP_INFO_STYLE + " Date: </span><span>" + res.getHeader("Date")+"</span>" +  BREAK_LINE + 
				HTTP_INFO_STYLE + " Response Time (ms):</span><span>" + res.getTime() +"</span>" +  BREAK_LINE + 
				"</div></>" + generateFormatedPayload(res.asString());

	}

	public String generateFormatedPayload(String payload) {
		try {
			String prettyPayload = "";
			if (payload == null)
				prettyPayload = "No Payload Body";
			else if (payload.trim().isEmpty())
				prettyPayload = "No Payload Body";
			else if (payload.trim().startsWith("{") || payload.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ObjectMapper().readTree(payload));	
			else
				prettyPayload = payload;

			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Payload (Click to Expand)</a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Payload (Click to Expand)</a><xmp style=\"display:none\">"
					+ payload + "</xmp></>";
		}
	}

	public String generateFormatedRequestHeader(RequestSpecification spec) {
		String headers = "";
		try {
			FilterableRequestSpecification filterRqSpec = (FilterableRequestSpecification) spec;
			Headers rqHeaders = filterRqSpec.getHeaders();

			for (Header header : rqHeaders) {
				if(header.getName().equalsIgnoreCase("appkey") && !(header.getValue().contains("internal")))
				{
					String appkey = header.getValue();
					String last4Chars = appkey.substring(Math.max(0, appkey.length() - 4));
					
					appkey = appkey.replaceAll("[0-9a-zA-Z]", "*");
					appkey = appkey.substring(0, appkey.length() - 4) + last4Chars;
					
					headers = headers + "<div>appkey: " + appkey + "</div>";
				}
				else
					headers = headers + "<div>" + header.getName() + ": " + header.getValue() + "</div>";
			}

			return "<a style=\"cursor:pointer\" onclick=\"$(this).next('font').toggle()\"> Request Header (Click to Expand)</a><font style=\"display:none\" color=\"green\">"
					+ headers + "</font></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('font').toggle()\"> Error in getting/formatting request header for report</a><font style=\"display:none\" color=\"green\">"
					+ headers + "</font></>";
		}
	}





}
