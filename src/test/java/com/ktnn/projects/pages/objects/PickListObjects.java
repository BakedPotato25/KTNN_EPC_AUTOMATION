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

    // toast shows ~0.25s after the action and auto-dismisses after ~3.2s
    private static final long TOAST_WAIT_SECONDS = 5;

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

    /** Count text briefly shows "in 0 results" right after navigation - wait past that first. */
    public void waitForGridPopulated() {
        getWaitDriver().until(d -> !getResultsCountText().contains("in 0 results"));
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

    public PickListObjects clickAddNew() {
        clickByJS(findWebElement(pickListLocator.getBtnAddNew()), "Add new (+)");
        return this;
    }

    public boolean isAddNewDialogOpen() {
        return !getListWebElement(By.xpath(pickListLocator.getLblAddNewTitle())).isEmpty();
    }

    /** Retypes once if the value didn't stick - guards against an occasional empty-field flake. */
    private void inputTextWithVerify(WebElement element, String title, String value) {
        inputText(element, title, value);
        if (!value.equals(getValueOfElement(element))) {
            inputText(element, title, value);
        }
    }

    public PickListObjects inputAddName(String value) {
        inputTextWithVerify(findWebElement(pickListLocator.getTxtAddName()), "Add new - Name", value);
        return this;
    }

    public PickListObjects inputAddCode(String value) {
        inputTextWithVerify(findWebElement(pickListLocator.getTxtAddCode()), "Add new - Code", value);
        return this;
    }

    public PickListObjects inputAddVersion(String value) {
        inputText(findWebElement(pickListLocator.getTxtAddVersion()), "Add new - Version", value);
        return this;
    }

    public PickListObjects inputAddDescription(String value) {
        inputText(findWebElement(pickListLocator.getTxtAddDescription()), "Add new - Description", value);
        return this;
    }

    /** Clicks the dialog title after typing to close the calendar popup the date inputs open. */
    public PickListObjects inputAddValidFor(String fromDate, String toDate) {
        WebElement fromInput = findWebElement(getByXpathDynamic(pickListLocator.getTxtAddValidForByIndex(), "1"));
        inputText(fromInput, "Add new - Valid For from", fromDate);
        WebElement toInput = findWebElement(getByXpathDynamic(pickListLocator.getTxtAddValidForByIndex(), "2"));
        inputText(toInput, "Add new - Valid For to", toDate);
        clickByJS(findWebElement(pickListLocator.getLblAddNewTitle()), "Add new (dialog title, closes calendar popup)");
        return this;
    }

    public PickListObjects clickAddNewSave() {
        waitFor(0.5); // lets Vue finish processing the last keystroke before Save reads form state
        clickByJS(findWebElement(pickListLocator.getBtnAddNewSave()), "Save (Add new)");
        return this;
    }

    public PickListObjects clickAddNewClose() {
        clickByJS(findWebElement(pickListLocator.getBtnAddNewClose()), "Close (Add new)");
        waitFor(1);
        return this;
    }

    public PickListObjects clickAddNewCloseX() {
        clickByJS(findWebElement(pickListLocator.getIcoAddNewCloseX()), "X (Add new)");
        waitFor(1);
        return this;
    }

    public String getRequiredFieldError(String fieldLabel) {
        List<WebElement> errors = getListWebElement(getByXpathDynamic(pickListLocator.getErrMsgByFieldLabel(), fieldLabel));
        return errors.isEmpty() ? "" : getTextElement(errors.get(0));
    }

    /** Deletes the 1 row left after a search narrows to the record just created. */
    public PickListObjects deleteFirstRowResult() {
        clickByJS(findWebElement(pickListLocator.getIcoRowDelete()), "Delete (row)");
        clickByJS(findWebElement(pickListLocator.getBtnConfirmYes()), "Yes (confirm delete)");
        return this;
    }

    /** Clears leftover toasts so the next read can't pick up an old message. */
    public PickListObjects dismissAllToasts() {
        getJsExecutor().executeScript(
                "document.querySelectorAll('.p-toast-close-button').forEach(b => b.click());");
        return this;
    }

    /**
     * Reads the toast raised by the previous action - call dismissAllToasts() before that action so
     * only the new one is present. Returns "" if none shows up, letting the caller assert softly.
     */
    public String getLatestToastMessage() {
        try {
            return getWaitDriver(TOAST_WAIT_SECONDS).until(d -> {
                List<WebElement> details = getListWebElement(By.xpath(pickListLocator.getTxtToastDetail()));
                for (int i = details.size() - 1; i >= 0; i--) {
                    String text = getTextElement(details.get(i));
                    if (!text.isEmpty()) return text; // skips the blank node of a leaving toast
                }
                return null;
            });
        } catch (Exception e) {
            return "";
        }
    }
}

