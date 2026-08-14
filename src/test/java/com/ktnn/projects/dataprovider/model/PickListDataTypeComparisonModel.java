package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListDataTypeComparisonModel extends BaseModel {
    public DataModel stringPickListName;
    public DataModel stringPickListCode;
    public DataModel numberPickListName;
    public DataModel numberPickListCode;
    public DataModel objectPickListName;
    public DataModel objectPickListCode;

    public PickListDataTypeComparisonModel() {
        super();
        stringPickListName = createDataModelObj("StringPickListName");
        stringPickListCode = createDataModelObj("StringPickListCode");
        numberPickListName = createDataModelObj("NumberPickListName");
        numberPickListCode = createDataModelObj("NumberPickListCode");
        objectPickListName = createDataModelObj("ObjectPickListName");
        objectPickListCode = createDataModelObj("ObjectPickListCode");
    }
}
