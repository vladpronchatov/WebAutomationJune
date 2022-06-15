package home;

import base.CommonAPI;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPageTitle extends CommonAPI {

    @Test
    public void pageTitle() throws InterruptedException {
        String actualTitle = "Amazon.com. Spend less. Smile more.";
        String expectedTitle = driver.getTitle();
        Assert.assertEquals(actualTitle,expectedTitle);
    }
}