package com.ktnn.controller;

import com.ktnn.driver.DriverManager;
import com.ktnn.report.ExtentReportManager;
import com.ktnn.report.ExtentTestManager;
import com.ktnn.utils.configloader.AbsPropertyUtils;
import com.ktnn.utils.configloader.CaptureUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.ktnn.consts.FrameConst.ElementProperty.ELEMENT_PROPERTY_TEXT_CONTENT;
import static com.ktnn.consts.FrameConst.ElementProperty.ELEMENT_PROPERTY_VALUE;
import static com.ktnn.consts.FrameConst.FailureHandling;
import static com.ktnn.consts.FrameConst.LogType;
import static com.ktnn.consts.FrameConst.WaitConfig.WAIT_EXPLICIT;
import static com.ktnn.consts.FrameConst.WaitConfig.WAIT_IMPLICIT;
import static com.ktnn.report.ReportConfig.*;
import static java.lang.Thread.sleep;

/**
 * WebUI provides the interaction method, based on the Selenium Automation Framework
 */
@Slf4j
public class WebUI {
    /**
     * Create a web drive wait
     *
     * @param duration : The interval in milliseconds to scan the element
     */
    public static WebDriverWait getWaitDriver(long... duration) {
        long interval = duration.length > 0 && duration[0] != 0 ? duration[0] : WAIT_EXPLICIT;
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(interval), Duration.ofMillis(500));
    }

    /**
     * Get Selenium Action
     */
    public static Actions getActions() {
        return new Actions(DriverManager.getDriver());
    }

    /**
     * Initialize the JavaScript Executor
     */
    public static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }


    // region Navigation

    /**
     * Open website with get URL
     */
    public static void goToURL(String URL) {
        var currentURL = DriverManager.getDriver().getCurrentUrl();
        if (URL.equalsIgnoreCase(currentURL)) {
            DriverManager.getDriver().navigate().refresh();
            return;
        }

        DriverManager.getDriver().get(URL);
        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Go to URL : " + BOLD_END + URL;
        addReportInfo(LogType.INFO, msg, "goToUrl_", null);
    }

    /**
     * Refresh the browser
     */
    public void refreshPage() {
        String URL = DriverManager.getDriver().getCurrentUrl();
        DriverManager.getDriver().navigate().refresh();

        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Refresh URL : " + BOLD_END + URL;
        addReportInfo(LogType.INFO, msg, "refreshPage", null);
    }


    /**
     * Back to previous page
     */
    public void backPreviousPage() {
        DriverManager.getDriver().navigate().back();

        String URL = DriverManager.getDriver().getCurrentUrl();
        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Back to URL : " + BOLD_END + URL;
        addReportInfo(LogType.INFO, msg, "backToPage", null);
    }

    /**
     * Verify the URL Page
     */
    public static boolean verifyPageUrl(String pageUrl) {
        log.info("Actual URL: {}", DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl().contains(pageUrl.trim());
    }

    /**
     * Open the new tab in the browser
     */
    public static void openNewTab() {
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
        addReportInfo(LogType.INFO, "Open new tab", "openNewTab", null);
    }

    /**
     * Open the new browser window
     */
    public static void openNewWindow() {
        DriverManager.getDriver().switchTo().newWindow(WindowType.WINDOW);
        addReportInfo(LogType.INFO, "Open new window", "openNewWindow", null);
    }

    /**
     * Get the current window
     *
     * @return The id of the current window
     */
    public String getCurrentWindowHandle() {
        return DriverManager.getDriver().getWindowHandle();
    }

    /**
     * Close the all windows at the browser
     */
    public void closeAllWindowExceptCurrent() {
        String currentWindow = DriverManager.getDriver().getWindowHandle();

        Set<String> listWindows = DriverManager.getDriver().getWindowHandles();
        for (String window : listWindows) {
            if (!window.equals(currentWindow)) {
                switchToWindowByHandle(window);
                try {
                    DriverManager.getDriver().close();
                } catch (NoSuchWindowException exception) {
                    log.error("Have an error when close window: {}", exception.getMessage());
                }
                waitFor(1);
            }
        }
        switchToWindowByHandle(currentWindow);
    }

    /**
     * Switch to the last window
     */
    public static void switchToLastWindow() {
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
    }

    // Windows

    /**
     * Switch to the specified window by a position
     *
     * @param position : The position of window
     */
    public static void switchToWindowOrTab(int position) {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[position].toString());
        addReportInfo(LogType.INFO, "Switch to window/tab at position: " + position, "switchToWindowOrTab", null);
    }

    /**
     * Verify the number of windows or tabs
     *
     * @param number : The expected number of windows
     * @return true if the same, false otherwise
     */
    public static boolean verifyNumberOfWindowsOrTab(int number) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT)).until(ExpectedConditions.numberOfWindowsToBe(number));
    }

    /**
     * Switch to the main window
     */
    public static void switchToMainWindow() {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[0].toString());
    }

    /**
     * Switch to the window by a window id
     *
     * @param windowHandle : The window id
     */
    public static void switchToWindowByHandle(String windowHandle) {
        DriverManager.getDriver().switchTo().window(windowHandle);
    }
    // endregion

    // region Find Element


    /**
     * Get Website Elements via locator
     *
     * @param locator : The locator of element, format: "ID|value" or "CSS|value" or "xpath"
     *                Example: "ID|username" or "CSS|login-button" or "//div[@class='login']"
     * @return : The WebElement found by the locator
     */
    public static WebElement findWebElement(String locator) {
        By byObject = By.xpath(locator); // Default to XPath if no match
        if (locator.contains("|")) {
            String[] locatorParts = locator.split("\\|");
            switch (locatorParts[0].toUpperCase()) {
                case "ID":
                    byObject = By.id(locatorParts[1]);
                    break;
                case "CSS":
                    byObject = By.cssSelector(locatorParts[1]);
                    break;
            }
        }
        return findWebElement(byObject);
    }

    /**
     * Get Website Elements via locator
     *
     * @param by : The By object of element, format: By.id("username") or By.cssSelector(".login-button") or By.xpath("//div[@class='login']")
     * @return : The WebElement found by the locator
     */
    public static WebElement findWebElement(By by) {
        return waitForElementVisible(by);
    }

    /**
     * Get all elements with a by object
     *
     * @param by : The by object of elements
     * @return : The elements
     */
    public static List<WebElement> getListWebElement(By by) {
        overwriteImplicitTimeout(Duration.ofSeconds(5));
        List<WebElement> elements = DriverManager.getDriver().findElements(by);
        overwriteImplicitTimeout(Duration.ofSeconds(0));
        return elements;
    }


    /**
     * Get a by object of elements
     *
     * @param locatorForm : The locator format
     * @param keyValues   : The key value for this format
     * @return : a by object of elements
     */
    public static By getByXpathDynamic(String locatorForm, String... keyValues) {
        return By.xpath(getXPathDynamicStr(locatorForm, (Object[]) keyValues));
    }


    /**
     * Receives a wildcard string, replace the wildcard with the value and return to the caller
     *
     * @param xpath Xpath with wildcard string
     * @param value multi value to be replaced in place of wildcard
     *              VD: ObjectUtils.getXpathDynamic("//button[normalize-space()='%s']//div[%d]//span[%d]", "Login", 2, 10);
     * @return dynamic xpath string
     */
    @SneakyThrows
    public static String getXPathDynamicStr(String xpath, Object... value) {
        if (Objects.isNull(xpath) || xpath.isEmpty()) {
            log.info("getXpathDynamic: Parameter passing error. The 'XPath' parameter is null.");
            throw new Exception("Warning !! The XPath is null.");
        } else {
            if (value.length == 0) return xpath;
            return String.format(xpath, value);
        }
    }

    /**
     * Get element or default
     */
    public static WebElement getFirstElementOrDefault(WebElement scope, By by) {
        var webElement = scope.findElements(by);
        if (Objects.nonNull(webElement) && !webElement.isEmpty()) return webElement.get(0);
        return null;
    }
    // endregion


    // region Base Action

    /**
     * Click the object
     */
    public void clickTo(WebElement element, String... titles) {
        element = waitForElementClickable(element);

        String locator = getLocatorFromWebElement(element);
        String value = getDomPropertyOfElement(element, ELEMENT_PROPERTY_TEXT_CONTENT.getValue());

        if (titles.length > 0) {
            value = titles[0];
        }
        element.click();

        log.info("Clicking on element: {}", value);
        String msg = String.format("Clicked <b>[%s]</b>  <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, locator);
        addReportInfo(LogType.INFO, msg, "clickElement_", locator);
    }

    public void inputText(By by, String title, String value, boolean... isClear) {
        WebElement element = findWebElement(by);
        inputText(element, title, value, isClear);
    }

    public void inputText(WebElement element, String title, String value, boolean... notClears) {
        try {
            if (isSameValueOfElement(element, value)) return;
            if (notClears.length == 0 || !notClears[0]) {
                clearTextForElement(element);
            }
            element.sendKeys(value);
        } finally {
            String locator = getLocatorFromWebElement(element);
            String msg = String.format("Insert text <b>[%s]</b> to <b>[%s]</b> <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, title, locator);
            addReportInfo(LogType.INFO, msg, "setText_" + value, locator);
        }
    }


    public void inputTextByJS(WebElement element, String title, String value) {
        getJsExecutor().executeScript("arguments[0].value = '';", element);
        getJsExecutor().executeScript(String.format("arguments[0].innerText = '%s'", value), element);

        String locator = getLocatorFromWebElement(element);
        String msg = String.format("Insert text <b>[%s]</b> to <b>[%s]</b> <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, title, locator);
        addReportInfo(LogType.INFO, msg, "setText_" + value, locator);
    }


    /**
     * Clear text of element (especial case)
     */
    public void clearTextForElement(WebElement element) {
        element.clear();
        /*getActions().click(element).keyDown(element, Keys.CONTROL).sendKeys("A").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
        if (!Strings.isEmpty(element.getText()))
            getActions().keyDown(element, Keys.CONTROL).sendKeys("A").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();*/
    }

    /**
     * Get text of an element
     */
    public static String getTextElement(By by) {
        return getTextElement(findWebElement(by));
    }

    /**
     * Get text of an element
     */
    public static String getTextElement(WebElement element) {
        return element.getText().trim();
    }


    /**
     * Get value of element with DOM
     */
    public String getValueOfElement(By by) {
        return getDomPropertyOfElement(by, ELEMENT_PROPERTY_VALUE.getValue());
    }

    /**
     * Get value of element with DOM
     */
    public String getValueOfElement(WebElement element) {
        return getDomPropertyOfElement(element, ELEMENT_PROPERTY_VALUE.getValue());
    }

    /**
     * Get the property of Element
     */
    public String getDomPropertyOfElement(By by, String propertyName) {
        WebElement webElement = findWebElement(by);
        return getDomPropertyOfElement(webElement, propertyName);
    }

    /**
     * Get the property of Element
     */
    public String getDomPropertyOfElement(WebElement element, String propertyName) {
        try {
            return element.getDomProperty(propertyName);
        } catch (Exception e) {
            return Strings.EMPTY;
        }
    }


    /**
     * Get the property of Element
     */
    public String getAttributeOfElement(WebElement element, String attName) {
        return element.getAttribute(attName);
    }

    /**
     * scroll to element
     */
    public static void scrollToElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * scroll to element
     */
    public static void scrollToElementWithBy(By by) {
        if (Objects.nonNull(by)) {
            scrollToElement(findWebElement(by));
        }
    }

    public void clickElementViaJs(By by, String... titles) {
        clickElementViaJs(findWebElement(by), titles);
    }

    public void clickElementViaJs(WebElement element, String... titles) {
        getJsExecutor().executeScript("arguments[0].click()", element);

        String locator = getLocatorFromWebElement(element);
        String value = getDomPropertyOfElement(element, ELEMENT_PROPERTY_TEXT_CONTENT.getValue());
        if (titles.length > 0) value = titles[0];

        String msg = String.format("Clicked <b>[%s]</b>  <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, locator);
        addReportInfo(LogType.INFO, msg, "clickElement_", locator);
    }

    /**
     * Fires real mousedown/mouseup/click events - plain click() gets ignored on this app.
     */
    public void clickByJS(WebElement element, String... titles) {
        if (Objects.isNull(element)) {
            throw new NoSuchElementException("clickByJS: element is null - the locator likely didn't match anything on the page");
        }
        String script = "function fireMouseEvent(el, type) {" +
                "  var ev = new MouseEvent(type, {bubbles: true, cancelable: true, view: window});" +
                "  el.dispatchEvent(ev);" +
                "}" +
                "fireMouseEvent(arguments[0], 'mousedown');" +
                "fireMouseEvent(arguments[0], 'mouseup');" +
                "fireMouseEvent(arguments[0], 'click');";
        getJsExecutor().executeScript(script, element);

        String locator = getLocatorFromWebElement(element);
        String value = getDomPropertyOfElement(element, ELEMENT_PROPERTY_TEXT_CONTENT.getValue());
        if (titles.length > 0) value = titles[0];

        String msg = String.format("Clicked (JS mousedown/mouseup/click) <b>[%s]</b>  <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, locator);
        addReportInfo(LogType.INFO, msg, "clickElementByJs_", locator);
    }


    /**
     * Upload file using sendKeys
     *
     * @param filePaths List of file Path
     */
    public static void uploadFileSendKeys(By by, String... filePaths) {
        WebElement element = findWebElement(by);
        uploadFileSendKeys(element, filePaths);
    }

    /**
     * Upload file using sendKeys
     *
     * @param filePaths List of file Path
     */
    public static void uploadFileSendKeys(WebElement element, String... filePaths) {
        if (Objects.isNull(filePaths) || filePaths.length == 0) return;

        Arrays.stream(filePaths).forEach(element::sendKeys);
        addReportInfo(LogType.INFO, "Upload file ..", "Upload File", getLocatorFromWebElement(element));
    }

    /**
     * Hover to element with Action
     */
    public void hoverElement(WebElement element, boolean... isJavaScripts) {
        try {
            if (isJavaScripts.length == 0) getActions().moveToElement(element).perform();
            else hoverElementByJS(element);
        } catch (Exception e) {
            log.error("HoverElement: {}", e.getMessage());
        }
    }

    /**
     * Hover to element with JavaScript
     */
    public void hoverElementByJS(WebElement element) {
        try {
            String mouseOverScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('mouseover', " + " true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('onmouseover');}";
            getJsExecutor().executeScript(mouseOverScript, element);
        } catch (Exception e) {
            log.error("HoverElementByJS: {}", e.getMessage());
        }
    }

    /**
     * Click the Enter Keyboard
     */
    public static boolean pressENTER() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean pressKeyEvent(int keyEvent) {
        try {
            Robot robot = new Robot();
            robot.keyPress(keyEvent);
            robot.keyRelease(keyEvent);
            addReportInfo(LogType.INFO, String.format("Press key %s from the keyboard", keyEvent), null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // endregion

    // region Assert Condition

    /**
     * Assert Element Objects.
     * Support 3 type of Failure Handling
     */
    public static void assertTrueCondition(WebElement element, boolean isResult, FailureHandling failureHandling, @Nullable String errMsg) {
        drawBorderForErrorElement(element, isResult);

        String locator = Strings.EMPTY;
        if (Objects.nonNull(element)) {
            locator = getLocatorFromWebElement(element);
        }

        if (Objects.isNull(errMsg) || errMsg.isEmpty()) {
            errMsg = String.format("Verify TRUE object: " + locator);
        }

        try {
            if (!isResult) {
                log.info("assertTrue: {} -> VERIFY : {}", errMsg, false);
            }
            switch (failureHandling) {
                case STOP_ON_FAILURE:
                    if (!isResult) {
                        ExtentReportManager.fail(String.format("%s -> VERIFY : %s", errMsg, FAIL));
                    }
                    Assert.assertTrue(isResult);
                    ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    break;
                case CONTINUE_ON_FAILURE:
                    if (!isResult) {
                        String softMsg = "SOFT ASSERT: Assert TRUE object: FAILED";

                        Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
                        ExtentReportManager.fail(String.format("%s -> VERIFY : %s", errMsg, FAIL));
                        ExtentReportManager.addScreenShot(softMsg + " " + locator);
                    } else {
                        ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    }
                    break;
                default:
                    break;
            }
        } finally {
            if (SCREEN_SHORT_ALL_STEPS) {
                CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME);
            }
            clearBorderForErrorElement(element, isResult);
        }
    }

    /**
     * Assert Fail
     */
    public static void assertFalseCondition(WebElement element, boolean isResult, FailureHandling failureHandling, String errMsg) {
        drawBorderForErrorElement(element, isResult);

        String locator = Strings.EMPTY;
        if (Objects.nonNull(element)) {
            locator = getLocatorFromWebElement(element);
        }

        String apiLog = "";
        if (Objects.isNull(errMsg) || errMsg.isEmpty()) {
            errMsg = String.format("Verify FALSE object: " + locator);
        } else {
            apiLog = String.format("API Log: %s", errMsg);
        }
        try {
            if (isResult) {
                log.info("assertFalse: {} -> VERIFY : {}", errMsg, !isResult);
                ExtentReportManager.logMessage(errMsg);
            }
            switch (failureHandling) {
                case STOP_ON_FAILURE:
                    Assert.assertFalse(isResult);
                    ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    break;
                case CONTINUE_ON_FAILURE:
                    if (isResult) {
                        String softMsg = "SOFT ASSERT: Verify FALSE object: FAILED";

                        Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
                        ExtentReportManager.fail(String.format("%s -> VERIFY : %s", errMsg, FAIL));
                        ExtentReportManager.addScreenShot(softMsg + " " + locator);
                    } else {
                        ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    }
                    break;
            }
        } finally {
            if (SCREEN_SHORT_ALL_STEPS) {
                CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME);
            }
            clearBorderForErrorElement(element, isResult);
        }
    }

    /**
     * Assert Equal
     */
    public static void assertEqualCondition(WebElement element, Object actual, Object expected, FailureHandling failureHandling, String errMsg) {
        boolean isResult = Objects.equals(actual, expected);
        drawBorderForErrorElement(element, isResult);

        String locator = Strings.EMPTY;
        if (Objects.nonNull(element)) {
            locator = getLocatorFromWebElement(element);
        }

        if (Objects.isNull(errMsg) || errMsg.isEmpty()) {
            errMsg = String.format("Verify equal object " + locator);
        }

        errMsg = String.format("%s - Actual: %s ; Expected: %s", errMsg, actual.toString(), expected.toString());

        try {
            if (!isResult) {
                log.info("assertEqual: {} -> VERIFY : {}", errMsg, false);
            }

            switch (failureHandling) {
                case STOP_ON_FAILURE:
                    if (!isResult) {
                        ExtentReportManager.fail(String.format("%s -> VERIFY : %s", errMsg, FAIL));
                    }
                    Assert.assertEquals(actual, expected);
                    ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    break;
                case CONTINUE_ON_FAILURE:
                    if (!isResult) {
                        String softMsg = "SOFT ASSERT: Verify the result: FAILED";
                        Reporter.getCurrentTestResult().setStatus(ITestResult.FAILURE);
                        ExtentReportManager.fail(String.format("%s -> VERIFY : %s", errMsg, FAIL));
                        ExtentReportManager.addScreenShot(softMsg + " " + locator);
                    } else {
                        ExtentReportManager.pass(String.format("%s -> VERIFY : %s", errMsg, PASS));
                    }
                    break;
            }
        } finally {
            if (SCREEN_SHORT_ALL_STEPS) {
                CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME + "_allSteps");
            }
            clearBorderForErrorElement(element, isResult);
        }
    }

    /**
     * Draw a border for the error element
     */
    private static void drawBorderForErrorElement(WebElement element, boolean isResult) {
        if (DRAW_BORDER_ERR_ELEMENT && !isResult && Objects.nonNull(element)) {
            try {
                scrollElementToViewCenter(element);
            } catch (Exception e) {
                log.error("Exception: {}", e.getMessage());
            }

            JavascriptExecutor js = getJsExecutor();
            js.executeScript("arguments[0].style.border='3px solid red'", element);
        }
    }


    /**
     * Draw a border for the error element
     */
    private static void drawBorderForErrorElement(By by, boolean isResult) {
        WebElement element = findWebElement(by);
        if (Objects.isNull(element)) {
            log.error("Element with locator {} : not found", by.toString());
            return;
        }
        drawBorderForErrorElement(element, isResult);
    }


    /**
     * Clear a border for the error element
     */
    private static void clearBorderForErrorElement(By by, boolean isResult) {
        WebElement element = findWebElement(by);
        clearBorderForErrorElement(element, isResult);
    }

    /**
     * Clear a border for the error element
     */
    private static void clearBorderForErrorElement(WebElement element, boolean isResult) {
        if (DRAW_BORDER_ERR_ELEMENT && !isResult && Objects.nonNull(element)) {
            JavascriptExecutor js = getJsExecutor();
            js.executeScript("arguments[0].style.border='0px solid red'", element);
        }
    }


    // endregion

    /**
     * Wait for element visible with By object
     *
     * @param by : The By object of element
     * @return : The WebElement if it is visible, null otherwise
     */
    public static WebElement waitForElementVisible(By by) {
        WebElement element = null;
        try {
            element = getWaitDriver().until(ExpectedConditions.visibilityOfElementLocated(by));
            if (Objects.nonNull(element))
                log.info("Element with locator {} : visible", by.toString());
        } catch (Exception e) {
            var elementList = DriverManager.getDriver().findElements(by);
            log.error("Element {} : invisible {}", by.toString(), elementList.isEmpty() ? "" : " Had more element with this XPATH. Please re-check!");
        }
        return element;
    }


    /**
     * Verify whether the element is visible or not
     */
    public static WebElement waitForElementVisible(WebElement element) {
        String locator = Strings.EMPTY;
        try {
            locator = getLocatorFromWebElement(element);
            log.info("Element {} : visible", locator);
        } catch (Exception e) {
            log.info("Element {} : invisible", locator);
        }
        return element;
    }

    /**
     * Wait for element clickable
     *
     * @param by: The By object of element
     * @return : The WebElement if it is clickable, null otherwise
     */
    public static WebElement waitForElementClickable(By by) {
        WebElement webElement = findWebElement(by);
        return waitForElementClickable(webElement);
    }


    /**
     * Wait for element clickable
     *
     * @param element: The WebElement to wait for
     * @return : The WebElement if it is clickable, null otherwise
     */
    public static WebElement waitForElementClickable(WebElement element) {
        String locator = Strings.EMPTY;
        String msg = "clickable";
        try {
            locator = getLocatorFromWebElement(element);
            element = getWaitDriver().until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            msg = "un-clickable";
        }
        log.info("Element {} : {}", locator, msg);
        return element;
    }

    /**
     *
     */
    public WebElement waitForElementPresent(By by) {
        try {
            return getWaitDriver().until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Throwable error) {
            log.error("Element not exist. {}", by.toString());
        }
        return null;
    }

    /**
     * Wait for element invisible
     */
    public static void waitForElementInvisible(Object object, long... waitDuration) {
        if (object instanceof By)
            getWaitDriver(waitDuration).until(ExpectedConditions.invisibilityOfElementLocated((By) object));
        else if (object instanceof WebElement)
            getWaitDriver(waitDuration).until(ExpectedConditions.invisibilityOf((WebElement) object));
    }

    public static boolean verifyElementDisplayed(Object object, int... wait) {
        try {
            WebElement element;
            if (object instanceof By) {
                overwriteImplicitTimeout(Duration.ofSeconds(wait.length > 0 ? wait[0] : WAIT_IMPLICIT));
                element = DriverManager.getDriver().findElement((By) object);
                overwriteImplicitTimeout(Duration.ofSeconds(0));
            } else element = (WebElement) object;
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static void waitToVerifyElementVisible(WebElement element, boolean expDisplay, FailureHandling failureHandling) {
        String locator = getLocatorFromWebElement(element);
        String msg;

        boolean isResult;
        if (expDisplay) {
            msg = String.format("Verify the object %s : visible", locator);
            waitForElementVisible(element);
            isResult = element.isDisplayed();
        } else {
            msg = String.format("Verify the object %s : invisible", locator);
            waitForElementVisible(element);
            isResult = !element.isDisplayed();
        }
        assertTrueCondition(element, isResult, failureHandling, msg);
    }

    /**
     * Verify whether the element is visible or not
     */
    private static void waitToVerifyElementVisibleWithBy(By by, boolean expDisplay, FailureHandling failureHandling) {
        String msg;
        WebElement element;
        boolean isResult;
        if (expDisplay) {
            msg = String.format("Verify the object %s : visible", by);
            element = waitForElementVisible(by);
            isResult = Objects.nonNull(element) && element.isDisplayed();
        } else {
            msg = String.format("Verify the object %s : invisible", by);
            element = waitForElementVisible(by);
            isResult = Objects.isNull(element) || !element.isDisplayed();
        }
        assertTrueCondition(element, isResult, failureHandling, msg);
    }

    // endregion

    // region Alert
    public static void alertAccept() {
        DriverManager.getDriver().switchTo().alert().accept();
    }

    public static void alertDismiss() {
        DriverManager.getDriver().switchTo().alert().dismiss();
    }

    public static void alertGetText() {
        DriverManager.getDriver().switchTo().alert().getText();
    }

    public static void alertSetText(String text) {
        DriverManager.getDriver().switchTo().alert().sendKeys(text);
    }

    public static boolean verifyAlertPresent() {
        try {
            getWaitDriver().until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Throwable error) {
            Assert.fail("Not found Alert.");
            return false;
        }
    }

    // endregion

    // region Utils
    public static void scrollElementToViewCenter(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);" + "var elementTop = arguments[0].getBoundingClientRect().top;" + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        getJsExecutor().executeScript(scrollElementIntoMiddle, element);
    }

    public boolean isSameValueOfElement(WebElement element, String expValue) {
        return expValue.equals(element.getText()) || expValue.equals(getValueOfElement(element));
    }

    public static String getLanguageValue(String key) {
        return AbsPropertyUtils.getLanguageValue(key);
    }

    /**
     * Force wait
     */
    public static void waitFor(double second) {
        try {
            sleep((long) (second * 1000));
        } catch (InterruptedException e) {
            log.error("VException: {}", e.getMessage());
        }
    }

    public static void overwriteImplicitTimeout(Duration duration) {
        DriverManager.getDriver().manage().timeouts().implicitlyWait(duration);
    }

    /**
     * Add more information for Report: Including Extent and Allure.
     * You can add more report at this function.
     */
    public static void addReportInfo(LogType logType, String extMsg, String capText, String locator) {
        // Add for Extent Report
        if (ExtentTestManager.getExtentTest() != null) {
            if (logType.equals(LogType.INFO)) ExtentReportManager.info(extMsg);
            else ExtentReportManager.pass(extMsg);
        }
    }

    /**
     * Get the element locator from WebElement
     */
    public static String getLocatorFromWebElement(WebElement element) {
        var list = element.toString().split("->");
        if (list.length > 1)
            return element.toString().split("->")[1].replaceFirst("xpath:(?s)(.*)]", "$1").trim();
        else return element.toString();
    }
    // endregion


    /**
     * Upload files using EventKey
     */
    public static void uploadFileUseRobot(WebElement element, String filePath) {
        //Click để mở form upload
        getActions().moveToElement(element).click().perform();
        waitFor(WAIT_IMPLICIT);

        // Khởi tạo Robot class
        Robot rb = null;
        try {
            rb = new Robot();
        } catch (AWTException e) {
            log.error("Exception init robot: {}", e.getMessage());
        }

        // Copy File path vào Clipboard
        StringSelection str = new StringSelection(filePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(str, null);

        // Nhấn Control+V để dán
        rb.keyPress(KeyEvent.VK_CONTROL);
        rb.keyPress(KeyEvent.VK_V);

        // Xác nhận Control V
        rb.keyRelease(KeyEvent.VK_CONTROL);
        rb.keyRelease(KeyEvent.VK_V);
        waitFor(WAIT_IMPLICIT);
        // Nhấn Enter
        rb.keyPress(KeyEvent.VK_ENTER);
        rb.keyRelease(KeyEvent.VK_ENTER);
        addReportInfo(LogType.INFO, "Upload file .." + filePath, "Upload File", getLocatorFromWebElement(element));
    }

    public static String getPageTitle() {
        String title = DriverManager.getDriver().getTitle();
        log.info("getPageTitle: Page Title: {}", title);
        return title;
    }

    public static boolean verifyElementTextEqual(By by, String expectedValue) {
        return getTextElement(by).trim().equals(expectedValue.trim());
    }

    public static void verifyElementTextEqual(WebElement webElement, String expectedValue) {
        String elementText = getTextElement(webElement);
        assertEqualCondition(webElement, elementText.trim(), expectedValue.trim(),
                FailureHandling.CONTINUE_ON_FAILURE, "Verify the text of element");
    }

//    public void waitForEleValueNotEmpty(WebElement element) {
//        getWebWait().until(ExpectedConditions.attributeToBeNotEmpty(element, "value"));
//    }
//
//    // Wait until the element value is different from the old value
//    public void waitForEleValueChanged(WebElement element, String oldValue) {
//        getWebWait().until(ExpectedConditions.not(
//                ExpectedConditions.attributeToBe(element, "value", oldValue)
//        ));
//    }
}
