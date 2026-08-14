package com.ktnn.projects.testscript;

import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst.CategoryType;
import com.ktnn.projects.common.TestBase;
import com.ktnn.projects.dataprovider.model.PickListAddItemModel;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListDataTypeChangeModel;
import com.ktnn.projects.dataprovider.model.PickListDataTypeLockedModel;
import com.ktnn.projects.dataprovider.model.PickListDeleteInUseModel;
import com.ktnn.projects.dataprovider.model.PickListDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListEditItemModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.dataprovider.model.PickListFilterModel;
import com.ktnn.projects.dataprovider.model.PickListItemOrderModel;
import com.ktnn.projects.dataprovider.model.PickListMultiDeleteInUseModel;
import com.ktnn.projects.dataprovider.model.PickListMultiDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListMultiFieldSearchModel;
import com.ktnn.projects.dataprovider.model.PickListOverallSaveModel;
import com.ktnn.projects.dataprovider.model.PickListSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSortModel;
import com.ktnn.projects.dataprovider.model.PickListTwoConditionFilterModel;
import com.ktnn.projects.dataprovider.providers.PickListProvider;
import com.ktnn.projects.pages.pages.CharacteristicCatalogPage;
import com.ktnn.projects.pages.pages.HomePage;
import com.ktnn.projects.pages.pages.PickListPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class PickListTest extends TestBase {
    PickListPage pickListPage;
    HomePage homePage;
    private String pendingCleanupKeyword;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
        homePage = new HomePage();
        // fail sớm rõ ràng nếu seed data dùng chung bị mất, thay vì để nhiều case fail rải rác
        homePage.gotoPickListPage().verifySeedDataAvailable();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        pickListPage = homePage.gotoPickListPage();
    }

    // Lưới an toàn cho exception giữa flow; không bao giờ assert để cleanup không làm fail test.
    @AfterMethod(alwaysRun = true)
    public void cleanupPendingTestData() {
        if (pendingCleanupKeyword == null) return;
        try {
            pickListPage.closeAddNewForm();
        } catch (Exception ignored) {
        }
        try {
            pickListPage.clickEditCancel();
        } catch (Exception ignored) {
        }
        try {
            pickListPage.removeLeftoverRecords(pendingCleanupKeyword);
        } catch (Exception ignored) {
        } finally {
            pendingCleanupKeyword = null;
        }
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm tương đối theo Name (PL_FUNC-1)",
        dataProvider = "KTNN_PickListSearch_001_Relative",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_001_Relative(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainKeyword(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm tuyệt đối theo Name (PL_FUNC-2)",
        dataProvider = "KTNN_PickListSearch_002_Exact",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_002_Exact(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultExactName(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm từ khoá không tồn tại (PL_FUNC-3)",
        dataProvider = "KTNN_PickListSearch_003_NotFound",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_003_NotFound(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm với khoảng trắng đầu/cuối từ khoá (PL_FUNC-4)",
        dataProvider = "KTNN_PickListSearch_004_TrimWhitespace",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_004_TrimWhitespace(PickListSearchModel model) {
        String keyword = model.getSearchKeyword().getValue();
        pickListPage
                .searchByKeyword(keyword)
                .verifySearchResultsContainKeyword(keyword.trim());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm tương đối theo Code (PL_FUNC-5)",
        dataProvider = "KTNN_PickListSearch_005_CodeRelative",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_005_CodeRelative(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainKeyword(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm tuyệt đối theo Code (PL_FUNC-6)",
        dataProvider = "KTNN_PickListSearch_006_CodeExact",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_006_CodeExact(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultExactCode(model.getSearchKeyword().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm theo Code không tồn tại (PL_FUNC-7)",
        dataProvider = "KTNN_PickListSearch_007_CodeNotFound",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_007_CodeNotFound(PickListSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra từ khoá khớp đồng thời nhiều bản ghi qua các trường khác nhau (PL_FUNC-8)",
        dataProvider = "KTNN_PickListSearch_008_MultiField",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSearch_008_MultiField(PickListMultiFieldSearchModel model) {
        pickListPage
                .searchByKeyword(model.getSearchKeyword().getValue())
                .verifySearchResultsContainRecords(
                        model.getExpectedNameA().getValue(),
                        model.getExpectedNameB().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Code, Sort = Ascending (PL_FUNC-9)",
        dataProvider = "KTNN_PickListSort_001_CodeAscending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_001_CodeAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyCodeColumnSorted(model.getSortDirection().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Code, Sort = Descending (PL_FUNC-10)",
        dataProvider = "KTNN_PickListSort_002_CodeDescending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_002_CodeDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyCodeColumnSorted(model.getSortDirection().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Name, Sort = Ascending (PL_FUNC-11)",
        dataProvider = "KTNN_PickListSort_003_NameAscending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_003_NameAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Name, Sort = Descending (PL_FUNC-12)",
        dataProvider = "KTNN_PickListSort_004_NameDescending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_004_NameDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Create Date, Sort = Descending (PL_FUNC-13)",
        dataProvider = "KTNN_PickListSort_005_CreateDateDescending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_005_CreateDateDescending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra Order = Create Date, Sort = Ascending (PL_FUNC-14)",
        dataProvider = "KTNN_PickListSort_006_CreateDateAscending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListSort_006_CreateDateAscending(PickListSortModel model) {
        pickListPage
                .sortBy(model.getOrderField().getValue(), model.getSortDirection().getValue())
                .verifyGridStillRendersResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra lọc Description - điều kiện 'like' (PL_FUNC-15)",
        dataProvider = "KTNN_PickListFilter_001_DescriptionLike",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_001_DescriptionLike(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterResultsContainDescription(model.getFilterValue().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra lọc Description - điều kiện '=' (PL_FUNC-16)",
        dataProvider = "KTNN_PickListFilter_002_DescriptionExact",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_002_DescriptionExact(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterResultsExactDescription(model.getFilterValue().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra lọc Version - điều kiện '=' (PL_FUNC-17)",
        dataProvider = "KTNN_PickListFilter_003_VersionExact",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_003_VersionExact(PickListFilterModel model) {
        pickListPage
                .filterBy(model.getFilterField().getValue(), model.getFilterOperator().getValue(), model.getFilterValue().getValue())
                .verifyFilterNarrowedResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kết hợp 2 điều kiện với And (PL_FUNC-18)",
        dataProvider = "KTNN_PickListFilter_004_CombineWithAnd",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_004_CombineWithAnd(PickListTwoConditionFilterModel model) {
        pickListPage
                .filterByTwoConditionsWithAnd(
                        model.getField1().getValue(), model.getOperator1().getValue(), model.getValue1().getValue(),
                        model.getField2().getValue(), model.getOperator2().getValue(), model.getValue2().getValue())
                .verifyFilterResultsContainDescription(model.getValue1().getValue());
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kết hợp 2 điều kiện với Or (PL_FUNC-19)",
        dataProvider = "KTNN_PickListFilter_005_CombineWithOr",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListFilter_005_CombineWithOr(PickListTwoConditionFilterModel model) {
        pickListPage
                .attemptCombineConditionsWithOr(
                        model.getField1().getValue(), model.getOperator1().getValue(), model.getValue1().getValue(),
                        model.getField2().getValue(), model.getOperator2().getValue(), model.getValue2().getValue())
                .verifyOrConditionNotAvailable();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra refresh tải lại danh sách và reset bộ lọc (PL_FUNC-20)",
        dataProvider = "KTNN_PickListRefresh_001_ResetSearch",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListRefresh_001_ResetSearch(PickListSearchModel model) {
        pickListPage
                .captureResultsCountBaseline()
                .searchByKeyword(model.getSearchKeyword().getValue())
                .clickRefresh()
                .verifySearchAndFilterReset();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập dữ liệu hợp lệ vào tất cả các trường (PL_FUNC-21)",
        dataProvider = "KTNN_PickListAddNew_001_Valid",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_001_Valid(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra lưới cập nhật sau khi thêm mới thành công (PL_FUNC-22)",
        dataProvider = "KTNN_PickListAddNew_002_GridUpdates",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_002_GridUpdates(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .captureResultsCountBaseline()
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyResultsCountIncreasedByOne()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập dữ liệu hợp lệ chỉ vào các trường bắt buộc (PL_FUNC-23)",
        dataProvider = "KTNN_PickListAddNew_003_RequiredFieldsOnly",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_003_RequiredFieldsOnly(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra để trống trường Name (PL_FUNC-24)",
        dataProvider = "KTNN_PickListAddNew_004_EmptyName",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_004_EmptyName(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("Name is required")
                .verifyRequiredFieldError("Name")
                .closeAddNewForm();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra để trống trường Code (PL_FUNC-25)",
        dataProvider = "KTNN_PickListAddNew_005_EmptyCode",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_005_EmptyCode(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("Code is required")
                .verifyRequiredFieldError("Code")
                .closeAddNewForm();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập khoảng trắng vào trường bắt buộc (PL_FUNC-26)",
        dataProvider = "KTNN_PickListAddNew_006_WhitespaceName",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_006_WhitespaceName(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("Name is required")
                .verifyRequiredFieldError("Name")
                .closeAddNewForm();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập trùng Code đã tồn tại (PL_FUNC-27)",
        dataProvider = "KTNN_PickListAddNew_007_DuplicateCode",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_007_DuplicateCode(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("Code is unique")
                .verifyAddNewDialogStillOpen()
                .closeAddNewForm();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [Close] không lưu dữ liệu (PL_FUNC-28)",
        dataProvider = "KTNN_PickListAddNew_008_CloseDiscardsData",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_008_CloseDiscardsData(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .closeAddNewForm()
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [X] không lưu dữ liệu (PL_FUNC-29)",
        dataProvider = "KTNN_PickListAddNew_009_XDiscardsData",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_009_XDiscardsData(PickListAddNewModel model) {
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .closeAddNewFormByX()
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchNoResults();
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập ValidFor có ngày bắt đầu lớn hơn ngày kết thúc (PL_FUNC-44)",
        dataProvider = "KTNN_PickListAddNew_010_ValidForFromAfterTo",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_010_ValidForFromAfterTo(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                // Excel gốc kỳ vọng báo lỗi/không cho lưu - hệ thống thật tự xoá trắng "đến ngày" khi blur, vẫn cho lưu
                .verifyValidForToClearedAsInvalid()
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập ValidFor có ngày bắt đầu bằng ngày kết thúc (PL_FUNC-45)",
        dataProvider = "KTNN_PickListAddNew_011_ValidForFromEqualsTo",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_011_ValidForFromEqualsTo(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nhập ValidFor có ngày bắt đầu nhỏ hơn ngày kết thúc (PL_FUNC-46)",
        dataProvider = "KTNN_PickListAddNew_012_ValidForFromBeforeTo",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddNew_012_ValidForFromBeforeTo(PickListAddNewModel model) {
        pendingCleanupKeyword = model.getName().getValue();
        pickListPage
                .openAddNewForm()
                .fillAddNewForm(model)
                .clickAddNewSave()
                .verifyToastMessageContains("successfully")
                .verifyAddNewDialogClosed()
                .searchByKeyword(model.getName().getValue())
                .verifySearchResultExactName(model.getName().getValue())
                .deleteRecordByExactSearch(model.getName().getValue());
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra sửa dữ liệu hợp lệ vào tất cả các trường có thể sửa (PL_FUNC-30)",
        dataProvider = "KTNN_PickListEdit_001_Valid",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_001_Valid(PickListEditModel model) {
        String initialName = model.getInitialName().getValue();
        String newName = model.getNewName().getValue();
        pendingCleanupKeyword = initialName;
        pickListPage
                .setupRecordToEdit(initialName, model.getInitialCode().getValue())
                .openEditFormByExactSearch(initialName)
                .fillEditForm(model)
                .clickEditSave()
                .verifyToastMessageContains("successfully");
        // cleanup keyword vẫn giữ tên ban đầu - vẫn khớp nếu rename chưa kịp thực hiện
        pickListPage
                .searchByKeyword(newName)
                .verifySearchResultExactName(newName)
                .deleteRecordByExactSearch(newName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra lưới cập nhật sau khi sửa thành công (PL_FUNC-31)",
        dataProvider = "KTNN_PickListEdit_002_GridUpdates",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_002_GridUpdates(PickListEditModel model) {
        String initialName = model.getInitialName().getValue();
        String newName = model.getNewName().getValue();
        pendingCleanupKeyword = initialName;
        pickListPage
                .setupRecordToEdit(initialName, model.getInitialCode().getValue())
                .openEditFormByExactSearch(initialName)
                .fillEditForm(model)
                .clickEditSave()
                .verifyToastMessageContains("successfully");
        // cleanup keyword vẫn giữ tên ban đầu - vẫn khớp nếu rename chưa kịp thực hiện
        pickListPage
                .searchByKeyword(newName)
                .verifySearchResultExactName(newName)
                .verifyFilterResultsExactDescription(model.getNewDescription().getValue())
                .deleteRecordByExactSearch(newName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá trắng trường Name (bắt buộc) khi sửa (PL_FUNC-32)",
        dataProvider = "KTNN_PickListEdit_003_EmptyName",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_003_EmptyName(PickListEditModel model) {
        String initialName = model.getInitialName().getValue();
        pendingCleanupKeyword = initialName;
        pickListPage
                .setupRecordToEdit(initialName, model.getInitialCode().getValue())
                .openEditFormByExactSearch(initialName)
                .clearEditName()
                .clickEditSave()
                .verifyToastMessageContains("Name is required")
                .verifyEditRequiredFieldError("Name")
                .clickEditCancel()
                .deleteRecordByExactSearch(initialName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra trường Code cho phép sửa (PL_FUNC-33)",
        dataProvider = "KTNN_PickListEdit_004_CodeEditable",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_004_CodeEditable(PickListEditModel model) {
        String initialName = model.getInitialName().getValue();
        String initialCode = model.getInitialCode().getValue();
        String attemptedCode = model.getAttemptedCode().getValue();
        pendingCleanupKeyword = initialName;
        pickListPage
                .setupRecordToEdit(initialName, initialCode)
                .openEditFormByExactSearch(initialName)
                .inputEditCode(attemptedCode)
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .searchByKeyword(initialName)
                .verifySearchResultExactCode(attemptedCode)
                .deleteRecordByExactSearch(initialName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [Cancel] không lưu thay đổi (PL_FUNC-34)",
        dataProvider = "KTNN_PickListEdit_005_CancelDiscardsChanges",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_005_CancelDiscardsChanges(PickListEditModel model) {
        String initialName = model.getInitialName().getValue();
        pendingCleanupKeyword = initialName;
        pickListPage
                .setupRecordToEdit(initialName, model.getInitialCode().getValue())
                .openEditFormByExactSearch(initialName)
                .fillEditForm(model)
                .clickEditCancel()
                .verifyEditPanelClosed()
                .searchByKeyword(initialName)
                .verifySearchResultExactName(initialName)
                .deleteRecordByExactSearch(initialName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra không thể thay đổi Data Type khi PickList đã có Item (PL_FUNC-47)",
        dataProvider = "KTNN_PickListEdit_006_DataTypeLockedWithItem",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_006_DataTypeLockedWithItem(PickListDataTypeLockedModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .addItemToCurrentEdit(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .verifyEditDataTypeDisabled()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra thay đổi Data Type thành công khi PickList chưa có Item (PL_FUNC-48)",
        dataProvider = "KTNN_PickListEdit_007_DataTypeChangeableWithoutItem",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_007_DataTypeChangeableWithoutItem(PickListDataTypeChangeModel model) {
        String pickListName = model.getPickListName().getValue();
        String newDataType = model.getNewDataType().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .selectEditDataType(newDataType)
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel()
                .openEditFormByExactSearch(pickListName)
                .verifyEditDataTypeValue(newDataType)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích icon trên bản ghi → side panel General hiện ra (PL_FUNC-49)",
        dataProvider = "KTNN_PickListEdit_008_SidePanelGeneralOpens",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_008_SidePanelGeneralOpens(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .verifyEditPanelExpanded()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích icon PickList Item → side panel chuyển sang tab PickList Item (PL_FUNC-50)",
        dataProvider = "KTNN_PickListEdit_009_SidePanelSwitchToItemTab",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_009_SidePanelSwitchToItemTab(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .clickEditPickListItemTab()
                .verifyPickListItemTabActive()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra 2 nút minimize/expand side panel (PL_FUNC-51)",
        dataProvider = "KTNN_PickListEdit_010_SidePanelMinimizeExpand",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListEdit_010_SidePanelMinimizeExpand(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .clickPanelToggle()
                .verifyEditPanelCollapsed()
                .clickPanelToggle()
                .verifyEditPanelExpanded()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra hiển thị confirm trước khi xoá (PL_FUNC-35)",
        dataProvider = "KTNN_PickListDelete_001_ConfirmDialogShown",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListDelete_001_ConfirmDialogShown(PickListDeleteModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .searchByKeyword(name)
                .clickRowDeleteIcon()
                .verifyDeleteConfirmDialogShown()
                .cancelDelete()
                .deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá 1 bản ghi - đồng ý xoá (PL_FUNC-36)",
        dataProvider = "KTNN_PickListDelete_002_ConfirmYes",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListDelete_002_ConfirmYes(PickListDeleteModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .searchByKeyword(name)
                .captureResultsCountBaseline()
                .clickRowDeleteIcon()
                .confirmDelete()
                .verifyToastMessageContains("successfully")
                .verifyResultsCountDecreasedBy(1)
                .searchByKeyword(name)
                .verifySearchNoResults();
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá 1 bản ghi bằng icon xoá trên thanh công cụ (toolbar) (PL_FUNC-37)",
        dataProvider = "KTNN_PickListDelete_003_ViaToolbarCheckbox",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListDelete_003_ViaToolbarCheckbox(PickListDeleteModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .searchByKeyword(name)
                .captureResultsCountBaseline()
                .selectRowCheckbox(1)
                .clickDeleteToolbar()
                .verifyDeleteConfirmDialogShown()
                .confirmDelete()
                .verifyToastMessageContains("successfully")
                .verifyResultsCountDecreasedBy(1)
                .searchByKeyword(name)
                .verifySearchNoResults();
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá 1 bản ghi - huỷ không xoá (PL_FUNC-38)",
        dataProvider = "KTNN_PickListDelete_004_ConfirmNo",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListDelete_004_ConfirmNo(PickListDeleteModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .searchByKeyword(name)
                .clickRowDeleteIcon()
                .cancelDelete()
                .searchByKeyword(name)
                .verifySearchResultExactName(name)
                .deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá 1 bản ghi PickList đang được sử dụng ở chức năng khác (PL_FUNC-39)",
        dataProvider = "KTNN_PickListDelete_005_BlockedWhenInUse",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListDelete_005_BlockedWhenInUse(PickListDeleteInUseModel model) {
        String pickListName = model.getPickListName().getValue();
        String catalogName = model.getCatalogName().getValue();
        String csName = model.getSpecificationName().getValue();
        pendingCleanupKeyword = pickListName;

        pickListPage.setupRecordToEdit(pickListName, model.getPickListCode().getValue());

        CharacteristicCatalogPage catalogPage = homePage.gotoCharacteristicCatalogPage();
        catalogPage
                .removeLeftoverCatalog(catalogName, csName)
                .createCatalog(catalogName, model.getCatalogCode().getValue())
                .addSpecificationLinkedToPickList(csName, model.getSpecificationCode().getValue(), pickListName);

        // Excel gốc kỳ vọng hệ thống chặn xoá (bản ghi vẫn còn) - verify đúng spec, biết trước sẽ Fail vì hệ thống thật cho xoá bình thường
        pickListPage = homePage.gotoPickListPage();
        pickListPage
                .searchByKeyword(pickListName)
                .clickRowDeleteIcon()
                .confirmDelete()
                .searchByKeyword(pickListName)
                .verifySearchResultExactName(pickListName);

        // dọn dẹp: xoá PickList nếu vẫn còn (trường hợp bug được fix sau này); Specification/Catalog luôn phải xoá dù PickList đã mất
        pickListPage.removeLeftoverRecords(pickListName);
        catalogPage = homePage.gotoCharacteristicCatalogPage();
        catalogPage.removeLeftoverCatalog(catalogName, csName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tích chọn nhiều bản ghi và xoá (PL_FUNC-40)",
        dataProvider = "KTNN_PickListMultiDelete_001_SelectMultipleAndDelete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListMultiDelete_001_SelectMultipleAndDelete(PickListMultiDeleteModel model) {
        String keyword = model.getSearchKeyword().getValue();
        pendingCleanupKeyword = keyword;
        pickListPage
                .setupRecordToEdit(model.getName1().getValue(), model.getCode1().getValue())
                .setupRecordToEdit(model.getName2().getValue(), model.getCode2().getValue())
                .searchByKeyword(keyword)
                .captureResultsCountBaseline()
                .selectRowCheckbox(1)
                .selectRowCheckbox(2)
                .clickDeleteToolbar()
                .confirmDelete()
                .verifyToastMessageContains("successfully")
                .verifyResultsCountDecreasedBy(2)
                .searchByKeyword(keyword)
                .verifySearchNoResults();
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tích chọn tất cả bằng checkbox header (PL_FUNC-41)",
        dataProvider = "KTNN_PickListMultiDelete_002_SelectAllCheckbox",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListMultiDelete_002_SelectAllCheckbox(PickListMultiDeleteModel model) {
        String keyword = model.getSearchKeyword().getValue();
        pendingCleanupKeyword = keyword;
        pickListPage
                .setupRecordToEdit(model.getName1().getValue(), model.getCode1().getValue())
                .setupRecordToEdit(model.getName2().getValue(), model.getCode2().getValue())
                .searchByKeyword(keyword)
                .selectAllCheckbox()
                .verifyAllVisibleRowsSelected(2)
                .clickDeleteToolbar()
                .confirmDelete()
                .searchByKeyword(keyword)
                .verifySearchNoResults();
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tích chọn nhiều bản ghi - huỷ không xoá (PL_FUNC-42)",
        dataProvider = "KTNN_PickListMultiDelete_003_CancelDelete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListMultiDelete_003_CancelDelete(PickListMultiDeleteModel model) {
        String keyword = model.getSearchKeyword().getValue();
        pendingCleanupKeyword = keyword;
        pickListPage
                .setupRecordToEdit(model.getName1().getValue(), model.getCode1().getValue())
                .setupRecordToEdit(model.getName2().getValue(), model.getCode2().getValue())
                .searchByKeyword(keyword)
                .selectRowCheckbox(1)
                .selectRowCheckbox(2)
                .clickDeleteToolbar()
                .cancelDelete()
                // Cancel không đổi selection - 2 checkbox vẫn đang tick, không search/selectAll lại
                // (selectAll lần 2 trên checkbox đã tick sẽ toggle OFF thay vì giữ nguyên chọn)
                .verifyResultsCountEquals(2)
                .clickDeleteToolbar()
                .confirmDelete();
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xoá nhiều bản ghi, trong đó có 1 bản ghi không xoá được do đang được sử dụng ở chức năng khác (PL_FUNC-43)",
        dataProvider = "KTNN_PickListMultiDelete_004_BlockedWhenOneInUse",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListMultiDelete_004_BlockedWhenOneInUse(PickListMultiDeleteInUseModel model) {
        String name1 = model.getName1().getValue();
        String code1 = model.getCode1().getValue();
        String name2 = model.getName2().getValue();
        String code2 = model.getCode2().getValue();
        String keyword = model.getSearchKeyword().getValue();
        String catalogName = model.getCatalogName().getValue();
        String csName = model.getSpecificationName().getValue();
        pendingCleanupKeyword = keyword;

        pickListPage
                .setupRecordToEdit(name1, code1)
                .setupRecordToEdit(name2, code2);

        CharacteristicCatalogPage catalogPage = homePage.gotoCharacteristicCatalogPage();
        catalogPage
                .removeLeftoverCatalog(catalogName, csName)
                .createCatalog(catalogName, model.getCatalogCode().getValue())
                .addSpecificationLinkedToPickList(csName, model.getSpecificationCode().getValue(), name2);

        // Hệ thống thật không chặn xoá bản ghi đang dùng (đã xác nhận ở PL_FUNC-39) - xoá đồng loạt cả 2 đều thành công
        pickListPage = homePage.gotoPickListPage();
        pickListPage
                .searchByKeyword(keyword)
                .captureResultsCountBaseline()
                .selectRowCheckbox(1)
                .selectRowCheckbox(2)
                .clickDeleteToolbar()
                .confirmDelete()
                .verifyToastMessageContains("successfully")
                .verifyResultsCountDecreasedBy(2)
                .searchByKeyword(keyword)
                .verifySearchNoResults();

        catalogPage = homePage.gotoCharacteristicCatalogPage();
        catalogPage.removeLeftoverCatalog(catalogName, csName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [+] → form Add item hiện inline trong side panel (PL_FUNC-67)",
        dataProvider = "KTNN_PickListAddItem_001_FormDisplaysInline",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_001_FormDisplaysInline(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .verifyAddItemFormDisplayed()
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tính đủ các field trong form Add item (PL_FUNC-68)",
        dataProvider = "KTNN_PickListAddItem_002_FormFieldsComplete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_002_FormFieldsComplete(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .verifyAddItemFormFieldsComplete()
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra toggle Is Default mặc định ON khi Add (PL_FUNC-69)",
        dataProvider = "KTNN_PickListAddItem_003_IsDefaultOnByDefault",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_003_IsDefaultOnByDefault(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .verifyItemIsDefaultOnByDefault()
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra thêm item hợp lệ - tất cả các trường (PL_FUNC-70)",
        dataProvider = "KTNN_PickListAddItem_004_ValidAllFields",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_004_ValidAllFields(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemConfirm()
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        // Item chỉ thật sự lưu khi Save panel thành công - mở lại bản ghi để verify đã persist, không chỉ tin UI ngay sau Confirm
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyItemInList(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra thêm item chỉ với các trường bắt buộc (PL_FUNC-71)",
        dataProvider = "KTNN_PickListAddItem_005_RequiredFieldsOnly",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_005_RequiredFieldsOnly(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemConfirm()
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyItemInList(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra để trống trường Name/Label (PL_FUNC-72)",
        dataProvider = "KTNN_PickListAddItem_006_EmptyNameLabel",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_006_EmptyNameLabel(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemConfirm()
                .verifyItemRequiredFieldError("Name")
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra để trống trường Code (PL_FUNC-73)",
        dataProvider = "KTNN_PickListAddItem_007_EmptyCode",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_007_EmptyCode(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemConfirm()
                .verifyItemRequiredFieldError("Code")
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra để trống trường Value (PL_FUNC-74)",
        dataProvider = "KTNN_PickListAddItem_008_EmptyValue",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_008_EmptyValue(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemConfirm()
                .verifyItemRequiredFieldError("Value")
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [✗] hủy Add - không lưu item (PL_FUNC-75)",
        dataProvider = "KTNN_PickListAddItem_009_CancelDiscardsItem",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListAddItem_009_CancelDiscardsItem(PickListAddItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .openAddItemForm()
                .fillAddItemForm(model)
                .clickItemCancelForm()
                .verifyItemNotInList(model.getItemNameLabel().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tính đủ của các control trong tab PickList Item (PL_FUNC-52)",
        dataProvider = "KTNN_PickListItemUI_001_ControlsComplete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemUI_001_ControlsComplete(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .verifyPickListItemControlsComplete()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tính đủ của các trường trong mỗi item (PL_FUNC-53)",
        dataProvider = "KTNN_PickListItemUI_002_RowFieldsComplete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemUI_002_RowFieldsComplete(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .verifyItemRowFieldsComplete()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra ô Search và các icon bên trong (PL_FUNC-54)",
        dataProvider = "KTNN_PickListItemUI_003_SearchControlsComplete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemUI_003_SearchControlsComplete(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .verifyItemSearchControlsComplete()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra icon Order sắp xếp tăng dần (1→9) (PL_FUNC-55)",
        dataProvider = "KTNN_PickListItemUI_004_OrderAscending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemUI_004_OrderAscending(PickListItemOrderModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItem1NameLabel().getValue(), model.getItem1Code().getValue(), model.getItem1Value().getValue())
                .addSimpleItem(model.getItem2NameLabel().getValue(), model.getItem2Code().getValue(), model.getItem2Value().getValue())
                // Tăng dần (item cũ nhất lên đầu) là trạng thái MẶC ĐỊNH của icon Order - không cần click,
                // xem chi tiết quirk "Order icon là toggle 1-click" ở PickListObjects.clickItemOrderToggle
                .verifyFirstItemRowContains(model.getItem1NameLabel().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra icon Order sắp xếp giảm dần (9→1) (PL_FUNC-56)",
        dataProvider = "KTNN_PickListItemUI_005_OrderDescending",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemUI_005_OrderDescending(PickListItemOrderModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItem1NameLabel().getValue(), model.getItem1Code().getValue(), model.getItem1Value().getValue())
                .addSimpleItem(model.getItem2NameLabel().getValue(), model.getItem2Code().getValue(), model.getItem2Value().getValue())
                .clickItemOrderToggle()
                .verifyFirstItemRowContains(model.getItem2NameLabel().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra dropdown Show có đủ tùy chọn (PL_FUNC-57)",
        dataProvider = "KTNN_PickListItemPaging_001_ShowOptionsComplete",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_001_ShowOptionsComplete(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .verifyItemShowOptionsComplete()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chọn Show = 5 (mặc định) (PL_FUNC-58)",
        dataProvider = "KTNN_PickListItemPaging_002_Show5Default",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_002_Show5Default(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .selectItemShowPageSize("5")
                .verifyItemShowPageSizeApplied(5)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chọn Show = 10 (PL_FUNC-59)",
        dataProvider = "KTNN_PickListItemPaging_003_Show10",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_003_Show10(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .selectItemShowPageSize("10")
                .verifyItemShowPageSizeApplied(10)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chọn Show = 25 (PL_FUNC-60)",
        dataProvider = "KTNN_PickListItemPaging_004_Show25",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_004_Show25(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .selectItemShowPageSize("25")
                .verifyItemShowPageSizeApplied(25)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chọn Show = 50 (PL_FUNC-61)",
        dataProvider = "KTNN_PickListItemPaging_005_Show50",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_005_Show50(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .selectItemShowPageSize("50")
                .verifyItemShowPageSizeApplied(50)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chọn Show = 100 (PL_FUNC-62)",
        dataProvider = "KTNN_PickListItemPaging_006_Show100",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_006_Show100(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .selectItemShowPageSize("100")
                .verifyItemShowPageSizeApplied(100)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nút [>] trang sau (PL_FUNC-63)",
        dataProvider = "KTNN_PickListItemPaging_007_NextPage",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_007_NextPage(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .verifyItemPageNavState(true, true, false, false)
                .clickItemPageNext()
                .verifyItemPageNavState(false, false, true, true)
                .verifyItemPaginatorRange(6, 6, 6)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nút [<] trang trước (PL_FUNC-64)",
        dataProvider = "KTNN_PickListItemPaging_008_PrevPage",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_008_PrevPage(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .clickItemPageNext()
                .clickItemPagePrev()
                .verifyItemPageNavState(true, true, false, false)
                .verifyItemPaginatorRange(1, 5, 6)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra nút [>>] trang cuối và [<<] trang đầu (PL_FUNC-65)",
        dataProvider = "KTNN_PickListItemPaging_009_FirstLastPage",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_009_FirstLastPage(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .clickItemPageLast()
                .verifyItemPaginatorRange(6, 6, 6)
                .clickItemPageFirst()
                .verifyItemPaginatorRange(1, 5, 6)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra thông tin Items x-y/z hiển thị đúng (PL_FUNC-66)",
        dataProvider = "KTNN_PickListItemPaging_010_ItemsRangeText",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemPaging_010_ItemsRangeText(PickListAddNewModel model) {
        String name = model.getName().getValue();
        String code = model.getCode().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, code)
                .openEditFormByExactSearch(name)
                .seedItems(code, 6)
                .clickEditPickListItemTab()
                .verifyItemPaginatorRange(1, 5, 6)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra hover vào icon ⋮ → hiện icon 👁 và icon 🗑 (PL_FUNC-76)",
        dataProvider = "KTNN_PickListItemView_001_HoverRevealsIcons",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemView_001_HoverRevealsIcons(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .hoverFirstRowItemMore()
                .verifyItemHoverIconsVisible()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích icon 👁 → sub-section edit hiện ngay dưới item (PL_FUNC-77)",
        dataProvider = "KTNN_PickListItemView_002_EyeOpensSubSection",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemView_002_EyeOpensSubSection(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .clickFirstRowItemEye()
                .verifyItemSubSectionDisplayed()
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra dữ liệu cũ được load đúng vào sub-section (PL_FUNC-78)",
        dataProvider = "KTNN_PickListItemView_003_DataLoadedCorrectly",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemView_003_DataLoadedCorrectly(PickListDataTypeLockedModel model) {
        String pickListName = model.getPickListName().getValue();
        String itemNameLabel = model.getItemNameLabel().getValue();
        String itemCode = model.getItemCode().getValue();
        String itemValue = model.getItemValue().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(itemNameLabel, itemCode, itemValue)
                .clickFirstRowItemEye()
                .verifyItemFieldsLoadedCorrectly(itemNameLabel, itemCode, itemValue)
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra sửa item hợp lệ - kích [✓] lưu thay đổi (PL_FUNC-79)",
        dataProvider = "KTNN_PickListItemEdit_001_ValidSaveViaConfirm",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemEdit_001_ValidSaveViaConfirm(PickListEditItemModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getInitialNameLabel().getValue(), model.getInitialCode().getValue(), model.getInitialValue().getValue())
                .clickFirstRowItemEye()
                .fillEditItemForm(model.getNewNameLabel().getValue(), model.getNewValue().getValue())
                .clickItemConfirm()
                .verifyFirstItemRowContains(model.getNewNameLabel().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra xóa trắng trường Name/Label khi sửa → validate lỗi (PL_FUNC-80)",
        dataProvider = "KTNN_PickListItemEdit_002_EmptyNameLabel",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemEdit_002_EmptyNameLabel(PickListDataTypeLockedModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickFirstRowItemEye()
                .clearItemNameLabel()
                .clickItemConfirm()
                .verifyItemRequiredFieldError("Name")
                .clickItemCancelForm()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [✗] hủy sửa - không lưu thay đổi (PL_FUNC-81)",
        dataProvider = "KTNN_PickListItemEdit_003_CancelDiscardsChanges",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemEdit_003_CancelDiscardsChanges(PickListEditItemModel model) {
        String pickListName = model.getPickListName().getValue();
        String initialNameLabel = model.getInitialNameLabel().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(initialNameLabel, model.getInitialCode().getValue(), model.getInitialValue().getValue())
                .clickFirstRowItemEye()
                .fillEditItemForm(model.getNewNameLabel().getValue(), model.getNewValue().getValue())
                .clickItemCancelForm()
                .verifyFirstItemRowContains(initialNameLabel)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích icon 🗑 → xóa item ngay lập tức không có confirm (PL_FUNC-82)",
        dataProvider = "KTNN_PickListItemDelete_001_NoConfirmDialog",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemDelete_001_NoConfirmDialog(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .clickFirstRowItemTrash()
                .verifyNoDeleteConfirmDialog()
                .verifyItemRowCount(0)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra không có nút Undo sau khi xóa item (PL_FUNC-83)",
        dataProvider = "KTNN_PickListItemDelete_002_NoUndoButton",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemDelete_002_NoUndoButton(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .clickFirstRowItemTrash()
                .verifyNoUndoButton()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm item theo Name/Label - có kết quả (PL_FUNC-84)",
        dataProvider = "KTNN_PickListItemSearch_001_ByNameLabel",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemSearch_001_ByNameLabel(PickListDataTypeLockedModel model) {
        String pickListName = model.getPickListName().getValue();
        String itemNameLabel = model.getItemNameLabel().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(itemNameLabel, model.getItemCode().getValue(), model.getItemValue().getValue())
                .searchItemByKeyword(itemNameLabel)
                // Hệ thống thật chỉ khớp search theo Code, không khớp Name/Label - Fail đúng thiết kế, xem note trong JSON
                .verifyItemSearchResultsContain(itemNameLabel)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tìm kiếm từ khóa không tồn tại (PL_FUNC-85)",
        dataProvider = "KTNN_PickListItemSearch_002_NotFound",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemSearch_002_NotFound(PickListAddNewModel model) {
        String name = model.getName().getValue();
        pendingCleanupKeyword = name;
        pickListPage
                .setupRecordToEdit(name, model.getCode().getValue())
                .openEditFormByExactSearch(name)
                .seedItems(model.getCode().getValue(), 1)
                .clickEditPickListItemTab()
                .searchItemByKeyword("xyzkhongtontai123")
                .verifyItemSearchNoResults()
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(name);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra chỉ 1 item được set Is Default = ON tại 1 thời điểm (PL_FUNC-86)",
        dataProvider = "KTNN_PickListItemIsDefault_001_MultipleAllowed",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemIsDefault_001_MultipleAllowed(PickListItemOrderModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItem1NameLabel().getValue(), model.getItem1Code().getValue(), model.getItem1Value().getValue())
                .addSimpleItem(model.getItem2NameLabel().getValue(), model.getItem2Code().getValue(), model.getItem2Value().getValue())
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                // Is Default mặc định ON khi thêm item mới - cả 2 item đều giữ ON, hệ thống không tự tắt item cũ
                .verifyRowItemIsDefaultChecked(1, true)
                .verifyRowItemIsDefaultChecked(2, true)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra tắt toggle Is Default của item đang ON (PL_FUNC-87)",
        dataProvider = "KTNN_PickListItemIsDefault_002_ToggleOff",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemIsDefault_002_ToggleOff(PickListDataTypeLockedModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickFirstRowItemEye()
                .toggleItemIsDefault()
                .clickItemConfirm()
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyRowItemIsDefaultChecked(1, false)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra hệ thống có cho phép nhiều Item cùng có Is Default = true (PL_FUNC-101)",
        dataProvider = "KTNN_PickListItemIsDefault_003_MultiplePersistAfterSave",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListItemIsDefault_003_MultiplePersistAfterSave(PickListItemOrderModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItem1NameLabel().getValue(), model.getItem1Code().getValue(), model.getItem1Value().getValue())
                .addSimpleItem(model.getItem2NameLabel().getValue(), model.getItem2Code().getValue(), model.getItem2Value().getValue())
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyRowItemIsDefaultChecked(1, true)
                .verifyRowItemIsDefaultChecked(2, true)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [Save] lưu toàn bộ thay đổi PickList (PL_FUNC-88)",
        dataProvider = "KTNN_PickListPanel_001_SavePersistsChanges",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListPanel_001_SavePersistsChanges(PickListOverallSaveModel model) {
        String pickListName = model.getPickListName().getValue();
        String newDescription = model.getNewDescription().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .inputEditDescription(newDescription)
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickEditSave()
                .verifyToastMessageContains("successfully")
                .clickEditCancel();
        // Bug thật đã xác nhận qua Playwright: Save chỉ lưu thay đổi của tab ĐANG ACTIVE lúc bấm Save -
        // tab PickList Item đang active nên Description ở tab General bị mất dù toast báo thành công.
        // Test viết đúng theo spec (kỳ vọng cả 2 thay đổi cùng được lưu) nên Fail đúng thiết kế ở bước dưới.
        pickListPage
                .searchByKeyword(pickListName)
                .verifyFilterResultsExactDescription(newDescription);
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyItemInList(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }

    @FrameAnnotation(
        category = {CategoryType.REGRESSION},
        author = {AuthorType.SWEETPOTATO},
        reviewer = {AuthorType.SWEETPOTATO})
    @Test(
        description = "Kiểm tra kích [Cancel] hủy toàn bộ thay đổi PickList (PL_FUNC-89)",
        dataProvider = "KTNN_PickListPanel_002_CancelDiscardsChanges",
        dataProviderClass = PickListProvider.class)
    public void KTNN_PickListPanel_002_CancelDiscardsChanges(PickListOverallSaveModel model) {
        String pickListName = model.getPickListName().getValue();
        pendingCleanupKeyword = pickListName;
        pickListPage
                .setupRecordToEdit(pickListName, model.getPickListCode().getValue())
                .openEditFormByExactSearch(pickListName)
                .inputEditDescription(model.getNewDescription().getValue())
                .clickEditPickListItemTab()
                .addSimpleItem(model.getItemNameLabel().getValue(), model.getItemCode().getValue(), model.getItemValue().getValue())
                .clickEditCancel()
                .verifyEditPanelClosed();
        pickListPage
                .openEditFormByExactSearch(pickListName)
                .clickEditPickListItemTab()
                .verifyItemRowCount(0)
                .clickEditCancel();
        pickListPage.deleteRecordByExactSearch(pickListName);
        pendingCleanupKeyword = null;
    }
}
