package com.ktnn.projects.pages.locator;

import lombok.Getter;

@Getter
public class HomeLocator extends BaseLocator {
    @Getter
    public static HomeLocator instance = new HomeLocator();

    private HomeLocator() {
    }

    String loadingOverlay = ".loading-overlay";

    // giới hạn phạm vi trong grid - text này cũng nằm ẩn trong sidebar menu
    String cardByName = "//div[contains(@class,'product-offer-list')]//span[normalize-space()='%s']";

    // tab div, không phải <a> router-link zero-width bên trong nó
    String tabHome = "//div[contains(@class,'tab')][normalize-space()='Home']";

    // icon "x" trên tab module đang active - tab Home không có
    String closeActiveTabIcon = "//div[contains(@class,'tab') and contains(@class,'active')]//span[contains(@class,'fa-times-circle-o')]";
}
