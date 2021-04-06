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

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_impact_score")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_impact_score.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetch_custom_doc_diff")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetch_custom_doc_diff.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearch")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearch.csv");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearch1")) {
			groupArray = CSVReaderUtil.readAllDataAtOnce("docsearch" + File.separator + "fetchsearch1.csv");

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

		} else if (testmethodname.getName().equalsIgnoreCase("docsearch_date_filter")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_date_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter2")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_region_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter3")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_sector_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter4")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_date_sector_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter5")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_period_region_filter.json");

		} else if (testmethodname.getName().equalsIgnoreCase("fetchsearchfilter6")) {
			groupArray = CSVReaderUtil
					.readAllDataAtOnce("docsearch_filters" + File.separator + "doctype_sector_region_filter.json");

		}

		return groupArray;

	}

	@DataProvider(name = "test_doctype_watchlist")
	public Object[][] test_doctype_watchlist() {
		Object[][] groupArray = null;
		groupArray = new String[][] { {
				"005930:ks,1810:hk,2317:tt,2330:tt,2454:tt,6758:jp,700:hk,7974:jp,992:hk,a,aapl,abbv,acn,adbe,adi,adsk,akam,amat,amd,amgn,amt,amzn,anet,antm,aph,arw,asml:na,atvi,avgo,avt,ba,baba,bby,bkng,brkr,c,cat,cci,chkp,chtr,ci,cmcsa,cone,cost,crm,csco,ctl,ctxs,cvs,cvx,data,dbx,dell,dhr,dis,ea,ebay,eqix,ew,fb,ffiv,ftnt,ge,gild,gm,googl,gsbd,hd,holx,hon,hpe,hpq,hubs,hum,ibm,ilmn,intc,intu,iqv,jbl,jnj,jnpr,jpm,ko,lrcx,ma,mcd,mchp,mdt,mrk,msft,mtd,mu,mygn,nflx,nke,nlok,nokia:fh,now,ntap,ntnx,nvda,nxpi,on,orcl,panw,payc,pfe,pfpt,pki,pstg,pypl,qcom,qgen,qrvo,rht,rng,rtx,s,sap:gr,sbac,sbux,shop:cn,siri,splk,spot,stm:fp,stx,swks,t,tdc,team,tel,tmo,tmus,tsla,ttwo,twlo,twtr,txn,unh,v,viac,vmw,vz,wat,wday,wdc,wmt,xlnx,xom,zen",
				"{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"regions\":{},\"source\":{},\"other\":{},\"date\":{}}" } };

		return groupArray;
	}

	@DataProvider(name = "test_doctype_query")
	public Object[][] test_doctype_query() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{ "aapl", "sales", "{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
									{ "msft", "sales AND marketing", "{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-q\",\"10-q/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"googl", "china OR profit", "{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"param\":\"filing_subtype\",\"values\":[\"10-q\",\"10-k\",\"nt 10-q\",\"nt 10-k\",\"10qsb\",\"10ksb\",\"10-q/a\",\"10-k/a\",\"10qsb/a\",\"10ksb/a\"]},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[\"8-k agreement\",\"8-k credit\",\"8-k earnings\",\"8-k mgmt\",\"8-k restruct\",\"8-k update\",\"8-k voting\",\"8-k other\",\"425\",\"s-4\",\"8-k/a\",\"s-4/a\"]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[\"6-k\",\"20-f\",\"40-f\",\"6-k/a\"]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[\"def 14a\",\"defa14a\",\"pre 14a\",\"dfan14a\",\"def 14c\",\"prer14a\",\"pre 14c\",\"497\",\"defr14a\",\"n-px\",\"defm14a\",\"prec14a\",\"prer14c\",\"prem14a\",\"prrn14a\",\"defc14a\",\"prem14c\",\"pren14a\",\"defn14a\",\"defa14c\",\"defr14c\",\"defm14c\",\"dfrn14a\",\"n-px/a\",\"prec14c\",\"defc14c\"]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[\"424b2\",\"424b3\",\"fwp\",\"424b5\",\"s-1/a\",\"pos am\",\"s-3\",\"s-1\",\"8-a12b\",\"s-3asr\",\"s-3/a\",\"424b4\",\"10-12g/a\",\"10-12b/a\",\"f-1\",\"f-3\",\"10-12g\",\"s-11\",\"10-12b\",\"sb-1\"]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[\"sc 13g/a\",\"sc 13g\",\"sc 13d/a\",\"sc 13d\",\"13f-hr\",\"13f-hr/a\"]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[\"s-8\",\"11-k\",\"s-8 pos\"]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[\"form-3\",\"form-4\",\"form-5\",\"form-3/a\",\"form-4/a\",\"form-5/a\"]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[\"1-a\",\"1-a pos\",\"1-a-w\",\"1-a/a\",\"1-e\",\"1-e/a\",\"1-z\",\"10-d\",\"10-d/a\",\"10-k405\",\"10-k405/a\",\"10-kt\",\"10-kt/a\",\"10-qt\",\"10-qt/a\",\"10ksb40\",\"10kt405\",\"10sb12b\",\"10sb12b/a\",\"10sb12g\",\"10sb12g/a\",\"11-k/a\",\"11-kt\",\"11-kt/a\",\"13f-nt\",\"13f-nt/a\",\"144\",\"144/a\",\"15-12b\",\"15-12b/a\",\"15-12g\",\"15-12g/a\",\"15-15d\",\"15-15d/a\",\"15f-12b\",\"15f-12b/a\",\"15f-12g\",\"15f-12g/a\",\"15f-15d\",\"15f-15d/a\",\"18-k\",\"2-e\",\"2-e/a\",\"20-f/a\",\"20fr12b\",\"20fr12b/a\",\"20fr12g\",\"20fr12g/a\",\"24f-2nt\",\"24f-2nt/a\",\"25\",\"25-nse\",\"25-nse/a\",\"25/a\",\"3\",\"305b2\",\"305b2/a\",\"35-cert\",\"35-cert/a\",\"40-17f1\",\"40-17f2\",\"40-17f2/a\",\"40-17g\",\"40-17g/a\",\"40-33\",\"40-6b\",\"40-6b/a\",\"40-8b25\",\"40-8f-2\",\"40-8f-2/a\",\"40-app\",\"40-app/a\",\"40-f/a\",\"40-oip\",\"40-oip/a\",\"40fr12b\",\"40fr12b/a\",\"40fr12g\",\"40fr12g/a\",\"424a\",\"424b1\",\"424b7\",\"424b8\",\"485apos\",\"485bpos\",\"485bxt\",\"486bpos\",\"497ad\",\"497h2\",\"497j\",\"497k\",\"6b ntc\",\"6b ordr\",\"8-a12b/a\",\"8-a12g\",\"8-a12g/a\",\"8-k12b\",\"8-k12b/a\",\"8-k12g3\",\"8-k12g3/a\",\"8-k15d5\",\"abs-15g\",\"abs-15g/a\",\"annlrpt\",\"app ntc\",\"app ordr\",\"app wd\",\"app wd/a\",\"app wdg\",\"ars\",\"ars/a\",\"aw\",\"aw wd\",\"cb\",\"cb/a\",\"corresp\",\"ct order\",\"d\",\"d/a\",\"defs14a\",\"del am\",\"drs\",\"drs/a\",\"drsltr\",\"effect\",\"f-1/a\",\"f-10\",\"f-10/a\",\"f-10ef\",\"f-10pos\",\"f-1mef\",\"f-2\",\"f-2/a\",\"f-3/a\",\"f-3asr\",\"f-3d\",\"f-3dpos\",\"f-3mef\",\"f-4\",\"f-4 pos\",\"f-4/a\",\"f-4mef\",\"f-6\",\"f-6 pos\",\"f-6/a\",\"f-6ef\",\"f-7\",\"f-7/a\",\"f-8\",\"f-8 pos\",\"f-8/a\",\"f-80\",\"f-80/a\",\"f-80pos\",\"f-9\",\"f-9 pos\",\"f-9/a\",\"f-9ef\",\"f-n\",\"f-n/a\",\"f-x\",\"f-x/a\",\"irannotice\",\"n-1/a\",\"n-14\",\"n-14 8c\",\"n-14 8c/a\",\"n-14/a\",\"n-14ae\",\"n-14ae/a\",\"n-14mef\",\"n-18f1\",\"n-1a\",\"n-1a/a\",\"n-2\",\"n-2/a\",\"n-23c-1\",\"n-23c-2\",\"n-23c-2/a\",\"n-23c3a\",\"n-23c3a/a\",\"n-2mef\",\"n-30b-2\",\"n-30d\",\"n-30d/a\",\"n-54a\",\"n-54c\",\"n-54c/a\",\"n-6f\",\"n-6f/a\",\"n-8a\",\"n-8a/a\",\"n-8f\",\"n-8f ntc\",\"n-8f/a\",\"n-csr\",\"n-csr/a\",\"n-csrs\",\"n-csrs/a\",\"n-mfp\",\"n-mfp/a\",\"n-mfp1\",\"n-q\",\"n-q/a\",\"nsar-a\",\"nsar-b\",\"nsar-u\",\"nsar-u/a\",\"nt 10-k/a\",\"nt 10-q/a\",\"nt 11-k\",\"nt 11-k/a\",\"nt 15d2\",\"nt 20-f\",\"nt 20-f/a\",\"nt-ncsr\",\"nt-nsar\",\"nt-nsar/a\",\"ntfnsar\",\"ntn 10k\",\"ntn 10q\",\"ntn 20f\",\"pos 8c\",\"pos amc\",\"pos ami\",\"pos ex\",\"pos462b\",\"pos462c\",\"posasr\",\"pres14a\",\"px14a6g\",\"px14a6n\",\"qualif\",\"regdex/a\",\"revoked\",\"rw\",\"rw wd\",\"s-11/a\",\"s-11mef\",\"s-1mef\",\"s-2\",\"s-2/a\",\"s-2mef\",\"s-3d\",\"s-3dpos\",\"s-3mef\",\"s-4 pos\",\"s-4ef\",\"s-4ef/a\",\"s-4mef\",\"s-b\",\"s-b/a\",\"sb-1/a\",\"sb-2\",\"sb-2/a\",\"sb-2mef\",\"sc 13e1\",\"sc 13e1/a\",\"sc 13e3\",\"sc 13e3/a\",\"sc 14d9\",\"sc 14d9/a\",\"sc 14f1\",\"sc 14f1/a\",\"sc to-c\",\"sc to-i\",\"sc to-i/a\",\"sc to-t\",\"sc to-t/a\",\"sc13e4f\",\"sc13e4f/a\",\"sc14d1f\",\"sc14d1f/a\",\"sc14d9c\",\"sc14d9f\",\"sc14d9f/a\",\"sd\",\"sd/a\",\"sp 15d2\",\"sp 15d2/a\",\"suppl\",\"t-3\",\"t-3/a\",\"u-1\",\"u-1/a\",\"u-12-ia\",\"u-12-ib\",\"u-13-60\",\"u-13-60/a\",\"u-33-s\",\"u-3a-2\",\"u-3a-2/a\",\"u-57\",\"u-57/a\",\"u-6b-2\",\"u-9c-3\",\"u-9c-3/a\",\"u5a\",\"u5a/a\",\"u5b\",\"u5s\",\"u5s/a\",\"under\",\"upload\",\"certnys\",\"no act\",\"certpac\",\"13fconp\",\"g-405n\",\"u-3a3-1\",\"sc 14d1\",\"ttw\",\"485a24f\",\"485a24e\",\"39-304d\",\"1-a-w/a\",\"n-cr/a\",\"19b-4\",\"26\",\"adv-h-c\",\"pre13e3\",\"s-6\",\"prea14a\",\"defs14c\",\"adv-h-t\",\"msd/a\",\"advco\",\"sc 14d1/a\",\"ta-2\",\"ta-1\",\"s-20\",\"f-7 pos\",\"g-fin/a\",\"cfportal\",\"reg-nr/a\",\"msdco\",\"13f-e/a\",\"qrtlyrpt/a\",\"13fconp/a\",\"g-finw\",\"40-8f-m\",\"8a12bt\",\"annlrpt/a\",\"ta-1/a\",\"focusn/a\",\"485b24f\",\"485b24e\",\"focusn\",\"1-z/a\",\"ma/a\",\"24f-1\",\"afdb/a\",\"ifc\",\"n14el24\",\"ta-w\",\"sf-1\",\"sf-3\",\"adv-e\",\"n-4 el/a\",\"dstrbrpt\",\"n-4 el\",\"40-24b2/a\",\"ebrd\",\"1-e ad\",\"8-m\",\"sf-1/a\",\"n-23c3c\",\"n-23c3b\",\"40-202a/a\",\"u-13e-1\",\"s-6el24/a\",\"n-23c3b/a\",\"n-cr\",\"c-w\",\"40-202a\",\"10-c/a\",\"u-7d\",\"u-6b-2/a\",\"n-23c-1/a\",\"n14ae24\",\"x-17a-5\",\"u5b/a\",\"s-6/a\",\"s-3d/a\",\"nrsro-ce/a\",\"pres14c\",\"nt n-mfp\",\"c\",\"10-qsb\",\"n-8b-2\",\"ma-w\",\"cfportal/a\",\"ma-i\",\"ma-a\",\"ma-i/a\",\"n-18f1/a\",\"nt 10-d/a\",\"24f-2el\",\"dstrbrpt/a\",\"253g2\",\"n-1a el/a\",\"253g1\",\"8-b12g\",\"2-a\",\"8-b12b\",\"nrsro-upd\",\"n-6/a\",\"497k1\",\"497k2\",\"dosltr\",\"n-1a el\",\"s-20/a\",\"10-c\",\"40-33/a\",\"c/a\",\"adb\",\"msd\",\"9-m\",\"nt 10-d\",\"n-6\",\"n-4\",\"bdco\",\"40-8f-b\",\"sdr\",\"40-8f-l/a\",\"8-k15d5/a\",\"40-8f-l\",\"u-12-ib/a\",\"x-17a-5/a\",\"def13e3/a\",\"advw\",\"13f-e\",\"sc 13e4/a\",\"8-b12b/a\",\"ma\",\"dos/a\",\"bw-2\",\"bw-3\",\"40-8f-m/a\",\"u-12-ia/a\",\"2-a/a\",\"n-23c3c/a\",\"s-6el24\",\"8a12bt/a\",\"s-8/a\",\"8-b12g/a\",\"iadb\",\"8a12bef\",\"n-3\",\"40-203a\",\"ttw/a\",\"40-8f-a\",\"afdb\",\"497k3a\",\"497k3b\",\"424h/a\",\"nt n-mfp1\",\"u-7d/a\",\"40-24b2\",\"sc 13e4\",\"n14el24/a\",\"n-mfp1/a\",\"s-bmef\",\"g-fin\",\"def-oc\",\"n-27d-1\",\"pre13e3/a\",\"n-8b-2/a\",\"adv-nr\",\"n-3/a\",\"424h\",\"taco\",\"487\",\"ebrd/a\",\"40-17gcs\",\"nt 15d2/a\",\"n-4/a\",\"18-12b\",\"def13e3\",\"sf-3/a\",\"24f-2tm\",\"40-8f-a/a\",\"reg-nr\",\"ta-2/a\",\"sl\",\"dos\",\"40-17f1/a\",\"486apos\",\"ntn 10d\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
//									{"vod:ln","sales", "{\"ticker\":{},\"doctype\":{\"rr\":{\"comrep_types\":{\"param\":\"rr_reasons\",\"values\":[\"rr_reasons_3\",\"rr_reasons_27\",\"rr_reasons_5\",\"rr_reasons_7\",\"rr_reasons_8\",\"rr_reasons_2\",\"rr_reasons_6\",\"rr_reasons_1\",\"rr_reasons_12\",\"rr_reasons_13\",\"rr_reasons_15\",\"rr_reasons_17\",\"rr_reasons_19\",\"rr_reasons_14\",\"rr_reasons_9\"]},\"othrep_types\":{\"param\":\"rr_styles\",\"values\":[\"rr_styles_1\",\"rr_styles_2\",\"rr_styles_3\",\"rr_styles_4\",\"rr_styles_5\",\"rr_styles_6\",\"rr_styles_7\",\"rr_styles_8\",\"rr_styles_9\"]},\"research-providers-1\":{\"param\":\"ctbid\",\"values\":[\"se_11097\",\"se_11831\",\"se_11835\",\"se_11840\",\"se_11103\",\"se_10619\",\"se_10895\"]},\"research-providers-3\":{\"param\":\"ctbid\",\"values\":[\"se_11821\",\"se_11099\",\"se_11823\",\"se_11098\",\"se_11828\",\"se_11825\",\"se_11826\",\"se_11111\",\"se_19010\",\"se_11102\",\"se_11115\",\"se_49969\",\"se_11116\",\"se_11846\",\"se_11847\",\"se_11849\",\"se_11104\",\"se_11118\",\"se_11851\",\"se_11122\",\"se_11105\",\"se_11125\",\"se_11856\",\"se_11858\",\"se_11860\",\"se_11859\",\"se_10782\",\"se_11864\",\"se_11131\",\"se_11106\",\"se_11107\",\"se_11132\",\"se_11133\",\"se_11135\",\"se_11108\",\"se_11136\"]},\"research-providers-4\":{\"param\":\"ctbid\",\"values\":[\"se_10003\",\"se_10976\",\"se_10005\",\"se_26207\",\"se_10057\",\"se_10975\",\"se_10006\",\"se_22076\",\"se_10024\",\"se_10033\",\"se_10035\",\"se_27030\",\"se_21511\",\"se_10977\",\"se_10978\",\"se_10047\",\"se_10062\",\"se_10979\",\"se_10980\",\"se_10981\",\"se_36482\",\"se_29804\",\"se_10982\",\"se_10983\",\"se_10984\",\"se_10083\",\"se_10986\",\"se_10119\",\"se_10993\",\"se_11824\",\"se_11100\",\"se_10166\",\"se_10987\",\"se_11822\",\"se_25087\",\"se_10988\",\"se_10989\",\"se_24851\",\"se_10125\",\"se_10990\",\"se_10991\",\"se_10992\",\"se_27298\",\"se_32905\",\"se_10138\",\"se_10995\",\"se_20467\",\"se_10151\",\"se_11109\",\"se_10996\",\"se_11110\",\"se_10165\",\"se_10997\",\"se_28681\",\"se_11001\",\"se_10221\",\"se_11003\",\"se_23375\",\"se_10998\",\"se_10999\",\"se_11000\",\"se_22857\",\"se_34639\",\"se_28602\",\"se_11002\",\"se_10203\",\"se_10208\",\"se_24001\",\"se_25387\",\"se_23000\",\"se_10233\",\"se_11004\",\"se_11005\",\"se_11006\",\"se_11007\",\"se_11008\",\"se_11009\",\"se_11010\",\"se_11011\",\"se_11113\",\"se_11015\",\"se_11016\",\"se_11017\",\"se_34845\",\"se_11832\",\"se_11012\",\"se_11013\",\"se_11014\",\"se_11834\",\"se_27805\",\"se_11018\",\"se_11019\",\"se_11021\",\"se_36696\",\"se_10292\",\"se_11020\",\"se_24110\",\"se_11836\",\"se_10301\",\"se_10303\",\"se_27852\",\"se_10309\",\"se_10311\",\"se_10314\",\"se_11022\",\"se_11023\",\"se_11024\",\"se_11025\",\"se_27424\",\"se_11026\",\"se_11027\",\"se_11030\",\"se_11028\",\"se_11031\",\"se_10367\",\"se_11033\",\"se_11034\",\"se_10386\",\"se_11035\",\"se_10389\",\"se_29829\",\"se_11839\",\"se_11036\",\"se_11037\",\"se_10410\",\"se_10412\",\"se_11842\",\"se_10428\",\"se_11843\",\"se_11038\",\"se_11039\",\"se_10427\",\"se_11040\",\"se_37430\",\"se_10433\",\"se_11844\",\"se_11042\",\"se_10478\",\"se_11043\",\"se_10494\",\"se_11845\",\"se_22108\",\"se_11044\",\"se_38697\",\"se_20499\",\"se_11045\",\"se_10514\",\"se_34189\",\"se_21812\",\"se_10521\",\"se_11848\",\"se_11046\",\"se_11047\",\"se_11048\",\"se_11049\",\"se_27775\",\"se_10543\",\"se_11050\",\"se_10546\",\"se_11051\",\"se_11052\",\"se_25363\",\"se_11053\",\"se_11054\",\"se_11119\",\"se_10624\",\"se_11055\",\"se_11056\",\"se_11117\",\"se_10600\",\"se_10601\",\"se_11058\",\"se_25667\",\"se_10615\",\"se_11120\",\"se_11060\",\"se_10620\",\"se_11852\",\"se_10622\",\"se_11061\",\"se_35085\",\"se_10641\",\"se_11121\",\"se_10640\",\"se_10645\",\"se_10648\",\"se_11123\",\"se_10660\",\"se_10661\",\"se_10665\",\"se_11124\",\"se_10668\",\"se_11062\",\"se_11063\",\"se_25984\",\"se_11064\",\"se_10677\",\"se_10679\",\"se_10688\",\"se_10716\",\"se_27572\",\"se_32642\",\"se_10699\",\"se_11065\",\"se_27795\",\"se_11066\",\"se_10707\",\"se_26017\",\"se_11067\",\"se_29738\",\"se_11068\",\"se_28853\",\"se_25891\",\"se_11070\",\"se_10746\",\"se_11071\",\"se_11861\",\"se_24652\",\"se_10749\",\"se_22815\",\"se_11127\",\"se_11072\",\"se_10767\",\"se_38956\",\"se_33901\",\"se_27738\",\"se_24608\",\"se_10781\",\"se_11130\",\"se_11075\",\"se_11078\",\"se_11081\",\"se_11865\",\"se_11862\",\"se_11076\",\"se_11863\",\"se_11077\",\"se_11080\",\"se_37889\",\"se_10801\",\"se_11082\",\"se_11083\",\"se_11084\",\"se_11085\",\"se_10844\",\"se_10850\",\"se_11868\",\"se_11087\",\"se_20275\",\"se_27374\",\"se_11134\",\"se_10868\",\"se_24602\",\"se_10876\",\"se_27634\",\"se_10891\",\"se_26372\",\"se_11089\",\"se_11088\",\"se_11869\",\"se_11090\",\"se_11091\",\"se_10919\",\"se_10926\",\"se_10927\",\"se_36000\",\"se_11092\",\"se_11093\",\"se_10952\",\"se_11094\",\"se_11137\",\"se_11095\",\"se_26544\",\"se_11096\",\"se_10969\",\"se_10974\",\"se_11057\",\"se_21659\"]},\"research-providers-5\":{\"param\":\"ctbid\",\"values\":[\"se_11139\",\"se_11140\",\"se_11141\",\"se_11142\",\"se_11143\",\"se_10004\",\"se_10008\",\"se_11155\",\"se_10028\",\"se_10029\",\"se_11161\",\"se_11192\",\"se_11199\",\"se_11207\",\"se_41466\",\"se_47715\",\"se_11222\",\"se_11223\",\"se_11225\",\"se_10009\",\"se_11146\",\"se_11147\",\"se_11150\",\"se_11149\",\"se_11151\",\"se_11153\",\"se_11154\",\"se_11157\",\"se_11159\",\"se_11162\",\"se_11163\",\"se_11164\",\"se_11165\",\"se_11166\",\"se_11167\",\"se_11820\",\"se_11170\",\"se_11171\",\"se_11173\",\"se_11174\",\"se_11176\",\"se_11178\",\"se_11179\",\"se_11180\",\"se_11181\",\"se_11183\",\"se_11185\",\"se_11190\",\"se_11188\",\"se_11189\",\"se_10048\",\"se_11193\",\"se_11195\",\"se_11196\",\"se_11197\",\"se_11198\",\"se_40721\",\"se_11202\",\"se_39629\",\"se_11204\",\"se_11205\",\"se_11206\",\"se_11208\",\"se_11209\",\"se_10068\",\"se_11212\",\"se_11213\",\"se_11214\",\"se_11216\",\"se_11218\",\"se_11219\",\"se_30024\",\"se_11220\",\"se_30580\",\"se_11224\",\"se_11242\",\"se_39311\",\"se_11243\",\"se_30424\",\"se_11251\",\"se_47950\",\"se_11252\",\"se_11256\",\"se_11258\",\"se_11259\",\"se_11261\",\"se_11262\",\"se_11264\",\"se_49262\",\"se_11265\",\"se_11266\",\"se_11267\",\"se_11268\",\"se_32895\",\"se_11273\",\"se_11278\",\"se_11279\",\"se_11227\",\"se_31047\",\"se_11228\",\"se_11229\",\"se_39666\",\"se_30874\",\"se_11231\",\"se_40244\",\"se_11234\",\"se_11235\",\"se_11237\",\"se_11238\",\"se_11236\",\"se_11239\",\"se_41727\",\"se_11240\",\"se_11241\",\"se_11244\",\"se_11245\",\"se_11247\",\"se_11248\",\"se_30779\",\"se_47792\",\"se_11255\",\"se_11257\",\"se_43434\",\"se_11269\",\"se_11270\",\"se_48874\",\"se_11272\",\"se_32603\",\"se_11274\",\"se_37397\",\"se_11276\",\"se_11277\",\"se_32140\",\"se_47194\",\"se_11291\",\"se_36840\",\"se_11292\",\"se_34483\",\"se_30811\",\"se_11297\",\"se_37525\",\"se_10197\",\"se_43738\",\"se_11300\",\"se_11302\",\"se_11303\",\"se_11304\",\"se_11305\",\"se_11307\",\"se_34466\",\"se_11322\",\"se_11324\",\"se_11325\",\"se_11326\",\"se_11328\",\"se_35945\",\"se_31102\",\"se_11282\",\"se_11284\",\"se_11285\",\"se_46234\",\"se_11286\",\"se_36559\",\"se_35871\",\"se_11287\",\"se_11290\",\"se_31527\",\"se_11294\",\"se_11295\",\"se_11296\",\"se_30149\",\"se_11827\",\"se_45979\",\"se_44207\",\"se_11299\",\"se_11829\",\"se_11301\",\"se_35545\",\"se_11306\",\"se_11830\",\"se_48418\",\"se_36106\",\"se_32658\",\"se_11311\",\"se_11312\",\"se_11313\",\"se_11314\",\"se_11316\",\"se_11317\",\"se_11318\",\"se_33193\",\"se_11320\",\"se_11321\",\"se_11323\",\"se_11332\",\"se_11334\",\"se_11335\",\"se_11330\",\"se_10259\",\"se_11333\",\"se_11337\",\"se_11338\",\"se_11339\",\"se_47525\",\"se_39112\",\"se_39877\",\"se_11344\",\"se_39309\",\"se_11345\",\"se_11346\",\"se_11348\",\"se_11349\",\"se_30753\",\"se_40208\",\"se_39717\",\"se_34953\",\"se_11376\",\"se_39249\",\"se_11350\",\"se_11351\",\"se_11353\",\"se_11354\",\"se_11355\",\"se_11356\",\"se_11357\",\"se_11358\",\"se_11359\",\"se_11362\",\"se_11363\",\"se_11837\",\"se_11366\",\"se_11367\",\"se_11370\",\"se_46862\",\"se_11372\",\"se_31570\",\"se_11373\",\"se_11374\",\"se_11375\",\"se_41148\",\"se_39571\",\"se_49824\",\"se_31813\",\"se_11380\",\"se_11382\",\"se_11383\",\"se_11384\",\"se_38792\",\"se_48150\",\"se_11398\",\"se_41099\",\"se_49968\",\"se_11401\",\"se_11377\",\"se_11379\",\"se_40432\",\"se_11381\",\"se_30308\",\"se_11390\",\"se_11385\",\"se_11387\",\"se_11391\",\"se_11392\",\"se_45481\",\"se_11394\",\"se_11838\",\"se_11395\",\"se_11397\",\"se_11399\",\"se_11400\",\"se_11402\",\"se_11403\",\"se_48564\",\"se_11405\",\"se_11407\",\"se_11412\",\"se_11428\",\"se_49541\",\"se_11406\",\"se_11408\",\"se_11409\",\"se_11410\",\"se_47781\",\"se_11417\",\"se_45287\",\"se_47231\",\"se_37754\",\"se_11420\",\"se_47033\",\"se_11422\",\"se_11423\",\"se_38748\",\"se_11427\",\"se_11429\",\"se_11439\",\"se_11440\",\"se_11443\",\"se_11444\",\"se_30990\",\"se_11430\",\"se_10424\",\"se_11431\",\"se_11432\",\"se_11433\",\"se_11434\",\"se_11435\",\"se_11436\",\"se_11441\",\"se_11442\",\"se_11445\",\"se_36995\",\"se_11449\",\"se_42428\",\"se_11451\",\"se_41038\",\"se_11452\",\"se_11453\",\"se_11454\",\"se_11455\",\"se_11456\",\"se_11457\",\"se_11458\",\"se_11460\",\"se_11461\",\"se_11463\",\"se_11464\",\"se_11465\",\"se_11467\",\"se_11474\",\"se_11489\",\"se_49170\",\"se_11490\",\"se_11462\",\"se_10472\",\"se_36211\",\"se_11468\",\"se_11471\",\"se_11472\",\"se_43350\",\"se_11473\",\"se_46895\",\"se_11475\",\"se_11476\",\"se_11477\",\"se_11478\",\"se_11481\",\"se_11479\",\"se_47214\",\"se_11482\",\"se_11483\",\"se_11485\",\"se_46707\",\"se_11488\",\"se_35728\",\"se_46118\",\"se_11491\",\"se_11492\",\"se_11493\",\"se_11496\",\"se_11495\",\"se_11500\",\"se_11501\",\"se_43156\",\"se_47580\",\"se_11506\",\"se_41468\",\"se_11497\",\"se_11498\",\"se_11499\",\"se_11503\",\"se_11504\",\"se_11505\",\"se_39234\",\"se_11507\",\"se_11512\",\"se_11514\",\"se_11515\",\"se_39128\",\"se_11516\",\"se_11521\",\"se_11522\",\"se_11523\",\"se_11526\",\"se_11533\",\"se_11534\",\"se_11535\",\"se_46315\",\"se_11509\",\"se_11510\",\"se_11511\",\"se_11518\",\"se_11524\",\"se_11525\",\"se_11527\",\"se_11528\",\"se_33690\",\"se_11530\",\"se_44453\",\"se_11531\",\"se_11850\",\"se_11532\",\"se_31515\",\"se_11536\",\"se_11537\",\"se_43895\",\"se_33841\",\"se_11544\",\"se_11547\",\"se_11550\",\"se_11538\",\"se_11539\",\"se_11542\",\"se_41998\",\"se_31357\",\"se_43424\",\"se_11545\",\"se_11549\",\"se_11553\",\"se_11554\",\"se_11563\",\"se_47199\",\"se_11564\",\"se_11565\",\"se_11578\",\"se_45621\",\"se_11555\",\"se_39716\",\"se_32007\",\"se_11557\",\"se_45872\",\"se_36032\",\"se_11559\",\"se_11560\",\"se_11561\",\"se_31323\",\"se_11562\",\"se_11566\",\"se_11567\",\"se_11568\",\"se_11570\",\"se_11571\",\"se_11574\",\"se_11575\",\"se_45582\",\"se_11577\",\"se_11579\",\"se_11580\",\"se_11581\",\"se_11582\",\"se_11583\",\"se_11585\",\"se_11586\",\"se_11589\",\"se_11590\",\"se_30379\",\"se_11592\",\"se_11596\",\"se_33271\",\"se_10656\",\"se_11605\",\"se_39899\",\"se_11608\",\"se_11610\",\"se_11611\",\"se_47140\",\"se_11612\",\"se_11595\",\"se_48132\",\"se_49332\",\"se_11597\",\"se_11598\",\"se_11599\",\"se_11600\",\"se_10646\",\"se_11602\",\"se_38547\",\"se_11604\",\"se_11606\",\"se_41812\",\"se_44938\",\"se_11609\",\"se_11613\",\"se_11614\",\"se_11623\",\"se_44806\",\"se_11854\",\"se_11616\",\"se_11617\",\"se_11618\",\"se_11619\",\"se_49335\",\"se_11620\",\"se_11621\",\"se_11622\",\"se_45984\",\"se_11855\",\"se_11631\",\"se_46474\",\"se_11649\",\"se_11653\",\"se_11654\",\"se_11655\",\"se_11656\",\"se_11657\",\"se_11624\",\"se_36175\",\"se_40239\",\"se_11626\",\"se_34152\",\"se_11627\",\"se_11628\",\"se_11629\",\"se_11630\",\"se_48316\",\"se_11633\",\"se_11634\",\"se_44955\",\"se_11635\",\"se_44361\",\"se_33348\",\"se_11636\",\"se_32687\",\"se_11639\",\"se_11640\",\"se_11641\",\"se_38538\",\"se_11644\",\"se_10729\",\"se_11647\",\"se_40767\",\"se_11651\",\"se_32240\",\"se_46868\",\"se_48461\",\"se_11857\",\"se_11658\",\"se_11659\",\"se_32979\",\"se_39887\",\"se_11661\",\"se_11662\",\"se_11663\",\"se_11667\",\"se_41742\",\"se_39836\",\"se_11664\",\"se_11668\",\"se_42634\",\"se_39359\",\"se_11670\",\"se_11672\",\"se_42941\",\"se_11674\",\"se_11675\",\"se_11676\",\"se_37966\",\"se_44386\",\"se_11677\",\"se_31606\",\"se_44794\",\"se_11680\",\"se_11681\",\"se_11683\",\"se_45056\",\"se_37957\",\"se_11687\",\"se_33439\",\"se_47401\",\"se_11695\",\"se_11700\",\"se_46428\",\"se_39591\",\"se_11708\",\"se_11713\",\"se_11714\",\"se_11866\",\"se_11715\",\"se_11723\",\"se_11724\",\"se_11725\",\"se_11729\",\"se_11730\",\"se_11739\",\"se_11684\",\"se_11685\",\"se_11686\",\"se_11688\",\"se_11689\",\"se_11691\",\"se_48231\",\"se_11693\",\"se_37900\",\"se_11696\",\"se_11698\",\"se_11701\",\"se_11702\",\"se_11703\",\"se_11704\",\"se_33105\",\"se_32321\",\"se_31742\",\"se_11705\",\"se_11706\",\"se_11707\",\"se_11709\",\"se_11710\",\"se_11711\",\"se_11717\",\"se_11718\",\"se_11720\",\"se_11721\",\"se_11722\",\"se_32076\",\"se_11726\",\"se_11728\",\"se_32834\",\"se_11733\",\"se_30202\",\"se_35753\",\"se_45897\",\"se_11735\",\"se_11736\",\"se_10834\",\"se_11737\",\"se_32744\",\"se_44624\",\"se_11738\",\"se_47785\",\"se_11741\",\"se_42629\",\"se_11745\",\"se_11746\",\"se_11867\",\"se_11748\",\"se_11749\",\"se_11750\",\"se_38711\",\"se_11751\",\"se_41850\",\"se_48726\",\"se_11753\",\"se_11754\",\"se_31879\",\"se_11755\",\"se_37306\",\"se_11757\",\"se_10867\",\"se_40162\",\"se_11759\",\"se_35943\",\"se_11760\",\"se_36324\",\"se_11761\",\"se_11768\",\"se_11769\",\"se_39056\",\"se_11770\",\"se_11772\",\"se_32943\",\"se_11775\",\"se_11776\",\"se_30340\",\"se_11778\",\"se_11780\",\"se_11786\",\"se_11787\",\"se_11788\",\"se_44627\",\"se_11777\",\"se_11779\",\"se_11781\",\"se_11782\",\"se_11783\",\"se_11784\",\"se_37625\",\"se_10903\",\"se_11790\",\"se_30520\",\"se_11802\",\"se_11789\",\"se_11791\",\"se_11792\",\"se_11794\",\"se_11795\",\"se_11796\",\"se_32874\",\"se_11798\",\"se_11799\",\"se_46530\",\"se_11800\",\"se_11801\",\"se_11807\",\"se_11805\",\"se_48458\",\"se_40983\",\"se_11809\",\"se_35394\",\"se_11811\",\"se_47200\",\"se_49378\",\"se_11813\",\"se_11814\",\"se_11815\",\"se_11816\",\"se_11870\",\"se_37237\",\"se_11818\",\"se_11352\",\"se_11388\",\"se_11593\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"a","sales AND marketing", "{\"ticker\":{},\"sector\":{},\"language\":{},\"section\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"regions\":{},\"source\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"all\"]}}},\"other\":{}}"},
									{"ulvr:ln","china OR profit", "{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"tsla", "sales OR aaple BEFORE increased NEAR \"2%\"", "{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"uber", "(Board OR management OR executives) NEAR 6 OR (entrenchment entrenched shielded)", "{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"icpt","OR(30 Thirty) BEFORE 1 day BEFORE 1 OR (prescription prescriptions)", "{\"ticker\":{},\"doctype\":{\"ni\":{\"Tags\":{\"param\":\"tags\",\"values\":[\"#earningsrelease\",\"#management\",\"#earningsannounce\",\"#dividend\",\"#acquisition\",\"#partnership\",\"#drug\",\"#stock\",\"#ipo\",\"#stocksplit\",\"#spinoff\",\"#others\"]},\"Source\":{\"param\":\"rep_subjects\",\"values\":[\"gpr\",\"pr\",\"bw\",\"gnw\",\"mw\",\"uk_disc\",\"cnw\",\"nwca\",\"nw\",\"acw\",\"acn\",\"act\",\"abn\",\"fsc\",\"to\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"601398:CH","sales","{\"ticker\":{},\"sector\":{},\"date\":{},\"regions\":{},\"section\":{},\"source\":{},\"language\":{},\"other\":{},\"doctype\":{},\"rss\":{\"\":{\"\":{\"param\":\"feed_id\",\"values\":[14]}}},\"exchange\":{}}"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_doctype_Filter")
	public Object[][] test_doctype_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "aapl",
				"{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "msft",
						"{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-q\",\"10-q/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "googl",
						"{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"param\":\"filing_subtype\",\"values\":[\"10-q\",\"10-k\",\"nt 10-q\",\"nt 10-k\",\"10qsb\",\"10ksb\",\"10-q/a\",\"10-k/a\",\"10qsb/a\",\"10ksb/a\"]},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[\"8-k agreement\",\"8-k credit\",\"8-k earnings\",\"8-k mgmt\",\"8-k restruct\",\"8-k update\",\"8-k voting\",\"8-k other\",\"425\",\"s-4\",\"8-k/a\",\"s-4/a\"]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[\"6-k\",\"20-f\",\"40-f\",\"6-k/a\"]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[\"def 14a\",\"defa14a\",\"pre 14a\",\"dfan14a\",\"def 14c\",\"prer14a\",\"pre 14c\",\"497\",\"defr14a\",\"n-px\",\"defm14a\",\"prec14a\",\"prer14c\",\"prem14a\",\"prrn14a\",\"defc14a\",\"prem14c\",\"pren14a\",\"defn14a\",\"defa14c\",\"defr14c\",\"defm14c\",\"dfrn14a\",\"n-px/a\",\"prec14c\",\"defc14c\"]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[\"424b2\",\"424b3\",\"fwp\",\"424b5\",\"s-1/a\",\"pos am\",\"s-3\",\"s-1\",\"8-a12b\",\"s-3asr\",\"s-3/a\",\"424b4\",\"10-12g/a\",\"10-12b/a\",\"f-1\",\"f-3\",\"10-12g\",\"s-11\",\"10-12b\",\"sb-1\"]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[\"sc 13g/a\",\"sc 13g\",\"sc 13d/a\",\"sc 13d\",\"13f-hr\",\"13f-hr/a\"]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[\"s-8\",\"11-k\",\"s-8 pos\"]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[\"form-3\",\"form-4\",\"form-5\",\"form-3/a\",\"form-4/a\",\"form-5/a\"]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[\"1-a\",\"1-a pos\",\"1-a-w\",\"1-a/a\",\"1-e\",\"1-e/a\",\"1-z\",\"10-d\",\"10-d/a\",\"10-k405\",\"10-k405/a\",\"10-kt\",\"10-kt/a\",\"10-qt\",\"10-qt/a\",\"10ksb40\",\"10kt405\",\"10sb12b\",\"10sb12b/a\",\"10sb12g\",\"10sb12g/a\",\"11-k/a\",\"11-kt\",\"11-kt/a\",\"13f-nt\",\"13f-nt/a\",\"144\",\"144/a\",\"15-12b\",\"15-12b/a\",\"15-12g\",\"15-12g/a\",\"15-15d\",\"15-15d/a\",\"15f-12b\",\"15f-12b/a\",\"15f-12g\",\"15f-12g/a\",\"15f-15d\",\"15f-15d/a\",\"18-k\",\"2-e\",\"2-e/a\",\"20-f/a\",\"20fr12b\",\"20fr12b/a\",\"20fr12g\",\"20fr12g/a\",\"24f-2nt\",\"24f-2nt/a\",\"25\",\"25-nse\",\"25-nse/a\",\"25/a\",\"3\",\"305b2\",\"305b2/a\",\"35-cert\",\"35-cert/a\",\"40-17f1\",\"40-17f2\",\"40-17f2/a\",\"40-17g\",\"40-17g/a\",\"40-33\",\"40-6b\",\"40-6b/a\",\"40-8b25\",\"40-8f-2\",\"40-8f-2/a\",\"40-app\",\"40-app/a\",\"40-f/a\",\"40-oip\",\"40-oip/a\",\"40fr12b\",\"40fr12b/a\",\"40fr12g\",\"40fr12g/a\",\"424a\",\"424b1\",\"424b7\",\"424b8\",\"485apos\",\"485bpos\",\"485bxt\",\"486bpos\",\"497ad\",\"497h2\",\"497j\",\"497k\",\"6b ntc\",\"6b ordr\",\"8-a12b/a\",\"8-a12g\",\"8-a12g/a\",\"8-k12b\",\"8-k12b/a\",\"8-k12g3\",\"8-k12g3/a\",\"8-k15d5\",\"abs-15g\",\"abs-15g/a\",\"annlrpt\",\"app ntc\",\"app ordr\",\"app wd\",\"app wd/a\",\"app wdg\",\"ars\",\"ars/a\",\"aw\",\"aw wd\",\"cb\",\"cb/a\",\"corresp\",\"ct order\",\"d\",\"d/a\",\"defs14a\",\"del am\",\"drs\",\"drs/a\",\"drsltr\",\"effect\",\"f-1/a\",\"f-10\",\"f-10/a\",\"f-10ef\",\"f-10pos\",\"f-1mef\",\"f-2\",\"f-2/a\",\"f-3/a\",\"f-3asr\",\"f-3d\",\"f-3dpos\",\"f-3mef\",\"f-4\",\"f-4 pos\",\"f-4/a\",\"f-4mef\",\"f-6\",\"f-6 pos\",\"f-6/a\",\"f-6ef\",\"f-7\",\"f-7/a\",\"f-8\",\"f-8 pos\",\"f-8/a\",\"f-80\",\"f-80/a\",\"f-80pos\",\"f-9\",\"f-9 pos\",\"f-9/a\",\"f-9ef\",\"f-n\",\"f-n/a\",\"f-x\",\"f-x/a\",\"irannotice\",\"n-1/a\",\"n-14\",\"n-14 8c\",\"n-14 8c/a\",\"n-14/a\",\"n-14ae\",\"n-14ae/a\",\"n-14mef\",\"n-18f1\",\"n-1a\",\"n-1a/a\",\"n-2\",\"n-2/a\",\"n-23c-1\",\"n-23c-2\",\"n-23c-2/a\",\"n-23c3a\",\"n-23c3a/a\",\"n-2mef\",\"n-30b-2\",\"n-30d\",\"n-30d/a\",\"n-54a\",\"n-54c\",\"n-54c/a\",\"n-6f\",\"n-6f/a\",\"n-8a\",\"n-8a/a\",\"n-8f\",\"n-8f ntc\",\"n-8f/a\",\"n-csr\",\"n-csr/a\",\"n-csrs\",\"n-csrs/a\",\"n-mfp\",\"n-mfp/a\",\"n-mfp1\",\"n-q\",\"n-q/a\",\"nsar-a\",\"nsar-b\",\"nsar-u\",\"nsar-u/a\",\"nt 10-k/a\",\"nt 10-q/a\",\"nt 11-k\",\"nt 11-k/a\",\"nt 15d2\",\"nt 20-f\",\"nt 20-f/a\",\"nt-ncsr\",\"nt-nsar\",\"nt-nsar/a\",\"ntfnsar\",\"ntn 10k\",\"ntn 10q\",\"ntn 20f\",\"pos 8c\",\"pos amc\",\"pos ami\",\"pos ex\",\"pos462b\",\"pos462c\",\"posasr\",\"pres14a\",\"px14a6g\",\"px14a6n\",\"qualif\",\"regdex/a\",\"revoked\",\"rw\",\"rw wd\",\"s-11/a\",\"s-11mef\",\"s-1mef\",\"s-2\",\"s-2/a\",\"s-2mef\",\"s-3d\",\"s-3dpos\",\"s-3mef\",\"s-4 pos\",\"s-4ef\",\"s-4ef/a\",\"s-4mef\",\"s-b\",\"s-b/a\",\"sb-1/a\",\"sb-2\",\"sb-2/a\",\"sb-2mef\",\"sc 13e1\",\"sc 13e1/a\",\"sc 13e3\",\"sc 13e3/a\",\"sc 14d9\",\"sc 14d9/a\",\"sc 14f1\",\"sc 14f1/a\",\"sc to-c\",\"sc to-i\",\"sc to-i/a\",\"sc to-t\",\"sc to-t/a\",\"sc13e4f\",\"sc13e4f/a\",\"sc14d1f\",\"sc14d1f/a\",\"sc14d9c\",\"sc14d9f\",\"sc14d9f/a\",\"sd\",\"sd/a\",\"sp 15d2\",\"sp 15d2/a\",\"suppl\",\"t-3\",\"t-3/a\",\"u-1\",\"u-1/a\",\"u-12-ia\",\"u-12-ib\",\"u-13-60\",\"u-13-60/a\",\"u-33-s\",\"u-3a-2\",\"u-3a-2/a\",\"u-57\",\"u-57/a\",\"u-6b-2\",\"u-9c-3\",\"u-9c-3/a\",\"u5a\",\"u5a/a\",\"u5b\",\"u5s\",\"u5s/a\",\"under\",\"upload\",\"certnys\",\"no act\",\"certpac\",\"13fconp\",\"g-405n\",\"u-3a3-1\",\"sc 14d1\",\"ttw\",\"485a24f\",\"485a24e\",\"39-304d\",\"1-a-w/a\",\"n-cr/a\",\"19b-4\",\"26\",\"adv-h-c\",\"pre13e3\",\"s-6\",\"prea14a\",\"defs14c\",\"adv-h-t\",\"msd/a\",\"advco\",\"sc 14d1/a\",\"ta-2\",\"ta-1\",\"s-20\",\"f-7 pos\",\"g-fin/a\",\"cfportal\",\"reg-nr/a\",\"msdco\",\"13f-e/a\",\"qrtlyrpt/a\",\"13fconp/a\",\"g-finw\",\"40-8f-m\",\"8a12bt\",\"annlrpt/a\",\"ta-1/a\",\"focusn/a\",\"485b24f\",\"485b24e\",\"focusn\",\"1-z/a\",\"ma/a\",\"24f-1\",\"afdb/a\",\"ifc\",\"n14el24\",\"ta-w\",\"sf-1\",\"sf-3\",\"adv-e\",\"n-4 el/a\",\"dstrbrpt\",\"n-4 el\",\"40-24b2/a\",\"ebrd\",\"1-e ad\",\"8-m\",\"sf-1/a\",\"n-23c3c\",\"n-23c3b\",\"40-202a/a\",\"u-13e-1\",\"s-6el24/a\",\"n-23c3b/a\",\"n-cr\",\"c-w\",\"40-202a\",\"10-c/a\",\"u-7d\",\"u-6b-2/a\",\"n-23c-1/a\",\"n14ae24\",\"x-17a-5\",\"u5b/a\",\"s-6/a\",\"s-3d/a\",\"nrsro-ce/a\",\"pres14c\",\"nt n-mfp\",\"c\",\"10-qsb\",\"n-8b-2\",\"ma-w\",\"cfportal/a\",\"ma-i\",\"ma-a\",\"ma-i/a\",\"n-18f1/a\",\"nt 10-d/a\",\"24f-2el\",\"dstrbrpt/a\",\"253g2\",\"n-1a el/a\",\"253g1\",\"8-b12g\",\"2-a\",\"8-b12b\",\"nrsro-upd\",\"n-6/a\",\"497k1\",\"497k2\",\"dosltr\",\"n-1a el\",\"s-20/a\",\"10-c\",\"40-33/a\",\"c/a\",\"adb\",\"msd\",\"9-m\",\"nt 10-d\",\"n-6\",\"n-4\",\"bdco\",\"40-8f-b\",\"sdr\",\"40-8f-l/a\",\"8-k15d5/a\",\"40-8f-l\",\"u-12-ib/a\",\"x-17a-5/a\",\"def13e3/a\",\"advw\",\"13f-e\",\"sc 13e4/a\",\"8-b12b/a\",\"ma\",\"dos/a\",\"bw-2\",\"bw-3\",\"40-8f-m/a\",\"u-12-ia/a\",\"2-a/a\",\"n-23c3c/a\",\"s-6el24\",\"8a12bt/a\",\"s-8/a\",\"8-b12g/a\",\"iadb\",\"8a12bef\",\"n-3\",\"40-203a\",\"ttw/a\",\"40-8f-a\",\"afdb\",\"497k3a\",\"497k3b\",\"424h/a\",\"nt n-mfp1\",\"u-7d/a\",\"40-24b2\",\"sc 13e4\",\"n14el24/a\",\"n-mfp1/a\",\"s-bmef\",\"g-fin\",\"def-oc\",\"n-27d-1\",\"pre13e3/a\",\"n-8b-2/a\",\"adv-nr\",\"n-3/a\",\"424h\",\"taco\",\"487\",\"ebrd/a\",\"40-17gcs\",\"nt 15d2/a\",\"n-4/a\",\"18-12b\",\"def13e3\",\"sf-3/a\",\"24f-2tm\",\"40-8f-a/a\",\"reg-nr\",\"ta-2/a\",\"sl\",\"dos\",\"40-17f1/a\",\"486apos\",\"ntn 10d\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
//				{ "tsla", "{\"ticker\":{},\"doctype\":{\"rr\":{\"comrep_types\":{\"param\":\"rr_reasons\",\"values\":[\"rr_reasons_3\",\"rr_reasons_27\",\"rr_reasons_5\",\"rr_reasons_7\",\"rr_reasons_8\",\"rr_reasons_2\",\"rr_reasons_6\",\"rr_reasons_1\",\"rr_reasons_12\",\"rr_reasons_13\",\"rr_reasons_15\",\"rr_reasons_17\",\"rr_reasons_19\",\"rr_reasons_14\",\"rr_reasons_9\"]},\"othrep_types\":{\"param\":\"rr_styles\",\"values\":[\"rr_styles_1\",\"rr_styles_2\",\"rr_styles_3\",\"rr_styles_4\",\"rr_styles_5\",\"rr_styles_6\",\"rr_styles_7\",\"rr_styles_8\",\"rr_styles_9\"]},\"research-providers-1\":{\"param\":\"ctbid\",\"values\":[\"se_11097\",\"se_11831\",\"se_11835\",\"se_11840\",\"se_11103\",\"se_10619\",\"se_10895\"]},\"research-providers-3\":{\"param\":\"ctbid\",\"values\":[\"se_11821\",\"se_11099\",\"se_11823\",\"se_11098\",\"se_11828\",\"se_11825\",\"se_11826\",\"se_11111\",\"se_19010\",\"se_11102\",\"se_11115\",\"se_49969\",\"se_11116\",\"se_11846\",\"se_11847\",\"se_11849\",\"se_11104\",\"se_11118\",\"se_11851\",\"se_11122\",\"se_11105\",\"se_11125\",\"se_11856\",\"se_11858\",\"se_11860\",\"se_11859\",\"se_10782\",\"se_11864\",\"se_11131\",\"se_11106\",\"se_11107\",\"se_11132\",\"se_11133\",\"se_11135\",\"se_11108\",\"se_11136\"]},\"research-providers-4\":{\"param\":\"ctbid\",\"values\":[\"se_10003\",\"se_10976\",\"se_10005\",\"se_26207\",\"se_10057\",\"se_10975\",\"se_10006\",\"se_22076\",\"se_10024\",\"se_10033\",\"se_10035\",\"se_27030\",\"se_21511\",\"se_10977\",\"se_10978\",\"se_10047\",\"se_10062\",\"se_10979\",\"se_10980\",\"se_10981\",\"se_36482\",\"se_29804\",\"se_10982\",\"se_10983\",\"se_10984\",\"se_10083\",\"se_10986\",\"se_10119\",\"se_10993\",\"se_11824\",\"se_11100\",\"se_10166\",\"se_10987\",\"se_11822\",\"se_25087\",\"se_10988\",\"se_10989\",\"se_24851\",\"se_10125\",\"se_10990\",\"se_10991\",\"se_10992\",\"se_27298\",\"se_32905\",\"se_10138\",\"se_10995\",\"se_20467\",\"se_10151\",\"se_11109\",\"se_10996\",\"se_11110\",\"se_10165\",\"se_10997\",\"se_28681\",\"se_11001\",\"se_10221\",\"se_11003\",\"se_23375\",\"se_10998\",\"se_10999\",\"se_11000\",\"se_22857\",\"se_34639\",\"se_28602\",\"se_11002\",\"se_10203\",\"se_10208\",\"se_24001\",\"se_25387\",\"se_23000\",\"se_10233\",\"se_11004\",\"se_11005\",\"se_11006\",\"se_11007\",\"se_11008\",\"se_11009\",\"se_11010\",\"se_11011\",\"se_11113\",\"se_11015\",\"se_11016\",\"se_11017\",\"se_34845\",\"se_11832\",\"se_11012\",\"se_11013\",\"se_11014\",\"se_11834\",\"se_27805\",\"se_11018\",\"se_11019\",\"se_11021\",\"se_36696\",\"se_10292\",\"se_11020\",\"se_24110\",\"se_11836\",\"se_10301\",\"se_10303\",\"se_27852\",\"se_10309\",\"se_10311\",\"se_10314\",\"se_11022\",\"se_11023\",\"se_11024\",\"se_11025\",\"se_27424\",\"se_11026\",\"se_11027\",\"se_11030\",\"se_11028\",\"se_11031\",\"se_10367\",\"se_11033\",\"se_11034\",\"se_10386\",\"se_11035\",\"se_10389\",\"se_29829\",\"se_11839\",\"se_11036\",\"se_11037\",\"se_10410\",\"se_10412\",\"se_11842\",\"se_10428\",\"se_11843\",\"se_11038\",\"se_11039\",\"se_10427\",\"se_11040\",\"se_37430\",\"se_10433\",\"se_11844\",\"se_11042\",\"se_10478\",\"se_11043\",\"se_10494\",\"se_11845\",\"se_22108\",\"se_11044\",\"se_38697\",\"se_20499\",\"se_11045\",\"se_10514\",\"se_34189\",\"se_21812\",\"se_10521\",\"se_11848\",\"se_11046\",\"se_11047\",\"se_11048\",\"se_11049\",\"se_27775\",\"se_10543\",\"se_11050\",\"se_10546\",\"se_11051\",\"se_11052\",\"se_25363\",\"se_11053\",\"se_11054\",\"se_11119\",\"se_10624\",\"se_11055\",\"se_11056\",\"se_11117\",\"se_10600\",\"se_10601\",\"se_11058\",\"se_25667\",\"se_10615\",\"se_11120\",\"se_11060\",\"se_10620\",\"se_11852\",\"se_10622\",\"se_11061\",\"se_35085\",\"se_10641\",\"se_11121\",\"se_10640\",\"se_10645\",\"se_10648\",\"se_11123\",\"se_10660\",\"se_10661\",\"se_10665\",\"se_11124\",\"se_10668\",\"se_11062\",\"se_11063\",\"se_25984\",\"se_11064\",\"se_10677\",\"se_10679\",\"se_10688\",\"se_10716\",\"se_27572\",\"se_32642\",\"se_10699\",\"se_11065\",\"se_27795\",\"se_11066\",\"se_10707\",\"se_26017\",\"se_11067\",\"se_29738\",\"se_11068\",\"se_28853\",\"se_25891\",\"se_11070\",\"se_10746\",\"se_11071\",\"se_11861\",\"se_24652\",\"se_10749\",\"se_22815\",\"se_11127\",\"se_11072\",\"se_10767\",\"se_38956\",\"se_33901\",\"se_27738\",\"se_24608\",\"se_10781\",\"se_11130\",\"se_11075\",\"se_11078\",\"se_11081\",\"se_11865\",\"se_11862\",\"se_11076\",\"se_11863\",\"se_11077\",\"se_11080\",\"se_37889\",\"se_10801\",\"se_11082\",\"se_11083\",\"se_11084\",\"se_11085\",\"se_10844\",\"se_10850\",\"se_11868\",\"se_11087\",\"se_20275\",\"se_27374\",\"se_11134\",\"se_10868\",\"se_24602\",\"se_10876\",\"se_27634\",\"se_10891\",\"se_26372\",\"se_11089\",\"se_11088\",\"se_11869\",\"se_11090\",\"se_11091\",\"se_10919\",\"se_10926\",\"se_10927\",\"se_36000\",\"se_11092\",\"se_11093\",\"se_10952\",\"se_11094\",\"se_11137\",\"se_11095\",\"se_26544\",\"se_11096\",\"se_10969\",\"se_10974\",\"se_11057\",\"se_21659\"]},\"research-providers-5\":{\"param\":\"ctbid\",\"values\":[\"se_11139\",\"se_11140\",\"se_11141\",\"se_11142\",\"se_11143\",\"se_10004\",\"se_10008\",\"se_11155\",\"se_10028\",\"se_10029\",\"se_11161\",\"se_11192\",\"se_11199\",\"se_11207\",\"se_41466\",\"se_47715\",\"se_11222\",\"se_11223\",\"se_11225\",\"se_10009\",\"se_11146\",\"se_11147\",\"se_11150\",\"se_11149\",\"se_11151\",\"se_11153\",\"se_11154\",\"se_11157\",\"se_11159\",\"se_11162\",\"se_11163\",\"se_11164\",\"se_11165\",\"se_11166\",\"se_11167\",\"se_11820\",\"se_11170\",\"se_11171\",\"se_11173\",\"se_11174\",\"se_11176\",\"se_11178\",\"se_11179\",\"se_11180\",\"se_11181\",\"se_11183\",\"se_11185\",\"se_11190\",\"se_11188\",\"se_11189\",\"se_10048\",\"se_11193\",\"se_11195\",\"se_11196\",\"se_11197\",\"se_11198\",\"se_40721\",\"se_11202\",\"se_39629\",\"se_11204\",\"se_11205\",\"se_11206\",\"se_11208\",\"se_11209\",\"se_10068\",\"se_11212\",\"se_11213\",\"se_11214\",\"se_11216\",\"se_11218\",\"se_11219\",\"se_30024\",\"se_11220\",\"se_30580\",\"se_11224\",\"se_11242\",\"se_39311\",\"se_11243\",\"se_30424\",\"se_11251\",\"se_47950\",\"se_11252\",\"se_11256\",\"se_11258\",\"se_11259\",\"se_11261\",\"se_11262\",\"se_11264\",\"se_49262\",\"se_11265\",\"se_11266\",\"se_11267\",\"se_11268\",\"se_32895\",\"se_11273\",\"se_11278\",\"se_11279\",\"se_11227\",\"se_31047\",\"se_11228\",\"se_11229\",\"se_39666\",\"se_30874\",\"se_11231\",\"se_40244\",\"se_11234\",\"se_11235\",\"se_11237\",\"se_11238\",\"se_11236\",\"se_11239\",\"se_41727\",\"se_11240\",\"se_11241\",\"se_11244\",\"se_11245\",\"se_11247\",\"se_11248\",\"se_30779\",\"se_47792\",\"se_11255\",\"se_11257\",\"se_43434\",\"se_11269\",\"se_11270\",\"se_48874\",\"se_11272\",\"se_32603\",\"se_11274\",\"se_37397\",\"se_11276\",\"se_11277\",\"se_32140\",\"se_47194\",\"se_11291\",\"se_36840\",\"se_11292\",\"se_34483\",\"se_30811\",\"se_11297\",\"se_37525\",\"se_10197\",\"se_43738\",\"se_11300\",\"se_11302\",\"se_11303\",\"se_11304\",\"se_11305\",\"se_11307\",\"se_34466\",\"se_11322\",\"se_11324\",\"se_11325\",\"se_11326\",\"se_11328\",\"se_35945\",\"se_31102\",\"se_11282\",\"se_11284\",\"se_11285\",\"se_46234\",\"se_11286\",\"se_36559\",\"se_35871\",\"se_11287\",\"se_11290\",\"se_31527\",\"se_11294\",\"se_11295\",\"se_11296\",\"se_30149\",\"se_11827\",\"se_45979\",\"se_44207\",\"se_11299\",\"se_11829\",\"se_11301\",\"se_35545\",\"se_11306\",\"se_11830\",\"se_48418\",\"se_36106\",\"se_32658\",\"se_11311\",\"se_11312\",\"se_11313\",\"se_11314\",\"se_11316\",\"se_11317\",\"se_11318\",\"se_33193\",\"se_11320\",\"se_11321\",\"se_11323\",\"se_11332\",\"se_11334\",\"se_11335\",\"se_11330\",\"se_10259\",\"se_11333\",\"se_11337\",\"se_11338\",\"se_11339\",\"se_47525\",\"se_39112\",\"se_39877\",\"se_11344\",\"se_39309\",\"se_11345\",\"se_11346\",\"se_11348\",\"se_11349\",\"se_30753\",\"se_40208\",\"se_39717\",\"se_34953\",\"se_11376\",\"se_39249\",\"se_11350\",\"se_11351\",\"se_11353\",\"se_11354\",\"se_11355\",\"se_11356\",\"se_11357\",\"se_11358\",\"se_11359\",\"se_11362\",\"se_11363\",\"se_11837\",\"se_11366\",\"se_11367\",\"se_11370\",\"se_46862\",\"se_11372\",\"se_31570\",\"se_11373\",\"se_11374\",\"se_11375\",\"se_41148\",\"se_39571\",\"se_49824\",\"se_31813\",\"se_11380\",\"se_11382\",\"se_11383\",\"se_11384\",\"se_38792\",\"se_48150\",\"se_11398\",\"se_41099\",\"se_49968\",\"se_11401\",\"se_11377\",\"se_11379\",\"se_40432\",\"se_11381\",\"se_30308\",\"se_11390\",\"se_11385\",\"se_11387\",\"se_11391\",\"se_11392\",\"se_45481\",\"se_11394\",\"se_11838\",\"se_11395\",\"se_11397\",\"se_11399\",\"se_11400\",\"se_11402\",\"se_11403\",\"se_48564\",\"se_11405\",\"se_11407\",\"se_11412\",\"se_11428\",\"se_49541\",\"se_11406\",\"se_11408\",\"se_11409\",\"se_11410\",\"se_47781\",\"se_11417\",\"se_45287\",\"se_47231\",\"se_37754\",\"se_11420\",\"se_47033\",\"se_11422\",\"se_11423\",\"se_38748\",\"se_11427\",\"se_11429\",\"se_11439\",\"se_11440\",\"se_11443\",\"se_11444\",\"se_30990\",\"se_11430\",\"se_10424\",\"se_11431\",\"se_11432\",\"se_11433\",\"se_11434\",\"se_11435\",\"se_11436\",\"se_11441\",\"se_11442\",\"se_11445\",\"se_36995\",\"se_11449\",\"se_42428\",\"se_11451\",\"se_41038\",\"se_11452\",\"se_11453\",\"se_11454\",\"se_11455\",\"se_11456\",\"se_11457\",\"se_11458\",\"se_11460\",\"se_11461\",\"se_11463\",\"se_11464\",\"se_11465\",\"se_11467\",\"se_11474\",\"se_11489\",\"se_49170\",\"se_11490\",\"se_11462\",\"se_10472\",\"se_36211\",\"se_11468\",\"se_11471\",\"se_11472\",\"se_43350\",\"se_11473\",\"se_46895\",\"se_11475\",\"se_11476\",\"se_11477\",\"se_11478\",\"se_11481\",\"se_11479\",\"se_47214\",\"se_11482\",\"se_11483\",\"se_11485\",\"se_46707\",\"se_11488\",\"se_35728\",\"se_46118\",\"se_11491\",\"se_11492\",\"se_11493\",\"se_11496\",\"se_11495\",\"se_11500\",\"se_11501\",\"se_43156\",\"se_47580\",\"se_11506\",\"se_41468\",\"se_11497\",\"se_11498\",\"se_11499\",\"se_11503\",\"se_11504\",\"se_11505\",\"se_39234\",\"se_11507\",\"se_11512\",\"se_11514\",\"se_11515\",\"se_39128\",\"se_11516\",\"se_11521\",\"se_11522\",\"se_11523\",\"se_11526\",\"se_11533\",\"se_11534\",\"se_11535\",\"se_46315\",\"se_11509\",\"se_11510\",\"se_11511\",\"se_11518\",\"se_11524\",\"se_11525\",\"se_11527\",\"se_11528\",\"se_33690\",\"se_11530\",\"se_44453\",\"se_11531\",\"se_11850\",\"se_11532\",\"se_31515\",\"se_11536\",\"se_11537\",\"se_43895\",\"se_33841\",\"se_11544\",\"se_11547\",\"se_11550\",\"se_11538\",\"se_11539\",\"se_11542\",\"se_41998\",\"se_31357\",\"se_43424\",\"se_11545\",\"se_11549\",\"se_11553\",\"se_11554\",\"se_11563\",\"se_47199\",\"se_11564\",\"se_11565\",\"se_11578\",\"se_45621\",\"se_11555\",\"se_39716\",\"se_32007\",\"se_11557\",\"se_45872\",\"se_36032\",\"se_11559\",\"se_11560\",\"se_11561\",\"se_31323\",\"se_11562\",\"se_11566\",\"se_11567\",\"se_11568\",\"se_11570\",\"se_11571\",\"se_11574\",\"se_11575\",\"se_45582\",\"se_11577\",\"se_11579\",\"se_11580\",\"se_11581\",\"se_11582\",\"se_11583\",\"se_11585\",\"se_11586\",\"se_11589\",\"se_11590\",\"se_30379\",\"se_11592\",\"se_11596\",\"se_33271\",\"se_10656\",\"se_11605\",\"se_39899\",\"se_11608\",\"se_11610\",\"se_11611\",\"se_47140\",\"se_11612\",\"se_11595\",\"se_48132\",\"se_49332\",\"se_11597\",\"se_11598\",\"se_11599\",\"se_11600\",\"se_10646\",\"se_11602\",\"se_38547\",\"se_11604\",\"se_11606\",\"se_41812\",\"se_44938\",\"se_11609\",\"se_11613\",\"se_11614\",\"se_11623\",\"se_44806\",\"se_11854\",\"se_11616\",\"se_11617\",\"se_11618\",\"se_11619\",\"se_49335\",\"se_11620\",\"se_11621\",\"se_11622\",\"se_45984\",\"se_11855\",\"se_11631\",\"se_46474\",\"se_11649\",\"se_11653\",\"se_11654\",\"se_11655\",\"se_11656\",\"se_11657\",\"se_11624\",\"se_36175\",\"se_40239\",\"se_11626\",\"se_34152\",\"se_11627\",\"se_11628\",\"se_11629\",\"se_11630\",\"se_48316\",\"se_11633\",\"se_11634\",\"se_44955\",\"se_11635\",\"se_44361\",\"se_33348\",\"se_11636\",\"se_32687\",\"se_11639\",\"se_11640\",\"se_11641\",\"se_38538\",\"se_11644\",\"se_10729\",\"se_11647\",\"se_40767\",\"se_11651\",\"se_32240\",\"se_46868\",\"se_48461\",\"se_11857\",\"se_11658\",\"se_11659\",\"se_32979\",\"se_39887\",\"se_11661\",\"se_11662\",\"se_11663\",\"se_11667\",\"se_41742\",\"se_39836\",\"se_11664\",\"se_11668\",\"se_42634\",\"se_39359\",\"se_11670\",\"se_11672\",\"se_42941\",\"se_11674\",\"se_11675\",\"se_11676\",\"se_37966\",\"se_44386\",\"se_11677\",\"se_31606\",\"se_44794\",\"se_11680\",\"se_11681\",\"se_11683\",\"se_45056\",\"se_37957\",\"se_11687\",\"se_33439\",\"se_47401\",\"se_11695\",\"se_11700\",\"se_46428\",\"se_39591\",\"se_11708\",\"se_11713\",\"se_11714\",\"se_11866\",\"se_11715\",\"se_11723\",\"se_11724\",\"se_11725\",\"se_11729\",\"se_11730\",\"se_11739\",\"se_11684\",\"se_11685\",\"se_11686\",\"se_11688\",\"se_11689\",\"se_11691\",\"se_48231\",\"se_11693\",\"se_37900\",\"se_11696\",\"se_11698\",\"se_11701\",\"se_11702\",\"se_11703\",\"se_11704\",\"se_33105\",\"se_32321\",\"se_31742\",\"se_11705\",\"se_11706\",\"se_11707\",\"se_11709\",\"se_11710\",\"se_11711\",\"se_11717\",\"se_11718\",\"se_11720\",\"se_11721\",\"se_11722\",\"se_32076\",\"se_11726\",\"se_11728\",\"se_32834\",\"se_11733\",\"se_30202\",\"se_35753\",\"se_45897\",\"se_11735\",\"se_11736\",\"se_10834\",\"se_11737\",\"se_32744\",\"se_44624\",\"se_11738\",\"se_47785\",\"se_11741\",\"se_42629\",\"se_11745\",\"se_11746\",\"se_11867\",\"se_11748\",\"se_11749\",\"se_11750\",\"se_38711\",\"se_11751\",\"se_41850\",\"se_48726\",\"se_11753\",\"se_11754\",\"se_31879\",\"se_11755\",\"se_37306\",\"se_11757\",\"se_10867\",\"se_40162\",\"se_11759\",\"se_35943\",\"se_11760\",\"se_36324\",\"se_11761\",\"se_11768\",\"se_11769\",\"se_39056\",\"se_11770\",\"se_11772\",\"se_32943\",\"se_11775\",\"se_11776\",\"se_30340\",\"se_11778\",\"se_11780\",\"se_11786\",\"se_11787\",\"se_11788\",\"se_44627\",\"se_11777\",\"se_11779\",\"se_11781\",\"se_11782\",\"se_11783\",\"se_11784\",\"se_37625\",\"se_10903\",\"se_11790\",\"se_30520\",\"se_11802\",\"se_11789\",\"se_11791\",\"se_11792\",\"se_11794\",\"se_11795\",\"se_11796\",\"se_32874\",\"se_11798\",\"se_11799\",\"se_46530\",\"se_11800\",\"se_11801\",\"se_11807\",\"se_11805\",\"se_48458\",\"se_40983\",\"se_11809\",\"se_35394\",\"se_11811\",\"se_47200\",\"se_49378\",\"se_11813\",\"se_11814\",\"se_11815\",\"se_11816\",\"se_11870\",\"se_37237\",\"se_11818\",\"se_11352\",\"se_11388\",\"se_11593\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "a", "{\"ticker\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "ulvr:ln",
						"{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "tsla",
						"{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "uber",
						"{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "aapl",
						"{\"ticker\":{},\"doctype\":{\"ni\":{\"Tags\":{\"param\":\"tags\",\"values\":[\"#earningsrelease\",\"#management\",\"#earningsannounce\",\"#dividend\",\"#acquisition\",\"#partnership\",\"#drug\",\"#stock\",\"#ipo\",\"#stocksplit\",\"#spinoff\",\"#others\"]},\"Source\":{\"param\":\"rep_subjects\",\"values\":[\"gpr\",\"pr\",\"bw\",\"gnw\",\"mw\",\"uk_disc\",\"cnw\",\"nwca\",\"nw\",\"acw\",\"acn\",\"act\",\"abn\",\"fsc\",\"to\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "aapl",
						"{\"ticker\":{},\"doctype\":{\"note\":{\"note-type\":{\"param\":\"note_subtype\",\"values\":[\"note\",\"email\",\"attachment\",\"clipper\",\"starred\",\"plotter\",\"sntclipper\",\"thesis\"]},\"username\":{\"param\":\"note_authors\",\"values\":[\"akash.aggarwal\",\"alap\",\"anurag\",\"arjun.kakkar\",\"ashish.garg\",\"atish.garg\",\"bhaskar\",\"bhuvnesh\",\"dhriti.nogia\",\"gaurav.anand\",\"ishant.mehta\",\"kapil.sardana\",\"malika.jain\",\"navreet\",\"neeti.bhatia\",\"ok.bye\",\"rajeev\",\"rakesh.sadu\",\"sangeeta.nandal\",\"shubham.walia\",\"sonia\",\"tejbir.singh\",\"test.armani\",\"test.dashboard77\",\"test.food\",\"test.march\",\"test.new00\",\"test.thesis01\",\"test.ttt\",\"test.usercheck105\",\"test.usercheck115\",\"test.usercheck119\",\"test.usercheck126\",\"test.zzz\",\"test0000\",\"test4098\",\"test809\",\"tsession\"]},\"note_origin\":{\"param\":\"note_origin\",\"values\":[\"clipper\",\"onenote\",\"sentieo\",\"Sentieo\"]},\"usertags\":{\"param\":\"usertags\",\"values\":[]},\"note_topic\":{\"param\":\"note_topic\",\"values\":[\"Earnings\",\"general\",\"General\",\"Meeting\",\"Sellside\",\"tejbir_test\",\"Transcript\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "aapl",
						"{\"ticker\":{},\"doctype\":{\"reg\":{\"reg\":{\"param\":\"filing_subtype\",\"values\":[\"fda\",\"epa\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "msft",
						"{\"ticker\":{},\"doctype\":{\"reg\":{\"reg\":{\"param\":\"filing_subtype\",\"values\":[\"fda\",\"epa\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "", "{\"ticker\":{\"\":{\"\":{\"param\":\"filter_tickers\",\"values\":[]},\"additional_tickers\":{\"param\":\"filter_tickers\",\"values\":[\"31122\"]}}},\"sector\":{},\"date\":{},\"regions\":{},\"section\":{},\"source\":{},\"language\":{},\"other\":{},\"doctype\":{},\"exchange\":{}}" },

		};

		return groupArray;
	}
	
	
	@DataProvider(name = "test_invalid_filters")
	public Object[][] test_invalid_filters() {
		Object[][] groupArray = null;
		groupArray = new String[][] { {"{\"ticker\":{1234}}"}, {"abcd"}, {"//////"},{"\"ticker\":{},\"doctype\":{\"abcd1234\"},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{},\"neeraj\":{}}"} };

		return groupArray;
	}

	@DataProvider(name = "fetchsearch_nodoc_2")
	public Object[][] fetchsearch_nodoc_2() {
		Object[][] groupArray = null;
		groupArray = new String[][] { {
				"{\"ticker\":{},\"sector\":{},\"regions\":{},\"other\":{},\"language\":{},\"source\":{},\"date\":{},\"section\":{},\"doctype\":{\"ef\":{\"QA\":{\"param\":\"filing_subtype\",\"values\":[\"10-q\",\"10-k\",\"nt 10-q\",\"nt 10-k\",\"10qsb\",\"10ksb\",\"10-q/a\",\"10-k/a\",\"10qsb/a\",\"10ksb/a\"]},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[\"8-k agreement\",\"8-k credit\",\"8-k earnings\",\"8-k mgmt\",\"8-k restruct\",\"8-k update\",\"8-k voting\",\"8-k other\",\"425\",\"s-4\",\"8-k/a\",\"s-4/a\"]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[\"6-k\",\"20-f\",\"40-f\",\"6-k/a\"]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[\"def 14a\",\"defa14a\",\"pre 14a\",\"dfan14a\",\"def 14c\",\"prer14a\",\"pre 14c\",\"497\",\"defr14a\",\"n-px\",\"defm14a\",\"prec14a\",\"prer14c\",\"prem14a\",\"prrn14a\",\"defc14a\",\"prem14c\",\"pren14a\",\"defn14a\",\"defa14c\",\"defr14c\",\"defm14c\",\"dfrn14a\",\"n-px/a\",\"prec14c\",\"defc14c\"]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[\"424b2\",\"424b3\",\"fwp\",\"424b5\",\"s-1/a\",\"pos am\",\"s-3\",\"s-1\",\"8-a12b\",\"s-3asr\",\"s-3/a\",\"424b4\",\"10-12g/a\",\"10-12b/a\",\"f-1\",\"f-3\",\"10-12g\",\"s-11\",\"10-12b\",\"sb-1\"]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[\"sc 13g/a\",\"sc 13g\",\"sc 13d/a\",\"sc 13d\",\"13f-hr\",\"13f-hr/a\"]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[\"s-8\",\"11-k\",\"s-8 pos\"]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[\"25-nse\",\"40-17g\",\"corresp\",\"ct order\",\"d\",\"effect\",\"f-6ef\",\"n-csr\",\"n-csrs\",\"n-q\",\"sc to-c\",\"sc to-i/a\",\"sc to-t/a\",\"sd\",\"suppl\",\"upload\",\"certnys\"]}},\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}}}" }, };

		return groupArray;
	}

	@DataProvider(name = "test_context_Filter")
	public Object[][] test_context_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "msft", "[\"tr.cfo\"]",
				"{\"ticker\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{\"\":{\"10k\":{\"param\":\"sections_filter\",\"values\":[]},\"10q\":{\"param\":\"sections_filter\",\"values\":[]},\"tr\":{\"param\":\"sections_filter\",\"values\":[\"tr.cfo\"]}}}}" },

		};

		return groupArray;
	}

	@DataProvider(name = "fetch_search_term_count")
	public Object[][] fetch_search_term_count() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "msft",
				"{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"param\":\"filing_subtype\",\"values\":[\"10-q\"]},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}",
				"30" }

		};

		return groupArray;
	}

	@DataProvider(name = "doctype_date_filters_combination")
	public Object[][] test_doctype_date_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { {"googl","filing_date:asc", "{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"param\":\"filing_subtype\",\"values\":[\"10-q\",\"10-k\",\"nt 10-q\",\"nt 10-k\",\"10qsb\",\"10ksb\",\"10-q/a\",\"10-k/a\",\"10qsb/a\",\"10ksb/a\"]},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[\"8-k agreement\",\"8-k credit\",\"8-k earnings\",\"8-k mgmt\",\"8-k restruct\",\"8-k update\",\"8-k voting\",\"8-k other\",\"425\",\"s-4\",\"8-k/a\",\"s-4/a\"]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[\"6-k\",\"20-f\",\"40-f\",\"6-k/a\"]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[\"def 14a\",\"defa14a\",\"pre 14a\",\"dfan14a\",\"def 14c\",\"prer14a\",\"pre 14c\",\"497\",\"defr14a\",\"n-px\",\"defm14a\",\"prec14a\",\"prer14c\",\"prem14a\",\"prrn14a\",\"defc14a\",\"prem14c\",\"pren14a\",\"defn14a\",\"defa14c\",\"defr14c\",\"defm14c\",\"dfrn14a\",\"n-px/a\",\"prec14c\",\"defc14c\"]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[\"424b2\",\"424b3\",\"fwp\",\"424b5\",\"s-1/a\",\"pos am\",\"s-3\",\"s-1\",\"8-a12b\",\"s-3asr\",\"s-3/a\",\"424b4\",\"10-12g/a\",\"10-12b/a\",\"f-1\",\"f-3\",\"10-12g\",\"s-11\",\"10-12b\",\"sb-1\"]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[\"sc 13g/a\",\"sc 13g\",\"sc 13d/a\",\"sc 13d\",\"13f-hr\",\"13f-hr/a\"]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[\"s-8\",\"11-k\",\"s-8 pos\"]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[\"form-3\",\"form-4\",\"form-5\",\"form-3/a\",\"form-4/a\",\"form-5/a\"]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[\"1-a\",\"1-a pos\",\"1-a-w\",\"1-a/a\",\"1-e\",\"1-e/a\",\"1-z\",\"10-d\",\"10-d/a\",\"10-k405\",\"10-k405/a\",\"10-kt\",\"10-kt/a\",\"10-qt\",\"10-qt/a\",\"10ksb40\",\"10kt405\",\"10sb12b\",\"10sb12b/a\",\"10sb12g\",\"10sb12g/a\",\"11-k/a\",\"11-kt\",\"11-kt/a\",\"13f-nt\",\"13f-nt/a\",\"144\",\"144/a\",\"15-12b\",\"15-12b/a\",\"15-12g\",\"15-12g/a\",\"15-15d\",\"15-15d/a\",\"15f-12b\",\"15f-12b/a\",\"15f-12g\",\"15f-12g/a\",\"15f-15d\",\"15f-15d/a\",\"18-k\",\"2-e\",\"2-e/a\",\"20-f/a\",\"20fr12b\",\"20fr12b/a\",\"20fr12g\",\"20fr12g/a\",\"24f-2nt\",\"24f-2nt/a\",\"25\",\"25-nse\",\"25-nse/a\",\"25/a\",\"3\",\"305b2\",\"305b2/a\",\"35-cert\",\"35-cert/a\",\"40-17f1\",\"40-17f2\",\"40-17f2/a\",\"40-17g\",\"40-17g/a\",\"40-33\",\"40-6b\",\"40-6b/a\",\"40-8b25\",\"40-8f-2\",\"40-8f-2/a\",\"40-app\",\"40-app/a\",\"40-f/a\",\"40-oip\",\"40-oip/a\",\"40fr12b\",\"40fr12b/a\",\"40fr12g\",\"40fr12g/a\",\"424a\",\"424b1\",\"424b7\",\"424b8\",\"485apos\",\"485bpos\",\"485bxt\",\"486bpos\",\"497ad\",\"497h2\",\"497j\",\"497k\",\"6b ntc\",\"6b ordr\",\"8-a12b/a\",\"8-a12g\",\"8-a12g/a\",\"8-k12b\",\"8-k12b/a\",\"8-k12g3\",\"8-k12g3/a\",\"8-k15d5\",\"abs-15g\",\"abs-15g/a\",\"annlrpt\",\"app ntc\",\"app ordr\",\"app wd\",\"app wd/a\",\"app wdg\",\"ars\",\"ars/a\",\"aw\",\"aw wd\",\"cb\",\"cb/a\",\"corresp\",\"ct order\",\"d\",\"d/a\",\"defs14a\",\"del am\",\"drs\",\"drs/a\",\"drsltr\",\"effect\",\"f-1/a\",\"f-10\",\"f-10/a\",\"f-10ef\",\"f-10pos\",\"f-1mef\",\"f-2\",\"f-2/a\",\"f-3/a\",\"f-3asr\",\"f-3d\",\"f-3dpos\",\"f-3mef\",\"f-4\",\"f-4 pos\",\"f-4/a\",\"f-4mef\",\"f-6\",\"f-6 pos\",\"f-6/a\",\"f-6ef\",\"f-7\",\"f-7/a\",\"f-8\",\"f-8 pos\",\"f-8/a\",\"f-80\",\"f-80/a\",\"f-80pos\",\"f-9\",\"f-9 pos\",\"f-9/a\",\"f-9ef\",\"f-n\",\"f-n/a\",\"f-x\",\"f-x/a\",\"irannotice\",\"n-1/a\",\"n-14\",\"n-14 8c\",\"n-14 8c/a\",\"n-14/a\",\"n-14ae\",\"n-14ae/a\",\"n-14mef\",\"n-18f1\",\"n-1a\",\"n-1a/a\",\"n-2\",\"n-2/a\",\"n-23c-1\",\"n-23c-2\",\"n-23c-2/a\",\"n-23c3a\",\"n-23c3a/a\",\"n-2mef\",\"n-30b-2\",\"n-30d\",\"n-30d/a\",\"n-54a\",\"n-54c\",\"n-54c/a\",\"n-6f\",\"n-6f/a\",\"n-8a\",\"n-8a/a\",\"n-8f\",\"n-8f ntc\",\"n-8f/a\",\"n-csr\",\"n-csr/a\",\"n-csrs\",\"n-csrs/a\",\"n-mfp\",\"n-mfp/a\",\"n-mfp1\",\"n-q\",\"n-q/a\",\"nsar-a\",\"nsar-b\",\"nsar-u\",\"nsar-u/a\",\"nt 10-k/a\",\"nt 10-q/a\",\"nt 11-k\",\"nt 11-k/a\",\"nt 15d2\",\"nt 20-f\",\"nt 20-f/a\",\"nt-ncsr\",\"nt-nsar\",\"nt-nsar/a\",\"ntfnsar\",\"ntn 10k\",\"ntn 10q\",\"ntn 20f\",\"pos 8c\",\"pos amc\",\"pos ami\",\"pos ex\",\"pos462b\",\"pos462c\",\"posasr\",\"pres14a\",\"px14a6g\",\"px14a6n\",\"qualif\",\"regdex/a\",\"revoked\",\"rw\",\"rw wd\",\"s-11/a\",\"s-11mef\",\"s-1mef\",\"s-2\",\"s-2/a\",\"s-2mef\",\"s-3d\",\"s-3dpos\",\"s-3mef\",\"s-4 pos\",\"s-4ef\",\"s-4ef/a\",\"s-4mef\",\"s-b\",\"s-b/a\",\"sb-1/a\",\"sb-2\",\"sb-2/a\",\"sb-2mef\",\"sc 13e1\",\"sc 13e1/a\",\"sc 13e3\",\"sc 13e3/a\",\"sc 14d9\",\"sc 14d9/a\",\"sc 14f1\",\"sc 14f1/a\",\"sc to-c\",\"sc to-i\",\"sc to-i/a\",\"sc to-t\",\"sc to-t/a\",\"sc13e4f\",\"sc13e4f/a\",\"sc14d1f\",\"sc14d1f/a\",\"sc14d9c\",\"sc14d9f\",\"sc14d9f/a\",\"sd\",\"sd/a\",\"sp 15d2\",\"sp 15d2/a\",\"suppl\",\"t-3\",\"t-3/a\",\"u-1\",\"u-1/a\",\"u-12-ia\",\"u-12-ib\",\"u-13-60\",\"u-13-60/a\",\"u-33-s\",\"u-3a-2\",\"u-3a-2/a\",\"u-57\",\"u-57/a\",\"u-6b-2\",\"u-9c-3\",\"u-9c-3/a\",\"u5a\",\"u5a/a\",\"u5b\",\"u5s\",\"u5s/a\",\"under\",\"upload\",\"certnys\",\"no act\",\"certpac\",\"13fconp\",\"g-405n\",\"u-3a3-1\",\"sc 14d1\",\"ttw\",\"485a24f\",\"485a24e\",\"39-304d\",\"1-a-w/a\",\"n-cr/a\",\"19b-4\",\"26\",\"adv-h-c\",\"pre13e3\",\"s-6\",\"prea14a\",\"defs14c\",\"adv-h-t\",\"msd/a\",\"advco\",\"sc 14d1/a\",\"ta-2\",\"ta-1\",\"s-20\",\"f-7 pos\",\"g-fin/a\",\"cfportal\",\"reg-nr/a\",\"msdco\",\"13f-e/a\",\"qrtlyrpt/a\",\"13fconp/a\",\"g-finw\",\"40-8f-m\",\"8a12bt\",\"annlrpt/a\",\"ta-1/a\",\"focusn/a\",\"485b24f\",\"485b24e\",\"focusn\",\"1-z/a\",\"ma/a\",\"24f-1\",\"afdb/a\",\"ifc\",\"n14el24\",\"ta-w\",\"sf-1\",\"sf-3\",\"adv-e\",\"n-4 el/a\",\"dstrbrpt\",\"n-4 el\",\"40-24b2/a\",\"ebrd\",\"1-e ad\",\"8-m\",\"sf-1/a\",\"n-23c3c\",\"n-23c3b\",\"40-202a/a\",\"u-13e-1\",\"s-6el24/a\",\"n-23c3b/a\",\"n-cr\",\"c-w\",\"40-202a\",\"10-c/a\",\"u-7d\",\"u-6b-2/a\",\"n-23c-1/a\",\"n14ae24\",\"x-17a-5\",\"u5b/a\",\"s-6/a\",\"s-3d/a\",\"nrsro-ce/a\",\"pres14c\",\"nt n-mfp\",\"c\",\"10-qsb\",\"n-8b-2\",\"ma-w\",\"cfportal/a\",\"ma-i\",\"ma-a\",\"ma-i/a\",\"n-18f1/a\",\"nt 10-d/a\",\"24f-2el\",\"dstrbrpt/a\",\"253g2\",\"n-1a el/a\",\"253g1\",\"8-b12g\",\"2-a\",\"8-b12b\",\"nrsro-upd\",\"n-6/a\",\"497k1\",\"497k2\",\"dosltr\",\"n-1a el\",\"s-20/a\",\"10-c\",\"40-33/a\",\"c/a\",\"adb\",\"msd\",\"9-m\",\"nt 10-d\",\"n-6\",\"n-4\",\"bdco\",\"40-8f-b\",\"sdr\",\"40-8f-l/a\",\"8-k15d5/a\",\"40-8f-l\",\"u-12-ib/a\",\"x-17a-5/a\",\"def13e3/a\",\"advw\",\"13f-e\",\"sc 13e4/a\",\"8-b12b/a\",\"ma\",\"dos/a\",\"bw-2\",\"bw-3\",\"40-8f-m/a\",\"u-12-ia/a\",\"2-a/a\",\"n-23c3c/a\",\"s-6el24\",\"8a12bt/a\",\"s-8/a\",\"8-b12g/a\",\"iadb\",\"8a12bef\",\"n-3\",\"40-203a\",\"ttw/a\",\"40-8f-a\",\"afdb\",\"497k3a\",\"497k3b\",\"424h/a\",\"nt n-mfp1\",\"u-7d/a\",\"40-24b2\",\"sc 13e4\",\"n14el24/a\",\"n-mfp1/a\",\"s-bmef\",\"g-fin\",\"def-oc\",\"n-27d-1\",\"pre13e3/a\",\"n-8b-2/a\",\"adv-nr\",\"n-3/a\",\"424h\",\"taco\",\"487\",\"ebrd/a\",\"40-17gcs\",\"nt 15d2/a\",\"n-4/a\",\"18-12b\",\"def13e3\",\"sf-3/a\",\"24f-2tm\",\"40-8f-a/a\",\"reg-nr\",\"ta-2/a\",\"sl\",\"dos\",\"40-17f1/a\",\"486apos\",\"ntn 10d\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"5y\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"vod","filing_date:asc", "{\"ticker\":{},\"doctype\":{\"rr\":{\"comrep_types\":{\"param\":\"rr_reasons\",\"values\":[\"rr_reasons_3\",\"rr_reasons_27\",\"rr_reasons_5\",\"rr_reasons_7\",\"rr_reasons_8\",\"rr_reasons_2\",\"rr_reasons_6\",\"rr_reasons_1\",\"rr_reasons_12\",\"rr_reasons_13\",\"rr_reasons_15\",\"rr_reasons_17\",\"rr_reasons_19\",\"rr_reasons_14\",\"rr_reasons_9\"]},\"othrep_types\":{\"param\":\"rr_styles\",\"values\":[\"rr_styles_1\",\"rr_styles_2\",\"rr_styles_3\",\"rr_styles_4\",\"rr_styles_5\",\"rr_styles_6\",\"rr_styles_7\",\"rr_styles_8\",\"rr_styles_9\"]},\"research-providers-1\":{\"param\":\"ctbid\",\"values\":[\"se_11097\",\"se_11831\",\"se_11835\",\"se_11840\",\"se_11103\",\"se_10619\",\"se_10895\"]},\"research-providers-3\":{\"param\":\"ctbid\",\"values\":[\"se_11821\",\"se_11099\",\"se_11823\",\"se_11098\",\"se_11828\",\"se_11825\",\"se_11826\",\"se_11111\",\"se_19010\",\"se_11102\",\"se_11115\",\"se_49969\",\"se_11116\",\"se_11846\",\"se_11847\",\"se_11849\",\"se_11104\",\"se_11118\",\"se_11851\",\"se_11122\",\"se_11105\",\"se_11125\",\"se_11856\",\"se_11858\",\"se_11860\",\"se_11859\",\"se_10782\",\"se_11864\",\"se_11131\",\"se_11106\",\"se_11107\",\"se_11132\",\"se_11133\",\"se_11135\",\"se_11108\",\"se_11136\"]},\"research-providers-4\":{\"param\":\"ctbid\",\"values\":[\"se_10003\",\"se_10976\",\"se_10005\",\"se_26207\",\"se_10057\",\"se_10975\",\"se_10006\",\"se_22076\",\"se_10024\",\"se_10033\",\"se_10035\",\"se_27030\",\"se_21511\",\"se_10977\",\"se_10978\",\"se_10047\",\"se_10062\",\"se_10979\",\"se_10980\",\"se_10981\",\"se_36482\",\"se_29804\",\"se_10982\",\"se_10983\",\"se_10984\",\"se_10083\",\"se_10986\",\"se_10119\",\"se_10993\",\"se_11824\",\"se_11100\",\"se_10166\",\"se_10987\",\"se_11822\",\"se_25087\",\"se_10988\",\"se_10989\",\"se_24851\",\"se_10125\",\"se_10990\",\"se_10991\",\"se_10992\",\"se_27298\",\"se_32905\",\"se_10138\",\"se_10995\",\"se_20467\",\"se_10151\",\"se_11109\",\"se_10996\",\"se_11110\",\"se_10165\",\"se_10997\",\"se_28681\",\"se_11001\",\"se_10221\",\"se_11003\",\"se_23375\",\"se_10998\",\"se_10999\",\"se_11000\",\"se_22857\",\"se_34639\",\"se_28602\",\"se_11002\",\"se_10203\",\"se_10208\",\"se_24001\",\"se_25387\",\"se_23000\",\"se_10233\",\"se_11004\",\"se_11005\",\"se_11006\",\"se_11007\",\"se_11008\",\"se_11009\",\"se_11010\",\"se_11011\",\"se_11113\",\"se_11015\",\"se_11016\",\"se_11017\",\"se_34845\",\"se_11832\",\"se_11012\",\"se_11013\",\"se_11014\",\"se_11834\",\"se_27805\",\"se_11018\",\"se_11019\",\"se_11021\",\"se_36696\",\"se_10292\",\"se_11020\",\"se_24110\",\"se_11836\",\"se_10301\",\"se_10303\",\"se_27852\",\"se_10309\",\"se_10311\",\"se_10314\",\"se_11022\",\"se_11023\",\"se_11024\",\"se_11025\",\"se_27424\",\"se_11026\",\"se_11027\",\"se_11030\",\"se_11028\",\"se_11031\",\"se_10367\",\"se_11033\",\"se_11034\",\"se_10386\",\"se_11035\",\"se_10389\",\"se_29829\",\"se_11839\",\"se_11036\",\"se_11037\",\"se_10410\",\"se_10412\",\"se_11842\",\"se_10428\",\"se_11843\",\"se_11038\",\"se_11039\",\"se_10427\",\"se_11040\",\"se_37430\",\"se_10433\",\"se_11844\",\"se_11042\",\"se_10478\",\"se_11043\",\"se_10494\",\"se_11845\",\"se_22108\",\"se_11044\",\"se_38697\",\"se_20499\",\"se_11045\",\"se_10514\",\"se_34189\",\"se_21812\",\"se_10521\",\"se_11848\",\"se_11046\",\"se_11047\",\"se_11048\",\"se_11049\",\"se_27775\",\"se_10543\",\"se_11050\",\"se_10546\",\"se_11051\",\"se_11052\",\"se_25363\",\"se_11053\",\"se_11054\",\"se_11119\",\"se_10624\",\"se_11055\",\"se_11056\",\"se_11117\",\"se_10600\",\"se_10601\",\"se_11058\",\"se_25667\",\"se_10615\",\"se_11120\",\"se_11060\",\"se_10620\",\"se_11852\",\"se_10622\",\"se_11061\",\"se_35085\",\"se_10641\",\"se_11121\",\"se_10640\",\"se_10645\",\"se_10648\",\"se_11123\",\"se_10660\",\"se_10661\",\"se_10665\",\"se_11124\",\"se_10668\",\"se_11062\",\"se_11063\",\"se_25984\",\"se_11064\",\"se_10677\",\"se_10679\",\"se_10688\",\"se_10716\",\"se_27572\",\"se_32642\",\"se_10699\",\"se_11065\",\"se_27795\",\"se_11066\",\"se_10707\",\"se_26017\",\"se_11067\",\"se_29738\",\"se_11068\",\"se_28853\",\"se_25891\",\"se_11070\",\"se_10746\",\"se_11071\",\"se_11861\",\"se_24652\",\"se_10749\",\"se_22815\",\"se_11127\",\"se_11072\",\"se_10767\",\"se_38956\",\"se_33901\",\"se_27738\",\"se_24608\",\"se_10781\",\"se_11130\",\"se_11075\",\"se_11078\",\"se_11081\",\"se_11865\",\"se_11862\",\"se_11076\",\"se_11863\",\"se_11077\",\"se_11080\",\"se_37889\",\"se_10801\",\"se_11082\",\"se_11083\",\"se_11084\",\"se_11085\",\"se_10844\",\"se_10850\",\"se_11868\",\"se_11087\",\"se_20275\",\"se_27374\",\"se_11134\",\"se_10868\",\"se_24602\",\"se_10876\",\"se_27634\",\"se_10891\",\"se_26372\",\"se_11089\",\"se_11088\",\"se_11869\",\"se_11090\",\"se_11091\",\"se_10919\",\"se_10926\",\"se_10927\",\"se_36000\",\"se_11092\",\"se_11093\",\"se_10952\",\"se_11094\",\"se_11137\",\"se_11095\",\"se_26544\",\"se_11096\",\"se_10969\",\"se_10974\",\"se_11057\",\"se_21659\"]},\"research-providers-5\":{\"param\":\"ctbid\",\"values\":[\"se_11139\",\"se_11140\",\"se_11141\",\"se_11142\",\"se_11143\",\"se_10004\",\"se_10008\",\"se_11155\",\"se_10028\",\"se_10029\",\"se_11161\",\"se_11192\",\"se_11199\",\"se_11207\",\"se_41466\",\"se_47715\",\"se_11222\",\"se_11223\",\"se_11225\",\"se_10009\",\"se_11146\",\"se_11147\",\"se_11150\",\"se_11149\",\"se_11151\",\"se_11153\",\"se_11154\",\"se_11157\",\"se_11159\",\"se_11162\",\"se_11163\",\"se_11164\",\"se_11165\",\"se_11166\",\"se_11167\",\"se_11820\",\"se_11170\",\"se_11171\",\"se_11173\",\"se_11174\",\"se_11176\",\"se_11178\",\"se_11179\",\"se_11180\",\"se_11181\",\"se_11183\",\"se_11185\",\"se_11190\",\"se_11188\",\"se_11189\",\"se_10048\",\"se_11193\",\"se_11195\",\"se_11196\",\"se_11197\",\"se_11198\",\"se_40721\",\"se_11202\",\"se_39629\",\"se_11204\",\"se_11205\",\"se_11206\",\"se_11208\",\"se_11209\",\"se_10068\",\"se_11212\",\"se_11213\",\"se_11214\",\"se_11216\",\"se_11218\",\"se_11219\",\"se_30024\",\"se_11220\",\"se_30580\",\"se_11224\",\"se_11242\",\"se_39311\",\"se_11243\",\"se_30424\",\"se_11251\",\"se_47950\",\"se_11252\",\"se_11256\",\"se_11258\",\"se_11259\",\"se_11261\",\"se_11262\",\"se_11264\",\"se_49262\",\"se_11265\",\"se_11266\",\"se_11267\",\"se_11268\",\"se_32895\",\"se_11273\",\"se_11278\",\"se_11279\",\"se_11227\",\"se_31047\",\"se_11228\",\"se_11229\",\"se_39666\",\"se_30874\",\"se_11231\",\"se_40244\",\"se_11234\",\"se_11235\",\"se_11237\",\"se_11238\",\"se_11236\",\"se_11239\",\"se_41727\",\"se_11240\",\"se_11241\",\"se_11244\",\"se_11245\",\"se_11247\",\"se_11248\",\"se_30779\",\"se_47792\",\"se_11255\",\"se_11257\",\"se_43434\",\"se_11269\",\"se_11270\",\"se_48874\",\"se_11272\",\"se_32603\",\"se_11274\",\"se_37397\",\"se_11276\",\"se_11277\",\"se_32140\",\"se_47194\",\"se_11291\",\"se_36840\",\"se_11292\",\"se_34483\",\"se_30811\",\"se_11297\",\"se_37525\",\"se_10197\",\"se_43738\",\"se_11300\",\"se_11302\",\"se_11303\",\"se_11304\",\"se_11305\",\"se_11307\",\"se_34466\",\"se_11322\",\"se_11324\",\"se_11325\",\"se_11326\",\"se_11328\",\"se_35945\",\"se_31102\",\"se_11282\",\"se_11284\",\"se_11285\",\"se_46234\",\"se_11286\",\"se_36559\",\"se_35871\",\"se_11287\",\"se_11290\",\"se_31527\",\"se_11294\",\"se_11295\",\"se_11296\",\"se_30149\",\"se_11827\",\"se_45979\",\"se_44207\",\"se_11299\",\"se_11829\",\"se_11301\",\"se_35545\",\"se_11306\",\"se_11830\",\"se_48418\",\"se_36106\",\"se_32658\",\"se_11311\",\"se_11312\",\"se_11313\",\"se_11314\",\"se_11316\",\"se_11317\",\"se_11318\",\"se_33193\",\"se_11320\",\"se_11321\",\"se_11323\",\"se_11332\",\"se_11334\",\"se_11335\",\"se_11330\",\"se_10259\",\"se_11333\",\"se_11337\",\"se_11338\",\"se_11339\",\"se_47525\",\"se_39112\",\"se_39877\",\"se_11344\",\"se_39309\",\"se_11345\",\"se_11346\",\"se_11348\",\"se_11349\",\"se_30753\",\"se_40208\",\"se_39717\",\"se_34953\",\"se_11376\",\"se_39249\",\"se_11350\",\"se_11351\",\"se_11353\",\"se_11354\",\"se_11355\",\"se_11356\",\"se_11357\",\"se_11358\",\"se_11359\",\"se_11362\",\"se_11363\",\"se_11837\",\"se_11366\",\"se_11367\",\"se_11370\",\"se_46862\",\"se_11372\",\"se_31570\",\"se_11373\",\"se_11374\",\"se_11375\",\"se_41148\",\"se_39571\",\"se_49824\",\"se_31813\",\"se_11380\",\"se_11382\",\"se_11383\",\"se_11384\",\"se_38792\",\"se_48150\",\"se_11398\",\"se_41099\",\"se_49968\",\"se_11401\",\"se_11377\",\"se_11379\",\"se_40432\",\"se_11381\",\"se_30308\",\"se_11390\",\"se_11385\",\"se_11387\",\"se_11391\",\"se_11392\",\"se_45481\",\"se_11394\",\"se_11838\",\"se_11395\",\"se_11397\",\"se_11399\",\"se_11400\",\"se_11402\",\"se_11403\",\"se_48564\",\"se_11405\",\"se_11407\",\"se_11412\",\"se_11428\",\"se_49541\",\"se_11406\",\"se_11408\",\"se_11409\",\"se_11410\",\"se_47781\",\"se_11417\",\"se_45287\",\"se_47231\",\"se_37754\",\"se_11420\",\"se_47033\",\"se_11422\",\"se_11423\",\"se_38748\",\"se_11427\",\"se_11429\",\"se_11439\",\"se_11440\",\"se_11443\",\"se_11444\",\"se_30990\",\"se_11430\",\"se_10424\",\"se_11431\",\"se_11432\",\"se_11433\",\"se_11434\",\"se_11435\",\"se_11436\",\"se_11441\",\"se_11442\",\"se_11445\",\"se_36995\",\"se_11449\",\"se_42428\",\"se_11451\",\"se_41038\",\"se_11452\",\"se_11453\",\"se_11454\",\"se_11455\",\"se_11456\",\"se_11457\",\"se_11458\",\"se_11460\",\"se_11461\",\"se_11463\",\"se_11464\",\"se_11465\",\"se_11467\",\"se_11474\",\"se_11489\",\"se_49170\",\"se_11490\",\"se_11462\",\"se_10472\",\"se_36211\",\"se_11468\",\"se_11471\",\"se_11472\",\"se_43350\",\"se_11473\",\"se_46895\",\"se_11475\",\"se_11476\",\"se_11477\",\"se_11478\",\"se_11481\",\"se_11479\",\"se_47214\",\"se_11482\",\"se_11483\",\"se_11485\",\"se_46707\",\"se_11488\",\"se_35728\",\"se_46118\",\"se_11491\",\"se_11492\",\"se_11493\",\"se_11496\",\"se_11495\",\"se_11500\",\"se_11501\",\"se_43156\",\"se_47580\",\"se_11506\",\"se_41468\",\"se_11497\",\"se_11498\",\"se_11499\",\"se_11503\",\"se_11504\",\"se_11505\",\"se_39234\",\"se_11507\",\"se_11512\",\"se_11514\",\"se_11515\",\"se_39128\",\"se_11516\",\"se_11521\",\"se_11522\",\"se_11523\",\"se_11526\",\"se_11533\",\"se_11534\",\"se_11535\",\"se_46315\",\"se_11509\",\"se_11510\",\"se_11511\",\"se_11518\",\"se_11524\",\"se_11525\",\"se_11527\",\"se_11528\",\"se_33690\",\"se_11530\",\"se_44453\",\"se_11531\",\"se_11850\",\"se_11532\",\"se_31515\",\"se_11536\",\"se_11537\",\"se_43895\",\"se_33841\",\"se_11544\",\"se_11547\",\"se_11550\",\"se_11538\",\"se_11539\",\"se_11542\",\"se_41998\",\"se_31357\",\"se_43424\",\"se_11545\",\"se_11549\",\"se_11553\",\"se_11554\",\"se_11563\",\"se_47199\",\"se_11564\",\"se_11565\",\"se_11578\",\"se_45621\",\"se_11555\",\"se_39716\",\"se_32007\",\"se_11557\",\"se_45872\",\"se_36032\",\"se_11559\",\"se_11560\",\"se_11561\",\"se_31323\",\"se_11562\",\"se_11566\",\"se_11567\",\"se_11568\",\"se_11570\",\"se_11571\",\"se_11574\",\"se_11575\",\"se_45582\",\"se_11577\",\"se_11579\",\"se_11580\",\"se_11581\",\"se_11582\",\"se_11583\",\"se_11585\",\"se_11586\",\"se_11589\",\"se_11590\",\"se_30379\",\"se_11592\",\"se_11596\",\"se_33271\",\"se_10656\",\"se_11605\",\"se_39899\",\"se_11608\",\"se_11610\",\"se_11611\",\"se_47140\",\"se_11612\",\"se_11595\",\"se_48132\",\"se_49332\",\"se_11597\",\"se_11598\",\"se_11599\",\"se_11600\",\"se_10646\",\"se_11602\",\"se_38547\",\"se_11604\",\"se_11606\",\"se_41812\",\"se_44938\",\"se_11609\",\"se_11613\",\"se_11614\",\"se_11623\",\"se_44806\",\"se_11854\",\"se_11616\",\"se_11617\",\"se_11618\",\"se_11619\",\"se_49335\",\"se_11620\",\"se_11621\",\"se_11622\",\"se_45984\",\"se_11855\",\"se_11631\",\"se_46474\",\"se_11649\",\"se_11653\",\"se_11654\",\"se_11655\",\"se_11656\",\"se_11657\",\"se_11624\",\"se_36175\",\"se_40239\",\"se_11626\",\"se_34152\",\"se_11627\",\"se_11628\",\"se_11629\",\"se_11630\",\"se_48316\",\"se_11633\",\"se_11634\",\"se_44955\",\"se_11635\",\"se_44361\",\"se_33348\",\"se_11636\",\"se_32687\",\"se_11639\",\"se_11640\",\"se_11641\",\"se_38538\",\"se_11644\",\"se_10729\",\"se_11647\",\"se_40767\",\"se_11651\",\"se_32240\",\"se_46868\",\"se_48461\",\"se_11857\",\"se_11658\",\"se_11659\",\"se_32979\",\"se_39887\",\"se_11661\",\"se_11662\",\"se_11663\",\"se_11667\",\"se_41742\",\"se_39836\",\"se_11664\",\"se_11668\",\"se_42634\",\"se_39359\",\"se_11670\",\"se_11672\",\"se_42941\",\"se_11674\",\"se_11675\",\"se_11676\",\"se_37966\",\"se_44386\",\"se_11677\",\"se_31606\",\"se_44794\",\"se_11680\",\"se_11681\",\"se_11683\",\"se_45056\",\"se_37957\",\"se_11687\",\"se_33439\",\"se_47401\",\"se_11695\",\"se_11700\",\"se_46428\",\"se_39591\",\"se_11708\",\"se_11713\",\"se_11714\",\"se_11866\",\"se_11715\",\"se_11723\",\"se_11724\",\"se_11725\",\"se_11729\",\"se_11730\",\"se_11739\",\"se_11684\",\"se_11685\",\"se_11686\",\"se_11688\",\"se_11689\",\"se_11691\",\"se_48231\",\"se_11693\",\"se_37900\",\"se_11696\",\"se_11698\",\"se_11701\",\"se_11702\",\"se_11703\",\"se_11704\",\"se_33105\",\"se_32321\",\"se_31742\",\"se_11705\",\"se_11706\",\"se_11707\",\"se_11709\",\"se_11710\",\"se_11711\",\"se_11717\",\"se_11718\",\"se_11720\",\"se_11721\",\"se_11722\",\"se_32076\",\"se_11726\",\"se_11728\",\"se_32834\",\"se_11733\",\"se_30202\",\"se_35753\",\"se_45897\",\"se_11735\",\"se_11736\",\"se_10834\",\"se_11737\",\"se_32744\",\"se_44624\",\"se_11738\",\"se_47785\",\"se_11741\",\"se_42629\",\"se_11745\",\"se_11746\",\"se_11867\",\"se_11748\",\"se_11749\",\"se_11750\",\"se_38711\",\"se_11751\",\"se_41850\",\"se_48726\",\"se_11753\",\"se_11754\",\"se_31879\",\"se_11755\",\"se_37306\",\"se_11757\",\"se_10867\",\"se_40162\",\"se_11759\",\"se_35943\",\"se_11760\",\"se_36324\",\"se_11761\",\"se_11768\",\"se_11769\",\"se_39056\",\"se_11770\",\"se_11772\",\"se_32943\",\"se_11775\",\"se_11776\",\"se_30340\",\"se_11778\",\"se_11780\",\"se_11786\",\"se_11787\",\"se_11788\",\"se_44627\",\"se_11777\",\"se_11779\",\"se_11781\",\"se_11782\",\"se_11783\",\"se_11784\",\"se_37625\",\"se_10903\",\"se_11790\",\"se_30520\",\"se_11802\",\"se_11789\",\"se_11791\",\"se_11792\",\"se_11794\",\"se_11795\",\"se_11796\",\"se_32874\",\"se_11798\",\"se_11799\",\"se_46530\",\"se_11800\",\"se_11801\",\"se_11807\",\"se_11805\",\"se_48458\",\"se_40983\",\"se_11809\",\"se_35394\",\"se_11811\",\"se_47200\",\"se_49378\",\"se_11813\",\"se_11814\",\"se_11815\",\"se_11816\",\"se_11870\",\"se_37237\",\"se_11818\",\"se_11352\",\"se_11388\",\"se_11593\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"3y\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"a","filing_date:asc", "{\"ticker\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"10y\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"ulvr:ln","filing_date:asc", "{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"2y\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"tsla","filing_date:desc", "{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"3m\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"uber","filing_date:desc", "{\"ticker\":{},\"doctype\":{\"nw\":{\"media_news\":{\"param\":\"source\",\"values\":[\"lx_175892\",\"lx_3588\",\"lx_192511\",\"lx_se_6598\",\"lx_se_4828\",\"lx_5230\",\"lx_7484\",\"lx_190697\",\"lx_se_8057\",\"lx_9295\",\"lx_se_9571\",\"lx_se_9682\",\"lx_67811\",\"lx_se_109488\",\"lx_91272\",\"lx_76706\",\"lx_12827\",\"lx_13418\",\"lx_se_14584\",\"lx_se_15335\",\"lx_se_48509\",\"lx_se_78351\",\"lx_se_19013\",\"lx_se_74038\",\"lx_se_82122\",\"lx_se_23458\",\"lx_se_23459\",\"lx_26178\",\"lx_26180\",\"lx_30491\",\"lx_se_132321\",\"lx_se_32525\",\"lx_85876\",\"lx_33552\",\"lx_87763\",\"lx_91334\",\"lx_se_34662\",\"lx_se_35349\",\"lx_se_48775\",\"lx_103480\",\"lx_se_38938\",\"lx_39578\",\"lx_238545\",\"lx_se_43363\",\"lx_se_44030\",\"lx_se_7553\",\"lx_se_45630\",\"lx_90965\",\"lx_91071\",\"lx_48113\",\"lx_49703\",\"lx_se_51611\",\"lx_se_51638\",\"lx_se_52922\",\"lx_54812\"]},\"cons_disc\":{\"param\":\"source\",\"values\":[\"lx_se_1110\",\"lx_se_1154\",\"lx_se_4171\",\"lx_se_4237\",\"lx_se_4249\",\"lx_se_9227\",\"lx_73596\",\"lx_se_11080\",\"lx_76086\",\"lx_se_11530\",\"lx_se_118419\",\"lx_se_11595\",\"lx_85698\",\"lx_112942\",\"lx_se_23192\",\"lx_se_83778\",\"lx_se_31465\",\"lx_226571\",\"lx_226562\",\"lx_226567\",\"lx_207252\",\"lx_226606\",\"lx_226613\",\"lx_226608\",\"lx_226570\",\"lx_223836\",\"lx_228904\",\"lx_se_88233\"]},\"cons_stpl\":{\"param\":\"source\",\"values\":[\"lx_1306\",\"lx_104677\",\"lx_se_1449\",\"lx_113125\",\"lx_se_5763\",\"lx_se_8774\",\"lx_se_9226\",\"lx_se_11595\",\"lx_106069\",\"lx_se_13320\",\"lx_se_14709\",\"lx_se_17855\",\"lx_se_17864\",\"lx_80937\",\"lx_se_80254\",\"lx_se_18921\",\"lx_104820\",\"lx_se_80680\",\"lx_se_21621\",\"lx_se_21625\",\"lx_se_23334\",\"lx_se_83847\",\"lx_85572\",\"lx_se_34650\",\"lx_se_39114\",\"lx_118069\",\"lx_90036\",\"lx_se_45762\"]},\"energy_utils\":{\"param\":\"source\",\"values\":[\"lx_71636\",\"lx_se_114629\",\"lx_se_3063\",\"lx_75753\",\"lx_se_12712\",\"lx_79011\",\"lx_se_189850\",\"lx_104804\",\"lx_79307\",\"lx_80652\",\"lx_se_121906\",\"lx_22266\",\"lx_82850\",\"lx_104856\",\"lx_33301\",\"lx_se_85705\",\"lx_36067\",\"lx_se_56717\",\"lx_87791\",\"lx_88345\",\"lx_88348\",\"lx_107381\",\"lx_se_39180\",\"lx_se_39907\",\"lx_se_90009\",\"lx_90047\",\"lx_se_49941\",\"lx_106757\",\"lx_92959\",\"lx_92928\",\"lx_180036\",\"lx_se_101010\",\"lx_se_54470\"]},\"fin_reale\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_104677\",\"lx_se_2380\",\"lx_se_72246\",\"lx_se_108300\",\"lx_se_4032\",\"lx_se_4870\",\"lx_se_48362\",\"lx_se_5597\",\"lx_se_64349\",\"lx_se_6491\",\"lx_se_7290\",\"lx_se_8649\",\"lx_se_58171\",\"lx_73596\",\"lx_10574\",\"lx_se_12030\",\"lx_se_76405\",\"lx_se_76686\",\"lx_77578\",\"lx_80122\",\"lx_se_80432\",\"lx_18501\",\"lx_se_19013\",\"lx_se_63339\",\"lx_se_24808\",\"lx_se_25073\",\"lx_se_25071\",\"lx_se_25105\",\"lx_se_25556\",\"lx_se_25561\",\"lx_se_83596\",\"lx_85205\",\"lx_se_32322\",\"lx_33727\",\"lx_se_33414\",\"lx_se_87630\",\"lx_se_34693\",\"lx_se_57457\",\"lx_se_62506\",\"lx_104990\",\"lx_se_38617\",\"lx_se_39907\",\"lx_se_105002\",\"lx_se_40306\",\"lx_88861\",\"lx_100641\",\"lx_se_40431\",\"lx_se_89439\",\"lx_se_105014\",\"lx_se_90024\",\"lx_44150\",\"lx_91081\",\"lx_44795\",\"lx_223395\",\"lx_se_49091\",\"lx_se_82674\",\"lx_se_49865\",\"lx_se_53132\"]},\"health_care\":{\"param\":\"source\",\"values\":[\"lx_se_2455\",\"lx_se_72230\",\"lx_se_6009\",\"lx_se_5965\",\"lx_5972\",\"lx_se_9226\",\"lx_se_48442\",\"lx_75585\",\"lx_se_75754\",\"lx_se_11573\",\"lx_77822\",\"lx_77827\",\"lx_14713\",\"lx_se_79268\",\"lx_se_17999\",\"lx_se_18252\",\"lx_se_18249\",\"lx_se_61329\",\"lx_20447\",\"lx_22458\",\"lx_se_82238\",\"lx_se_22508\",\"lx_83276\",\"lx_83451\",\"lx_133239\",\"lx_se_24954\",\"lx_82874\",\"lx_85635\",\"lx_88965\",\"lx_se_27015\",\"lx_se_31758\",\"lx_se_31875\",\"lx_85599\",\"lx_se_32058\",\"lx_se_33290\",\"lx_se_70288\",\"lx_191344\",\"lx_140489\",\"lx_179298\",\"lx_se_38892\",\"lx_90016\",\"lx_134551\",\"lx_176499\",\"lx_191025\"]},\"indu\":{\"param\":\"source\",\"values\":[\"lx_se_71628\",\"lx_se_71453\",\"lx_se_1574\",\"lx_71791\",\"lx_se_1587\",\"lx_se_71798\",\"lx_se_93773\",\"lx_se_1599\",\"lx_72151\",\"lx_72430\",\"lx_se_3148\",\"lx_72342\",\"lx_98911\",\"lx_se_73643\",\"lx_60797\",\"lx_105978\",\"lx_210013\",\"lx_75023\",\"lx_118050\",\"lx_104767\",\"lx_se_11501\",\"lx_13240\",\"lx_se_16080\",\"lx_se_79231\",\"lx_se_18662\",\"lx_se_80562\",\"lx_116094\",\"lx_83248\",\"lx_se_24808\",\"lx_74887\",\"lx_se_24989\",\"lx_se_24945\",\"lx_84005\",\"lx_se_30828\",\"lx_197597\",\"lx_se_85311\",\"lx_85790\",\"lx_32727\",\"lx_34615\",\"lx_se_39675\",\"lx_89157\",\"lx_se_41415\",\"lx_se_43796\",\"lx_se_45814\",\"lx_91589\"]},\"macro\":{\"param\":\"source\",\"values\":[\"lx_se_124622\",\"lx_4854\",\"lx_4878\",\"lx_4883\",\"lx_4888\",\"lx_7356\",\"lx_7358\",\"lx_76334\",\"lx_se_48509\",\"lx_17228\",\"lx_18038\",\"lx_se_83559\",\"lx_se_36032\",\"lx_213718\",\"lx_50689\",\"lx_51124\"]},\"material\":{\"param\":\"source\",\"values\":[\"lx_se_988\",\"lx_71642\",\"lx_se_2472\",\"lx_5100\",\"lx_se_7288\",\"lx_se_9144\",\"lx_75447\",\"lx_75450\",\"lx_75443\",\"lx_104754\",\"lx_75454\",\"lx_76078\",\"lx_75449\",\"lx_85301\",\"lx_se_85919\",\"lx_se_39332\",\"lx_se_39335\",\"lx_se_42919\",\"lx_se_42920\",\"lx_86039\"]},\"tech_tele\":{\"param\":\"source\",\"values\":[\"lx_71785\",\"lx_1563\",\"lx_se_8261\",\"lx_76004\",\"lx_76046\",\"lx_120788\",\"lx_77860\",\"lx_14170\",\"lx_se_79041\",\"lx_80117\",\"lx_80198\",\"lx_81978\",\"lx_23976\",\"lx_82845\",\"lx_82853\",\"lx_104855\",\"lx_24806\",\"lx_se_100808\",\"lx_se_83730\",\"lx_85302\",\"lx_34520\",\"lx_se_35108\",\"lx_75002\",\"lx_88820\",\"lx_se_39675\",\"lx_106072\",\"lx_90279\",\"lx_45820\",\"lx_45817\",\"lx_se_43796\",\"lx_47769\",\"lx_se_54126\",\"lx_55233\"]},\"pr_rel\":{\"param\":\"source\",\"values\":[\"lx_87245\",\"lx_se_23246\",\"lx_90838\",\"lx_91113\",\"lx_105028\",\"lx_75882\",\"lx_87336\",\"lx_189571\"]},\"misc\":{\"param\":\"source\",\"values\":[\"lx_104701\",\"lx_99282\",\"lx_89990\",\"lx_99283\",\"lx_10505\",\"lx_67772\",\"lx_82763\",\"lx_83236\",\"lx_179238\",\"lx_se_24989\",\"lx_59700\",\"lx_31466\",\"lx_77865\",\"lx_33727\",\"lx_108076\",\"lx_45791\",\"lx_191100\",\"lx_49703\",\"lx_50297\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"1m\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"aapl","filing_date:desc", "{\"ticker\":{},\"doctype\":{\"ni\":{\"Tags\":{\"param\":\"tags\",\"values\":[\"#earningsrelease\",\"#management\",\"#earningsannounce\",\"#dividend\",\"#acquisition\",\"#partnership\",\"#drug\",\"#stock\",\"#ipo\",\"#stocksplit\",\"#spinoff\",\"#others\"]},\"Source\":{\"param\":\"rep_subjects\",\"values\":[\"gpr\",\"pr\",\"bw\",\"gnw\",\"mw\",\"uk_disc\",\"cnw\",\"nwca\",\"nw\",\"acw\",\"acn\",\"act\",\"abn\",\"fsc\",\"to\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"},
									{"aapl","filing_date:desc",  "{\"ticker\":{},\"doctype\":{\"note\":{\"note-type\":{\"param\":\"note_subtype\",\"values\":[\"note\",\"email\",\"attachment\",\"clipper\",\"starred\",\"plotter\",\"sntclipper\",\"thesis\"]},\"username\":{\"param\":\"note_authors\",\"values\":[\"akash.aggarwal\",\"alap\",\"anurag\",\"arjun.kakkar\",\"ashish.garg\",\"atish.garg\",\"bhaskar\",\"bhuvnesh\",\"dhriti.nogia\",\"gaurav.anand\",\"ishant.mehta\",\"kapil.sardana\",\"malika.jain\",\"navreet\",\"neeti.bhatia\",\"ok.bye\",\"rajeev\",\"rakesh.sadu\",\"sangeeta.nandal\",\"shubham.walia\",\"sonia\",\"tejbir.singh\",\"test.armani\",\"test.dashboard77\",\"test.food\",\"test.march\",\"test.new00\",\"test.thesis01\",\"test.ttt\",\"test.usercheck105\",\"test.usercheck115\",\"test.usercheck119\",\"test.usercheck126\",\"test.zzz\",\"test0000\",\"test4098\",\"test809\",\"tsession\"]},\"note_origin\":{\"param\":\"note_origin\",\"values\":[\"clipper\",\"onenote\",\"sentieo\",\"Sentieo\"]},\"usertags\":{\"param\":\"usertags\",\"values\":[]},\"note_topic\":{\"param\":\"note_topic\",\"values\":[\"Earnings\",\"general\",\"General\",\"Meeting\",\"Sellside\",\"tejbir_test\",\"Transcript\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"all\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}"}
		};	
		return groupArray;
	}

	@DataProvider(name = "doctype_customdate_filters_combination")
	public Object[][] test_doctype_customdate_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "aapl",
				"{\"ticker\":{},\"doctype\":{\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"2019-09-30T18:30:00.000Z\",\"2020-02-11T18:30:00.000Z\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },
				{ "msft",
						"{\"ticker\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"sector\":{},\"regions\":{},\"date\":{\"\":{\"\":{\"param\":\"date\",\"values\":[\"2017-12-31T18:30:00.000Z\",\"2019-08-19T18:30:00.000Z\"]}}},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" }, };

		return groupArray;
	}

	@DataProvider(name = "doctype_language_filters_combination")
	public Object[][] test_doctype_language_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "aapl",
				"{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{\"\":{\"\":{\"param\":\"language\",\"values\":[1]}}},\"other\":{},\"section\":{}}" },
				{ "vod:ln",
						"{\"ticker\":{},\"doctype\":{\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{\"\":{\"\":{\"param\":\"language\",\"values\":[1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142]}}},\"other\":{},\"section\":{}}" }, };

		return groupArray;
	}

	@DataProvider(name = "doctype_sector_filters_combination")
	public Object[][] test_doctype_sector_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "",
				"{\"ticker\":{},\"sector\":{\"Information Technology\":{\"\":{\"param\":\"filter_subsectors\",\"values\":[\"Application Software\",\"Communications Equipment\",\"Data Processing & Outsourced Services\",\"Electronic Components\",\"Electronic Equipment & Instruments\",\"Electronic Manufacturing Services\",\"IT Consulting & Other Services\",\"Internet Services & Infrastructure\",\"Semiconductor Equipment\",\"Semiconductors\",\"Systems Software\",\"Technology Distributors\",\"Technology Hardware, Storage & Peripherals\"]}}},\"language\":{},\"section\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"regions\":{},\"source\":{},\"other\":{},\"date\":{}}" },
				{ "a", "{\"ticker\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}},\"ppt\":{\"company-presentations\":{\"param\":\"ppt_category\",\"values\":[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"8\",\"0\"]}},\"ef\":{\"QA\":{\"values\":[\"10-k\",\"10-k/a\"],\"param\":\"filing_subtype\"},\"ECM\":{\"param\":\"filing_subtype\",\"values\":[]},\"ADR\":{\"param\":\"filing_subtype\",\"values\":[]},\"prx\":{\"param\":\"filing_subtype\",\"values\":[]},\"OR\":{\"param\":\"filing_subtype\",\"values\":[]},\"hdr\":{\"param\":\"filing_subtype\",\"values\":[]},\"emben\":{\"param\":\"filing_subtype\",\"values\":[]},\"intran\":{\"param\":\"filing_subtype\",\"values\":[]},\"other_subtype\":{\"param\":\"filing_subtype\",\"values\":[]}}},\"sector\":{\"Health Care\":{\"\":{\"param\":\"filter_subsectors\",\"values\":[\"Biotechnology\",\"Health Care Distributors\",\"Health Care Equipment\",\"Health Care Facilities\",\"Health Care Services\",\"Health Care Supplies\",\"Health Care Technology\",\"Life Sciences Tools & Services\",\"Managed Health Care\",\"Pharmaceuticals\"]}}},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" },

		};

		return groupArray;
	}

	@DataProvider(name = "test_doctype_region_Filter")
	public Object[][] test_doctype_region_Filter() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "",
				"{\"ticker\":{},\"sector\":{},\"date\":{},\"regions\":{\"Europe\":{\"Western Europe\":{\"param\":\"geog_subfilter\",\"values\":[\"va\",\"ch\",\"ad\",\"is\",\"it\",\"im\",\"gb\",\"ie\",\"es\",\"gr\",\"nl\",\"pt\",\"no\",\"li\",\"lu\",\"gi\",\"be\",\"fr\",\"dk\",\"de\",\"fi\",\"fo\",\"mc\",\"mt\",\"sm\",\"se\"]},\"Central & Eastern Europe\":{\"param\":\"geog_subfilter\",\"values\":[\"ee\",\"am\",\"al\",\"cz\",\"ge\",\"at\",\"az\",\"ru\",\"rs\",\"lv\",\"lt\",\"tm\",\"tj\",\"ro\",\"pl\",\"bg\",\"ba\",\"hr\",\"hu\",\"by\",\"me\",\"md\",\"kg\",\"uz\",\"cy\",\"mk\",\"sk\",\"si\",\"kz\",\"ua\"]}}},\"section\":{},\"source\":{},\"language\":{},\"other\":{},\"doctype\":{\"tt\":{\"events\":{\"param\":\"tt_category\",\"values\":[\"analyst_shareholder_meeting_other\",\"conference\",\"earnings\",\"guidance\",\"m&a\"]}}},\"exchange\":{}}" }

		};
		return groupArray;
	}

	@DataProvider(name = "fetch_files_meta_data")
	public Object[][] fetch_files_meta_data() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "[\"5e1596cdab1b861f647ffa37\"]" }, { "null" }, { "12345" } };
		return groupArray;
	}

	@DataProvider(name = "fetch_file_content")
	public Object[][] fetch_file_content() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "5d1ddb1d92d3de0a2be4c732" }, { "5d17546d92d3de0a2a4377b7" } };
		return groupArray;
	}

	@DataProvider(name = "get_docnote_pdf")
	public Object[][] get_docnote_pdf() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "5e566ebf8707d25bd739e308" } };
		return groupArray;
	}

	@DataProvider(name = "fetch_note_search")
	public Object[][] fetch_note_search() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "note#$email#$attachment#$clipper#$starred#$plotter#$sntclipper#$thesis#$",
				"note#$",
				"{\"ticker\":{},\"doctype\":{\"note\":{\"note-type\":{\"param\":\"note_subtype\",\"values\":[\"note\",\"email\",\"attachment\",\"clipper\",\"starred\",\"plotter\",\"sntclipper\",\"thesis\"]},\"username\":{\"param\":\"note_authors\",\"values\":[\"test.mock\",\"test.thesis\"]},\"note_origin\":{\"param\":\"note_origin\",\"values\":[\"clipper\",\"evernote\",\"onenote\",\"sentieo\"]},\"usertags\":{\"param\":\"usertags\",\"values\":[]},\"note_topic\":{\"param\":\"note_topic\",\"values\":[\"dash\",\"Earnings\",\"General\",\"Meeting\",\"Sellside\",\"Transcript\"]}}},\"sector\":{},\"regions\":{},\"date\":{},\"source\":{},\"language\":{},\"other\":{},\"section\":{}}" }, };

		return groupArray;
	}

	@DataProvider(name = "request_feed")
	public Object[][] request_feed() {
		Object[][] groupArray = null;
		groupArray = new String[][] {
				{ "Market Watch Real time Headlines", "http://feeds.marketwatch.com/marketwatch/realtimeheadlines/" } };

		return groupArray;
	}

	@DataProvider(name = "update_feed")
	public Object[][] update_feed() {
		Object[][] groupArray = null;
		groupArray = new String[][] {
				{ "5e94096fb6037338344fc9a9", "test 2", "http://feeds.feedburner.com/TechCrunch/" } };

		return groupArray;
	}

	@DataProvider(name = "unsubscribe_feed")
	public Object[][] unsubscribe_feed() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "5e94096fb6037338344fc9a9" } };

		return groupArray;
	}

	@DataProvider(name = "fetch_document_note_info")
	public Object[][] fetch_document_note_info() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "[]", "[\"5ea2d438c0af314efda2eeb9\"]" },
				{ "[]", "[\"5e884c51c0af31078aed24e1\"]" } };

		return groupArray;
	}

	@DataProvider(name = "fetch_transform_note_content")
	public Object[][] fetch_transform_note_content() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "note", "5e91afd1142593f1c9d1d179" } };

		return groupArray;
	}
	
	@DataProvider(name = "test_doctype_ticker_publicapi")
	public Object[][] test_doctype_ticker_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"ef" , "10-k", "aapl"}, {"ef" , "10-q/a", ""}, {"ppt" , "1", ""}, {"nw" , "lx_3588", "msft"},{"tt" , "earnings", ""},
									{"gbf" , "1", ""}, {"ni" , "pr", ""},{"reg" , "epa", ""},
									{"jr" , "c19", ""}, {"sd" , "all", ""}, {"rss" , "14", ""}, {"bd" , "", ""}
		};
		return groupArray;
	}	
	
