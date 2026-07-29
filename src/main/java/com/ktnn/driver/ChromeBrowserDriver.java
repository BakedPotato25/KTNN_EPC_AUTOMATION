package com.ktnn.driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.ktnn.consts.FrameConst.ExecuteConfig.HEADLESS_FLAG;

public class ChromeBrowserDriver extends BrowserDriver {
    @Override
    public WebDriver createDriver(boolean... isLoadings) {
        return new ChromeDriver((ChromeOptions) getOptions(isLoadings));
    }

    @Override
    public MutableCapabilities getOptions(boolean... isLoadings) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--disable-infobars");
        chromeOptions.addArguments("--disable-notifications");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--no-sandbox"); // Bỏ qua OS security model, hữu ích cho CI
        chromeOptions.setAcceptInsecureCerts(true);

        // Ẩn popup first-run / restore-session khi dùng browser profile mới
        chromeOptions.addArguments("--no-first-run");
        chromeOptions.addArguments("--no-default-browser-check");
        chromeOptions.addArguments("--disable-session-crashed-bubble");
        chromeOptions.addArguments("--disable-features=InfiniteSessionRestore");

        // Tắt popup save-password / breach-warning của Chrome - đây là UI native, không phải của trang
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        chromeOptions.setExperimentalOption("prefs", prefs);

        if (HEADLESS_FLAG) chromeOptions.addArguments("--headless=new");

        if (isLoadings.length > 0 && isLoadings[0])
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

        return chromeOptions;
    }
}
