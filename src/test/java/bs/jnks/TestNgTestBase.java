package bs.jnks;

import core.SuiteConfiguration;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;

/**
 * Base class for TestNG-based test classes
 */
public class TestNgTestBase {

    protected static String baseUrl;
    protected static Capabilities capabilities;

    protected WebDriver driver;

    @BeforeSuite
    public void initTestSuite() throws IOException {
        SuiteConfiguration config = new SuiteConfiguration();
        baseUrl = config.getProperty("site.url");
        capabilities = config.getCapabilities();
    }
}
