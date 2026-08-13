package com.ktnn.projects.pages.locator;

import lombok.Getter;

@Getter
public class CharacteristicCatalogLocator extends BaseLocator {
    @Getter
    public static CharacteristicCatalogLocator instance = new CharacteristicCatalogLocator();

    private CharacteristicCatalogLocator() {
    }

    String txtSearch = "//input[@type='search' or contains(@placeholder,'Search') or contains(@placeholder,'Tìm kiếm')]";
    // nút addon thứ 1 cạnh search (pi-plus=Add new) - cùng vị trí index như PickList
    String btnAddNew = "(//div[contains(@class,'p-inputgroupaddon')]//button)[1]";
    // icon edit/xoá của dòng đầu tiên - dùng sau khi search thu hẹp còn đúng 1 record
    String icoRowEdit = "(//table//tbody//tr)[1]//span[contains(@class,'pi-pen-to-square')]";
    String icoRowDelete = "(//table//tbody//tr)[1]//span[contains(@class,'pi-trash')]";
    String btnConfirmYes = "//button[normalize-space()='Yes']";

    // dialog Add new cấp Catalog - field có placeholder giống PickList
    String txtAddCatalogName = "//input[@placeholder='Enter name']";
    String txtAddCatalogCode = "//input[@placeholder='Enter Code']";
    // scope trong role=dialog - side panel Edit (mở song song phía sau dialog) cũng có nút Save riêng, không scope sẽ bắt nhầm
    String btnAddNewSave = "//div[@role='dialog']//button[normalize-space()='Save']";

    // tab thứ 2 trong side panel Edit = "Characteristic Specification" (tab thứ 1 là General, không có tên cố định qua id)
    String tabCharacteristicSpecification = "(//div[@role='tablist']//*[@role='tab'])[2]";
    // nút [+] Add trong tab Characteristic Specification - .p-button-contrast không đủ đặc hiệu (trùng nút khác),
    // phải giữ đủ tổ hợp class mới lọc đúng đúng 1 nút
    String btnAddSpecification = "CSS|.p-button.p-component.p-button-icon-only.p-button-contrast";
    // thẻ 1 item Characteristic Specification trong danh sách - đi ngược từ tên item lên card cha rồi tìm icon xoá
    String icoSpecificationDeleteByName = "//span[normalize-space()='%s']/ancestor::div[contains(@class,'general-card')][1]//span[contains(@class,'fa-trash')]";

    // dialog Add new cấp Characteristic Specification - field không có placeholder, định vị qua attribute name.
    // Phải scope trong role=dialog vì side panel Edit Catalog mở song song phía sau cũng có input name/code trùng attribute.
    String txtAddCsName = "//div[@role='dialog']//input[@name='name']";
    String txtAddCsCode = "//div[@role='dialog']//input[@name='code']";
    // dropdown không có placeholder/name ổn định - định vị theo label đứng trước rồi lấy combobox gần nhất phía sau
    String cboValueType = "//*[normalize-space()='Value Type *']/following::span[@role='combobox'][1]";
    String cboPickList = "//*[normalize-space()='PickList *']/following::span[@role='combobox'][1]";
    // popup option dùng chung cho mọi dropdown dạng PrimeVue Select trong trang này
    String optionByText = "//li[@role='option'][normalize-space()='%s']";
    // dropdown PickList có ô Search riêng - PrimeVue render overlay cuối DOM nên lấy input Search cuối cùng đang hiện
    String txtPickListDropdownSearch = "(//input[@placeholder='Search'])[last()]";
    String optionPickListStartsWith = "(//li[@role='option'][starts-with(normalize-space(.),'%s')])[last()]";

    // tab Layout của dialog Add new Characteristic Specification - required field mới hiện ra sau khi chọn tab này
    String tabLayout = "//*[@role='tab'][normalize-space()='Layout']";
    String txtCsTitle = "//div[@role='dialog']//input[@name='title']";
    String cboDataType = "//*[normalize-space()='Data Type *']/following::span[@role='combobox'][1]";
    String cboControlType = "//*[normalize-space()='Control Type *']/following::span[@role='combobox'][1]";
}
