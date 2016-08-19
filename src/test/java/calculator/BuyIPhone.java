package calculator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import static org.hamcrest.Matchers.equalTo;

public class BuyIPhone extends BaseTest {

    @Rule
    public ErrorCollector collector = new ErrorCollector();

    @Test
    public void buyIphone() throws Exception {
        driver.get("http://city.com.ua/goods/smartphones/apple-iphone-6s-16gb-rose-gold.html");
        driver.findElement(By.id("buy_credit_btn")).click();
        WebElement frame = driver.findElement(By.xpath(".//iframe[contains(@id,'fancybox-frame')]"));
        driver.switchTo().frame(frame);
        WebElement inputDownPayment = driver.findElement(By.xpath(".//div[@class='downpayment_block']/input"));
        Actions builder = new Actions(driver);
        builder.sendKeys(inputDownPayment, Keys.chord(Keys.CONTROL, "a"), "2000").perform();
        inputDownPayment.sendKeys(Keys.ENTER);

        int pumb = Integer.parseInt(driver.findElement(By.xpath(".//tr[2]/td[3]")).getText().replace(" грн", ""));
        int alfaBank = Integer.parseInt(driver.findElement(By.xpath(".//tr[3]/td[3]")).getText().replace(" грн", ""));
        collector.checkThat(PUMB, equalTo(1519));
        collector.checkThat(alfaBank, equalTo(1479));
        driver.switchTo().defaultContent();
    }
}
