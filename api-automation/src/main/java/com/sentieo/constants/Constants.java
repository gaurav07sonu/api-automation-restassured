package com.sentieo.constants;

import java.io.File;

public class Constants {
	
	//general set of data
	public static final String RESOURCE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "test" + File.separator + "resources";
	public static final String EMAIL = "alphagani35@gmail.com";
	public static final String PASSWORD = "DGL=14412jg";
	public static final String APP_URL = "https://app.sentieo.com";
	public static final String USER_APP_URL = "https://user-app.sentieo.com";
	public static final String LOGIN_URL = "/api/login_1/";
	
	
	//comparable / Screener
	public static final String FETCH_SCREENER_MODELS = "/api/fetch_screener_models/";
	public static final String COMPARABLE_SEARCH = "/api/comparable_search/";
	public static final String MANAGEMENT_INFO = "/api/managementinfo/";
	public static final String FETCH_HOLDINGS = "/api/fetch_institutional_holdings_snapshot/";
	public static final String FETCH_MAIN_GRAPH = "/api/fetch_main_graph/";
	public static final String FETCH_COMPANY_EVENTS = "/api/fetch_company_events/";
	public static final String MANAGEMENT_INFO_NEW = "/api/managementinfo_new/";
	
	
	//doc-search
	public static final String FETCH_SAVED_FILTERS = "/api/fetch_saved_filters/";
	public static final String FETCH_NOTE_FILTERS = "/api/fetch_note_filters/";
	public static final String FETCH_SEARCH_LIBRARY = "/api/fetch_searchlibrary/";
	public static final String FETCH_SEARCH = "/api/fetch_search/";
	public static final String BULK_DOWNLOAD = "/api/bulk_download/";
	public static final String FETCH_DOCS_META_DATA = "/api/fetch_docs_meta_data/";
	public static final String FETCH_CUSTOM_DOC_DIFF = "/api/fetch_custom_doc_diff/";
	public static final String FETCH_IMPACT_SCORE = "/api/fetch_impact_score/";
	
	
	//finance / plotter
	public static final String FETCH_CURRENT_STOCK_DATA = "/api/fetch_current_stock_data/";
	public static final String FETCH_CAPITAL_EVENTS = "/api/fetch_capital_events/";
	public static final String GET_TRACKER_MAPPINGS = "/api/get_tracker_mappings/";
	public static final String FETCH_COMPANY_DOCS = "/api/fetch_company_docs/";
	public static final String GET_COMPANY_RETURN = "/api/get_company_return/";
	public static final String NEW_FETCH_MOSAIC_SUMMARY_DATA = "/api/new_fetch_mosaic_summary_data/";
	public static final String FETCH_COMPANY_SUMARY_TABLE = "/api/fetch_company_summary_table/";
	public static final String FETCH_ANALYST_RECOMMEND = "/api/fetch_analyst_recommend/";
	public static final String FETCH_YEARLY_DATA = "/api/fetch_yearly_data/";
	public static final String FETCH_TRADING_RATIO = "/api/fetch_trading_ratios/";
	public static final String FETCH_COMPANY_STATUS = "/api/fetch_company_status/";
	public static final String FETCH_NEW_COMPANY_HEADER_DATA = "/api/fetch_new_company_header_data/";
	public static final String EARNINGS_SURPRISES_DATA = "/api/earnings_surprises_data/";
	public static final String FETCH_PAST_INTRADAY = "/api/fetch_past_intra/";
	public static final String FETCH_GRAPH_DATA = "/api/fetch_graph_data/";
	public static final String FETCH_VALUE_DATA= "/api/fetch_value_table/";
	public static final String FETCH_HOLDINGS_DATA= "/api/fetch_institutional_holdings_data3/";
	
	
	//market-summary
	public static final String FDS_INDICES_CHANGE = "/api/fds_indices_change/";
	public static final String GET_SPOTFX_DATA = "/api/get_spotfx_data/";
	public static final String GET_RATES_API = "/api/get_rates_api/";
	public static final String INDEX_CHART_API = "/api/index_chart_api/";
	
	
	//notebook
	public static final String SET_NOTE_HTML = "/api/set_note_html/";
	public static final String DELETE_NOTE = "/api/delete_user_notebook/";
	public static final String EMAIL_NOTE = "/api/email_from_note/";
	public static final String FETCH_NOTE_LIST = "/api/fetch_notes_list_view/";
	public static final String UPLOAD_FILE = "/upload/";
	public static final String CREATE_ATTACHMENT_NOTE = "/api/create_note_for_attachments/";
	public static final String THESIS_ENTITY = "/api/thesis_entity/";
	public static final String UPDATE_TAG_TICKER = "/api/update_note_tag_ticker/";
	public static final String TEMPLATE_ENTITY = "/api/template_entity/";
	public static final String STAR_NOTE = "/api/add_star_note/";
	public static final String UNSTAR_NOTE = "/api/remove_star_note/";
	public static final String SAVE_ATTACHMENT = "/api/save_attachment/";
	public static final String REMOVE_ATTACHMENT = "/api/remove_attachments/";
	public static final String USER_COMMENTS = "/api/user_comments/";
	public static final String SEND_NOTE_EMAIL = "notebookapitesting@gmail.com";
	
	
	// notebook public apis
	public static final String APPURL = "https://api.sentieo.com/v1";
	public static final String X_USER_KEY = "5d3808857a782c6baaa6c00a";
	public static final String X_API_KEY = "n6tzgSmCQe779XyjdgTaI1AyyjbMHLeE7KjSWmjF";
	public static final String XUSERKEY = "x-user-key";
	public static final String XAPIKEY = "x-api-key";
	public static final String FILE_UPLOAD = "/files";
	public static final String NOTES = "/notes";

