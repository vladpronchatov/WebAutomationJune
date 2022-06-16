package pages;

import base.CommonAPI;
import datafetch.FetchTheSteps;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import reporting.TestLogger;

import java.io.IOException;

public class AllFunctionality {

    LandingPage landingPage = null;
    SectionMenuPage sectionPage = null;
    SearchPage searchPage = null;
    LogInPage logInPage = null;

    String captureUrl = "https://nypost.com/";

    public void logIn(WebDriver driver){
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        landingPage = PageFactory.initElements(driver, LandingPage.class);
        logInPage = PageFactory.initElements(driver, LogInPage.class);
        landingPage.clickOnLogIn();
        logInPage.enterEmailAddress("abc123@gmail.com");
        logInPage.enterPassword("abc123");
        //logInPage.clickOnLogInSubmitButton();
    }
    public void search(WebDriver driver){
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        landingPage = PageFactory.initElements(driver, LandingPage.class);
        landingPage.clickOnSearch();
        searchPage = PageFactory.initElements(driver, SearchPage.class);
        searchPage.searchIconClickNEnterNewsNSubmit();
    }

    public void clickOnSectionMenu(WebDriver driver)throws InterruptedException {
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        landingPage = PageFactory.initElements(driver, LandingPage.class);
        landingPage.clickOnSectionMenu();
    }

    public void sectionsMenu(WebDriver driver, String capturedUrl)throws InterruptedException {
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        try {
            clickOnSectionMenu(driver);
        }catch (Exception ex){
            driver.navigate().to(capturedUrl);
            clickOnSectionMenu(driver);
        }
        sectionPage = PageFactory.initElements(driver,SectionMenuPage.class);
        String headLineNews = sectionPage.goToMetroPage(driver).getHeadLineNewsText();
        System.out.println(headLineNews);
        clickOnSectionMenu(driver);
        sectionPage.goToBusinessPage(driver).getHeadLineNewsText();
        clickOnSectionMenu(driver);
        sectionPage.goToEntertainmentPage(driver).getHeadLineNewsText();
    }

    public void runAllTheFeatureTest(WebDriver driver) throws InterruptedException, IOException {
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        FetchTheSteps fetchTheSteps = new FetchTheSteps();
        String[] featureSteps = fetchTheSteps.getDataFromExcelFile();
        for (int i=1; i<featureSteps.length; i++){
            select(featureSteps[i], driver);
        }
    }
    public void select(String featureName, WebDriver driver)throws InterruptedException, IOException {
        TestLogger.log(getClass().getSimpleName() + ": " + CommonAPI.convertToString(new Object(){}.getClass().getEnclosingMethod().getName()));
        switch(featureName){
            case "signUp":
                logIn(driver);
                break;
            case "sectionsMenu":
                sectionsMenu(driver,captureUrl);
                break;
            case "search":
                search(driver);
                break;
            default:
                throw new InvalidArgumentException("Invalid features choice");
        }
    }
}