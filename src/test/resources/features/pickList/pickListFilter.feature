Feature: PickList Filter

  Scenario: Kiểm tra lọc Description - điều kiện 'like' (PL_FUNC-15)
    Given PickList filter test data "KTNN_PickListFilter_001_DescriptionLike" is loaded
    When I filter PickList using the loaded field, operator and value
    Then the grid rows should contain the filtered description

  Scenario: Kiểm tra lọc Description - điều kiện '=' (PL_FUNC-16)
    Given PickList filter test data "KTNN_PickListFilter_002_DescriptionExact" is loaded
    When I filter PickList using the loaded field, operator and value
    Then the Description should exactly match the filtered value

  Scenario: Kiểm tra lọc Version - điều kiện '=' (PL_FUNC-17)
    Given PickList filter test data "KTNN_PickListFilter_003_VersionExact" is loaded
    When I filter PickList using the loaded field, operator and value
    Then the filtered results should be narrower than before

  Scenario: Kiểm tra kết hợp 2 điều kiện với And (PL_FUNC-18)
    Given PickList two-condition filter test data "KTNN_PickListFilter_004_CombineWithAnd" is loaded
    When I filter PickList using two conditions combined with And
    Then the grid rows should contain the first filter condition's value
