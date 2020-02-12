package com.sentieo.heartbeat;

import static com.jayway.restassured.RestAssured.given;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class APIDriverHeartbeat {
	protected String apid = "";
	protected String usid = "";
	
	protected static HashMap<String, String> users = new HashMap<String, String>();
	protected static Set<String> taggedUsers = new HashSet<String>();
	protected static Set<String> failedAPIData = new HashSet<String>();
	

	protected static final String BREAK_LINE = "</br>";
	protected static StringBuffer sbPass = new StringBuffer();
	protected static StringBuffer sbFail = new StringBuffer();
	protected String time = "";
	protected String header = "";
	protected String footer = "";
	
	protected RequestSpecification loginSpec(HashMap<String, String> formParams) {
		formParams.put("csrfmiddlewaretoken", "a");
		return given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
				.accept(ContentType.JSON)
				.cookie("csrftoken", "a")
				.formParameters(formParams);
	}
	
	protected RequestSpecification formParamsSpec(HashMap<String, String> formParams) {
		formParams.put("csrfmiddlewaretoken", "a");
		return given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
					.accept(ContentType.JSON)
					.cookie("csrftoken", "a")
					.cookie("apid", apid)
					.cookie("usid", usid)
					.formParameters(formParams);
	}
	
	protected RequestSpecification queryParamsSpec(HashMap<String, String> queryParams) {
		queryParams.put("csrfmiddlewaretoken", "a");
		return given().contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.cookie("csrftoken", "a")
					.cookie("apid", apid)
					.cookie("usid", usid)
					.queryParams(queryParams);
	}
	
	protected RequestSpecification pathParamsSpec(HashMap<String, String> queryParams) {
		queryParams.put("csrfmiddlewaretoken", "a");
		return given().contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.cookie("csrftoken", "a")
					.cookie("apid", apid)
					.cookie("usid", usid)
					.pathParams(queryParams);
	}
	
	protected RequestSpecification multipartParamSpec(HashMap<String, Object> formParams, File file) {
		formParams.put("csrfmiddlewaretoken", "a");
		return given().multiPart(file)
					.accept(ContentType.JSON)
					.cookie("csrftoken", "a")
					.cookie("apid", apid)
					.cookie("usid", usid)
					.formParameters(formParams);
	}
	
	protected RequestSpecification multipartParamSpecForPublicApis(HashMap<String, Object> formParams, HashMap<String, String> headers, File file) {
		return given().multiPart(file)
					.accept(ContentType.JSON)
					.formParameters(formParams)
					.headers(headers);
	}
	
	protected RequestSpecification requestHeadersSpecForPublicApis(HashMap<String, String> headers) {
			return given().contentType("application/json; charset=UTF-8")
						.accept(ContentType.JSON)
						.headers(headers);
	}
	
	protected RequestSpecification requestHeadersFormSpecForPublicApis(String body, HashMap<String, String> headers) {
		return given().contentType("application/json; charset=UTF-8")
					.accept(ContentType.JSON)
					.headers(headers)
					.body(body);
	}
	
	protected RequestSpecification queryParamsSpecForPublicApis(HashMap<String, String> queryParams, HashMap<String, String> headers) {
		return given().contentType(ContentType.JSON)
					.accept(ContentType.JSON)
					.queryParams(queryParams)
					.headers(headers);
					
	}
	
	public static void updatePassResult(String path, String team, String statusCode, Response resp, HashMap<String, String> parameters) {
		sbPass.append("<tr class=\"item\">");
		
		sbPass.append("<td>");
		sbPass.append(path);
		sbPass.append("</td>");
		
		sbPass.append("<td>");
		sbPass.append(team);
		sbPass.append("</td>");
		
		sbPass.append("<td>");
		sbPass.append(statusCode);
		sbPass.append("</td>");
		
		sbPass.append("<td>");
		sbPass.append(generateFormatedResponse(resp, parameters, false, ""));
		sbPass.append("</td>");
		
		sbPass.append("<td>");
		sbPass.append("<span>&#9989;</span>");
		sbPass.append("</td>");
		
		sbPass.append("</tr>");
		taggedUsers.add(users.get(team));
	}
	
	public void updateFailResult(String path, String team, String statusCode, Response resp, HashMap<String, String> parameters, String error) {
		sbFail.append("<tr class=\"item\">");
	
		sbFail.append("<td>");
		sbFail.append(path);
		sbFail.append("</td>");
		
		sbFail.append("<td>");
		sbFail.append(team);
		sbFail.append("</td>");
		
		sbFail.append("<td>");
		sbFail.append(statusCode);
		sbFail.append("</td>");
		
		sbFail.append("<td>");
		sbFail.append(generateFormatedResponse(resp, parameters, true, error));
		sbFail.append("</td>");
		
		sbFail.append("<td>");
		sbFail.append("<span>&#10060;</span>");
		sbFail.append("</td>");
		
		sbFail.append("</tr>");
		taggedUsers.add(users.get(team));
		if(statusCode.equals("200")) {
			failedAPIData.add(path.substring(path.lastIndexOf("api"), path.length()-1) + " | Data check failed: " + error + "\n");
		}
		else {
			failedAPIData.add(path.substring(path.lastIndexOf("api"), path.length()-1) + " | Unexpected response code: " + statusCode + "\n");
		}
	}
		
	public static String readHTMLHeader() {
		StringBuilder sb = new StringBuilder();
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));
		SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
		String time = f.format(new Date());

        try (BufferedReader br = Files.newBufferedReader(Paths.get("src/test/resources/api-heartbeat/header.txt"))) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return sb.toString().replaceAll("TIME_PLACEHOLDER", time);
	}
	
	
	public static String readHTMLFooter() {
		StringBuilder sb = new StringBuilder();

        try (BufferedReader br = Files.newBufferedReader(Paths.get("src/test/resources/api-heartbeat/footer.txt"))) {

            // read line by line
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }

        return sb.toString();
	}
	
	
	public static String generateFormatedResponse(Response res, HashMap<String, String> parameters, boolean flag, String exceptionMsg) {
		JSONObject json = new JSONObject(parameters);
		if(flag) {
			return generateFormatedPayload(json.toString()) + BREAK_LINE + generateFormatedResponse(res.asString()) + BREAK_LINE + generateFormatedException(exceptionMsg);
		}
		else {
			return generateFormatedPayload(json.toString()) + BREAK_LINE + generateFormatedResponse("Not showing response body for passed tests!");
		}
	}
	

	public static String generateFormatedPayload(String payload) {
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
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Payload <u> <font size=\"2\" color=\"blue\">(Click to Expand / Collapse)</font> </u></a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Payload (Click to Expand / Collapse)</a><xmp style=\"display:none\">"
					+ payload + "</xmp></>";
		}
	}
	
	
	public static String generateFormatedResponse(String payload) {
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
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Response body <u> <font size=\"2\" color=\"blue\">(Click to Expand / Collapse) </font> </u> </a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Response body (Click to Expand / Collapse)</a><xmp style=\"display:none\">"
					+ payload + "</xmp></>";
		}
	}
	
	public static String generateFormatedException(String exceptionMsg) {
		try {
			String prettyPayload = "";
			if (exceptionMsg == null)
				prettyPayload = "No exception";
			else if (exceptionMsg.trim().isEmpty())
				prettyPayload = "No exception";
			else if (exceptionMsg.trim().startsWith("{") || exceptionMsg.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ObjectMapper().readTree(exceptionMsg));	
			else
				prettyPayload = exceptionMsg;

			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Exception stacktrace <u> <font size=\"2\" color=\"blue\">(Click to Expand / Collapse) </font> </u> </a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Response body (Click to Expand / Collapse)</a><xmp style=\"display:none\">"
					+ exceptionMsg + "</xmp></>";
		}
	}

}
