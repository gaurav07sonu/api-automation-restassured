package com.sentieo.listener;

import com.relevantcodes.extentreports.LogStatus;
import com.sentieo.report.ExtentManager;
import com.sentieo.report.ExtentTestManager;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
 
 
public class TestListener implements ITestListener {
 
    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
    
    private static String getMethodLabel(String text, String params) {
    	return "<font size=\"2\">" + text + " ("  + params +  ") <br/><br/>";
        //return "<font size=\"2\">" + text + " (Test) <br/><br/>";
    }
    
    private static String getClassLabel(String text) {
        return "<font size=\"2\">" + text.substring(text.lastIndexOf(".") + 1, text.length()) + " (Class) <br/><br/>";
    }
    
    //<span class="label label-primary">Primary Label</span>
    
    //Before starting all tests, below method runs.
    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("I am in onStart method " + iTestContext.getName());
        //ExtentTestManager.startTest(iTestContext.getCurrentXmlTest().getName(), desc);
    }
 
    //After ending all tests, below method runs.
    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("I am in onFinish method " + iTestContext.getName());
        //Do tier down operations for extentreports reporting!
        ExtentTestManager.endTest();
        ExtentManager.getReporter().flush();
    }
 
    @Override
    public void onTestStart(ITestResult iTestResult) {
    	Object[] params = iTestResult.getParameters();
    	StringBuffer sb = new StringBuffer();
    	for (int i=0;i<params.length;i++) {
    		if(!StringUtils.isEmpty(params[i].toString())) {
	    		sb.append(params[i].toString());
	    		if(i<params.length-1) {
	    			sb.append(" , ");
	    		}
    		}
		}
    	String paramsList = sb.toString();
    	if(paramsList.length()>100)
    	paramsList = paramsList.substring(0, Math.min(paramsList.length(), 100));
    	String methodDesc = getMethodLabel(iTestResult.getMethod().getMethodName(), paramsList);
    	String classDesc = getClassLabel(iTestResult.getTestClass().getName());
    	
    	String description = "<u>Test description</u> --> " + iTestResult.getMethod().getDescription();
        System.out.println("I am in onTestStart method " +  getTestMethodName(iTestResult) + " start");
        //Start operation for extentreports.
        ExtentTestManager.startTest( classDesc + methodDesc ,"");
        ExtentTestManager.getTest().setDescription(description);
    }
 
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("I am in onTestSuccess method " +  getTestMethodName(iTestResult) + " succeed");
        //Extentreports log operation for passed tests.
        ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
    }
 
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("I am in onTestFailure method " +  getTestMethodName(iTestResult) + " failed");
 
       
    }
 
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("I am in onTestSkipped method "+  getTestMethodName(iTestResult) + " skipped");
        //Extentreports log operation for skipped tests.
        ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
    }
 
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }
 
}