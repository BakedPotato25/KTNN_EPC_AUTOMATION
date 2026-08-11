package com.ktnn.projects.dataprovider.providers;

import com.ktnn.datadriven.BaseProvider;
import com.ktnn.projects.dataprovider.DataPath;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.dataprovider.model.PickListFilterModel;
import com.ktnn.projects.dataprovider.model.PickListMultiFieldSearchModel;
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
}
