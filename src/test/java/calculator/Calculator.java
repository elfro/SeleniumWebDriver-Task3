package calculator;

import dataproviders.DataProviderClass;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import org.testng.Assert;
import org.openqa.selenium.*;

import java.util.concurrent.TimeUnit;

public class Calculator {
    private WebDriver driver;
    private String baseUrl;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\src\\test\\resources\\geckodriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        baseUrl = "http://www.paycheckcity.com/";
    }

    @Test(groups = {"regression", "p1"})
    public void testSalary() throws Exception {
        String expectedNetPayResult = "$8,387.79";
        driver.get(baseUrl + "calculator/salary/");
        WebElement calcDateElement = driver.findElement(By.id("calcDate"));
        calcDateElement.clear();
        calcDateElement.sendKeys("08/31/2016");
        driver.findElement(By.xpath(".//div[@id='widget_state']//input[@class='dijitReset dijitInputField dijitArrowButtonInner']")).click();
        driver.findElement(By.xpath(".//div[@id='state_popup']//div[text()='California']")).click();
        WebElement grossPayElement = driver.findElement(By.id("generalInformation.grossPayAmount"));
        grossPayElement.clear();
        grossPayElement.sendKeys("10000");
        driver.findElement(By.xpath(".//div[@id='widget_generalInformation.grossPayMethodType']//input[@class=\"dijitReset dijitInputField dijitArrowButtonInner\"]")).click();
        driver.findElement(By.xpath(".//div[@id=\"generalInformation.grossPayMethodType_popup\"]//div[text()='Pay Per Period']")).click();
        driver.findElement(By.xpath(".//div[@id='widget_generalInformation.payFrequencyType']//input[@class=\"dijitReset dijitInputField dijitArrowButtonInner\"]")).click();
        driver.findElement(By.xpath(".//div[@id=\"generalInformation.payFrequencyType_popup\"]//div[text()='Monthly']")).click();

        driver.findElement(By.xpath(".//input[@name='generalInformation.exemptFederal']")).click();
        driver.findElement(By.id("calculate")).click();
        Wait fluenWait = new FluentWait(driver)
                .withTimeout(300, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        fluenWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//div[@id=\"dijit_TitlePane_1_pane\"]//span[@class=\"resultDef\" and text()=\"Net Pay\"]")));
        String actualNetPayResult = driver.findElement(By.xpath(".//div[@id=\"dijit_TitlePane_1_pane\"]//span[@class=\"resultData\"]")).getText();
        Assert.assertEquals(expectedNetPayResult, actualNetPayResult);

    }

    @Test(groups = {"regression", "p1"}, dataProvider = "hourlyData", dataProviderClass = DataProviderClass.class)
    public void testHourly(String payRate,
                           String hours,
                           String payRate1,
                           String hours1,
                           String expectedGrossPay) throws Exception {
        driver.get(baseUrl + "calculator/hourly/");
        WebElement payRateElement = driver.findElement(By.id("calcType.rates0.payRate"));
        payRateElement.clear();
        payRateElement.sendKeys(payRate);
        WebElement hoursElement = driver.findElement(By.id("calcType.rates0.hours"));
        hoursElement.clear();
        hoursElement.sendKeys(hours);
        driver.findElement(By.id("addRate")).click();
        WebElement payRateElement1 = driver.findElement(By.id("calcType.rates1.payRate"));
        payRateElement1.clear();
        payRateElement1.sendKeys(payRate1);
        WebElement hoursElement1 = driver.findElement(By.id("calcType.rates1.hours"));
        hoursElement1.clear();
        hoursElement1.sendKeys(hours1);
        driver.findElement(By.id("calculate")).click();

        By grossPayLocator = By.xpath(".//span[@class=\"resultDef\" and text()=\"Gross Pay\"]");
        (new WebDriverWait(driver, 1000))
                .until(ExpectedConditions.visibilityOfElementLocated(grossPayLocator));
        String actualResultGrossPay = driver.findElement(By.xpath(".//span[@class=\"resultDef\" and text()=\"Gross Pay\"]/following-sibling::node()[2]")).getText();
        Assert.assertEquals(actualResultGrossPay, expectedGrossPay);

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}