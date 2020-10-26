package com.sentieo.returnsperchange;

import static com.sentieo.constants.Constants.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.APIResponse;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CommonUtil;
import com.sentieo.utils.CoreCommonException;

public class ReturnsPercentChange extends APIDriver {

	List<String> returnsTickers = new ArrayList<String>();
	String startString = "";
	String endString = "";
	BigDecimal perChangeCriteria = new BigDecimal(60);
	static List<String> failedTickers = new ArrayList<String>();

	@BeforeClass(alwaysRun = true)
	public void initVerify() {
		CommonUtil commUtil = new CommonUtil();
		long startDate = commUtil.getTimeStamp(-30);
		startString = String.valueOf(startDate);
		long endDate = commUtil.getTimeStamp(+10);
		endString = String.valueOf(endDate);
		verify = new APIAssertions();
		returnsTickers = commUtil.getReturnsTickers();
	}

	@Test(description = "Verify Data", priority = 1)
	public void verifyReturnsPerecentChange() throws CoreCommonException {
		if (failedTickers.size() != 0)
			verify.assertFalse(true, "Returns update more than 60 % for these tickers " + failedTickers);
		else
			verify.assertTrue(true, "Verify Returns per change");
		verify.verifyAll();
	}

	@Test(description = "Match stock price plotter series and stream call ", dataProvider = "returnsData", dataProviderClass = DataProviderClass.class, priority = 0)
	public void verifyReturnsPerecentChange(String abs_weight_method, String abs_rebalance) throws CoreCommonException {
		HashMap<String, String> parameters = new HashMap<String, String>();
		String fetchGraphURI = APP_URL + FETCH_GRAPH_DATA;
		try {
			for (int i = 0; i < returnsTickers.size(); i++) {
				parameters.put("head_name", "Returns");
				parameters.put("singleapi", "true");
				parameters.put("pagetype", "plotter");
				parameters.put("graphtype_original", "avg_pricechange");
				parameters.put("graphtype", "avg_pricechange");
				parameters.put("abs_ticker_list", returnsTickers.get(i));
				parameters.put("abs_watchlist", returnsTickers.get(i));
				parameters.put("abs_weight_method", abs_weight_method);
				parameters.put("abs_rebalance", abs_rebalance);
				parameters.put("display_period", "continuous");
				parameters.put("rel_weight_method", abs_weight_method);
				parameters.put("rel_rebalance", abs_rebalance);
				parameters.put("abs_addipoinstantly", "True");
				parameters.put("abs_dividends", "True");
				parameters.put("rel_dividends", "True");
				parameters.put("ticker", "aapl");
				parameters.put("cumulative_yaxis", "Total Returns");
				parameters.put("start_date", startString);
				parameters.put("end_date", endString);

				RequestSpecification spec = queryParamsSpec(parameters);
				Response resp = RestOperationUtils.get(fetchGraphURI, spec, parameters);
				APIResponse apiResp = new APIResponse(resp);
				verify.verifyStatusCode(apiResp.getStatusCode(), 200);
				if (apiResp.getStatusCode() == 200) {
					JSONObject respJson = new JSONObject(apiResp.getResponseAsString());
					verify.verifyEquals(respJson.getJSONObject("response").getBoolean("status"), true,
							"Verify the API Response Status");
					verify.verifyResponseTime(resp, 5000);
					JSONArray values = respJson.getJSONObject("result").getJSONArray("series").getJSONObject(0)
							.getJSONArray("series");
					if (values.length() != 0 & values != null) {
						Double returnValue = (Double) values.getJSONArray(values.length() - 1).get(1);
						Double returnValue2 = (Double) values.getJSONArray(values.length() - 2).get(1);
						double diff = 0;
						if (returnValue > returnValue2)
							diff = returnValue - returnValue2;
						else
							diff = returnValue2 - returnValue;
						double per = diff / returnValue;
						per = per * 100;
						String returnsPER = BigDecimal.valueOf(per).toPlainString();
						BigDecimal bigDecimalCurrency = new BigDecimal(returnsPER);
						if (bigDecimalCurrency.compareTo(perChangeCriteria) == 1)
							failedTickers.add(returnsTickers.get(i));
						else if (bigDecimalCurrency.compareTo(perChangeCriteria) == 0)
							failedTickers.add(returnsTickers.get(i));
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			verify.verifyAll();
		}
	}

	
}
