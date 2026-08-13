package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.CharacteristicCatalogLocator;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

@Getter
public class CharacteristicCatalogObjects extends BaseObjects {
    @Getter
    public static CharacteristicCatalogObjects instance = new CharacteristicCatalogObjects();

    private final CharacteristicCatalogLocator locator;

    private CharacteristicCatalogObjects() {
        locator = CharacteristicCatalogLocator.getInstance();
    }

    public WebElement findSearchInput() {
        return findWebElement(locator.getTxtSearch());
    }

    /** Chờ lưới re-render sau search - không chờ sẽ đọc nhầm trạng thái lưới cũ (giống quirk đã biết bên PickList). */
    public CharacteristicCatalogObjects searchByKeyword(String keyword) {
        WebElement searchInput = findSearchInput();
        inputText(searchInput, "Search", keyword);
        List<String> before = getAllRowTexts();
        searchInput.sendKeys(org.openqa.selenium.Keys.ENTER);
        try {
            getWaitDriver().until(d -> !getAllRowTexts().equals(before));
        } catch (Exception ignored) {
        }
        return this;
    }

    public CharacteristicCatalogObjects clickAddNew() {
        clickByJS(findWebElement(locator.getBtnAddNew()), "Add new (+)");
        return this;
    }

    public CharacteristicCatalogObjects inputCatalogName(String value) {
        inputText(findWebElement(locator.getTxtAddCatalogName()), "Catalog - Name", value);
        return this;
    }

    public CharacteristicCatalogObjects inputCatalogCode(String value) {
        inputText(findWebElement(locator.getTxtAddCatalogCode()), "Catalog - Code", value);
        return this;
    }

    /** Nút Save dùng chung cho cả dialog Add new Catalog và dialog Add new Characteristic Specification. */
    public CharacteristicCatalogObjects clickDialogSave() {
        blurActiveElement();
        clickByJS(findWebElement(locator.getBtnAddNewSave()), "Save");
        return this;
    }

    public CharacteristicCatalogObjects clickRowEditIcon() {
        clickByJS(findWebElement(locator.getIcoRowEdit()), "Edit (row icon)");
        return this;
    }

    public CharacteristicCatalogObjects clickRowDeleteIcon() {
        clickByJS(findWebElement(locator.getIcoRowDelete()), "Delete (row icon)");
        return this;
    }

    public CharacteristicCatalogObjects clickConfirmYes() {
        clickByJS(findWebElement(locator.getBtnConfirmYes()), "Yes (confirm delete)");
        return this;
    }

    public CharacteristicCatalogObjects clickCharacteristicSpecificationTab() {
        clickByJS(findWebElement(locator.getTabCharacteristicSpecification()), "Characteristic Specification tab");
        return this;
    }

    public CharacteristicCatalogObjects clickAddSpecification() {
        clickByJS(findWebElement(locator.getBtnAddSpecification()), "Add (+) Characteristic Specification");
        return this;
    }

    public CharacteristicCatalogObjects inputCsName(String value) {
        inputText(findWebElement(locator.getTxtAddCsName()), "Specification - Name", value);
        return this;
    }

    public CharacteristicCatalogObjects inputCsCode(String value) {
        inputText(findWebElement(locator.getTxtAddCsCode()), "Specification - Code", value);
        return this;
    }

    /**
     * ChromeDriver isDisplayed() báo sai (invisible) cho các combobox trong dialog "Add new Characteristic
     * Specification" dù element render bình thường (đã verify qua JS getBoundingClientRect/computed style lúc debug) -
     * dùng presence thay vì visible để tránh false-negative, clickByJS không phụ thuộc isDisplayed() nên vẫn click được.
     */
    private void selectDropdownOption(By triggerBy, String optionText) {
        clickByJS(waitForElementPresent(triggerBy), optionText);
        WebElement option = findWebElement(getByXpathDynamic(locator.getOptionByText(), optionText));
        clickByJS(option, optionText);
    }

    public CharacteristicCatalogObjects selectValueType(String valueType) {
        selectDropdownOption(By.xpath(locator.getCboValueType()), valueType);
        return this;
    }

    /** PickList field có ô Search riêng bên trong overlay - phải gõ tìm trước khi option hiện ra. */
    public CharacteristicCatalogObjects selectPickList(String pickListName) {
        clickByJS(findWebElement(locator.getCboPickList()), "PickList");
        WebElement searchInput = findWebElement(locator.getTxtPickListDropdownSearch());
        inputText(searchInput, "PickList search", pickListName);
        WebElement option = findWebElement(getByXpathDynamic(locator.getOptionPickListStartsWith(), pickListName));
        clickByJS(option, pickListName);
        return this;
    }

    public CharacteristicCatalogObjects clickLayoutTab() {
        clickByJS(findWebElement(locator.getTabLayout()), "Layout tab");
        return this;
    }

    public CharacteristicCatalogObjects inputCsTitle(String value) {
        inputText(findWebElement(locator.getTxtCsTitle()), "Specification - Title", value);
        return this;
    }

    public CharacteristicCatalogObjects selectDataType(String dataType) {
        selectDropdownOption(By.xpath(locator.getCboDataType()), dataType);
        return this;
    }

    public CharacteristicCatalogObjects selectControlType(String controlType) {
        selectDropdownOption(By.xpath(locator.getCboControlType()), controlType);
        return this;
    }

    /**
     * Xoá item Specification theo tên hiển thị trong danh sách - dùng khi dọn dẹp dữ liệu test.
     * Dùng presence thay vì visible - cùng quirk ChromeDriver isDisplayed() không đáng tin cậy như selectDropdownOption.
     */
    public CharacteristicCatalogObjects clickDeleteSpecificationByName(String name) {
        clickByJS(waitForElementPresent(getByXpathDynamic(locator.getIcoSpecificationDeleteByName(), name)), "Delete specification " + name);
        return this;
    }

    public boolean hasDeletableRow() {
        return !getListWebElement(By.xpath(locator.getIcoRowDelete())).isEmpty();
    }

    public List<String> getAllRowTexts() {
        return getListWebElement(By.xpath("//table//tbody//tr")).stream()
                .map(el -> getTextElement(el))
                .collect(java.util.stream.Collectors.toList());
    }
}
