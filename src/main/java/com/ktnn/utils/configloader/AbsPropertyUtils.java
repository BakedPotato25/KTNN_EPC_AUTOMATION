package com.ktnn.utils.configloader;


import com.ktnn.consts.FrameConst;
import com.ktnn.utils.LanguageUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.*;

import static com.ktnn.consts.FrameConst.LANG_EN;
import static com.ktnn.consts.FrameConst.LANG_VI;
import static java.util.Locale.ENGLISH;

@Getter
@Setter
@Slf4j
public abstract class AbsPropertyUtils {
    private static ResourceBundle LANGUAGE_RESOURCE;
    public static Properties properties;
    protected static Map<String, ResourceBundle> ERROR_CODE_RESOURCE = new HashMap<>();

    public static void loadLanguageBundle(String language) {
        Locale locale = new Locale(LANG_VI, "VI");
        switch (language.toLowerCase()) {
            case LANG_EN:
                locale = ENGLISH;
                break;
            case LANG_VI:
            default:    // Default value: VI
                break;
        }
        LANGUAGE_RESOURCE = ResourceBundle.getBundle("language", locale);
    }

    /**
     * Get language for application
     *
     * @param key : Language keyword
     * @return : The value of language keyword
     */
    public static String getLanguageValue(String key) {
        try {
            if (LANGUAGE_RESOURCE == null) loadLanguageBundle(FrameConst.AppConfig.APP_LANGUAGE);
            return LANGUAGE_RESOURCE.getString(key);
        } catch (Exception e) {
            return "LANGUAGE_NOT_FOUND";
        }
    }

    public static String getValue(String key) {
        String keyValue = null;
        try {
            // Lấy giá trị từ file đã Set
            keyValue = properties.getProperty(key);
            return LanguageUtils.convertCharset_ISO_8859_1_To_UTF8(keyValue);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return keyValue;
    }

    public static Boolean getBoolValue(String key) {
        String value = getValue(key);
        try {
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            log.error("VException: getBoolValue: {}", e.getMessage());
        }
        return false;
    }

    public void releaseLanguageResource() {
        LANGUAGE_RESOURCE = null;
    }

    /**
     * Load all errCode for the testing project.
     */
    public void loadErrorBundle() {
        ERROR_CODE_RESOURCE.put(LANG_VI, ResourceBundle.getBundle("errCode", new Locale(LANG_VI)));
        ERROR_CODE_RESOURCE.put(LANG_EN, ResourceBundle.getBundle("errCode", new Locale(LANG_EN)));
    }

    /**
     * Update property
     *
     * @param property : Properties object
     * @param key      : Key
     * @param value    : Value
     */
    public abstract void updateProperty(Properties property, String key, String value, boolean... hasBoolean) ;

    public abstract void executionInfo(Properties properties);

    /**
     * Load all properties for the testing project.
     */
    public abstract void loadAllProperties();

    /**
     * Update env and property
     */
    public abstract void updateMavenProperties(Properties property, JSONObject configObjects);
}
