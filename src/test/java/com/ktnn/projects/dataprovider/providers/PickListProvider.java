package com.ktnn.projects.dataprovider.providers;

import com.ktnn.datadriven.BaseProvider;
import com.ktnn.projects.dataprovider.DataPath;
import com.ktnn.projects.dataprovider.model.PickListAddItemModel;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListDataTypeChangeModel;
import com.ktnn.projects.dataprovider.model.PickListDataTypeLockedModel;
import com.ktnn.projects.dataprovider.model.PickListDeleteInUseModel;
import com.ktnn.projects.dataprovider.model.PickListDataTypeComparisonModel;
import com.ktnn.projects.dataprovider.model.PickListDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListEditItemModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.dataprovider.model.PickListItemValueModel;
import com.ktnn.projects.dataprovider.model.PickListFilterModel;
import com.ktnn.projects.dataprovider.model.PickListItemOrderModel;
import com.ktnn.projects.dataprovider.model.PickListMultiDeleteInUseModel;
import com.ktnn.projects.dataprovider.model.PickListMultiDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListMultiFieldSearchModel;
import com.ktnn.projects.dataprovider.model.PickListOverallSaveModel;
import com.ktnn.projects.dataprovider.model.PickListSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSortModel;
import com.ktnn.projects.dataprovider.model.PickListTwoConditionFilterModel;
import com.ktnn.utils.configloader.JsonUtils;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;

public class PickListProvider extends BaseProvider {
    JsonUtils jsonUtils = JsonUtils.getInstance();

