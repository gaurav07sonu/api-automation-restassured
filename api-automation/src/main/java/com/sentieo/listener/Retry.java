package com.sentieo.listener;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
 
public class Retry implements IRetryAnalyzer {
 
    private int count = 0;
    private static int maxTry = 0; //Run the failed test 2 times
    AtomicInteger countAtomic = new AtomicInteger(maxTry);
 
//    @Override
//    public boolean retry(ITestResult iTestResult) {
//        if (!iTestResult.isSuccess()) {                      //Check if test not succeed
//            if (count < maxTry) {                            //Check if maxtry count is reached
//                count++;                                     //Increase the maxTry count by 1
//                iTestResult.setStatus(ITestResult.FAILURE);  //Mark test as failed
//                //extendReportsFailOperations(iTestResult);    //ExtentReports fail operations
//                return true;                                 //Tells TestNG to re-run the test
//            }
//        } else {
//            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
//        }
//        return false;
//    }
    
    
    @Override
    public boolean retry(ITestResult iTestResult) {
        boolean retry = false;
        if (isRetryAvailable()) {
            System.out.println("Going to retry test case: " + iTestResult.getMethod() + ", " + (maxTry - countAtomic.intValue() + 1) + " out of " + maxTry);
            retry = true;
            countAtomic.decrementAndGet();
        }
        else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return retry;
    }
    
    public boolean isRetryAvailable() {
        return (countAtomic.intValue() > 0);
    }
 

}