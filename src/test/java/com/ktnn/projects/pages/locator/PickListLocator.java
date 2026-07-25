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
}