    @DataProvider(name = "KTNN_PickListSearch_001_Relative")
    public Object[][] KTNN_PickListSearch_001_Relative(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_002_Exact")
    public Object[][] KTNN_PickListSearch_002_Exact(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_003_NotFound")
    public Object[][] KTNN_PickListSearch_003_NotFound(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_004_TrimWhitespace")
    public Object[][] KTNN_PickListSearch_004_TrimWhitespace(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_005_CodeRelative")
    public Object[][] KTNN_PickListSearch_005_CodeRelative(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_006_CodeExact")
    public Object[][] KTNN_PickListSearch_006_CodeExact(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_007_CodeNotFound")
    public Object[][] KTNN_PickListSearch_007_CodeNotFound(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSearch_008_MultiField")
    public Object[][] KTNN_PickListSearch_008_MultiField(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListMultiFieldSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_001_CodeAscending")
    public Object[][] KTNN_PickListSort_001_CodeAscending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_002_CodeDescending")
    public Object[][] KTNN_PickListSort_002_CodeDescending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_003_NameAscending")
    public Object[][] KTNN_PickListSort_003_NameAscending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_004_NameDescending")
    public Object[][] KTNN_PickListSort_004_NameDescending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_005_CreateDateDescending")
    public Object[][] KTNN_PickListSort_005_CreateDateDescending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListSort_006_CreateDateAscending")
    public Object[][] KTNN_PickListSort_006_CreateDateAscending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSortModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListFilter_001_DescriptionLike")
    public Object[][] KTNN_PickListFilter_001_DescriptionLike(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListFilterModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListFilter_002_DescriptionExact")
    public Object[][] KTNN_PickListFilter_002_DescriptionExact(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListFilterModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListFilter_003_VersionExact")
    public Object[][] KTNN_PickListFilter_003_VersionExact(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListFilterModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListFilter_004_CombineWithAnd")
    public Object[][] KTNN_PickListFilter_004_CombineWithAnd(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListTwoConditionFilterModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListFilter_005_CombineWithOr")
    public Object[][] KTNN_PickListFilter_005_CombineWithOr(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListTwoConditionFilterModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListRefresh_001_ResetSearch")
    public Object[][] KTNN_PickListRefresh_001_ResetSearch(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListSearchModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_001_Valid")
    public Object[][] KTNN_PickListAddNew_001_Valid(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_002_GridUpdates")
    public Object[][] KTNN_PickListAddNew_002_GridUpdates(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_003_RequiredFieldsOnly")
    public Object[][] KTNN_PickListAddNew_003_RequiredFieldsOnly(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_004_EmptyName")
    public Object[][] KTNN_PickListAddNew_004_EmptyName(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_005_EmptyCode")
    public Object[][] KTNN_PickListAddNew_005_EmptyCode(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_006_WhitespaceName")
    public Object[][] KTNN_PickListAddNew_006_WhitespaceName(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_007_DuplicateCode")
    public Object[][] KTNN_PickListAddNew_007_DuplicateCode(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_008_CloseDiscardsData")
    public Object[][] KTNN_PickListAddNew_008_CloseDiscardsData(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_009_XDiscardsData")
    public Object[][] KTNN_PickListAddNew_009_XDiscardsData(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_010_ValidForFromAfterTo")
    public Object[][] KTNN_PickListAddNew_010_ValidForFromAfterTo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_011_ValidForFromEqualsTo")
    public Object[][] KTNN_PickListAddNew_011_ValidForFromEqualsTo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddNew_012_ValidForFromBeforeTo")
    public Object[][] KTNN_PickListAddNew_012_ValidForFromBeforeTo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_001_Valid")
    public Object[][] KTNN_PickListEdit_001_Valid(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_002_GridUpdates")
    public Object[][] KTNN_PickListEdit_002_GridUpdates(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_003_EmptyName")
    public Object[][] KTNN_PickListEdit_003_EmptyName(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_004_CodeEditable")
    public Object[][] KTNN_PickListEdit_004_CodeEditable(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_005_CancelDiscardsChanges")
    public Object[][] KTNN_PickListEdit_005_CancelDiscardsChanges(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_006_DataTypeLockedWithItem")
    public Object[][] KTNN_PickListEdit_006_DataTypeLockedWithItem(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_007_DataTypeChangeableWithoutItem")
    public Object[][] KTNN_PickListEdit_007_DataTypeChangeableWithoutItem(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeChangeModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_008_SidePanelGeneralOpens")
    public Object[][] KTNN_PickListEdit_008_SidePanelGeneralOpens(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_009_SidePanelSwitchToItemTab")
    public Object[][] KTNN_PickListEdit_009_SidePanelSwitchToItemTab(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListEdit_010_SidePanelMinimizeExpand")
    public Object[][] KTNN_PickListEdit_010_SidePanelMinimizeExpand(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDelete_001_ConfirmDialogShown")
    public Object[][] KTNN_PickListDelete_001_ConfirmDialogShown(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDelete_002_ConfirmYes")
    public Object[][] KTNN_PickListDelete_002_ConfirmYes(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDelete_003_ViaToolbarCheckbox")
    public Object[][] KTNN_PickListDelete_003_ViaToolbarCheckbox(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDelete_004_ConfirmNo")
    public Object[][] KTNN_PickListDelete_004_ConfirmNo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDelete_005_BlockedWhenInUse")
    public Object[][] KTNN_PickListDelete_005_BlockedWhenInUse(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDeleteInUseModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListMultiDelete_001_SelectMultipleAndDelete")
    public Object[][] KTNN_PickListMultiDelete_001_SelectMultipleAndDelete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListMultiDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListMultiDelete_002_SelectAllCheckbox")
    public Object[][] KTNN_PickListMultiDelete_002_SelectAllCheckbox(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListMultiDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListMultiDelete_003_CancelDelete")
    public Object[][] KTNN_PickListMultiDelete_003_CancelDelete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListMultiDeleteModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListMultiDelete_004_BlockedWhenOneInUse")
    public Object[][] KTNN_PickListMultiDelete_004_BlockedWhenOneInUse(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListMultiDeleteInUseModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_001_FormDisplaysInline")
    public Object[][] KTNN_PickListAddItem_001_FormDisplaysInline(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_002_FormFieldsComplete")
    public Object[][] KTNN_PickListAddItem_002_FormFieldsComplete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_003_IsDefaultOnByDefault")
    public Object[][] KTNN_PickListAddItem_003_IsDefaultOnByDefault(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_004_ValidAllFields")
    public Object[][] KTNN_PickListAddItem_004_ValidAllFields(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_005_RequiredFieldsOnly")
    public Object[][] KTNN_PickListAddItem_005_RequiredFieldsOnly(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_006_EmptyNameLabel")
    public Object[][] KTNN_PickListAddItem_006_EmptyNameLabel(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_007_EmptyCode")
    public Object[][] KTNN_PickListAddItem_007_EmptyCode(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_008_EmptyValue")
    public Object[][] KTNN_PickListAddItem_008_EmptyValue(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListAddItem_009_CancelDiscardsItem")
    public Object[][] KTNN_PickListAddItem_009_CancelDiscardsItem(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemUI_001_ControlsComplete")
    public Object[][] KTNN_PickListItemUI_001_ControlsComplete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemUI_002_RowFieldsComplete")
    public Object[][] KTNN_PickListItemUI_002_RowFieldsComplete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemUI_003_SearchControlsComplete")
    public Object[][] KTNN_PickListItemUI_003_SearchControlsComplete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemUI_004_OrderAscending")
    public Object[][] KTNN_PickListItemUI_004_OrderAscending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemOrderModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemUI_005_OrderDescending")
    public Object[][] KTNN_PickListItemUI_005_OrderDescending(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemOrderModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_001_ShowOptionsComplete")
    public Object[][] KTNN_PickListItemPaging_001_ShowOptionsComplete(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_002_Show5Default")
    public Object[][] KTNN_PickListItemPaging_002_Show5Default(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_003_Show10")
    public Object[][] KTNN_PickListItemPaging_003_Show10(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_004_Show25")
    public Object[][] KTNN_PickListItemPaging_004_Show25(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_005_Show50")
    public Object[][] KTNN_PickListItemPaging_005_Show50(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_006_Show100")
    public Object[][] KTNN_PickListItemPaging_006_Show100(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_007_NextPage")
    public Object[][] KTNN_PickListItemPaging_007_NextPage(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_008_PrevPage")
    public Object[][] KTNN_PickListItemPaging_008_PrevPage(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_009_FirstLastPage")
    public Object[][] KTNN_PickListItemPaging_009_FirstLastPage(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemPaging_010_ItemsRangeText")
    public Object[][] KTNN_PickListItemPaging_010_ItemsRangeText(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemView_001_HoverRevealsIcons")
    public Object[][] KTNN_PickListItemView_001_HoverRevealsIcons(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemView_002_EyeOpensSubSection")
    public Object[][] KTNN_PickListItemView_002_EyeOpensSubSection(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemView_003_DataLoadedCorrectly")
    public Object[][] KTNN_PickListItemView_003_DataLoadedCorrectly(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemEdit_001_ValidSaveViaConfirm")
    public Object[][] KTNN_PickListItemEdit_001_ValidSaveViaConfirm(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemEdit_002_EmptyNameLabel")
    public Object[][] KTNN_PickListItemEdit_002_EmptyNameLabel(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemEdit_003_CancelDiscardsChanges")
    public Object[][] KTNN_PickListItemEdit_003_CancelDiscardsChanges(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListEditItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemDelete_001_NoConfirmDialog")
    public Object[][] KTNN_PickListItemDelete_001_NoConfirmDialog(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemDelete_002_NoUndoButton")
    public Object[][] KTNN_PickListItemDelete_002_NoUndoButton(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemSearch_001_ByNameLabel")
    public Object[][] KTNN_PickListItemSearch_001_ByNameLabel(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemSearch_002_NotFound")
    public Object[][] KTNN_PickListItemSearch_002_NotFound(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemIsDefault_001_MultipleAllowed")
    public Object[][] KTNN_PickListItemIsDefault_001_MultipleAllowed(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemOrderModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemIsDefault_002_ToggleOff")
    public Object[][] KTNN_PickListItemIsDefault_002_ToggleOff(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemIsDefault_003_MultiplePersistAfterSave")
    public Object[][] KTNN_PickListItemIsDefault_003_MultiplePersistAfterSave(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemOrderModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListPanel_001_SavePersistsChanges")
    public Object[][] KTNN_PickListPanel_001_SavePersistsChanges(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListOverallSaveModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListPanel_002_CancelDiscardsChanges")
    public Object[][] KTNN_PickListPanel_002_CancelDiscardsChanges(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListOverallSaveModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeForm_001_String")
    public Object[][] KTNN_PickListDataTypeForm_001_String(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeValue_001_StringFreeText")
    public Object[][] KTNN_PickListDataTypeValue_001_StringFreeText(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeForm_002_Number")
    public Object[][] KTNN_PickListDataTypeForm_002_Number(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeValue_002_NumberFilters")
    public Object[][] KTNN_PickListDataTypeValue_002_NumberFilters(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemValueModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeValue_003_NumberUnitOptional")
    public Object[][] KTNN_PickListDataTypeValue_003_NumberUnitOptional(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeLockedModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeForm_004_Object")
    public Object[][] KTNN_PickListDataTypeForm_004_Object(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddNewModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeValue_004_ObjectMultilineJson")
    public Object[][] KTNN_PickListDataTypeValue_004_ObjectMultilineJson(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemValueModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeValue_005_ObjectInvalidJsonBlocked")
    public Object[][] KTNN_PickListDataTypeValue_005_ObjectInvalidJsonBlocked(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListItemValueModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListDataTypeForm_003_CompareAllTypes")
    public Object[][] KTNN_PickListDataTypeForm_003_CompareAllTypes(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListDataTypeComparisonModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemValidFor_001_FromAfterTo")
    public Object[][] KTNN_PickListItemValidFor_001_FromAfterTo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    @DataProvider(name = "KTNN_PickListItemValidFor_002_FromEqualsTo")
    public Object[][] KTNN_PickListItemValidFor_002_FromEqualsTo(Method method) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, method.getName());
        return updateDataModel(new PickListAddItemModel(), dataList);
    }

    /**
     * Đọc data test từ pickList.json cho step Cucumber - dùng lại đúng nguồn JSON của @DataProvider,
     * không tạo data riêng. updateDataModel tạo instance mới qua reflection bên trong (không mutate
     * object truyền vào) nên phải lấy giá trị từ Object[][] trả về, không dùng lại tham số đã truyền.
     */
    public PickListAddNewModel loadPickListAddNewModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListAddNewModel(), dataList);
        return (PickListAddNewModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Edit. */
    public PickListEditModel loadPickListEditModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListEditModel(), dataList);
        return (PickListEditModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Search. */
    public PickListSearchModel loadPickListSearchModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListSearchModel(), dataList);
        return (PickListSearchModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho scenario Filter kết hợp Or (PL_FUNC-19). */
    public PickListTwoConditionFilterModel loadPickListTwoConditionFilterModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListTwoConditionFilterModel(), dataList);
        return (PickListTwoConditionFilterModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Sort (PL_FUNC-9..14). */
    public PickListSortModel loadPickListSortModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListSortModel(), dataList);
        return (PickListSortModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Filter (PL_FUNC-15,16,17). */
    public PickListFilterModel loadPickListFilterModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListFilterModel(), dataList);
        return (PickListFilterModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho scenario tìm kiếm khớp nhiều trường (PL_FUNC-8). */
    public PickListMultiFieldSearchModel loadPickListMultiFieldSearchModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListMultiFieldSearchModel(), dataList);
        return (PickListMultiFieldSearchModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Delete 1 bản ghi (PL_FUNC-35,36,37,38). */
    public PickListDeleteModel loadPickListDeleteModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListDeleteModel(), dataList);
        return (PickListDeleteModel) data[0][0];
    }

    /** Tương tự loadPickListAddNewModel, dùng cho nhóm scenario Delete nhiều bản ghi (PL_FUNC-40,41,42). */
    public PickListMultiDeleteModel loadPickListMultiDeleteModel(String jsonKey) {
        var dataList = jsonUtils.readDataTestFromJSON(DataPath.DATA_PICK_LIST, jsonKey);
        Object[][] data = updateDataModel(new PickListMultiDeleteModel(), dataList);
        return (PickListMultiDeleteModel) data[0][0];
    }
}
