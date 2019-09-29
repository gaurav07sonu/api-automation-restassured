package com.sentieo.utils;

import static com.sentieo.constants.Constants.RESOURCE_PATH;

import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class CommonUtil {
	public static HashMap<Integer, String> randomTickers = new HashMap<Integer, String>();

	public String convertTimestampIntoDate(int time) {

		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
				.withZone(ZoneId.systemDefault());
		long epoch = time;
		Instant instant = Instant.ofEpochSecond(epoch);
		String output = formatter.format(instant);
		return output;
	}

	public String dateValidationForChart() {
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		dateformat = new SimpleDateFormat("M/d/yy");
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		int dayofweek = calNewYork.get(Calendar.DAY_OF_WEEK);
		if (dayofweek == 1) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -1);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		}
		if (dayofweek == 2) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -2);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else if (dayofweek == 3) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -3);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else if (dayofweek == 4) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -4);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else if (dayofweek == 5) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -5);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else if (dayofweek == 6) {
			calNewYork.add(Calendar.DAY_OF_MONTH, -6);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		} else {
			calNewYork.add(Calendar.DAY_OF_MONTH, 0);
			String str = dateformat.format(calNewYork.getTime());
			return str;
		}

	}

	public String getCurrentUSDate() {
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat = new SimpleDateFormat("M/dd/yy");
		dateformat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		String str = dateformat.format(calNewYork.getTime());
		return str;
	}

	public static boolean isNumber(String s) {
		for (int i = 0; i < s.length(); i++)
			if (Character.isDigit(s.charAt(i)) == false)
				return false;

		return true;
	}

	public void generateRandomTickers() {
		List<String[]> tickers = randomTickerCSV();
		randomTickers.put(1001, "AAPL");
		randomTickers.put(1002, "AMZN");
		randomTickers.put(1003, "TSLA");
		randomTickers.put(1004, "ASNA");
		for (String[] row : tickers) {
			int highlightLabelRandom = new Random().nextInt(tickers.size());
			String[] cell = tickers.get(highlightLabelRandom);
			for (String tickerName : cell) {
				randomTickers.put(highlightLabelRandom, tickerName);
				if (randomTickers.size() >= 100)
					break;
			}

			if (randomTickers.size() >= 100)
				break;
		}
	}

	public List<String[]> randomTickerCSV() {
		FileReader filereader;
		try {
			filereader = new FileReader(
					RESOURCE_PATH + File.separator + "finance" + File.separator + "randomtickers.csv");
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
