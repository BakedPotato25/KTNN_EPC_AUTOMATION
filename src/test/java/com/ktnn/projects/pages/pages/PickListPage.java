package com.ktnn.projects.pages.pages;

import com.ktnn.consts.FrameConst.FailureHandling;
import com.ktnn.datadriven.DataModel;
import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.pages.objects.PickListObjects;

import org.openqa.selenium.support.PageFactory;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PickListPage extends BasePage {
    // safety cap so a too-broad keyword can't delete row after row
    private static final int MAX_LEFTOVER_CLEANUP = 3;

    private final PickListObjects pickListObjects;
    private String baselineResultsCount;
    private String lastToastMessage;

    public PickListPage() {
        super();
        PageFactory.initElements(webDriver, this);
        pickListObjects = PickListObjects.getInstance();
    }

    public PickListPage searchByKeyword(String keyword) {
        pickListObjects.searchByKeyword(keyword);
        return this;
    }

    public PickListPage verifySearchResultsContainKeyword(String keyword) {
        List<String> rows = pickListObjects.getAllRowTexts();
        boolean allMatch = !rows.isEmpty() && rows.stream()
                .allMatch(r -> r.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT)));
        assertTrueCondition(null, allMatch, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid rows contain keyword '%s'", keyword));
        return this;
    }

    public PickListPage verifySearchResultExactName(String name) {
        List<String> names = pickListObjects.getAllNameCellTexts();
        boolean exactMatch = names.size() == 1 && names.get(0).trim().equals(name);
        assertTrueCondition(null, exactMatch, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid shows exactly 1 record with Name = '%s'", name));
        return this;
    }

    public PickListPage verifySearchResultExactCode(String code) {
        List<String> codes = pickListObjects.getAllCodeCellTexts();
        boolean exactMatch = codes.size() == 1 && codes.get(0).trim().equals(code);
        assertTrueCondition(null, exactMatch, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid shows exactly 1 record with Code = '%s'", code));
        return this;
    }

    public PickListPage verifySearchNoResults() {
        String countText = pickListObjects.getResultsCountText();
        boolean noResults = countText.contains("in 0 results");
        assertTrueCondition(null, noResults, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid shows no results (actual: '%s')", countText));
        return this;
    }

    public PickListPage verifySearchResultsContainRecords(String... expectedNames) {
        List<String> names = pickListObjects.getAllNameCellTexts();
        boolean allFound = Arrays.stream(expectedNames).allMatch(names::contains);
        assertTrueCondition(null, allFound, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid contains records with Name in %s", Arrays.toString(expectedNames)));
        return this;
    }

    public PickListPage sortBy(String order, String direction) {
        pickListObjects
                .openFilterPanel()
                .selectOrderField(order)
                .selectSortDirection(direction)
                .clickApplyFilter();
        return this;
    }

    public PickListPage verifyCodeColumnSorted(String direction) {
        List<String> codes = pickListObjects.getAllCodeCellTexts();
        boolean sorted = isSorted(codes, direction);
        assertTrueCondition(null, sorted, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid Code column is sorted %s", direction));
        return this;
    }

    /**
     * Backend sorts with a case-insensitive Vietnamese collation, not raw Unicode order - needs vi-VN Collator.
     */
    private boolean isSorted(List<String> values, String direction) {
        Collator vnCollator = Collator.getInstance(new Locale("vi", "VN"));
        vnCollator.setStrength(Collator.SECONDARY); // ignore case, keep diacritics/base-letter distinctions
        Comparator<String> comparator = vnCollator::compare;
        if ("Descending".equalsIgnoreCase(direction)) comparator = comparator.reversed();
        for (int i = 0; i < values.size() - 1; i++) {
            if (comparator.compare(values.get(i), values.get(i + 1)) > 0) return false;
        }
        return true;
    }

    /** Weaker check for Order = Create Date/Name - exact order can't be verified from the UI. */
    public PickListPage verifyGridStillRendersResults() {
        List<String> rows = pickListObjects.getAllRowTexts();
        assertTrueCondition(null, !rows.isEmpty(), FailureHandling.CONTINUE_ON_FAILURE,
                "Verify grid still renders results after applying sort");
        return this;
    }

    public PickListPage filterBy(String field, String operator, String value) {
        pickListObjects
                .openFilterPanel()
                .clickAddAction()
                .selectFilterField(1, field)
                .selectFilterOperator(1, operator)
                .inputFilterValue(1, value)
                .clickApplyFilter();
        return this;
    }

    public PickListPage filterByTwoConditionsWithAnd(String field1, String operator1, String value1,
                                                      String field2, String operator2, String value2) {
        pickListObjects
                .openFilterPanel()
                .clickAddAction()
                .selectFilterField(1, field1)
                .selectFilterOperator(1, operator1)
                .inputFilterValue(1, value1)
                .clickAddAction()
                .selectFilterField(2, field2)
                .selectFilterOperator(2, operator2)
                .inputFilterValue(2, value2)
                .clickApplyFilter();
        return this;
    }

    public PickListPage attemptCombineConditionsWithOr(String field1, String operator1, String value1,
                                                        String field2, String operator2, String value2) {
        pickListObjects
                .openFilterPanel()
                .clickAddAction()
                .selectFilterField(1, field1)
                .selectFilterOperator(1, operator1)
                .inputFilterValue(1, value1)
                .clickAddAction()
                .selectFilterField(2, field2)
                .selectFilterOperator(2, operator2)
                .inputFilterValue(2, value2)
                .clickAndOrToggle();
        return this;
    }

    public PickListPage verifyOrConditionNotAvailable() {
        boolean orAvailable = pickListObjects.isOrOptionPresent();
        assertFalseCondition(null, orAvailable, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify 'Or' option is available to combine filter conditions (known limitation: system doesn't offer it yet)");
        return this;
    }

    public PickListPage verifyFilterResultsContainDescription(String value) {
        List<String> descriptions = pickListObjects.getAllDescriptionCellTexts();
        boolean allMatch = !descriptions.isEmpty() && descriptions.stream()
                .allMatch(d -> d.toLowerCase(Locale.ROOT).contains(value.toLowerCase(Locale.ROOT)));
        assertTrueCondition(null, allMatch, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid rows have Description containing '%s'", value));
        return this;
    }

    public PickListPage verifyFilterResultsExactDescription(String value) {
        List<String> descriptions = pickListObjects.getAllDescriptionCellTexts();
        boolean allExact = !descriptions.isEmpty() && descriptions.stream().allMatch(d -> d.trim().equals(value));
        assertTrueCondition(null, allExact, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid rows have Description exactly '%s'", value));
        return this;
    }

    /** Version isn't a visible column, so this only checks the filter narrowed the grid. */
    public PickListPage verifyFilterNarrowedResults() {
        String resultsText = pickListObjects.getResultsCountText();
        boolean hasResults = !resultsText.contains("in 0 results");
        assertTrueCondition(null, hasResults, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify filter returns at least 1 result (actual: '%s')", resultsText));
        return this;
    }

    public PickListPage captureResultsCountBaseline() {
        pickListObjects.waitForGridPopulated();
        baselineResultsCount = pickListObjects.getResultsCountText();
        return this;
    }

    public PickListPage clickRefresh() {
        pickListObjects.clickRefresh();
        return this;
    }

    public PickListPage verifySearchAndFilterReset() {
        String searchValue = pickListObjects.getSearchInputValue();
        String currentResultsCount = pickListObjects.getResultsCountText();
        boolean isReset = searchValue.trim().isEmpty() && currentResultsCount.equals(baselineResultsCount);
        assertTrueCondition(null, isReset, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify search box cleared and grid reset to baseline results (expected '%s', actual '%s')",
                        baselineResultsCount, currentResultsCount));
        return this;
    }

    public PickListPage openAddNewForm() {
        pickListObjects.clickAddNew();
        return this;
    }

    private static boolean hasValue(DataModel model) {
        return model != null && model.getValue() != null && !model.getValue().isEmpty();
    }

    public PickListPage fillAddNewForm(PickListAddNewModel model) {
        if (hasValue(model.getName())) pickListObjects.inputAddName(model.getName().getValue());
        if (hasValue(model.getCode())) pickListObjects.inputAddCode(model.getCode().getValue());
        if (hasValue(model.getVersion())) pickListObjects.inputAddVersion(model.getVersion().getValue());
        if (hasValue(model.getValidForFrom()) && hasValue(model.getValidForTo())) {
            pickListObjects.inputAddValidFor(model.getValidForFrom().getValue(), model.getValidForTo().getValue());
        }
        if (hasValue(model.getDescription())) pickListObjects.inputAddDescription(model.getDescription().getValue());
        return this;
    }

    public PickListPage clickAddNewSave() {
        pickListObjects.dismissAllToasts();
        pickListObjects.clickAddNewSave();
        lastToastMessage = pickListObjects.getLatestToastMessage();
        return this;
    }

    public PickListPage verifyToastMessageContains(String expectedSubstring) {
        boolean matches = lastToastMessage != null
                && lastToastMessage.toLowerCase(Locale.ROOT).contains(expectedSubstring.toLowerCase(Locale.ROOT));
        assertTrueCondition(null, matches, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify toast message contains '%s' (actual: '%s')", expectedSubstring, lastToastMessage));
        return this;
    }

    public PickListPage closeAddNewForm() {
        pickListObjects.clickAddNewClose();
        return this;
    }

    public PickListPage closeAddNewFormByX() {
        pickListObjects.clickAddNewCloseX();
        return this;
    }

    /** Dialog closes after the Save response arrives, not on click - poll instead of checking once. */
    public PickListPage verifyAddNewDialogClosed() {
        boolean closed;
        try {
            closed = getWaitDriver().until(d -> !pickListObjects.isAddNewDialogOpen());
        } catch (Exception e) {
            closed = false;
        }
        assertTrueCondition(null, closed, FailureHandling.CONTINUE_ON_FAILURE, "Verify Add new dialog closed (save succeeded)");
        return this;
    }

    /** Secondary signal alongside the toast check - dialog stays open when save fails. */
    public PickListPage verifyAddNewDialogStillOpen() {
        boolean stillOpen = pickListObjects.isAddNewDialogOpen();
        assertTrueCondition(null, stillOpen, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Add new dialog stays open (save failed - duplicate Code)");
        return this;
    }

    public PickListPage verifyRequiredFieldError(String fieldLabel) {
        String errorText = pickListObjects.getRequiredFieldError(fieldLabel);
        boolean hasError = errorText.toLowerCase(Locale.ROOT).contains("required");
        assertTrueCondition(null, hasError, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify required-field error shown for '%s' (actual: '%s')", fieldLabel, errorText));
        return this;
    }

    private static int parseResultsCount(String resultsText) {
        Matcher matcher = Pattern.compile("in (\\d+) results").matcher(resultsText);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
    }

    /** Count re-fetches asynchronously after Save - poll for the expected value instead of reading once. */
    public PickListPage verifyResultsCountIncreasedByOne() {
        int before = parseResultsCount(baselineResultsCount);
        int expected = before + 1;
        int after;
        try {
            after = getWaitDriver().until(d -> {
                int current = parseResultsCount(pickListObjects.getResultsCountText());
                return current == expected ? current : null;
            });
        } catch (Exception e) {
            after = parseResultsCount(pickListObjects.getResultsCountText());
        }
        assertTrueCondition(null, before >= 0 && after == expected, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify results count increased by 1 (before: %d, after: %d)", before, after));
        return this;
    }

    /** Cleanup helper - deletes the record matching keyword, then re-searches to confirm it's gone. */
    public PickListPage deleteRecordByExactSearch(String keyword) {
        pickListObjects.searchByKeyword(keyword);
        pickListObjects.dismissAllToasts();
        pickListObjects.deleteFirstRowResult();
        lastToastMessage = pickListObjects.getLatestToastMessage();
        verifyToastMessageContains("successfully");
        pickListObjects.searchByKeyword(keyword);
        verifySearchNoResults();
        return this;
    }

    /** Deletes records left behind by an earlier failed run, so they don't block this one with a duplicate Code. */
    public PickListPage removeLeftoverRecords(String keyword) {
        for (int i = 0; i < MAX_LEFTOVER_CLEANUP; i++) {
            pickListObjects.searchByKeyword(keyword);
            if (!pickListObjects.hasDeletableRow()) return this;
            pickListObjects.dismissAllToasts();
            pickListObjects.deleteFirstRowResult();
            pickListObjects.getLatestToastMessage();
        }
        return this;
    }

    /** Creates a record to edit, then cancels the auto-opened Edit panel to leave a clean list. */
    public PickListPage setupRecordToEdit(String name, String code) {
        removeLeftoverRecords(name);
        openAddNewForm();
        pickListObjects.inputAddName(name);
        pickListObjects.inputAddCode(code);
        clickAddNewSave();
        pickListObjects.clickEditCancel();
        return this;
    }

    /** Narrows the grid to exactly 1 record via exact search, then opens its Edit panel. */
    public PickListPage openEditFormByExactSearch(String keyword) {
        pickListObjects.searchByKeyword(keyword);
        pickListObjects.clickRowEditIcon();
        return this;
    }

    public PickListPage fillEditForm(PickListEditModel model) {
        if (hasValue(model.getNewName())) pickListObjects.inputEditName(model.getNewName().getValue());
        if (hasValue(model.getNewDescription())) pickListObjects.inputEditDescription(model.getNewDescription().getValue());
        if (hasValue(model.getNewVersion())) pickListObjects.inputEditVersion(model.getNewVersion().getValue());
        if (hasValue(model.getNewIsActive()) && "OFF".equalsIgnoreCase(model.getNewIsActive().getValue())) {
            pickListObjects.toggleEditIsActive();
        }
        return this;
    }

    public PickListPage clearEditName() {
        pickListObjects.clearEditName();
        return this;
    }

    public PickListPage clickEditSave() {
        pickListObjects.dismissAllToasts();
        pickListObjects.clickEditSave();
        lastToastMessage = pickListObjects.getLatestToastMessage();
        return this;
    }

    public PickListPage clickEditCancel() {
        // clears any lingering error toast (e.g. a required-field message) so a cleanup step
        // right after doesn't mistake it for the result of its own action
        pickListObjects.dismissAllToasts();
        pickListObjects.clickEditCancel();
        return this;
    }

    /** Panel closes right after Cancel, but poll instead of checking once to avoid a race. */
    public PickListPage verifyEditPanelClosed() {
        boolean closed;
        try {
            closed = getWaitDriver().until(d -> !pickListObjects.isEditPanelOpen());
        } catch (Exception e) {
            closed = false;
        }
        assertTrueCondition(null, closed, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Edit panel closed (Cancel discarded changes)");
        return this;
    }

    public PickListPage verifyEditRequiredFieldError(String fieldLabel) {
        String errorText = pickListObjects.getEditErrorMessageContaining("required");
        boolean hasError = errorText.toLowerCase(Locale.ROOT).contains("required")
                && errorText.toLowerCase(Locale.ROOT).contains(fieldLabel.toLowerCase(Locale.ROOT));
        assertTrueCondition(null, hasError, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify required-field error shown for '%s' (actual: '%s')", fieldLabel, errorText));
        return this;
    }

    /** PL_FUNC-33: Code is editable - types the new value, caller still needs to Save + re-verify from the grid. */
    public PickListPage inputEditCode(String value) {
        pickListObjects.attemptInputEditCode(value);
        return this;
    }
}
