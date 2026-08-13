Feature: PickList Delete

  Scenario: Kiểm tra hiển thị confirm trước khi xoá (PL_FUNC-35)
    Given PickList delete test data "KTNN_PickListDelete_001_ConfirmDialogShown" is loaded
    When I set up a record to delete
    And I search PickList by the delete record's name
    And I click the row delete icon
    Then the delete confirmation dialog should be shown
    When I cancel the delete
    And I delete the record by exact search to clean up test data

  Scenario: Kiểm tra xoá 1 bản ghi - đồng ý xoá (PL_FUNC-36)
    Given PickList delete test data "KTNN_PickListDelete_002_ConfirmYes" is loaded
    When I set up a record to delete
    And I search PickList by the delete record's name
    And I capture the current results count as baseline
    And I click the row delete icon
    And I confirm the delete
    Then the toast message should contain "successfully"
    And the results count should have decreased by 1
    When I search PickList by the delete record's name
    Then the grid should show no results

  Scenario: Kiểm tra xoá 1 bản ghi bằng icon xoá trên thanh công cụ (toolbar) (PL_FUNC-37)
    Given PickList delete test data "KTNN_PickListDelete_003_ViaToolbarCheckbox" is loaded
    When I set up a record to delete
    And I search PickList by the delete record's name
    And I capture the current results count as baseline
    And I select the checkbox of row 1
    And I click the delete icon on the toolbar
    Then the delete confirmation dialog should be shown
    When I confirm the delete
    Then the toast message should contain "successfully"
    And the results count should have decreased by 1
    When I search PickList by the delete record's name
    Then the grid should show no results

  Scenario: Kiểm tra xoá 1 bản ghi - huỷ không xoá (PL_FUNC-38)
    Given PickList delete test data "KTNN_PickListDelete_004_ConfirmNo" is loaded
    When I set up a record to delete
    And I search PickList by the delete record's name
    And I click the row delete icon
    And I cancel the delete
    When I search PickList by the delete record's name
    Then the grid should show exactly 1 record with the delete record's name
    And I delete the record by exact search to clean up test data

  Scenario: Kiểm tra tích chọn nhiều bản ghi và xoá (PL_FUNC-40)
    Given PickList multi-delete test data "KTNN_PickListMultiDelete_001_SelectMultipleAndDelete" is loaded
    When I set up two records to delete
    And I search PickList using the multi-delete keyword
    And I capture the current results count as baseline
    And I select the checkboxes of both rows
    And I click the delete icon on the toolbar
    And I confirm the delete
    Then the toast message should contain "successfully"
    And the results count should have decreased by 2
    When I search PickList using the multi-delete keyword
    Then the grid should show no results

  Scenario: Kiểm tra tích chọn tất cả bằng checkbox header (PL_FUNC-41)
    Given PickList multi-delete test data "KTNN_PickListMultiDelete_002_SelectAllCheckbox" is loaded
    When I set up two records to delete
    And I search PickList using the multi-delete keyword
    And I select the header checkbox to select all
    Then all 2 visible rows should be selected
    When I click the delete icon on the toolbar
    And I confirm the delete
    When I search PickList using the multi-delete keyword
    Then the grid should show no results

  Scenario: Kiểm tra tích chọn nhiều bản ghi - huỷ không xoá (PL_FUNC-42)
    Given PickList multi-delete test data "KTNN_PickListMultiDelete_003_CancelDelete" is loaded
    When I set up two records to delete
    And I search PickList using the multi-delete keyword
    And I select the checkboxes of both rows
    And I click the delete icon on the toolbar
    And I cancel the delete
    Then the grid should show exactly 2 results
    When I click the delete icon on the toolbar
    And I confirm the delete
