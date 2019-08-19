package com.sentieo.rest.base;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.specification.RequestSpecification;

public class APIDriver {
	
	protected String apid = "";
	protected String usid = "";
	
	
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
	
}
