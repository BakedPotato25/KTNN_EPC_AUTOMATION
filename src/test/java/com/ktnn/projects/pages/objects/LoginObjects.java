package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.LoginLocator;
import lombok.Getter;
import org.openqa.selenium.WebElement;

@Getter
public class LoginObjects extends BaseObjects {
    @Getter
    public static LoginObjects instance = new LoginObjects();

    private final LoginLocator loginLocator;

    private LoginObjects() {
        loginLocator = LoginLocator.getInstance();
    }

    public WebElement findUsernameInput() {
        return findWebElement(loginLocator.getTxtUsername());
    }

    public WebElement findPasswordInput() {
        return findWebElement(loginLocator.getTxtPassword());
    }

    public WebElement findLoginButton() {
        return findWebElement(loginLocator.getBtnLogin());
    }

    public LoginObjects inputUsername(String value) {
        inputText(findUsernameInput(), "Username", value);
        return this;
    }

    public LoginObjects inputPassword(String value) {
        inputText(findPasswordInput(), "Password", value);
        return this;
    }

    public LoginObjects clickLoginButton() {
        clickByJS(findLoginButton(), "Login");
        return this;
    }
}
