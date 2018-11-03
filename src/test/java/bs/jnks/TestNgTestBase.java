package bs.jnks;

import core.SuiteConfiguration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.URL;

/**
 * Base class for TestNG-based test classes
 */
public class TestNgTestBase {

    static String baseUrl;
    private static Capabilities capabilities;
    WebDriver driver;
    private static final String USERNAME = "";
    private static final String AUTOMATE_KEY = "";
    private static final String URLB = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    @BeforeSuite
    public void initTestSuite() throws IOException {
        SuiteConfiguration config = new SuiteConfiguration();
        baseUrl = config.getProperty("site.url");
        capabilities = config.getCapabilities();
        driver = new RemoteWebDriver(new URL(URLB), capabilities);
    }

    @AfterSuite
    public void tearDown(){
        driver.quit();
    }
}
