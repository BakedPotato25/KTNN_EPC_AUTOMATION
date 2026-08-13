package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListDeleteModel extends BaseModel {
    public DataModel name;
    public DataModel code;

    public PickListDeleteModel() {
        super();
        name = createDataModelObj("Name");
        code = createDataModelObj("Code");
    }
}
