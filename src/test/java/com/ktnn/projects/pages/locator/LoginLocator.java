package com.ktnn.projects.pages.locator;

import lombok.Getter;

// Keycloak-rendered page, not part of the app - login button is an <input>, not a <button>
@Getter
public class LoginLocator extends BaseLocator {
    @Getter
    public static LoginLocator instance = new LoginLocator();

    private LoginLocator() {
    }

    String txtUsername = "ID|username";
    String txtPassword = "ID|password";
    String btnLogin = "//input[@type='submit' and @value='Đăng nhập']";
}
