package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListTwoConditionFilterModel extends BaseModel {
    public DataModel field1;
    public DataModel operator1;
    public DataModel value1;
    public DataModel field2;
    public DataModel operator2;
    public DataModel value2;

    public PickListTwoConditionFilterModel() {
        super();
        field1 = createDataModelObj("Field1");
        operator1 = createDataModelObj("Operator1");
        value1 = createDataModelObj("Value1");
        field2 = createDataModelObj("Field2");
        operator2 = createDataModelObj("Operator2");
        value2 = createDataModelObj("Value2");
    }
}
