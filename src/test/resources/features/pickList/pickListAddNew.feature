Feature: PickList Add New

  Scenario: Kiểm tra nhập dữ liệu hợp lệ vào tất cả các trường (PL_FUNC-21)
    Given PickList test data "KTNN_PickListAddNew_001_Valid" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "successfully"
    And the Add new dialog should be closed
    When I search PickList by the created record's name
    Then the grid should show exactly 1 record with that name
    And I delete the record to clean up test data
