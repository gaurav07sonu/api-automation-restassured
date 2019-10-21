package com.sentieo.dataprovider;

import java.io.File;
import java.lang.reflect.Method;
import static com.sentieo.constants.Constants.*;

import org.testng.annotations.DataProvider;

import com.sentieo.utils.CSVReaderUtil;

public class DataProviderClass {

	@DataProvider(name = "fetch_search_SearchOnly")
	public String[][] Data(Method testmethodname) {
		String[][] groupArray = null;
		if (testmethodname.getName().equalsIgnoreCase("fetchsearchSearchOnly")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearchSearchOnly.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchtickerOnly")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearchtickerOnly.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_impact_score")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_impact_score.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_custom_doc_diff")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_custom_doc_diff.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearch")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearch.csv");

		} else if (testmethodname.getName().contains("fetchsearch2")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearch2.csv");

		} else if (testmethodname.getName().contains("bulk_download")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "bulk_download.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_docs_meta_data")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_docs_meta_data.csv");

		}
		return groupArray;

	}

	@DataProvider(name = "SearchProvider")
	public Object[][] getDataFromDataprovider(Method testmethodname) {
		Object[][] groupArray = null;
		if (testmethodname.getName().equalsIgnoreCase("fetchsearchRegions")) {
			groupArray = new Object[][] { { "{\"United States\":{\"United States\":[\"us\"]}}" },
					{ "{\"Canada\":{\"Canada\":[\"ca\"]}}" },
					{ "{\"Latin America & Caribbean\":{\"South America\":[\"pe\",\"br\",\"co\",\"cl\",\"ar\",\"ve\"],\"Mexico\":[\"mx\"],\"Caribbean\":[\"vg\",\"ky\",\"bm\",\"jm\"]}}",
							"false" },
					{ "{\"Europe\":{\"Western Europe\":[\"gb\",\"de\",\"it\",\"fr\",\"ch\",\"be\",\"ie\",\"es\",\"lu\",\"nl\",\"fi\",\"pt\",\"gr\",\"dk\",\"mt\",\"is\",\"no\",\"gi\",\"se\"],\"Central & Eastern Europe\":[\"ru\",\"pl\",\"hu\",\"cz\",\"lv\",\"bg\",\"at\",\"ee\",\"cy\",\"ro\",\"si\",\"lt\",\"sk\",\"ua\"],\"Southern Europe\":[\"hr\",\"mk\",\"rs\",\"me\"]}}",
							"true" },
					{ "{\"Middle East & Africa\":{\"Middle East\":[\"qa\",\"ae\",\"om\",\"bh\",\"il\",\"sa\",\"jo\",\"kw\",\"lb\",\"ps\",\"sy\",\"tr\"],\"Africa\":[\"za\",\"eg\",\"ng\",\"mu\",\"ke\",\"ma\",\"gh\",\"mw\",\"na\",\"tn\",\"ug\",\"tz\"]}}",
							"false" },
					{ "{\"Asia Pacific\":{\"East Asia\":[\"jp\",\"cn\",\"tw\",\"kr\",\"hk\"],\"South East Asia\":[\"id\",\"ph\",\"my\",\"th\",\"vn\",\"sg\"],\"South Asia\":[\"in\",\"bd\",\"lk\",\"pk\"],\"Central Asia\":[\"kz\"]}}",
							"false" },
					{ "{\"Australia & Oceania\":{\"Australia\":[\"au\"],\"Oceania\":[\"nz\",\"pg\"]}}", "true" },
					{ "{\"Smart Groupings\":{\"G-10\":[\"gb\",\"jp\",\"de\",\"it\",\"fr\",\"ch\",\"be\",\"ca\",\"nl\",\"us\",\"se\"],\"Emerging Markets\":[\"pe\",\"qa\",\"cn\",\"id\",\"br\",\"ph\",\"my\",\"in\",\"za\",\"ru\",\"th\",\"pl\",\"co\",\"hu\",\"mx\",\"ae\",\"tw\",\"cz\",\"eg\",\"cl\"],\"Frontier Markets\":[\"qa\",\"vn\",\"ng\",\"bg\",\"ee\",\"cy\",\"ro\",\"si\",\"mt\",\"lt\",\"sk\",\"mu\",\"ar\",\"om\",\"bh\",\"ke\"],\"BRICS\":[\"cn\",\"br\",\"in\",\"za\",\"ru\"],\"Next Eleven\":[\"id\",\"ph\",\"mx\",\"eg\",\"kr\",\"vn\",\"ng\"],\"European Union\":[\"gb\",\"de\",\"it\",\"fr\",\"be\",\"pl\",\"hu\",\"cz\",\"ie\",\"lv\",\"bg\",\"es\",\"at\",\"lu\",\"nl\",\"ee\",\"cy\",\"fi\",\"ro\",\"pt\",\"gr\",\"dk\",\"si\",\"mt\",\"lt\"]}}",
							"false" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchSector")) {
			groupArray = new Object[][] { { "Communication Services#$", "true" }, { "Industrials#$", "false" },
					{ "Consumer Staples#$", "false" }, { "Health Care#$", "false" }, { "Materials#$", "false" },
					{ "Information Technology#$", "false" }, { "Consumer Discretionary#$", "false" },
					{ "Utilities#$", "true" }, { "Real Estate#$", "true" }, { "Energy#$", "true" },
					{ "Financials#$", "false" }, { "Other#$", "true" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchDate")) {
			groupArray = new Object[][] { { "all", "true" }, { "7d", "false" }, { "1m", "false" }, { "3m", "false" },
					{ "6m", "true" }, { "1y", "true" }, { "2y", "true" }, { "5y", "false" }, { "10y", "false" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchDoctype")) {
			groupArray = new Object[][] { { "ef#$", "false" }, { "tt#$", "true" }, { "ppt#$", "false" },
					{ "ni#$", "true" }, { "gbf#$", "false" }, { "nw#$", "false" } };

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchSource")) {
			groupArray = new Object[][] { { "false" }, { "true" } };

		}
		return groupArray;

	}

	@DataProvider(name = "fetch_yearly_data")
	public Object[][] fetchEarlyData(Method testmethodname) {
		Object[][] groupArray = null;
		if (testmethodname.getName().equalsIgnoreCase("fetchgraphdata")) {
			groupArray = new String[][] { { "p_sales" }, { "p_eps" }, { "ev_sales" }, { "ev_ebitda" }, { "ev_ebit" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchgraphdata1")) {
			groupArray = new String[][] { { "TotalRevenue" }, { "DilutedEPSTotal" }, { "Ebitda" }, { "Ebit" },
					{ "NetIncome" }, { "DividendsPaidPerShare" }, { "PurchaseOfPropertyPlantAndEquipment" },
					{ "CashFlowPerShare" } };

		} else if (testmethodname.getName().equalsIgnoreCase("fetchgraphdata2")) {
			groupArray = new String[][] { { "mkt_cap" }, { "cogs" }, { "da" }, { "ebit" }, { "ebit-per" }, { "ebitda" },
					{ "ebitda-per" }, { "eps" }, { "sales" }, { "p_eps" }, { "gross_profit" }, { "p_sales" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchvaluetable")) {
			groupArray = new String[][] { { "GrossProfit", "usd" }, { "DilutedEPSTotal", "cad" }, { "Ebitda", "euro" },
					{ "Ebit", "usd" }, { "NetIncome", "usd" } };
		} else if (testmethodname.getName().equalsIgnoreCase("earningssurprisesdata")) {
			groupArray = new String[][] { { "TotalRevenue", "QuarterlyRestated" }, { "TotalRevenue", "AnnualRestated" },
					{ "DilutedEPSTotal", "AnnualRestated" }, { "DilutedEPSTotal", "QuarterlyRestated" },
					{ "FreeCashFlowPerShare", "QuarterlyRestated" }, { "FreeCashFlowPerShare", "AnnualRestated" } };
		}

		return groupArray;
	}

	@DataProvider(name = "fetch_graph_data")
	public Object[][] fetchGraphData() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Revenue", "sales", "ibes" }, { "COGS", "cogs", "rf" },
				{ "Gross Profit", "gross_profit", "rf" } };
		return groupArray;
	}

	@DataProvider(name = "fetch_data")
	public Object[][] fetchData() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "P/E", "p_eps", "rf" }, { "EV/Sales", "ev_sales", "rf" },
				{ "EV/EBITDA", "ev_ebitda", "rf" } };
		return groupArray;
	}

	@DataProvider(name = "fetch_yearly_data1")
	public String[][] fetchEarlyData() {
		return CSVReaderUtil.readAllDataAtOnce("finance" + File.separator + "fetch_yearly_data.csv");
	}

	@DataProvider(name = "users")
	public Object[][] fetchUsers() {
		Object[][] users = null;
		users = new String[][] { { "alphagani35@gmail.com", "DGL=14412jg" }, { "bhaskar@sentieo.com", "Sentieo.789" },
				{ "atish.garg@sentieo.com", "Atishgarg1@" }, { "sanjay.saini@sentieo.com", "sanjay.saini97" } };
		return users;
	}

	@DataProvider(name = "numberof_rows")
	public Object[][] fetchPrivateData() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "all" }, { "4" }, { "50" } };
		return groupArray;
	}

	@DataProvider(name = "sortingData")
	public Object[][] privateDataSorting() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "asc", FETCH_CB_EXIT_TABLE }, { "asc", FETCH_CB_ACQ_TABLE },
				{ "asc", FETCH_CB_FUNDS_TABLE }, { "asc", FETCH_CB_KEY_INVESTORS }, { "desc", FETCH_CB_EXIT_TABLE },
				{ "desc", FETCH_CB_ACQ_TABLE }, { "desc", FETCH_CB_FUNDS_TABLE }, { "desc", FETCH_CB_KEY_INVESTORS } };
		return groupArray;
	}

	@DataProvider(name = "withoutsortingorder")
	public Object[][] privateDataWithoutSortingOrder() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { FETCH_CB_EXIT_TABLE }, { FETCH_CB_ACQ_TABLE }, { FETCH_CB_FUNDS_TABLE },
				{ FETCH_CB_KEY_INVESTORS }, { FETCH_CB_EXIT_TABLE } };
		return groupArray;
	}

}
