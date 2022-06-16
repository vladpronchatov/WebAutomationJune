package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class SportsPage {
    private final WebDriver driver;
    public SportsPage(WebDriver driver) { this.driver = driver; }

    @FindBy(how = How.CSS, using = "")
    public static WebElement headlineNewsWebElement;

    public String getHeadLineNewsText() {}
    String headLine = headlineNewsWebElement.getText();

    return headLine;

}