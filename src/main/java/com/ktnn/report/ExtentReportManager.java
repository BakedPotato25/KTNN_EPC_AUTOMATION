package com.ktnn.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst;
import com.ktnn.driver.DriverManager;
import com.ktnn.utils.IconUtils;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.QueryableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.SpecificationQuerier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.ktnn.consts.FrameConst.JIRA_DOMAIN;
import static com.ktnn.report.ReportConfig.*;

@Slf4j
public final class ExtentReportManager {

    private static ExtentReports extentReports;
    static String currentReportName;
    static String screenshotPath;
    //static String videoPath;

    public static void initReports(String reportName, String appVersion, String appName, String excEnv, boolean isUpdate) {
        if (Objects.isNull(extentReports) || isUpdate) {
            if (Objects.isNull(extentReports))
                extentReports = new ExtentReports();

            String TESTING_VERSION = FrameConst.AppConfig.APP_VERSION;
            if (Objects.nonNull(excEnv) && Objects.nonNull(appName) && Objects.nonNull(appVersion))
                TESTING_VERSION = String.format("%s_%s_%s", excEnv.toUpperCase(), appName.toUpperCase(), appVersion);

            reportName = Objects.nonNull(reportName) && !reportName.isEmpty() ? "_" + reportName : Strings.EMPTY;

            currentReportName = createExtentReportPath(reportName);
            ExtentSparkReporter spark = new ExtentSparkReporter(currentReportName);
            String jsonFilePath = createJsonExtentObserverPath(reportName);

            JsonFormatter json = new JsonFormatter(jsonFilePath);
            extentReports.attachReporter(json, spark);

            configSpark(spark, TESTING_VERSION);

            if (!reportName.isEmpty() || isUpdate) {
                extentReports.setSystemInfo("Framework Name", REPORT_TITLE);
                extentReports.setSystemInfo("Author", AUTHOR);
                extentReports.setSystemInfo("Testing Version", TESTING_VERSION);
            }
        }

        screenshotPath = EXTENT_REPORT_PATH + File.separator + "Screenshots";
        File file = new File(screenshotPath);
        if (!file.exists()) {
            file.mkdir();
            log.info("Create folder for screenshot: {}", file);
        }
        File vFile = new File(EXTENT_RECORDING_PATH);
        if (!vFile.exists()) {
            vFile.mkdir();
            log.info("Create folder for video recording: {}", file);
        }
    }

    private static void configSpark(ExtentSparkReporter spark, String testVersion) {
        spark.config().setOfflineMode(true);
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setDocumentTitle(REPORT_TITLE + " Test version " + testVersion);
        spark.config().setReportName(REPORT_TITLE + " Test version " + testVersion);
        spark.config().setEncoding("UTF-8");
        spark.viewConfigurer().viewOrder().as(
                new ViewName[]{
                        ViewName.DASHBOARD,
                        ViewName.TEST,
                        ViewName.CATEGORY,
                        ViewName.DEVICE,
                        ViewName.EXCEPTION,
                        ViewName.AUTHOR,
                        ViewName.LOG}).apply();
    }

    public static void flushReports() {
        if (Objects.nonNull(extentReports)) {
            extentReports.flush();
            updateContent();
        }
        ExtentTestManager.unload();
    }


    public static void createTest(String testCaseName) {
        if (Objects.nonNull(extentReports))
            ExtentTestManager.setExtentTest(extentReports.createTest(testCaseName, null));
    }

    public static void createTest(String testCaseName, String description, String browser) {
        EXECUTED_TESTCASE_NAME = testCaseName;
        String testName = IconUtils.getBrowserIcon(browser) + " : " + testCaseName + String.format("<br/> <p style='font-size: 0.75em'>%s</p>", description);
        ExtentTestManager.setExtentTest(extentReports.createTest(testName, null));
    }

    public static void unloadTest() {
        if (Objects.nonNull(ExtentTestManager.getExtentTest())) {
            ExtentReportManager.addScreenShot(EXECUTED_TESTCASE_NAME + "_Finished");
            ExtentTestManager.unload();
        }
    }

    public static void removeTest(String testCaseName) {
        unloadTest();
        if (Objects.nonNull(extentReports))
            extentReports.removeTest(testCaseName);
    }

    /**
     * Adds the screenshot.
     *
     * @param message the message
     */
    public static void addScreenShot(String message) {
        addScreenShot(Status.INFO, message);
    }

