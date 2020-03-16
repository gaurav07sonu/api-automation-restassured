package com.sentieo.user;

import static com.sentieo.utils.FileUtil.RESOURCE_PATH;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sentieo.utils.CSVReaderUtil;

public class Example {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		String file="user" + File.separator + "settings.json";
		try (FileReader reader = new FileReader(RESOURCE_PATH + File.separator + file)) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			JSONObject employeeList = (JSONObject) new JSONTokener(obj.toString()).nextValue();
			boolean on_off = employeeList.getBoolean("on_off");
//			String press_release = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("press_release").toString();
//			String transcript = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("transcript").toString();
//			JSONArray presentation = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("presentation");
//			JSONArray news = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("news");
//			JSONArray price_alerts = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("price_alerts");
//			JSONArray crypto_alerts = employeeList.getJSONObject("result").getJSONObject("ticker_settings")
//					.getJSONArray("crypto_alerts");
//		
			System.out.println(on_off);
			System.out.println(employeeList.getJSONArray("press_release").toString());
			System.out.println(employeeList.getJSONArray("transcript").toString());
			System.out.println(employeeList.getJSONArray("presentation").toString());
			System.out.println(employeeList.getJSONArray("news").toString());
			System.out.println(employeeList.getJSONArray("price_alerts").toString());
			System.out.println(employeeList.getJSONArray("crypto_alerts").toString());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void compare()
	{
		
	}

}
