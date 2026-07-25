package com.ktnn.driver;

import lombok.NoArgsConstructor;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

/**
 * Driver management, you can set/get Selenium Web Driver and get this info.
 */
@NoArgsConstructor
public class DriverManager {

    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /**
     * Get Selenium Web Driver
     * @return The Selenium Web Driver
     */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Set the Selenium Web Driver
     * @param driver: The Selenium Web Driver
     */
    public static void setDriver(WebDriver driver) {
        DriverManager.driverThread.set(driver);
    }

    /**
     * Remote Selenium WebDriver
     */
    public static void quitDriver() {
        if (Objects.nonNull(DriverManager.getDriver())) {
            DriverManager.getDriver().quit();
            DriverManager.driverThread.remove();
        }
    }

    /**
     * Get the running browser
     * @return The browser information (Name, Version, Platform)
     */
    public static String getBrowserInfo() {
        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
        return String.format("Browser: %s Version: %s Platform: %s", cap.getBrowserName(), cap.getBrowserVersion(),
                cap.getPlatformName());
    }
}
