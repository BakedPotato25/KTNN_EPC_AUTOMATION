package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.PickListLocator;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PickListObjects extends BaseObjects {
    @Getter
    public static PickListObjects instance = new PickListObjects();

    private final PickListLocator pickListLocator;

    private PickListObjects() {
        pickListLocator = PickListLocator.getInstance();
    }

    public WebElement findSearchInput() {
        return findWebElement(pickListLocator.getTxtSearch());
    }

    public PickListObjects searchByKeyword(String keyword) {
        WebElement searchInput = findSearchInput();
        inputText(searchInput, "Search", keyword);
        searchInput.sendKeys(Keys.ENTER);
        waitFor(1.5); // grid re-renders right after Enter, rows go stale if we read too soon
        return this;
    }

    public List<String> getAllRowTexts() {
        return getListWebElement(By.xpath(pickListLocator.getRowGrid())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    public List<String> getAllNameCellTexts() {
        return getListWebElement(By.xpath(pickListLocator.getCellName())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    public List<String> getAllCodeCellTexts() {
        return getListWebElement(By.xpath(pickListLocator.getCellCode())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    public List<String> getAllDescriptionCellTexts() {
        return getListWebElement(By.xpath(pickListLocator.getCellDescription())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    public String getResultsCountText() {
        return getTextElement(findWebElement(pickListLocator.getLblResultsCount()));
    }

    public String getSearchInputValue() {
        return getValueOfElement(findSearchInput());
    }

    public PickListObjects openFilterPanel() {
        clickByJS(findWebElement(pickListLocator.getIcoFilterToggle()), "Order/Sort/Filter toggle");
        return this;
    }

    private void selectDropdownOption(WebElement trigger, String optionText) {
        clickByJS(trigger, optionText);
        WebElement option = findWebElement(getByXpathDynamic(pickListLocator.getOptionByText(), optionText));
        clickByJS(option, optionText);
    }

    public PickListObjects selectOrderField(String fieldName) {
        selectDropdownOption(findWebElement(pickListLocator.getCboOrderTrigger()), fieldName);
        return this;
    }

    public PickListObjects selectSortDirection(String direction) {
        selectDropdownOption(findWebElement(pickListLocator.getCboSortTrigger()), direction);
        return this;
    }

    public PickListObjects clickAddAction() {
        clickByJS(findWebElement(pickListLocator.getBtnAddAction()), "Add Action");
        return this;
    }

    public PickListObjects selectFilterField(int rowIndex, String fieldName) {
        WebElement trigger = findWebElement(getByXpathDynamic(pickListLocator.getCboFilterFieldByRow(), String.valueOf(rowIndex)));
        selectDropdownOption(trigger, fieldName);
        return this;
    }

    public PickListObjects selectFilterOperator(int rowIndex, String operator) {
        WebElement trigger = findWebElement(getByXpathDynamic(pickListLocator.getCboFilterOperatorByRow(), String.valueOf(rowIndex)));
        selectDropdownOption(trigger, operator);
        return this;
    }

    public PickListObjects inputFilterValue(int rowIndex, String value) {
        WebElement input = findWebElement(getByXpathDynamic(pickListLocator.getTxtFilterValueByRow(), String.valueOf(rowIndex)));
        inputText(input, "Filter value row " + rowIndex, value);
        return this;
    }

    public PickListObjects clickAndOrToggle() {
        clickByJS(findWebElement(pickListLocator.getIcoAndOrToggle()), "And/Or toggle");
        return this;
    }

    public boolean isOrOptionPresent() {
        return !getListWebElement(By.xpath(String.format(pickListLocator.getOptionByText(), "Or"))).isEmpty();
    }

    public PickListObjects clickApplyFilter() {
        clickByJS(findWebElement(pickListLocator.getBtnApplyFilter()), "Apply Order/Sort/Filter");
        waitFor(1.5); // grid re-renders right after apply, rows go stale if we read too soon
        return this;
    }

    public PickListObjects clickRefresh() {
        clickByJS(findWebElement(pickListLocator.getBtnRefresh()), "Refresh");
        waitFor(1.5);
        return this;
    }
}
