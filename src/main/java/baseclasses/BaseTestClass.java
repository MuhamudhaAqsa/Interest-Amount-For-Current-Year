package baseclasses;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import utilities.ExtentReportManager;

public class BaseTestClass {

    public WebDriver driver;
    public ExtentReports report = ExtentReportManager.getReportInstance();
    public ExtentTest logger;

    /**
     * Opens the browser using Selenium Manager (no manual driver setup required).
     * 
     * @param browserName name of the browser to be opened
     */
    public void invokeBrowser(String browserName) {
        try {
            if (browserName.equalsIgnoreCase("firefox")) {
                driver = new FirefoxDriver();   // Selenium Manager auto-handles geckodriver
            } else if (browserName.equalsIgnoreCase("edge")) {
                driver = new EdgeDriver();      // Selenium Manager auto-handles msedgedriver
            } else {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options); // Selenium Manager auto-handles chromedriver
            }
        } catch (Exception e) {
            System.out.println("Browser launch failed: " + e.getMessage());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        report.flush();
        if (driver != null) {
            driver.quit();
        }
    }

    /* Holds the execution until page load */
    public void waitForPageLoad() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int i = 0;
        while (i != 180) {
            String pageState = (String) js.executeScript("return document.readyState;");
            if (pageState.equals("complete")) {
                break;
            } else {
                waitLoad(1);
            }
            i++;
        }

        waitLoad(2);

        i = 0;
        while (i != 180) {
            boolean jsState = (boolean) js.executeScript("return window.jQuery != undefined && jQuery.active == 0;");
            if (jsState) {
                break;
            } else {
                waitLoad(1);
            }
            i++;
        }
    }

    /**
     * Holds the execution for given time
     * 
     * @param seconds seconds to wait
     */
    public void waitLoad(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
