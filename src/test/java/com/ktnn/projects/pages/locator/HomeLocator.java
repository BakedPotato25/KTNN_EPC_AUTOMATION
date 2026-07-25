package com.ktnn.projects.pages.locator;

import lombok.Getter;

@Getter
public class HomeLocator extends BaseLocator {
    @Getter
    public static HomeLocator instance = new HomeLocator();

    private HomeLocator() {
    }

    String loadingOverlay = ".loading-overlay";

    // scoped to the grid - same text also sits hidden in the sidebar menu
    String cardByName = "//div[contains(@class,'product-offer-list')]//span[normalize-space()='%s']";

    // tab div, not the zero-width <a> router-link inside it
    String tabHome = "//div[contains(@class,'tab')][normalize-space()='Home']";

    // "x" icon on the active module tab - Home tab has none
    String closeActiveTabIcon = "//div[contains(@class,'tab') and contains(@class,'active')]//span[contains(@class,'fa-times-circle-o')]";
}
