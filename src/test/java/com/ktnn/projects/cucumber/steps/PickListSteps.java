package com.ktnn.projects.cucumber.steps;

import com.ktnn.projects.cucumber.hooks.CucumberHooks;
import com.ktnn.projects.dataprovider.model.PickListAddNewModel;
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
}
