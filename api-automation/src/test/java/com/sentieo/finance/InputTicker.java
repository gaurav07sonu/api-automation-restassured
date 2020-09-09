package com.sentieo.finance;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import static com.sentieo.utils.FileUtil.*;

public class InputTicker {

	public List<String[]> readTickerCSV() {
		FileReader filereader;
		try {
			filereader = new FileReader(RESOURCE_PATH + File.separator + "finance" + File.separator + "sanity.csv");
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
