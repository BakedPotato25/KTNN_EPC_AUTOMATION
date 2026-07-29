package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListEditModel extends BaseModel {
    public DataModel initialName;
    public DataModel initialCode;
    public DataModel newName;
    public DataModel newDescription;
    public DataModel newVersion;
    public DataModel newIsActive;
    public DataModel attemptedCode;

    public PickListEditModel() {
        super();
        initialName = createDataModelObj("InitialName");
        initialCode = createDataModelObj("InitialCode");
        newName = createDataModelObj("NewName");
        newDescription = createDataModelObj("NewDescription");
        newVersion = createDataModelObj("NewVersion");
        newIsActive = createDataModelObj("NewIsActive");
        attemptedCode = createDataModelObj("AttemptedCode");
    }
}
