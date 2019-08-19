package com.sentieo.utils;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;

public class FormatterUtil {

	private static final String BREAK_LINE = "</br>";
	
	public static String generateFormatedResponse(Response res, String schema) {
		return "Comparison of response body with schema: </br> " + generateFormatedPayload(res.asString(), "Response body (Click to Expand)") + BREAK_LINE + generateFormatedSchema(schema, "Schema (Click to Expand)");
	}
	
	public static String generateFormatedResponse(Response res, String schema, String diff) {
		return "Comparison of actual response body with expected schema: </br> " + 
				generateFormatedPayload(res.asString(), "Actual response body (Click to Expand)") + 
				BREAK_LINE + 
				generateFormatedSchema(schema, "Expected schema (Click to Expand)") +
				BREAK_LINE + 
				generateFormatedSchemaDiff(diff, "Expected schema and Actual response diff (Click to Expand)");
	}
	
	public static String generateFormatedPayload(String payload, String title) {
		String prettyPayload = "";
		try {
			if (payload.trim().startsWith("{") || payload.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ObjectMapper().readTree(payload));	
			else
				prettyPayload = payload;
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> "  + title + "</a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Payload (Click to Expand)</a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		}
	}
	
	public static String generateFormatedSchemaDiff(String schemaDiff, String title) {
		try {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> "  + title + "</a><xmp style=\"display:none\">"
					+ schemaDiff + "</xmp></>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String generateFormatedSchema(String schema, String title) {
		String prettyPayload = "";
		try {
			if(StringUtils.isEmpty(schema)) {
				prettyPayload = "Schema file not found";
			}
			else if (schema.trim().startsWith("{") || schema.trim().startsWith("["))
				prettyPayload = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(new ObjectMapper().readTree(schema));	
			else
				prettyPayload = schema;
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> "  + title + "</a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		} catch (Exception e) {
			return BREAK_LINE
					+ "<a style=\"cursor:pointer\" onclick=\"$(this).next('xmp').toggle()\"> Invalid JSON/XML Payload (Click to Expand)</a><xmp style=\"display:none\">"
					+ prettyPayload + "</xmp></>";
		}
	}

}
