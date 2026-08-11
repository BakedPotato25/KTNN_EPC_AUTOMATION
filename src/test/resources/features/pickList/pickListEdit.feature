Feature: PickList Edit

  Scenario: Kiểm tra sửa dữ liệu hợp lệ vào tất cả các trường có thể sửa (PL_FUNC-30)
    Given PickList edit test data "KTNN_PickListEdit_001_Valid" is loaded
    When I set up a record to edit
    And I open the Edit form for the record
    And I fill the Edit form with the loaded data
    And I save the Edit form
    Then the toast message should contain "successfully"
    When I search PickList by the record's new name
    Then the grid should show exactly 1 record with the new name
    And I delete the record by its new name to clean up test data

  Scenario: Kiểm tra lưới cập nhật sau khi sửa thành công (PL_FUNC-31)
    Given PickList edit test data "KTNN_PickListEdit_002_GridUpdates" is loaded
    When I set up a record to edit
    And I open the Edit form for the record
    And I fill the Edit form with the loaded data
    And I save the Edit form
    Then the toast message should contain "successfully"
    When I search PickList by the record's new name
    Then the grid should show exactly 1 record with the new name
    And the Description should exactly match the new description
    And I delete the record by its new name to clean up test data

  Scenario: Kiểm tra xoá trắng trường Name (bắt buộc) khi sửa (PL_FUNC-32)
    Given PickList edit test data "KTNN_PickListEdit_003_EmptyName" is loaded
    When I set up a record to edit
    And I open the Edit form for the record
    And I clear the Name field in the Edit form
    And I save the Edit form
    Then the toast message should contain "Name is required"
    And the Edit form should show a required-field error for "Name"
    When I cancel the Edit form
    And I delete the record by its initial name to clean up test data

  Scenario: Kiểm tra trường Code cho phép sửa (PL_FUNC-33)
    Given PickList edit test data "KTNN_PickListEdit_004_CodeEditable" is loaded
    When I set up a record to edit
    And I open the Edit form for the record
    And I input a different Code in the Edit form
    And I save the Edit form
    Then the toast message should contain "successfully"
    When I search PickList by the record's initial name
    Then the grid should show exactly 1 record with the attempted Code
    And I delete the record by its initial name to clean up test data

  Scenario: Kiểm tra kích [Cancel] không lưu thay đổi (PL_FUNC-34)
    Given PickList edit test data "KTNN_PickListEdit_005_CancelDiscardsChanges" is loaded
    When I set up a record to edit
    And I open the Edit form for the record
    And I fill the Edit form with the loaded data
    And I cancel the Edit form
    Then the Edit panel should be closed
    When I search PickList by the record's initial name
    Then the grid should show exactly 1 record with the initial name
    And I delete the record by its initial name to clean up test data
