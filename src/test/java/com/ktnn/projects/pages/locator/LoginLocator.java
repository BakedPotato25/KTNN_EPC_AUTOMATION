package com.ktnn.projects.pages.locator;

import lombok.Getter;

// Page do Keycloak render, không thuộc app - nút login là <input>, không phải <button>
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
