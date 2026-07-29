package com.ktnn.driver;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static com.ktnn.consts.FrameConst.ExecuteConfig.HEADLESS_FLAG;


public class FirefoxBrowserDriver extends BrowserDriver {
    @Override
    public WebDriver createDriver(boolean... isLoadings) {
        // TODO: handle missing Firefox install
        return new FirefoxDriver((FirefoxOptions) getOptions(isLoadings));
    }

    @Override
    public MutableCapabilities getOptions(boolean... isLoadings) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.addArguments("--start-maximized");
        firefoxOptions.setAcceptInsecureCerts(true);
        if (HEADLESS_FLAG) firefoxOptions.addArguments("--headless");

        if (isLoadings.length > 0 && isLoadings[0])
            firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return firefoxOptions;
    }
}
