package com.sentieo.finance;

import static com.sentieo.constants.Constants.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;


public class MobileFinanceApi extends APIDriver {
	
	APIAssertions verify = new APIAssertions();
	InputTicker obj = new InputTicker();
	List<String[]> tickers = obj.readTickerCSV();

	@BeforeMethod
	public void initVerify() {
		verify = new APIAssertions();

	}
	
	@Test(groups="mobile",description = "fetch_graph_data - Price chart")
	public void fetchGraphDataPriceChart() throws Exception {
		String URI = APP_URL + FETCH_GRAPH_DATA;
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				
		HashMap<String, String> graphData = new HashMap<String, String>();

		List<String> graphType = new ArrayList<String>();
		graphType.add("stock");
		graphType.add("newratio");
		graphType.add("shorts");

		List<String> seriesId = new ArrayList<String>();
		seriesId.add("ev_ebitda");
		seriesId.add("p_sales");
		seriesId.add("p_eps");

		for (String graph : graphType) {
		 
			if (graph.equals("stock")) {
				System.out.println("graph type is stock");
				graphData.put("ticker", cell);
				graphData.put("graphtype", graph);
				graphData.put("pagetype", "realtool");
				graphData.put("term", cell);

				RequestSpecification spec = formParamsSpec(graphData);
				Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, graphData);				
				fetchGraphDataPriceChartAssertions(resp);


			} else if (graph.equals("newratio")) {
				System.out.println("graph type is newratio");
				for (String seriesType : seriesId) {
					System.out.println("series is "+ seriesType);
					graphData.put("ticker", cell);
					graphData.put("graphtype", graph);
					graphData.put("seriesid", seriesType);
					graphData.put("ratio", seriesType);
					graphData.put("ptype", "rolling");
					graphData.put("shift", "backward");
					graphData.put("ratio_name", "Next+4+Quarters");

					RequestSpecification spec = formParamsSpec(graphData);
					Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, graphData);					
					fetchGraphDataPriceChartAssertions(resp);

				}
			} else {
				
				System.out.println("graph type is shorts");
				graphData.put("ticker", cell);
				graphData.put("subtype_shorts", "shorts");
				graphData.put("graphtype", graph);
				graphData.put("seriesid", "shorts");
				graphData.put("call_key", "get_graph");

				RequestSpecification spec = formParamsSpec(graphData);
				Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, graphData);	
				fetchGraphDataPriceChartAssertions(resp);
			}			
		}
	}
		}
		verify.verifyAll();
	}
	
	public void fetchGraphDataPriceChartAssertions(Response resp) throws Exception
	{
		APIResponse apiResp = new APIResponse(resp);
		JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
		verify.verifyStatusCode(apiResp.getStatusCode(), 200);
		verify.verifyResponseTime(resp, 5000);
		verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
				"Verify the API Response Status");
