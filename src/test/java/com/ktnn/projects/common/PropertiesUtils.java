package com.ktnn.projects.common;

import com.ktnn.database.config.DatabaseInfo;
import com.ktnn.database.config.DatabaseType;
import com.ktnn.report.ReportConfig;
import com.ktnn.utils.configloader.AbsPropertyUtils;
import com.ktnn.utils.configloader.ResourceReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static com.ktnn.consts.FrameConst.*;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.JAPANESE;

/**
 * Properties được load từ cấu hình của project
 */
@Slf4j
public class PropertiesUtils extends AbsPropertyUtils {
    private static ResourceBundle resourceConfig;

    @Getter
    public static PropertiesUtils instance = new PropertiesUtils();

    private PropertiesUtils() {
    }

    /**
     * Cập nhật/thiết lập ngôn ngữ dùng cho website
     *
     * @param language :
     */
    private PropertiesUtils(String language) {
        Locale locale = Locale.ENGLISH;
        if ("vi".equals(language)) {
            locale = new Locale("vi", "VI");
        }
        resourceConfig = ResourceBundle.getBundle("language", locale);
    }

    /**
     * Tạo mới một Properties object
     */
    public static PropertiesUtils getInstance(String language) {
        return new PropertiesUtils(language);
    }

    /**
     * Trả về giá trị ngôn ngữ của keyword vào data model
     *
     * @param key : Key trong bundle ngôn ngữ
     * @return Giá trị của key này
     */
    public static String getLanguageValue(String key) {
        try {
            if (resourceConfig == null) getInstance(properties.getProperty("language"));
            return resourceConfig.getString(key);
        } catch (Exception e) {
            log.error("VException: getLanguageValue: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Cập nhật property
     *
     * @param property : Properties object
     * @param key      : Key
     * @param value    : Value
     */
    @Override
    public void updateProperty(Properties property, String key, String value, boolean... hasBoolean) {
        if (hasBoolean.length >= 1 && hasBoolean[0]) {
            property.setProperty(key, String.valueOf(value));
        } else if (Objects.nonNull(value) && !value.isEmpty()) {
            property.setProperty(key, value);
        }
    }

    /**
     * Cập nhật env và property
     */
    public void updateMavenProperties(Properties property, JSONObject configObjects) {
        // Đọc properties từ Maven
        Properties properties = System.getProperties();
        String exeTarget = properties.getProperty("exeTarget", property.getProperty("exeTarget", "LOCAL"));
        String appLanguage = properties.getProperty("exeLanguage", property.getProperty("exeLanguage", "vi"));
        String appVersion = properties.getProperty("appVersion", property.getProperty("appVersion", "1.0"));
        String exeEnv = properties.getProperty("exeEnv", property.getProperty("exeEnv", "sit"));
        String exeCategory = properties.getProperty("exeCategory", property.getProperty("exeCategory", "ALL"));
        String exeJiraId = properties.getProperty("exeJiraId", property.getProperty("exeJiraId", ""));
        String exeDBVerification = properties.getProperty("exeDBVerification", property.getProperty("exeDBVerification", "false"));

        // Cập nhật property file bằng giá trị từ system properties hoặc giá trị mặc định
        updateProperty(property, "exeTarget", exeTarget);
        updateProperty(property, "exeLanguage", appLanguage);
        updateProperty(property, "appVersion", appVersion);
        updateProperty(property, "exeEnv", exeEnv);
        updateProperty(property, "exeCategory", exeCategory);
        updateProperty(property, "exeJiraId", exeJiraId);
        updateProperty(property, "exeDBVerification", exeDBVerification);

        ExecuteConfig.EXE_ENV = exeEnv.toUpperCase();
        AppConfig.APP_LANGUAGE = appLanguage.toUpperCase();
        AppConfig.APP_VERSION = appVersion;

        String exeBrowser = properties.getProperty("exeBrowser", property.getProperty("exeBrowser", "chrome"));
        String exeHeadlessMode = properties.getProperty("exeHeadlessMode", property.getProperty("exeHeadlessMode", "false"));
        updateProperty(property, "exeBrowser", exeBrowser);
        updateProperty(property, "exeHeadlessMode", exeHeadlessMode);

        // Cấu hình application
        JSONObject configJSON = configObjects.getJSONObject("config");
        JSONObject appJSON = configJSON.getJSONObject("env");
        JSONObject envConfig = (JSONObject) appJSON.get(ExecuteConfig.EXE_ENV.toLowerCase());

        updateProperty(property, "baseUrl", envConfig.getString("baseUrl"));
        updateProperty(property, "account", envConfig.getString("account"));
        updateProperty(property, "password", envConfig.getString("password"));
        updateProperty(property, "apiUrl", envConfig.getString("apiUrl"));

        // Report chung
        PROJECT_NAME = property.getProperty("projectName", "KTNN_EPC_AUTO");
        JIRA_DOMAIN = property.getProperty("jiraDomain", "");
        DATABASE_CONNECT_CONFIG = Boolean.parseBoolean(property.getProperty("exeDBVerification", "false"));

        AppConfig.APP_DOMAIN = envConfig.getString("baseUrl");
        AppConfig.USER_NAME= envConfig.getString("account");
        AppConfig.PASSWORD = envConfig.getString("password");
        AppConfig.API_DOMAIN = envConfig.getString("apiUrl");

        ReportConfig.OVERRIDE_REPORTS = Boolean.parseBoolean(property.getProperty("overrideReports", "false"));
        ReportConfig.AUTHOR = property.getProperty("author", "KTNN");
        ReportConfig.REPORT_TITLE = property.getProperty("reportTitle", "KTNN Automation Report");
        ReportConfig.SCREENSHOT_PASSED_STEPS = Boolean.parseBoolean(property.getProperty("screenshotPassedSteps", "false"));
        ReportConfig.SCREENSHOT_FAILED_STEPS = Boolean.parseBoolean(property.getProperty("screenshotFailedSteps", "true"));
        ReportConfig.SCREENSHOT_SKIPPED_STEPS = Boolean.parseBoolean(property.getProperty("screenshotSkippedSteps", "true"));
        ReportConfig.SCREEN_SHORT_ALL_STEPS = Boolean.parseBoolean(property.getProperty("screenshotAllSteps", "false"));
        ReportConfig.DRAW_BORDER_ERR_ELEMENT = Boolean.parseBoolean(property.getProperty("drawBorderErrElement", "false"));
        ReportConfig.VIDEO_RECORD = Boolean.parseBoolean(property.getProperty("videoRecord", "false"));
        ReportConfig.SAVE_RESPONSE_TO_DATABASE = Boolean.parseBoolean(property.getProperty("exeCollectRes", "false"));
        ReportConfig.JIRA_EXECUTION_ID = property.getProperty("exeJiraId", "");
        ReportConfig.DEVELOP_STATE = Boolean.parseBoolean(property.getProperty("exeDevelopState", "false"));
        ReportConfig.MERGE_REPORT = Boolean.parseBoolean(property.getProperty("exeMergeReport", "false"));

        WaitConfig.WAIT_EXPLICIT = Long.parseLong(property.getProperty("waitExplicit", "10"));
        WaitConfig.WAIT_IMPLICIT = Long.parseLong(property.getProperty("waitImplicit", "10"));
        WaitConfig.WAIT_PAGE_LOADED = Long.parseLong(property.getProperty("waitPageLoaded", "10"));
        WaitConfig.WAIT_DEFAULT = Long.parseLong(property.getProperty("waitDefault", "10"));

        ExecuteConfig.EXE_BROWSER = exeBrowser.toLowerCase();
        ExecuteConfig.CATEGORY_TYPE = property.getProperty("exeCategory", "ALL");
        ExecuteConfig.EXE_ENV_TARGET = ExeTarget.valueOf(property.getProperty("exeTarget", "LOCAL").toUpperCase());

        SeleniumConfig.REMOTE_URL = property.getProperty("exeRemoteURL", "http://localhost:4444/wd/hub");
        SeleniumConfig.REMOTE_PORT = property.getProperty("exeRemotePort", "4444");
        /* Thêm property khác ở đây */

        /* Danh sách kết nối database */
        DATABASE_CONNECT_LIST.clear();
        if (!DATABASE_CONNECT_CONFIG) return;
        /* TODO: Cần kiểm tra lại */
        JSONObject databaseJSON = configObjects.getJSONObject("database");
        JSONObject dbEnvJSON = databaseJSON.getJSONObject("env");
        JSONArray databaseEnvList = (JSONArray) dbEnvJSON.get(ExecuteConfig.EXE_ENV.toLowerCase());

        databaseEnvList.toList().forEach(d -> {
            HashMap tmpDB = (HashMap) d;
            DatabaseType type = DatabaseType.valueOf(String.valueOf(tmpDB.get("type")).toUpperCase());
            DATABASE_CONNECT_LIST.add(DatabaseInfo.builder().url(String.valueOf(tmpDB.get("url"))).name(String.valueOf(tmpDB.get("name"))).userName(String.valueOf(tmpDB.get("username"))).password(String.valueOf(tmpDB.get("password"))).configPath(String.valueOf(tmpDB.get("config"))).type(type).build());
        });
    }


    public static void loadLanguageBundle(String language) {
        Locale locale = new Locale("vi", "VI");
        switch (language.toLowerCase()) {
            case "en":
                locale = ENGLISH;
                break;
            case "ja":
                locale = JAPANESE;
                break;
            case "vi":
            default:    // Giá trị mặc định: VI
                break;
        }
        resourceConfig = ResourceBundle.getBundle("language", locale);
    }

    /**
     * Load toàn bộ properties của project
     */
    private static void loadConfig() {
        if (Objects.nonNull(properties)) return;

        String resourceFolderPath = "src/test/resources/config";
        List<String> files = loadFilesFromResourceFolder(resourceFolderPath);

        try {
            properties = new Properties();
            for (String f : files) {
                if (!f.endsWith(".properties")) continue;

                Properties tempProp = new Properties();
                tempProp.load(new FileInputStream(f));
                properties.putAll(tempProp);
            }
        } catch (Exception e) {
            log.error("VException: loadAllFiles: {}", e.getMessage());
        }
    }

    /**
     * Load toàn bộ cấu hình env
     *
     * @return : Một JSONObject
     */
    public static JSONObject loadEnvConfiguration() {
        // Thêm toàn bộ property
        JSONObject envConfigJSON = new JSONObject();

        String configPath = "config/env.json";
        JSONObject configObject = new JSONObject(Objects.requireNonNull(readDataFromFile(configPath)));
        envConfigJSON.put("config", configObject);

        String dbPath = "config/database.json";
        JSONObject dbObject = new JSONObject(Objects.requireNonNull(readDataFromFile(dbPath)));
        envConfigJSON.put("database", dbObject);
        return envConfigJSON;
    }

    @Override
    public void executionInfo(Properties properties) {
        System.out.println("\n\n==================EXECUTION INFO========================");
        System.out.println("\tWEB AUTOMATION - PRODUCT MANAGEMENT SYSTEM");
        System.out.printf("\tEnvironment: %s%n", ExecuteConfig.EXE_ENV);
        System.out.printf("\tBrowser: %s%n", ExecuteConfig.EXE_BROWSER);
        System.out.printf("\tExecution Type: %s%n", ExecuteConfig.CATEGORY_TYPE);
        System.out.printf("\tLanguage: %s%n", AppConfig.APP_LANGUAGE);
        System.out.printf("\tTesting Version: %s%n", AppConfig.APP_VERSION);
        System.out.println("========================================================\n\n");
    }

    @Override
    public void loadAllProperties() {
        /* Load cấu hình environment */
        JSONObject envConfigs = loadEnvConfiguration();

        /* Load toàn bộ cấu hình */
        loadConfig();

        /* Cập nhật properties */
        updateMavenProperties(properties, envConfigs);
        executionInfo(properties);
    }


    public static List<String> loadFilesFromResourceFolder(String folderPath) {
        List<String> files = new LinkedList<>();
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                if (file.isFile()) {
                    files.add(file.getAbsolutePath());
                }
            }
        }
        return files;
    }

    /**
     * Đọc dữ liệu từ resource
     *
     * @param filePath : đường dẫn file
     */
    public static String readDataFromFile(String filePath) {
        try {
            log.info("Read configuration data from: {}", filePath);
            return ResourceReader.readDataFromResource(filePath);
        } catch (IOException e) {
            log.error("Error: {}", e.getMessage());
        }
        return null;
    }


}
