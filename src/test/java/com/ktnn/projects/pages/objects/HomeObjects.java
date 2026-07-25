package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.HomeLocator;
import lombok.Getter;
import org.openqa.selenium.By;

@Getter
public class HomeObjects extends BaseObjects {
    @Getter
    public static HomeObjects instance = new HomeObjects();

    private final HomeLocator homeLocator;

    private HomeObjects() {
        homeLocator = HomeLocator.getInstance();
    }

    public void waitForPageLoaded() {
        waitForElementInvisible(By.cssSelector(homeLocator.getLoadingOverlay()), 30);
    }
}
