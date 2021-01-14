package com.sentieo.constants;

import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class Constants {

	// general set of data
//	public static final String RESOURCE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
//			+ "test" + File.separator + "resources";

	public static String EMAIL = "";
	public static String PASSWORD = "";
	public static String APP_URL = "";
	public static String USER_APP_URL = "";
	public static String PUBLIC_API_URL = "";
	public static String X_API_KEY = "";
	public static String X_USER_KEY = "";
	public static String DEVICE_NAME = "";
	public static String IOS_VERSION = "";
	public static String APP_VERSION = "";


	static {
		String envArg = System.getProperty("env");
		//String envArg = "app";
		String usernameArg = System.getProperty("username");
		String passwordArg = System.getProperty("password");
		Yaml yaml = new Yaml(new Constructor(Configuration.class));
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = cl.getResourceAsStream("env_details.yaml");
		for (Object object : yaml.loadAll(inputStream)) {
			if (((Configuration) object).getEnvName().equals(envArg)) {
				APP_URL = ((Configuration) object).getAppURL();
				USER_APP_URL = ((Configuration) object).getUserAppUrl();
				EMAIL = ((Configuration) object).getUserName();
				PASSWORD = ((Configuration) object).getPassword();
				PUBLIC_API_URL = ((Configuration) object).getPublicApiUrl();
				X_API_KEY = ((Configuration) object).getXapikey();
				X_USER_KEY = ((Configuration) object).getXuserkey();
				APP_VERSION = ((Configuration) object).getAppVersion();
				IOS_VERSION = ((Configuration) object).getIosVersion();
				DEVICE_NAME = ((Configuration) object).getDeviceName();
				System.out.println(APP_URL);
				System.out.println(USER_APP_URL);
				System.out.println(PUBLIC_API_URL);
				System.out.println(X_API_KEY);
				System.out.println(EMAIL);
				System.out.println(PASSWORD);
			}
		}
		if (!StringUtils.isBlank(usernameArg)) {
			EMAIL = usernameArg;
		}
		if (!StringUtils.isBlank(passwordArg)) {
			PASSWORD = passwordArg;
		}
		System.out.println("static block is invoked");
	}

	public static final String LOGIN_URL = "/api/login_1/";

	// comparable / Screener
	public static final String FETCH_SCREENER_MODELS = "/api/fetch_screener_models/";
	public static final String COMPARABLE_SEARCH = "/api/comparable_search/";
	public static final String MANAGEMENT_INFO = "/api/managementinfo/";
	public static final String FETCH_HOLDINGS = "/api/fetch_institutional_holdings_snapshot/";
	public static final String FETCH_MAIN_GRAPH = "/api/fetch_main_graph/";
	public static final String FETCH_COMPANY_EVENTS = "/api/fetch_company_events/";
	public static final String MANAGEMENT_INFO_NEW = "/api/managementinfo_new/";
	public static final String FIN_SETTINGS = "/api/fetch_fin_settings/";
	public static final String METADATA = "/api/fetch_metadata/";
	public static final String SAVE_RISK_REWARD_VIEWS = "/api/save_risk_reward_views/";
	public static final String UPDATE_SCREENER_VIEWS = "/api/update_screener_views/";
	public static final String DELETE_RISK_REWARD_VIEWS = "/api/delete_risk_reward_views/";

	// doc-search
	public static final String FETCH_SAVED_FILTERS = "/api/fetch_saved_filters/";
	public static final String FETCH_NOTE_FILTERS = "/api/fetch_note_filters/";
	public static final String FETCH_SEARCH_LIBRARY = "/api/fetch_searchlibrary/";
	public static final String FETCH_SEARCH = "/api/fetch_search/";
	public static final String BULK_DOWNLOAD = "/api/bulk_download/";
	public static final String FETCH_DOCS_META_DATA = "/api/fetch_docs_meta_data/";
	public static final String FETCH_CUSTOM_DOC_DIFF = "/api/fetch_custom_doc_diff/";
	public static final String FETCH_IMPACT_SCORE = "/api/fetch_impact_score/";
	public static final String LOAD_SAVED_SEARCH_DATA = "/api/load_saved_search_data/";
	public static final String FETCH_SECTIONS = "/api/fetch_sections/";
	public static final String FETCH_PDF_FLAG = "/api/fetch_pdf_flag/";
	public static final String FETCH_SEARCHLIBRARY = "/api/fetch_searchlibrary/";
	public static final String FETCH_NOTE_DOC = "/api/fetch_note_doc/";
	public static final String FETCH_TRANSFORM_DOC_CONTENT = "/api/fetch_transform_doc_content/";
	public static final String FETCH_EXHIBITS = "/api/fetch_exhibits/";
	public static final String FETCH_PAGELINK = "/api/fetch_pagelink/";
	public static final String PUB_DOC_VIEWER = "/api/pub_doc_viewer/";
	public static final String GET_BULK_DOWNLOAD_DOC = "/api/get_bulk_download_doc/";
	public static final String GET_DOC_PDF = "/api/get_doc_pdf/";
	public static final String QUERY_SUGGEST_AUTOCOMPLETE = "/api/query_suggest_autocomplete/";
	public static final String GET_USER_DOWNLOADED_DOCS_STATUS = "/api/get_user_downloaded_docs_status/";
	public static final String FETCH_SNIPPETS = "/api/fetch_snippets/";
	public static final String FETCH_SEARCH_TERM_COUNT = "/api/fetch_search_term_count/";
	public static final String FETCH_LANDING_PAGE_DATA = "/api/fetch_landing_page_data/";
	public static final String FETCH_FILES_META_DATA = "/api/fetch_files_meta_data/";
	public static final String SAVE_USER_SEARCH = "/api/save_user_search/";
	public static final String FETCH_FILE_CONTENT = "/api/fetch_file_content/";
	public static final String LOAD_USER_SEARCH = "/api/load_user_search/";
	public static final String FETCH_USER_VIEWED_DOCS = "/api/fetch_user_viewed_docs/";
	public static final String INDEX_USER_VIEWED_DOC = "/api/index_user_viewed_doc/";
	public static final String DELETE_SAVED_SEARCH = "/api/delete_saved_search/";
	public static final String FETCH_NOTE_SEARCH = "/api/fetch_note_search/";
	public static final String GET_DOCNOTE_PDF = "/api/get_docnote_pdf/";
	public static final String REQUEST_FEED = "/api/request_feed/";
	public static final String UPDATE_FEED = "/api/update_feed/";
	public static final String UNSUBSCRIBE_FEED = "/api/unsubscribe_feed/";
	public static final String FETCH_DOCUMENT_NOTE_INFO = "/api/fetch_document_note_info/";
	public static final String FETCH_SEARCH_FILTERS = "/api/fetch_search_filters/";
	public static final String FETCH_SEARCH_SETTINGS = "/api/fetch_search_settings/";
	public static final String FETCH_TRANSFORM_NOTE_CONTENT = "/api/fetch_transform_note_content/";

//	 doc search public apis
	public static final String XUSERKEY1 = "x-user-key";
	public static final String XAPIKEY1 = "x-api-key";
	public static final String SEARCH = "/documents/search";

	// finance / plotter

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
	public static final String FETCH_VALUE_DATA = "/api/fetch_value_table/";
	public static final String FETCH_NEW_MODEL_DATA = "/api/fetch_new_model_data/";
	public static final String FETCH_HOLDINGS_DATA = "/api/fetch_institutional_holdings_data3/";
	public static final String FETCH_LIVE_PRICE = "/api/fetch_live_price/";
	public static final String FETCH_COMPANY_SUMMARY_TABLE = "/api/fetch_company_summary_table/";
	public static final String FETCH_USER_TICKER_VALUES = "/api/fetch_user_ticker_values/";
	public static final String FETCH_EARNING_HEADER_DATA = "/api/fetch_earning_header_data/";
	public static final String FETCH_DOCS = "/api/fetch_docs/";
	public static final String GET_COMPANY_AUTOCOMPLETE = "/api/get_company_autocomplete/";
	public static final String FETCH_COMPANY_LIST = "/api/fetch_company_list/";
	public static final String FETCH_STOCK_META = "/api/stock_meta/";
	public static final String FETCH_INTRA_HEADER_DATA = "/api/fetch_intra_header_data/";
	public static final String GET_YEARLY_TABLE = "/api/get_yearly_table/";

	public static final String TM_MAP_TICKER="/api/tm_map_ticker/";
	// market-summary
	public static final String FDS_INDICES_CHANGE = "/api/fds_indices_change/";
	public static final String GET_SPOTFX_DATA = "/api/get_spotfx_data/";
	public static final String GET_RATES_API = "/api/get_rates_api/";
	public static final String INDEX_CHART_API = "/api/index_chart_api/";

	// notebook
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
	public static final String FETCH_NOTE_HISTORY = "/api/fetch_note_history/";
	public static final String FETCH_NOTEBOOK_DATA = "/api/notebook_data/";
	public static final String FETCH_NOTE_HTML = "/api/get_note_html/";
	public static final String FETCH_NOTE_FACET_AND_HTML = "/api/fetch_note_facet_and_html/";
	public static final String FETCH_NOTE_SETTING = "/api/notebook_settings/";
	public static final String FETCH_NOTE_VERSION = "/api/note_version/";
	public static final String FETCH_NOTE_LOCK_STATUS = "/api/note_lock_status/";
	public static final String FETCH_NOTE = "/api/fetch_note/";
	public static final String FETCH_THESIS_FIELDS = "/api/fetch_thesis_fields/";
	public static final String FETCH_INITIAL_LOADING = "/api/initial_loading/";
	public static final String FETCH_RECENT_NOTES = "/api/fetch_recent_notes/";
	public static final String GET_HIERARCHY = "/api/get_hierarchy/";
	public static final String GET_THESIS_LIST = "/api/get_thesis_list/";
	public static final String SHARE_NEW_USER_NOTE = "/api/share_new_user_note/";
	public static final String NEW_BOOKMARK_NOTE = "/api/new_bookmark_note/";
	public static final String INDEX_USER_BOOKMARK_DOC = "/api/index_user_bookmark_doc/";
	public static final String UPDATE_FIELD_VALUE = "/api/update_field_value/";
	public static final String NEW_CLIPPER_NOTE = "/api/new_clipper_note/";
	public static final String UPDATE_FIELD = "/api/update_field/";
	public static final String NEW_SECTION = "/api/new_section/";
	public static final String DELETE_HIGHLIGHT = "/api/delete_highlight/";
	public static final String DELETE_USER_BOOKMARK = "/api/delete_user_bookmark/";
	public static final String UPDATE_SECTION = "/api/update_section/";
	public static final String GET_NOTE_CONTENT_FOR_IFRAME = "/api/get_note_content_for_iframe/";
	public static final String CONSUME_CITATION_LINK = "/api/consume_citation_link/";
	public static final String GET_NEW_FREE_CITATION_LINK = "/api/get_new_free_citation_link/";
	public static final String SEARCH_ENTITIES = "/api/search_entities/";

	// notebook public apis
	public static final String XUSERKEY = "x-user-key";
	public static final String XAPIKEY = "x-api-key";
	public static final String FILE_UPLOAD = "/files";
	public static final String LARGE_FILE_UPLOAD = "/file_url";
	public static final String NOTES = "/notes";

	// security master APIs
	public static final String ENTITY = "/sm/user/entities";
	public static final String CHILD_ENTITIES = "/child_entities";
	public static final String SECURITIES = "/sm/user/securities";
	public static final String QUOTES = "/sm/user/quotes";
	public static final String CHILD_QUOTES = "/child_quotes";
	public static final String SEARCH_QUOTE = "/sm/sentieo/quotes/search/";
	public static final String SECURITY_SEARCH = "/sm/sentieo/securities/search/";
	public static final String SECURITY_MAP_SEARCH = "/sm/sentieo/securities/map/";
	public static final String MESSAGE = "No object matches the given query";
	public static final String CODE = "Not Found";
	
	//Watchlist
	public static final String WATCHLISTS = "/watchlists";

	// plotter //mosaic
	public static final String GET_GTRENDS = "/api/get_gtrends/";
	public static final String MAPPING = "/api/get_tracker_mappings/";
	public static final String SCORES_TABLE = "/api/fetch_si_scores_table/";
	public static final String GNIP_SEARCH_ESTIMATE = "/api/gnip_search_estimate/";
	public static final String ALEXA = "/api/alexaapi/";
	public static final String GTRENDSAUTOCOMPLETE = "/api/gtrends_autocomplete/";
	public static final String UNIFIEDTRACKERTABLE = "/api/fetch_unified_tracker_table/";
	public static final String FETCHTRACKERTABLE = "/api/fetch_tracker_table/";
	public static final String LOADGRAPH_NEW = "/api/loadgraph_new/";
	public static final String LOADTEMPLATE_SENTIEO = "/api/loadtemplate_sentieo/";
	public static final String SET_MOSAIC_DEFAULT_SETTINGS = "/api/get_or_set_mosaic_default_settings/";
	public static final String FETCH_SAVED_SERIES = "/api/fetch_saved_series/";
	public static final String DELETE_GRAPH="/api/deletegraph_rt/";

	// screener
	public static final String FETCH_SCREENER_SEARCH = "/api/fetch_screener_search/";
	public static final String FETCH_SCREEN_FILTERS = "/api/fetch_screen_filters/";
	public static final String FETCH_FIELDS_INFO = "/api/fetch_fields_info/";
	public static final String FETCH_SCREENER_PARAM_USAGE = "/api/fetch_screener_param_usage/";

	// statement

	public static final String FETCH_ALL_XBRL_TABLES = "/api/fetch_all_xbrl_tables/";
	public static final String GET_XBRL_DATA_TABLE = "/api/get_xbrl_data_table/";
	public static final String FETCH_UNIFIED_STREAM = "/api/fetch_unified_stream/";

	// user
	public static final String FETCH_USER_PORTFOLIO = "/api/fetch_user_portfolio_data/";
	public static final String FETCH_TICKER_QUOTE = "/api/fetch_ticker_quote/";
	public static final String FETCH_FIREWALL_TEST = "/api/fetch_firewall_test";
	public static final String FETCH_USERS_WATCHLIST = "/api/fetch_users_watchlist_api/";
	public static final String CHECK_DOMAIN = "/api/check_domain/";
	public static final String CHECK_TICKER_SETTINGS = "/alert/check_ticker_settings/";
	public static final String GET_USER_WATCHLIST = "/api/get_user_watchlist_data/";
	public static final String INITIAL_LOADING = "/api/initial_loading/";
	public static final String ADD_WATCHLIST = "/api/add_watchlist/";
	public static final String DELETE_WATCHLIST = "/api/delete_watchlist/";
	public static final String FETCH_ALL_USERS = "/api/fetch_all_users/";
	public static final String SET_RECENT_TICKERS = "/api/set_recent_tickers/";
	public static final String REMOVE_TICKERS_WATCHLIST = "/api/remove_tickers_watchlist/";
	public static final String EDIT_WATCHLIST = "/api/edit_watchlist/";
	public static final String UNFOLLOW_TICKER = "/alert/unfollow_ticker/";
	public static final String FOLLOW = "/alert/follow_ticker/";
	// public static final String FETCH_ALERT_SETTINGS="/api/fetch_alert_settings/";
	public static final String ALERT_NOTIFICATION_CLICK = "/alert/notification_click/";
	public static final String NEW_ALERT_NOTIFICATION = "/alert/new_alert_notification/";

	// private companies
	public static final String FETCH_CB_EXIT_TABLE = "/api/fetch_cb_exit_table/";
	// public static final String FETCH_NEW_COMPANY_HEADER_DATA
	// ="/api/fetch_new_company_header_data/";
	public static final String FETCH_CB_FR_TABLE = "/api/fetch_cb_fr_table/";
	public static final String FETCH_CB_FR_HIGHLIGHTS = "/api/fetch_cb_fr_highlights/";
	public static final String FETCH_CB_FR_cumulative = "/api/fetch_cb_fr_cumulative/";
	public static final String FETCH_CB_INV_TABLE = "/api/fetch_cb_inv_table/";
	public static final String FETCH_CB_INV_QUARTERWISE = "/api/fetch_cb_inv_quarterwise/";
	public static final String FETCH_CB_INV_CATEGORY = "/api/fetch_cb_inv_category/";
	public static final String FETCH_CB_EXIT_CATEGORY = "/api/fetch_cb_exit_category/";
	public static final String FETCH_CB_ACQ_TABLE = "/api/fetch_cb_acq_table/";
	public static final String FETCH_CB_FUNDS_TABLE = "/api/fetch_cb_funds_table/";
	public static final String FETCH_CB_KEY_INVESTORS = "/api/fetch_cb_key_investors/";
	public static final String MSTAR_SET_MAPPING = "/api/mstar_set_mapping/";
	public static final String MSTAR_DEL_MAPPING = "/api/mstar_del_mapping/";
	public static final String MSTAR_GET_ALL_MAPPINGS = "/api/mstar_get_all_mappings/";
	public static final String MSTAR_GET_ALL_INSTRUMENTS = "/api/mstar_get_all_instruments/";

	public static final String FETCHANALYSTRECOMMEND = "/api/fetch_analyst_recommend/";
	public static final String FETCHINSIDER = "/api/fetch_insider/";
	public static final String FETCHETFHOLDINGS = "/api/fetch_etf_holdings/";
	public static final String FETCHCALENDAR = "/api/fetch_calendar/";
	public static final String FETCHSHAREHOLDERS = "/api/fetch_share_holders/";

	// Dashboard

	public static final String CREATE_DASHBOARD = "/api/create_dashboard/";
	public static final String DELETE_DASHBOARD = "/api/delete_dashboard/";
	public static final String GET_DASHBOARD_LIST = "/api/get_dashboard_list/";
	public static final String UPDATE_DASHBOARD_WIDGET = "/api/update_dashboard_widget/";
	public static final String INSTAGRAM_MAPPINGS = "/api/get_instagram_mappings/";
	public static final String DASHBOARD_SHARE="/api/share_entity/";
	public static final String DASHBOARD_DATA="/api/get_dashboard_data/";
	public static final String UPDATE_DB_TOKEN_LIST="/api/update_db_token_list/";
	public static final String CLONE_DASHBOARD="/api/clone_dashboard/";

	// Table x

	public static final String CHAIN_USER_DATA = "/api/tablex_fetch_chain_user_data/";

	// Mobile fin APIs
	public static final String MOBILE_STOCK_DATA = "/api/mobile_stock_data/";
	public static final String MOBILE_FIN_MODEL_YEARLY_NEW = "/api/mobile_fin_model_yearly_new/";
	public static final String FETCH_MOBILE_LIVE_PRICE = "/api/fetch_mobile_live_price/";
	public static final String XBRL_TABLE_WITH_CHANGE = "/api/xbrl_table_with_change/";

	
	//Mobile Main Apis
	public static final String SET_CSRF_COOKIE = "/api/set_csrf_cookie/";
	public static final String CHECK_AUTOUPDATE = "/api/check_autoupdate/";
	public static final String LOGIN_1 = "/api/login_1/";
	
	public static final String FETCH_DOCS_MOBILE = "/api/fetch_docs/";
	public static final String SAVE_FV_MARKET = "/api/save_fv_market/";
	public static final String SAVE_FV_WATCHLIST = "/api/save_fv_watchlist/";
	public static final String ADD_FEEDBACK = "/api/add_feedback/";
	public static final String FIN_WEI_ANALYSIS_MOBILE = "/api/fin_wei_analysis_mobile/";
	public static final String FIN_MARKET_ANALYSIS_MOBILE = "/api/fin_market_analysis_mobile/";
	public static final String RELATIVE_PRICEVSSP = "/api/relativePriceVsSP/";
}