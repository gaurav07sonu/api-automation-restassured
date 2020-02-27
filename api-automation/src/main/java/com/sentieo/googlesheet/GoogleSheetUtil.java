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
import java.util.List;

public class GoogleSheetUtil extends GoogleSheetAuth {
	static String spreadsheetId = "1jdLRMXRvBqJQfxN5aR2fk7f2yb49ZjwfYNhtxTicBeA";

	public static ValueRange response;
	public static UpdateValuesResponse request;
	public static List<String> alreadyLogged = new ArrayList<String>();
	public static List<String> notLogged = new ArrayList<String>();
	
	
	public static String getDownTime(String previousFailedTime, String recoveredAtTime) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-M-yyyy hh:mm:ss a z");
	    LocalDateTime previousFailedAt = LocalDateTime.parse(previousFailedTime, formatter);
	    LocalDateTime recoveredAt = LocalDateTime.parse(recoveredAtTime, formatter);
	    long diff = ChronoUnit.SECONDS.between(previousFailedAt, recoveredAt);
	    return String.valueOf(diff);
	}

	public static List<String> updateDowntimeData(String sheetTab, boolean testStatus, String currentTime) throws IOException, GeneralSecurityException, InterruptedException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = getSheetsService();
		Thread.sleep(5000);
		ValueRange response = service.spreadsheets().values().get(spreadsheetId, sheetTab).execute();
		List<List<Object>> values = response.getValues();
		List<List<Object>> list = new ArrayList<List<Object>>();
		boolean isSheetEmpty = true;
		for (int c = 0; c < values.size(); c++) {
			// for (List<Object> row : values) {
			if (c != values.size() - 1) {
				list.add(values.get(c));
			} else {
				// read last row
				isSheetEmpty = false;
				System.out.println("Last row values: " + values.get(values.size() - 1));
				List<Object> lastRow = values.get(c);
				if (lastRow.size()==1) {
					if(testStatus) {
						//enter value for 'recoveredAt' and 'downtime'
						String downtime = getDownTime(lastRow.get(0).toString(), currentTime);
						// If test failed
						List<Object> temp = Arrays.asList(lastRow.get(0).toString(), currentTime, downtime);
						list.add(temp);
						updateSheetValues(list, sheetTab);
					}
					else {
						//do nothing
					}
					// if test case failed again
					// do nothing

					// if test case passed
					// enter value for 'recoveredAt' and 'downtime'

					// add downtime value
				} else {
					// if test case failed
					if(!testStatus) {
						list.add(values.get(c));
						List<Object> temp = Arrays.asList(currentTime, "", "");
						list.add(temp);
						updateSheetValues(list, sheetTab);
					}
					
					// if test case passed
					// do nothing
				}
			}

		}
		if (isSheetEmpty) {
			System.out.println("Sheet is empty");
			// If test failed for the first time
			List<Object> temp = Arrays.asList(currentTime, "", "");
			list.add(temp);
			updateSheetValues(list, sheetTab);
		}
		return null;
	}

	public static void updateSheetValues(List<List<Object>> data, String sheetTab)
			throws IOException, GeneralSecurityException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		Sheets service = getSheetsService();
		ValueRange body = new ValueRange().setValues(data);
		UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, sheetTab, body)
				.setValueInputOption("RAW").execute();
		System.out.printf("%d cells updated.", result.getUpdatedCells());
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

	public static void main(String[] args) throws IOException, GeneralSecurityException, InterruptedException {
		String DATE_FORMAT = "dd-M-yyyy hh:mm:ss a z";
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
	    ZoneId fromTimeZone = ZoneId.of("Asia/Kolkata");
	     
	    LocalDateTime currentTime = LocalDateTime.now(); 
	    ZonedDateTime currentISTime = currentTime.atZone(fromTimeZone);   
	    System.out.println(formatter.format(currentISTime));

		updateDowntimeData("user", false, formatter.format(currentISTime));
	}
}
