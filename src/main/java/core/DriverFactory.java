package core;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static core.DriverConfig.getPropertyValue;

public class DriverFactory {
    private WebDriver webDriver;

    public WebDriver getWebDriver() {
//        switch (driver) {
//            case CHROME:
//                webDriver = getChromeDriver();
//                webDriver.manage().window().maximize();
//                break;
//            case FIREFOX:
//                webDriver = getFirefoxDriver();
//                webDriver.manage().window().maximize();
//                break;
//            case IE:
//                webDriver = getInternetExplorerDriver();
//                webDriver.manage().window().maximize();
//                break;
//            case SAFARI:
//                webDriver = new SafariDriver();
//                webDriver.manage().window().setSize(new Dimension(1920, 1080));
//                break;
//            case PHANTOMJS:
//                webDriver = getPhantomJs();
//                webDriver.manage().window().setSize(new Dimension(1920, 1080));
//                break;
//        }
        webDriver.manage().deleteAllCookies();
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        return webDriver;
    }

    public WebDriver getWebDriver(String driverName) {
        System.out.println("Set Up browser: " + driverName);
        return webDriver;
    }

    private WebDriver getFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", getPropertyValue("firefox.driver"));
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addPreference("security.sandbox.content.level", "4");
        firefoxOptions.addPreference("webdriver.firefox.marionette", true);
        firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setPreference("browser.download.dir", "temp");
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/pdf,application/x-pdf");
        profile.setPreference("pdfjs.disabled", true);
        firefoxOptions.setProfile(profile);
        return new FirefoxDriver(firefoxOptions);
    }

    private WebDriver getChromeDriver() {
        System.setProperty("webdriver.chrome.driver", getPropertyValue("chrome.driver"));
        ChromeOptions optionsChrome = new ChromeOptions();
        optionsChrome.addArguments("start-maximized");
        optionsChrome.addArguments("test-type");
        optionsChrome.addArguments("disable-infobars");
        optionsChrome.addArguments("--disable-extensions");
        optionsChrome.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
        return new ChromeDriver(optionsChrome);
    }

    private WebDriver getInternetExplorerDriver() {
        System.setProperty("webdriver.ie.driver", getPropertyValue("ie.driver"));
        DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
        caps.setCapability(InternetExplorerDriver.SILENT, true);
        caps.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, UnexpectedAlertBehaviour.ACCEPT);
        caps.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        caps.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
        caps.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        caps.setCapability(InternetExplorerDriver.BROWSER_ATTACH_TIMEOUT, 15000);
        caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        caps.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        caps.setJavascriptEnabled(true);
        return new InternetExplorerDriver(new InternetExplorerOptions(caps));
    }

    private WebDriver getPhantomJs() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                getPropertyValue("ghost.driver"));
        caps.setCapability("takesScreenshot", true);
        caps.setJavascriptEnabled(true);
        caps.setCapability("unhandledPromptBehavior", "accept");
        caps.setCapability("phantomjs.page.settings.userAgent",
                "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/52.0");
        return new PhantomJSDriver(caps);
    }
}
