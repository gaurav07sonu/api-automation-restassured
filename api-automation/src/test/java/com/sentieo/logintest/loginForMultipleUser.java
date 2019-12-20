package com.sentieo.logintest;

import static com.sentieo.constants.Constants.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.sentieo.rest.base.APIDriver;
import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.assertion.APIAssertions;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.report.ExtentTestManager;

public class loginForMultipleUser extends APIDriver {

	APIAssertions verify = new APIAssertions();
	String userName;

	@BeforeMethod
	public void setUp() {
		verify = new APIAssertions();
	}

	@Test(groups = "sanity", description = "check", dataProvider = "users", dataProviderClass = DataProviderClass.class)
	public void verifyLogin(String email, String password) throws Exception {
		String URI = USER_APP_URL + LOGIN_URL;
		System.out.println(URI + "==================");
		ExtentTestManager.getTest().log(LogStatus.PASS, URI);
	}

}
