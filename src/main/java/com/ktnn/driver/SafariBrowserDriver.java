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
        // On Windows use WebDriverManager.getInstance(DriverManagerType.SAFARI).browserInDocker()
        // On Mac use WebDriverManager.getInstance(DriverManagerType.SAFARI).setup()
        return new SafariDriver();
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
