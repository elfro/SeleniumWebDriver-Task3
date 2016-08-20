package calculator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

@RunWith(Parameterized.class)
public class Calculator extends BaseTest {

    private String calcDate;
    private String state;
    private String grossPay;
    private String grossPayType;
    private String payFrequency;
    private Boolean iAmExemptFrom;
    private double expectedNetPayResult;

    @Parameterized.Parameters
    public static Collection salaryData() {
        return Arrays.asList(
                new Object[][]{
                        {"12/31/2016", "Alaska", "1999", "Pay Per Period", "Weekly", false, 1422.37},
                        {"12/31/2017", "Hawaii", "75000", "Annually", "Annual", false, 49701.43}
                }
        );
    }

    public Calculator(String calcDate, String state, String grossPay, String grossPayType, String payFrequency, Boolean iAmExemptFrom, double expectedNetPayResult) {
        this.calcDate = calcDate;
        this.state = state;
        this.grossPay = grossPay;
        this.grossPayType = grossPayType;
        this.payFrequency = payFrequency;
        this.iAmExemptFrom = iAmExemptFrom;
        this.expectedNetPayResult = expectedNetPayResult;
    }


    @Test
    public void testSalary() throws Exception {
        driver.get("http://www.paycheckcity.com/calculator/salary/");
        WebElement calcDateElement = driver.findElement(By.id("calcDate"));
        calcDateElement.clear();
        calcDateElement.sendKeys(calcDate);
        WebElement stateElement = driver.findElement(By.id("state"));
        stateElement.clear();
        stateElement.sendKeys(state);
        WebElement grossPayElement = driver.findElement(By.id("generalInformation.grossPayAmount"));
        grossPayElement.clear();
        grossPayElement.sendKeys(grossPay);
        WebElement grossPayTypeElement = driver.findElement(By.id("generalInformation.grossPayMethodType"));
        grossPayTypeElement.clear();
        grossPayTypeElement.sendKeys(grossPayType);
        WebElement payFrequencyElement = driver.findElement(By.id("generalInformation.payFrequencyType"));
        payFrequencyElement.clear();
        payFrequencyElement.sendKeys(payFrequency);
        Boolean valueOfCheckBox = driver.findElement(By.xpath(".//input[@name='generalInformation.exemptFederal']")).isSelected();
        if (iAmExemptFrom != valueOfCheckBox) {
            driver.findElement(By.xpath(".//input[@name='generalInformation.exemptFederal']")).click();
        }

        driver.findElement(By.id("calculate")).click();
        Wait fluenWait = new FluentWait(driver)
                .withTimeout(300, TimeUnit.SECONDS)
                .pollingEvery(2, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        By netPayValue = By.xpath(".//div[@id=\"dijit_TitlePane_1_pane\"]//span[@class=\"resultData\"]");
        fluenWait.until(ExpectedConditions.visibilityOfElementLocated(netPayValue));
        double actualNetPayResult = Double.parseDouble(driver.findElement(netPayValue).getText().replace("$", "").replace(",", ""));
        assertThat(actualNetPayResult, is(closeTo(expectedNetPayResult, 5)));
    }
}