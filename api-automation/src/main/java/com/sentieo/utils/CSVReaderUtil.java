package com.sentieo.utils;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import static com.sentieo.utils.FileUtil.*;

public class CSVReaderUtil {
	
	public static void main(String[] args) { 
		String[][] arr = readAllDataAtOnce("doctype.csv");
		for (String[] st : arr) {
			System.out.println(st.length);
		}
	}
	
 
	public static String[][] readAllDataAtOnce(String file) 
	{ 
		List<String[]> allData = new ArrayList<String[]>();
		try { 
			FileReader filereader = new FileReader(RESOURCE_PATH + File.separator + file); 

			// create csvReader object and skip first Line 
			CSVReader csvReader = new CSVReaderBuilder(filereader) 
									.withSkipLines(1) 
									.build(); 
			allData = csvReader.readAll(); 

			for (String[] row : allData) { 
				for (String cell : row) { 
					System.out.print(cell + "\t"); 
				} 
				System.out.println(); 
			} 
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		}
		return castType(allData); 
	}
	
	
	public static String[][] castType(List<String[]> data) {
		String[][] array = new String[data.size()][];
		for (int i = 0; i < array.length; i++)
			array[i] = data.get(i);
		return array;
	}

	

}


