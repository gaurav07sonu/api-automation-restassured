package com.sentieo.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.codec.binary.Base64;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.sentieo.assertion.APIAssertions;



public class CommonUtil {
	APIAssertions verify = new APIAssertions();
	public static final String RESOURCE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "resources";
	public static HashMap<Integer, String> randomTickers = new HashMap<Integer, String>();
	public static HashMap<Integer, String> revenueTickers = new HashMap<Integer, String>();

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

	public void generateRandomTickers(Method testMethod) {
		List<String[]> tickers = randomTickerCSV(testMethod);
		randomTickers.clear();
		if (!testMethod.getName().equalsIgnoreCase("keyMultiplesNTM")
				&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM"))
				&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesP_BookValue"))
				&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX"))
				&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesEVGROSSPROFIT"))) {
			randomTickers.put(1001, "AAPL");
			randomTickers.put(1002, "AMZN");
			randomTickers.put(1003, "TSLA");
			randomTickers.put(1004, "ASNA");
		}
		for (String[] row : tickers) {
			int highlightLabelRandom = new Random().nextInt(tickers.size());
			String[] cell = tickers.get(highlightLabelRandom);
			for (String tickerName : cell) {
				randomTickers.put(highlightLabelRandom, tickerName);
				if (testMethod.getName().equalsIgnoreCase("keyMultiplesNTM")
						|| testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM")
						|| testMethod.getName().equalsIgnoreCase("keyMultiplesP_BookValue")
						|| testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX")
						|| testMethod.getName().equalsIgnoreCase("keyMultiplesEVGROSSPROFIT")) {
					if (randomTickers.size() >= 5)
						break;
				} else {
					if (randomTickers.size() >= 100)
						break;
				}
			}
			if (testMethod.getName().equalsIgnoreCase("keyMultiplesNTM")
					|| testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM")
					|| testMethod.getName().equalsIgnoreCase("keyMultiplesP_BookValue")
					|| testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX")
					|| testMethod.getName().equalsIgnoreCase("keyMultiplesEVGROSSPROFIT")) {
				if (randomTickers.size() >= 5)
					break;
			} else {
				if (randomTickers.size() >= 100)
					break;
			}
		}
	}

