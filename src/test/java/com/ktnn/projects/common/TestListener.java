package com.ktnn.projects.common;

import com.aventstack.extentreports.Status;
import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.annotations.TFSLink;
import com.ktnn.consts.AuthorType;
import com.ktnn.driver.DriverManager;
import com.ktnn.report.ExtentReportManager;
import com.ktnn.report.ExtentTestManager;
import com.ktnn.utils.IconUtils;
import com.ktnn.utils.configloader.CaptureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.testng.*;

import java.util.Objects;

import static com.ktnn.consts.FrameConst.CategoryType;
import static com.ktnn.consts.FrameConst.ExecuteConfig.EXE_BROWSER;
import static com.ktnn.report.ReportConfig.*;

/*
 * Mục đích: Implement testing listener
 * Datetime:
 */
@Slf4j
public class TestListener implements ITestListener, ISuiteListener, IInvokedMethodListener, IConfigurationListener {
    private final String separateItem = "\n---------------------------------------------------------------";

    public String getTestName(ITestResult result) {
        return result.getTestName() != null ? result.getTestName() : result.getMethod().getConstructorOrMethod().getName();
    }

    public String getTestDescription(ITestResult result) {
        return result.getMethod().getDescription() != null ? result.getMethod().getDescription() : Strings.EMPTY;
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }

    @Override
    public void onStart(ISuite iSuite) {
        log.info("{}\nTestListener: TESTING FOR TEST SUITE: {}{}", separateItem, iSuite.getName(), separateItem);
        iSuite.setAttribute("WebDriver", DriverManager.getDriver());
    }

    @Override
    public void onFinish(ISuite iSuite) {
        log.info("\nTestListener: FINISH TESTING FOR TEST SUITE: {} {}", iSuite.getName(), separateItem);
    }


    @Override
    public void onTestStart(ITestResult iTestResult) {
        log.info("{}\nTestListener: START TC:  {}", separateItem, getTestName(iTestResult));

        ExtentTestManager.unload();
        addTestToExtentReport(iTestResult);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        log.info("\nTestListener: COMPLETED TC: {} - PASS {}", getTestName(iTestResult), separateItem);
        updateRetryTestName(iTestResult);

        if (SCREENSHOT_PASSED_STEPS) {
            CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME);
        }

        //Ghi log ExtentReports cho test pass.
        ExtentReportManager.logMessage(Status.PASS, "Test case: " + getTestName(iTestResult) + " - PASS");
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        log.info("\nTestListener: COMPLETED TC: {} - FAIL {}", getTestName(iTestResult), separateItem);
        updateRetryTestName(iTestResult);

