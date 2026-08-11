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

  Scenario: Kiểm tra lưới cập nhật sau khi thêm mới thành công (PL_FUNC-22)
    Given PickList test data "KTNN_PickListAddNew_002_GridUpdates" is loaded
    When I capture the current results count as baseline
    And I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "successfully"
    And the results count should have increased by 1
    When I search PickList by the created record's name
    Then the grid should show exactly 1 record with that name
    And I delete the record to clean up test data

  Scenario: Kiểm tra nhập dữ liệu hợp lệ chỉ vào các trường bắt buộc (PL_FUNC-23)
    Given PickList test data "KTNN_PickListAddNew_003_RequiredFieldsOnly" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "successfully"
    And the Add new dialog should be closed
    When I search PickList by the created record's name
    Then the grid should show exactly 1 record with that name
    And I delete the record to clean up test data

  Scenario: Kiểm tra để trống trường Name (PL_FUNC-24)
    Given PickList test data "KTNN_PickListAddNew_004_EmptyName" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "Name is required"
    And the required-field error for "Name" should be shown
    When I close the Add new form

  Scenario: Kiểm tra để trống trường Code (PL_FUNC-25)
    Given PickList test data "KTNN_PickListAddNew_005_EmptyCode" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "Code is required"
    And the required-field error for "Code" should be shown
    When I close the Add new form

  Scenario: Kiểm tra nhập khoảng trắng vào trường bắt buộc (PL_FUNC-26)
    Given PickList test data "KTNN_PickListAddNew_006_WhitespaceName" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "Name is required"
    And the required-field error for "Name" should be shown
    When I close the Add new form

  Scenario: Kiểm tra nhập trùng Code đã tồn tại (PL_FUNC-27)
    Given PickList test data "KTNN_PickListAddNew_007_DuplicateCode" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I save the Add new form
    Then the toast message should contain "Code is unique"
    And the Add new dialog should stay open
    When I close the Add new form

  Scenario: Kiểm tra kích [Close] không lưu dữ liệu (PL_FUNC-28)
    Given PickList test data "KTNN_PickListAddNew_008_CloseDiscardsData" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I close the Add new form
    Then the Add new dialog should be closed
    When I search PickList by the created record's name
    Then the grid should show no results

  Scenario: Kiểm tra kích [X] không lưu dữ liệu (PL_FUNC-29)
    Given PickList test data "KTNN_PickListAddNew_009_XDiscardsData" is loaded
    When I open the Add new form
    And I fill the Add new form with the loaded data
    And I close the Add new form by clicking X
    Then the Add new dialog should be closed
    When I search PickList by the created record's name
    Then the grid should show no results
