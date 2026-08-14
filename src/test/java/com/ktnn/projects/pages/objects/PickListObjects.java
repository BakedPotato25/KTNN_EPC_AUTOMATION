package com.ktnn.projects.pages.objects;

import com.ktnn.projects.pages.locator.PickListLocator;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PickListObjects extends BaseObjects {
    @Getter
    public static PickListObjects instance = new PickListObjects();

    // toast hiện ra ~0.25s sau action và tự biến mất sau ~3.2s
    private static final long TOAST_WAIT_SECONDS = 5;

    private final PickListLocator pickListLocator;

    private PickListObjects() {
        pickListLocator = PickListLocator.getInstance();
    }

    public WebElement findSearchInput() {
        return findWebElement(pickListLocator.getTxtSearch());
    }

    /**
     * Retry 1 lần nếu bị stale: app re-render toàn bộ list area sau khi save/delete,
     * có thể thay search box giữa lúc find và lúc gõ vào nó.
     */
    public PickListObjects searchByKeyword(String keyword) {
        try {
            typeSearchAndSubmit(keyword);
        } catch (StaleElementReferenceException e) {
            typeSearchAndSubmit(keyword);
        }
        return this;
    }

    private void typeSearchAndSubmit(String keyword) {
        WebElement searchInput = findSearchInput();
        inputText(searchInput, "Search", keyword);
        List<String> before = getAllRowTexts();
        searchInput.sendKeys(Keys.ENTER);
        waitForGridToRerender(before);
    }

    public List<String> getAllRowTexts() {
        return getListWebElement(By.xpath(pickListLocator.getRowGrid())).stream()
                .map(el -> getTextElement(el))
                .collect(Collectors.toList());
    }

    /**
     * Poll nội dung row tới khi khác trạng thái trước action, thay vì sleep mù.
     * So sánh toàn bộ nội dung chứ không chỉ số lượng, vì sort chỉ đổi thứ tự các row có sẵn.
     */
    private void waitForGridToRerender(List<String> before) {
        try {
            getWaitDriver().until(d -> !getAllRowTexts().equals(before));
        } catch (Exception ignored) {
        }
    }

    /**
     * Không phải check số lượng row - trạng thái "no data" rỗng cũng render ra &lt;tr&gt;.
     * Chỉ row có data thật mới có icon delete.
     */
    public boolean hasDeletableRow() {
        return !getListWebElement(By.xpath(pickListLocator.getIcoRowDelete())).isEmpty();
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

    /** Text count hiện tạm "in 0 results" ngay sau khi điều hướng - chờ qua trạng thái đó trước. */
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

    /** Gõ lại 1 lần nếu giá trị không ăn - phòng trường hợp field thỉnh thoảng bị rỗng do flake. */
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

    /**
     * Blur thật trước, rồi mới click title dialog để đóng popup calendar mà input ngày mở ra.
     * clickByJS chỉ bắn synthetic MouseEvent, không di chuyển document.activeElement như click thật
     * nên không đủ để trigger blur - app tự xoá trắng "đến ngày" khi nhỏ hơn "từ ngày" chỉ chạy đúng lúc blur thật.
     */
    public PickListObjects inputAddValidFor(String fromDate, String toDate) {
        WebElement fromInput = findWebElement(getByXpathDynamic(pickListLocator.getTxtAddValidForByIndex(), "1"));
        inputText(fromInput, "Add new - Valid For from", fromDate);
        WebElement toInput = findWebElement(getByXpathDynamic(pickListLocator.getTxtAddValidForByIndex(), "2"));
        inputText(toInput, "Add new - Valid For to", toDate);
        blurActiveElement();
        clickByJS(findWebElement(pickListLocator.getLblAddNewTitle()), "Add new (dialog title, closes calendar popup)");
        return this;
    }

    /** PL_FUNC-44: đọc lại giá trị ô "đến ngày" sau khi nhập - hệ thống tự xoá trắng nếu nhỏ hơn "từ ngày". */
    public String getAddValidForToValue() {
        WebElement toInput = findWebElement(getByXpathDynamic(pickListLocator.getTxtAddValidForByIndex(), "2"));
        return getValueOfElement(toInput);
    }

    public PickListObjects clickAddNewSave() {
        blurActiveElement(); // commit Vue model của field cuối trước khi Save đọc form state
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

    /** Poll thay vì sleep mù; timeout thì bỏ qua để verify ở caller bắt được lỗi thật. */
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

    public PickListObjects clickRowDeleteIcon() {
        clickByJS(findWebElement(pickListLocator.getIcoRowDelete()), "Delete (row icon)");
        return this;
    }

    public boolean isConfirmDialogOpen() {
        return !getListWebElement(By.xpath(pickListLocator.getBtnConfirmYes())).isEmpty();
    }

    public PickListObjects clickConfirmYes() {
        clickByJS(findWebElement(pickListLocator.getBtnConfirmYes()), "Yes (confirm delete)");
        return this;
    }

    public PickListObjects clickConfirmNo() {
        clickByJS(findWebElement(pickListLocator.getBtnConfirmNo()), "No (cancel delete)");
        return this;
    }

    /** Xoá đúng 1 row còn lại sau khi search thu hẹp về record vừa tạo. */
    public PickListObjects deleteFirstRowResult() {
        clickRowDeleteIcon();
        clickConfirmYes();
        return this;
    }

    /** Checkbox chọn dòng - input thật ẩn nên click qua getListWebElement thay vì findWebElement (đòi visible). */
    public PickListObjects clickRowCheckbox(int rowIndex) {
        List<WebElement> matches = getListWebElement(getByXpathDynamic(pickListLocator.getChkRowByIndex(), String.valueOf(rowIndex)));
        clickByJS(matches.isEmpty() ? null : matches.get(0), "Checkbox row " + rowIndex);
        return this;
    }

    public PickListObjects clickSelectAllCheckbox() {
        List<WebElement> matches = getListWebElement(By.xpath(pickListLocator.getChkSelectAll()));
        clickByJS(matches.isEmpty() ? null : matches.get(0), "Select all checkbox");
        return this;
    }

    /** Đọc trạng thái qua wrapper div (data-p-checked) - input thật ẩn nên không đáng tin cậy để đọc. */
    public boolean isRowCheckboxChecked(int rowIndex) {
        WebElement wrapper = findWebElement(getByXpathDynamic(pickListLocator.getWrapperCheckboxByRowIndex(), String.valueOf(rowIndex)));
        return "true".equals(wrapper.getAttribute("data-p-checked"));
    }

    public PickListObjects clickDeleteToolbar() {
        clickByJS(findWebElement(pickListLocator.getBtnDeleteToolbar()), "Delete (toolbar)");
        return this;
    }

    /**
     * Dọn toast còn sót lại để lần đọc sau không nhầm message cũ thành kết quả mới.
     * Click close chỉ bắt đầu animation biến mất, nên phải xoá text ngay lập tức luôn.
     */
    public PickListObjects dismissAllToasts() {
        getJsExecutor().executeScript(
                "document.querySelectorAll('.p-toast-close-button').forEach(b => b.click());"
                        + "document.querySelectorAll('.p-toast-detail').forEach(el => el.textContent = '');");
        return this;
    }

    /** Đọc toast mới nhất; gọi dismissAllToasts() trước đó. Trả về "" nếu không có toast nào. */
    public String getLatestToastMessage() {
        try {
            return getWaitDriver(TOAST_WAIT_SECONDS).until(d -> {
                List<WebElement> details = getListWebElement(By.xpath(pickListLocator.getTxtToastDetail()));
                for (int i = details.size() - 1; i >= 0; i--) {
                    String text = getTextElement(details.get(i));
                    if (!text.isEmpty()) return text; // bỏ qua node rỗng của toast đang biến mất
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

    /** Panel mount trước khi data load xong, nên chờ Name có giá trị chứ không chỉ chờ panel xuất hiện. */
    public PickListObjects clickRowEditIcon() {
        clickByJS(findRowEditIcon(), "Edit (row icon)");
        waitForElementVisible(By.xpath(pickListLocator.getBtnEditCancel()));
        waitForInputValueNotEmpty(findWebElement(pickListLocator.getTxtEditName()));
        return this;
    }

    public boolean isEditPanelOpen() {
        return !getListWebElement(By.xpath(pickListLocator.getBtnEditCancel())).isEmpty();
    }

    public PickListObjects inputEditName(String value) {
        inputTextWithVerify(findWebElement(pickListLocator.getTxtEditName()), "Edit - Name", value);
        return this;
    }

    /** Xoá Name để trigger validation required-field; clear()+sendKeys("") không notify Vue. */
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

    /** Input switch thật của PrimeVue cố tình vô hình, nên tìm bằng presence chứ không phải visibility. */
    public PickListObjects toggleEditIsActive() {
        List<WebElement> matches = getListWebElement(By.xpath(pickListLocator.getSwtEditIsActive()));
        clickByJS(matches.isEmpty() ? null : matches.get(0), "Is Active toggle");
        return this;
    }

    /** Gõ vào Code để chứng minh field vẫn cho sửa (PL_FUNC-33) - caller save rồi verify lại từ grid. */
    public PickListObjects attemptInputEditCode(String value) {
        inputText(findWebElement(pickListLocator.getTxtEditCode()), "Edit - Code (attempt)", value);
        return this;
    }

    public PickListObjects clickEditSave() {
        blurActiveElement(); // commit Vue model của field cuối trước khi Save đọc form state
        clickByJS(findWebElement(pickListLocator.getBtnEditSave()), "Save (Edit)");
        return this;
    }

    public PickListObjects clickEditCancel() {
        clickByJS(findWebElement(pickListLocator.getBtnEditCancel()), "Cancel (Edit)");
        waitForEditPanelClosed();
        return this;
    }

    /** Poll thay vì sleep mù; timeout thì bỏ qua để verify ở caller bắt được lỗi thật. */
    private void waitForEditPanelClosed() {
        try {
            getWaitDriver().until(d -> !isEditPanelOpen());
        } catch (Exception ignored) {
        }
    }

    /** Anchor text nên là một đoạn message như "required", không phải label field (dễ match sai). */
    public String getEditErrorMessageContaining(String anchorText) {
        List<WebElement> errors = getListWebElement(getByXpathDynamic(pickListLocator.getErrEditMessageContaining(), anchorText));
        return errors.isEmpty() ? "" : getTextElement(errors.get(0));
    }

    public PickListObjects selectAddDataType(String value) {
        selectDropdownOption(findWebElement(pickListLocator.getCboAddDataType()), value);
        return this;
    }

    /**
     * ChromeDriver isDisplayed() báo sai (invisible) cho combobox này ngay sau khi chuyển tab -
     * cùng quirk đã gặp ở CharacteristicCatalog (xem project-ktnn-epc-ui-findings), dùng presence thay vì visible.
     */
    public PickListObjects selectEditDataType(String value) {
        selectDropdownOption(waitForElementPresent(By.xpath(pickListLocator.getCboEditDataType())), value);
        return this;
    }

    public boolean isEditDataTypeDisabled() {
        return "true".equals(getAttributeOfElement(waitForElementPresent(By.xpath(pickListLocator.getCboEditDataType())), "aria-disabled"));
    }

    public String getEditDataTypeValue() {
        return getTextElement(waitForElementPresent(By.xpath(pickListLocator.getCboEditDataType())));
    }

    public PickListObjects clickEditGeneralTab() {
        clickByJS(findWebElement(pickListLocator.getTabEditGeneral()), "General tab");
        return this;
    }

    public PickListObjects clickEditPickListItemTab() {
        clickByJS(findWebElement(pickListLocator.getTabEditPickListItem()), "PickList Item tab");
        return this;
    }

    /**
     * clickByJS (synthetic event) không kích hoạt được toggle này - phải click thật qua Selenium binding chuẩn.
     * Toast còn sót lại từ action trước (Save/Delete) có thể che đúng vị trí nút, click thật bị chặn
     * (ElementClickIntercepted) nên phải dismiss hết toast trước.
     */
    public PickListObjects clickPanelToggle() {
        dismissAllToasts();
        clickTo(findWebElement(pickListLocator.getBtnPanelToggle()), "Panel minimize/expand toggle");
        return this;
    }

    /** Panel thu nhỏ vẫn giữ 2 icon tab, chỉ ẩn field - dùng field Name (chỉ có ở tab General, panel mở rộng) để biết trạng thái. */
    public boolean isEditPanelExpanded() {
        return !getListWebElement(By.xpath(pickListLocator.getTxtEditName())).isEmpty();
    }

    /** App có bug DOM trùng id "page-picklist-item" - luôn tìm nút [+] trong pane đang thật sự hiển thị. */
    public PickListObjects clickAddItem() {
        clickByJS(findWebElement(pickListLocator.getBtnAddItem()), "Add (+) PickList Item");
        return this;
    }

    public PickListObjects inputItemNameLabel(String value) {
        inputText(findWebElement(pickListLocator.getTxtItemNameLabel()), "Item - Name/Label", value);
        return this;
    }

    public PickListObjects inputItemCode(String value) {
        inputText(findWebElement(pickListLocator.getTxtItemCode()), "Item - Code", value);
        return this;
    }

    public PickListObjects inputItemValue(String value) {
        inputText(findWebElement(pickListLocator.getTxtItemValue()), "Item - Value", value);
        return this;
    }

    public PickListObjects clickItemConfirm() {
        clickByJS(findWebElement(pickListLocator.getBtnItemConfirm()), "Confirm (✓) item");
        return this;
    }

    /** Nút [+] Add item chỉ hiện khi tab PickList Item đang active - dùng làm tín hiệu xác nhận đã chuyển tab. */
    public boolean isPickListItemTabActive() {
        return !getListWebElement(By.xpath(pickListLocator.getBtnAddItem())).isEmpty();
    }
}

