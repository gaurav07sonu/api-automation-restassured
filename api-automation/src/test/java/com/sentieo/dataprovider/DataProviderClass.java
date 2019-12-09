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

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearch2")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearch2.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("bulk_download")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "bulk_download.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_docs_meta_data")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_docs_meta_data.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("load_saved_search_data")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "load_saved_search_data.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_company_docs")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_company_docs.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_pdf_flag")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_pdf_flag.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_note_doc")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_note_doc.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_transform_doc_content")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_transform_doc_content.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_exhibits")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_exhibits.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_pagelink")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_pagelink.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("pub_doc_viewer")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "pub_doc_viewer.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("pub_doc_viewer1")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "pub_doc_viewer1.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("get_bulk_download_doc")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "get_bulk_download_doc.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("get_doc_pdf")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "get_doc_pdf.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("query_suggest_autocomplete")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "query_suggest_autocomplete.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_snippets")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_snippets.csv");

		}else if (testmethodname.getName().equalsIgnoreCase("fetch_search_term_count")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_search_term_count.csv");

		}
		return groupArray;

	}

	@DataProvider(name = "SearchProvider")
	public Object[][] getDataFromDataprovider(Method testmethodname) {
		Object[][] groupArray = null;
		if (testmethodname.getName().equalsIgnoreCase("fetchsearchRegions")) {
			groupArray = new Object[][] { { "{\"United States\":{\"United States\":[\"us\"]}}","true" },
					{ "{\"Canada\":{\"Canada\":[\"ca\"]}}","true" },
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

		}else if (testmethodname.getName().equalsIgnoreCase("fetchsearchContext")) {
			groupArray = new Object[][] { {"10k.item 1", "true"},{"10k.item 1a", "false"},{"10k.item 1b", "true"},{"10k.item 2", "true"},{"10k.item 3", "true"},{"10k.item 4", "false"},{"10k.item 5", "true"},{"10k.item 6", "false"},{"10k.item 7", "true"},{"10k.item 7a", "true"},{"10k.item 8", "true"},{"10k.item 9", "false"},{"10k.item 9a", "true"},{"10k.item 9b", "true"},{"10k.item 10", "true"},{"10k.item 11", "false"},{"10k.item 12", "true"},{"10k.item 13", "true"},{"10k.item 14", "true"},{"10k.item 15", "true"}};
	
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
	@DataProvider(name = "graphDataYearlyEstimate")
	public Object[][] yearlyEstimate() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"DilutedEPSTotal"},{"TotalRevenue"},{"GrossProfit"},{"Ebitda"}, {"Ebit"},{"NetIncome"},{"DividendsPaidPerShare"},{"CashFlowPerShare"},{"PurchaseOfPropertyPlantAndEquipment"},{"GrossMargin"},{"EbitdaMargin"},{"EbitMargin"}};
		return groupArray;
		}
	
	@DataProvider(name = "tradingMultiplesCombination")
	public Object[][] tradingMultiples() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"p_eps","rolling","Next 4 Quarters"},{"p_eps","rolling","NTM Rolling"},{"p_eps","carry","Current Year"},{"p_eps","rolling","Trailing 4 Quarters"}, {"p_eps","carry","Next Year"}
		,{"ev_sales","rolling","Next 4 Quarters"},{"ev_sales","rolling","NTM Rolling"},{"ev_sales","carry","Current Year"},{"ev_sales","rolling","Trailing 4 Quarters"},{"ev_sales","carry","Next Year"},
		{"p_sales","rolling","Next 4 Quarters"},{"p_sales","rolling","NTM Rolling"},{"p_sales","carry","Current Year"},{"p_sales","rolling","Trailing 4 Quarters"},{"p_sales","carry","Next Year"},
		{"ev_ebitda","rolling","Next 4 Quarters"},{"ev_ebitda","rolling","NTM Rolling"},{"ev_ebitda","carry","Current Year"},{"ev_ebitda","rolling","Trailing 4 Quarters"},{"ev_ebitda","carry","Next Year"},
		{"ev_ebit","rolling","Next 4 Quarters"},{"ev_ebit","rolling","NTM Rolling"},{"ev_ebit","carry","Current Year"},{"ev_ebit","rolling","Trailing 4 Quarters"},{"ev_ebit","carry","Next Year"}};
		return groupArray;
		}
	
	@DataProvider(name = "tradingMultiplesLTMNTM")
	public Object[][] tradingMultiplesLTMNTM() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"p_eps","blended"},{"p_eps","backward"},{"ev_sales","blended"},{"ev_sales","backward"},{"p_sales","blended"},{"p_sales","backward"},{"ev_ebitda","blended"},{"ev_ebitda","backward"},{"ev_ebit","blended"},{"ev_ebit","backward"}};
		return groupArray;
		}
	
	@DataProvider(name = "plotterDailySeries")
	public Object[][] dailySeries() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"P/E","p_eps"},{"EV/EBITDA","ev_ebitda"},{"EV/GROSS PROFIT","ev_grossprofit"},{"EV/(EBITDA-CAPEX)","ev_ebitdacapex"},{"EV/EBIT","ev_ebit"},{"EV/Sales","ev_sales"},{"P/Sales","p_sales"},{"FCF Yield","fcfyield_mkt"}
		,{"FCF Yield using EV","fcfyield_ev"},{"P/Book Value","price_bookvalue"},{"P/Tangible Book Value","tang_bookvalue"},{"P/Cash Flow","price_cashflow"},
		{"Enterprise Value","ev_daily"},{"Market Cap","mkt_cap_daily"}};
		return groupArray;
		}
	
	}


