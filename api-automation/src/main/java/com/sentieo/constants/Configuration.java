package com.sentieo.constants;

public class Configuration {
	private String envName;
    private String appURL;
    private String userName;
    private String password;
    private String userAppUrl;
	public String getAppURL() {
		return appURL;
	}
	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public String getUserAppUrl() {
		return userAppUrl;
	}
	public void setUserAppUrl(String userAppUrl) {
		this.userAppUrl = userAppUrl;
	}
	
}
