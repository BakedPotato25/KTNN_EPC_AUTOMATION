package com.ktnn.projects.pages.objects;

import com.ktnn.driver.DriverManager;
import com.ktnn.projects.common.BasePage;
import lombok.Getter;
import org.openqa.selenium.WebDriver;

@Getter
public class BaseObjects extends BasePage {

    protected WebDriver webDriver;

    public BaseObjects() {
        this.webDriver = DriverManager.getDriver();
    }
}
