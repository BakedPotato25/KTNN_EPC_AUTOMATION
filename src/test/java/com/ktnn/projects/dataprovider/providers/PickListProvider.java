package com.ktnn.projects.dataprovider.providers;

import com.ktnn.datadriven.BaseProvider;
import com.ktnn.projects.dataprovider.DataPath;
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
}
