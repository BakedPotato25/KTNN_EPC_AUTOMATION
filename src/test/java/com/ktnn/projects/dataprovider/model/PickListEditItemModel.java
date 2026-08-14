package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListEditItemModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel initialNameLabel;
    public DataModel initialCode;
    public DataModel initialValue;
    public DataModel newNameLabel;
    public DataModel newValue;

    public PickListEditItemModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        initialNameLabel = createDataModelObj("InitialNameLabel");
        initialCode = createDataModelObj("InitialCode");
        initialValue = createDataModelObj("InitialValue");
        newNameLabel = createDataModelObj("NewNameLabel");
        newValue = createDataModelObj("NewValue");
    }
}
