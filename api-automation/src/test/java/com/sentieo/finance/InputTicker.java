package com.sentieo.finance;

import static com.sentieo.constants.Constants.RESOURCE_PATH;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import static com.sentieo.constants.Constants.*;

public class InputTicker {

	public List<String[]> readTickerCSV() {
		FileReader filereader;
		try {
			if (!APP_URL.contains("app") && !APP_URL.contains("balyasny") && !APP_URL.contains("citadel")&&!APP_URL.contains("tiger") && !APP_URL.contains("sb.sentieo.com")) {
				filereader = new FileReader(
						RESOURCE_PATH + File.separator + "finance" + File.separator + "regression.csv");
			} else {
				filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + "sanity.csv");
			}
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
