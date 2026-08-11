package com.ktnn.projects.cucumber.steps;

import com.ktnn.projects.cucumber.hooks.CucumberHooks;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
import com.ktnn.projects.dataprovider.model.PickListEditModel;
import com.ktnn.projects.dataprovider.model.PickListSearchModel;
import com.ktnn.projects.dataprovider.model.PickListSortModel;
import com.ktnn.projects.dataprovider.model.PickListTwoConditionFilterModel;
import com.ktnn.projects.dataprovider.providers.PickListProvider;
import com.ktnn.projects.pages.pages.PickListPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Step chỉ orchestrate, gọi thẳng method có sẵn của PickListPage - không viết lại logic
 * Selenium/assert nào mới (toàn bộ logic vẫn nằm ở tầng Locator->Objects->Page).
 */
public class PickListSteps {
    private final PickListProvider pickListProvider = new PickListProvider();
    private PickListPage pickListPage;
    private PickListAddNewModel addNewModel;
    private PickListEditModel editModel;
    private PickListSearchModel searchModel;
    private PickListTwoConditionFilterModel twoConditionModel;
    private PickListSortModel sortModel;

    @Before
    public void beforeScenario() {
        // tái dùng homePage tạo 1 lần ở CucumberHooks, đúng pattern TestBase/PickListTest
        pickListPage = CucumberHooks.homePage.gotoPickListPage();
    }

    @After
    public void afterScenario() {
        // an toàn cho exception giữa flow; không assert để cleanup không làm fail scenario
        try {
            pickListPage.closeAddNewForm();
        } catch (Exception ignored) {
        }
        try {
            pickListPage.clickEditCancel();
        } catch (Exception ignored) {
        }
    }

    @Given("PickList test data {string} is loaded")
    public void pickListTestDataIsLoaded(String jsonKey) {
        addNewModel = pickListProvider.loadPickListAddNewModel(jsonKey);
    }

    @When("I capture the current results count as baseline")
    public void iCaptureTheCurrentResultsCountAsBaseline() {
        pickListPage.captureResultsCountBaseline();
    }

    @When("I open the Add new form")
    public void iOpenTheAddNewForm() {
        pickListPage.openAddNewForm();
    }

    @And("I fill the Add new form with the loaded data")
    public void iFillTheAddNewFormWithTheLoadedData() {
        pickListPage.fillAddNewForm(addNewModel);
    }

    @And("I save the Add new form")
    public void iSaveTheAddNewForm() {
        pickListPage.clickAddNewSave();
    }

    @Then("the toast message should contain {string}")
    public void theToastMessageShouldContain(String expectedSubstring) {
        pickListPage.verifyToastMessageContains(expectedSubstring);
    }

    @And("the Add new dialog should be closed")
    public void theAddNewDialogShouldBeClosed() {
        pickListPage.verifyAddNewDialogClosed();
    }

    @And("the Add new dialog should stay open")
    public void theAddNewDialogShouldStayOpen() {
        pickListPage.verifyAddNewDialogStillOpen();
    }

    @And("the results count should have increased by 1")
    public void theResultsCountShouldHaveIncreasedBy1() {
        pickListPage.verifyResultsCountIncreasedByOne();
    }

    @Then("the required-field error for {string} should be shown")
    public void theRequiredFieldErrorForShouldBeShown(String fieldLabel) {
        pickListPage.verifyRequiredFieldError(fieldLabel);
    }

    @When("I close the Add new form")
    public void iCloseTheAddNewForm() {
        pickListPage.closeAddNewForm();
    }

    @When("I close the Add new form by clicking X")
    public void iCloseTheAddNewFormByClickingX() {
        pickListPage.closeAddNewFormByX();
    }

    @Then("the grid should show no results")
    public void theGridShouldShowNoResults() {
        pickListPage.verifySearchNoResults();
    }

    @When("I search PickList by the created record's name")
    public void iSearchPickListByTheCreatedRecordsName() {
        pickListPage.searchByKeyword(addNewModel.getName().getValue());
    }

    @Then("the grid should show exactly 1 record with that name")
    public void theGridShouldShowExactly1RecordWithThatName() {
        pickListPage.verifySearchResultExactName(addNewModel.getName().getValue());
    }

    @And("I delete the record to clean up test data")
    public void iDeleteTheRecordToCleanUpTestData() {
        pickListPage.deleteRecordByExactSearch(addNewModel.getName().getValue());
    }

    // ----- Edit -----

    @Given("PickList edit test data {string} is loaded")
    public void pickListEditTestDataIsLoaded(String jsonKey) {
        editModel = pickListProvider.loadPickListEditModel(jsonKey);
    }

    @When("I set up a record to edit")
    public void iSetUpARecordToEdit() {
        pickListPage.setupRecordToEdit(editModel.getInitialName().getValue(), editModel.getInitialCode().getValue());
    }

    @And("I open the Edit form for the record")
    public void iOpenTheEditFormForTheRecord() {
        pickListPage.openEditFormByExactSearch(editModel.getInitialName().getValue());
    }

    @And("I fill the Edit form with the loaded data")
    public void iFillTheEditFormWithTheLoadedData() {
        pickListPage.fillEditForm(editModel);
    }