    /**
     * Adds the screenshot.
     *
     * @param status  the status
     * @param message the message
     */
    public static void addScreenShot(Status status, String message) {
        if (ExtentTestManager.getExtentTest() != null) {
            try {
                if (DriverManager.getDriver() != null) {
                    String base64Image = "data:image/png;base64," + ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BASE64);
                    ExtentTestManager.getExtentTest().log(status, message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64Image).build());
                } else
                    ExtentTestManager.getExtentTest().log(status, message);
            } catch (Exception exception) {
                ExtentTestManager.getExtentTest().log(status, message);
                ExtentTestManager.getExtentTest().log(status, exception);
            }
        }
    }

    synchronized public static void addAuthors(AuthorType[] authors) {
        if (authors == null) {
            ExtentTestManager.getExtentTest().assignAuthor("AUTHOR");
        } else {
            for (AuthorType author : authors) {
                ExtentTestManager.getExtentTest().assignAuthor(author.toString());
            }
        }

    }

    public static void addTFSLink(String tfsLink) {
        if (Objects.nonNull(tfsLink) && !tfsLink.isEmpty()) {
            String[] tmp = tfsLink.split(",");
            for (String link : tmp)
                ExtentTestManager.getExtentTest().info(MarkupHelper.createLabel("This TC has been FAILED, see details at <a href=\"" + JIRA_DOMAIN + link + "\">TFS Link</a>", ExtentColor.AMBER));
        }
    }

    public static void logResponse(Response response) {
        logResponseInReport(response);
    }

    public static void logResponseInReport(Response response) {
        if (FrameConst.LOG_LEVEL.equalsIgnoreCase("Debug")) {
            System.out.println("=== HTTP Code: " + response.statusCode());
            System.out.println("SDebug - Response: \n" + response.asPrettyString());
        }

        if (Objects.nonNull(ExtentTestManager.getExtentTest())) {
            ExtentTestManager.getExtentTest().log(Status.INFO, "API Response: HTTP Code :   " + response.statusCode());
            ExtentTestManager.getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(response.asPrettyString()));
        }
    }

    public static void logRequest(RequestSpecification requestSpecification, FrameConst.HTTPMethod method) {
        QueryableRequestSpecification query = SpecificationQuerier.query(requestSpecification);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method.name()).append(" : ").append(query.getURI()).append("\nHeaders:");
        for (Header header : query.getHeaders()) {
            stringBuilder.append("\n\t").append(header.getName()).append(":").append(header.getValue()).append("\n");
        }
        String reqBody = query.getBody();
        if (Objects.nonNull(reqBody))
            stringBuilder.append("Body\n").append(reqBody);

        logRequestInReport(stringBuilder.toString());
        if (FrameConst.LOG_LEVEL.equalsIgnoreCase("Debug")) {
            requestSpecification.log().all();
        }
    }

    public static void logRequestInReport(String request) {
        if (Objects.nonNull(ExtentTestManager.getExtentTest())) {
            ExtentTestManager.getExtentTest().log(Status.INFO, MarkupHelper.createCodeBlock(request));
        }
    }

    synchronized public static void addCategory(String cateName) {
        ExtentTestManager.getExtentTest().assignCategory(cateName);
    }


    synchronized public static void addDevices(String device) {
        ExtentTestManager.getExtentTest().assignDevice(device.trim().toUpperCase());
    }

    public static void logMessage(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().log(Status.INFO, message);
    }

    public static void logMessage(Status status, String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().log(status, message);
    }

    public static void logMessage(Status status, Object message) {
        if (ExtentTestManager.getExtentTest() != null)
            ExtentTestManager.getExtentTest().log(status, (Throwable) message);
    }

    public static void pass(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().pass(message);
    }

    public static void pass(Markup message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().pass(message);
    }

    public static void fail(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().fail(message);
    }

    public static void fail(Object message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().fail((String) message);
    }

    public static void fail(Markup message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().fail(message);
    }

    public static void skip(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().skip(message);
    }

    public static void skip(Markup message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().skip(message);
    }

    public static void info(Markup message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().info(message);
    }

    public static void info(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().info(message);
    }

    public static void warning(String message) {
        if (ExtentTestManager.getExtentTest() != null) ExtentTestManager.getExtentTest().log(Status.WARNING, message);
    }

    private static void updateContent() {
        String defaultLogo = "spark/logo.png";
        String newLogo = "./css/Logo_KTNN.jpg"; // provided by report deploy step, not part of this repo

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(currentReportName), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String val;
            while ((val = br.readLine()) != null) {
                if (val.isEmpty())
                    continue;
                if (val.contains(defaultLogo))
                    val = val.replace(defaultLogo, newLogo);
                if (val.contains("class=\"nav-logo\""))
                    val = val.replace("class=\"nav-logo\"", " class=\"nav-logo\" style=\"padding:0 0\"");
                if (val.contains("</html>"))
                    val = val.replace("</html>", "<style>.test-wrapper .test-content .test-content-detail .detail-head .info{height: unset;}</style></html>");
                stringBuilder.append(val).append("\n");
            }
            br.close();

            File f = new File(currentReportName);
            FileUtils.writeStringToFile(f, stringBuilder.toString(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("VException: {}", e.getMessage());
        }
    }

    /**
     * Create the report file path
     */
    public static String createExtentReportPath(String reportName) {
        String name = OVERRIDE_REPORTS ?
                getCurrentDate() + "_" + EXTENT_REPORT_NAME + reportName + ".html"
                : EXTENT_REPORT_NAME + reportName + ".html";
        return EXTENT_REPORT_PATH + "/" + name;
    }

    public static String createJsonExtentObserverPath(String reportName) {
        String name = OVERRIDE_REPORTS ?
                getCurrentDate() + "_" + EXTENT_REPORT_NAME + reportName + ".json"
                : EXTENT_REPORT_NAME + reportName + ".json";
        return EXTENT_REPORT_PATH + "/" + name;
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(new Date());
    }
}
