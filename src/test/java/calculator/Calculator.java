package calculator;

import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.junit.runners.Parameterized;
import org.junit.runner.RunWith;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class Calculator {
    private static WebDriver driver;
    private static String baseUrl;

    private String calcDate;
    private String state;
    private String grossPay;
    private String grossPayType;
    private String payFrequency ;
    private Boolean iAmExemptFrom;
    private double expectedNetPayResult;

    @Parameterized.Parameters
    public  static Collection salaryData() {
        return Arrays.asList(
                new Object[][]{
                        { "12/31/2016", "Alaska", "1999", "Pay Per Period", "Weekly", false, 1422.37 },
                        { "12/31/2017", "Hawaii", "75000", "Annually", "Annual", false, 49701.43}
                }
        );
    }

    public Calculator (String calcDate, String state, String grossPay, String grossPayType, String payFrequency, Boolean iAmExemptFrom, double expectedNetPayResult) {
        this.calcDate=calcDate;
        this.state = state;
        this.grossPay = grossPay;
        this.grossPayType = grossPayType;
        this.payFrequency = payFrequency;
        this.iAmExemptFrom = iAmExemptFrom;
        this.expectedNetPayResult = expectedNetPayResult;
    }

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\geckodriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        baseUrl = "http://www.paycheckcity.com/";
    }

    @Test
    public void testSalary() throws Exception {
        driver.get(baseUrl + "calculator/salary/");
        WebElement calcDateElement = driver.findElement(By.id("calcDate"));
        calcDateElement.clear();
        calcDateElement.sendKeys(calcDate);
        driver.findElement(By.xpath(".//div[@id='widget_state']//input[@class='dijitReset dijitInputField dijitArrowButtonInner']")).click();
        driver.findElement(By.xpath(".//div[@id='state_popup']//div[text()='"+state+"']")).click();
        WebElement grossPayElement = driver.findElement(By.id("generalInformation.grossPayAmount"));
        grossPayElement.clear();
        grossPayElement.sendKeys(grossPay);
        driver.findElement(By.xpath(".//div[@id='widget_generalInformation.grossPayMethodType']//input[@class=\"dijitReset dijitInputField dijitArrowButtonInner\"]")).click();
        driver.findElement(By.xpath(".//div[@id=\"generalInformation.grossPayMethodType_popup\"]//div[text()='"+grossPayType+"']")).click();
        driver.findElement(By.xpath(".//div[@id='widget_generalInformation.payFrequencyType']//input[@class=\"dijitReset dijitInputField dijitArrowButtonInner\"]")).click();
        driver.findElement(By.xpath(".//div[@id=\"generalInformation.payFrequencyType_popup\"]//div[text()='"+payFrequency+"']")).click();
        Boolean valueOfCheckBox = driver.findElement(By.xpath(".//input[@name='generalInformation.exemptFederal']")).isSelected();
        if (iAmExemptFrom != valueOfCheckBox)
        { driver.findElement(By.xpath(".//input[@name='generalInformation.exemptFederal']")).click(); }

        driver.findElement(By.id("calculate")).click();
        Wait fluenWait = new FluentWait(driver)
                .withTimeout(300, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        fluenWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id=\"dijit_TitlePane_1_pane\"]//span[@class=\"resultDef\" and text()=\"Net Pay\"]")));
        double actualNetPayResult = Double.parseDouble(driver.findElement(By.xpath(".//div[@id=\"dijit_TitlePane_1_pane\"]//span[@class=\"resultData\"]")).getText().replace("$","").replace(",",""));
        assertThat(actualNetPayResult, is(closeTo(expectedNetPayResult, 5)));
    }

    @AfterClass
    public static void tearDown() throws Exception {
        driver.quit();
    }
}