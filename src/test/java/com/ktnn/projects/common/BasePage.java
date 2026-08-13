package com.ktnn.projects.common;

import com.ktnn.consts.FrameConst;
import com.ktnn.consts.FrameConst.LogType;
import com.ktnn.consts.ProjectConst;
import com.ktnn.controller.WebUI;
import com.ktnn.driver.DriverManager;
import com.ktnn.projects.pages.locator.HomeLocator;
import com.ktnn.projects.pages.pages.CharacteristicCatalogPage;
import com.ktnn.projects.pages.pages.PickListPage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static com.ktnn.report.ReportConfig.*;

/**
 * Method dùng chung cho mọi page object để thao tác với element.
 * Mọi Page class (vd LoginPage, DashboardPage) đều phải extends class này.
 */
@Getter
@Setter
@Slf4j
public class BasePage extends WebUI {
    public WebDriver webDriver;

    public BasePage() {
        webDriver = DriverManager.getDriver();
    }

    //region Redirect to Page

    /**
     * Điều hướng tới URL cụ thể và verify đã chuyển trang thành công.
     *
     * @param URL       : URL Page
     * @param pageTitle : Tên page (dùng cho report/assert message)
     */
    protected void goToSpecificURL(String URL, String pageTitle) {
        goToURL(URL);
        assertTrueCondition(null, verifyPageUrl(URL), FrameConst.FailureHandling.CONTINUE_ON_FAILURE, String.format("Verify the '%s' page", pageTitle));
        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Go to URL : " + BOLD_END + DriverManager.getDriver().getCurrentUrl();
        addReportInfo(LogType.INFO, msg, null, null);
    }

    /**
     * App này redirect về home nếu gọi hash deep-link trực tiếp, nên phải click qua card thay vì set URL.
     */
    protected void gotoModuleViaHomeCard(ProjectConst.ModuleURL module) {
        // Đóng tab module còn sót lại từ test trước, không sẽ tích tab đầy suốt suite
        closeActiveModuleTab();

        // Không giả định đang ở Home - test trước có thể để lại 1 page module đang mở
        WebElement homeTab = findWebElement(HomeLocator.getInstance().getTabHome());
        clickByJS(homeTab, "Home");
        waitForElementVisible(getByXpathDynamic(HomeLocator.getInstance().getCardByName(), module.getName()));
        WebElement card = findWebElement(getByXpathDynamic(HomeLocator.getInstance().getCardByName(), module.getName()));
        clickByJS(card, module.getName());
        try {
            // single-spa chuyển route bất đồng bộ, URL chỉ đổi sau khi module mount xong
            getWaitDriver().until(ExpectedConditions.urlContains(module.getPath()));
        } catch (Exception e) {
            log.error("gotoModuleViaHomeCard: URL never changed to contain '{}'", module.getPath());
        }
        assertTrueCondition(null, verifyPageUrl(module.getPath()), FrameConst.FailureHandling.CONTINUE_ON_FAILURE, String.format("Verify the '%s' page", module.getName()));
    }

    public PickListPage gotoPickListPage() {
        gotoModuleViaHomeCard(ProjectConst.ModuleURL.PICK_LIST);
        return new PickListPage();
    }

    public CharacteristicCatalogPage gotoCharacteristicCatalogPage() {
        gotoModuleViaHomeCard(ProjectConst.ModuleURL.CHARACTERISTIC_CATALOG);
        return new CharacteristicCatalogPage();
    }

    private void closeActiveModuleTab() {
        List<WebElement> closeIcons = getListWebElement(By.xpath(HomeLocator.getInstance().getCloseActiveTabIcon()));
        if (!closeIcons.isEmpty()) {
            clickByJS(closeIcons.get(0), "Close current tab");
        }
    }
    //endregion

    /**
     * Chờ tới khi attribute 'value' của input element không rỗng
     */
    protected void waitForInputValueNotEmpty(WebElement element) {
        getWaitDriver().until(ExpectedConditions.attributeToBeNotEmpty(element, "value"));
    }

    /**
     * Chờ tới khi text/value của element không rỗng
     */
    protected void waitForElementValueNotEmpty(WebElement element) {
        getWaitDriver().until(driver -> !getValueOfElement(element).trim().isEmpty());
    }
}
