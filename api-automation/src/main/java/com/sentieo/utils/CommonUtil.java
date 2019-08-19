package com.sentieo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class CommonUtil {
	
	public String convertTimestampIntoDate(int time) {

		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
				.withZone(ZoneId.systemDefault());
		long epoch = time;
		Instant instant = Instant.ofEpochSecond(epoch);
		String output = formatter.format(instant);
		return output;
	}
	public String dateValidationForChart()
	{
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
        if (dayofweek == 2 ) {
            calNewYork.add(Calendar.DAY_OF_MONTH, -2);
            String str = dateformat.format(calNewYork.getTime());
            return str;
        } 
        else if (dayofweek == 3) {
            calNewYork.add(Calendar.DAY_OF_MONTH, -3);
            String str = dateformat.format(calNewYork.getTime());
            return str;
        }
        else if (dayofweek == 4) {
            calNewYork.add(Calendar.DAY_OF_MONTH, -4);
            String str = dateformat.format(calNewYork.getTime());
            return str;
        }
        else if (dayofweek == 5) {
            calNewYork.add(Calendar.DAY_OF_MONTH, -5);
            String str = dateformat.format(calNewYork.getTime());
            return str;
        }
        else if (dayofweek == 6) {
            calNewYork.add(Calendar.DAY_OF_MONTH, -6);
            String str = dateformat.format(calNewYork.getTime());
            return str;
        }
        else
        {
        	 calNewYork.add(Calendar.DAY_OF_MONTH,0);
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
	

}
