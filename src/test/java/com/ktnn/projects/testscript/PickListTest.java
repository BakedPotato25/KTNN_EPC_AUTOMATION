package com.ktnn.projects.testscript;

import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst.CategoryType;
import com.ktnn.projects.common.TestBase;
import com.ktnn.projects.dataprovider.model.PickListFilterModel;
import com.ktnn.projects.dataprovider.model.PickListMultiFieldSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSortModel;
import com.ktnn.projects.dataprovider.model.PickListTwoConditionFilterModel;
import com.ktnn.projects.dataprovider.providers.PickListProvider;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.PickListPage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PickListTest extends TestBase {
    PickListPage pickListPage;
    HomePage homePage;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
        homePage = new HomePage();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        pickListPage = homePage.gotoPickListPage();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm tương đối theo Name (PL_FUNC-1)",
            dataProvider = "KTNN_PickListSearch_001_Relative", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_001_Relative(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainKeyword(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm tuyệt đối theo Name (PL_FUNC-2)",
            dataProvider = "KTNN_PickListSearch_002_Exact", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_002_Exact(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultExactName(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm từ khoá không tồn tại (PL_FUNC-3)",
            dataProvider = "KTNN_PickListSearch_003_NotFound", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_003_NotFound(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm với khoảng trắng đầu/cuối từ khoá (PL_FUNC-4)",
            dataProvider = "KTNN_PickListSearch_004_TrimWhitespace", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_004_TrimWhitespace(PickListSearchModel model) {
        String keyword = model.getSearchKeyword().getValue();
        pickListPage
                .searchByKeyword(keyword)
                .verifySearchResultsContainKeyword(keyword.trim());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm tương đối theo Code (PL_FUNC-5)",
            dataProvider = "KTNN_PickListSearch_005_CodeRelative", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_005_CodeRelative(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainKeyword(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm tuyệt đối theo Code (PL_FUNC-6)",
            dataProvider = "KTNN_PickListSearch_006_CodeExact", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_006_CodeExact(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultExactCode(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra tìm kiếm theo Code không tồn tại (PL_FUNC-7)",
            dataProvider = "KTNN_PickListSearch_007_CodeNotFound", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_007_CodeNotFound(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra từ khoá khớp đồng thời nhiều bản ghi qua các trường khác nhau (PL_FUNC-8)",
            dataProvider = "KTNN_PickListSearch_008_MultiField", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_008_MultiField(PickListMultiFieldSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainRecords(
                        model.getExpectedNameA().getValue(),
                        model.getExpectedNameB().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Code, Sort = Ascending (PL_FUNC-9)",
            dataProvider = "KTNN_PickListSort_001_CodeAscending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_001_CodeAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyCodeColumnSorted(model.getSortDirection().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Code, Sort = Descending (PL_FUNC-10)",
            dataProvider = "KTNN_PickListSort_002_CodeDescending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_002_CodeDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyCodeColumnSorted(model.getSortDirection().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Name, Sort = Ascending (PL_FUNC-11)",
            dataProvider = "KTNN_PickListSort_003_NameAscending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_003_NameAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Name, Sort = Descending (PL_FUNC-12)",
            dataProvider = "KTNN_PickListSort_004_NameDescending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_004_NameDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Create Date, Sort = Descending (PL_FUNC-13)",
            dataProvider = "KTNN_PickListSort_005_CreateDateDescending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_005_CreateDateDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra Order = Create Date, Sort = Ascending (PL_FUNC-14)",
            dataProvider = "KTNN_PickListSort_006_CreateDateAscending", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_006_CreateDateAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra lọc Description - điều kiện 'like' (PL_FUNC-15)",
            dataProvider = "KTNN_PickListFilter_001_DescriptionLike", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_001_DescriptionLike(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterResultsContainDescription(model.getFilterValue().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra lọc Description - điều kiện '=' (PL_FUNC-16)",
            dataProvider = "KTNN_PickListFilter_002_DescriptionExact", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_002_DescriptionExact(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterResultsExactDescription(model.getFilterValue().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra lọc Version - điều kiện '=' (PL_FUNC-17)",
            dataProvider = "KTNN_PickListFilter_003_VersionExact", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_003_VersionExact(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterNarrowedResults();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra kết hợp 2 điều kiện với And (PL_FUNC-18)",
            dataProvider = "KTNN_PickListFilter_004_CombineWithAnd", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_004_CombineWithAnd(PickListTwoConditionFilterModel model) {
        pickListPage
                .filterByTwoConditionsWithAnd(
                        model.getField1().getValue(), model.getOperator1().getValue(), model.getValue1().getValue(),
                        model.getField2().getValue(), model.getOperator2().getValue(), model.getValue2().getValue())
                .verifyFilterResultsContainDescription(model.getValue1().getValue());
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra kết hợp 2 điều kiện với Or (PL_FUNC-19)",
            dataProvider = "KTNN_PickListFilter_005_CombineWithOr", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_005_CombineWithOr(PickListTwoConditionFilterModel model) {
        pickListPage
                .attemptCombineConditionsWithOr(
                        model.getField1().getValue(), model.getOperator1().getValue(), model.getValue1().getValue(),
                        model.getField2().getValue(), model.getOperator2().getValue(), model.getValue2().getValue())
                .verifyOrConditionNotAvailable();
    }

    @FrameAnnotation(category = {CategoryType.REGRESSION}, author = {AuthorType.SWEETPOTATO}, reviewer = {AuthorType.SWEETPOTATO})
    @Test(description = "Kiểm tra refresh tải lại danh sách và reset bộ lọc (PL_FUNC-20)",
            dataProvider = "KTNN_PickListRefresh_001_ResetSearch", dataProviderClass = PickListProvider.class)
    public void KTNN_PickListRefresh_001_ResetSearch(PickListSearchModel model) {
        pickListPage
                .captureResultsCountBaseline()
                .searchByKeyword(model.getSearchKeyword().getValue())
                .clickRefresh()
                .verifySearchAndFilterReset();
    }
}
