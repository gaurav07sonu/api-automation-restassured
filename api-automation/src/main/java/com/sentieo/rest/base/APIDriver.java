package com.sentieo.rest.base;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.util.HashMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.utils.CoreCommonException;
import static com.sentieo.constants.Constants.*;

public class APIDriver {
	
	protected APIAssertions verify = null;
	static protected String apid = "";
	static protected String usid = "";
	

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();
	}
	
	
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
	
	protected RequestSpecification kibanaAPISpec(HashMap<String, String> headers) {
		return given().auth().basic("watcher", "@p!M0n!nt0r!ng@S3nt!3o").contentType(ContentType.JSON)
					.accept(ContentType.JSON).headers(headers);
					
	}
	
	protected RequestSpecification targetProcess() {
		return given().auth()
				  .basic("sanjay.saini@sentieo.com", "Iniaskyajnas97").contentType(ContentType.JSON);
	}
	
	@BeforeSuite(alwaysRun = true)
	public void login() throws CoreCommonException {
		try {
			RestAssured.baseURI = APP_URL;
			String URI = USER_APP_URL + LOGIN_URL;
			HashMap<String, String> loginData = new HashMap<String, String>();
			loginData.put("email", EMAIL);
			loginData.put("password", PASSWORD);

			RequestSpecification spec = loginSpec(loginData);
			Response resp = RestOperationUtils.login(URI, null, spec, loginData);
			apid = resp.getCookie("apid");
			usid = resp.getCookie("usid");
			if(apid.isEmpty() || usid.isEmpty()) {
				System.out.println("Login failed");
				System.exit(1);
				}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
