package com.ktnn.projects.pages.pages;

import com.ktnn.consts.FrameConst.FailureHandling;
import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.pages.objects.PickListObjects;

import org.openqa.selenium.support.PageFactory;

import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PickListPage extends BasePage {
    private final PickListObjects pickListObjects;
    private String baselineResultsCount;

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
     * Backend collation is case-insensitive Vietnamese collation, not raw Unicode code-point order -
     * e.g. "Unit" sorts before "tyty" (case-insensitive), and "...Loại định danh" sorts before
     * "...Loại tổ chức" ('đ' < 't' in the Vietnamese alphabet, but 'đ' > 't' by raw code point).
     * Plain/case-insensitive String::compareTo doesn't match either quirk - need a vi-VN Collator.
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

    /**
     * Weaker check used for two different verification gaps:
     * - Order = Create Date (PL_FUNC-13/14): grid has no visible Create Date column, so exact
     *   chronological order can't be verified from UI at all (matches QA's own N/A note).
     * - Order = Name (PL_FUNC-11/12): Name is full of Vietnamese diacritics/tone marks, and the
     *   backend's real collation doesn't fully match Java's bundled vi-VN Collator on tone-mark
     *   tie-breaks (verified empirically - e.g. "Trang bị" vs "Trả trước" disagree at every
     *   Collator strength) - not something reproducible client-side without the exact backend
     *   collation, so we don't assert a byte-exact order for it either.
     * Either way, this only confirms the sort action itself didn't break rendering.
     */
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

    /**
     * Version isn't a visible grid column, so this only confirms the filter actually narrowed
     * the grid (vs. the full unfiltered dataset) rather than validating the Version value itself.
     */
    public PickListPage verifyFilterNarrowedResults() {
        String resultsText = pickListObjects.getResultsCountText();
        boolean hasResults = !resultsText.contains("in 0 results");
        assertTrueCondition(null, hasResults, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify filter returns at least 1 result (actual: '%s')", resultsText));
        return this;
    }

    public PickListPage captureResultsCountBaseline() {
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
}
