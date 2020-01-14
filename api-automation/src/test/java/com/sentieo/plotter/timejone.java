package com.sentieo.plotter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.velocity.runtime.directive.Parse;

public class timejone {

	public static void main(String[] args) {
		Calendar calNewYork = Calendar.getInstance();
//	     calNewYork.setTimeZone(TimeZone.getTimeZone("America/New_York"));
//	     String time=calNewYork.get(Calendar.HOUR_OF_DAY) + ":"+ calNewYork.get(Calendar.MINUTE); 
//
//	     String[] hourMin = time.split(":");
//	     int hour = Integer.parseInt(hourMin[0]);
//	     int mins = Integer.parseInt(hourMin[1]);
//	    // int hoursInMins = hour * 60;
//	     System.out.println( time);
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTime(new Date());
	     SimpleDateFormat sdf = new SimpleDateFormat("kk:mm aa");
	     sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
	     String time=sdf.format(calendar.getTime());
	     System.out.println(time);
	     //Will print in UTC
	    // System.out.println(sdf.format(calendar.getTime()));    
	}

}
