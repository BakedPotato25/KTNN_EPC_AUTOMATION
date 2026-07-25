package com.ktnn.driver;

import com.ktnn.exceptions.HeadlessNotSupportedException;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import static com.ktnn.consts.FrameConst.ExecuteConfig.HEADLESS_FLAG;


public class SafariBrowserDriver extends BrowserDriver {
    @Override
    public WebDriver createDriver(boolean... isLoadings) {
        /*
         * The driver for using safari on Window
         * WebDriverManager wdm = WebDriverManager.getInstance(DriverManagerType.SAFARI).browserInDocker();
         */
        // WebDriverManager wdm = WebDriverManager.getInstance(DriverManagerType.SAFARI).browserInDocker();
        return new SafariDriver();
        /*
         * The default driver for safari when using mac
         * WebDriverManager.getInstance(DriverManagerType.SAFARI).setup();
         * return new SafariDriver(getOptions());
         */
    }

    @Override
    public MutableCapabilities getOptions(boolean... isLoadings) {
        SafariOptions safariOptions = new SafariOptions();
        safariOptions.setAutomaticInspection(false);

        if (HEADLESS_FLAG)
            throw new HeadlessNotSupportedException(safariOptions.getBrowserName());
        return safariOptions;
    }
}
