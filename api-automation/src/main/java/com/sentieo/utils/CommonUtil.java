package com.sentieo.utils;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import static com.sentieo.utils.FileUtil.*;

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

	public void generateRandomTickers(Method testMethod) {
		List<String[]> tickers = randomTickerCSV(testMethod);
		randomTickers.clear();
		if (!testMethod.getName().equalsIgnoreCase("keyMultiplesNTM")&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM"))
				&& (!testMethod.getName().equalsIgnoreCase("keyMultiplesP_BookValue"))
				&&(!testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX"))
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
					if (randomTickers.size() >=5)
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
			}

			else if (testMethod.getName().equalsIgnoreCase("keyMultiplesTangibleBookValueNTM")) {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "Tangible.csv");
			} else if (testMethod.getName().equalsIgnoreCase("keyMultiplesEVGROSSPROFIT")) {
				filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + "gross.csv");
			}

			else if (testMethod.getName().equalsIgnoreCase("keyMultiplesEVEBITDA_CAPEX")) {
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
	
	public String getCurrentDate()
	{
		Calendar calNewYork = Calendar.getInstance();
		DateFormat dateformat;
		dateformat = new SimpleDateFormat("M/dd/yy");
		calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		calNewYork.add(Calendar.DAY_OF_MONTH, 0);
		String str = dateformat.format(calNewYork.getTime());
		return str;		
	}
	
	public Integer getYearFromTimeStamp(double timestamp)
	{	
		 Timestamp ts=new Timestamp((long) timestamp);  
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(new Date(ts.getTime()));
		 int year = cal.get(Calendar.YEAR);
		return year;
		
	}
	public String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() <5) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = "Automation"+salt.toString();
        return saltStr;

    }
	public  List<String> pickNRandomItems(List<String> lst, int n) {
	    List<String> copy = new LinkedList<String>(lst);
	    Collections.shuffle(copy);
	    return copy.subList(0, n);
	}
	
	public String getCurrentTimeStamp()
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();
        Timestamp tsFromInstant = Timestamp.from(instant);
        long stamp=tsFromInstant.getTime();
        String timeStamp=Long.toString(stamp);
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

}

