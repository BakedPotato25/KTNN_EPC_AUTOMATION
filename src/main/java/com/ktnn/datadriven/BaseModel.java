package com.ktnn.datadriven;

import lombok.*;

/**
 * Create a base model for data-driven.
 * All models have to extend this base model
 */
@Getter
@Setter
@AllArgsConstructor
public class BaseModel {
    public DataModel testId;            // ID of test case
    public DataModel account;           // Account to execute test case
    public DataModel testDesc;          // Description of test case
    public DataModel testScenario;      // Scenario of test case
    public DataModel preconditions;     // Preconditions of test case
    public DataModel executed;          // true: executed; false: not executed with dev mode
    public DataModel category;          // Category of test case: REGRESSION, SMOKE, SANITY, etc.

    public BaseModel() {
        testId = createDataModelObj("Id");
        account = createDataModelObj("AccountTest");
        testDesc = createDataModelObj("Description");
        testScenario = createDataModelObj("TestScenario");
        preconditions = createDataModelObj("Preconditions");
        executed = createDataModelObj("Executed");
        category = createDataModelObj("Category");
    }


    /**
     * Generate a new instance with dev name and language key
     *
     * @param name        : The dev name of element
     * @param languageKey : The language key of element
     */
    public DataModel createModelMapperObj(String name, String languageKey) {
        return DataModel.builder().devName(name).langProperty(languageKey).build();
    }

    /**
     * Generate a new instance
     *
     * @param name        : The dev name of element
     * @param languageKey : The language key of element
     * @param fill        : The fill status : true - fill information; false - don't fill
     */
    public DataModel createModelMapperObj(String name, boolean fill, String languageKey) {
        return DataModel.builder().devName(name).fill(fill).langProperty(languageKey).build();
    }

    /**
     * Generate a new instance
     *
     * @param name : The dev name of element
     */
    public DataModel createDataModelObj(String name) {
        return DataModel.builder().devName(name).build();
    }
}
