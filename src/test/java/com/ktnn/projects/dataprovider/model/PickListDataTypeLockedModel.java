package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListDataTypeLockedModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel itemNameLabel;
    public DataModel itemCode;
    public DataModel itemValue;

    public PickListDataTypeLockedModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        itemNameLabel = createDataModelObj("ItemNameLabel");
        itemCode = createDataModelObj("ItemCode");
        itemValue = createDataModelObj("ItemValue");
    }
}