    @And("I save the Edit form")
    public void iSaveTheEditForm() {
        pickListPage.clickEditSave();
    }

    @And("I clear the Name field in the Edit form")
    public void iClearTheNameFieldInTheEditForm() {
        pickListPage.clearEditName();
    }

    @And("I cancel the Edit form")
    public void iCancelTheEditForm() {
        pickListPage.clickEditCancel();
    }

    @And("I input a different Code in the Edit form")
    public void iInputADifferentCodeInTheEditForm() {
        pickListPage.inputEditCode(editModel.getAttemptedCode().getValue());
    }

    @Then("the Edit form should show a required-field error for {string}")
    public void theEditFormShouldShowARequiredFieldErrorFor(String fieldLabel) {
        pickListPage.verifyEditRequiredFieldError(fieldLabel);
    }

    @Then("the Edit panel should be closed")
    public void theEditPanelShouldBeClosed() {
        pickListPage.verifyEditPanelClosed();
    }

    @When("I search PickList by the record's new name")
    public void iSearchPickListByTheRecordsNewName() {
        pickListPage.searchByKeyword(editModel.getNewName().getValue());
    }

    @When("I search PickList by the record's initial name")
    public void iSearchPickListByTheRecordsInitialName() {
        pickListPage.searchByKeyword(editModel.getInitialName().getValue());
    }

    @Then("the grid should show exactly 1 record with the new name")
    public void theGridShouldShowExactly1RecordWithTheNewName() {
        pickListPage.verifySearchResultExactName(editModel.getNewName().getValue());
    }

    @Then("the grid should show exactly 1 record with the initial name")
    public void theGridShouldShowExactly1RecordWithTheInitialName() {
        pickListPage.verifySearchResultExactName(editModel.getInitialName().getValue());
    }

    @Then("the grid should show exactly 1 record with the attempted Code")
    public void theGridShouldShowExactly1RecordWithTheAttemptedCode() {
        pickListPage.verifySearchResultExactCode(editModel.getAttemptedCode().getValue());
    }

    @And("the Description should exactly match the new description")
    public void theDescriptionShouldExactlyMatchTheNewDescription() {
        pickListPage.verifyFilterResultsExactDescription(editModel.getNewDescription().getValue());
    }

    @And("I delete the record by its new name to clean up test data")
    public void iDeleteTheRecordByItsNewNameToCleanUpTestData() {
        pickListPage.deleteRecordByExactSearch(editModel.getNewName().getValue());
    }

    @And("I delete the record by its initial name to clean up test data")
    public void iDeleteTheRecordByItsInitialNameToCleanUpTestData() {
        pickListPage.deleteRecordByExactSearch(editModel.getInitialName().getValue());
    }

    // ----- Search -----

    @Given("PickList search test data {string} is loaded")
    public void pickListSearchTestDataIsLoaded(String jsonKey) {
        searchModel = pickListProvider.loadPickListSearchModel(jsonKey);
    }

    @When("I search PickList using the loaded keyword")
    public void iSearchPickListUsingTheLoadedKeyword() {
        pickListPage.searchByKeyword(searchModel.getSearchKeyword().getValue());
    }

    @Then("the grid rows should contain the trimmed keyword")
    public void theGridRowsShouldContainTheTrimmedKeyword() {
        pickListPage.verifySearchResultsContainKeyword(searchModel.getSearchKeyword().getValue().trim());
    }

    // ----- Filter Or (PL_FUNC-19) -----

    @Given("PickList two-condition filter test data {string} is loaded")
    public void pickListTwoConditionFilterTestDataIsLoaded(String jsonKey) {
        twoConditionModel = pickListProvider.loadPickListTwoConditionFilterModel(jsonKey);
    }

    @When("I attempt to combine two filter conditions with Or")
    public void iAttemptToCombineTwoFilterConditionsWithOr() {
        pickListPage.attemptCombineConditionsWithOr(
                twoConditionModel.getField1().getValue(), twoConditionModel.getOperator1().getValue(), twoConditionModel.getValue1().getValue(),
                twoConditionModel.getField2().getValue(), twoConditionModel.getOperator2().getValue(), twoConditionModel.getValue2().getValue());
    }

    @Then("the Or option should not be available")
    public void theOrOptionShouldNotBeAvailable() {
        pickListPage.verifyOrConditionNotAvailable();
    }

    // ----- Sort (PL_FUNC-9..14) -----

    @Given("PickList sort test data {string} is loaded")
    public void pickListSortTestDataIsLoaded(String jsonKey) {
        sortModel = pickListProvider.loadPickListSortModel(jsonKey);
    }

    @When("I sort PickList using the loaded order and direction")
    public void iSortPickListUsingTheLoadedOrderAndDirection() {
        pickListPage.sortBy(sortModel.getOrderField().getValue(), sortModel.getSortDirection().getValue());
    }

    @Then("the Code column should be sorted in the loaded direction")
    public void theCodeColumnShouldBeSortedInTheLoadedDirection() {
        pickListPage.verifyCodeColumnSorted(sortModel.getSortDirection().getValue());
    }

    @Then("the grid should still render results")
    public void theGridShouldStillRenderResults() {
        pickListPage.verifyGridStillRendersResults();
    }
}
