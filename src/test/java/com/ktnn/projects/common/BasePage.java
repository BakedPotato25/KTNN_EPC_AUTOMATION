package com.ktnn.projects.common;

import com.ktnn.consts.FrameConst;
import com.ktnn.consts.FrameConst.LogType;
import com.ktnn.consts.ProjectConst;
import com.ktnn.controller.WebUI;
import com.ktnn.driver.DriverManager;
import com.ktnn.projects.pages.locator.HomeLocator;
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
 * Base methods used by every page object to interact with elements.
 * All Page classes (e.g. LoginPage, DashboardPage) must extend this class.
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
     * Go to specific URL and verify navigation succeeded.
     *
     * @param URL       : URL Page
     * @param pageTitle : Page title (for report/assert message)
     */
    protected void goToSpecificURL(String URL, String pageTitle) {
        goToURL(URL);
        assertTrueCondition(null, verifyPageUrl(URL), FrameConst.FailureHandling.CONTINUE_ON_FAILURE, String.format("Verify the '%s' page", pageTitle));
        String msg = BOLD_START + Icon.ICON_NAVIGATE_RIGHT + " Go to URL : " + BOLD_END + DriverManager.getDriver().getCurrentUrl();
        addReportInfo(LogType.INFO, msg, null, null);
    }

    /**
     * Hash deep-links get redirected back to home on this app, so we click the card instead.
     */
    protected void gotoModuleViaHomeCard(ProjectConst.ModuleURL module) {
        // Close leftover module tab from the previous test, else tabs pile up across the suite
        closeActiveModuleTab();

        // Don't assume we're on Home already - previous test may have left a module page open
        WebElement homeTab = findWebElement(HomeLocator.getInstance().getTabHome());
        clickByJS(homeTab, "Home");
        waitForElementVisible(getByXpathDynamic(HomeLocator.getInstance().getCardByName(), module.getName()));
        WebElement card = findWebElement(getByXpathDynamic(HomeLocator.getInstance().getCardByName(), module.getName()));
        clickByJS(card, module.getName());
        try {
            // single-spa route change is async, URL updates only after the module mounts
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

    private void closeActiveModuleTab() {
        List<WebElement> closeIcons = getListWebElement(By.xpath(HomeLocator.getInstance().getCloseActiveTabIcon()));
        if (!closeIcons.isEmpty()) {
            clickByJS(closeIcons.get(0), "Close current tab");
        }
    }
    //endregion

    /**
     * Wait until the 'value' attribute of an input element is not empty
     */
    protected void waitForInputValueNotEmpty(WebElement element) {
        getWaitDriver().until(ExpectedConditions.attributeToBeNotEmpty(element, "value"));
    }

    /**
     * Wait until the text/value of an element is not empty
     */
    protected void waitForElementValueNotEmpty(WebElement element) {
        getWaitDriver().until(driver -> !getValueOfElement(element).trim().isEmpty());
    }
}
