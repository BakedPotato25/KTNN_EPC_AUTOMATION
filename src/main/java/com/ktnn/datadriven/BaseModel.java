package com.ktnn.datadriven;

import lombok.*;

/**
 * Base model cho data-driven.
 * Mọi model đều phải extends base model này
 */
@Getter
@Setter
@AllArgsConstructor
public class BaseModel {
    public DataModel testId;            // ID của test case
    public DataModel account;           // Account dùng để chạy test case
    public DataModel testDesc;          // Mô tả test case
    public DataModel testScenario;      // Kịch bản test case
    public DataModel preconditions;     // Tiền điều kiện của test case
    public DataModel executed;          // true: đã chạy; false: chưa chạy (dev mode)
    public DataModel category;          // Loại test case: REGRESSION, SMOKE, SANITY, ...

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
     * Tạo instance mới với dev name và language key
     *
     * @param name        : Dev name của element
     * @param languageKey : Language key của element
     */
    public DataModel createModelMapperObj(String name, String languageKey) {
        return DataModel.builder().devName(name).langProperty(languageKey).build();
    }

    /**
     * Tạo instance mới
     *
     * @param name        : Dev name của element
     * @param languageKey : Language key của element
     * @param fill        : Trạng thái fill : true - có fill dữ liệu; false - không fill
     */
    public DataModel createModelMapperObj(String name, boolean fill, String languageKey) {
        return DataModel.builder().devName(name).fill(fill).langProperty(languageKey).build();
    }

    /**
     * Tạo instance mới
     *
     * @param name : Dev name của element
     */
    public DataModel createDataModelObj(String name) {
        return DataModel.builder().devName(name).build();
    }
}
