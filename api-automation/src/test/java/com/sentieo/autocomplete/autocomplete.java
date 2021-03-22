package com.sentieo.autocomplete;

import static com.sentieo.constants.Constants.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CSVReaderUtil;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;
import com.sentieo.utils.JSONUtils;

public class autocomplete extends APIDriver {

	APIAssertions verify = null;
	JSONUtils jsonUtils = null;
	String URI = null;

	static String[][] tickers;

	@BeforeMethod(alwaysRun = true)
	public void setUp(Method testMethod) {
		verify = new APIAssertions();
		jsonUtils = new JSONUtils();
		CommonUtil commUtil = new CommonUtil();
		commUtil.generateRandomTickers(testMethod);
	}

	@BeforeClass(alwaysRun = true)
	public void setURI() {
		URI = USER_APP_URL;
	}

	@BeforeClass(alwaysRun = true)
	public void setTickers() {
		tickers = CSVReaderUtil.readAllDataAtOnce("finance" + File.separator + "autocomplete_ticker_list.csv");
	}

	@SuppressWarnings("unused")
	@Test(groups = {"checktest"}, description = "Check autocomplete api", dataProvider = "module-type", dataProviderClass = DataProviderClass.class)
	public void validateTickers(String moduleType, String sentieoEntity) throws CoreCommonException, IOException {
		try {
			for (String[] row : tickers) {
				String tickername = "";
				String type = "";
				String status = "";
				try {
					for (String cell : row) {
						if (tickername.isEmpty()) {
							tickername = cell;
							continue;
						}
						if (type.isEmpty()) {
							type = cell;
							continue;
						}
						if (status.isEmpty()) {
							status = cell;
						}
					}
					if (moduleType.equalsIgnoreCase("EDT")) // to print proper name in report
						moduleType = "company";

					HashMap<String, String> parameters = new HashMap<String, String>();
					parameters.put("suggest", tickername);
					parameters.put("allow_pvt_company", "true");
					parameters.put("pagetype", moduleType);
					parameters.put("sentieoentity", sentieoEntity);

					RequestSpecification spec = formParamsSpec(parameters);
					Response resp = RestOperationUtils.get(APP_URL + SEARCH_ENTITIES, spec, parameters);
					APIResponse apiResp = new APIResponse(resp);
					ExtentTestManager.getTest().log(LogStatus.INFO, "Ticker/Partial Search : " + tickername);
					if (!(apiResp.getStatusCode() == 200))
						verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
					verify.verifyResponseTime(resp, 5000);
					if (apiResp.getStatusCode() == 200) {
						JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
						if (!respJson.getJSONObject("response").getBoolean("status"))
							verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"),
									"verify api status");
						if (type.equalsIgnoreCase("public")) {
							JSONArray companylist;
							if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company"))
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
							else
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("sentieoentity");
							if (null == companylist || companylist.length() == 0) {
								verify.assertTrue(false, "Ticker not coming for search : " + tickername);
							}
							if (companylist.length() > 0) {
								JSONObject tickerData = companylist.getJSONObject(0);
								String token_label = tickerData.getString("token_label");
								String ticker_status = tickerData.getString("status");
								if (!tickerData.getString("name").toLowerCase().contains(tickername.toLowerCase()))
									verify.assertEqualsActualContainsExpected(
											tickerData.getString("name").toLowerCase(), tickername.toLowerCase(),
											"verify ticker name");
								if (ticker_status.isEmpty())
									verify.assertTrue(!ticker_status.isEmpty(), "verify ticker status");

								if (tickerData.getString("_id").isEmpty())
									verify.assertTrue(!tickerData.getString("_id").isEmpty(),
											"verify ticker _id present");
								if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
									if (!tickerData.getString("type").equalsIgnoreCase("company"))
										verify.verifyEquals(tickerData.getString("type"), "company",
												"verify company type");
								} else {
									if (!tickerData.getString("type").equalsIgnoreCase("sentieoentity"))
										verify.verifyEquals(tickerData.getString("type"), "sentieoentity",
												"verify company type");
								}
							}
						} else if (type.equalsIgnoreCase("private")) {
							JSONArray privcomp;
							if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company"))
								privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privcomp");
							else
								privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privateentity");
							if (null == privcomp || privcomp.length() == 0) {
								verify.assertTrue(false, "Ticker not coming for search : " + tickername);
							}
							if (privcomp.length() > 0) {
								JSONObject tickerData = privcomp.getJSONObject(0);
								String token_label = tickerData.getString("token_label");
								String ticker_status = tickerData.getString("status");
								if (!tickerData.getString("name").toLowerCase().contains(tickername.toLowerCase()))
									verify.assertEqualsActualContainsExpected(
											tickerData.getString("name").toLowerCase(), tickername.toLowerCase(),
											"verify ticker name");
								if (ticker_status.isEmpty())
									verify.assertTrue(!ticker_status.isEmpty(), "verify ticker status");

								if (tickerData.getString("_id").isEmpty())
									verify.assertTrue(!tickerData.getString("_id").isEmpty(),
											"verify ticker _id present");
								if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
									if (!tickerData.getString("type").equalsIgnoreCase("privcomp"))
										verify.verifyEquals(tickerData.getString("type"), "privcomp",
												"verify company type");
								} else {
									if (!tickerData.getString("type").equalsIgnoreCase("privateentity"))
										verify.verifyEquals(tickerData.getString("type"), "privateentity",
												"verify company type");
								}

								if (tickerData.getString("name").toLowerCase()
										.contains(tickerData.getString("_id").toLowerCase()))
									verify.assertTrue(false, "Id appearing in name" + tickerData.getString("name"));
							}
						} else {// for partial text search
							if (sentieoEntity.equals("0") || moduleType.equalsIgnoreCase("company")) {
								JSONArray companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
								verify.assertTrue(companylist.length() > 0, "company data should be present");

								JSONArray privcomp = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privcomp");
								verify.assertTrue(privcomp.length() > 0, "privcomp data should be present");

								if (privcomp.length() > 0) {
									if (privcomp.getJSONObject(0).getString("name").toLowerCase()
											.contains(privcomp.getJSONObject(0).getString("_id").toLowerCase()))
										verify.assertTrue(false,
												"Id appearing in name" + privcomp.getJSONObject(0).getString("name"));
								}
								if (!moduleType.equalsIgnoreCase("company")) {
//								JSONArray crypto = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("crypto");
//								verify.assertTrue(crypto.length() > 0, "crypto data should be present");

									JSONArray entity = respJson.getJSONObject("result").getJSONObject("data")
											.getJSONArray("entity");
									verify.assertTrue(entity.length() > 0, "entity data should be present");

//								if(!(tickername.equalsIgnoreCase("8") && tickername.equalsIgnoreCase("AA"))) {
//								JSONArray organization = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("organization");
//								verify.assertTrue(organization.length() > 0, "organization data should be present");
//								
//								
//								JSONArray debt = respJson.getJSONObject("result").getJSONObject("data")
//										.getJSONArray("debt");
//								verify.assertTrue(debt.length() > 0, "debt data should be present");
								}
							} else {
								JSONArray privateentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("privateentity");
								verify.assertTrue(privateentity.length() > 0, "privateentity data should be present");

								JSONArray sentieoentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("sentieoentity");
								verify.assertTrue(sentieoentity.length() > 0, "sentieoentity data should be present");

								JSONArray subsidiary = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("subsidiary");
								verify.assertTrue(subsidiary.length() > 0, "subsidiary data should be present");

								JSONArray cryptoentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("cryptoentity");
								verify.assertTrue(cryptoentity.length() > 0, "cryptoentity data should be present");

								JSONArray secentity = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("secentity");
								verify.assertTrue(secentity.length() > 0, "secentity data should be present");
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					verify.assertTrue(false, "ticker : " + tickername + e.toString());
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	@Test(groups = {"checktest" }, description = "Check autocomplete api", dataProvider = "module-type1", dataProviderClass = DataProviderClass.class)
	public void validateETF(String moduleType, String sentieoEntity,String privateCompany) throws CoreCommonException, IOException {
		try {
			for (Entry<Integer, String> tickerValue : CommonUtil.randomTickers.entrySet()) {
				String tickername = "";
				try {
						tickername = tickerValue.getValue();
						if (moduleType.equalsIgnoreCase("EDT")) // to print proper name in report
							moduleType = "company";
						HashMap<String, String> parameters = new HashMap<String, String>();
						parameters.put("suggest", tickername);
						parameters.put("allow_pvt_company", privateCompany);
						parameters.put("pagetype", moduleType);
						parameters.put("sentieoentity", sentieoEntity);

						RequestSpecification spec = formParamsSpec(parameters);
						Response resp = RestOperationUtils.get(APP_URL + SEARCH_ENTITIES, spec, parameters);
						APIResponse apiResp = new APIResponse(resp);
						ExtentTestManager.getTest().log(LogStatus.INFO, "Ticker/Partial Search : " + tickername);
						if (!(apiResp.getStatusCode() == 200))
							verify.verifyEquals(apiResp.getStatusCode(), 200, "Api response");
						verify.verifyResponseTime(resp, 5000);
						if (apiResp.getStatusCode() == 200) {
							JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
							if (!respJson.getJSONObject("response").getBoolean("status"))
								verify.assertTrue(respJson.getJSONObject("response").getBoolean("status"),
										"verify api status");
							JSONArray companylist;
							JSONArray nportmfetf;
							if (sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
								nportmfetf = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("nportmfetf");
							} else {
								companylist = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("company");
								nportmfetf = respJson.getJSONObject("result").getJSONObject("data")
										.getJSONArray("nportmfetf");
							}
							if (null == companylist || companylist.length() == 0) {
								verify.assertTrue(false, "Ticker not coming for search : " + tickername);
							}
							if (null == nportmfetf || nportmfetf.length() == 0) {
								verify.assertTrue(false, "ETF not coming for search : " + tickername);
							}
							if (companylist.length() > 0) {
								validateTickerData(companylist, tickername, moduleType, sentieoEntity);
							}
							if (nportmfetf.length() > 0) {
								validateTickerData(companylist, tickername, moduleType, sentieoEntity);
							}

						}
					
				} catch (Exception e) {
					e.printStackTrace();
					verify.assertTrue(false, "ticker : " + tickername + e.toString());
				}
			}
		} catch (JSONException je) {
			je.printStackTrace();
			ExtentTestManager.getTest().log(LogStatus.FAIL, je.getMessage());
			verify.verificationFailures.add(je);
		} finally {
			verify.verifyAll();
		}
	}

	public void validateTickerData(JSONArray companylist, String tickername, String moduleType, String sentieoEntity) {
		JSONObject tickerData = companylist.getJSONObject(0);
		String token_label = tickerData.getString("token_label");
		String ticker_status = tickerData.getString("status");
		String name = tickerData.getString("name");
		if (!name.toLowerCase().contains(tickername.toLowerCase()))
			verify.assertEqualsActualContainsExpected(name.toLowerCase(), tickername.toLowerCase(),
					"verify ticker name");
		if (!token_label.equalsIgnoreCase(tickername)) {
			verify.assertEqualsActualContainsExpected(token_label.toLowerCase(), tickername.toLowerCase(),
					"verify ticker name");
		}
		if (ticker_status.isEmpty())
			verify.assertTrue(!ticker_status.isEmpty(), "verify ticker status");

		if (tickerData.getString("_id").isEmpty())
			verify.assertTrue(!tickerData.getString("_id").isEmpty(), "verify ticker _id present");
//		if(sentieoEntity.equalsIgnoreCase("0") || moduleType.equalsIgnoreCase("company")) {
//		if (!tickerData.getString("type").equalsIgnoreCase("company")) 
//			verify.verifyEquals(tickerData.getString("type"), "company", "verify company type");
//		}else {
//			if (!tickerData.getString("type").equalsIgnoreCase("sentieoentity"))
//				verify.verifyEquals(tickerData.getString("type"), "sentieoentity", "verify company type");									
//		}
	}

}
