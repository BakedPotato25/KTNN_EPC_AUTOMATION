package com.ktnn.driver;

import lombok.NoArgsConstructor;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Objects;

/**
 * Quản lý driver, dùng để set/get Selenium Web Driver và lấy thông tin driver.
 */
@NoArgsConstructor
public class DriverManager {

    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /**
     * Lấy Selenium Web Driver
     * @return Selenium Web Driver
     */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Gán Selenium Web Driver
     * @param driver: Selenium Web Driver
     */
    public static void setDriver(WebDriver driver) {
        DriverManager.driverThread.set(driver);
    }

    /**
     * Đóng Selenium WebDriver
     */
    public static void quitDriver() {
        if (Objects.nonNull(DriverManager.getDriver())) {
            DriverManager.getDriver().quit();
            DriverManager.driverThread.remove();
        }
    }

    /**
     * Lấy thông tin browser đang chạy
     * @return Thông tin browser (Name, Version, Platform)
     */
    public static String getBrowserInfo() {
        Capabilities cap = ((RemoteWebDriver) DriverManager.getDriver()).getCapabilities();
        return String.format("Browser: %s Version: %s Platform: %s", cap.getBrowserName(), cap.getBrowserVersion(),
                cap.getPlatformName());
    }
}
