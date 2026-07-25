package com.ktnn.utils;

import com.ktnn.consts.FrameConst.Browser;
import com.ktnn.report.ReportConfig.*;

/**
 * Class for getting browser icon and OS icon and display on the report
 */
public final class IconUtils {
    public static String getBrowserIcon(String browser) {
        if (browser.contains(Browser.CHROME.toString())) {
            return Icon.ICON_BROWSER_CHROME;
        } else if (browser.contains(Browser.EDGE.toString())) {
            return Icon.ICON_BROWSER_EDGE;
        } else if (browser.contains(Browser.FIREFOX.toString())) {
            return Icon.ICON_BROWSER_FIREFOX;
        } else if (browser.contains(Browser.SAFARI.toString())) {
            return Icon.ICON_BROWSER_SAFARI;
        } else {
            return browser;
        }
    }
    public static String getOSIcon() {
        String operationSystem = getOSInfo().toLowerCase();
        if (operationSystem.contains("win")) {
            return Icon.ICON_OS_WINDOWS;
        } else if (operationSystem.contains("nix") || operationSystem.contains("nux") || operationSystem.contains("aix")) {
            return Icon.ICON_OS_LINUX;
        } else if (operationSystem.contains("mac")) {
            return Icon.ICON_OS_MAC;
        }
        return operationSystem;
    }

    /**
     * Get OS system
     */
    public static String getOSInfo() {
        return System.getProperty("os.name").trim();
    }
}
