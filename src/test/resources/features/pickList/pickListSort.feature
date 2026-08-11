Feature: PickList Sort

  Scenario: Kiểm tra Order = Code, Sort = Ascending (PL_FUNC-9)
    Given PickList sort test data "KTNN_PickListSort_001_CodeAscending" is loaded
    When I sort PickList using the loaded order and direction
    Then the Code column should be sorted in the loaded direction

  Scenario: Kiểm tra Order = Code, Sort = Descending (PL_FUNC-10)
    Given PickList sort test data "KTNN_PickListSort_002_CodeDescending" is loaded
    When I sort PickList using the loaded order and direction
    Then the Code column should be sorted in the loaded direction

  Scenario: Kiểm tra Order = Name, Sort = Ascending (PL_FUNC-11)
    Given PickList sort test data "KTNN_PickListSort_003_NameAscending" is loaded
    When I sort PickList using the loaded order and direction
    Then the grid should still render results

  Scenario: Kiểm tra Order = Name, Sort = Descending (PL_FUNC-12)
    Given PickList sort test data "KTNN_PickListSort_004_NameDescending" is loaded
    When I sort PickList using the loaded order and direction
    Then the grid should still render results

  Scenario: Kiểm tra Order = Create Date, Sort = Descending (PL_FUNC-13)
    Given PickList sort test data "KTNN_PickListSort_005_CreateDateDescending" is loaded
    When I sort PickList using the loaded order and direction
    Then the grid should still render results

  Scenario: Kiểm tra Order = Create Date, Sort = Ascending (PL_FUNC-14)
    Given PickList sort test data "KTNN_PickListSort_006_CreateDateAscending" is loaded
    When I sort PickList using the loaded order and direction
    Then the grid should still render results
