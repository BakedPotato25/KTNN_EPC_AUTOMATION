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
        List<String> before = getAllRowTexts();
        searchInput.sendKeys(Keys.ENTER);
        waitForGridToRerender(before);
        return this;
    }

    public List<String> getAllRowTexts() {
        return getListWebElement(By.xpath(pickListLocator.getRowGrid())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    /**
     * Grid re-renders asynchronously after search/filter/sort/refresh - polls the visible row
     * texts instead of a blind sleep so a slow response can't be read too soon. Compares full row
     * content (not just count) because sort only reorders rows without changing how many there
     * are. Falls through on timeout (the rare case where the new state matches the old one)
     * instead of failing here - the caller's own verify step catches a genuinely stale read.
     */
    private void waitForGridToRerender(List<String> before) {
        try {
            getWaitDriver().until(d -> !getAllRowTexts().equals(before));
        } catch (Exception ignored) {
        }
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
        List<String> before = getAllRowTexts();
        clickByJS(findWebElement(pickListLocator.getBtnApplyFilter()), "Apply Order/Sort/Filter");
        waitForGridToRerender(before);
        return this;
    }

    public PickListObjects clickRefresh() {
        List<String> before = getAllRowTexts();
        clickByJS(findWebElement(pickListLocator.getBtnRefresh()), "Refresh");
        waitForGridToRerender(before);
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
        blurActiveElement(); // commits the last field's Vue model before Save reads form state
        clickByJS(findWebElement(pickListLocator.getBtnAddNewSave()), "Save (Add new)");
        return this;
    }

    public PickListObjects clickAddNewClose() {
        clickByJS(findWebElement(pickListLocator.getBtnAddNewClose()), "Close (Add new)");
        waitForAddNewDialogClosed();
        return this;
    }

    public PickListObjects clickAddNewCloseX() {
        clickByJS(findWebElement(pickListLocator.getIcoAddNewCloseX()), "X (Add new)");
        waitForAddNewDialogClosed();
        return this;
    }

    /** Polls instead of a blind sleep - falls through on timeout so the caller's own verify still catches a real "still open" bug. */
    private void waitForAddNewDialogClosed() {
        try {
            getWaitDriver().until(d -> !isAddNewDialogOpen());
        } catch (Exception ignored) {
        }
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

    /**
     * Clears leftover toasts so the next read can't pick up an old message. Clicking close only
     * starts the leave transition - the detail text lingers in the DOM briefly after, so poll
     * until it's actually gone instead of returning right after the click.
     */
    public PickListObjects dismissAllToasts() {
        getJsExecutor().executeScript(
                "document.querySelectorAll('.p-toast-close-button').forEach(b => b.click());");
        try {
            getWaitDriver(3).until(d -> getListWebElement(By.xpath(pickListLocator.getTxtToastDetail())).isEmpty());
        } catch (Exception ignored) {
        }
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

    public WebElement findRowEditIcon() {
        return findWebElement(pickListLocator.getIcoRowEdit());
    }

    public PickListObjects clickRowEditIcon() {
        clickByJS(findRowEditIcon(), "Edit (row icon)");
        waitForElementVisible(By.xpath(pickListLocator.getBtnEditCancel())); // panel finished mounting
        return this;
    }

    public boolean isEditPanelOpen() {
        return !getListWebElement(By.xpath(pickListLocator.getBtnEditCancel())).isEmpty();
    }

    public PickListObjects inputEditName(String value) {
        inputTextWithVerify(findWebElement(pickListLocator.getTxtEditName()), "Edit - Name", value);
        return this;
    }

    /**
     * Clears the Name field entirely to trigger the required-field validation. clear()+sendKeys("")
     * doesn't register here since sendKeys("") is a no-op and clear() alone doesn't notify Vue -
     * needs the native setter + dispatched input event instead.
     */
    public PickListObjects clearEditName() {
        setValueByNativeSetter(findWebElement(pickListLocator.getTxtEditName()), "");
        return this;
    }

    public PickListObjects inputEditDescription(String value) {
        inputText(findWebElement(pickListLocator.getTxtEditDescription()), "Edit - Description", value);
        return this;
    }

    public PickListObjects inputEditVersion(String value) {
        inputText(findWebElement(pickListLocator.getTxtEditVersion()), "Edit - Version", value);
        return this;
    }

    /**
     * PrimeVue's real switch input is intentionally invisible (a styled sibling shows the visual
     * slider), so findWebElement's visibility wait never succeeds on it - look it up by presence
     * only instead.
     */
    public PickListObjects toggleEditIsActive() {
        List<WebElement> matches = getListWebElement(By.xpath(pickListLocator.getSwtEditIsActive()));
        clickByJS(matches.isEmpty() ? null : matches.get(0), "Is Active toggle");
        return this;
    }

    /** Types into Code to prove the field accepts edits (PL_FUNC-33) - caller saves and re-verifies from the grid. */
    public PickListObjects attemptInputEditCode(String value) {
        inputText(findWebElement(pickListLocator.getTxtEditCode()), "Edit - Code (attempt)", value);
        return this;
    }

    public PickListObjects clickEditSave() {
        blurActiveElement(); // commits the last field's Vue model before Save reads form state
        clickByJS(findWebElement(pickListLocator.getBtnEditSave()), "Save (Edit)");
        return this;
    }

    public PickListObjects clickEditCancel() {
        clickByJS(findWebElement(pickListLocator.getBtnEditCancel()), "Cancel (Edit)");
        waitForEditPanelClosed();
        return this;
    }

    /** Polls instead of a blind sleep - falls through on timeout so the caller's own verify still catches a real "still open" bug. */
    private void waitForEditPanelClosed() {
        try {
            getWaitDriver().until(d -> !isEditPanelOpen());
        } catch (Exception ignored) {
        }
    }

    /** Anchor text should be a fragment like "required" - deliberately not the field label, since a
     * required-field marker on the label itself could otherwise false-match. */
    public String getEditErrorMessageContaining(String anchorText) {
        List<WebElement> errors = getListWebElement(getByXpathDynamic(pickListLocator.getErrEditMessageContaining(), anchorText));
        return errors.isEmpty() ? "" : getTextElement(errors.get(0));
    }
}

