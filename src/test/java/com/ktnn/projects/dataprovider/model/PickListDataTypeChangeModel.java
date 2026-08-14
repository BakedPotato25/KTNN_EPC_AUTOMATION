package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListDataTypeChangeModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel newDataType;

    public PickListDataTypeChangeModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        newDataType = createDataModelObj("NewDataType");
    }
}
