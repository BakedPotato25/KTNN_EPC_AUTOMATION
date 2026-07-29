package com.ktnn.driver;

import com.ktnn.consts.FrameConst;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;

import java.net.URL;
import java.util.EnumMap;
import java.util.Objects;

import static com.ktnn.consts.FrameConst.ExecuteConfig;
import static com.ktnn.consts.FrameConst.ExecuteConfig.EXE_ENV_TARGET;
import static com.ktnn.consts.FrameConst.SeleniumConfig;

/**
 * Factory tạo driver trình duyệt, dùng để tạo driver và lấy options.
 */
@Slf4j
public class BrowserFactory {
    final static EnumMap<FrameConst.Browser, BrowserDriver> browserDriverMap;

    static {
        browserDriverMap = new EnumMap<>(FrameConst.Browser.class);
        browserDriverMap.put(FrameConst.Browser.CHROME, new ChromeBrowserDriver());
        browserDriverMap.put(FrameConst.Browser.EDGE, new EdgeBrowserDriver());
        browserDriverMap.put(FrameConst.Browser.FIREFOX, new FirefoxBrowserDriver());
        browserDriverMap.put(FrameConst.Browser.SAFARI, new SafariBrowserDriver());
    }

    private BrowserFactory() {
    }

    /**
     * Tạo Selenium Web Driver với browser cụ thể, tuỳ theo target là LOCAL hay REMOTE
     * - LOCAL: browser sẽ chạy trên máy local với browser cụ thể
     * - REMOTE: browser sẽ chạy với nhiều loại browser trên máy remote
     *
     * @param browser : Tên browser
     */
    public static void initWebDriver(String browser, boolean... isLoadings) {
        // Dùng browser từ ExecuteConfig nếu đã set, ngược lại lấy từ tham số truyền vào
        if (Objects.nonNull(ExecuteConfig.EXE_BROWSER)) {
            browser = ExecuteConfig.EXE_BROWSER;
        } else ExecuteConfig.EXE_BROWSER = browser;

        WebDriver webdriver = null;
        try {
            FrameConst.Browser browserType = FrameConst.Browser.valueOf(browser.toUpperCase());

            /* Khởi tạo browser driver */
            BrowserDriver browserDriver = browserDriverMap.get(browserType);
            switch (EXE_ENV_TARGET) {
                case LOCAL:
                    webdriver = browserDriver.createDriver(isLoadings);
                    break;
                case REMOTE:
                    webdriver = initRemoteWebDriver(browserDriver.getOptions(isLoadings));
                    break;
            }
        } catch (Exception e) {
            log.error("Browser|Target not supported: {}", e.getMessage());
            throw new IllegalArgumentException("Browser|Target not supported: " + e.getMessage());
        }

        /* Cập nhật WebDriverManager với WebDriver hiện tại */
        webdriver = ThreadGuard.protect(webdriver);
        DriverManager.setDriver(webdriver);
    }

    /**
     * Tạo Selenium RemoteWebDriver cho các instance Remote
     *
     * @param capability : Capabilities của browser
     * @return Selenium RemoteWebDriver
     */
    private static RemoteWebDriver initRemoteWebDriver(MutableCapabilities capability) {
        RemoteWebDriver remoteWebDriver = null;
        try {
            String remoteURL = String.format("http://%s:%s/wd/hub", SeleniumConfig.REMOTE_URL, SeleniumConfig.REMOTE_PORT);

            remoteWebDriver = new RemoteWebDriver(new URL(remoteURL), capability);
            remoteWebDriver.setFileDetector(new LocalFileDetector());
        } catch (Exception e) {
            log.error("Remote URL is invalid or Remote Port is not available");
            log.error(String.format("Browser: %s", capability.getBrowserName()), e);
            throw new IllegalArgumentException("Browser|Target is not available: " + e.getMessage());
        }
        return remoteWebDriver;
    }

}
