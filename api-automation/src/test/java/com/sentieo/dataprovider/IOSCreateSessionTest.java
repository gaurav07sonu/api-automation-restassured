package com.sentieo.dataprovider;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Random;

public class IOSCreateSessionTest {
    private IOSDriver<WebElement> driver;
    private static AppiumDriverLocalService service;
    private AppiumServiceBuilder builder;

    @BeforeSuite
    public void setUp() throws Exception {
    	
    	Random rand = new Random(); 
		  
        int rand_int1 = rand.nextInt(9999); 
        
        File classpathRoot = new File(System.getProperty("user.dir"));
    	
        System.out.println(System.getProperty("user.dir"));
		
        File appDir = new File(classpathRoot, "/app");
        File app = new File(appDir.getCanonicalPath(), "TestApp-iphonesimulator.app");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("noReset", "false");
		capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.12.1");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.2");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone XR");
		//capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
		capabilities.setCapability("setWebContentsDebuggingEnabled", true);
		capabilities.setCapability("newCommandTimeout", 2000);
		capabilities.setCapability("useNewWDA", false);
		
		builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		builder.usingPort(rand_int1);
		builder.withCapabilities(capabilities);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL, "debug");
		
        service = AppiumDriverLocalService.buildService(builder);
        service.start();
        driver = new IOSDriver<WebElement>(service.getUrl(), capabilities);
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testCreateSession () {
        // Check that the XCUIElementTypeApplication was what we expect it to be
        IOSElement applicationElement = (IOSElement) driver.findElementByClassName("XCUIElementTypeApplication");
        String applicationName = applicationElement.getAttribute("name");
        Assert.assertEquals(applicationName, "TestApp");
    }
}