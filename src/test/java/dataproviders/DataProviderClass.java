package dataproviders;

import org.testng.annotations.DataProvider;

public class DataProviderClass {

    @DataProvider(name = "hourlyData")
    public static Object[][] hourlyData() {
        return new Object[][]{{"50", "160", "80", "8", "$8,640.00"}, {"90", "9", "110", "10", "$1,910.00"}};
    }
}

