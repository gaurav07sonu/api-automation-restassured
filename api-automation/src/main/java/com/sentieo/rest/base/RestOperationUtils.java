package com.sentieo.rest.base;

import static com.jayway.restassured.RestAssured.given;

import java.util.HashMap;
import java.util.Map;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.report.Reporter;
import com.sentieo.utils.CoreCommonException;

public class RestOperationUtils {
	private static final String BREAK_LINE = "\n";
	protected static final Reporter reporter = Reporter.getInstance();

	private RestOperationUtils() {

	}

	public static Response post(String url, String payload, RequestSpecification spec, Map params)
			throws CoreCommonException {
		Response res = null;
		try {
			String classname = new Exception().getStackTrace()[1].getClassName();
			String payloadOutput = (payload == null || payload.isEmpty()) ? ""
					: reporter.generateFormatedPayload(payload);
			String infoMessage = BREAK_LINE + "<div> Type: [POST] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			infoMessage = infoMessage + BREAK_LINE + "<div>   URI : " + url + "</div>" + payloadOutput;
			String testname = new Exception().getStackTrace()[1].getMethodName();
			if (!(testname.equalsIgnoreCase("fetch_docid")))
				ExtentTestManager.getTest().log(LogStatus.INFO, infoMessage);
			if (payload == null) {
				res = given().spec(spec).when().post(url);
			} else {
				res = given().spec(spec).body(payload).when().post(url);
			}
		//	if (res.getStatusCode() != 200 && res.getStatusCode() != 201)
				if (!(testname.equalsIgnoreCase("fetch_docid")))
					ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));
//				else if (classname.contains("comparables") || classname.contains("finance")
//						|| classname.contains("screener") || classname.contains("EDT") || classname.contains("market"))
//					ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));

		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response login(String url, String payload, RequestSpecification spec, HashMap<String, String> params)
			throws CoreCommonException {
		Response res = null;
		try {
			if (payload == null) {
				res = given().spec(spec).when().post(url);
			} else {
				res = given().spec(spec).body(payload).when().post(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response get(String url, RequestSpecification spec, Map params) throws CoreCommonException {
		Response res = null;
		try {
			String classname = new Exception().getStackTrace()[1].getClassName();
			String infoMessage = BREAK_LINE + "<div> Type: [GET] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + BREAK_LINE + "<div> URI: " + url + "</div>";
			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);
			String testname = new Exception().getStackTrace()[1].getMethodName();
			if (!(testname.equalsIgnoreCase("fetch_docid")))
				ExtentTestManager.getTest().log(LogStatus.INFO, infoMessage);
			
			res = given().spec(spec).when().get(url);
//			if (res.getStatusCode() != 200 && res.getStatusCode() != 201)
			if (!(testname.equalsIgnoreCase("fetch_docid")))
				ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));
//			else if (classname.contains("comparables") || classname.contains("finance")
//					|| classname.contains("screener") || classname.contains("EDT") || classname.contains("market"))
//				ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response put(String url, String payload, RequestSpecification spec, Map params)
			throws CoreCommonException {
		Response res = null;
		try {
			String payloadOutput = (payload == null || payload.isEmpty()) ? ""
					: reporter.generateFormatedPayload(payload);
			String infoMessage = BREAK_LINE + "<div> Type: [PUT] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);
			infoMessage = infoMessage + BREAK_LINE + "<div>   URI : " + url + "</div>" + payloadOutput;

			// reporter.reportStep(StepStatus.INFO, REQUEST_MSG, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().put(url);
			} else {
				res = given().spec(spec).body(payload).when().put(url);
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response delete(String url, String payload, RequestSpecification spec, BaseRestParameter params)
			throws CoreCommonException {
		Response res = null;
		try {
			String payloadOutput = payload == null ? "" : reporter.generateFormatedPayload(payload);
			String infoMessage = BREAK_LINE + "<div> Type: [DELETE] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + BREAK_LINE + "<div> URI : " + url + "</div>" + payloadOutput;
			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			// reporter.reportStep(StepStatus.INFO, REQUEST_MSG, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().delete(url);
			} else {
				res = given().spec(spec).body(payload).when().delete(url);
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response patch(String url, String payload, RequestSpecification spec, BaseRestParameter params)
			throws CoreCommonException {
		Response res = null;
		try {
			String payloadOutput = (payload == null || payload.isEmpty()) ? ""
					: reporter.generateFormatedPayload(payload);

			String infoMessage = BREAK_LINE + "<div> Type: [PATCH] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + BREAK_LINE + "<div>  URI : " + url + "</div>" + payloadOutput;
			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			// reporter.reportStep(StepStatus.INFO, REQUEST_MSG, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().patch(url);
			} else {
				res = given().spec(spec).body(payload).when().patch(url);
			}
		} catch (Exception e) {
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response post(String url, String payload, RequestSpecification spec, Map params,
			boolean useExtentReport) throws CoreCommonException {
		Response res = null;
		try {
			String payloadOutput = (payload == null || payload.isEmpty()) ? ""
					: reporter.generateFormatedPayload(payload);
			String infoMessage = BREAK_LINE + "<div> Type: [POST] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			infoMessage = infoMessage + BREAK_LINE + "<div>   URI : " + url + "</div>" + payloadOutput;

			if (useExtentReport)
				ExtentTestManager.getTest().log(LogStatus.INFO, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().post(url);
			} else {
				res = given().spec(spec).body(payload).when().post(url);
			}

			if (useExtentReport)
				ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));

		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		}
		return res;
	}

	public static Response patch(String url, String payload, RequestSpecification spec, Map<?, ?> params)
			throws CoreCommonException {
		Response res = null;
		try {
			String payloadOutput = (payload == null || payload.isEmpty()) ? ""
					: reporter.generateFormatedPayload(payload);
			String infoMessage = BREAK_LINE + "<div> Type: [POST] </div>" + BREAK_LINE + "<div> Parameters: "
					+ (params == null ? "[EMPTY]" : params.toString()) + "</div>";

			infoMessage = infoMessage + reporter.generateFormatedRequestHeader(spec);

			infoMessage = infoMessage + BREAK_LINE + "<div>   URI : " + url + "</div>" + payloadOutput;
			ExtentTestManager.getTest().log(LogStatus.INFO, infoMessage);

			if (payload == null) {
				res = given().spec(spec).when().patch(url);
			} else {
				res = given().spec(spec).body(payload).when().patch(url);
			}
			ExtentTestManager.getTest().log(LogStatus.INFO, reporter.generateFormatedResponse(res));
		} catch (Exception e) {
			e.printStackTrace();
			throw new CoreCommonException(e);
		}
		return res;
	}
	
}
