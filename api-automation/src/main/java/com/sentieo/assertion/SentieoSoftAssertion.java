package com.sentieo.assertion;

import java.util.ArrayList;
import java.util.List;

import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.report.ExtentTestManager;
import com.sentieo.utils.CoreCommonException;


public abstract class SentieoSoftAssertion {

	protected static final String BREAK_LINE = "</br>";
    public List<Throwable> verificationFailures = new ArrayList<Throwable>();

	public List<Throwable> getVerificationFailures() {
		return verificationFailures; 
	}
	
	public void verifyAll() throws CoreCommonException{
		if (!this.getVerificationFailures().isEmpty()) {
			int size = this.getVerificationFailures().size();
            // if there's only one failure just set that
            if (size == 1) {
            	throw new CoreCommonException(((Throwable) this.getVerificationFailures().get(0)).getMessage());
            } else if(size!=0) {
                // create a failure message with all failures and stack
                // traces (except last failure)
                StringBuffer failureMessage = new StringBuffer("Multiple validation failures (").append(size).append("):"+BREAK_LINE);
                ExtentTestManager.getTest().log(LogStatus.FAIL, failureMessage.toString());
                // set merged throwable
                Throwable merged = new Throwable(failureMessage.toString());
                throw new CoreCommonException(merged.getMessage());
            }
		}
	}

}
