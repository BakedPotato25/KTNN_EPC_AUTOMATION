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
 * WebUI cung cấp các method thao tác, dựa trên Selenium Automation Framework
 */
@Slf4j
public class WebUI {
    /**
     * Tạo web driver wait
     *
     * @param duration : khoảng thời gian (ms) để quét element
     */
    public static WebDriverWait getWaitDriver(long... duration) {
        long interval = duration.length > 0 && duration[0] != 0 ? duration[0] : WAIT_EXPLICIT;
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(interval), Duration.ofMillis(500));
    }

    /**
     * Lấy Selenium Action
     */
    public static Actions getActions() {
        return new Actions(DriverManager.getDriver());
    }

    /**
     * Khởi tạo JavaScript Executor
     */
    public static JavascriptExecutor getJsExecutor() {
        return (JavascriptExecutor) DriverManager.getDriver();
    }


    // region Navigation

    /**
     * Mở website bằng URL
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
     * Refresh trình duyệt
     */
    public void refreshPage() {
        String URL = DriverManager.getDriver().getCurrentUrl();
        DriverManager.getDriver().navigate().refresh();

        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Refresh URL : " + BOLD_END + URL;
        addReportInfo(LogType.INFO, msg, "refreshPage", null);
    }


    /**
     * Quay lại trang trước
     */
    public void backPreviousPage() {
        DriverManager.getDriver().navigate().back();

        String URL = DriverManager.getDriver().getCurrentUrl();
        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Back to URL : " + BOLD_END + URL;
        addReportInfo(LogType.INFO, msg, "backToPage", null);
    }

    /**
     * Verify URL của trang
     */
    public static boolean verifyPageUrl(String pageUrl) {
        log.info("Actual URL: {}", DriverManager.getDriver().getCurrentUrl());
        return DriverManager.getDriver().getCurrentUrl().contains(pageUrl.trim());
    }

    /**
     * Mở tab mới trong trình duyệt
     */
    public static void openNewTab() {
        DriverManager.getDriver().switchTo().newWindow(WindowType.TAB);
        addReportInfo(LogType.INFO, "Open new tab", "openNewTab", null);
    }

    /**
     * Mở cửa sổ trình duyệt mới
     */
    public static void openNewWindow() {
        DriverManager.getDriver().switchTo().newWindow(WindowType.WINDOW);
        addReportInfo(LogType.INFO, "Open new window", "openNewWindow", null);
    }

    /**
     * Lấy cửa sổ hiện tại
     *
     * @return id của cửa sổ hiện tại
     */
    public String getCurrentWindowHandle() {
        return DriverManager.getDriver().getWindowHandle();
    }

    /**
     * Đóng tất cả cửa sổ trên trình duyệt
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
     * Chuyển sang cửa sổ cuối cùng
     */
    public static void switchToLastWindow() {
        Set<String> windowHandles = DriverManager.getDriver().getWindowHandles();
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[windowHandles.size() - 1].toString());
    }

    // Cửa sổ

    /**
     * Chuyển sang cửa sổ theo vị trí chỉ định
     *
     * @param position : vị trí cửa sổ
     */
    public static void switchToWindowOrTab(int position) {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[position].toString());
        addReportInfo(LogType.INFO, "Switch to window/tab at position: " + position, "switchToWindowOrTab", null);
    }

    /**
     * Verify số lượng windows/tabs
     *
     * @param number : số cửa sổ mong đợi
     * @return true nếu giống, false nếu khác
     */
    public static boolean verifyNumberOfWindowsOrTab(int number) {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(WAIT_EXPLICIT)).until(ExpectedConditions.numberOfWindowsToBe(number));
    }

    /**
     * Chuyển sang cửa sổ chính
     */
    public static void switchToMainWindow() {
        DriverManager.getDriver().switchTo().window(DriverManager.getDriver().getWindowHandles().toArray()[0].toString());
    }

    /**
     * Chuyển sang cửa sổ theo window id
     *
     * @param windowHandle : id của cửa sổ
     */
    public static void switchToWindowByHandle(String windowHandle) {
        DriverManager.getDriver().switchTo().window(windowHandle);
    }
    // endregion

    // region Find Element


    /**
     * Lấy element trên website qua locator
     *
     * @param locator : locator của element, format: "ID|value" hoặc "CSS|value" hoặc "xpath"
     *                Ví dụ: "ID|username" hoặc "CSS|login-button" hoặc "//div[@class='login']"
     * @return : WebElement tìm được theo locator
     */
    public static WebElement findWebElement(String locator) {
        By byObject = By.xpath(locator); // Mặc định XPath nếu không khớp prefix nào
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
     * Lấy element trên website qua locator
     *
     * @param by : By object của element, format: By.id("username") hoặc By.cssSelector(".login-button") hoặc By.xpath("//div[@class='login']")
     * @return : WebElement tìm được theo locator
     */
    public static WebElement findWebElement(By by) {
        return waitForElementVisible(by);
    }

    /**
     * Lấy tất cả element theo by object
     *
     * @param by : by object của các element
     * @return : danh sách element
     */
    public static List<WebElement> getListWebElement(By by) {
        overwriteImplicitTimeout(Duration.ofSeconds(5));
        List<WebElement> elements = DriverManager.getDriver().findElements(by);
        overwriteImplicitTimeout(Duration.ofSeconds(0));
        return elements;
    }


    /**
     * Lấy by object của elements
     *
     * @param locatorForm : định dạng locator
     * @param keyValues   : giá trị key cho định dạng này
     * @return : by object của elements
     */
    public static By getByXpathDynamic(String locatorForm, String... keyValues) {
        return By.xpath(getXPathDynamicStr(locatorForm, (Object[]) keyValues));
    }


    /**
     * Nhận chuỗi có wildcard, thay wildcard bằng giá trị rồi trả về cho caller
     *
     * @param xpath Xpath chứa wildcard string
     * @param value các giá trị thay thế cho wildcard
     *              VD: ObjectUtils.getXpathDynamic("//button[normalize-space()='%s']//div[%d]//span[%d]", "Login", 2, 10);
     * @return chuỗi xpath động
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
     * Lấy element hoặc trả về default
     */
    public static WebElement getFirstElementOrDefault(WebElement scope, By by) {
        var webElement = scope.findElements(by);
        if (Objects.nonNull(webElement) && !webElement.isEmpty()) return webElement.get(0);
        return null;
    }
    // endregion


    // region Base Action

    /**
     * Click vào object
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
     * Clear text của element (trường hợp đặc biệt)
     */
    public void clearTextForElement(WebElement element) {
        element.clear();
        /*getActions().click(element).keyDown(element, Keys.CONTROL).sendKeys("A").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
        if (!Strings.isEmpty(element.getText()))
            getActions().keyDown(element, Keys.CONTROL).sendKeys("A").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();*/
    }

    /**
     * Lấy text của element
     */
    public static String getTextElement(By by) {
        return getTextElement(findWebElement(by));
    }

    /**
     * Lấy text của element
     */
    public static String getTextElement(WebElement element) {
        return element.getText().trim();
    }


    /**
     * Lấy value của element qua DOM
     */
    public String getValueOfElement(By by) {
        return getDomPropertyOfElement(by, ELEMENT_PROPERTY_VALUE.getValue());
    }

    /**
     * Lấy value của element qua DOM
     */
    public String getValueOfElement(WebElement element) {
        return getDomPropertyOfElement(element, ELEMENT_PROPERTY_VALUE.getValue());
    }

    /**
     * Lấy property của Element
     */
    public String getDomPropertyOfElement(By by, String propertyName) {
        WebElement webElement = findWebElement(by);
        return getDomPropertyOfElement(webElement, propertyName);
    }

    /**
     * Lấy property của Element
     */
    public String getDomPropertyOfElement(WebElement element, String propertyName) {
        try {
            return element.getDomProperty(propertyName);
        } catch (Exception e) {
            return Strings.EMPTY;
        }
    }


    /**
     * Lấy property của Element
     */
    public String getAttributeOfElement(WebElement element, String attName) {
        return element.getAttribute(attName);
    }

    /**
     * scroll tới element
     */
    public static void scrollToElement(WebElement element) {
        getJsExecutor().executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * scroll tới element
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
     * Bắn chuỗi event mousedown/mouseup/click thật - click() thường bị app này bỏ qua.
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
     * Set giá trị qua native property setter + bắn event input/change.
     * Cần cho field Vue mà clear()+sendKeys() không nhận.
     */
    public void setValueByNativeSetter(WebElement element, String value) {
        String script = "var el = arguments[0]; var value = arguments[1];" +
                "var proto = el.tagName === 'TEXTAREA' ? window.HTMLTextAreaElement.prototype : window.HTMLInputElement.prototype;" +
                "var setter = Object.getOwnPropertyDescriptor(proto, 'value').set;" +
                "setter.call(el, value);" +
                "el.dispatchEvent(new Event('input', {bubbles: true}));" +
                "el.dispatchEvent(new Event('change', {bubbles: true}));";
        getJsExecutor().executeScript(script, element, value);

        String locator = getLocatorFromWebElement(element);
        String msg = String.format("Set value via native setter <b>[%s]</b>  <br/> <span style='font-size: 0.75em'>(Element's locator:  %s)</span>", value, locator);
        addReportInfo(LogType.INFO, msg, "setValueByNativeSetter_", locator);
    }

    /**
     * Blur input đang focus bằng JS để Vue's v-model commit giá trị.
     * Dùng trước action (vd Save) đọc form ngay sau khi gõ phím.
     */
    public void blurActiveElement() {
        getJsExecutor().executeScript("if (document.activeElement) document.activeElement.blur();");
    }


    /**
     * Upload file bằng sendKeys
     *
     * @param filePaths danh sách đường dẫn file
     */
    public static void uploadFileSendKeys(By by, String... filePaths) {
        WebElement element = findWebElement(by);
        uploadFileSendKeys(element, filePaths);
    }

    /**
     * Upload file bằng sendKeys
     *
     * @param filePaths danh sách đường dẫn file
     */
    public static void uploadFileSendKeys(WebElement element, String... filePaths) {
        if (Objects.isNull(filePaths) || filePaths.length == 0) return;

        Arrays.stream(filePaths).forEach(element::sendKeys);
        addReportInfo(LogType.INFO, "Upload file ..", "Upload File", getLocatorFromWebElement(element));
    }

    /**
     * Hover vào element bằng Action
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
     * Hover vào element bằng JavaScript
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
     * Nhấn phím Enter
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
     * Hỗ trợ 3 loại Failure Handling
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
     * Assert điều kiện False
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
     * Assert điều kiện bằng nhau
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
     * Vẽ border cho element bị lỗi
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
     * Vẽ border cho element bị lỗi
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
     * Xoá border cho element bị lỗi
     */
    private static void clearBorderForErrorElement(By by, boolean isResult) {
        WebElement element = findWebElement(by);
        clearBorderForErrorElement(element, isResult);
    }

    /**
     * Xoá border cho element bị lỗi
     */
    private static void clearBorderForErrorElement(WebElement element, boolean isResult) {
        if (DRAW_BORDER_ERR_ELEMENT && !isResult && Objects.nonNull(element)) {
            JavascriptExecutor js = getJsExecutor();
            js.executeScript("arguments[0].style.border='0px solid red'", element);
        }
    }


    // endregion

    /**
     * Wait element visible qua By object
     *
     * @param by : By object của element
     * @return : WebElement nếu visible, null nếu không
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
     * Verify element có visible hay không
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
     * Wait element clickable
     *
     * @param by: By object của element
     * @return : WebElement nếu clickable, null nếu không
     */
    public static WebElement waitForElementClickable(By by) {
        WebElement webElement = findWebElement(by);
        return waitForElementClickable(webElement);
    }


    /**
     * Wait element clickable
     *
     * @param element: WebElement cần wait
     * @return : WebElement nếu clickable, null nếu không
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
     * Wait element invisible
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
     * Verify element có visible hay không
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
     * Chờ cứng (force wait)
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
     * Thêm thông tin cho Report: gồm Extent và Allure.
     * Có thể thêm loại report khác tại function này.
     */
    public static void addReportInfo(LogType logType, String extMsg, String capText, String locator) {
        // Thêm cho Extent Report
        if (ExtentTestManager.getExtentTest() != null) {
            if (logType.equals(LogType.INFO)) ExtentReportManager.info(extMsg);
            else ExtentReportManager.pass(extMsg);
        }
    }

    /**
     * Lấy locator của element từ WebElement
     */
    public static String getLocatorFromWebElement(WebElement element) {
        var list = element.toString().split("->");
        if (list.length > 1)
            return element.toString().split("->")[1].replaceFirst("xpath:(?s)(.*)]", "$1").trim();
        else return element.toString();
    }
    // endregion


    /**
     * Upload file bằng EventKey
     */
    public static void uploadFileUseRobot(WebElement element, String filePath) {
        // Click để mở form upload
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

        rb.keyRelease(KeyEvent.VK_CONTROL);
        rb.keyRelease(KeyEvent.VK_V);
        waitFor(WAIT_IMPLICIT);
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
}
