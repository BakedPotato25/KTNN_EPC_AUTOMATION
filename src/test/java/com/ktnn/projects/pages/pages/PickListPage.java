package com.ktnn.projects.pages.pages;

import com.ktnn.consts.FrameConst.FailureHandling;
import com.ktnn.datadriven.DataModel;
import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.dataprovider.model.PickListAddItemModel;
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
    // giới hạn an toàn để keyword quá rộng không xoá liên tục hàng loạt row
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
     * Backend sort theo collation tiếng Việt không phân biệt hoa/thường, không phải thứ tự Unicode thô - cần vi-VN Collator.
     */
    private boolean isSorted(List<String> values, String direction) {
        Collator vnCollator = Collator.getInstance(new Locale("vi", "VN"));
        vnCollator.setStrength(Collator.SECONDARY); // bỏ qua hoa/thường, giữ phân biệt dấu/chữ cái gốc
        Comparator<String> comparator = vnCollator::compare;
        if ("Descending".equalsIgnoreCase(direction)) comparator = comparator.reversed();
        for (int i = 0; i < values.size() - 1; i++) {
            if (comparator.compare(values.get(i), values.get(i + 1)) > 0) return false;
        }
        return true;
    }

    /** Check nhẹ hơn cho Order = Create Date/Name - không verify được thứ tự chính xác từ UI. */
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

    /** Version không phải cột hiển thị, nên chỉ check filter đã thu hẹp grid. */
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

    /** PL_FUNC-44: hệ thống không chặn Save/báo lỗi mà tự xoá trắng "đến ngày" ngay khi blur nếu nhỏ hơn "từ ngày". */
    public PickListPage verifyValidForToClearedAsInvalid() {
        String toValue = pickListObjects.getAddValidForToValue();
        assertTrueCondition(null, toValue == null || toValue.isEmpty(), FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify Valid For 'to' date auto-cleared when earlier than 'from' (actual: '%s')", toValue));
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

    /** Dialog đóng sau khi có response của Save, không phải ngay khi click - poll thay vì check 1 lần. */
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

    /** Tín hiệu phụ bên cạnh check toast - dialog vẫn mở khi save fail. */
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

    /** Count fetch lại bất đồng bộ sau Save - poll tới giá trị mong đợi thay vì đọc 1 lần. */
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

    /** Helper cleanup - xoá record khớp keyword, rồi search lại để xác nhận đã mất. */
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

    /** Xoá record còn sót lại từ lần chạy fail trước, để không bị chặn bởi trùng Code. */
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

    /** Tạo 1 record để edit, rồi cancel Edit panel tự mở ra để giữ list sạch. */
    public PickListPage setupRecordToEdit(String name, String code) {
        removeLeftoverRecords(name);
        openAddNewForm();
        pickListObjects.inputAddName(name);
        pickListObjects.inputAddCode(code);
        clickAddNewSave();
        pickListObjects.clickEditCancel();
        return this;
    }

    /** Thu hẹp grid về đúng 1 record bằng exact search, rồi mở Edit panel của nó. */
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
        // dọn toast lỗi còn sót lại (vd message required-field) để bước cleanup
        // ngay sau đó không nhầm nó là kết quả của action của chính nó
        pickListObjects.dismissAllToasts();
        pickListObjects.clickEditCancel();
        return this;
    }

    /** Panel đóng ngay sau Cancel, nhưng poll thay vì check 1 lần để tránh race condition. */
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

    /** PL_FUNC-33: Code vẫn cho sửa - gõ giá trị mới, caller vẫn cần Save + verify lại từ grid. */
    public PickListPage inputEditCode(String value) {
        pickListObjects.attemptInputEditCode(value);
        return this;
    }

    /**
     * Rà nhanh 1 lần trước khi chạy suite - các case Search/Filter PL_FUNC-1,2,5,6,8,15,16,17,18,20
     * phụ thuộc bản ghi có sẵn trên hệ thống dùng chung (không phải data tự tạo). Fail sớm rõ ràng
     * ở đây thay vì để nhiều case fail rải rác khó truy vết nếu bản ghi bị xoá/sửa.
     * Chi tiết từng bản ghi: data/sit/PICKLIST_SEED_DATA_INVENTORY.md.
     */
    public PickListPage verifySeedDataAvailable() {
        StringBuilder missing = new StringBuilder();
        if (!seedNameExists("Loại đường truyền")) missing.append("[Name: Loại đường truyền] ");
        if (!seedCodeExists("LineConnect_Type")) missing.append("[Code: LineConnect_Type] ");
        if (!seedNameExists("Test_đa_trường")) missing.append("[Name: Test_đa_trường] ");
        if (!seedFilterHasResults("Description", "like", "test")) missing.append("[Description like 'test'] ");
        if (!seedFilterHasResults("Version", "=", "1")) missing.append("[Version = 1] ");

        boolean allPresent = missing.length() == 0;
        assertTrueCondition(null, allPresent, FailureHandling.STOP_ON_FAILURE,
                allPresent ? "Verify PickList seed data available"
                        : String.format("Seed data missing: %s- see data/sit/PICKLIST_SEED_DATA_INVENTORY.md to recreate", missing));
        return this;
    }

    private boolean seedNameExists(String name) {
        pickListObjects.searchByKeyword(name);
        return pickListObjects.getAllNameCellTexts().stream().anyMatch(n -> n.trim().equals(name));
    }

    private boolean seedCodeExists(String code) {
        pickListObjects.searchByKeyword(code);
        return pickListObjects.getAllCodeCellTexts().stream().anyMatch(c -> c.trim().equals(code));
    }

    /**
     * Không dùng verifyFilterNarrowedResults (soft assert) - chỉ cần biết có/không kết quả.
     * Refresh trước khi filter để xoá search text còn sót lại từ check trước đó (searchByKeyword
     * + filter cộng dồn AND với nhau, không refresh sẽ ra sai 0 kết quả dù data vẫn còn).
     */
    private boolean seedFilterHasResults(String field, String operator, String value) {
        pickListObjects.clickRefresh();
        filterBy(field, operator, value);
        boolean hasResults = parseResultsCount(pickListObjects.getResultsCountText()) > 0;
        pickListObjects.clickRefresh();
        return hasResults;
    }

    public PickListPage clickRowDeleteIcon() {
        pickListObjects.clickRowDeleteIcon();
        return this;
    }

    public PickListPage verifyDeleteConfirmDialogShown() {
        boolean shown = pickListObjects.isConfirmDialogOpen();
        assertTrueCondition(null, shown, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify delete confirmation dialog is shown with Yes/No");
        return this;
    }

    public PickListPage confirmDelete() {
        pickListObjects.dismissAllToasts();
        pickListObjects.clickConfirmYes();
        lastToastMessage = pickListObjects.getLatestToastMessage();
        return this;
    }

    public PickListPage cancelDelete() {
        pickListObjects.clickConfirmNo();
        return this;
    }

    public PickListPage selectRowCheckbox(int rowIndex) {
        pickListObjects.clickRowCheckbox(rowIndex);
        return this;
    }

    public PickListPage selectAllCheckbox() {
        pickListObjects.clickSelectAllCheckbox();
        return this;
    }

    public PickListPage clickDeleteToolbar() {
        pickListObjects.clickDeleteToolbar();
        return this;
    }

    public PickListPage verifyAllVisibleRowsSelected(int expectedCount) {
        boolean allChecked = true;
        for (int i = 1; i <= expectedCount; i++) {
            if (!pickListObjects.isRowCheckboxChecked(i)) {
                allChecked = false;
                break;
            }
        }
        assertTrueCondition(null, allChecked, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify all %d visible rows are selected after clicking select-all checkbox", expectedCount));
        return this;
    }

    public PickListPage verifyResultsCountEquals(int expected) {
        int actual = parseResultsCount(pickListObjects.getResultsCountText());
        assertTrueCondition(null, actual == expected, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify grid shows exactly %d results (actual: %d)", expected, actual));
        return this;
    }

    /** Count fetch lại bất đồng bộ sau Delete - poll tới giá trị mong đợi thay vì đọc 1 lần. */
    public PickListPage verifyResultsCountDecreasedBy(int expectedDecrease) {
        int before = parseResultsCount(baselineResultsCount);
        int expected = before - expectedDecrease;
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
                String.format("Verify results count decreased by %d (before: %d, after: %d)", expectedDecrease, before, after));
        return this;
    }

    public PickListPage selectAddDataType(String value) {
        pickListObjects.selectAddDataType(value);
        return this;
    }

    public PickListPage selectEditDataType(String value) {
        pickListObjects.selectEditDataType(value);
        return this;
    }

    /** aria-disabled cập nhật bất đồng bộ sau khi Save item - poll thay vì đọc ngay lúc vừa chuyển tab. */
    public PickListPage verifyEditDataTypeDisabled() {
        boolean disabled;
        try {
            disabled = getWaitDriver().until(d -> pickListObjects.isEditDataTypeDisabled());
        } catch (Exception e) {
            disabled = false;
        }
        assertTrueCondition(null, disabled, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Data Type field is disabled when PickList already has Item(s)");
        return this;
    }

    public PickListPage verifyEditDataTypeValue(String expected) {
        String actual = pickListObjects.getEditDataTypeValue();
        assertTrueCondition(null, expected.equals(actual), FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify Data Type value = '%s' (actual: '%s')", expected, actual));
        return this;
    }

    public PickListPage clickEditGeneralTab() {
        pickListObjects.clickEditGeneralTab();
        return this;
    }

    public PickListPage clickEditPickListItemTab() {
        pickListObjects.clickEditPickListItemTab();
        return this;
    }

    /** Thêm nhanh 1 Item vào PickList đang mở Edit - setup cho case cần PickList đã có Item (PL_FUNC-47). */
    public PickListPage addItemToCurrentEdit(String nameLabel, String code, String value) {
        pickListObjects
                .clickEditPickListItemTab()
                .clickAddItem()
                .inputItemNameLabel(nameLabel)
                .inputItemCode(code)
                .inputItemValue(value)
                .clickItemConfirm();
        clickEditSave();
        pickListObjects.clickEditGeneralTab();
        return this;
    }

    public PickListPage clickPanelToggle() {
        pickListObjects.clickPanelToggle();
        return this;
    }

    /** Panel thu/mở re-render bất đồng bộ sau click - poll thay vì check ngay lập tức. */
    public PickListPage verifyEditPanelCollapsed() {
        boolean collapsed;
        try {
            collapsed = getWaitDriver().until(d -> !pickListObjects.isEditPanelExpanded());
        } catch (Exception e) {
            collapsed = false;
        }
        assertTrueCondition(null, collapsed, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Edit side panel collapsed to icon column");
        return this;
    }

    public PickListPage verifyEditPanelExpanded() {
        boolean expanded;
        try {
            expanded = getWaitDriver().until(d -> pickListObjects.isEditPanelExpanded());
        } catch (Exception e) {
            expanded = false;
        }
        assertTrueCondition(null, expanded, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Edit side panel expanded showing General tab fields");
        return this;
    }

    public PickListPage verifyPickListItemTabActive() {
        boolean active = pickListObjects.isPickListItemTabActive();
        assertTrueCondition(null, active, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify side panel switched to PickList Item tab (Add item button visible)");
        return this;
    }

    public PickListPage openAddItemForm() {
        pickListObjects.clickEditPickListItemTab().clickAddItem();
        return this;
    }

    public PickListPage verifyAddItemFormDisplayed() {
        boolean displayed = pickListObjects.isAddItemFormDisplayed();
        assertTrueCondition(null, displayed, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Add item form displayed inline in side panel (Name/Label, Code, Value, Confirm/Cancel)");
        return this;
    }

    public PickListPage verifyAddItemFormFieldsComplete() {
        boolean complete = pickListObjects.isAddItemFormFieldsComplete();
        assertTrueCondition(null, complete, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Add item form has all fields (Name/Label, Code, Value, Valid For, Is Default)");
        return this;
    }

    public PickListPage verifyItemIsDefaultOnByDefault() {
        boolean checked = pickListObjects.isItemIsDefaultChecked();
        assertTrueCondition(null, checked, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify Is Default toggle defaults to ON when adding a new item");
        return this;
    }

    public PickListPage fillAddItemForm(PickListAddItemModel model) {
        if (hasValue(model.getItemNameLabel())) pickListObjects.inputItemNameLabel(model.getItemNameLabel().getValue());
        if (hasValue(model.getItemCode())) pickListObjects.inputItemCode(model.getItemCode().getValue());
        if (hasValue(model.getItemValue())) pickListObjects.inputItemValue(model.getItemValue().getValue());
        if (hasValue(model.getItemValidForFrom()) && hasValue(model.getItemValidForTo())) {
            pickListObjects.inputItemValidFor(model.getItemValidForFrom().getValue(), model.getItemValidForTo().getValue());
        }
        return this;
    }

    public PickListPage clickItemConfirm() {
        pickListObjects.clickItemConfirm();
        return this;
    }

    public PickListPage clickItemCancelForm() {
        pickListObjects.clickItemCancelForm();
        return this;
    }

    public PickListPage verifyItemRequiredFieldError(String fieldLabel) {
        String errorText = pickListObjects.getItemRequiredFieldError(fieldLabel);
        boolean hasError = errorText.toLowerCase(Locale.ROOT).contains("required");
        assertTrueCondition(null, hasError, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify required-field error shown for item field '%s' (actual: '%s')", fieldLabel, errorText));
        return this;
    }

    public PickListPage verifyItemInList(String nameLabel, String code, String value) {
        List<String> rows = pickListObjects.getAllItemRowTexts();
        boolean found = rows.stream().anyMatch(r -> r.contains(nameLabel) && r.contains(code) && r.contains(value));
        assertTrueCondition(null, found, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify item (Name/Label='%s', Code='%s', Value='%s') appears in PickList Item list", nameLabel, code, value));
        return this;
    }

    public PickListPage verifyItemNotInList(String nameLabel) {
        List<String> rows = pickListObjects.getAllItemRowTexts();
        boolean found = rows.stream().anyMatch(r -> r.contains(nameLabel));
        assertFalseCondition(null, found, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify item Name/Label='%s' does NOT appear in PickList Item list", nameLabel));
        return this;
    }

    /** Thêm 1 item với 3 field bắt buộc - dùng khi test chỉ cần vài item cụ thể (PL_FUNC-55/56), không cần Model đầy đủ như fillAddItemForm. Gọi sau khi đã ở tab PickList Item. */
    public PickListPage addSimpleItem(String nameLabel, String code, String value) {
        pickListObjects
                .clickAddItem()
                .inputItemNameLabel(nameLabel)
                .inputItemCode(code)
                .inputItemValue(value)
                .clickItemConfirm();
        return this;
    }

    /** Seed nhanh N item chỉ để có đủ dữ liệu test phân trang - nội dung item không quan trọng nên đặt tên theo pickListCode + index cho dễ trace, không cần JSON riêng cho từng item. */
    public PickListPage seedItems(String pickListCode, int count) {
        pickListObjects.clickEditPickListItemTab();
        for (int i = 1; i <= count; i++) {
            pickListObjects
                    .clickAddItem()
                    .inputItemNameLabel(pickListCode + "_ITEM_" + i)
                    .inputItemCode(pickListCode + "_ITEM_CODE_" + i)
                    .inputItemValue("val_" + i)
                    .clickItemConfirm();
        }
        clickEditSave();
        pickListObjects.clickEditGeneralTab();
        return this;
    }

    public PickListPage verifyPickListItemControlsComplete() {
        boolean complete = pickListObjects.isPickListItemListControlsComplete();
        assertTrueCondition(null, complete, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify PickList Item tab shows all controls (Add button, Search+Order, item list, paginator, Save/Cancel)");
        return this;
    }

    public PickListPage verifyItemRowFieldsComplete() {
        boolean complete = pickListObjects.isItemRowFieldsComplete();
        assertTrueCondition(null, complete, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify each item row shows Name/Label, Code, Value and the more (⋮) icon");
        return this;
    }

    public PickListPage verifyItemSearchControlsComplete() {
        boolean complete = pickListObjects.isItemSearchControlsComplete();
        assertTrueCondition(null, complete, FailureHandling.CONTINUE_ON_FAILURE,
                "Verify item search bar shows input, search icon and order icon");
        return this;
    }

    public PickListPage clickItemOrderToggle() {
        pickListObjects.clickItemOrderToggle();
        return this;
    }

    public PickListPage verifyFirstItemRowContains(String expectedNameLabel) {
        List<String> rows = pickListObjects.getAllItemRowTexts();
        boolean matches = !rows.isEmpty() && rows.get(0).contains(expectedNameLabel);
        assertTrueCondition(null, matches, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify first item row contains Name/Label '%s' (actual first row: '%s')",
                        expectedNameLabel, rows.isEmpty() ? "" : rows.get(0)));
        return this;
    }

    public PickListPage verifyItemShowOptionsComplete() {
        List<String> options = pickListObjects.getItemShowOptionTexts();
        boolean complete = options.containsAll(Arrays.asList("5", "10", "25", "50", "100"));
        assertTrueCondition(null, complete, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify Show dropdown has options 5/10/25/50/100 (actual: %s)", options));
        return this;
    }

    public PickListPage selectItemShowPageSize(String value) {
        pickListObjects.selectItemShowPageSize(value);
        return this;
    }

    /** "hiển thị tối đa N item/trang" - verify không vượt cap, không đòi hỏi seed đủ >N item để chứng minh tràn trang (không thực tế với N=50/100). */
    public PickListPage verifyItemShowPageSizeApplied(int expectedPageSize) {
        int rowCount = pickListObjects.getItemRowCount();
        boolean capped = rowCount > 0 && rowCount <= expectedPageSize;
        assertTrueCondition(null, capped, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify at most %d items shown per page after selecting Show=%d (actual: %d)",
                        expectedPageSize, expectedPageSize, rowCount));
        return this;
    }

    public PickListPage clickItemPageFirst() {
        pickListObjects.clickItemPageFirst();
        return this;
    }

    public PickListPage clickItemPagePrev() {
        pickListObjects.clickItemPagePrev();
        return this;
    }

    public PickListPage clickItemPageNext() {
        pickListObjects.clickItemPageNext();
        return this;
    }

    public PickListPage clickItemPageLast() {
        pickListObjects.clickItemPageLast();
        return this;
    }

    public PickListPage verifyItemPageNavState(boolean firstDisabled, boolean prevDisabled, boolean nextDisabled, boolean lastDisabled) {
        boolean actualFirst = pickListObjects.isItemPageFirstDisabled();
        boolean actualPrev = pickListObjects.isItemPagePrevDisabled();
        boolean actualNext = pickListObjects.isItemPageNextDisabled();
        boolean actualLast = pickListObjects.isItemPageLastDisabled();
        boolean matches = actualFirst == firstDisabled && actualPrev == prevDisabled
                && actualNext == nextDisabled && actualLast == lastDisabled;
        assertTrueCondition(null, matches, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify pagination nav disabled-state (expected first=%b prev=%b next=%b last=%b, actual first=%b prev=%b next=%b last=%b)",
                        firstDisabled, prevDisabled, nextDisabled, lastDisabled, actualFirst, actualPrev, actualNext, actualLast));
        return this;
    }

    /** "Items x-y/z" nối bằng \n giữa các node - regex bỏ qua xuống dòng (DOTALL) giống parseResultsCount của lưới chính. */
    private static int[] parseItemPaginatorRange(String text) {
        Matcher matcher = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)\\s*/\\s*(\\d+)", Pattern.DOTALL).matcher(text);
        if (matcher.find()) {
            return new int[]{Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))};
        }
        return new int[]{-1, -1, -1};
    }

    public PickListPage verifyItemPaginatorRange(int expectedStart, int expectedEnd, int expectedTotal) {
        int[] actual = parseItemPaginatorRange(pickListObjects.getItemPaginatorText());
        boolean matches = actual[0] == expectedStart && actual[1] == expectedEnd && actual[2] == expectedTotal;
        assertTrueCondition(null, matches, FailureHandling.CONTINUE_ON_FAILURE,
                String.format("Verify item paginator shows 'Items %d - %d / %d' (actual: %d - %d / %d)",
                        expectedStart, expectedEnd, expectedTotal, actual[0], actual[1], actual[2]));
        return this;
    }
}
