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

    // column order: checkbox, Name, Code, Description, Actions
    String cellName = "//table//tbody//tr/td[2]";
    String cellCode = "//table//tbody//tr/td[3]";
    String cellDescription = "//table//tbody//tr/td[4]";

    // paginator's "Show x in y results" line
    String lblResultsCount = "//div[contains(.,'results')][not(.//div[contains(.,'results')])]";

    // icon in search box, toggles Order/Sort/Filter panel (hidden by default)
    String icoFilterToggle = "CSS|.btn-filter";

    // 3rd addon button next to search (pi-plus=Add new, fa-trash=Delete, pi-undo=Refresh)
    String btnRefresh = "(//div[contains(@class,'p-inputgroupaddon')]//button)[3]";

    // dropdown id changes on every mount - locate via label text instead
    String cboOrderTrigger = "//span[normalize-space()='Order']/following-sibling::div[contains(@class,'select-custom')]//span[@role='combobox']";
    String cboSortTrigger = "//span[normalize-space()='Sort']/following-sibling::div[contains(@class,'select-custom')]//span[@role='combobox']";

    // dropdown option popup - only 1 dropdown open at a time in our flows
    String optionByText = "//li[@role='option'][normalize-space()='%s']";

    String btnAddAction = "CSS|.btn-add";

    // applies both Order/Sort and filter conditions
    String btnApplyFilter = "CSS|.btn-search";

    String icoAndOrToggle = "//div[contains(@class,'search-list-conditions-isroot')]//span[contains(@class,'fa-caret-down')]";

    // filter condition row, 1-based index via %s
    String cboFilterFieldByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//div[contains(@class,'tree-node')]/div[1]//span[@role='combobox']";
    String cboFilterOperatorByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//div[contains(@class,'operators')]//span[@role='combobox']";
    String txtFilterValueByRow = "(//div[contains(@class,'search-list-conditions-node')])[%s]//input[@type='text']";

    // 1st addon button next to search (pi-plus=Add new)
    String btnAddNew = "(//div[contains(@class,'p-inputgroupaddon')]//button)[1]";

    // also doubles as a blur target to close the Valid For calendar popup
    String lblAddNewTitle = "//span[contains(@class,'p-dialog-title')][normalize-space()='Add new']";
    String txtAddName = "//input[@placeholder='Enter name']";
    String txtAddCode = "//input[@placeholder='Enter Code']";
    String txtAddVersion = "//input[@placeholder='Enter version']";
    String txtAddDescription = "//textarea[@placeholder='Enter description']";
    // Valid For has 2 date inputs sharing a placeholder, %s picks 1=from, 2=to
    String txtAddValidForByIndex = "(//input[@placeholder='dd/mm/yyyy'])[%s]";
    String btnAddNewSave = "CSS|.btn-save";
    String btnAddNewClose = "//button[@aria-label='Close']";
    String icoAddNewCloseX = "//div[contains(@class,'p-dialog')][.//span[contains(@class,'p-dialog-title')][normalize-space()='Add new']]//button[contains(@class,'p-dialog-close-button')]";

    // required-field inline error message, located relative to its field's label text (e.g. "Name")
    String errMsgByFieldLabel = "//label[.//span[starts-with(normalize-space(.),'%s')]]/parent::div/following-sibling::div[contains(@class,'error')][1]//small";

    // PrimeVue toast detail text (success/error variants share this class)
    String txtToastDetail = "//div[contains(@class,'p-toast-detail')]";

    // 1st row's delete icon (pi-trash) - used after search narrows to exactly 1 record
    String icoRowDelete = "(//table//tbody//tr)[1]//span[contains(@class,'pi-trash')]";
    // Yes/No confirm buttons have no aria-label, only plain visible text
    String btnConfirmYes = "//button[normalize-space()='Yes']";

    // 1st row's edit icon (pi-pen-to-square) - opens the Edit side panel for that record
    String icoRowEdit = "(//table//tbody//tr)[1]//span[contains(@class,'pi-pen-to-square')]";

    // Edit panel is a separate component from Add new (own labels/buttons), can't reuse locators.
    // Code stays editable here, contradicts spec - see PL_FUNC-33.
    // [last()]: reopening the panel can briefly leave a stale element matching too, and the
    // visibility wait re-fetches the first match, so pick the last to avoid the stale one.
    String txtEditName = "(//input[@placeholder='Enter name'])[last()]";
    String txtEditCode = "(//input[@placeholder='Enter code'])[last()]";
    String txtEditVersion = "(//input[@placeholder='Enter version'])[last()]";
    String txtEditDescription = "(//textarea[@placeholder='Enter description'])[last()]";
    // scoped to the panel - the same toggle-switch component is also used for the EN/VI nav switch
    String swtEditIsActive = "(//div[contains(@class,'tab-content-parent')]//input[@type='checkbox'][@role='switch'])[last()]";
    String btnEditSave = "(//button[@aria-label='Save'])[last()]";
    String btnEditCancel = "(//button[@aria-label='Cancel'])[last()]";

    // Edit panel's inline validation message - a plain span, unlike Add new's ancestor-based block
    String errEditMessageContaining = "//span[contains(@class,'text-red-500')][contains(.,'%s')]";
}
