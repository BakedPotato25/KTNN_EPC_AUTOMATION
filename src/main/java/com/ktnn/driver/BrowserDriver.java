package com.ktnn.driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;

/**
 * Browser driver, you can create the driver and get the options.
 */
public abstract class BrowserDriver {
    protected WebDriver driver;
    public abstract WebDriver createDriver(boolean... isLoadings);
    public abstract MutableCapabilities getOptions(boolean... isLoadings);
}