	public List<String[]> randomTickerCSV(Method testMethod) {
		FileReader filereader;
		try {
			if (testMethod.getName().equalsIgnoreCase("keyMultiplesNTM")
					|| (testMethod.getName().equalsIgnoreCase("keyMultiplesP_CashFlow"))) {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "dailyseries.csv");
			} else if (testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM")) {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "Tangible.csv");
			} else if (testMethod.getName().equalsIgnoreCase("keyMultiplesEVGROSSPROFIT")) {
				filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + "gross.csv");
			} else if (testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX")) {
				filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + "capex.csv");
			} else if (testMethod.getName().equalsIgnoreCase("keyMultiplesP_BookValue")) {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "BookValue.csv");
			} else {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "randomtickers.csv");
			}
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String[]> readTickerCSV(String csv) {
		FileReader filereader;
		try {
			filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + csv);
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Double getpostivePercentageChange(Double firstValue, Double secondValue) {
		Double difference = firstValue - secondValue;
		Double average = (firstValue + secondValue) / 2;
		Double divideDIfference = difference / average;
		Double perChange = divideDIfference * 100;
		return perChange;
	}

	public Double getnegativePercentageChange(Double firstValue, Double secondValue) {
		Double difference = secondValue - firstValue;
		Double average = (firstValue + secondValue) / 2;
		Double divideDIfference = difference / average;
		Double perChange = divideDIfference * 100;
		return perChange;
	}

	public String getCurrentDate() {
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		dateformat = new SimpleDateFormat("M/dd/yy");
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		calNewYork.add(Calendar.DAY_OF_MONTH, 0);
		String str = dateformat.format(calNewYork.getTime());
		return str;
	}

	public Integer getYearFromTimeStamp(double timestamp) {
		Timestamp ts = new Timestamp((long) timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(ts.getTime()));
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	public String getRandomString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 5) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = "Automation" + salt.toString();
		return saltStr;
	}

	public List<String> pickNRandomItems(List<String> lst, int n) {
		List<String> copy = new LinkedList<String>(lst);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}

	public String getCurrentTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Instant instant = timestamp.toInstant();
		Timestamp tsFromInstant = Timestamp.from(instant);
		long stamp = tsFromInstant.getTime();
		String timeStamp = Long.toString(stamp);
		return timeStamp;
	}

	public boolean validateTimeStampIsTodaysDate(double timestamp) {
		int digit = (int) (timestamp / 1000);
		String commentDate = convertTimestampIntoDate(digit);
		DateFormat dateformat = new SimpleDateFormat("M/d/yy");
		String currentDate = dateformat.format(new Date().getTime());
		if (commentDate.contains(currentDate))
			return true;
		return false;
	}
	
	public static String encodeFileToBase64Binary(File file){
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            fileInputStreamReader.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedfile;
    }

	public long daysDifferenceBetweenTwoTimestamps(long time1, long time2) {
		final long MILLIS_PER_DAY = 1000 * 60 * 60 * 24;
		// Set both times to 0:00:00
		time1 -= time1 % MILLIS_PER_DAY;
		time2 -= time2 % MILLIS_PER_DAY;
		return TimeUnit.DAYS.convert(time2 - time1, TimeUnit.MILLISECONDS);
	}

	public HashMap<Integer, String> readJSONFromFile() {
		JSONParser parser = new JSONParser();
		try {
			int i = 0;
			Object obj = parser.parse(new FileReader(
					(RESOURCE_PATH + File.separator + "finance" + File.separator + "revenueticker.json")));
			// A JSON object. Key value pairs are unordered. JSONObject supports
			// java.util.Map interface.
			JSONObject jsonObject = (JSONObject) obj;
			// A JSON array. JSONObject supports java.util.List interface.
			// System.out.println(array);
			JSONArray companyList = (JSONArray) jsonObject.get("tickers");
			// An iterator over a collection. Iterator takes the place of Enumeration in the
			// Java Collections Framework.
			// Iterators differ from enumerations in two ways:
			// 1. Iterators allow the caller to remove elements from the underlying
			// collection during the iteration with well-defined semantics.
			// 2. Method names have been improved.
			@SuppressWarnings("unchecked")
			Iterator<Object> iterator = companyList.iterator();
			while (iterator.hasNext()) {
				revenueTickers.put(i, iterator.next().toString());
				i++;
				if (revenueTickers.size() > 5)
					return revenueTickers;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// get complete data into list from file
	public List<String> readFile(String fileName) {
		BufferedReader reader;
		List<String> returnList = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader(RESOURCE_PATH + File.separator + fileName));
			String line = reader.readLine();
			while (line != null) {
				// read next line
				line = reader.readLine();
				returnList.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnList;
	}

	public List<String> getDay(String fileName) {
		List<String> abd = null;
		List<String> abcd = readFile(fileName);
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_YEAR);
		abd = revampValues(day, abcd);
		if (abd == null) {
			if (day != 0) {
				int newDay = (day % 9 == 0) ? 9 : (day % 9);
				abd = revampValues(newDay, abcd);
			}
		} else {
			return abd;
		}

		return abd;
	}

	public List<String> revampValues(int day, List<String> tickers) {
		int getMOdulus = day % 50;
		int min_index = getMOdulus * 50;
		int max_index = (getMOdulus + 1) * 50;
		if (tickers.size() > min_index && tickers.size() > max_index) {
			tickers = tickers.subList(min_index, max_index);
			// returnArray = convertListIntoArray(abcd);
			return tickers;
		} else
			tickers = null;

		return tickers;
	}
	
	public void verifykeyAvailable(JSONObject result, String key, String type) {
		if (result.has(key)) {
			verify.verifyEquals(result.get(key).getClass().getName(), type,
					"Verify data type for key: "+key );
		} else
			verify.assertTrue(false, key + " :key not found");
	}

}
