package com.ktnn.report;

import com.ktnn.consts.AuthorType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.ktnn.consts.FrameConst.PROJECT_NAME;
import static com.ktnn.consts.FrameConst.PROJECT_PATH;

@Setter
@Getter
public class ReportConfig {
    public static String REPORT_TITLE = "AUTOMATION TEST REPORT";
    public static String AUTHOR = "ADMIN";

    public static final String BOLD_START = "<b>";
    public static final String BOLD_END = "</b>";
    public static final String FAIL = BOLD_START + "FAIL" + BOLD_END;
    public static final String PASS = BOLD_START + "PASS" + BOLD_END;
    public static final String NO = "no";

    public static String REPORT_FOLDER_PATH = String.format("%s/%s", PROJECT_PATH, "reports");
    public static boolean OVERRIDE_REPORTS = false;
    public static String EXTENT_REPORT_NAME = PROJECT_NAME;
    public static String EXTENT_REPORT_PATH = REPORT_FOLDER_PATH + "/ExtentReports";
    public static String EXTENT_SCREENSHOT_PATH = EXTENT_REPORT_PATH + "/Screenshots";
    public static String EXTENT_RECORDING_PATH = EXTENT_REPORT_PATH + "/Videos";
    public static String EXTENT_REPORT_FILE_NAME = EXTENT_REPORT_NAME + ".html";

    public static String EXPORT_VIDEO_PATH = REPORT_FOLDER_PATH + "/Videos";
    public static String ALLURE_REPORT_PATH = REPORT_FOLDER_PATH + "/AllureReports";

    public static boolean SCREENSHOT_PASSED_STEPS;
    public static boolean SCREENSHOT_FAILED_STEPS;
    public static boolean SCREENSHOT_SKIPPED_STEPS;
    public static boolean SCREEN_SHORT_ALL_STEPS;
    public static boolean DRAW_BORDER_ERR_ELEMENT;
    public static boolean VIDEO_RECORD;
    public static boolean MERGE_REPORT = false;

    public static Boolean DEVELOP_STATE = false;
    public static Boolean SAVE_RESPONSE_TO_DATABASE = false;

    public static String EXECUTION_ID = UUID.randomUUID().toString();
    public static String ENV_CONFIGURATION_PATH;
    public static String JIRA_EXECUTION_ID = "";
    public static String TEST_SCRIPT_NAME = "";
    public static String ISSUE_MANAGEMENT_TITLE = "JiraLink";
    public static AuthorType TEST_SCRIPT_AUTHOR = AuthorType.ADMIN;
    public static Map<AuthorType, String> XRAY_EXECUTION_MAP = new HashMap<>();
    public static String EXECUTED_TESTCASE_NAME = "";

    public static int COUNT_TESTCASE_RUN_LOCAL = 0;
    public static int COUNT_TESTCASE_UPLOAD_JIRA = 0;
    public static Map<String, String> TC_JIRA_MAP = new HashMap<>();
    public static Map<String, String> TEST_UPLOAD_JIRA = new HashMap<>();

    @Getter
    public static class Icon {
        public static final String ICON_SMILEY_PASS = "<i class='fa fa-smile-o' style='font-size:24px'></i>";
        public static final String ICON_SMILEY_SKIP = "<i class=\"fas fa-frown-open\"></i>";
        public static final String ICON_SMILEY_FAIL = "<i class='fa fa-frown-o' style='font-size:24px'></i>";
        public static final String ICON_OS_WINDOWS = "<i class='fa fa-windows' ></i>";
        public static final String ICON_OS_MAC = "<i class='fa fa-apple' ></i>";
        public static final String ICON_OS_LINUX = "<i class='fa fa-linux' ></i>";
        public static final String ICON_BROWSER_OPERA = "<i class=\"fa fa-opera\" aria-hidden=\"true\"></i>";
        public static final String ICON_BROWSER_EDGE = "<i class=\"fa fa-edge\" aria-hidden=\"true\"></i>";
        public static final String ICON_BROWSER_CHROME = "<i class=\"fa fa-chrome\" aria-hidden=\"true\"></i>";
        public static final String ICON_BROWSER_FIREFOX = "<i class=\"fa fa-firefox\" aria-hidden=\"true\"></i>";
        public static final String ICON_BROWSER_SAFARI = "<i class=\"fa fa-safari\" aria-hidden=\"true\"></i>";
        public static final String ICON_ANDROID = "<i class=\"fa fa-android\" aria-hidden=\"true\"></i>";
        public static final String ICON_IOS = "<i class=\"fa fa-apple\" aria-hidden=\"true\"></i>";
        public static final String ICON_NATIVE_APP = "<i class=\"fa fa-mobile\" aria-hidden=\"true\"></i>";
        public static final String ICON_NAVIGATE_RIGHT = "<i class='fa fa-arrow-circle-right' ></i>";
        public static final String ICON_LAPTOP = "<i class='fa fa-laptop' style='font-size:18px'></i>";
        public static final String ICON_BUG = "<i class='fa fa-bug' ></i>";
        public static final String ICON_SOCIAL_GITHUB_PAGE_URL = "";
        public static final String ICON_SOCIAL_LINKEDIN_URL = "";
        public static final String ICON_SOCIAL_GITHUB_URL = "";
        public static final String ICON_SOCIAL_LINKEDIN = "<a href='" + ICON_SOCIAL_LINKEDIN_URL + "'><i class='fa fa-linkedin-square' style='font-size:24px'></i></a>";
        public static final String ICON_SOCIAL_GITHUB = "<a href='" + ICON_SOCIAL_GITHUB_URL + "'><i class='fa fa-github-square' style='font-size:24px'></i></a>";
        public static final String ICON_CAMERA = "<i class=\"fa fa-camera\" aria-hidden=\"true\"></i>";
        public static final String ICON_BROWSER_PREFIX = "<i class=\"fa fa-";
        public static final String ICON_BROWSER_SUFFIX = "\" aria-hidden=\"true\"></i>";
    }
}
