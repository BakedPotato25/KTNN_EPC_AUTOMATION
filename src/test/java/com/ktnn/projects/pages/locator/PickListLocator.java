package com.ktnn.projects.pages.locator;

import lombok.Getter;

@Getter
public class PickListLocator extends BaseLocator {
    @Getter
    public static PickListLocator instance = new PickListLocator();

    private PickListLocator() {
    }

    String txtSearch = "//input[@type='search' or contains(@placeholder,'Search') or contains(@placeholder,'Tìm kiếm')]";
    String rowGrid = "//table//tbody//tr";

    // thứ tự cột: checkbox, Name, Code, Description, Actions
    String cellName = "//table//tbody//tr/td[2]";
    String cellCode = "//table//tbody//tr/td[3]";
    String cellDescription = "//table//tbody//tr/td[4]";

    // dòng "Show x in y results" của paginator
    String lblResultsCount = "//div[contains(.,'results')][not(.//div[contains(.,'results')])]";

    // icon trong search box, bật/tắt panel Order/Sort/Filter (mặc định ẩn)
    String icoFilterToggle = "CSS|.btn-filter";

    // nút addon thứ 3 cạnh search (pi-plus=Add new, fa-trash=Delete, pi-undo=Refresh)
    String btnRefresh = "(//div[contains(@class,'p-inputgroupaddon')]//button)[3]";

    // id dropdown đổi mỗi lần mount - định vị qua label text thay vì id
    String cboOrderTrigger = "//span[normalize-space()='Order']/following-sibling::div[contains(@class,'select-custom')]//span[@role='combobox']";
    String cboSortTrigger = "//span[normalize-space()='Sort']/following-sibling::div[contains(@class,'select-custom')]//span[@role='combobox']";

    // popup option của dropdown - trong các flow của mình chỉ mở 1 dropdown tại 1 thời điểm
    String optionByText = "//li[@role='option'][normalize-space()='%s']";

    String btnAddAction = "CSS|.btn-add";

    // áp dụng cả điều kiện Order/Sort và filter
    String btnApplyFilter = "CSS|.btn-search";

    String icoAndOrToggle = "//div[contains(@class,'search-list-conditions-isroot')]//span[contains(@class,'fa-caret-down')]";

    // dòng điều kiện filter, index bắt đầu từ 1 qua %s
    String cboFilterFieldByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//div[contains(@class,'tree-node')]/div[1]//span[@role='combobox']";
    String cboFilterOperatorByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//div[contains(@class,'operators')]//span[@role='combobox']";
    String txtFilterValueByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//input[@type='text']";

    // nút addon thứ 1 cạnh search (pi-plus=Add new)
    String btnAddNew = "(//div[contains(@class,'p-inputgroupaddon')]//button)[1]";

    // cũng dùng làm điểm blur để đóng popup calendar của Valid For
    String lblAddNewTitle = "//span[contains(@class,'p-dialog-title')][normalize-space()='Add new']";
    String txtAddName = "//input[@placeholder='Enter name']";
    String txtAddCode = "//input[@placeholder='Enter Code']";
    String txtAddVersion = "//input[@placeholder='Enter version']";
    String txtAddDescription = "//textarea[@placeholder='Enter description']";
    // Valid For có 2 input ngày dùng chung placeholder, %s chọn 1=from, 2=to
    String txtAddValidForByIndex = "(//input[@placeholder='dd/mm/yyyy'])[%s]";
    String btnAddNewSave = "CSS|.btn-save";
    String btnAddNewClose = "//button[@aria-label='Close']";
    String icoAddNewCloseX = "//div[contains(@class,'p-dialog')][.//span[contains(@class,'p-dialog-title')][normalize-space()='Add new']]//button[contains(@class,'p-dialog-close-button')]";

    // thông báo lỗi inline của required field, định vị tương đối theo label text của field (vd "Name")
    String errMsgByFieldLabel = "//label[.//span[starts-with(normalize-space(.),'%s')]]/parent::div/following-sibling::div[contains(@class,'error')][1]//small";

    // text chi tiết toast của PrimeVue (biến thể success/error dùng chung class này)
    String txtToastDetail = "//div[contains(@class,'p-toast-detail')]";

