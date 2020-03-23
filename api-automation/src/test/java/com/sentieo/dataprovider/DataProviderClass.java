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

		} else if (testmethodname.getName().equalsIgnoreCase("load_saved_search_data")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "load_saved_search_data.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_company_docs")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_company_docs.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_pdf_flag")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_pdf_flag.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_note_doc")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_note_doc.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_transform_doc_content")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch" + File.separator + "fetch_transform_doc_content.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_exhibits")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_exhibits.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_pagelink")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_pagelink.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("pub_doc_viewer")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "pub_doc_viewer.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("pub_doc_viewer1")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "pub_doc_viewer1.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("get_bulk_download_doc")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "get_bulk_download_doc.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("get_doc_pdf")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "get_doc_pdf.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("query_suggest_autocomplete")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch" + File.separator + "query_suggest_autocomplete.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_snippets")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_snippets.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_search_term_count")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_search_term_count.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter1")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_date_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter2")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_region_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter3")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_sector_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter4")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_date_sector_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter5")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_period_region_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter6")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_sector_region_filter.json");
			
		}
		
		return groupArray;

	}

	@DataProvider(name = "SearchProvider")
	public Object[][] getDataFromDataprovider(Method testmethodname) {
		Object[][] groupArray = null;
		if (testmethodname.getName().equalsIgnoreCase("fetchsearchRegions")) {
			groupArray = new Object[][] { { "{\"United States\":{\"United States\":[\"us\"]}}", "true" },
					{ "{\"Canada\":{\"Canada\":[\"ca\"]}}", "true" },
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
			groupArray = new Object[][] { { "Communication Services#$",
					"Advertising#$Alternative Carriers#$Broadcasting#$Cable & Satellite#$Integrated Telecommunication Services#$Interactive Home Entertainment#$Interactive Media & Services#$Movies & Entertainment#$Publishing#$Wireless Telecommunication Services#$",
					"true" },
					{ "Industrials#$",
							"Aerospace & Defense#$Agricultural & Farm Machinery#$Air Freight & Logistics#$Airlines#$Airport Services#$Building Products#$Commercial Printing#$Construction & Engineering#$Construction Machinery & Heavy Trucks#$Diversified Support Services#$Electrical Components & Equipment#$Environmental & Facilities Services#$Heavy Electrical Equipment#$Highways & Railtracks#$Human Resource & Employment Services#$Industrial Conglomerates#$Industrial Machinery#$Marine#$Marine Ports & Services#$Office Services & Supplies#$Railroads#$Research & Consulting Services#$Security & Alarm Services#$Trading Companies & Distributors#$Trucking#$",
							"false" },
					{ "Consumer Staples#$",
							"Agricultural Products#$Brewers#$Distillers & Vintners#$Drug Retail#$Food Distributors#$Food Retail#$Household Products#$Hypermarkets & Super Centers#$Packaged Foods & Meats#$Personal Products#$Soft Drinks#$Tobacco#$",
							"false" },
					{ "Health Care#$",
							"Biotechnology#$Health Care Distributors#$Health Care Equipment#$Health Care Facilities#$Health Care Services#$Health Care Supplies#$Health Care Technology#$Life Sciences Tools & Services#$Managed Health Care#$Pharmaceuticals#$",
							"false" },
					{ "Materials#$",
							"Aluminum#$Commodity Chemicals#$Construction Materials#$Copper#$Diversified Chemicals#$Diversified Metals & Mining#$Fertilizers & Agricultural Chemicals#$Forest Products#$Gold#$Industrial Gases#$Metal & Glass Containers#$Paper Packaging#$Paper Products#$Precious Metals & Minerals#$Silver#$Specialty Chemicals#$Steel#$",
							"false" },
					{ "Information Technology#$",
							"Application Software#$Communications Equipment#$Data Processing & Outsourced Services#$Electronic Components#$Electronic Equipment & Instruments#$Electronic Manufacturing Services#$IT Consulting & Other Services#$Internet Services & Infrastructure#$Semiconductor Equipment#$Semiconductors#$Systems Software#$Technology Distributors#$Technology Hardware, Storage & Peripherals#$",
							"false" },
					{ "Consumer Discretionary#$",
							"Apparel Retail#$Apparel, Accessories & Luxury Goods#$Auto Parts & Equipment#$Automobile Manufacturers#$Automotive Retail#$Casinos & Gaming#$Computer & Electronics Retail#$Consumer Electronics#$Department Stores#$Distributors#$Education Services#$Footwear#$General Merchandise Stores#$Home Furnishings#$Home Improvement Retail#$Homebuilding#$Homefurnishing Retail#$Hotels, Resorts & Cruise Lines#$Household Appliances#$Housewares & Specialties#$Internet & Direct Marketing Retail#$Leisure Facilities#$Leisure Products#$Motorcycle Manufacturers#$Restaurants#$Specialized Consumer Services#$Specialty Stores#$Textiles#$Tires & Rubber#$",
							"false" },
					{ "Utilities#$",
							"Electric Utilities#$Gas Utilities#$Independent Power Producers & Energy Traders#$Multi-Utilities#$Renewable Electricity#$Water Utilities#$",
							"true" },
					{ "Real Estate#$",
							"Diversified REITs#$Diversified Real Estate Activities#$Health Care REITs#$Hotel & Resort REITs#$Industrial REITs#$Office REITs#$Real Estate Development#$Real Estate Operating Companies#$Real Estate Services#$Residential REITs#$Retail REITs#$Specialized REITs#$",
							"true" },
					{ "Energy#$",
							"Coal & Consumable Fuels#$Integrated Oil & Gas#$Oil & Gas Drilling#$Oil & Gas Equipment & Services#$Oil & Gas Exploration & Production#$Oil & Gas Refining & Marketing#$Oil & Gas Storage & Transportation#$",
							"true" },
					{ "Financials#$",
							"Asset Management & Custody Banks#$Consumer Finance#$Diversified Banks#$Diversified Capital Markets#$Financial Exchanges & Data#$Insurance Brokers#$Investment Banking & Brokerage#$Life & Health Insurance#$Mortgage REITs#$Multi-Sector Holdings#$Multi-line Insurance#$Other Diversified Financial Services#$Property & Casualty Insurance#$Regional Banks#$Reinsurance#$Specialized Finance#$Thrifts & Mortgage Finance#$",
							"false" },
					{ "Other#$", "Other#$", "true" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchDate")) {
			groupArray = new Object[][] { { "all", "true" }, { "7d", "false" }, { "1m", "false" }, { "3m", "false" },
					{ "6m", "true" }, { "1y", "true" }, { "2y", "true" }, { "5y", "false" }, { "10y", "false" } };
		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchDoctype")) {
			groupArray = new Object[][] { { "ef#$",
					"10-q#$10-k#$nt 10-q#$nt 10-k#$10qsb#$10ksb#$10-q/a#$10-k/a#$10qsb/a#$10ksb/a#$8-k agreement#$8-k credit#$8-k earnings#$8-k mgmt#$8-k restruct#$8-k update#$8-k voting#$8-k other#$425#$s-4#$8-k/a#$s-4/a#$6-k#$20-f#$40-f#$6-k/a#$def 14a#$defa14a#$pre 14a#$dfan14a#$def 14c#$prer14a#$pre 14c#$497#$defr14a#$n-px#$defm14a#$prec14a#$prer14c#$prem14a#$prrn14a#$defc14a#$prem14c#$pren14a#$defn14a#$defa14c#$defr14c#$defm14c#$dfrn14a#$n-px/a#$prec14c#$defc14c#$424b2#$424b3#$fwp#$424b5#$s-1/a#$pos am#$s-3#$s-1#$8-a12b#$s-3asr#$s-3/a#$424b4#$10-12g/a#$10-12b/a#$f-1#$f-3#$10-12g#$s-11#$10-12b#$sb-1#$sc 13g/a#$sc 13g#$sc 13d/a#$sc 13d#$13f-hr#$13f-hr/a#$s-8#$11-k#$s-8 pos#$form-3#$form-4#$form-5#$form-3/A#$form-4/A#$form-5/A#$1-a#$1-a pos#$1-a-w#$1-a/a#$1-e#$1-e/a#$1-z#$10-d#$10-d/a#$10-k405#$10-k405/a#$10-kt#$10-kt/a#$10-qt#$10-qt/a#$10ksb40#$10kt405#$10sb12b#$10sb12b/a#$10sb12g#$10sb12g/a#$11-k/a#$11-kt#$11-kt/a#$13f-nt#$13f-nt/a#$144#$144/a#$15-12b#$15-12b/a#$15-12g#$15-12g/a#$15-15d#$15-15d/a#$15f-12b#$15f-12b/a#$15f-12g#$15f-12g/a#$15f-15d#$15f-15d/a#$18-k#$2-e#$2-e/a#$20-f/a#$20fr12b#$20fr12b/a#$20fr12g#$20fr12g/a#$24f-2nt#$24f-2nt/a#$25#$25-nse#$25-nse/a#$25/a#$3#$305b2#$305b2/a#$35-cert#$35-cert/a#$40-17f1#$40-17f2#$40-17f2/a#$40-17g#$40-17g/a#$40-33#$40-6b#$40-6b/a#$40-8b25#$40-8f-2#$40-8f-2/a#$40-app#$40-app/a#$40-f/a#$40-oip#$40-oip/a#$40fr12b#$40fr12b/a#$40fr12g#$40fr12g/a#$424a#$424b1#$424b7#$424b8#$485apos#$485bpos#$485bxt#$486bpos#$497ad#$497h2#$497j#$497k#$6b ntc#$6b ordr#$8-a12b/a#$8-a12g#$8-a12g/a#$8-k12b#$8-k12b/a#$8-k12g3#$8-k12g3/a#$8-k15d5#$abs-15g#$abs-15g/a#$annlrpt#$app ntc#$app ordr#$app wd#$app wd/a#$app wdg#$ars#$ars/a#$aw#$aw wd#$cb#$cb/a#$corresp#$ct order#$d#$d/a#$defs14a#$del am#$drs#$drs/a#$drsltr#$effect#$f-1/a#$f-10#$f-10/a#$f-10ef#$f-10pos#$f-1mef#$f-2#$f-2/a#$f-3/a#$f-3asr#$f-3d#$f-3dpos#$f-3mef#$f-4#$f-4 pos#$f-4/a#$f-4mef#$f-6#$f-6 pos#$f-6/a#$f-6ef#$f-7#$f-7/a#$f-8#$f-8 pos#$f-8/a#$f-80#$f-80/a#$f-80pos#$f-9#$f-9 pos#$f-9/a#$f-9ef#$f-n#$f-n/a#$f-x#$f-x/a#$irannotice#$n-1/a#$n-14#$n-14 8c#$n-14 8c/a#$n-14/a#$n-14ae#$n-14ae/a#$n-14mef#$n-18f1#$n-1a#$n-1a/a#$n-2#$n-2/a#$n-23c-1#$n-23c-2#$n-23c-2/a#$n-23c3a#$n-23c3a/a#$n-2mef#$n-30b-2#$n-30d#$n-30d/a#$n-54a#$n-54c#$n-54c/a#$n-6f#$n-6f/a#$n-8a#$n-8a/a#$n-8f#$n-8f ntc#$n-8f/a#$n-csr#$n-csr/a#$n-csrs#$n-csrs/a#$n-mfp#$n-mfp/a#$n-mfp1#$n-q#$n-q/a#$nsar-a#$nsar-b#$nsar-u#$nsar-u/a#$nt 10-k/a#$nt 10-q/a#$nt 11-k#$nt 11-k/a#$nt 15d2#$nt 20-f#$nt 20-f/a#$nt-ncsr#$nt-nsar#$nt-nsar/a#$ntfnsar#$ntn 10k#$ntn 10q#$ntn 20f#$pos 8c#$pos amc#$pos ami#$pos ex#$pos462b#$pos462c#$posasr#$pres14a#$px14a6g#$px14a6n#$qualif#$regdex/a#$revoked#$rw#$rw wd#$s-11/a#$s-11mef#$s-1mef#$s-2#$s-2/a#$s-2mef#$s-3d#$s-3dpos#$s-3mef#$s-4 pos#$s-4ef#$s-4ef/a#$s-4mef#$s-b#$s-b/a#$sb-1/a#$sb-2#$sb-2/a#$sb-2mef#$sc 13e1#$sc 13e1/a#$sc 13e3#$sc 13e3/a#$sc 14d9#$sc 14d9/a#$sc 14f1#$sc 14f1/a#$sc to-c#$sc to-i#$sc to-i/a#$sc to-t#$sc to-t/a#$sc13e4f#$sc13e4f/a#$sc14d1f#$sc14d1f/a#$sc14d9c#$sc14d9f#$sc14d9f/a#$sd#$sd/a#$sp 15d2#$sp 15d2/a#$suppl#$t-3#$t-3/a#$u-1#$u-1/a#$u-12-ia#$u-12-ib#$u-13-60#$u-13-60/a#$u-33-s#$u-3a-2#$u-3a-2/a#$u-57#$u-57/a#$u-6b-2#$u-9c-3#$u-9c-3/a#$u5a#$u5a/a#$u5b#$u5s#$u5s/a#$under#$upload#$certnys#$no act#$certpac#$13fconp#$g-405n#$u-3a3-1#$sc 14d1#$ttw#$485a24f#$485a24e#$39-304d#$1-a-w/a#$n-cr/a#$19b-4#$26#$adv-h-c#$pre13e3#$s-6#$prea14a#$defs14c#$adv-h-t#$msd/a#$advco#$sc 14d1/a#$ta-2#$ta-1#$s-20#$f-7 pos#$g-fin/a#$cfportal#$reg-nr/a#$msdco#$13f-e/a#$qrtlyrpt/a#$13fconp/a#$g-finw#$40-8f-m#$8a12bt#$annlrpt/a#$ta-1/a#$focusn/a#$485b24f#$485b24e#$focusn#$1-z/a#$ma/a#$24f-1#$afdb/a#$ifc#$n14el24#$ta-w#$sf-1#$sf-3#$adv-e#$n-4 el/a#$dstrbrpt#$n-4 el#$40-24b2/a#$ebrd#$1-e ad#$8-m#$sf-1/a#$n-23c3c#$n-23c3b#$40-202a/a#$u-13e-1#$s-6el24/a#$n-23c3b/a#$n-cr#$c-w#$40-202a#$10-c/a#$u-7d#$u-6b-2/a#$n-23c-1/a#$n14ae24#$x-17a-5#$u5b/a#$s-6/a#$s-3d/a#$nrsro-ce/a#$pres14c#$nt n-mfp#$c#$10-qsb#$n-8b-2#$ma-w#$cfportal/a#$ma-i#$ma-a#$ma-i/a#$n-18f1/a#$nt 10-d/a#$24f-2el#$dstrbrpt/a#$253g2#$n-1a el/a#$253g1#$8-b12g#$2-a#$8-b12b#$nrsro-upd#$n-6/a#$497k1#$497k2#$dosltr#$n-1a el#$s-20/a#$10-c#$40-33/a#$c/a#$adb#$msd#$9-m#$nt 10-d#$n-6#$n-4#$bdco#$40-8f-b#$sdr#$40-8f-l/a#$8-k15d5/a#$40-8f-l#$u-12-ib/a#$x-17a-5/a#$def13e3/a#$advw#$13f-e#$sc 13e4/a#$8-b12b/a#$ma#$dos/a#$bw-2#$bw-3#$40-8f-m/a#$u-12-ia/a#$2-a/a#$n-23c3c/a#$s-6el24#$8a12bt/a#$s-8/a#$8-b12g/a#$iadb#$8a12bef#$n-3#$40-203a#$ttw/a#$40-8f-a#$afdb#$497k3a#$497k3b#$424h/a#$nt n-mfp1#$u-7d/a#$40-24b2#$sc 13e4#$n14el24/a#$n-mfp1/a#$s-bmef#$g-fin#$def-oc#$n-27d-1#$pre13e3/a#$n-8b-2/a#$adv-nr#$n-3/a#$424h#$taco#$487#$ebrd/a#$40-17gcs#$nt 15d2/a#$n-4/a#$18-12b#$def13e3#$sf-3/a#$24f-2tm#$40-8f-a/a#$reg-nr#$ta-2/a#$sl#$dos#$40-17f1/a#$486apos#$ntn 10d#$",
					"false" },
					{ "tt#$", "analyst_shareholder_meeting_other#$conference#$earnings#$guidance#$m&a#$", "true" },
					{ "ni#$",
							"#earningsrelease#$#management#$#earningsannounce#$#dividend#$#acquisition#$#partnership#$#drug#$#stock#$#ipo#$#stocksplit#$#spinoff#$#others#$gpr#$pr#$bw#$gnw#$mw#$uk_disc#$cnw#$nwca#$nw#$acw#$acn#$act#$abn#$fsc#$to#$",
							"false" },
					{ "ppt#$", "1#$2#$3#$4#$5#$6#$8#$0#$", "true" },
					{ "gbf#$",
							"ars#$intrm#$invbook#$mgtpres#$opr met#$proddev#$list#$prosp#$circ#$fi debt#$offer#$esg#$",
							"false" },
					{ "nw#$",
							"lx_192511#$lx_se_6598#$lx_190697#$lx_se_8057#$lx_se_9682#$lx_se_109488#$lx_91272#$lx_76706#$lx_12827#$lx_se_14584#$lx_se_48509#$lx_se_19013#$lx_se_74038#$lx_se_82122#$lx_se_23459#$lx_se_132321#$lx_se_32525#$lx_85876#$lx_91334#$lx_se_35349#$lx_se_48775#$lx_88957#$lx_39578#$lx_90492#$lx_91071#$lx_49703#$lx_se_51611#$lx_se_51638#$lx_se_52922#$lx_se_1110#$lx_se_1154#$lx_se_4171#$lx_se_4237#$lx_se_4249#$lx_se_9227#$lx_73596#$lx_se_11080#$lx_76086#$lx_se_11530#$lx_se_118419#$lx_se_11595#$lx_85698#$lx_112942#$lx_se_23192#$lx_se_83778#$lx_se_31465#$lx_se_88233#$lx_1306#$lx_104677#$lx_se_1449#$lx_113125#$lx_se_5763#$lx_se_8774#$lx_se_9226#$lx_se_11595#$lx_106069#$lx_se_13320#$lx_se_14709#$lx_se_17855#$lx_se_17864#$lx_80937#$lx_se_80254#$lx_se_18921#$lx_104820#$lx_se_80680#$lx_se_21621#$lx_se_21625#$lx_se_23334#$lx_se_83847#$lx_85572#$lx_se_34650#$lx_se_39114#$lx_118069#$lx_90036#$lx_se_45762#$lx_71636#$lx_se_114629#$lx_se_3063#$lx_75753#$lx_se_12712#$lx_79011#$lx_se_189850#$lx_104804#$lx_79307#$lx_80652#$lx_se_121906#$lx_22266#$lx_82850#$lx_104856#$lx_33301#$lx_se_85705#$lx_36067#$lx_se_56717#$lx_87791#$lx_88345#$lx_88348#$lx_107381#$lx_se_39180#$lx_se_39907#$lx_se_90009#$lx_90047#$lx_se_49941#$lx_106757#$lx_92959#$lx_92928#$lx_180036#$lx_se_101010#$lx_se_54470#$lx_se_124622#$lx_104677#$lx_se_2380#$lx_se_72246#$lx_se_108300#$lx_se_4032#$lx_se_4870#$lx_se_48362#$lx_se_5597#$lx_se_64349#$lx_se_6491#$lx_se_7290#$lx_se_8649#$lx_se_58171#$lx_73596#$lx_10574#$lx_se_12030#$lx_se_76405#$lx_se_76686#$lx_77578#$lx_80122#$lx_80432#$lx_se_19013#$lx_se_63339#$lx_se_24808#$lx_se_25073#$lx_se_25105#$lx_se_25556#$lx_se_25561#$lx_se_83596#$lx_85205#$lx_se_32322#$lx_33727#$lx_se_33414#$lx_se_87630#$lx_se_34693#$lx_se_57457#$lx_se_62506#$lx_104990#$lx_se_38617#$lx_se_39907#$lx_se_105002#$lx_se_40306#$lx_88861#$lx_100641#$lx_se_40431#$lx_se_89439#$lx_se_105014#$lx_se_90024#$lx_44150#$lx_91081#$lx_223395#$lx_se_49091#$lx_se_82674#$lx_se_49865#$lx_se_53132#$lx_se_2455#$lx_se_72230#$lx_se_6009#$lx_se_5965#$lx_se_5972#$lx_se_9226#$lx_se_48442#$lx_75585#$lx_se_75754#$lx_se_11573#$lx_77822#$lx_77827#$lx_14713#$lx_se_79268#$lx_se_17999#$lx_se_18252#$lx_se_18249#$lx_se_61329#$lx_20447#$lx_22458#$lx_se_82238#$lx_se_22508#$lx_83276#$lx_83451#$lx_133239#$lx_se_24954#$lx_82874#$lx_85635#$lx_88965#$lx_se_27015#$lx_se_31758#$lx_se_31875#$lx_85599#$lx_se_32058#$lx_se_33290#$lx_se_70288#$lx_191344#$lx_140489#$lx_179298#$lx_se_38892#$lx_90016#$lx_134551#$lx_176499#$lx_191025#$lx_se_71628#$lx_se_71453#$lx_se_1574#$lx_71791#$lx_se_1587#$lx_se_71798#$lx_se_93773#$lx_se_1599#$lx_72151#$lx_72430#$lx_se_3148#$lx_72342#$lx_se_73643#$lx_105978#$lx_210013#$lx_75023#$lx_118050#$lx_104767#$lx_se_11501#$lx_se_16080#$lx_se_79231#$lx_se_18662#$lx_se_80562#$lx_116094#$lx_83248#$lx_se_24808#$lx_74887#$lx_90564#$lx_se_24945#$lx_84005#$lx_se_30828#$lx_197597#$lx_se_85311#$lx_85790#$lx_se_39675#$lx_89157#$lx_119560#$lx_se_43796#$lx_se_45814#$lx_91589#$lx_se_124622#$lx_4854#$lx_4878#$lx_4883#$lx_4888#$lx_7356#$lx_7358#$lx_76334#$lx_se_48509#$lx_17228#$lx_18038#$lx_se_83559#$lx_se_36032#$lx_213718#$lx_50689#$lx_51124#$lx_se_988#$lx_71642#$lx_se_2472#$lx_5100#$lx_se_7288#$lx_se_9144#$lx_75447#$lx_75450#$lx_75443#$lx_104754#$lx_75454#$lx_76078#$lx_75449#$lx_85301#$lx_se_85919#$lx_se_39332#$lx_se_39335#$lx_se_42919#$lx_se_42920#$lx_86039#$lx_71785#$lx_se_8261#$lx_76004#$lx_76046#$lx_120788#$lx_77860#$lx_14170#$lx_se_79041#$lx_80198#$lx_81978#$lx_23976#$lx_82845#$lx_82853#$lx_104855#$lx_24806#$lx_se_100808#$lx_se_83730#$lx_85302#$lx_se_35108#$lx_75002#$lx_88820#$lx_se_39675#$lx_106072#$lx_90279#$lx_se_43796#$lx_47769#$lx_se_54126#$lx_87245#$lx_se_23246#$lx_90838#$lx_91113#$lx_105028#$lx_75882#$lx_87336#$lx_104701#$lx_99282#$lx_89990#$lx_99283#$lx_se_8057#$lx_10505#$lx_67772#$lx_82763#$lx_83236#$lx_179238#$lx_se_24989#$lx_59700#$lx_31466#$lx_77865#$lx_33727#$lx_108076#$lx_45791#$lx_se_6598#$lx_214603#$lx_191100#$lx_49703#$lx_50297#$",
							"false" } };

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchSource")) {
			groupArray = new Object[][] { { "false" }, { "true" } };

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchContext")) {
			groupArray = new Object[][] { { "10k.item 1", "true" }, { "10k.item 1a", "false" },
					{ "10k.item 1b", "true" }, { "10k.item 2", "true" }, { "10k.item 3", "true" },
					{ "10k.item 4", "false" }, { "10k.item 5", "true" }, { "10k.item 6", "false" },
					{ "10k.item 7", "true" }, { "10k.item 7a", "true" }, { "10k.item 8", "true" },
					{ "10k.item 9", "false" }, { "10k.item 9a", "true" }, { "10k.item 9b", "true" },
					{ "10k.item 10", "true" }, { "10k.item 11", "false" }, { "10k.item 12", "true" },
					{ "10k.item 13", "true" }, { "10k.item 14", "true" }, { "10k.item 15", "true" } };

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
		groupArray = new String[][] { { "DilutedEPSTotal" }, { "TotalRevenue" }, { "GrossProfit" }, { "Ebitda" },
				{ "Ebit" }, { "NetIncome" }, { "DividendsPaidPerShare" }, { "CashFlowPerShare" },
				{ "PurchaseOfPropertyPlantAndEquipment" }, { "GrossMargin" }, { "EbitdaMargin" }, { "EbitMargin" } };
		return groupArray;
	}

	@DataProvider(name = "tradingMultiplesCombination")
	public Object[][] tradingMultiples() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "p_eps", "rolling", "Next 4 Quarters" }, { "p_eps", "rolling", "NTM Rolling" },
				{ "p_eps", "carry", "Current Year" }, { "p_eps", "rolling", "Trailing 4 Quarters" },
				{ "p_eps", "carry", "Next Year" }, { "ev_sales", "rolling", "Next 4 Quarters" },
				{ "ev_sales", "rolling", "NTM Rolling" }, { "ev_sales", "carry", "Current Year" },
				{ "ev_sales", "rolling", "Trailing 4 Quarters" }, { "ev_sales", "carry", "Next Year" },
				{ "p_sales", "rolling", "Next 4 Quarters" }, { "p_sales", "rolling", "NTM Rolling" },
				{ "p_sales", "carry", "Current Year" }, { "p_sales", "rolling", "Trailing 4 Quarters" },
				{ "p_sales", "carry", "Next Year" }, { "ev_ebitda", "rolling", "Next 4 Quarters" },
				{ "ev_ebitda", "rolling", "NTM Rolling" }, { "ev_ebitda", "carry", "Current Year" },
				{ "ev_ebitda", "rolling", "Trailing 4 Quarters" }, { "ev_ebitda", "carry", "Next Year" },
				{ "ev_ebit", "rolling", "Next 4 Quarters" }, { "ev_ebit", "rolling", "NTM Rolling" },
				{ "ev_ebit", "carry", "Current Year" }, { "ev_ebit", "rolling", "Trailing 4 Quarters" },
				{ "ev_ebit", "carry", "Next Year" } };
		return groupArray;
	}

	@DataProvider(name = "tradingMultiplesLTMNTM")
	public Object[][] tradingMultiplesLTMNTM() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "p_eps", "blended" }, { "p_eps", "backward" }, { "ev_sales", "blended" },
				{ "ev_sales", "backward" }, { "p_sales", "blended" }, { "p_sales", "backward" },
				{ "ev_ebitda", "blended" }, { "ev_ebitda", "backward" }, { "ev_ebit", "blended" },
				{ "ev_ebit", "backward" } };
		return groupArray;
	}

	@DataProvider(name = "plotterDailySeries")
	public Object[][] dailySeries() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"P/E","p_eps"},{"EV/EBITDA","ev_ebitda"},{"EV/EBIT","ev_ebit"},{"EV/Sales","ev_sales"},{"P/Sales","p_sales"},{"P/Cash Flow","price_cashflow"},
		{"Enterprise Value","ev_daily"},{"Market Cap","mkt_cap_daily"}};
		return groupArray;
		}

	@DataProvider(name = "EDTEstimates14-22")
	public Object[][] dilutedEPS() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Diluted EPS-2014","aapl"},{"Diluted EPS-2015","aapl"},
			{"Diluted EPS-2016","aapl"},{"Diluted EPS-2017","aapl"}
		,{"Diluted EPS-2018","aapl"},{"Diluted EPS-2019","aapl"},
		{"Diluted EPS-2020","aapl"},{"Diluted EPS-2021","aapl"},{"Diluted EPS-2022","aapl"},{"Diluted EPS-NTM","aapl"},
		{"Diluted EPS-2014","7203:JP"},{"Diluted EPS-2015","7203:JP"},{"Diluted EPS-2016","7203:JP"},{"Diluted EPS-2017","7203:JP"}
		,{"Diluted EPS-2018","7203:JP"},{"Diluted EPS-2019","7203:JP"},
		{"Diluted EPS-2020","7203:JP"},{"Diluted EPS-2021","7203:JP"},{"Diluted EPS-2022","7203:JP"},{"Diluted EPS-NTM","7203:jp"}};
		return groupArray;
		}
	

	@DataProvider(name = "EDTEstimates14-21")
	public Object[][] dilutedEPS21() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Diluted EPS-2014","ALV:GR "},{"Diluted EPS-2015","ALV:GR "},{"Diluted EPS-2016","ALV:GR "}
		,{"Diluted EPS-2017","ALV:GR"}
		,{"Diluted EPS-2018","ALV:GR"},{"Diluted EPS-2019","ALV:GR"},
		{"Diluted EPS-2020","ALV:GR"},{"Diluted EPS-2021","ALV:GR"},
		{"Diluted EPS-NTM","ALV:GR"},{"Diluted EPS-2014","BP.:LN"},{"Diluted EPS-2015","BP.:LN"}
		,{"Diluted EPS-2016","BP.:LN"},{"Diluted EPS-2017","BP.:LN"}
		,{"Diluted EPS-2018","BP.:LN"},{"Diluted EPS-2019","BP.:LN"},{"Diluted EPS-2020","BP.:LN"},
		{"Diluted EPS-2021","BP.:LN"},
		{"Diluted EPS-NTM","BP.:LN"}};
		return groupArray;
		}
	
	@DataProvider(name = "EDTEstimates19-21")
	public Object[][] dilutedEPS19to21() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Diluted EPS-2019","uber"},
		{"Diluted EPS-2020","uber"},{"Diluted EPS-2021","uber"},
		{"Diluted EPS-NTM","uber"}};
		return groupArray;
		}
	

	@DataProvider(name = "EDTEstimates16-21")
	public Object[][] dilutedEPS16to21() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Diluted EPS-2016","ola:cn "}
		,{"Diluted EPS-2017","ola:cn"}
		,{"Diluted EPS-2018","ola:cn"},{"Diluted EPS-2019","ola:cn"},
		{"Diluted EPS-2020","ola:cn"},{"Diluted EPS-2021","ola:cn"},
		{"Diluted EPS-NTM","ola:cn"}};
		return groupArray;
		}
	
	@DataProvider(name = "EDTEstimates14-20")
	public Object[][] dilutedEPS14to20() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Diluted EPS-2014","loxo"},{"Diluted EPS-2015","loxo"},{"Diluted EPS-2016","loxo"}
		,{"Diluted EPS-2017","loxo"}
		,{"Diluted EPS-2018","loxo"},{"Diluted EPS-2019","loxo"},
		{"Diluted EPS-2020","loxo"},
		{"Diluted EPS-NTM","loxo"}};
		return groupArray;
		}
	}
