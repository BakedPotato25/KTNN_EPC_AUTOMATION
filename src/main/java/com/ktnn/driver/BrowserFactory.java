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
 * Browser Factory, you can create the driver and get the options.
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
     * Create the Selenium Web Driver with the specific browser depending on the target can be LOCAL or REMOTE
     * - LOCAL: The browser will be executed in the local machine with specific browser
     * - REMOTE: The browser will be executed with multiple browsers in the remote machine
     *
     * @param browser : The browser name
     */
    public static void initWebDriver(String browser, boolean... isLoadings) {
        // Use the browser from ExecuteConfig if it is set or passed as a parameter
        if (Objects.nonNull(ExecuteConfig.EXE_BROWSER)) {
            browser = ExecuteConfig.EXE_BROWSER;
        } else ExecuteConfig.EXE_BROWSER = browser;

        WebDriver webdriver = null;
        try {
            FrameConst.Browser browserType = FrameConst.Browser.valueOf(browser.toUpperCase());

            /* Init the browser driver */
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

        /* Update the WebDriverManager with the current WebDriver */
        webdriver = ThreadGuard.protect(webdriver);
        DriverManager.setDriver(webdriver);
    }

    /**
     * Create the Selenium RemoteWebDriver for Remote instances
     *
     * @param capability : The browser capabilities
     * @return The Selenium RemoteWebDriver
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