	//plotter
	public static final String GET_GTRENDS = "/api/get_gtrends/";
	public static final String WEBSITETRAFFIC ="/api/alexaapi/";
	
	
	//screener
	public static final String FETCH_SCREENER_SEARCH = "/api/fetch_screener_search/";
	public static final String FETCH_SCREEN_FILTERS = "/api/fetch_screen_filters/";
	public static final String FETCH_FIELDS_INFO = "/api/fetch_fields_info/";
	public static final String FETCH_SCREENER_PARAM_USAGE = "/api/fetch_screener_param_usage/";
	
	//statement
	
	public static final String FETCH_ALL_XBRL_TABLES = "/api/fetch_all_xbrl_tables/";
	public static final String GET_XBRL_DATA_TABLE = "/api/get_xbrl_data_table/";
	public static final String FETCH_UNIFIED_STREAM = "/api/fetch_unified_stream/";
	
	
	
	//user
	public static final String FETCH_USER_PORTFOLIO= "/api/fetch_user_portfolio_data/";
	public static final String FETCH_TICKER_QUOTE="/api/fetch_ticker_quote/";
	public static final String FETCH_FIREWALL_TEST="/api/fetch_firewall_test";
	public static final String FETCH_USERS_WATCHLIST= "/api/fetch_users_watchlist_api/";	
	
	//private companies
	public static final String FETCH_CB_EXIT_TABLE="/api/fetch_cb_exit_table/";
	//public static final String FETCH_NEW_COMPANY_HEADER_DATA  ="/api/fetch_new_company_header_data/";
	public static final String FETCH_CB_FR_TABLE="/api/fetch_cb_fr_table/";
	public static final String FETCH_CB_FR_HIGHLIGHTS="/api/fetch_cb_fr_highlights/";
	public static final String FETCH_CB_FR_cumulative="/api/fetch_cb_fr_cumulative/";
	public static final String FETCH_CB_INV_TABLE="/api/fetch_cb_inv_table/";
	public static final String FETCH_CB_INV_QUARTERWISE="/api/fetch_cb_inv_quarterwise/";
	public static final String FETCH_CB_INV_CATEGORY="/api/fetch_cb_inv_category/";
	public static final String FETCH_CB_EXIT_CATEGORY="/api/fetch_cb_exit_category/";
	public static final String FETCH_CB_ACQ_TABLE="/api/fetch_cb_acq_table/";
	public static final String FETCH_CB_FUNDS_TABLE="/api/fetch_cb_funds_table/";
	public static final String FETCH_CB_KEY_INVESTORS="/api/fetch_cb_key_investors/";
	public static final String MSTAR_SET_MAPPING="/api/mstar_set_mapping/";
	public static final String MSTAR_DEL_MAPPING="/api/mstar_del_mapping/";
	public static final String MSTAR_GET_ALL_MAPPINGS="/api/mstar_get_all_mappings/";
	public static final String MSTAR_GET_ALL_INSTRUMENTS="/api/mstar_get_all_instruments/";

}
