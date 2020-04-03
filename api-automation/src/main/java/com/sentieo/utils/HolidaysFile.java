package com.sentieo.utils;

import java.util.Arrays;
import java.util.List;

public class HolidaysFile {

	// US Markets (nasdaq and nyse)
	public static List<String> nasdaqHolidays = Arrays.asList("01/01/20", "01/20/20", "02/17/20", "04/10/20",
			"05/25/20", "07/03/20", "09/07/20", "11/26/20", "12/25/20", "01/01/21", "01/18/21", "02/15/21", "04/02/21",
			"05/31/21", "07/05/21", "09/06/21", "11/25/21", "12/24/21");

	// frankfurt holidays (:fp)
	public static List<String> frankFurtHolidays = Arrays.asList("01/01/20", "04/10/20", "04/13/20", "05/01/20",
			"06/01/20", "09/07/20", "12/24/20", "12/25/20", "01/01/21", "12/31/20", "04/12/21", "04/05/21", "05/24/21",
			"04/15/22", "04/18/22", "06/06/22", "10/03/22", "12/26/21", "12/24/21", "12/25/21", "01/01/22", "12/31/21",
			"12/26/22", "12/24/22", "12/25/22", "01/01/23", "12/31/22");

	// australian holidays (:au)
	public static List<String> australianHolidays = Arrays.asList("01/01/20", "01/27/20", "04/10/20", "04/13/20",
			"04/25/20", "06/08/20", "12/25/20", "12/28/20", "01/01/21", "12/31/20");

	// europe holidays
	public static List<String> europeanHolidays = Arrays.asList("01/01/20", "04/10/20", "04/13/20", "05/01/20",
			"12/25/20", "01/01/21");

	// canada holidays (:cn)
	public static List<String> canadaHolidays = Arrays.asList("01/01/20", "02/17/20", "04/10/20", "05/18/20",
			"12/25/20", "01/01/21", "07/01/20", "08/03/20", "09/07/20", "10/12/20", "12/28/20");

	// london holidays
	public static List<String> londonHolidays = Arrays.asList("01/01/20", "04/10/20", "04/13/20", "05/08/20",
			"12/25/20", "01/01/21", "05/25/20", "08/31/20", "12/24/20", "12/28/20");

	public static List<String> combinedHolidays = Arrays.asList("01/01/20", "04/10/20", "04/13/20", "05/08/20",
			"12/25/20", "01/01/21", "05/25/20", "08/31/20", "12/24/20", "12/28/20", "01/01/20", "04/10/20", "04/13/20",
			"05/01/20", "12/25/20", "01/01/21", "01/01/20", "04/10/20", "04/13/20", "05/01/20", "06/01/20", "09/07/20",
			"12/24/20", "12/25/20", "01/01/21", "12/31/20", "04/12/21", "04/05/21", "05/24/21", "04/15/22", "04/18/22",
			"06/06/22", "10/03/22", "12/26/21", "12/24/21", "12/25/21", "01/01/22", "12/31/21", "12/26/22", "12/24/22",
			"12/25/22", "01/01/23", "12/31/22");

	public List<String> returnHoldays(String ticker) {

		String Switch = null;
		String[] split = ticker.split(":");
		if (split.length == 2) {
			Switch = split[1];
		} else {
			Switch = "US";
		}
		List<String> newList = null;
		switch (Switch) {
		case "au":
			newList = HolidaysFile.australianHolidays;
			break;
		case "gr":
			newList = HolidaysFile.frankFurtHolidays;
			break;
		case "cn":
			newList = HolidaysFile.canadaHolidays;
			break;
		case "ln":
			newList = HolidaysFile.londonHolidays;
			break;
		case "fp":
			newList = HolidaysFile.europeanHolidays;
			break;
		case "US":
			newList = HolidaysFile.nasdaqHolidays;
			break;
		default:
			newList = HolidaysFile.combinedHolidays;
			break;
		}
		return newList;
	}
}