        if (SCREENSHOT_FAILED_STEPS) {
            CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME);
        }
        log.error("FAILED !! Screenshot for test case: {}", getTestName(iTestResult));
        if (ExtentTestManager.getExtentTest() == null) {
            addTestToExtentReport(iTestResult);
        }

        //Extent report ghi screenshot file và log
        ExtentReportManager.addScreenShot(Status.FAIL, getTestName(iTestResult));
        ExtentReportManager.logMessage(Status.FAIL, "Test case: " + getTestName(iTestResult) + " - FAIL");
        if (Objects.nonNull(iTestResult.getThrowable())) {
            log.error(String.valueOf(iTestResult.getThrowable()));
            ExtentReportManager.logMessage(Status.FAIL, iTestResult.getThrowable());
        }
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        log.info("\nTestListener: COMPLETED TC: {} - SKIP {}", getTestName(iTestResult), separateItem);
        updateRetryTestName(iTestResult);

        if (SCREENSHOT_SKIPPED_STEPS) {
            CaptureUtils.captureScreenshot(DriverManager.getDriver(), EXECUTED_TESTCASE_NAME);
        }

        if (ExtentTestManager.getExtentTest() == null) {
            addTestToExtentReport(iTestResult);
        }

        ExtentReportManager.logMessage(Status.SKIP, "Test case: " + getTestName(iTestResult) + " - SKIP");
        ExtentReportManager.unloadTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        if (ExtentTestManager.getExtentTest() == null) {
            addTestToExtentReport(iTestResult);
        }
        log.error("Test failed but it is in defined success ratio {}", getTestName(iTestResult));
        ExtentReportManager.logMessage("Test failed but it is in defined success ratio " + getTestName(iTestResult));
        ExtentReportManager.unloadTest();
    }

    public AuthorType[] getAuthorType(ITestResult iTestResult) {
        if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameAnnotation.class) == null) {
            return null;
        }
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameAnnotation.class).author();
    }

    public CategoryType[] getCategoryType(ITestResult iTestResult) {
        if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameAnnotation.class) == null) {
            return null;
        }
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameAnnotation.class).category();
    }


    @Override
    public void onConfigurationSuccess(ITestResult tr) {
        String className = tr.getTestClass().getName();
        ExtentReportManager.logMessage(Status.WARNING, "Configuration: " + getTestName(tr) + " - PASS");
        ExtentReportManager.unloadTest();
        ExtentReportManager.removeTest(tr.getName() + " " + className.substring(className.lastIndexOf(".") + 1));
        flushReport(tr);
    }

    @Override
    public void onConfigurationFailure(ITestResult tr) {
        ExtentReportManager.logMessage(Status.WARNING, "Configuration: " + getTestName(tr) + " - FAIL");
        if (Objects.nonNull(tr.getThrowable())) {
            log.error(String.valueOf(tr.getThrowable()));
            ExtentReportManager.logMessage(Status.WARNING, tr.getThrowable());
        }
        ExtentReportManager.unloadTest();
        flushReport(tr);
    }

    @Override
    public void onConfigurationSkip(ITestResult tr) {
        ExtentReportManager.logMessage(Status.WARNING, "Configuration: " + getTestName(tr) + " - SKIP");
        ExtentReportManager.unloadTest();
        flushReport(tr);
    }

    @Override
    public void beforeConfiguration(ITestResult tr) {
        String className = tr.getTestClass().getName();
        ExtentReportManager.createTest(tr.getName() + " " + className.substring(className.lastIndexOf(".") + 1));
        ExtentReportManager.logMessage(Status.WARNING, "START - Configuration: " + getTestName(tr));
    }

    private void updateRetryTestName(ITestResult iTestResult) {
        String oldName = getTestName(iTestResult);
        if (Objects.nonNull(ExtentTestManager.getExtentTest())) {
            String extendName = ExtentTestManager.getExtentTest().getModel().getName();
            String newName = extendName.replace(oldName, iTestResult.getName());
            ExtentTestManager.getExtentTest().getModel().setName(newName);
        }
    }

    public void flushReport(ITestResult iTestResult) {
        String method = iTestResult.getMethod().getConstructorOrMethod().getName();
        if (method.contains("afterTest")) ExtentReportManager.flushReports();
    }

    private void addTestToExtentReport(ITestResult iTestResult) {
        String browser = iTestResult.getTestContext().getCurrentXmlTest().getParameter("browser");
        if (Objects.isNull(browser)) browser = EXE_BROWSER.toUpperCase();
        else browser = browser.trim().toUpperCase();

        AuthorType[] author = getAuthorType(iTestResult);
        // method test không có @FrameAnnotation (vd scenario tự sinh bởi cucumber-testng) -> getAuthorType trả null
        if (author == null) author = new AuthorType[0];
        String des = (author.length > 0 ? (author[0] + " - ") : Strings.EMPTY) + getTestDescription(iTestResult);
        iTestResult.setAttribute("author", author.length > 0 ? author[0].toString() : "");


        if (iTestResult.getAttributeNames().contains("invocation")) {
            String dataId = iTestResult.getAttributeNames().contains("dataId") ? iTestResult.getAttribute("dataId").toString() : iTestResult.getName();
            des = String.format("%s </br> ID: %s - Invocation %s", des, dataId, iTestResult.getAttribute("invocation"));
        }

        ExtentReportManager.createTest(iTestResult.getName(), des, browser);
        ExtentReportManager.addAuthors(author);
        String nameTestClass = iTestResult.getTestClass().getName();
        ExtentReportManager.addCategory(nameTestClass.substring(nameTestClass.lastIndexOf(".") + 1));
        ExtentReportManager.addDevices(browser);
        ExtentReportManager.addTFSLink(getTFSLink(iTestResult));
        ExtentReportManager.info(BOLD_START + IconUtils.getOSIcon() + " " + IconUtils.getOSInfo() + BOLD_END);
    }

    public String getTFSLink(ITestResult iTestResult) {
        if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TFSLink.class) == null) {
            return null;
        }
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TFSLink.class).value();
    }
}
