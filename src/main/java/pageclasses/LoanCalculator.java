package pageclasses;

import java.util.List;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import baseclasses.PageBaseClass;
import utilities.ExcelDataFile;

public class LoanCalculator extends PageBaseClass {

    private JavascriptExecutor javascript;

    public LoanCalculator(WebDriver driver, ExtentTest logger) {
        super(driver, logger);
        javascript = (JavascriptExecutor) driver;
    }

    @FindBy(id = "loanamount")
    WebElement loanAmount_textField;

    @FindBy(id = "loaninterest")
    WebElement loanInterest_textField;

    @FindBy(id = "loanterm")
    WebElement loanTenure_textField;

    @FindBy(id = "loanfees")
    WebElement fees_textField;

    @FindBy(xpath = "//*[@id='loansummary-emi']/p/span")
    WebElement loanEmi_value;

    @FindBy(xpath = "//*[@id='loansummary-apr']/p/span")
    WebElement loanApr_value;

    @FindBy(xpath = "//*[@id='loansummary-totalinterest']/p/span")
    WebElement totalInterest_value;

    @FindBy(xpath = "//*[@id='loansummary-totalamount']/p/span")
    WebElement totalPayment_value;

    @FindBy(xpath="//*[@id='loanpaymenttable']/table/tbody/tr[1]")
    WebElement loanPaymentTable_headerRow;
    
    @FindBy(xpath="//*[@id='loanpaymenttable']/table/tbody/tr[contains(@class, 'yearlypaymentdetails')]")
    List<WebElement> loanPaymentTable_dataRows;

    public void setLoanAmount(String amount) {
        try {
            javascript.executeScript("arguments[0].value='" + 0 + "'", loanAmount_textField);
            loanAmount_textField.sendKeys(amount);
            logger.log(Status.PASS, "Entered Loan Amount : " + amount);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void setLoanIntrest(String intrestRate) {
        try {
            loanInterest_textField.clear();
            loanInterest_textField.sendKeys(intrestRate);
            logger.log(Status.PASS, "Entered Loan Interest Rate : " + intrestRate);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void setLoanTenure(String tenure) {
        try {
            javascript.executeScript("arguments[0].value='" + tenure + "'", loanTenure_textField);
            logger.log(Status.PASS, "Entered Loan Tenure : " + tenure);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void setFees(String fees) {
        try {
            fees_textField.clear();
            fees_textField.sendKeys(fees);
            logger.log(Status.PASS, "Entered Fees & Charges : " + fees);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }
    
    public void clickLoanAmountField() {
        loanAmount_textField.click();
    }

    public void verifyLoanEmi(String expectedLoanEmi) {
        try {
            String loanEmi = loanEmi_value.getText();
            Assert.assertEquals(loanEmi, expectedLoanEmi);
            logger.log(Status.PASS,
                    "Actual Loan EMI : " + loanEmi + " - equals to Expected EMI Amount : " + expectedLoanEmi);
            System.out.println("Loan EMI : " + loanEmi);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void verifyLoanApr(String expectedLoanApr) {
        try {
            String loanApr = loanApr_value.getText();
            Assert.assertEquals(loanApr, expectedLoanApr);
            logger.log(Status.PASS,
                    "Actual Loan APR : " + loanApr + " - equals to Expected Loan APR : " + expectedLoanApr);
            System.out.println("Loan APR : " + loanApr);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void verifyTotalInterest(String expectedTotalIntrest) {
        try {
            String totalIntrest = totalInterest_value.getText();
            Assert.assertEquals(totalIntrest, expectedTotalIntrest);
            logger.log(Status.PASS, "Actual Total Intrest : " + totalIntrest + " - equals to Expected Total Intrest : "
                    + expectedTotalIntrest);
            System.out.println("TotalInterest : " + totalIntrest);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void verifyTotaPayment(String expectedTotalPayment) {
        try {
            String totalPayment = totalPayment_value.getText();
            Assert.assertEquals(totalPayment, expectedTotalPayment);
            logger.log(Status.PASS, "Actual Total Payment : " + totalPayment + " - equals to Expected Total Payment : "
                    + expectedTotalPayment);
            System.out.println("Total Payment : " + totalPayment);
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }

    public void handleAd() {
        try {
            logger.log(Status.INFO, "Handling Ad");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            List<WebElement> parentFrames = driver.findElements(By.xpath("//iframe[contains(@id, 'aswift_')]"));
            if (!parentFrames.isEmpty()) {
                driver.switchTo().frame(parentFrames.get(0));
                logger.log(Status.INFO, "Switched to parent ad iframe");

                List<WebElement> childFrames = driver.findElements(By.id("ad_iframe"));
                if (!childFrames.isEmpty()) {
                    driver.switchTo().frame(childFrames.get(0));
                    logger.log(Status.INFO, "Switched to child ad iframe");

                    List<WebElement> dismissButtons = driver.findElements(By.xpath("//div[@id='dismiss-button']/div"));
                    if (!dismissButtons.isEmpty()) {
                        wait.until(ExpectedConditions.elementToBeClickable(dismissButtons.get(0))).click();
                        logger.log(Status.PASS, "Ad Closed");
                    } else {
                        logger.log(Status.INFO, "Dismiss button not found, skipping...");
                    }
                    driver.switchTo().defaultContent();
                } else {
                    logger.log(Status.INFO, "Child ad iframe not found, skipping...");
                    driver.switchTo().defaultContent();
                }
            } else {
                logger.log(Status.INFO, "No ad iframe found, skipping...");
            }
        } catch (Exception e) {
            logger.log(Status.WARNING, "Error while handling ad: " + e.getMessage());
            driver.switchTo().defaultContent();
        }
    }
    
    public void extractDataFromTable(String sheetName) {
        try {
            logger.log(Status.INFO, "Storing data into Excel File in Sheet " + sheetName);
            ExcelDataFile excelfile = new ExcelDataFile(
                    System.getProperty("user.dir") + "\\testdata\\TestOutputData.xlsx");

            if (excelfile.isSheetExist(sheetName)) {
                excelfile.removeSheet(sheetName);
            }
            excelfile.addSheet(sheetName);

            List<WebElement> columns = loanPaymentTable_headerRow.findElements(By.xpath("//th"));

            int colNum = 1;
            for (int i = 0; i < columns.size(); i++) {
                if (!(columns.get(i).getText().isBlank() || columns.get(i).getText().isEmpty())) {
                    System.out.println(columns.get(i).getText());
                    excelfile.setCellData(sheetName, colNum, 1, columns.get(i).getText());
                    colNum++;
                }
            }

            for (int i = 0; i < loanPaymentTable_dataRows.size(); i++) {
                columns = loanPaymentTable_dataRows.get(i).findElements(By.tagName("td"));
                for (int j = 0; j < columns.size(); j++) {
                    System.out.println(columns.get(j).getText());
                    excelfile.setCellData(sheetName, j + 1, i + 2, columns.get(j).getText());
                }
            }
            logger.log(Status.PASS, "Data Stored Successfully");
        } catch (Exception e) {
            reportFail(e.getMessage());
        }
    }
}
