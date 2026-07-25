package com.ktnn.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    private static int status = ITestResult.CREATED;
    // private final int maxRetry = Integer.parseInt(AbsPropertyUtils.getPropertyValue(FrameConst.ConfigProperties.RETRY_COUNT));
    private int count = 0;

    public static int getRetryStatus() {
        return status;
    }

    @Override
    public boolean retry(ITestResult result) {
        /*if (AbsPropertyUtils.getPropertyValue(FrameConst.ConfigProperties.RETRY_FAILED_TESTS).equalsIgnoreCase("yes")) {
            value = count < maxRetry;
            count++;
            }
        }*/
        return false;
    }
}
