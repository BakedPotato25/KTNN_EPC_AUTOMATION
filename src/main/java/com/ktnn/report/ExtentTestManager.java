package com.ktnn.report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.ktnn.consts.FrameConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentTest getExtentTest() {
        return extentTest.get();
    }

    public static void setExtentTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void unload() {
        extentTest.remove();
    }
    public static void addReportInfo(FrameConst.LogType logType, String extMsg, boolean isResult, String responseCodeBlock) {
        // Thêm vào ExtentReport
        if (ExtentTestManager.getExtentTest() != null) {
            switch (logType) {
                case INFO:
                    ExtentReportManager.info(extMsg);
                    break;
                case VERIFY:
                    if (isResult) ExtentReportManager.pass(extMsg);
                    else ExtentReportManager.fail(extMsg);
                    if (Objects.nonNull(responseCodeBlock)) {
                        ExtentTestManager.getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(responseCodeBlock));
                    }
                case WARNING:
                    if (Objects.nonNull(responseCodeBlock)) {
                        ExtentTestManager.getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(responseCodeBlock));
                    }
            }
        }
    }
}