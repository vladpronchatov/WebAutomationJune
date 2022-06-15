package base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import reporting.ExtentManager;
import reporting.ExtentTestManager;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CommonAPI {

    //Extent Report
    public static ExtentReports extent;

    @BeforeSuite
    public void extentSetup(ITestContext context) {
        ExtentManager.setOutputDirectory(context);
        extent = ExtentManager.getInstance();
    }

    @BeforeMethod
    public void startExtent(Method method) {
        String className = method.getDeclaringClass().getSimpleName();
        String methodName = method.getName().toLowerCase();
        ExtentTestManager.startTest(method.getName());
        ExtentTestManager.getTest().assignCategory(className);
    }

    protected String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

    @AfterMethod
    public void afterEachTestMethod(ITestResult result) {
        ExtentTestManager.getTest().getTest().setStartedTime(getTime(result.getStartMillis()));
        ExtentTestManager.getTest().getTest().setEndedTime(getTime(result.getEndMillis()));

        for (String group : result.getMethod().getGroups()) {
            ExtentTestManager.getTest().assignCategory(group);
        }

        if (result.getStatus() == 1) {
            ExtentTestManager.getTest().log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == 2) {
            ExtentTestManager.getTest().log(LogStatus.FAIL, getStackTrace(result.getThrowable()));
        } else if (result.getStatus() == 3) {
            ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
        }
        ExtentTestManager.endTest();
        extent.flush();
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(driver, result.getName());
        }
        driver.quit();
    }
    @AfterSuite
    public void generateReport() {
        extent.close();
    }
    private Date getTime(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.getTime();
    }
    //webDriver
    public String browserStackUserName = "vladislavproncha_t4pJdM";
    public String browserStackAccessKey = "sYQ8BHDfdmB6z2JhRAdU";
    public String sauceLabsUserName = "oauth-vp1192-b117f";
    public String sauceLabsAccessKey = "7c083fcb-7aa0-4435-a390-06f134335f76";

    public static WebDriver driver = null;

    @Parameters({"useCloudEnv", "envName", "ps", "os_version", "browserName", "browserVersion", "url"})
    @BeforeMethod
    public void setUp(String useCloudEnv, String envName, String OS,String os_version, String browserName,
                      String browserVersion, String url) throws MalformedURLException {
        if(useCloudEnv.equalsIgnoreCase("true")){
            //get cloud driver
            getCloudDriver(envName, OS, os_version, browserName, browserVersion);
        }else{
            //get local driver
            getLocalDriver(OS,browserName);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get(url);
        driver.manage().window().maximize();
    }
    public WebDriver getLocalDriver(String OS, String browserName){
        if(browserName.equalsIgnoreCase("chrome")){
            if(OS.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.chrome.driver", "/Users/vlad/Documents/chromedriver/chromedriver-2.exe");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }else if(OS.equalsIgnoreCase("Windows")){
                System.setProperty("webdriver.chrome.driver", "Generic/driver/chromedriver.exe");
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            }
        }else if(browserName.equalsIgnoreCase("firefox")){
            if(OS.equalsIgnoreCase("OS X")){
                System.setProperty("webdriver.gecko.driver", "/Users/vlad/Documents/firefoxgecko/geckodriver 2.exe");
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }else if(OS.equalsIgnoreCase("Windows")){
                System.setProperty("webdriver.gecko.driver", "Generic/driver/geckodriver.exe");
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            }
        }else if(browserName.equalsIgnoreCase("ie")){
            System.setProperty("webdriver.ie.driver", "Generic/driver/internetexplorerdriver.exe");
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();

        }else if(browserName.equalsIgnoreCase("safari")){
            driver = new SafariDriver();
        }
        return driver;
    }
    public WebDriver getCloudDriver(String envName, String OS,String os_version,
                                    String browserName, String browserVersion) throws MalformedURLException {

        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("browser",browserName);
        cap.setCapability("browserVersion",browserVersion);
        cap.setCapability("os", OS);
        cap.setCapability("os_version", os_version);
        if(envName.equalsIgnoreCase("Browserstack")){
            driver = new RemoteWebDriver(new URL("http://"+browserStackUserName+":"+browserStackAccessKey+
                    "@hub-cloud.browserstack.com/wd/hub"),cap);
        }else if(envName.equalsIgnoreCase("Saucelabs")) {
            driver = new RemoteWebDriver(new URL("http://" + sauceLabsUserName + ":" + sauceLabsAccessKey +
                    "@ondemand.saucelabs.com:80/wd/hub"), cap);
        }

        return driver;
    }

    @AfterMethod
    public void cleanUp() {driver.close();}

    //Selenium API's
    public static void typeOnWebElement(String locator, String value) {
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value);
        } catch (Exception ex1) {
            try {
                driver.findElement(By.className(locator)).sendKeys(value);
            } catch (Exception ex2) {
                try {
                    driver.findElement(By.id(locator)).sendKeys(value);
                } catch (Exception ex3) {
                    driver.findElement(By.xpath(locator)).sendKeys(value);
                }
            }
        }
    }
    public static void typeOnWebElementNEnter(String locator, String value){
        try {
            driver.findElement(By.cssSelector(locator)).sendKeys(value, Keys.ENTER);
        }catch (Exception ex1){
            try{
                driver.findElement(By.className(locator)).sendKeys(value, Keys.ENTER);
            }catch (Exception ex2){
                try {
                    driver.findElement(By.id(locator)).sendKeys(value, Keys.ENTER);
                }catch (Exception ex3){
                    driver.findElement(By.xpath(locator)).sendKeys(value, Keys.ENTER);
                }
            }
        }
    }
    public void clearInputBox(String locator){
        try {
            driver.findElement(By.cssSelector(locator)).clear();
        }catch (Exception ex1){
            try{
                driver.findElement(By.id(locator)).clear();
            }catch (Exception ex2){
                try {
                    driver.findElement(By.className(locator)).clear();
                }catch (Exception ex3){
                    driver.findElement(By.xpath(locator)).clear();
                }
            }
        }
    }

    public List<String> getListOfTest(String locator){
        List<WebElement> webElementList = driver.findElements(By.cssSelector(locator));
        List<String> listOfText = new ArrayList<>();
        for(WebElement element:webElementList){
            listOfText.add(element.getText());
        }
        return listOfText;
    }

    public void clickOnWebElement(String locator){
        try {
            driver.findElement(By.cssSelector(locator)).click();
        }catch (Exception ex1){
            try{
                driver.findElement(By.className(locator)).click();
            }catch (Exception ex2){
                try{
                    driver.findElement(By.id(locator)).click();
                }catch (Exception ex3){
                    driver.findElement(By.xpath(locator)).click();
                }
            }
        }
    }
    public String getWebText(String locator){
        String text = "";
        try {
            text =  driver.findElement(By.cssSelector(locator)).getText();
        }catch (Exception ex1){
            try{
                text = driver.findElement(By.className(locator)).getText();
            }catch (Exception ex2){
                try{
                    text = driver.findElement(By.id(locator)).getText();
                }catch (Exception ex3){
                    text =  driver.findElement(By.xpath(locator)).getText();
                }
            }
        }
        return text;
    }
    public List<WebElement> getListOfWebElements(String locator){
        List<WebElement> elementList = driver.findElements(By.cssSelector(locator));
        return elementList;
    }
    public List<String> getListOfText(List<WebElement> elements){
        List<String> listOfText = new ArrayList<String>();
        for(WebElement element:elements){
            listOfText.add(element.getText());
        }
        return listOfText;
    }
    public void printText(List<String> list){
        for(String st:list){
            System.out.println(st);
        }
    }

    public void sleepFor(int sec)throws InterruptedException{
        Thread.sleep(1000 * sec);
    }

    //Synchronization
    public static void waitUntilClickAble(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void waitUntilVisible(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitUntilSelectable(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));
    }
    public static void waitUntilClickAble(WebElement locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        }catch(Exception ex){
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        }
    }
    public static void waitUntilVisible(WebElement locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement element = wait.until(ExpectedConditions.visibilityOf(locator));
    }

    public static void waitUntilSelectable(WebElement locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        boolean element = wait.until(ExpectedConditions.elementToBeSelected(locator));
    }
    public static void captureScreenshot(WebDriver driver, String screenshotName) {
        DateFormat df = new SimpleDateFormat("(MM.dd.yyyy-HH:mma)");
        Date date = new Date();
        df.format(date);

        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(System.getProperty("/Users/vlad/Documents") + "/SeleniumScreenshots/" + screenshotName + " " + df.format(date) + ".png"));
            System.out.println("Screenshot captured");
        } catch (Exception e) {
            System.out.println("Exception while taking screenshot " + e.getMessage());
            ;
        }
    }

    public static String convertToString(String st) {
        String splitString = "";
        splitString = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(st), ' ');
        return splitString;
    }
}
