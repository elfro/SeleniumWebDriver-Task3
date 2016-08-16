package dataproviders;

import org.testng.annotations.DataProvider;

public class DataProviderClass {

    @DataProvider(name = "calculatorData")
    public static Object[][] calculatorData() {
        String calcDate = "08/31/2016";
        String state = "California";
        String grossPay = "10000";
        String groosPayType = "Pay Per Period";
        String payFrequency = "Monthly";
        String iAmExemptFrom = "Federal";
        String expectedNetPayResult = "$8,387.79";

        return new Object[][]{{calcDate, state, grossPay, groosPayType, payFrequency, iAmExemptFrom, expectedNetPayResult}};
    }

    @DataProvider(name = "hourlyData")
    public static Object[][] hourlyData() {
        String payRate = "50";
        String hours = "160";
        String payRate1 = "80";
        String hours1 = "8";
        String expectedGrossPay = "$8,640.00";

        return new Object[][]{{payRate, hours, payRate1, hours1, expectedGrossPay}};
    }
}

