package com.ktnn.projects.pages;

import com.ktnn.driver.DriverManager;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.LoginPage;

import static com.ktnn.consts.FrameConst.AppConfig.APP_DOMAIN;

public class PageManagement {
    public static LoginPage accessWebPage() {
        DriverManager.getDriver().get(APP_DOMAIN);
        return new LoginPage();
    }

    public static HomePage gotoHomePage() {
        return new HomePage();
    }
}
