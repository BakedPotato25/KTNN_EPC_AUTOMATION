package com.ktnn.projects.pages.pages;

import com.ktnn.projects.common.BasePage;
import com.ktnn.projects.pages.objects.CharacteristicCatalogObjects;
import org.openqa.selenium.support.PageFactory;

/**
 * Chỉ đủ dùng để dựng tiền điều kiện/dọn dẹp cho PL_FUNC-39 (PickList đang được
 * Characteristic Specification tham chiếu) - không phải automation đầy đủ cho module này.
 */
public class CharacteristicCatalogPage extends BasePage {
    private final CharacteristicCatalogObjects objects;

    public CharacteristicCatalogPage() {
        super();
        PageFactory.initElements(webDriver, this);
        objects = CharacteristicCatalogObjects.getInstance();
    }

    public CharacteristicCatalogPage searchByKeyword(String keyword) {
        objects.searchByKeyword(keyword);
        return this;
    }

    public CharacteristicCatalogPage createCatalog(String name, String code) {
        objects.clickAddNew().inputCatalogName(name).inputCatalogCode(code).clickDialogSave();
        return this;
    }

    /** Tạo Characteristic Specification mới trong Catalog đang mở, Value Type = PickList, tham chiếu pickListName. */
    public CharacteristicCatalogPage addSpecificationLinkedToPickList(String csName, String csCode, String pickListName) {
        objects
                .clickCharacteristicSpecificationTab()
                .clickAddSpecification()
                .inputCsName(csName)
                .inputCsCode(csCode)
                .selectValueType("PickList")
                .selectPickList(pickListName)
                .clickLayoutTab()
                .inputCsTitle(csName)
                .selectDataType("Object")
                .selectControlType("Combobox")
                .clickDialogSave();
        return this;
    }

    public CharacteristicCatalogPage deleteSpecificationByName(String name) {
        objects.clickDeleteSpecificationByName(name).clickConfirmYes();
        return this;
    }

    public CharacteristicCatalogPage deleteCatalogByExactSearch(String name) {
        objects.searchByKeyword(name).clickRowDeleteIcon().clickConfirmYes();
        return this;
    }

    /**
     * Xoá catalog + specification còn sót từ lần chạy fail trước, tránh lỗi trùng Code khi setup lại.
     * Catalog không xoá được nếu còn Specification con nên phải mở panel xoá Specification trước.
     */
    public CharacteristicCatalogPage removeLeftoverCatalog(String catalogName, String csName) {
        objects.searchByKeyword(catalogName);
        if (!objects.hasDeletableRow()) return this;
        objects.clickRowEditIcon().clickCharacteristicSpecificationTab();
        try {
            deleteSpecificationByName(csName);
        } catch (Exception ignored) {
        }
        deleteCatalogByExactSearch(catalogName);
        return this;
    }
}
