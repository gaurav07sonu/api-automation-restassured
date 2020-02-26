package com.sentieo.notebook;

import java.util.HashMap;

import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.rest.base.APIDriver;
import com.sentieo.rest.base.RestOperationUtils;
import com.sentieo.utils.CoreCommonException;

public class KibanaLogs extends APIDriver {
	
	@Test
	public void getKibanaLogs() throws CoreCommonException {
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap.put("Host", "logwatcher.sentieo.com");
		hmap.put("Origin", "https://logwatcher.sentieo.com");
		hmap.put("Referer", "https://logwatcher.sentieo.com/app/kibana");
		hmap.put("kbn-xsrf", "reporting");
		RequestSpecification spec = kibanaAPISpec(hmap).queryParam("uri", "/api--*/_search");
		String payload = "{\n" + 
				"  \"size\": 500,\n" + 
				"  \"sort\": [\n" + 
				"    {\n" + 
				"      \"timestamp_ms\": {\n" + 
				"        \"order\": \"desc\",\n" + 
				"        \"unmapped_type\": \"boolean\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"query\": {\n" + 
				"    \"bool\": {\n" + 
				"      \"must\": [\n" + 
				"        {\n" + 
				"          \"query_string\": {\n" + 
				"            \"analyze_wildcard\": true,\n" + 
				"            \"query\": \"*\"\n" + 
				"          }\n" + 
				"        },\n" + 
				"        {\n" + 
				"          \"match\": {\n" + 
				"            \"hostname\": {\n" + 
				"              \"query\": \"app.sentieo.com\",\n" + 
				"              \"type\": \"phrase\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        },\n" + 
				"        {\n" + 
				"          \"match\": {\n" + 
				"            \"uid\": {\n" + 
				"              \"query\": \"5e182d3a7a782c26a95423a3\",\n" + 
				"              \"type\": \"phrase\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        },\n" + 
				"        {\n" + 
				"          \"range\": {\n" + 
				"            \"timestamp_ms\": {\n" + 
				"              \"gte\": 1582543125365,\n" + 
				"              \"lte\": 1582546725365,\n" + 
				"              \"format\": \"epoch_millis\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"must_not\": [\n" + 
				"        {\n" + 
				"          \"match\": {\n" + 
				"            \"status_code\": {\n" + 
				"              \"query\": 200,\n" + 
				"              \"type\": \"phrase\"\n" + 
				"            }\n" + 
				"          }\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"highlight\": {\n" + 
				"    \"pre_tags\": [\n" + 
				"      \"@kibana-highlighted-field@\"\n" + 
				"    ],\n" + 
				"    \"post_tags\": [\n" + 
				"      \"@/kibana-highlighted-field@\"\n" + 
				"    ],\n" + 
				"    \"fields\": {\n" + 
				"      \"*\": {}\n" + 
				"    },\n" + 
				"    \"require_field_match\": false,\n" + 
				"    \"fragment_size\": 2147483647\n" + 
				"  },\n" + 
				"  \"_source\": {\n" + 
				"    \"excludes\": []\n" + 
				"  },\n" + 
				"  \"aggs\": {\n" + 
				"    \"2\": {\n" + 
				"      \"date_histogram\": {\n" + 
				"        \"field\": \"timestamp_ms\",\n" + 
				"        \"interval\": \"1m\",\n" + 
				"        \"time_zone\": \"Asia/Kolkata\",\n" + 
				"        \"min_doc_count\": 1\n" + 
				"      }\n" + 
				"    }\n" + 
				"  },\n" + 
				"  \"stored_fields\": [\n" + 
				"    \"*\"\n" + 
				"  ],\n" + 
				"  \"script_fields\": {},\n" + 
				"  \"docvalue_fields\": [\n" + 
				"    \"timestamp_ms\"\n" + 
				"  ]\n" + 
				"}";
		Response resp = RestOperationUtils.post("https://logwatcher.sentieo.com/api/console/proxy", payload, spec, null);
		System.out.println(resp.asString());
		
		
//		RequestSpecification spec = kibanaAPISpec().queryParam("_g", "(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-30m,mode:quick,to:now))")
//				.queryParam("_a", "(columns:!(uid),filters:!(('$$hashKey':'object:1203','$state':(store:appState),meta:(alias:!n,disabled:!f,index:'api*',key:status_code,negate:!t,value:'200'),query:(match:(status_code:(query:200,type:phrase)))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'api*',key:uid,negate:!f,value:'59b3a86a1333c556a300483b'),query:(match:(uid:(query:'59b3a86a1333c556a300483b',type:phrase))))),index:'api*',interval:auto,query:'',sort:!(timestamp_ms,desc))");
//		Response resp = RestOperationUtils.get("https://logwatcher.sentieo.com/app/kibana#/discover", spec, null);
//		System.out.println(resp.asString());
	
		
		//https://logwatcher.sentieo.com/app/kibana#/discover?_g=(refreshInterval:(display:Off,pause:!f,value:0),time:(from:now-30m,mode:quick,to:now))&_a=(columns:!(uid),filters:!(('$$hashKey':'object:1203','$state':(store:appState),meta:(alias:!n,disabled:!f,index:'api*',key:status_code,negate:!t,value:'200'),query:(match:(status_code:(query:200,type:phrase)))),('$state':(store:appState),meta:(alias:!n,disabled:!f,index:'api*',key:uid,negate:!f,value:'59b3a86a1333c556a300483b'),query:(match:(uid:(query:'59b3a86a1333c556a300483b',type:phrase))))),index:'api*',interval:auto,query:'',sort:!(timestamp_ms,desc))
	}

	

}
