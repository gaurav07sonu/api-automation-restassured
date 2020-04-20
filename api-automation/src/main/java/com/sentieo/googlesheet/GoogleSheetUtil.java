package com.sentieo.googlesheet;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.model.*;

import com.google.api.services.sheets.v4.Sheets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class GoogleSheetUtil extends GoogleSheetAuth {
	//static String spreadsheetId = "1jdLRMXRvBqJQfxN5aR2fk7f2yb49ZjwfYNhtxTicBeA";
	public static String spreadsheetId = "";
	public static ValueRange response;
	public static UpdateValuesResponse request;
	public static List<String> alreadyLogged = new ArrayList<String>();
	public static List<String> notLogged = new ArrayList<String>();
	public static Sheets service = null;
	
	
	public static String getDownTime(String previousFailedTime, String recoveredAtTime) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss a z");
	    LocalDateTime previousFailedAt = LocalDateTime.parse(previousFailedTime, formatter);
	    LocalDateTime recoveredAt = LocalDateTime.parse(recoveredAtTime, formatter);
	    long diff = ChronoUnit.SECONDS.between(previousFailedAt, recoveredAt);
	    return String.valueOf(diff);
	}

	public static List<String> updateDowntimeData(String sheetTab, boolean testStatus, String currentTime, HashSet<String> failedAPIs) throws IOException, GeneralSecurityException, InterruptedException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = getSheetsService();
		Thread.sleep(5000);
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, sheetTab).execute();
		List<List<Object>> allRowsData = response.getValues();
		List<List<Object>> list = new ArrayList<List<Object>>();
		for (int individualRowIndex = 0; individualRowIndex < allRowsData.size(); individualRowIndex++) {
			//In case sheet is Blank and this is first instance
			//If test passed then do nothing Else add first time failure data
			if(individualRowIndex == allRowsData.size() - 1 && allRowsData.get(individualRowIndex).toString().contains("failed")) {
				list.add(allRowsData.get(individualRowIndex));
				if(testStatus) {
					//Do nothing
				}
				else {
					List<Object> temp = Arrays.asList(currentTime, "", "", failedAPIs.toString());
					list.add(temp);
					updateSheetValues(list, sheetTab);
				}
				break;
			}
			// In case sheet already has few failed tests
			// Add all existing failed tests data already in sheet till {LastRow-1}
			else if (individualRowIndex != allRowsData.size() - 1) {
				list.add(allRowsData.get(individualRowIndex));
			} else {
				// Now read last row
				System.out.println("Last row values: " + allRowsData.get(allRowsData.size() - 1));
				List<Object> lastRow = allRowsData.get(individualRowIndex);
				if (!StringUtils.isEmpty(lastRow.get(0).toString()) && StringUtils.isEmpty(lastRow.get(1).toString())) {  // Module not recovered previously, so edit same entry
					if(testStatus) {  // If test passed
						//enter value for 'recoveredAt' and 'downtime'
						String downtime = getDownTime(lastRow.get(0).toString(), currentTime);
						List<Object> temp = Arrays.asList(lastRow.get(0).toString(), currentTime, downtime, "");
						list.add(temp);
						updateSheetValues(list, sheetTab);
					}
					else { // If test failed again
						List<Object> temp = Arrays.asList(lastRow.get(0).toString(), "", "", failedAPIs.toString());
						list.add(temp);
						updateSheetValues(list, sheetTab);
						//do nothing
					}
					
				} else {
					// if test case failed
					if(!testStatus) {
						list.add(allRowsData.get(individualRowIndex));
						List<Object> temp = Arrays.asList(currentTime, "", "", failedAPIs.toString());
						list.add(temp);
						updateSheetValues(list, sheetTab);
					}
				}
			}

		}
		return null;
	}
	
	
	public static List<String> updateDowntimeHistoryData(String sheetTab, String currentTime, HashSet<String> failedAPIs) throws IOException, GeneralSecurityException, InterruptedException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = getSheetsService();
		Thread.sleep(5000);
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, sheetTab).execute();
		List<List<Object>> allRowsData = response.getValues();
		List<List<Object>> list = new ArrayList<List<Object>>();
		for (int individualRowIndex = 0; individualRowIndex < allRowsData.size(); individualRowIndex++) {
			list.add(allRowsData.get(individualRowIndex));
			}
		List<Object> temp = Arrays.asList(currentTime, failedAPIs.toString());
		list.add(temp);
		updateSheetValues(list, sheetTab);
		return null;
	}

	public static void updateSheetValues(List<List<Object>> data, String sheetTab)
			throws IOException, GeneralSecurityException {
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Sheets service = getSheetsService();
			ValueRange body = new ValueRange().setValues(data);
			UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, sheetTab, body)
					.setValueInputOption("RAW").execute();
			System.out.printf("%d cells updated.", result.getUpdatedCells());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static ValueRange getResponse(String SheetName, String RowStart, String RowEnd) throws IOException {
		Sheets service = getSheetsService();

		String range = SheetName + "!" + RowStart + ":" + RowEnd;
		response = service.spreadsheets().values().get(spreadsheetId, range).execute();

		return response;

	}

	public static BatchUpdateSpreadsheetResponse batchUpdate(String find, String replacement) throws IOException {
		Sheets service = getSheetsService();
		// [START sheets_batch_update]
		List<Request> requests = new ArrayList<>();
		// Change the spreadsheet's title.
		requests.add(new Request().setUpdateSpreadsheetProperties(new UpdateSpreadsheetPropertiesRequest()
				.setProperties(new SpreadsheetProperties().setTitle("TargetProcess excel integration"))
				.setFields("title")));
		// Find and replace text.
		requests.add(new Request()
				.setFindReplace(new FindReplaceRequest().setFind(find).setReplacement(replacement).setAllSheets(true)));
		BatchUpdateSpreadsheetRequest body = new BatchUpdateSpreadsheetRequest().setRequests(requests);
		BatchUpdateSpreadsheetResponse response = service.spreadsheets().batchUpdate(spreadsheetId, body).execute();
		FindReplaceResponse findReplaceResponse = response.getReplies().get(1).getFindReplace();
		System.out.printf("%d replacements made.", findReplaceResponse.getOccurrencesChanged());
		// [END sheets_batch_update]
		return response;
	}
	
	
	public static void setValue(String RowStart, String RowEnd, int value) throws IOException{
		//Sheets service = getSheetsService();
		String range = RowStart+":"+RowEnd;

		List<List<Object>> arrData = getData(value);

		ValueRange oRange = new ValueRange();
		oRange.setRange(range); // I NEED THE NUMBER OF THE LAST ROW
		oRange.setValues(arrData);

		List<ValueRange> oList = new ArrayList<>();
		oList.add(oRange);

		BatchUpdateValuesRequest oRequest = new BatchUpdateValuesRequest();
		oRequest.setValueInputOption("RAW");
		oRequest.setData(oList);

		BatchUpdateValuesResponse oResp1 = service.spreadsheets().values().batchUpdate(spreadsheetId, oRequest).execute();

	}
	
    public static List<String> getData() throws IOException, GeneralSecurityException {
    	if(service==null) {
    		 service = getSheetsService();
    	}
    	List<String> list = new ArrayList<String>();
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String[] range = {"Sheet1!A2:B"};
        
        for (String tab : range) {
             Sheets service = getSheetsService();
             ValueRange response = service.spreadsheets().values()
                     .get(spreadsheetId, tab)
                     .execute();
             List<List<Object>> values = response.getValues();
             if (values == null || values.isEmpty()) {
                 System.out.println("No data found.");
             } 
             else {
                 for (List row : values) {
                	 list.add(row.toString());
                 	System.out.println(row);
                 }
             }
		}
        return list;
    }
    
	public static List<List<Object>> getData (int value)  {

		List<Object> data1 = new ArrayList<Object>();
		data1.add (value);

		List<List<Object>> data = new ArrayList<List<Object>>();
		data.add (data1);

		return data;
	}

	public static void main(String[] args) throws IOException, GeneralSecurityException, InterruptedException {
		String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a z";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
	    ZoneId fromTimeZone = ZoneId.of("Asia/Kolkata");
	     
	    LocalDateTime currentTime = LocalDateTime.now(); 
	    ZonedDateTime currentISTime = currentTime.atZone(fromTimeZone);   
	    System.out.println(formatter.format(currentISTime));

		//updateDowntimeData("user", false, formatter.format(currentISTime));
	}
}
