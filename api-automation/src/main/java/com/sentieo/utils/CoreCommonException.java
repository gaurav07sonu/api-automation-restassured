package com.sentieo.utils;

import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.report.ExtentTestManager;

public class CoreCommonException extends Exception {
	 
	 private static final long serialVersionUID = -6725534691869595212L;

	 public CoreCommonException(String message) {
	        super(message);
	        if(!message.equals("expected [true] but found [false]"))
	        	ExtentTestManager.getTest().log(LogStatus.FAIL, message);
	    }

	 public CoreCommonException(String message, Throwable cause) {
	  super(message, cause);
	 }

	 public CoreCommonException(Exception cause) {
	  super(cause);
	 }
}