//		verify.verifyEquals(respJson.getJSONObject("result").get("series").getClass().getName()
//				 ,"org.json.JSONArray", "Verify data type for key: series");
				  verifykeyAvailable(respJson.getJSONObject("result"),"series","org.json.JSONArray");
	}

	
   @Test(groups = "mobile", description = "mobile financial model")
	public void mobileFinModelYearlyNew() throws Exception {
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				
				HashMap<String, String> tickerData = new HashMap<String, String>();
			
				tickerData.put("ticker", cell);
				tickerData.put("loc", "ios");
				RequestSpecification spec = formParamsSpec(tickerData);
				
				Response resp = RestOperationUtils.post(MOBILE_FIN_MODEL_YEARLY_NEW, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				
				//System.out.println("Response body is: "+ respJson );
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				
				
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status"); 
				JSONObject result = respJson.getJSONObject("result");
				
				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("first_col_map", "org.json.JSONObject");
				keyMap.put("top_header", "org.json.JSONArray");
				keyMap.put("data", "org.json.JSONArray");				

          for (Map.Entry<String, String> set : keyMap.entrySet()) {
				verifykeyAvailable(result, set.getKey(), set.getValue());
				System.out.println(set.getKey() + " = " + set.getValue());
			}
          verifykeyAvailable(result.getJSONObject("formatting"),"$0.00","org.json.JSONArray");
          
				JSONArray data = result.getJSONArray("data");
				if (data.length() == 0 || data == null)
				{
					verify.assertTrue(false, "API shows blank data");
				}
			}
		}
			
				verify.verifyAll();		
   }
   
   @Test(groups = "mobile", description = "mobile live price")
  	public void fetchMobileLivePrice() throws Exception {
  		for (String[] row : tickers) {
  			for (String cell : row) {
  				cell = cell.toLowerCase();
  				
  				HashMap<String, String> tickerData = new HashMap<String, String>();
  				tickerData.put("ticker", cell);
  				tickerData.put("loc", "ios");
  				tickerData.put("lp", "-1");
  				RequestSpecification spec = formParamsSpec(tickerData);
  				
  				Response resp = RestOperationUtils.post(FETCH_MOBILE_LIVE_PRICE, null, spec, tickerData);
  				APIResponse apiResp = new APIResponse(resp);
  				JSONArray respArray= new JSONArray(apiResp.getResponseAsString());
  				
  				System.out.println(respArray);
  				//JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
  				//System.out.println("Response body is: "+ apiResp.getResponseAsString() );
  				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
  				verify.verifyResponseTime(resp, 5000); 
  				if (respArray.length() == 0 || respArray == null)
  					verify.assertTrue(false, "API shows blank data");
  				verify.verifyEquals(respArray.get(0), cell.toUpperCase(),
						"Verify ticker data"); 				
  			}
  		}
  		verify.verifyAll();
     }
   
   @Test(groups="mobile",description = "fetch_graph_data - Fin Matrices",dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)
	public void fetchGraphDataFinMatrices(String subType) throws Exception {
	             
	   for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				HashMap<String, String> tickerData = new HashMap<String, String>();
				tickerData.put("ticker", cell);
				tickerData.put("yoy", "true");
				tickerData.put("ttmind", "true");
				tickerData.put("getestimates", "true");
				tickerData.put("graphtype", "financialData");
				tickerData.put("subtype", subType);
				tickerData.put("loc", "ios");
				
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
							
				verifykeyAvailable(respJson.getJSONObject("result"),"series","org.json.JSONArray");
				
				JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("yaxis", "java.lang.String");
				keyMap.put("series", "org.json.JSONArray");
				keyMap.put("title", "java.lang.String");
				
				if (series.length() != 0 || series != null)
				{
					for(int i=0; i<series.length(); i++)
					{
								
						for (Map.Entry<String, String> set : keyMap.entrySet()) {
							verifykeyAvailable(series.getJSONObject(i), set.getKey(), set.getValue());
							System.out.println(set.getKey() + " = " + set.getValue());
						}
//						
//						
//						JSONArray series2 = series.getJSONObject(i).getJSONArray("series");
//						for(int j = 0; j< series2.length(); j++)
//						{
//						verify.verifyEquals(series2.getJSONObject(i).get("color").getClass().getName(), "java.lang.String",
//								"Verify data type for key series -> color");
//						}
					}
				}
				else
				{
					verify.assertTrue(false, "API shows blank series");
				}
			}
	   }
	   verify.verifyAll();
           }
   
   @Test(groups="mobile",description = "fetch_graph_data - Yearly Stacked Price Chart ")
	public void fetchGraphDataYearlyStackedChart() throws Exception {
	             
	   for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				HashMap<String, String> tickerData = new HashMap<String, String>();
				
				tickerData.put("ticker", cell);
				tickerData.put("term", cell);
				tickerData.put("loc", "ios");
				tickerData.put("yoy", "1");
				tickerData.put("graphtype", "stock");
				
				RequestSpecification spec = formParamsSpec(tickerData);
				Response resp = RestOperationUtils.post(FETCH_GRAPH_DATA, null, spec, tickerData);
				APIResponse apiResp = new APIResponse(resp);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
		
				verifykeyAvailable(respJson.getJSONObject("result"),"series","org.json.JSONArray");
				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("title", "java.lang.String");
				keyMap.put("series", "org.json.JSONArray");
				
				JSONArray series = respJson.getJSONObject("result").getJSONArray("series");
				
				if (series.length() != 0 || series != null)
				{
				 for(int i=0; i< series.length();i++)
				 {
					 for (Map.Entry<String, String> set : keyMap.entrySet()) {
							verifykeyAvailable(series.getJSONObject(i), set.getKey(), set.getValue());
							System.out.println(set.getKey() + " = " + set.getValue());
						}
				}
				}
				else
				{
					verify.assertTrue(false, "API shows blank series");
				}
			}
	   }
	   verify.verifyAll();
          }
   
	@Test(groups="mobile", description = "fetch_value_table_for_mobile", dataProvider = "fetch_yearly_data", dataProviderClass = DataProviderClass.class)	 
   public void fetchValueTableForMobile(String subtype) throws Exception {
		
		for (String[] row : tickers) {
			for (String cell : row) {
				cell = cell.toLowerCase();
				HashMap<String, String> tickerData = new HashMap<String, String>();	
				tickerData.put("ticker", cell);
				tickerData.put("type", subtype);
				tickerData.put("loc", "ios");
				RequestSpecification spec = formParamsSpec(tickerData);
			    Response resp = RestOperationUtils.post(FETCH_VALUE_DATA, null, spec, tickerData);
                APIResponse apiResp = new APIResponse(resp);
				
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
				verify.verifyResponseTime(resp, 5000);
				verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
						"Verify the API Response Status");
				JSONObject result = respJson.getJSONObject("result");
				
				HashMap<String, String> keyMap = new HashMap<String, String>();
				keyMap.put("data", "org.json.JSONArray");
				keyMap.put("cur_year", "java.lang.Integer");
				keyMap.put("top_header", "org.json.JSONArray");
				keyMap.put("first_col_map", "org.json.JSONObject");
				keyMap.put("report_currency", "java.lang.String");
				
				for (Map.Entry<String, String> set : keyMap.entrySet()) {
					verifykeyAvailable(result, set.getKey(), set.getValue());
					System.out.println(set.getKey() + " = " + set.getValue());
				}
				
				verify.verifyEquals((result.get("cur_year")), Calendar.getInstance().get(Calendar.YEAR),
						"Verify the value of key cur_year");
				
				JSONArray data = result.getJSONArray("data");
				if (data.length() == 0 || data == null)
				{
					verify.assertTrue(false, "API shows blank data");
				}					
			}
		}
		verify.verifyAll();
   }
   
   
   public void verifykeyAvailable(JSONObject result, String key, String type) {
		if (result.has(key)) {
			verify.verifyEquals(result.get(key).getClass().getName(), type,
					"Verify data type for key: "+key );
		} else
			verify.assertTrue(false, key + " :key not found");
	}
   
  
}

	
