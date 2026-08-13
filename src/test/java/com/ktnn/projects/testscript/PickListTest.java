package com.ktnn.projects.testscript;

import com.ktnn.annotations.FrameAnnotation;
import com.ktnn.consts.AuthorType;
import com.ktnn.consts.FrameConst.CategoryType;
import com.ktnn.projects.common.TestBase;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.dataprovider.model.PickListFilterModel;
import com.ktnn.projects.dataprovider.model.PickListMultiDeleteModel;
import com.ktnn.projects.dataprovider.model.PickListMultiFieldSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSortModel;
import com.ktnn.projects.dataprovider.model.PickListTwoConditionFilterModel;
import com.ktnn.projects.dataprovider.providers.PickListProvider;
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
}
