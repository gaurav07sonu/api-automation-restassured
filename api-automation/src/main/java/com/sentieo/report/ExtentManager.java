package com.sentieo.report;

import java.io.File;

import com.relevantcodes.extentreports.ExtentReports;

//ExtentReports extent instance created here. That instance can be reachable by getReporter() method.
 
public class ExtentManager {
	
	public static void main(String[] args) {
		System.out.println(args[0]);
		System.out.println(args[1]);
	}
 
    private static ExtentReports extent;
 
    public synchronized static ExtentReports getReporter(){
        if(extent == null){
            String workingDir = System.getProperty("user.dir");
            extent = new ExtentReports(workingDir+ File.separator + "ExtentReports" + File.separator + "ExtentReportResults.html", true);

        }
        return extent;
    }

}