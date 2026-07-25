package com.ktnn.projects.pages.pages;

import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.pages.objects.HomeObjects;
import org.openqa.selenium.support.PageFactory;

public class HomePage extends BasePage {
    private final HomeObjects homeObjects;

    public HomePage() {
        super();
        PageFactory.initElements(webDriver, this);
        homeObjects = HomeObjects.getInstance();
        homeObjects.waitForPageLoaded();
    }
}
