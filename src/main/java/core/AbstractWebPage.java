package core;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Abstract class representation of a AbstractWebPage in the UI. AbstractWebPage object pattern
 */
public class AbstractWebPage {
    protected WebDriver driver;
    protected int defaultTimeout = 15;
    protected int shortTimeout = 2;

    /*
     * Constructor injecting the WebDriver interface
     *
     * @param webDriver
     */
    public AbstractWebPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public boolean isTitleCorrect(String title) {
        return StringUtils.equalsIgnoreCase(getTitle(), title);
    }

    protected void waitForPageToLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = driver1 -> ((JavascriptExecutor) driver1).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout * 2);
        wait.until(pageLoadCondition);
    }

    protected void waitForPageTitleIs(String pageTitle) throws Exception {
        try {
            new WebDriverWait(driver, defaultTimeout).until(ExpectedConditions.titleIs(pageTitle));
        } catch (UnhandledAlertException alertE) {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
            throw new Exception(String
                    .format("Expected condition failed: waiting for title to be \"%s\". Current title: \"%s\". Tried for %s second(s) ",
                            pageTitle,
                            driver.getTitle(),
                            defaultTimeout));
        }
    }

    protected void sleepFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected boolean isElementPresent(By byLocator) {
        WebDriverWait wait = new WebDriverWait(driver, shortTimeout);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
            return true;
        } catch (Exception e) {
            if (StringUtils.containsIgnoreCase(e.getCause().getMessage(), "invalid selector"))
                System.out.println(e.getCause().getMessage());
            return false;
        }
    }

    protected boolean isElementPresent(By byLocator, int timeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeInSeconds);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(byLocator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected WebElement waitForWebElementToBeDisplayed(By locator, int time) {
        return (new WebDriverWait(driver, time))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected Boolean waitForWebElementNotToBeDisplayed(By locator, int time) {
        return (new WebDriverWait(driver, time))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected boolean isElementPresentAndDisplayed(By byLocator) {
        if (isElementPresent(byLocator)) {
            WebElement webElement = driver.findElement(byLocator);
            return webElement.isDisplayed();
        }
        return false;
    }

    protected void findAndClickWebElementBy(By locator) throws Exception {
        Actions action = new Actions(driver);
        try {
            System.out.println("Try to click: " + locator.toString());
            action
                    .moveToElement(waitForWebElementToBeDisplayed(locator, shortTimeout))
                    .pause(200)
                    .click(driver.findElement(locator))
                    .pause(100)
                    .perform();
        } catch (Exception e) {
            throw new Exception("Find and click action could not be performed for element: " + locator.toString());
        }
    }

    protected void findAndClickWebElementBy(By locator, int timeOut) throws Exception {
        Actions action = new Actions(driver);
        try {
            System.out.println(String.format("Try to click for %s seconds: ", timeOut) + locator.toString());
            action
                    .moveToElement(driver.findElement(locator))
                    .pause(200)
                    .click(driver.findElement(locator))
                    .pause(100)
                    .perform();
        } catch (Exception e) {
            throw new Exception("Find and click action could not be performed for element: " + locator.toString());
        }
    }

    protected void findAndClickWebElementBy(WebElement webElement) throws Exception {
        Actions action = new Actions(driver);
        try {
            action.moveToElement(webElement).pause(200).build().perform();
            webElement.click();
        } catch (Exception e) {
            throw new Exception("Find and click action could not be performed for element: " + webElement.toString());
        }
    }

    protected void clickIfPresent(By element) {
        if (isElementPresent(element)) {
            try {
                findAndClickWebElementBy(element);
            } catch (Exception ignored) {
            }
        }
    }

    protected void clickIfPresent(By element, int timeOut) {
        if (isElementPresent(element, timeOut)) {
            try {
                findAndClickWebElementBy(element, timeOut);
            } catch (Exception ignored) {
            }
        }
    }

    protected boolean isDialogPresent() {
        WebDriverWait wait = new WebDriverWait(driver, defaultTimeout * 2);
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void selectOptionOnPage(By locator, int index) {
        Select selectProperty = new Select(driver.findElement(locator));
        selectProperty.selectByIndex(index);
    }

    public void selectOptionOnPage(WebElement locator, int index) {
        Select selectProperty = new Select(locator);
        selectProperty.selectByIndex(index);
    }

    public void selectOptionByValue(By locator, String visibleText) {
        Select selectProperty = new Select(driver.findElement(locator));
        selectProperty.selectByVisibleText(visibleText);
    }

    public String getFirstSelectedOption(By locator) {
        String firstSelectedOption = "";
        Select selectProperty = new Select(driver.findElement(locator));
        firstSelectedOption = selectProperty.getFirstSelectedOption().getText();
        return firstSelectedOption;
    }

    public String getAlertText() {
        boolean isAlertPresent = isDialogPresent();
        String alertTxt = "";
        if (isAlertPresent) {
            alertTxt = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
        }
        return alertTxt;
    }

    public void findAndSetValueToElement(By webElement, String value) {
        WebElement element = driver.findElement(webElement);
        Actions actions = new Actions(driver);
        actions.moveToElement(element).pause(200).perform();
        try {
            actions.moveToElement(element).pause(200).click(element).pause(200).perform();
        } catch (Exception e) {
            actions.moveToElement(element).pause(200).click(element).pause(200).perform();
        }
        element.clear();
        element.sendKeys(value);
    }

    protected void mouseHoverAndClick(WebElement elementToHover, WebElement elementToClick) {
        Actions action = new Actions(driver);
        action.moveToElement(elementToHover).pause(200).build().perform();
        elementToClick.click();
    }
}
