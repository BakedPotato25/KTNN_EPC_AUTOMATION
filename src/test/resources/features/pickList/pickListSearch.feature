Feature: PickList Search

  Scenario: Kiểm tra tìm kiếm tương đối theo Name (PL_FUNC-1)
    Given PickList search test data "KTNN_PickListSearch_001_Relative" is loaded
    When I search PickList using the loaded keyword
    Then the grid rows should contain the searched keyword

  Scenario: Kiểm tra tìm kiếm tuyệt đối theo Name (PL_FUNC-2)
    Given PickList search test data "KTNN_PickListSearch_002_Exact" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show exactly 1 record with the searched name

  Scenario: Kiểm tra tìm kiếm từ khoá không tồn tại (PL_FUNC-3)
    Given PickList search test data "KTNN_PickListSearch_003_NotFound" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show no results

  Scenario: Kiểm tra tìm kiếm với khoảng trắng đầu/cuối từ khoá (PL_FUNC-4)
    Given PickList search test data "KTNN_PickListSearch_004_TrimWhitespace" is loaded
    When I search PickList using the loaded keyword
    Then the grid rows should contain the trimmed keyword

  Scenario: Kiểm tra tìm kiếm tương đối theo Code (PL_FUNC-5)
    Given PickList search test data "KTNN_PickListSearch_005_CodeRelative" is loaded
    When I search PickList using the loaded keyword
    Then the grid rows should contain the searched keyword

  Scenario: Kiểm tra tìm kiếm tuyệt đối theo Code (PL_FUNC-6)
    Given PickList search test data "KTNN_PickListSearch_006_CodeExact" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show exactly 1 record with the searched code

  Scenario: Kiểm tra tìm kiếm theo Code không tồn tại (PL_FUNC-7)
    Given PickList search test data "KTNN_PickListSearch_007_CodeNotFound" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show no results

  Scenario: Kiểm tra từ khoá khớp đồng thời nhiều bản ghi qua các trường khác nhau (PL_FUNC-8)
    Given PickList multi-field search test data "KTNN_PickListSearch_008_MultiField" is loaded
    When I search PickList using the loaded multi-field keyword
    Then the grid should show both expected records

  Scenario: Kiểm tra kết hợp 2 điều kiện với Or (PL_FUNC-19)
    Given PickList two-condition filter test data "KTNN_PickListFilter_005_CombineWithOr" is loaded
    When I attempt to combine two filter conditions with Or
    Then the Or option should not be available

  Scenario: Kiểm tra refresh tải lại danh sách và reset bộ lọc (PL_FUNC-20)
    Given PickList search test data "KTNN_PickListRefresh_001_ResetSearch" is loaded
    When I capture the current results count as baseline
    And I search PickList using the loaded keyword
    And I click Refresh
    Then the search and filter should be reset
