package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListOverallSaveModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel newDescription;
    public DataModel itemNameLabel;
    public DataModel itemCode;
    public DataModel itemValue;

    public PickListOverallSaveModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        newDescription = createDataModelObj("NewDescription");
        itemNameLabel = createDataModelObj("ItemNameLabel");
        itemCode = createDataModelObj("ItemCode");
        itemValue = createDataModelObj("ItemValue");
    }
}
