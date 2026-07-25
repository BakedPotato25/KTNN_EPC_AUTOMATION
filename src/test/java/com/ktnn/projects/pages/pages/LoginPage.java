package com.ktnn.projects.pages.pages;

import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.pages.objects.LoginObjects;

import org.openqa.selenium.support.PageFactory;

public class LoginPage extends BasePage {
    private final LoginObjects loginObjects;

    public LoginPage() {
        super();
        PageFactory.initElements(webDriver, this);
        loginObjects = LoginObjects.getInstance();
    }

    public HomePage login(String username, String password) {
        loginObjects
                .inputUsername(username)
                .inputPassword(password)
                .clickLoginButton();
        return new HomePage();
    }
}
