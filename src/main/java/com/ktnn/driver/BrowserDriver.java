package com.ktnn.driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

/**
 * Driver trình duyệt, dùng để tạo driver và lấy options.
 */
public abstract class BrowserDriver {
    protected WebDriver driver;
    public abstract WebDriver createDriver(boolean... isLoadings);
    public abstract MutableCapabilities getOptions(boolean... isLoadings);
}