//	@DataProvider(name = "test_doctype_rr_publicapi")
	public Object[][] test_doctype_rr_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"se_10619","rr_reasons_19","rr_styles_8"}, {"se_11864","rr_reasons_2","rr_styles_9"}
		};
		return groupArray;
	}
	
	@DataProvider(name = "test_doctype_notes_publicapi")
	public Object[][] test_doctype_notes_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"attachment", "testing.akash"}
		};
		return groupArray;
	}
	
	@DataProvider(name = "invalid_test_doctype_ticker_publicapi")
	public Object[][] invalid_test_doctype_ticker_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"ef" , "", "aapl"},  {"" , "", "1234568"}, {"abcd" , "12345", "aaaaaaaaaa"},{"@#$%^&*" , "@#$%$#", "%^&^%$"}
		};
		return groupArray;
	}
	
	
	@DataProvider(name = "test_query_publicapi")
	public Object[][] test_query_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"nw" , "sales", "601398:CH"}, {"nw" , "in:title sales", ""}, {"ef" , "sales AND China", ""},
									{"tt" , "china OR profit", ""}, {"nw" , "~month", ""}, {"nw" , "~year", ""}
		};
		return groupArray;
	}

	@DataProvider(name = "test_datefrom_publicapi")
	public Object[][] test_datefrom_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"ef" ,"16-Aug-2020"}, {"ef" , ""},
		};
		return groupArray;
	}
	
	@DataProvider(name = "test_start_size_publicapi")
	public Object[][] test_start_size_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"0" , "5"}, {"5" , "5"},
		};
		return groupArray;
	}
	
	@DataProvider(name = "invalid_test_datefrom_publicapi")
	public Object[][] invalid_test_datefrom_publicapi() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"ef" , "16-Aug-2022"}, {"ef" , "16-Auggggggggggggg-2015"},
		};
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
			groupArray = new String[][] { { "GrossProfit", "usd" }, { "DilutedEPSTotal", "cad" }, { "Ebitda", "eur" },
					{ "Ebit", "usd" }, { "NetIncome", "usd" } };
		} else if (testmethodname.getName().equalsIgnoreCase("earningssurprisesdata")) {
			groupArray = new String[][] { { "TotalRevenue", "QuarterlyRestated" }, { "TotalRevenue", "AnnualRestated" },
					{ "DilutedEPSTotal", "AnnualRestated" }, { "DilutedEPSTotal", "QuarterlyRestated" },
					{ "FreeCashFlowPerShare", "QuarterlyRestated" }, { "FreeCashFlowPerShare", "AnnualRestated" } };
		}
		 else if (testmethodname.getName().equalsIgnoreCase("fetchGraphDataFinMatrices")) {
			 groupArray = new String[][] { { "eps" }, { "sales"}, { "gross_profit" },
					{ "cogs"},{"sga"},{"ebitda"},{"da"},{"ebit"},{"ebit-per"},{"ebitda-per"},{"gross_margin"},{"fcf"},{"capex"},{"net_inc"},{"tax"},{"net_int"}};			
		 }
	     else if (testmethodname.getName().equalsIgnoreCase("fetchValueTableForMobile")) {
		 groupArray = new String[][] { { "eps" }, { "sales" }, { "gross_profit" },
				{ "cogs" }, { "sga" },{"ebitda" },{"da"},{"ebit"},{"ebit-per"},{"ebitda-per"},{"gross_margin"},{"fcf"},{"capex"},{"net_inc"},{"tax"},{"net_int"}};
	 
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
		groupArray = new String[][] { { "P/E", "p_eps" }, { "EV/EBITDA", "ev_ebitda" }, { "EV/EBIT", "ev_ebit" },
				{ "EV/Sales", "ev_sales" }, { "P/Sales", "p_sales" }, { "P/Cash Flow", "price_cashflow" },
				{ "Enterprise Value", "ev_daily" }, { "Market Cap", "mkt_cap_daily" } };
		return groupArray;
	}

	@DataProvider(name = "EDTEstimates14-22")
	public Object[][] dilutedEPS() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Diluted EPS-2014", "aapl" }, { "Diluted EPS-2015", "aapl" },
				{ "Diluted EPS-2016", "aapl" }, { "Diluted EPS-2017", "aapl" }, { "Diluted EPS-2018", "aapl" },
				{ "Diluted EPS-2019", "aapl" }, { "Diluted EPS-2020", "aapl" }, { "Diluted EPS-2021", "aapl" },
				{ "Diluted EPS-2022", "aapl" }, { "Diluted EPS-NTM", "aapl" }, { "Diluted EPS-2014", "7203:JP" },
				{ "Diluted EPS-2015", "7203:JP" }, { "Diluted EPS-2016", "7203:JP" }, { "Diluted EPS-2017", "7203:JP" },
				{ "Diluted EPS-2018", "7203:JP" }, { "Diluted EPS-2019", "7203:JP" }, { "Diluted EPS-2020", "7203:JP" },
				{ "Diluted EPS-2021", "7203:JP" }, { "Diluted EPS-2022", "7203:JP" },
				{ "Diluted EPS-NTM", "7203:jp" } };
		return groupArray;
	}

	@DataProvider(name = "EDTEstimates14-21")
	public Object[][] dilutedEPS21() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Diluted EPS-2014", "ALV:GR " }, { "Diluted EPS-2015", "ALV:GR " },
				{ "Diluted EPS-2016", "ALV:GR " }, { "Diluted EPS-2017", "ALV:GR" }, { "Diluted EPS-2018", "ALV:GR" },
				{ "Diluted EPS-2019", "ALV:GR" }, { "Diluted EPS-2020", "ALV:GR" }, { "Diluted EPS-2021", "ALV:GR" },
				{ "Diluted EPS-2022", "ALV:GR" }, { "Diluted EPS-NTM", "ALV:GR" }, { "Diluted EPS-2014", "BP.:LN" },
				{ "Diluted EPS-2015", "BP.:LN" }, { "Diluted EPS-2016", "BP.:LN" }, { "Diluted EPS-2017", "BP.:LN" },
				{ "Diluted EPS-2018", "BP.:LN" }, { "Diluted EPS-2019", "BP.:LN" }, { "Diluted EPS-2020", "BP.:LN" },
				{ "Diluted EPS-2021", "BP.:LN" }, { "Diluted EPS-2022", "BP.:LN" }, { "Diluted EPS-NTM", "BP.:LN" } };
		return groupArray;
	}

	@DataProvider(name = "EDTEstimates19-21")
	public Object[][] dilutedEPS19to21() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Diluted EPS-2019", "uber" }, { "Diluted EPS-2020", "uber" },
				{ "Diluted EPS-2021", "uber" }, { "Diluted EPS-2022", "uber" }, { "Diluted EPS-NTM", "uber" } };
		return groupArray;
	}

	@DataProvider(name = "EDTEstimates16-21")
	public Object[][] dilutedEPS16to21() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Diluted EPS-2016", "ola:cn " }, { "Diluted EPS-2017", "ola:cn" },
				{ "Diluted EPS-2018", "ola:cn" }, { "Diluted EPS-2019", "ola:cn" }, { "Diluted EPS-2020", "ola:cn" },
				{ "Diluted EPS-2021", "ola:cn" }, { "Diluted EPS-2022", "ola:cn" }, { "Diluted EPS-NTM", "ola:cn" } };
		return groupArray;
	}

	@DataProvider(name = "EDTEstimates14-20")
	public Object[][] dilutedEPS14to20() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "Diluted EPS-2014", "loxo" }, { "Diluted EPS-2015", "loxo" },
				{ "Diluted EPS-2016", "loxo" }, { "Diluted EPS-2017", "loxo" }, { "Diluted EPS-2018", "loxo" },
				{ "Diluted EPS-2019", "loxo" }, { "Diluted EPS-2020", "loxo" }, { "Diluted EPS-NTM", "loxo" } };
		return groupArray;
	}

	@DataProvider(name = "s&pPE")
	public Object[][] PE() {
		Object[][] groupArray = null;
		groupArray = new String[][] { { "backward", "lulu,sp500" }, { "blended", "lulu,sp500" },
				{ "backward", "aapl,sp500" }, { "blended", "aapl,sp500" } };
		return groupArray;
	}

	@DataProvider(name = "fetchUnifiedStream")
	public Object[][] fetchUnifiedStream() {
		return new Object[][] { { "low", "articles", "aapl,msft,lb", "sentieo" }, { "low", "fi", "aapl", "whitelist" },
				{ "low", "tweets", "aapl,msft,tsla", "all" }, };
	}

	@DataProvider(name = "searchEstimates")
	public Object[][] gnipSearchEstimate() {
		return new Object[][] { {
				" \"la senza\" , vspink , \"henri bendel\" , \"bath and body works\" OR bathandbodyworks , \"victoria's secret\" OR \"victorias secret\" OR "
						+ "victoriassecret",
				"lb" },
				{ "ipad , Airpods , \"apple watch\" OR applewatch , \"apple tv\" OR appletv , \"Apple Music\" OR applemusic , iphone",
						"aapl" },
				{ "\"bahama breeze\" OR bahamabreeze , \"olive garden\" OR olivegarden , \"capital grille\" OR capitalgrille , \"longhorn steakhouse\" OR longhornsteakhouse\r\n"
						+ "", "dri" } };
	}

	@DataProvider(name = "searchAutocomplete")
	public Object[][] gnipAutocomplete() {
		return new Object[][] { { "ipad+iphone" } };
	}

	@DataProvider(name = "chainUser")
	public Object[][] chainUserData() {
		return new Object[][] { { "10-k", "aapl" }, { "8-k", "aapl" }, { "10-q", "aapl" }, { "10-k", "amzn" },
				{ "8-k", "amzn" }, { "10-q", "amzn" } };
	}

	@DataProvider(name = "loadGraph")
	public Object[][] loadGraph() {
		return new Object[][] { { "templates" }, { "recent" }, { "tagged_series" }, { "saved_series" } };
	}

	@DataProvider(name = "alertType")
	public Object[][] alertType() {
		return new Object[][] { { "filing" }, { "pr_alert" }, { "news" }, { "presentation" }, { "transcript" } };
	}

	@DataProvider(name = "mosaicsetting")
	public Object[][] mosaicsetting() {
		return new Object[][] { { "Revenue", "Q" }, { "Stock", "Q" }, { "Revenue", "M" }, { "Stock", "M" } };
	}

	@DataProvider(name = "autocomplete")
	public Object[][] queryAutocomplete() {
		return new Object[][] { { "sales", "aapl" }, { "revenue", "msft" } };
	}
	
	@DataProvider(name = "notebookImageData")
	Object[][] getImageData() {
		String[][] groupArray = null;
		groupArray = CSVReaderUtil.readAllDataAtOnce("notebookPublicApi" + File.separator + "notebookImageData.csv");
		return groupArray;
	}

	@DataProvider(name = "module-type")
	public String[][] fetchAutocompleteData_tickers() {
		return new String[][] { { "EDT", "1" }, { "docsearch", "1" }, { "notebook", "0" } };
	}
	
	@DataProvider(name = "module-type1")
	public String[][] fetchAutocompleteData_ticker() {
		return new String[][] { { "dashboard", "1","true"},{ "most", "0" ,"false"}};
	}
	@DataProvider(name = "revenue")
	public Object[][] revenuePeriodType() {
		return new Object[][] { { "Quarterly" }, { "Annual" } };
	}
	
	@DataProvider(name = "instagram")
	public Object[][] instagramMetric() {
		return new Object[][] { { "all_list" }, { "likes_list" } , { "comments_list" }, { "posts_list" }
		, { "followers_list" }, { "likes_list/posts_list" }, { "comments_list/posts_list" }};
	}
	
	@DataProvider(name = "returnsData")
	public Object[][] returnsRebalance() {
		return new Object[][] { { "eq_wtd", "never" }, { "eq_wtd", "daily" }
		, { "eq_wtd", "weekly" }, { "eq_wtd", "monthly" },{ "eq_wtd", "quarterly" }
		,{ "eq_wtd", "yearly" },{ "mkt_cap", "never" }, { "mkt_cap", "daily" }
		, { "mkt_cap", "weekly" }, { "mkt_cap", "monthly" },{ "mkt_cap", "quarterly" }
		,{ "mkt_cap", "yearly" } };
	}

	@DataProvider(name = "test_entity_numbersearch_invalid")
	public Object[][] test_entity_numbersearch_invalid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"abcd"},{"^&*(%"},{"1234abcd"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_numbersearch_valid")
	public Object[][] test_entity_numbersearch_valid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"100"},{"500.1"},{"-200"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_datesearch_valid")
	public Object[][] test_entity_datesearch_valid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"2020-10-01T00:00:00"},{"2020-09-09T00:00:00"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_datesearch_invalid")
	public Object[][] test_entity_datesearch_invalid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"2020-0909"},{"2020-15-01"},{"2020-Sep-09"},{"2020/Sep/09"},{"2020/09/09"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_singlelinesearch_valid")
	public Object[][] test_entity_singlelinesearch_valid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"This note is under test."},{"My name is Gaurav"}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_multilinesearch_valid")
	public Object[][] test_entity_multilinesearch_valid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"This note is under test. Please ignore this."}
		};
		return groupArray;
	}

	@DataProvider(name = "test_entity_dropdownsearch_valid")
	public Object[][] test_entity_dropdownsearch_valid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"Gaurav1"},{"Gaurav2"}
		};
		return groupArray;
	}

	@DataProvider(name = "TestEntityNumberRangeValid")
	public Object[][] TestEntityNumberRangeValid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"-100","600"}, {"100","600"},{"500","600"},{"-10000","1"}
		};
		return groupArray;
	}

	@DataProvider(name = "TestEntityNumberRangeInvalid")
	public Object[][] TestEntityNumberRangeInvalid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"1","99"},{"101","500"}, {"-10000","-500"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioValid1")
	public Object[][] EntityNumberRangeScenarioValid1() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"1","101"}, {"-500","101"},{"100","500.1"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioInvalid1")
	public Object[][] EntityNumberRangeScenarioInvalid1() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"600","100"}, {"500","100"},{"1","-1"},{"1000","100"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioValid2")
	public Object[][] EntityNumberRangeScenarioValid2() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"99","502"}, {"-500","-200"},{"500","500.1"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioInvalid2")
	public Object[][] EntityNumberRangeScenarioInvalid2() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"100","100"}, {"1000","100"},{"1","-1"},{"500","500"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioValid3")
	public Object[][] EntityNumberRangeScenarioValid3() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"100"},{"-500"},{"500.1"}
		};
		return groupArray;
	}

	@DataProvider(name = "EntityNumberRangeScenarioValid4")
	public Object[][] EntityNumberRangeScenarioValid4() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"100"},{"500.1"}
		};
		return groupArray;
	}

	@DataProvider(name = "TestEntityDateRangeValid")
	public Object[][] TestEntityDateRangeValid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"2020-09-09T00:00:00","2020-10-01T00:00:00"}, {"2012-01-01T00:00:00","2022-10-01T00:00:00"},
									 {"2012-10-01T00:00:00","2022-10-01T00:00:00"}
		};
		return groupArray;
	}

	@DataProvider(name = "TestEntityDateRangeInvalid")
	public Object[][] TestEntityDateRangeInvalid() {
		Object[][] groupArray = null;
		groupArray = new String[][] {{"2020-01-09T00:00:00","2020-05-01T00:00:00"}, {"2020-11-01T00:00:00","2018-10-01T00:00:00"}
		};
		return groupArray;
	}
}
