package com.ktnn.utils.configloader;

import java.util.Objects;

import static com.ktnn.consts.FrameConst.LANG_EN;
import static com.ktnn.consts.FrameConst.LANG_VI;

/**
 * Class dùng cho ngôn ngữ local
 */
public class APILanguageProperty {
    private static ThreadLocal<String> LANGUAGE = new ThreadLocal<>();

    public static String getExeLanguage() {
        String value = LANGUAGE.get();
        return Objects.nonNull(value) && value.contains(LANG_EN) ? LANG_EN : LANG_VI;
    }

    public static void setLanguage(String language) {
        LANGUAGE.set(language);
    }

    public void unload() {
        LANGUAGE.remove(); // Compliant
    }
}
