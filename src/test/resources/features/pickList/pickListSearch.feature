Feature: PickList Search

  Scenario: Kiểm tra tìm kiếm từ khoá không tồn tại (PL_FUNC-3)
    Given PickList search test data "KTNN_PickListSearch_003_NotFound" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show no results

  Scenario: Kiểm tra tìm kiếm với khoảng trắng đầu/cuối từ khoá (PL_FUNC-4)
    Given PickList search test data "KTNN_PickListSearch_004_TrimWhitespace" is loaded
    When I search PickList using the loaded keyword
    Then the grid rows should contain the trimmed keyword

  Scenario: Kiểm tra tìm kiếm theo Code không tồn tại (PL_FUNC-7)
    Given PickList search test data "KTNN_PickListSearch_007_CodeNotFound" is loaded
    When I search PickList using the loaded keyword
    Then the grid should show no results

  Scenario: Kiểm tra kết hợp 2 điều kiện với Or (PL_FUNC-19)
    Given PickList two-condition filter test data "KTNN_PickListFilter_005_CombineWithOr" is loaded
    When I attempt to combine two filter conditions with Or
    Then the Or option should not be available