    // icon xoá (pi-trash) của dòng đầu tiên - dùng sau khi search thu hẹp còn đúng 1 record
    String icoRowDelete = "(//table//tbody//tr)[1]//span[contains(@class,'pi-trash')]";
    // nút confirm Yes/No không có aria-label, chỉ có text hiển thị thường
    String btnConfirmYes = "//button[normalize-space()='Yes']";
    String btnConfirmNo = "//button[normalize-space()='No']";

    // checkbox chọn dòng: input thật ẩn (opacity 0) giống switch Is Active - click bằng JS trực tiếp vào input,
    // đọc trạng thái checked qua wrapper div.p-checkbox (data-p-checked) vì input ẩn
    String chkRowByIndex = "(//table//tbody//tr)[%s]//input[@type='checkbox']";
    String wrapperCheckboxByRowIndex = "(//table//tbody//tr)[%s]//div[contains(@class,'p-checkbox')]";
    String chkSelectAll = "//table//thead//input[@type='checkbox']";
    // nút addon thứ 2 cạnh search (fa-trash) - xoá (các) bản ghi đã tick checkbox, khác icoRowDelete (icon riêng từng dòng)
    String btnDeleteToolbar = "(//div[contains(@class,'p-inputgroupaddon')]//button)[2]";

    // icon edit (pi-pen-to-square) của dòng đầu tiên - mở Edit side panel cho record đó
    String icoRowEdit = "(//table//tbody//tr)[1]//span[contains(@class,'pi-pen-to-square')]";

    // Edit panel là component riêng so với Add new (label/button riêng), không tái dùng locator được.
    // Code vẫn có thể sửa ở đây, trái với spec - xem PL_FUNC-33.
    // [last()]: mở lại panel có thể để sót element cũ (stale) cũng khớp match trong thời gian ngắn,
    // mà wait visibility lại lấy match đầu tiên, nên chọn last() để tránh element stale đó.
    String txtEditName = "(//input[@placeholder='Enter name'])[last()]";
    String txtEditCode = "(//input[@placeholder='Enter code'])[last()]";
    String txtEditVersion = "(//input[@placeholder='Enter version'])[last()]";
    String txtEditDescription = "(//textarea[@placeholder='Enter description'])[last()]";
    // giới hạn trong phạm vi panel - cùng component toggle-switch này cũng dùng cho nút chuyển EN/VI ở nav
    String swtEditIsActive = "(//div[contains(@class,'tab-content-parent')]//input[@type='checkbox'][@role='switch'])[last()]";
    String btnEditSave = "(//button[@aria-label='Save'])[last()]";
    String btnEditCancel = "(//button[@aria-label='Cancel'])[last()]";

    // thông báo validation inline của Edit panel - là span thường, khác với block dựa trên ancestor của Add new
    String errEditMessageContaining = "//span[contains(@class,'text-red-500')][contains(.,'%s')]";

    // 2 icon tab của Edit side panel, định vị qua attribute title (không phải role=tab)
    String tabEditGeneral = "//div[@title='General']";
    String tabEditPickListItem = "//div[@title='PickList Item']";
    // nút thu nhỏ/mở rộng side panel - link icon lưới, con trực tiếp của .head, TOGGLE được cả 2 chiều
    // (không dùng div.actions chứa icon chevron: div đó bị Vue tháo bỏ hoàn toàn khỏi DOM khi panel đã thu nhỏ,
    // không bấm lại được để mở rộng - link này ổn định, luôn tồn tại ở cả 2 trạng thái)
    String btnPanelToggle = "//div[contains(@class,'head')]/a";

    // nút [+] Add item trong tab PickList Item - app có bug DOM trùng id "page-picklist-item" (1 bản ẩn,
    // 1 bản active), phải scope theo pane KHÔNG display:none để không bắt trúng bản ẩn/stale.
    // Cũng dùng làm tín hiệu xác nhận đã chuyển sang tab PickList Item (nút chỉ hiện khi tab active).
    String btnAddItem = "//div[@id='page-picklist-item'][not(contains(@style,'display: none'))]//div[contains(@class,'icon-action')]";
}
