//package com.sentieo.tableX;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Objects;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//import com.opencsv.CSVWriter;
//import com.relevantcodes.extentreports.LogStatus;
//import com.sentieo.report.ExtentTestManager;
//import com.sentieo.rest.base.APIDriver;
//import com.sentieo.utils.CommonUtil;
//import com.sentieo.utils.CoreCommonException;
//public class TestTableExplorer extends  APIDriver {
//    protected String[] tickerList;
//    protected String[] documenntType;
//    protected List<String> updateddocumenntType;
//    protected String source = "";
//    protected String getDocId = "";
//    protected String getTableId = "";
//    protected String completeSheetRun = "";
//    CommonUtil commUtil = new CommonUtil();
//    List<String[]> data;
//    @BeforeTest
//    public void addValuesToDataProvider() {
//        if (!printdata().equals("false")) {
//            completeSheetRun = printdata();
//        }
//        if (!getTickerlName().equals("ticker")) {
//            tickerList = getTickerlName().split(",");
//            Arrays.parallelSetAll(tickerList, (i) -> tickerList[i].trim());
//        } else {
//            tickerList = getDay("tableX.txt");
//            if (tickerList != null) {
//                Arrays.parallelSetAll(tickerList, (i) -> tickerList[i].trim());
//            }
//        }
//        if (!getModelName().equals("modelname")) {
//            updateddocumenntType = new ArrayList<String>();
//            documenntType = getModelName().split(",");
//            Arrays.parallelSetAll(documenntType, (i) -> documenntType[i].trim());
//            for (int i = 0; i < documenntType.length; i++) {
//                String gg = changePeriod(documenntType[i]);
//                updateddocumenntType.add(gg.toLowerCase());
//            }
//        }
//    }
//    @Test(groups = "sanity", description = "fetch company header data")
//    public void fetchLatestEarningsDoc() throws Exception {
//        if (updateddocumenntType != null && updateddocumenntType.size() > 0 && tickerList != null
//                && tickerList.length > 0) {
//            data = new ArrayList<String[]>();
//            data.add(new String[] { "Ticker", "Source", "DocId", "TableID", "Error" });
//            for (int j = 0; j < updateddocumenntType.size(); j++) {
//                source = updateddocumenntType.get(j).toLowerCase();
//                for (int k = 0; k < tickerList.length; k++) {
//                    String ticker = tickerList[k].toLowerCase();
//                    HashMap<String, String> tickerData = commUtil
//                            .makeDict(new String[] { "ticker", ticker, "source", source });
//                    JSONObject json = commUtil.sendParameters(tickerData, FETCH_TABLEX_NAME, "post");
//                    JSONObject verifyTableData = json.getJSONObject("result");
//                    if (verifyTableData.has("tablex_data")) {
//                        String getValue = verifyTableData.optString("tablex_data");
//                        if (json != null && !getValue.equals("false")) {
//                            Thread.sleep(1000);
//                            fetchCompanyHeaderData(json, ticker, source);
//                        }
//                    }
//                }
//            }
//            writeDataForCustomSeperatorCSV();
//            verify.verifyAll();
//        }
//    }
//    public void fetchCompanyHeaderData(JSONObject json, String ticker, String source)
//            throws CoreCommonException, InterruptedException {
//        try {
//            JSONObject completeAPIResult = json.getJSONObject("result");
//            JSONArray getFinancialTables = completeAPIResult.getJSONArray("tables_info");
//            for (int i = 1; i < getFinancialTables.length(); i++) {
//                JSONArray childJsonArray = getFinancialTables.optJSONArray(i);
//                if (childJsonArray != null && childJsonArray.length() > 0) {
//                    getDocId = childJsonArray.optString(1);
//                    getTableId = childJsonArray.optString(2);
//                    if (!getDocId.isEmpty() && !getTableId.isEmpty()) {
//                        Thread.sleep(1000);
//                        fetchTableXPreview(getDocId, getTableId, ticker, source);
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            data.add(new String[] { ticker, source, getDocId, getTableId, e.getMessage() });
//            ExtentTestManager.getTest().log(LogStatus.FAIL, "Exception Error :- " + e.getMessage() + "Ticker : "
//                    + ticker + "Source :" + source + "DocId :-" + getDocId + "TableID :-" + getTableId);
//            verify.verificationFailures.add(e);
//        }
//    }
//    public void fetchTableXPreview(String docId, String tableID, String ticker, String docType)
//            throws CoreCommonException, InterruptedException {
//        HashMap<String, String> tickerData = null;
//        JSONObject completeAPIResult;
//        tickerData = commUtil.makeDict(new String[] { "ticker", ticker, "docId", docId, "tableId", tableID, "tx_access",
//                "true", "preview_opt", "true", "tab_click", "true", "latest_doc_flag", "true", "fetch_html", "true",
//                "fetch_all", "true", "fetch_all_data", "true", "source", docType, "module", "Tables Chain", "url_loc",
//                "company", "docid", docId, "tableid", tableID });
//        Thread.sleep(1000);
//        if (docType.equalsIgnoreCase("8-k")) {
//            tickerData.put("flag_8k", "true");
//        }
//        JSONObject json = commUtil.sendParameters(tickerData, FETCH_TABLEX_PREVIEW, "post");
//        if (json != null) {
//            completeAPIResult = json.getJSONObject("result");
//            try {
//                @SuppressWarnings("unused")
//                String verifyACtion = completeAPIResult.getString("action");
//            } catch (Exception e) {
//                String getTableName = completeAPIResult.getString("table_name").toString();
//                String getName = getQueryList(source);
//                if (getTableName.contains(getName)) {
//                    JSONObject getHeaderRowIndex = completeAPIResult.getJSONObject("header_row_indexes");
//                    JSONObject getTableFromPreviewData = completeAPIResult.getJSONObject("preview_data")
//                            .getJSONObject("table_values_data");
//                    JSONArray getHtmlData = completeAPIResult.getJSONArray("html_data");
//                    JSONArray getQuarterSize = completeAPIResult.getJSONArray("quarters_list");
//                    boolean getStatus = verifySize(getQuarterSize, getHtmlData, getHeaderRowIndex,
//                            getTableFromPreviewData);
//                    if (!getStatus) {
//                        data.add(new String[] { ticker, source, getDocId, getTableId, "Size not Matched" });
//                        verify.verificationFailures.add(e);
//                        ExtentTestManager.getTest().log(LogStatus.FAIL, "Size not Matched" + "Ticker : " + ticker
//                                + "Source :" + source + "DocId :-" + getDocId + "TableID :-" + getTableId);
//                    }
//                }
//            }
//        } else {
//            data.add(new String[] { ticker, source, getDocId, getTableId, "No JSON DATA" });
//            ExtentTestManager.getTest().log(LogStatus.FAIL, "No JSON DATA : " + "Ticker : " + ticker + "Source :"
//                    + source + "DocId :-" + getDocId + "TableID :-" + getTableId);
//        }
//    }
//    public boolean verifySize(JSONArray A, JSONArray B, JSONObject C, JSONObject D) {
//        if (A.length() != B.length() && A.length() != C.length() && B.length() != C.length() && A.length() != D.length()
//                && D.length() != B.length()) {
//            return false;
//        }
//        return true;
//    }
//    public String getQueryList(String animal) {
//        String newList = null;
//        switch (animal) {
//        case "8-k":
//            newList = "8K";
//            break;
//        case "10-q":
//            newList = "10Q";
//            break;
//        case "10-k":
//            newList = "10K";
//            break;
//        }
//        return newList;
//    }
//    public String changePeriod(String animal) {
//        String newList = null;
//        switch (animal) {
//        case "8k":
//            newList = "8-K";
//            break;
//        case "8-k":
//            newList = "8-K";
//            break;
//        case "10-q":
//            newList = "10-q";
//            break;
//        case "10q":
//            newList = "10-Q";
//            break;
//        case "10k":
//            newList = "10-K";
//            break;
//        case "10-k":
//            newList = "10-K";
//            break;
//        }
//        return newList;
//    }
//    public String[] getDay(String fileName) {
//        String[] abd = null;
//        List<String> abcd = common.readFile(fileName);
//        abcd.removeIf(Objects::isNull);
//        if (completeSheetRun.contains("true")) {
//            abd = common.convertListIntoArray(abcd);
//        } else {
//            Calendar calendar = Calendar.getInstance();
//            int day = calendar.get(Calendar.DAY_OF_YEAR);
//            abd = revampValues(day, abcd);
//            if (abd == null) {
//                if (day != 0) {
//                    int newDay = (day % 9 == 0) ? 9 : (day % 9);
//                    abd = revampValues(newDay, abcd);
//                }
//            } else {
//                return abd;
//            }
//        }
//        return abd;
//    }
//    public void writeDataForCustomSeperatorCSV() {
//        File file = new File(RESOURCE_PATH + File.separator + "errorData.csv");
//        if (data.size() > 1) {
//            try {
//                // create FileWriter object with file as parameter
//                FileWriter outputfile = new FileWriter(file, false);
//                // create CSVWriter object filewriter object as parameter
//                CSVWriter writer = new CSVWriter(outputfile, ',', CSVWriter.NO_QUOTE_CHARACTER,
//                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
//                // create a List which contains String array
//                writer.writeAll(data);
//                // closing writer connection
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public String[] revampValues(int day, List<String> abcd) {
//        String[] returnArray;
//        int getMOdulus = day % 50;
//        int min_index = getMOdulus * 100;
//        int max_index = (getMOdulus + 1) * 100;
//        if (abcd.size() > min_index && abcd.size() > max_index) {
//            abcd = abcd.subList(min_index, max_index);
//            returnArray = common.convertListIntoArray(abcd);
//        } else {
//            returnArray = null;
//        }
//        return returnArray;
//    }
//}
