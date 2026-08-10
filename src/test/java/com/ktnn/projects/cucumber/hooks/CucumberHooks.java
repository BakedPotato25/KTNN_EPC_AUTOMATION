package com.ktnn.projects.cucumber.hooks;

import com.ktnn.consts.FrameConst;
import com.ktnn.driver.BrowserFactory;
import com.ktnn.driver.DriverManager;
import com.ktnn.projects.common.PropertiesUtils;
import com.ktnn.projects.pages.PageManagement;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.LoginPage;
import com.ktnn.report.ExtentReportManager;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;

import static com.ktnn.consts.FrameConst.AppConfig.APP_VERSION;
import static com.ktnn.consts.FrameConst.AppConfig.PASSWORD;
import static com.ktnn.consts.FrameConst.AppConfig.USER_NAME;
import static com.ktnn.consts.FrameConst.ExecuteConfig.EXE_ENV;
import static com.ktnn.consts.FrameConst.PROJECT_NAME;

/**
 * Bootstrap driver/login/report cho suite Cucumber - không extends được TestBase vì
 * PickListCucumberRunner đã extends AbstractTestNGCucumberTests (Java không cho multiple
 * inheritance), nên gọi lại đúng các API tĩnh mà TestBase gọi, qua Cucumber's @BeforeAll/@AfterAll
 * (chạy 1 lần cho cả suite, độc lập TestNG lifecycle) thay vì @BeforeClass/@BeforeTest của TestNG.
 */
public class CucumberHooks {
    // homePage tạo 1 lần sau login, các step tái dùng .gotoXxxPage() từ đây - đúng pattern
    // TestBase/PickListTest (không reconstruct HomePage() nếu không đang ở màn hình Home).
    public static HomePage homePage;

    @BeforeAll
    public static void setup() {
        PropertiesUtils.getInstance().loadAllProperties();
        BrowserFactory.initWebDriver(FrameConst.ExecuteConfig.EXE_BROWSER);
        ExtentReportManager.initReports("PickList Cucumber", APP_VERSION, PROJECT_NAME, EXE_ENV, false);

        LoginPage loginPage = PageManagement.accessWebPage();
        homePage = loginPage.login(USER_NAME, PASSWORD);
    }

    @AfterAll
    public static void teardown() {
        ExtentReportManager.flushReports();
        DriverManager.quitDriver();
    }
}
