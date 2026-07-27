package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListAddNewModel extends BaseModel {
    public DataModel name;
    public DataModel code;
    public DataModel version;
    public DataModel validForFrom;
    public DataModel validForTo;
    public DataModel description;

    public PickListAddNewModel() {
        super();
        name = createDataModelObj("Name");
        code = createDataModelObj("Code");
        version = createDataModelObj("Version");
        validForFrom = createDataModelObj("ValidForFrom");
        validForTo = createDataModelObj("ValidForTo");
        description = createDataModelObj("Description");
    }
}
