package com.ktnn.projects.dataprovider.model;

import com.ktnn.datadriven.BaseModel;
import com.ktnn.datadriven.DataModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickListAddItemModel extends BaseModel {
    public DataModel pickListName;
    public DataModel pickListCode;
    public DataModel itemNameLabel;
    public DataModel itemCode;
    public DataModel itemValue;
    public DataModel itemValidForFrom;
    public DataModel itemValidForTo;

    public PickListAddItemModel() {
        super();
        pickListName = createDataModelObj("PickListName");
        pickListCode = createDataModelObj("PickListCode");
        itemNameLabel = createDataModelObj("ItemNameLabel");
        itemCode = createDataModelObj("ItemCode");
        itemValue = createDataModelObj("ItemValue");
        itemValidForFrom = createDataModelObj("ItemValidForFrom");
        itemValidForTo = createDataModelObj("ItemValidForTo");
    }
}
